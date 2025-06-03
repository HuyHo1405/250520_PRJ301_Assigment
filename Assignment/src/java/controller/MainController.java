package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Status: Hoàn thành
 * Người thực hiện: Huy
 * Ngày hoàn thành: 31/05/2025
 * Phần MainController đã ổn định, có thể chỉnh sửa thêm tính năng nếu yêu cầu bài cần.
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    private static final String LOGIN_PAGE = "login.jsp";
    private static final String ERROR_PAGE = "error.jsp";

    public static final List<String> PRODUCT_ACTIONS = Arrays.asList(
            "list", // Hiển thị tất cả sản phẩm
            "view", // Xem chi tiết sản phẩm
            "search", // Tìm kiếm theo từ khoá
            "category" // Lọc theo danh mục
    );

    public static final List<String> CART_ACTIONS = Arrays.asList(
            "view", // Xem giỏ hàng
            "add", // Thêm sản phẩm vào giỏ
            "update", // Cập nhật số lượng
            "remove", // Xoá sản phẩm khỏi giỏ
            "clear" // Xoá toàn bộ giỏ
    );

    public static final List<String> USER_ACTIONS = Arrays.asList(
            "loginForm", // Hiển thị form đăng nhập
            "login", // Xử lý đăng nhập
            "registerForm", // Hiển thị form đăng ký
            "register", // Xử lý đăng ký
            "logout", // Đăng xuất
            "profile", // Xem hồ sơ cá nhân
            "updateProfile", // Cập nhật thông tin
            "changePassword" // Đổi mật khẩu
    );

    public static final List<String> ORDER_ACTIONS = Arrays.asList(
            "checkoutForm", // Trang chọn thanh toán
            "checkout", // Xác nhận đặt hàng
            "myOrders", // Xem đơn hàng của tôi
            "view", // Xem chi tiết đơn hàng
            "cancel", // Hủy đơn hàng
            "status" // Trạng thái đơn hàng
    );

    public static final List<String> REVIEW_ACTIONS = Arrays.asList(
            "form", // Hiển thị form đánh giá
            "submit", // Gửi đánh giá
            "product" // Xem đánh giá của sản phẩm
    );

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = LOGIN_PAGE;

        try {
            String action = request.getParameter("action");

            if (action == null) {
                url = ERROR_PAGE;

            } else if (PRODUCT_ACTIONS.contains(action)) {
                url = "/product"; // ProductServlet

            } else if (CART_ACTIONS.contains(action)) {
                url = "/cart"; // CartServlet

            } else if (USER_ACTIONS.contains(action)) {
                url = "/user"; // UserServlet

            } else if (ORDER_ACTIONS.contains(action)) {
                url = "/order"; // OrderServlet

            } else if (REVIEW_ACTIONS.contains(action)) {
                url = "/review"; // ReviewServlet

            } else {
                url = ERROR_PAGE; // chuyển về error.jsp
            }

        } catch (Exception e) {
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
