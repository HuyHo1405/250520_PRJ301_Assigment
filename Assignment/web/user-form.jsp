<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Main Page</title>
        <link rel="stylesheet" href="assets/css/user-form.css"/>
    </head>
    <body>
        <c:set var="action" value="${actionType != null ? actionType : 'login'}" />
        <c:set var="errorMsg" value="${error != null ? error : ''}" />
        <c:set var="inputEmail" value="${inputEmail != null ? inputEmail : (user != null ? user.email_address : '')}" />
        <c:set var="inputPhone" value="${inputPhone != null ? inputPhone : (user != null ? user.phone_number : '')}" />

        <div class="container">
            <div class="left-banner">
            </div>
            <div class="form-box">
                <div class="heading">
                    <h1>
                        <c:choose>
                            <c:when test="${action eq 'login'}">Login Form</c:when>
                            <c:when test="${action eq 'register'}">Register Form</c:when>
                            <c:when test="${action eq 'profile'}">Edit Profile Form</c:when>
                            <c:when test="${action eq 'forgotPassword'}">Forgot Password Form</c:when>
                            <c:when test="${action eq 'resetPassword'}">Reset Password Form</c:when>
                            <c:when test="${action eq 'changePassword'}">Change Password Form</c:when>
                        </c:choose>
                    </h1>
                </div>
                <hr>
                <c:if test="${not empty errorMsg}">
                    <p style="color:red;">${errorMsg}</p>
                </c:if>
                <c:if test="${not empty message}">
                    <p style="color:green;">${message}</p>
                </c:if>
                <form action="MainController" method="post">
                    <input type="hidden" name="action" value="${action}"/>
                    <c:if test="${action eq 'login' or action eq 'register' or action eq 'profile' or action eq 'forgotPassword'}">
                        Email: <input type="email" name="email" value="${param.email != null ? param.email : inputEmail}" required><br> <%-- Giữ param.email --%>
                    </c:if>
                    <c:if test="${action eq 'register' or action eq 'profile'}">
                        Phone: <input type="text" name="phone" value="${param.phone != null ? param.phone : inputPhone}" required><br> <%-- Giữ param.phone --%>
                    </c:if>
                    <c:if test="${action eq 'login' or action eq 'register'}">
                        Password: <input type="password" name="password" required><br>
                    </c:if>
                    <c:if test="${action eq 'register'}">
                        Confirm Password: <input type="password" name="confirmPassword" required><br>
                    </c:if>
                    <c:if test="${action eq 'resetPassword'}">
                        <input type="hidden" name="token" value="${param.token}"><br>
                        New Password: <input type="password" name="newPassword" required><br>
                        Confirm New Password: <input type="password" name="confirmNewPassword" required><br>
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
                            <c:when test="${action eq 'forgotPassword'}">Submit</c:when>
                            <c:when test="${action eq 'resetPassword'}">Reset Password</c:when>
                            <c:when test="${action eq 'changePassword'}">Change Password</c:when>
                        </c:choose>
                    </button>
                </form>
                <br>
                <c:choose>
                    <c:when test="${action eq 'login'}">
                        <form action="MainController" method="post" class="login-options">
                            <button name="action" value="toRegister">Register</button>
                            <button name="action" value="toForgotPassword">Forgot Password</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form action="MainController" method="post">
                            <button name="action" value="toLogin">Back</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </body>
</html>