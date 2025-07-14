<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            <c:choose>
                <c:when test="${actionType eq 'createProduct'}">Create Product</c:when>
                <c:when test="${actionType eq 'updateProduct'}">Edit Product</c:when>
                <c:otherwise>View Product</c:otherwise>
            </c:choose>
        </title>
    </head>
    <body>
        <h1>
            <c:choose>
                <c:when test="${actionType eq 'createProduct'}">Create New Product</c:when>
                <c:when test="${actionType eq 'updateProduct'}">Edit Product: ${product.name}</c:when>
                <c:otherwise>View Product: ${product.name}</c:otherwise>
            </c:choose>
        </h1>
        <hr>

        <form action="MainController" method="POST">
            <c:if test="${actionType eq 'updateProduct' or actionType eq 'viewProductDetail'}">
                <input type="hidden" name="productId" value="${product.id}"/>
            </c:if>

            <div>
                <label for="productName">Product Name:</label>
                <input type="text" id="productName" name="name" value="${product.name}" required 
                       ${actionType eq 'viewProductDetail' ? 'readonly' : ''}/>
            </div>
            <br>

            <div>
                <label for="description">Description:</label>
                <textarea id="description" name="description" 
                          ${actionType eq 'viewProductDetail' ? 'readonly' : ''}>${product.description}</textarea>
            </div>
            <br>

            <div>
                <label for="coverImageLink">Cover Image URL:</label>
                <input type="text" id="coverImageLink" name="coverImageLink"
                       value="${product.cover_image_link}"
                       readonly />

                <br>
                <input type="file" id="imageFileInput" accept="image/*"
                       ${actionType eq 'viewProductDetail' ? 'disabled' : ''}/>

                <c:if test="${not empty product.cover_image_link}">
                    <br><img id="coverPreview" src="${product.cover_image_link}" alt="Current Image"
                             style="max-width: 150px; display: block; margin-bottom: 10px;" />
                </c:if>
            </div>
            <br>

            <div>
                <label for="category">Category:</label>
                <c:choose>
                    <c:when test="${actionType eq 'viewProductDetail'}">
                        <div>${categoryMap[product.category_id]}</div>
                    </c:when>
                    <c:otherwise>
                        <select id="category" name="categoryId" required>
                            <option value="">-- Select Category --</option>
                            <c:forEach var="entry" items="${categoryMap}">
                                <option value="${entry.key}" 
                                        ${entry.key == product.category_id ? 'selected' : ''}>
                                    ${entry.value}
                                </option>
                            </c:forEach>
                        </select>
                    </c:otherwise>
                </c:choose>
            </div>
            <br>

            <div>
                <c:choose>
                    <c:when test="${actionType eq 'createProduct'}">
                        <button type="submit" name="action" value="createProduct">Create Product</button>
                        <button type="reset">Reset</button>
                    </c:when>
                    <c:when test="${actionType eq 'updateProduct'}">
                        <button type="submit" name="action" value="updateProduct">Update Product</button>
                        <button type="reset">Reset</button>
                    </c:when>
                    <c:otherwise> <%-- viewProductDetail --%>
                        <button type="submit" name="action" value="toEditProduct">Edit Product</button>
                    </c:otherwise>
                </c:choose>
                <button form="back" type="submit" name="action" value="toAdminProductPage">Back to List</button>
            </div>
        </form>
                <form id="back" action="MainController" method="post"></form>

        <c:if test="${actionType eq 'viewProductDetail' and not empty variation}">
            <hr>
            <h2>Product Variations & Options</h2>
            <c:forEach var="variation" items="${variation}">
                <div>
                    <h3>-- ${variation.name} --</h3>
                    <ul>
                        <c:forEach var="option" items="${variationOption}">
                            <c:if test="${option.variation_id == variation.id}">
                                <li>${option.value}</li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </div>
                <br>
            </c:forEach>
            <div>
                <form action="MainController" method="POST">
                    <input type="hidden" name="productId" value="${product.id}"/>
                    <button type="submit" name="action" value="toManageProductVariations">
                        Manage Variations & Options for this Product
                    </button>
                </form>
            </div>
        </c:if>
    
    <script>
        console.log("JS loaded");
document.getElementById("imageFileInput").addEventListener("change", function () {
    console.log("File chọn: ", this.files[0]);
    const file = this.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("imageFile", file);
    formData.append("action", "uploadProductImages");

    fetch("MainController", {
        method: "POST",
        body: formData
    })
    .then(res => res.text())
    .then(url => {
        document.getElementById("coverImageLink").value = url;
    })
    .catch(err => {
        alert("Upload thất bại: " + err);
    });
});
</script>
    </body>
</html>
