package com.ids.ict.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.classes.WorkaroundMapFragment;
import com.ids.ict.adapters.PinMeterListArrayAdapter;
import com.ids.ict.classes.InitialMapSettings;
import com.ids.ict.classes.IssueNumber;
import com.ids.ict.classes.IssueTypes;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.MapLabel;
import com.ids.ict.classes.MapPinLocation;
import com.ids.ict.classes.MobileConfiguration;
import com.ids.ict.classes.Provider;
import com.ids.ict.parser.GetMobileConfiguration;
import com.ids.ict.parser.InitialMapSettingsXMLPullParserHandler;
import com.ids.ict.parser.IssueTypesXMLPullParserHandler;
import com.ids.ict.parser.MapLabelXMLPullParserHandler;
import com.ids.ict.parser.MapPinLocationXMLPullParserHandler;
import com.ids.ict.parser.ProviderXMLPullParserHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.shipp.util.DatabaseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MyLocationActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback, GoogleMap.OnCameraMoveListener {

    ArrayList<MapPinLocation> result;
    GoogleMap googleMap;
    private GridView horizontalGridView;
    ArrayList<String> list;
    private float zoom, currentzoom, threshold;
    static LatLng location;
    boolean waitingForLocationUpdate = true;
    ToggleButton mainView, detailedView;
    Spinner issueTypeSpinner, providerSpinner;
    ArrayList<IssueNumber> imageNumberArray = new ArrayList<IssueNumber>();
    ArrayList<IssueNumber> imageIssue = new ArrayList<IssueNumber>();
    int footerButton;
    ListView issueTypelist;
    boolean showDialog = false;
    Provider selectedProvider = new Provider();
    IssueTypes selectedIssue = new IssueTypes();
    String selectedView;
    ArrayList<IssueTypes> issueArray;
    public String view;
    private HorizontalScrollView lineargrid;
    private ScrollView mScrollView;
    ProgressDialog dialog;
    private Handler mHandler;
    private boolean active = false;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    public double latitude;
    public double longitude;
    boolean isFirst = true;
    LatLng location1;
    DatabaseHandler db = new DatabaseHandler(MyLocationActivity.this);
    private String provider;
    private boolean locationfound = false;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    public static Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mActivity = this;
        Actions.setLocal(this);
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.my_location);

        // Getting Google Play availability status
        mainView = (ToggleButton) findViewById(R.id.main_view);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        detailedView = (ToggleButton) findViewById(R.id.detailed_view);

        issueTypeSpinner = (Spinner) findViewById(R.id.issue_type_spinner);
        providerSpinner = (Spinner) findViewById(R.id.provider_spinner);
        issueTypelist = (ListView) findViewById(R.id.issueTypelist);
        lineargrid = (HorizontalScrollView) findViewById(R.id.title_horizontalScrollView);
        horizontalGridView = (GridView) findViewById(R.id.horizontal_gridView);


        TextView error = (TextView) findViewById(R.id.numbererror);
        if (MyApplication.nightMod) {
            mainView.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.view_type_night));
            detailedView.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.view_type_night));

            //error.setTextColor(getResources().getColor(R.color.nightBlue));

            mainView.setPadding(20, 20, 20, 20);
            mainView.setTextColor(getResources().getColor(R.color.nightBlue));
            detailedView.setPadding(20, 20, 20, 20);
        } else {
            mainView.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.view_type_button));

            detailedView.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.view_type_button));


            //error.setTextColor(getResources().getColor(R.color.bordo));


            mainView.setPadding(20, 20, 20, 20);
            detailedView.setPadding(20, 20, 20, 20);
        }
        /*if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            dialog = ProgressDialog.show(MyLocationActivity.this, "",
                    "Loading..Wait..", true);
        } else {
            dialog = ProgressDialog.show(MyLocationActivity.this, "",
                    "الرجاء الانتظار", true);
        }*/
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
            // not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();

        } else { // Google Play Services are available

            if (googleMap == null) {

                WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);//.getMap();
                mapFragment.onCreate(savedInstanceState);
                mapFragment.onResume();
                mapFragment.getMapAsync(this);


               /* try {
                    ((WorkaroundMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map)).getMapAsync(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/


                mScrollView = (ScrollView) findViewById(R.id.scroll);

                ((WorkaroundMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map))
                        .setListener(new WorkaroundMapFragment.OnTouchListener() {
                            @Override
                            public void onTouch() {
                                mScrollView
                                        .requestDisallowInterceptTouchEvent(true);
                            }
                        });

            }

            if (MyApplication.nightMod) {
                final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
                buttop.setBackgroundResource(R.drawable.footer_nt);
                final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
                footer.setBackgroundResource(R.drawable.footer_nt);
            }

            if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
                buttop.setImageResource(R.drawable.back_btn_en);
            }



           /* //LatLng cur_Latlng = new LatLng(33.8547, 35.8623);
            LatLng cur_Latlng = new LatLng(25.486607, 51.177402);
            // googleMap.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    cur_Latlng).zoom(7).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    FillInitialMapSettings fillinitialmaps = new FillInitialMapSettings();
                    fillinitialmaps.execute();

                }
            }, 1000);


//            googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
//
//                private float currentZoom = -1;
//
//                @Override
//                public void onCameraChange(CameraPosition pos) {
//                    if (pos.zoom != currentZoom) {
//                        currentZoom = pos.zoom;
//                        if (threshold != 0) {
//                            if (currentZoom >= threshold) {
//
//                                detailedView.performClick();
//                                currentzoom = pos.zoom;
//
//                            } else {
//
//                                if (currentZoom < threshold) {
//                                    mainView.performClick();
//                                }
//                            }
//                        }
//                    }
//                }
//            });
        }

        Actions.loadMainBar(this);
        ViewResizing.setPageTextResizing(this);

        Log.wtf("getLookup('76')","nameen : " + getLookup("76").nameen);
        Log.wtf("getLookup('87')","nameen : " + getLookup("87").nameen);
    }


    @Override
    public void onMapReady(GoogleMap map) {

        this.googleMap = map;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        /*map.setMyLocationEnabled(true);
                        map.setTrafficEnabled(true);
                        map.setIndoorEnabled(true);
                        map.setBuildingsEnabled(true);*/
        googleMap.getUiSettings().setZoomControlsEnabled(true);



        //LatLng cur_Latlng = new LatLng(33.8547, 35.8623);
        LatLng cur_Latlng = new LatLng(25.486607, 51.177402);

        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                cur_Latlng).zoom(7).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.setOnCameraMoveListener(this);
    }


    /*public class getLocationName extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String res = "";
            String lang = "en";
            if (MyApplication.Lang.equals(MyApplication.ENGLISH))
                lang = "en";
            else
                lang = "ar";

            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=+" + latitude + "," + longitude + "&sensor=true_or_false" + "&language=" + lang;
            Log.wtf("getLocationName","url : " + url);

            Connection conn = new Connection(url);

            if (!conn.hasError()) {
                InputStream is = conn.getInputStream();
                res = convertStreamToString(is);
            }

            Log.wtf("getLocationName","res : " + res);
            return res;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            try {
                boolean contains = false;
                ArrayList<String> names = GetLocName(aVoid);

                if (names.size() != 0 && isFirst){

                    for (int i = 0; i < result.size(); i++) {
                        if (names.contains(result.get(i).getSubLocality()) || names.contains(result.get(i).getSubLocalityAr())) {
                            contains = true;
                        }
                    }

                    try {
                        if (!contains) {
                            if (MyApplication.Lang.equals(MyApplication.ARABIC))
                                Actions.onCreateDialog3(MyLocationActivity.this, getLookup("76").namear);
                            else
                                Actions.onCreateDialog3(MyLocationActivity.this, getLookup("76").nameen);

                        } else {
                            if (MyApplication.Lang.equals(MyApplication.ARABIC))
                                Actions.onCreateDialog3(MyLocationActivity.this, getLookup("87").namear);
                            else
                                Actions.onCreateDialog3(MyLocationActivity.this, getLookup("87").nameen);
                        }

                        Log.wtf("getLookup('76')","nameen : " + getLookup("76").nameen);
                        Log.wtf("getLookup('76')","nameen : " + getLookup("87").nameen);
                    } catch (Exception e) {
                        Log.wtf("getLocationName","onCreateDialog3 error : " + e.getMessage());
                    }
                    isFirst = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }*/


