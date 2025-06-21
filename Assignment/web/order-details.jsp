<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.dto.*, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Details</title>
</head>
<body>
<h1>Chi tiết đơn hàng</h1>

<%
    ShoppingOrderDTO order = (ShoppingOrderDTO) request.getAttribute("order");
    OrderStatusDTO orderStatus = (OrderStatusDTO) request.getAttribute("orderStatus");
    PaymentMethodDTO paymentMethod = (PaymentMethodDTO) request.getAttribute("paymentMethod");
    PaymentTypeDTO paymentType = (PaymentTypeDTO) request.getAttribute("paymentType");
    ShippingMethodDTO shippingMethod = (ShippingMethodDTO) request.getAttribute("shippingMethod");
    AddressDTO address = (AddressDTO) request.getAttribute("defaultAdress");
    UserDTO user = (UserDTO) request.getAttribute("user");
    List<OrderLineDTO> orderItemList = (List<OrderLineDTO>) request.getAttribute("OrderItemList");
    List<OrderStatusDTO> statusList = (List<OrderStatusDTO>) request.getAttribute("OrderStatusList"); // Bạn nhớ set từ servlet
%>

<h2>Thông tin đơn hàng</h2>
<form action="MainController" method="post">
    <input type="hidden" name="action" value="updateOrderStatus">
    <input type="hidden" name="orderId" value="<%= order.getId() %>">

    <ul>
        <li><strong>Mã đơn hàng:</strong> <%= order.getOrder_code() %></li>
        <li><strong>Trạng thái:</strong>
            <select name="orderStatusId">
                <%
                    if (statusList != null) {
                        for (OrderStatusDTO status : statusList) {
                %>
                <option value="<%= status.getId() %>" <%= status.getId() == orderStatus.getId() ? "selected" : "" %>>
                    <%= status.getStatus() %>
                </option>
                <%
                        }
                    }
                %>
            </select>
            <button type="submit">Cập nhật trạng thái</button>
        </li>
        <li><strong>Ngày đặt hàng:</strong> <%= order.getOrderDate() %></li>
        <li><strong>Tổng tiền:</strong> <%= order.getOrderTotal() %> VND</li>
    </ul>
</form>

<h2>Thông tin thanh toán</h2>
<ul>
    <li><strong>Phương thức thanh toán:</strong> <%= paymentMethod.getName() %></li>
    <li><strong>Loại thanh toán:</strong> <%= paymentType.getValue() %></li>
</ul>

<h2>Thông tin giao hàng</h2>
<ul>
    <li><strong>Phương thức vận chuyển:</strong> <%= shippingMethod.getName() %> - <%= shippingMethod.getPrice() %> VND</li>
    <li><strong>Địa chỉ giao hàng:</strong> <%= address.getFullAddress() %></li>
</ul>

<h2>Thông tin khách hàng</h2>
<ul>
    <li><strong>Email:</strong> <%= user.getEmail_address() %></li>
    <li><strong>Điện thoại:</strong> <%= user.getPhone_number() %></li>
</ul>

<h2>Sản phẩm trong đơn</h2>
<%
    if (orderItemList != null && !orderItemList.isEmpty()) {
%>
<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>Item ID</th>
        <th>Số lượng</th>
        <th>Giá</th>
    </tr>
    <%
        for (OrderLineDTO item : orderItemList) {
    %>
    <tr>
        <td><%= item.getId() %></td>
        <td><%= item.getItem_id() %></td>
        <td><%= item.getQuantity() %></td>
        <td><%= item.getPrice() %></td>
    </tr>
    <%
        }
    %>
</table>
<%
    } else {
%>
<p>Không có sản phẩm nào trong đơn hàng.</p>
<%
    }
%>

<!-- Form để quay về danh sách đơn hàng -->
<form action="MainController" method="post"">
    <input type="hidden" name="action" value="toAdminOrdersPage">
    <button type="submit">Quay lại danh sách đơn hàng</button>
</form>

</body>
</html>
