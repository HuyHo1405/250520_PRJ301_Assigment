
package model.dto;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết set up các field cho class này
 */
public class CategoryDTO {
    private int id;
    private int parent_category_id;
    private String name;
    private boolean is_active;

    public CategoryDTO() {
    }

    public CategoryDTO(int parent_category_id, String name, boolean is_active) {
        this.id = -1;
        this.parent_category_id = parent_category_id;
        this.name = name;
        this.is_active = is_active;
    }
    
    public CategoryDTO(int id, int parent_category_id, String name, boolean is_active) {
        this.id = id;
        this.parent_category_id = parent_category_id;
        this.name = name;
        this.is_active = is_active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_category_id() {
        return parent_category_id;
    }

    public void setParent_category_id(int parent_category_id) {
        this.parent_category_id = parent_category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" + "id=" + id + ", parent_category_id=" + parent_category_id + ", name=" + name + ", is_active=" + is_active + '}';
    }

}
