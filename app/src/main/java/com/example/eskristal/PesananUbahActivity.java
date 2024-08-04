package com.example.eskristal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PesananUbahActivity extends AppCompatActivity {
    private int xId;
    private String xTanggal,xAlamat, xIdProduk, xIdUser, xNohp, xLevel, xPassword, xProses;
    private EditText etTanggal,etAlamat, etIdProduk, etIdUser, etNohp, etLevel, etPassword, etProses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_ubah);

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xAlamat = terima.getStringExtra("xAlamat");
        xIdProduk = terima.getStringExtra("xIdProduk");
        xIdUser = terima.getStringExtra("xIdUser");
        xNohp = terima.getStringExtra("xNohp");
        xLevel = terima.getStringExtra("xLevel");
        xPassword = terima.getStringExtra("xPassword");
        xProses = terima.getStringExtra("xProses");
        xTanggal = terima.getStringExtra("xTanggal");

        etAlamat = findViewById(R.id.et_alamat);
        etIdProduk = findViewById(R.id.et_id_produk);
        etIdUser = findViewById(R.id.et_id_user);
        etNohp = findViewById(R.id.et_nohp);
        etLevel = findViewById(R.id.et_level);
        etPassword = findViewById(R.id.et_password);
        etProses = findViewById(R.id.et_proses);
        etTanggal = findViewById(R.id.et_tanggal);

        etAlamat.setText(xAlamat);
        etIdProduk.setText(xIdProduk);
        etIdUser.setText(xIdUser);
        etNohp.setText(xNohp);
        etLevel.setText(xLevel);
        etPassword.setText(xPassword);
        etTanggal.setText(xTanggal);

        // Set nilai awal etProses
        if (xProses.equals("diproses")) {
            etProses.setText("diantar");
        } else if (xProses.equals("diantar")) {
            etProses.setText("selesai");
        }

    }

}
