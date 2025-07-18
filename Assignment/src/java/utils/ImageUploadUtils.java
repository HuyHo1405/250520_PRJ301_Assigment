package utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;

import jakarta.servlet.http.Part;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ImageUploadUtils {

    private static final String APPLICATION_NAME = "MyUploaderApp";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens"; // nơi lưu access token
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "client_secret.json"; // đổi tên file tải về nếu cần

    // ---- START: Mock Part Class ----
    public static class MockPart implements Part {

        private final String name;
        private final java.io.File file;
        private final String contentType;

        public MockPart(String name, java.io.File file, String contentType) {
            this.name = name;
            this.file = file;
            this.contentType = contentType;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getSubmittedFileName() {
            return file.getName();
        }

        @Override
        public long getSize() {
            return file.length();
        }

        @Override
        public void write(String fileName) {
        }

        @Override
        public void delete() {
        }

        @Override
        public String getHeader(String name) {
            return null;
        }

        @Override
        public Collection<String> getHeaders(String name) {
            return null;
        }

        @Override
        public Collection<String> getHeaderNames() {
            return null;
        }
    }
    // ---- END: Mock Part Class ----

    // ---- START: Drive Upload with OAuth ----
    private static Credential getCredentials() throws Exception {
        InputStream in = ImageUploadUtils.class.getClassLoader().getResourceAsStream("client_secret.json");
        if (in == null) {
            throw new FileNotFoundException("Không tìm thấy file client_secret.json trong classpath.");
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                SCOPES
        )
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8889).build();
        return new com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp(flow, receiver)
                .authorize("user");
    }

    private static Drive getDriveService() throws Exception {
        Credential credential = getCredentials();
        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential
        ).setApplicationName(APPLICATION_NAME).build();
    }

    public static String uploadFile(Part filePart) throws Exception {
        String fileName = filePart.getSubmittedFileName();
        String mimeType = filePart.getContentType();

        System.out.println("[LOG] Bắt đầu upload file: " + fileName);
        System.out.println("[LOG] MIME type: " + mimeType);

        java.io.File tempFile = java.io.File.createTempFile("upload-", "-" + fileName);
        System.out.println("[LOG] Tạo file tạm: " + tempFile.getAbsolutePath());

        try ( InputStream in = filePart.getInputStream();  OutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        }

        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList("1SMc0XodpGAFRVokhX1wIqQQ1jpEZGwVh")); // 👈 gán vào folder đích

        FileContent mediaContent = new FileContent(mimeType, tempFile);

        Drive driveService = getDriveService();
        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        System.out.println("[LOG] Upload thành công. File ID: " + uploadedFile.getId());

        Permission permission = new Permission().setType("anyone").setRole("reader");
        driveService.permissions().create(uploadedFile.getId(), permission).execute();

        System.out.println("[LOG] Đã set quyền public.");

        return "https://drive.google.com/thumbnail?id=" + uploadedFile.getId() + "&sz=s800";
    }

    // ---- MAIN TEST ----
    public static void main(String[] args) {
        try {
            java.io.File image = new java.io.File("C:\\Users\\ho huy\\Downloads\\5ed58cbf79e583bfbab7016077008e28.jpg");
            if (!image.exists()) {
                System.out.println("⚠️ File không tồn tại: " + image.getAbsolutePath());
                return;
            }

            MockPart part = new MockPart("imageFile", image, "image/jpeg");

            String url = uploadFile(part);
            System.out.println("✅ Upload thành công: " + url);
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi upload ảnh:");
            e.printStackTrace();
        }
    }
}
