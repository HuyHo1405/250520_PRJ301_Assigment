<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Management</title>
    <link rel="stylesheet" href="assets/css/address-management.css"/>
    </head>
<body>
    <div class="main-container">
        <h1>Order Management</h1>
        <hr>

        <c:if test="${not empty errorMsg}">
            <p class="error">${errorMsg}</p>
        </c:if>

        <div class="top-actions">
            <form action="AdminOrderController" method="get">
                <input type="hidden" name="action" value="searchOrders"/>
                <input type="text" name="strKeyword" placeholder="Search by order code" />
                <button type="submit">Search</button>
                <a href="AdminOrderController?action=exportOrders" style="margin-left: 20px;">📥 Export CSV</a>
            </form>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Order Code</th>
                    <th>Order Date</th>
                    <th>Total</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty orderList}">
                        <c:forEach var="order" items="${orderList}">
                            <tr>
                                <td>${order.id}</td>
                                <td>${order.order_code}</td>
                                <td>${order.orderDate}</td>
                                <td>${order.orderTotal}</td>
                                <td>${statusMap[order.orderStatusId]}</td>
                                <td>
                                    <form action="MainController" method="get">
                                        <input type="hidden" name="action" value="viewOrderDetail" />
                                        <input type="hidden" name="orderId" value="${order.id}" />
                                        <button type="submit">View</button>
                                    </form>

                                    <c:if test="${order.orderStatusId != 6}">
                                        <form action="AdminOrderController" method="post"
                                              onsubmit="return confirm('Are you sure you want to cancel this order?');">
                                            <input type="hidden" name="action" value="disableOrder" />
                                            <input type="hidden" name="orderId" value="${order.id}" />
                                            <button type="submit" style="background-color:#e74c3c; color:white;">Disable</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="6">No orders found.</td></tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    <hr>
    <div class="back-button-container">
        <form action="MainController" method="post">
            <button name="action" value="toWelcome">Back</button>
        </form>
    </div>
    </div>
    
</body>
</html>