/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import model.dao.CartDAO;
import model.dto.CartDTO;

/**
 *
 * @author khoac
 */
@WebServlet(name = "CartController", urlPatterns = {"/CartController"})
public class CartController extends HttpServlet {
private final CartDAO cdao = new CartDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("error.jsp");
            return;
        }

        switch (action) {
            case "getCart":
                handleGetCart(request, response);
                break;

            case "addToCart":
                handleAddToCart(request, response);
                break;

            case "updateCart":
                handleUpdateCart(request, response);
                break;

            case "removeFromCart":
                handleRemoveFromCart(request, response);
                break;

            case "clearCart":
                handleClearCart(request, response);
                break;

            case "checkoutCart":
                handleCheckoutCart(request, response);
                break;

            default:
                response.sendRedirect("error.jsp");
        }
    }

    private void handleGetCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartDTO> cart = (List<CartDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        CartDTO item = cdao.findById(id);
        if (item == null) {
            response.sendRedirect("productNotFound.jsp");
            return;
        }

        HttpSession session = request.getSession();
        List<CartDTO> cart = (List<CartDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        cart.add(item);
        session.setAttribute("cart", cart);

        response.sendRedirect("CartController?action=getCart");
    }

    private void handleUpdateCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("CartController?action=getCart");
    }

    private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        HttpSession session = request.getSession();
        List<CartDTO> cart = (List<CartDTO>) session.getAttribute("cart");

        if (cart != null) {
            cart.removeIf(item -> item.getId() == id);
            session.setAttribute("cart", cart);
        }

        response.sendRedirect("CartController?action=getCart");
    }

    private void handleClearCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("cart");
        response.sendRedirect("CartController?action=getCart");
    }

    private void handleCheckoutCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartDTO> cart = (List<CartDTO>) session.getAttribute("cart");

        if (cart != null && !cart.isEmpty()) {
            session.removeAttribute("cart");
            request.setAttribute("message", "Thanh toán thành công!");
        } else {
            request.setAttribute("message", "Giỏ hàng trống.");
        }

        request.getRequestDispatcher("checkout.jsp").forward(request, response);
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
