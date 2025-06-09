/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.PaymentMethodDTO;
import utils.DbUtils;

/**
 *
 * @author Admin
 */
public class PaymentMethodDAO {
    private static final String TABLE_NAME = "payment_method";

    private PaymentMethodDTO mapToPaymentMethod(ResultSet rs) throws SQLException {
        return new PaymentMethodDTO(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getInt("payment_type_id"),
            rs.getString("provider"),
            rs.getString("account_number"),
            rs.getDate("expiry_date"),
            rs.getBoolean("is_default")
        );
    }

    private List<PaymentMethodDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;

        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<PaymentMethodDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapToPaymentMethod(rs));
            }

            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public List<PaymentMethodDTO> getAllByUserId(int userId) {
        return retrieve("user_id = ?", userId);
    }

    public PaymentMethodDTO getById(int id) {
        List<PaymentMethodDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public boolean create(PaymentMethodDTO method) {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, payment_type_id, provider, account_number, expiry_date, is_default) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, method.getUser_id());
            ps.setInt(2, method.getPayment_type_id());
            ps.setString(3, method.getProvider());
            ps.setString(4, method.getAccount_number());
            ps.setDate(5, method.getExpiry_date());
            ps.setBoolean(6, method.isIs_default());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(PaymentMethodDTO method) {
        String sql = "UPDATE " + TABLE_NAME + " SET user_id = ?, payment_type_id = ?, provider = ?, account_number = ?, expiry_date = ?, is_default = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, method.getUser_id());
            ps.setInt(2, method.getPayment_type_id());
            ps.setString(3, method.getProvider());
            ps.setString(4, method.getAccount_number());
            ps.setDate(5, method.getExpiry_date());
            ps.setBoolean(6, method.isIs_default());
            ps.setInt(7, method.getId());

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
