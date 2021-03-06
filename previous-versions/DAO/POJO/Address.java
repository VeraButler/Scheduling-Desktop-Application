package DAO.POJO;

import DAO.CityDao;
import DAO.CountryDao;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;

public class Address {
    private int addressId; // PK
    private String address;
    private String address2;
    private int cityId; // FK as address_ibfk_1 -- on update and delete restrict
    private String postalCode;
    private String phone;
    private Calendar createDate;
    private String createdBy;
    private Calendar lastUpdate;
    private String lastUpdateBy;

    public Address(int addressId, String address, String address2, int cityId, String postalCode, String phone, Calendar createDate, String createdBy, Calendar lastUpdate, String lastUpdateBy) {
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.cityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        // PK check
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        // FK check
        this.cityId = cityId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    @Override
    public String toString() {
        // get city name
        City city = null;
        int cityId;
        try {
            city = CityDao.getCity(this.cityId);
        } catch (ClassNotFoundException | ParseException | SQLException e) {
            e.printStackTrace();
        }

        // get country name
        Country country = null;
        try {
            assert city != null;
            country = CountryDao.getCountry(city.getCountryId());
        } catch (ClassNotFoundException | ParseException | SQLException e) {
            e.printStackTrace();
        }
        String cityName = city.getCity();
        String countryName = country.getCountry();
        Text addressTitle = new Text("Address: ");
        addressTitle.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 25));
        String address;
        if(address2.equals("")) {
            address = addressTitle.getText() + System.lineSeparator() +
                    getAddress() + System.lineSeparator() +
                    cityName + System.lineSeparator() +
                    getPostalCode() + System.lineSeparator() +
                    countryName + System.lineSeparator() +
                    System.lineSeparator() +
                    "Phone:" + getPhone();
        } else {
            address = "Address: " +
                    getAddress() + System.lineSeparator() +
                    getAddress2() + System.lineSeparator() +
                    city + System.lineSeparator() +
                    getPostalCode() + System.lineSeparator() +
                    country + System.lineSeparator() +
                    System.lineSeparator() +
                    "Phone:" + getPhone();
        }
        return address;
    }
}
