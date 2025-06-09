/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

/**
 * Status: Chờ thực hiện
 * Người thực hiện: [...........]
 * Ngày bắt đầu: [...........]
 * viết set up các field cho class này
 */
public class UserDTO {
    private int id;
    private String email_address;
    private String phone_number;
    private String hashed_password;

    public UserDTO() {
    }

    public UserDTO(int id, String email_address, String phone_number, String hashed_password) {
        this.id = id;
        this.email_address = email_address;
        this.phone_number = phone_number;
        this.hashed_password = hashed_password;
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
    
    
    
}
