package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.dto.OrderExportDTO;
import utils.DbUtils;

public class OrderExportDAO {

    public List<OrderExportDTO> getAllForExport() {
        List<OrderExportDTO> list = new ArrayList<>();

        String sql = 
            "SELECT o.id, o.order_code, os.status_name, pm.name AS payment_method, pt.name AS payment_type, " +
            "       sm.name AS shipping_method, o.order_total, u.name AS user_name, " +
            "       CONCAT_WS(', ', a.unit_number, a.street_number, a.address_line1, a.address_line2, a.city, a.region) AS full_address, " +
            "       o.order_date, o.created_at " +
            "FROM shopping_order o " +
            "JOIN order_status os ON o.order_status_id = os.id " +
            "JOIN payment_method pm ON o.payment_method_id = pm.id " +
            "JOIN payment_type pt ON pm.payment_type_id = pt.id " +
            "JOIN shipping_method sm ON o.shipping_method_id = sm.id " +
            "JOIN address a ON o.address_id = a.id " +
            "JOIN user u ON o.user_id = u.id " +
            "ORDER BY o.id DESC";

        try (Connection conn = DbUtils.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new OrderExportDTO(
                    rs.getInt("id"),
                    rs.getString("order_code"),
                    rs.getString("status_name"),
                    rs.getString("payment_method"),
                    rs.getString("payment_type"),
                    rs.getString("shipping_method"),
                    rs.getDouble("order_total"),
                    rs.getString("user_name"),
                    rs.getString("full_address"),
                    rs.getTimestamp("order_date"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
