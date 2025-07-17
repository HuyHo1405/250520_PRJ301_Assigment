
package model.dto;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết set up các field cho class này
 */
public class OrderStatusDTO {
    private int id;
    private String status;
    private boolean is_active = true;

    public OrderStatusDTO(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public OrderStatusDTO() {
    }

    public OrderStatusDTO(String status) {
        this.id = -1;
        this.status = status;
        this.is_active = true;
    }
    
    public OrderStatusDTO(int id, String status, boolean is_active) {
        this.id = id;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderStatusDTO{" + "id=" + id + ", status=" + status + '}';
    }
    
}
