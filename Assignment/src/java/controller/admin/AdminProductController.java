/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.dao.ProductDAO;
import model.dto.ProductDTO;
import utils.ValidationUtils;

/**
 * Status: Chờ thực hiện Người thực hiện: [...........] Ngày bắt đầu:
 * [...........] viết servlet cho Admin Product Controller
 */
@WebServlet(name = "AdminProductController", urlPatterns = {"/AdminProductController"})
public class AdminProductController extends HttpServlet {

    private static final String WELCOME_PAGE = "welcome.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String PRODUCT_LIST_PAGE = "admin-product-management.jsp";
    private static final String PRODUCT_DETAIL_PAGE = "product-detail.jsp";
    private static final String PRODUCT_FORM_PAGE = "product-form.jsp";

    private final ProductDAO PDAO = new ProductDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = WELCOME_PAGE;

        try {
            String action = request.getParameter("action");

            switch (action) {
                case "listAllProducts":
                    url = handleListAllProducts(request);
                    break;
                case "viewProductDetail":
                    url = handleViewProductDetail(request);
                    break;
                case "createProduct":
                    url = handleCreateProduct(request);
                    break;
                case "updateProduct":
                    url = handleUpdateProduct(request);
                    break;
                case "disableProduct":
                    url = handleDisableProduct(request);
                    break;
                case "uploadProductImages":
                    url = handleUploadProductImages(request);
                    break;
                case "searchProducts":
                    url = handleSearchProducts(request);
                    break;
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

    private String handleListAllProducts(HttpServletRequest request) {
        List<ProductDTO> list = PDAO.retrieve("1=1");
        request.setAttribute("productList", list);
        return PRODUCT_LIST_PAGE;
    }

    private String handleViewProductDetail(HttpServletRequest request) {
        int id = toInt(request.getParameter("productId"));
        if (ValidationUtils.isInvalidId(id)) {
            return error(request, "ID sản phẩm không hợp lệ.");
        }

        ProductDTO product = PDAO.findById(id);
        if (product == null) {
            return error(request, "Không tìm thấy sản phẩm.");
        }

        request.setAttribute("product", product);
        return PRODUCT_DETAIL_PAGE;
    }

    private String handleCreateProduct(HttpServletRequest request) {
        try {
            int categoryId = toInt(request.getParameter("categoryId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String coverImageLink = request.getParameter("coverImageLink");

            ProductDTO product = new ProductDTO(categoryId, name, description, coverImageLink);
            boolean success = PDAO.create(product);

            request.setAttribute(success ? "message" : "errorMsg",
                    success ? "Tạo sản phẩm thành công." : "Tạo sản phẩm thất bại.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Lỗi xử lý tạo sản phẩm.");
        }

        return handleListAllProducts(request);
    }

    private String handleUpdateProduct(HttpServletRequest request) {
        try {
            int productId = toInt(request.getParameter("productId"));

            if (productId <= 0) {
                request.setAttribute("errorMsg", "ID sản phẩm không hợp lệ.");
                return handleListAllProducts(request);
            }

            // Kiểm tra xem sản phẩm có tồn tại không
            ProductDTO existing = PDAO.findById(productId);
            if (existing == null) {
                request.setAttribute("errorMsg", "Sản phẩm không tồn tại.");
                return handleListAllProducts(request);
            }

            // Lấy dữ liệu từ form
            int categoryId = toInt(request.getParameter("categoryId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String coverImageLink = request.getParameter("coverImageLink");

            // Tạo đối tượng mới để cập nhật
            ProductDTO updated = new ProductDTO(categoryId, name, description, coverImageLink);
            boolean success = PDAO.update(updated);

            request.setAttribute(success ? "message" : "errorMsg",
                    success ? "Cập nhật sản phẩm thành công." : "Cập nhật sản phẩm thất bại.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Lỗi xử lý khi cập nhật sản phẩm.");
        }

        return handleListAllProducts(request);
    }

    private String handleDisableProduct(HttpServletRequest request) {
        int id = toInt(request.getParameter("productId"));
        boolean success = PDAO.disable(id);

        request.setAttribute(success ? "message" : "errorMsg",
                success ? "Đã xóa sản phẩm." : "Xóa sản phẩm thất bại.");
        return handleListAllProducts(request);
    }

    private String handleUploadProductImages(HttpServletRequest request) {
        // Upload xử lý riêng qua multipart hoặc file handler
        request.setAttribute("message", "Chức năng đang phát triển.");
        return PRODUCT_FORM_PAGE;
    }

    private String handleSearchProducts(HttpServletRequest request) {
        String keyword = request.getParameter("keyword");
        List<ProductDTO> list = PDAO.getProductsByName(keyword);
        request.setAttribute("productList", list);
        return PRODUCT_LIST_PAGE;
    }

    // Utility methods
    private int toInt(String val) {
        try {
            return Integer.parseInt(val.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private String error(HttpServletRequest request, String msg) {
        request.setAttribute("errorMsg", msg);
        return ERROR_PAGE;
    }
}
