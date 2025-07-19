<%@page contentType="text/html" pageEncoding="UTF-8"%>

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

    <div class="system-btn">
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toSystemConfig"/>
            <button type="submit">System Config</button>
        </form>
    </div>

    <div class="auser-btn">
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="listAllUsers"/>
            <button type="submit">Admin User Management</button>
        </form>
    </div>

    <div class="aproduct-btn">
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="listAllProducts"/>
            <button type="submit">Admin Product Management</button>
        </form>
    </div>

</div>   

