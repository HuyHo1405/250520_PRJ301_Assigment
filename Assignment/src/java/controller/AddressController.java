/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.dao.AddressDAO;
import model.dao.CountryDAO;
import model.dao.UserAddressDAO;
import model.dto.AddressDTO;
import model.dto.UserAddressDTO;
import utils.DbUtils;
import utils.UserUtils;
import utils.ValidationUtils;

/**
 *
 * @author ho huy
 */
@WebServlet(name = "AddressController", urlPatterns = {"/AddressController"})
public class AddressController extends HttpServlet {

    //static fields
    private static final String WELCOME_PAGE = "welcome.jsp";
    private static final String ADDRESS_FORM_PAGE = "address-form.jsp";
    private static final String ADDRESS_MANAGEMENT_PAGE = "address-management.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private final CountryDAO CDAO = new CountryDAO();
    private final AddressDAO ADAO = new AddressDAO();
    private final UserAddressDAO UADAO = new UserAddressDAO();

    //process
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = WELCOME_PAGE;
        try {
            String action = request.getParameter("action");

            switch (action) {
                case "toAddAddress":
                    request.setAttribute("countries", CDAO.retrieve("1 = 1"));
                    request.setAttribute("actionType", "addAddress");
                    url = ADDRESS_FORM_PAGE;
                    break;
                case "toEditAddress":
                    request.setAttribute("countries", CDAO.retrieve("1 = 1"));
                    request.setAttribute("actionType", "editAddress");
                    url = ADDRESS_FORM_PAGE;
                    break;
                case "toAddressManagement":
                    prepareAddressManagementView(request, UserUtils.getUserId(request));
                    url = ADDRESS_MANAGEMENT_PAGE;
                    break;
                case "addAddress":
                    url = handleAddAddress(request, response);
                    break;
                case "updateAddress":
                    url = handleUpdateAddress(request, response);
                    break;
                case "updateDefaultAddress":
                    url = handleUpdateDefaultAddress(request, response);
                    break;
                case "removeAddress":
                    url = handleDeleteAddress(request, response);
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

    //doGet & doPost
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

    //handle methods
    public String handleAddAddress(HttpServletRequest request, HttpServletResponse response) {
        int countryId = toInt(request.getParameter("countryId"));
        String unitNum = request.getParameter("unitNumber");
        String streetNum = request.getParameter("streetNumber");
        String addressLine1 = request.getParameter("addressLine1");
        String addressLine2 = request.getParameter("addressLine2");
        String city = request.getParameter("city");
        String region = request.getParameter("region");

        String error;
        if((error = validateField(countryId, unitNum, streetNum, addressLine1, addressLine2, city, region)) != null){
            request.setAttribute("error", error);
            return ADDRESS_FORM_PAGE;
        }
        
        int addressId = ADAO.createAndReturnId(new AddressDTO(countryId, unitNum, streetNum, addressLine1, addressLine2, city, region));
        int userId = UserUtils.getUser(request).getId();
        UADAO.create(new UserAddressDTO(userId, addressId));
        prepareAddressManagementView(request, UserUtils.getUserId(request));
        return ADDRESS_MANAGEMENT_PAGE;
    }

    public String handleUpdateAddress(HttpServletRequest request, HttpServletResponse response) {
        int addressId = toInt(request.getParameter("addressId"));
        int countryId = toInt(request.getParameter("countryId"));
        String unitNum = request.getParameter("unitNumber");
        String streetNum = request.getParameter("streetNumber");
        String addressLine1 = request.getParameter("addressLine1");
        String addressLine2 = request.getParameter("addressLine2");
        String city = request.getParameter("city");
        String region = request.getParameter("region");
        
        String error;
        if((error = validateField(countryId, unitNum, streetNum, addressLine1, addressLine2, city, region)) != null){
            request.setAttribute("error", error);
            return ADDRESS_FORM_PAGE;
        }
        
        ADAO.update(new AddressDTO(addressId, countryId, unitNum, streetNum, addressLine1, addressLine2, city, region));
        request.setAttribute("defaultAddress", getDefaultAddress(UserUtils.getUserId(request)));
        request.setAttribute("otherAddresses", getOtherAddress("1 = 1", UserUtils.getUserId(request)));
        return ADDRESS_MANAGEMENT_PAGE;
    }

    public String handleUpdateDefaultAddress(HttpServletRequest request, HttpServletResponse response) {
        int addressId = toInt(request.getParameter("addressId"));
        if(ValidationUtils.isInvalidId(addressId)){
            request.setAttribute("error", "Không tìm thấy địa chỉ");
            return ADDRESS_MANAGEMENT_PAGE;
        }
        
        int userId = UserUtils.getUserId(request);
        AddressDTO cur = getDefaultAddress(userId);
        if(cur != null){
            UADAO.update(new UserAddressDTO(userId, cur.getId(), false));
        }
        
        UADAO.update(new UserAddressDTO(userId, addressId, true));
        prepareAddressManagementView(request, UserUtils.getUserId(request));
        return ADDRESS_MANAGEMENT_PAGE;
    }

    public String handleDeleteAddress(HttpServletRequest request, HttpServletResponse response) {
        int addressId = toInt(request.getParameter("addressId"));
        if(ValidationUtils.isInvalidId(addressId)){
            request.setAttribute("error", "Không tìm thấy địa chỉ");
            return ADDRESS_MANAGEMENT_PAGE;
        }
        int userId = UserUtils.getUserId(request);
        UADAO.delete(userId, addressId);
        prepareAddressManagementView(request, UserUtils.getUserId(request));
        return ADDRESS_MANAGEMENT_PAGE;
    }

    //joint table
    private AddressDTO getDefaultAddress(int userId) {
        String sql = "SELECT a.* FROM address a "
                + "JOIN user_address ua ON a.id = ua.address_id "
                + "WHERE ua.user_id = ? AND ua.is_default = 1";

        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { 
                    return new AddressDTO(
                        rs.getInt("id"),
                        rs.getInt("country_id"),
                        rs.getString("unit_number"),
                        rs.getString("street_number"),
                        rs.getString("address_line1"),
                        rs.getString("address_line2"),
                        rs.getString("city"),
                        rs.getString("region")
                );
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<AddressDTO> getOtherAddress(String condition, Object... params) {
        String sql = "SELECT a.* FROM address a "
                + "JOIN user_address ua ON a.id = ua.address_id "
                + "WHERE ua.is_default = 0 AND ua.user_id = ? AND " + condition;

        List<AddressDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new AddressDTO(
                            rs.getInt("id"),
                            rs.getInt("country_id"),
                            rs.getString("unit_number"),
                            rs.getString("street_number"),
                            rs.getString("address_line1"),
                            rs.getString("address_line2"),
                            rs.getString("city"),
                            rs.getString("region")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //some useful methods
    private int toInt(String n) {
        try {
            return Integer.parseInt(n);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String validateField(int countryId, String unitNum, String streetNum, String addressLine1, String addressLine2, String city, String region){
        if (ValidationUtils.isInvalidId(countryId)
                || ValidationUtils.isEmpty(unitNum) || ValidationUtils.isEmpty(streetNum) || ValidationUtils.isEmpty(addressLine1)
                || ValidationUtils.isEmpty(city) || ValidationUtils.isEmpty(region)) {
            return "Vui lòng điền đầy đủ thông tin.";
        }
        
        String error;
        if ((error = ValidationUtils.checkLength("unit number", unitNum, 20)) != null
                || (error = ValidationUtils.checkLength("street number", streetNum, 20)) != null
                || (error = ValidationUtils.checkLength("address line 1", addressLine1, 255)) != null
                || (error = ValidationUtils.checkLength("address line 2", addressLine2, 255)) != null
                || (error = ValidationUtils.checkLength("city", city, 100)) != null
                || (error = ValidationUtils.checkLength("region", region, 100)) != null) {
            return error;
        }

        if (addressLine2 != null && addressLine2.length() > 255) {
            return "Địa chỉ dòng 2 quá dài (tối đa 255 ký tự).";
        }
        return null;
    }
    
    private void prepareAddressManagementView(HttpServletRequest request, int userId) {
        request.setAttribute("defaultAddress", getDefaultAddress(userId));
        request.setAttribute("otherAddresses", getOtherAddress("1 = 1", userId));
    }
}
