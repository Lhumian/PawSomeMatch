package com.petopia.loginsignup;

public class CompletedTransaction {
    private String petId;
    private String petName;
    private String seller;
    private String reference;
    private double price;
    private String orderDate;

    // Constructor
    public CompletedTransaction(String petId, String petName, String seller, String reference, double price, String orderDate) {
        this.petId = petId;
        this.petName = petName;
        this.seller = seller;
        this.reference = reference;
        this.price = price;
        this.orderDate = orderDate;
    }

    // Getters
    public String getPetId() {
        return petId;
    }

    public String getPetName() {
        return petName;
    }

    public String getSeller() {
        return seller;
    }

    public String getReference() {
        return reference;
    }

    public double getPrice() {
        return price;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
