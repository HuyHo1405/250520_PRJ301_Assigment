
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.VariationDTO;
import utils.DbUtils;

/**
 * Status: đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết crud cho class này
 */
public class VariationDAO {
    private static final String TABLE_NAME = "variation";

    private VariationDTO mapToVariation(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int productId = rs.getInt("product_id");
        String name = rs.getString("name");

        return new VariationDTO(id, productId, name);
    }

    private List<VariationDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;

        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<VariationDTO> variationList = new ArrayList<>();

            while (rs.next()) {
                variationList.add(mapToVariation(rs));
            }

            return variationList;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public List<VariationDTO> getAllVariations() {
        return retrieve("1 = 1");
    }

    public VariationDTO getVariationById(int id) {
        List<VariationDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public List<VariationDTO> getVariationsByProductId(int productId) {
        return retrieve("product_id = ?", productId);
    }

    public boolean create(VariationDTO variation) {
        String sql = "INSERT INTO " + TABLE_NAME + " (product_id, name) VALUES (?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, variation.getProduct_id());
            ps.setString(2, variation.getName());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(VariationDTO variation) {
        String sql = "UPDATE " + TABLE_NAME + " SET product_id = ?, name = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, variation.getProduct_id());
            ps.setString(2, variation.getName());
            ps.setInt(3, variation.getId());

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
