package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.dao.AddressDAO;
import model.dao.OrderLineDAO;
import model.dao.PaymentMethodDAO;
import model.dao.ProductItemDAO;
import model.dao.ShippingMethodDAO;
import model.dao.ShoppingCartDAO;
import model.dao.ShoppingCartItemDAO;
import model.dao.ShoppingOrderDAO;
import model.dto.AddressDTO;
import model.dto.OrderLineDTO;
import model.dto.PaymentMethodDTO;
import model.dto.ShippingMethodDTO;
import model.dto.ShoppingCartItemDTO;
import model.dto.ShoppingOrderDTO;
import utils.UserUtils;
import utils.ValidationUtils;

/**
 * The `CartController` servlet handles all shopping cart related operations,
 * including adding/removing items, updating quantities, clearing the cart,
 * and managing the checkout process leading to order creation.
 */
@WebServlet(name = "CartController", urlPatterns = {"/CartController"})
public class CartController extends HttpServlet {

    private final ShoppingCartDAO SCDAO = new ShoppingCartDAO();
    private final ShoppingCartItemDAO SCIDAO = new ShoppingCartItemDAO();
    private final ShoppingOrderDAO SODAO = new ShoppingOrderDAO();
    private final OrderLineDAO OLDAO = new OrderLineDAO();
    private final ProductItemDAO PIDAO = new ProductItemDAO();
    private final AddressDAO ADAO = new AddressDAO();
    private final PaymentMethodDAO PMDAO = new PaymentMethodDAO();
    private final ShippingMethodDAO SMDAO = new ShippingMethodDAO();
    
    /**
     * Processes requests for both HTTP `GET` and `POST` methods.
     * This method acts as a central dispatcher for various shopping cart actions
     * based on the "action" parameter.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = "";
        try {
            String action = request.getParameter("action");
            if (action == null) {
                response.sendRedirect("error.jsp"); // Redirect to an error page if no action is specified
                return;
            }

            switch (action) {
                case "toCart":
                    // Retrieves or creates a shopping cart for the current user and displays its contents.
                    int cartId = SCDAO.createOrGetUserCartId(UserUtils.getUserId(request));
                    request.setAttribute("shoppingCartList", SCIDAO.findByCartId(cartId));
                    url = "cart.jsp";
                    break;
                case "toCheckOut":
                    // Prepares data for the checkout page, including user addresses, payment methods, and shipping methods.
                    int userId = UserUtils.getUserId(request);
                    List<AddressDTO> addressList = ADAO.getUserAddress(userId);
                    List<PaymentMethodDTO> paymentMethods = PMDAO.retrieve("is_active = 1");
                    List<ShippingMethodDTO> shippingMethods = SMDAO.retrieve("is_active = 1");

                    request.setAttribute("userId", userId);
                    request.setAttribute("addressList", addressList);
                    request.setAttribute("paymentMethods", paymentMethods);
                    request.setAttribute("shippingMethods", shippingMethods);

                    url= "cart-form.jsp";
                    break;
                case "addToCart":
                    // Handles adding a product item to the user's shopping cart.
                    url = handleAddToCart(request, response);
                    break;
                case "updateCart":
                case "removeFromCart":
                    // Handles updating the quantity of an item in the cart or removing an item from the cart.
                    url = handleUpdateCart(action, request, response);
                    break;
                case "clearCart":
                    // Clears all items from the user's shopping cart.
                    url = handleClearCart(request, response);
                    break;
                case "checkoutCart":
                    // Processes the checkout, creating a new order and associated order lines.
                    url = handleCheckoutCart(request, response);
                    break;
                default:
                    response.sendRedirect("error.jsp"); // Redirect to an error page for unhandled actions
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging purposes
            // Consider redirecting to a specific error page or setting an error message for the user
            response.sendRedirect("error.jsp"); // Fallback to a generic error page
        } finally {
            // Forward the request to the determined URL in a try-finally block to ensure it's always executed
            if (!response.isCommitted()) { // Prevent IllegalStateException if response has already been committed by a redirect
                request.getRequestDispatcher(url).forward(request, response);
            }
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
     * Delegates to the `processRequest` method to handle the request.
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

    /**
     * Handles the addition of a product item to the user's shopping cart.
     * Validates input parameters (userId, itemId, quantity) and attempts to add the item.
     *
     * @param request The HttpServletRequest object containing item and quantity details.
     * @param response The HttpServletResponse object.
     * @return The URL to redirect to after processing (e.g., product detail page or error page).
     */
    private String handleAddToCart(HttpServletRequest request, HttpServletResponse response) {
        int userId = UserUtils.getUserId(request); // Get userId from session or request
        int itemId = toInt(request.getParameter("itemId"));
        int quantity = toInt(request.getParameter("quantity"));
        
        if (ValidationUtils.isInvalidId(userId)) {
            request.setAttribute("errorMsg", "Người dùng chưa đăng nhập hoặc không hợp lệ.");
            return "error.jsp"; // Or login page
        }
        
        if (ValidationUtils.isInvalidId(itemId) || quantity < 1) {
            request.setAttribute("errorMsg", "Sản phẩm hoặc số lượng không hợp lệ.");
            // Assuming "product_detail.jsp" is where the add to cart button usually is
            return "product_detail.jsp"; 
        }
        
        int cartId = SCDAO.createOrGetUserCartId(userId);
        if (!SCIDAO.create(new ShoppingCartItemDTO(cartId, itemId, quantity))) {
            request.setAttribute("errorMsg", "Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng.");
            return "error.jsp";
        }
        
        request.setAttribute("message", "Đã thêm sản phẩm vào giỏ hàng thành công!");
        // Redirect to a confirmation page or back to the product list
        return "welcome.jsp"; 
    }