//    private String convertStreamToString(InputStream is) {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        StringBuilder sb = new StringBuilder();
//
//        String line = null;
//        try {
//            while ((line = reader.readLine()) != null) {
//                sb.append(line).append('\n');
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return sb.toString();
//    }


//    public ArrayList<String> GetLocName(String JsonString) throws JSONException, UnsupportedEncodingException {
//
//
//        ArrayList<String> names = new ArrayList<String>();
//        JSONObject object = new JSONObject(JsonString);
//
//        //  JSONObject obj = object.getJSONObject("results");
//        // String state = obj.getString("item");
//        //  if(state.equals("success")) {
//
//        JSONArray jarray = object.getJSONArray("results");
//        for (int i = 0; i < jarray.length(); i++) {
//            JSONObject json_data = jarray.getJSONObject(i);
//            JSONArray jarray2 = json_data.getJSONArray("address_components");
//            for (int j = 0; j < jarray2.length(); j++) {
//                JSONObject json_data2 = jarray2.getJSONObject(j);
//                names.add(json_data2.getString("long_name"));
//            }
//        }
//        return names;
//        // }
//
//    }


    @Override
    protected void onResume() {
        super.onResume();

        //  registerLocationUpdates();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    registerLocationUpdates();

                    try {
                        //pd.dismiss();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBarLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {

                    }

                    FillPinsOnMap fillpinsonmap = new FillPinsOnMap();
                    fillpinsonmap.execute("detailed", "0", "0");
                    FillPinMeter fillPinMeter = new FillPinMeter();
                    fillPinMeter.execute();
                    fillProviderSpinner fillspinner = new fillProviderSpinner();
                    fillspinner.execute();
                    fillIssueTypeSpinner fillspinnerissue = new fillIssueTypeSpinner();
                    fillspinnerissue.execute();

                    showDialog = true;

                    getMobileConfigurationAsynck getmobile = new getMobileConfigurationAsynck();
                    getmobile.execute();

                }
                else {

                    FillPinsOnMap fillpinsonmap = new FillPinsOnMap();
                    fillpinsonmap.execute("detailed", "0", "0");
                    FillPinMeter fillPinMeter = new FillPinMeter();
                    fillPinMeter.execute();
                    fillProviderSpinner fillspinner = new fillProviderSpinner();
                    fillspinner.execute();
                    fillIssueTypeSpinner fillspinnerissue = new fillIssueTypeSpinner();
                    fillspinnerissue.execute();

                    showDialog = true;

                    getMobileConfigurationAsynck getmobile = new getMobileConfigurationAsynck();
                    getmobile.execute();
                }

            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }


    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }


    private final Runnable m_Runnable = new Runnable() {
        public void run()
        {
            if (Actions.isNetworkAvailable(MyLocationActivity.this) && active) {

                FillPinsOnMap fillpinsonmap = new FillPinsOnMap();
                fillpinsonmap.execute(view, selectedProvider.getId(), selectedIssue.getId());
            }
            MyLocationActivity.this.mHandler.postDelayed(m_Runnable, Long.parseLong(refreshtime));
        }

    };


    public ArrayList<MobileConfiguration> mobile;
    public String refreshtime;


    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }


    public void backTo(View v) {
        Actions.backTo(this);
    }

    @Override
    public void onCameraMove() {
        isFirst = true;
    }


    private class MyLocationListener implements LocationListener {

        private float tilt;

        public void onLocationChanged(Location location) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if (!locationfound)
                if (longitude != 0.0 && longitude != 0.0) {
                    location1 = new LatLng(latitude, longitude);

//            LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//            List<String> providers = mLocationManager.getProviders(true);
//            Geocoder geocoder;
//            List<Address> addresses;
//            geocoder = new Geocoder(MyLocationActivity.this, Locale.getDefault());
//
//            try {
//                addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String city = addresses.get(0).getLocality();
//                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();// Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

                    // Zoom in, animating the camera.

                    // address = loca.getText().toString();

                    isFirst = true;

                    if (zoom == 0)
                        zoom = 16;
                    if (threshold == 0)
                        threshold = 14;
                    if (zoom != 0) {

                        //  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1,
                        //        zoom));
                        //   googleMap.getUiSettings().setScrollGesturesEnabled(false);
                        // googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000,
                        //  null);
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(c), new GoogleMap.CancelableCallback()
//                        {
//
//                            @Override
//                            public void onFinish()
//                            {
//                                googleMap.getUiSettings().setScrollGesturesEnabled(true);
//
//                            }
//
//                            @Override
//                            public void onCancel()
//                            {
//                                googleMap.getUiSettings().setAllGesturesEnabled(true);
//
//                            }
//                        });

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(location1)
                                .zoom(zoom)
                                .bearing(60)
                                .tilt(30)
                                .build();
                        try{
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Log.wtf("MyLocatoin","googleMap.animateCamera error : " + e.getMessage());
                        }

                        //  googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 10000, null);

//                        CameraUpdate center=
//                                CameraUpdateFactory.newLatLng(location1);
//                     //   CameraUpdate zoomcam=CameraUpdateFactory.zoomTo(zoom);
//
//                        googleMap.moveCamera(center);
                        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(location1));//Moves the camera to users current longitude and latitude
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location1,(float) 14.6));//Animates camera and zooms to preferred state on the user's current location.
//                                          //  googleMap.animateCamera(zoomcam,10000,null);
//                    }

