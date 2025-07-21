package model.dto;

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
