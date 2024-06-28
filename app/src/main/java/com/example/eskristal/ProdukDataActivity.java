package com.example.eskristal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eskristal.Adapter.AdapterDataProduk;
import com.example.eskristal.Adapter.SessionManager;
import com.example.eskristal.Api.ApiClient;
import com.example.eskristal.Api.ApiInterface;
import com.example.eskristal.Model.Produk.GetProduk;
import com.example.eskristal.Model.Produk.Produk;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdukDataActivity extends AppCompatActivity {

    ApiInterface mApiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static ProdukDataActivity ma;
    private FloatingActionButton fabAdd;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_data);
        sessionManager = new SessionManager(ProdukDataActivity.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_heros);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        ma = this;
        refresh();

        fabAdd = findViewById(R.id.fab_add);

        String level = sessionManager.getUserDetail().get(SessionManager.LEVEL);

        if (level.equals("admin")) {
            fabAdd.setVisibility(View.VISIBLE);
        } else {
            fabAdd.setVisibility(View.GONE);
        }
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProdukDataActivity.this, ProdukTambahActivity.class));
            }
        });
    }

    public void refresh() {
        Call<GetProduk> HerosCall = mApiInterface.getHeros();
        HerosCall.enqueue(new Callback<GetProduk>() {
            @Override
            public void onResponse(Call<GetProduk> call, Response<GetProduk>
                    response) {
                List<Produk> HerosList = response.body().getListDataHeros();
                Log.d("Retrofit Get", "Jumlah data Heros: " +
                        String.valueOf(HerosList.size()));
                mAdapter = new AdapterDataProduk(HerosList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }

    private void moveToLogin() {
        Intent intent = new Intent(ProdukDataActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String level = sessionManager.getUserDetail().get(SessionManager.LEVEL);
        if ("admin".equals(level)) {
            getMenuInflater().inflate(R.menu.menu_admin, menu);
        } else if ("user".equals(level)) {
            getMenuInflater().inflate(R.menu.menu_user, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionLogout) {
            sessionManager.logoutSession();
            moveToLogin();
            return true;
        } else if (item.getItemId() == R.id.actionUserList) {
            Intent intent = new Intent(this, UserDataActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.actionProdukList) {
            Intent intent = new Intent(this, ProdukDataActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.actionPesananList) {
            Intent intent = new Intent(this, PesananDataActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.actionRiwayatPesanan) {
            Intent intent = new Intent(this, PesananDataActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}