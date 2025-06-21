
package model.dto;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết set up các field cho class này
 */
public class PaymentTypeDTO {
    private int id;
    private String value;

    public PaymentTypeDTO() {
    }

    public PaymentTypeDTO(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PaymentTypeDTO{" + "id=" + id + ", value=" + value + '}';
    }
}
