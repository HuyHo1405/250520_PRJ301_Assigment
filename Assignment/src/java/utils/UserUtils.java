
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
    
    private static boolean hasRole(UserDTO user, String role){
        return user.getRole().equals(role);
    }
    
    public static boolean isAdmin(UserDTO user){
        return hasRole(user, "AD");
    }
    
    public static boolean isMemeber(UserDTO user){
        return hasRole(user, "MB");
    }
    
}
