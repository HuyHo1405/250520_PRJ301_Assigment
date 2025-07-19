package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.dao.ProductDAO;
import model.dao.ProductItemDAO;
import model.dto.ProductDTO;
import model.dto.ProductItemDTO;

/**
 * Status: Chờ thực hiện Người thực hiện: [...........] Ngày bắt đầu:
 * [...........] viết servlet cho Product Controller
 */
@WebServlet(name = "ProductController", urlPatterns = {"/ProductController"})
public class ProductController extends HttpServlet {

    private ProductDAO pdao = new ProductDAO();
    private ProductItemDAO pidao = new ProductItemDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("error.jsp");
            return;
        }

        switch (action) {
            case "listProducts":
                handleListProducts(request, response);
                break;
            case "viewProduct":
                handleViewProduct(request, response);
                break;
            case "searchProducts":
                handleSearchProducts(request, response);
                break;
            case "categoryProducts":
                handleCategoryProducts(request, response);
                break;
            case "featuredProducts":
                handleFeaturedProducts(request, response);
                break;
            case "newArrivals":
                handleNewArrivals(request, response);
                break;
            default:
                response.sendRedirect("error.jsp");
        }
    }

    private void handleListProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ProductDTO> products = pdao.retrieve("is_active = 1");
        request.setAttribute("products", products);
        request.getRequestDispatcher("welcome.jsp").forward(request, response);
    }

    private void handleViewProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("id"));
        ProductDTO product = pdao.findById(productId);
        List<ProductItemDTO> productItems = pidao.getByProductId(productId);

        request.setAttribute("product", product);
        request.setAttribute("productItems", productItems);

        request.getRequestDispatcher("product_detail.jsp").forward(request, response);
    }

    private void handleSearchProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<ProductDTO> products = pdao.getProductsByName(keyword);
        request.setAttribute("products", products);
        request.getRequestDispatcher("welcome.jsp").forward(request, response);
    }

    private void handleCategoryProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int categoryId = Integer.parseInt(request.getParameter("category_id"));
        List<ProductDTO> products = pdao.getProductsByCategoryId(categoryId);
        request.setAttribute("products", products);
        request.getRequestDispatcher("welcome.jsp").forward(request, response);
    }

    private void handleFeaturedProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ProductDTO> products = pdao.retrieve("id < 10 AND is_active = 1");
        request.setAttribute("products", products);
        request.getRequestDispatcher("featured.jsp").forward(request, response);
    }

    private void handleNewArrivals(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ProductDTO> products = pdao.retrieve("is_active = 1 ORDER BY id DESC LIMIT 10");
        request.setAttribute("products", products);
        request.getRequestDispatcher("new_arrivals.jsp").forward(request, response);
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
