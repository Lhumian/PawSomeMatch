package com.petopia.loginsignup;

import java.io.Serializable;

public class AddressModel implements Serializable{
    private String name;
    private String phone;
    private String city;
    private String barangay;
    private String street;

    // Constructor
    public AddressModel(String name, String phone, String city, String barangay, String street) {
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.barangay = barangay;
        this.street = street;
    }

    // Getter and Setter methods for each field
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
