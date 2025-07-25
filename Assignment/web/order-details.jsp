<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Details</title>
    <link rel="stylesheet" href="assets/css/order-details.css"/>
</head>
<body>

<div class="layout">
<jsp:include page="assets/components/sidebar.jsp" />

        <div class="main-content">
        <div class="order-details-container">
<h1>Order Details</h1>
<hr>

<h2>Order Information</h2>
<c:choose>
    <c:when test="${sessionScope.user.role eq 'admin'}">
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="updateOrderStatus"/>
            <input type="hidden" name="orderId" value="${order.id}"/>
            <ul>
                <li><strong>Order Code:</strong> ${order.order_code}</li>
                <li><strong>Status:</strong>
                    <select name="orderStatusId">
                        <c:forEach var="status" items="${orderStatusList}">
                            <option value="${status.id}" ${status.id == order.orderStatusId ? 'selected' : ''}>
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
    </c:when>
    <c:otherwise>
        <ul>
            <li><strong>Order ID:</strong> ${order.id}</li>
            <li><strong>Status:</strong>
                <c:choose>
                    <c:when test="${order.orderStatusId == 1}">Pending</c:when>
                    <c:when test="${order.orderStatusId == 2}">Confirmed</c:when>
                    <c:when test="${order.orderStatusId == 3}">Processing</c:when>
                    <c:when test="${order.orderStatusId == 4}">Shipped</c:when>
                    <c:when test="${order.orderStatusId == 5}">Delivered</c:when>
                    <c:when test="${order.orderStatusId == 6}">Cancelled</c:when>
                    <c:when test="${order.orderStatusId == 7}">Returned</c:when>
                    <c:when test="${order.orderStatusId == 8}">Refunded</c:when>
                    <c:otherwise>Unknown</c:otherwise>
                </c:choose>
            </li>
            <li><strong>Order Date:</strong> ${order.orderDate}</li>
        </ul>
    </c:otherwise>
</c:choose>

<h2>Shipping Address</h2>
<p>${defaultAdress.fullAddress}</p>

<c:if test="${sessionScope.user.role eq 'admin'}">
    <h2>Payment Information</h2>
    <ul>
        <li><strong>Provider:</strong> ${paymentType.value}</li>
        <li><strong>Payment Method:</strong> ${paymentMethod.provider}</li>
    </ul>

    <h2>Shipping Method</h2>
    <p><strong>${shippingMethod.name}</strong> - ${shippingMethod.price} VND</p>

    <h2>Customer</h2>
    <ul>
        <li><strong>Email:</strong> ${user.email_address}</li>
        <li><strong>Phone:</strong> ${user.phone_number}</li>
    </ul>
</c:if>

<h2>Order Items</h2>
<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>Unit Price</th>
        <th>Quantity</th>
        <th>Total</th>
    </tr>
    <c:set var="grandTotal" value="0" />
    <c:forEach var="line" items="${orderItemList}">
        <c:set var="product" value="${productsMap[line.item_id]}" />
        <c:set var="lineTotal" value="${line.price * line.quantity}" />
        <tr>
            <td>${line.id}</td>
            <td>${line.item_id}</td>
            <td>${line.quantity}</td>
            <td>${line.price}</td>
        </tr>
        <c:set var="grandTotal" value="${grandTotal + lineTotal}" />
    </c:forEach>
    <tr>
        <td colspan="3" style="text-align:right"><strong>Grand Total:</strong></td>
        <td>${grandTotal}</td>
    </tr>
</table>

<hr>
<form action="MainController" method="post">
    <input type="hidden" name="action" value="toWelcome"/>
    <input type="submit" value="Back"/>
</form>

</div> <!-- end container -->
</div> <!-- end container -->

</body>
</html>
