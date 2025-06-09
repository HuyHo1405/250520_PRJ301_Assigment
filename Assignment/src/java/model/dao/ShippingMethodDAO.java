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
import model.dto.ShippingMethodDTO;
import utils.DbUtils;

/**
 *
 * @author Admin
 */
public class ShippingMethodDAO {
    private static final String TABLE_NAME = "shipping_method";

    private ShippingMethodDTO mapToShippingMethod(ResultSet rs) throws SQLException {
        return new ShippingMethodDTO(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getDouble("price")
        );
    }

    private List<ShippingMethodDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;

        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<ShippingMethodDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapToShippingMethod(rs));
            }

            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public List<ShippingMethodDTO> getAll() {
        return retrieve("1=1");
    }

    public ShippingMethodDTO getById(int id) {
        List<ShippingMethodDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public boolean create(ShippingMethodDTO method) {
        String sql = "INSERT INTO " + TABLE_NAME + " (name, price) VALUES (?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, method.getName());
            ps.setDouble(2, method.getPrice());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(ShippingMethodDTO method) {
        String sql = "UPDATE " + TABLE_NAME + " SET name = ?, price = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, method.getName());
            ps.setDouble(2, method.getPrice());
            ps.setInt(3, method.getId());

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
