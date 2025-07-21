
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.dto.OrderLineDTO;
import utils.DbUtils;

public class OrderLineDAO {
    private static final String TABLE_NAME = "order_line";

    private OrderLineDTO mapToOrderLine(ResultSet rs) throws SQLException {
        return new OrderLineDTO(
            rs.getInt("id"),
            rs.getInt("order_id"),
            rs.getInt("item_id"),
            rs.getInt("quantity"),
            rs.getDouble("price")
        );
    }

    public List<OrderLineDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            List<OrderLineDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapToOrderLine(rs));
            }
            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public boolean create(OrderLineDTO orderLine) {
        String sql = "INSERT INTO " + TABLE_NAME + " (order_id, item_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, orderLine.getOrder_id());
            ps.setInt(2, orderLine.getItem_id());
            ps.setInt(3, orderLine.getQuantity());
            ps.setDouble(4, orderLine.getPrice());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderLine.setId(generatedKeys.getInt(1));
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(OrderLineDTO orderLine) {
        String sql = "UPDATE " + TABLE_NAME + " SET order_id = ?, item_id = ?, quantity = ?, price = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderLine.getOrder_id());
            ps.setInt(2, orderLine.getItem_id());
            ps.setInt(3, orderLine.getQuantity());
            ps.setDouble(4, orderLine.getPrice());
            ps.setInt(5, orderLine.getId());

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
    
    public OrderLineDTO findById(int id) {
        List<OrderLineDTO> list = retrieve("id = ?", id);
        return (list == null || list.isEmpty()) ? null : list.get(0);
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

    public int count() {
        String sql = "SELECT COUNT(*) AS total FROM " + TABLE_NAME;
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            System.err.println("Error in count(): " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int countByOrderId(int orderId) {
        String sql = "SELECT COUNT(*) AS total FROM " + TABLE_NAME + " WHERE order_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            System.err.println("Error in countByOrderId(): " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public List<OrderLineDTO> findByOrderId(int orderId) {
        return retrieve("order_id = ?", orderId);
    }
}
