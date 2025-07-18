/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.dto.CategoryDTO;
import model.dto.ProductItemDTO;
import model.dto.VariationDTO;
import model.dto.VariationOptionDTO;

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
    
    public static Map<ProductItemDTO, List<VariationOptionDTO>> generateComb(
            int productId, 
            List<VariationDTO> variations, 
            List<VariationOptionDTO> options
    ){
        Map<Integer, List<VariationOptionDTO>> optionMap = new HashMap<>();
        for (VariationOptionDTO option : options) {
            optionMap.computeIfAbsent(option.getVariation_id(), k -> new ArrayList<>()).add(option);
        }
        Map<ProductItemDTO, List<VariationOptionDTO>> resultMap = new LinkedHashMap<>();
        backtrack(productId, variations, optionMap, 0, new ArrayList<>(), resultMap );
        return resultMap ;
    }
        
    public static Map<ProductItemDTO, List<VariationOptionDTO>> generateNewOptionComb(
            int productId,
            VariationOptionDTO newOption,
            List<VariationDTO> variations,
            List<VariationOptionDTO> allOptions
    ) {
        // 1. Lọc optionMap: chỉ giữ newOption ở variation của nó
        Map<Integer, List<VariationOptionDTO>> optionMap = new HashMap<>();

        for (VariationDTO var : variations) {
            if (var.getId() == newOption.getVariation_id()) {
                List<VariationOptionDTO> singleList = new ArrayList<>();
                singleList.add(newOption);
                optionMap.put(var.getId(), singleList);
            } else {
                List<VariationOptionDTO> opts = allOptions.stream()
                        .filter(o -> o.getVariation_id() == var.getId())
                        .collect(Collectors.toList());
                optionMap.put(var.getId(), opts);
            }
        }

        // 2. Gọi lại backtrack như bình thường
        Map<ProductItemDTO, List<VariationOptionDTO>> resultMap = new LinkedHashMap<>();
        backtrack(productId, variations, optionMap, 0, new ArrayList<>(), resultMap);
        return resultMap;
    }
    
    private static void backtrack(
             int productId,
            List<VariationDTO> variations,
            Map<Integer, List<VariationOptionDTO>> optionMap,
            int depth,
            List<VariationOptionDTO> currentOptions,
             Map<ProductItemDTO, List<VariationOptionDTO>> resultMap
    ){
        if (depth == variations.size()) {
            ProductItemDTO item = new ProductItemDTO();
            item.setProduct_id(productId);

            // Generate SKU từ các option name: e.g. "Red-Large"
            StringBuilder sb = new StringBuilder();
            sb.append("P").append(productId);
            for (VariationOptionDTO opt : currentOptions) {
                sb.append("-").append(opt.getValue());
            }
            item.setSku(sb.toString().toUpperCase());

            item.setPrice(0.0); // default price
            item.setQuantity_in_stock(0);   // default stock

            resultMap.put(item, new ArrayList<>(currentOptions));
            return;
        }

        VariationDTO currentVariation = variations.get(depth);
        List<VariationOptionDTO> options = optionMap.get(currentVariation.getId());

        if (options != null) {
            for (VariationOptionDTO option : options) {
                currentOptions.add(option);
                backtrack(productId, variations, optionMap, depth + 1, currentOptions, resultMap);
                currentOptions.remove(currentOptions.size() - 1);
            }
        }

    }
    
    public static void main(String[] args) {
        int productId = 101;

        // Tạo variation list
        List<VariationDTO> variations = new ArrayList<>();
        variations.add(new VariationDTO(1, 1, "Color"));
        variations.add(new VariationDTO(2, 1, "Size"));

        // Tạo option list
        List<VariationOptionDTO> options = new ArrayList<>();
        options.add(new VariationOptionDTO(1, 1, "Red"));
        options.add(new VariationOptionDTO(2, 1, "Blue"));
        options.add(new VariationOptionDTO(3, 2, "S"));
        options.add(new VariationOptionDTO(4, 2, "M"));

        // ✅ Test generateComb
        System.out.println("=== Tổ hợp toàn bộ:");
        Map<ProductItemDTO, List<VariationOptionDTO>> fullComb = generateComb(productId, variations, options);
        printCombMap(fullComb);

        // ✅ Giả lập thêm option mới cho Color
        VariationOptionDTO newOption = new VariationOptionDTO(5, 1, "Green"); // Green thuộc Color (variation_id = 1)

        System.out.println("\n=== Tổ hợp chỉ với option mới (Green):");
        Map<ProductItemDTO, List<VariationOptionDTO>> newComb = generateNewOptionComb(productId, newOption, variations, options);
        printCombMap(newComb);
    }

    private static void printCombMap(Map<ProductItemDTO, List<VariationOptionDTO>> map) {
        for (Map.Entry<ProductItemDTO, List<VariationOptionDTO>> entry : map.entrySet()) {
            ProductItemDTO item = entry.getKey();
            List<VariationOptionDTO> opts = entry.getValue();

            // Java 8 compatible join
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < opts.size(); i++) {
                sb.append(opts.get(i).getValue());
                if (i < opts.size() - 1) sb.append(", ");
            }

            System.out.println("SKU: " + item.getSku() + " | Options: [" + sb.toString() + "]");
            System.out.println(item);
        }
    }
}
