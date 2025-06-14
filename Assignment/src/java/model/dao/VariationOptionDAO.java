
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.VariationOptionDTO;
import utils.DbUtils;

/**
 * Status: đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết crud cho class này
 */
public class VariationOptionDAO {
    private static final String TABLE_NAME = "variation_option";

    private VariationOptionDTO mapToVariationOption(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int variationId = rs.getInt("variation_id");
        String value = rs.getString("value");

        return new VariationOptionDTO(id, variationId, value);
    }

    private List<VariationOptionDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;

        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<VariationOptionDTO> list = new ArrayList<>();

            while (rs.next()) {
                list.add(mapToVariationOption(rs));
            }

            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public List<VariationOptionDTO> getAllOptions() {
        return retrieve("1 = 1");
    }

    public VariationOptionDTO getOptionById(int id) {
        List<VariationOptionDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public List<VariationOptionDTO> getOptionsByVariationId(int variationId) {
        return retrieve("variation_id = ?", variationId);
    }

    public boolean create(VariationOptionDTO option) {
        String sql = "INSERT INTO " + TABLE_NAME + " (variation_id, value) VALUES (?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, option.getVariation_id());
            ps.setString(2, option.getValue());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(VariationOptionDTO option) {
        String sql = "UPDATE " + TABLE_NAME + " SET variation_id = ?, value = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, option.getVariation_id());
            ps.setString(2, option.getValue());
            ps.setInt(3, option.getId());

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
