
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
 * Status: đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết crud cho class này
 */
public class ProductConfigDAO {
    private static final String TABLE_NAME = "product_configuration";

    private ProductConfigDTO mapToProductConfiguration(ResultSet rs) throws SQLException {
        int itemId = rs.getInt("item_id");
        int optionId = rs.getInt("option_id");
        return new ProductConfigDTO(itemId, optionId);
    }

    public List<ProductConfigDTO> retrieve(String condition, Object... params) {
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
    
    public List<ProductConfigDTO> findByItemId(int itemId) {
        return retrieve("item_id = ?", itemId);
    }

    public List<ProductConfigDTO> findByOptionId(int optionId) {
        return retrieve("option_id = ?", optionId);
    }

    public boolean deleteByProductId(int productId){
        String sql = "DELETE pc\n"
                + "FROM product_configuration pc\n"
                + "JOIN product_item pi ON pc.item_id = pi.id\n"
                + "WHERE pi.product_id = ?;";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean existsById(int itemId, int optionId) {
        String sql = "SELECT 1 FROM " + TABLE_NAME + " WHERE item_id = ? AND option_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setInt(2, optionId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.err.println("Error in exists(): " + e.getMessage());
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
}
