<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="assets/css/menu.css">

<div class="container">
    <div class="heading">
        <h4>Menu</h4>
    </div>

    <div class="edit-btn">
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toProfile"/>
            <button type="submit">Edit Profile</button>
        </form>
    </div>

    <div class="address-btn">
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toAddressManagement"/>
            <button type="submit">Address Management</button>
        </form>
    </div>

    <!-- Chỉ hiển thị nếu là admin -->
    <c:if test="${sessionScope.user.role eq 'admin'}">
        <div class="system-btn">
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="toSystemConfigManagement"/>
                <button type="submit">System Configuration</button>
            </form>
        </div>

        <div class="auser-btn">
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="toAdminUserPage"/>
                <button type="submit">Admin User Management</button>
            </form>
        </div>

        <div class="aproduct-btn">
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="toAdminProductPage"/>
                <button type="submit">Admin Product Management</button>
            </form>
        </div>
        
        <div class="aproduct-btn">
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="toAdminOrdersPage"/>
                <button type="submit">Admin Order Management</button>
            </form>
        </div>
    </c:if>
</div>
