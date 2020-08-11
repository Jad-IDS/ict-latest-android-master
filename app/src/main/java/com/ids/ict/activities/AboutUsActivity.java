package com.ids.ict.activities;

import android.app.Activity;
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
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


//activity to load a list of news based on previously chosen category
//note that category is set manually
public class AboutUsActivity extends Activity {

    WebView mWebView;
    public static String detailstext = "";


    //  String pageUrl=""+app.link+"GeneralServices.asmx/GetAboutUs?language=en&password=";
    int footerButton;
    MyApplication app;
    TextView details;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;
    ProgressBar mProgressBar;
    String lang = "";

    WebSettings newsDetailSettings;
    private WebView webview;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor edit;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplicationContext();
        lang = Actions.setLocal(this);//}
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.aboutus);
        mSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        edit = mSharedPreferences.edit();
        webview = (WebView) this.findViewById(R.id.aboutus_text);
        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
            footer.setBackgroundResource(R.drawable.footer_nt);
        }
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);
        }
        //	if(detailstext.equalsIgnoreCase(""))
        //	{
        if (Actions.isNetworkAvailable(AboutUsActivity.this)) {
            readConsumerGuide read = new readConsumerGuide();
            read.execute();
        } else {
            detailstext = mSharedPreferences.getString("aboutus", "");
            webview.setHorizontalScrollBarEnabled(true);
            webview.setVerticalScrollBarEnabled(true);
            webview.setBackgroundColor(0x00000000);
//			String text = "<html><head><style type=\"text/css\">"+
//					"body {font-family: \"Arial\"; font-size: 20px;; color:#425968; height: auto; }\n"+
//					"</style></head><body>"
//					+"<p align=\"right\" style=\"color:white;font-weight:bold"
//					+"\">"
//					+detailstext
//					+ "</p> "
//					+ "</body></html>";
//			webview.loadDataWithBaseURL("", text, "text/html", "UTF-8", "");
            Actions.LoadWebViewwithCustomFont(detailstext, webview);
        }
        ViewResizing.setPageTextResizing(this);

    }

    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }

    public void backTo(View v) {
        Actions.backTo(this);
    }

    public class readConsumerGuide extends AsyncTask<Void, Void, String> {
        //ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            //pd=ProgressDialog.show(AboutUsActivity.this, "", AboutUsActivity.this.getResources().getString(R.string.loading), false);
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
            detailstext = v;
            webview.setHorizontalScrollBarEnabled(true);
            webview.setVerticalScrollBarEnabled(true);
            webview.setBackgroundColor(0x00000000);
//			String text = "<html><head><style type=\"text/css\">"+
//					"body {font-family: \"Arial\"; font-size: 20px;; color:#425968; height: auto; }\n"+
//					"</style></head><body>"
//					+"<p align=\"right\" style=\"color:white;font-weight:bold"
//					+"\">"
//					+v
//					+ "</p> "
//					+ "</body></html>";
//			webview.loadDataWithBaseURL("", text, "text/html", "UTF-8", "");
            Actions.LoadWebViewwithCustomFont(v, webview);
            edit.putString("aboutus", detailstext);
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

    public String[] get_values() {

        int k = 1;
        TCTDbAdapter source = new TCTDbAdapter(this);
        source.open();
        //source.de
        ArrayList<Event> arr1 = source.getissue_Type("1");
        ArrayList<Event> arr2 = source.getissue_Type("2");
        source.close();
        String[] values = new String[arr1.size() + arr2.size() + 2];
        if (arr1.size() > 0) {
            Event[] n = arr1.toArray(new Event[arr1.size()]);
            values[0] = "Mobile";

            for (int i = 0; i < n.length; i++) {
                values[i + 1] = n[i].getName();
                k++;
            }
        }

        //	ArrayList<Event> arr2 = source.getissue_Type("2");
        values[k] = "Fixed Line";
        if (arr2.size() > 0) {
            Event[] n1 = arr2.toArray(new Event[arr2.size()]);

            for (int i = 0; i < n1.length; i++) {
                values[k + 1] = n1[i].getName();
                k++;
            }
        }

        return values;
    }

    public Event[] get_values_all() {
        Event[] n = null;
        Event[] n1 = null;
        int k = 0;
        TCTDbAdapter source = new TCTDbAdapter(this);
        source.open();
        ArrayList<Event> arr1 = source.getissue_Type("1");
        ArrayList<Event> arr2 = source.getissue_Type("2");
        Event[] values = new Event[arr1.size() + arr2.size() + 2];
        if (arr1.size() > 0) {
            n = arr1.toArray(new Event[arr1.size()]);
            //values[0] = "Mobile";
        }
        if (arr2.size() > 0) {
            n1 = arr2.toArray(new Event[arr2.size()]);
            //values[0] = "Mobile";
        }

        for (int i = 0; i < n.length; i++) {
            values[i] = n[i];
            k++;
        }
        for (int i = 0; i < n1.length; i++) {
            values[k] = n1[i];
            k++;
        }
        //	ArrayList<Event> arr2 = source.getissue_Type("2");


        return values;
    }

    public void topBarBack(View v) {
        AboutUsActivity.this.finish();
    }

    private String get_terms() {
        String title = null;
        try {
            URL url;
            // com.ids.indyact.ViewResizing.setPageTextResizing(this);
            if (mSharedPreferences.getString(getString(R.string.language_key), "en").equals(MyApplication.ENGLISH)) {
                url = new URL("" + app.link + "GeneralServices.asmx/GetAboutUs?language=en&password=" + app.pass + "");
            } else {
                url = new URL("" + app.link + "GeneralServices.asmx/GetAboutUs?language=ar&password=" + app.pass + "");
            }
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

        }
        return title;

        //	return "";
    }

    protected class LaunchingEvent extends AsyncTask<Void, Void, Integer> {
        com.ids.ict.Error error;
        Event[] nn, nn1, nn2;

        @Override
        protected void onPreExecute() {


            //mProgressBar.setVisibility(View.VISIBLE);
            //	mProgressBar.bringToFront();


        }

        @Override
        protected Integer doInBackground(Void... params) {
            Log.d("launching", "passed here");
            TCTDbAdapter source = new TCTDbAdapter(AboutUsActivity.this);
            source.open();
            AtomicReference<Event[]> ref = new AtomicReference<Event[]>(null);

            String lan;
            String eventsSource = "" + app.link + "GeneralServices.asmx/GetIssueTypes?";
            eventsSource = eventsSource + "language=en&password=&mainIssueTypeId=1";
            Log.d("eventsource", eventsSource);

            //error=Actions.readEvents(ref,eventsSource);
            //nn2  = ref.get();


            AtomicReference<Event[]> ref1 = new AtomicReference<Event[]>(null);
            //String lan;

            eventsSource = "" + app.link + "GeneralServices.asmx/GetIssueTypes?language=en&password=&mainIssueTypeId=2";
            Log.d("eventsource", eventsSource);
            //error=Actions.readEvents(ref,eventsSource);
            //nn1  = ref.get();

            ArrayList<Event> arr = source.getissue_Type("2");
            nn2 = arr.toArray(new Event[arr.size()]);

            ArrayList<Event> arr1 = source.getissue_Type("1");
            nn1 = arr1.toArray(new Event[arr1.size()]);
            source.close();

            nn = new Event[nn1.length + nn2.length + 2];
            int k = 0;
            for (int i = 0; i < nn1.length; i++) {
                nn[i] = nn1[i];
                k++;
            }
            for (int i = 0; i < nn2.length; i++) {
                nn[k] = nn2[i];
                k++;
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
        }

    }

    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(AboutUsActivity.this, MoreActivity.class);
                AboutUsActivity.this.startActivity(intent);
                break;
            }
            case R.id.home: {
                intent.setClass(AboutUsActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                AboutUsActivity.this.startActivity(intent);
                AboutUsActivity.this.finish();
                break;
            }

        }
    }

}