<%-- 
    Document   : reset-password
    Created on : Jul 7, 2025, 7:31:56 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đặt lại mật khẩu</title>
        <script>
            // Validate hai mật khẩu giống nhau
            function validateForm() {
                const newPassword = document.getElementById("newPassword").value;
                const confirmPassword = document.getElementById("confirmPassword").value;
                if (newPassword !== confirmPassword) {
                    alert("Mật khẩu xác nhận không khớp.");
                    return false;
                }
                return true;
            }

            // Ngăn người dùng bấm nút back
            if (window.history && window.history.pushState) {
                window.history.pushState(null, "", window.location.href);
                window.onpopstate = function () {
                    window.history.pushState(null, "", window.location.href);
                };
            }
        </script>
    </head>
    <body>
        <h2>Đặt lại mật khẩu</h2>

        <form action="MainController" method="post" onsubmit="return validateForm()">
            <input type="hidden" name="action" value="resetPassword">
            <input type="hidden" name="email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : request.getParameter("email") %>">

            <label>Mật khẩu mới:</label><br>
            <input type="password" id="newPassword" name="newPassword" required><br><br>

            <label>Nhập lại mật khẩu:</label><br>
            <input type="password" id="confirmPassword" name="confirmPassword" required><br><br>

            <button type="submit">Đặt lại mật khẩu</button>
        </form>

        <p style="color:red">
            <%= request.getAttribute("errorMsg") == null ? "" : request.getAttribute("errorMsg") %>
        </p>
        <p style="color:green">
            <%= request.getAttribute("message") == null ? "" : request.getAttribute("message") %>
        </p>
    </body>
</html>
