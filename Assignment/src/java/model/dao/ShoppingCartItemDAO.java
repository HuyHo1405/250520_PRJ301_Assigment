package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.ShoppingCartItemDTO;
import utils.DbUtils;

public class ShoppingCartItemDAO {
    private static final String TABLE_NAME = "shopping_cart_item";

    private ShoppingCartItemDTO mapToItem(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int cartId = rs.getInt("cart_id");
        int itemId = rs.getInt("item_id");
        int quantity = rs.getInt("quantity");
        return new ShoppingCartItemDTO(id, cartId, itemId, quantity);
    }

    public List<ShoppingCartItemDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;

        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<ShoppingCartItemDTO> itemList = new ArrayList<>();

            while (rs.next()) {
                itemList.add(mapToItem(rs));
            }

            return itemList;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean create(ShoppingCartItemDTO item) {
        String sql = "INSERT INTO " + TABLE_NAME + " (cart_id, item_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getCart_id());
            ps.setInt(2, item.getItem_id());
            ps.setInt(3, item.getQuantity());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(ShoppingCartItemDTO item) {
        String sql = "UPDATE " + TABLE_NAME + " SET cart_id = ?, item_id = ?, quantity = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getCart_id());
            ps.setInt(2, item.getItem_id());
            ps.setInt(3, item.getQuantity());
            ps.setInt(4, item.getId());
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
    
    public boolean softDelete(int id){
        String sql = "UPDATE " + TABLE_NAME + " SET is_deleted = 1 WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean softDeleteCartItem(int cartId){
        String sql = "UPDATE " + TABLE_NAME + " SET is_deleted = 1 WHERE cart_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public List<ShoppingCartItemDTO> findByCartId(int cartId) {
        return retrieve("cart_id = ?", cartId);
    }

    public ShoppingCartItemDTO findByCartIdAndItemId(int cartId, int itemId) {
        List<ShoppingCartItemDTO> list = retrieve("cart_id = ? AND item_id = ?", cartId, itemId);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public boolean existsByCartIdAndItemId(int cartId, int itemId) {
        String sql = "SELECT 1 FROM " + TABLE_NAME + " WHERE cart_id = ? AND item_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, itemId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.err.println("Error in existsByCartIdAndItemId(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public double calculateTotal(int cartId){
        String sql = "SELECT SUM(sci.quantity * pi.price)\n"
                + "FROM shopping_cart_item sci\n"
                + "JOIN product_item pi ON pi.id = sci.item_id\n"
                + "WHERE pi.is_deleted = 0 and sci.cart_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
}
