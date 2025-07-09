<%-- 
    Document   : admin-user-management
    Created on : Jul 7, 2025, 3:34:17 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.dto.UserDTO" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý người dùng</title>
</head>
<body>
    <h1>Quản lý người dùng</h1>

    <%
        String editId = request.getParameter("editId");
        List<UserDTO> userList = (List<UserDTO>) request.getAttribute("userList");
    %>

    <% if (request.getAttribute("message") != null) { %>
    <p><b style="color:green;"><%= request.getAttribute("message") %></b></p>
    <% } %>

    <% if (request.getAttribute("errorMsg") != null) { %>
        <p><b style="color:red;"><%= request.getAttribute("errorMsg") %></b></p>
    <% } %>

    <table border="1">
        <tr>
            <th>ID</th>
            <th>Email</th>
            <th>SĐT</th>
            <th>Vai trò</th>
            <th>Hành động</th>
        </tr>

        <% if (userList != null && !userList.isEmpty()) {
            for (UserDTO user : userList) {
                boolean editable = String.valueOf(user.getId()).equals(editId);
        %>
        <tr>
            <td><%= user.getId() %></td>
            <td colspan="2">
                <form action="MainController" method="post">
                    <input type="hidden" name="userId" value="<%= user.getId() %>"/>
                    <% if (editable) { %>
                        Email: <input type="text" name="email" value="<%= user.getEmail_address() %>"/>
                        SĐT: <input type="text" name="phone" value="<%= user.getPhone_number() %>"/>
            </td>
            <td>
                <select name="newRole">
                    <option value="user" <%= "user".equals(user.getRole()) ? "selected" : "" %>>user</option>
                    <option value="admin" <%= "admin".equals(user.getRole()) ? "selected" : "" %>>admin</option>
                </select>
            </td>
            <td>
                <input type="hidden" name="action" value="updateUser"/>
                <input type="hidden" name="userId" value="<%= user.getId() %>"/>
                <button type="submit">Cập nhật</button>
                </form>

                <form action="MainController" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="disableUser"/>
                    <input type="hidden" name="userId" value="<%= user.getId() %>"/>
                    <button type="submit" onclick="return confirm('Bạn có chắc muốn vô hiệu hoá người dùng này?')">Vô hiệu hoá</button>
                </form>

                <form action="MainController" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="changeUserRole"/>
                    <input type="hidden" name="userId" value="<%= user.getId() %>"/>
                    <input type="hidden" name="newRole" value="<%= user.getRole().equals("admin") ? "user" : "admin" %>"/>
                    <button type="submit">Đổi vai trò</button>
                </form>
                
            </td>
            <% } else { %>
                Email: <input type="text" value="<%= user.getEmail_address() %>" readonly/>
                SĐT: <input type="text" value="<%= user.getPhone_number() %>" readonly/>
            </td>
            <td><%= user.getRole() %></td>
            <td>
                <form action="MainController" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="listAllUsers"/>
                    <button type="submit" name="editId" value="<%= user.getId() %>">Chỉnh sửa</button>
                </form>
            </td>
            <% } %>
        </tr>
        <% }
        } else { %>
        <tr><td colspan="5">Không có người dùng nào.</td></tr>
        <% } %>
    </table>

    <div id="userAddRow"></div>
    <button id="userAddBtn" onclick="addUserRow()">Thêm người dùng mới</button>

    <br/><br/>
    <form action="MainController" method="post">
        <input type="hidden" name="action" value="toWelcome"/>
        <button type="submit">Trở về trang chính</button>
    </form>

    <script>
        function addUserRow() {
            document.getElementById('userAddRow').innerHTML = `
                <form action="MainController" method="post">
                    <input type="hidden" name="action" value="createUser"/>
                    Email: <input type="text" name="email" required/>
                    SĐT: <input type="text" name="phone" required/>
                    Mật khẩu: <input type="password" name="password" required/>
                    Vai trò:
                    <select name="role">
                        <option value="user">user</option>
                        <option value="admin">admin</option>
                    </select>
                    <button type="submit">Tạo</button>
                </form>
            `;
            document.getElementById('userAddBtn').style.display = 'none';
        }
    </script>
</body>
</html>
