
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.UserAddressDTO;
import utils.DbUtils;

public class UserAddressDAO {
    private static final String TABLE_NAME = "user_address";

    private UserAddressDTO mapToUserAddress(ResultSet rs) throws SQLException {
        return new UserAddressDTO(
            rs.getInt("user_id"),
            rs.getInt("address_id"),
            rs.getBoolean("is_default")
        );
    }

    public List<UserAddressDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<UserAddressDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapToUserAddress(rs));
            }
            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean create(UserAddressDTO userAddress) {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, address_id, is_default) VALUES (?, ?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userAddress.getUser_id());
            ps.setInt(2, userAddress.getAddress_id());
            ps.setBoolean(3, userAddress.isIs_default());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in add(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(UserAddressDTO userAddress) {
        String sql = "UPDATE " + TABLE_NAME + " SET is_default = ? WHERE user_id = ? AND address_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userAddress.getUser_id());
            ps.setInt(2, userAddress.getAddress_id());
            ps.setBoolean(3, userAddress.isIs_default());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in update(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int userId, int addressId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE user_id = ? AND address_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, addressId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public List<UserAddressDTO> findByUserId(int userId) {
        return retrieve("user_id = ?", userId);
    }

    public UserAddressDTO getDefaultAddressByUserId(int userId) {
        List<UserAddressDTO> list = retrieve("user_id = ? AND is_default = 1", userId);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    public boolean unsetDefaultAddress(int userId) {
        String sql = "UPDATE " + TABLE_NAME + " SET is_default = 0 WHERE user_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in unsetDefaultAddress(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean exists(int userId, int addressId) {
        String sql = "SELECT 1 FROM " + TABLE_NAME + " WHERE user_id = ? AND address_id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, addressId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.err.println("Error in exists(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
