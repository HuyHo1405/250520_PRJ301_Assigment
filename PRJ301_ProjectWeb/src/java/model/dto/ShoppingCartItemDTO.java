
package model.dto;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết set up các field cho class này
 */
public class ShoppingCartItemDTO {
    private int id;
    private int cart_id;
    private int item_id;
    private int quantity;

    public ShoppingCartItemDTO() {
    }

    public ShoppingCartItemDTO(int id, int cart_id, int item_id, int quantity) {
        this.id = id;
        this.cart_id = cart_id;
        this.item_id = item_id;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ShoppingCartItemDTO{" + "id=" + id + ", cart_id=" + cart_id + ", item_id=" + item_id + ", quantity=" + quantity + '}';
    }
}
