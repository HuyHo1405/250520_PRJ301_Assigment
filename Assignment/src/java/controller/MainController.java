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
 * Status: Hoàn thành Người thực hiện: Huy Ngày hoàn thành: 19/06/2025 Sửa lại
 * một số tên action để ko bị trùng lặp
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    private static final String LOGIN_PAGE = "login.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String WELCOME_PAGE = "welcome.jsp";

    public static final List<String> ADDRESS_ACTIONS = Arrays.asList(
            "toProfile",
            "toAddressManagement",
            "toAddAddress",
            "toEditAddress",
            "addAddress",
            "updateAddress",
            "updateDefaultAddress",
            "removeAddress"
    );

    // UserController.java (Dành cho người dùng cuối)
    public static final List<String> USER_ACTIONS = Arrays.asList(
            "toLogin",
            "toRegister",
            "toProfile",
            "register",
            "login",
            "logout",
            "changePassword",
            "updateProfile" // Đổi từ "update" → rõ nghĩa hơn
    );

    // ProductController.java (Dành cho người dùng cuối)
    public static final List<String> PRODUCT_ACTIONS = Arrays.asList(
            "listProducts",
            "viewProduct",
            "searchProducts",
            "categoryProducts",
            "featuredProducts",
            "newArrivals"
    );

    // CartController.java
    public static final List<String> CART_ACTIONS = Arrays.asList(
            "getCart",
            "addToCart",
            "updateCart",
            "removeFromCart",
            "clearCart",
            "checkoutCart"
    );

    // OrderController.java (Dành cho người dùng cuối)
    public static final List<String> ORDER_ACTIONS = Arrays.asList(
            "listMyOrders",
            "viewMyOrder",
            "placeOrder",
            "cancelOrder",
            "trackOrder"
    );

    // ReviewController.java
    public static final List<String> REVIEW_ACTIONS = Arrays.asList(
            "listReviewsByProduct",
            "submitReview",
            "updateReview",
            "deleteReview",
            "viewReview"
    );

    // AdminOrderController.java
    public static final List<String> ADMIN_ORDER_ACTIONS = Arrays.asList(
            "toAdminOrdersPage",
            "viewOrderDetail",
            "updateOrderStatus",
            "disableOrder", // Thay delete → rõ nghĩa hơn (soft delete)
            "searchOrders",
            "exportOrders"
    );

    // AdminProductController.java
    public static final List<String> ADMIN_PRODUCT_ACTIONS = Arrays.asList(
            "listAllProducts",
            "viewProductDetail",
            "createProduct",
            "updateProduct",
            "disableProduct",
            "uploadProductImages",
            "searchProducts"
    );

    // AdminUserController.java
    public static final List<String> ADMIN_USER_ACTIONS = Arrays.asList(
            "toAdminUserPage",
            "createUser",
//            "updateUser",
            "toggleIsActiveUser",
            "changeUserRole"
//            "toResetPassword",
//            "forgotPassword",
//            "resetPassword",
//            "toForgotPassword"
    );

    // SystemConfigController.java (Admin)
    public static final List<String> SYSTEM_CONFIG_ACTIONS = Arrays.asList(
            "toSystemConfigManagement",
            "getSystemConfig",
            "addSystemConfig",
            "updateSystemConfig",
            "toggleIsActiveSystemConfig",
            "clearSystemCache"
    );

    //
    public static final List<String> WELCOME_ACTIONS = Arrays.asList(
            "toWelcome",
            ""
    );

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = LOGIN_PAGE;

        try {
            String action = request.getParameter("action");

            if (action == null || action.trim().isEmpty()) {
                url = ERROR_PAGE;

            } else if (ADDRESS_ACTIONS.contains(action)) {
                url = "/AddressController"; // AddressController.java

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

            } else if (WELCOME_ACTIONS.contains(action)) {
                url = WELCOME_PAGE; // SystemConfigController.java

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
