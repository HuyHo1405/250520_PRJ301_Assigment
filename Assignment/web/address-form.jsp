<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.dto.CountryDTO"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= request.getAttribute("formTitle") != null ? request.getAttribute("formTitle") : "Địa chỉ" %></title>
</head>
<body>

<%
    String error = (String) request.getAttribute("error");
    if (error == null) error = "";

    List<CountryDTO> countries = (List<CountryDTO>) request.getAttribute("countries");
    if (countries == null) countries = java.util.Collections.emptyList();

    String action = (String) request.getAttribute("actionType"); // "addAddress" hoặc "editAddress"
    if (action == null) action = "addAddress";
    if(action.equals("editAddress")) action = "updateAddress";
    
    String addressId = request.getParameter("addressId");
    String inputUnitNumber = request.getParameter("unitNumber") != null ? request.getParameter("unitNumber") : "";
    String inputStreetNumber = request.getParameter("streetNumber") != null ? request.getParameter("streetNumber") : "";
    String inputAddress1 = request.getParameter("addressLine1") != null ? request.getParameter("addressLine1") : "";
    String inputAddress2 = request.getParameter("addressLine2") != null ? request.getParameter("addressLine2") : "";
    String inputCity = request.getParameter("city") != null ? request.getParameter("city") : "";
    String inputRegion = request.getParameter("region") != null ? request.getParameter("region") : "";
    String selectedCountryId = request.getParameter("countryId") != null ? request.getParameter("countryId") : "";
%>

<h1><%= request.getAttribute("formTitle") != null ? request.getAttribute("formTitle") : "Địa chỉ" %></h1>

<% if (!error.isEmpty()) { %>
    <p style="color:red;"><%= error %></p>
<% } %>

<form action="MainController" method="post">
    <input type="hidden" name="action" value="<%= action %>"/>
    <% if (addressId != null && !addressId.isEmpty()) { %>
        <input type="hidden" name="addressId" value="<%= addressId %>"/>
    <% } %>

    Quốc gia:
    <select name="countryId" required>
        <option value="">-- Chọn quốc gia --</option>
        <% for (CountryDTO country : countries) { 
            String selected = String.valueOf(country.getId()).equals(selectedCountryId) ? "selected" : "";
        %>
            <option value="<%= country.getId() %>" <%= selected %>><%= country.getCountry_name() %></option>
        <% } %>
    </select><br>

    Unit Number: <input type="text" name="unitNumber" value="<%= inputUnitNumber %>" maxlength="20"><br>
    Street Number: <input type="text" name="streetNumber" value="<%= inputStreetNumber %>" maxlength="20"><br>
    Địa chỉ 1: <input type="text" name="addressLine1" value="<%= inputAddress1 %>" required maxlength="255"><br>
    Địa chỉ 2: <input type="text" name="addressLine2" value="<%= inputAddress2 %>" maxlength="255"><br>
    Thành phố: <input type="text" name="city" value="<%= inputCity %>" required maxlength="100"><br>
    Vùng/Region: <input type="text" name="region" value="<%= inputRegion %>" maxlength="100"><br>

    <button type="submit"><%= action.equals("updateAddress") ? "Cập nhật" : "Thêm mới" %></button>
</form>

<br>
<form action="MainController" method="post">
    <input type="hidden" name="action" value="toManageAddresses"/>
    <button type="submit">Quay lại danh sách</button>
</form>

</body>
</html>
