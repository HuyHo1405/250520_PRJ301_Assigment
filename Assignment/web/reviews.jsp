<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.dto.ReviewDTO" %>
<%@ page import="model.dao.UserDAO" %>
<%@ page import="model.dto.UserDTO" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="assets/css/review.css">
    </head>
    <body>
        <%
            List<ReviewDTO> reviews = (List<ReviewDTO>) request.getAttribute("reviews");
            int orderedProductId = Integer.parseInt(request.getParameter("orderedProductId"));

            UserDAO udao = new UserDAO();

            UserDTO currentUser = (UserDTO) session.getAttribute("user");
            Integer userId = (currentUser != null) ? currentUser.getId() : null;
        %>
        <div id="layout">

            <div id="sidebar"></div>

            <div id="content">
                <div id="header-wrapper">
                    <jsp:include page="assets/components/header.jsp" />
                </div>

                <div id="main-wrapper">
                    <button id="toggle-btn" class="toggle-btn" onclick="toggleMenu()">Show Menu</button>
                    <div class="review-section">
                        <h2>Đánh giá sản phẩm</h2>

                        <div class="review-list">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Người đánh giá</th>
                                        <th>Điểm</th>
                                        <th>Bình luận</th>
                                        <th>Hành động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        if (reviews == null || reviews.isEmpty()) {
                                    %>
                                    <tr>
                                        <td colspan="4">Chưa có đánh giá nào cho sản phẩm này.</td>
                                    </tr>
                                    <%
                                        } else {
                                            for (ReviewDTO r : reviews) {
                                                UserDTO user = udao.findById(r.getUser_id());
                                    %>
                                    <tr>
                                        <td><%= user != null ? user.getEmail_address() : "Ẩn danh" %></td>
                                        <td><%= r.getRating_value() %> / 5</td>
                                        <td><%= r.getComment() %></td>
                                        <td>
                                            <% if (userId != null && userId == r.getUser_id()) { %>
                                            <div class="review-actions">
                                                <!-- Form chỉnh sửa -->
                                                <form action="ReviewController" method="post" class="inline-form">
                                                    <input type="hidden" name="action" value="updateReview" />
                                                    <input type="hidden" name="id" value="<%= r.getId() %>" />
                                                    <input type="hidden" name="userId" value="<%= r.getUser_id() %>" />
                                                    <input type="hidden" name="orderedProductId" value="<%= r.getOrdered_product_id() %>" />

                                                    <input type="number" name="rating" min="1" max="5" value="<%= r.getRating_value() %>" required />
                                                    <input type="text" name="comment" value="<%= r.getComment() %>" required />
                                                    <input type="submit" value="Cập nhật" />
                                                </form>

                                                <!-- Form xóa -->
                                                <form action="ReviewController" method="post" onsubmit="return confirm('Xác nhận xóa?');" class="inline-form">
                                                    <input type="hidden" name="action" value="deleteReview" />
                                                    <input type="hidden" name="id" value="<%= r.getId() %>" />
                                                    <input type="hidden" name="orderedProductId" value="<%= r.getOrdered_product_id() %>" />
                                                    <input type="submit" value="Xóa" />
                                                </form>
                                            </div>
                                            <% } %>
                                        </td>
                                    </tr>
                                    <%
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>

                        <div class="review-form-section">
                            <h3>Thêm đánh giá mới</h3>
                            <%
                                if (userId != null) {
                            %>
                            <form action="ReviewController" method="post" class="review-form">
                                <input type="hidden" name="action" value="submitReview" />
                                <input type="hidden" name="userId" value="<%= userId %>" />
                                <input type="hidden" name="orderedProductId" value="<%= orderedProductId %>" />

                                <label>Điểm (1-5):</label>
                                <input type="number" name="rating" min="1" max="5" required />

                                <label>Bình luận:</label>
                                <input type="text" name="comment" required />

                                <input type="submit" value="Gửi đánh giá" />
                            </form>
                            <%
                                } else {
                            %>
                            <p><a href="login.jsp">Đăng nhập</a> để gửi đánh giá.</p>
                            <%
                                }
                            %>
                        </div>

                        <div class="back-link">
                            <a href="welcome.jsp">Back</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="assets/js/menu.js"></script>
    </body>
</html>
