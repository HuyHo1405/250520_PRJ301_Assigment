/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

/**
 * Status: Chờ thực hiện
 * Người thực hiện: [...........]
 * Ngày bắt đầu: [...........]
 * viết set up các field cho class này
 */
public class ReviewDTO {
    private int id;
    private int user_id;
    private int ordered_product_id;
    private int rating_value;
    private String comment;

    public ReviewDTO() {
    }

    public ReviewDTO(int id, int user_id, int ordered_product_id, int rating_value, String comment) {
        this.id = id;
        this.user_id = user_id;
        this.ordered_product_id = ordered_product_id;
        this.rating_value = rating_value;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getOrdered_product_id() {
        return ordered_product_id;
    }

    public void setOrdered_product_id(int ordered_product_id) {
        this.ordered_product_id = ordered_product_id;
    }

    public int getRating_value() {
        return rating_value;
    }

    public void setRating_value(int rating_value) {
        this.rating_value = rating_value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
