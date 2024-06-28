package com.example.eskristal.Model.Produk;

import com.google.gson.annotations.SerializedName;

public class PostPutDelProduk {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    Produk mHeros;
    @SerializedName("message")
    String message;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Produk getHeros() {
        return mHeros;
    }
    public void setHeros(Produk Heros) {
        mHeros = Heros;
    }
}
