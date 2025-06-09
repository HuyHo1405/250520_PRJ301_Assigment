/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

import java.sql.Date;

/**
 *
 * @author Admin
 */
public class ShoppingOrderDTO {

    private int id;
    private Date orderDate;
    private double orderTotal;
    private int orderStatusId;
    private int paymentMethodId;
    private int shippingMethodId;
    private int addressId;
    private int userId;
    private Date createdAt;
    private Date updatedAt;

    public ShoppingOrderDTO() {
    }

    public ShoppingOrderDTO(int id, Date orderDate, double orderTotal, int orderStatusId, int paymentMethodId,
            int shippingMethodId, int addressId, int userId, Date createdAt, Date updatedAt) {
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
    }

    // Getters and setters for all fields
    // ...
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
