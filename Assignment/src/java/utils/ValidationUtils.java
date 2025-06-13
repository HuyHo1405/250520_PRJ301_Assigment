/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author ho huy
 */
public class ValidationUtils {
    
    private static boolean isValid(String str, String regex){
        return str != null && str.matches(regex);
    }
    
    public static boolean isValidEmail(String email){
        return isValid(email, RegexPatterns.EMAIL);
    }
    
    public static boolean isValidPhone(String phone){
        return isValid(phone, RegexPatterns.PHONE);
    }
    
}
