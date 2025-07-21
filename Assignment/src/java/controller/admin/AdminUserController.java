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

@WebServlet(name = "AdminUserController", urlPatterns = {"/AdminUserController"})
public class AdminUserController extends HttpServlet {
    private static final String ERROR_PAGE = "error.jsp";
    private static final String ADMIN_USER_MANAGEMENT_PAGE = "admin-user-management.jsp";
    private final UserDAO UDAO = new UserDAO();
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
    
    private String handleListAllUsers(HttpServletRequest request, HttpServletResponse response) {
        List<UserDTO> list = UDAO.retrieve("1 = 1 ORDER BY is_active DESC, role ASC");
        request.setAttribute("userList", list);
        return ADMIN_USER_MANAGEMENT_PAGE;
    }

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
    
    private String handleChangeUserRole(HttpServletRequest request, HttpServletResponse response) {
        int userId = toInt(request.getParameter("userId"));
        String newRole = request.getParameter("newRole");

        boolean success = UDAO.updateRole(userId, newRole);

        request.setAttribute(success ? "message" : "errorMsg",
                success ? "Cập nhật vai trò thành công." : "Cập nhật vai trò thất bại.");

        return handleListAllUsers(request, response);
    }

    private boolean isExistedEmail(String email) {
        return !UDAO.retrieve("email_address = ?", email).isEmpty();
    }

    private int toInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return -1;
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

    

}
