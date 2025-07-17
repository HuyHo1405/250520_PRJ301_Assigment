<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.dto.*"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý cấu hình hệ thống</title>
    </head>
    <body>
        <h1>Quản lý cấu hình hệ thống</h1>

        <%
            String editType = request.getParameter("editType");
            String editId = request.getParameter("editId");
        %>

        <h2>Trạng thái đơn hàng</h2>
        <table border="1">
            <tr><th>ID</th><th>Trạng thái</th><th>Hành động</th></tr>
                    <%
                        List<OrderStatusDTO> orderStatusList = (List<OrderStatusDTO>) request.getAttribute("orderStatusList");
                        if (orderStatusList != null) {
                            for (OrderStatusDTO dto : orderStatusList) {
                                boolean editable = "orderStatus".equals(editType) && String.valueOf(dto.getId()).equals(editId);
                    %>
            <tr>
                <td><%= dto.getId() %></td>
                <td>
                    <form action="SystemConfigController" method="post">
                        <input type="hidden" name="type" value="orderStatus"/>
                        <input type="hidden" name="id" value="<%= dto.getId() %>"/>
                        <%
                            if (editable) {
                        %>
                        <input type="text" name="status" value="<%= dto.getStatus() %>"/>
                </td>
                <td>
                    <input type="hidden" name="action" value="updateSystemConfig"/>
                    <button type="submit">Cập nhật</button>
                    </form>
                    <form action="SystemConfigController" method="post" style="display:inline;">
                        <input type="hidden" name="type" value="orderStatus"/>
                        <input type="hidden" name="id" value="<%= dto.getId() %>"/>
                        <input type="hidden" name="action" value="removeSystemConfig"/>
                        <button type="submit" onclick="return confirm('Bạn có chắc muốn xoá cấu hình này?')">Xoá</button>
                    </form>
                </td>
                <%
                    } else {
                %>
            <input type="text" name="status" value="<%= dto.getStatus() %>" readonly/>
        </td>
        <td>
            <input type="hidden" name="action" value="getSystemConfig"/>
            <input type="hidden" name="editType" value="orderStatus"/>
            <button type="submit" name="editId" value="<%= dto.getId() %>">Chỉnh sửa</button>
            </form>
        </td>
        <%
            }
        %>
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
<div id="orderStatusAddRow"></div>
<button id="orderStatusAddBtn" onclick="addOrderStatusRow()">Thêm trạng thái mới</button>

<h2>Phương thức thanh toán</h2>
<table border="1">
    <tr><th>ID</th><th>Giá trị</th><th>Hành động</th></tr>
            <%
                List<PaymentTypeDTO> paymentTypeList = (List<PaymentTypeDTO>) request.getAttribute("paymentTypeList");
                if (paymentTypeList != null) {
                    for (PaymentTypeDTO dto : paymentTypeList) {
                        boolean editable = "paymentType".equals(editType) && String.valueOf(dto.getId()).equals(editId);
            %>
    <tr>
        <td><%= dto.getId() %></td>
        <td>
            <form action="SystemConfigController" method="post">
                <input type="hidden" name="type" value="paymentType"/>
                <input type="hidden" name="id" value="<%= dto.getId() %>"/>
                <%
                    if (editable) {
                %>
                <input type="text" name="value" value="<%= dto.getValue() %>"/>
        </td>
        <td>
            <input type="hidden" name="action" value="updateSystemConfig"/>
            <button type="submit">Cập nhật</button>
            </form>
            <form action="SystemConfigController" method="post" style="display:inline;">
                <input type="hidden" name="type" value="paymentType"/>
                <input type="hidden" name="id" value="<%= dto.getId() %>"/>
                <input type="hidden" name="action" value="removeSystemConfig"/>
                <button type="submit" onclick="return confirm('Bạn có chắc muốn xoá cấu hình này?')">Xoá</button>
            </form>
        </td>
        <%
            } else {
        %>
    <input type="text" name="value" value="<%= dto.getValue() %>" readonly/>
</td>
<td>
    <input type="hidden" name="action" value="getSystemConfig"/>
    <input type="hidden" name="editType" value="paymentType"/>
    <button type="submit" name="editId" value="<%= dto.getId() %>">Chỉnh sửa</button>
    </form>
</td>
<%
    }
%>
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
<div id="paymentTypeAddRow"></div>
<button id="paymentTypeAddBtn" onclick="addPaymentTypeRow()">Thêm phương thức mới</button>

<h2>Phương thức giao hàng</h2>
<table border="1">
    <tr><th>ID</th><th>Tên</th><th>Giá</th><th>Hành động</th></tr>
            <%
                List<ShippingMethodDTO> shippingList = (List<ShippingMethodDTO>) request.getAttribute("shippingMethodList");
                if (shippingList != null) {
                    for (ShippingMethodDTO dto : shippingList) {
                        boolean editable = "shippingMethod".equals(editType) && String.valueOf(dto.getId()).equals(editId);
            %>
    <tr>
        <td><%= dto.getId() %></td>
        <td colspan="2">
            <form action="SystemConfigController" method="post">
                <input type="hidden" name="type" value="shippingMethod"/>
                <input type="hidden" name="id" value="<%= dto.getId() %>"/>
                <%
                    if (editable) {
                %>
                Tên: <input type="text" name="name" value="<%= dto.getName() %>"/>
                Giá: <input type="text" name="price" value="<%= dto.getPrice() %>"/>
        </td>
        <td>
            <input type="hidden" name="action" value="updateSystemConfig"/>
            <button type="submit">Cập nhật</button>
            </form>
            <form action="SystemConfigController" method="post" style="display:inline;">
                <input type="hidden" name="type" value="shippingMethod"/>
                <input type="hidden" name="id" value="<%= dto.getId() %>"/>
                <input type="hidden" name="action" value="removeSystemConfig"/>
                <button type="submit" onclick="return confirm('Bạn có chắc muốn xoá cấu hình này?')">Xoá</button>
            </form>
        </td>
        <%
            } else {
        %>
        Tên: <input type="text" name="name" value="<%= dto.getName() %>" readonly/>
    Giá: <input type="text" name="price" value="<%= dto.getPrice() %>" readonly/>
</td>
<td>
    <input type="hidden" name="action" value="getSystemConfig"/>
    <input type="hidden" name="editType" value="shippingMethod"/>
    <button type="submit" name="editId" value="<%= dto.getId() %>">Chỉnh sửa</button>
    </form>
</td>
<%
    }
%>
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
<div id="shippingMethodAddRow"></div>
<button id="shippingMethodAddBtn" onclick="addShippingMethodRow()">Thêm phương thức mới</button>

<h2>Quốc gia</h2>
<table border="1">
    <tr><th>ID</th><th>Tên</th><th>Hành động</th></tr>
            <%
                List<CountryDTO> countryList = (List<CountryDTO>) request.getAttribute("countryList");
                if (countryList != null) {
                    for (CountryDTO dto : countryList) {
                        boolean editable = "country".equals(editType) && String.valueOf(dto.getId()).equals(editId);
            %>
    <tr>
        <td><%= dto.getId() %></td>
        <td>
            <form action="SystemConfigController" method="post">
                <input type="hidden" name="type" value="country"/>
                <input type="hidden" name="id" value="<%= dto.getId() %>"/>
                <%
                    if (editable) {
                %>
                <input type="text" name="name" value="<%= dto.getCountry_name() %>"/>
        </td>
        <td>
            <input type="hidden" name="action" value="updateSystemConfig"/>
            <button type="submit">Cập nhật</button>
            </form>
            <form action="SystemConfigController" method="post" style="display:inline;">
                <input type="hidden" name="type" value="country"/>
                <input type="hidden" name="id" value="<%= dto.getId() %>"/>
                <input type="hidden" name="action" value="removeSystemConfig"/>
                <button type="submit" onclick="return confirm('Bạn có chắc muốn xoá cấu hình này?')">Xoá</button>
            </form>
        </td>
        <%
            } else {
        %>
    <input type="text" name="name" value="<%= dto.getCountry_name() %>" readonly/>
</td>
<td>
    <input type="hidden" name="action" value="getSystemConfig"/>
    <input type="hidden" name="editType" value="country"/>
    <button type="submit" name="editId" value="<%= dto.getId() %>">Chỉnh sửa</button>
    </form>
</td>
<%
    }
%>
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
<div id="countryAddRow"></div>
<button id="countryAddBtn" onclick="addCountryRow()">Thêm quốc gia mới</button>

<br/><br/>
<form action="SystemConfigController" method="post">
    <input type="hidden" name="action" value="toSystemConfig"/>
    <button type="submit">Trở về trang chính</button>
</form>

<script>
    function addOrderStatusRow() {
        document.getElementById('orderStatusAddRow').innerHTML = `
            <form action="SystemConfigController" method="post">
                <input type="hidden" name="action" value="addSystemConfig"/>
                <input type="hidden" name="type" value="orderStatus"/>
                <input type="text" name="status" placeholder="Nhập trạng thái đơn hàng mới"/>
                <button type="submit">Submit</button>
            </form>
        `;
        document.getElementById('orderStatusAddBtn').style.display = 'none';
    }

    function addPaymentTypeRow() {
        document.getElementById('paymentTypeAddRow').innerHTML = `
            <form action="SystemConfigController" method="post">
                <input type="hidden" name="action" value="addSystemConfig"/>
                <input type="hidden" name="type" value="paymentType"/>
                <input type="text" name="value" placeholder="Nhập tên phương thức"/>
                <button type="submit">Submit</button>
            </form>
        `;
        document.getElementById('paymentTypeAddBtn').style.display = 'none';
    }

    function addShippingMethodRow() {
        document.getElementById('shippingMethodAddRow').innerHTML = `
            <form action="SystemConfigController" method="post">
                <input type="hidden" name="action" value="addSystemConfig"/>
                <input type="hidden" name="type" value="shippingMethod"/>
                Tên: <input type="text" name="name" placeholder="Tên phương thức"/>
                Giá: <input type="text" name="price" placeholder="Giá"/>
                <button type="submit">Submit</button>
            </form>
        `;
        document.getElementById('shippingMethodAddBtn').style.display = 'none';
    }

    function addCountryRow() {
        document.getElementById('countryAddRow').innerHTML = `
            <form action="SystemConfigController" method="post">
                <input type="hidden" name="action" value="addSystemConfig"/>
                <input type="hidden" name="type" value="country"/>
                <input type="text" name="name" placeholder="Tên quốc gia"/>
                <button type="submit">Submit</button>
            </form>
        `;
        document.getElementById('countryAddBtn').style.display = 'none';
    }
</script>
</body>
</html>
