
package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.dto.ShoppingOrderDTO;
import utils.DbUtils;

/**
 * Status: đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết crud cho class này
 */
public class ShoppingOrderDAO {
    private static final String TABLE_NAME = "shopping_order";

    private ShoppingOrderDTO mapToShoppingOrder(ResultSet rs) throws SQLException {
    return new ShoppingOrderDTO(
        rs.getInt("id"),
        new Date(rs.getTimestamp("order_date").getTime()),  // explicit conversion
        rs.getDouble("order_total"),
        rs.getInt("order_status_id"),
        rs.getInt("payment_method_id"),
        rs.getInt("shipping_method_id"),
        rs.getInt("address_id"),
        rs.getInt("user_id"),
        new Date(rs.getTimestamp("created_at").getTime()),
        new Date(rs.getTimestamp("updated_at").getTime())
    );
}


    private List<ShoppingOrderDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            List<ShoppingOrderDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapToShoppingOrder(rs));
            }
            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<ShoppingOrderDTO> getAllOrders() {
        return retrieve("1=1");
    }

    public ShoppingOrderDTO getOrderById(int id) {
        List<ShoppingOrderDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public List<ShoppingOrderDTO> getOrdersByUserId(int userId) {
        return retrieve("user_id = ?", userId);
    }

    public boolean create(ShoppingOrderDTO order) {
        String sql = "INSERT INTO " + TABLE_NAME +
            " (order_date, order_total, order_status_id, payment_method_id, shipping_method_id, address_id, user_id, created_at, updated_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, new Timestamp(order.getOrderDate().getTime()));
            ps.setDouble(2, order.getOrderTotal());
            ps.setInt(3, order.getOrderStatusId());
            ps.setInt(4, order.getPaymentMethodId());
            ps.setInt(5, order.getShippingMethodId());
            ps.setInt(6, order.getAddressId());
            ps.setInt(7, order.getUserId());
            ps.setTimestamp(8, new Timestamp(order.getCreatedAt().getTime()));
            ps.setTimestamp(9, new Timestamp(order.getUpdatedAt().getTime()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setId(generatedKeys.getInt(1));
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(ShoppingOrderDTO order) {
        String sql = "UPDATE " + TABLE_NAME +
            " SET order_date = ?, order_total = ?, order_status_id = ?, payment_method_id = ?, shipping_method_id = ?, " +
            "address_id = ?, user_id = ?, created_at = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(order.getOrderDate().getTime()));
            ps.setDouble(2, order.getOrderTotal());
            ps.setInt(3, order.getOrderStatusId());
            ps.setInt(4, order.getPaymentMethodId());
            ps.setInt(5, order.getShippingMethodId());
            ps.setInt(6, order.getAddressId());
            ps.setInt(7, order.getUserId());
            ps.setTimestamp(8, new Timestamp(order.getCreatedAt().getTime()));
            ps.setTimestamp(9, new Timestamp(order.getUpdatedAt().getTime()));
            ps.setInt(10, order.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in update(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
