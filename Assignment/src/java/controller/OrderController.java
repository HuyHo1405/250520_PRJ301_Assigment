package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.dao.AddressDAO;
import model.dao.OrderLineDAO;
import model.dao.ProductDAO;
import model.dao.ShoppingOrderDAO;
import model.dto.AddressDTO;
import model.dto.OrderLineDTO;
import model.dto.ProductDTO;
import model.dto.ShoppingOrderDTO;
import model.dto.UserDTO;
import utils.UserUtils;

/**
 * The `OrderController` servlet manages all user-facing order-related operations.
 * This includes displaying a user's order history, viewing details of a specific order,
 * and allowing users to cancel orders based on their current status.
 */
@WebServlet(name = "OrderController", urlPatterns = {"/OrderController"})
public class OrderController extends HttpServlet {
    
    // Constants for JSP page paths
    private static final String USER_ORDER_LIST_PAGE = "user-orders.jsp";
    private static final String ORDER_DETAIL_PAGE = "order-detail.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    
    // Data Access Objects (DAOs) for interacting with the database
    private static final ShoppingOrderDAO SODAO = new ShoppingOrderDAO();
    private static final ProductDAO PDAO = new ProductDAO(); // Used to get product details for order lines
    private static final AddressDAO ADAO = new AddressDAO(); // Used to get shipping address details
    private static final OrderLineDAO OLDAO = new OrderLineDAO(); // Used to get items within an order
    
    /**
     * Processes requests for both HTTP `GET` and `POST` methods.
     * This method acts as the central dispatcher for order-related actions.
     * It ensures that a user is logged in before proceeding with any order operations.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        String url = "";
        UserDTO loggedInUser = UserUtils.getUser(request); // Retrieve the logged-in user from session/request

        // Check if the user is logged in. If not, redirect to the login page.
        if (loggedInUser == null) {
            request.setAttribute("errorMessage", "Bạn cần đăng nhập để truy cập chức năng đơn hàng.");
            request.getRequestDispatcher("user-form.jsp").forward(request, response); // Redirect to login/user form
            return;
        }

        try {
            // Determine which action to perform based on the "action" parameter
            switch (action) {
                case "listMyOrders":
                    // Handles displaying a list of all orders for the logged-in user.
                    url = handleSearchOrders(request, response, loggedInUser.getId());
                    break;
                case "viewMyOrder":
                    // Handles displaying detailed information for a specific order.
                    url = handleViewOrders(request, response, loggedInUser.getId());
                    break;
                case "placeOrder":
                    // This case is commented out, suggesting "checkoutCart" in CartController handles order placement.
                    // If order placement functionality is ever moved here, it would be implemented in handlePlaceOrders.
                    // url = handlePlaceOrders(request, response, loggedInUser.getId());
                    break;
                case "cancelOrder":
                    // Handles the cancellation of a user's order.
                    url = handleCancelOrder(request, response, loggedInUser.getId());
                    break;
                case "toOrder":
                    // Navigates to the user's order list page directly without fetching data.
                    // Typically, `listMyOrders` would be called to populate the data first.
                    url = USER_ORDER_LIST_PAGE;
                    break;
                default:
                    // If an unknown action is received, set an error message.
                    request.setAttribute("errorMessage", "Hành động đơn hàng không hợp lệ: " + action);
                    url = ERROR_PAGE; // Fallback to a general error page
                    break;
            }
        } catch (Exception e) {
            // Catch any unexpected exceptions during request processing
            System.err.println("Lỗi trong OrderController: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            request.setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            url = ERROR_PAGE; // Redirect to a general error page
        } finally {
            // Forward the request to the determined URL in a finally block to ensure execution
            if (!response.isCommitted()) { // Prevent IllegalStateException if response has already been committed by a redirect
                request.getRequestDispatcher(url).forward(request, response);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * Delegates the processing to the `processRequest` method.
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
     * Delegates the processing to the `processRequest` method.
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
        return "Controller for user order management.";
    }// </editor-fold>

    /**
     * Returns the human-readable name for an order status ID.
     * This utility method helps in displaying user-friendly status messages.
     *
     * @param statusId The integer ID of the order status.
     * @return A string representing the order status name.
     */
    private String getOrderStatusName(int statusId) {
        String statusName;
        switch (statusId) {
            case 1:
                statusName = "Đang chờ xử lý"; // Pending
                break;
            case 2:
                statusName = "Đã xác nhận"; // Confirmed
                break;
            case 3:
                statusName = "Đang xử lý"; // Processing
                break;
            case 4:
                statusName = "Đã giao hàng"; // Shipped
                break;
            case 5:
                statusName = "Đã nhận hàng"; // Delivered
                break;
            case 6:
                statusName = "Đã hủy"; // Cancelled
                break;
            case 7:
                statusName = "Đã trả lại"; // Returned
                break;
            case 8:
                statusName = "Đã hoàn tiền"; // Refunded
                break;
            default:
                statusName = "Không xác định"; // Unknown
                break;
        }
        return statusName;
    }