    /**
     * Handles updating the quantity of an item in the cart or removing an item from the cart.
     * Determines the specific action based on the 'action' parameter.
     *
     * @param action The specific action to perform ("updateCart" or "removeFromCart").
     * @param request The HttpServletRequest object containing item ID and quantity (for update).
     * @param response The HttpServletResponse object.
     * @return The URL to redirect to after processing (e.g., cart page or error page).
     */
    private String handleUpdateCart(String action, HttpServletRequest request, HttpServletResponse response) {
        int id = toInt(request.getParameter("id")); // This `id` typically refers to ShoppingCartItemDTO's ID
        
        if (ValidationUtils.isInvalidId(id)) {
            request.setAttribute("errorMsg", "Mục giỏ hàng không tìm thấy.");
            // This should lead back to the cart view to show the error
            int cartId = SCDAO.createOrGetUserCartId(UserUtils.getUserId(request));
            request.setAttribute("shoppingCartList", SCIDAO.findByCartId(cartId));
            return "cart.jsp"; 
        }
        
        if ("updateCart".equals(action)) {
            int userId = UserUtils.getUserId(request); // Ensure userId is valid
            int itemId = toInt(request.getParameter("itemId")); // ProductItem ID
            int cartId = toInt(request.getParameter("cartId")); // ShoppingCart ID
            int quantity = toInt(request.getParameter("quantity"));
            
            if (ValidationUtils.isInvalidId(userId) || quantity < 1
                    || ValidationUtils.isInvalidId(itemId) // Should validate itemId as well
                    || ValidationUtils.isInvalidId(cartId)) { // Should validate cartId as well
                request.setAttribute("errorMsg", "Thông tin cập nhật không hợp lệ.");
                int currentCartId = SCDAO.createOrGetUserCartId(UserUtils.getUserId(request));
                request.setAttribute("shoppingCartList", SCIDAO.findByCartId(currentCartId));
                return "cart.jsp"; // Go back to cart management page
            }
            
            if (!SCIDAO.update(new ShoppingCartItemDTO(id, cartId, itemId, quantity))) {
                request.setAttribute("errorMsg", "Có lỗi xảy ra khi cập nhật giỏ hàng.");
                return "error.jsp";
            }
            request.setAttribute("message", "Đã cập nhật giỏ hàng thành công!");
            
        } else if ("removeFromCart".equals(action)) {
            if (!SCIDAO.softDelete(id)) { // Assuming softDelete uses the shoppingCartItemId
                request.setAttribute("errorMsg", "Có lỗi xảy ra khi xóa sản phẩm khỏi giỏ hàng.");
                return "error.jsp";
            }
            request.setAttribute("message", "Đã xóa sản phẩm khỏi giỏ hàng thành công!");
        } else {
            return "error.jsp"; // Should not happen with current switch-case structure
        }

        // After update or removal, refresh the cart view
        int currentCartId = SCDAO.createOrGetUserCartId(UserUtils.getUserId(request));
        request.setAttribute("shoppingCartList", SCIDAO.findByCartId(currentCartId));
        return "cart.jsp"; 
    }

    /**
     * Clears all items from the user's shopping cart.
     * Performs a soft delete on all shopping cart items associated with the user's cart.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The URL to redirect to after processing (e.g., cart page or error page).
     */
    private String handleClearCart(HttpServletRequest request, HttpServletResponse response) {
        int userId = UserUtils.getUserId(request); // Ensure userId is valid
        if (ValidationUtils.isInvalidId(userId)) {
            request.setAttribute("errorMsg", "Người dùng không hợp lệ.");
            return "error.jsp";
        }

        int cartId = SCDAO.createOrGetUserCartId(userId);
        boolean success = SCIDAO.softDeleteCartItem(cartId); // Assuming this method clears all items in a cart

        if (!success) {
            request.setAttribute("errorMsg", "Không thể xóa giỏ hàng.");
            return "error.jsp";
        }

        request.setAttribute("message", "Giỏ hàng đã được xóa thành công!");
        request.setAttribute("shoppingCartList", SCIDAO.findByCartId(cartId)); // Refresh the empty cart
        return "cart.jsp"; 
    }

