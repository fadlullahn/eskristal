package com.example.eskristal.Model.Produk;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetProduk {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<Produk> listDataHeros;
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
    public List<Produk> getListDataHeros() {
        return listDataHeros;
    }
    public void setListDataHeros(List<Produk> listDataHeros) {
        this.listDataHeros = listDataHeros;
    }
}
