package com.example.eskristal.Model.Pesanan;

import com.google.gson.annotations.SerializedName;

public class Pesanan {

    @SerializedName("id")
    private String id;
    @SerializedName("produk")
    private String produk;
    @SerializedName("user")
    private String user;
    @SerializedName("price")
    private String price;
    @SerializedName("date")
    private String date;
    @SerializedName("image")
    private String image;

    @SerializedName("proses")
    private String proses;

    public Pesanan(){}

    public Pesanan(String id, String produk,String user, String price, String date, String image, String proses) {
        this.id = id;
        this.produk = produk;
        this.user = user;
        this.price = price;
        this.date = date;
        this.image = image;
        this.proses = proses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduk() {
        return produk;
    }

    public void setProduk(String produk) {
        this.produk = produk;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getProses() {
        return proses;
    }

    public void setProses(String proses) {
        this.proses = proses;
    }

}
