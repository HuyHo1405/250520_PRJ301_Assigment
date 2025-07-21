package model.dto;

public class ProductConfigDTO {
    private int item_id;
    private int option_id;

    public ProductConfigDTO() {
    }

    public ProductConfigDTO(int item_id, int option_id) {
        this.item_id = item_id;
        this.option_id = option_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }

    @Override
    public String toString() {
        return "ProductConfigDTO{" + "item_id=" + item_id + ", option_id=" + option_id + '}';
    }
    
}
