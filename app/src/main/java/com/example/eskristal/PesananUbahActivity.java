package com.example.eskristal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eskristal.Api.ApiClient;
import com.example.eskristal.Api.ApiInterface;
import com.example.eskristal.Model.Pesanan.ResponseModelPesanan;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesananUbahActivity extends AppCompatActivity {
    private int xId;
    private String xAlamat, xIdProduk, xIdUser, xNohp, xLevel, xPassword, xProses;
    private EditText etAlamat, etIdProduk, etIdUser, etNohp, etLevel, etPassword, etProses;

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

        etAlamat = findViewById(R.id.et_alamat);
        etIdProduk = findViewById(R.id.et_id_produk);
        etIdUser = findViewById(R.id.et_id_user);
        etNohp = findViewById(R.id.et_nohp);
        etLevel = findViewById(R.id.et_level);
        etPassword = findViewById(R.id.et_password);
        etProses = findViewById(R.id.et_proses);

        etAlamat.setText(xAlamat);
        etIdProduk.setText(xIdProduk);
        etIdUser.setText(xIdUser);
        etNohp.setText(xNohp);
        etLevel.setText(xLevel);
        etPassword.setText(xPassword);

        // Set nilai awal etProses
        if (xProses.equals("diproses")) {
            etProses.setText("diantar");
        } else if (xProses.equals("diantar")) {
            etProses.setText("selesai");
        }

        // Panggil updateDataPesanan setelah mengatur nilai etProses
        updateDataPesanan();
    }

    private void updateDataPesanan() {
        ApiInterface ardDataPesanan = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseModelPesanan> ubahDataPesanan = ardDataPesanan.ardUpdateDataPesanan(
                xId,
                etIdProduk.getText().toString(),
                etIdUser.getText().toString(),
                etAlamat.getText().toString(),
                etNohp.getText().toString(),
                etLevel.getText().toString(),
                etPassword.getText().toString(),
                etProses.getText().toString()
        );

        ubahDataPesanan.enqueue(new Callback<ResponseModelPesanan>() {
            @Override
            public void onResponse(Call<ResponseModelPesanan> call, Response<ResponseModelPesanan> response) {
                if (response.isSuccessful()) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(PesananUbahActivity.this, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PesananUbahActivity.this, "Gagal mengubah data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModelPesanan> call, Throwable t) {
                Toast.makeText(PesananUbahActivity.this, "Gagal Menghubungi Server | " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
