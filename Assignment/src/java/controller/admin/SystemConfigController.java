package controller.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.dao.CategoryDAO;
import model.dao.CountryDAO;
import model.dao.OrderStatusDAO;
import model.dao.PaymentTypeDAO;
import model.dao.ShippingMethodDAO;
import model.dto.CategoryDTO;
import model.dto.CountryDTO;
import model.dto.OrderStatusDTO;
import model.dto.PaymentTypeDTO;
import model.dto.ShippingMethodDTO;
import utils.CacheManager;
import utils.ValidationUtils;

/**
 * Servlet controller for managing system configurations, including:
 * order statuses, payment types, shipping methods, countries, and categories.
 * Handles actions such as add/update/toggle visibility and clearing system cache.
 * 
 * Accessible via the /SystemConfigController URL.
 */
@WebServlet(name = "SystemConfigController", urlPatterns = {"/SystemConfigController"})
public class SystemConfigController extends HttpServlet {

    private static final String SYSTEM_CONFIG_MANAGEMENT_PAGE = "system-config-management.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String APP_VERSION = "1.0.0";

    private final OrderStatusDAO OSDAO = new OrderStatusDAO();
    private final PaymentTypeDAO PTDAO = new PaymentTypeDAO();
    private final ShippingMethodDAO SMDAO = new ShippingMethodDAO();
    private final CountryDAO CDAO = new CountryDAO();
    private final CategoryDAO CADAO = new CategoryDAO();

