
package model.dto;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết set up các field cho class này
 */
public class ProductDTO {
    private int id;
    private int category_id;
    private String name;
    private String description;
    private String cover_image_link;
    private boolean is_active;

    public ProductDTO() {
    }
    public ProductDTO(int category_id, String name, String description, String cover_image_link) {
        this.id = -1;
        this.category_id = category_id;
        this.name = name;
        this.description = description;
        this.cover_image_link = cover_image_link;
        this.is_active = true;
    }

    public ProductDTO(int id, int category_id, String name, String description, String cover_image_link, boolean is_active) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.description = description;
        this.cover_image_link = cover_image_link;
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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover_image_link() {
        return cover_image_link;
    }

    public void setCover_image_link(String cover_image_link) {
        this.cover_image_link = cover_image_link;
    }

    @Override
    public String toString() {
        return "ProductDTO{" + "id=" + id + ", category_id=" + category_id + ", name=" + name + ", description=" + description + ", cover_image_link=" + cover_image_link + '}';
    }
}
