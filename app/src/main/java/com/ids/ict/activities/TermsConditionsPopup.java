package com.ids.ict.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;


/**
 * Created by user on 12/12/2016.
 */

public class TermsConditionsPopup extends Activity {


    String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.popup);

        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
        }

        result = getIntent().getExtras().getString("result");

        WebView wv = (WebView)  findViewById(R.id.wv);
        TextView x = (TextView)  findViewById(R.id.x);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        wv.getSettings().setJavaScriptEnabled(true);

        //   wv.loadDataWithBaseURL("", "<html><body style=\"text-align:justify;\">" + result + "</body></html>", "text/html", "UTF-8", "");
    if(!result.contains("null"))
        Actions.LoadWebViewwithCustomFont(result, wv);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

    }
}
