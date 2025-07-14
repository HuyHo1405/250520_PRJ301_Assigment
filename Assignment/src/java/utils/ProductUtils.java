/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.dto.CategoryDTO;

/**
 *
 * @author ho huy
 */
public class ProductUtils {
    
    public static Map<Integer, String> getCategoryMap(List<CategoryDTO> list) {
        Map<Integer, String> map = new HashMap<>();
        for (CategoryDTO category : list) {
            map.put(category.getId(), category.getName());
        }
        return map;
    }
    

}
