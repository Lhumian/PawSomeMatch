package com.petopia.loginsignup;

public class Notification {
    private int notif_id;
    private String message;
    private String date;

    public Notification(int notif_id, String message, String date) {
        this.notif_id = notif_id;
        this.message = message;
        this.date = date;
    }

    public  int getNotif_id() {
        return  notif_id;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}
