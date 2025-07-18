package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.PaymentTypeDTO;
import utils.DbUtils;

/**
 * Status: đã hoàn thành Người thực hiện: Thịnh Ngày bắt đầu: 09/06/2025 viết
 * crud cho class này
 */
public class PaymentTypeDAO {

    private static final String TABLE_NAME = "payment_type";

    public PaymentTypeDTO mapToPaymentType(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String value = rs.getString("value");
        boolean is_active = rs.getBoolean("is_active");
        return new PaymentTypeDTO(id, value, is_active);
    }

    public List<PaymentTypeDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;

        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<PaymentTypeDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapToPaymentType(rs));
            }

            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public List<PaymentTypeDTO> getAllPaymentTypes() {
        return retrieve("1 = 1");
    }

    public PaymentTypeDTO getPaymentTypeById(int id) {
        List<PaymentTypeDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public PaymentTypeDTO getPaymentTypeByValue(String value) {
        List<PaymentTypeDTO> list = retrieve("value = ?", value);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public boolean create(PaymentTypeDTO type) {
        String sql = "INSERT INTO " + TABLE_NAME + " (value) VALUES (?)";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type.getValue());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(PaymentTypeDTO type) {
        String sql = "UPDATE " + TABLE_NAME + " SET value = ?, is_active = ? WHERE id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type.getValue());
            ps.setBoolean(2, type.getIs_active());
            ps.setInt(3, type.getId());
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

    public boolean toggleIsActive(int id, boolean currStatus) {
        String sql = "UPDATE " + TABLE_NAME + " SET is_active = ? WHERE id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, !currStatus);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in disablePaymentType(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public PaymentTypeDTO findById(int id) {
        List<PaymentTypeDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

}
