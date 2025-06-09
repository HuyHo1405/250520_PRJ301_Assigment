/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

/**
 *
 * @author Admin
 */
public class ProductConfigDTO {
    private int item_id;
    private int option_id;

    public ProductConfigDTO() {
    }

    public ProductConfigDTO(int item_id, int option_id) {
        this.item_id = item_id;
        this.option_id = option_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }
    
}
