<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
       <link rel="stylesheet" href="assets/css/product-form.css">
        </head>
    <body>
        
        <div class="dashboard-wrapper">
        <jsp:include page="assets/components/sidebar.jsp" />

        <div class="main-content">
            <h1>
                <c:choose>
                    <c:when test="${actionType eq 'createProduct'}">Create New Product</c:when>
                    <c:when test="${actionType eq 'updateProduct'}">Edit Product: ${product.name}</c:when>
                    <c:otherwise>View Product: ${product.name}</c:otherwise>
                </c:choose>
            </h1>
            <hr>

            <form action="MainController" method="POST" class="product-form">
                <c:if test="${actionType eq 'updateProduct' or actionType eq 'viewProductDetail'}">
                    <input type="hidden" name="productId" value="${product.id}"/>
                </c:if>

                <div class="form-group">
                    <label for="productName">Product Name:</label>
                    <input type="text" id="productName" name="name" value="${product.name}" required
                           class="input-field" ${actionType eq 'viewProductDetail' ? 'readonly' : ''}/>
                </div>
                <br>

                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea id="description" name="description" class="textarea-field"
                              ${actionType eq 'viewProductDetail' ? 'readonly' : ''}>${product.description}</textarea>
                </div>
                <br>

                <div class="form-group">
                    <label for="coverImageLink">Cover Image URL:</label>
                    <input type="text" id="coverImageLink" name="coverImageLink"
                           value="${product.cover_image_link}"
                           readonly class="input-field input-readonly"/>

                    <br>
                    <input type="file" id="imageFileInput" accept="image/*" class="file-input"
                           ${actionType eq 'viewProductDetail' ? 'disabled' : ''}/>

                    <c:if test="${not empty product.cover_image_link}">
                        <br><img id="coverPreview" src="${product.cover_image_link}" alt="Current Image"
                                 class="product-image-preview" />
                    </c:if>
                </div>
                <br>

                <div class="form-group">
                    <label for="category">Category:</label>
                    <c:choose>
                        <c:when test="${actionType eq 'viewProductDetail'}">
                            <div class="display-value">${categoryMap[product.category_id]}</div>
                        </c:when>
                        <c:otherwise>
                            <select id="category" name="categoryId" required class="select-field">
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

                <div class="form-actions">
                    <c:choose>
                        <c:when test="${actionType eq 'createProduct'}">
                            <button type="submit" name="action" value="createProduct" class="button-primary">Create Product</button>
                            <button type="reset" class="button-secondary">Reset</button>
                        </c:when>
                        <c:when test="${actionType eq 'updateProduct'}">
                            <button type="submit" name="action" value="updateProduct" class="button-primary">Update Product</button>
                            <button type="reset" class="button-secondary">Reset</button>
                        </c:when>
                        <c:otherwise> <%-- viewProductDetail --%>
                            <button type="submit" name="action" value="toEditProduct" class="button-primary">Edit Product</button>
                        </c:otherwise>
                    </c:choose>
                    <button form="back" type="submit" name="action" value="toAdminProductPage" class="button-secondary">Back to List</button>
                </div>
            </form>
            <form id="back" action="MainController" method="post"></form>

            <c:if test="${actionType eq 'viewProductDetail' and not empty variation}">
                <hr>
                <h2>Product Variations & Options</h2>
                <div class="variations-container">
                    <c:forEach var="variation" items="${variation}">
                        <div class="variation-display-card">
                            <h3>-- ${variation.name} --</h3>
                            <ul class="option-list">
                                <c:forEach var="option" items="${variationOption}">
                                    <c:if test="${option.variation_id == variation.id}">
                                        <li>${option.value}</li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:forEach>
                </div>
                <div class="form-actions">
                    <form action="MainController" method="POST" style="display:inline;">
                        <input type="hidden" name="productId" value="${product.id}"/>
                        <button type="submit" name="action" value="toAdminProductItemPage" class="button-primary">
                            Manage Variations & Options for this Product
                        </button>
                    </form>
                </div>
            </c:if>
        </div>
        </div>
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
                    // Optional: Update image preview
                    const preview = document.getElementById("coverPreview");
                    if (preview) {
                        preview.src = url;
                    } else {
                        // If no preview exists, create one
                        const img = document.createElement('img');
                        img.id = 'coverPreview';
                        img.src = url;
                        img.alt = 'Uploaded Image';
                        img.className = 'product-image-preview'; // Apply class
                        document.getElementById('imageFileInput').after(img); // Place after file input
                    }
                })
                .catch(err => {
                    alert("Upload thất bại: " + err);
                });
            });
        </script>
    </body>
</html>