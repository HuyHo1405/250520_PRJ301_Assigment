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
 *
 * @author khoac
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
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = "";
        try {
            String action = request.getParameter("action");
            if (action == null) {
                response.sendRedirect("error.jsp");
                return;
            }

            switch (action) {
                case "toCart":
                    int cartId = SCDAO.createOrGetUserCartId(UserUtils.getUserId(request));
                    request.setAttribute("shoppingCartList", SCIDAO.findByCartId(cartId));
                    url = "cart.jsp";
                    break;
                case "toCheckOut":
                    int userId = UserUtils.getUserId(request); // hoặc lấy từ session

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
                    url = handleAddToCart(request, response);
                    break;
                case "updateCart":
                case "removeFromCart":
                    url = handleUpdateCart(action, request, response);
                    break;
                case "clearCart":
                    url = handleClearCart(request, response);
                    break;
                case "checkoutCart":
                    url = handleCheckoutCart(request, response);
                    break;
                default:
                    response.sendRedirect("error.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private String handleAddToCart(HttpServletRequest request, HttpServletResponse response) {
        int userId = toInt(request.getParameter("userId"));
        int itemId = toInt(request.getParameter("itemId"));
        int quantity = toInt(request.getParameter("quantity"));
        
        if(userId == -1){
            return "error.jsp";
        }
        
        if(itemId == -1 || quantity == -1 || quantity < 1){
            request.setAttribute("errorMsg", "Invalid input parameter");
            return "product_detail.jsp";
        }
        
        int cartId = SCDAO.createOrGetUserCartId(userId);
        if(!SCIDAO.create(new ShoppingCartItemDTO(cartId, itemId, quantity))){
            request.setAttribute("errorMsg", "Internal Error");
            return "error.jsp";
        }
        
        request.setAttribute("msg", "Add item to cart successfully!");
        return "welcome.jsp";
    }

    private String handleUpdateCart(String action, HttpServletRequest request, HttpServletResponse response) {
        int id = toInt(request.getParameter("id"));
        
        if(ValidationUtils.isInvalidId(id)){
            request.setAttribute("errorMsg", "Item not found");
            return ""; // trang quản lý cart
        }
        
        if("updateCart".equals(action)){
            int userId = toInt(request.getParameter("userId"));
            int itemId = toInt(request.getParameter("itemId"));
            int cartId = toInt(request.getParameter("cartId"));
            int quantity = toInt(request.getParameter("quantity"));
            if (userId == -1) {
                return "error.jsp";
            }
            
            if (quantity == -1 || quantity < 1
                    || ValidationUtils.isInvalidId(id)
                    || ValidationUtils.isInvalidId(itemId)
                    || ValidationUtils.isInvalidId(cartId)) {
                request.setAttribute("errorMsg", "Invalid input parameter");
                return ""; // trang quản lý cart
            }
            
            if(!SCIDAO.update(new ShoppingCartItemDTO(id, cartId, itemId, quantity))){
                request.setAttribute("errorMsg", "Internal Error");
                return "error.jsp";
            }
            request.setAttribute("msg", "Update item successfully!");
            return ""; // trang quản lý cart
            
        }else if("removeFromCart".equals(action)){
            if(!SCIDAO.softDelete(id)){
                request.setAttribute("errorMsg", "Internal Error");
                return "error.jsp";
            }
            request.setAttribute("msg", "Remove item successfully!");
            return ""; // trang quản lý cart
        }else{
            return "error.jsp";
        }
    }

    private String handleClearCart(HttpServletRequest request, HttpServletResponse response) {
        int userId = toInt(request.getParameter("userId"));
        if (userId == -1) {
            return "error.jsp";
        }

        int cartId = SCDAO.createOrGetUserCartId(userId);
        boolean success = SCIDAO.softDeleteCartItem(cartId);

        if (!success) {
            request.setAttribute("errorMsg", "Failed to clear cart.");
            return "error.jsp";
        }

        request.setAttribute("msg", "Cart cleared successfully!");
        return ""; // cart.jsp
    }

    private String handleCheckoutCart(HttpServletRequest request, HttpServletResponse response) {
        int userId = toInt(request.getParameter("userId"));
        int addressId = toInt(request.getParameter("addressId"));
        int paymentMethodId = toInt(request.getParameter("paymentMethodId"));
        int shippingMethodId = toInt(request.getParameter("shippingMethodId"));

        if (ValidationUtils.isInvalidId(userId)
                || ValidationUtils.isInvalidId(addressId)
                || ValidationUtils.isInvalidId(paymentMethodId)
                || ValidationUtils.isInvalidId(shippingMethodId)) {
            request.setAttribute("errorMsg", "Thông tin không hợp lệ.");
            System.out.println("1:");
            return "cart.jsp";
        }

        int cartId = SCDAO.createOrGetUserCartId(userId);
        double orderTotal = SCIDAO.calculateTotal(cartId);

        ShoppingOrderDTO order = new ShoppingOrderDTO(
                orderTotal,
                1, // orderStatusId: e.g. 1 = "Placed"
                paymentMethodId,
                shippingMethodId,
                addressId,
                userId
        );

        int orderId = SODAO.createReturnId(order);
        if(ValidationUtils.isInvalidId(orderId)){
            request.setAttribute("errorMsg", "Internal Error.");
            System.out.println("2:");
            return "cart.jsp";
        }
        order.setOrder_code(String.format("ORD%06d", orderId));
        SODAO.update(order);
        
        List<ShoppingCartItemDTO> cartItems = SCIDAO.retrieve("cart_id = ?", cartId);
        for (ShoppingCartItemDTO ci : cartItems) {
            double price = PIDAO.getPrice(ci.getItem_id());
            OrderLineDTO line =  new OrderLineDTO(
                    orderId, 
                    ci.getItem_id(), 
                    ci.getQuantity(), 
                    price
            );
            OLDAO.create(line);
        }
        SCIDAO.softDeleteCartItem(cartId);
        request.setAttribute("successMsg", "Đặt hàng thành công!");
        return "cart.jsp"; 
    }

    private int toInt(String str) {
        try {
            return Integer.parseInt(str.trim());
        } catch (Exception e) {
            return -1;
        }
    }
}
