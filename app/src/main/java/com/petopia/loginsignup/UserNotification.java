package com.petopia.loginsignup;

public class UserNotification {
    private  int like_ID;
    private int userId;
    private String firstName;
    private String lastName;
    private String petId; // Include pet_ID field
    private String petName;
    private String likedPetId; // Include liked_pet_ID field
    private boolean isAccepted;

    public UserNotification(int like_ID, int userId, String firstName, String lastName, String petId, String petName, String likedPetId, boolean isAccepted) {
        this.like_ID = like_ID;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.petId = petId;
        this.petName = petName;
        this.likedPetId = likedPetId;
        this.isAccepted = isAccepted;
    }
    public int getLike_ID() {
        return like_ID;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPetId() {
        return petId;
    }

    public String getPetName() {
        return petName;
    }

    public String getLikedPetId() {
        return likedPetId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
