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
import model.dao.CountryDAO;
import model.dao.OrderStatusDAO;
import model.dao.PaymentTypeDAO;
import model.dao.ShippingMethodDAO;
import model.dto.CountryDTO;
import model.dto.OrderStatusDTO;
import model.dto.PaymentTypeDTO;
import model.dto.ShippingMethodDTO;
import utils.CacheManager;
import utils.ValidationUtils;

/**
 * Status: Chờ thực hiện Người thực hiện: [...........] Ngày bắt đầu:
 * [...........] viết servlet cho System Config Controller
 */
@WebServlet(name = "SystemConfigController", urlPatterns = {"/SystemConfigController"})
public class SystemConfigController extends HttpServlet {

    private static final String WELCOME_PAGE = "welcome.jsp";
    private static final String SYSTEM_CONFIG_PAGE = "system-config.jsp";
    private static final String SYSTEM_CONFIG_MANAGEMENT_PAGE = "system-config-management.jsp";
    private static final String ERROR_PAGE = "error.jsp";

    private final OrderStatusDAO OSDAO = new OrderStatusDAO();
    private final PaymentTypeDAO PTDAO = new PaymentTypeDAO();
    private final ShippingMethodDAO SMDAO = new ShippingMethodDAO();
    private final CountryDAO CDAO = new CountryDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = WELCOME_PAGE;
        try {
            String action = request.getParameter("action");

            switch (action) {
                case "toSystemConfigManagement":
                    url = SYSTEM_CONFIG_MANAGEMENT_PAGE;
                    break;
                case "toSystemConfig":
                    url = SYSTEM_CONFIG_PAGE;
                    break;
                case "getSystemConfig":
                    url = prepareManagementView(request);
                    break;
                case "updateSystemConfig":
                    url = handleUpdateSystemConfig(request, response);
                    break;
                case "getAppVersion":
                    url = handleGetAppVersion(request, response);
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

    //some useful methods
    private int toInt(String n) {
        try {
            return Integer.parseInt(n);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double toDouble(String n) {
        try {
            return Double.parseDouble(n);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Hàm tổng hợp, gọi cả 4 hàm trên
    private String prepareManagementView(HttpServletRequest request) {
        List<OrderStatusDTO> orderStatusList = OSDAO.retrieve("1 = 1");
        List<PaymentTypeDTO> paymentTypeList = PTDAO.retrieve("1 = 1");
        List<ShippingMethodDTO> shippingMethodList = SMDAO.retrieve("1 = 1");
        List<CountryDTO> countryList = CDAO.retrieve("1 = 1");

        request.setAttribute("orderStatusList", orderStatusList);
        request.setAttribute("paymentTypeList", paymentTypeList);
        request.setAttribute("shippingMethodList", shippingMethodList);
        request.setAttribute("countryList", countryList);

        return SYSTEM_CONFIG_MANAGEMENT_PAGE;
    }

    private String handleUpdateSystemConfig(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");

        switch (type) {
            case "orderStatus": {
                int id = toInt(request.getParameter("id"));
                String status = request.getParameter("status");

                if (ValidationUtils.isInvalidId(id) || ValidationUtils.isEmpty(status)) {
                    request.setAttribute("error", "Thông tin trạng thái đơn hàng không hợp lệ.");
                    return SYSTEM_CONFIG_PAGE;
                }

                OSDAO.update(new OrderStatusDTO(id, status.trim()));
                break;
            }

            case "paymentType": {
                int id = toInt(request.getParameter("id"));
                String value = request.getParameter("value");

                if (ValidationUtils.isInvalidId(id) || ValidationUtils.isEmpty(value)) {
                    request.setAttribute("error", "Thông tin phương thức thanh toán không hợp lệ.");
                    return SYSTEM_CONFIG_PAGE;
                }

                PTDAO.update(new PaymentTypeDTO(id, value.trim()));
                break;
            }

            case "shippingMethod": {
                int id = toInt(request.getParameter("id"));
                String name = request.getParameter("name");
                double price = toDouble(request.getParameter("price"));

                if (ValidationUtils.isInvalidId(id) || ValidationUtils.isEmpty(name) || price < 0) {
                    request.setAttribute("error", "Thông tin phương thức giao hàng không hợp lệ.");
                    return SYSTEM_CONFIG_PAGE;
                }

                SMDAO.update(new ShippingMethodDTO(id, name.trim(), price));
                break;
            }

            case "country": {
                int id = toInt(request.getParameter("id"));
                String name = request.getParameter("name");

                if (ValidationUtils.isInvalidId(id) || ValidationUtils.isEmpty(name)) {
                    request.setAttribute("error", "Thông tin quốc gia không hợp lệ.");
                    return SYSTEM_CONFIG_PAGE;
                }

                CDAO.update(new CountryDTO(id, name.trim()));
                break;
            }

            default:
                request.setAttribute("error", "Loại cấu hình không hợp lệ.");
                return SYSTEM_CONFIG_PAGE;
        }

        prepareManagementView(request);
        return SYSTEM_CONFIG_MANAGEMENT_PAGE;
    }

    private String handleGetAppVersion(HttpServletRequest request, HttpServletResponse response) {
        String version = "1.0.0";
        request.setAttribute("appVersion", version);
        return SYSTEM_CONFIG_PAGE;
    }

    private String handleClearSystemCache(HttpServletRequest request, HttpServletResponse response) {
        CacheManager.clear();

        request.setAttribute("message", "Đã xoá cache hệ thống thành công.");
        return SYSTEM_CONFIG_PAGE;
    }

}
