package model.dto;

public class PaymentTypeDTO {
    private int id;
    private String value;
    private boolean is_active = true;

    public PaymentTypeDTO() {
    }

    public PaymentTypeDTO(String value) {
        this.id = -1;
        this.value = value;
        this.is_active = true;
    }
    
    public PaymentTypeDTO(String value, boolean is_active) {
        this.id = -1;
        this.value = value;
        this.is_active = is_active;
    }
    
    public PaymentTypeDTO(int id, String value, boolean is_active) {
        this.id = id;
        this.value = value;
        this.is_active = is_active;
    }

    public PaymentTypeDTO(int id, String value) {
        this.id = id;
        this.value = value;
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
