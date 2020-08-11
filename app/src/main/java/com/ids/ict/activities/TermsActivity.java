package com.ids.ict.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.ViewResizing;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.sql.Connection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TermsActivity extends Activity {

    Connection conn;
    String lang = "", textColor = "425968",detailstxt= "";
    WebView webview;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor edit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = Actions.setLocal(this);
        //  TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.activity_terms);
        final RelativeLayout main = (RelativeLayout) findViewById(R.id.termMainRL);

        mSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        edit = mSharedPreferences.edit();
//		Button agree = (Button) findViewById(R.id.agree_button);
//		Button disagree = (Button) findViewById(R.id.disagree_button);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        ViewResizing.setPageTextResizing(this);
        Typeface tf = null;
        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            footer.setBackgroundResource(R.drawable.footer_nt);
            main.setBackgroundColor(getResources().getColor(R.color.nightBlue));
//			agree.setBackgroundColor(getResources().getColor(R.color.night_gray));
//			disagree.setBackgroundColor(getResources().getColor(R.color.night_gray));
//			agree.setTextColor(getResources().getColor(R.color.white));
//			disagree.setTextColor(getResources().getColor(R.color.white));
            textColor = "000000";
        } else
            textColor = "000000";

        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);
        } else {
        }

        if (lang.equals(MyApplication.ENGLISH)) {
            tf = MyApplication.facePolarisMedium;
        } else {
            tf = MyApplication.faceDinar;
        }

        Intent intent = getIntent();
        int fromMore;
        try {
            fromMore = intent.getExtras().getInt("fromMore");
        } catch (Exception e) {
            fromMore = -1;

        }

        if (fromMore == 1) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setVisibility(View.VISIBLE);

            //final LinearLayout butnlayout = (LinearLayout) findViewById(R.id.buttonLL);
            //butnlayout.setVisibility(View.GONE);

            final ImageView home = (ImageView) findViewById(R.id.home);
            home.setImageResource(R.drawable.home);

            final ImageView morebtn = (ImageView) findViewById(R.id.morebtn);
            morebtn.setImageResource(R.drawable.more_new);

        } else {
            LinearLayout footr = (LinearLayout) findViewById(R.id.footer);
            footr.setVisibility(View.INVISIBLE);
        }

//		agree.setTypeface(tf);
//		disagree.setTypeface(tf);

        webview = (WebView) this.findViewById(R.id.webView1);
        webview.setVerticalScrollBarEnabled(true);
        webview.setBackgroundColor(0x00000000);
        webview.setHorizontalScrollBarEnabled(true);


//		agree.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(TermsActivity.this, RegisterActivity.class);
//				startActivity(intent);
//				TermsActivity.this.finish();
//				TermsActivity.this.finish();
//			}
//		});
//
//		disagree.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(TermsActivity.this, LanguageActivity.class);
//				startActivity(intent);
//				TermsActivity.this.finish();
//			}
//		});
        if (Actions.isNetworkAvailable(TermsActivity.this)) {

            Launching ln = new Launching();
            ln.execute();
        }else{

            if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {

                detailstxt = mSharedPreferences.getString("termsEn", "");
            }else{

                detailstxt = mSharedPreferences.getString("termsAr", "");
            }
            webview.setHorizontalScrollBarEnabled(true);
            webview.setVerticalScrollBarEnabled(true);
            webview.setBackgroundColor(0x00000000);

            webview.getSettings().setJavaScriptEnabled(true);
            if (MyApplication.Lang.equals(MyApplication.ARABIC)) {

                Actions.LoadWebViewwithCustomFont(detailstxt, webview);
            } else {

                Actions.LoadWebViewwithCustomFont(detailstxt, webview);
            }
        }

    }


    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(TermsActivity.this, MoreActivity.class);
                TermsActivity.this.startActivity(intent);
                TermsActivity.this.finish();
                break;
            }
            case R.id.home: {
                intent.setClass(TermsActivity.this, HomePageActivity.class);
                TermsActivity.this.startActivity(intent);
                break;
            }

        }
    }


    protected class Launching extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            //pd = ProgressDialog.show(TermsActivity.this, "", "", true);
        }

        @Override
        protected String doInBackground(Void... a) {
            String result = null;
            try {
                URL url;
                if (lang.equals(MyApplication.ENGLISH)) {
                    url = new URL("" + MyApplication.link + MyApplication.general + "/GetTerms?language=en&password=");
                } else {
                    url = new URL("" + MyApplication.link + MyApplication.general + "/GetTerms?language=ar&password=");
                }
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList websiteList = doc.getElementsByTagName("string");
                Element websiteElement = (Element) websiteList.item(0);
                websiteList = websiteElement.getChildNodes();
                result = ((Node) websiteList.item(0)).getNodeValue();

            } catch (Exception e) {
                System.out.println("XML Pasing Excpetion = " + e);
                String url1 = "" + MyApplication.link + MyApplication.post + "VerifyRegistration";
                com.ids.ict.classes.Connection conn = new com.ids.ict.classes.Connection(url1);

                try {
                    String error_return = conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {

            detailstxt = result;

            if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {

                edit.putString("termsEn", detailstxt).apply();
            }else{

                edit.putString("termsAr", detailstxt).apply();
            }

            webview.getSettings().setJavaScriptEnabled(true);

            Actions.LoadWebViewwithCustomFont(detailstxt, webview);
            try {


                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void topBarBack(View v) {
        TermsActivity.this.finish();
    }

}