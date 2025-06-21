
package model.dto;

import java.sql.Timestamp;

public class OrderExportDTO {
    private int id;
    private String orderCode;
    private String statusName;
    private String paymentMethod;
    private String paymentType;
    private String shippingMethod;
    private double orderTotal;
    private String userName;
    private String address;
    private Timestamp orderDate;
    private Timestamp createdAt;

    public OrderExportDTO(int id, String orderCode, String statusName, String paymentMethod, String paymentType, String shippingMethod, double orderTotal, String userName, String address, Timestamp orderDate, Timestamp createdAt) {
        this.id = id;
        this.orderCode = orderCode;
        this.statusName = statusName;
        this.paymentMethod = paymentMethod;
        this.paymentType = paymentType;
        this.shippingMethod = shippingMethod;
        this.orderTotal = orderTotal;
        this.userName = userName;
        this.address = address;
        this.orderDate = orderDate;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() { return id; }
    public String getOrderCode() { return orderCode; }
    public String getStatusName() { return statusName; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentType() { return paymentType; }
    public String getShippingMethod() { return shippingMethod; }
    public double getOrderTotal() { return orderTotal; }
    public String getUserName() { return userName; }
    public String getAddress() { return address; }
    public Timestamp getOrderDate() { return orderDate; }
    public Timestamp getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "OrderExportDTO{" + "id=" + id + ", orderCode=" + orderCode + ", statusName=" + statusName + ", paymentMethod=" + paymentMethod + ", paymentType=" + paymentType + ", shippingMethod=" + shippingMethod + ", orderTotal=" + orderTotal + ", userName=" + userName + ", address=" + address + ", orderDate=" + orderDate + ", createdAt=" + createdAt + '}';
    }
    
}

