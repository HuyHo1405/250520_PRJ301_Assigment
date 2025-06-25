package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthenticationUtils {

    // Danh sách các JSP cần đăng nhập mới được truy cập
    private static final List<String> PROTECTED_JSP = Arrays.asList(
            "/user-form.jsp",
            "/welcome.jsp",
            "/profile.jsp",
            "/address-management.jsp",
            "/address-form.jsp",
            "/admin-order-management.jsp",
            "/order-details.jsp",
            "/system-config.jsp",
            "/index.jsp"
    );

    // Các ACTION cần xác thực theo controller
    public static final List<String> ADDRESS_ACTIONS = Arrays.asList(
            "toProfile",
            "toAddressManagement",
            "toAddAddress",
            "toEditAddress",
            "addAddress", 
            "searchAddress",
            "updateAddress", 
            "updateDefaultAddress", 
            "removeAddress"
    );

    public static final List<String> USER_ACTIONS = Arrays.asList(
            "logout", 
            "changePassword", 
            "updateProfile"
    );

    public static final List<String> CART_ACTIONS = Arrays.asList(
            "getCart", 
            "addToCart", 
            "updateCart", 
            "removeFromCart", 
            "clearCart", 
            "checkoutCart"
    );

    public static final List<String> ORDER_ACTIONS = Arrays.asList(
            "listMyOrders", 
            "viewMyOrder", 
            "placeOrder", 
            "cancelOrder", 
            "trackOrder"
    );

    public static final List<String> REVIEW_ACTIONS = Arrays.asList(
            "submitReview", 
            "updateReview", 
            "deleteReview"
    );

    public static final List<String> ADMIN_ORDER_ACTIONS = Arrays.asList(
            "toAdminOrdersPage", 
            "viewOrderDetail", 
            "updateOrderStatus",
            "disableOrder", 
            "searchOrders", 
            "exportOrders"
    );

    public static final List<String> ADMIN_PRODUCT_ACTIONS = Arrays.asList(
            "listAllProducts", 
            "viewProductDetail", 
            "createProduct", 
            "updateProduct",
            "disableProduct",
            "uploadProductImages", 
            "searchProducts"
    );

    public static final List<String> ADMIN_USER_ACTIONS = Arrays.asList(
            "listAllUsers", 
            "viewUserDetail", 
            "createUser", 
            "updateUser",
            "disableUser", 
            "changeUserRole", 
            "resetUserPassword"
    );

    public static final List<String> SYSTEM_CONFIG_ACTIONS = Arrays.asList(
            "getSystemConfig", 
            "updateSystemConfig", 
            "getAppVersion", 
            "clearSystemCache"
    );

    public static final List<String> MAIN_ACTIONS = Arrays.asList(
            "toWelcome"
    );

    // Dữ liệu ánh xạ controller → actions
    private static final Map<String, List<String>> PROTECTED_ACTIONS_MAP = createProtectedActionsMap();

    private static Map<String, List<String>> createProtectedActionsMap() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("AddressController", ADDRESS_ACTIONS);
        map.put("UserController", USER_ACTIONS);
        map.put("CartController", CART_ACTIONS);
        map.put("OrderController", ORDER_ACTIONS);
        map.put("ReviewController", REVIEW_ACTIONS);
        map.put("AdminOrderController", ADMIN_ORDER_ACTIONS);
        map.put("AdminProductController", ADMIN_PRODUCT_ACTIONS);
        map.put("AdminUserController", ADMIN_USER_ACTIONS);
        map.put("SystemConfigController", SYSTEM_CONFIG_ACTIONS);
        map.put("MainController", MAIN_ACTIONS);
        return map;
    }

    public static boolean isProtectedAction(String uri, String action) {
        if (uri == null || action == null) {
            return false;
        }

        String controller = uri.substring(uri.lastIndexOf("/") + 1);
        List<String> actions = PROTECTED_ACTIONS_MAP.get(controller);
        return actions != null && actions.contains(action);
    }

    public static boolean isProtectedJsp(String uri) {
        return uri != null && PROTECTED_JSP.stream().anyMatch(uri::endsWith);
    }
}
