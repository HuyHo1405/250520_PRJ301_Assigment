package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import model.dao.CategoryDAO;
import model.dao.ProductDAO;


/**
 * MainController is the central servlet that routes incoming requests
 * based on the 'action' parameter to the appropriate sub-controllers.
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
@MultipartConfig
public class MainController extends HttpServlet {

    private static final String LOGIN_PAGE = "login.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String WELCOME_PAGE = "welcome.jsp";

    /** Actions related to address management */
    public static final List<String> ADDRESS_ACTIONS = Arrays.asList(
            "toProfile",
            "toAddressManagement",
            "toAddAddress",
            "toEditAddress",
            "addAddress",
            "searchAddress",
            "updateAddress",
            "updateDefaultAddress",
            "removeAddress"
    );

    /** Actions related to user account management */
    public static final List<String> USER_ACTIONS = Arrays.asList(
            "toLogin",
            "toRegister",
            "toForgotPassword",
            "toResetPassword",
            "toProfile",
            "register",
            "login",
            "logout",
            "forgotPassword",
            "resetPassword",
            "updateProfile"
    );

    /** Actions related to product browsing and filtering */
    public static final List<String> PRODUCT_ACTIONS = Arrays.asList(
            "listProducts",
            "viewProduct",
            "searchProducts",
            "categoryProducts",
            "featuredProducts",
            "newArrivals"
    );

    /** Actions related to shopping cart */
    public static final List<String> CART_ACTIONS = Arrays.asList(
            "toCart",
            "toCheckOut",
            "getCart",
            "addToCart",
            "updateCart",
            "removeFromCart",
            "clearCart",
            "checkoutCart"
    );

    /** Actions for user-side order handling */
    public static final List<String> ORDER_ACTIONS = Arrays.asList(
            "listMyOrders",
            "viewMyOrder",
            "placeOrder",
            "cancelOrder",
            "trackOrder"
    );

    /** Actions related to product reviews */
    public static final List<String> REVIEW_ACTIONS = Arrays.asList(
            "listReviewsByProduct",
            "submitReview",
            "updateReview",
            "deleteReview",
            "viewReview"
    );

    /** Actions related to admin order management */
    public static final List<String> ADMIN_ORDER_ACTIONS = Arrays.asList(
            "toAdminOrdersPage",
            "viewOrderDetail",
            "updateOrderStatus",
            "disableOrder",
            "searchOrders",
            "exportOrders"
    );

    /** Actions related to admin product management */
    public static final List<String> ADMIN_PRODUCT_ACTIONS = Arrays.asList(
            "toAdminProductPage",
            "toCreateProduct",
            "toEditProduct",
            "viewProductDetail",
            "createProduct",
            "updateProduct",
//            "uploadProductImages",
            "toggleIsActiveProduct",
            "searchProductsManagement"
    );
    
    /** Actions related to admin product item variations and options */
    public static final List<String> ADMIN_PRODUCT_ITEM_ACTIONS = Arrays.asList(
            "toAdminProductItemPage", 
            "updateProductItem", 
            "toggleIsActiveProductItem", 
            "addOption",
            "addVariation",
            "updateOption",
            "updateVariation",
            "removeOption",
            "removeVariation",
            "generateProductItemMatrix",
            "exportProductItemList"
    );
    
    /** Actions related to admin user management */
    public static final List<String> ADMIN_USER_ACTIONS = Arrays.asList(
            "toAdminUserPage",
            "createUser",
            "toggleIsActiveUser",
            "changeUserRole"
    );

    /** Actions related to system configuration management */
    public static final List<String> SYSTEM_CONFIG_ACTIONS = Arrays.asList(
            "toSystemConfigManagement",
            "getSystemConfig",
            "addSystemConfig",
            "updateSystemConfig",
            "toggleIsActiveSystemConfig",
            "clearSystemCache"
    );

    /** Actions that load the welcome page */
    public static final List<String> WELCOME_ACTIONS = Arrays.asList(
            "toWelcome",
            ""
    );

    private final ProductDAO PDAO = new ProductDAO();
    private final CategoryDAO CDAO = new CategoryDAO();
    
    /**
     * Central handler that delegates requests to corresponding controllers based on 'action' parameter.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = LOGIN_PAGE;

        try {
            String action = request.getParameter("action");
            System.out.println(action);
            if (action == null || action.trim().isEmpty()) {
                url = ERROR_PAGE;
            } else if (ADDRESS_ACTIONS.contains(action)) {
                url = "/AddressController";
            } else if (USER_ACTIONS.contains(action)) {
                url = "/UserController";
            } else if (PRODUCT_ACTIONS.contains(action)) {
                url = "/ProductController";
            } else if (CART_ACTIONS.contains(action)) {
                url = "/CartController";
            } else if (ORDER_ACTIONS.contains(action)) {
                url = "/OrderController";
            } else if (REVIEW_ACTIONS.contains(action)) {
                url = "/ReviewController";
            } else if (ADMIN_ORDER_ACTIONS.contains(action)) {
                url = "/AdminOrderController";
            } else if (ADMIN_PRODUCT_ACTIONS.contains(action)) {
                url = "/AdminProductController";
            } else if (ADMIN_PRODUCT_ITEM_ACTIONS.contains(action)) {
                url = "/AdminProductItemController";
            } else if (ADMIN_USER_ACTIONS.contains(action)) {
                url = "/AdminUserController";
            } else if (SYSTEM_CONFIG_ACTIONS.contains(action)) {
                url = "/SystemConfigController";
            } else if (WELCOME_ACTIONS.contains(action)) {
                request.setAttribute("productList", PDAO.retrieve("is_active = 1"));
                request.setAttribute("categoryList", CDAO.retrieve("is_active = 1"));
                url = WELCOME_PAGE;
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
        String contentType = request.getContentType();
        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            request.getRequestDispatcher("/AdminProductController").forward(request, response);
        } else {
            // Đây là request POST bình thường (ví dụ: application/x-www-form-urlencoded, application/json)
            processRequest(request, response);
        }
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