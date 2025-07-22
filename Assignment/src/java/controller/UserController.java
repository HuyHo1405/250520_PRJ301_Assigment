package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.dao.CategoryDAO;
import model.dao.ProductDAO;
import model.dao.UserDAO;
import model.dto.UserDTO;
import utils.HashUtils;
import utils.MailUtils;
import utils.ResetTokenManager;
import utils.UserUtils;
import utils.ValidationUtils;

@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    /**
     * UserController chịu trách nhiệm xử lý các hành động liên quan đến người
     * dùng như: đăng nhập, đăng ký, quên mật khẩu, cập nhật hồ sơ và đặt lại
     * mật khẩu. Controller này định tuyến theo tham số `action` và trả về trang
     * phù hợp.
     *
     * Các action xử lý gồm: login, logout, register, forgotPassword,
     * resetPassword, updateProfile.
     *
     * Các trang JSP liên quan: - welcome.jsp: Trang chính sau khi đăng nhập -
     * user-form.jsp: Form dùng chung cho login, register, forgot/reset password
     * - error.jsp: Trang hiển thị lỗi
     */
    private static final String WELCOME_PAGE = "welcome.jsp";
    private static final String USER_FORM_PAGE = "user-form.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private final UserDAO UDAO = new UserDAO();
    private final ProductDAO PDAO = new ProductDAO();
    private final CategoryDAO CDAO = new CategoryDAO();

    /**
     * Xử lý các yêu cầu GET và POST, định tuyến theo action.
     */
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
                case "updateProfile":
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

    /**
     * Đăng nhập người dùng.
     *
     * @return Tên trang JSP cần forward đến.
     */
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
            request.setAttribute("productList", PDAO.retrieve("is_active = 1"));
            request.setAttribute("categoryList", CDAO.retrieve("is_active = 1"));
            return WELCOME_PAGE;
        } else {
            request.setAttribute("error", "Email Or Password is Incorrect.");
            request.setAttribute("inputEmail", email);
            return USER_FORM_PAGE;
        }
    }

    /**
     * Đăng xuất người dùng.
     *
     * @return Chuyển hướng về form đăng nhập.
     */
    public String handleLogout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        request.setAttribute("actionType", "login");
        return USER_FORM_PAGE;
    }

    /**
     * Đăng ký người dùng mới.
     *
     * @return Trả về trang form với lỗi nếu đăng ký thất bại, hoặc chuyển sang
     * login nếu thành công.
     */
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

    /**
     * Cập nhật thông tin người dùng (email, số điện thoại).
     *
     * @return Trả về trang form với lỗi nếu thất bại, hoặc logout và yêu cầu
     * đăng nhập lại nếu thành công.
     */
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

    /**
     * Đổi mật khẩu người dùng đang đăng nhập.
     *
     * @return Chuyển hướng về trang đăng nhập nếu thành công, hiển thị lỗi nếu
     * mật khẩu không khớp.
     */
    public String handleChangePassword(HttpServletRequest request, HttpServletResponse response) {
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

    /**
     * Gửi email đặt lại mật khẩu nếu email hợp lệ và tồn tại trong hệ thống.
     *
     * @return Trả về form với thông báo hoặc lỗi.
     */
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

    /**
     * Xử lý khi người dùng click vào link đặt lại mật khẩu và nhập mật khẩu
     * mới.
     *
     * @return Trả về trang form với lỗi nếu thất bại hoặc không hợp lệ.
     */
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

    /**
     * Kiểm tra email đã tồn tại trong hệ thống chưa.
     *
     * @param email Email cần kiểm tra.
     * @return true nếu email đã tồn tại, false nếu chưa.
     */
    private boolean isExistedEmail(String email) {
        return !UDAO.retrieve("email_address = ?", email).isEmpty();
    }
}
