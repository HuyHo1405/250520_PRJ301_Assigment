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
import java.util.Map;
import model.dao.ProductConfigDAO;
import model.dao.ProductItemDAO;
import model.dao.VariationDAO;
import model.dao.VariationOptionDAO;
import model.dto.ProductConfigDTO;
import model.dto.ProductItemDTO;
import model.dto.VariationDTO;
import model.dto.VariationOptionDTO;
import utils.ProductUtils;
import utils.ValidationUtils;

@WebServlet(name = "AdminProductItemController", urlPatterns = {"/AdminProductItemController"})
public class AdminProductItemController extends HttpServlet {

    private static final String ERROR_PAGE = "error.jsp";
    private static final String ADMIN_PRODUCT_ITEM_MANAGEMENT = "admin-item-management.jsp";

    private static final VariationDAO VDAO = new VariationDAO();
    private static final VariationOptionDAO VODAO = new VariationOptionDAO();
    private static final ProductItemDAO PIDAO = new ProductItemDAO();
    private static final ProductConfigDAO PCDAO = new ProductConfigDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = "";
        try {
            String action = request.getParameter("action");

            switch (action) {
                case "toAdminProductItemPage":
                    prepareProductItemManagement(request);
                    url = ADMIN_PRODUCT_ITEM_MANAGEMENT;
                    break;
                case "updateProductItem":
                    url = handleUpdateProductItem(request, response);
                    break;
                case "toggleIsActiveProductItem":
                    url = handleToggleIsActiveProductItem(request, response);
                    break;
                case "addOption":
                    url = handleAddOption(request, response);
                    break;
                case "updateOption":
                case "removeOption":
                    url = handleUpdateOption(action, request, response);
                    break;
                case "addVariation":
                    url = handleAddVariation(request, response);
                    break;
                case "updateVariation":
                case "removeVariation":
                    url = handleUpdateVariation(request, response);
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

    private String handleUpdateProductItem(HttpServletRequest request, HttpServletResponse response) {
        int id = toInt(request.getParameter("itemId"));
        int stock = toInt(request.getParameter("stock"));
        double price = toDouble(request.getParameter("price"));

        if (ValidationUtils.isInvalidId(id) || stock < 0 || price <= 0) {
            request.setAttribute("errorMsg", "Invalid parameter");
            return ERROR_PAGE;
        }

        ProductItemDTO item = PIDAO.findById(id);
        if (item == null) {
            request.setAttribute("errorMsg", "Item not found");
            return ERROR_PAGE;
        }

        item.setPrice(price);
        item.setQuantity_in_stock(stock);

        PIDAO.update(item);
        prepareProductItemManagement(request);
        return ADMIN_PRODUCT_ITEM_MANAGEMENT;
    }

    private String handleToggleIsActiveProductItem(HttpServletRequest request, HttpServletResponse response) {
        int id = toInt(request.getParameter("itemId"));

        if (ValidationUtils.isInvalidId(id)) {
            request.setAttribute("errorMsg", "Invalid ID");
            return ERROR_PAGE;
        }

        ProductItemDTO item = PIDAO.findById(id);
        if (item == null) {
            request.setAttribute("errorMsg", "Item not found");
            return ERROR_PAGE;
        }

        PIDAO.toggleIsActive(item.getId(), item.getIs_active());
        prepareProductItemManagement(request);
        return ADMIN_PRODUCT_ITEM_MANAGEMENT;
    }

    private String handleAddOption(HttpServletRequest request, HttpServletResponse response) {
        String optionValue = request.getParameter("optionValue");
        int variationId = toInt(request.getParameter("variationId"));
        int productId = toInt(request.getParameter("productId"));

        if (ValidationUtils.isEmpty(optionValue) || ValidationUtils.isInvalidId(variationId) || ValidationUtils.isInvalidId(productId)) {
            request.setAttribute("errorMsg", "Invalid Parameter");
            return ERROR_PAGE;
        }

        VariationOptionDTO newOption = new VariationOptionDTO(-1, variationId, optionValue);

        int optionId = VODAO.createReturnId(newOption);
        if (optionId == -1) {
            request.setAttribute("errorMsg", "Internal Error");
            return ERROR_PAGE;
        }
        
        newOption.setId(optionId);
        if (!rebuildItemsWithOption(request, productId, newOption)) {
            return ERROR_PAGE;
        }

        prepareProductItemManagement(request);
        return ADMIN_PRODUCT_ITEM_MANAGEMENT;
    }

    private String handleAddVariation(HttpServletRequest request, HttpServletResponse response) {
        int productId = toInt(request.getParameter("productId"));
        String name = request.getParameter("variationName");

        if (ValidationUtils.isInvalidId(productId) || ValidationUtils.isEmpty(name)) {
            request.setAttribute("errorMsg", "Invalid Parameter");
            return ERROR_PAGE;
        }

        VDAO.create(new VariationDTO(productId, name));

        PIDAO.deleteByProductId(productId);

        List<VariationDTO> variations = VDAO.retrieve("product_id = ?", productId);
        List<VariationOptionDTO> options = VODAO.getOptionsByProductId(productId);

        Map<ProductItemDTO, List<VariationOptionDTO>> newComb = ProductUtils.generateComb(
                productId,
                variations,
                options
        );

        for (Map.Entry<ProductItemDTO, List<VariationOptionDTO>> entry : newComb.entrySet()) {
            ProductItemDTO item = entry.getKey();
            List<VariationOptionDTO> itemOptions = entry.getValue();

            int itemId = PIDAO.createReturnId(item);
            if (itemId == -1) {
                request.setAttribute("errorMsg", "Internal Error");
                return ERROR_PAGE;
            }

            for (VariationOptionDTO option : itemOptions) {
                PCDAO.create(new ProductConfigDTO(itemId, option.getId()));
            }
        }

        prepareProductItemManagement(request);
        return ADMIN_PRODUCT_ITEM_MANAGEMENT;
    }

    private String handleUpdateOption(String action, HttpServletRequest request, HttpServletResponse response) {
        int optionId = toInt(request.getParameter("optionId"));
        int productId = toInt(request.getParameter("productId"));
        String value = request.getParameter("optionValue");

        if ("updateOption".equals(action) && ValidationUtils.isEmpty(value)) {
            request.setAttribute("errorMsg", "Invalid Parameter");
            return ERROR_PAGE;
        }

        if (ValidationUtils.isInvalidId(optionId) || ValidationUtils.isInvalidId(productId)) {
            request.setAttribute("errorMsg", "Invalid id");
            return ERROR_PAGE;
        }

        VariationOptionDTO option = VODAO.findById(optionId);
        if (option == null) {
            request.setAttribute("errorMsg", "Option Not Found");
            return ERROR_PAGE;
        }

        PIDAO.deleteByOptionId(optionId);

        if ("updateOption".equals(action)) {
            option.setValue(value);
            VODAO.update(option);

            if (!rebuildItemsWithOption(request, productId, option)) {
                return ERROR_PAGE;
            }
        } else {
            VODAO.softDelete(optionId);
        }

        prepareProductItemManagement(request);

        return ADMIN_PRODUCT_ITEM_MANAGEMENT;
    }

    private String handleUpdateVariation(HttpServletRequest request, HttpServletResponse response) {
        int variationId = toInt(request.getParameter("variationId"));
        int productId = toInt(request.getParameter("productId"));
        String name = request.getParameter("variationName");
        String action = request.getParameter("action");
        
        if ("updateVariation".equals(action) && ValidationUtils.isEmpty(name)) {
            request.setAttribute("errorMsg", "Invalid Parameter");
            return ERROR_PAGE;
        }

        if (ValidationUtils.isInvalidId(variationId) || ValidationUtils.isInvalidId(productId)) {
            request.setAttribute("errorMsg", "Invalid id");
            return ERROR_PAGE;
        }

        VariationDTO variation = VDAO.findById(variationId);
        if (variation == null) {
            request.setAttribute("errorMsg", "Variation Not Found");
            return ERROR_PAGE;
        }

        if ("updateVariation".equals(action)) {
            variation.setName(name);
            VDAO.update(variation);
        } else {
            //xóa variation
            VDAO.softDelete(variationId);
            
            // xóa toàn bộ item
            List<VariationOptionDTO> optionList = VODAO.retrieve("variation_id = ?", variationId);
            for (VariationOptionDTO option : optionList) {
                PIDAO.deleteByOptionId(option.getId());
            }
            
            //tạo item mới
            if(!rebuildItemsWithVariation(productId, variation)){
                 return ERROR_PAGE;
            }
        }

        prepareProductItemManagement(request);
        return ADMIN_PRODUCT_ITEM_MANAGEMENT;
    }

    private boolean rebuildItemsWithVariation(int productId, VariationDTO variation) {
        List<VariationDTO> variations = VDAO.retrieve("product_id = ? AND is_deleted = 0", productId);
        List<VariationOptionDTO> allOptions = VODAO.getOptionsByProductId(productId);  

        Map<ProductItemDTO, List<VariationOptionDTO>> combinations = ProductUtils.generateComb(productId, variations, allOptions);

        for (Map.Entry<ProductItemDTO, List<VariationOptionDTO>> entry : combinations.entrySet()) {
            int itemId = PIDAO.createReturnId(entry.getKey());
            if (itemId <= 0) {
                return false;
            }

            for (VariationOptionDTO option : entry.getValue()) {
                if (!PCDAO.create(new ProductConfigDTO(itemId, option.getId()))) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean rebuildItemsWithOption(HttpServletRequest request, int productId, VariationOptionDTO newOption) {
        List<VariationDTO> variations = VDAO.retrieve("product_id = ? AND is_deleted = 0", productId);
        List<VariationOptionDTO> currentOptions = VODAO.getOptionsByProductId(productId);

        Map<ProductItemDTO, List<VariationOptionDTO>> newComb = ProductUtils.generateNewOptionComb(
                productId,
                newOption,
                variations,
                currentOptions
        );

        for (Map.Entry<ProductItemDTO, List<VariationOptionDTO>> entry : newComb.entrySet()) {
            ProductItemDTO item = entry.getKey();
            int itemId = PIDAO.createReturnId(item);
            if (itemId != -1) {
                for (VariationOptionDTO opt : entry.getValue()) {
                    PCDAO.create(new ProductConfigDTO(itemId, opt.getId()));
                }
            } else {
                request.setAttribute("errorMsg", "Internal Error");
                return false;
            }
        }
        return true;
    }
    
    private void prepareProductItemManagement(HttpServletRequest request) {
        int productId = toInt(request.getParameter("productId"));
        int editId = toInt(request.getParameter("editId"));
        if (!ValidationUtils.isInvalidId(productId)) {
            if (!ValidationUtils.isInvalidId(editId)) {
                request.setAttribute("editId", editId);
            }
            request.setAttribute("variationList", VDAO.retrieve("product_id = ? AND is_deleted = 0", productId));
            request.setAttribute("optionList", VODAO.retrieve("is_deleted = 0"));
            request.setAttribute("itemList", PIDAO.retrieve("product_id = ? AND is_deleted = 0", productId));
        }
    }

    private int toInt(String val) {
        try {
            return Integer.parseInt(val.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private double toDouble(String str) {
        try {
            return Double.parseDouble(str.trim());
        } catch (Exception e) {
            return -1;
        }
    }
}
