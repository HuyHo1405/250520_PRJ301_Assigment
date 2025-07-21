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

@WebServlet(name = "OrderController", urlPatterns = {"/OrderController"})
public class OrderController extends HttpServlet {
    
    private static final String USER_ORDER_LIST_PAGE = "user-orders.jsp";
    private static final String ORDER_DETAIL_PAGE = "order-detail.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    
    private static final ShoppingOrderDAO SODAO = new ShoppingOrderDAO();
    private static final ProductDAO PDAO = new ProductDAO();
    private static final AddressDAO ADAO = new AddressDAO();
    private static final OrderLineDAO OLDAO =new OrderLineDAO();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        String url = "";
        UserDTO loggedInUser = UserUtils.getUser(request);

        if (loggedInUser == null) {
            request.setAttribute("errorMessage", "You need to be logged in to access order functions.");
            request.getRequestDispatcher("user-form.jsp").forward(request, response);
            return;
        }


        try {
            switch (action) {
                case "listMyOrders":
                    url = handleSearchOrders(request, response, loggedInUser.getId());
                    break;
                case "viewMyOrder":
                    url = handleViewOrders(request, response, loggedInUser.getId());
                    break;
                case "placeOrder":
//                    url = handlePlaceOrders(request, response, loggedInUser.getId());
                    break;
                case "cancelOrder":
                    url = handleCancelOrder(request, response, loggedInUser.getId());
                    break;
                case "toOrder":
                    url = "user-orders.jsp";
                    break;
                default:
                    request.setAttribute("errorMessage", "Invalid order action: " + action);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error in OrderController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
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

    private String getOrderStatusName(int statusId) {
        String statusName;
        switch (statusId) {
            case 1:
                statusName = "Pending";
                break;
            case 2:
                statusName = "Confirmed";
                break;
            case 3:
                statusName = "Processing";
                break;
            case 4:
                statusName = "Shipped";
                break;
            case 5:
                statusName = "Delivered";
                break;
            case 6:
                statusName = "Cancelled";
                break;
            case 7:
                statusName = "Returned";
                break;
            case 8:
                statusName = "Refunded";
                break;
            default:
                statusName = "Unknown";
                break;
        }
        return statusName;
    }

    private String handleSearchOrders(HttpServletRequest request, HttpServletResponse response, int userId) {

        ShoppingOrderDAO orderDAO = new ShoppingOrderDAO();
        List<ShoppingOrderDTO> myOrders = SODAO.findByUserId(userId);

        if (myOrders != null) {
            request.setAttribute("myOrders", myOrders);
            return USER_ORDER_LIST_PAGE;
        } else {
            request.setAttribute("errorMessage", "Could not retrieve your orders.");
            return ERROR_PAGE;
        }
    }

    private String handleViewOrders(HttpServletRequest request, HttpServletResponse response, int userId) {
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Order ID is missing.");
            return USER_ORDER_LIST_PAGE;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);

            ShoppingOrderDTO order = SODAO.findById(orderId);

            if (order == null) {
                request.setAttribute("errorMessage", "Order not found.");
                return USER_ORDER_LIST_PAGE;
            }

            if (order.getUserId() != userId) {
                request.setAttribute("errorMessage", "You are not authorized to view this order.");
                return USER_ORDER_LIST_PAGE;
            }

            List<OrderLineDTO> orderLines = OLDAO.findByOrderId(orderId);
            AddressDTO address = ADAO.findById(order.getAddressId());

            // Create a Map to hold products, keyed by product ID
            Map<Integer, ProductDTO> productsMap = new HashMap<>();
            if (orderLines != null) {
                for (OrderLineDTO line : orderLines) {
                    ProductDTO product = PDAO.findById(line.getItem_id());
                    // Add the product to the map, using its ID as the key
                    if (product != null) {
                        productsMap.put(product.getId(), product);
                    }
                }
            }

            request.setAttribute("order", order);
            request.setAttribute("orderLines", orderLines);
            request.setAttribute("shippingAddress", address);
            request.setAttribute("productsMap", productsMap);

            return ORDER_DETAIL_PAGE;

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Order ID format.");
            return USER_ORDER_LIST_PAGE;
        }
    }

    private String handleCancelOrder(HttpServletRequest request, HttpServletResponse response, int userId) {
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Order ID is missing for cancellation.");
            return USER_ORDER_LIST_PAGE;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            ShoppingOrderDAO orderDAO = new ShoppingOrderDAO();
            ShoppingOrderDTO order = SODAO.findById(orderId);

            if (order == null) {
                request.setAttribute("errorMessage", "Order not found for cancellation.");
                return USER_ORDER_LIST_PAGE;
            }

            // Ensure the order belongs to the logged-in user
            if (order.getUserId() != userId) {
                request.setAttribute("errorMessage", "You are not authorized to cancel this order.");
                return USER_ORDER_LIST_PAGE;
            }

            // Check if the order status allows cancellation (e.g., Pending or Confirmed)
            // Order Status: 1=Pending, 2=Confirmed, 3=Processing, 4=Shipped, 5=Delivered, 6=Cancelled
            if (order.getOrderStatusId() == 1 || order.getOrderStatusId() == 2) {
                order.setOrderStatusId(6); // Set status to Cancelled
                java.util.Date now = new java.util.Date();
                order.setUpdatedAt(new java.sql.Timestamp(now.getTime()));

                if (orderDAO.update(order)) {
                    request.setAttribute("successMessage", "Order " + orderId + " has been successfully cancelled.");
                } else {
                    request.setAttribute("errorMessage", "Failed to cancel order " + orderId + ".");
                }
            } else {
                request.setAttribute("errorMessage", "Order " + orderId + " cannot be cancelled as it is already " + getOrderStatusName(order.getOrderStatusId()) + ".");
            }
            return handleSearchOrders(request, response, userId);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Order ID format for cancellation.");
            return USER_ORDER_LIST_PAGE;
        }
    }
    
}
