package com.petopia.loginsignup;

public class SellerPayment {
    private int imageID;
    private int petID;
    private String referenceNumber;
    private String imageName;

    public SellerPayment(int imageID, int petID, String referenceNumber, String imageName) {
        this.imageID = imageID;
        this.petID = petID;
        this.referenceNumber = referenceNumber;
        this.imageName = imageName;
    }

    public int getImageID() {
        return imageID;
    }

    public int getPetID() {
        return petID;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public String getImageName() {
        return imageName;
    }
}
