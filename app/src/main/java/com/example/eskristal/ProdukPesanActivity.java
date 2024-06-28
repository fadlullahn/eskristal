package com.example.eskristal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.eskristal.Adapter.SessionManager;
import com.example.eskristal.Api.ApiClient;
import com.example.eskristal.Api.ApiInterface;
import com.example.eskristal.Model.Pesanan.ResponseModelPesanan;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdukPesanActivity extends AppCompatActivity {

    TextView edtName, edtPrice, edtId;
    ImageView imgHolder;
    String nameP;
    SessionManager sessionManager;
    private EditText etAlamat, etNohp, etLevel, etPassword, etIdProduk, etIdUser,etProses;
    private Button btnPesan;
    private String id_produk, id_user, alamat, nohp, level, password,proses;
    ApiInterface mApiInterface;
    private final int ALERT_DIALOG_CLOSE = Config.ALERT_DIALOG_CLOSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_pesan);

        etIdProduk = findViewById(R.id.et_id_produk);
        etIdUser = findViewById(R.id.et_id_user);
        etAlamat = findViewById(R.id.et_alamat);
        etNohp = findViewById(R.id.et_nohp);
        etLevel = findViewById(R.id.et_level);
        etPassword = findViewById(R.id.et_password);
        etProses = findViewById(R.id.et_proses);
        btnPesan = findViewById(R.id.btn_pesan);

        edtId = findViewById(R.id.edt_id);
        edtName = findViewById(R.id.edt_name);
        edtPrice = findViewById(R.id.edt_price);
        imgHolder = findViewById(R.id.imgHolder);

        edtId.setVisibility(View.GONE);
        etIdProduk.setVisibility(View.GONE);

        sessionManager = new SessionManager(ProdukPesanActivity.this);
        nameP = sessionManager.getUserDetail().get(SessionManager.NAME);
        etIdUser.setText(nameP);

        Intent mIntent = getIntent();
        edtId.setText(mIntent.getStringExtra("Id"));
        edtName.setText(mIntent.getStringExtra("Name"));
        edtPrice.setText(mIntent.getStringExtra("Price"));
        etIdProduk.setText(mIntent.getStringExtra("Name"));

        Glide.with(ProdukPesanActivity.this)
                .load(Config.IMAGES_URL + mIntent.getStringExtra("Image"))
                .apply(new RequestOptions().override(750, 750))
                .into(imgHolder);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        etProses.setText("diproses");
        etProses.setVisibility(View.GONE);

        etIdUser.setEnabled(false);
        etIdUser.setFocusable(false);
        etIdUser.setClickable(false);

        etPassword.setEnabled(false);
        etPassword.setFocusable(false);
        etPassword.setClickable(false);

        etLevel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String levelText = etLevel.getText().toString().trim();
                String priceText = edtPrice.getText().toString().trim();

                if (!levelText.isEmpty() && !priceText.isEmpty()) {
                    try {
                        double price = Double.parseDouble(priceText);
                        int level = Integer.parseInt(levelText);

                        double result = level * price;

                        if (result % 1 == 0) {
                            etPassword.setText(String.valueOf((int) result));
                        } else {
                            etPassword.setText(String.valueOf(result));
                        }
                    } catch (NumberFormatException e) {
                        etLevel.setError("Level harus berupa angka");
                        edtPrice.setError("Price tidak valid");
                    }
                }
            }
        });

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_produk = etIdProduk.getText().toString().trim();
                id_user = etIdUser.getText().toString().trim();
                alamat = etAlamat.getText().toString().trim();
                nohp = etNohp.getText().toString().trim();
                level = etLevel.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                proses = etProses.getText().toString().trim();

                if (id_produk.isEmpty()) {
                    etIdProduk.setError("");
                } else if (id_user.isEmpty()) {
                    etIdUser.setError("");
                } else if (alamat.isEmpty()) {
                    etAlamat.setError("Isi Alamat");
                } else if (nohp.isEmpty()) {
                    etNohp.setError("Isi no.hp");
                } else if (level.isEmpty()) {
                    etLevel.setError("Isi jumlah pesanan");
                } else if (password.isEmpty()) {
                    etPassword.setError("");
                } else if (proses.isEmpty()) {
                    etProses.setError("");
                } else {
                    createDataPesanan();
                }
            }
        });
    }


    private void createDataPesanan() {
        ApiInterface ardDataPesanan = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModelPesanan> simpanData = ardDataPesanan.ardCreateDataPesanan(id_produk, id_user, alamat, nohp, level, password,proses);

        simpanData.enqueue(new Callback<ResponseModelPesanan>() {
            @Override
            public void onResponse(Call<ResponseModelPesanan> call, Response<ResponseModelPesanan> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(ProdukPesanActivity.this, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModelPesanan> call, Throwable t) {
                Toast.makeText(ProdukPesanActivity.this, "Gagal Menghubungi Server | " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showAlertDialog(ALERT_DIALOG_CLOSE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = "Batal Pesan";
            dialogMessage = "Apakah anda ingin membatalkan Pemesanan?";
        } else {
            dialogMessage = "";
            dialogTitle = "";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isDialogClose) {
                            finish();
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}