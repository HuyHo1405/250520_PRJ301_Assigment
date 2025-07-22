/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Authenticator;
import java.sql.Date;
import java.util.Properties;

/**
 *
 * @author Admin
 */
public class MailUtils {

    private static final String FROM_EMAIL = "leducthinh.110805@gmail.com"; // Email gửi
    private static final String PASSWORD = "bhdlmzvhtupxmjii";              // App password Gmail

    public static void sendResetPasswordEmail(String toEmail, String resetLink) throws MessagingException {
        // Cấu hình SMTP cho Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Tạo session xác thực
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        // Tạo nội dung email
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Reset your password");

        // HTML nội dung
        String htmlContent = "<h3>Password Reset Request</h3>"
        + "<p>You have requested to reset your password. Click the link below to proceed:</p>"
        + "<p><a href=\"" + resetLink + "\">👉 Reset Password</a></p>"
        + "<p><i>This link will expire in 10 minutes for security reasons.</i></p>";

        message.setContent(htmlContent, "text/html; charset=UTF-8");

        // Gửi email
        Transport.send(message);
        System.out.println("📩 Đã gửi liên kết đặt lại mật khẩu tới: " + toEmail);
    }
    
    public static void main(String[] args) {
        try {
            MailUtils.sendResetPasswordEmail("madaz.393875@gmail.com", "https://www.youtube.com/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setContent(htmlContent, "text/html; charset=UTF-8");

        Transport.send(message);
        System.out.println("📩 Email sent to: " + toEmail);
    }

}
