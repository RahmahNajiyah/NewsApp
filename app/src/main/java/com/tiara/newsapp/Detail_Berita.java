package com.tiara.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Detail_Berita extends AppCompatActivity {
    WebView web;
    String link_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__berita);
        WebView web = (WebView ) findViewById(R.id.webBerita);

        link_web = getIntent().getStringExtra("link");
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setLoadsImagesAutomatically(true);
        web.setWebViewClient(new MyWeb());
        web.loadUrl(link_web);


    }

    private class MyWeb extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
