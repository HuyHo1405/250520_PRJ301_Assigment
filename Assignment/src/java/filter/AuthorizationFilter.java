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
import utils.AuthorizationUtils;
import utils.UserUtils;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String action = req.getParameter("action");
        String uri = req.getRequestURI();

        if (uri.startsWith(req.getContextPath() + "/assets/")
                || uri.endsWith(".css") || uri.endsWith(".js")
                || uri.endsWith(".png") || uri.endsWith(".jpg")
                || uri.endsWith(".jpeg") || uri.endsWith(".gif")
                || uri.endsWith(".woff") || uri.endsWith(".woff2")
                || uri.endsWith(".ttf") || uri.endsWith(".svg")) {

            chain.doFilter(request, response);
            return;
        }

        boolean isProtected = AuthorizationUtils.isProtectedJsp(uri) 
                            || AuthorizationUtils.isProtectedAction(uri, action);
        
        boolean isAdmin = UserUtils.isAdmin(req);

        if (isProtected && !isAdmin) {
            res.sendRedirect("welcome.jsp");
        }else{
        }
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

}
