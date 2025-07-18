
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.dto.ProductItemDTO;
import utils.DbUtils;

/**
 * Status: đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết crud cho class này
 */
public class ProductItemDAO {
    private static final String TABLE_NAME = "product_item";

    private ProductItemDTO mapToProductItem(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int productId = rs.getInt("product_id");
        String sku = rs.getString("sku");
        int quantityInStock = rs.getInt("quantity_in_stock");
        String itemImageLink = rs.getString("item_image_link");
        double price = rs.getDouble("price");
        boolean isActive = rs.getBoolean("is_active");
        return new ProductItemDTO(id, productId, sku, quantityInStock, itemImageLink, price, isActive);
    }

    public List<ProductItemDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;

        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<ProductItemDTO> itemList = new ArrayList<>();

            while (rs.next()) {
                itemList.add(mapToProductItem(rs));
            }

            return itemList;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean create(ProductItemDTO item) {
        String sql = "INSERT INTO " + TABLE_NAME + " (product_id, sku) VALUES (?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getProduct_id());
            ps.setString(2, item.getSku());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public int createReturnId(ProductItemDTO item) {
        String sql = "INSERT INTO " + TABLE_NAME + " (product_id, sku, quantity_in_stock, price, is_active, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, item.getProduct_id());
            ps.setString(2, item.getSku());
            ps.setInt(3, 0);
            ps.setDouble(4, 0);
            ps.setBoolean(5, false);
            ps.setBoolean(6, false);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try ( ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // ID vừa được tạo
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in createReturnId(): " + e.getMessage());
            e.printStackTrace();
        }
        return -1; // lỗi hoặc không insert
    }

    public boolean update(ProductItemDTO item) {
        String sql = "UPDATE " + TABLE_NAME + " SET product_id = ?, sku = ?, quantity_in_stock = ?, item_image_link = ?, price = ?, is_active = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getProduct_id());
            ps.setString(2, item.getSku());
            ps.setInt(3, item.getQuantity_in_stock());
            ps.setString(4, item.getItem_image_link());
            ps.setDouble(5, item.getPrice());
            ps.setBoolean(6, item.getIs_active());
            ps.setInt(7, item.getId());

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
    
    public boolean deleteByProductId(int productId) {
        String sql = "UPDATE " + TABLE_NAME + " SET is_deleted = 1 WHERE product_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteByOptionId(int optionId){
        String sql = "UPDATE pi\n"
                + "SET pi.is_deleted = 1\n"
                + "FROM product_item pi\n"
                + "JOIN product_configuration pc ON pi.id = pc.item_id\n"
                + "WHERE pc.option_id = ?;";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, optionId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean toggleIsActive(int id, boolean curr){
        String sql = "UPDATE " + TABLE_NAME + " SET is_active = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, !curr);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in update(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public ProductItemDTO findById(int id) {
        List<ProductItemDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM " + TABLE_NAME + " WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
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

    public List<ProductItemDTO> getByProductId(int productId) {
        return retrieve("product_id = ?", productId);
    }
    
    public static void main(String[] args) {
        ProductItemDAO dao = new ProductItemDAO();
        
        dao.deleteByOptionId(1);
        for (ProductItemDTO p : dao.retrieve("product_id = 1 AND is_deleted = 0")) {
            System.out.println(p);
        }
    }
    
    
}
