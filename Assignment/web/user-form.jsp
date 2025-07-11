<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>User Form</title>
    </head>
    <body>

        <c:set var="action" value="${actionType != null ? actionType : 'login'}" />
        <c:set var="errorMsg" value="${error != null ? error : ''}" />
        <c:set var="inputEmail" value="${inputEmail != null ? inputEmail : (user != null ? user.email_address : '')}" />
        <c:set var="inputPhone" value="${inputPhone != null ? inputPhone : (user != null ? user.phone_number : '')}" />

        <h1>
            <c:choose>
                <c:when test="${action eq 'login'}">Login Form</c:when>
                <c:when test="${action eq 'register'}">Register Form</c:when>
                <c:when test="${action eq 'profile'}">Edit Profile Form</c:when>
                <c:when test="${action eq 'forgotPassword'}">???</c:when>
                <c:when test="${action eq 'changePassword'}">???</c:when>
            </c:choose>
        </h1>
        
        <hr>

        <c:if test="${not empty errorMsg}">
            <p style="color:red;">${errorMsg}</p>
        </c:if>

        <!-- Gộp form -->
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="${action}"/>

            <c:if test="${action eq 'login' or action eq 'register' or action eq 'profile' or action eq 'forgotPassword'}">
                Email: <input type="email" name="email" value="${inputEmail}" required><br>
            </c:if>

            <c:if test="${action eq 'register' or action eq 'profile'}">
                Phone: <input type="text" name="phone" value="${inputPhone}" required><br>
            </c:if>

            <c:if test="${action eq 'login' or action eq 'register'}">
                Password: <input type="password" name="password" required><br>
            </c:if>

            <c:if test="${action eq 'register'}">
                Confirm Password: <input type="password" name="confirmPassword" required><br>
            </c:if>

            <c:if test="${action eq 'changePassword'}">
                Old Password: <input type="password" name="oldPassword" required><br>
                New Password: <input type="password" name="newPassword" required><br>
                Confirm New Password: <input type="password" name="confirmNewPassword" required><br>
            </c:if>

            <button type="submit">
                <c:choose>
                    <c:when test="${action eq 'login'}">Login</c:when>
                    <c:when test="${action eq 'register'}">Register</c:when>
                    <c:when test="${action eq 'profile'}">Submit</c:when>
                    <c:when test="${action eq 'forgotPassword'}">???</c:when>
                    <c:when test="${action eq 'changePassword'}">???</c:when>
                </c:choose>
            </button>
        </form>

        <!-- Nút phụ -->
        <br>
        <c:choose>
            <c:when test="${action eq 'login'}">
                <form action="MainController" method="post" style="display: inline">
                    <button name="action" value="toRegister">Register</button>
                </form>
                <form action="MainController" method="post" style="display: inline">
                    <button name="action" value="forgotPassword">Forgot Password</button>
                </form>
            </c:when>

            <c:otherwise>
                <form action="MainController" method="post">
                    <button name="action" value="toLogin">Back</button>
                </form>
            </c:otherwise>
        </c:choose>

    </body>
</html>
