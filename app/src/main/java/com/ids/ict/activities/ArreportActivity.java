package com.ids.ict.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

public class ArreportActivity extends FragmentActivity implements LocationListener {

    String url;
    String qatarID, num;
    static String email;
    int footerButton;
    String reg_id;
    Bundle bundle;
    String[] bspin2 = null;
    String[] aspin1 = null;
    EditText a;
    String spID;
    String issueID;
    String locmand = "";
    EditText roamsp;
    String gettok = "";
    int mncnum = 0;
    EditText b;
    int issus;
    String loc = "";
    int k = 0;
    int pos = 0;
    int pos1 = 0;
    EditText comm_edit;
    Connection conn;
    String sp = "";
    MyApplication app;
    TextView loca;
    String isroam = "";
    private ListView listMenu;
    boolean waitingForLocationUpdate = true;
    private final Context context = this;
    private RelativeLayout layout;
    private boolean open = false;
    String affecqatarid = "";
    String s = "";
    // map
    // GoogleMap googleMap;
    EditText email1;
    protected LocationListener locationListener;
    Button cancel_b;
    Button send_b;
    Typeface tf;
    protected LocationManager locationManager;
    Double longitude = 0.0, latitude = 0.0;// use these values to send in report
    Location location;
    String provider;
    // Marker marker;
    EditText num1;
    boolean firstRun = true;// to ignore drawing on first call coz probably it

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        Actions.setLocal(this);
        setContentView(R.layout.arreportpg);
        Actions.loadMainBar(this);
        app = (MyApplication) getApplicationContext();
        // app.face=Typeface.createFromAsset(getAssets(), "fonts/tahomabd.ttf");
        ViewResizing.setPageTextResizing(this);
        footerButton = this.getIntent().getIntExtra("footerButton", R.id.home);
        // app=(MyApplication) getApplicationContext();

        this.layout = (RelativeLayout) findViewById(R.id.layoutToMove);

