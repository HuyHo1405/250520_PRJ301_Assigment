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
 * The `AddressController` servlet manages user addresses, including adding,
 * updating, deleting, setting default addresses, and viewing address lists.
 * It interacts with `AddressDAO`, `CountryDAO`, and `UserAddressDAO` to
 * perform database operations.
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
     * Processes requests for both HTTP `GET` and `POST` methods.
     * This method acts as a central dispatcher for various address-related actions
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
                    // Assuming an addressId parameter is passed for editing
                    int addressIdToEdit = toInt(request.getParameter("addressId"));
                    if (!ValidationUtils.isInvalidId(addressIdToEdit)) {
                        request.setAttribute("address", ADAO.findById(addressIdToEdit));
                    } else {
                        request.setAttribute("error", "Địa chỉ không hợp lệ để chỉnh sửa.");
                    }
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
            e.printStackTrace(); // Log the exception for debugging
            url = ERROR_PAGE;
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
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
        return "Short description";
    }// </editor-fold>

    /**
     * Handles the addition of a new address for the current user.
     * Extracts address details from the request, validates them, and
     * creates a new address entry in the database.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The path to the address management page or address form page if validation fails.
     */
    private String handleAddAddress(HttpServletRequest request, HttpServletResponse response) {
        AddressDTO address = extractAddressFromRequest(request, -1);
        String error = validateAddressFields(address);

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("countries", CDAO.retrieve("1 = 1")); // Re-set countries for form
            request.setAttribute("actionType", "addAddress");
            return ADDRESS_FORM_PAGE;
        }

        int addressId = ADAO.createAndReturnId(address);
        int userId = UserUtils.getUserId(request);
        UADAO.create(new UserAddressDTO(userId, addressId));
        prepareAddressManagementView(request, userId);
        request.setAttribute("message", "Thêm địa chỉ thành công!");
        return ADDRESS_MANAGEMENT_PAGE;
    }
    
    /**
     * Handles searching for user addresses based on a keyword.
     * Retrieves addresses associated with the current user that match the keyword
     * in their full address.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The path to the address management page.
     */
    private String handleSearchAddress(HttpServletRequest request, HttpServletResponse response) {
        String keyword = request.getParameter("strKeyword");
        int userId = UserUtils.getUserId(request);
        
        AddressDTO defaultAddress = getDefaultAddress(userId);
        if(defaultAddress != null){
            request.setAttribute("defaultAddressId", defaultAddress.getId());
        }
        
        request.setAttribute("addressList", getUserAddress("full_address LIKE ?", userId, "%" + keyword + "%"));
        return ADDRESS_MANAGEMENT_PAGE;
    }

    /**
     * Handles updating an existing address.
     * Extracts updated address details from the request, validates them, and
     * updates the address entry in the database.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The path to the address management page or address form page if validation fails.
     */
    private String handleUpdateAddress(HttpServletRequest request, HttpServletResponse response) {
        int addressId = toInt(request.getParameter("addressId"));
        AddressDTO address = extractAddressFromRequest(request, addressId);
        String error = validateAddressFields(address);

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("countries", CDAO.retrieve("1 = 1")); // Re-set countries for form
            request.setAttribute("actionType", "updateAddress");
            request.setAttribute("address", address); // Pass back the invalid address for correction
            return ADDRESS_FORM_PAGE;
        }

        ADAO.update(address);
        prepareAddressManagementView(request, UserUtils.getUserId(request));
        request.setAttribute("message", "Cập nhật địa chỉ thành công!");
        return ADDRESS_MANAGEMENT_PAGE;
    }

    /**
     * Handles setting a specific address as the default address for the current user.
     * Deactivates the current default address (if any) and sets the new address as default.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The path to the address management page.
     */
    private String handleUpdateDefaultAddress(HttpServletRequest request, HttpServletResponse response) {
        int addressId = toInt(request.getParameter("addressId"));

        if (ValidationUtils.isInvalidId(addressId)) {
            request.setAttribute("errorMsg", "Không tìm thấy địa chỉ");
            prepareAddressManagementView(request, UserUtils.getUserId(request));
            return ADDRESS_MANAGEMENT_PAGE;
        }

        int userId = UserUtils.getUserId(request);

        AddressDTO currentDefault = getDefaultAddress(userId);
        if (currentDefault != null) {
            // Deactivate the current default address
            UADAO.update(new UserAddressDTO(userId, currentDefault.getId(), false));
        }

        // Set the new address as default
        UADAO.update(new UserAddressDTO(userId, addressId, true));

        prepareAddressManagementView(request, userId);
        request.setAttribute("message", "Địa chỉ mặc định đã được cập nhật!");
        return ADDRESS_MANAGEMENT_PAGE;
    }


    /**
     * Handles deleting a user's address.
     * Removes the association between the user and the address in the database.
     * Note: This does not delete the address from the `address` table itself,
     * only breaks the `user_address` relationship.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The path to the address management page.
     */
    private String handleDeleteAddress(HttpServletRequest request, HttpServletResponse response) {
        int addressId = toInt(request.getParameter("addressId"));
        if (ValidationUtils.isInvalidId(addressId)) {
            request.setAttribute("errorMsg", "Không tìm thấy địa chỉ");
            prepareAddressManagementView(request, UserUtils.getUserId(request));
            return ADDRESS_MANAGEMENT_PAGE;
        }

        int userId = UserUtils.getUserId(request);
        UADAO.delete(userId, addressId);
        prepareAddressManagementView(request, userId);
        request.setAttribute("message", "Địa chỉ đã được xoá thành công!");
        return ADDRESS_MANAGEMENT_PAGE;
    }

    /**
     * Retrieves the default address for a given user ID.
     * Queries the database to find the address marked as default for the user.
     *
     * @param userId The ID of the user.
     * @return The `AddressDTO` object of the default address, or `null` if no default is found.
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
            e.printStackTrace(); // Log the exception
        }
        return null;
    }

    /**
     * Retrieves a list of addresses associated with a specific user,
     * optionally filtered by a condition.
     *
     * @param condition The SQL WHERE clause condition (e.g., "full_address LIKE ?").
     * @param params Varargs of parameters to be set in the prepared statement for the condition.
     * @return A list of `AddressDTO` objects for the user.
     */
    private List<AddressDTO> getUserAddress(String condition, Object... params) {
        String sql = "SELECT a.* FROM address a " +
                     "JOIN user_address ua ON a.id = ua.address_id " +
                     "WHERE ua.user_id = ? AND " + condition;

        List<AddressDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the userId as the first parameter
            ps.setObject(1, params[0]);
            // Set subsequent parameters for the condition
            for (int i = 1; i < params.length; i++) {
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
            e.printStackTrace(); // Log the exception
        }
        return list;
    }

    /**
     * Extracts address details from the HttpServletRequest and creates an `AddressDTO` object.
     *
     * @param request The HttpServletRequest object.
     * @param id The ID of the address (use -1 for new addresses).
     * @return An `AddressDTO` object populated with data from the request.
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
     * Validates the fields of an `AddressDTO` object.
     * Checks for empty required fields and validates string lengths.
     *
     * @param address The `AddressDTO` object to validate.
     * @return A string containing an error message if validation fails, or `null` if valid.
     */
    private String validateAddressFields(AddressDTO address) {
        if (ValidationUtils.isInvalidId(address.getCountryId()) ||
                ValidationUtils.isEmpty(address.getUnitNumber()) ||
                ValidationUtils.isEmpty(address.getStreetNumber()) ||
                ValidationUtils.isEmpty(address.getAddressLine1()) ||
                ValidationUtils.isEmpty(address.getCity()) ||
                ValidationUtils.isEmpty(address.getRegion())) {
            return "Vui lòng điền đầy đủ thông tin bắt buộc (số nhà, tên đường, địa chỉ dòng 1, thành phố, vùng, quốc gia).";
        }

        String error;
        if ((error = ValidationUtils.checkLength("số căn hộ/phòng", address.getUnitNumber(), 20)) != null ||
            (error = ValidationUtils.checkLength("số đường", address.getStreetNumber(), 20)) != null ||
            (error = ValidationUtils.checkLength("địa chỉ dòng 1", address.getAddressLine1(), 255)) != null ||
            (error = ValidationUtils.checkLength("địa chỉ dòng 2", address.getAddressLine2(), 255)) != null ||
            (error = ValidationUtils.checkLength("thành phố", address.getCity(), 100)) != null ||
            (error = ValidationUtils.checkLength("vùng/bang", address.getRegion(), 100)) != null) {
            return error;
        }

        return null;
    }

    /**
     * Prepares the necessary data to display the address management view.
     * Retrieves the user's default address and a list of all user addresses,
     * then sets them as request attributes.
     *
     * @param request The HttpServletRequest object.
     * @param userId The ID of the current user.
     */
    private void prepareAddressManagementView(HttpServletRequest request, int userId) {
        AddressDTO defAddress = getDefaultAddress(userId);
        request.setAttribute("defaultAddressId", defAddress != null ? defAddress.getId() : null);
        request.setAttribute("addressList", getUserAddress("1=1", userId));
    }

    /**
     * Converts a string to an integer.
     * Returns -1 if the string cannot be parsed as an integer.
     *
     * @param s The string to convert.
     * @return The integer value, or -1 if a `NumberFormatException` occurs.
     */
    private int toInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}