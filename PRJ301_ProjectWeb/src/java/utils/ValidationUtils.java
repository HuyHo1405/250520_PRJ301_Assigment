/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author ho huy
 */
public class ValidationUtils {

    private static boolean isValid(String str, String regex) {
        return str != null && str.matches(regex);
    }

    public static boolean isValidEmail(String email) {
        return isValid(email, RegexPatterns.EMAIL);
    }

    public static boolean isValidPhone(String phone) {
        return isValid(phone, RegexPatterns.PHONE);
    }

    public static String isNull(String name, Object obj) {
        return obj == null ? "Không tìm thấy " + name : null;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isInvalidId(int id) {
        return id == -1;
    }

    public static String checkLength(String name, String value, int maxLength) {
        return (value.length() > maxLength) ? name + " quá dài (tối đa " + maxLength + " ký tự)" : null;
    }
}
