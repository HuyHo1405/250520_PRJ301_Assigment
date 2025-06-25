<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Details</title>
</head>
<body>
<h1>Order Details</h1>

<h2>Order Information</h2>
<form action="MainController" method="post">
    <input type="hidden" name="action" value="updateOrderStatus">
    <input type="hidden" name="orderId" value="${order.id}">

    <ul>
        <li><strong>Order Code:</strong> ${order.order_code}</li>
        <li><strong>Status:</strong>
            <select name="orderStatusId">
                <c:forEach var="status" items="${OrderStatusList}">
                    <option value="${status.id}" ${status.id == orderStatus.id ? 'selected' : ''}>
                        ${status.status}
                    </option>
                </c:forEach>
            </select>
            <button type="submit">Update Status</button>
        </li>
        <li><strong>Order Date:</strong> ${order.orderDate}</li>
        <li><strong>Total:</strong> ${order.orderTotal} VND</li>
    </ul>
</form>

<h2>Payment Information</h2>
<ul>
    <li><strong>Payment Method:</strong> ${paymentMethod.name}</li>
    <li><strong>Payment Type:</strong> ${paymentType.value}</li>
</ul>

<h2>Shipping Information</h2>
<ul>
    <li><strong>Shipping Method:</strong> ${shippingMethod.name} - ${shippingMethod.price} VND</li>
    <li><strong>Shipping Address:</strong> ${defaultAdress.fullAddress}</li>
</ul>

<h2>Customer Information</h2>
<ul>
    <li><strong>Email:</strong> ${user.email_address}</li>
    <li><strong>Phone:</strong> ${user.phone_number}</li>
</ul>

<h2>Order Items</h2>
<c:choose>
    <c:when test="${not empty OrderItemList}">
        <table border="1" cellpadding="5" cellspacing="0">
            <tr>
                <th>ID</th>
                <th>Item ID</th>
                <th>Quantity</th>
                <th>Price</th>
            </tr>
            <c:forEach var="item" items="${OrderItemList}">
                <tr>
                    <td>${item.id}</td>
                    <td>${item.item_id}</td>
                    <td>${item.quantity}</td>
                    <td>${item.price}</td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
        <p>No items in this order.</p>
    </c:otherwise>
</c:choose>

<!-- Back to order list -->
<form action="MainController" method="post">
    <input type="hidden" name="action" value="toAdminOrdersPage">
    <button type="submit">Back to Order List</button>
</form>

</body>
</html>
