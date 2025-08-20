package com.example.myapplication;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class BookReadingActivity extends AppCompatActivity {
    
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reading);
        
        initViews();
        setupWebView();
    }
    
    private void initViews() {
        webView = findViewById(R.id.webview);
    }
    
    private void setupWebView() {
        if (webView != null) {
            WebSettings webSettings = webView.getSettings();
            // 启用JavaScript和DOM存储
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            // 自适应屏幕
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            
            // 设置WebViewClient确保链接在本应用中打开
            webView.setWebViewClient(new WebViewClient());
            
            // 加载指定的典籍阅读网址
            String url = "https://www.cdlvi.cn/allSearch/searchList?searchType=6&showType=1&pageNo=1";
            webView.loadUrl(url);
        }
    }
    
    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}