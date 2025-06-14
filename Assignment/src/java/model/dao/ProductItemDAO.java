
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        return new ProductItemDTO(id, productId, sku, quantityInStock, itemImageLink, price);
    }

    private List<ProductItemDTO> retrieve(String condition, Object... params) {
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

    public List<ProductItemDTO> getAllItems() {
        return retrieve("1 = 1");
    }

    public ProductItemDTO getItemById(int id) {
        List<ProductItemDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public List<ProductItemDTO> getItemsByProductId(int productId) {
        return retrieve("product_id = ?", productId);
    }

    public boolean create(ProductItemDTO item) {
        String sql = "INSERT INTO " + TABLE_NAME + " (product_id, sku, quantity_in_stock, item_image_link, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getProduct_id());
            ps.setString(2, item.getSku());
            ps.setInt(3, item.getQuantity_in_stock());
            ps.setString(4, item.getItem_image_link());
            ps.setDouble(5, item.getPrice());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(ProductItemDTO item) {
        String sql = "UPDATE " + TABLE_NAME + " SET product_id = ?, sku = ?, quantity_in_stock = ?, item_image_link = ?, price = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getProduct_id());
            ps.setString(2, item.getSku());
            ps.setInt(3, item.getQuantity_in_stock());
            ps.setString(4, item.getItem_image_link());
            ps.setDouble(5, item.getPrice());
            ps.setInt(6, item.getId());

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
