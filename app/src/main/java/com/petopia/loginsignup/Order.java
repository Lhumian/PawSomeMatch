package com.petopia.loginsignup;

public class Order {
    private String petName;
    private String buyerPhone;
    private double petPrice;
    private double totalPrice;
    private String paymentMode;
    private String city;
    private String barangay;
    private String street;
    private String orderDate;
    private String deliveryProgress;
    private int petId;

    public Order(String petName, String buyerPhone, double petPrice, double totalPrice, String paymentMode, String city, String barangay, String street, String orderDate, String deliveryProgress, int petId) {
        this.petName = petName;
        this.buyerPhone = buyerPhone;
        this.petPrice = petPrice;
        this.totalPrice = totalPrice;
        this.paymentMode = paymentMode;
        this.city = city;
        this.barangay = barangay;
        this.street = street;
        this.orderDate = orderDate;
        this.deliveryProgress = deliveryProgress;
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public double getPetPrice() {
        return petPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getCity() {
        return city;
    }

    public String getBarangay() {
        return barangay;
    }

    public String getStreet() {
        return street;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getDeliveryProgress() {
        return deliveryProgress;
    }
    public int getPetId() {
        return petId;
    }
}
