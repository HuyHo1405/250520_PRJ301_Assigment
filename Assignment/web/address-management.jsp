<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.dto.AddressDTO"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý địa chỉ</title>
    </head>
    <body>

        <%
            String error = (String) request.getAttribute("error");
            if (error == null) error = "";

            AddressDTO defaultAddress = (AddressDTO) request.getAttribute("defaultAddress");
            List<AddressDTO> otherAddresses = (List<AddressDTO>) request.getAttribute("otherAddresses");
            if (otherAddresses == null) otherAddresses = java.util.Collections.emptyList();
        %>

        <h1>Quản lý địa chỉ</h1>

        <% if (!error.isEmpty()) { %>
        <p style="color:red;"><%= error %></p>
        <% } %>

        <!-- ✅ Địa chỉ mặc định -->
        <h2>Địa chỉ mặc định</h2>
        <% if (defaultAddress == null) { %>
        <p>Chưa có địa chỉ mặc định.</p>
        <% } else { %>
        <div>
            <strong><%= defaultAddress.getAddress_line1() %></strong><br>
            <%= defaultAddress.getAddress_line2() != null ? defaultAddress.getAddress_line2() + "<br>" : "" %>
            <%= defaultAddress.getCity() + ", " + defaultAddress.getRegion() %><br>
            Quốc gia ID: <%= defaultAddress.getCountry_id() %><br>
        </div>
        <% } %>

        <hr>

        <!-- ✅ Danh sách các địa chỉ khác -->
        <h2>Các địa chỉ khác</h2>
        <% if (otherAddresses.isEmpty()) { %>
        <p>Không có địa chỉ nào khác.</p>
        <% } else { %>
        <table border="1" cellpadding="5" cellspacing="0">
            <tr>
                <th>Địa chỉ</th>
                <th>Thành phố</th>
                <th>Region</th>
                <th>Thao tác</th>
            </tr>
            <% for (AddressDTO addr : otherAddresses) { %>
            <tr>
                <td><%= addr.getAddress_line1() + " " + (addr.getAddress_line2() != null ? addr.getAddress_line2() : "") %></td>
                <td><%= addr.getCity() %></td>
                <td><%= addr.getRegion() %></td>
                <td>
                    <form action="MainController" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="updateDefaultAddress"/>
                        <input type="hidden" name="addressId" value="<%= addr.getId() %>"/>
                        <button type="submit">Đặt làm mặc định</button>
                    </form>

                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="toEditAddress"/>
                        <input type="hidden" name="addressId" value="<%= addr.getId() %>"/>
                        <input type="hidden" name="countryId" value="<%= addr.getCountry_id() %>"/>
                        <input type="hidden" name="unitNumber" value="<%= addr.getUnit_number() != null ? addr.getUnit_number() : "" %>"/>
                        <input type="hidden" name="streetNumber" value="<%= addr.getStreet_number() != null ? addr.getStreet_number() : "" %>"/>
                        <input type="hidden" name="addressLine1" value="<%= addr.getAddress_line1() != null ? addr.getAddress_line1() : "" %>"/>
                        <input type="hidden" name="addressLine2" value="<%= addr.getAddress_line2() != null ? addr.getAddress_line2() : "" %>"/>
                        <input type="hidden" name="city" value="<%= addr.getCity() != null ? addr.getCity() : "" %>"/>
                        <input type="hidden" name="region" value="<%= addr.getRegion() != null ? addr.getRegion() : "" %>"/>

                        <button type="submit">Sửa</button>
                    </form>

                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="removeAddress"/>
                        <input type="hidden" name="addressId" value="<%= addr.getId() %>"/>
                        <button type="submit" onclick="return confirm('Xóa địa chỉ này?')">Xóa</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
        <% } %>

        <hr>

        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toAddAddress"/>
            <button type="submit">Thêm địa chỉ mới</button>
        </form>

        <br>
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toWelcome"/>
            <button type="submit">Quay lại hồ sơ</button>
        </form>

    </body>
</html>
