package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.dto.OrderStatusDTO;

public class OrderUtils {

    private static final String PREFIX = "ORD-";

    public static Map<Integer, String> getMap(List<OrderStatusDTO> list){
        Map<Integer, String> map = new HashMap<>();
        for (OrderStatusDTO category : list) {
            map.put(category.getId(), category.getStatus());
        }
        return map;
    }
    
    public static String generateOrderCode(int id, int status, Timestamp createdAt) {
        LocalDateTime createdTime = createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        String timePart = createdTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String raw = id + "-" + status + "-" + timePart;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(raw.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }

            return PREFIX + id + "-" + hex.substring(0, 8).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Error generating order code", e);
        }
    }

}
