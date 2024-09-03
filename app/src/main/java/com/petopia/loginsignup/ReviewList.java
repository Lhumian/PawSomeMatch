package com.petopia.loginsignup;

public class ReviewList {
    private String pet_ID;
    private String pet_name;
    private String reference;
    private String buyer_name;
    private int rating;
    private String description;

    // Constructor
    public ReviewList(String pet_ID, String pet_name, String reference, String buyer_name, int rating, String description) {
        this.pet_ID = pet_ID;
        this.pet_name = pet_name;
        this.reference = reference;
        this.buyer_name = buyer_name;
        this.rating = rating;
        this.description = description;
    }

    // Getter methods
    public String getPet_ID() {
        return pet_ID;
    }

    public String getPet_name() {
        return pet_name;
    }

    public String getReference() {
        return reference;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public int getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }
}
