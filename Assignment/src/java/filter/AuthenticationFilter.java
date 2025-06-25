/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ho huy
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {
    
    public AuthenticationFilter() {
    }    
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String action = req.getParameter("action");
        if (action == null) action = "";

        List<String> ignoreActions = Arrays.asList(
            "toLogin", "login",
            "toRegister", "register",
            "toForgotPassword", "forgotPassword"
        );

        // Bỏ qua kiểm tra đăng nhập cho các action cho phép
        if (!ignoreActions.contains(action)) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                res.sendRedirect("MainController?action=toLogin");
                return; // Dừng filter nếu chưa đăng nhập
            }
        }

        chain.doFilter(request, response); // Cho phép tiếp tục xử lý
    }

    @Override
    public void destroy() {        
    }

    @Override
    public void init(FilterConfig filterConfig) {        
    }
    
}
