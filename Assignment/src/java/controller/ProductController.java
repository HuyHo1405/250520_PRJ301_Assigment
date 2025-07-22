package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.dao.CategoryDAO;
import model.dao.ProductDAO;
import model.dao.ProductItemDAO;
import model.dto.ProductDTO;
import model.dto.ProductItemDTO;

/**
 * The `ProductController` servlet handles all product-related operations for the user-facing
 * part of the application. This includes listing products, viewing product details,
 * searching, filtering by category, and displaying featured or new arrival products.
 */
@WebServlet(name = "ProductController", urlPatterns = {"/ProductController"})
public class ProductController extends HttpServlet {

    // DAOs (Data Access Objects) for interacting with the database
    private ProductDAO pdao = new ProductDAO();         // For main product information
    private ProductItemDAO pidao = new ProductItemDAO(); // For product variations/items (e.g., specific sizes, colors)
    private CategoryDAO CDAO = new CategoryDAO();       // For product categories

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * This method acts as a dispatcher, forwarding the request to specific handler methods
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

        String action = request.getParameter("action");
        if (action == null) {
            // If no action is specified, redirect to an error page.
            response.sendRedirect("error.jsp");
            return;
        }

        // Use a switch statement to handle different actions
        switch (action) {
            case "listProducts":
                // Handles listing all active products.
                handleListProducts(request, response);
                break;
            case "viewProduct":
                // Handles displaying detailed information for a specific product.
                handleViewProduct(request, response);
                break;
            case "searchProducts":
                // Handles searching for products by keyword and/or category.
                handleSearchProducts(request, response);
                break;
            case "categoryProducts":
                // Handles listing products belonging to a specific category.
                handleCategoryProducts(request, response);
                break;
            case "featuredProducts":
                // Handles displaying a list of featured products.
                handleFeaturedProducts(request, response);
                break;
            case "newArrivals":
                // Handles displaying a list of newly arrived products.
                handleNewArrivals(request, response);
                break;
            default:
                // For any unhandled action, redirect to an error page.
                response.sendRedirect("error.jsp");
        }
    }

    /**
     * Retrieves and displays a list of all active products.
     * The product list is set as a request attribute and then forwarded to `welcome.jsp`.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException if a servlet-specific error occurs during forwarding.
     * @throws IOException if an I/O error occurs during forwarding.
     */
    private void handleListProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ProductDTO> products = pdao.retrieve("is_active = 1");
        request.setAttribute("products", products);
        request.getRequestDispatcher("welcome.jsp").forward(request, response);
    }

    /**
     * Retrieves and displays detailed information for a single product, including its product items (variants).
     * The product and its items are set as request attributes and then forwarded to `product_detail.jsp`.
     *
     * @param request The HttpServletRequest object, expecting a "productId" parameter.
     * @param response The HttpServletResponse object.
     * @throws ServletException if a servlet-specific error occurs during forwarding.
     * @throws IOException if an I/O error occurs during forwarding.
     * @throws NumberFormatException if "productId" is not a valid integer.
     */
    private void handleViewProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            ProductDTO product = pdao.findById(productId);
            List<ProductItemDTO> productItems = pidao.getByProductId(productId);

            if (product == null) {
                request.setAttribute("errorMessage", "Không tìm thấy sản phẩm.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("product", product);
            request.setAttribute("productItems", productItems);

            request.getRequestDispatcher("product_detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Mã sản phẩm không hợp lệ.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Handles searching for products based on a keyword and/or category ID.
     * If a category ID is provided, the search is filtered by that category.
     * The results are displayed on the `welcome.jsp` page along with the category list.
     *
     * @param request The HttpServletRequest object, expecting "keyword" and optional "categoryId" parameters.
     * @param response The HttpServletResponse object.
     * @throws ServletException if a servlet-specific error occurs during forwarding.
     * @throws IOException if an I/O error occurs during forwarding.
     */
    private void handleSearchProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        // Ensure keyword is not null to avoid NullPointerException in SQL query
        keyword = (keyword == null) ? "" : keyword; 
        int categoryId = toInt(request.getParameter("categoryId")); // Convert categoryId to integer, -1 if invalid

        if (categoryId == -1) {
            // Search by keyword across all active products if no valid category is specified.
            request.setAttribute("productList", pdao.retrieve("is_active = 1 AND name LIKE ?", "%" + keyword + "%"));
        } else {
            // Search by keyword within a specific active category.
            request.setAttribute("productList", pdao.retrieve("is_active = 1 AND category_id = ? AND name LIKE ?", categoryId, "%" + keyword + "%"));
        }
        // Always provide the category list for navigation/filtering options on the page.
        request.setAttribute("categoryList", CDAO.retrieve("is_active = 1")); 
        request.getRequestDispatcher("welcome.jsp").forward(request, response);
    }

    /**
     * Handles displaying products belonging to a specific category.
     *
     * @param request The HttpServletRequest object, expecting a "category_id" parameter.
     * @param response The HttpServletResponse object.
     * @throws ServletException if a servlet-specific error occurs during forwarding.
     * @throws IOException if an I/O error occurs during forwarding.
     * @throws NumberFormatException if "category_id" is not a valid integer.
     */
    private void handleCategoryProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int categoryId = Integer.parseInt(request.getParameter("category_id"));
            List<ProductDTO> products = pdao.getProductsByCategoryId(categoryId);
            request.setAttribute("products", products);
            request.getRequestDispatcher("welcome.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Mã danh mục không hợp lệ.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Handles displaying a curated list of "featured" products.
     * Currently, it retrieves products with IDs less than 10 and that are active.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException if a servlet-specific error occurs during forwarding.
     * @throws IOException if an I/O error occurs during forwarding.
     */
    private void handleFeaturedProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ProductDTO> products = pdao.retrieve("id < 10 AND is_active = 1"); // Example logic for featured products
        request.setAttribute("products", products);
        request.getRequestDispatcher("featured.jsp").forward(request, response);
    }

    /**
     * Handles displaying a list of the latest "new arrivals" products.
     * It retrieves the 10 most recently added active products.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException if a servlet-specific error occurs during forwarding.
     * @throws IOException if an I/O error occurs during forwarding.
     */
    private void handleNewArrivals(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ProductDTO> products = pdao.retrieve("is_active = 1 ORDER BY id DESC LIMIT 10"); // Example logic for new arrivals
        request.setAttribute("products", products);
        request.getRequestDispatcher("new_arrivals.jsp").forward(request, response);
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
        return "Product Controller handles user-facing product Browse and details.";
    }// </editor-fold>

    /**
     * Helper method to safely convert a String to an int.
     * Returns -1 if the string is null, empty, or cannot be parsed as an integer.
     *
     * @param str The string to convert.
     * @return The integer value, or -1 if conversion fails.
     */
    private int toInt(String str) {
        if (str == null || str.trim().isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            // Log the exception for debugging if necessary
            return -1;
        }
    }
}