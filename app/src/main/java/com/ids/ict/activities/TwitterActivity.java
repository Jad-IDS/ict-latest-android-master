package com.ids.ict.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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

public class TwitterActivity extends Activity {
    //asynctask to read market state and time from net

    WebView mWebView;
    private ProgressDialog pd;
    int footerButton;
    //  String pageUrl=""+app.link+"GeneralServices.asmx/GetAboutSpeedTest?language=en";

    MyApplication app;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplicationContext();

        Actions.setLocal(this);//}
        if (savedInstanceState != null) {


        }
     //   TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.twitter);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Actions.loadMainBar(this);
        ViewResizing.setPageTextResizing(this);

        WebView webview = (WebView) this.findViewById(R.id.speed_webView);
        webview.setVerticalScrollBarEnabled(true);
        webview.setHorizontalScrollBarEnabled(true);
        //   webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("http://twitter.com/home?status=" + get_text() + " " + get_link());
        // Actions.DeleteCache(this);
        //  Bundle bundle;
        // bundle = this.getIntent().getExtras();


        //   pageUrl=bundle.getString("url");
        //  pd=new ProgressDialog(this);
        //    webpageSettings();
        // footerButton= this.getIntent().getIntExtra("footerButton", R.id.home);
        // Actions.setFooterButtonState(this,footerButton );


    }

    public void backTo(View v) {
        Actions.backTo(this);
    }

    private String get_text() {
        String name = "";
        try {

            URL url = new URL("" + app.link + "GeneralServices.asmx/GetSocialMediaSharingText?password=" + app.pass + "&language=en");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("string");
            Element nameElement = (Element) nodeList.item(0);
            nodeList = nameElement.getChildNodes();
            name = ((Node) nodeList.item(0)).getNodeValue();
            /** Assign textview array lenght by arraylist size */
//	name = new TextView[nodeList.getLength()];
//	website = new TextView[nodeList.getLength()];
            //category = new TextView[nodeList.getLength()];


        } catch (Exception e) {
            System.out.println("gg " + e);
            name = "-1";
        }
        return name;
    }

    private String get_link() {
        String name = "";
        try {

            URL url = new URL("" + app.link + "GeneralServices.asmx/GetSocailMediaLinks?password=" + app.pass + "&language=en");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("SocialMedia");

            /** Assign textview array lenght by arraylist size */
            //	name = new TextView[nodeList.getLength()];
            //	website = new TextView[nodeList.getLength()];
            //category = new TextView[nodeList.getLength()];

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);


                Element fstElmnt = (Element) node;
                NodeList nameList = fstElmnt.getElementsByTagName("Name");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                String id = ((Node) nameList.item(0)).getNodeValue();

                NodeList websiteList = fstElmnt.getElementsByTagName("Link");
                Element websiteElement = (Element) websiteList.item(0);
                websiteList = websiteElement.getChildNodes();
                String title = ((Node) websiteList.item(0)).getNodeValue();

                if (id.equals("Twitter")) {
                    name = title;
                    break;
                }
            }
            /** Assign textview array lenght by arraylist size */
            //	name = new TextView[nodeList.getLength()];
            //	website = new TextView[nodeList.getLength()];
            //category = new TextView[nodeList.getLength()];


        } catch (Exception e) {
            System.out.println("gg " + e);
        }
        return name;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void webpageSettings() {
        mWebView = (WebView) findViewById(R.id.speed_webView);
        mWebView.clearCache(true);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            //using these parameters to check in case of redirection,
            //methods below will be called multiple times,
            boolean loadingFinished = true;
            boolean redirect = false;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;

                view.loadUrl(url);

                return false;

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                loadingFinished = false;
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                //only first time page is loaded
                /*ProgressDialog test = pd;
                if (!pd.isShowing())
                    try {
                        pd = ProgressDialog.show(TwitterActivity.this, "", TwitterActivity.this.getString(R.string.loading), true, true);
                    } catch (Exception e) {

                    }*/

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!redirect) {
                    loadingFinished = true;
                }

                if (loadingFinished && !redirect) {//finished loading all pages
                    //called after loading to force the zoom
                    //needed here in cases or redirection
                    // adView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
                    //HIDE LOADING IT HAS FINISHED

                    //pd.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBarLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    redirect = false;
                }


            }


        });
        //must set this to true otherwise twitter will not load
        //disabled by default for space savings and security.
        //when true it would then allow ANY website that takes advantage of DOM storage
        //to use said storage options on the device. note:this functionality is irrelevant for
        //websites that do not use the HTML 5 specifications,since it is part of that spec.
        mWebView.getSettings().setDomStorageEnabled(true);
        //loads the WebView completely zoomed out
        //called before loading to initialize
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //makes the Webview have a normal viewport (such as a normal desktop browser)
        mWebView.getSettings().setUseWideViewPort(true);
        //  Loading ld=new Loading();
        //  ld.execute();
        //  mWebView.loadUrl(pageUrl);


    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();

        // if(app.logOut && app.appType!=app.MIX){
        //	 this.finish();
        // }
        // else{
        //force localization language,need to call in every activity
        //just in case of orientation change to reforce localization
        //otherwise it will reset to localization of device
        // if(app.rotationActive){
        Actions.setLocal(this);//}

        //start the repeating task,note that getmarket info runs repeateadly in getmarketstatus
        //according to the result returned from status

        // }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//mWebView.clearCache(true);

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
            TCTDbAdapter source = new TCTDbAdapter(TwitterActivity.this);
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
            //	if(error.getState()){
            //	Actions.onCreateDialog(EventsListActivity.this, error.getMessage(),false);
            //	}
            //	else{
            //	EventsListArrayAdapterMenu adapter  = new EventsListArrayAdapterMenu( TwitterActivity.this ,nn);
            //	listMenu.setAdapter(adapter);
            //	}
            //	mProgressBar.setVisibility(View.GONE);
        }

    }
}