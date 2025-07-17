<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.dto.UserDTO"%>
<%@page import="utils.UserUtils"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>User Form</title>
        <link rel="stylesheet" href="assets/css/user-form.css">
    </head>
    <body>

        <%
            String action = (String) request.getAttribute("actionType");
            if (action == null) action = "login";

            String error = (String) request.getAttribute("error");
            if (error == null) error = "";

            UserDTO user = UserUtils.getUser(request);

            String inputEmail = (String) request.getAttribute("inputEmail");
            String inputPhone = (String) request.getAttribute("inputPhone");

            if (inputEmail == null) inputEmail = (user != null) ? user.getEmail_address() : "";
            if (inputPhone == null) inputPhone = (user != null) ? user.getPhone_number() : "";

            String heading = "";
            switch (action) {
                case "login": heading = "Đăng nhập"; break;
                case "register": heading = "Đăng ký"; break;
                case "profile": heading = "Cập nhật thông tin"; break;
                case "forgotPassword": heading = "Quên mật khẩu"; break;
                case "changePassword": heading = "Đổi mật khẩu"; break;
            }
        %>

        <h1><%= heading %></h1>

        <% if (!error.isEmpty()) { %>
        <p style="color:red;"><%= error %></p>
        <% } %>

        <% if (action.equals("login")) { %>
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="login"/>
            Email: <input type="email" name="email" value="<%= inputEmail %>" required><br>
            Password: <input type="password" name="password" required><br>
            <button type="submit">Đăng nhập</button>
        </form><br>

        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toRegister"/>
            <button type="submit">Đăng ký</button>
        </form>

        <form action="AdminUserController" method="post">
            <input type="hidden" name="action" value="forgotPassword"/>
            <button type="submit">Quên mật khẩu</button>
        </form>

        <% } else if (action.equals("register")) { %>
        <form action="UserController" method="post">
            <input type="hidden" name="action" value="register"/>
            Email: <input type="email" name="email" value="<%= inputEmail %>" required><br>
            Phone: <input type="text" name="phone" value="<%= inputPhone %>" required><br>
            Password: <input type="password" name="password" required><br>
            Confirm Password: <input type="password" name="confirmPassword" required><br>
            <button type="submit">Đăng ký</button>
        </form><br>

        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toLogin"/>
            <button type="submit">Đăng nhập</button>
        </form>

        <% } else if (action.equals("profile")) { %>
        <form action="UserController" method="post">
            <input type="hidden" name="action" value="update"/>
            Email: <input type="email" name="email" value="<%= inputEmail %>" required><br>
            Phone: <input type="text" name="phone" value="<%= inputPhone %>" required><br>
            <button type="submit">Cập nhật</button>
        </form><br>

        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toLogin"/>
            <button type="submit">Đăng nhập</button>
        </form>

        <% } else if (action.equals("forgotPassword")) { %>
        <form action="AdminUserController" method="post">
            <input type="hidden" name="action" value="forgotPassword"/>
            Email: <input type="email" name="email" required><br>
            <button type="submit">Gửi yêu cầu khôi phục</button>
        </form><br>

        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toLogin"/>
            <button type="submit">Đăng nhập</button>
        </form>

        <% } else if (action.equals("changePassword")) { %>
        <form action="AdminUserController" method="post">
            <input type="hidden" name="action" value="changePassword"/>
            Old Password: <input type="password" name="oldPassword" required><br>
            New Password: <input type="password" name="newPassword" required><br>
            Confirm New Password: <input type="password" name="confirmNewPassword" required><br>
            <button type="submit">Đổi mật khẩu</button>
        </form><br>

        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toLogin"/>
            <button type="submit">Đăng nhập</button>
        </form>
        <% } %>

    </body>
</html>
