package com.example.photoapp;

@SuppressWarnings("WeakerAccess")
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

    @SuppressWarnings("unused")
    public String getIdIm() {
        return idIm;
    }



}
