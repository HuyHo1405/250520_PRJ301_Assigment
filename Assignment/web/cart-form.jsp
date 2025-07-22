<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thanh toán</title>
    <link rel="stylesheet" href="assets/css/cart-form.css">
</head>
<body>
    <div id="layout">
        
            <jsp:include page="assets/components/sidebar.jsp" />    
    
<div class="container">
    <h2>Thông tin thanh toán</h2>

    <c:if test="${not empty errorMsg}">
        <p style="color:red">${errorMsg}</p>
    </c:if>
    <c:if test="${not empty successMsg}">
        <p style="color:green">${successMsg}</p>
    </c:if>

    <form action="MainController" method="post">
        <input type="hidden" name="action" value="checkoutCart"/>
        <input type="hidden" name="userId" value="${userId}"/>

        <label for="addressId">Địa chỉ giao hàng:</label><br>
        <select name="addressId" id="addressId" required>
            <c:forEach var="addr" items="${addressList}">
                <option value="${addr.id}">${addr.fullAddress}</option>
            </c:forEach>
        </select><br><br>

        <label for="paymentMethodId">Phương thức thanh toán:</label><br>
        <select name="paymentMethodId" id="paymentMethodId" required>
            <c:forEach var="pm" items="${paymentMethods}">
                <option value="${pm.id}">${pm.name}</option>
            </c:forEach>
        </select><br><br>

        <label for="shippingMethodId">Phương thức giao hàng:</label><br>
        <select name="shippingMethodId" id="shippingMethodId" required>
            <c:forEach var="sm" items="${shippingMethods}">
                <option value="${sm.id}">${sm.name}</option>
            </c:forEach>
        </select><br><br>

        <button type="submit">Xác nhận đặt hàng</button>
    </form>

    <br>
    <a href="MainController?action=viewCart">← Quay lại giỏ hàng</a>
</div>
</div> 
</body>
</html>