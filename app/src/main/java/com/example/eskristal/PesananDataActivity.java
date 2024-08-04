package com.example.eskristal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eskristal.Adapter.AdapterDataPesanan;
import com.example.eskristal.Adapter.SessionManager;
import com.example.eskristal.Api.ApiClient;
import com.example.eskristal.Api.ApiInterface;
import com.example.eskristal.Model.Pesanan.GetPesanan;
import com.example.eskristal.Model.Pesanan.Pesanan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesananDataActivity extends AppCompatActivity {

    ApiInterface mApiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static PesananDataActivity ma;
    SessionManager sessionManager;
    Button btnRight, btnLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_data);
        sessionManager = new SessionManager(PesananDataActivity.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_heros);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        ma = this;
        btnRight = findViewById(R.id.btnRight);
        btnLeft = findViewById(R.id.btnLeft);
        refresh();

        btnLeft.setOnClickListener(v -> {
            sessionManager.logoutSession();
            moveToLogin();
        });

        String level = sessionManager.getUserDetail().get(SessionManager.LEVEL);

        if (level.equals("karyawan")) {
            btnRight.setVisibility(View.GONE);
        } else {
            btnRight.setVisibility(View.GONE);
            btnLeft.setVisibility(View.GONE);
        }
    }

    private void moveToLogin() {
        Intent intent = new Intent(PesananDataActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    public void refresh() {
        Call<GetPesanan> pesananCall = mApiInterface.getPesanan();
        pesananCall.enqueue(new Callback<GetPesanan>() {
            @Override
            public void onResponse(Call<GetPesanan> call, Response<GetPesanan> response) {
                List<Pesanan> allPesananList = response.body().getListDataPesanan();
                Log.d("Retrofit Get", "Jumlah data Pesanan: " + allPesananList.size());

                // Ambil level pengguna dari SessionManager
                SessionManager sessionManager = new SessionManager(PesananDataActivity.this);
                String level = sessionManager.getUserDetail().get(SessionManager.LEVEL);

                List<Pesanan> filteredPesananList;

                if ("user".equals(level)) {
                    // Filter data berdasarkan username jika level pengguna adalah 'user'
                    String username = sessionManager.getUserDetail().get(SessionManager.USERNAME);
                    filteredPesananList = new ArrayList<>();
                    for (Pesanan pesanan : allPesananList) {
                        if (username.equals(pesanan.getUser())) { // Ganti getUser() dengan metode yang sesuai jika perlu
                            filteredPesananList.add(pesanan);
                        }
                    }
                } else {
                    // Tampilkan semua data jika level pengguna adalah 'admin' atau 'karyawan'
                    filteredPesananList = allPesananList;
                }

                // Set adapter dengan data yang telah difilter
                mAdapter = new AdapterDataPesanan(filteredPesananList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetPesanan> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }


}
