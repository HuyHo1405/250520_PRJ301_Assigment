package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.dto.ShoppingCartDTO;
import utils.DbUtils;

public class ShoppingCartDAO {
    private static final String TABLE_NAME = "shopping_cart";

    private ShoppingCartDTO mapToCart(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        return new ShoppingCartDTO(id, userId);
    }

    public List<ShoppingCartDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE is_deleted = 0 AND " + condition;
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<ShoppingCartDTO> carts = new ArrayList<>();

            while (rs.next()) {
                carts.add(mapToCart(rs));
            }

            return carts;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private int createByUserId(int userId) {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id) VALUES (?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); ) {
            ps.setInt(1, userId);
            if(ps.executeUpdate() > 0){
                try ( ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    public int createOrGetUserCartId(int userId){
        int cartId = findCartIdByUserId(userId);
        return cartId == -1? createByUserId(userId): cartId;
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
    
    public ShoppingCartDTO findById(int id) {
        List<ShoppingCartDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public int findCartIdByUserId(int userId) {
        List<ShoppingCartDTO> list = retrieve("user_id = ? and is_deleted = 0", userId);
        return list != null && !list.isEmpty() ? list.get(0).getId() : -1;
    }

    public boolean existsByUserId(int userId) {
        String sql = "SELECT 1 FROM " + TABLE_NAME + " WHERE user_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.err.println("Error in existsByUserId(): " + e.getMessage());
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