        tf = MyApplication.faceDinar;
        // sendb.setTypeface(tf);
        TextView ser = (TextView) findViewById(R.id.textView1);
        TextView ser1 = (TextView) findViewById(R.id.textVw2);
        TextView email_lab = (TextView) findViewById(R.id.textView2);
        TextView issue_lab = (TextView) findViewById(R.id.textView4);
        TextView comm_lab = (TextView) findViewById(R.id.textView41);
        TextView sp_lab = (TextView) findViewById(R.id.textView14);
        TextView roam_lab = (TextView) findViewById(R.id.textView421);
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
        roamsp = (EditText) findViewById(R.id.spinCountry1);
        a = (EditText) findViewById(R.id.spinCountry);
        a.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                searchCitiesList();
            }
        });
        bundle = this.getIntent().getExtras();
        b = (EditText) findViewById(R.id.spinSP);
        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                searchCitiesList_sp();
            }
        });
        comm_edit = (EditText) findViewById(R.id.comment_edit);
        // TextView loc_lab1 = (TextView) findViewById(R.id.textView14);
        // loc_lab1.setVisibility(View.VISIBLE);
        // loca = (TextView) findViewById(R.id.loclabeldet);
        // loca.setText(getcountrycode1());
        // final Button butSend = (Button) findViewById(R.id.send_button);
        num1 = (EditText) findViewById(R.id.fil_num_edit);
        email1 = (EditText) findViewById(R.id.fill_email_edit);

        issus = bundle.getInt("eventId");
        sp = bundle.getString("eventDate");
        locmand = bundle.getString("eventDescription");
        isroam = bundle.getString("eventLocation");
        String issuename = bundle.getString("eventName");
        try {
            mncnum = Integer.parseInt(getmnc());
        } catch (Exception e) {
            mncnum = 0;
        }
        String[] sp_tab = get_spinner2();
        // mncnum=2;
        if (mncnum == 1) {
            b.setText(sp_tab[1]);
        } else if (mncnum == 2) {
            b.setText(sp_tab[0]);
        }

        if (isroam.equals("true")) {
            TextView aroa = (TextView) findViewById(R.id.textView421);
            aroa.setVisibility(View.VISIBLE);
            TextView aroa1 = (TextView) findViewById(R.id.textVw2111);
            aroa1.setVisibility(View.VISIBLE);
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
        }
        // a.setAdapter(null);
        String[] fill1 = get_spinner1("" + issus);
        // a.setText(fill1[0]);
        // a.setAdapter(dataAdapter);
        String[] fill2 = get_spinner2();
        // b.setText(fill2[0]);
        // b.setAdapter(dataAdapter1);

        new Thread(new Task()).start();
        cancel_b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ArreportActivity.this,
                        HomePageActivity.class);

                startActivity(intent);
            }
        });
        listMenu = (ListView) findViewById(R.id.listMenu1);
        // LaunchingEvent eventLaunching = new LaunchingEvent();
        // eventLaunching.execute();
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

        registerLocationUpdates();
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
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        // connect to the GPS location service
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
            TextView loc_lab = (TextView) findViewById(R.id.textView14);
            loc_lab.setVisibility(View.VISIBLE);
            loca = (TextView) findViewById(R.id.loclabeldet);
            loca.setText(getcountrycode1());
            if (waitingForLocationUpdate) {
                // getNearbyStores();
                waitingForLocationUpdate = false;
            }

            if (ActivityCompat.checkSelfPermission(ArreportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(ArreportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    public void searchCitiesList() {
        final Dialog dialog = new Dialog(ArreportActivity.this);
        dialog.setContentView(R.layout.cities_listview);
        // dialog.setTitle("الرجاء إختيار نوع المشكلة");
        final ListView listView = (ListView) dialog.findViewById(R.id.list);
        String[] values = get_spinner1("" + issus);
        dialog.show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.aligned_right1, values);
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
        final Dialog dialog = new Dialog(ArreportActivity.this);
        dialog.setContentView(R.layout.cities_listview);
        // dialog.setTitle("الرجاء إختيار مزود الخدمة");
        final ListView listView = (ListView) dialog.findViewById(R.id.list);
        String[] values = get_spinner2();
        dialog.show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.aligned_right1, values);
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
        final Dialog dialog = new Dialog(ArreportActivity.this);
        dialog.setContentView(R.layout.cities_listview);
        final ListView listView = (ListView) dialog.findViewById(R.id.list);
        String[] values = get_spinner3();
        dialog.show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.aligned_right1, values);
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

            TCTDbAdapter source = new TCTDbAdapter(ArreportActivity.this);
            source.open();
            ArrayList<Profile> arr = source.getAllProfiles();
            num1.setText(arr.get(0).getnum());
            email1.setText(arr.get(0).getemail());
            reg_id = arr.get(0).getId();
            gettok = gettoken();
            qatarID = arr.get(0).getqatarID();
            num = arr.get(0).getnum();
            email = arr.get(0).getemail();
            url = "" + MyApplication.link + "PostData.asmx/SendReport";
            conn = new Connection(url);
            Button send_b = (Button) findViewById(R.id.send_button_rep);
            send_b.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        double a = Double
                                .parseDouble(num1.getText().toString());
                        if (num1.getText().toString().equals("")) {
                            Actions.onCreateDialog1(ArreportActivity.this,
                                    "رقم الهاتف لا يجب أن يكون فارغ");
                        } else if (!RegisterActivity.isEmailValid(email1
                                .getText().toString())) {
                            Actions.onCreateDialog1(ArreportActivity.this,
                                    "البريد الإلكتروني غير صحيح");
                        } else if (!(num1.getText().length() == 8)) {
                            Actions.onCreateDialog1(ArreportActivity.this,
                                    "رقم الهاتف يجب أن يتألف من 8 أرقام");
                        } else {
                            sendRequest();
                        }
                    } catch (Exception e) {
                        Actions.onCreateDialog1(ArreportActivity.this,
                                "رقم الهاتف ورقم البطاقة الشخصية يجب أن يتألفو فقط من أرقام");
                    }
                }
            });
            source.close();

        }

    }

    public String getcountrycode1() {
        String countryName = "";
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                // countryName=addresses.get(0).getCountryName();
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


    public void sendRequest() {
        final Connection conn;
        try {
            int seca = pos;
            issueID = aspin1[seca];
            final String spname = b.getText().toString();
            int spb = pos1;
            spID = bspin2[spb];
            conn = new Connection(url);
            if (conn.getError().getState()) {
                Log.d("passing here", "error in url");
            } else {
                if ((latitude == 0.0 || longitude == 0.0)
                        && locmand.equals("true")) {
                    boolean gps_enabled = locationManager
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (gps_enabled) {
                        Actions.onCreateDialog1(ArreportActivity.this, "يرجى الانتظار لتحديد موقعك..");
                    } else {
                        Actions.onCreateDialog1(ArreportActivity.this, "قم بتشغيل خدمات الموقع للسماح ل ICT QATAR بتحديد موقعك");
                    }
                } else if (isroam.equals("true")
                        & roamsp.getText().toString().equals("")) {
                    Actions.onCreateDialog1(ArreportActivity.this, "يرجى تحديد بلد التجوال قبل المتابعة");
                } else {
                    if (a.getText().toString().equals("")) {
                        Actions.onCreateDialog1(ArreportActivity.this, "يرجى تحديد نوع المشكلة قبل المتابعة");
                    } else {
                        if (b.getText().toString().equals("")) {
                            Actions.onCreateDialog1(ArreportActivity.this, "يرجى تحديد مزود الخدمة قبل المتابعة");
                        } else {
                            try {
                                if (!isblocked(num).equals("-1")) {
                                    if (!isblocked(num).equals("0")) {
                                        Actions.onCreateDialog1(ArreportActivity.this, "تم حظر رقم هاتفك المحمول من قبل المسؤول");
                                    } else {
                                        if (!isquotareached(reg_id).equals("0")) {
                                            Actions.onCreateDialog1(ArreportActivity.this, "لقد استنفذت الحد الأقصى من عدد البلاغات في اليوم");
                                        } else {
                                            if (num1.getText().length() != 8) {
                                            } else {
                                                try {
                                                    loc = getcountryid(roamsp.getText().toString());
                                                } catch (Exception e) {
                                                    loc = "165";
                                                }
                                                String aloc;
                                                try {
                                                    aloc = loca.getText().toString();
                                                } catch (Exception e) {
                                                    aloc = "";
                                                }
                                                final String aloc1 = aloc;
                                                String mm = num1.getText().toString();
                                                if (!num.equals(num1.getText()
                                                        .toString())) {
                                                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                                                            ArreportActivity.this);
                                                    TextView textView;
                                                    final EditText affnum;
                                                    LayoutInflater inflater = ArreportActivity.this.getLayoutInflater();
                                                    final View textEntryView = inflater.inflate(R.layout.dialog_layout1, null);
                                                    textView = (TextView) textEntryView.findViewById(R.id.dialogMsg);
                                                    textView.setText("ادخل رقم البطاقة الشخصية للرقم الجديد");
                                                    affnum = (EditText) textEntryView.findViewById(R.id.numaff);
                                                    final int k1 = 0;
                                                    builder.setView(textEntryView).setNegativeButton("تم", new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {
                                                            try {
                                                                double a11 = Double.parseDouble(affnum.getText().toString());
                                                                if (affnum.getText().length() != 11) {
                                                                    onCreateDialog1_aff(ArreportActivity.this, " رقم البطاقة يجب أن يتألف من إحدى عشر رقما. ", gettok, num1.getText().toString(), num,
                                                                            email1.getText().toString(), b.getText().toString(), comm_edit.getText().toString(), getcurrentDate(),
                                                                            affnum.getText().toString(), a.getText().toString(), issueID, spID, getcurrentDate(), getcurrentDate(),
                                                                            "" + latitude, "" + longitude, qatarID, aloc1, loc, reg_id, getcurrentDate_aff());
                                                                } else {

                                                                    Intent intent = new Intent(ArreportActivity.this, SendReportActivity.class);
                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putString("gettok", gettok);
                                                                    bundle.putString("mobilenum", num1.getText().toString());
                                                                    bundle.putString("affmobilenum", num);
                                                                    bundle.putString("email", email1.getText().toString());
                                                                    bundle.putString("qatariID", b.getText().toString());
                                                                    bundle.putString("comments", comm_edit.getText().toString());
                                                                    bundle.putString("date", getcurrentDate());
                                                                    bundle.putString("issue", affnum.getText().toString());
                                                                    bundle.putString("sp", a.getText().toString());
                                                                    bundle.putString("issueid", issueID);
                                                                    bundle.putString("spid", spID);
                                                                    bundle.putString("creationdate", getcurrentDate());
                                                                    bundle.putString("sendingdate", getcurrentDate());
                                                                    bundle.putString("locx", "" + latitude);
                                                                    bundle.putString("locy", "" + longitude);
                                                                    bundle.putString("iniqatarid", "" + qatarID);
                                                                    bundle.putString("locat", aloc1);
                                                                    bundle.putString("countryid", loc);
                                                                    bundle.putString("password", reg_id);
                                                                    bundle.putString("date_aff", getcurrentDate_aff());
                                                                    bundle.putString("status", "sended");
                                                                    bundle.putString("roam_country", roamsp.getText().toString());
                                                                    intent.putExtras(bundle);
                                                                    startActivity(intent);
                                                                }
                                                            } catch (Exception e) {
                                                                Actions.onCreateDialog1(ArreportActivity.this, " رقم البطاقة يجب أن يتألف من إحدى عشر رقما. ");
                                                            }
                                                        }
                                                    });
                                                    builder.setView(textEntryView).setPositiveButton("إلغاء", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    Dialog d = builder.create();
                                                    d.getWindow().setLayout((int) (ArreportActivity.this.getWindow().peekDecorView().getWidth() * 0.9),
                                                            (int) (ArreportActivity.this.getWindow().peekDecorView().getHeight() * 0.9));
                                                    d.show();
                                                } else {
                                                    Intent intent = new Intent(ArreportActivity.this, SendReportActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("gettok", gettok);
                                                    bundle.putString("mobilenum", num1.getText().toString());
                                                    bundle.putString("affmobilenum", num);
                                                    bundle.putString("email", email1.getText().toString());
                                                    bundle.putString("qatariID", b.getText().toString());
                                                    bundle.putString("comments", comm_edit.getText().toString());
                                                    bundle.putString("date", getcurrentDate());
                                                    bundle.putString("issue", qatarID);
                                                    bundle.putString("sp", a.getText().toString());
                                                    bundle.putString("issueid", issueID);
                                                    bundle.putString("spid", spID);
                                                    bundle.putString("creationdate", getcurrentDate());
                                                    bundle.putString("sendingdate", getcurrentDate());
                                                    bundle.putString("locx", "" + latitude);
                                                    bundle.putString("locy", "" + longitude);
                                                    bundle.putString("iniqatarid", "" + qatarID);
                                                    bundle.putString("locat", aloc1);
                                                    bundle.putString("countryid", loc);
                                                    bundle.putString("password", reg_id);
                                                    bundle.putString("date_aff", getcurrentDate_aff());
                                                    bundle.putString("status", "sended");
                                                    bundle.putString("roam_country", roamsp.getText().toString());
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);

                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (!num.equals(num1.getText().toString())) {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(ArreportActivity.this);
                                        TextView textView;
                                        final EditText affnum;
                                        LayoutInflater inflater = ArreportActivity.this.getLayoutInflater();
                                        final View textEntryView = inflater.inflate(R.layout.dialog_layout1, null);
                                        textView = (TextView) textEntryView.findViewById(R.id.dialogMsg);
                                        textView.setText("ادخل رقم البطاقة الشخصية للرقم الجديد");
                                        affnum = (EditText) textEntryView.findViewById(R.id.numaff);
                                        final int k1 = 0;
                                        builder.setView(textEntryView).setNegativeButton("تم", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                try {
                                                    double a11 = Double.parseDouble(affnum.getText().toString());
                                                    String aloc;
                                                    try {
                                                        aloc = loca.getText().toString();
                                                    } catch (Exception e) {
                                                        aloc = "";
                                                    }
                                                    final String aloc1 = aloc;
                                                    if (affnum.getText().length() != 11) {
                                                        onCreateDialog1_aff(ArreportActivity.this, " رقم البطاقة يجب أن يتألف من إحدى عشر رقما. ", gettok, num1.getText().toString(), num, email1.getText().toString(),
                                                                b.getText().toString(), comm_edit.getText().toString(), getcurrentDate(), affnum.getText().toString(), a.getText().toString(),
                                                                issueID, spID, getcurrentDate(), getcurrentDate(), "" + latitude, "" + longitude, qatarID, aloc1, loc, reg_id, getcurrentDate_aff());
                                                    } else {
                                                        TCTDbAdapter datasource = new TCTDbAdapter(ArreportActivity.this);
                                                        datasource.open();
                                                        try {
                                                            long ln = datasource.createReports_off(reg_id, num, email, qatarID, "Arabic", comm_edit.getText().toString(), loca.getText().toString(),
                                                                    getcurrentDate(), getcurrentDate_aff(), a.getText().toString(), "Failed", issueID, spID, "" + latitude, "" + longitude, loc, qatarID, spname);

                                                            if (!(ln > -1)) {
                                                                throw new Exception();
                                                            }
                                                            ArrayList<Mail_OFF> arr = datasource.getLastReports_off();
                                                            Intent intent = new Intent(ArreportActivity.this, FailedReportActivity.class);
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("mobilenum", num1.getText().toString());
                                                            bundle.putString("email", email1.getText().toString());
                                                            bundle.putString("qatariID", b.getText().toString());
                                                            bundle.putString("comments", comm_edit.getText().toString());
                                                            bundle.putString("date", getcurrentDate());
                                                            bundle.putString("issue", affnum.getText().toString());
                                                            bundle.putString("sp", a.getText().toString());
                                                            bundle.putString("locx", "" + latitude);
                                                            bundle.putString("locy", "" + longitude);
                                                            bundle.putString("countid", loc);
                                                            bundle.putString("status", reg_id);
                                                            bundle.putInt("idrep", arr.get(0).getid_rep());
                                                            bundle.putString("roam_country", roamsp.getText().toString());
                                                            intent.putExtras(bundle);
                                                            startActivity(intent);
                                                        } catch (Exception e1) {
                                                            System.out.println("eee " + e1);
                                                            ArrayList<Mail_OFF> arr = datasource.getLastReports_off();
                                                            Intent intent = new Intent(ArreportActivity.this, FailedReportActivity.class);
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("mobilenum", num1.getText().toString());
                                                            bundle.putString("email", email1.getText().toString());
                                                            bundle.putString("qatariID", b.getText().toString());
                                                            bundle.putString("comments", comm_edit.getText().toString());
                                                            bundle.putString("date", getcurrentDate());
                                                            bundle.putString("issue", qatarID);
                                                            bundle.putString("sp", a.getText().toString());
                                                            bundle.putString("locx", "" + latitude);
                                                            bundle.putString("locy", "" + longitude);
                                                            bundle.putString("countid", loc);
                                                            bundle.putString("status", reg_id);
                                                            bundle.putString("roam_country", roamsp.getText().toString());
                                                            intent.putExtras(bundle);
                                                            startActivity(intent);
                                                        }
                                                        datasource
                                                                .close();

                                                    }
                                                } catch (Exception e) {
                                                    Actions.onCreateDialog1(
                                                            ArreportActivity.this,
                                                            " رقم البطاقة يجب أن يتألف من إحدى عشر رقما. ");

                                                }
                                            }
                                        });
                                        builder.setView(textEntryView).setPositiveButton("إلغاء", new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                        Dialog d = builder.create();
                                        d.getWindow().setLayout((int) (ArreportActivity.this.getWindow().peekDecorView().getWidth() * 0.9),
                                                (int) (ArreportActivity.this.getWindow().peekDecorView().getHeight() * 0.9));
                                        d.show();
                                    } else {
                                        TCTDbAdapter datasource = new TCTDbAdapter(
                                                ArreportActivity.this);
                                        datasource.open();
                                        String loic = "";
                                        try {
                                            try {
                                                loic = loca.getText()
                                                        .toString();
                                            } catch (Exception e12) {
                                                loic = "";
                                            }
                                            long ln = datasource.createReports_off(reg_id, num, email, qatarID, "Arabic", comm_edit.getText().toString(), loic, getcurrentDate(), getcurrentDate_aff(),
                                                    a.getText().toString(), "Failed", issueID, spID, "" + latitude, "" + longitude, loc, qatarID, spname);

                                            if (!(ln > -1)) {
                                                throw new Exception();
                                            }
                                            ArrayList<Mail_OFF> arr = datasource.getLastReports_off();
                                            Intent intent = new Intent(ArreportActivity.this, FailedReportActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("mobilenum", num1.getText().toString());
                                            bundle.putString("email", email1.getText().toString());
                                            bundle.putString("qatariID", b.getText().toString());
                                            bundle.putString("comments", comm_edit.getText().toString());
                                            bundle.putString("date", getcurrentDate());
                                            bundle.putString("issue", qatarID);
                                            bundle.putString("sp", a.getText().toString());
                                            bundle.putString("locx", "" + latitude);
                                            bundle.putString("locy", "" + longitude);
                                            bundle.putString("countid", loc);
                                            bundle.putString("status", reg_id);
                                            bundle.putInt("idrep", arr.get(0).getid_rep());
                                            bundle.putString("roam_country", roamsp.getText().toString());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        } catch (Exception e1) {
                                            System.out.println("eee " + e1);
                                            ArrayList<Mail_OFF> arr = datasource.getLastReports_off();

                                            Intent intent = new Intent(ArreportActivity.this, FailedReportActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("mobilenum", num1.getText().toString());
                                            bundle.putString("email", email1.getText().toString());
                                            bundle.putString("qatariID", b.getText().toString());
                                            bundle.putString("comments", comm_edit.getText().toString());
                                            bundle.putString("date", getcurrentDate());
                                            bundle.putString("issue", qatarID);
                                            bundle.putString("sp", a.getText().toString());
                                            bundle.putString("locx", "" + latitude);
                                            bundle.putString("locy", "" + longitude);
                                            bundle.putString("countid", loc);
                                            bundle.putString("status", reg_id);
                                            bundle.putInt("idrep", arr.get(0).getid_rep());
                                            bundle.putString("roam_country", roamsp.getText().toString());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                        datasource.close();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (!num.equals(num1.getText().toString())) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(ArreportActivity.this);
                                    TextView textView;
                                    final EditText affnum;
                                    LayoutInflater inflater = ArreportActivity.this.getLayoutInflater();
                                    final View textEntryView = inflater.inflate(R.layout.dialog_layout1, null);
                                    textView = (TextView) textEntryView.findViewById(R.id.dialogMsg);
                                    textView.setText("ادخل رقم البطاقة الشخصية للرقم الجديد");
                                    affnum = (EditText) textEntryView.findViewById(R.id.numaff);
                                    final int k1 = 0;
                                    builder.setView(textEntryView).setNegativeButton("تم", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            try {
                                                double a11 = Double.parseDouble(affnum.getText().toString());
                                                if (affnum.getText().length() != 11) {
                                                    Actions.onCreateDialog1(ArreportActivity.this, " رقم البطاقة يجب أن يتألف من إحدى عشر رقما. ");
                                                } else {
                                                    TCTDbAdapter datasource = new TCTDbAdapter(ArreportActivity.this);
                                                    datasource.open();
                                                    try {
                                                        long ln = datasource.createReports_off(reg_id, num, email, qatarID, "Arabic", comm_edit.getText().toString(), loca.getText().toString(), getcurrentDate(),
                                                                getcurrentDate_aff(), a.getText().toString(), "Failed", issueID, spID, "" + latitude, "" + longitude, loc, qatarID, spname);

                                                        if (!(ln > -1)) {
                                                            throw new Exception();
                                                        }
                                                        ArrayList<Mail_OFF> arr = datasource.getLastReports_off();
                                                        Intent intent = new Intent(ArreportActivity.this, FailedReportActivity.class);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("mobilenum", num1.getText().toString());
                                                        bundle.putString("email", email1.getText().toString());
                                                        bundle.putString("qatariID", b.getText().toString());
                                                        bundle.putString("comments", comm_edit.getText().toString());
                                                        bundle.putString("date", getcurrentDate());
                                                        bundle.putString("issue", qatarID);
                                                        bundle.putString("sp", a.getText().toString());
                                                        bundle.putString("locx", "" + latitude);
                                                        bundle.putString("locy", "" + longitude);
                                                        bundle.putString("countid", loc);
                                                        bundle.putString("status", reg_id);
                                                        bundle.putInt("idrep", arr.get(0).getid_rep());
                                                        bundle.putString("roam_country", roamsp.getText().toString());
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                    } catch (Exception e1) {
                                                        System.out.println("eee " + e1);
                                                        ArrayList<Mail_OFF> arr = datasource.getLastReports_off();
                                                        Intent intent = new Intent(ArreportActivity.this, FailedReportActivity.class);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("mobilenum", num1.getText().toString());
                                                        bundle.putString("email", email1.getText().toString());
                                                        bundle.putString("qatariID", b.getText().toString());
                                                        bundle.putString("comments", comm_edit.getText().toString());
                                                        bundle.putString("date", getcurrentDate());
                                                        bundle.putString("issue", qatarID);
                                                        bundle.putString("sp", a.getText().toString());
                                                        bundle.putString("locx", "" + latitude);
                                                        bundle.putString("locy", "" + longitude);
                                                        bundle.putString("countid", loc);
                                                        bundle.putString("status", reg_id);
                                                        bundle.putString("roam_country", roamsp.getText().toString());
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                    }
                                                    datasource.close();
                                                }
                                            } catch (Exception e) {
                                                Actions.onCreateDialog1(ArreportActivity.this, " رقم البطاقة يجب أن يتألف من إحدى عشر رقما. ");
                                            }
                                        }
                                    });

                                    builder.setView(textEntryView).setPositiveButton("إلغاء", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                                    Dialog d = builder.create();
                                    d.getWindow().setLayout((int) (ArreportActivity.this.getWindow().peekDecorView().getWidth() * 0.9),
                                            (int) (ArreportActivity.this.getWindow().peekDecorView().getHeight() * 0.9));
                                    d.show();
                                } else {
                                    TCTDbAdapter datasource = new TCTDbAdapter(ArreportActivity.this);
                                    datasource.open();
                                    try {
                                        long ln = datasource.createReports_off(reg_id, num, email, qatarID, "Arabic", comm_edit.getText().toString(), loca.getText().toString(), getcurrentDate(),
                                                getcurrentDate_aff(), a.getText().toString(), "Failed", issueID, spID, "" + latitude, "" + longitude, loc, qatarID, spname);
                                        if (!(ln > -1)) {
                                            throw new Exception();
                                        }
                                        ArrayList<Mail_OFF> arr = datasource.getLastReports_off();
                                        Intent intent = new Intent(ArreportActivity.this, FailedReportActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("mobilenum", num1.getText().toString());
                                        bundle.putString("email", email1.getText().toString());
                                        bundle.putString("qatariID", b.getText().toString());
                                        bundle.putString("comments", comm_edit.getText().toString());
                                        bundle.putString("date", getcurrentDate());
                                        bundle.putString("issue", qatarID);
                                        bundle.putString("sp", a.getText().toString());
                                        bundle.putString("locx", "" + latitude);
                                        bundle.putString("locy", "" + longitude);
                                        bundle.putString("countid", loc);
                                        bundle.putString("status", reg_id);
                                        bundle.putInt("idrep", arr.get(0).getid_rep());
                                        bundle.putString("roam_country", roamsp.getText().toString());
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } catch (Exception e1) {
                                        System.out.println("eee " + e1);
                                        ArrayList<Mail_OFF> arr = datasource.getLastReports_off();
                                        Intent intent = new Intent(ArreportActivity.this, FailedReportActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("mobilenum", num1.getText().toString());
                                        bundle.putString("email", email1.getText().toString());
                                        bundle.putString("qatariID", b.getText().toString());
                                        bundle.putString("comments", comm_edit.getText().toString());
                                        bundle.putString("date", getcurrentDate());
                                        bundle.putString("issue", qatarID);
                                        bundle.putString("sp", a.getText().toString());
                                        bundle.putString("locx", "" + latitude);
                                        bundle.putString("locy", "" + longitude);
                                        bundle.putString("countid", loc);
                                        bundle.putString("status", reg_id);
                                        bundle.putString("roam_country", roamsp.getText().toString());
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
            String url1 = "" + MyApplication.link + "PostData.asmx/VerifyRegistration";
            Connection conn1 = new Connection(url1);
            try {
                String error_return = conn1.executeMultipartPost_Send_Error(
                        this.getClass().getSimpleName(),
                        Actions.getDeviceName(), "1", e.getMessage());
            } catch (Exception e1) {
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
        textView.setText("ادخل رقم البطاقة الشخصية للرقم الجديد");
        affnum = (EditText) textEntryView.findViewById(R.id.numaff);

        final int k1 = 0;
        // final int k=0;;
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(textEntryView)
                // Add action buttons
                .setNegativeButton("تم", new DialogInterface.OnClickListener() {
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

                                Intent intent = new Intent(
                                        ArreportActivity.this,
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
                                bundle.putString("issue", affnum.getText()
                                        .toString());
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
                            Actions.onCreateDialog1(ArreportActivity.this,
                                    "The qatari ID should be 11 number");

                        }
                    }
                });
        builder.setView(textEntryView)
                // Add action buttons
                .setPositiveButton("إلغاء",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        Dialog d = builder.create();
        d.getWindow().setLayout(
                (int) (ArreportActivity.this.getWindow().peekDecorView()
                        .getWidth() * 0.9),
                (int) (ArreportActivity.this.getWindow().peekDecorView()
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
        textView.setText("ادخل رقم البطاقة الشخصية للرقم الجديد");
        affnum = (EditText) textEntryView.findViewById(R.id.numaff);

        final int k1 = 0;
        // final int k=0;;
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(textEntryView)
                // Add action buttons
                .setNegativeButton("تم", new DialogInterface.OnClickListener() {
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

                                Intent intent = new Intent(
                                        ArreportActivity.this,
                                        FailedReportActivity.class);
                                Bundle bundle = new Bundle();
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

                                TCTDbAdapter datasource = new TCTDbAdapter(
                                        ArreportActivity.this);
                                datasource.open();
                                try {

                                    long ln = datasource.createReports_off(
                                            reg_id, num, email, qatarID,
                                            "Arabic", comm_edit.getText()
                                                    .toString(), loca.getText()
                                                    .toString(),
                                            getcurrentDate(),
                                            getcurrentDate_aff(), a.getText()
                                                    .toString(), "Failed", "",
                                            "", "" + latitude, "" + longitude,
                                            loc, qatarID, "");

                                    if (!(ln > -1)) {// case row not inserted,
                                        // may be duplicated
                                        throw new Exception();
                                    }
                                } catch (Exception e) {

                                }
                                ArrayList<Mail_OFF> arr = datasource
                                        .getLastReports_off();
                                datasource.close();
                                bundle.putInt("idrep", arr.get(0).getid_rep());
                                bundle.putString("roam_country", roamsp
                                        .getText().toString());
                                // adding the bundle to the intent
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        } catch (Exception e) {
                            Actions.onCreateDialog1(ArreportActivity.this,
                                    "The qatari ID should be 11 number");

                        }
                    }
                });
        builder.setView(textEntryView)
                // Add action buttons
                .setPositiveButton("إلغاء",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        Dialog d = builder.create();
        d.getWindow().setLayout(
                (int) (ArreportActivity.this.getWindow().peekDecorView()
                        .getWidth() * 0.9),
                (int) (ArreportActivity.this.getWindow().peekDecorView()
                        .getHeight() * 0.9));

        d.show();
    }

    public void onCreateDialog1_aff(final Activity activity, final String msg,
                                    final String gettok, final String mobnum, final String affmobnum,
                                    final String email, final String qatarid, final String spid,
                                    final String comm, final String date, final String issue,
                                    final String sp, final String issueid, final String creatdate,
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
                .setNegativeButton("تم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog_aff(activity, msg, gettok, mobnum, affmobnum,
                                email, qatarid, spid, comm, date, issue, sp,
                                issueid, creatdate, senddate, locx, locy,
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
                                        final String spid, final String comm, final String date,
                                        final String issue, final String sp, final String issueid,
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
                .setNegativeButton("تم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog_aff_off(activity, msg, gettok, mobnum,
                                affmobnum, email, qatarid, spid, comm, date,
                                issue, sp, issueid, creatdate, senddate, locx,
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
            String url1 = "" + MyApplication.link + "PostData.asmx/VerifyRegistration";
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
        return mnc;
    }

    public String isquotareached(String pass) {
        String name = "";
        try {

            URL url = new URL("" + MyApplication.link
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
            String url1 = "" + MyApplication.link + "PostData.asmx/VerifyRegistration";
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

            URL url = new URL("" + MyApplication.link
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

    public String getcurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a",
                new Locale("en"));
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public String getcurrentDate_aff() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a",
                new Locale("en"));
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public String gettoken() {
        String name = "";
        try {

            URL url = new URL("" + MyApplication.link
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
            String url1 = "" + MyApplication.link + "PostData.asmx/VerifyRegistration";
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

    public String getcountrycode() {
        String countryName = "";
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                countryName = addresses.get(0).getCountryName();
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

        TCTDbAdapter datasource = new TCTDbAdapter(ArreportActivity.this);
        datasource.open();
        ArrayList<Country> arr = datasource.getcountry("Arabic");
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

    public String splitres(String res) {
        String[] a = res.split(">");
        String[] b = a[2].split("</");
        return b[0];
    }

    public String[] get_spinner1(String idissue) {
        String[] a = null;
        TCTDbAdapter datasource = new TCTDbAdapter(ArreportActivity.this);
        datasource.open();
        a = datasource.getissue_detail_name(idissue, "Arabic");

        aspin1 = datasource.getissue_detail_id(idissue, "Arabic");
        datasource.close();
        // }
        return a;
    }

    public String[] get_spinner2() {
        String[] a = null;

        TCTDbAdapter source = new TCTDbAdapter(ArreportActivity.this);
        source.open();
        ArrayList<ServicePro> arr = source.getsp_lang("Arabic");
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
        return a;
    }

    public String[] get_spinner3() {
        String[] a = null;
        TCTDbAdapter datasource = new TCTDbAdapter(ArreportActivity.this);
        datasource.open();
        ArrayList<Country> arr = datasource.getcountry("Arabic");
        a = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            a[i] = arr.get(i).getName();
        }
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

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        // locationManager.removeUpdates(this);

        // this.finish();

    }

    protected Location showCurrentLocation(String provider) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            // Finds a provider that matches the criteria
            String provider1 = locationManager.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            location = locationManager.getLastKnownLocation(provider1);
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}

// }
