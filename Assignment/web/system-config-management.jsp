<%-- 
    Document   : system-config-management
    Created on : Jun 16, 2025, 2:42:03 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.dto.OrderStatusDTO"%>
<%@page import="model.dto.PaymentTypeDTO"%>
<%@page import="model.dto.ShippingMethodDTO"%>
<%@page import="model.dto.CountryDTO"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý cấu hình hệ thống</title>
</head>
<body>
    <h1>Quản lý cấu hình hệ thống</h1>
    <%-- Order Status --%>
    <h2>Trạng thái đơn hàng</h2>
    <table border="1">
        <tr><th>ID</th><th>Trạng thái</th><th>Hành động</th></tr>
        <%
            List<OrderStatusDTO> orderStatusList = (List<OrderStatusDTO>) request.getAttribute("orderStatusList");
            if (orderStatusList != null) {
                for (OrderStatusDTO dto : orderStatusList) {
        %>
        <tr>
            <td><%= dto.getId() %></td>
            <td>
                <form action="MainController" method="post">
                    <input type="hidden" name="action" value="updateSystemConfig"/>
                    <input type="hidden" name="type" value="orderStatus"/>
                    <input type="hidden" name="id" value="<%= dto.getId() %>"/>
                    <input type="text" name="status" value="<%= dto.getStatus() %>"/>
                    <button type="submit">Cập nhật</button>
                </form>
            </td>
        </tr>
        <%
                }
            } else {
        %>
        <tr><td colspan="3">Không có dữ liệu trạng thái đơn hàng.</td></tr>
        <%
            }
        %>
    </table>

    <%-- Payment Type --%>
    <h2>Phương thức thanh toán</h2>
    <table border="1">
        <tr><th>ID</th><th>Giá trị</th><th>Hành động</th></tr>
        <%
            List<PaymentTypeDTO> paymentTypeList = (List<PaymentTypeDTO>) request.getAttribute("paymentTypeList");
            if (paymentTypeList != null) {
                for (PaymentTypeDTO dto : paymentTypeList) {
        %>
        <tr>
            <td><%= dto.getId() %></td>
            <td>
                <form action="MainController" method="post">
                    <input type="hidden" name="action" value="updateSystemConfig"/>
                    <input type="hidden" name="type" value="paymentType"/>
                    <input type="hidden" name="id" value="<%= dto.getId() %>"/>
                    <input type="text" name="value" value="<%= dto.getValue() %>"/>
                    <button type="submit">Cập nhật</button>
                </form>
            </td>
        </tr>
        <%
                }
            } else {
        %>
        <tr><td colspan="3">Không có dữ liệu phương thức thanh toán.</td></tr>
        <%
            }
        %>
    </table>

    <%-- Shipping Method --%>
    <h2>Phương thức giao hàng</h2>
    <table border="1">
        <tr><th>ID</th><th>Tên</th><th>Giá</th><th>Hành động</th></tr>
        <%
            List<ShippingMethodDTO> shippingList = (List<ShippingMethodDTO>) request.getAttribute("shippingMethodList");
            if (shippingList != null) {
                for (ShippingMethodDTO dto : shippingList) {
        %>
        <tr>
            <td><%= dto.getId() %></td>
            <td>
                <form action="MainController" method="post">
                    <input type="hidden" name="action" value="updateSystemConfig"/>
                    <input type="hidden" name="type" value="shippingMethod"/>
                    <input type="hidden" name="id" value="<%= dto.getId() %>"/>
                    <input type="text" name="name" value="<%= dto.getName() %>"/>
                    <input type="text" name="price" value="<%= dto.getPrice() %>"/>
                    <button type="submit">Cập nhật</button>
                </form>
            </td>
        </tr>
        <%
                }
            } else {
        %>
        <tr><td colspan="4">Không có dữ liệu phương thức giao hàng.</td></tr>
        <%
            }
        %>
    </table>

    <%-- Country --%>
    <h2>Quốc gia</h2>
    <table border="1">
        <tr><th>ID</th><th>Tên</th><th>Hành động</th></tr>
        <%
            List<CountryDTO> countryList = (List<CountryDTO>) request.getAttribute("countryList");
            if (countryList != null) {
                for (CountryDTO dto : countryList) {
        %>
        <tr>
            <td><%= dto.getId() %></td>
            <td>
                <form action="MainController" method="post">
                    <input type="hidden" name="action" value="updateSystemConfig"/>
                    <input type="hidden" name="type" value="country"/>
                    <input type="hidden" name="id" value="<%= dto.getId() %>"/>
                    <input type="text" name="name" value="<%= dto.getCountry_name() %>"/>
                    <button type="submit">Cập nhật</button>
                </form>
            </td>
        </tr>
        <%
                }
            } else {
        %>
        <tr><td colspan="3">Không có dữ liệu quốc gia.</td></tr>
        <%
            }
        %>
    </table>

    <br/>
    <form action="MainController" method="post">
        <input type="hidden" name="action" value="toSystemConfig"/>
        <button type="submit">Trở về trang chính</button>
    </form>
</body>
</html>
