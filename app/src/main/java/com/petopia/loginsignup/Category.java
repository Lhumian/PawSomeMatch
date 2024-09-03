package com.petopia.loginsignup;

public class Category {
    private int cat_ID;
    private String category;

    public Category(int cat_ID, String category) {
        this.cat_ID = cat_ID;
        this.category = category;
    }

    public int getId() {
        return cat_ID;
    }

    public String getName() {
        return category;
    }

    @Override
    public String toString() {
        return category;
    }
}