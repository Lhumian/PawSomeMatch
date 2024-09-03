package com.petopia.loginsignup;

import android.os.Parcel;
import android.os.Parcelable;

public class SellPets implements Parcelable {
    private String pet_ID, pet_name, category, breed, age, weight, vaccine_status, gender, color, description, breedtype;
    private float price;

    public SellPets(String pet_ID, String pet_name, String category, String breed, String age, String weight, String vaccine_status, String gender, String color, String description, String breedtype, float price) {
        this.pet_ID = pet_ID;
        this.pet_name = pet_name;
        this.category = category;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.vaccine_status = vaccine_status;
        this.gender = gender;
        this.color = color;
        this.price = price;
        this.description = description;
        this.breedtype = breedtype;
    }

    public String getPet_ID() {
        return pet_ID;
    }

    public void setPet_ID(String pet_ID) {
        this.pet_ID = pet_ID;
    }

    public String getPet_Name() {
        return pet_name;
    }

    public void setPet_Name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVaccine_status() {
        return vaccine_status;
    }

    public void setVaccine_status(String vaccine_status) {
        this.vaccine_status = vaccine_status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBreedtype() {
        return breedtype;
    }

    public void setBreedtype(String breedtype) {
        this.description = breedtype;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Parcelable implementation

    protected SellPets(Parcel in) {
        pet_ID = in.readString();
        pet_name = in.readString();
        category = in.readString();
        breed = in.readString();
        age = in.readString();
        weight = in.readString();
        vaccine_status = in.readString();
        gender = in.readString();
        color = in.readString();
        breedtype = in.readString();
        price = in.readFloat();
        description = in.readString();
    }

    public static final Creator<SellPets> CREATOR = new Creator<SellPets>() {
        @Override
        public SellPets createFromParcel(Parcel in) {
            return new SellPets(in);
        }

        @Override
        public SellPets[] newArray(int size) {
            return new SellPets[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pet_ID);
        dest.writeString(pet_name);
        dest.writeString(category);
        dest.writeString(breed);
        dest.writeString(age);
        dest.writeString(weight);
        dest.writeString(vaccine_status);
        dest.writeString(gender);
        dest.writeString(color);
        dest.writeString(breedtype);
        dest.writeFloat(price);
        dest.writeString(description);
    }
}
