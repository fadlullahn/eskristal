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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eskristal.Api.ApiInterface;
import com.example.eskristal.Api.ApiClient;
import com.example.eskristal.Adapter.AdapterDataUser;
import com.example.eskristal.Model.User.DataModel;
import com.example.eskristal.Model.User.ResponseModel;
import com.example.eskristal.Adapter.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDataActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private RecyclerView rvData;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;
    private List<DataModel> listData = new ArrayList<>();
    private SwipeRefreshLayout srlData;
    private ProgressBar pbData;
    private FloatingActionButton fabTambah;
    private String extraData, extraDataPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(UserDataActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        rvData = findViewById(R.id.rv_data);
        srlData = findViewById(R.id.srl_data);
        pbData = findViewById(R.id.pb_data);
        fabTambah = findViewById(R.id.fab_tambah);

        lmData = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);

        Intent intent = getIntent();
        extraData = intent.getStringExtra("extraData");
        extraDataPlus = intent.getStringExtra("extraDataPlus");


        srlData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlData.setRefreshing(true);
                retrieveData();
                srlData.setRefreshing(false);
            }
        });

        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDataActivity.this, UserTambahActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveData();
    }

    public void retrieveData() {
        pbData.setVisibility(View.VISIBLE);

        ApiInterface ardData = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> tampilData = ardData.ardRetrieveData();

        tampilData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                listData = response.body().getData();

                List<DataModel> filteredList = new ArrayList<>();
                for (DataModel item : listData) {
                    if (extraData != null && extraDataPlus != null) {
                        if (item.getLevel().equals(extraData) || item.getLevel().equals(extraDataPlus)) {
                            filteredList.add(item);
                        }
                    } else if (extraData != null) {
                        if (item.getLevel().equals(extraData)) {
                            filteredList.add(item);
                        }
                    } else {
                        if (item.getLevel().equals("user")) {
                            filteredList.add(item);
                        }
                    }
                }

                adData = new AdapterDataUser(UserDataActivity.this, filteredList);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();

                pbData.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UserDataActivity.this, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();

                pbData.setVisibility(View.INVISIBLE);
            }
        });
    }


}
