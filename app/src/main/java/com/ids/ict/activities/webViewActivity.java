package com.ids.ict.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.ViewResizing;

public class webViewActivity extends Activity {

    String lang = "" , url = "";
    WebView webView;
    ProgressBar pgbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = Actions.setLocal(this);
        setContentView(R.layout.activity_web_view);
        Actions.loadMainBar(this);

        webView = findViewById(R.id.Wview);
        pgbar = findViewById(R.id.pgbar);

        Intent intent = getIntent();
        url = intent.getStringExtra("url") + "lang=" + ( MyApplication.Lang.matches(MyApplication.ENGLISH) ? "en" : "ar");
        Log.wtf("webViewActivity","url : " + url);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        pgbar.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                pgbar.setVisibility(View.GONE);
            }
        });
    }


    public void topBarBack(View v) {
        Intent intent = new Intent(webViewActivity.this, LoginNewActivity.class);
        startActivity(intent);
        webViewActivity.this.finish();
    }

}
