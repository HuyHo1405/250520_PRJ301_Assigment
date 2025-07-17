package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.dto.ShoppingOrderDTO;
import utils.DbUtils;

/**
 * Status: đã hoàn thành 
 * Người thực hiện: Huy
 * Ngày bắt đầu: 19/06/2025
 * Thêm order code để nhanh tìm kiếm sản phẩm
 */
public class ShoppingOrderDAO {
    private static final String TABLE_NAME = "shopping_order";

    private ShoppingOrderDTO mapToShoppingOrder(ResultSet rs) throws SQLException {
        return new ShoppingOrderDTO(
            rs.getInt("id"),
            rs.getTimestamp("order_date"),
            rs.getDouble("order_total"),
            rs.getInt("order_status_id"),
            rs.getInt("payment_method_id"),
            rs.getInt("shipping_method_id"),
            rs.getInt("address_id"),
            rs.getInt("user_id"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at"),
            rs.getString("order_code")
        );
    }

    public List<ShoppingOrderDTO> retrieve(String condition, Object... params) {
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

    public boolean create(ShoppingOrderDTO order) {
        String sql = "INSERT INTO " + TABLE_NAME +
            " (order_date, order_total, order_status_id, payment_method_id, shipping_method_id, address_id, user_id, created_at, updated_at, order_code) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, order.getOrderDate());
            ps.setDouble(2, order.getOrderTotal());
            ps.setInt(3, order.getOrderStatusId());
            ps.setInt(4, order.getPaymentMethodId());
            ps.setInt(5, order.getShippingMethodId());
            ps.setInt(6, order.getAddressId());
            ps.setInt(7, order.getUserId());
            ps.setTimestamp(8, order.getCreatedAt());
            ps.setTimestamp(9, order.getUpdatedAt());
            ps.setString(10, order.getOrder_code());

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
            "address_id = ?, user_id = ?, created_at = ?, updated_at = ?, order_code = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, order.getOrderDate());
            ps.setDouble(2, order.getOrderTotal());
            ps.setInt(3, order.getOrderStatusId());
            ps.setInt(4, order.getPaymentMethodId());
            ps.setInt(5, order.getShippingMethodId());
            ps.setInt(6, order.getAddressId());
            ps.setInt(7, order.getUserId());
            ps.setTimestamp(8, order.getCreatedAt());
            ps.setTimestamp(9, order.getUpdatedAt());
            ps.setString(10, order.getOrder_code());
            ps.setInt(11, order.getId());

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
    
    public ShoppingOrderDTO findById(int id){
        List<ShoppingOrderDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }
    
    public ShoppingOrderDTO findByOrderCode(String orderCode) {
        List<ShoppingOrderDTO> list = retrieve("order_code = ?", orderCode);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public List<ShoppingOrderDTO> findByUserId(int userId) {
        return retrieve("user_id = ?", userId);
    }

    public List<ShoppingOrderDTO> findByStatus(int statusId) {
        return retrieve("order_status_id = ?", statusId);
    }

    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM " + TABLE_NAME + " WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.err.println("Error in existsById(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) AS total FROM " + TABLE_NAME;
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            System.err.println("Error in countAll(): " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    public static void main(String[] args) {
        ShoppingOrderDAO sodao = new ShoppingOrderDAO();
        List ls = sodao.retrieve("order_code LIKE ?", "%" + "ord-1" + "%");
        for (Object l : ls) {
            System.out.println(ls.indexOf(l));
        }
    }
}
