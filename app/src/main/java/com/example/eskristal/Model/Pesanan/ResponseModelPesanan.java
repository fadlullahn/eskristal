package com.example.eskristal.Model.Pesanan;

import com.example.eskristal.Model.Pesanan.DataModelPesanan;

import java.util.List;

public class ResponseModelPesanan {
    private int kode;
    private String pesan;
    private List<DataModelPesanan> data;

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public List<DataModelPesanan> getDataPesanan() {
        return data;
    }

    public void setDataPesanan(List<DataModelPesanan> data) {
        this.data = data;
    }
}
