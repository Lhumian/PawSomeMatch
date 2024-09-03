package com.petopia.loginsignup;

import org.json.JSONException;
import org.json.JSONObject;

public class UserMessage {
    private int acceptedUserId;
    private String acceptedFirstName;
    private String acceptedLastName;
    private int acceptingUserId;
    private String acceptingFirstName;
    private String acceptingLastName;
    private String acceptingPetName;
    private String acceptingPetID;
    private String acceptedPetName;
    private String acceptedPetID;
    private boolean isAccepted;

    public UserMessage(int acceptedUserId, String acceptedFirstName, String acceptedLastName, int acceptingUserId, String acceptingFirstName, String acceptingLastName, String acceptingPetName, String acceptingPetID, String acceptedPetName, String acceptedPetID, boolean isAccepted) {
        this.acceptedUserId = acceptedUserId;
        this.acceptedFirstName = acceptedFirstName;
        this.acceptedLastName = acceptedLastName;
        this.acceptingUserId = acceptingUserId;
        this.acceptingFirstName = acceptingFirstName;
        this.acceptingLastName = acceptingLastName;
        this.acceptingPetName = acceptingPetName;
        this.acceptingPetID = acceptingPetID;
        this.acceptedPetName = acceptedPetName;
        this.acceptedPetID = acceptedPetID;
        this.isAccepted = isAccepted;
    }

    public int getAcceptedUserId() {
        return acceptedUserId;
    }

    public String getAcceptedFirstName() {
        return acceptedFirstName;
    }

    public String getAcceptedLastName() {
        return acceptedLastName;
    }

    public int getAcceptingUserId() {
        return acceptingUserId;
    }

    public String getAcceptingFirstName() {
        return acceptingFirstName;
    }

    public String getAcceptingLastName() {
        return acceptingLastName;
    }

    public String getAcceptingPetName() {
        return acceptingPetName;
    }

    public String getAcceptingPetID() {
        return acceptingPetID;
    }

    public String getAcceptedPetName() {
        return acceptedPetName;
    }

    public String getAcceptedPetID() {
        return acceptedPetID;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
