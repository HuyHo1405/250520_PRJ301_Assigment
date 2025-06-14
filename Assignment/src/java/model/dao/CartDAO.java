
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.CartDTO;
import utils.DbUtils;

/**
 * Status: đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết crud cho class này
 */
public class CartDAO {

    private static final String TABLE_NAME = "category";

    private CartDTO mapToCart(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Integer parentCategoryId = rs.getObject("parent_category_id") != null ? rs.getInt("parent_category_id") : null;
        String name = rs.getString("name");

        return new CartDTO(id, parentCategoryId, name);
    }

    private List<CartDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;

        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<CartDTO> cartList = new ArrayList<>();

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

    public List<CartDTO> getAllCarts() {
        return retrieve("1 = 1");
    }

    public CartDTO getCartById(int id) {
        List<CartDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public List<CartDTO> getCartsByParentId(Integer parentId) {
        if (parentId == null) {
            return retrieve("parent_category_id IS NULL");
        } else {
            return retrieve("parent_category_id = ?", parentId);
        }
    }

    public boolean create(CartDTO cart) {
        String sql = "INSERT INTO " + TABLE_NAME + " (parent_category_id, name) VALUES (?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            if (cart.getParent_category_id() == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, cart.getParent_category_id());
            }
            ps.setString(2, cart.getName());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(CartDTO cart) {
        String sql = "UPDATE " + TABLE_NAME + " SET parent_category_id = ?, name = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            if (cart.getParent_category_id() == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, cart.getParent_category_id());
            }
            ps.setString(2, cart.getName());
            ps.setInt(3, cart.getId());

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
