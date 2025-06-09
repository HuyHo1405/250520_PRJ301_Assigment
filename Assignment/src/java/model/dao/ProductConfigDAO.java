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
import model.dto.ProductConfigDTO;
import utils.DbUtils;

/**
 *
 * @author Admin
 */
public class ProductConfigDAO {
    private static final String TABLE_NAME = "product_configuration";

    private ProductConfigDTO mapToProductConfiguration(ResultSet rs) throws SQLException {
        int itemId = rs.getInt("item_id");
        int optionId = rs.getInt("option_id");
        return new ProductConfigDTO(itemId, optionId);
    }

    private List<ProductConfigDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;

        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<ProductConfigDTO> list = new ArrayList<>();

            while (rs.next()) {
                list.add(mapToProductConfiguration(rs));
            }

            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public List<ProductConfigDTO> getAllConfigurations() {
        return retrieve("1 = 1");
    }

    public List<ProductConfigDTO> getConfigurationsByItemId(int itemId) {
        return retrieve("item_id = ?", itemId);
    }

    public List<ProductConfigDTO> getConfigurationsByOptionId(int optionId) {
        return retrieve("option_id = ?", optionId);
    }

    public boolean create(ProductConfigDTO config) {
        String sql = "INSERT INTO " + TABLE_NAME + " (item_id, option_id) VALUES (?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, config.getItem_id());
            ps.setInt(2, config.getOption_id());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int itemId, int optionId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE item_id = ? AND option_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setInt(2, optionId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
