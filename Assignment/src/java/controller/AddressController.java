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
 * AddressController manages address-related actions including creation,
 * update, search, deletion, and selection of default address.
 *
 * It interacts with Address, Country, and UserAddress DAOs to perform operations
 * and routes users to relevant JSP pages.
 *
 * URL pattern: /AddressController
 */
@WebServlet(name = "AddressController", urlPatterns = {"/AddressController"})
public class AddressController extends HttpServlet {

    private static final String WELCOME_PAGE = "welcome.jsp";
    private static final String PROFILE_PAGE = "profile.jsp";
    private static final String ADDRESS_FORM_PAGE = "address-form.jsp";
    private static final String ADDRESS_MANAGEMENT_PAGE = "address-management.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private final CountryDAO CDAO = new CountryDAO();
    private final AddressDAO ADAO = new AddressDAO();
    private final UserAddressDAO UADAO = new UserAddressDAO();

    /**
     * Main controller method that processes all incoming requests and
     * delegates them based on the "action" parameter.
     *
     * @param request  HTTP servlet request
     * @param response HTTP servlet response
     * @throws ServletException if servlet error occurs
     * @throws IOException      if I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = WELCOME_PAGE;
        try {
            String action = request.getParameter("action");

            switch (action) {
                case "toProfile":
                    prepareAddressManagementView(request, UserUtils.getUserId(request));
                    url = PROFILE_PAGE;
                    break;
                case "toAddAddress":
                    request.setAttribute("countries", CDAO.retrieve("1 = 1"));
                    request.setAttribute("actionType", "addAddress");
                    url = ADDRESS_FORM_PAGE;
                    break;
                case "toEditAddress":
                    request.setAttribute("countries", CDAO.retrieve("1 = 1"));
                    request.setAttribute("actionType", "updateAddress");
                    url = ADDRESS_FORM_PAGE;
                    break;
                case "toAddressManagement":
                    prepareAddressManagementView(request, UserUtils.getUserId(request));
                    url = ADDRESS_MANAGEMENT_PAGE;
                    break;
                case "addAddress":
                    url = handleAddAddress(request, response);
                    break;
                case "searchAddress":
                    url = handleSearchAddress(request, response);
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
     * Handles creation of a new address for the current user.
     * Validates fields and stores address data in database.
     *
     * @param request  servlet request
     * @param response servlet response
     * @return the destination JSP page
     */
    private String handleAddAddress(HttpServletRequest request, HttpServletResponse response) {
        AddressDTO address = extractAddressFromRequest(request, -1);
        String error = validateAddressFields(address);

        if (error != null) {
            request.setAttribute("error", error);
            return ADDRESS_FORM_PAGE;
        }

        int addressId = ADAO.createAndReturnId(address);
        int userId = UserUtils.getUserId(request);
        UADAO.create(new UserAddressDTO(userId, addressId));
        prepareAddressManagementView(request, userId);
        return ADDRESS_MANAGEMENT_PAGE;
    }
    
    /**
     * Searches user's addresses using a keyword on full_address.
     *
     * @param request  servlet request
     * @param response servlet response
     * @return the destination JSP page
     */
    private String handleSearchAddress(HttpServletRequest request, HttpServletResponse response) {
        String keyword = request.getParameter("strKeyword");
        int userId = UserUtils.getUserId(request);
        
        if(getDefaultAddress(userId) != null){
            request.setAttribute("defaultAddressId", getDefaultAddress(userId).getId());
        }
        
        request.setAttribute("addressList", getUserAddress("full_address LIKE ?", userId, "%" + keyword + "%"));
        return ADDRESS_MANAGEMENT_PAGE;
    }

    /**
     * Updates an existing address with new data.
     *
     * @param request  servlet request
     * @param response servlet response
     * @return the destination JSP page
     */
    private String handleUpdateAddress(HttpServletRequest request, HttpServletResponse response) {
        int addressId = toInt(request.getParameter("addressId"));
        AddressDTO address = extractAddressFromRequest(request, addressId);
        String error = validateAddressFields(address);

        if (error != null) {
            request.setAttribute("error", error);
            return ADDRESS_FORM_PAGE;
        }

        ADAO.update(address);
        prepareAddressManagementView(request, UserUtils.getUserId(request));
        return ADDRESS_MANAGEMENT_PAGE;
    }

