<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang Sản Phẩm</title>
    <link rel="stylesheet" href="assets/css/welcome.css">
</head>
<body>
    <div class="dashboard-wrapper">
        <jsp:include page="assets/components/sidebar.jsp" />

        <div class="main-content">

            <!-- Search Form -->
            <div class="search-section">
                <form action="MainController" method="get">
                    <input type="hidden" name="action" value="searchProducts" />

                    <input type="text" name="keyword" placeholder="Tìm kiếm sản phẩm..."
                           value="${param.keyword}" />

                    <select name="categoryId">
                        <option value="">Tất cả danh mục</option>
                        <c:forEach var="category" items="${categoryList}">
                            <option value="${category.id}"
                                    <c:if test="${param.categoryId == category.id}">selected</c:if>>
                                ${category.name}
                            </option>
                        </c:forEach>
                    </select>

                    <button type="submit">Tìm kiếm</button>
                </form>
            </div>

            <!-- Product Grid -->
            <div class="products-grid">
                <c:if test="${empty productList}">
                    <p class="no-products-message">Không tìm thấy sản phẩm nào.</p>
                </c:if>

                <c:forEach var="product" items="${productList}">
                    <div class="product-card">
                        <img src="<c:url value='${product.cover_image_link != null ? product.cover_image_link : "/assets/images/placeholder.png"}' />"
                             alt="${product.name}" class="product-image" />

                        <div class="product-info">
                            <h3 class="product-name">${product.name}</h3>


                            <p class="product-description">
                                ${fn:substring(product.description, 0, 100)}...
                            </p>

                            <form action="MainController" method="get">
                                <input type="hidden" name="action" value="viewProduct" />
                                <input type="hidden" name="productId" value="${product.id}" />
                                <button type="submit" class="view-details-button">Xem chi tiết</button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</body>
</html>