package model.dto;

public class OrderLineDTO {
    private int id;
    private int order_id;
    private int item_id;
    private int quantity;
    private double price;

    public OrderLineDTO() {
    }

    public OrderLineDTO(int order_id, int item_id, int quantity, double price) {
        this.order_id = order_id;
        this.item_id = item_id;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderLineDTO(int id, int order_id, int item_id, int quantity, double price) {
        this.id = id;
        this.order_id = order_id;
        this.item_id = item_id;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderLineDTO{" + "id=" + id + ", order_id=" + order_id + ", item_id=" + item_id + ", quantity=" + quantity + ", price=" + price + '}';
    }
    
    
}
