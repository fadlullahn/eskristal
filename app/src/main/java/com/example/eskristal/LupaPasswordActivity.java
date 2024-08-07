package com.example.eskristal;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class LupaPasswordActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        // Find the WebView by its ID
        webView = findViewById(R.id.webView);

        // Set WebViewClient to ensure links are opened within WebView
        webView.setWebViewClient(new WebViewClient());

        // Enable JavaScript (optional, if your website requires it)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Load a web page
        webView.loadUrl("https://eskristal.fadlullahnft.com/forgot_password.php");
    }
}
