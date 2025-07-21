package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.dao.UserDAO;
import model.dto.UserDTO;
import utils.HashUtils;
import utils.MailUtils;
import utils.ResetTokenManager;
import utils.UserUtils;
import utils.ValidationUtils;

@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    private static final String WELCOME_PAGE = "welcome.jsp";
    private static final String USER_FORM_PAGE = "user-form.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private final UserDAO UDAO = new UserDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = WELCOME_PAGE;
        try {
            String action = request.getParameter("action");

            switch (action) {
                case "toLogin":
                    request.setAttribute("actionType", "login");
                    url = USER_FORM_PAGE;
                    break;
                case "toRegister":
                    request.setAttribute("actionType", "register");
                    url = USER_FORM_PAGE;
                    break;
                case "toForgotPassword":
                    request.setAttribute("actionType", "forgotPassword");
                    url = USER_FORM_PAGE;
                    break;
                case "toResetPassword":
                    request.setAttribute("actionType", "resetPassword");
                    url = USER_FORM_PAGE;
                    break;
                case "login":
                    url = handleLogin(request, response);
                    break;
                case "logout":
                    url = handleLogout(request, response);
                    break;
                case "register":
                    url = handleRegister(request, response);
                    break;
                case "forgotPassword":
                    url = handleForgotPassword(request, response);
                    break;
                case "resetPassword":
                    url = handleResetPassword(request, response);
                    break;
                case "update":
                    url = handleUpdate(request, response);
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

    public String handleLogin(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        boolean checkEmpty = email == null || password == null || email.isEmpty() || password.isEmpty();
        boolean validateEmail = !ValidationUtils.isValidEmail(email);

        String error = "";
        if (checkEmpty) {
            error = "Email và mật khẩu không được để trống.";
        } else if (validateEmail) {
            error = "Email không hợp lệ.";
        }

        if (checkEmpty || validateEmail) {
            request.setAttribute("error", error);
            request.setAttribute("inputEmail", email);
            return USER_FORM_PAGE;
        }

        String hashedPassword = HashUtils.hashPassword(password);
        List<UserDTO> ls = UDAO.retrieve("email_address = ? AND hashed_password = ?", email, hashedPassword);

        if (!ls.isEmpty()) {
            HttpSession session = request.getSession();
            session.setAttribute("user", ls.get(0));
            return WELCOME_PAGE;
        } else {
            request.setAttribute("error", "Email hoặc mật khẩu không đúng.");
            request.setAttribute("inputEmail", email);
            return USER_FORM_PAGE;
        }
    }

    public String handleLogout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        request.setAttribute("actionType", "login");
        return USER_FORM_PAGE;
    }

    public String handleRegister(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        boolean checkEmpty = email == null || phone == null || password == null || confirmPassword == null
                || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty();
        boolean validateEmail = !ValidationUtils.isValidEmail(email);
        boolean validatePhone = !ValidationUtils.isValidPhone(phone);
        boolean checkConfirmPassword = !password.equals(confirmPassword);
        boolean checkExistedEmail = isExistedEmail(email);

        String error = "";
        if (checkEmpty) {
            error = "Vui lòng điền đầy đủ thông tin.";
        } else if (validateEmail) {
            error = "Email không hợp lệ.";
        } else if (validatePhone) {
            error = "Số điện thoại không hợp lệ.";
        } else if (checkConfirmPassword) {
            error = "Mật khẩu xác nhận không khớp.";
        } else if (checkExistedEmail) {
            error = "Email đã được đăng ký.";
        }

        if (checkEmpty || validateEmail || validatePhone || checkConfirmPassword || checkExistedEmail) {
            request.setAttribute("error", error);
            request.setAttribute("inputEmail", email);
            request.setAttribute("inputPhone", phone);
            request.setAttribute("inputPassword", password);
            request.setAttribute("inputConformPassword", confirmPassword);
            request.setAttribute("actionType", "register");
            return USER_FORM_PAGE;
        }

        String hashedPassword = HashUtils.hashPassword(password);
        UserDTO newUser = new UserDTO(email, phone, hashedPassword); // mặc định role = 'user'
        UDAO.create(newUser);

        request.setAttribute("actionType", "login");
        return USER_FORM_PAGE;
    }

    public String handleUpdate(HttpServletRequest request, HttpServletResponse response) {
        String newEmail = request.getParameter("email");
        String newPhone = request.getParameter("phone");

        boolean checkEmpty = newEmail == null || newPhone == null || newEmail.isEmpty() || newPhone.isEmpty();
        boolean validateEmail = !ValidationUtils.isValidEmail(newEmail);
        boolean validatePhone = !ValidationUtils.isValidPhone(newPhone);

        String error = "";
        if (checkEmpty) {
            error = "Vui lòng điền đầy đủ thông tin.";
        } else if (validateEmail) {
            error = "Email không hợp lệ.";
        } else if (validatePhone) {
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


    public String handleChangePassword(HttpServletRequest request, HttpServletResponse response){
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            return USER_FORM_PAGE;
        }
        
        String hashedPassword = HashUtils.hashPassword(password);
        UserDTO user = UserUtils.getUser(request);
        user.setHashed_password(hashedPassword);
        
        UDAO.update(user);
        request.getSession().invalidate();
        request.setAttribute("actionType", "login");
        return USER_FORM_PAGE;
    }
    
    private String handleForgotPassword(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        
        UserDTO user = UDAO.findByEmail(email);

        if (user == null) {
            request.setAttribute("errorMsg", "Email không tồn tại.");
            return "forgot-password.jsp";
        }

        // Tạo link reset có token (hết hạn sau 10 phút)
        String token = ResetTokenManager.generateToken(email); // ← tạo token an toàn
        String baseURL = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        String resetLink = baseURL + "/MainController?action=toResetPassword&token=" + token;

        try {
            MailUtils.sendResetPasswordEmail(email, resetLink); // ← gửi mail tới chính email user
            request.setAttribute("message", "📩 Đã gửi liên kết đặt lại mật khẩu đến email của bạn.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Gửi email thất bại: " + e.getMessage());
        }

        request.setAttribute("actionType", "forgotPassword");
        return USER_FORM_PAGE;
    }
    
    private String handleResetPassword(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter("token");
        String emailFromToken = ResetTokenManager.getEmailIfValid(token);
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmNewPassword");

        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMsg", "Mật khẩu xác nhận không khớp.");
            request.setAttribute("email", emailFromToken); // giữ lại email để điền lại form
            request.setAttribute("actionType", "resetPassword");
            return USER_FORM_PAGE;
        }

        UserDAO dao = new UserDAO();
        UserDTO user = dao.findByEmail(emailFromToken);

        if (user == null) {
            request.setAttribute("errorMsg", "Không tìm thấy tài khoản.");
            request.setAttribute("actionType", "resetPassword");
            return USER_FORM_PAGE;
        }

        String hashedPassword = HashUtils.hashPassword(newPassword);
        user.setHashed_password(hashedPassword);
        boolean success = dao.update(user);

        if (!success) {
            request.setAttribute("errorMsg", "Đặt lại mật khẩu thất bại.");
            request.setAttribute("actionType", "resetPassword");
        }
        return USER_FORM_PAGE;
    }

    private boolean isExistedEmail(String email) {
        return !UDAO.retrieve("email_address = ?", email).isEmpty();
    }
}
