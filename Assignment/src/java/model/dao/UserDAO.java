/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.UserDTO;
import utils.DbUtils;

/**
 * Status: Chờ thực hiện Người thực hiện: [...........] Ngày bắt đầu:
 * [...........] viết các CRUD cần thiết
 */
public class UserDAO {

    private static final String TABLE_NAME = "users";

    private UserDTO mapToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email_address");
        String phone = rs.getString("phone_number");
        String password = rs.getString("hashed_password");
        return new UserDTO(id, email, phone, password);
    }

    private List<UserDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<UserDTO> list = new ArrayList<>();

            while (rs.next()) {
                list.add(mapToUser(rs));
            }

            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public List<UserDTO> getAllUsers() {
        return retrieve("1 = 1");
    }

    public UserDTO getUserById(int id) {
        List<UserDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public UserDTO getUserByEmail(String email) {
        List<UserDTO> list = retrieve("email_address = ?", email);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public boolean create(UserDTO user) {
        String sql = "INSERT INTO " + TABLE_NAME + " (email_address, phone_number, hashed_password) VALUES (?, ?, ?)";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail_address());
            ps.setString(2, user.getPhone_number());
            ps.setString(3, user.getHashed_password());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(UserDTO user) {
        String sql = "UPDATE " + TABLE_NAME + " SET email_address = ?, phone_number = ?, hashed_password = ? WHERE id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail_address());
            ps.setString(2, user.getPhone_number());
            ps.setString(3, user.getHashed_password());
            ps.setInt(4, user.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in update(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
