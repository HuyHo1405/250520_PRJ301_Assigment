<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
        <form action="MainController" method="post">
            Email: <input type="email" name="email" value="${sessionScope.user.email_address}" required><br>
            Phone: <input type="text" name="phone" value="${sessionScope.user.phone_number}" required><br>
            <button name="action" value="Update Profile">Update</button>
        </form>

        <!-- Section: Address Info -->
        <h2>Address Information</h2>
        <c:choose>
            <c:when test="${empty addressList}">
                <p>You have no saved addresses.</p>
                <form action="MainController" method="post">
                    <button name="action" value="toAddressManagement">Manage Addresses</button>
                </form>
            </c:when>

            <c:otherwise>
                <form action="MainController" method="post">
                    <label for="addressId">Select Default Address:</label>
                    <select name="addressId" required>
                        <c:forEach var="addr" items="${addressList}">
                            <option value="${addr.id}" <c:if test="${addr.id == defaultAddressId}">selected</c:if>>
                                ${addr.fullAddress}
                            </option>
                        </c:forEach>
                    </select><br>
                    <button name="action" value="updateDefaultAddress">Update Default Address</button>
                </form>
            </c:otherwise>
        </c:choose>

        
        <c:if test="${sessionScope.user.role eq 'admin'}">
            <h2>Admin Action</h2>
            <form action="MainController" method="post">
                <button name="action" value="toSystemConfigManagement">System Configuration Management</button>
            </form>
        </c:if>


        <hr>

        <form action="MainController" method="post">
            <button name="action" value="toWelcome">Back to Home</button>
        </form>

    </body>
</html>
