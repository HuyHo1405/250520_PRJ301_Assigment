<%--
    Document   : user-orders
    Created on : Jul 21, 2025, 8:09:33 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>My Orders</title>
    <link rel="stylesheet" href="assets/css/user-orders.css">
    <link rel="stylesheet" href="assets/css/sidebar.css"> <%-- Assuming your sidebar has its own CSS --%>
</head>
<body>

<div class="dashboard-wrapper">
    <jsp:include page="assets/components/sidebar.jsp" />

    <div class="main-content">
        <div class="ucontainer">

            <h1>My Orders</h1>

            <c:if test="${not empty errorMessage}">
                <p class="message error">${errorMessage}</p>
            </c:if>
            <c:if test="${not empty successMessage}">
                <p class="message success">${successMessage}</p>
            </c:if>

            <c:choose>
                <c:when test="${not empty myOrders}">
                    <table class="orders-table">
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Status</th>
                                <th>Total Price</th>
                                <th>Order Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${myOrders}">
                                <tr>
                                    <td data-label="Order ID:">${order.id}</td>
                                    <td data-label="Status:">
                                        <c:set var="statusName" value="Unknown"/>
                                        <c:choose>
                                            <c:when test="${order.orderStatusId == 1}"><c:set var="statusName" value="Pending"/></c:when>
                                            <c:when test="${order.orderStatusId == 2}"><c:set var="statusName" value="Confirmed"/></c:when>
                                            <c:when test="${order.orderStatusId == 3}"><c:set var="statusName" value="Processing"/></c:when>
                                            <c:when test="${order.orderStatusId == 4}"><c:set var="statusName" value="Shipped"/></c:when>
                                            <c:when test="${order.orderStatusId == 5}"><c:set var="statusName" value="Delivered"/></c:when>
                                            <c:when test="${order.orderStatusId == 6}"><c:set var="statusName" value="Cancelled"/></c:when>
                                            <c:when test="${order.orderStatusId == 7}"><c:set var="statusName" value="Returned"/></c:when>
                                            <c:when test="${order.orderStatusId == 8}"><c:set var="statusName" value="Refunded"/></c:when>
                                        </c:choose>
                                        <span class="order-status-badge status-${fn:toLowerCase(statusName)}">
                                            ${statusName}
                                        </span>
                                    </td>
                                    <td data-label="Total Price:">
                                        <fmt:formatNumber value="${order.orderTotal}" type="currency" currencySymbol="$"/>
                                    </td>
                                    <td data-label="Order Date:">
                                        <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd"/>
                                    </td>
                                    <td data-label="Actions:" class="order-actions">
                                        <form action="MainController" method="Post" class="inline-form">
                                            <input type="hidden" name="action" value="viewMyOrder"/>
                                            <input type="hidden" name="orderId" value="${order.id}"/>
                                            <button type="submit" class="button view-details-button">View Details</button>
                                        </form>

                                        <c:if test="${order.orderStatusId == 1 || order.orderStatusId == 2}">
                                            <form action="MainController" method="post" class="inline-form">
                                                <input type="hidden" name="action" value="cancelOrder"/>
                                                <input type="hidden" name="orderId" value="${order.id}"/>
                                                <button type="submit" class="button cancel-order-button"
                                                        onclick="return confirm('Are you sure you want to cancel this order?');">Cancel Order</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p class="no-orders-message">You have no orders yet. Start shopping!</p>
                </c:otherwise>
            </c:choose>

        </div> <%-- end container --%>
    </div> <%-- end main-content --%>
</div> <%-- end dashboard-wrapper --%>

</body>
</html>