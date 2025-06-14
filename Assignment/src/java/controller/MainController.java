package controller;

import java.io.IOException;
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
 * Ngày hoàn thành: 10/06/2025
 * Update thêm các Controllers khác.
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    private static final String LOGIN_PAGE = "login.jsp";
    private static final String ERROR_PAGE = "error.jsp";

    // UserController.java (Dành cho người dùng cuối)
    public static final List<String> USER_ACTIONS = Arrays.asList(
        "toLogin",           // Chuyển đến trang đăng nhập
        "toRegister",        // Chuyển đến trang đăng ký
        "toProfile",         // Chuyển đến trang hồ sơ người dùng
        "toForgotPassword",  // Chuyển đến trang quên mật khẩu
        "register",          // Xử lý đăng ký tài khoản
        "login",             // Xử lý đăng nhập
        "logout",            // Xử lý đăng xuất
        "changePassword",    // Xử lý đổi mật khẩu
        "update"             // Xử lý cập nhật thông tin hồ sơ
    );

    // ProductController.java (Dành cho người dùng cuối)
    public static final List<String> PRODUCT_ACTIONS = Arrays.asList(
            "list", // Hiển thị tất cả sản phẩm
            "view", // Xem chi tiết sản phẩm
            "search", // Tìm kiếm theo từ khoá
            "category", // Lọc theo danh mục
            "featured", // Xem sản phẩm nổi bật
            "newArrivals" // Xem sản phẩm mới về
    );

    // CartController.java
    public static final List<String> CART_ACTIONS = Arrays.asList(
            "get", // Xem giỏ hàng
            "add", // Thêm sản phẩm vào giỏ
            "update", // Cập nhật số lượng sản phẩm trong giỏ
            "remove", // Xóa sản phẩm khỏi giỏ
            "clear", // Xóa toàn bộ giỏ hàng
            "checkout" // Tiến hành thanh toán
    );

    // OrderController.java (Dành cho người dùng cuối)
    public static final List<String> ORDER_ACTIONS = Arrays.asList(
            "list", // Danh sách đơn hàng của người dùng
            "view", // Xem chi tiết đơn hàng
            "place", // Đặt đơn hàng mới
            "cancel", // Hủy đơn hàng
            "track" // Theo dõi trạng thái đơn hàng
    );

    // ReviewController.java
    public static final List<String> REVIEW_ACTIONS = Arrays.asList(
            "listByProduct", // Liệt kê đánh giá theo sản phẩm
            "submit", // Gửi đánh giá mới
            "update", // Cập nhật đánh giá
            "delete", // Xóa đánh giá
            "view" // Xem chi tiết một đánh giá
    );

    // AdminOrderController.java
    public static final List<String> ADMIN_ORDER_ACTIONS = Arrays.asList(
            "list", // Liệt kê tất cả đơn hàng
            "view", // Xem chi tiết đơn hàng
            "updateStatus", // Cập nhật trạng thái đơn hàng
            "delete", // Xóa đơn hàng (cân nhắc thay bằng vô hiệu hóa)
            "search", // Tìm kiếm đơn hàng
            "export" // Xuất dữ liệu đơn hàng
    );

    // AdminProductController.java
    public static final List<String> ADMIN_PRODUCT_ACTIONS = Arrays.asList(
            "list", // Liệt kê tất cả sản phẩm
            "view", // Xem chi tiết sản phẩm
            "create", // Tạo sản phẩm mới
            "update", // Cập nhật thông tin sản phẩm
            "delete", // Xóa sản phẩm
            "uploadImages", // Tải ảnh sản phẩm
            "search" // Tìm kiếm sản phẩm
    );

    // AdminUserController.java
    public static final List<String> ADMIN_USER_ACTIONS = Arrays.asList(
            "list", // Liệt kê tất cả người dùng
            "view", // Xem chi tiết người dùng
            "create", // Tạo người dùng mới
            "update", // Cập nhật thông tin người dùng
            "delete", // Xóa người dùng
            "changeRole", // Thay đổi vai trò người dùng
            "resetPassword" // Đặt lại mật khẩu người dùng
    );

    // SystemConfigController.java (Admin)
    public static final List<String> SYSTEM_CONFIG_ACTIONS = Arrays.asList(
            "get", // Lấy cấu hình hệ thống
            "update", // Cập nhật cấu hình hệ thống
            "version", // Lấy phiên bản ứng dụng
            "clearCache" // Xóa bộ nhớ cache
    );

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = LOGIN_PAGE;

        try {
            String action = request.getParameter("action");

            if (action == null || action.trim().isEmpty()) {
                url = ERROR_PAGE;

            } else if (USER_ACTIONS.contains(action)) {
                url = "/UserController"; // UserController.java

            } else if (PRODUCT_ACTIONS.contains(action)) {
                url = "/ProductController"; // ProductController.java

            } else if (CART_ACTIONS.contains(action)) {
                url = "/CartController"; // CartController.java

            } else if (ORDER_ACTIONS.contains(action)) {
                url = "/OrderController"; // OrderController.java

            } else if (REVIEW_ACTIONS.contains(action)) {
                url = "/ReviewController"; // ReviewController.java

            } else if (ADMIN_ORDER_ACTIONS.contains(action)) {
                url = "/AdminOrderController"; // AdminOrderController.java

            } else if (ADMIN_PRODUCT_ACTIONS.contains(action)) {
                url = "/AdminProductController"; // AdminProductController.java

            } else if (ADMIN_USER_ACTIONS.contains(action)) {
                url = "/AdminUserController"; // AdminUserController.java

            } else if (SYSTEM_CONFIG_ACTIONS.contains(action)) {
                url = "/SystemConfigController"; // SystemConfigController.java

            } else {
                url = ERROR_PAGE;
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
