package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.OrderStatusDTO;
import utils.DbUtils;

/**
 * Status: đã hoàn thành Người thực hiện: Thịnh Ngày bắt đầu: 09/06/2025 viết
 * crud cho class này
 */
public class OrderStatusDAO {

    private static final String TABLE_NAME = "order_status";

    public OrderStatusDTO mapToOrderStatus(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String status = rs.getString("status");
        boolean is_active = rs.getBoolean("is_active");
        return new OrderStatusDTO(id, status, is_active);
    }

    public List<OrderStatusDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;

        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<OrderStatusDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapToOrderStatus(rs));
            }

            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean create(OrderStatusDTO status) {
        String sql = "INSERT INTO " + TABLE_NAME + " (status) VALUES (?)";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(OrderStatusDTO status) {
        String sql = "UPDATE " + TABLE_NAME + " SET status = ? WHERE id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.getStatus());
            ps.setInt(2, status.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in update(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean disableOrderStatus(int id) {
        String sql = "UPDATE " + TABLE_NAME + " SET is_active = ? WHERE id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, false); // Set is_active to false
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in disableOrderStatus(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public OrderStatusDTO findById(int id) {
        List<OrderStatusDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

}
