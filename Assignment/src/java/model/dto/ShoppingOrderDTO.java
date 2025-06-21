package model.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Huy
 * Ngày bắt đầu: 19/06/2025
 * Chuyển các trường Date thành Timestamp để chuẩn hóa với JDBC
 */
public class ShoppingOrderDTO {

    private int id;
    private Timestamp orderDate;
    private double orderTotal;
    private int orderStatusId;
    private int paymentMethodId;
    private int shippingMethodId;
    private int addressId;
    private int userId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String order_code;

    public ShoppingOrderDTO() {
    }

    // Constructor KHÔNG có ID (cho insert mới)
    public ShoppingOrderDTO(double orderTotal, int orderStatusId, int paymentMethodId, int shippingMethodId, int addressId, int userId) {
        Timestamp cur = Timestamp.valueOf(LocalDateTime.now());
        this.id = -1;
        this.orderDate = cur;
        this.orderTotal = orderTotal;
        this.orderStatusId = orderStatusId;
        this.paymentMethodId = paymentMethodId;
        this.shippingMethodId = shippingMethodId;
        this.addressId = addressId;
        this.userId = userId;
        this.createdAt = cur;
        this.updatedAt = cur;
        this.order_code = null;
    }

    // Full constructor
    public ShoppingOrderDTO(int id, Timestamp orderDate, double orderTotal, int orderStatusId, int paymentMethodId, int shippingMethodId, int addressId, int userId, Timestamp createdAt, Timestamp updatedAt, String order_code) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderTotal = orderTotal;
        this.orderStatusId = orderStatusId;
        this.paymentMethodId = paymentMethodId;
        this.shippingMethodId = shippingMethodId;
        this.addressId = addressId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.order_code = order_code;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public int getShippingMethodId() {
        return shippingMethodId;
    }

    public void setShippingMethodId(int shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    @Override
    public String toString() {
        return "ShoppingOrderDTO{" + "id=" + id + ", orderDate=" + orderDate + ", orderTotal=" + orderTotal + ", orderStatusId=" + orderStatusId + ", paymentMethodId=" + paymentMethodId + ", shippingMethodId=" + shippingMethodId + ", addressId=" + addressId + ", userId=" + userId + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", order_code=" + order_code + '}';
    }
    
    
}
