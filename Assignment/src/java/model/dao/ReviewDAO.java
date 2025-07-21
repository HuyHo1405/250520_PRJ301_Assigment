package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.dto.ReviewDTO;
import utils.DbUtils;

public class ReviewDAO {
    private static final String TABLE_NAME = "review";

    private ReviewDTO mapToReview(ResultSet rs) throws SQLException {
        return new ReviewDTO(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getInt("ordered_product_id"),
            rs.getInt("rating_value"),
            rs.getString("comment")
        );
    }

    public List<ReviewDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            List<ReviewDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapToReview(rs));
            }
            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean create(ReviewDTO review) {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, ordered_product_id, rating_value, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, review.getUser_id());
            ps.setInt(2, review.getOrdered_product_id());
            ps.setInt(3, review.getRating_value());
            ps.setString(4, review.getComment());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                review.setId(generatedKeys.getInt(1));
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(ReviewDTO review) {
        String sql = "UPDATE " + TABLE_NAME + " SET user_id = ?, ordered_product_id = ?, rating_value = ?, comment = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, review.getUser_id());
            ps.setInt(2, review.getOrdered_product_id());
            ps.setInt(3, review.getRating_value());
            ps.setString(4, review.getComment());
            ps.setInt(5, review.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in update(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public ReviewDTO findById(int id) {
        List<ReviewDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public boolean exists(int id) {
        String sql = "SELECT 1 FROM " + TABLE_NAME + " WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.err.println("Error in exists(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public int count() {
        String sql = "SELECT COUNT(*) AS total FROM " + TABLE_NAME;
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            System.err.println("Error in count(): " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public List<ReviewDTO> getByUserId(int userId) {
        return retrieve("user_id = ?", userId);
    }

    public List<ReviewDTO> getByOrderedProductId(int orderedProductId) {
        return retrieve("ordered_product_id = ?", orderedProductId);
    }
}
