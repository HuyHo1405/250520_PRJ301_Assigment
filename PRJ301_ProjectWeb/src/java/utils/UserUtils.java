
package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.dto.UserDTO;

public class UserUtils {

    public static boolean isLoggedIn(HttpServletRequest request){
        return getUser(request) != null;
    }
    
    public static int getUserId(HttpServletRequest request){
        if(isLoggedIn(request)){
            return getUser(request).getId();
        }
        return -1;
    }
    
    public static UserDTO getUser(HttpServletRequest request){
        HttpSession ss = request.getSession();
        if(ss != null){
            return (UserDTO) ss.getAttribute("user");
        }
        return null;
    }
    
    private static boolean hasRole(HttpServletRequest request, String role){
        UserDTO user = getUser(request);
        if(user == null)
            return false;
        return user.getRole().equals(role);
    }
    
    public static boolean isAdmin(HttpServletRequest request){
        return hasRole(request, "AD");
    }
    
    public static boolean isMemeber(HttpServletRequest request){
        return hasRole(request, "MB");
    }
    
}
