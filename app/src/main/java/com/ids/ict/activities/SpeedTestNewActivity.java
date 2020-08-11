/*******************************************************************************
 * Copyright (c) 2012 Evelina Vrabie
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package com.ids.ict.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.GaugeView;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.Profile;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.MobileConfiguration;
import com.ids.ict.classes.NetworkUtil;
import com.ids.ict.parser.AboutSpeedTestXMLPullParserHandler;
import com.ids.ict.parser.GetMobileConfiguration;
import com.ids.ict.parser.SpeedTestResultParser;
import com.speedchecker.android.sdk.Public.SpeedTestListener;
import com.speedchecker.android.sdk.Public.SpeedTestResult;
import com.speedchecker.android.sdk.SpeedcheckerSDK;


import java.util.ArrayList;
import java.util.Locale;

public class SpeedTestNewActivity extends Activity implements SpeedTestListener {



    private int clientID = 54;
    private String result = "", downloadResult = "", uploadResult = "", aboutSpeedTest = "";
    private LinearLayout llCover;
    private Boolean allow = true;
    int footerButton;
    String num = "";
    String lang = "";
    ArrayList<MobileConfiguration> mobile;
    boolean waitingForLocationUpdate = true, closed = false;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Double longitude = 0.0, latitude = 0.0;// use these values to send in report
    Location location;
    String provider;
    String qatarid = "";
    String maxDownloadSize;
    String maxUploadSize = "256";
    String downloadStartSize;
    String uploadStartSize;
    String stepNumber;
    String stDownloadtimeThreshold;
    String uploadTimeThreshold;
    Button aboutustext;
    Typeface tf;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor edit;
    // String
    // pageUrl=""+app.link+"GeneralServices.asmx/GetAboutSpeedTest?language=en";
    ArrayList<MobileConfiguration> array = new ArrayList<MobileConfiguration>();
    MyApplication app;
    // speed test
    private Button startBtn, sendtbtn;
    Button restarttbtn;
    private TextView downloadTV, uploadTV;
    private GaugeView mGaugeView1;
    private RelativeLayout rlMain, rlMeter, rlDownload, rlUpload;
    LinearLayout stTopLinear;
    final double NANOS_PER_SECOND = 1000000000.0;
    final double BYTES_PER_MIB = 1024 * 1024;
    public String zoomingthreshold;
    public static long testStart;
    private int animDuration;
    FrameLayout layout_MainMenu;
    private LinearLayout stValuesLinear;
    TextView txt_download, txt_upload;
    Button repeat_test;
    Button share;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    String defMobile="";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Actions.loadMainBar(this);
        lang = Actions.setLocal(this);
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.speedtest_new_activity1);

        app = (MyApplication) getApplicationContext();

        verifyLocationPermissions(this);


        SpeedcheckerSDK.SpeedTest.setOnSpeedTestListener(SpeedTestNewActivity.this);
        SpeedcheckerSDK.SpeedTest.askPermissions(this);


        mSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        edit = mSharedPreferences.edit();
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        llCover = (LinearLayout) findViewById(R.id.llCover);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mGaugeView1 = (GaugeView) findViewById(R.id.gauge_view1);
        mGaugeView1.setTargetValue(0);
        downloadTV = (TextView) findViewById(R.id.txt_download);
        uploadTV = (TextView) findViewById(R.id.txt_upload);
        startBtn = (Button) findViewById(R.id.startBtn);
        sendtbtn = (Button) findViewById(R.id.sndtbtn);
        restarttbtn = (Button) findViewById(R.id.retstbtn);
        rlMain = (RelativeLayout) findViewById(R.id.rLayout1);
        rlDownload = (RelativeLayout) findViewById(R.id.rlDownload);
        rlUpload = (RelativeLayout) findViewById(R.id.rlUpload);
        rlMeter = (RelativeLayout) findViewById(R.id.rLayout2);
        stTopLinear = (LinearLayout) findViewById(R.id.topss);
        stValuesLinear = (LinearLayout) findViewById(R.id.valuess);
        RelativeLayout main = (RelativeLayout) findViewById(R.id.layoutToMove);
        TextView TV1 = (TextView) findViewById(R.id.textView1);
        repeat_test = (Button) findViewById(R.id.repeat_test);
        share = (Button) findViewById(R.id.share);
        TextView stDetails = (TextView) findViewById(R.id.about_details);
        TextView lb_upload = (TextView) findViewById(R.id.lb_upload);
        txt_upload = (TextView) findViewById(R.id.txt_upload);
        TextView lb_download = (TextView) findViewById(R.id.lb_download);
        txt_download = (TextView) findViewById(R.id.txt_download);

        aboutustext = (Button) findViewById(R.id.aboutusbutton);
        layout_MainMenu = (FrameLayout) findViewById(R.id.mainmenu);
        layout_MainMenu.getForeground().setAlpha(0);
        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
            footer.setBackgroundResource(R.drawable.footer_nt);
            main.setBackgroundColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));
            TV1.setTextColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.white));
            TV1.setBackgroundColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));


            aboutustext.setBackground(ContextCompat.getDrawable(SpeedTestNewActivity.this, R.drawable.button_night));

            llCover.setBackgroundColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));
            //aboutustext.setColorFilter(R.color.nightBlue);//, PorterDuff.Mode.MULTIPLY);
            //   aboutustext.setBackgroundColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));
            //  share.setTextColor(ContextCompat.getColor(SpeedTestNewActivity.this,R.color.nightBlue));
            // repeat_test.setTextColor(ContextCompat.getColor(SpeedTestNewActivity.this,R.color.nightBlue));
            stDetails.setTextColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.black));

            startBtn.setBackgroundColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));


            stTopLinear.setBackgroundColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));
            restarttbtn.setBackground(ContextCompat.getDrawable(SpeedTestNewActivity.this, R.drawable.button_night));
            //restarttbtn.setColorFilter(R.color.nightBlue);//, PorterDuff.Mode.MULTIPLY);
            //  stTopLinear.setBackgroundColor(getResources().getColor(
            //   R.color.night_gray));
            sendtbtn.setBackgroundColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));
            stValuesLinear.setBackgroundColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));
            // lb_upload.setTextColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));
            //lb_upload.setBackgroundColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));
            lb_upload.setBackgroundColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));
            rlDownload.setBackgroundColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));
            rlUpload.setBackgroundColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightBlue));

            /*txt_download.setTextColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.white));
            txt_upload.setTextColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.white));*/


            txt_download.setTextColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightYellow));
            txt_upload.setTextColor(ContextCompat.getColor(SpeedTestNewActivity.this, R.color.nightYellow));
        }

        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);
        } else {
        }

        // mGaugeView1.setTargetValue(0);
        // restarttbtn.setEnabled(false);
        // animDuration = getResources().getInteger(
        // android.R.integer.config_longAnimTime);
        animDuration = 800;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*super.onCreate(savedInstanceState);
        app = (MyApplication) getApplicationContext();

        lang = Actions.setLocal(this);*/

        if (savedInstanceState != null) {

        }

        ViewResizing.setPageTextResizing(this);

        txt_download.setTypeface(MyApplication.facePolarisMedium);
        txt_upload.setTypeface(MyApplication.facePolarisMedium);

        new Thread() {
            public void run() {
                SpeedTestNewActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        registerLocationUpdates();
                    }
                });
            }
        }.start();


        try {
            TCTDbAdapter sour = new TCTDbAdapter(SpeedTestNewActivity.this);
            sour.open();
            ArrayList<Profile> arr = sour.getAllProfiles();
            sour.close();
            num = arr.get(0).getnum();
            qatarid = arr.get(0).getqatarID();
            defMobile = arr.get(0).getnum();
        } catch (Exception e) {
            e.printStackTrace();
            num = qatarid = defMobile = "";
        }

        Bundle bundle;
        bundle = this.getIntent().getExtras();


        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;
        } else {

            tf = MyApplication.faceDinar;
        }

        if (Actions.isNetworkAvailable(SpeedTestNewActivity.this)) {
            getMobileConfigurationAsynck getMobile = new getMobileConfigurationAsynck();
            getMobile.execute();
        } else {
            //Actions.onCreateDialog1(SpeedTestNewActivity.this,getString(R.string.no));
        }
    }


    class IsBlocked extends AsyncTask<String, String, String> {

        String name = "";
        String res = "";

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub

            try {
                Connection conn = new Connection(app.link + "GeneralServices.asmx/IsNumberBlocked?number=" + param[0] + "&password=" + MyApplication.pass);

                if (!conn.hasError()) {

                    SpeedTestResultParser parser = new SpeedTestResultParser();

                    res = parser.parse(conn.getInputStream2());

                    return res + "";
                }

            } catch (Exception e) {
                System.out.println("gg " + e);
                name = "-1";
            }
            return name;
        }

        @Override
        protected void onPostExecute(String value) {
            super.onPostExecute(value);

            result = value;
            Log.wtf("result", result);


            if (result.contains("1")) { // blocked

                restarttbtn.setVisibility(View.VISIBLE);
                if (MyApplication.Lang.equals(MyApplication.ARABIC))
                    Actions.onCreateBlockedDialog(SpeedTestNewActivity.this, getLookup("94").namear);
                else
                    Actions.onCreateBlockedDialog(SpeedTestNewActivity.this, getLookup("94").nameen);

            } else {//not blocked
                downloadTV.setText("-");
                uploadTV.setText("-");
                //restarttbtn.setVisibility(View.INVISIBLE);
                //crossfade();
                testStart = System.currentTimeMillis();
                Log.wtf("testEnd", testStart+"");

                if (allow) {
                    UpdateGaugeView update = new UpdateGaugeView();
                    update.execute();
                }//blocked
            }


        }
    }

    public void onRequestPermissionsResult(int requestCode, String
            permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                //If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

                    allow = true; // permission was granted, yay! Do the // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the // functionality that depends onthis permission.
                    //
                }
                return;
            }

            // other 'case' lines to check for other // permissions this app mightrequest
        }
    }

    public static void verifyLocationPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }

    public void backTo(View v) {
        Actions.backTo(this);
    }

    public void start(View v) {
        Log.wtf("*********BALASHNAAA", System.currentTimeMillis()+"");




        MyApplication.connectionType = NetworkUtil.getConnectivityStatusString(this);

        if (Actions.isNetworkAvailable(SpeedTestNewActivity.this)) {

            Log.wtf("testStart", System.currentTimeMillis()+"");
            restarttbtn.setVisibility(View.INVISIBLE);
            new IsBlocked().execute(defMobile);

        } else {
            if (mSharedPreferences.getString(getResources().getString(R.string.is_blocked), "").equals("")) {
                /*edit.putString(getResources().getString(R.string.is_blocked), "true").apply();
                //alert blocked
                if (MyApplication.Lang.equals(MyApplication.ARABIC))
                    Actions.onCreateBlockedDialog(SpeedTestNewActivity.this, getLookup("94").namear);
                else
                    Actions.onCreateBlockedDialog(SpeedTestNewActivity.this, getLookup("94").nameen);*/



                String error;
                if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH))
                    error = getLookup("90").nameen;
                else
                    error = getLookup("90").namear;
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            } else {
                if (mSharedPreferences.getString(getResources().getString(R.string.is_blocked), "").equals("true")) {
                    //alert blocked;
                    if (MyApplication.Lang.equals(MyApplication.ARABIC))
                        Actions.onCreateBlockedDialog(SpeedTestNewActivity.this, getLookup("94").namear);
                    else
                        Actions.onCreateBlockedDialog(SpeedTestNewActivity.this, getLookup("94").nameen);
                } else {

                    //send offline;
                    String error;
                    if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH))
                        error = getLookup("90").nameen;
                    else
                        error = getLookup("90").namear;
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public LookUp getLookup(String id) {
        SharedPreferences mshaPreferences = getSharedPreferences("PrefEng",
                Context.MODE_PRIVATE);
        LookUp look = new LookUp();
        Gson gson = new Gson();
        String json = mshaPreferences.getString(id, "");
        look = gson.fromJson(json, LookUp.class);

        return look;
    }

    public void restart(View v) {
        downloadTV.setText("-");
        uploadTV.setText("-");
        restarttbtn.setVisibility(View.GONE);
        testStart = System.currentTimeMillis();
        if (allow) {
            UpdateGaugeView update = new UpdateGaugeView();
            update.execute();
        }
    }

    public class UpdateGaugeView extends AsyncTask<Void, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //downloadTV.setText("Download: ");
            //uploadTV.setText("Upload: ");
            // tvPingResults.setText("Ping: ");
            Log.wtf("UpdateGaugeView", System.currentTimeMillis()+"");
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";

            SpeedcheckerSDK.SpeedTest.startTest(SpeedTestNewActivity.this);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            startBtn.setEnabled(false);
            restarttbtn.setEnabled(false);

        }
    }

    private void crossfade() {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        rlMeter.setAlpha(0f);
        //   rlMeter.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        rlMeter.animate().alpha(1f).setDuration(animDuration).setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        rlMain.animate().alpha(0f).setDuration(animDuration)
                .setListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        rlMain.setVisibility(View.GONE);
                    }
                });
    }

    public void showpopupaboutus(View v) {

        if (Actions.isNetworkAvailable(SpeedTestNewActivity.this)) {
            Launching getSpeedTestDetails = new Launching();
            getSpeedTestDetails.execute();
        } else {


            TextView details = (TextView) SpeedTestNewActivity.this
                    .findViewById(R.id.about_details);
            if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {

                aboutSpeedTest = mSharedPreferences.getString("aboutSpeedTestEn", "");

            } else {

                aboutSpeedTest = mSharedPreferences.getString("aboutSpeedTestAr", "");
                Log.wtf("Offline", aboutSpeedTest);
            }

            details.setText(Html.fromHtml(aboutSpeedTest));
            ViewResizing.textResize(SpeedTestNewActivity.this, details, (int) details.getTextSize());
            showPopup(aboutSpeedTest);
        }

    }

    public void send(View v) {
        // redirect to speed test form here
        Bundle bundle = new Bundle();
        bundle.putString("upload", uploadTV.getText().toString());
        bundle.putString("download", downloadTV.getText().toString());
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("latitude", latitude);
        Intent intent = new Intent(SpeedTestNewActivity.this,
                SpeedTestFormActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class getMobileConfigurationAsynck extends
            AsyncTask<Void, Void, ArrayList<MobileConfiguration>> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            //pd = ProgressDialog.show(SpeedTestNewActivity.this, "", SpeedTestNewActivity.this.getResources().getString(R.string.loading), false);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MobileConfiguration> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            //Connection.disableSSLCertificateChecking();
            String url = MyApplication.link + MyApplication.general
                    + "GetMobileConfiguration?key=";

            Connection conn = new Connection(url);

            if (!conn.hasError()) {
                GetMobileConfiguration parser = new GetMobileConfiguration();

                mobile = parser.parse(conn.getInputStream());
            }
            return mobile;
        }

        @Override
        protected void onPostExecute(ArrayList<MobileConfiguration> v) {

            for (int i = 0; i < v.size(); i++) {
                if (v.get(i).getKey().equalsIgnoreCase("STMaxDownloadSize"))
                    maxDownloadSize = v.get(i).getValue();
                else if (v.get(i).getKey()
                        .equalsIgnoreCase("ZoomingThresholdValue"))
                    zoomingthreshold = v.get(i).getValue();
                else if (v.get(i).getKey().equalsIgnoreCase("STMaxUploadSize"))
                    maxUploadSize = v.get(i).getValue();
                else if (v.get(i).getKey()
                        .equalsIgnoreCase("STDownloadStartSize"))
                    downloadStartSize = v.get(i).getValue();
                else if (v.get(i).getKey()
                        .equalsIgnoreCase("STUploadStartSize"))
                    uploadStartSize = v.get(i).getValue();
                else if (v.get(i).getKey().equalsIgnoreCase("STStepsNumber"))
                    stepNumber = v.get(i).getValue();
                else if (v.get(i).getKey()
                        .equalsIgnoreCase("STDownloadTimeThreshold"))
                    stDownloadtimeThreshold = v.get(i).getValue();
                else if (v.get(i).getKey()
                        .equalsIgnoreCase("STUploadTimeThreshold"))
                    uploadTimeThreshold = v.get(i).getValue();
            }
            try {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                //pd.dismiss();
            } catch (Exception e) {

            }
        }

    }

    void registerLocationUpdates() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);

        provider = locationManager.getBestProvider(criteria, true);

        // Cant get a hold of provider
        if (provider == null) {
            // Log.v(TAG, "Provider is null");
            // showNoProvider();
            return;
        } else {
            // Log.v(TAG, "Provider: " + provider);
        }

        locationListener = new MyLocationListener();

        // locationManager.requestLocationUpdates(provider, 0, 0,
        // locationListener);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        // connect to the GPS location service
        Location oldLocation = locationManager.getLastKnownLocation(provider);

        if (oldLocation != null) {
            // Log.v(TAG, "Got Old location");
            // latitude = oldLocation.getLatitude();
            // longitude = oldLocation.getLongitude();
            waitingForLocationUpdate = false;
            // getNearbyStores();
        } else {
            // Log.v(TAG, "NO Last Location found");
        }
    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("hello im here", "the value are " + latitude + " "
                    + longitude);// , msg)
            // Log.v(TAG, "IN ON LOCATION CHANGE");

            if (waitingForLocationUpdate) {
                // getNearbyStores();
                waitingForLocationUpdate = false;
            }

            if (ActivityCompat.checkSelfPermission(SpeedTestNewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SpeedTestNewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(this);
        }

        public void onStatusChanged(String s, int i, Bundle bundle) {
            // Log.v(TAG, "Status changed: " + s);
        }

        public void onProviderEnabled(String s) {
            // Log.e(TAG, "PROVIDER DISABLED: " + s);
        }

        public void onProviderDisabled(String s) {
            // Log.e(TAG, "PROVIDER DISABLED: " + s);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            Log.wtf("English: ", ""+MyApplication.Lang);;
        } else {

            Log.wtf("Arabic:", ""+MyApplication.Lang);
        }


        //Actions.setLocal(this);
        txt_download.setText("-");
        txt_upload.setText("-");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.wtf("CALLED", "CALLED");
        closed = true;
    }

    protected class Launching extends AsyncTask<Void, Void, String> {
        String lang = "";

        @Override
        protected void onPreExecute() {
            if (MyApplication.Lang.equals(MyApplication.ARABIC))
                lang = "ar";
            else
                lang = "en";
        }

        @Override
        protected String doInBackground(Void... a) {
            //Connection.disableSSLCertificateChecking();
            String url = MyApplication.link + MyApplication.general
                    + "GetAboutSpeedTest?Language=" + lang;
            String result = "";
            Connection conn = new Connection(url);

            if (!conn.hasError()) {
                AboutSpeedTestXMLPullParserHandler parser = new AboutSpeedTestXMLPullParserHandler();

                result = parser.parse(conn.getInputStream());
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {

            aboutSpeedTest = result;

            if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {

                edit.putString("aboutSpeedTestEn", aboutSpeedTest).apply();
            } else {

                edit.putString("aboutSpeedTestAr", aboutSpeedTest).apply();
            }

            Log.wtf("Online", aboutSpeedTest);
            TextView details = (TextView) SpeedTestNewActivity.this
                    .findViewById(R.id.about_details);
            details.setText(Html.fromHtml(aboutSpeedTest));

            ViewResizing.textResize(SpeedTestNewActivity.this, details, (int) details.getTextSize());
            showPopup(result);
        }
    }

    private void showPopup(String result) {
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//
//
//        WebView wv = new WebView(this);
//        wv.getSettings().setJavaScriptEnabled(true);
//        wv.loadDataWithBaseURL("", "<html><body style=\"text-align:justify;\">" + result + "</body></html>", "text/html", "UTF-8", "");
//        wv.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//
//                return true;
//            }
//        });
//
//        alert.setView(wv);
//
//        alert.show();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.about_us_popup, null);
        dialogView.setBackgroundColor(Color.WHITE);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();

        WebView wv = (WebView) dialogView.findViewById(R.id.wv);
        TextView x = (TextView) dialogView.findViewById(R.id.x);
        TextView abouttitle = (TextView) dialogView.findViewById(R.id.abouttitle);

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        wv.getSettings().setJavaScriptEnabled(true);
        String fontName = "";
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            fontName = "file:///android_asset/fonts/Galaxie_Polaris_Medium.otf";
            abouttitle.setTypeface(MyApplication.facePolarisMedium);
        } else {
            fontName = "file:///android_asset/fonts/GE_Dinar_One_Medium.otf";
            abouttitle.setTypeface(MyApplication.faceDinar);

        }
//        wv.loadDataWithBaseURL("", "<html>"+"" +
//                        "<head> \n" +
//                "<style type=\"text/css\" > \n" +
//                "body {text-align:center; font-family: MyFont;src: url(" + fontName + "); font-size: %d; height: auto; }\n"+
//                "</style> \n" +
//                "</head> \n" +
//                "<body >" + result + "</body></html>", "text/html", "UTF-8", "");
//        wv.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//
//                return true;
//            }
//        });
        Actions.LoadWebViewwithCustomFont(result,wv);
//        alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        //      alertDialog.getWindow().setDimAmount(0.0f);
        alertDialog.setView(dialogView, 0, 0, 0, 0);
        alertDialog.show();

    }

    public void topBarBack(View v) {
        SpeedTestNewActivity.this.finish();
    }

    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(SpeedTestNewActivity.this, MoreActivity.class);
                SpeedTestNewActivity.this.startActivity(intent);
                break;
            }
            case R.id.home: {
                intent.setClass(SpeedTestNewActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                SpeedTestNewActivity.this.startActivity(intent);
                SpeedTestNewActivity.this.finish();
                break;
            }

        }
    }



    //<editor-fold desc="new speed test">
    @Override
    public void onTestStarted() {

        Log.wtf("Test", "started");
        mGaugeView1.setTargetValue(0);
    }

    @Override
    public void onFetchServerFailed() {

        Log.wtf("FetchServer", "Failed");
        startBtn.setEnabled(true);
        restarttbtn.setEnabled(true);
    }

    @Override
    public void onFindingBestServerStarted() {

    }

    @Override
    public void onTestFinished(SpeedTestResult speedTestResult) {

        Log.wtf("onTestFinished", speedTestResult.toString());

        Log.wtf("closed?", "" + closed);

        result = "Ping: " + speedTestResult.getPing();
        result += "\n";
        result += "Download speed: " + speedTestResult.getDownloadSpeed();
        result += "\n";
        result += "Upload speed: " + speedTestResult.getUploadSpeed();
        result += "\n";
        result += "Test length: " + speedTestResult.getDate();

        // tvPingResults.setText("Ping: "+test.getPingTime()+" ms");
        downloadTV.setText(String.format(Locale.ENGLISH, "%.2f Mbps", speedTestResult.getDownloadSpeed()));
        uploadTV.setText(String.format(Locale.ENGLISH, "%.2f Mbps", speedTestResult.getUploadSpeed()));
        mGaugeView1.setTargetValue(0);
        startBtn.setEnabled(true);
        restarttbtn.setEnabled(true);
        restarttbtn.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString("upload", uploadTV.getText().toString());
        bundle.putString("download", downloadTV.getText().toString());
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("latitude", latitude);
        Intent intent = new Intent(SpeedTestNewActivity.this,
                SpeedTestFormActivity.class);
        intent.putExtras(bundle);
        if (!closed)
            startActivity(intent);
        else
            Log.wtf("msakkara", "so ma shi");
    }

    @Override
    public void onPingStarted() {

        Log.wtf("Ping", "started");
        Log.wtf("onPingStarted", System.currentTimeMillis()+"");
    }

    @Override
    public void onPingFinished(int i) {

        Log.wtf("onPingFinished", "" + i);
        // tvPingResults.setText("Ping: "+ping+" ms");
        Log.wtf("onPingFinished", System.currentTimeMillis()+"");
    }

    @Override
    public void onDownloadTestStarted() {

        Log.wtf("DownloadTest", "started");

        mGaugeView1.setTargetValue(0);
    }

    @Override
    public void onDownloadTestProgress(int speedKbs, double v) {

        Log.wtf("onDownloadTestProgress", "" + speedKbs);
        Log.wtf("onDownloadTestProgress v" , "" + v);

        //mGaugeView1.setTargetValue((float) (speedKbs * 0.01));
        mGaugeView1.setTargetValue((float) (v * 10));
        // mGaugeView1.setTargetValue((float) 900 );onUploadTestProgress

        //downloadTV.setText(String.format(Locale.ENGLISH, "%.2f Mbps", (float) (speedKbs * 0.001)));
        downloadTV.setText(String.format(Locale.ENGLISH, "%.2f Mbps", (float) (v)));
    }

    @Override
    public void onDownloadTestFinished(double speedKbs) {

        Log.wtf("onDownloadTestFinished", "" + speedKbs);
        mGaugeView1.setTargetValue(0);
        //downloadTV.setText(String.format(Locale.ENGLISH, "%.2f Mbps", (float) (speedKbs * 0.001)));
        downloadTV.setText(String.format(Locale.ENGLISH, "%.2f Mbps", (float) (speedKbs)));
    }

    @Override
    public void onUploadTestStarted() {
        Log.wtf("onUploadTest", "started");
        mGaugeView1.setTargetValue(0);
    }

    @Override
    public void onUploadTestProgress(int speedKbs, double v) {

        Log.wtf("onUploadTestProgress", "" + speedKbs);
        Log.wtf("onUploadTestProgress v", "" + v);
        //mGaugeView1.setTargetValue((float) (speedKbs * 0.01));
        mGaugeView1.setTargetValue((float) (v * 10));
        //uploadTV.setText(String.format(Locale.ENGLISH, "%.2f Mbps", (float) (speedKbs * 0.001)));
        uploadTV.setText(String.format(Locale.ENGLISH, "%.2f Mbps", (float) (v )));
    }

    @Override
    public void onUploadTestFinished(double speedKbs) {

        Log.wtf("onUploadTestFinished", "" + speedKbs);

        mGaugeView1.setTargetValue(0);
        //uploadTV.setText(String.format(Locale.ENGLISH, "%.2f Mbps", (float) (speedKbs * 0.001)));
        uploadTV.setText(String.format(Locale.ENGLISH, "%.2f Mbps", (float) (speedKbs )));
    }

    @Override
    public void onTestFatalError(String s) {

    }

    @Override
    public void onTestInterrupted(String s) {

    }
    //</editor-fold>
}
