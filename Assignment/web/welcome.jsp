<%-- 
    Document   : welcome
    Created on : 14-Jun-2025, 12:08:08
    Author     : ho huy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="utils.UserUtils"%>
<%@page import="model.dto.UserDTO"%>
<%@page import="model.dao.ProductDAO"%>
<%@page import="java.util.List"%>
<%@page import="model.dto.ProductDTO"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="assets/css/welcome.css">
    </head>
    <body>
        <%
            ProductDAO pdao = new ProductDAO();
            
        %>
        <div id="layout">

            <div id="sidebar"></div>

            <div id="content">
                <div id="header-wrapper">
                    <jsp:include page="assets/components/header.jsp" />
                </div>

                <div id="main-wrapper">
                    <button class="toggle-btn" onclick="toggleMenu()">Show Menu</button>
                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="listProducts"/>
                        <input type="submit" value="View all <%=pdao.count()%> products available"/>
                    </form>
                    <%
                                    List<ProductDTO> list = (List<ProductDTO>) request.getAttribute("products");
                                    if(list!=null){
                    %>

                    <table>
                        <thead>
                            <tr>
                                <th>Hình ảnh</th>
                                <th>Tên sản phẩm</th>
                                <th>Mô tả</th>
                                <th>Xem sản phẩm</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (ProductDTO p : list) {
                            %>
                            <tr>
                                <td><img src="<%= p.getCover_image_link() %>" alt="Ảnh sản phẩm"/></td>
                                <td><%= p.getName() %></td>
                                <td><%= p.getDescription() %></td>
                                <td>
                                    <form action="MainController" method="post">
                                        <input type="hidden" name="action" value="viewProduct"/>
                                        <input type="hidden" name="id" value="<%= p.getId()%>"/>
                                        <input type="submit" value="Xem chi tiết"/>
                                    </form>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>

        <script src="assets/js/welcome.js"></script>
    </body>
</html>
