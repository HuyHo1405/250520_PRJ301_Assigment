package controller.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import model.dao.AddressDAO;
import model.dao.OrderExportDAO;
import model.dao.OrderLineDAO;
import model.dao.OrderStatusDAO;
import model.dao.PaymentMethodDAO;
import model.dao.PaymentTypeDAO;
import model.dao.ShippingMethodDAO;
import model.dao.ShoppingOrderDAO;
import model.dao.UserDAO;
import model.dto.AddressDTO;
import model.dto.OrderExportDTO;
import model.dto.OrderLineDTO;
import model.dto.OrderStatusDTO;
import model.dto.PaymentMethodDTO;
import model.dto.PaymentTypeDTO;
import model.dto.ShippingMethodDTO;
import model.dto.ShoppingOrderDTO;
import model.dto.UserDTO;
import utils.MailUtils;
import utils.OrderUtils;
import utils.ValidationUtils;

/**
 * AdminOrderController handles order management operations for administrators,
 * including viewing orders, updating statuses, searching, disabling, and exporting.
 *
 * URL pattern: /AdminOrderController
 */
@WebServlet(name = "AdminOrderController", urlPatterns = {"/AdminOrderController"})
public class AdminOrderController extends HttpServlet {

    private static final String ERROR_PAGE = "error.jsp";
    private static final String WELCOME_PAGE = "welcome.jsp";
    private static final String ADMIN_ORDER_MANAGEMENT_PAGE = "admin-order-management.jsp";
    private static final String ORDER_DETAILS = "order-details.jsp";

    private final ShoppingOrderDAO SODAO = new ShoppingOrderDAO();
    private final OrderLineDAO OLDAO = new OrderLineDAO();
    private final OrderStatusDAO OSDAO = new OrderStatusDAO();
    private final UserDAO UDAO = new UserDAO();
    private final AddressDAO ADAO = new AddressDAO();
    private final PaymentMethodDAO PMDAO = new PaymentMethodDAO();
    private final PaymentTypeDAO PTDAO = new PaymentTypeDAO();
    private final ShippingMethodDAO SMDAO = new ShippingMethodDAO();
    private final OrderExportDAO OEDAO = new OrderExportDAO();

    private final int CANCEL_ID = 6;

    /**
     * Main controller method that dispatches requests based on the "action" parameter.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = WELCOME_PAGE;
        try {
            String action = request.getParameter("action");

            switch (action) {
                case "toAdminOrdersPage":
                    request.setAttribute("orderList", SODAO.retrieve("1 = 1 ORDER BY order_date DESC, order_status_id ASC"));
                    request.setAttribute("statusMap", OrderUtils.getMap(OSDAO.retrieve("1=1")));
                    url = ADMIN_ORDER_MANAGEMENT_PAGE;
                    break;
                case "viewOrderDetail":
                    url = handleViewOrderDetail(request, response);
                    break;
                case "updateOrderStatus":
                    url = handleUpdateOrderStatus(request, response);
                    break;
                case "disableOrder":
                    url = handleDisableOrder(request, response);
                    break;
                case "searchOrders":
                    url = handleSearchOrder(request, response);
                    break;
                case "exportOrders":
                    handleExportOrders(request, response);
                    return;
                default:
                    url = ERROR_PAGE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            url = ERROR_PAGE;
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

    /**
     * Loads all details of a specific order by ID and sets attributes for the JSP view.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @return JSP path for displaying order details
     */
    private String handleViewOrderDetail(HttpServletRequest request, HttpServletResponse response) {

        int orderId = toInt(request.getParameter("orderId"));

        if (ValidationUtils.isInvalidId(orderId)) {
            return error(request, "Invalid order ID.");
        }

        ShoppingOrderDTO order = SODAO.findById(orderId);
        System.out.println("order = " + order);
        if (order == null) {
            return error(request, "Không tìm thấy đơn hàng.");
        }

        OrderStatusDTO status = OSDAO.findById(order.getOrderStatusId());
        System.out.println("status = " + status);
        if (status == null) {
            return error(request, "Không tìm thấy trạng thái đơn hàng.");
        }

        PaymentMethodDTO paymentMethod = PMDAO.findById(order.getPaymentMethodId());
        System.out.println("paymentMethod = " + paymentMethod);
        if (paymentMethod == null) {
            return error(request, "Không tìm thấy phương thức thanh toán.");
        }

        PaymentTypeDTO type = PTDAO.findById(paymentMethod.getPayment_type_id());
        System.out.println("paymentType = " + type);
        if (type == null) {
            return error(request, "Không tìm thấy loại thanh toán.");
        }

        ShippingMethodDTO shippingMethod = SMDAO.findById(order.getShippingMethodId());
        System.out.println("shippingMethod = " + shippingMethod);
        if (shippingMethod == null) {
            return error(request, "Không tìm thấy phương thức giao hàng.");
        }

        AddressDTO address = ADAO.findById(order.getAddressId());
        System.out.println("address = " + address);
        if (address == null) {
            return error(request, "Không tìm thấy địa chỉ.");
        }

        UserDTO user = UDAO.findById(order.getUserId());
        System.out.println("user = " + user);
        if (user == null) {
            return error(request, "Không tìm thấy người dùng.");
        }

        List<OrderLineDTO> orderItems = OLDAO.retrieve("order_id = ?", orderId);
        System.out.println("OrderItemList size = " + (orderItems != null ? orderItems.size() : 0));

        request.setAttribute("order", order);
        request.setAttribute("orderStatus", status);
        request.setAttribute("paymentMethod", paymentMethod);
        request.setAttribute("paymentType", type);
        request.setAttribute("shippingMethod", shippingMethod);
        request.setAttribute("defaultAdress", address);
        request.setAttribute("user", user);
        request.setAttribute("orderItemList", orderItems);
        request.setAttribute("orderStatusList", OSDAO.retrieve("1=1"));
        
        System.out.println("All data loaded successfully. Forwarding to ORDER_DETAILS.");
        return ORDER_DETAILS;
    }

