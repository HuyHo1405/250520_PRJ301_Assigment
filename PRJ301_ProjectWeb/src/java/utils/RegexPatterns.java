
package utils;

public class RegexPatterns {
    
    public static final String EMAIL = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    public static final String PHONE = "^0\\d{9}$";
    
    public static void main(String[] args) {
        System.out.println("0123456789".matches(PHONE));
    }
}
