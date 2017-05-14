package edu.sjsu.yduan.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {
    WebView web;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        web = (WebView) findViewById(R.id.web);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
    }
    @Override
    public void onStart() {
        super.onStart();
        loadWeb();
    }
    private void loadWeb(){
        web.loadUrl(url);
    }
//    @JavascriptInterface
//    public void showToast(String toast) {
////        webv.addJavascriptInterface(new WebAppInterface(this), "android");
////        webv.loadUrl("javascript:alert(android.showToast('Hello world!'))");
//        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
//    }

}
