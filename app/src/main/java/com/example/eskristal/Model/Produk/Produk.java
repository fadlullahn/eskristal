package com.example.eskristal.Model.Produk;

import com.google.gson.annotations.SerializedName;

public class Produk {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private String price;
    @SerializedName("date")
    private String date;
    @SerializedName("image")
    private String image;

    public Produk(){}

    public Produk(String id, String name, String price, String date, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
