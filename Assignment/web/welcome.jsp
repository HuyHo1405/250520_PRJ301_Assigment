<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Welcome</title>
    </head>
    <body>

        <h1>Hello ${sessionScope.user.email_address}!</h1>
        <hr>

        <form action="MainController" method="post" style="display:inline;">
            <input type="hidden" name="action" value="toAdminOrdersPage"/>
            <button type="submit">Order Management</button>
        </form>

        <form action="MainController" method="post" style="display:inline;">
            <input type="hidden" name="action" value="toProfile"/>
            <button type="submit">My Profile</button>
        </form>

        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toSystemConfig"/>
            <button type="submit">System Config</button>
        </form>

    </body>
</html>
