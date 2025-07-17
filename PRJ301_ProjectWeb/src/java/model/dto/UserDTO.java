
package model.dto;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Huy
 * Ngày bắt đầu: 13/06/2025
 * thêm role cho user
 */
public class UserDTO {
    private int id;
    private String email_address;
    private String phone_number;
    private String hashed_password;
    private String role;
    private boolean is_active;

    public UserDTO() {
    }
    

    public UserDTO(String email_address, String phone_number, String hashed_password) {
        this.id = -1;
        this.email_address = email_address;
        this.phone_number = phone_number;
        this.hashed_password = hashed_password;
        this.role = "user";
        this.is_active = true;
    }

    public UserDTO(int id, String email_address, String phone_number, String hashed_password, String role, boolean is_active) {
        this.id = id;
        this.email_address = email_address;
        this.phone_number = phone_number;
        this.hashed_password = hashed_password;
        this.role = role;
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

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getHashed_password() {
        return hashed_password;
    }

    public void setHashed_password(String hashed_password) {
        this.hashed_password = hashed_password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "id=" + id + ", email_address=" + email_address + ", phone_number=" + phone_number + ", hashed_password=" + hashed_password + ", role=" + role + '}';
    }
    
}
