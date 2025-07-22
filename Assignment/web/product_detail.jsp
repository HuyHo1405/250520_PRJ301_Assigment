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
            <jsp:include page="assets/components/sidebar.jsp" />

            <div id="content">
                

                <div id="main-wrapper">
                    <div class="product-container">
                        <h1><%= product.getName() %></h1>
                        <img src="<%= product.getCover_image_link() %>" alt="Ảnh sản phẩm"/>
                        <p><strong>Mô tả:</strong> <%= product.getDescription() %></p>

                        <h2>Phiên bản sản phẩm:</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>SKU</th>
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
                                    if (!item.getIs_active()) continue;
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
                                            <input type="hidden" name="userId" value="${sessionScope.user.id}" />
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
                        <form action="MainController">
                            <button name="action" value="toWelcome">Back</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!--<script src="assets/js/menu.js"></script>-->

    </body>
</html>