    /**
     * Sets an order’s status to "Cancelled" by using a predefined status ID.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @return JSP path for result view
     */
    private String handleDisableOrder(HttpServletRequest request, HttpServletResponse response) {
        int orderId = toInt(request.getParameter("orderId"));
        return handleUpdateOrderStatus(orderId, CANCEL_ID, request);
    }

    /**
     * Updates the status of an order to a new status from request parameters.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @return JSP path for result view
     */
    private String handleUpdateOrderStatus(HttpServletRequest request, HttpServletResponse response) {
        int orderId = toInt(request.getParameter("orderId"));
        int orderStatusId = toInt(request.getParameter("orderStatusId"));
        return handleUpdateOrderStatus(orderId, orderStatusId, request);
    }

     /**
     * Handles the actual update of an order’s status and sends email notification to user.
     *
     * @param orderId       ID of the order
     * @param orderStatusId New status ID
     * @param request       HTTP request
     * @return JSP path for result view
     */
    private String handleUpdateOrderStatus(int orderId, int orderStatusId, HttpServletRequest request) {
        if (ValidationUtils.isInvalidId(orderId) || ValidationUtils.isInvalidId(orderStatusId)) {
            return error(request, "Invalid order or status ID.");
        }

        List<ShoppingOrderDTO> orderList = SODAO.retrieve("id = ?", orderId);
        if (orderList.isEmpty()) {
            return error(request, "Order not found.");
        }

        List<OrderStatusDTO> statusList = OSDAO.retrieve("id = ?", orderStatusId);
        if (statusList.isEmpty()) {
            return error(request, "Order status not found.");
        }

        ShoppingOrderDTO order = orderList.get(0);
        order.setOrderStatusId(orderStatusId);
        SODAO.update(order);

        try {
            UserDTO user = UDAO.findById(order.getUserId());
            if (user != null) {
                String toEmail = user.getEmail_address();
                String newStatusName = statusList.get(0).getStatus();
                String subject = "Your Order #" + order.getOrder_code() + " Status Update";

                String content = "<h3>Your Order Status Has Been Updated</h3>"
                        + "<p>Your order <b>#" + order.getOrder_code() + "</b> has been updated to: <b>" + newStatusName + "</b>.</p>"
                        + "<p>Thank you for shopping with us!</p>";

                MailUtils.sendHtmlEmail(toEmail, subject, content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("orderList", SODAO.retrieve("1 = 1"));
        request.setAttribute("statusMap", OrderUtils.getMap(OSDAO.retrieve("1=1")));
        return ADMIN_ORDER_MANAGEMENT_PAGE;
    }

    /**
     * Searches for orders by order code using a keyword.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @return JSP path for displaying filtered order list
     */
    private String handleSearchOrder(HttpServletRequest request, HttpServletResponse response) {
        String keyword = request.getParameter("strKeyword");
        request.setAttribute("orderList", SODAO.retrieve("order_code LIKE ?", "%" + keyword + "%"));
        request.setAttribute("statusMap", OrderUtils.getMap(OSDAO.retrieve("1=1")));
        return ADMIN_ORDER_MANAGEMENT_PAGE;
    }

    /**
     * Exports the full list of orders as a CSV file to be downloaded by admin.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws IOException if an I/O error occurs while writing the file
     */
    private void handleExportOrders(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"shopping_orders.csv\"");

        PrintWriter writer = response.getWriter();

        // Header CSV
        writer.println("Order ID,Order Code,Status,Payment Method,Payment Type,Shipping Method,Total Amount,User Name,Address,Order Date,Created At");

        List<OrderExportDTO> orders = OEDAO.getAllForExport();

        for (OrderExportDTO order : orders) {
            writer.printf("%d,%s,%s,%s,%s,%s,%.2f,%s,%s,%s,%s%n",
                    order.getId(),
                    escapeCSV(order.getOrderCode()),
                    escapeCSV(order.getStatusName()),
                    escapeCSV(order.getPaymentMethod()),
                    escapeCSV(order.getPaymentType()),
                    escapeCSV(order.getShippingMethod()),
                    order.getOrderTotal(),
                    escapeCSV(order.getUserName()),
                    escapeCSV(order.getAddress()),
                    order.getOrderDate() != null ? order.getOrderDate().toString() : "",
                    order.getCreatedAt() != null ? order.getCreatedAt().toString() : ""
            );
        }

        writer.flush();
        writer.close();
    }

    /**
     * Safely parses an integer from a string. Returns -1 on failure.
     *
     * @param str the string to parse
     * @return parsed integer or -1 if invalid
     */
    private int toInt(String str) {
        if (str != null) {
            try {
                return Integer.parseInt(str.trim());
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }

    /**
     * Sets an error message in the request scope and returns the error page path.
     *
     * @param request HTTP request
     * @param msg     error message to show
     * @return error JSP path
     */
    private String error(HttpServletRequest request, String msg) {
        request.setAttribute("errorMsg", msg);
        return ERROR_PAGE;
    }
    
    /**
     * Escapes a string to be safely written into a CSV field.
     * Encloses in quotes if necessary and escapes inner quotes.
     *
     * @param value original string value
     * @return escaped CSV-compatible string
     */
    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        boolean hasSpecial = value.contains(",") || value.contains("\"") || value.contains("\n");
        if (hasSpecial) {
            value = value.replace("\"", "\"\""); // escape dấu nháy
            return "\"" + value + "\"";
        }
        return value;
    }
}