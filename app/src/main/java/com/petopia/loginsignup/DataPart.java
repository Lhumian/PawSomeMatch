package com.petopia.loginsignup;

public class DataPart {
    private String fileName;
    private byte[] data;
    private String type;

    public DataPart() {
    }

    public DataPart(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    public DataPart(String fileName, byte[] data, String type) {
        this.fileName = fileName;
        this.data = data;
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
