<%-- 
    Document   : user-orders
    Created on : Jul 21, 2025, 8:09:33 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.dto.ShoppingOrderDTO"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Orders</title>
    </head>
    <body>
        <h1>My Orders</h1>

        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            String successMessage = (String) request.getAttribute("successMessage");
            if (errorMessage != null) {
        %>
            <p style="color:red"><%= errorMessage %></p>
        <%
            }
            if (successMessage != null) {
        %>
            <p style="color:green"><%= successMessage %></p>
        <%
            }

            List<ShoppingOrderDTO> myOrders = (List<ShoppingOrderDTO>) request.getAttribute("myOrders");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        %>

        <table border="1" cellpadding="5" cellspacing="0">
            <tr>
                <th>Order ID</th>
                <th>Status</th>
                <th>Total Price</th>
                <th>Order Date</th>
                <th>Actions</th>
            </tr>

            <%
                if (myOrders != null && !myOrders.isEmpty()) {
                    for (ShoppingOrderDTO order : myOrders) {

                        String statusName = "Unknown";
                        switch (order.getOrderStatusId()) {
                            case 1: statusName = "Pending"; break;
                            case 2: statusName = "Confirmed"; break;
                            case 3: statusName = "Processing"; break;
                            case 4: statusName = "Shipped"; break;
                            case 5: statusName = "Delivered"; break;
                            case 6: statusName = "Cancelled"; break;
                            case 7: statusName = "Returned"; break;
                            case 8: statusName = "Refunded"; break;
                        }
            %>
            <tr>
                <td><%= order.getId() %></td>
                <td><%= statusName %></td>
                <td>$<%= order.getOrderTotal() %></td>
                <td><%= sdf.format(order.getOrderDate()) %></td>
                <td>
                    <form action="OrderController" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="viewMyOrder"/>
                        <input type="hidden" name="orderId" value="<%= order.getId() %>"/>
                        <input type="submit" value="View Details"/>
                    </form>

                    <%
                        if (order.getOrderStatusId() == 1 || order.getOrderStatusId() == 2) {
                    %>
                    <form action="OrderController" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="cancelOrder"/>
                        <input type="hidden" name="orderId" value="<%= order.getId() %>"/>
                        <input type="submit" value="Cancel Order"/>
                    </form>
                    <%
                        }
                    %>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="5">You have no orders.</td>
            </tr>
            <%
                }
            %>
        </table>

    </body>
</html>
