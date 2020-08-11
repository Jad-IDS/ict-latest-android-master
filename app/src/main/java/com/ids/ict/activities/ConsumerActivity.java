package com.ids.ict.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.ViewResizing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ConsumerActivity extends Activity {

    WebView mWebView;
    //String pageUrl=""+app.link+"GeneralServices.asmx/GetConsumerGuide?language=en&password=";
    int footerButton;
    MyApplication app;
    TextView details;

    ProgressBar mProgressBar;

    View mainBar, footer;
    String lang = "";
    WebSettings newsDetailSettings;
    private WebView webview;
    public static String textdetails = "";
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor edit;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplicationContext();
        lang = Actions.setLocal(this);//}
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.consumerguide);
        lang = Actions.setLocal(this);
        ViewResizing.setPageTextResizing(this);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        edit = mSharedPreferences.edit();
        RelativeLayout layoutToMove = (RelativeLayout) findViewById(R.id.layoutToMove);
        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
            footer.setBackgroundResource(R.drawable.footer_nt);
            layoutToMove.setBackgroundColor(getResources().getColor(R.color.nightBlue));
        }

        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);
        }
        webview = (WebView) this.findViewById(R.id.consumer_text);
        if (Actions.isNetworkAvailable(ConsumerActivity.this)) {

            Log.wtf("network","available");

            readConsumerGuide read = new readConsumerGuide();
            read.execute();
        } else {

            Log.wtf("network","not available");
            Log.wtf("MyApplication.Lang","is "+MyApplication.Lang);
            if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {

                textdetails = mSharedPreferences.getString("consumerguideEn", "");
            }else{

                textdetails = mSharedPreferences.getString("consumerguideAr", "");
            }

            webview.setHorizontalScrollBarEnabled(true);
            webview.setVerticalScrollBarEnabled(true);
            webview.setBackgroundColor(0x00000000);
            webview.getSettings().setJavaScriptEnabled(true);
            Actions.LoadWebViewwithCustomFont(textdetails, webview);
        }

    }

    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }

    public void backTo(View v) {
        Actions.backTo(this);
    }

    public class readConsumerGuide extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            //pd = ProgressDialog.show(ConsumerActivity.this, "", ConsumerActivity.this.getResources().getString(R.string.loading), false);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            final String a = get_terms();
            return a;
        }

        @Override
        protected void onPostExecute(String v) {

            Log.wtf("onPostExecute","is "+ v);

            textdetails = v;
            webview.setHorizontalScrollBarEnabled(true);
            webview.setVerticalScrollBarEnabled(true);
            webview.setBackgroundColor(0x00000000);
            webview.getSettings().setJavaScriptEnabled(true);
            //	webview.loadDataWithBaseURL("", text, "text/html", "UTF-8", "");
            Actions.LoadWebViewwithCustomFont(v, webview);

            if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {

                edit.putString("consumerguideEn", textdetails).apply();
            }else{

                edit.putString("consumerguideAr", textdetails).apply();
            }

            //edit.putString("consumerguide", v);
            edit.commit();
            try {
                //pd.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {

            }
        }

    }

    private String get_terms() {
        String title = null;
        try {


            URL url;
            // com.ids.indyact.ViewResizing.setPageTextResizing(this);


            Log.wtf("link","is "+ app.link);
            Log.wtf("pass","is "+ app.pass);

            if (MyApplication.Lang.equals(MyApplication.ENGLISH))
                url = new URL("" + app.link + "GeneralServices.asmx/GetConsumerGuide?language=en&password=" + app.pass + "");
            else
                url = new URL("" + app.link + "GeneralServices.asmx/GetConsumerGuide?language=ar&password=" + app.pass + "");
            //url = new URL(""+app.link+"GeneralServices.asmx/GetConsumerGuide?language="+ MyApplication.Lang + "&password="+app.pass+"");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList websiteList = doc.getElementsByTagName("string");
            Element websiteElement = (Element) websiteList.item(0);
            websiteList = websiteElement.getChildNodes();
            title = ((Node) websiteList.item(0)).getNodeValue();
            /** Assign textview array lenght by arraylist size */
            //	name = new TextView[nodeList.getLength()];
            //	website = new TextView[nodeList.getLength()];
            //category = new TextView[nodeList.getLength()];


        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
            String url1 = "" + app.link + "PostData.asmx/VerifyRegistration";
            Connection conn = new Connection(url1);

            try {
                String error_return = conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return title;

        //	return "";
    }


    public void topBarBack(View v) {
        ConsumerActivity.this.finish();
    }

    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(ConsumerActivity.this, MoreActivity.class);
                ConsumerActivity.this.startActivity(intent);
                break;
            }
            case R.id.home: {
                intent.setClass(ConsumerActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ConsumerActivity.this.startActivity(intent);
                ConsumerActivity.this.finish();
                break;
            }

        }
    }
}