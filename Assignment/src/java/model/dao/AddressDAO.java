package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.dto.AddressDTO;
import utils.DbUtils;

public class AddressDAO {

    private static final String TABLE_NAME = "address";

    private AddressDTO mapToAddress(ResultSet rs) throws SQLException {
        return new AddressDTO(
                rs.getInt("id"),
                rs.getInt("country_id"),
                rs.getString("unit_number"),
                rs.getString("street_number"),
                rs.getString("address_line1"),
                rs.getString("address_line2"),
                rs.getString("city"),
                rs.getString("region"),
                rs.getString("full_address")
        );
    }

    public List<AddressDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<AddressDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapToAddress(rs));
            }

            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean create(AddressDTO address) {
        String sql = "INSERT INTO " + TABLE_NAME + " (country_id, unit_number, street_number, address_line1, address_line2, city, region, full_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, address.getCountryId());
            ps.setString(2, address.getUnitNumber());
            ps.setString(3, address.getStreetNumber());
            ps.setString(4, address.getAddressLine1());
            ps.setString(5, address.getAddressLine2());
            ps.setString(6, address.getCity());
            ps.setString(7, address.getRegion());
            ps.setString(8, address.getFullAddress());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public Integer createAndReturnId(AddressDTO address) {
        String sql = "INSERT INTO " + TABLE_NAME + " (country_id, unit_number, street_number, address_line1, address_line2, city, region, full_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, address.getCountryId());
            ps.setString(2, address.getUnitNumber());
            ps.setString(3, address.getStreetNumber());
            ps.setString(4, address.getAddressLine1());
            ps.setString(5, address.getAddressLine2());
            ps.setString(6, address.getCity());
            ps.setString(7, address.getRegion());
            ps.setString(8, address.getFullAddress());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating address failed, no rows affected.");
            }

            try ( ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating address failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error in createAndReturnId(): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<AddressDTO> getUserAddress(int userId){
        String sql = "select a.* from address a \n"
                + "join user_address ua on a.id = ua.address_id\n"
                + "where ua.user_id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, userId);
            ResultSet rs = ps.executeQuery();
            List<AddressDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapToAddress(rs));
            }

            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    public boolean update(AddressDTO address) {
        String sql = "UPDATE " + TABLE_NAME + " SET country_id = ?, unit_number = ?, street_number = ?, address_line1 = ?, address_line2 = ?, city = ?, region = ? WHERE id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, address.getCountryId());
            ps.setString(2, address.getUnitNumber());
            ps.setString(3, address.getStreetNumber());
            ps.setString(4, address.getAddressLine1());
            ps.setString(5, address.getAddressLine2());
            ps.setString(6, address.getCity());
            ps.setString(7, address.getRegion());
            ps.setInt(8, address.getId());

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

    public AddressDTO findById(int id) {
        List<AddressDTO> list = retrieve("id = ?", id);
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean existsById(int id) {
        return !retrieve("id = ?", id).isEmpty();
    }
    
    public int countByCountryId(int countryId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE country_id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println("Error in countByCountryId(): " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    public static void main(String[] args) {
        AddressDAO dao = new AddressDAO();
        for (AddressDTO a : dao.getUserAddress(12)) {
            System.out.println(a);
        }
    }
}
