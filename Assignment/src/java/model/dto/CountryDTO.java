package model.dto;

public class CountryDTO {
    private int id;
    private String country_name;
    private boolean is_active = true;

    public CountryDTO() {
    }

    public CountryDTO(int id, String country_name) {
        this.id = id;
        this.country_name = country_name;
    }

    public CountryDTO(String country_name) {
        this.id = -1;
        this.country_name = country_name;
        this.is_active = true;
    }

    public CountryDTO(String country_name, boolean is_active) {
        this.id = -1;
        this.country_name = country_name;
        this.is_active = is_active;
    }
    
    public CountryDTO(int id, String country_name, boolean is_active) {
        this.id = id;
        this.country_name = country_name;
        this.is_active = is_active;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    @Override
    public String toString() {
        return "CountryDTO{" + "id=" + id + ", country_name=" + country_name + '}';
    }

}