    /**
     * Handles retrieving and displaying all orders for a specific user.
     * Sets the list of orders as a request attribute.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @param userId The ID of the user whose orders are to be retrieved.
     * @return The JSP page path to display the list of orders.
     */
    private String handleSearchOrders(HttpServletRequest request, HttpServletResponse response, int userId) {
        List<ShoppingOrderDTO> myOrders = SODAO.findByUserId(userId);

        if (myOrders != null) {
            request.setAttribute("myOrders", myOrders);
            return USER_ORDER_LIST_PAGE;
        } else {
            request.setAttribute("errorMessage", "Không thể truy xuất đơn hàng của bạn.");
            return ERROR_PAGE;
        }
    }

    /**
     * Handles viewing the detailed information of a specific order.
     * It retrieves the order, its associated order lines, shipping address, and product details.
     * Ensures that the requested order belongs to the logged-in user for security.
     *
     * @param request The HttpServletRequest object, containing the "orderId" parameter.
     * @param response The HttpServletResponse object.
     * @param userId The ID of the currently logged-in user.
     * @return The JSP page path to display the order details.
     */
    private String handleViewOrders(HttpServletRequest request, HttpServletResponse response, int userId) {
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Mã đơn hàng bị thiếu.");
            return USER_ORDER_LIST_PAGE; // Go back to order list if ID is missing
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);

            ShoppingOrderDTO order = SODAO.findById(orderId);

            // Check if the order exists
            if (order == null) {
                request.setAttribute("errorMessage", "Không tìm thấy đơn hàng.");
                return USER_ORDER_LIST_PAGE;
            }

            // Security check: Ensure the order belongs to the current user
            if (order.getUserId() != userId) {
                request.setAttribute("errorMessage", "Bạn không được phép xem đơn hàng này.");
                return USER_ORDER_LIST_PAGE;
            }

            // Retrieve associated data for the order details page
            List<OrderLineDTO> orderLines = OLDAO.findByOrderId(orderId);
            AddressDTO address = ADAO.findById(order.getAddressId());

            // Create a Map to hold product details, keyed by product ID, for easy lookup in JSP
            Map<Integer, ProductDTO> productsMap = new HashMap<>();
            if (orderLines != null) {
                for (OrderLineDTO line : orderLines) {
                    ProductDTO product = PDAO.findById(line.getItem_id());
                    if (product != null) {
                        productsMap.put(product.getId(), product);
                    }
                }
            }

            // Set attributes for the JSP page
            request.setAttribute("order", order);
            request.setAttribute("orderLines", orderLines);
            request.setAttribute("shippingAddress", address);
            request.setAttribute("productsMap", productsMap);

            return ORDER_DETAIL_PAGE;

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Định dạng Mã đơn hàng không hợp lệ.");
            return USER_ORDER_LIST_PAGE;
        }
    }

    /**
     * Handles the cancellation of a user's order.
     * It validates the order ID, checks ownership, and verifies if the order status
     * allows for cancellation before updating the order status to "Cancelled".
     *
     * @param request The HttpServletRequest object, containing the "orderId" parameter.
     * @param response The HttpServletResponse object.
     * @param userId The ID of the currently logged-in user.
     * @return The JSP page path to display the updated list of orders.
     */
    private String handleCancelOrder(HttpServletRequest request, HttpServletResponse response, int userId) {
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Mã đơn hàng bị thiếu để hủy.");
            return USER_ORDER_LIST_PAGE;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            ShoppingOrderDTO order = SODAO.findById(orderId);

            // Check if the order exists
            if (order == null) {
                request.setAttribute("errorMessage", "Không tìm thấy đơn hàng để hủy.");
                return USER_ORDER_LIST_PAGE;
            }

            // Security check: Ensure the order belongs to the current user
            if (order.getUserId() != userId) {
                request.setAttribute("errorMessage", "Bạn không được phép hủy đơn hàng này.");
                return USER_ORDER_LIST_PAGE;
            }

            // Check if the order status allows cancellation (e.g., Pending (1) or Confirmed (2))
            // Order Status: 1=Pending, 2=Confirmed, 3=Processing, 4=Shipped, 5=Delivered, 6=Cancelled
            if (order.getOrderStatusId() == 1 || order.getOrderStatusId() == 2) {
                order.setOrderStatusId(6); // Set status to Cancelled
                java.util.Date now = new java.util.Date();
                order.setUpdatedAt(new java.sql.Timestamp(now.getTime())); // Update timestamp

                if (SODAO.update(order)) {
                    request.setAttribute("successMessage", "Đơn hàng " + orderId + " đã được hủy thành công.");
                } else {
                    request.setAttribute("errorMessage", "Không thể hủy đơn hàng " + orderId + ".");
                }
            } else {
                request.setAttribute("errorMessage", "Đơn hàng " + orderId + " không thể hủy vì trạng thái hiện tại là: " + getOrderStatusName(order.getOrderStatusId()) + ".");
            }
            // After cancellation attempt, refresh the order list
            return handleSearchOrders(request, response, userId);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Định dạng Mã đơn hàng không hợp lệ để hủy.");
            return USER_ORDER_LIST_PAGE;
        }
    }
}