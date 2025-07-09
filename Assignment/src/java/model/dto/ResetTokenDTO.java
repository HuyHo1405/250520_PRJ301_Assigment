/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

/**
 *
 * @author Admin
 */
public class ResetTokenDTO {
    private String email;
    private long expiryTimeMillis;

    public ResetTokenDTO(String email, long expiryTimeMillis) {
        this.email = email;
        this.expiryTimeMillis = expiryTimeMillis;
    }

    public String getEmail() {
        return email;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTimeMillis;
    }
}