//                        CameraPosition cameraPosition = new CameraPosition.Builder().target(
//                               location1).zoom(zoom).build();working here


                    }


                    Log.d("rrr", zoom + "");
                    if (waitingForLocationUpdate) {
                        waitingForLocationUpdate = false;
                    }

                    if (  ActivityCompat.checkSelfPermission(MyLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//				ActivityCompat.requestPermissions(MyLocationActivity.this, new String[]{
//						Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    } else
                        locationManager.removeUpdates(this);
                    locationfound = true;
                }

        }

        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        public void onProviderEnabled(String s) {
        }

        public void onProviderDisabled(String s) {
        }
    }


    void registerLocationUpdates() {

        /*try {
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MyLocationActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } catch (Exception e) { }*/

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) this .getSystemService(LOCATION_SERVICE);

        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

        locationListener = new MyLocationListener();

        provider = locationManager.getBestProvider(criteria, true);

        if (provider == null) {
            return;
        }

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MyLocationActivity.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            ActivityCompat.requestPermissions(MyLocationActivity.this, new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION }, 2);
        }
        else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Location oldLocation = locationManager.getLastKnownLocation(provider);

            if (oldLocation != null) {
                waitingForLocationUpdate = false;
            } else {
                oldLocation = locationManager.getLastKnownLocation(provider);
//                LatLng ll = new LatLng(oldLocation.getLatitude(),oldLocation.getLongitude());
//                CameraUpdate center = CameraUpdateFactory.newLatLng(ll);
//                googleMap.animateCamera(center, 400, null);
//                CameraPosition cameraPosition = new CameraPosition.Builder()
//                        .target(new LatLng(oldLocation.getLatitude(),oldLocation.getLongitude()))
//                        .zoom(zoom)
//                        .bearing(60)
//                        .tilt(30)
//                        .build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }

    }


    @Override
    public void onLocationChanged(Location location) {

        TextView tvLocation = (TextView) findViewById(R.id.tv_location);

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        //   googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        // googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

        // Setting latitude and longitude in the TextView tv_location
        tvLocation.setText("Latitude:" + latitude + ", Longitude:" + longitude);

    }


    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


    private void gridViewSetting(GridView gridview, int size) {

        // Calculated single Item Layout Width for each grid element ....
        int width = MyApplication.screenWidth / 3;

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int totalWidth = (int) (width * size * density);
        int singleItemWidth = (int) (width * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridview.setLayoutParams(params);
        gridview.setColumnWidth(singleItemWidth);
        gridview.setHorizontalSpacing(2);
        gridview.setStretchMode(GridView.STRETCH_SPACING);
        gridview.setNumColumns(size);
    }


    protected class FillPinMeter extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;
        ArrayList<MapLabel> result;
        DatabaseHandler db = new DatabaseHandler(MyLocationActivity.this);

        @Override
        protected void onPreExecute() {

            /*pd = ProgressDialog.show(
                    MyLocationActivity.this,
                    "",
                    MyLocationActivity.this.getResources().getString(
                            R.string.loading), false);*/

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            // LayoutParams params=horizontalGridView.getLayoutParams();
            // params.width=MyApplication.screenWidth;
            // horizontalGridView.setLayoutParams(params);

        }

        @Override
        protected String doInBackground(Void... a) {
            if (Actions.isNetworkAvailable(MyLocationActivity.this)) {
                //Connection.disableSSLCertificateChecking();
                String url = MyApplication.link + MyApplication.map
                        + "GetMainMapLabels?";
                Connection conn = new Connection(url);

                if (!conn.hasError()) {
                    MapLabelXMLPullParserHandler parser = new MapLabelXMLPullParserHandler();

                    result = parser.parse(conn.getInputStream());
                    db.InsertMapLabels(result);
                }
            } else {
                // get the settings from the database
                result = db.getMapLabels();

            }
            return "";

        }

        @Override
        protected void onPostExecute(String r) {

            horizontalGridView.setNumColumns(result.size());

            // gridViewSetting(horizontalGridView, result.size());
            PinMeterListArrayAdapter adapter = new PinMeterListArrayAdapter(
                    MyLocationActivity.this, result);
            horizontalGridView.setAdapter(adapter);

            try {
                //pd.dismiss();

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {

            }

        }
    }


    protected class FillPinsOnMap extends AsyncTask<String, Void, ArrayList<MapPinLocation>> {
        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            if (showDialog) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
                /*pd = ProgressDialog.show(
                        MyLocationActivity.this,
                        "",
                        MyLocationActivity.this.getResources().getString(
                                R.string.loading), false);*/

        }

        @Override
        protected ArrayList<MapPinLocation> doInBackground(String... a) {
            view = a[0];
            if (Actions.isNetworkAvailable(MyLocationActivity.this)) {

                //Connection.disableSSLCertificateChecking();
                String url = null;
                String lang = "en";
                if (MyApplication.Lang.equals(MyApplication.ARABIC))
                    lang = "ar";
                else
                    lang = "en";
                if (a[0].equals("main"))
                    url = MyApplication.link + MyApplication.map + "GetMainViewPins?Language=" + lang
                            + "&serviceProviderId=" + a[1] + "&byLocalityOnly=0";
                else if (a[0].equalsIgnoreCase("detailed"))
                    url = MyApplication.link + MyApplication.map + "GetDetailedViewPins?Language="
                            + lang + "&serviceProviderId=" + a[1] + "&byLocalityOnly=10&issueTypeId=" + a[2];

                Log.wtf("FillPinsOnMap","url : " + url);
                Connection conn = new Connection(url);

                if (!conn.hasError()) {
                    MapPinLocationXMLPullParserHandler parser = new MapPinLocationXMLPullParserHandler();

                    result = parser.parse(conn.getInputStream());
                    Log.wtf("FillPinsOnMap","pin count = " + result.size());

                    db.InsertPins(result, view, a[1], a[2]);
                }
            } else {
                if (a[0].equalsIgnoreCase("main"))
                    result = db.getPins(a[1], "0", "main");
                else if (a[0].equalsIgnoreCase("detailed"))
                    result = db.getPins(a[1], a[2], "detailed");

            }
            return result;

        }

        @Override
        protected void onPostExecute(ArrayList<MapPinLocation> r) {
            // put the pins on map
            showDialog = false;

            FillPins(r, view);

            if(isFirst) {

                if (result.size() > 0) {

                    if (MyApplication.Lang.equals(MyApplication.ARABIC))
                        Actions.onCreateDialog3(MyLocationActivity.this, getLookup("87").namear);
                    else
                        Actions.onCreateDialog3(MyLocationActivity.this, getLookup("87").nameen);
                }
                else {

                    if (MyApplication.Lang.equals(MyApplication.ARABIC))
                        Actions.onCreateDialog3(MyLocationActivity.this, getLookup("76").namear);
                    else
                        Actions.onCreateDialog3(MyLocationActivity.this, getLookup("76").nameen);
                }

                isFirst = false;
            }

            try {
                //pd.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {

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


    public void FillPins(ArrayList<MapPinLocation> pins, String view) {

        if (googleMap != null) {
            googleMap.clear();
        }
        imageNumberArray.clear();

        for (int i = 0; i < pins.size(); i++) {
            if (Actions.isNetworkAvailable(MyLocationActivity.this)) {
                String issuname = pins.get(i).getIssueType();
                int number = Integer.parseInt(pins.get(i).getNumber());
                if (view.equalsIgnoreCase("main")) {

                    for (int j = 0; j < imageIssue.size(); j++) {

                        if (imageIssue.get(j).getLabel().trim()
                                .equalsIgnoreCase(issuname.trim())) {

                            IssueNumber iss = new IssueNumber();
                            iss.setImageName(imageIssue.get(j).getImageName());
                            iss.setLabel(issuname);
                            iss.setNumber(pins.get(i).getNumber());
                            int k = containsImageIssue(iss, imageNumberArray);
                            if (k == -1)
                                imageNumberArray.add(iss);
                            else {
                                int nb = Integer.parseInt(imageNumberArray.get(
                                        k).getNumber());
                                int sum = nb + number;
                                imageNumberArray.get(k).setNumber(
                                        String.valueOf(sum));
                            }

                        }
                    }
                } else if (view.equalsIgnoreCase("detailed")) {

                    IssueNumber iss = new IssueNumber();
                    iss.setImageName(pins.get(i).getPinIcon());
                    if (MyApplication.Lang.equals(MyApplication.ENGLISH))
                        iss.setLabel(pins.get(i).getIssueType());
                    else
                        iss.setLabel(pins.get(i).getIssueTypeAr());
                    iss.setNumber("1");
                    int p = containsImageIssue(iss, imageNumberArray);
                    if (p == -1)
                        imageNumberArray.add(iss);
                    else {
                        int nb = Integer.parseInt(imageNumberArray.get(p)
                                .getNumber());
                        int sum = nb + 1;
                        imageNumberArray.get(p).setNumber(String.valueOf(sum));
                    }
                }
            }

            String iconname = Actions.imageNameClean(pins.get(i).getPinIcon());
            if (!iconname.trim().equals("")) {
                location = new LatLng(Double.parseDouble(pins.get(i)
                        .getLocationY()), Double.parseDouble(pins.get(i)
                        .getLocationX()));

                if (googleMap != null) {


                    if (MyApplication.Lang.equals(MyApplication.ARABIC)) {

                        Marker hamburg = googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(pins.get(i).getSubLocalityAr())// +" "+pins.get(i).getIssueTypeAr())
                                .snippet(pins.get(i).getIssueTypeAr())
                                .icon(BitmapDescriptorFactory
                                        .fromResource(getResources().getIdentifier(
                                                iconname, "drawable",
                                                getPackageName()))));
                    } else {
                        Marker hamburg = googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(pins.get(i).getSubLocality())// +" "+pins.get(i).getIssueType())
                                .snippet(pins.get(i).getIssueType())
                                .icon(BitmapDescriptorFactory
                                        .fromResource(getResources().getIdentifier(
                                                iconname, "drawable",
                                                getPackageName()))));
                    }

                    /*Marker hamburg = googleMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(pins.get(i).getIssueType() + " " + pins.get(i).getNumber() + "\n" + pins.get(i).getLocality())
                            .icon(BitmapDescriptorFactory
                                    .fromResource(getResources().getIdentifier(
                                            iconname, "drawable",
                                            getPackageName()))));*/

                }

            }
        }

        Iterator<IssueNumber> iter = imageNumberArray.iterator();
        while (iter.hasNext()) {
            IssueNumber p = iter.next();
            if (p.getNumber().equals("0"))
                iter.remove();
        }

        if (Actions.isNetworkAvailable(MyLocationActivity.this)) {
            // save issue number list
            db.InsertIssueNumber(imageNumberArray, view);
        } else {
            imageNumberArray = db.getIssueNumber(view);
        }
        try {
            IssueTypeNumberArrayAdapter issuenumber = new IssueTypeNumberArrayAdapter(
                    MyLocationActivity.this, imageNumberArray, view);
            issueTypelist.setAdapter(issuenumber);
            issuenumber.setListViewHeightBasedOnChildren(issueTypelist);
        } catch (Exception e) {

        }


        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(MyLocationActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(MyLocationActivity.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(MyLocationActivity.this);
                snippet.setTextColor(Color.BLACK);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }


    public int containsImageIssue(IssueNumber is, ArrayList<IssueNumber> array) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getImageName().equalsIgnoreCase(is.getImageName())
                    && array.get(i).getLabel().equalsIgnoreCase(is.getLabel()))
                return i;

        }
        return -1;
    }


    protected class fillProviderSpinner extends AsyncTask<Void, Void, ArrayList<Provider>> {
        ArrayList<Provider> arrayprovider = new ArrayList<Provider>();
        ProgressDialog pd;
        DatabaseHandler db = new DatabaseHandler(MyLocationActivity.this);

        @Override
        protected void onPreExecute() {
            /*pd = ProgressDialog.show(
                    MyLocationActivity.this,
                    "",
                    MyLocationActivity.this.getResources().getString(
                            R.string.loading), false);*/

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<Provider> doInBackground(Void... params) {
            if (Actions.isNetworkAvailable(MyLocationActivity.this)) {
                //Connection.disableSSLCertificateChecking();
                String lang = "en";
                if (MyApplication.Lang.equals(MyApplication.ARABIC)) lang = "ar";

                String url = MyApplication.link + MyApplication.general + "GetServiceProviders?password=" + MyApplication.pass + "&Language=" + lang;
                Log.wtf("fillProviderSpinner","url : " + url);

                Connection conn = new Connection(url);

                if (!conn.hasError()) {
                    ProviderXMLPullParserHandler parser = new ProviderXMLPullParserHandler(
                            getString(R.string.All));

                    arrayprovider = parser.parse(conn.getInputStream());
                    db.InsertServiceProviders(arrayprovider);
                }
            } else {
                arrayprovider = db.getProviders();
            }
            return arrayprovider;

        }

        @Override
        protected void onPostExecute(ArrayList<Provider> g) {

            fillspinner(g);
            try {
                //pd.dismiss();

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {

            }
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }


    public class IssueTypeNumberArrayAdapter extends ArrayAdapter<IssueNumber> {
        // Activity context;
        private String view;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        ArrayList<IssueNumber> list = new ArrayList<IssueNumber>();

        public IssueTypeNumberArrayAdapter(Activity context,
                                           ArrayList<IssueNumber> list, String view) {

            super(MyLocationActivity.this, R.layout.issuetype_list_item, list);

            //   this.context = context;
            this.list = list;
            this.view = view;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_launcher)
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true)
                    .displayer(new RoundedBitmapDisplayer(20)).build();
            View row = convertView;
            RecordHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.issuetype_list_item, parent, false);

                holder = new RecordHolder();
                holder.txtTitle = (TextView) row.findViewById(R.id.issuename);
                holder.imageItem = (ImageView) row.findViewById(R.id.issueimage);
                holder.number = (TextView) row.findViewById(R.id.issuenumber);
                row.setTag(holder);
            } else {
                holder = (RecordHolder) row.getTag();
            }
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
                holder.txtTitle.setTypeface(MyApplication.facePolarisMedium);
            } else {
                holder.txtTitle.setTypeface(MyApplication.faceDinar);
            }
            IssueNumber item = list.get(position);

            holder.txtTitle.setText(item.getLabel());
            holder.number.setText(item.getNumber());


        /*if (MyApplication.nightMod)
            holder.number.setTextColor(context.getResources().getColor(
                    R.color.nightBlue));
        else
            holder.number.setTextColor(context.getResources().getColor(
                    R.color.bordo));*/


            if (view.equalsIgnoreCase("main")) {
                // new
                // DownloadImageTask((ImageView)row.findViewById(R.id.issueimage)).execute(item.getImageName());
                ImageLoader.getInstance().displayImage(item.getImageName(),
                        (ImageView) row.findViewById(R.id.issueimage), options,
                        animateFirstListener);
            } else if (view.equalsIgnoreCase("detailed")) {
                int i = getResources().getIdentifier(item.getImageName(),
                        "drawable", getPackageName());
                String cleaned = Actions.imageNameClean(item.getImageName());
                Drawable d = Actions.getImageFromResources(MyLocationActivity.this, cleaned);

                holder.imageItem.setImageDrawable(d);
            }

            return row;

        }

        class RecordHolder {
            TextView txtTitle;
            ImageView imageItem;
            TextView number;

        }

        public void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight
                    + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }

        private class AnimateFirstDisplayListener extends
                SimpleImageLoadingListener {

            final List<String> displayedImages = Collections
                    .synchronizedList(new LinkedList<String>());

            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                if (loadedImage != null) {
                    ImageView imageView = (ImageView) view;
                    boolean firstDisplay = !displayedImages.contains(imageUri);
                    if (firstDisplay) {
                        FadeInBitmapDisplayer.animate(imageView, 500);
                        displayedImages.add(imageUri);
                    }
                }
            }
        }
    }


    protected class fillIssueTypeSpinner extends AsyncTask<Void, Void, ArrayList<IssueTypes>> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
           /* pd = ProgressDialog.show(
                    MyLocationActivity.this,
                    "",
                    MyLocationActivity.this.getResources().getString(
                            R.string.loading), false);*/

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            issueArray = new ArrayList<IssueTypes>();

        }

        @Override
        protected ArrayList<IssueTypes> doInBackground(Void... params) {
            if (Actions.isNetworkAvailable(MyLocationActivity.this)) {
                //Connection.disableSSLCertificateChecking();
                String url = "";
                if(MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    url = MyApplication.link + MyApplication.map
                            + "GetIssueTypes?language=en";
                }
                else
                    url = MyApplication.link + MyApplication.map
                            + "GetIssueTypes?language=ar";

                Connection conn = new Connection(url);

                if (!conn.hasError()) {
                    IssueTypesXMLPullParserHandler parser = new IssueTypesXMLPullParserHandler(
                            getApplicationContext(), getString(R.string.All));

                    issueArray = parser.parse(conn.getInputStream());
                    db.InsertIssueTypes(issueArray);
                }
            } else {
                issueArray = db.getIssueTypes();
            }
            return issueArray;

        }

        @Override
        protected void onPostExecute(ArrayList<IssueTypes> g) {
            for (int i = 0; i < g.size(); i++) {
                IssueNumber maplabel = new IssueNumber();
                maplabel.setImageName(g.get(i).getLogo());
                maplabel.setLabel(g.get(i).getName());
                maplabel.setNumber("0");
                if (!imageIssue.contains(maplabel))
                    imageIssue.add(maplabel);

            }
            fillspinnerIssueTypes(g);
            try {
                //pd.dismiss();

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {

            }
        }

    }


    public void fillspinner(final ArrayList<Provider> array) {

        ArrayAdapter<Provider> providerArrayAdapter = new ArrayAdapter<Provider>(
                this, R.layout.spinner_title, array) {

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.spinner_list_text, parent, false);
                }
                TextView tv = (TextView) v.findViewById(R.id.TextView01);

                tv.setText(this.getItem(position).getName());

                return v;
            }

            @Override
            public Provider getItem(int position) {

                Provider v = array.get(position);

                return v;
            }

            ;

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    v = vi.inflate(R.layout.spinner_title, parent, false);
                    ViewResizing.setListRowTextResizing(v,
                            MyLocationActivity.this);
                }
                TextView tv = (TextView) v.findViewById(R.id.TextView01);

                tv.setText(this.getItem(position).getName());
                if (MyApplication.nightMod)
                    tv.setTextColor(getResources().getColor(R.color.nightBlue));
                else
                    tv.setTextColor(getResources().getColor(R.color.bordo));

                TextView title = (TextView) v.findViewById(R.id.title);
                title.setText(getString(R.string.service_provider));
                return v;
            }
        };
        providerArrayAdapter.setDropDownViewResource(R.layout.spinner_list_text);
        providerSpinner.setAdapter(providerArrayAdapter);
        providerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapter, View v,
                                       int i, long lng) {
                selectedProvider = (Provider) adapter.getSelectedItem();
                if (!selectedProvider.getId().equals("0")) {

                    if (googleMap != null) {
                        googleMap.clear();
                    }
                    showDialog = true;
                    FillPinsOnMap fillpinsonmap = new FillPinsOnMap();
                    fillpinsonmap.execute("detailed",
                            selectedProvider.getId(),
                            selectedIssue.getId());
                }
            }

            public void onNothingSelected(AdapterView arg0) {
            }
        });
    }


    public void fillspinnerIssueTypes(final ArrayList<IssueTypes> array) {

        ArrayAdapter<IssueTypes> issueArrayAdapter = new ArrayAdapter<IssueTypes>(
                this, R.layout.spinner_title, array) {

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.spinner_list_text, parent, false);
                }
                TextView tv = (TextView) v.findViewById(R.id.TextView01);

                // ViewResizing.textResize(KamcoPortfolioActivity.this,
                // tv,(int)((TextView)tv).getTextSize());

                tv.setText(this.getItem(position).getName());

                return v;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.spinner_title, parent, false);
                    ViewResizing.setListRowTextResizing(v,
                            MyLocationActivity.this);
                }
                TextView tv = (TextView) v.findViewById(R.id.TextView01);

                tv.setText(this.getItem(position).getName());
                if (MyApplication.nightMod)
                    tv.setTextColor(getResources().getColor(R.color.nightBlue));
                else
                    tv.setTextColor(getResources().getColor(R.color.bordo));
                TextView title = (TextView) v.findViewById(R.id.title);

                title.setText(MyLocationActivity.this.getString(R.string.issue_type));

                return v;
            }
        };
        issueArrayAdapter.setDropDownViewResource(R.layout.spinner_list_text);
        issueTypeSpinner.setAdapter(issueArrayAdapter);
        issueTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapter, View v,
                                       int i, long lng) {
                // ViewResizing.textResize(MyLocationActivity.this,
                // (TextView) v,
                // (int) ((TextView) v).getTextSize()/* app.XSMALL */);
                // handle item selection event
                selectedIssue = (IssueTypes) adapter.getSelectedItem();
                if (!selectedIssue.getId().equals("0")) {
                    if (googleMap != null) {
                        googleMap.clear();
                    }
                    showDialog = true;
                    FillPinsOnMap fillpinsonmap = new FillPinsOnMap();
                    fillpinsonmap.execute("detailed",
                            selectedProvider.getId(),
                            selectedIssue.getId());
                }
            }

            public void onNothingSelected(AdapterView arg0) {

                // do something else

            }
        });
    }


    private class getMobileConfigurationAsynck extends
            AsyncTask<Void, Void, ArrayList<MobileConfiguration>> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            /*pd = ProgressDialog.show(
                    MyLocationActivity.this,
                    "",
                    MyLocationActivity.this.getResources().getString(
                            R.string.loading), false);*/

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MobileConfiguration> doInBackground(Void... params) {
            if (Actions.isNetworkAvailable(MyLocationActivity.this)) {
                //Connection.disableSSLCertificateChecking();
                String url = MyApplication.link + MyApplication.general
                        + "GetMobileConfiguration?key=";

                Connection conn = new Connection(url);

                if (!conn.hasError()) {
                    GetMobileConfiguration parser = new GetMobileConfiguration();

                    mobile = parser.parse(conn.getInputStream());
                    db.InsertMobileconfig(mobile);
                }
            } else {
                mobile = db.getMobileconfigs();
            }
            return mobile;
        }

        @Override
        protected void onPostExecute(ArrayList<MobileConfiguration> v) {

            for (int i = 0; i < v.size(); i++) {
                if (v.get(i).getKey().equalsIgnoreCase("OMAutoRefreshTime"))
                    refreshtime = v.get(i).getValue();

            }
            try {
                //pd.dismiss();

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

            } catch (Exception e) {

            }

            if (active) {
                if (refreshtime == null || refreshtime.equals("")) refreshtime = "5000";
                mHandler = new Handler();
                mHandler.postDelayed(m_Runnable, Integer.parseInt(refreshtime));
            }
        }

    }


    public void topBarBack(View v) {
        MyLocationActivity.this.finish();
    }


    protected class FillInitialMapSettings extends
            AsyncTask<Void, Void, InitialMapSettings> {
        ProgressDialog pd;
        ArrayList<InitialMapSettings> result;

        @Override
        protected void onPreExecute() {
            /*pd = ProgressDialog.show(
                    MyLocationActivity.this,
                    "",
                    MyLocationActivity.this.getResources().getString(
                            R.string.loading), false);*/
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected InitialMapSettings doInBackground(Void... a) {

            InitialMapSettings map = new InitialMapSettings();

            if (Actions.isNetworkAvailable(MyLocationActivity.this)) {
                //Connection.disableSSLCertificateChecking();
                String url = MyApplication.link + MyApplication.map
                        + "GetInitialMapSettings";

                Connection conn = new Connection(url);

                if (!conn.hasError()) {
                    InitialMapSettingsXMLPullParserHandler parser = new InitialMapSettingsXMLPullParserHandler();

                    result = parser.parse(conn.getInputStream());
                }
                map = result.get(0);
                db.InsertMapSettings(map);
            } else {

                map = db.getMapSettings();

            }
            return map;

        }

        @Override
        protected void onPostExecute(InitialMapSettings r) {

            try {
                zoom = Float.parseFloat(r.getZoomSettings());
                threshold = Float.parseFloat(r.getThreshold());

                if (ActivityCompat.checkSelfPermission(MyLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MyLocationActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    ActivityCompat.requestPermissions(MyLocationActivity.this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                } else {

                    /*zoom = Float.parseFloat(r.getZoomSettings());
                    threshold = Float.parseFloat(r.getThreshold());*/
                    registerLocationUpdates();
                    // }

                    try {
                        //pd.dismiss();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBarLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {

                    }
                    // location = new LatLng(Double.parseDouble(r.getLocationX()),
                    // Double.parseDouble(r.getLocationY()));
                    //
                    // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,
                    // Float.parseFloat(r.getZoomSettings())));
                    //
                    // // Zoom in, animating the camera.
                    // googleMap.animateCamera(CameraUpdateFactory.zoomTo(Float
                    // .parseFloat(r.getZoomSettings())), 2000, null);
                    int off = 0;

                    try {
                        off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
                    } catch (Exception e) {
                        off = 0;
                    }
                    if (off == 0) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MyLocationActivity.this, R.style.AlertDialogCustom));
                        builder
                                .setMessage(MyLocationActivity.this.getResources().getString(R.string.enablegps))
                                .setCancelable(true)
                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();
                                        Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivityForResult(onGPS, 0);
                                    }
                                })
                                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        FillPinsOnMap fillpinsonmap = new FillPinsOnMap();
                                        fillpinsonmap.execute("detailed", "0", "0");
                                        //MyLocationActivity.this.finish();
                                        FillPinMeter fillPinMeter = new FillPinMeter();
                                        fillPinMeter.execute();
                                        fillProviderSpinner fillspinner = new fillProviderSpinner();
                                        fillspinner.execute();
                                        fillIssueTypeSpinner fillspinnerissue = new fillIssueTypeSpinner();
                                        fillspinnerissue.execute();

                                        showDialog = true;


                                        getMobileConfigurationAsynck getmobile = new getMobileConfigurationAsynck();
                                        getmobile.execute();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        FillPinsOnMap fillpinsonmap = new FillPinsOnMap();
                        fillpinsonmap.execute("detailed", "0", "0");
                        FillPinMeter fillPinMeter = new FillPinMeter();
                        fillPinMeter.execute();
                        fillProviderSpinner fillspinner = new fillProviderSpinner();
                        fillspinner.execute();
                        fillIssueTypeSpinner fillspinnerissue = new fillIssueTypeSpinner();
                        fillspinnerissue.execute();

                        showDialog = true;


                        getMobileConfigurationAsynck getmobile = new getMobileConfigurationAsynck();
                        getmobile.execute();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }


        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        detailedView.setChecked(false);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            FillPinMeter fillPinMeter = new FillPinMeter();
            fillPinMeter.execute();
            fillProviderSpinner fillspinner = new fillProviderSpinner();
            fillspinner.execute();
            fillIssueTypeSpinner fillspinnerissue = new fillIssueTypeSpinner();
            fillspinnerissue.execute();
            showDialog = true;
            FillPinsOnMap fillpinsonmap = new FillPinsOnMap();
            fillpinsonmap.execute("detailed", "0", "0");
            getMobileConfigurationAsynck getmobile = new getMobileConfigurationAsynck();
            getmobile.execute();

        }
    }


    public void viewChange(View v) {
        int id = v.getId();
        if (id == R.id.main_view) {
            if (googleMap != null) {
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000, null);
            }

            mainView.setChecked(true);
            horizontalGridView.setVisibility(View.GONE);

            //  lineargrid.setVisibility(View.GONE);
            detailedView.setChecked(false);
            if (MyApplication.nightMod)

                detailedView.setTextColor(getResources().getColor(
                        R.color.nightBlue));
            else
                detailedView.setTextColor(getResources()
                        .getColor(R.color.bordo));
            mainView.setTextColor(getResources().getColor(R.color.white));
            //   issueTypeSpinner.setVisibility(View.GONE);
            showDialog = true;
            // issueTypeSpinner.setVisibility(View.GONE);
//            FillPinsOnMap fillpinsonmap = new FillPinsOnMap();
//            fillpinsonmap.execute("main", selectedProvider.getId(), "0");
        } else if (id == R.id.detailed_view) {
            showDialog = true;
            if (currentzoom < threshold && googleMap != null)
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(threshold),
                        2000, null);
            horizontalGridView.setVisibility(View.VISIBLE);
            lineargrid.setVisibility(View.VISIBLE);
            issueTypeSpinner.setVisibility(View.VISIBLE);

            if (googleMap != null) {
                googleMap.clear();
            }
            detailedView.setTextColor(getResources().getColor(R.color.white));
            detailedView.setChecked(true);
            mainView.setChecked(false);
            if (MyApplication.nightMod)

                mainView.setTextColor(getResources()
                        .getColor(R.color.nightBlue));
            else
                mainView.setTextColor(getResources().getColor(R.color.bordo));
            issueTypeSpinner.setVisibility(View.VISIBLE);
            FillPinsOnMap fillpinsonmap = new FillPinsOnMap();
            fillpinsonmap.execute("detailed", selectedProvider.getId(),
                    selectedIssue.getId());
        }
    }


    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(MyLocationActivity.this, MoreActivity.class);
                MyLocationActivity.this.startActivity(intent);
                break;
            }
            case R.id.home: {
                MyLocationActivity.this.finish();
                break;
            }

        }
    }

}
