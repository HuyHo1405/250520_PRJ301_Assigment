<%-- 
    Document   : system-config.jsp
    Created on : Jun 16, 2025, 2:32:03 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Cấu hình hệ thống</title>
    </head>
    <body>
        <h1>Trang cấu hình hệ thống</h1>



        <form action="MainController" method="post">
            <input type="hidden" name="action" value="clearSystemCache"/>
            <button type="submit">Xoá cache hệ thống</button>
        </form>

        <br/>

        <form action="MainController" method="get">
            <input type="hidden" name="action" value="getSystemConfig"/>
            <button type="submit">Quản lý cấu hình chi tiết</button>
        </form>

        <br/>
        
        <form action="SystemConfigController" method="get">
            <input type="hidden" name="action" value="getAppVersion"/>
            <button type="submit">Xem phiên bản ứng dụng</button>
        </form>

        <%
            String appVersion = (String) request.getAttribute("appVersion");
            if (appVersion != null) {
        %>
        <p><strong>Phiên bản ứng dụng:</strong> <%= appVersion %></p>
        <%
            }
        %>
    </body>
</html>
