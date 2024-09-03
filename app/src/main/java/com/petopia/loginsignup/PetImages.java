package com.petopia.loginsignup;

public class PetImages {
    private String petId;
    private String certificationImage;
    private String vaccineImage;
    private String profileImage;

    public PetImages() {
        // Default constructor required for calls to DataSnapshot.getValue(PetImages.class)
    }

    public PetImages(String petId, String certificationImage, String vaccineImage, String profileImage) {
        this.petId = petId;
        this.certificationImage = certificationImage;
        this.vaccineImage = vaccineImage;
        this.profileImage = profileImage;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getCertificationImage() {
        return certificationImage;
    }

    public void setCertificationImage(String certificationImage) {
        this.certificationImage = certificationImage;
    }

    public String getVaccineImage() {
        return vaccineImage;
    }

    public void setVaccineImage(String vaccineImage) {
        this.vaccineImage = vaccineImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
