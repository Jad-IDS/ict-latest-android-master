package com.ids.ict.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.Country;
import com.ids.ict.classes.Mail_OFF;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.Profile;
import com.ids.ict.R;
import com.ids.ict.classes.ServicePro;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;

import org.shipp.util.MenuEventController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class reportActivity extends FragmentActivity implements LocationListener {

    String url;
    String qatarID;
    static String num;
    static String email;
    String reg_id, id_rep;
    Bundle bundle;
    Button send_b;
    String issueID;
    String spID;
    String[] bspin2 = null;
    Typeface tf;
    String[] aspin1 = null;
    int issus;
    String sp = "";
    String isquot = "";
    String isblocked = "";
    String locmand = "";
    String isroam = "";
    EditText a;
    String gettok = "";
    String isbloc = "";
    // protected Context context;
    EditText b;
    int pos;
    int pos1;
    Button cancel_b;
    EditText comm_edit;
    EditText roamsp;
    Connection conn;
    private ListView listMenu;
    private final Context context = this;
    boolean waitingForLocationUpdate = true;
    // String provider;
    private RelativeLayout layout;
    private boolean open = false;
    String affecqatarid = "";
    String s = "";
    // map
    // GoogleMap googleMap;
    MyApplication app;
    String loc = "";
    int k = 0;
    EditText email1;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Double longitude = 0.0, latitude = 0.0;// use these values to send in report
    Location location;
    String provider;
    // Marker marker;
    TextView loca;
    int footerButton;
    EditText num1;
    boolean firstRun = true;// to ignore drawing on first call coz probably it
    // may be old value (incorrect-not exact current
    // location)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Actions.setLocal(this);
        setContentView(R.layout.reportpg);
        Actions.loadMainBar(this);

        app = (MyApplication) getApplicationContext();

        // app.face=Typeface.createFromAsset(getAssets(), "fonts/tahomabd.ttf");

        footerButton = this.getIntent().getIntExtra("footerButton", R.id.home);
        // Toast.makeText(this, "mnc value is " + getmnc(), 10).show();

        // LaunchingEvent eventLaunching = new LaunchingEvent();
        // eventLaunching.execute();
        try {
            registerLocationUpdates();
        } catch (Exception e) {
            String url1 = "" + app.link + "PostData.asmx/VerifyRegistration";
            Connection conn = new Connection(url1);

            try {
                String error_return = conn.executeMultipartPost_Send_Error(this
                                .getClass().getSimpleName(), Actions.getDeviceName(),
                        "1", e.getMessage());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        /*
         * locationManager = (LocationManager)
		 * getSystemService(Context.LOCATION_SERVICE); //
		 * locationManager.requestLocationUpdates
		 * (LocationManager.NETWORK_PROVIDER,0,0, this); boolean gps_enabled =
		 * locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		 * boolean network_enabled =
		 * locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		 * if (!gps_enabled) {//error message
		 * 
		 * 
		 * 
		 * Actions.showDialog(this,
		 * getResources().getString(R.string.noNetworkmsg), false);
		 * 
		 * } else {//gps on Criteria criteria = new Criteria(); provider =
		 * locationManager.getBestProvider(criteria, false); location =
		 * locationManager.getLastKnownLocation(provider); //
		 * locationManager.requestLocationUpdates( //
		 * LocationManager.GPS_PROVIDER, // 0, // 0, // this); }
		 * 
		 * if(location!=null){
		 * 
		 * onLocationChanged(location);
		 * 
		 * }
		 */

        // Define the criteria how to select the locatioin provider -> use
        // default
        // Criteria criteria = new Criteria();
        // provider = locationManager.getBestProvider(criteria, false);
        // Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        // if (location != null) {
        // System.out.println("Provider " + provider + " has been selected.");
        // onLocationChanged(location);
        // }
        // this.layout = (RelativeLayout) findViewById(R.id.layoutToMove);

        tf = Typeface.createFromAsset(getAssets(), "arial.ttf");
        // sendb.setTypeface(tf);
        TextView ser = (TextView) findViewById(R.id.textView1);
        TextView ser1 = (TextView) findViewById(R.id.textVw2);
        TextView email_lab = (TextView) findViewById(R.id.textView2);
        TextView issue_lab = (TextView) findViewById(R.id.textView4);
        TextView comm_lab = (TextView) findViewById(R.id.textView41);
        TextView sp_lab = (TextView) findViewById(R.id.textView42);
        TextView roam_lab = (TextView) findViewById(R.id.textView4_roa);
        TextView loc_lab = (TextView) findViewById(R.id.textView11);
        TextView t1_num_ejb1 = (TextView) findViewById(R.id.textVw21);
        t1_num_ejb1.setTypeface(tf);
        send_b = (Button) findViewById(R.id.send_button_rep);
        cancel_b = (Button) findViewById(R.id.cancel_button);
        cancel_b.setTypeface(tf);
        send_b.setTypeface(tf);
        ser.setTypeface(tf);
        ser1.setTypeface(tf);
        email_lab.setTypeface(tf);
        issue_lab.setTypeface(tf);
        comm_lab.setTypeface(tf);
        sp_lab.setTypeface(tf);
        roam_lab.setTypeface(tf);
        loc_lab.setTypeface(tf);
        roamsp = (EditText) findViewById(R.id.spinRoam);
        a = (EditText) findViewById(R.id.spinCountry);
        a.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                searchCitiesList();
            }
        });/*
			 * new Thread(new Runnable() { public void run(){
			 * 
			 * isquot =isquotareached(reg_id); isbloc = isblocked(num);
			 * 
			 * 
			 * } }).start();
			 */
        // a.getLayoutParams().width = width;
        bundle = this.getIntent().getExtras();
        b = (EditText) findViewById(R.id.spinSP);
        int mncnum = 0;
        try {
            mncnum = Integer.parseInt(getmnc());
        } catch (Exception e) {
            mncnum = 0;
        }
        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                searchCitiesList_sp();
            }
        });
        comm_edit = (EditText) findViewById(R.id.comment_edit);
        // final Button butSend = (Button) findViewById(R.id.send_button);
        num1 = (EditText) findViewById(R.id.fil_num_edit);
        this.layout = (RelativeLayout) findViewById(R.id.layoutToMove);
        email1 = (EditText) findViewById(R.id.fill_email_edit);

        listMenu = (ListView) findViewById(R.id.listMenu1);

		/*
		 * final String [] values = get_values();//{"hii","hii1","hii2"};
		 * ArrayAdapter<String> adapter = new
		 * ArrayAdapter<String>(this,R.layout.aligned_right, values);
		 * this.eventList.setAdapter(adapter);
		 */
        this.listMenu.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                if (position == 0 || position == 10) {

                } else {
                    Intent intent = new Intent(getApplicationContext(),
                            reportActivity.class);
                    // use Bundle to send store data to be send to
                    // StockListActivity
                    Bundle bundle = new Bundle();
                    Event[] a = get_values_all();
                    Event event = null;// = a[position-1];
                    if (position > 9) {
                        event = a[position - 2];
                    } else {
                        event = a[position - 1];
                    }

                    // Event event=(Event)listMenu.getItemAtPosition(position);
                    bundle.putInt("eventId", event.getId());
                    bundle.putString("eventName", event.getName());
                    bundle.putString("eventDescription", event.getDescription());
                    bundle.putString("eventDate", event.getDate());
                    bundle.putString("caller", "eventslist");
                    bundle.putString("eventLocation", event.getLocation());
                    bundle.putString("activity", "EventsListActivity");
                    // adding the bundle to the intent
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        issus = bundle.getInt("eventId");
        sp = bundle.getString("eventDate");
        locmand = bundle.getString("eventDescription");
        isroam = bundle.getString("eventLocation");
        String issuename = bundle.getString("eventName");
        String[] sp_tab = get_spinner2();
        // mncnum=2;
        if (mncnum == 1) {
            b.setText(sp_tab[1]);
        } else if (mncnum == 2) {
            b.setText(sp_tab[0]);
        }
        if (isroam.equals("true")) {
            TextView aroa = (TextView) findViewById(R.id.textView4_roa);
            aroa.setVisibility(View.VISIBLE);

            roamsp.setVisibility(View.VISIBLE);
            String[] fill1 = get_spinner3();

            // roamsp.setText(fill1[0]);
            roamsp.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    searchCitiesList_roaming();
                }
            });
            // roamsp.setAdapter(dataAdapter);
        }
        // a.setAdapter(null);
        String[] fill1 = get_spinner1("" + issus);
        // a.setText(fill1[0]);
        pos = 0;
        // a.setAdapter(dataAdapter);
        String[] fill2 = get_spinner2();
        // b.setText(fill2[0]);
        pos1 = 0;
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, fill2);
        dataAdapter1
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // b.setAdapter(dataAdapter1);

        new Thread(new Task()).start();

        listMenu = (ListView) findViewById(R.id.listMenu1);

        ViewResizing.setPageTextResizing(this);
        // initializeMap();
    }

    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }

    public void backTo(View v) {
        Actions.backTo(this);
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
            TextView loc_lab = (TextView) findViewById(R.id.textView11);
            loc_lab.setVisibility(View.VISIBLE);
            loca = (TextView) findViewById(R.id.locattion_date);
            loca.setText(getcountrycode1());
            if (waitingForLocationUpdate) {
                // getNearbyStores();
                waitingForLocationUpdate = false;
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

    public void searchCitiesList() {
        final Dialog dialog = new Dialog(reportActivity.this);
        dialog.setContentView(R.layout.cities_listview);
        // dialog.setTitle("Select Issue type");
        final ListView listView = (ListView) dialog.findViewById(R.id.list);
        String[] values = get_spinner1("" + issus);
        dialog.show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.aligned_right, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView
                        .getItemAtPosition(position);
                a.setText(itemValue);
                pos = itemPosition;
                dialog.cancel();

            }

        });
    }

    public void searchCitiesList_sp() {
        final Dialog dialog = new Dialog(reportActivity.this);
        dialog.setContentView(R.layout.cities_listview);
        // dialog.setTitle("Select Service Provider");
        final ListView listView = (ListView) dialog.findViewById(R.id.list);
        String[] values = get_spinner2();
        dialog.show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.aligned_right, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView
                        .getItemAtPosition(position);
                b.setText(itemValue);
                pos1 = itemPosition;
                // Show Alert
                // Toast.makeText(
                // getApplicationContext(),
                // // "Position :" + itemPosition + "  ListItem : "
                // + itemValue, Toast.LENGTH_LONG).show();
                dialog.cancel();

            }

        });
    }

    public void searchCitiesList_roaming() {
        final Dialog dialog = new Dialog(reportActivity.this);
        dialog.setContentView(R.layout.cities_listview);
        // dialog.setTitle("Select Country");
        final ListView listView = (ListView) dialog.findViewById(R.id.list);
        String[] values = get_spinner3();
        dialog.show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.aligned_right, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView
                        .getItemAtPosition(position);
                roamsp.setText(itemValue);
                // Show Alert
                // Toast.makeText(
                // getApplicationContext(),
                // // "Position :" + itemPosition + "  ListItem : "
                // + itemValue, Toast.LENGTH_LONG).show();
                dialog.cancel();

            }

        });
    }

    class Task implements Runnable {

        @Override
        public void run() {

            TCTDbAdapter source = new TCTDbAdapter(reportActivity.this);
            source.open();
            ArrayList<Profile> arr = source.getAllProfiles();
            num1.setText(arr.get(0).getnum());
            email1.setText(arr.get(0).getemail());
            reg_id = arr.get(0).getId();
            gettok = gettoken();
            qatarID = arr.get(0).getqatarID();
            num = arr.get(0).getnum();
            email = arr.get(0).getemail();
            url = "" + app.link + "PostData.asmx/SendReport";
            conn = new Connection(url);
            String s;

            send_b.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    double a = Double.parseDouble(num1.getText().toString());
                    if (num1.getText().toString().equals("")) {
                        Actions.onCreateDialog1(reportActivity.this,
                                "Number could not be empty!");
                    } else if (!RegisterActivity.isEmailValid(email1.getText()
                            .toString())) {
                        Actions.onCreateDialog1(reportActivity.this,
                                "Invalid Email format!");
                    } else if (!(num1.getText().length() == 8)) {
                        Actions.onCreateDialog1(reportActivity.this,
                                "The Mobile Number must be 8 digits!");
                    } else {
                        sendRequest();
                    }
                }
            });

            cancel_b.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(reportActivity.this,
                            HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            source.close();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // locationManager.requestLocationUpdates(provider, 400, 0, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        // if(!firstRun){
        double lat = (location.getLatitude());
        double lng = (location.getLongitude());
        latitude = lat;// String.valueOf(lat));
        longitude = lng;// .setText(String.valueOf(lng));
        TextView loc_lab = (TextView) findViewById(R.id.textView11);
        loc_lab.setVisibility(View.VISIBLE);
        loca = (TextView) findViewById(R.id.locattion_date);
        loca.setText(getcountrycode1());
        // }else{
        // firstRun=false;
        // }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    public void sendRequest() {
        final Connection conn;

        // check if error in edit texts exist
        // case no error, process request
        // get parameters name value pairs
        // String[][] parameters = formatParameters();
        // encode parameters before adding to URL then create new
        // connection
        // Looper.prepare();
        try {
            int seca = pos;// a.getSelectedItemPosition();
            issueID = aspin1[seca];
            final String spname = b.getText().toString();// b.getSelectedItem().toString();
            int spb = pos1;// b.getSelectedItemPosition();
            spID = bspin2[spb];
            conn = new Connection(url);
            // Log.d("url=", url);
            // conn = new Connection(formatParameters());
            if (conn.getError().getState()) {// error in URL format
                Log.d("passing here", "error in url");
                // display error message

            } else {// URL successfully created
                if ((latitude == 0.0 || longitude == 0.0)
                        && locmand.equals("true")) {

                    boolean gps_enabled = locationManager
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (gps_enabled) {

                        Actions.onCreateDialog1(reportActivity.this,
                                "Please wait to determine your location..");

                    } else {

                        Actions.onCreateDialog1(reportActivity.this,
                                "Turn On Location Services to Allow ICT Qatar to Determine Your Location");
                    }
                } else if (isroam.equals("true")
                        & roamsp.getText().toString().equals("")) {
                    // if(roamsp.equals("")){
                    Actions.onCreateDialog1(reportActivity.this,
                            "Please select roaming country before you proceed");
                    // }
                } else {
                    if (a.getText().toString().equals("")) {
                        Actions.onCreateDialog1(reportActivity.this,
                                "Please select issue type before you proceed");

                    } else {
                        if (b.getText().toString().equals("")) {
                            Actions.onCreateDialog1(reportActivity.this,
                                    "Please select service provider before you proceed");

                        } else {

                            try {
                                if (!isblocked(num).equals("-1")) {
                                    if (!isblocked(num).equals("0")) {
                                        Actions.onCreateDialog1(
                                                reportActivity.this,
                                                "Your Mobile Number has been blocked by the administrator.");
                                    } else {
                                        if (!isquotareached(reg_id).equals("0")) {
                                            Actions.onCreateDialog1(
                                                    reportActivity.this,
                                                    "You have reached the maximum of your quota for today!");
                                        } else {
                                            if (num1.getText().length() != 8) {

                                            } else {
                                                try {
                                                    loc = getcountryid(roamsp
                                                            .getText()
                                                            .toString());
                                                } catch (Exception e) {
                                                    loc = "165";
                                                }
                                                String aloc;
                                                try {
                                                    aloc = loca.getText()
                                                            .toString();

                                                } catch (Exception e) {
                                                    aloc = "";
                                                }
                                                final String aloc1 = aloc;
                                                String mm = num1.getText()
                                                        .toString();
                                                if (!num.equals(num1.getText()
                                                        .toString())) {// hereee

                                                    // onCreateDialogaff1(reportActivity.this,"Please Enter your affected Qatari ID");
                                                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                                                            reportActivity.this);
                                                    TextView textView;
                                                    final EditText affnum;
                                                    // Get the layout inflater
                                                    LayoutInflater inflater = reportActivity.this
                                                            .getLayoutInflater();
                                                    final View textEntryView = inflater
                                                            .inflate(
                                                                    R.layout.dialog_layout1,
                                                                    null);
                                                    textView = (TextView) textEntryView
                                                            .findViewById(R.id.dialogMsg);
                                                    textView.setText("Enter the Qatari ID of the new number");
                                                    affnum = (EditText) textEntryView
                                                            .findViewById(R.id.numaff);

                                                    final int k1 = 0;
                                                    // final int k=0;;
                                                    // Inflate and set the
                                                    // layout for the dialog
                                                    // Pass null as the parent
                                                    // view because its going in
                                                    // the dialog layout
                                                    builder.setView(
                                                            textEntryView)
                                                            // Add action
                                                            // buttons
                                                            .setNegativeButton(
                                                                    "ok",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(
                                                                                DialogInterface dialog,
                                                                                int id) {
                                                                            try {
                                                                                double a11 = Double
                                                                                        .parseDouble(affnum
                                                                                                .getText()
                                                                                                .toString());

                                                                                if (affnum
                                                                                        .getText()
                                                                                        .length() != 11) {

                                                                                    onCreateDialog1_aff(
                                                                                            reportActivity.this,
                                                                                            "The qatari ID should be 11 number",
                                                                                            gettok,
                                                                                            num1.getText()
                                                                                                    .toString(),
                                                                                            num,
                                                                                            email1.getText()
                                                                                                    .toString(),
                                                                                            affnum.getText()
                                                                                                    .toString(),
                                                                                            comm_edit
                                                                                                    .getText()
                                                                                                    .toString(),
                                                                                            getcurrentDate(),
                                                                                            a.getText()
                                                                                                    .toString(),
                                                                                            b.getText()
                                                                                                    .toString(),
                                                                                            issueID,
                                                                                            spID,
                                                                                            getcurrentDate(),
                                                                                            getcurrentDate(),
                                                                                            ""
                                                                                                    + latitude,
                                                                                            ""
                                                                                                    + longitude,
                                                                                            qatarID,
                                                                                            aloc1,
                                                                                            loc,
                                                                                            reg_id,
                                                                                            getcurrentDate_aff());// (reportActivity.this,
                                                                                    // "The qatari ID should be 11 number");

                                                                                } else {

                                                                                    Intent intent = new Intent(
                                                                                            reportActivity.this,
                                                                                            SendReportActivity.class);
                                                                                    Bundle bundle = new Bundle();
                                                                                    bundle.putString(
                                                                                            "gettok",
                                                                                            gettok);
                                                                                    bundle.putString(
                                                                                            "mobilenum",
                                                                                            num1.getText()
                                                                                                    .toString());
                                                                                    bundle.putString(
                                                                                            "affmobilenum",
                                                                                            num);
                                                                                    bundle.putString(
                                                                                            "email",
                                                                                            email1.getText()
                                                                                                    .toString());
                                                                                    bundle.putString(
                                                                                            "qatariID",
                                                                                            b.getText()
                                                                                                    .toString());
                                                                                    bundle.putString(
                                                                                            "comments",
                                                                                            comm_edit
                                                                                                    .getText()
                                                                                                    .toString());
                                                                                    bundle.putString(
                                                                                            "date",
                                                                                            getcurrentDate());
                                                                                    bundle.putString(
                                                                                            "issue",
                                                                                            affnum.getText()
                                                                                                    .toString());
                                                                                    bundle.putString(
                                                                                            "sp",
                                                                                            a.getText()
                                                                                                    .toString());
                                                                                    bundle.putString(
                                                                                            "issueid",
                                                                                            issueID);
                                                                                    bundle.putString(
                                                                                            "spid",
                                                                                            spID);
                                                                                    bundle.putString(
                                                                                            "creationdate",
                                                                                            getcurrentDate());
                                                                                    bundle.putString(
                                                                                            "sendingdate",
                                                                                            getcurrentDate());
                                                                                    bundle.putString(
                                                                                            "locx",
                                                                                            ""
                                                                                                    + latitude);
                                                                                    bundle.putString(
                                                                                            "locy",
                                                                                            ""
                                                                                                    + longitude);
                                                                                    bundle.putString(
                                                                                            "iniqatarid",
                                                                                            ""
                                                                                                    + qatarID);
                                                                                    bundle.putString(
                                                                                            "locat",
                                                                                            aloc1);
                                                                                    bundle.putString(
                                                                                            "countryid",
                                                                                            loc);
                                                                                    bundle.putString(
                                                                                            "password",
                                                                                            reg_id);
                                                                                    bundle.putString(
                                                                                            "date_aff",
                                                                                            getcurrentDate_aff());
                                                                                    bundle.putString(
                                                                                            "status",
                                                                                            "sended");
                                                                                    bundle.putString(
                                                                                            "roam_country",
                                                                                            roamsp.getText()
                                                                                                    .toString());
                                                                                    // adding
                                                                                    // the
                                                                                    // bundle
                                                                                    // to
                                                                                    // the
                                                                                    // intent
                                                                                    intent.putExtras(bundle);
                                                                                    startActivity(intent);

                                                                                }
                                                                            } catch (Exception e) {
                                                                                Actions.onCreateDialog1(
                                                                                        reportActivity.this,
                                                                                        "The qatari ID should be 11 number");

                                                                            }
                                                                        }
                                                                    });
                                                    builder.setView(
                                                            textEntryView)
                                                            // Add action
                                                            // buttons
                                                            .setPositiveButton(
                                                                    "cancel",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(
                                                                                DialogInterface dialog,
                                                                                int id) {
                                                                            dialog.dismiss();
                                                                        }
                                                                    });
                                                    Dialog d = builder.create();
                                                    d.getWindow()
                                                            .setLayout(
                                                                    (int) (reportActivity.this
                                                                            .getWindow()
                                                                            .peekDecorView()
                                                                            .getWidth() * 0.9),
                                                                    (int) (reportActivity.this
                                                                            .getWindow()
                                                                            .peekDecorView()
                                                                            .getHeight() * 0.9));

                                                    d.show();

                                                } else {

                                                    Intent intent = new Intent(
                                                            reportActivity.this,
                                                            SendReportActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("gettok",
                                                            gettok);
                                                    bundle.putString(
                                                            "mobilenum", num1
                                                                    .getText()
                                                                    .toString());
                                                    bundle.putString(
                                                            "affmobilenum", num);
                                                    bundle.putString("email",
                                                            email1.getText()
                                                                    .toString());
                                                    bundle.putString(
                                                            "qatariID", b
                                                                    .getText()
                                                                    .toString());
                                                    bundle.putString(
                                                            "comments",
                                                            comm_edit.getText()
                                                                    .toString());
                                                    bundle.putString("date",
                                                            getcurrentDate());
                                                    bundle.putString("issue",
                                                            qatarID);
                                                    bundle.putString("sp", a
                                                            .getText()
                                                            .toString());
                                                    bundle.putString("issueid",
                                                            issueID);
                                                    bundle.putString("spid",
                                                            spID);
                                                    bundle.putString(
                                                            "creationdate",
                                                            getcurrentDate());
                                                    bundle.putString(
                                                            "sendingdate",
                                                            getcurrentDate());
                                                    bundle.putString("locx", ""
                                                            + latitude);
                                                    bundle.putString("locy", ""
                                                            + longitude);
                                                    bundle.putString(
                                                            "iniqatarid", ""
                                                                    + qatarID);
                                                    bundle.putString("locat",
                                                            aloc1);
                                                    bundle.putString(
                                                            "countryid", loc);
                                                    bundle.putString(
                                                            "password", reg_id);
                                                    bundle.putString(
                                                            "date_aff",
                                                            getcurrentDate_aff());
                                                    bundle.putString("status",
                                                            "sended");
                                                    bundle.putString(
                                                            "roam_country",
                                                            roamsp.getText()
                                                                    .toString());
                                                    // adding the bundle to the
                                                    // intent
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);

                                                }
                                            }
                                        }
                                    }
                                } else {

                                    if (!num.equals(num1.getText().toString())) {

                                        // onCreateDialogaff1(reportActivity.this,"Please Enter your affected Qatari ID");
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(
                                                reportActivity.this);
                                        TextView textView;
                                        final EditText affnum;
                                        // Get the layout inflater
                                        LayoutInflater inflater = reportActivity.this
                                                .getLayoutInflater();
                                        final View textEntryView = inflater
                                                .inflate(
                                                        R.layout.dialog_layout1,
                                                        null);
                                        textView = (TextView) textEntryView
                                                .findViewById(R.id.dialogMsg);
                                        textView.setText("Enter the Qatari ID of the new number");
                                        affnum = (EditText) textEntryView
                                                .findViewById(R.id.numaff);

                                        final int k1 = 0;
                                        // final int k=0;;
                                        // Inflate and set the layout for the
                                        // dialog
                                        // Pass null as the parent view because
                                        // its going in the dialog layout
                                        builder.setView(textEntryView)
                                                // Add action buttons
                                                .setNegativeButton(
                                                        "ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(
                                                                    DialogInterface dialog,
                                                                    int id) {
                                                                try {
                                                                    double a11 = Double
                                                                            .parseDouble(affnum
                                                                                    .getText()
                                                                                    .toString());
                                                                    String aloc;
                                                                    try {
                                                                        aloc = loca
                                                                                .getText()
                                                                                .toString();

                                                                    } catch (Exception e) {
                                                                        aloc = "";
                                                                    }
                                                                    final String aloc1 = aloc;
                                                                    if (affnum
                                                                            .getText()
                                                                            .length() != 11) {

                                                                        onCreateDialog1_aff_off(
                                                                                reportActivity.this,
                                                                                " The qatari ID should be 11 number",
                                                                                gettok,
                                                                                num1.getText()
                                                                                        .toString(),
                                                                                num,
                                                                                email1.getText()
                                                                                        .toString(),
                                                                                b.getText()
                                                                                        .toString(),
                                                                                comm_edit
                                                                                        .getText()
                                                                                        .toString(),
                                                                                getcurrentDate(),
                                                                                affnum.getText()
                                                                                        .toString(),
                                                                                a.getText()
                                                                                        .toString(),
                                                                                issueID,
                                                                                spID,
                                                                                getcurrentDate(),
                                                                                getcurrentDate(),
                                                                                ""
                                                                                        + latitude,
                                                                                ""
                                                                                        + longitude,
                                                                                qatarID,
                                                                                aloc1,
                                                                                loc,
                                                                                reg_id,
                                                                                getcurrentDate_aff());// (reportActivity.this,
                                                                        // "The qatari ID should be 11 number");

                                                                    } else {

                                                                        TCTDbAdapter datasource = new TCTDbAdapter(
                                                                                reportActivity.this);
                                                                        datasource
                                                                                .open();
                                                                        try {

                                                                            long ln = datasource
                                                                                    .createReports_off(
                                                                                            reg_id,
                                                                                            num,
                                                                                            email,
                                                                                            qatarID,
                                                                                            "Arabic",
                                                                                            comm_edit
                                                                                                    .getText()
                                                                                                    .toString(),
                                                                                            loca.getText()
                                                                                                    .toString(),
                                                                                            getcurrentDate(),
                                                                                            getcurrentDate_aff(),
                                                                                            a.getText()
                                                                                                    .toString(),
                                                                                            "Failed",
                                                                                            issueID,
                                                                                            spID,
                                                                                            ""
                                                                                                    + latitude,
                                                                                            ""
                                                                                                    + longitude,
                                                                                            loc,
                                                                                            qatarID,
                                                                                            spname);

                                                                            if (!(ln > -1)) {// case
                                                                                // row
                                                                                // not
                                                                                // inserted,
                                                                                // may
                                                                                // be
                                                                                // duplicated
                                                                                throw new Exception();
                                                                            }
                                                                            ArrayList<Mail_OFF> arr = datasource
                                                                                    .getLastReports_off();

                                                                            Intent intent = new Intent(
                                                                                    reportActivity.this,
                                                                                    FailedReportActivity.class);
                                                                            Bundle bundle = new Bundle();
                                                                            bundle.putString(
                                                                                    "mobilenum",
                                                                                    num1.getText()
                                                                                            .toString());
                                                                            bundle.putString(
                                                                                    "email",
                                                                                    email1.getText()
                                                                                            .toString());
                                                                            bundle.putString(
                                                                                    "qatariID",
                                                                                    b.getText()
                                                                                            .toString());
                                                                            bundle.putString(
                                                                                    "comments",
                                                                                    comm_edit
                                                                                            .getText()
                                                                                            .toString());
                                                                            bundle.putString(
                                                                                    "date",
                                                                                    getcurrentDate());
                                                                            bundle.putString(
                                                                                    "issue",
                                                                                    affnum.getText()
                                                                                            .toString());
                                                                            bundle.putString(
                                                                                    "sp",
                                                                                    a.getText()
                                                                                            .toString());
                                                                            bundle.putString(
                                                                                    "locx",
                                                                                    ""
                                                                                            + latitude);
                                                                            bundle.putString(
                                                                                    "locy",
                                                                                    ""
                                                                                            + longitude);
                                                                            bundle.putString(
                                                                                    "countid",
                                                                                    loc);
                                                                            bundle.putString(
                                                                                    "status",
                                                                                    reg_id);
                                                                            bundle.putInt(
                                                                                    "idrep",
                                                                                    arr.get(0)
                                                                                            .getid_rep());
                                                                            bundle.putString(
                                                                                    "roam_country",
                                                                                    roamsp.getText()
                                                                                            .toString());
                                                                            // adding
                                                                            // the
                                                                            // bundle
                                                                            // to
                                                                            // the
                                                                            // intent
                                                                            intent.putExtras(bundle);
                                                                            startActivity(intent);
                                                                            // case
                                                                            // saved
                                                                            // Actions.onCreateDialog(this,
                                                                            // "Event saved successfully.",false);

                                                                        } catch (Exception e1) {// case
                                                                            // not
                                                                            // saved
                                                                            // Actions.onCreateDialog(this,
                                                                            // "Event had been previously saved.",false)
                                                                            // ;
                                                                            // Actions.onCreateDialog(this,
                                                                            // "Event saved successfully.",false);
                                                                            System.out
                                                                                    .println("eee "
                                                                                            + e1);
                                                                            ArrayList<Mail_OFF> arr = datasource
                                                                                    .getLastReports_off();

                                                                            Intent intent = new Intent(
                                                                                    reportActivity.this,
                                                                                    FailedReportActivity.class);
                                                                            Bundle bundle = new Bundle();
                                                                            bundle.putString(
                                                                                    "mobilenum",
                                                                                    num1.getText()
                                                                                            .toString());
                                                                            bundle.putString(
                                                                                    "email",
                                                                                    email1.getText()
                                                                                            .toString());
                                                                            bundle.putString(
                                                                                    "qatariID",
                                                                                    b.getText()
                                                                                            .toString());
                                                                            bundle.putString(
                                                                                    "comments",
                                                                                    comm_edit
                                                                                            .getText()
                                                                                            .toString());
                                                                            bundle.putString(
                                                                                    "date",
                                                                                    getcurrentDate());
                                                                            bundle.putString(
                                                                                    "issue",
                                                                                    qatarID);
                                                                            bundle.putString(
                                                                                    "sp",
                                                                                    a.getText()
                                                                                            .toString());
                                                                            bundle.putString(
                                                                                    "locx",
                                                                                    ""
                                                                                            + latitude);
                                                                            bundle.putString(
                                                                                    "locy",
                                                                                    ""
                                                                                            + longitude);
                                                                            bundle.putString(
                                                                                    "countid",
                                                                                    loc);
                                                                            bundle.putString(
                                                                                    "status",
                                                                                    reg_id);
                                                                            bundle.putString(
                                                                                    "roam_country",
                                                                                    roamsp.getText()
                                                                                            .toString());
                                                                            // bundle.putInt("idrep",
                                                                            // arr.get(0).getid_rep());
                                                                            // adding
                                                                            // the
                                                                            // bundle
                                                                            // to
                                                                            // the
                                                                            // intent
                                                                            intent.putExtras(bundle);
                                                                            startActivity(intent);
                                                                        }
                                                                        datasource
                                                                                .close();

                                                                    }
                                                                } catch (Exception e) {
                                                                    Actions.onCreateDialog1(
                                                                            reportActivity.this,
                                                                            "The qatari ID should be 11 number");

                                                                }
                                                            }
                                                        });

                                        builder.setView(textEntryView)
                                                // Add action buttons
                                                .setPositiveButton(
                                                        "cancel",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(
                                                                    DialogInterface dialog,
                                                                    int id) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                        Dialog d = builder.create();
                                        d.getWindow().setLayout(
                                                (int) (reportActivity.this
                                                        .getWindow()
                                                        .peekDecorView()
                                                        .getWidth() * 0.9),
                                                (int) (reportActivity.this
                                                        .getWindow()
                                                        .peekDecorView()
                                                        .getHeight() * 0.9));

                                        d.show();

                                    } else {

                                        TCTDbAdapter datasource = new TCTDbAdapter(
                                                reportActivity.this);
                                        datasource.open();
                                        String loic = "";
                                        try {
                                            // String loc="";
                                            try {
                                                // loc=getcountryid(getcountrycode());
                                                loic = loca.getText()
                                                        .toString();
                                            } catch (Exception e12) {
                                                // loc="111";
                                                loic = "";
                                            }
                                            long ln = datasource
                                                    .createReports_off(
                                                            reg_id,
                                                            num,
                                                            email,
                                                            qatarID,
                                                            "English",
                                                            comm_edit.getText()
                                                                    .toString(),
                                                            loic,
                                                            getcurrentDate(),
                                                            getcurrentDate_aff(),
                                                            a.getText()
                                                                    .toString(),
                                                            "Failed", issueID,
                                                            spID,
                                                            "" + latitude,
                                                            "" + longitude,
                                                            loc, qatarID,
                                                            spname);
                                            //
                                            if (!(ln > -1)) {// case row not
                                                // inserted, may
                                                // be duplicated
                                                // throw new Exception();
                                            }
                                            ArrayList<Mail_OFF> arr = datasource
                                                    .getLastReports_off();

                                            Intent intent = new Intent(
                                                    reportActivity.this,
                                                    FailedReportActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("mobilenum", num1
                                                    .getText().toString());
                                            bundle.putString("email", email1
                                                    .getText().toString());
                                            bundle.putString("qatariID", b
                                                    .getText().toString());
                                            bundle.putString("comments",
                                                    comm_edit.getText()
                                                            .toString());
                                            bundle.putString("date",
                                                    getcurrentDate());
                                            bundle.putString("issue", qatarID);
                                            bundle.putString("sp", a.getText()
                                                    .toString());
                                            bundle.putString("locx", ""
                                                    + latitude);
                                            bundle.putString("locy", ""
                                                    + longitude);
                                            bundle.putString("countid", loc);
                                            bundle.putString("status", reg_id);
                                            bundle.putInt("idrep", arr.get(0)
                                                    .getid_rep());
                                            bundle.putString("roam_country",
                                                    roamsp.getText().toString());
                                            // adding the bundle to the intent
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            // case savefd
                                            // Actions.onCreateDialog(this,
                                            // "Event saved successfully.",false);

                                        } catch (Exception e1) {// case not
                                            // saved
                                            // Actions.onCreateDialog(this,
                                            // "Event had been previously saved.",false)
                                            // ;
                                            // Actions.onCreateDialog(this,
                                            // "Event saved successfully.",false);
                                            System.out.println("eee " + e1);
                                            ArrayList<Mail_OFF> arr = datasource
                                                    .getLastReports_off();

                                            Intent intent = new Intent(
                                                    reportActivity.this,
                                                    FailedReportActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("mobilenum", num1
                                                    .getText().toString());
                                            bundle.putString("email", email1
                                                    .getText().toString());
                                            bundle.putString("qatariID", b
                                                    .getText().toString());
                                            bundle.putString("comments",
                                                    comm_edit.getText()
                                                            .toString());
                                            bundle.putString("date",
                                                    getcurrentDate());
                                            bundle.putString("issue", qatarID);
                                            bundle.putString("sp", a.getText()
                                                    .toString());
                                            bundle.putString("locx", ""
                                                    + latitude);
                                            bundle.putString("locy", ""
                                                    + longitude);
                                            bundle.putString("countid", loc);
                                            bundle.putString("status", reg_id);
                                            bundle.putInt("idrep", arr.get(0)
                                                    .getid_rep());
                                            bundle.putString("roam_country",
                                                    roamsp.getText().toString());
                                            // adding the bundle to the intent
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                        datasource.close();
                                    }
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                if (!num.equals(num1.getText().toString())) {

                                    // onCreateDialogaff1(reportActivity.this,"Please Enter your affected Qatari ID");
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                                            reportActivity.this);
                                    TextView textView;
                                    final EditText affnum;
                                    // Get the layout inflater
                                    LayoutInflater inflater = reportActivity.this
                                            .getLayoutInflater();
                                    final View textEntryView = inflater
                                            .inflate(R.layout.dialog_layout1,
                                                    null);
                                    textView = (TextView) textEntryView
                                            .findViewById(R.id.dialogMsg);
                                    textView.setText("Enter the Qatari ID of the new number");
                                    affnum = (EditText) textEntryView
                                            .findViewById(R.id.numaff);

                                    final int k1 = 0;
                                    // final int k=0;;
                                    // Inflate and set the layout for the dialog
                                    // Pass null as the parent view because its
                                    // going in the dialog layout
                                    builder.setView(textEntryView)
                                            // Add action buttons
                                            .setNegativeButton(
                                                    "ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {
                                                            try {
                                                                double a11 = Double
                                                                        .parseDouble(affnum
                                                                                .getText()
                                                                                .toString());
                                                                String aloc;
                                                                try {
                                                                    aloc = loca
                                                                            .getText()
                                                                            .toString();

                                                                } catch (Exception e) {
                                                                    aloc = "";
                                                                }
                                                                final String aloc1 = aloc;
                                                                if (affnum
                                                                        .getText()
                                                                        .length() != 11) {

                                                                    onCreateDialog1_aff_off(
                                                                            reportActivity.this,
                                                                            "The qatari ID should be 11 number ",
                                                                            gettok,
                                                                            num1.getText()
                                                                                    .toString(),
                                                                            num,
                                                                            email1.getText()
                                                                                    .toString(),
                                                                            b.getText()
                                                                                    .toString(),
                                                                            comm_edit
                                                                                    .getText()
                                                                                    .toString(),
                                                                            getcurrentDate(),
                                                                            affnum.getText()
                                                                                    .toString(),
                                                                            a.getText()
                                                                                    .toString(),
                                                                            issueID,
                                                                            spID,
                                                                            getcurrentDate(),
                                                                            getcurrentDate(),
                                                                            ""
                                                                                    + latitude,
                                                                            ""
                                                                                    + longitude,
                                                                            qatarID,
                                                                            aloc1,
                                                                            loc,
                                                                            reg_id,
                                                                            getcurrentDate_aff());// (reportActivity.this,
                                                                    // "The qatari ID should be 11 number");

                                                                } else {

                                                                    TCTDbAdapter datasource = new TCTDbAdapter(
                                                                            reportActivity.this);
                                                                    datasource
                                                                            .open();
                                                                    try {

                                                                        long ln = datasource
                                                                                .createReports_off(
                                                                                        reg_id,
                                                                                        num,
                                                                                        email,
                                                                                        qatarID,
                                                                                        "Arabic",
                                                                                        comm_edit
                                                                                                .getText()
                                                                                                .toString(),
                                                                                        loca.getText()
                                                                                                .toString(),
                                                                                        getcurrentDate(),
                                                                                        getcurrentDate_aff(),
                                                                                        a.getText()
                                                                                                .toString(),
                                                                                        "Failed",
                                                                                        issueID,
                                                                                        spID,
                                                                                        ""
                                                                                                + latitude,
                                                                                        ""
                                                                                                + longitude,
                                                                                        loc,
                                                                                        qatarID,
                                                                                        spname);

                                                                        if (!(ln > -1)) {// case
                                                                            // row
                                                                            // not
                                                                            // inserted,
                                                                            // may
                                                                            // be
                                                                            // duplicated
                                                                            throw new Exception();
                                                                        }
                                                                        ArrayList<Mail_OFF> arr = datasource
                                                                                .getLastReports_off();

                                                                        Intent intent = new Intent(
                                                                                reportActivity.this,
                                                                                FailedReportActivity.class);
                                                                        Bundle bundle = new Bundle();
                                                                        bundle.putString(
                                                                                "mobilenum",
                                                                                num1.getText()
                                                                                        .toString());
                                                                        bundle.putString(
                                                                                "email",
                                                                                email1.getText()
                                                                                        .toString());
                                                                        bundle.putString(
                                                                                "qatariID",
                                                                                b.getText()
                                                                                        .toString());
                                                                        bundle.putString(
                                                                                "comments",
                                                                                comm_edit
                                                                                        .getText()
                                                                                        .toString());
                                                                        bundle.putString(
                                                                                "date",
                                                                                getcurrentDate());
                                                                        bundle.putString(
                                                                                "issue",
                                                                                affnum.getText()
                                                                                        .toString());
                                                                        bundle.putString(
                                                                                "sp",
                                                                                a.getText()
                                                                                        .toString());
                                                                        bundle.putString(
                                                                                "locx",
                                                                                ""
                                                                                        + latitude);
                                                                        bundle.putString(
                                                                                "locy",
                                                                                ""
                                                                                        + longitude);
                                                                        bundle.putString(
                                                                                "countid",
                                                                                loc);
                                                                        bundle.putString(
                                                                                "status",
                                                                                reg_id);
                                                                        bundle.putInt(
                                                                                "idrep",
                                                                                arr.get(0)
                                                                                        .getid_rep());
                                                                        bundle.putString(
                                                                                "roam_country",
                                                                                roamsp.getText()
                                                                                        .toString());
                                                                        // adding
                                                                        // the
                                                                        // bundle
                                                                        // to
                                                                        // the
                                                                        // intent
                                                                        intent.putExtras(bundle);
                                                                        startActivity(intent);
                                                                        // case
                                                                        // saved
                                                                        // Actions.onCreateDialog(this,
                                                                        // "Event saved successfully.",false);

                                                                    } catch (Exception e1) {// case
                                                                        // not
                                                                        // saved
                                                                        // Actions.onCreateDialog(this,
                                                                        // "Event had been previously saved.",false)
                                                                        // ;
                                                                        // Actions.onCreateDialog(this,
                                                                        // "Event saved successfully.",false);
                                                                        System.out
                                                                                .println("eee "
                                                                                        + e1);
                                                                        ArrayList<Mail_OFF> arr = datasource
                                                                                .getLastReports_off();

                                                                        Intent intent = new Intent(
                                                                                reportActivity.this,
                                                                                FailedReportActivity.class);
                                                                        Bundle bundle = new Bundle();
                                                                        bundle.putString(
                                                                                "mobilenum",
                                                                                num1.getText()
                                                                                        .toString());
                                                                        bundle.putString(
                                                                                "email",
                                                                                email1.getText()
                                                                                        .toString());
                                                                        bundle.putString(
                                                                                "qatariID",
                                                                                b.getText()
                                                                                        .toString());
                                                                        bundle.putString(
                                                                                "comments",
                                                                                comm_edit
                                                                                        .getText()
                                                                                        .toString());
                                                                        bundle.putString(
                                                                                "date",
                                                                                getcurrentDate());
                                                                        bundle.putString(
                                                                                "issue",
                                                                                qatarID);
                                                                        bundle.putString(
                                                                                "sp",
                                                                                a.getText()
                                                                                        .toString());
                                                                        bundle.putString(
                                                                                "locx",
                                                                                ""
                                                                                        + latitude);
                                                                        bundle.putString(
                                                                                "locy",
                                                                                ""
                                                                                        + longitude);
                                                                        bundle.putString(
                                                                                "countid",
                                                                                loc);
                                                                        bundle.putString(
                                                                                "status",
                                                                                reg_id);
                                                                        bundle.putString(
                                                                                "roam_country",
                                                                                roamsp.getText()
                                                                                        .toString());
                                                                        // bundle.putInt("idrep",
                                                                        // arr.get(0).getid_rep());
                                                                        // adding
                                                                        // the
                                                                        // bundle
                                                                        // to
                                                                        // the
                                                                        // intent
                                                                        intent.putExtras(bundle);
                                                                        startActivity(intent);
                                                                    }
                                                                    datasource
                                                                            .close();

                                                                }
                                                            } catch (Exception e) {
                                                                Actions.onCreateDialog1(
                                                                        reportActivity.this,
                                                                        "The qatari ID should be 11 number");

                                                            }
                                                        }
                                                    });

                                    builder.setView(textEntryView)
                                            // Add action buttons
                                            .setPositiveButton(
                                                    "cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                    Dialog d = builder.create();
                                    d.getWindow()
                                            .setLayout(
                                                    (int) (reportActivity.this
                                                            .getWindow()
                                                            .peekDecorView()
                                                            .getWidth() * 0.9),
                                                    (int) (reportActivity.this
                                                            .getWindow()
                                                            .peekDecorView()
                                                            .getHeight() * 0.9));

                                    d.show();

                                } else {
                                    TCTDbAdapter datasource = new TCTDbAdapter(
                                            reportActivity.this);
                                    datasource.open();
                                    try {

                                        long ln = datasource.createReports_off(
                                                reg_id, num, email, qatarID,
                                                "English", comm_edit.getText()
                                                        .toString(), loca
                                                        .getText().toString(),
                                                getcurrentDate(),
                                                getcurrentDate_aff(), a
                                                        .getText().toString(),
                                                "Failed", issueID, spID, ""
                                                        + latitude, ""
                                                        + longitude, loc,
                                                qatarID, spname);

                                        if (!(ln > -1)) {// case row not
                                            // inserted, may be
                                            // duplicated
                                            throw new Exception();
                                        }
                                        ArrayList<Mail_OFF> arr = datasource
                                                .getLastReports_off();

                                        Intent intent = new Intent(
                                                reportActivity.this,
                                                FailedReportActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("mobilenum", num1
                                                .getText().toString());
                                        bundle.putString("email", email1
                                                .getText().toString());
                                        bundle.putString("qatariID", b
                                                .getText().toString());
                                        bundle.putString("comments", comm_edit
                                                .getText().toString());
                                        bundle.putString("date",
                                                getcurrentDate());
                                        bundle.putString("issue", qatarID);
                                        bundle.putString("sp", a.getText()
                                                .toString());
                                        bundle.putString("locx", "" + latitude);
                                        bundle.putString("locy", "" + longitude);
                                        bundle.putString("countid", loc);
                                        bundle.putString("status", reg_id);
                                        bundle.putInt("idrep", arr.get(0)
                                                .getid_rep());
                                        bundle.putString("roam_country", roamsp
                                                .getText().toString());
                                        // adding the bundle to the intent
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        // case saved
                                        // Actions.onCreateDialog(this,
                                        // "Event saved successfully.",false);

                                    } catch (Exception e1) {// case not saved
                                        // Actions.onCreateDialog(this,
                                        // "Event had been previously saved.",false)
                                        // ;
                                        // Actions.onCreateDialog(this,
                                        // "Event saved successfully.",false);
                                        System.out.println("eee " + e1);
                                        ArrayList<Mail_OFF> arr = datasource
                                                .getLastReports_off();

                                        Intent intent = new Intent(
                                                reportActivity.this,
                                                FailedReportActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("mobilenum", num1
                                                .getText().toString());
                                        bundle.putString("email", email1
                                                .getText().toString());
                                        bundle.putString("qatariID", b
                                                .getText().toString());
                                        bundle.putString("comments", comm_edit
                                                .getText().toString());
                                        bundle.putString("date",
                                                getcurrentDate());
                                        bundle.putString("issue", qatarID);
                                        bundle.putString("sp", a.getText()
                                                .toString());
                                        bundle.putString("locx", "" + latitude);
                                        bundle.putString("locy", "" + longitude);
                                        bundle.putString("countid", loc);
                                        bundle.putString("status", reg_id);
                                        bundle.putString("roam_country", roamsp
                                                .getText().toString());
                                        // bundle.putInt("idrep",
                                        // arr.get(0).getid_rep());
                                        // adding the bundle to the intent
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                    datasource.close();
                                }
                            }
                        }
                    }
                }
            }
            // Log.d("result", s);

        } catch (Exception e) {
            String url1 = "" + app.link + "PostData.asmx/VerifyRegistration";
            Connection conn1 = new Connection(url1);

            try {
                String error_return = conn1.executeMultipartPost_Send_Error(
                        this.getClass().getSimpleName(),
                        Actions.getDeviceName(), "1", e.getMessage());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    public void dialog_aff(final Activity act1, final String msg,
                           final String gettok, final String mobnum, final String affmobnum,
                           final String email, final String qatarid, final String spid,
                           final String comm, final String date, final String issue,
                           final String sp, final String issueid, final String creatdate,
                           final String senddate, final String locx, final String locy,
                           final String iniqatarid, final String locat,
                           final String countryid, final String password, final String date_aff) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(act1);
        TextView textView;
        final EditText affnum;
        // Get the layout inflater
        LayoutInflater inflater = act1.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_layout1,
                null);
        textView = (TextView) textEntryView.findViewById(R.id.dialogMsg);
        textView.setText("Enter the Qatari ID of the new number");
        affnum = (EditText) textEntryView.findViewById(R.id.numaff);

        final int k1 = 0;
        // final int k=0;;
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(textEntryView)
                // Add action buttons
                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            double a11 = Double.parseDouble(affnum.getText()
                                    .toString());

                            if (affnum.getText().length() != 11) {

                                onCreateDialog1_aff(act1, msg, gettok, mobnum,
                                        affmobnum, email, qatarid, comm, date,
                                        issue, sp, issueid, spid, creatdate,
                                        senddate, locx, locy, iniqatarid,
                                        locat, countryid, password, date_aff);// (reportActivity.this,
                                // "The qatari ID should be 11 number");

                            } else {

                                Intent intent = new Intent(reportActivity.this,
                                        SendReportActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("gettok", gettok);
                                bundle.putString("mobilenum", num1.getText()
                                        .toString());
                                bundle.putString("affmobilenum", num);
                                bundle.putString("email", email1.getText()
                                        .toString());
                                bundle.putString("qatariID", b.getText()
                                        .toString());
                                bundle.putString("comments", comm_edit
                                        .getText().toString());
                                bundle.putString("date", getcurrentDate());
                                bundle.putString("issue", qatarID);
                                bundle.putString("sp", a.getText().toString());
                                bundle.putString("issueid", issueID);
                                bundle.putString("spid", spID);
                                bundle.putString("creationdate",
                                        getcurrentDate());
                                bundle.putString("sendingdate",
                                        getcurrentDate());
                                bundle.putString("locx", "" + latitude);
                                bundle.putString("locy", "" + longitude);
                                bundle.putString("iniqatarid", "" + qatarID);
                                bundle.putString("locat", "");
                                bundle.putString("countryid", loc);
                                bundle.putString("password", reg_id);
                                bundle.putString("date_aff",
                                        getcurrentDate_aff());
                                bundle.putString("status", "sended");
                                bundle.putString("roam_country", roamsp
                                        .getText().toString());
                                // adding the bundle to the intent
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        } catch (Exception e) {
                            Actions.onCreateDialog1(reportActivity.this,
                                    "The qatari ID should be 11 number");

                        }
                    }
                });
        builder.setView(textEntryView)
                // Add action buttons
                .setPositiveButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        Dialog d = builder.create();
        d.getWindow().setLayout(
                (int) (reportActivity.this.getWindow().peekDecorView()
                        .getWidth() * 0.9),
                (int) (reportActivity.this.getWindow().peekDecorView()
                        .getHeight() * 0.9));

        d.show();
    }

    public void dialog_aff_off(final Activity act1, final String msg,
                               final String gettok, final String mobnum, final String affmobnum,
                               final String email, final String qatarid, final String spid,
                               final String comm, final String date, final String issue,
                               final String sp, final String issueid, final String creatdate,
                               final String senddate, final String locx, final String locy,
                               final String iniqatarid, final String locat,
                               final String countryid, final String password, final String date_aff) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(act1);
        TextView textView;
        final EditText affnum;
        // Get the layout inflater
        LayoutInflater inflater = act1.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_layout1,
                null);
        textView = (TextView) textEntryView.findViewById(R.id.dialogMsg);
        textView.setText("Enter the Qatari ID of the new number");
        affnum = (EditText) textEntryView.findViewById(R.id.numaff);

        final int k1 = 0;
        // final int k=0;;
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(textEntryView)
                // Add action buttons
                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            double a11 = Double.parseDouble(affnum.getText()
                                    .toString());

                            if (affnum.getText().length() != 11) {

                                onCreateDialog1_aff_off(act1, msg, gettok,
                                        mobnum, affmobnum, email, qatarid,
                                        comm, date, issue, sp, issueid, spid,
                                        creatdate, senddate, locx, locy,
                                        iniqatarid, locat, countryid, password,
                                        date_aff);// (reportActivity.this,
                                // "The qatari ID should be 11 number");

                            } else {

                                Intent intent = new Intent(reportActivity.this,
                                        FailedReportActivity.class);
                                Bundle bundle = new Bundle();
                                TCTDbAdapter datasource = new TCTDbAdapter(
                                        reportActivity.this);
                                datasource.open();
                                try {
                                    final String spname = b.getText()
                                            .toString();
                                    long ln = datasource.createReports_off(
                                            reg_id, num, email, qatarID,
                                            "English", comm_edit.getText()
                                                    .toString(), loca.getText()
                                                    .toString(),
                                            getcurrentDate(),
                                            getcurrentDate_aff(), a.getText()
                                                    .toString(), "Failed",
                                            issueID, spID, "" + latitude, ""
                                                    + longitude, loc, qatarID,
                                            spname);

                                    if (!(ln > -1)) {// case row not inserted,
                                        // may be duplicated
                                        throw new Exception();
                                    }
                                } catch (Exception e) {

                                }
                                ArrayList<Mail_OFF> arr = datasource
                                        .getLastReports_off();

                                datasource.close();
                                bundle.putString("mobilenum", num1.getText()
                                        .toString());
                                bundle.putString("email", email1.getText()
                                        .toString());
                                bundle.putString("qatariID", b.getText()
                                        .toString());
                                bundle.putString("comments", comm_edit
                                        .getText().toString());
                                bundle.putString("date", getcurrentDate());
                                bundle.putString("issue", affnum.getText()
                                        .toString());
                                bundle.putString("sp", a.getText().toString());
                                bundle.putString("locx", "" + latitude);
                                bundle.putString("locy", "" + longitude);
                                bundle.putString("countid", loc);
                                bundle.putString("status", reg_id);
                                bundle.putInt("idrep", arr.get(0).getid_rep());
                                bundle.putString("roam_country", roamsp
                                        .getText().toString());
                                // adding the bundle to the intent
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        } catch (Exception e) {
                            Actions.onCreateDialog1(reportActivity.this,
                                    "The qatari ID should be 11 number");

                        }
                    }
                });
        builder.setView(textEntryView)
                // Add action buttons
                .setPositiveButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        Dialog d = builder.create();
        d.getWindow().setLayout(
                (int) (reportActivity.this.getWindow().peekDecorView()
                        .getWidth() * 0.9),
                (int) (reportActivity.this.getWindow().peekDecorView()
                        .getHeight() * 0.9));

        d.show();
    }

    public void onCreateDialog1_aff(final Activity activity, final String msg,
                                    final String gettok, final String mobnum, final String affmobnum,
                                    final String email, final String qatarid, final String comm,
                                    final String date, final String issue, final String sp,
                                    final String issueid, final String spid, final String creatdate,
                                    final String senddate, final String locx, final String locy,
                                    final String iniqatarid, final String locat,
                                    final String countryid, final String password, final String date_aff) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        TextView textView;
        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_layout,
                null);
        textView = (TextView) textEntryView.findViewById(R.id.dialogMsg);
        textView.setGravity(Gravity.CENTER);
        textView.setText(msg);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(textEntryView)
                // Add action buttons
                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog_aff(activity, msg, gettok, mobnum, affmobnum,
                                email, qatarid, comm, date, issue, sp, issueid,
                                spid, creatdate, senddate, locx, locy,
                                iniqatarid, locat, countryid, password,
                                date_aff);
                    }
                });
        Dialog d = builder.create();
        d.getWindow().setLayout(
                (int) (activity.getWindow().peekDecorView().getWidth() * 0.9),
                (int) (activity.getWindow().peekDecorView().getHeight() * 0.9));
        // WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        // lp.copyFrom(d.getWindow().getAttributes());
        // lp.width = WindowManager.LayoutParams.FILL_PARENT;
        // lp.height = WindowManager.LayoutParams.FILL_PARENT;
        d.show();
        // d.getWindow().setAttributes(lp);
    }

    public void onCreateDialog1_aff_off(final Activity activity,
                                        final String msg, final String gettok, final String mobnum,
                                        final String affmobnum, final String email, final String qatarid,
                                        final String comm, final String date, final String issue,
                                        final String sp, final String issueid, final String spid,
                                        final String creatdate, final String senddate, final String locx,
                                        final String locy, final String iniqatarid, final String locat,
                                        final String countryid, final String password, final String date_aff) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        TextView textView;
        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_layout,
                null);
        textView = (TextView) textEntryView.findViewById(R.id.dialogMsg);
        textView.setGravity(Gravity.CENTER);
        textView.setText(msg);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(textEntryView)
                // Add action buttons
                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog_aff_off(activity, msg, gettok, mobnum,
                                affmobnum, email, qatarid, comm, date, issue,
                                sp, issueid, spid, creatdate, senddate, locx,
                                locy, iniqatarid, locat, countryid, password,
                                date_aff);
                    }
                });
        Dialog d = builder.create();
        d.getWindow().setLayout(
                (int) (activity.getWindow().peekDecorView().getWidth() * 0.9),
                (int) (activity.getWindow().peekDecorView().getHeight() * 0.9));
        // WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        // lp.copyFrom(d.getWindow().getAttributes());
        // lp.width = WindowManager.LayoutParams.FILL_PARENT;
        // lp.height = WindowManager.LayoutParams.FILL_PARENT;
        d.show();
        // d.getWindow().setAttributes(lp);
    }

    public String getmnc() {
        String mnc = "";
        try {
            TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String networkOperator = tel.getNetworkOperator();

            if (networkOperator != null) {
                // int mcc = Integer.parseInt(networkOperator.substring(0, 3));
                mnc = networkOperator.substring(3);
            }
        } catch (Exception e) {
            mnc = "";
        }
        return mnc;
    }

    public static void onCreateDialogaff1(final Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        TextView textView;
        EditText affnum;
        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_layout1,
                null);
        textView = (TextView) textEntryView.findViewById(R.id.dialogMsg);
        textView.setText(msg);
        affnum = (EditText) textEntryView.findViewById(R.id.numaff);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(textEntryView)
                // Add action buttons
                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        Dialog d = builder.create();
        d.getWindow().setLayout(
                (int) (activity.getWindow().peekDecorView().getWidth() * 0.9),
                (int) (activity.getWindow().peekDecorView().getHeight() * 0.9));
        // WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        // lp.copyFrom(d.getWindow().getAttributes());
        // lp.width = WindowManager.LayoutParams.FILL_PARENT;
        // lp.height = WindowManager.LayoutParams.FILL_PARENT;
        d.show();
        // d.getWindow().setAttributes(lp);
    }

    public String getcurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public String getcurrentDate_aff() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public String gettoken() {
        String name = "";
        try {

            URL url = new URL("" + app.link
                    + "PostData.asmx/StartSendingReport?password=" + reg_id);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("string");
            Element nameElement = (Element) nodeList.item(0);
            nodeList = nameElement.getChildNodes();
            name = ((Node) nodeList.item(0)).getNodeValue();
            /** Assign textview array lenght by arraylist size */
            // name = new TextView[nodeList.getLength()];
            // website = new TextView[nodeList.getLength()];
            // category = new TextView[nodeList.getLength()];

        } catch (Exception e) {
            System.out.println("gg " + e);
            String url1 = "" + app.link + "PostData.asmx/VerifyRegistration";
            Connection conn = new Connection(url1);

            try {
                String error_return = conn.executeMultipartPost_Send_Error(this
                                .getClass().getSimpleName(), Actions.getDeviceName(),
                        "1", e.getMessage());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return name;
    }

    public String isquotareached(String pass) {
        String name = "";
        try {

            URL url = new URL("" + app.link
                    + "GeneralServices.asmx/IsQuotaReached?password=" + pass);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("int");
            Element nameElement = (Element) nodeList.item(0);
            nodeList = nameElement.getChildNodes();
            name = ((Node) nodeList.item(0)).getNodeValue();
            /** Assign textview array lenght by arraylist size */
            // name = new TextView[nodeList.getLength()];
            // website = new TextView[nodeList.getLength()];
            // category = new TextView[nodeList.getLength()];

        } catch (Exception e) {
            System.out.println("gg " + e);
            String url1 = "" + app.link + "PostData.asmx/VerifyRegistration";
            Connection conn = new Connection(url1);

            try {
                String error_return = conn.executeMultipartPost_Send_Error(this
                                .getClass().getSimpleName(), Actions.getDeviceName(),
                        "1", e.getMessage());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return name;
    }

    public String isblocked(String num) {
        String name = "";
        try {

            URL url = new URL("" + app.link
                    + "GeneralServices.asmx/IsNumberBlocked?number=" + num
                    + "&password=" + reg_id);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("int");
            Element nameElement = (Element) nodeList.item(0);
            nodeList = nameElement.getChildNodes();
            name = ((Node) nodeList.item(0)).getNodeValue();
            /** Assign textview array lenght by arraylist size */
            // name = new TextView[nodeList.getLength()];
            // website = new TextView[nodeList.getLength()];
            // category = new TextView[nodeList.getLength()];

        } catch (Exception e) {
            System.out.println("gg " + e);
            name = "-1";
        }
        return name;
    }

    public String getcountrycode() {
        String countryName = "";
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                countryName = addresses.get(0).getCountryName();
                // addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

        return countryName;
    }

    public String getcountrycode1() {
        String countryName = "";
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {

                countryName = addresses.get(0).getAddressLine(0) + " "
                        + addresses.get(0).getAddressLine(1) + " "
                        + addresses.get(0).getAdminArea() + " "
                        + addresses.get(0).getSubAdminArea();
                countryName = countryName.replaceAll("null", "");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

        return countryName;
    }

    public String getcountryid(String countryName) {
        // String countryName="qatar";
        String name = "165";

        TCTDbAdapter datasource = new TCTDbAdapter(reportActivity.this);
        datasource.open();
        ArrayList<Country> arr = datasource.getcountry("English");
        datasource.close();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getName().equals(countryName)) {
                name = arr.get(i).getId();
                break;
            }
        }
		/*
		 * try{
		 * 
		 * URL url = new
		 * URL(""+app.link+"GeneralServices.asmx/GetCountries?password="
		 * +reg_id+"&language=en"); DocumentBuilderFactory dbf =
		 * DocumentBuilderFactory.newInstance(); DocumentBuilder db =
		 * dbf.newDocumentBuilder(); Document doc = db.parse(new
		 * InputSource(url.openStream())); doc.getDocumentElement().normalize();
		 * 
		 * NodeList nodeList = doc.getElementsByTagName("Country");
		 * 
		 * /** Assign textview array lenght by arraylist size
		 */
        // name = new TextView[nodeList.getLength()];
        // website = new TextView[nodeList.getLength()];
        // category = new TextView[nodeList.getLength()];
		/*
		 * for (int i = 0; i < nodeList.getLength(); i++) {
		 * 
		 * Node node = nodeList.item(i);
		 * 
		 * 
		 * 
		 * Element fstElmnt = (Element) node; NodeList nameList =
		 * fstElmnt.getElementsByTagName("Id"); Element nameElement = (Element)
		 * nameList.item(0); nameList = nameElement.getChildNodes(); String id =
		 * ((Node) nameList.item(0)).getNodeValue();
		 * 
		 * NodeList websiteList = fstElmnt.getElementsByTagName("Name"); Element
		 * websiteElement = (Element) websiteList.item(0); websiteList =
		 * websiteElement.getChildNodes(); String title = ((Node)
		 * websiteList.item(0)).getNodeValue();
		 * 
		 * if(title.equals(countryName)){ name = id; break; } } /** Assign
		 * textview array lenght by arraylist size
		 */
        // name = new TextView[nodeList.getLength()];
        // website = new TextView[nodeList.getLength()];
        // category = new TextView[nodeList.getLength()];

        // }catch(Exception e){
        // System.out.println("gg "+ e);
        // }
        return name;
    }

    private String send_rep(String reg_id, String mobilenumber,
                            String affe_num, String qatarID, String email,
                            String issueDetailID, String SPID, String roamingID,
                            String creationDate, String sendingDate, String comments,
                            double locX, double locY, String countryID, String affec_qatarID,
                            String spCode) {
        String title = null;
        try {

            // com.ids.indyact.ViewResizing.setPageTextResizing(this);

            URL url = new URL("" + app.link
                    + "GeneralServices.asmx/SendReport?registrationId="
                    + reg_id + "&mobileNumber=" + mobilenumber
                    + "&affectedNumber=" + affe_num + "&qatarId=" + qatarID
                    + "&email=" + email + "&issueDetailId=" + issueDetailID
                    + "&serviceProviderID=" + SPID + "&roamingId=" + roamingID
                    + "&creationDate=" + creationDate + "&sendingDate="
                    + sendingDate + "&mobileDevice=Android&comments="
                    + comments + "&locationX=" + locX + "&locationY=" + locY
                    + "&countryId=" + countryID + "&affectedQatarId="
                    + affec_qatarID + "&serviceProviderCode=" + spCode);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList websiteList = doc.getElementsByTagName("int");
            Element websiteElement = (Element) websiteList.item(0);
            websiteList = websiteElement.getChildNodes();
            title = ((Node) websiteList.item(0)).getNodeValue();
            /** Assign textview array lenght by arraylist size */
            // name = new TextView[nodeList.getLength()];
            // website = new TextView[nodeList.getLength()];
            // category = new TextView[nodeList.getLength()];

        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
            String url1 = "" + app.link + "PostData.asmx/VerifyRegistration";
            Connection conn = new Connection(url1);

            try {
                String error_return = conn.executeMultipartPost_Send_Error(this
                                .getClass().getSimpleName(), Actions.getDeviceName(),
                        "1", e.getMessage());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return title;

        // return "";
    }

    public String splitres(String res) {
        String[] a = res.split(">");
        String[] b = a[2].split("</");
        return b[0];
    }

    public String[] get_spinner1(String idissue) {
        String[] a = null;
		/*
		 * try{
		 * 
		 * // com.ids.indyact.ViewResizing.setPageTextResizing(this);
		 * 
		 * URL url = new URL(""+app.link+
		 * "GeneralServices.asmx/GetIssueDetails?language=en&password=&issueid="
		 * + idissue); DocumentBuilderFactory dbf =
		 * DocumentBuilderFactory.newInstance(); DocumentBuilder db =
		 * dbf.newDocumentBuilder(); Document doc = db.parse(new
		 * InputSource(url.openStream())); doc.getDocumentElement().normalize();
		 * 
		 * NodeList nodeList = doc.getElementsByTagName("IssueDetail");
		 * 
		 * /** Assign textview array lenght by arraylist size // name = new
		 * TextView[nodeList.getLength()]; // website = new
		 * TextView[nodeList.getLength()]; //category = new
		 * TextView[nodeList.getLength()]; // RelativeLayout relab =
		 * (RelativeLayout) findViewById(R.id.relab); a = new
		 * String[nodeList.getLength()]; aspin1 = new
		 * String[nodeList.getLength()]; TCTDbAdapter datasource = new
		 * TCTDbAdapter(reportActivity.this); datasource.open(); for (int i = 0;
		 * i < nodeList.getLength(); i++) {
		 * 
		 * Node node = nodeList.item(i);
		 * 
		 * 
		 * 
		 * Element fstElmnt = (Element) node; NodeList nameList =
		 * fstElmnt.getElementsByTagName("Id"); Element nameElement = (Element)
		 * nameList.item(0); nameList = nameElement.getChildNodes(); String name
		 * = ((Node) nameList.item(0)).getNodeValue(); aspin1[i] = name;
		 * NodeList websiteList = fstElmnt.getElementsByTagName("Name"); Element
		 * websiteElement = (Element) websiteList.item(0); websiteList =
		 * websiteElement.getChildNodes(); String title = ((Node)
		 * websiteList.item(0)).getNodeValue();
		 * 
		 * a[i] = title; try {
		 * 
		 * long ln = datasource.createissue_detail(Integer.parseInt(name),
		 * title, idissue,"English");
		 * 
		 * 
		 * if (!(ln > -1)) {// case row not inserted, may be duplicated throw
		 * new Exception(); }
		 * 
		 * // case saved // Actions.onCreateDialog(this,
		 * "Event saved successfully.",false);
		 * 
		 * } catch (Exception e) {// case not saved //
		 * Actions.onCreateDialog(this,
		 * "Event had been previously saved.",false) // ; } //
		 * relab.addView(myButton);
		 * 
		 * /* Element fstElmnt3 = (Element) node; NodeList nameList2 =
		 * fstElmnt3.getElementsByTagName("AuthorName"); Element nameElement2 =
		 * (Element) nameList2.item(0); nameList2 =
		 * nameElement2.getChildNodes(); String name2 = ((Node)
		 * nameList2.item(0)).getNodeValue();
		 */

        // }
        // datasource.close();
        // } catch (Exception e) {
        // System.out.println("XML Pasing Excpetion = " + e);

        // }
        // */
        TCTDbAdapter datasource = new TCTDbAdapter(reportActivity.this);
        datasource.open();
        a = datasource.getissue_detail_name(idissue, "English");

        aspin1 = datasource.getissue_detail_id(idissue, "English");
        datasource.close();
        return a;
    }

    public String[] get_values() {

        int k = 1;
        TCTDbAdapter source = new TCTDbAdapter(this);
        source.open();
        // source.de
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

        // ArrayList<Event> arr2 = source.getissue_Type("2");
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
            // values[0] = "Mobile";
        }
        if (arr2.size() > 0) {
            n1 = arr2.toArray(new Event[arr2.size()]);
            // values[0] = "Mobile";
        }

        for (int i = 0; i < n.length; i++) {
            values[i] = n[i];
            k++;
        }
        for (int i = 0; i < n1.length; i++) {
            values[k] = n1[i];
            k++;
        }
        // ArrayList<Event> arr2 = source.getissue_Type("2");

        return values;
    }

    public void openCloseMenu(View view) {
        if (!this.open) {
            this.open = true;
            MenuEventController.open(this.context, this.layout);
            MenuEventController.closeKeyboard(this.context, view);
        } else {
            this.open = false;
            MenuEventController.close(this.context, this.layout);
            MenuEventController.closeKeyboard(this.context, view);
        }
    }

    public String[] get_spinner2() {
        String[] a = null;

        TCTDbAdapter source = new TCTDbAdapter(reportActivity.this);
        source.open();
        ArrayList<ServicePro> arr = source.getsp_lang("English");
        int siz = arr.size();
        source.close();
        int k = 0;
        if (sp.equals("false")) {
            a = new String[2];
            bspin2 = new String[2];

            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).getistrans().equals("false")) {
                    bspin2[k] = Integer.toString(arr.get(i).getId());
                    a[k] = arr.get(i).getName();
                    k++;
                }
            }

        } else {
            a = new String[2];
            bspin2 = new String[2];
            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).getistrans().equals("true")) {
                    bspin2[k] = Integer.toString(arr.get(i).getId());
                    a[k] = arr.get(i).getName();
                    k++;
                }
            }
        }
		/*
		 * try{
		 * 
		 * // com.ids.indyact.ViewResizing.setPageTextResizing(this);
		 * 
		 * URL url = new URL(""+app.link+
		 * "GeneralServices.asmx/GetServiceProviders?language=en&password="
		 * +"+app.pass+"); DocumentBuilderFactory dbf =
		 * DocumentBuilderFactory.newInstance(); DocumentBuilder db =
		 * dbf.newDocumentBuilder(); Document doc = db.parse(new
		 * InputSource(url.openStream())); doc.getDocumentElement().normalize();
		 * 
		 * NodeList nodeList = doc.getElementsByTagName("ServiceProvider");
		 * 
		 * /** Assign textview array lenght by arraylist size // name = new
		 * TextView[nodeList.getLength()]; // website = new
		 * TextView[nodeList.getLength()]; //category = new
		 * TextView[nodeList.getLength()]; // RelativeLayout relab =
		 * (RelativeLayout) findViewById(R.id.relab); a = new
		 * String[nodeList.getLength()]; bspin2 = new
		 * String[nodeList.getLength()]; TCTDbAdapter datasource = new
		 * TCTDbAdapter(reportActivity.this); datasource.open(); for (int i = 0;
		 * i < nodeList.getLength(); i++) {
		 * 
		 * Node node = nodeList.item(i);
		 * 
		 * 
		 * 
		 * Element fstElmnt = (Element) node; NodeList nameList =
		 * fstElmnt.getElementsByTagName("Id"); Element nameElement = (Element)
		 * nameList.item(0); nameList = nameElement.getChildNodes(); String name
		 * = ((Node) nameList.item(0)).getNodeValue();
		 * 
		 * bspin2[i] = name;
		 * 
		 * NodeList websiteList = fstElmnt.getElementsByTagName("Name"); Element
		 * websiteElement = (Element) websiteList.item(0); websiteList =
		 * websiteElement.getChildNodes(); String title = ((Node)
		 * websiteList.item(0)).getNodeValue();
		 * 
		 * a[i] = title;
		 * 
		 * NodeList websiteList1 =
		 * fstElmnt.getElementsByTagName("IsForTransfer"); Element
		 * websiteElement1 = (Element) websiteList1.item(0); websiteList1 =
		 * websiteElement1.getChildNodes(); String title1 = ((Node)
		 * websiteList1.item(0)).getNodeValue();
		 * 
		 * try {
		 * 
		 * long ln = datasource.create_sp(Integer.parseInt(name), title,
		 * title1);
		 * 
		 * 
		 * if (!(ln > -1)) {// case row not inserted, may be duplicated throw
		 * new Exception(); }
		 * 
		 * // case saved // Actions.onCreateDialog(this,
		 * "Event saved successfully.",false);
		 * 
		 * } catch (Exception e) {// case not saved //
		 * Actions.onCreateDialog(this,
		 * "Event had been previously saved.",false) // ; }
		 * 
		 * // relab.addView(myButton);
		 * 
		 * /* Element fstElmnt3 = (Element) node; NodeList nameList2 =
		 * fstElmnt3.getElementsByTagName("AuthorName"); Element nameElement2 =
		 * (Element) nameList2.item(0); nameList2 =
		 * nameElement2.getChildNodes(); String name2 = ((Node)
		 * nameList2.item(0)).getNodeValue();
		 * 
		 * 
		 * 
		 * 
		 * } datasource.close(); } catch (Exception e) {
		 * System.out.println("XML Pasing Excpetion = " + e); TCTDbAdapter
		 * datasource = new TCTDbAdapter(reportActivity.this);
		 * datasource.open(); a = datasource.getsp_name(); bspin2 =
		 * datasource.getsp_id(); datasource.close(); } }
		 */
        return a;
    }

    public String[] get_spinner3() {
        String[] a = null;
        TCTDbAdapter datasource = new TCTDbAdapter(reportActivity.this);
        datasource.open();
        ArrayList<Country> arr = datasource.getcountry("English");
        a = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            a[i] = arr.get(i).getName();
        }
        datasource.close();
        return a;
    }

    protected Location showCurrentLocation(String provider) {

        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            // Finds a provider that matches the criteria
            String provider1 = locationManager.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            location = locationManager.getLastKnownLocation(provider1);
        }
        return location;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    protected class LaunchingEvent extends AsyncTask<Void, Void, Integer> {
        com.ids.ict.Error error;
        Event[] nn, nn1, nn2;

        @Override
        protected void onPreExecute() {

            // mProgressBar.setVisibility(View.VISIBLE);
            // mProgressBar.bringToFront();

        }

        @Override
        protected Integer doInBackground(Void... params) {
            Log.d("launching", "passed here");
            Looper.prepare();
            registerLocationUpdates();

            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // if(error.getState()){
            // Actions.onCreateDialog(EventsListActivity.this,
            // error.getMessage(),false);
            // }
            // else{
            // EventsListArrayAdapterMenu adapter = new
            // EventsListArrayAdapterMenu( reportActivity.this ,nn);
            // listMenu.setAdapter(adapter);
            // }
            // mProgressBar.setVisibility(View.GONE);
        }

    }
}

// }
