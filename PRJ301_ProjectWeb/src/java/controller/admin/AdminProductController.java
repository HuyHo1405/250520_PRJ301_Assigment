/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import model.dao.ProductDAO;
import model.dto.ProductDTO;
import utils.ValidationUtils;

/**
 * Status: Chờ thực hiện Người thực hiện: [...........] Ngày bắt đầu:
 * [...........] viết servlet cho Admin Product Controller
 */
@WebServlet(name = "AdminProductController", urlPatterns = {"/AdminProductController"})
@MultipartConfig
public class AdminProductController extends HttpServlet {

    private static final String WELCOME_PAGE = "welcome.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String PRODUCT_LIST_PAGE = "admin-product-management.jsp";
    private static final String PRODUCT_DETAIL_PAGE = "product-detail.jsp";

    private final ProductDAO PDAO = new ProductDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = WELCOME_PAGE;

        try {
            String action = request.getParameter("action");

            switch (action) {
                case "listAllProducts":
                    url = handleListAllProducts(request, response);
                    break;
                case "viewProductDetail":
                    url = handleViewProductDetail(request, response);
                    break;
                case "createProduct":
                    url = handleCreateProduct(request, response);
                    break;
                case "updateProduct":
                    url = handleUpdateProduct(request, response);
                    break;
                case "disableProduct":
                    url = handleDisableProduct(request, response);
                    break;
                case "uploadProductImages":
                    url = handleUploadProductImages(request, response);
                    break;
                case "searchProducts":
                    url = handleSearchProducts(request, response);
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

    private String handleListAllProducts(HttpServletRequest request, HttpServletResponse response) {
        String keyword = request.getParameter("keyword");
        List<ProductDTO> list;

        if (keyword != null && !keyword.trim().isEmpty()) {
            list = PDAO.getProductsByName(keyword); // hàm tìm kiếm sản phẩm
        } else {
            list = PDAO.retrieve("is_active = 1"); // lấy tất cả sản phẩm
        }

        request.setAttribute("productList", list);

        String editId = request.getParameter("editId");
        if (editId != null && !editId.trim().isEmpty()) {
            request.setAttribute("editId", editId);
        }

        return PRODUCT_LIST_PAGE;
    }

    private String handleViewProductDetail(HttpServletRequest request, HttpServletResponse response) {
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

    private String handleCreateProduct(HttpServletRequest request, HttpServletResponse response) {
        try {
            int categoryId = toInt(request.getParameter("categoryId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String coverImageLink = request.getParameter("coverImageLink");

            ProductDTO product = new ProductDTO(categoryId, name, description, coverImageLink);
            boolean success = PDAO.create(product);

            request.setAttribute(success ? "message" : "errorMsg",
                    success ? "Tạo sản phẩm thành công." : "Tạo sản phẩm thất bại.");

            if (success) {
                // Lấy sản phẩm vừa tạo bằng tên (ưu tiên chính xác nếu tên duy nhất)
                List<ProductDTO> createdList = PDAO.getProductsByName(name);
                ProductDTO created = (createdList != null && !createdList.isEmpty()) ? createdList.get(0) : null;

                if (created != null) {
                    List<ProductDTO> showOnlyCreated = new java.util.ArrayList<>();
                    showOnlyCreated.add(created);
                    request.setAttribute("productList", showOnlyCreated);
                } else {
                    request.setAttribute("productList", new java.util.ArrayList<>()); // danh sách rỗng nếu không tìm thấy
                }

                return PRODUCT_LIST_PAGE;
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Lỗi xử lý tạo sản phẩm.");
        }

        return handleListAllProducts(request, response);
    }

    private String handleUpdateProduct(HttpServletRequest request, HttpServletResponse response) {
        try {
            int productId = toInt(request.getParameter("productId"));

            if (productId <= 0) {
                request.setAttribute("errorMsg", "ID sản phẩm không hợp lệ.");
                return handleListAllProducts(request, response);
            }

            ProductDTO existing = PDAO.findById(productId);
            if (existing == null) {
                request.setAttribute("errorMsg", "Sản phẩm không tồn tại.");
                return handleListAllProducts(request, response);
            }

            int categoryId = toInt(request.getParameter("categoryId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String imageLink = request.getParameter("coverImageLink"); // lấy từ input

            // Cập nhật dữ liệu
            existing.setName(name);
            existing.setDescription(description);
            existing.setCategory_id(categoryId);
            existing.setCover_image_link(imageLink); // gán link ảnh mới

            boolean success = PDAO.update(existing);

            request.setAttribute(success ? "message" : "errorMsg",
                    success ? "Cập nhật sản phẩm thành công." : "Cập nhật sản phẩm thất bại.");

            List<ProductDTO> list = Arrays.asList(PDAO.findById(productId));
            request.setAttribute("productList", list);
            request.setAttribute("editId", String.valueOf(productId)); // giữ chế độ chỉnh sửa

            return PRODUCT_LIST_PAGE;

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Lỗi xử lý khi cập nhật sản phẩm.");
            return handleListAllProducts(request, response);
        }
    }

    private String handleDisableProduct(HttpServletRequest request, HttpServletResponse response) {
        int id = toInt(request.getParameter("productId"));
        boolean success = PDAO.disable(id);

        request.setAttribute(success ? "message" : "errorMsg",
                success ? "Đã xóa sản phẩm." : "Xóa sản phẩm thất bại.");
        return handleListAllProducts(request, response);
    }

    private String handleUploadProductImages(HttpServletRequest request, HttpServletResponse response) {
        // Upload xử lý riêng qua multipart hoặc file handler
        request.setAttribute("message", "Chức năng đang phát triển.");
        return "";
    }

    private String handleSearchProducts(HttpServletRequest request, HttpServletResponse response) {
        String keyword = request.getParameter("keyword");
        if (keyword == null || keyword.trim().isEmpty()) {
            return handleListAllProducts(request, response);
        }
        List<ProductDTO> list = PDAO.getProductsByName(keyword);
        request.setAttribute("productList", list);

        // Giữ lại editId nếu có
        String editId = request.getParameter("editId");
        if (editId != null) {
            request.setAttribute("editId", editId);
        }

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
