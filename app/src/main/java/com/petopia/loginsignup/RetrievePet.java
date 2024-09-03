package com.petopia.loginsignup;

import java.util.List;

public class RetrievePet {
    private int petId;
    private List<String> likedUserNames;

    public RetrievePet(int petId, List<String> likedUserNames) {
        this.petId = petId;
        this.likedUserNames = likedUserNames;
    }

    public int getPetId() {
        return petId;
    }

    public List<String> getLikedUserNames() {
        return likedUserNames;
    }
}
