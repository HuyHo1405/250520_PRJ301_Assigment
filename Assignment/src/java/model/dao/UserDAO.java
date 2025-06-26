package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.UserDTO;
import utils.DbUtils;
import utils.HashUtils;

/**
 * Status: đã hoàn thành Người thực hiện: Huy Ngày bắt đầu: 13/06/2025 thêm role
 * cho user
 */
public class UserDAO {

    private static final String TABLE_NAME = "users";

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

    //crud
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
        String sql = "UPDATE " + TABLE_NAME + " SET is_active = FALSE WHERE id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in disableUser(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        UserDAO udao = new UserDAO();
        List<UserDTO> ls = udao.retrieve("1=1");
        for (UserDTO user : ls) {
            System.out.println(user);
            user.setHashed_password(HashUtils.hashPassword(user.getHashed_password()));
            udao.update(user);
        }

    }

}
