
package model.dto;

/**
 * Status: Đã hoàn thành
 * Người thực hiện: Thịnh
 * Ngày bắt đầu: 09/06/2025
 * viết set up các field cho class này
 */
public class AddressDTO {
    private int id;
    private int country_id;
    private String unit_number;
    private String street_number;
    private String address_line1;
    private String address_line2;
    private String city;
    private String region;

    public AddressDTO() {
    }

    public AddressDTO(int id, int country_id, String unit_number, String street_number, String address_line1, String address_line2, String city, String region) {
        this.id = id;
        this.country_id = country_id;
        this.unit_number = unit_number;
        this.street_number = street_number;
        this.address_line1 = address_line1;
        this.address_line2 = address_line2;
        this.city = city;
        this.region = region;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getUnit_number() {
        return unit_number;
    }

    public void setUnit_number(String unit_number) {
        this.unit_number = unit_number;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public String getAddress_line1() {
        return address_line1;
    }

    public void setAddress_line1(String address_line1) {
        this.address_line1 = address_line1;
    }

    public String getAddress_line2() {
        return address_line2;
    }

    public void setAddress_line2(String address_line2) {
        this.address_line2 = address_line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    
}
