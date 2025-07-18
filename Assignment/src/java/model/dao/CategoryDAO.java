
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.CategoryDTO;
import utils.DbUtils;

/**
 * Status: đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết crud cho class này
 */
public class CategoryDAO {

    private static final String TABLE_NAME = "category";

    private CategoryDTO mapToCart(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int parentCategoryId = rs.getObject("parent_category_id") != null ? rs.getInt("parent_category_id") : -1;
        String name = rs.getString("name");
        boolean is_active = rs.getBoolean("is_active");
        return new CategoryDTO(id, parentCategoryId, name, is_active);
    }

    public List<CategoryDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;

        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<CategoryDTO> cartList = new ArrayList<>();

            while (rs.next()) {
                cartList.add(mapToCart(rs));
            }

            return cartList;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean create(CategoryDTO cart) {
        String sql = "INSERT INTO " + TABLE_NAME + " (parent_category_id, name, is_active) VALUES (?, ?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            if (cart.getParent_category_id() == -1) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, cart.getParent_category_id());
            }
            ps.setString(2, cart.getName());
            ps.setBoolean(3, cart.getIs_active());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(CategoryDTO cart) {
        String sql = "UPDATE " + TABLE_NAME + " SET parent_category_id = ?, name = ?, is_active = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            if (cart.getParent_category_id() == -1) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, cart.getParent_category_id());
            }
            ps.setString(2, cart.getName());
            ps.setBoolean(3, cart.getIs_active());
            ps.setInt(4, cart.getId());

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

    public CategoryDTO findById(int id) {
        List<CategoryDTO> list = retrieve("id = ?", id);
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

    public int countByParentId(int parentId) {
        String sql = "SELECT COUNT(*) AS total FROM " + TABLE_NAME + " WHERE parent_category_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, parentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            System.err.println("Error in countByParentId(): " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public boolean toggleIsActive(int id, boolean currStatus) {
        String sql = "UPDATE " + TABLE_NAME + " SET is_active = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, !currStatus);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in toggle(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
