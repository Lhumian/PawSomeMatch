package com.petopia.loginsignup;

import java.util.Date;

public class DeliveryOrder {
    private int order_id;
    private String pet_id;
    private int buyer_id;
    private String buyer_phone;
    private double pet_price;
    private double total_price;
    private String payment_mode;
    private String city;
    private String barangay;
    private String street;
    private String order_date;
    private String delivery_progress;

    // Getters and Setters for order_id
    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    // Getters and Setters for pet_id
    public String getPetID() {
        return pet_id;
    }

    public void setPetID(String pet_id) {
        this.pet_id = pet_id;
    }

    // Getters and Setters for buyer_id
    public int getBuyerId() {
        return buyer_id;
    }

    public void setBuyerId(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    // Getters and Setters for buyer_phone
    public String getBuyerPhone() {
        return buyer_phone;
    }

    public void setBuyerPhone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    // Getters and Setters for pet_price
    public double getPetPrice() {
        return pet_price;
    }

    public void setPetPrice(double pet_price) {
        this.pet_price = pet_price;
    }

    // Getters and Setters for total_price
    public double getTotalPrice() {
        return total_price;
    }

    public void setTotalPrice(double total_price) {
        this.total_price = total_price;
    }

    // Getters and Setters for payment_mode
    public String getPaymentMode() {
        return payment_mode;
    }

    public void setPaymentMode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    // Getters and Setters for city
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // Getters and Setters for barangay
    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    // Getters and Setters for street
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    // Getters and Setters for order_date
    public String getOrderDate() {
        return order_date;
    }

    public void setOrderDate(String order_date) {
        this.order_date = order_date;
    }

    // Getters and Setters for delivery_progress
    public String getDeliveryProgress() {
        return delivery_progress;
    }

    public void setDeliveryProgress(String delivery_progress) {
        this.delivery_progress = delivery_progress;
    }
}
