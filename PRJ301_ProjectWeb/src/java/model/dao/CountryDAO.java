package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.CountryDTO;
import utils.DbUtils;

/**
 * Status: đã hoàn thành Người thực hiện: Thịnh Ngày bắt đầu: 09/06/2025 viết
 * crud cho class này
 */
public class CountryDAO {

    private static final String TABLE_NAME = "country";

    //map
    public CountryDTO mapToCountry(ResultSet rs) throws SQLException {
        return new CountryDTO(
                rs.getInt("id"),
                rs.getString("country_name"),
                rs.getBoolean("is_active")
        );
    }

    //crud
    public List<CountryDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;

        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<CountryDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapToCountry(rs));
            }

            return list;
        } catch (Exception e) {
            System.err.println("Error in retrieve(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
    
    public List<CountryDTO> getAllCountries(){
        return retrieve("1 = 1");
    }

    public boolean create(CountryDTO country) {
        String sql = "INSERT INTO " + TABLE_NAME + " (country_name) VALUES (?)";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, country.getCountry_name());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(CountryDTO country) {
        String sql = "UPDATE " + TABLE_NAME + " SET country_name = ? WHERE id = ?";
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, country.getCountry_name());
            ps.setInt(2, country.getId());
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

    
    public boolean disableCountry(int id) {
        String sql = "UPDATE " + TABLE_NAME + " SET is_active = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, false); // Set is_active to false
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in disableCountry(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
