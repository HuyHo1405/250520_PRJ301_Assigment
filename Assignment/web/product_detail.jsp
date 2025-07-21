<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.dto.ProductDTO"%>
<%@page import="model.dto.ProductItemDTO"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="assets/css/product_detail.css">
    </head>
    <body>
        <%
                ProductDTO product = (ProductDTO) request.getAttribute("product");
                List<ProductItemDTO> items = (List<ProductItemDTO>) request.getAttribute("productItems");
        %>
        <div id="layout">

            <div id="sidebar"></div>

            <div id="content">
                <div id="header-wrapper">
                    <jsp:include page="assets/components/header.jsp" />
                </div>

                <div id="main-wrapper">
                    <button id="toggle-btn" class="toggle-btn" onclick="toggleMenu()">Show Menu</button>
                    <div class="product-container">
                        <h1><%= product.getName() %></h1>
                        <img src="<%= product.getCover_image_link() %>" alt="Ảnh sản phẩm"/>
                        <p><strong>Mô tả:</strong> <%= product.getDescription() %></p>

                        <h2>Phiên bản sản phẩm:</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Mã phiên bản</th>
                                    <th>Số lượng</th>
                                    <th>Hình ảnh</th>
                                    <th>Giá</th>
                                    <th>Xem đánh giá</th>
                                    <th>Thêm vào giỏ</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    for (ProductItemDTO item : items) {
                                %>
                                <tr>
                                    <td><%= item.getSku() %></td>
                                    <td><%= item.getQuantity_in_stock() %></td>
                                    <td><img src="<%= item.getItem_image_link() %>" alt="Ảnh sản phẩm"/></td>
                                    <td><%= item.getPrice() %></td>
                                    <td>
                                        <form action="MainController" method="post">
                                            <input type="hidden" name="action" value="listReviewsByProduct" />
                                            <input type="hidden" name="orderedProductId" value="<%= item.getId() %>" />
                                            <input type="submit" value="Xem đánh giá" />
                                        </form>
                                    </td>
                                    <td>
                                        <form action="MainController" method="post">
                                            <input type="hidden" name="action" value="addToCart" />
                                            <input type="hidden" name="itemId" value="<%= item.getId() %>" />
                                            <input type="number" name="quantity" value="1" min="1"/>
                                            <input type="submit" value="Thêm" />
                                        </form>
                                    </td>
                                </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                    <div class="back-link">
                        <a href="welcome.jsp">Back</a>
                    </div>
                </div>
            </div>
        </div>

        <script src="assets/js/menu.js"></script>

    </body>
</html>
