package com.petopia.loginsignup;

import android.os.Parcel;
import android.os.Parcelable;

public class Pets implements Parcelable {
    private String pet_ID, pet_name, category, breed, age, weight, vaccine_status, gender, color, breedtype, studfee, shares;

    public Pets(String pet_ID, String pet_name, String category, String breed, String age, String weight, String vaccine_status, String gender, String color, String breedtype, String studfee, String shares) {
        this.pet_ID = pet_ID;
        this.pet_name = pet_name;
        this.category = category;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.vaccine_status = vaccine_status;
        this.gender = gender;
        this.color = color;
        this.breedtype = breedtype;
        this.studfee = studfee;
        this.shares = shares;
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

    public String getBreedType() {
        return breedtype;
    }

    public void setBreedType(String breedtype) {
        this.breedtype = breedtype;
    }

    public String getStudfee() {
        return studfee;
    }

    public void setStudfee(String studfee) {
        this.studfee = studfee;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    // Parcelable implementation

    protected Pets(Parcel in) {
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
        studfee = in.readString();
        shares = in.readString();
    }

    public static final Creator<Pets> CREATOR = new Creator<Pets>() {
        @Override
        public Pets createFromParcel(Parcel in) {
            return new Pets(in);
        }

        @Override
        public Pets[] newArray(int size) {
            return new Pets[size];
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
        dest.writeString(studfee);
        dest.writeString(shares);
    }
}
