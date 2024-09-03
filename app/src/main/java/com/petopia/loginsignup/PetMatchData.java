package com.petopia.loginsignup;

import android.widget.ImageView;

public class PetMatchData {
    private int pet_ID;
    private int user_id;
    private String pet_name;
    private String category;
    private String breed;
    private int age;
    private String weight;
    private String vaccine_status;
    private String gender;
    private String color;
    private String breedtype;
    private String owner_name;

    private String studfee;
    private String shares;

    private String imageUrl;

    public PetMatchData(int pet_ID, int user_id, String pet_name, String category, String breed, int age, String weight, String vaccine_status, String gender, String color, String breedtype, String owner_name, String studfee, String shares, String imageUrl) {
        this.pet_ID = pet_ID;
        this.user_id = user_id;
        this.pet_name = pet_name;
        this.category = category;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.vaccine_status = vaccine_status;
        this.gender = gender;
        this.color = color;
        this.breedtype = breedtype;
        this.owner_name = owner_name;
        this.studfee = studfee;
        this.shares = shares;
        this.imageUrl = imageUrl;
    }

    public PetMatchData() {
        // Default constructor required for calls to DataSnapshot.getValue(Pet.class)
    }

    public int getPet_ID() {
        return pet_ID;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getPet_name() {
        return pet_name;
    }

    public String getCategory() {
        return category;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public String getWeight() {
        return weight;
    }

    public String getVaccine_status() {
        return vaccine_status;
    }

    public String getGender() {
        return gender;
    }

    public String getColor() {
        return color;
    }

    public String getBreedtype() {return breedtype;}
    public String getStudfee() {return studfee;}
    public String getShares() {return shares;}

    public String getOwner_name() {
        return owner_name;
    } // Getter method for owner's name

    public void setPet_ID(int pet_ID) {
        this.pet_ID = pet_ID;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setVaccine_status(String vaccine_status) {
        this.vaccine_status = vaccine_status;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public void setBreedtype(String breedtype) {
        this.breedtype = breedtype;
    }

    public void setStudfee(String studfee) {this.studfee = studfee;}
    public void setShares(String shares) {this.shares = shares;}

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


