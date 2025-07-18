/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import utils.ImageUploadUtils;
import java.io.IOException;
import jakarta.servlet.http.Part;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import model.dao.CategoryDAO;
import model.dao.ProductDAO;
import model.dao.VariationDAO;
import model.dao.VariationOptionDAO;
import model.dto.ProductDTO;
import utils.ProductUtils;
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
    private static final String ADMIN_PRODUCT_MANAGEMENT_PAGE = "admin-product-management.jsp";
    private static final String PRODUCT_FORM = "product-form.jsp";

    private final ProductDAO PDAO = new ProductDAO();
    private final CategoryDAO CDAO = new CategoryDAO();
    private final VariationDAO VDAO = new VariationDAO();
    private final VariationOptionDAO VODAO = new VariationOptionDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = "";

        try {
            String action = request.getParameter("action");

            switch (action) {
                case "toAdminProductPage":
                    url = handleSearchProducts(request, response);
                    break;
                case "toCreateProduct":
                case "toEditProduct":
                case "viewProductDetail":
                    url = handleToProductForm(request, action);
                    break;
                case "createProduct":
                    url = handleCreateProduct(request, response);
                    break;
                case "updateProduct":
                    url = handleUpdateProduct(request, response);
                    break;
                case "toggleIsActiveProduct":
                    url = handleToggleIsActive(request, response);
                    break;
                case "searchProductsManagement":
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
        String contentType = request.getContentType();

        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            // Since this controller is receiving the multipart request,
            // we will parse the parts here to get the 'action' and file.
            String action = null;
            // No need to store the specific file Part here if handleUploadProductImages
            // is going to get it itself using request.getPart("imageFile").
            // However, to get the 'action' from multipart, you still need to iterate parts.
            try {
                Collection<Part> parts = request.getParts(); // This line parses the entire multipart request
                for (Part part : parts) {
                    if ("action".equals(part.getName())) {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream()))) {
                            action = reader.readLine();
                            break; // Found action, no need to read other parts for it
                        }
                    }
                }
            } catch (ServletException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error parsing multipart data: " + e.getMessage());
                return;
            }

            if ("uploadProductImages".equals(action)) {
                // Call your provided method
                handleUploadProductImages(request, response);
            } else {
                // Handle other multipart actions here, or send an error if action is not supported
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported multipart action: " + (action != null ? action : "null"));
            }

        } else {
            // Handle regular POST requests here (if AdminProductController also handles them directly)
            processRequest(request, response);
        }
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

    private String handleSearchProducts(HttpServletRequest request, HttpServletResponse response) {
        String keyword = request.getParameter("keyword");
        keyword = keyword == null ? "" : keyword;
        int categoryId = toInt(request.getParameter("searchCategoryId"));
        List<ProductDTO> list;

        if (categoryId != -1) {
            list = PDAO.retrieve("name LIKE ? AND category_id = ? ORDER BY is_active DESC, category_id ASC", "%" + keyword + "%", categoryId); // hàm tìm kiếm sản phẩm
        } else {
            list = PDAO.retrieve("name LIKE ? ORDER BY is_active DESC, category_id ASC", "%" + keyword + "%");
        }

        request.setAttribute("productList", list);
        request.setAttribute("categoryMap", ProductUtils.getCategoryMap(CDAO.retrieve("1=1")));
        return ADMIN_PRODUCT_MANAGEMENT_PAGE;
    }

    private String handleToProductForm(HttpServletRequest request, String action) {
        ProductDTO product = null;
        if (!"toCreateProduct".equals(action)) {
            int id = toInt(request.getParameter("productId"));
            if (ValidationUtils.isInvalidId(id)) {
                return error(request, "Invalid Id.");
            }

            product = PDAO.findById(id);
            if (product == null) {
                return error(request, "Product Not Found");
            }
        }

        prepareProductForm(request, product, action);
        return PRODUCT_FORM;
    }

    private String handleCreateProduct(HttpServletRequest request, HttpServletResponse response) {
        int categoryId = toInt(request.getParameter("categoryId"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String coverImageLink = request.getParameter("coverImageLink");

        ProductDTO product = new ProductDTO(categoryId, name, description, coverImageLink);
        boolean success = PDAO.create(product);

        request.setAttribute(success ? "message" : "errorMsg",
                success ? "Tạo sản phẩm thành công." : "Tạo sản phẩm thất bại.");

        return handleSearchProducts(request, response);
    }

    private String handleUpdateProduct(HttpServletRequest request, HttpServletResponse response) {
        int productId = toInt(request.getParameter("productId"));

        if (productId <= 0) {
            request.setAttribute("errorMsg", "ID sản phẩm không hợp lệ.");
            return handleSearchProducts(request, response);
        }

        ProductDTO existing = PDAO.findById(productId);
        if (existing == null) {
            request.setAttribute("errorMsg", "Sản phẩm không tồn tại.");
            return handleSearchProducts(request, response);
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

        return handleSearchProducts(request, response);
    }

    private String handleToggleIsActive(HttpServletRequest request, HttpServletResponse response) {
        int id = toInt(request.getParameter("productId"));
        
        System.out.println(id);
        
        if(ValidationUtils.isInvalidId(id)){
            request.setAttribute("errorMsg", "Invalid Id");
            return ERROR_PAGE;
        }
        
        ProductDTO product = PDAO.findById(id);
        
        if(product == null){
            request.setAttribute("errorMsg", "Product Not Found");
            return ERROR_PAGE;
        }
        
        product.setIs_active(!product.getIs_active());
        if(PDAO.update(product)){
            request.setAttribute("message", "Action Success");
            return handleSearchProducts(request, response);
        }else{
            request.setAttribute("errorMsg", "Product Not Found");
            return ERROR_PAGE;
        }
    }

    private void handleUploadProductImages(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        try {
            System.out.println("[DEBUG] Nhận request upload ảnh...");

            Part filePart = request.getPart("imageFile");
            if (filePart == null || filePart.getSize() == 0) {
                System.out.println("[DEBUG] Không có file nào được gửi lên.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Không có file nào được gửi lên.");
                return;
            }

            System.out.println("[DEBUG] Tên file: " + filePart.getSubmittedFileName());
            System.out.println("[DEBUG] Kích thước file: " + filePart.getSize());
            System.out.println("[DEBUG] Kiểu MIME: " + filePart.getContentType());

            String imageUrl = ImageUploadUtils.uploadFile(filePart);

            System.out.println("[DEBUG] Upload thành công. URL: " + imageUrl);
            response.getWriter().write(imageUrl);

        } catch (Exception e) {
            System.out.println("[ERROR] Lỗi upload ảnh:");
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Lỗi upload ảnh: " + e.getMessage());
        }
    }

    // Utility methods
    private int toInt(String val) {
        try {
            return Integer.parseInt(val.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private void prepareProductForm(HttpServletRequest request, ProductDTO product, String action) {
        request.setAttribute("categoryMap", ProductUtils.getCategoryMap(CDAO.retrieve("1=1")));
        request.setAttribute("actionType", "createProduct");
        if ("viewProductDetail".equals(action) || "toEditProduct".equals(action)) {
                request.setAttribute("actionType", action.equals("toEditProduct")? "updateProduct": "viewProductDetail");
                request.setAttribute("product", product);
                request.setAttribute("variation", VDAO.retrieve("product_id = ?", product.getId()));
                request.setAttribute("variationOption", VODAO.getOptionsByProductId(product.getId()));
        }
    }

    private String error(HttpServletRequest request, String msg) {
        request.setAttribute("errorMsg", msg);
        return ERROR_PAGE;
    }
}
