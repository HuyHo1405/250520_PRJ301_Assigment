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
import model.dto.AddressDTO;
import utils.DbUtils;

/**
 *
 * @author Admin
 */
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
            rs.getString("region")
        );
    }

    private List<AddressDTO> retrieve(String condition, Object... params) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + condition;
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
        return null;
    }

    public List<AddressDTO> getAllAddresses() {
        return retrieve("1 = 1");
    }

    public AddressDTO getAddressById(int id) {
        List<AddressDTO> list = retrieve("id = ?", id);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public boolean create(AddressDTO address) {
        String sql = "INSERT INTO " + TABLE_NAME + " (country_id, unit_number, street_number, address_line1, address_line2, city, region) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, address.getCountry_id());
            ps.setString(2, address.getUnit_number());
            ps.setString(3, address.getStreet_number());
            ps.setString(4, address.getAddress_line1());
            ps.setString(5, address.getAddress_line2());
            ps.setString(6, address.getCity());
            ps.setString(7, address.getRegion());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in create(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(AddressDTO address) {
        String sql = "UPDATE " + TABLE_NAME + " SET country_id = ?, unit_number = ?, street_number = ?, address_line1 = ?, address_line2 = ?, city = ?, region = ? WHERE id = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, address.getCountry_id());
            ps.setString(2, address.getUnit_number());
            ps.setString(3, address.getStreet_number());
            ps.setString(4, address.getAddress_line1());
            ps.setString(5, address.getAddress_line2());
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
        try (Connection conn = DbUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
