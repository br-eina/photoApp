package com.example.photoapp;

public class Data {

    // Variables for JSON
    private String value;
    private String idIm;

    public Data(String idIm, String value) {
        this.idIm = idIm;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getIdIm() {
        return idIm;
    }



}
