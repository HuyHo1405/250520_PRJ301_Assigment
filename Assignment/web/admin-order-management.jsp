<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.dto.ShoppingOrderDTO"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý đơn hàng</title>
        <style>
            table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                padding: 8px;
                border: 1px solid #ccc;
                text-align: left;
            }
            th {
                background-color: #f4f4f4;
            }
            .error {
                color: red;
                margin-bottom: 10px;
            }
        </style>
    </head>
    <body>

        <h1>Quản lý đơn hàng</h1>

        <% String errorMsg = (String) request.getAttribute("errorMsg"); %>
        <% if (errorMsg != null) { %>
        <div class="error"><%= errorMsg %></div>
        <% } %>

        <form action="AdminOrderController" method="get" style="margin-bottom: 20px;">
            <input type="hidden" name="action" value="searchOrders"/>
            <input type="text" name="strKeyword" placeholder="Tìm theo mã đơn hàng" />
            <button type="submit">Tìm kiếm</button>
            <a href="AdminOrderController?action=exportOrders" style="margin-left: 20px;">📥 Xuất CSV</a>
        </form>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Mã đơn</th>
                    <th>Ngày đặt</th>
                    <th>Tổng tiền</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <%
                    final int CANCEL_ID = 6;
                    List<ShoppingOrderDTO> orderList = (List<ShoppingOrderDTO>) request.getAttribute("orderList");
                    if (orderList != null) {
                        for (ShoppingOrderDTO order : orderList) {
                %>
                <tr>
                    <td><%= order.getId() %></td>
                    <td><%= order.getOrder_code() %></td>
                    <td><%= order.getOrderDate() %></td>
                    <td><%= order.getOrderTotal() %></td>
                    <td><%= order.getOrderStatusId() %></td>
                    <td>
                        <form action="MainController" method="get" style="display:inline;">
                            <input type="hidden" name="action" value="viewOrderDetail">
                            <input type="hidden" name="orderId" value="<%= order.getId() %>">
                            <button type="submit">Xem chi tiết</button>
                        </form> 

                        <% if (order.getOrderStatusId() != CANCEL_ID) { %>
                        <!-- Form disable order -->
                        <form action="AdminOrderController" method="post" style="display:inline;"
                              onsubmit="return confirm('Bạn có chắc muốn hủy đơn hàng này không?');">
                            <input type="hidden" name="action" value="disableOrder">
                            <input type="hidden" name="orderId" value="<%= order.getId() %>">
                            <button type="submit" style="background-color:#e74c3c; color:white;">Disable</button>
                        </form>
                        <% } %>

                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr><td colspan="6">Không có đơn hàng nào.</td></tr>
                <%
                    }
                %>
            </tbody>
        </table>

        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toWelcome"/>
            <button type="submit">Quay lại hồ sơ</button>
        </form>
    </body>
</html>
