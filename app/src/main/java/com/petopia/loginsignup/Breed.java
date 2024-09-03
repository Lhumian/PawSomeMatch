package com.petopia.loginsignup;

public class Breed {
    private int breed_ID;
    private String name;
    private int speciesId;

    public Breed(int id, String name, int speciesId) {
        this.breed_ID = id;
        this.name = name;
        this.speciesId = speciesId;
    }

    public int getBreed_ID() {
        return breed_ID;
    }

    public String getName() {
        return name;
    }

    public int getSpeciesId() {
        return speciesId;
    }

    @Override
    public String toString() {
        return name;
    }
}
