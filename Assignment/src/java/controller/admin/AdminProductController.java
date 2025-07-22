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
 * The `AdminProductController` servlet handles administrative operations related to
 * product management, including viewing, creating, editing, activating/deactivating,
 * searching products, and uploading product images.
 * It interacts with various Data Access Objects (DAOs) to manage product-related data.
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

    /**
     * Processes requests for both HTTP `GET` and `POST` methods.
     * This method acts as a central dispatcher for various product-related actions
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
     * Handles the HTTP `GET` method.
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
     * Handles the HTTP `POST` method.
     * This method specifically handles `multipart/form-data` requests for file uploads,
     * particularly for `uploadProductImages` action, and delegates other POST requests
     * to `processRequest`.
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

    /**
     * Handles the search functionality for products.
     * Retrieves products based on a keyword and an optional category ID.
     * Sets the product list and category map as request attributes.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The path to the admin product management page.
     */
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

    /**
     * Prepares the product form for creation, editing, or viewing product details.
     * Retrieves product data if an ID is provided and sets relevant attributes
     * for the JSP form.
     *
     * @param request The HttpServletRequest object.
     * @param action The action type (e.g., "toCreateProduct", "toEditProduct", "viewProductDetail").
     * @return The path to the product form JSP page or an error page.
     */
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

    /**
     * Handles the creation of a new product.
     * Retrieves product details from request parameters, creates a `ProductDTO` object,
     * and attempts to persist it to the database.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The path to the admin product management page after creation attempt.
     */
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

    /**
     * Handles the update of an existing product.
     * Retrieves product ID and updated details from request parameters,
     * finds the existing product, updates its properties, and persists changes
     * to the database.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The path to the admin product management page after update attempt.
     */
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

    /**
     * Toggles the `is_active` status of a product (activates or deactivates it).
     * Retrieves the product by ID, flips its `is_active` status, and updates it
     * in the database.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The path to the admin product management page or an error page.
     */
    private String handleToggleIsActive(HttpServletRequest request, HttpServletResponse response) {
        int id = toInt(request.getParameter("productId"));

        System.out.println(id);

        if (ValidationUtils.isInvalidId(id)) {
            request.setAttribute("errorMsg", "Invalid Id");
            return ERROR_PAGE;
        }

        ProductDTO product = PDAO.findById(id);

        if (product == null) {
            request.setAttribute("errorMsg", "Product Not Found");
            return ERROR_PAGE;
        }

        product.setIs_active(!product.getIs_active());
        if (PDAO.update(product)) {
            request.setAttribute("message", "Action Success");
            return handleSearchProducts(request, response);
        } else {
            request.setAttribute("errorMsg", "Product Not Found");
            return ERROR_PAGE;
        }
    }

    /**
     * Handles the upload of product images.
     * Retrieves the image file from the multipart request, uploads it using
     * `ImageUploadUtils`, and sends the generated image URL back as a response.
     *
     * @param request The HttpServletRequest object, containing the multipart file.
     * @param response The HttpServletResponse object to write the image URL or error message.
     * @throws IOException if an I/O error occurs during file processing or response writing.
     */
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

    /**
     * Converts a string value to an integer.
     * Returns -1 if the string is null or cannot be parsed as an integer.
     *
     * @param val The string to convert.
     * @return The integer value, or -1 if conversion fails.
     */
    private int toInt(String val) {
        try {
            return Integer.parseInt(val.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Prepares the product form with necessary data for display.
     * This includes category data, action type (create, update, view),
     * product details, and associated variation and variation option data.
     *
     * @param request The HttpServletRequest object.
     * @param product The `ProductDTO` object to pre-populate the form (can be null for creation).
     * @param action The action type (e.g., "toCreateProduct", "toEditProduct", "viewProductDetail").
     */
    private void prepareProductForm(HttpServletRequest request, ProductDTO product, String action) {
        request.setAttribute("categoryMap", ProductUtils.getCategoryMap(CDAO.retrieve("is_active = 1")));
        request.setAttribute("actionType", "createProduct");
        if ("viewProductDetail".equals(action) || "toEditProduct".equals(action)) {
            request.setAttribute("actionType", action.equals("toEditProduct") ? "updateProduct" : "viewProductDetail");
            request.setAttribute("product", product);
            request.setAttribute("variation", VDAO.retrieve("product_id = ? and is_deleted = 0", product.getId()));
            request.setAttribute("variationOption", VODAO.getOptionsByProductId(product.getId()));
        }
    }

    /**
     * Sets an error message as a request attribute and returns the path to the
     * error page.
     *
     * @param request The HttpServletRequest object.
     * @param msg The error message to set.
     * @return The path to the error page.
     */
    private String error(HttpServletRequest request, String msg) {
        request.setAttribute("errorMsg", msg);
        return ERROR_PAGE;
    }
}