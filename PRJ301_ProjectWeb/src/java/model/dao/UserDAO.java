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
 * Status: đã hoàn thành Người thực hiện: Huy Ngày bắt đầu: 13/06/2025 thêm role
 * cho user
 */
public class UserDAO {

    private static final String TABLE_NAME = "users";

    public List<UserDTO> getAllUsers() {
        List<UserDTO> userList = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try {
            Connection conn = DbUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setId(rs.getInt("id"));
                user.setEmail_address(rs.getString("email_address"));
                user.setPhone_number(rs.getString("phone_number"));
                user.setHashed_password(rs.getString("hashed_password"));
                user.setIs_active(rs.getBoolean("is_active"));
                user.setRole(rs.getString("role"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public boolean updatePassword(int userID, String newPassword) {
        String sql = "UPDATE " + TABLE_NAME + " SET hashed_password = ? WHERE id = ?";
        try {
            Connection conn = DbUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setInt(2, userID);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //map
    private UserDTO mapToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email_address");
        String phone = rs.getString("phone_number");
        String password = rs.getString("hashed_password");
        String role = rs.getString("role");
        boolean is_active = rs.getBoolean("is_active");
        return new UserDTO(id, email, phone, password, role, is_active);
    }

    //basic crud
    public List<UserDTO> retrieve(String condition, Object... params) {
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

    public boolean create(UserDTO user) {
        String sql = "INSERT INTO " + TABLE_NAME + " (email_address, phone_number, hashed_password, role, is_active) VALUES (?, ?, ?, ?, ?)";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail_address());
            ps.setString(2, user.getPhone_number());
            ps.setString(3, user.getHashed_password());
            ps.setString(4, user.getRole());
            ps.setBoolean(5, user.getIs_active()); // explicitly set is_active = true
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

    //advance crud
    public UserDTO findById(int id) {
        List<UserDTO> list = retrieve("id = ?", id);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    public UserDTO findByEmail(String email) {
        List<UserDTO> list = retrieve("email_address = ?", email);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    public UserDTO findByPhone(String phone) {
        List<UserDTO> list = retrieve("phone_number = ?", phone);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM " + TABLE_NAME + " WHERE email_address = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.err.println("Error in existsByEmail(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRole(int userId, String newRole) {
        String sql = "UPDATE " + TABLE_NAME + " SET role = ? WHERE id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newRole);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in updateRole(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean disableUser(int userId) {
        String sql = "UPDATE " + TABLE_NAME + " SET is_active = 0 WHERE id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in disableUser(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
