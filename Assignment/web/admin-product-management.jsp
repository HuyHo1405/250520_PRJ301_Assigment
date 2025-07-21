<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Management</title>
        <link rel="stylesheet" href="assets/css/address-management.css"/>
        </head>
    <body>
        <div class="main-container">
            <h1>Product Management</h1>
            <hr>

            <div class="top-actions">
                <input form="seacheProductWithCategory" type="text" name="keyword" value="${param.keyword}" placeholder="Search by product name"/>
                <select form="seacheProductWithCategory" name="searchCategoryId">
                    <option value="">-- Select Category --</option>
                    <c:forEach var="entry" items="${categoryMap}">
                        <option value="${entry.key}" ${entry.key == selectedCategory ? 'selected' : ''}>
                            ${entry.value}
                        </option>
                    </c:forEach>
                </select>
                <button form="seacheProductWithCategory" type="submit" name="action" value="searchProductsManagement">Search</button>

                <button form="toCreateProduct" type="submit" name="action" value="toCreateProduct">+ Create New Product</button>
            </div>

            <table>
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Product Name</th>
                        <th>Description</th>
                        <th>Image</th>
                        <th>Category</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
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
                                        <input type="hidden" name="searchCategoryId" value="${selectedCategory}"/>
                                        <button type="submit" name="action" value="viewProductDetail">View</button>
                                        <button type="submit" name="action" value="toAdminProductItemPage">Items Management</button>
                                        <button type="submit" name="action" value="toEditProduct">Edit</button>
                                        <button type="submit" name="action" value="toggleIsActiveProduct"
                                                onclick="return confirm('Are you sure you want to ${!product.is_active? 'activate': 'deactivate'} this product?');">
                                            ${!product.is_active? 'Activate': 'Deactivate'}
                                        </button>
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
                </tbody>
            </table>
        <hr>
        <div class="back-button-container">
            <button form="back" name="action" value="toWelcome">Back</button>
        </div>
        </div>


        <form id="toCreateProduct" action="MainController" method="POST"></form>
        <form id="seacheProductWithCategory" action="MainController" method="POST"></form>
        <form id="back" action="MainController" method="POST"></form>
    </body>
</html>