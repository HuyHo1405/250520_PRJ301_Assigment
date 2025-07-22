<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Giỏ hàng của bạn</title>
        <link rel="stylesheet" href="assets/css/cart.css"/>
    </head>
    <body>
        <div class="dashboard-wrapper">
        <jsp:include page="assets/components/sidebar.jsp" />

        <div class="main-content">
            <h2>Shopping Cart Item</h2>

            <c:choose>
                <c:when test="${empty shoppingCartList}">
                    <p>Không có sản phẩm nào trong giỏ hàng.</p>
                </c:when>
                <c:otherwise>
                    <table border="1" cellpadding="10" cellspacing="0">
                        <thead>
                            <tr>
                                <th>ORD</th>
                                <th>Cart Id</th>
                                <th>Item Id</th>
                                <th>Quantity</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cart" items="${shoppingCartList}">
                                <tr>
                                    <td>${lolo}</td>
                                    <td>${cart.id}</td>
                                    <td>${cart.item_id}</td>
                                    <td>${cart.quantity}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <form action="MainController" method="post" style="margin-top: 20px;">
                        <input type="hidden" name="action" value="toCheckOut"/>
                        <input type="submit" value="Thanh toán"/>
                    </form>
                </c:otherwise>
            </c:choose>

        </div>
    </body>
</html>