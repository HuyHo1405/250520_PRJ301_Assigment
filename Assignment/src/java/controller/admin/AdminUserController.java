package controller.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.dao.UserDAO;
import model.dto.UserDTO;
import utils.HashUtils;
import utils.ValidationUtils;

/**
 * The `AdminUserController` servlet handles administrative operations related to
 * user management, including listing all users, creating new users,
 * toggling user active status, changing user roles, and redirecting to password reset.
 * It interacts with the `UserDAO` to perform database operations.
 */
@WebServlet(name = "AdminUserController", urlPatterns = {"/AdminUserController"})
public class AdminUserController extends HttpServlet {
    private static final String ERROR_PAGE = "error.jsp";
    private static final String ADMIN_USER_MANAGEMENT_PAGE = "admin-user-management.jsp";
    private final UserDAO UDAO = new UserDAO();

    /**
     * Processes requests for both HTTP `GET` and `POST` methods.
     * This method acts as a central dispatcher for various user-related actions
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
                case "toAdminUserPage":
                    url = handleListAllUsers(request, response);
                    break;
                case "createUser":
                    url = handleCreateUser(request, response);
                    break;
                case "toggleIsActiveUser":
                    url = handleToggleIsActive(request, response);
                    break;
                case "changeUserRole":
                    url = handleChangeUserRole(request, response);
                    break;
                case "toResetPassword":
                    String email = request.getParameter("email");
                    request.setAttribute("email", email);
                    url = "reset-password.jsp";
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

    /**
     * Handles listing all users in the system.
     * Retrieves all users from the database, sorts them, and sets the list
     * as a request attribute for display on the admin user management page.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The path to the admin user management page.
     */
    private String handleListAllUsers(HttpServletRequest request, HttpServletResponse response) {
        List<UserDTO> list = UDAO.retrieve("1 = 1 ORDER BY is_active DESC, role ASC");
        request.setAttribute("userList", list);
        return ADMIN_USER_MANAGEMENT_PAGE;
    }

    /**
     * Handles the creation of a new user.
     * Retrieves user details from request parameters, hashes the password,
     * creates a `UserDTO` object, and attempts to persist it to the database.
     * Sets a message indicating success or failure.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The path to the admin user management page after creation attempt.
     */
    private String handleCreateUser(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String hashedPassword = HashUtils.hashPassword(password);
        String role = request.getParameter("role");
        String isActive = request.getParameter("isActive");

        UserDTO user = new UserDTO(email, phone, hashedPassword);
        user.setRole(role);
        user.setIs_active(Boolean.parseBoolean(isActive));
        boolean success = UDAO.create(user);

        request.setAttribute(success ? "message" : "errorMsg",
                success ? "Tạo người dùng thành công." : "Tạo người dùng thất bại.");

        return handleListAllUsers(request, response);
    }

    /**
     * Toggles the `is_active` status of a user (activates or deactivates them).
     * Retrieves the user by ID, flips their `is_active` status, and updates it
     * in the database. Handles invalid user ID or user not found scenarios.
     *
     * @param request The HttpServletRequest object containing the user ID.
     * @param response The HttpServletResponse object.
     * @return The path to the admin user management page or an error page.
     */
    private String handleToggleIsActive(HttpServletRequest request, HttpServletResponse response) {
        int userId = toInt(request.getParameter("userId"));

        if(ValidationUtils.isInvalidId(userId)){
            request.setAttribute("errorMsg", "Invalid Id.");
            return ERROR_PAGE;
        }

        UserDTO user = UDAO.findById(userId);
        if(user == null){
            request.setAttribute("errorMsg", "User Not Found");
            return ERROR_PAGE;
        }else{
            user.setIs_active(!user.getIs_active());
            UDAO.update(user);
        }
        return handleListAllUsers(request, response);
    }

    /**
     * Changes the role of a specific user.
     * Retrieves the user ID and the new role from request parameters,
     * and attempts to update the user's role in the database.
     * Sets a message indicating success or failure.
     *
     * @param request The HttpServletRequest object containing user ID and new role.
     * @param response The HttpServletResponse object.
     * @return The path to the admin user management page after role update attempt.
     */
    private String handleChangeUserRole(HttpServletRequest request, HttpServletResponse response) {
        int userId = toInt(request.getParameter("userId"));
        String newRole = request.getParameter("newRole");

        boolean success = UDAO.updateRole(userId, newRole);

        request.setAttribute(success ? "message" : "errorMsg",
                success ? "Cập nhật vai trò thành công." : "Cập nhật vai trò thất bại.");

        return handleListAllUsers(request, response);
    }

    /**
     * Checks if a user with the given email address already exists in the database.
     *
     * @param email The email address to check.
     * @return `true` if an account with the email exists, `false` otherwise.
     */
    private boolean isExistedEmail(String email) {
        return !UDAO.retrieve("email_address = ?", email).isEmpty();
    }

    /**
     * Converts a string value to an integer.
     * Returns -1 if the string is null or cannot be parsed as an integer.
     *
     * @param value The string to convert.
     * @return The integer value, or -1 if conversion fails.
     */
    private int toInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return -1;
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
}