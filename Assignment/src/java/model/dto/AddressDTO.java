package model.dto;

import java.util.Objects;

public class AddressDTO {
    private int id;
    private int countryId;
    private String unitNumber;
    private String streetNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String region;
    private String fullAddress;

    public AddressDTO() {
        this.id = -1;
    }

    public AddressDTO(int countryId, String unitNumber, String streetNumber, String addressLine1, String addressLine2, String city, String region) {
        this(-1, countryId, unitNumber, streetNumber, addressLine1, addressLine2, city, region);
    }

    public AddressDTO(int id, int countryId, String unitNumber, String streetNumber, String addressLine1, String addressLine2, String city, String region) {
        this.id = id;
        this.countryId = countryId;
        this.unitNumber = unitNumber;
        this.streetNumber = streetNumber;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.region = region;
        this.fullAddress = generateFullAddress();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
        updateFullAddress();
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        updateFullAddress();
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        updateFullAddress();
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        updateFullAddress();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        updateFullAddress();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
        updateFullAddress();
    }

    public String getFullAddress() {
        return fullAddress;
    }

    private void updateFullAddress() {
        this.fullAddress = generateFullAddress();
    }

    private String generateFullAddress() {
        StringBuilder sb = new StringBuilder();
        appendIfNotNull(sb, unitNumber);
        appendIfNotNull(sb, streetNumber);
        appendIfNotNull(sb, addressLine1);
        appendIfNotNull(sb, addressLine2);
        appendIfNotNull(sb, city);
        appendIfNotNull(sb, region);
        return sb.toString().replaceAll(", $", "");
    }

    private void appendIfNotNull(StringBuilder sb, String value) {
        if (Objects.nonNull(value) && !value.trim().isEmpty()) {
            sb.append(value.trim()).append(", ");
        }
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", unitNumber='" + unitNumber + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", fullAddress='" + fullAddress + '\'' +
                '}';
    }
}
