package com.ids.ict.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.Profile;
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

public class SpeedTestActivity extends Activity {
    //asynctask to read market state and time from net

    WebView mWebView;
    private ProgressDialog pd;
    //MyApplication app;
    int footerButton;
    String num = "";
    String lang = "";
    boolean waitingForLocationUpdate = true;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Double longitude = 0.0, latitude = 0.0;//use these values to send in report
    Location location;
    String provider;
    String qatarid = "";
    //  String pageUrl=""+app.link+"GeneralServices.asmx/GetAboutSpeedTest?language=en";

    MyApplication app;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplicationContext();

        lang = Actions.setLocal(this);//}
        if (savedInstanceState != null) {


        }
       // TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.speedtest);
        Actions.loadMainBar(this);
        ViewResizing.setPageTextResizing(this);
        registerLocationUpdates();
        WebView webview = (WebView) this.findViewById(R.id.speed_webView);
        webview.clearCache(true);

        webview.setVerticalScrollBarEnabled(true);
        webview.setHorizontalScrollBarEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        String useag = getResources().getString(R.string.useragent);
        webview.getSettings().setUserAgentString(useag);
        webview.getSettings().setLoadWithOverviewMode(true);
        //makes the Webview have a normal viewport (such as a normal desktop browser)
        webview.getSettings().setUseWideViewPort(true);
        final ProgressBar loadingProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        TCTDbAdapter sour = new TCTDbAdapter(SpeedTestActivity.this);
        sour.open();
        ArrayList<Profile> arr = sour.getAllProfiles();
        sour.close();
        num = arr.get(0).getnum();
        qatarid = arr.get(0).getqatarID();
        //  webview.getSettings().setDefaultZoom();
        webview.loadUrl(get_consumerlink());
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
        // Actions.DeleteCache(this);
        Bundle bundle;
        bundle = this.getIntent().getExtras();


        //   pageUrl=bundle.getString("url");
        //  pd=new ProgressDialog(this);
        //    webpageSettings();
        footerButton = this.getIntent().getIntExtra("footerButton", R.id.home);


    }

    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }

    public void backTo(View v) {
        Actions.backTo(this);
    }

    private String get_consumerlink() {
        String name = null;
        try {
            URL url;
            if (lang.equals(MyApplication.ENGLISH)) {
                url = new URL("" + app.link + "GeneralServices.asmx/GetSocailMediaLinks?password=" + app.pass + "&language=en");
            } else {
                url = new URL("" + app.link + "GeneralServices.asmx/GetSocailMediaLinks?password=" + app.pass + "&language=ar");

            }
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

                if (id.equals("SpeedTest")) {
                    name = title + "&locationX=" + longitude + "&location=" + latitude + "&mobile=" + num + "&qatariID=" + qatarid + "&mnc=" + getmnc();
                    break;
                }
            }
            /** Assign textview array lenght by arraylist size */
            //	name = new TextView[nodeList.getLength()];
            //	website = new TextView[nodeList.getLength()];
            //category = new TextView[nodeList.getLength()];


        } catch (Exception e) {
            System.out.println("gg " + e);
            String url1 = "" + app.link + "PostData.asmx/VerifyRegistration";
            Connection conn = new Connection(url1);

            try {
                String error_return = conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return name;


        //	return "";
    }

    void registerLocationUpdates() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        provider = locationManager.getBestProvider(criteria, true);

        // Cant get a hold of provider
        if (provider == null) {
            //   Log.v(TAG, "Provider is null");
            // showNoProvider();
            return;
        } else {
            // Log.v(TAG, "Provider: " + provider);
        }

        locationListener = new MyLocationListener();

        //  locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        // connect to the GPS location service
        Location oldLocation = locationManager.getLastKnownLocation(provider);

        if (oldLocation != null) {
            //   Log.v(TAG, "Got Old location");
            // latitude = oldLocation.getLatitude();
            // longitude = oldLocation.getLongitude();
            waitingForLocationUpdate = false;
            //      getNearbyStores();
        } else {
            //  Log.v(TAG, "NO Last Location found");
        }
    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("hello im here", "the value are " + latitude + " " + longitude);//, msg)
            //    Log.v(TAG, "IN ON LOCATION CHANGE");


            if (waitingForLocationUpdate) {
                //       getNearbyStores();
                waitingForLocationUpdate = false;
            }

            locationManager.removeUpdates(this);
        }

        public void onStatusChanged(String s, int i, Bundle bundle) {
            //   Log.v(TAG, "Status changed: " + s);
        }

        public void onProviderEnabled(String s) {
            //   Log.e(TAG, "PROVIDER DISABLED: " + s);
        }

        public void onProviderDisabled(String s) {
            //     Log.e(TAG, "PROVIDER DISABLED: " + s);
        }
    }

    public String getmnc() {
        String mnc = "";
        try {
            TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String networkOperator = tel.getNetworkOperator();

            if (networkOperator != null) {
                //        int mcc = Integer.parseInt(networkOperator.substring(0, 3));
                mnc = networkOperator.substring(3);
            }
        } catch (Exception e) {
            String url1 = "" + app.link + "PostData.asmx/VerifyRegistration";
            Connection conn = new Connection(url1);

            try {
                String error_return = conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return mnc;
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
                ProgressDialog test = pd;
                if (!pd.isShowing())
                    try {
                        pd = ProgressDialog.show(SpeedTestActivity.this, "", SpeedTestActivity.this.getString(R.string.loading), true, true);
                    } catch (Exception e) {

                    }
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

                    pd.dismiss();
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
            TCTDbAdapter source = new TCTDbAdapter(SpeedTestActivity.this);
            source.open();
            AtomicReference<Event[]> ref = new AtomicReference<Event[]>(null);

            String lan;
            String eventsSource = "" + app.link + "GeneralServices.asmx/GetIssueTypes?";
            eventsSource = eventsSource + "language=en&password=" + "+app.pass+" + "&mainIssueTypeId=1";
            Log.d("eventsource", eventsSource);

            //error=Actions.readEvents(ref,eventsSource);
            //nn2  = ref.get();


            AtomicReference<Event[]> ref1 = new AtomicReference<Event[]>(null);
            //String lan;

            eventsSource = "" + app.link + "GeneralServices.asmx/GetIssueTypes?language=en&password=" + "+app.pass+" + "&mainIssueTypeId=2";
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
            //	EventsListArrayAdapterMenu adapter  = new EventsListArrayAdapterMenu( SpeedTestActivity.this ,nn);
            //	listMenu.setAdapter(adapter);
            //	}
            //	mProgressBar.setVisibility(View.GONE);
        }

    }

    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(SpeedTestActivity.this, MoreActivity.class);
                SpeedTestActivity.this.startActivity(intent);
                break;
            }
            case R.id.home: {

                intent.setClass(SpeedTestActivity.this, HomePageActivity.class);
                SpeedTestActivity.this.startActivity(intent);
                break;
            }

        }
    }
}