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
 * The `AuthenticationFilter` is a servlet filter responsible for enforcing authentication
 * across the web application. It intercepts all incoming requests (`/*`) and checks
 * if a user is logged in, redirecting them to the login page if they try to access
 * protected resources without authentication. It provides exceptions for public
 * resources like static assets and specific authentication-related actions.
 *
 * @author ho huy
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    /**
     * Default constructor for the filter.
     */
    public AuthenticationFilter() {
    }

    /**
     * The `doFilter` method is the core of the filter. It performs the authentication
     * check for incoming requests.
     *
     * @param request The `ServletRequest` object representing the client's request.
     * @param response The `ServletResponse` object representing the filter's response.
     * @param chain The `FilterChain` object to which the filter can pass the request and response.
     * @throws IOException if an I/O error occurs during the filtering process.
     * @throws ServletException if a servlet-specific error occurs during the filtering process.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Get the "action" parameter from the request, defaulting to an empty string if null.
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        // Get the full request URI.
        String uri = req.getRequestURI();
        
        // Define a list of URI patterns that should be ignored by the filter
        // These typically include static assets like CSS, JS, images, and fonts.
        // If the URI matches any of these patterns, the request is allowed to proceed
        // without authentication checks.
        if (uri.startsWith(req.getContextPath() + "/assets/")
                || uri.endsWith(".css") || uri.endsWith(".js")
                || uri.endsWith(".png") || uri.endsWith(".jpg")
                || uri.endsWith(".jpeg") || uri.endsWith(".gif")
                || uri.endsWith(".woff") || uri.endsWith(".woff2")
                || uri.endsWith(".ttf") || uri.endsWith(".svg")) {

            chain.doFilter(request, response); // Allow the request to pass through
            return;
        }
        
        // Define a list of "action" parameter values that do not require authentication.
        // These are typically actions related to logging in, registering, or password recovery.
        List<String> ignoreActions = Arrays.asList(
                "toLogin", "login",
                "toRegister", "register",
                "toForgotPassword", "forgotPassword",
                "toResetPassword", "resetPassword"
        );

        // If the current action is NOT in the list of ignored actions,
        // then perform an authentication check.
        if (!ignoreActions.contains(action)) {
            HttpSession session = req.getSession(false); // Get the current session without creating a new one

            // Check if the session is null (no session exists) or if the "user" attribute
            // (indicating a logged-in user) is not present in the session.
            if (session == null || session.getAttribute("user") == null) {
                // If the user is not authenticated, redirect them to the login page.
                // It redirects to "MainController?action=toLogin", which is handled by a servlet
                // to display the login form.
                res.sendRedirect("MainController?action=toLogin");
                return; // Stop the filter chain execution as the user is redirected
            }
        }

        // If the request passes all checks (either an ignored action or the user is authenticated),
        // allow the request to proceed to the next filter in the chain or the target servlet/JSP.
        chain.doFilter(request, response);
    }

    /**
     * Called by the web container to indicate to a filter that it is being taken out of service.
     * This method is typically used for resource cleanup.
     */
    @Override
    public void destroy() {
        // No specific cleanup resources are held by this filter, so this method is empty.
    }

    /**
     * Called by the web container to indicate to a filter that it is being placed into service.
     * This method is typically used to initialize filter-specific resources.
     *
     * @param filterConfig The `FilterConfig` object containing the filter's initialization parameters.
     * @throws ServletException if an exception occurs that interrupts the filter's normal operation.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No specific initialization is needed for this filter, so this method is empty.
    }

}