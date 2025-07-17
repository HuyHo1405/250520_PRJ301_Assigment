<%-- 
    Document   : forgot-password
    Created on : Jul 7, 2025, 7:23:38 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Forgot Password</title>
    </head>
    <body>
        <h2>Quên mật khẩu</h2>
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="forgotPassword">

            <label>Email của bạn:</label><br>
            <input type="email" name="email" required><br><br>

            <button type="submit">Gửi liên kết đặt lại mật khẩu</button>
        </form>

        <p style="color: red;"><%= request.getAttribute("errorMsg") != null ? request.getAttribute("errorMsg") : "" %></p>
        <p style="color: green;"><%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %></p>
        <br>
        <form action="MainController" method="get">
            <input type="hidden" name="action" value="toLogin">
            <button type="submit">Quay lại đăng nhập</button>
        </form>
    </body>
</html>
