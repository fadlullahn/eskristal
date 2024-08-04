package com.example.eskristal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.eskristal.Adapter.SessionManager;
import com.example.eskristal.Api.ApiClient;
import com.example.eskristal.Api.ApiInterface;
import com.example.eskristal.Model.Pesanan.PostPutDelPesanan;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesananTambahActivity extends AppCompatActivity {

    EditText edtProduk, edtUser, edtPrice, edtAlamat, edtNohp, edtJumlah, edtProses,tvHarga;
    private RadioGroup radioGroupPembayaran;
    private RadioButton radioCod;
    private RadioButton radioTransfer;
    private Button btnGalery, btnSubmit;
    ImageView imgHolder,imgHolderProduk;
    private TextView tvNorek;

    private String mediaPath;
    private String postPath;

    ApiInterface mApiInterface;
    private static final int REQUEST_PICK_PHOTO = Config.REQUEST_PICK_PHOTO;
    private static final int REQUEST_WRITE_PERMISSION = Config.REQUEST_WRITE_PERMISSION;
    private static final String INSERT_FLAG = Config.INSERT_FLAG;
    SessionManager sessionManager;
    String username;

    // Akses Izin Ambil Gambar dari Storage
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            savePesanan();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_tambah);
        sessionManager = new SessionManager(PesananTambahActivity.this);

        // Identifikasi Komponen Form
        edtProduk = (EditText) findViewById(R.id.edt_produk);
        edtUser = (EditText) findViewById(R.id.edt_user);
        edtPrice = (EditText) findViewById(R.id.edt_price);
        edtAlamat = (EditText) findViewById(R.id.edt_alamat);
        edtNohp = (EditText) findViewById(R.id.edt_nohp);
        edtJumlah = (EditText) findViewById(R.id.edt_jumlah);
        edtProses = (EditText) findViewById(R.id.edt_proses);
        tvNorek = (TextView) findViewById(R.id.tv_norek);

        tvHarga = (EditText) findViewById(R.id.tv_harga);

        imgHolder = (ImageView) findViewById(R.id.imgHolder);
        imgHolderProduk = (ImageView) findViewById(R.id.imgHolderProduk);
        btnGalery = (Button) findViewById(R.id.btn_galery);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        radioGroupPembayaran = findViewById(R.id.radio_group_pembayaran);
        radioCod = findViewById(R.id.radio_cod);
        radioTransfer = findViewById(R.id.radio_transfer);

        // Set initial visibility
        btnGalery.setVisibility(View.GONE);
        tvNorek.setVisibility(View.GONE);

        // Set RadioGroup listener
        radioGroupPembayaran.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_cod) {
                    // COD selected, show only the Save button
                    btnGalery.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.VISIBLE);
                    tvNorek.setVisibility(View.GONE);
                } else if (checkedId == R.id.radio_transfer) {
                    // Transfer selected, show both Upload and Save buttons
                    btnGalery.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.VISIBLE);
                    tvNorek.setVisibility(View.VISIBLE);
                }
            }
        });


        username = sessionManager.getUserDetail().get(SessionManager.USERNAME);

        Intent mIntent = getIntent();
        edtProduk.setText(mIntent.getStringExtra("Name"));
        edtUser.setText(username);
        tvHarga.setText(mIntent.getStringExtra("Price"));

        Glide.with(PesananTambahActivity.this)
                .load(Config.IMAGES_URL + mIntent.getStringExtra("Image"))
                .apply(new RequestOptions().override(550, 550))
                .into(imgHolderProduk);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        btnGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });


        edtJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String jumlahText = edtJumlah.getText().toString().trim();
                String hargaText = tvHarga.getText().toString().trim();

                if (!jumlahText.isEmpty() && !hargaText.isEmpty()) {
                    try {
                        int jumlah = Integer.parseInt(jumlahText);
                        double harga = Double.parseDouble(hargaText);

                        double total = jumlah * harga;

                        // Periksa apakah hasilnya adalah bilangan bulat
                        if (total % 1 == 0) {
                            edtPrice.setText(String.valueOf((int) total));
                        } else {
                            edtPrice.setText(String.valueOf(total));
                        }
                    } catch (NumberFormatException e) {
                        edtJumlah.setError("Jumlah harus berupa angka");
                        tvHarga.setError("Harga tidak valid");
                    }
                }
            }
        });

        edtProses.setVisibility(View.GONE);

        edtProduk.setEnabled(false);
        edtProduk.setFocusable(false);

        edtUser.setEnabled(false);
        edtUser.setFocusable(false);

        tvHarga.setEnabled(false);
        tvHarga.setFocusable(false);

        edtPrice.setEnabled(false);
        edtPrice.setFocusable(false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    // Ambil Image Dari Galeri dan Foto
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPath = cursor.getString(columnIndex);
                    imgHolder.setImageURI(data.getData());
                    cursor.close();

                    postPath = mediaPath;
                }
            }
        }
    }

    // Simpan Gambar
    private void savePesanan() {
        final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        RequestBody produkBody = RequestBody.create(MediaType.parse("text/plain"), edtProduk.getText().toString());
        RequestBody userBody = RequestBody.create(MediaType.parse("text/plain"), edtUser.getText().toString());
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), edtPrice.getText().toString());
        RequestBody dateBody = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody alamatBody = RequestBody.create(MediaType.parse("text/plain"), edtAlamat.getText().toString());
        RequestBody nohpBody = RequestBody.create(MediaType.parse("text/plain"), edtNohp.getText().toString());
        RequestBody jumlahBody = RequestBody.create(MediaType.parse("text/plain"), edtJumlah.getText().toString());
        RequestBody prosesBody = RequestBody.create(MediaType.parse("text/plain"), edtProses.getText().toString());
        RequestBody flagBody = RequestBody.create(MediaType.parse("text/plain"), INSERT_FLAG);

        if (mediaPath == null) {
            postPesananWithoutImage(produkBody, userBody, priceBody, dateBody, alamatBody, nohpBody, jumlahBody, prosesBody, flagBody);
        } else {
            postPesananWithImage(produkBody, userBody, priceBody, dateBody, alamatBody, nohpBody, jumlahBody, prosesBody, flagBody);
        }
    }

    private void postPesananWithImage(RequestBody produkBody, RequestBody userBody, RequestBody priceBody, RequestBody dateBody,
                                      RequestBody alamatBody, RequestBody nohpBody, RequestBody jumlahBody, RequestBody prosesBody,
                                      RequestBody flagBody) {
        File imageFile = new File(mediaPath);
        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("image", imageFile.getName(), reqBody);

        Call<PostPutDelPesanan> postPesananCall = mApiInterface.postPesanan(partImage, produkBody, userBody, priceBody, dateBody, alamatBody, nohpBody, jumlahBody, prosesBody, flagBody);
        postPesananCall.enqueue(new Callback<PostPutDelPesanan>() {
            @Override
            public void onResponse(Call<PostPutDelPesanan> call, Response<PostPutDelPesanan> response) {
                ProdukDataActivity.ma.refresh();
                finish();
            }

            @Override
            public void onFailure(Call<PostPutDelPesanan> call, Throwable t) {
                Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error, image", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void postPesananWithoutImage(RequestBody produkBody, RequestBody userBody, RequestBody priceBody, RequestBody dateBody,
                                         RequestBody alamatBody, RequestBody nohpBody, RequestBody jumlahBody, RequestBody prosesBody,
                                         RequestBody flagBody) {
        Call<PostPutDelPesanan> postPesananCall = mApiInterface.postPesanan(null, produkBody, userBody, priceBody, dateBody, alamatBody, nohpBody, jumlahBody, prosesBody, flagBody);
        postPesananCall.enqueue(new Callback<PostPutDelPesanan>() {
            @Override
            public void onResponse(Call<PostPutDelPesanan> call, Response<PostPutDelPesanan> response) {
                ProdukDataActivity.ma.refresh();
                finish();
            }

            @Override
            public void onFailure(Call<PostPutDelPesanan> call, Throwable t) {
                Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error, no image", Toast.LENGTH_LONG).show();
            }
        });
    }



    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            savePesanan();
        }
    }

//    private void requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
//        } else {
//            // Untuk versi Android di bawah Q, lakukan permintaan izin secara terpisah
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
//            } else {
//                saveImageUpload();
//            }
//        }
//    }

//    private void requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
//        } else {
//            saveImageUpload();
//        }
//    }
}