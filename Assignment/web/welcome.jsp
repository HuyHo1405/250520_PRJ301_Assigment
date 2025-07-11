<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome</title>
    </head>
    <body>
        <h1>Hello ${user.email_address}!</h1>

        <hr>

        <form action="MainController" method="post" style="display:inline;">
            <button name="action" value="toProfile">Edit Profile</button>
            <button name="action" value="logout">Logout</button>
        </form>
    </body>
</html>
