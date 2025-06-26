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
import model.dao.UserDAO;
import model.dto.UserDTO;
import utils.UserUtils;
import utils.ValidationUtils;

/**
 * Status: Chờ thực hiện Người thực hiện: [...........] Ngày bắt đầu:
 * [...........] viết servlet cho Admin User Controller
 */
@WebServlet(name = "AdminUserController", urlPatterns = {"/AdminUserController"})
public class AdminUserController extends HttpServlet {

    private static final String WELCOME_PAGE = "welcome.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String USER_LIST_PAGE = "admin-user-management.jsp";
    private static final String USER_DETAIL_PAGE = "user-detail.jsp";
    private static final String USER_FORM_PAGE = "admin-user-form.jsp";

    private final UserDAO UDAO = new UserDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = WELCOME_PAGE;

        try {
            String action = request.getParameter("action");

            switch (action) {
                case "listAllUsers":
                    url = handleListAllUsers(request, response);
                    break;
                case "viewUserDetail":
                    url = handleViewUserDetail(request, response);
                    break;
                case "createUser":
                    url = handleCreateUser(request, response);
                    break;
                case "updateUser":
                    url = handleUpdateUser(request, response);
                    break;
                case "disableUser":
                    url = handleDisableUser(request, response);
                    break;
                case "changeUserRole":
                    url = handleChangeUserRole(request, response);
                    break;
                case "resetUserPassword":
                    url = handleResetUserPassword(request, response);
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

    // Methods
    private String handleListAllUsers(HttpServletRequest request, HttpServletResponse response) {
        List<UserDTO> list = UDAO.retrieve("1 = 1");
        request.setAttribute("userList", list);
        return USER_LIST_PAGE;
    }

    private String handleViewUserDetail(HttpServletRequest request, HttpServletResponse response) {
        int userId = toInt(request.getParameter("userId"));
        if (ValidationUtils.isInvalidId(userId)) {
            return error(request, "ID người dùng không hợp lệ.");
        }

        UserDTO user = UDAO.findById(userId);
        if (user == null) {
            return error(request, "Không tìm thấy người dùng.");
        }

        request.setAttribute("user", user);
        return USER_DETAIL_PAGE;
    }

    private String handleCreateUser(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        UserDTO user = new UserDTO(email, phone, password);
        boolean success = UDAO.create(user);

        request.setAttribute(success ? "message" : "errorMsg",
                success ? "Tạo người dùng thành công." : "Tạo người dùng thất bại.");

        return handleListAllUsers(request, response);
    }

    private String handleUpdateUser(HttpServletRequest request, HttpServletResponse response) {
        String newEmail = request.getParameter("email");
        String newPhone = request.getParameter("phone");

        boolean checkEmpty = newEmail == null || newPhone == null || newEmail.isEmpty() || newPhone.isEmpty();
        boolean validateEmail = !ValidationUtils.isValidEmail(newEmail);
        boolean validatePhone = !ValidationUtils.isValidPhone(newPhone);
        
        String error = "";
        if(checkEmpty){
            error = "Vui lòng điền đầy đủ thông tin.";
        }else if(validateEmail){
            error = "Email không hợp lệ.";
        }else if(validatePhone){
            error = "Số điện thoại không hợp lệ.";
        }
        
        if (checkEmpty || validateEmail || validatePhone) {
            request.setAttribute("error", error);
            request.setAttribute("actionType", "profile");
            request.setAttribute("inputEmail", newEmail);
            request.setAttribute("inputPhone", newPhone);
            return USER_FORM_PAGE;
        }

        UserDTO user = UserUtils.getUser(request);

        // Nếu email thay đổi → kiểm tra có bị trùng hay không
        if (!user.getEmail_address().equals(newEmail)) {
            if (isExistedEmail(newEmail)) {
                request.setAttribute("error", "Email đã được đăng ký.");
                return USER_FORM_PAGE;
            }
            user.setEmail_address(newEmail);
        }
        user.setPhone_number(newPhone);

        UDAO.update(user);
        request.getSession().invalidate();
        request.setAttribute("actionType", "login");
        return USER_FORM_PAGE;
    }

    private String handleDisableUser(HttpServletRequest request, HttpServletResponse response) {
        int userId = toInt(request.getParameter("userId"));
        boolean success = UDAO.disableUser(userId);

        request.setAttribute(success ? "message" : "errorMsg",
                success ? "Đã vô hiệu hóa người dùng." : "Vô hiệu hóa thất bại.");

        return handleListAllUsers(request, response);
    }

    private String handleChangeUserRole(HttpServletRequest request, HttpServletResponse response) {
        int userId = toInt(request.getParameter("userId"));
        String newRole = request.getParameter("newRole");

        boolean success = UDAO.updateRole(userId, newRole);

        request.setAttribute(success ? "message" : "errorMsg",
                success ? "Cập nhật vai trò thành công." : "Cập nhật vai trò thất bại.");

        return handleViewUserDetail(request, response);
    }

    private String handleResetUserPassword(HttpServletRequest request, HttpServletResponse response) {
        return handleViewUserDetail(request, response);
    }

    //some useful methods
    private boolean isExistedEmail(String email){
        return !UDAO.retrieve("email_address = ?", email).isEmpty();
    }
    
    private int toInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private String error(HttpServletRequest request, String msg) {
        request.setAttribute("errorMsg", msg);
        return ERROR_PAGE;
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
