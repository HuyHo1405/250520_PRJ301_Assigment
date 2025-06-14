<%-- 
    Document   : welcome
    Created on : 14-Jun-2025, 12:08:08
    Author     : ho huy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="utils.UserUtils"%>
<%@page import="model.dto.UserDTO"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello <%= UserUtils.getUser(request).getEmail_address()%>!</h1>
        
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toProfile"/>
            <button type="submit">Edit Profile</button>
        </form>
    </body>
</html>