    /**
     * Core method that dispatches requests based on the 'action' parameter.
     * Supports loading, updating, adding, toggling config entries, and clearing cache.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException if servlet error occurs
     * @throws IOException      if I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = "";
        try {
            String action = request.getParameter("action");

            switch (action) {
                case "toSystemConfigManagement":
                    System.out.println("here");
                    prepareManagementView(request);
                    url = SYSTEM_CONFIG_MANAGEMENT_PAGE;
                    break;
                case "getSystemConfig":
                    url = prepareManagementView(request);
                    break;
                case "updateSystemConfig":
                    url = handleUpdateSystemConfig(request, response);
                    break;
                case "addSystemConfig":
                    url = handleAddSystemConfig(request, response);
                    break;
                case "toggleIsActiveSystemConfig":
                    url = handleToggleIsActive(request, response);
                    break;
                case "clearSystemCache":
                    url = handleClearSystemCache(request, response);
                    break;
                default:
                    url = ERROR_PAGE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            url = ERROR_PAGE;
        } finally {
            System.out.println(url);
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

    /**
     * Safely parses a string to integer.
     *
     * @param n the string to parse
     * @return parsed int, or -1 on error
     */
    private int toInt(String n) {
        try {
            return Integer.parseInt(n);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Safely parses a string to double.
     *
     * @param n the string to parse
     * @return parsed double, or -1 on error
     */
    private double toDouble(String n) {
        try {
            return Double.parseDouble(n.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Prepares data for the system configuration management view.
     * Loads all necessary config types and sets them into request attributes.
     *
     * @param request the current HTTP request
     * @return the path to the management page
     */
    private String prepareManagementView(HttpServletRequest request) {
        List<OrderStatusDTO> orderStatusList = OSDAO.retrieve("1 = 1 ORDER BY is_active DESC");
        List<PaymentTypeDTO> paymentTypeList = PTDAO.retrieve("1 = 1 ORDER BY is_active DESC");
        List<ShippingMethodDTO> shippingMethodList = SMDAO.retrieve("1 = 1 ORDER BY is_active DESC");
        List<CountryDTO> countryList = CDAO.retrieve("1 = 1 ORDER BY is_active DESC");
        List<CategoryDTO> categoryList = CADAO.retrieve("1 = 1 ORDER BY is_active DESC"); // ✅ thêm dòng này

        String editId = request.getParameter("editId");
        String editType = request.getParameter("editType");
        String addType = request.getParameter("addModeType");

        request.setAttribute("appVersion", APP_VERSION);
        request.setAttribute("editType", editType);
        request.setAttribute("addModeType", addType);
        request.setAttribute("editId", editId);
        request.setAttribute("orderStatusList", orderStatusList);
        request.setAttribute("paymentTypeList", paymentTypeList);
        request.setAttribute("shippingMethodList", shippingMethodList);
        request.setAttribute("countryList", countryList);
        request.setAttribute("categoryList", categoryList); // ✅ truyền category xuống JSP

        return SYSTEM_CONFIG_MANAGEMENT_PAGE;
    }

    /**
     * Handles update operations for different config types based on request parameters.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the management page view
     */
    private String handleUpdateSystemConfig(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
        System.out.println(type);
        switch (type) {
            case "orderStatus": {
                int id = toInt(request.getParameter("id"));
                String status = request.getParameter("value");
                
                if (ValidationUtils.isInvalidId(id) || ValidationUtils.isEmpty(status)) {
                    request.setAttribute("error", "Thông tin trạng thái đơn hàng không hợp lệ.");
                    prepareManagementView(request);
                    return SYSTEM_CONFIG_MANAGEMENT_PAGE;
                }

                OSDAO.update(new OrderStatusDTO(id, status.trim(), isActive));
                break;
            }

            case "paymentType": {
                int id = toInt(request.getParameter("id"));
                String value = request.getParameter("value");

                if (ValidationUtils.isInvalidId(id) || ValidationUtils.isEmpty(value)) {
                    request.setAttribute("error", "Thông tin phương thức thanh toán không hợp lệ.");
                    prepareManagementView(request);
                    return SYSTEM_CONFIG_MANAGEMENT_PAGE;
                }

                PTDAO.update(new PaymentTypeDTO(id, value.trim(), isActive));
                break;
            }

            case "shippingMethod": {
                int id = toInt(request.getParameter("id"));
                String name = request.getParameter("value");
                double price = toDouble(request.getParameter("price"));

                if (ValidationUtils.isInvalidId(id) || ValidationUtils.isEmpty(name) || price < 0) {
                    request.setAttribute("error", "Thông tin phương thức giao hàng không hợp lệ.");
                    prepareManagementView(request);
                    return SYSTEM_CONFIG_MANAGEMENT_PAGE;
                }

                SMDAO.update(new ShippingMethodDTO(id, name.trim(), price, isActive));
                break;
            }

            case "country": {
                int id = toInt(request.getParameter("id"));
                String name = request.getParameter("value");

                if (ValidationUtils.isInvalidId(id) || ValidationUtils.isEmpty(name)) {
                    request.setAttribute("error", "Thông tin quốc gia không hợp lệ.");
                    prepareManagementView(request);
                    return SYSTEM_CONFIG_MANAGEMENT_PAGE;
                }

                CDAO.update(new CountryDTO(id, name.trim(), isActive));
                break;
            }
            
            case "category": {
                int id = toInt(request.getParameter("id"));
                String name = request.getParameter("value");
                int parentId = toInt(request.getParameter("parentId")); // có thể là -1

                if (ValidationUtils.isInvalidId(id) || ValidationUtils.isEmpty(name)) {
                    request.setAttribute("error", "Thông tin danh mục không hợp lệ.");
                    prepareManagementView(request);
                    return SYSTEM_CONFIG_MANAGEMENT_PAGE;
                }

                CategoryDTO category = new CategoryDTO(id, parentId, name.trim(), isActive); // constructor cần đủ
                CADAO.update(category);
                break;
            }
            
            default:
                request.setAttribute("error", "Loại cấu hình không hợp lệ.");
                prepareManagementView(request);
                return SYSTEM_CONFIG_MANAGEMENT_PAGE;
        }

        prepareManagementView(request);
        return SYSTEM_CONFIG_MANAGEMENT_PAGE;
    }

    /**
     * Clears application-level cache and returns to error page with a message.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return error page path
     */
    private String handleClearSystemCache(HttpServletRequest request, HttpServletResponse response) {
        CacheManager.clear();

        request.setAttribute("message", "Đã xoá cache hệ thống thành công.");
        return ERROR_PAGE;
    }

    /**
     * Handles add operations for different config types based on request parameters.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the management page view
     */
    private String handleAddSystemConfig(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        String isActive = request.getParameter("isActive");

        System.out.println(type);
        System.out.println(isActive);
        
        switch (type) {
            case "orderStatus": {
                String status = request.getParameter("value");
                if (ValidationUtils.isEmpty(status)) {
                    request.setAttribute("error", "Tên trạng thái đơn hàng không được để trống.");
                    prepareManagementView(request);
                    return SYSTEM_CONFIG_MANAGEMENT_PAGE;
                }
                OSDAO.create(new OrderStatusDTO(status.trim(), Boolean.parseBoolean(isActive)));
                break;
            }

            case "paymentType": {
                String value = request.getParameter("value");
                if (ValidationUtils.isEmpty(value)) {
                    request.setAttribute("error", "Tên phương thức thanh toán không được để trống.");
                    prepareManagementView(request);
                    return SYSTEM_CONFIG_MANAGEMENT_PAGE;
                }
                PTDAO.create(new PaymentTypeDTO(value.trim(), Boolean.parseBoolean(isActive)));
                break;
            }

            case "shippingMethod": {
                String name = request.getParameter("value");
                double price = toDouble(request.getParameter("price"));
                if (ValidationUtils.isEmpty(name) || price < 0) {
                    request.setAttribute("error", "Tên hoặc giá phương thức giao hàng không hợp lệ.");
                    prepareManagementView(request);
                    return SYSTEM_CONFIG_MANAGEMENT_PAGE;
                }
                SMDAO.create(new ShippingMethodDTO(name.trim(), price, Boolean.parseBoolean(isActive)));
                break;
            }

            case "country": {
                String name = request.getParameter("value");
                if (ValidationUtils.isEmpty(name)) {
                    request.setAttribute("error", "Tên quốc gia không được để trống.");
                    prepareManagementView(request);
                    return SYSTEM_CONFIG_MANAGEMENT_PAGE;
                }
                CDAO.create(new CountryDTO(name.trim(), Boolean.parseBoolean(isActive)));
                break;
            }
            
            case "category": {
                String name = request.getParameter("value");
                int parentId = toInt(request.getParameter("parentId"));
                if (ValidationUtils.isEmpty(name)) {
                    request.setAttribute("error", "Tên danh mục không được để trống.");
                    prepareManagementView(request);
                    return SYSTEM_CONFIG_MANAGEMENT_PAGE;
                }
                CADAO.create(new CategoryDTO(parentId, name.trim(), Boolean.parseBoolean(isActive)));
                break;
            }

            default:
                request.setAttribute("error", "Loại cấu hình không hợp lệ.");
                prepareManagementView(request);
                return SYSTEM_CONFIG_MANAGEMENT_PAGE;
        }

        prepareManagementView(request);
        return SYSTEM_CONFIG_MANAGEMENT_PAGE;
    }

    /**
     * Toggles the active status (visibility) of a specific configuration entry.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the management page view
     */
    private String handleToggleIsActive(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        int id = toInt(request.getParameter("id"));
        String currStatus = request.getParameter("status");
        
        if (ValidationUtils.isInvalidId(id)) {
            request.setAttribute("errorMsg", "Invalid Id");
            prepareManagementView(request);
            return SYSTEM_CONFIG_MANAGEMENT_PAGE;
        }
        
        if (currStatus == null || (!currStatus.equals("true") && !currStatus.equals("false"))){
            request.setAttribute("errorMsg", "Invalid status");
            prepareManagementView(request);
            return SYSTEM_CONFIG_MANAGEMENT_PAGE;
        }

        boolean success = false;
        String successMessage = "Đã vô hiệu hoá cấu hình thành công.";
        String errorMessage = "Không thể vô hiệu hoá cấu hình.";

        switch (type) {
            case "orderStatus":
                success = OSDAO.toggleIsActive(id, Boolean.parseBoolean(currStatus));
                break;
            case "paymentType":
                success = PTDAO.toggleIsActive(id, Boolean.parseBoolean(currStatus));
                break;
            case "shippingMethod":
                success = SMDAO.toggleIsActive(id, Boolean.parseBoolean(currStatus));
                break;
            case "country":
                success = CDAO.toggleIsActive(id, Boolean.parseBoolean(currStatus));
                break;
            case "category":
                success = CADAO.toggleIsActive(id, Boolean.parseBoolean(currStatus));
                break;
            default:
                request.setAttribute("error", "Loại cấu hình không hợp lệ.");
                prepareManagementView(request);
                return SYSTEM_CONFIG_MANAGEMENT_PAGE;
        }

        if (success) {
            request.setAttribute("message", successMessage);
        } else {
            request.setAttribute("error", errorMessage);
        }

        prepareManagementView(request); // Refresh data after operation
        return SYSTEM_CONFIG_MANAGEMENT_PAGE;
    }

}