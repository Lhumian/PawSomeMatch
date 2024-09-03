package com.petopia.loginsignup;

public class CancelData {
    private int pet_ID;
    private String pet_name;
    private String buyer_phone;
    private double pet_price;
    private double total_price;
    private String payment_mode;
    private String city;
    private String barangay;
    private String street;
    private String order_date;
    private String reason;
    private String status;
    private String cancel_date;

    // Constructors
    public CancelData(int pet_ID, String pet_name, String buyer_phone, double pet_price, double total_price, String payment_mode, String city, String barangay, String street, String order_date, String reason, String status, String cancel_date) {
        this.pet_ID = pet_ID;
        this.pet_name = pet_name;
        this.buyer_phone = buyer_phone;
        this.pet_price = pet_price;
        this.total_price = total_price;
        this.payment_mode = payment_mode;
        this.city = city;
        this.barangay = barangay;
        this.street = street;
        this.order_date = order_date;
        this.reason = reason;
        this.status = status;
        this.cancel_date = cancel_date;
    }

    // Getters and setters
    public int getPet_ID() {
        return pet_ID;
    }

    public void setPet_ID(int pet_ID) {
        this.pet_ID = pet_ID;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    public double getPet_price() {
        return pet_price;
    }

    public void setPet_price(double pet_price) {
        this.pet_price = pet_price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
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

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancel_date() {
        return cancel_date;
    }

    public void setCancel_date(String cancel_date) {
        this.cancel_date = cancel_date;
    }
}
