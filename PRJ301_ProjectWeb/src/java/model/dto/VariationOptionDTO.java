
package model.dto;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết set up các field cho class này
 */
public class VariationOptionDTO {
    private int id;
    private int variation_id;
    private String value;

    public VariationOptionDTO() {
    }

    public VariationOptionDTO(int id, int variation_id, String value) {
        this.id = id;
        this.variation_id = variation_id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVariation_id() {
        return variation_id;
    }

    public void setVariation_id(int variation_id) {
        this.variation_id = variation_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "VariationOptionDTO{" + "id=" + id + ", variation_id=" + variation_id + ", value=" + value + '}';
    }
}
