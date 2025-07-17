<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
</head>
<body>

<h1>${sessionScope.user.email_address} Profile</h1>
<hr>

<!-- Section: Account Info -->
<h2>Account Information</h2>
<form action="UserController" method="post">
    <input type="hidden" name="action" value="update"/>

    Email: <input type="email" name="email" value="${sessionScope.user.email_address}" required><br>
    Phone: <input type="text" name="phone" value="${sessionScope.user.phone_number}" required><br>

    <button type="submit">Update</button>
</form>

<hr>

<!-- Section: Address Info -->
<h2>Address Information</h2>

<c:choose>
    <c:when test="${empty addressList}">
        <p>You have no saved addresses.</p>
        <form action="AddressController" method="post">
            <input type="hidden" name="action" value="toAddressManagement" />
            <button type="submit">Manage Addresses</button>
        </form>
    </c:when>

    <c:otherwise>
        <form action="AddressController" method="post">
            <input type="hidden" name="action" value="updateDefaultAddress" />

            <label for="addressId">Select Default Address:</label><br>
            <select name="addressId" required>
                <c:forEach var="addr" items="${addressList}">
                    <option value="${addr.id}" <c:if test="${addr.id == defaultAddressId}">selected</c:if>>
                        ${addr.fullAddress}
                    </option>
                </c:forEach>
            </select><br><br>

            <button type="submit">Update Default Address</button>
        </form>
    </c:otherwise>
</c:choose>

<hr>

<!-- Back to Welcome -->
<form action="MainController" method="post">
    <button name="action" value="toWelcome">Back to Home</button>
</form>

</body>
</html>
