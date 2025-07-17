<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý địa chỉ</title>
    <style>
        table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        th, td { padding: 8px; border: 1px solid #ccc; }
        th { background-color: #f0f0f0; }
        .error { color: red; margin: 10px 0; }
        .top-actions { display: flex; align-items: center; gap: 10px; margin-bottom: 10px; }
    </style>
</head>
<body>

<h1>Quản lý địa chỉ</h1>
<hr>

<div class="top-actions">
    <form action="MainController" method="post">
        <input type="hidden" name="action" value="searchAddress" />
        <input type="text" name="strKeyword" placeholder="Tìm theo thành phố hoặc vùng..." value="${param.strKeyword}" />
        <button type="submit">Tìm kiếm</button>
    </form>

    <form action="MainController" method="post">
        <input type="hidden" name="action" value="toAddAddress" />
        <button type="submit">Thêm địa chỉ mới</button>
    </form>
</div>

<c:if test="${not empty error}">
    <p class="error">${error}</p>
</c:if>

<table>
    <tr>
        <th>Address</th>
        <th>City</th>
        <th>Region</th>
        <th>Action</th>
    </tr>
    <c:forEach var="addr" items="${addressList}">
        <tr <c:if test="${addr.id != defaultAddressId}">style="font-weight:bold;"</c:if>>
            <td>${addr.fullAddress} ${addr.id != defaultAddressId? '': '(default)'}</td>
            <td>${addr.city}</td>
            <td>${addr.region}</td>
            <td>
                <form action="MainController" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="toEditAddress"/>
                    <input type="hidden" name="addressId" value="${addr.id}"/>
                    <button type="submit">Sửa</button>
                </form>

                <c:if test="${addr.id != defaultAddressId}">
                    <form action="MainController" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="updateDefaultAddress"/>
                        <input type="hidden" name="addressId" value="${addr.id}"/>
                        <button type="submit">Đặt làm mặc định</button>
                    </form>

                    <form action="MainController" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="removeAddress"/>
                        <input type="hidden" name="addressId" value="${addr.id}"/>
                        <button type="submit" onclick="return confirm('Xóa địa chỉ này?')">Xóa</button>
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>

<hr>

<form action="MainController" method="post" style="display:inline;">
    <input type="hidden" name="action" value="toWelcome"/>
    <button type="submit">Quay lại hồ sơ</button>
</form>

</body>
</html>
