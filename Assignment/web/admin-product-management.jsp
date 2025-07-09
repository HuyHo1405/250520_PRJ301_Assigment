<%-- 
    Document   : admin-product-management
    Created on : Jul 7, 2025, 4:43:54 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.dto.ProductDTO" %>
<%!
    public String convertDriveLink(String originalLink) {
        try {
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("/d/([a-zA-Z0-9_-]+)").matcher(originalLink);
            if (matcher.find()) {
                return "https://drive.google.com/thumbnail?id=" + matcher.group(1) + "&sz=w400";
            }
        } catch (Exception e) {
            return originalLink;
        }
        return originalLink;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý sản phẩm</title>
        <script>
            function extractDriveId(url) {
                const match = url.match(/\/d\/([^/]+)\//);
                return match ? match[1] : null;
            }

            function updateDriveImage(id) {
                const input = document.getElementById("coverImageInput" + id);
                const preview = document.getElementById("coverImagePreview" + id);
                const fileId = extractDriveId(input.value);
                if (fileId) {
                    preview.src = "https://drive.google.com/thumbnail?id=" + fileId + "&sz=w400";
                } else {
                    preview.src = input.value;
                }
            }
        </script>
    </head>
    <body>
        <h1>Quản lý sản phẩm</h1>

        <form action="AdminProductController" method="post">
            <input type="hidden" name="action" value="searchProducts"/>
            Tìm kiếm:
            <input type="text" name="keyword"
                   value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %>"
                   placeholder="Nhập tên sản phẩm..."/>
            <button type="submit">Tìm</button>
        </form>


        <%
            String editId = request.getParameter("editId");
            List<ProductDTO> productList = (List<ProductDTO>) request.getAttribute("productList");
        %>

        <% if (request.getAttribute("message") != null) { %>
        <p><b style="color:green;"><%= request.getAttribute("message") %></b></p>
            <% } %>

        <% if (request.getAttribute("errorMsg") != null) { %>
        <p><b style="color:red;"><%= request.getAttribute("errorMsg") %></b></p>
            <% } %>

        <table border="1">
            <tr>
                <th>ID</th>
                <th>Tên</th>
                <th>Mô tả</th>
                <th>Hình ảnh</th>
                <th>ID danh mục</th>
                <th>Hành động</th>
            </tr>

            <% if (productList != null && !productList.isEmpty()) {
                for (ProductDTO product : productList) {
                    boolean editable = String.valueOf(product.getId()).equals(editId);
            %>
            <tr>
                <td><%= product.getId() %></td>
                <% if (editable) { %>
                <td colspan="4"> <%-- Thay đổi colspan nếu cần để chứa đủ form --%>
                    <form action="AdminProductController" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="productId" value="<%= product.getId() %>"/>
                        <input type="hidden" name="action" value="updateProduct"/> <%-- Đảm bảo action đúng --%>

                        <table style="width:100%;">
                            <tr>
                                <td style="width:100px;">Tên:</td>
                                <td><input type="text" name="name" value="<%= product.getName() %>" required style="width:95%;"/></td>
                            </tr>
                            <tr>
                                <td>Mô tả:</td>
                                <td><input type="text" name="description" value="<%= product.getDescription() %>" style="width:95%;"/></td>
                            </tr>
                            <tr>
                                <td>Hình ảnh:</td>
                                <td>
                                    <input type="text" name="coverImageLink" id="coverImageInput<%= product.getId() %>"
                                       value="<%= product.getCover_image_link() %>" style="width:95%;"
                                       oninput="updateDriveImage(<%= product.getId() %>)"/>
                                    <br/>
                                    <img id="coverImagePreview<%= product.getId() %>"
                                         src="<%= convertDriveLink(product.getCover_image_link()) %>"
                                         width="120" alt="Ảnh sản phẩm"
                                         onerror="this.style.display='none';"/>
                                </td>
                            </tr>
                            <tr>
                                <td>ID danh mục:</td>
                                <td><input type="number" name="categoryId" value="<%= product.getCategory_id() %>" required/></td>
                            </tr>
                        </table>
                </td>
                <td>
                    <button type="submit">Cập nhật</button>
                    </form> <%-- THẺ ĐÓNG FORM ĐƯỢC DI CHUYỂN XUỐNG ĐÂY --%>

                    <form action="MainController" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="disableProduct"/>
                        <input type="hidden" name="productId" value="<%= product.getId() %>"/>
                        <button type="submit" onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">Xóa</button>
                    </form>
                </td>
                <% } else { %>
                <td><%= product.getName() %></td>
                <td><%= product.getDescription() %></td>
                <td><img src="<%= convertDriveLink(product.getCover_image_link()) %>" 
                         alt="image" width="80" onerror="this.style.display='none';"/></td>
                <td><%= product.getCategory_id() %></td>
                <td>
                    <form action="MainController" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="listAllProducts"/>
                        <input type="hidden" name="keyword" value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %>"/>
                        <button type="submit" name="editId" value="<%= product.getId() %>">Chỉnh sửa</button>
                    </form>
                </td>
                <% } %>
            </tr>
            <% } %>
            <% } else { %>
            <tr><td colspan="6">Không có sản phẩm nào.</td></tr>
            <% } %>
        </table>


        <div id="productAddRow"></div>
        <button id="productAddBtn" onclick="addProductRow()">Thêm sản phẩm mới</button>

        <br/><br/>
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toWelcome"/>
            <button type="submit">Trở về trang chính</button>
        </form>

        <script>
            function addProductRow() {
                const keywordInput = document.querySelector('input[name="keyword"]');
                const keywordValue = keywordInput ? keywordInput.value.replace(/"/g, '&quot;') : '';

                document.getElementById('productAddRow').innerHTML = `
                    <form action="MainController" method="post"> <%-- CHANGED ACTION --%>
                        <input type="hidden" name="action" value="createProduct"/>
                        <input type="hidden" name="keyword" value="${keywordValue}"/>
                        Tên: <input type="text" name="name" required/>
                        Mô tả: <input type="text" name="description" />
                        Hình ảnh: <input type="text" name="coverImageLink" required/>
                        ID danh mục: <input type="number" name="categoryId" required/>
                        <button type="submit">Tạo</button>
                    </form>
                `;
                document.getElementById('productAddBtn').style.display = 'none';
            }
        </script>

    </body>
</html>