    /**
     * Updates the default address of the current user.
     *
     * @param request  servlet request
     * @param response servlet response
     * @return the destination JSP page
     */
    private String handleUpdateDefaultAddress(HttpServletRequest request, HttpServletResponse response) {
        int addressId = toInt(request.getParameter("addressId"));

        if (ValidationUtils.isInvalidId(addressId)) {
            request.setAttribute("errorMsg", "Không tìm thấy địa chỉ");
            return ADDRESS_MANAGEMENT_PAGE;
        }

        int userId = UserUtils.getUserId(request);

        AddressDTO currentDefault = getDefaultAddress(userId);
        if (currentDefault != null) {
            UADAO.update(new UserAddressDTO(userId, currentDefault.getId(), false));
        } else {
        }

        UADAO.update(new UserAddressDTO(userId, addressId, true));

        prepareAddressManagementView(request, userId);

        return ADDRESS_MANAGEMENT_PAGE;
    }

    /**
     * Deletes an address from the user's address list.
     *
     * @param request  servlet request
     * @param response servlet response
     * @return the destination JSP page
     */
    private String handleDeleteAddress(HttpServletRequest request, HttpServletResponse response) {
        int addressId = toInt(request.getParameter("addressId"));
        if (ValidationUtils.isInvalidId(addressId)) {
            request.setAttribute("errorMsg", "Không tìm thấy địa chỉ");
            return ADDRESS_MANAGEMENT_PAGE;
        }

        int userId = UserUtils.getUserId(request);
        UADAO.delete(userId, addressId);
        prepareAddressManagementView(request, userId);
        return ADDRESS_MANAGEMENT_PAGE;
    }

    /**
     * Retrieves the default address for the given user ID.
     *
     * @param userId the user's ID
     * @return AddressDTO object of default address or null
     */
    private AddressDTO getDefaultAddress(int userId) {
        String sql = "SELECT a.* FROM address a " +
                     "JOIN user_address ua ON a.id = ua.address_id " +
                     "WHERE ua.user_id = ? AND ua.is_default = 1";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a list of address records for the user matching the condition.
     *
     * @param condition SQL condition string
     * @param params    parameters for SQL query
     * @return list of AddressDTO objects
     */
    private List<AddressDTO> getUserAddress(String condition, Object... params) {
        String sql = "SELECT a.* FROM address a " +
                     "JOIN user_address ua ON a.id = ua.address_id " +
                     "WHERE ua.user_id = ? AND " + condition;

        List<AddressDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Extracts address fields from HTTP request to construct AddressDTO.
     *
     * @param request HTTP servlet request
     * @param id      the ID of the address (or -1 if new)
     * @return AddressDTO populated with request data
     */
    private AddressDTO extractAddressFromRequest(HttpServletRequest request, int id) {
        return new AddressDTO(
                id,
                toInt(request.getParameter("countryId")),
                request.getParameter("unitNumber"),
                request.getParameter("streetNumber"),
                request.getParameter("addressLine1"),
                request.getParameter("addressLine2"),
                request.getParameter("city"),
                request.getParameter("region")
        );
    }

    /**
     * Validates address fields for required values and length constraints.
     *
     * @param address the AddressDTO to validate
     * @return error message string, or null if valid
     */
    private String validateAddressFields(AddressDTO address) {
        if (ValidationUtils.isInvalidId(address.getCountryId()) ||
                ValidationUtils.isEmpty(address.getUnitNumber()) ||
                ValidationUtils.isEmpty(address.getStreetNumber()) ||
                ValidationUtils.isEmpty(address.getAddressLine1()) ||
                ValidationUtils.isEmpty(address.getCity()) ||
                ValidationUtils.isEmpty(address.getRegion())) {
            return "Vui lòng điền đầy đủ thông tin.";
        }

        String error;
        if ((error = ValidationUtils.checkLength("unit number", address.getUnitNumber(), 20)) != null ||
            (error = ValidationUtils.checkLength("street number", address.getStreetNumber(), 20)) != null ||
            (error = ValidationUtils.checkLength("address line 1", address.getAddressLine1(), 255)) != null ||
            (error = ValidationUtils.checkLength("address line 2", address.getAddressLine2(), 255)) != null ||
            (error = ValidationUtils.checkLength("city", address.getCity(), 100)) != null ||
            (error = ValidationUtils.checkLength("region", address.getRegion(), 100)) != null) {
            return error;
        }

        return null;
    }

    /**
     * Prepares attributes required to display address management view.
     *
     * @param request HTTP servlet request
     * @param userId  current user's ID
     */
    private void prepareAddressManagementView(HttpServletRequest request, int userId) {
        AddressDTO defAddress = getDefaultAddress(userId);
        request.setAttribute("defaultAddressId", defAddress != null? defAddress.getId(): null);
        request.setAttribute("addressList", getUserAddress("1=1", userId));
    }

    /**
     * Converts a string to integer safely. Returns -1 if invalid.
     *
     * @param s the string to convert
     * @return parsed integer or -1 on failure
     */
    private int toInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    
}