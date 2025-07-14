
package model.dto;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết set up các field cho class này
 */
public class CategoryDTO {
    private int id;
    private Integer parent_category_id;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(int id, Integer parent_category_id, String name) {
        this.id = id;
        this.parent_category_id = parent_category_id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getParent_category_id() {
        return parent_category_id;
    }

    public void setParent_category_id(Integer parent_category_id) {
        this.parent_category_id = parent_category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CartDTO{" + "id=" + id + ", parent_category_id=" + parent_category_id + ", name=" + name + '}';
    }
    
    
}
