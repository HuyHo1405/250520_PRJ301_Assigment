/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import model.dto.ResetTokenDTO;

/**
 *
 * @author Admin
 */
public class ResetTokenManager {
    private static final Map<String, ResetTokenDTO> tokenStore = new HashMap<>();
    private static final long EXPIRY_DURATION = 10 * 60 * 1000; // 10 phút

    // Tạo token và lưu lại
    public static String generateToken(String email) {
        String token = UUID.randomUUID().toString();
        long expiry = System.currentTimeMillis() + EXPIRY_DURATION;
        tokenStore.put(token, new ResetTokenDTO(email, expiry));
        return token;
    }

    // Trả về email nếu token còn hạn, null nếu hết hạn hoặc không tồn tại
    public static String getEmailIfValid(String token) {
        ResetTokenDTO dto = tokenStore.get(token);
        if (dto != null && !dto.isExpired()) {
            return dto.getEmail();
        }
        return null;
    }

    // Xoá token sau khi dùng
    public static void invalidate(String token) {
        tokenStore.remove(token);
    }
}
