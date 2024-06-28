package com.example.eskristal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eskristal.Adapter.SessionManager;

public class MainActivity extends AppCompatActivity {
    Button btnDataUser, btnDataProduk, btnDataPesanan;
    TextView etUsername, etName, etLevel, etId, etPassword;
    SessionManager sessionManager;
    String username, name, level, id, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(MainActivity.this);
        if (!sessionManager.isLoggedIn()) {
            moveToLogin();
        } else {
            level = sessionManager.getUserDetail().get(SessionManager.LEVEL);

            if ("admin".equals(level)) {
            } else if ("karyawan".equals(level)) {
                Intent intent = new Intent(MainActivity.this, PesananDataActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(MainActivity.this, ProdukDataActivity.class);
                startActivity(intent);
                finish();
            }
        }

        btnDataPesanan = findViewById(R.id.btnDataPesanan);
        btnDataProduk = findViewById(R.id.btnDataProduk);
        btnDataUser = findViewById(R.id.btnDataUser);

        etUsername = findViewById(R.id.etMainUsername);
        etName = findViewById(R.id.etMainName);
        etLevel = findViewById(R.id.etMainLevel);
        etId = findViewById(R.id.etMainId);
        etPassword = findViewById(R.id.etMainPassword);

        username = sessionManager.getUserDetail().get(SessionManager.USERNAME);
        name = sessionManager.getUserDetail().get(SessionManager.NAME);
        level = sessionManager.getUserDetail().get(SessionManager.LEVEL);
        id = sessionManager.getUserDetail().get(SessionManager.USER_ID);
        password = sessionManager.getUserDetail().get(SessionManager.PASSWORD);

        etUsername.setText(username);
        etName.setText(name);
        etLevel.setText(level);
        etId.setText(id);
        etPassword.setText(password);

        etUsername.setVisibility(View.GONE);
        etName.setVisibility(View.GONE);
        etLevel.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        etId.setVisibility(View.GONE);

        btnDataUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserDataActivity.class);
            startActivity(intent);
        });

        btnDataProduk.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProdukDataActivity.class);
            startActivity(intent);
        });

        btnDataPesanan.setOnClickListener(v -> {
            Intent intent = new Intent(this, PesananDataActivity.class);
            startActivity(intent);
        });
    }

    private void moveToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
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
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}