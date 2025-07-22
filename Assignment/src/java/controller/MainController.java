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
 * The `MainController` acts as a central dispatcher for all incoming web requests.
 * It determines which specific controller should handle a request based on the
 * "action" parameter in the URL. It also defines lists of actions grouped by
 * their functional areas (e.g., user management, product handling, cart operations).
 * The `@MultipartConfig` annotation is used to enable handling of file uploads,
 * particularly for product image uploads.
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
@MultipartConfig // Enables multipart form data handling for file uploads
public class MainController extends HttpServlet {

    private static final String LOGIN_PAGE = "login.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String WELCOME_PAGE = "welcome.jsp";

    // Lists of actions, categorised by the controller responsible for handling them.
    // These lists help the MainController to route requests efficiently.

    /**
     * Actions related to user addresses.
     */
    public static final List<String> ADDRESS_ACTIONS = Arrays.asList(
            "toProfile", // Often includes address management
            "toAddressManagement",
            "toAddAddress",
            "toEditAddress",
            "addAddress",
            "searchAddress",
            "updateAddress",
            "updateDefaultAddress",
            "removeAddress"
    );

    /**
     * Actions related to user authentication and profile management.
     */
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

    /**
     * Actions for public product Browse and viewing.
     */
    public static final List<String> PRODUCT_ACTIONS = Arrays.asList(
            "listProducts",
            "viewProduct",
            "searchProducts",
            "categoryProducts",
            "featuredProducts",
            "newArrivals"
    );

    /**
     * Actions related to the shopping cart.
     */
    public static final List<String> CART_ACTIONS = Arrays.asList(
            "toCart",
            "toCheckOut",
            "getCart", // Redundant if "toCart" serves this purpose
            "addToCart",
            "updateCart",
            "removeFromCart",
            "clearCart",
            "checkoutCart"
    );

    /**
     * Actions related to user's personal orders.
     */
    public static final List<String> ORDER_ACTIONS = Arrays.asList(
            "listMyOrders",
            "viewMyOrder",
            "placeOrder", // This might be handled by CartController's checkoutCart
            "cancelOrder",
            "trackOrder"
    );

    /**
     * Actions related to product reviews.
     */
    public static final List<String> REVIEW_ACTIONS = Arrays.asList(
            "listReviewsByProduct",
            "submitReview",
            "updateReview",
            "deleteReview",
            "viewReview"
    );

    /**
     * Actions for administrator management of orders.
     */
    public static final List<String> ADMIN_ORDER_ACTIONS = Arrays.asList(
            "toAdminOrdersPage",
            "viewOrderDetail",
            "updateOrderStatus",
            "disableOrder", // Soft delete or deactivate order
            "searchOrders",
            "exportOrders"
    );

    /**
     * Actions for administrator management of products (main product entries).
     */
    public static final List<String> ADMIN_PRODUCT_ACTIONS = Arrays.asList(
            "toAdminProductPage",
            "toCreateProduct",
            "toEditProduct",
            "viewProductDetail", // Admin view of product detail
            "createProduct",
            "updateProduct",
            // "uploadProductImages", // Handled internally by create/update product or a separate utility
            "toggleIsActiveProduct", // Activate/deactivate a product
            "searchProductsManagement"
    );
    
    /**
     * Actions for administrator management of product items (variants, SKUs).
     */
    public static final List<String> ADMIN_PRODUCT_ITEM_ACTIONS = Arrays.asList(
            "toAdminProductItemPage", 
            "updateProductItem", 
            "toggleIsActiveProductItem", 
            "addOption",       // For product options (e.g., Color, Size)
            "addVariation",    // For specific variations (e.g., Red-Large)
            "updateOption",
            "updateVariation",
            "removeOption",
            "removeVariation",
            "generateProductItemMatrix", // Potentially for generating all combinations of options
            "exportProductItemList"
    );
    
    /**
     * Actions for administrator management of users.
     */
    public static final List<String> ADMIN_USER_ACTIONS = Arrays.asList(
            "toAdminUserPage",
            "createUser",
            "toggleIsActiveUser",
            "changeUserRole"
    );

    /**
     * Actions for system configuration and cache management.
     */
    public static final List<String> SYSTEM_CONFIG_ACTIONS = Arrays.asList(
            "toSystemConfigManagement",
            "getSystemConfig",
            "addSystemConfig",
            "updateSystemConfig",
            "toggleIsActiveSystemConfig",
            "clearSystemCache"
    );

    /**
     * Actions leading to the welcome or home page.
     */
    public static final List<String> WELCOME_ACTIONS = Arrays.asList(
            "toWelcome",
            "" // Default action if no "action" parameter is provided
    );

    // Data Access Objects (DAOs) used by the MainController, typically for common data needed on welcome/home pages.
    private final ProductDAO PDAO = new ProductDAO();
    private final CategoryDAO CDAO = new CategoryDAO();
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * This method acts as the main dispatcher, routing incoming requests to the
     * appropriate specialized controller based on the "action" request parameter.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = LOGIN_PAGE; // Default URL if no action is matched

        try {
            String action = request.getParameter("action");
            System.out.println("Action received: " + action); // For debugging purposes

            if (action == null || action.trim().isEmpty() || WELCOME_ACTIONS.contains(action)) {
                // If action is null, empty, or a welcome action, prepare welcome page data.
                request.setAttribute("productList", PDAO.retrieve("is_active = 1"));
                request.setAttribute("categoryList", CDAO.retrieve("is_active = 1"));
                url = WELCOME_PAGE;
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
            } else {
                // If no matching action is found, direct to an error page.
                url = ERROR_PAGE;
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            url = ERROR_PAGE; // Ensure error page is shown on unexpected exceptions
        } finally {
            // Forward the request to the determined URL.
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * Delegates to the `processRequest` method to handle the request.
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
     * It specifically checks for "multipart/form-data" content type to route
     * file upload requests (like product image uploads) directly to
     * `AdminProductController`. Otherwise, it delegates to `processRequest`.
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
            // If it's a file upload (e.g., for product images), forward to AdminProductController
            request.getRequestDispatcher("/AdminProductController").forward(request, response);
        } else {
            // For regular POST requests (e.g., application/x-www-form-urlencoded, application/json)
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
        return "Main dispatcher servlet for the application.";
    }// </editor-fold>

}