    <%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="utils.UserUtils"%>
<%@page import="model.dto.UserDTO"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="assets/css/header.css">
    </head>
    <body>
        <div class="container">
            <div class="top-bar">
                <div class="left-content">
                    <p>Chào mừng đến ShopHub, <%= UserUtils.getUser(request).getEmail_address() %>!</p>
                </div>
                <div class="right-content">
                    <div class="logout-btn">
                        <form action="MainController" method="post">
                            <input type="hidden" name="action" value="logout"/>
                            <input type="submit" value="Logout"/>
                        </form>
                    </div>
                </div>

            </div>

            <div class="middle-bar">
                <div class="logo">
                    <img src="assets/images/hang-hoa-la-gi-481722-removebg-preview.png" alt="Shopping Cart" class="logo-icon">
                    <span>ShopHub</span>
                </div>
                <div class="search-bar">
                    <form action="MainController" method="post" class="search-form">
                        <input type="hidden" name="action" value="searchProducts" />
                        <input type="text" name="keyword" placeholder="ShopHub - trang web bán hàng công nghệ PRJ301" />
                        <button type="submit">
                            <img src="assets/images/glass-2026458_960_720-removebg-preview.png" alt="Search" class="search-icon">
                        </button>
                    </form>                 
                </div>
                <div class="cart">
                    <img src="assets/images/pngtree-shopping-cart-icon-design-template-picture-image_8184878-removebg-preview.png" alt="Cart" class="cart-icon">
                </div>
            </div>
        </div>

    </body>
</html>
