
package model.dto;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết set up các field cho class này
 */
public class ShippingMethodDTO {
    private int id;
    private String name;
    private double price;
    private boolean is_active = true;

    public ShippingMethodDTO() {
    }

    public ShippingMethodDTO(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ShippingMethodDTO(String name, double price) {
        this.id = -1;
        this.name = name;
        this.price = price;
        this.is_active = true;
    }

    
    
    public ShippingMethodDTO(int id, String name, double price, boolean is_active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.is_active = is_active;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ShippingMethodDTO{" + "id=" + id + ", name=" + name + ", price=" + price + '}';
    }
}
