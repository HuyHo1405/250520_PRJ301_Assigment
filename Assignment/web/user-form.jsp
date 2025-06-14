<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.dto.UserDTO"%>
<%@page import="utils.UserUtils"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>User Form</title>
    </head>
    <body>
        
        <%
            String action = (String) request.getAttribute("actionType");
            if (action == null) action = "login";
            String error = (String) request.getAttribute("error");
            if (error == null) error = "";
            UserDTO user = UserUtils.getUser(request);
        %>
        
        <h1>
            <%= 
                action.equals("login") ? "Đăng nhập" :
                action.equals("register") ? "Đăng ký" :
                action.equals("editProfile") ? "Cập nhật thông tin" :
                action.equals("forgotPassword") ? "Quên mật khẩu" : ""
            %>
        </h1>

        <% if (error != null) { %>
        <p style="color:red;"><%= error %></p>
        <% } %>

        <% if (action.equals("login")) { %>
        
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="login"/>
            Email: <input type="email" name="email" required><br>
            Password: <input type="password" name="password" required><br>
            <button type="submit">Đăng nhập</button>
        </form></br>
        
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toRegister"/>
            <button type="submit">Đăng ký</button>
        </form>
        
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toForgotPassword"/>
            <button type="submit">Quên mật khẩu</button>
        </form>

        <% } else if (action.equals("register")) { %>
        
        <form action="UserController" method="post">
            <input type="hidden" name="action" value="register"/>
            <%
                String inputEmail = (String) request.getAttribute("inputEmail");
                if(inputEmail == null) inputEmail = "";
                String inputPhone = (String) request.getAttribute("inputPhone");
                if(inputPhone == null) inputPhone = "";
            %>
            Email: <input type="email" name="email" value="<%= inputEmail %>" required><br>
            Phone: <input type="text" name="phone" value="<%= inputPhone %>" required><br>
            Password: <input type="password" name="password" required><br>
            Confirm Password: <input type="password" name="confirmPassword" required><br>
            <button type="submit">Đăng ký</button>
        </form>
            
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toLogin"/>
            <button type="submit">Đăng nhập</button>
        </form>

        <% } else if (action.equals("profile")) { %>
        
        <form action="UserController" method="post">
            <input type="hidden" name="action" value="update"/>
            <%
                String inputEmail = (String) request.getAttribute("inputEmail");
                if(inputEmail == null) inputEmail = user.getEmail_address();
                String inputPhone = (String) request.getAttribute("inputPhone");
                if(inputPhone == null) inputPhone = user.getPhone_number();
            %>
            Email: <input type="email" name="email" value="<%= inputEmail %>" required><br>
            Phone: <input type="text" name="phone" value="<%= inputPhone %>" required><br>
            <button type="submit">Cập nhật</button>
        </form>
            
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toLogin"/>
            <button type="submit">Đăng nhập</button>
        </form>
            
        <% } else if (action.equals("forgotPassword")) { %>
        
        <form action="UserController" method="post">
            <input type="hidden" name="action" value="forgotPassword"/>
            Email: <input type="email" name="email" required><br>
            <button type="submit">Gửi yêu cầu khôi phục</button>
        </form>
        
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toLogin"/>
            <button type="submit">Đăng nhập</button>
        </form>

        <% } else if (action.equals("changePassword")) { %>
        
        <!-- TODO change password -->
        
        <% } %>
    </body>
</html>