    /**
     * Handles the checkout process, creating a new order from the shopping cart.
     * Validates checkout details, creates a new order, populates order lines,
     * and clears the shopping cart.
     *
     * @param request The HttpServletRequest object containing checkout details.
     * @param response The HttpServletResponse object.
     * @return The URL to redirect to after processing (e.g., order confirmation page or cart page with errors).
     */
    private String handleCheckoutCart(HttpServletRequest request, HttpServletResponse response) {
        int userId = UserUtils.getUserId(request);
        int addressId = toInt(request.getParameter("addressId"));
        int paymentMethodId = toInt(request.getParameter("paymentMethodId"));
        int shippingMethodId = toInt(request.getParameter("shippingMethodId"));

        // Basic validation for required fields
        if (ValidationUtils.isInvalidId(userId)
                || ValidationUtils.isInvalidId(addressId)
                || ValidationUtils.isInvalidId(paymentMethodId)
                || ValidationUtils.isInvalidId(shippingMethodId)) {
            request.setAttribute("errorMsg", "Vui lòng chọn đầy đủ thông tin địa chỉ, phương thức thanh toán và vận chuyển.");
            // Re-prepare checkout data to allow user to correct inputs
            prepareCheckoutView(request, userId);
            return "cart-form.jsp"; 
        }

        int cartId = SCDAO.createOrGetUserCartId(userId);
        List<ShoppingCartItemDTO> cartItems = SCIDAO.retrieve("cart_id = ?", cartId);

        if (cartItems.isEmpty()) {
            request.setAttribute("errorMsg", "Giỏ hàng của bạn đang trống. Vui lòng thêm sản phẩm để đặt hàng.");
            return "cart.jsp"; // Redirect to cart if empty
        }

        double orderTotal = SCIDAO.calculateTotal(cartId); // Calculate total before creating order

        ShoppingOrderDTO order = new ShoppingOrderDTO(
                orderTotal,
                1, // Default orderStatusId, e.g., 1 for "Placed"
                paymentMethodId,
                shippingMethodId,
                addressId,
                userId
        );

        int orderId = SODAO.createReturnId(order);
        if (ValidationUtils.isInvalidId(orderId)) {
            request.setAttribute("errorMsg", "Có lỗi xảy ra khi tạo đơn hàng. Vui lòng thử lại.");
            prepareCheckoutView(request, userId);
            return "cart-form.jsp"; 
        }
        
        // Generate and update order code
        order.setOrder_code(String.format("ORD%06d", orderId));
        SODAO.update(order); // Update the order with the generated code

        // Create order lines from cart items
        for (ShoppingCartItemDTO ci : cartItems) {
            double price = PIDAO.getPrice(ci.getItem_id()); // Get the current price of the product item
            OrderLineDTO line = new OrderLineDTO(
                    orderId,
                    ci.getItem_id(),
                    ci.getQuantity(),
                    price
            );
            OLDAO.create(line);
            // Optionally, update product item stock here
        }

        // Clear the shopping cart after successful checkout
        SCIDAO.softDeleteCartItem(cartId);
        
        request.setAttribute("message", "Đặt hàng thành công! Mã đơn hàng của bạn là: " + order.getOrder_code());
        // Redirect to an order confirmation page or user's order history
        return "order-confirmation.jsp"; // Or "user-orders.jsp"
    }

    /**
     * Helper method to prepare data for the checkout view in case of validation errors.
     * This avoids code duplication in `handleCheckoutCart`.
     *
     * @param request The HttpServletRequest object.
     * @param userId The ID of the current user.
     */
    private void prepareCheckoutView(HttpServletRequest request, int userId) {
        List<AddressDTO> addressList = ADAO.getUserAddress(userId);
        List<PaymentMethodDTO> paymentMethods = PMDAO.retrieve("is_active = 1");
        List<ShippingMethodDTO> shippingMethods = SMDAO.retrieve("is_active = 1");

        request.setAttribute("userId", userId);
        request.setAttribute("addressList", addressList);
        request.setAttribute("paymentMethods", paymentMethods);
        request.setAttribute("shippingMethods", shippingMethods);
    }

    /**
     * Converts a string to an integer.
     * Returns -1 if the string cannot be parsed as an integer or is null/empty.
     *
     * @param str The string to convert.
     * @return The integer value, or -1 if an error occurs during parsing.
     */
    private int toInt(String str) {
        if (str == null || str.trim().isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}