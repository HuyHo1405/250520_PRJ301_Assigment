<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Product Management</h1>
        <hr>

        <input form="seacheProductWithCategory" type="text" name="keyword" value="${param.keyword}"/>
        <select form="seacheProductWithCategory" name="searchCategoryId">
            <option value="">-- Select Category --</option>
            <c:forEach var="entry" items="${categoryMap}">
                <option value="${entry.key}" ${entry.key == selectedCategory ? 'selected' : ''}>
                    ${entry.value}
                </option>
            </c:forEach>
        </select>
        <button form="seacheProductWithCategory" name="action" value="searchProductsManagement">Search</button>

        <button form="toCreateProduct" name="action" value="toCreateProduct">+ Create New Product</button>

        <table border="1" style="border-collapse:collapse;">
            <tr>
                <th>Id</th>
                <th>Product Name</th>
                <th>Description</th>
                <th>Image</th>
                <th>Category</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
            <c:if test="${not empty productList}">
                <c:forEach var="product" items="${productList}">
                    <tr>
                        <td>${product.id}</td>
                        <td>${product.name}</td>
                        <td>${product.description}</td>
                        <td>
                            <c:if test="${not empty product.cover_image_link}">
                                <img src="${product.cover_image_link}" alt="${product.name}" style="width: 50px; height: 50px; object-fit: cover;"/>
                            </c:if>
                            <c:if test="${empty product.cover_image_link}">
                                No Image
                            </c:if>
                        </td>
                        <td>
                            ${categoryMap[product.category_id]}
                        </td>
                        <td>
                            ${product.is_active? 'Active': 'Not Active'}
                        </td>
                        <td>
                            <form action="MainController" method="POST">
                                <input type="hidden" name="productId" value="${product.id}"/>
                                <input type="hidden" name="keyword" value="${param.keyword}"/>
                                <button type="submit" name="action" value="toEditProduct">Edit</button>
                                <button type="submit" name="action" value="viewProductDetail">View</button>
                                <button type="submit" name="action" value="toggleIsActiveProduct">${!product.is_active? 'Activate': 'Deactivate'}</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            <c:if test="${empty productList}">
                <tr>
                    <td colspan="7">No products found.</td>
                </tr>
            </c:if>
        </table>

        <hr>
        <button form="back" name="action" value="toSystemConfigManagement">Back</button>

        <form id="toCreateProduct" action="MainController" method="POST"></form>
        <form id="seacheProductWithCategory" action="MainController" method="POST"></form>
        <form id="back" action="MainController" method="POST"></form>
    </body>
</html>