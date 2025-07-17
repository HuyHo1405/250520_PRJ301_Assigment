
package model.dto;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết set up các field cho class này
 */
public class ProductItemDTO {
    private int id;
    private int product_id;
    private String sku;
    private int quantity_in_stock;
    private String item_image_link;
    private double price;

    public ProductItemDTO() {
    }

    public ProductItemDTO(int id, int product_id, String sku, int quantity_in_stock, String item_image_link, double price) {
        this.id = id;
        this.product_id = product_id;
        this.sku = sku;
        this.quantity_in_stock = quantity_in_stock;
        this.item_image_link = item_image_link;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQuantity_in_stock() {
        return quantity_in_stock;
    }

    public void setQuantity_in_stock(int quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
    }

    public String getItem_image_link() {
        return item_image_link;
    }

    public void setItem_image_link(String item_image_link) {
        this.item_image_link = item_image_link;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductItemDTO{" + "id=" + id + ", product_id=" + product_id + ", sku=" + sku + ", quantity_in_stock=" + quantity_in_stock + ", item_image_link=" + item_image_link + ", price=" + price + '}';
    }
    
}
