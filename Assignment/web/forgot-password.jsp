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
        <link rel="stylesheet" href="assets/css/forgot-password.css"/>
    </head>
    <body>
        <div class="container">

            <div class="left-banner">
                <img src="assets/images/hashtag-mua-sam-tha-ga-khong-lo-ve-gia.jpg" alt="ShopHub Banner">
            </div>

            <div class="form-box">

                <div class="heading">
                    <h2>Quên mật khẩu</h2>
                </div>

                <div class="forgot">
                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="forgotPassword">

                        <label>Email của bạn:</label><br>
                        <input type="email" name="email" required><br><br>

                        <button type="submit">Gửi liên kết đặt lại mật khẩu</button>
                    </form>
                </div>

                <div class="message">
                    <p style="color: red;"><%= request.getAttribute("errorMsg") != null ? request.getAttribute("errorMsg") : "" %></p>
                    <p style="color: green;"><%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %></p>
                </div>

                <div class="back-link">
                    <form action="MainController" method="get">
                        <input type="hidden" name="action" value="toLogin">
                        <button type="submit">Quay lại đăng nhập</button>
                    </form>
                </div>
            </div>

        </div>
    </body>
</html>
