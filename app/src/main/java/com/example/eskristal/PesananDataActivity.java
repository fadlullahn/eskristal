package com.example.eskristal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eskristal.Adapter.SessionManager;
import com.example.eskristal.Api.ApiInterface;
import com.example.eskristal.Api.ApiClient;
import com.example.eskristal.Adapter.AdapterDataPesanan;
import com.example.eskristal.Model.Pesanan.DataModelPesanan;
import com.example.eskristal.Model.Pesanan.ResponseModelPesanan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesananDataActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private RecyclerView rvData;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;
    private List<DataModelPesanan> listDataPesanan = new ArrayList<>();
    private SwipeRefreshLayout srlData;
    private ProgressBar pbData;
    Button btnRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(PesananDataActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_data);

        rvData = findViewById(R.id.rv_data);
        srlData = findViewById(R.id.srl_data);
        pbData = findViewById(R.id.pb_data);
        btnRight = findViewById(R.id.btnRight);

        lmData = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);

        srlData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlData.setRefreshing(true);
                retrieveDataPesanan();
                srlData.setRefreshing(false);
            }
        });

        btnRight.setOnClickListener(v -> {
            sessionManager.logoutSession();
            moveToLogin();
        });
    }

    private void moveToLogin() {
        Intent intent = new Intent(PesananDataActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveDataPesanan();
    }

    public void retrieveDataPesanan(){
        pbData.setVisibility(View.VISIBLE);

        ApiInterface ardDataPesanan = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModelPesanan> tampilData = ardDataPesanan.ardRetrieveDataPesanan();

        tampilData.enqueue(new Callback<ResponseModelPesanan>() {
            @Override
            public void onResponse(Call<ResponseModelPesanan> call, Response<ResponseModelPesanan> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                listDataPesanan = response.body().getDataPesanan();

                adData = new AdapterDataPesanan(PesananDataActivity.this, listDataPesanan);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();

                pbData.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseModelPesanan> call, Throwable t) {
                Toast.makeText(PesananDataActivity.this, "Gagal Menghubungi Server : "+t.getMessage(), Toast.LENGTH_SHORT).show();

                pbData.setVisibility(View.INVISIBLE);
            }
        });
    }
}
