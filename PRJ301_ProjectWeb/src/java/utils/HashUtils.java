package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import model.dao.UserDAO;
import model.dto.UserDTO;

/**
 * Status: đã hoàn thành Người thực hiện: Huy Ngày bắt đầu: 13/06/2025 viết hash
 * utils để bảo mật user
 */
public class HashUtils {
    // Hàm hash password

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    // Hàm chuyển byte[] thành String hex
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        UserDAO udao = new UserDAO();
        List<UserDTO> list = udao.getAllUsers();
        for (UserDTO user : list) {
            udao.updatePassword(user.getId(), hashPassword(user.getHashed_password()));
        }
    }
}
