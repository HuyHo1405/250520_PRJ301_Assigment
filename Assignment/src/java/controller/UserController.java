package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Status: Đang thực hiện 
 * Người thực hiện: Huy 
 * Ngày bắt đầu: 13/06/2025 
 * viết servlet cho User Controller
 */
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    //static fields
    private static final String WELCOME_PAGE = "welcome.jsp";
    private static final String LOGIN_PAGE = "login.jsp";
    private static final String ERROR_PAGE = "error.jsp";

    //process
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = WELCOME_PAGE;
        try {
            String action = request.getParameter("action");

            switch (action) {
                case "login":
                    url = handleLogin(request, response);
                    break;
                case "logout":
//                    url = handleLogout(request);
                    break;
                case "register":
//                    url = handleRegister(request);
                    break;
                case "profile":
//                    url = handleProfile(request);
                    break;
                case "changePassword":
//                    url = handleChangePassword(request);
                    break;
                case "forgotPassword":
//                    url = handleForgotPassword(request);
                    break;
                default:
                    url = ERROR_PAGE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            url = ERROR_PAGE;
        } finally {
            response.sendRedirect(url);
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
    public String handleLogin(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

    public String handleLogout(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

    public String handleRegister(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

    public String handleProfile(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

    public String handleChangePassword(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

    public String handleForgotPassword(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

}
