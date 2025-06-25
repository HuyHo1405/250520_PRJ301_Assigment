<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Form</title>
</head>
<body>

<!-- Default values -->
<c:set var="action" value="${empty actionType ? 'login' : actionType}" />
<c:set var="error" value="${empty error ? '' : error}" />
<c:set var="inputEmail" value="${not empty inputEmail ? inputEmail : (not empty user ? user.email_address : '')}" />
<c:set var="inputPhone" value="${not empty inputPhone ? inputPhone : (not empty user ? user.phone_number : '')}" />

<!-- Page heading -->
<c:choose>
    <c:when test="${action eq 'login'}"><h1>Login</h1></c:when>
    <c:when test="${action eq 'register'}"><h1>Register</h1></c:when>
    <c:when test="${action eq 'forgotPassword'}"><h1>Forgot Password</h1></c:when>
    <c:when test="${action eq 'changePassword'}"><h1>Change Password</h1></c:when>
</c:choose>
<hr>

<!-- Error message -->
<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>

<!-- Main form -->
<form action="${action eq 'register' || action eq 'forgotPassword' ? 'UserController' : 'MainController'}" method="post">
    <input type="hidden" name="action" value="${action}" />

    <!-- Email field (not for changePassword) -->
    <c:if test="${action ne 'changePassword'}">
        Email: <input type="email" name="email" value="${inputEmail}" required><br>
    </c:if>

    <!-- Phone field (only for register) -->
    <c:if test="${action eq 'register'}">
        Phone: <input type="text" name="phone" value="${inputPhone}" required><br>
    </c:if>

    <!-- Password field (login & register) -->
    <c:if test="${action eq 'login' || action eq 'register'}">
        Password: <input type="password" name="password" required><br>
    </c:if>

    <!-- Confirm password (register) -->
    <c:if test="${action eq 'register'}">
        Confirm Password: <input type="password" name="confirmPassword" required><br>
    </c:if>

    <!-- Change password fields -->
    <c:if test="${action eq 'changePassword'}">
        Old Password: <input type="password" name="oldPassword" required><br>
        New Password: <input type="password" name="newPassword" required><br>
        Confirm New Password: <input type="password" name="confirmNewPassword" required><br>
    </c:if>

    <button type="submit">
        <c:choose>
            <c:when test="${action eq 'login'}">Login</c:when>
            <c:when test="${action eq 'register'}">Register</c:when>
            <c:when test="${action eq 'forgotPassword'}">Send Reset Request</c:when>
            <c:when test="${action eq 'changePassword'}">Change Password</c:when>
        </c:choose>
    </button>
</form>

<!-- Navigation buttons -->
<br>
<form action="MainController" method="post">
    <c:choose>
        <c:when test="${action eq 'login'}">
            <button name="action" value="toRegister">Register</button>
            <button name="action" value="toForgotPassword">Forgot Password</button>
        </c:when>
        <c:when test="${action eq 'register' || action eq 'forgotPassword' || action eq 'changePassword'}">
            <button name="action" value="toLogin">Back</button>
        </c:when>
    </c:choose>
</form>

</body>
</html>
