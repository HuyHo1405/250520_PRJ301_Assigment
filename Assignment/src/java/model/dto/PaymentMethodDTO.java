
package model.dto;

import java.sql.Date;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết set up các field cho class này
 */
public class PaymentMethodDTO {
    private int id;
    private int user_id;
    private int payment_type_id;
    private String provider;
    private String account_number;
    private Date expiry_date;
    private boolean is_default;

    public PaymentMethodDTO() {
    }

    public PaymentMethodDTO(int id, int user_id, int payment_type_id, String provider, String account_number, Date expiry_date, boolean is_default) {
        this.id = id;
        this.user_id = user_id;
        this.payment_type_id = payment_type_id;
        this.provider = provider;
        this.account_number = account_number;
        this.expiry_date = expiry_date;
        this.is_default = is_default;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPayment_type_id() {
        return payment_type_id;
    }

    public void setPayment_type_id(int payment_type_id) {
        this.payment_type_id = payment_type_id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public Date getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(Date expiry_date) {
        this.expiry_date = expiry_date;
    }

    public boolean isIs_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }
}
