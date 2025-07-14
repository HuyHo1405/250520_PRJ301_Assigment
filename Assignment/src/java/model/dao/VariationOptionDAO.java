
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

    public List<VariationOptionDTO> retrieve(String condition, Object... params) {
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
    
    public List<VariationOptionDTO> getOptionsByVariationId(int variationId) {
        return retrieve("variation_id = ?", variationId);
    }
    
    public List<VariationOptionDTO> getOptionsByProductId(int productId) {
        String sql = "SELECT DISTINCT\n"
                + "    VO.*\n"
                + "FROM\n"
                + "    product_item AS PI\n"
                + "JOIN\n"
                + "    product_configuration AS PC ON PI.id = PC.item_id\n"
                + "JOIN\n"
                + "    variation_option AS VO ON PC.option_id = VO.id\n"
                + "JOIN\n"
                + "    Variation AS V ON VO.variation_id = V.id\n"
                + "WHERE\n"
                + "    PI.product_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            List<VariationOptionDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapToVariationOption(rs));
            }
            return list;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean existsByVariationIdAndValue(int variationId, String value) {
        String sql = "SELECT 1 FROM " + TABLE_NAME + " WHERE variation_id = ? AND value = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, variationId);
            ps.setString(2, value);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.err.println("Error in existsByVariationIdAndValue(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteByVariationId(int variationId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE variation_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, variationId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in deleteByVariationId(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
