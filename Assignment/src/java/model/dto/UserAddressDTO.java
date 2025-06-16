
package model.dto;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết set up các field cho class này
 */
public class UserAddressDTO {
    private int user_id;
    private int address_id;
    private boolean is_default;

    public UserAddressDTO() {
    }

    public UserAddressDTO(int user_id, int address_id) {
        this.user_id = user_id;
        this.address_id = address_id;
        this.is_default = false;
    }
    
    public UserAddressDTO(int user_id, int address_id, boolean is_default) {
        this.user_id = user_id;
        this.address_id = address_id;
        this.is_default = is_default;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public boolean isIs_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }
}
