/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class CacheManager {
    private static final Map<String, Object> CACHE = new HashMap<>();

    public static void put(String key, Object value) {
        CACHE.put(key, value);
    }

    public static Object get(String key) {
        return CACHE.get(key);
    }

    public static void clear() {
        CACHE.clear();
        System.out.println("Cache cleared by SimpleCacheManager");
    }

    public static boolean isEmpty() {
        return CACHE.isEmpty();
    }
}
