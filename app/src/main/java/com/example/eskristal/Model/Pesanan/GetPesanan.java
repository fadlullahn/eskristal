package com.example.eskristal.Model.Pesanan;

import com.example.eskristal.Model.Pesanan.Pesanan;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetPesanan {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<Pesanan> listDataPesanan;
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
    public List<Pesanan> getListDataPesanan() {
        return listDataPesanan;
    }
    public void setListDataPesanan(List<Pesanan> listDataPesanan) {
        this.listDataPesanan = listDataPesanan;
    }
}
