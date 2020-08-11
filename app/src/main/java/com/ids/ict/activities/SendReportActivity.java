package com.ids.ict.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.Mail_OFF;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.SharedPreference;
import com.ids.ict.parser.SpeedTestResultParser;

import org.shipp.util.MenuEventController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class SendReportActivity extends Activity {

    Typeface tf;
    String gettok, mobilenumber, affmobile, email, qatarid, date, date_aff,
            affqatarid, comments, sendingdate, createdate, isspecialneed,
            specialneednumber, craNumber, dateOfRequest, eventNameEn, eventNameAr;
    String issueid, spid, locx, locy, location, countryid, pass, issue, spname,
            roam_c, address = "";
    public double latitude;
    public double longitude;
//    SharedPreferences mshaPreferences = getSharedPreferences("PrefEng",
//            Context.MODE_PRIVATE);
//    SharedPreferences.Editor edit;

    String spComplaint, longWait, cmplNum, cmplDate, callNum, calldate, status,
            waitTime, IssueType, isIndividual, isCorporate, cRNumber,
            channelusedid;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    boolean waitingForLocationUpdate = true, ctgSelected = false,
            lineSelected = false;
    private String provider;
    Connection conn;
    private boolean open = false;
    private final Context context = this;
    int footerButton;
    MyApplication app;
    TextView roam_lab;
    Bundle mbundle;
    String s = "";
    String lang = "";
    ProgressDialog dialog;
    int k = 0;
    private RelativeLayout layout;
    private String areaId = "0", subAreaId = "0";
    Button update_location;
    String street, building, zone;
    TextView header, ticketNumTxt, textView0;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;
    SharedPreferences mshSharedPreferences;
    SharedPreferences.Editor edit;
    String result;
    LinearLayout llHeader;

    TextView ticketID, ticketType, tvContactNumTxt;
    String contactNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Actions.setLocal(this);
       // TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.successreportpgtest);
        Actions.loadMainBar(this);

        ViewResizing.setPageTextResizing(this);

        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            tf = MyApplication.facePolarisMedium;
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);
        } else {
            tf = MyApplication.faceDinar;
        }
        mshSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        edit = mshSharedPreferences.edit();
        //edit = mshaPreferences.edit();
        llHeader = (LinearLayout) findViewById(R.id.llHeader);
        TextView issueTxt = (TextView) findViewById(R.id.issuetypeTxt);
        TextView ticketType = (TextView) findViewById(R.id.ticketType);
        TextView ticketID = (TextView) findViewById(R.id.ticketID);


        issueTxt.setTypeface(tf);
        ticketID.setTypeface(MyApplication.facePolarisMedium);
        ticketType.setTypeface(tf);

        TextView mobNum = (TextView) findViewById(R.id.mobNumTxt);
        mobNum.setTypeface(MyApplication.facePolarisMedium);

        TextView tvContactNumTxt = (TextView) findViewById(R.id.tvContactNumTxt);
        tvContactNumTxt.setTypeface(MyApplication.facePolarisMedium);
        //mobNum.setTypeface(tf);
        TextView emailTxt = (TextView) findViewById(R.id.emailTxt);
        emailTxt.setTypeface(tf);
        TextView comm = (TextView) findViewById(R.id.commentTxt);
        comm.setTypeface(tf);
        TextView sp = (TextView) findViewById(R.id.spTxt);
        sp.setTypeface(tf);
        TextView complained = (TextView) findViewById(R.id.spcomplainTxt);
        complained.setTypeface(tf);
        TextView waitValue = (TextView) findViewById(R.id.waitValueTxt);
        waitValue.setTypeface(tf);
        TextView cmpNum = (TextView) findViewById(R.id.cmpNumTxt);
        //	cmpNum.setTypeface(tf);
        cmpNum.setTypeface(MyApplication.facePolarisMedium);
        TextView callPhone = (TextView) findViewById(R.id.callPhoneTxt);
        //callPhone.setTypeface(tf);
        callPhone.setTypeface(MyApplication.facePolarisMedium);
        TextView cmpDate = (TextView) findViewById(R.id.cmpDateTxt);
         cmpDate.setTypeface(MyApplication.facePolarisMedium);
        TextView callDate = (TextView) findViewById(R.id.callDateTxt);
        //callDate.setTypeface(tf);
        TextView roam = (TextView) findViewById(R.id.roamTxt);
        roam.setTypeface(tf);
        TextView affmobNum = (TextView) findViewById(R.id.affmobNumTxt);
        //affmobNum.setTypeface(tf);
        affmobNum.setTypeface(MyApplication.facePolarisMedium);
        TextView crNum = (TextView) findViewById(R.id.qidedt);
        //crNum.setTypeface(tf);
        crNum.setTypeface(MyApplication.facePolarisMedium);
        TextView locationTxt = (TextView) findViewById(R.id.locationTxt);
        textView0 = (TextView) findViewById(R.id.textView0);
        ticketNumTxt = (TextView) findViewById(R.id.ticketNumTxt);
        locationTxt.setTypeface(tf);
        header = (TextView) findViewById(R.id.textView1);

        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        TextView specialneedtxt = (TextView) findViewById(R.id.SpecialNeedTxt);
        specialneedtxt.setText(getLookup("50").namear);
        if (MyApplication.Lang.equals(MyApplication.ENGLISH))
            specialneedtxt.setText(getLookup("50").nameen);


        TextView callnumbertxt = (TextView) findViewById(R.id.referencetxt);
        callnumbertxt.setText(getLookup("21").namear);
        if (MyApplication.Lang.equals(MyApplication.ENGLISH))
            callnumbertxt.setText(getLookup("21").nameen);


        TextView specialneednumbertxt = (TextView) findViewById(R.id.specialneednumber);
        specialneednumbertxt.setTypeface(MyApplication.facePolarisMedium);
        LinearLayout roam_lab = (LinearLayout) findViewById(R.id.roam_lab);
        TextView craNumbertxt = (TextView) findViewById(R.id.craNumber);

        mbundle = this.getIntent().getExtras();
        update_location = (Button) findViewById(R.id.update_location);
        // database variables
        gettok = mbundle.getString("gettok", "");

        email = mbundle.getString("email", "");

        mobilenumber = mbundle.getString("mobilenum", "");

        contactNum = mbundle.getString("contactnum", "");
        tvContactNumTxt.setText(contactNum);

        qatarid = mbundle.getString("qatariID", "");

        pass = mbundle.getString("password", "");

        // form variables
        IssueType = mbundle.getString("IssueType", "1");


        eventNameAr = mbundle.getString("eventNameAr", "");
        eventNameEn = mbundle.getString("eventNameEn", "");


        issue = mbundle.getString("issue", "");
        issueid = mbundle.getString("issueid", "");


        if (getIntent().getExtras().getString("from").equals("ticket")) {

            SharedPreferences prefsEn = getSharedPreferences("PrefEng", Context.MODE_PRIVATE );

            header.setText(getString(R.string.entered_info));
            ticketID.setVisibility(View.VISIBLE);
            ticketType.setVisibility(View.VISIBLE);
            ticketID.setText("Arsel-" + issueid);

            //header.append(" Arsel-" + issueid);

            try {
                if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                    if (IssueType.equals("1"))
                        ticketType.setText("-" + getLookup(prefsEn, "23").nameen);
                    else
                        ticketType.setText("-" + getLookup(prefsEn, "24").nameen);

                } else {
                    Log.wtf("Ticket",getLookup(prefsEn, "23").namear);
                    Log.wtf("Ticket",getLookup(prefsEn, "24").namear);

                    if (IssueType.equals("1"))
                        ticketType.setText("-" + getLookup(prefsEn, "23").namear);
                    else
                        ticketType.setText("-" + getLookup(prefsEn, "24").namear);
                }
            } catch (Exception e) {
                if (IssueType.equals("1"))
                    ticketType.setText( getResources().getString(R.string.complaint));
                else
                    ticketType.setText( getResources().getString(R.string.inquiry));
            }

            textView0 = (TextView) findViewById(R.id.textView0);
            ticketNumTxt = (TextView) findViewById(R.id.ticketNumTxt);

            textView0.setVisibility(View.GONE);
            ticketNumTxt.setVisibility(View.GONE);
        } else {
            header.setText(getString(R.string.entered_info2));

            textView0.setVisibility(View.GONE);
            ticketNumTxt.setVisibility(View.GONE);
        }


        affqatarid = mbundle.getString("iniqatarid", "");
        affmobile = mbundle.getString("affmobilenum", "");
        comments = mbundle.getString("comments", "");
        spname = mbundle.getString("sp", "");
        dateOfRequest = mbundle.getString("dateOfRequest", "");
        spid = mbundle.getString("spid", "");
        craNumber = mbundle.getString("CRNumber", "");
        locx = mbundle.getString("locx", "");
        locy = mbundle.getString("locy", "");
        location = mbundle.getString("locat", "");
        street = mbundle.getString("street", "");
        building = mbundle.getString("building", "");
        zone = mbundle.getString("zone", "");
        countryid = mbundle.getString("countryid", "");
        spComplaint = mbundle.getString("spComplaint", "");
        longWait = mbundle.getString("longWait", "");
        waitTime = mbundle.getString("waitTime", "");
        cmplNum = mbundle.getString("cmplNum", "");
        areaId = mbundle.getString("areaId", "");
        subAreaId = mbundle.getString("subAreaId", "");
        cmplDate = mbundle.getString("cmplDate", "1/1/1900").split("T")[0];
        callNum = mbundle.getString("callNum", "");

        TextView complaintreferencenumber = (TextView) findViewById(R.id.referencenumber);

        complaintreferencenumber.setText(cmplNum);
        complaintreferencenumber.setTypeface(MyApplication.facePolarisMedium);
        calldate = mbundle.getString("calldate", "1/1/1900");
        isIndividual = mbundle.getString("isIndividual", "flase");
        isCorporate = mbundle.getString("isCorporate", "false");
        cRNumber = mbundle.getString("CRNumber", "");
        roam_c = mbundle.getString("roam_country", "");
        // other variables
        date = mbundle.getString("date", "");
        createdate = mbundle.getString("creationdate", "");

        sendingdate = mbundle.getString("sendingdate", "");

        date_aff = mbundle.getString("date_aff", "");
        status = mbundle.getString("status", "");
        channelusedid = mbundle.getString("channelusedid", "");
        isspecialneed = mbundle.getString("isspecialneed", "");
        specialneednumber = mbundle.getString("specialneedregnumber", "");
        specialneednumbertxt.setText(specialneednumber);

        setTypeFaceAll();

        issueTxt.setText(issue);
        ticketNumTxt.setText(issueid);
        mobNum.setText(mobilenumber);
        affmobNum.setText(mobilenumber);
        if (isIndividual.equalsIgnoreCase("true")) {
            crNum.setText(affqatarid);
        } else {
            crNum.setText(cRNumber);
        }
        craNumbertxt.setText(craNumber);
        emailTxt.setText(email);
        if(comments.contains("null"))
            comm.setText("");
        else
        comm.setText(comments);
        sp.setText(spname);
        if (spComplaint.equalsIgnoreCase("3")) {
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                complained.setText(getLookup("3").namear);
            } else {
                complained.setText(getLookup("3").nameen);
            }
            LinearLayout l1 = (LinearLayout) findViewById(R.id.waitTimelayout);
            l1.setVisibility(View.VISIBLE);
            if (longWait.equalsIgnoreCase("true")) {
                waitValue.setText(getResources().getString(R.string.more_than2)
                        + " " + waitTime);
            } else {
                waitValue.setText(getResources().getString(R.string.less_than2)
                        + " " + waitTime);
            }
            if (waitTime.equals(""))
                l1.setVisibility(View.GONE);
            LinearLayout l2 = (LinearLayout) findViewById(R.id.cmpllayout);
            l2.setVisibility(View.VISIBLE);
            cmpNum.setText(cmplNum);
            cmpDate.setText(cmplDate);
        } else if (spComplaint.equalsIgnoreCase("4")) {
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                complained.setText(getLookup("4").namear);
            } else {
                complained.setText(getLookup("4").nameen);
            }
            LinearLayout l3 = (LinearLayout) findViewById(R.id.calllayout);
            l3.setVisibility(View.VISIBLE);
            LinearLayout l4 = (LinearLayout) findViewById(R.id.locationlayout);
            l4.setVisibility(View.VISIBLE);
            callPhone.setText(callNum);
            callDate.setText(calldate);
            locationTxt.setText(location);
        } else {
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                complained.setText(getLookup("5").namear);
            } else {
                complained.setText(getLookup("5").nameen);
            }
        }
        if (IssueType.equalsIgnoreCase("2")) {
            crNum.setVisibility(View.GONE);
            complained.setVisibility(View.GONE);
            TextView textView7 = (TextView) findViewById(R.id.textView7);
            textView7.setVisibility(View.GONE);
            TextView textView16 = (TextView) findViewById(R.id.textView16);
            textView16.setVisibility(View.GONE);

        }
        issueTxt.setText(mbundle.getString("issue"));
        mobNum.setText(mobilenumber);
        emailTxt.setText(email);
        comm.setText(mbundle.getString("comments"));
        sp.setText(mbundle.getString("sp"));
        Button send = (Button) findViewById(R.id.send_button_rep);
        Button can = (Button) findViewById(R.id.cancel_button_rep);
        Button comment = (Button) findViewById(R.id.send_comment);

        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            final RelativeLayout ll = (RelativeLayout) findViewById(R.id.mainlayout);
            ll.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            llHeader.setBackgroundColor(ContextCompat.getColor(this,R.color.nightBlue));

            LinearLayout sendll = (LinearLayout) findViewById(R.id.sendll);
            LinearLayout commentll = (LinearLayout) findViewById(R.id.commentll);

            sendll.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
            commentll.setBackgroundColor(ContextCompat.getColor(this,R.color.white));

            ticketID.setBackgroundColor(ContextCompat.getColor(this,R.color.nightBlue));
            ticketType.setBackgroundColor(ContextCompat.getColor(this,R.color.nightBlue));
            ticketType.setTextColor(ContextCompat.getColor(this,R.color.white));
            ticketID.setTextColor(ContextCompat.getColor(this,R.color.white));

            /*can.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            send.setBackgroundColor(getResources().getColor(R.color.nightBlue));*/

            can.setBackground(ContextCompat.getDrawable(SendReportActivity.this, R.drawable.button_night));
            send.setBackground(ContextCompat.getDrawable(SendReportActivity.this, R.drawable.button_night));

            comment.setBackground(ContextCompat.getDrawable(SendReportActivity.this, R.drawable.button_night));
            TextView textView1 = (TextView) findViewById(R.id.textView1);
            textView1.setBackgroundColor(getResources().getColor(
                    R.color.nightBlue));
            textView1.setTextColor(getResources().getColor(R.color.white));
            update_location.setBackground(ContextCompat.getDrawable(SendReportActivity.this, R.drawable.button_night));
        }
        setLookups();
        if (gettok.equalsIgnoreCase("-1")) {
            LinearLayout sendll = (LinearLayout) findViewById(R.id.sendll);
            sendll.setVisibility(View.GONE);
            LinearLayout commentll = (LinearLayout) findViewById(R.id.commentll);
            commentll.setVisibility(View.VISIBLE);

        }
        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                /*sendreq eventLaunching = new sendreq();
                eventLaunching.execute();*/

                if (Actions.isNetworkAvailable(SendReportActivity.this)) {

                    new IsBlocked().execute(mobilenumber, "0");
                } else {
                    if (mshSharedPreferences.getString(getResources().getString(R.string.is_blocked), "").equals("")) {
                        /*edit.putString(getResources().getString(R.string.is_blocked), "true").apply();
                        //alert blocked
                        if (MyApplication.Lang.equals(MyApplication.ARABIC))
                            Actions.onCreateBlockedDialog(SendReportActivity.this, getLookup("94").namear);
                        else
                            Actions.onCreateBlockedDialog(SendReportActivity.this, getLookup("94").nameen);*/


                        sendreq eventLaunching = new sendreq();
                        eventLaunching.execute();



                    } else {
                        if (mshSharedPreferences.getString(getResources().getString(R.string.is_blocked), "").equals("true")) {
                            //alert blocked;
                            if (MyApplication.Lang.equals(MyApplication.ARABIC))
                                Actions.onCreateBlockedDialog(SendReportActivity.this, getLookup("94").namear);
                            else
                                Actions.onCreateBlockedDialog(SendReportActivity.this, getLookup("94").nameen);
                        } else {
                            //send offline;
                            sendreq eventLaunching = new sendreq();
                            eventLaunching.execute();
                        }
                    }
                }
            }
        });







        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(SendReportActivity.this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                }
            }
        } catch (Exception e) {

        }



        /*int off;
        try {
            off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Exception e) {
            off = 0;
        }
        if (off == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(SendReportActivity.this, R.style.AlertDialogCustom));
            builder
                    .setMessage(SendReportActivity.this.getResources().getString(R.string.enablegps))
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
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            registerLocationUpdates();
        }*/

        //registerLocationUpdates();


        //<editor-fold>
        comment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int eventId = mbundle.getInt("eventId", -1);
                String detailNameEn = mbundle.getString("detailNameEn", "");
                String detailNameAr = mbundle.getString("detailNameAr", "");
                Intent i = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("id", eventId);
                if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    bundle.putString("title", detailNameEn);
                } else {
                    bundle.putString("title", detailNameAr);
                }
                i.putExtras(bundle);

                i.setClass(SendReportActivity.this, PopUpTicketActivity.class);
                SendReportActivity.this.startActivity(i);
            }
        });

        can.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SendReportActivity.this.finish();
            }
        });
        //</editor-fold>
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    registerLocationUpdates();
                ;
            }
        }
    }

    class IsBlocked extends AsyncTask<String, String, String> {

        String name = "";
        String res = "";
        String key = "";

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub
            key = param[1];
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
            Log.wtf("key", key);

            if (key.equals("0")) { //send complaint


                if (result.contains("1")) { //blockes
                    if (MyApplication.Lang.equals(MyApplication.ARABIC))
                        Actions.onCreateBlockedDialog(SendReportActivity.this, getLookup("94").namear);
                    else
                        Actions.onCreateBlockedDialog(SendReportActivity.this, getLookup("94").nameen);

                } else { //niot blocked


                    sendreq eventLaunching = new sendreq();
                    eventLaunching.execute();



                }

            } else { //update location

                Log.wtf("update", "location");

                if (result.contains("1")) { // blocked



                    if (MyApplication.Lang.equals(MyApplication.ARABIC))
                        Actions.onCreateBlockedDialog(SendReportActivity.this, getLookup("94").namear);
                    else
                        Actions.onCreateBlockedDialog(SendReportActivity.this, getLookup("94").nameen);



                } else { //not blocked

//                    Log.wtf("not", "blocked");
//
//                    int off = 0;
//                    try {
//                        off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
//                    } catch (Exception e) {
//                        off = 0;
//                    }
//                    if (off == 0) {
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(SendReportActivity.this, R.style.AlertDialogCustom));
//                        builder
//                                .setMessage(SendReportActivity.this.getResources().getString(R.string.enablegps))
//                                .setCancelable(true)
//                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                        Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                        startActivityForResult(onGPS, 0);
//                                    }
//                                })
//                                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        AlertDialog alert = builder.create();
//                        alert.show();
//                    } else {
//
//                        Log.wtf("registerLocationUpdates", "func");
//
//                        registerLocationUpdates();
//                    }
                    //registerLocationUpdates();
                    UpdateLocationAsynck update = new UpdateLocationAsynck();
                    update.execute(Actions.create_token_new(),
                            String.valueOf(longitude), String.valueOf(latitude),
                            String.valueOf(IssueType));



                }
            }


        }
    }


    public LookUp getLookup(SharedPreferences mshaPreferences, String id) {
        LookUp look = new LookUp();
        Gson gson = new Gson();
        String json = mshaPreferences.getString(id, "");
        look = gson.fromJson(json, LookUp.class);

        return look;
    }

    private void setTypeFaceAll() {
        TextView textView0 = (TextView) findViewById(R.id.textView0);
        textView0.setTypeface(tf);
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setTypeface(tf);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setTypeface(tf);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(tf);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setTypeface(tf);
        TextView textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setTypeface(tf);
        TextView textView6 = (TextView) findViewById(R.id.textView6);
        textView6.setTypeface(tf);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
        textView7.setTypeface(tf);
        TextView textView8 = (TextView) findViewById(R.id.textView8);
        //textView8.setTypeface(tf);
        textView8.setTypeface(MyApplication.facePolarisMedium);
        TextView textView9 = (TextView) findViewById(R.id.textView9);
        textView9.setTypeface(tf);
        TextView textView10 = (TextView) findViewById(R.id.textView10);
        textView10.setTypeface(tf);
        TextView textView11 = (TextView) findViewById(R.id.textView11);
        textView11.setTypeface(tf);
        TextView textView12 = (TextView) findViewById(R.id.textView12);
        textView12.setTypeface(tf);
        TextView textView13 = (TextView) findViewById(R.id.waittimetxt);
        textView13.setTypeface(tf);
        TextView textView14 = (TextView) findViewById(R.id.textView14);
        textView14.setTypeface(tf);
        TextView textView15 = (TextView) findViewById(R.id.textView15);
        textView15.setTypeface(tf);
        TextView textView16 = (TextView) findViewById(R.id.textView16);
        textView16.setTypeface(tf);
        if (isIndividual.equalsIgnoreCase("true")) {
            textView16.setText(getResources().getString(R.string.Aff_qatari));
        } else {
            textView16.setText(getResources().getString(R.string.corp));
        }

    }

    public void updateLocation(View v) {

        if (Actions.isNetworkAvailable(SendReportActivity.this)) {

            new IsBlocked().execute(mobilenumber, "1");
        } else {
            if (mshSharedPreferences.getString(getResources().getString(R.string.is_blocked), "").equals("")) {
                /*edit.putString(getResources().getString(R.string.is_blocked), "true").apply();
                //alert blocked
                if (MyApplication.Lang.equals(MyApplication.ARABIC))
                    Actions.onCreateBlockedDialog(SendReportActivity.this, getLookup("94").namear);
                else
                    Actions.onCreateBlockedDialog(SendReportActivity.this, getLookup("94").nameen);*/

//
//
//
//
//
//                int off = 0;
//                try {
//                    off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
//                } catch (Exception e) {
//                    off = 0;
//                }
//                if (off == 0) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(SendReportActivity.this, R.style.AlertDialogCustom));
//                    builder
//                            .setMessage(SendReportActivity.this.getResources().getString(R.string.enablegps))
//                            .setCancelable(true)
//                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                    startActivityForResult(onGPS, 0);
//                                }
//                            })
//                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                } else {
//                    registerLocationUpdates();
//                }
                UpdateLocationAsynck update = new UpdateLocationAsynck();
                update.execute(Actions.create_token_new(),
                        String.valueOf(longitude), String.valueOf(latitude),
                        String.valueOf(IssueType));
            //registerLocationUpdates();






            } else {
                if (mshSharedPreferences.getString(getResources().getString(R.string.is_blocked), "").equals("true")) {
                    //alert blocked;
                    if (MyApplication.Lang.equals(MyApplication.ARABIC))
                        Actions.onCreateBlockedDialog(SendReportActivity.this, getLookup("94").namear);
                    else
                        Actions.onCreateBlockedDialog(SendReportActivity.this, getLookup("94").nameen);
                } else {









                    int off = 0;
                    try {
                        off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
                    } catch (Exception e) {
                        off = 0;
                    }
                    if (off == 0) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(SendReportActivity.this, R.style.AlertDialogCustom));
                        builder
                                .setMessage(SendReportActivity.this.getResources().getString(R.string.enablegps))
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
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        registerLocationUpdates();
                    }
                }
                //registerLocationUpdates();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            registerLocationUpdates();
        }
    }

    void registerLocationUpdates() {
        /*dialog = new ProgressDialog(SendReportActivity.this);
        dialog.show();*/

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        progressBarLayout.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.VISIBLE);


        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        provider = locationManager.getBestProvider(criteria, true);
        if (provider == null) {
            return;
        } else {
        }
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
        Location oldLocation = locationManager.getLastKnownLocation(provider);

        if (oldLocation != null) {
            waitingForLocationUpdate = false;
        } else {
        }

        /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBarLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.wtf("SendReport","onResume");
        Actions.setLocal(SendReportActivity.this);
    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("hello im here", "the value are " + latitude + " "
                    + longitude);
            // loca = (TextView) findViewById(R.id.loclabeldet);
            // loca.setText(getcountrycode1(latitude, longitude));
            address = getcountrycode1(latitude, longitude);


            // here call the web service


            // address = loca.getText().toString();
            if (waitingForLocationUpdate) {
                waitingForLocationUpdate = false;
            }

            if (ActivityCompat.checkSelfPermission(SendReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SendReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        }

        public void onProviderEnabled(String s) {
        }

        public void onProviderDisabled(String s) {
        }
    }

    public class UpdateLocationAsynck extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBarLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String res = "";
            try {
                Connection conn = null;
                conn = new Connection("" + app.link
                        + "PostData.asmx/UpdateReportLocation");

                res = conn.UpdateLocationFunction(params[0], params[1],
                        params[2], params[3]);

            } catch (Exception e) {
                Log.d("eee", e + "");

            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {


                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                //dialog.dismiss();
            } catch (Exception e) {

            }
            if (result.contains("true")) {
                if (MyApplication.Lang.equals(MyApplication.ARABIC))
                    Actions.onCreateDialog3(SendReportActivity.this,
                            getLookup("48").namear);
                else
                    Actions.onCreateDialog3(SendReportActivity.this,
                            getLookup("48").nameen);
            } else {
                if (MyApplication.Lang.equals(MyApplication.ARABIC))
                    Actions.onCreateDialog3(SendReportActivity.this,
                            getLookup("49").namear);
                else
                    Actions.onCreateDialog3(SendReportActivity.this,
                            getLookup("49").nameen);
            }
        }

    }

    public String getcountrycode1(double lat, double longt) {
        String countryName = "";
        Locale locale;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            locale = new Locale("en");
        } else {
            locale = new Locale("ar");
        }
        Geocoder gcd = new Geocoder(this, locale);
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(lat, longt, 1);
            if (addresses.size() > 0) {
                countryName = addresses.get(0).getAddressLine(0) + " "
                        + addresses.get(0).getAddressLine(1) + " "
                        + addresses.get(0).getAdminArea() + " "
                        + addresses.get(0).getSubAdminArea();
                countryName = countryName.replaceAll("null", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return countryName;
    }

//	public class sendreq1 extends AsyncTask<Void, Void, Integer> {
//		com.ids.ict.Error error;
//		Event[] nn;
//
//		@Override
//		public void onPreExecute() {
//		}
//
//		@Override
//		public Integer doInBackground(Void... params) {
//			Log.d("launching", "passed here");
//
//			try {
//				Connection conn = null;
//				conn = new Connection("" + app.link
//						+ "PostData.asmx/SendReport");
//				try {
//					s = conn.executeMultipartPost_event("" + app.link
//							+ "PostData.asmx/SendReport3", gettok, affmobile,
//							mobilenumber, qatarid, email, issueid, spid, "1",
//							createdate, sendingdate, comments, locx, locy,
//							countryid, affqatarid, spname, IssueType,
//							spComplaint, longWait, cmplNum, cmplDate, callNum,
//							calldate, isIndividual, isCorporate, cRNumber, "6",
//							channelusedid, isspecialneed, specialneednumber);
//					System.out.println("String  " + s);
//					String m = splitres(s);
//					try {
//						k = Integer.parseInt(m);
//					} catch (Exception e) {
//						k = 0;
//					}
//					TCTDbAdapter datasource = new TCTDbAdapter(
//							SendReportActivity.this);
//					datasource.open();
//					if (k > 0) {
//						try {
//							long ln = datasource.createReports_off(pass,
//									mobilenumber, email, qatarid, "Arabic",
//									comments, location, createdate, date_aff,
//									issue, "sended", issueid, spid, "" + locx,
//									"" + locy, location, affqatarid, spname);
//							if (!(ln > -1)) {
//								throw new Exception();
//							}
//							return 1;
//						} catch (Exception e) {
//							return 0;
//						}
//					} else {
//						try {
//							long ln = datasource.createReports_off(pass,
//									mobilenumber, email, qatarid, "Arabic",
//									comments, location, createdate, date_aff,
//									issue, "Failed", issueid, spid, "" + locx,
//									"" + locy, location, affqatarid, spname);
//							if (!(ln > -1)) {
//								throw new Exception();
//							}
//							Actions.onCreateDialog1(SendReportActivity.this,
//									"لا يمكن التحقق من الاتصال بالخادم. يرجى المحاولة مرة أخرى.");
//							ArrayList<Mail_OFF> arr = datasource
//									.getLastReports_off();
//							datasource.close();
//							return 0;
//						} catch (Exception e) {
//							datasource.close();
//						}
//						return 0;
//					}
//				} catch (Exception e) {
//					try {
//						TCTDbAdapter datasource = new TCTDbAdapter(
//								SendReportActivity.this);
//						datasource.open();
//						long ln = datasource.createReports_off(pass,
//								mobilenumber, email, qatarid, "Arabic",
//								comments, location, createdate, date_aff,
//								issue, "Failed", issueid, spid, "" + locx, ""
//										+ locy, location, affqatarid, spname);
//						if (!(ln > -1)) {
//							throw new Exception();
//						}
//						ArrayList<Mail_OFF> arr = datasource
//								.getLastReports_off();
//						datasource.close();
//						return 0;
//					} catch (Exception e1) {
//						return 0;
//					}
//				}
//			} catch (Exception e) {
//				return 0;
//			}
//		}
//
//		@Override
//		public void onPostExecute(Integer result) {
//
//			dialog.dismiss();
//			if (result == 0) {
//				Intent intent = new Intent(SendReportActivity.this,
//						FailedReportActivity.class);
//				startActivity(intent);
//			} else {
//				Intent intent = new Intent(SendReportActivity.this,
//						SuccessReportConfirmActivity.class);
//				startActivity(intent);
//			}
//
//		}
//
//	}

    public class sendreq extends AsyncTask<Void, Void, Integer> {
        com.ids.ict.Error error;
        Event[] nn;

        @Override
        public void onPreExecute() {
            /*if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
                dialog = ProgressDialog.show(SendReportActivity.this, "",
                        "Loading..Wait..", true);
            } else {
                dialog = ProgressDialog.show(SendReportActivity.this, "",
                        "الرجاء الإنتظار...", true);
            }*/

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public Integer doInBackground(Void... params) {
            Log.d("launching", "passed here");
            try {
                Connection conn = null;
                conn = new Connection("" + app.link + "PostData.asmx/SendReport");
                try {
                    s = conn.executeMultipartPost_event(
                            "PostData.asmx/SendReport3", gettok, mobilenumber,
                            affmobile, qatarid, email, issueid, spid, "1",
                            createdate, sendingdate, comments, locx, locy,
                            countryid, affqatarid, spname, IssueType,
                            spComplaint, longWait, cmplNum, cmplDate, callNum,
                            calldate, isIndividual, isCorporate, cRNumber, "6",
                            channelusedid, isspecialneed, specialneednumber, dateOfRequest, areaId, subAreaId, street, building, zone, contactNum);

                    String m = splitres(s);
                    try {
                        k = Integer.parseInt(m);
                    } catch (Exception e) {
                        k = 0;
                    }
                    TCTDbAdapter datasource = new TCTDbAdapter(
                            SendReportActivity.this);
                    datasource.open();
                    if (k > 0) {
                        try {
                            long ln = datasource.createReports_off(pass,
                                    mobilenumber, email, qatarid, "English",
                                    comments, location, createdate, date_aff,
                                    issue, "sended", issueid, spid, "" + locx,
                                    "" + locy, location, affqatarid, spname);
                            if (!(ln > -1)) {
                                throw new Exception();
                            }
                            return 1;
                        } catch (Exception e) {
                            return 0;
                        }
                    } else {
                        try {
                            long ln = datasource.createReports_off(pass,
                                    mobilenumber, email, qatarid, "English",
                                    comments, location, createdate, date_aff,
                                    issue, "Failed", issueid, spid, "" + locx,
                                    "" + locy, location, affqatarid, spname);
                            if (!(ln > -1)) {
                                throw new Exception();
                            }
                            Actions.onCreateDialog1(SendReportActivity.this,
                                    "Connection to server cannot be verified. Please try again.");
                            datasource.close();
                            return 0;
                        } catch (Exception e) {
                            datasource.close();
                        }
                        return 0;
                    }

                } catch (Exception e) {
                    try {
//                        TCTDbAdapter datasource = new TCTDbAdapter(
//                                SendReportActivity.this);
//                        datasource.open();
//                        long ln = datasource.createReports_off(pass,
//                                mobilenumber, email, qatarid, "English",
//                                comments, location, createdate, date_aff,
//                                issue, "Failed", issueid, spid, "" + locx, ""
//                                        + locy, location, affqatarid, spname);
//                        if (!(ln > -1)) {
//                            throw new Exception();
//                        }
//                        ArrayList<Mail_OFF> arr = datasource
//                                .getLastReports_off();
//                        datasource.close();
                        Mail_OFF tick = new Mail_OFF();
                        tick.setAreaId(areaId);
                        tick.setBuilding(building);
                        tick.setPassword(pass);
                        tick.setStreet(street);
                        tick.setToken(gettok);
                        tick.setnum(mobilenumber);
                        tick.setAffectedNumber(affmobile);
                        tick.setqatarID(qatarid);
                        tick.setemail(email);
                        tick.setissueid(issueid);
                        tick.setspid(spid);
                        tick.setcomm(spComplaint);
                        tick.setlocy(latitude + "");
                        tick.setlocx(longitude + "");
                        tick.setloc(location);
                        tick.setaffcqatar(qatarid);
                        tick.setissuename(IssueType);
                        tick.setdate(date);
                        tick.setChannelUsedId(channelusedid);
                        tick.setIsSpecialNeed("" + isspecialneed);
                        tick.setSpecialneednumber(specialneednumber);
                        tick.setDateOfRequest(dateOfRequest);
                        tick.setAreaId(areaId);
                        tick.setSubAreaId(subAreaId);
                        tick.setStreet(street);
                        tick.setBuilding(building);
                        tick.setZone(zone);
                        tick.setContactNumber(contactNum);


                        SharedPreference settings = new SharedPreference();
                        ArrayList<Mail_OFF> array = settings
                                .getFavorites(getApplicationContext());
                        if (array == null) {
                            Log.wtf("null", "array");
                            array = new ArrayList<Mail_OFF>();
                            array.add(tick);
                            settings.saveFavorites(getApplicationContext(), array);
                        } else
                            settings.addFavorite(getApplicationContext(),
                                    tick);
                        // editEn.putString(code, nameen);
                        // editAr.putString(code, namear);
                        //edit.commit();
                        return 0;
                    } catch (Exception e1) {
                        return 0;
                    }
                }
            } catch (Exception e) {
                return 0;
            }
        }

        @Override
        public void onPostExecute(Integer result) {
            try {
                //dialog.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                if (result == 0) {
                    IssueType = "-1";
                }
                Intent intent = new Intent(SendReportActivity.this,
                        SuccessReportConfirmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("IssueType", IssueType);
                intent.putExtras(bundle);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SendReportActivity.this.finish();
            } catch (Exception e) {
                String url1 = "" + app.link
                        + "PostData.asmx/VerifyRegistration";
                Connection conn = new Connection(url1);

                try {
                    String error_return = conn.executeMultipartPost_Send_Error(
                            this.getClass().getSimpleName(),
                            Actions.getDeviceName(), "1", e.getMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {

        Log.wtf("SendReport","Pressing back");

        if (!getIntent().getExtras().getString("from").equals("ticket")) {

            //Actions.setLocalComplaint(SendReportActivity.this);
        }else{
            finish();
        }
    }

//		if (gettok.equalsIgnoreCase("-1")) {
//
//			if (IssueType.equalsIgnoreCase("2")) {
//				MyApplication.lunched = -2;
//			} else {
//				MyApplication.lunched = -1;
//			}
//			Intent intent = new Intent();
//			intent.setClass(SendReportActivity.this, ComplaintActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//			SendReportActivity.this.startActivity(intent);
//			SendReportActivity.this.finish();
//		} else {
//			SendReportActivity.this.finish();
//		}
//	}

    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }

    public void backTo(View v) {
        Actions.backTo(this);
    }

    public String splitres(String res) {
        String[] a = res.split(">");
        String[] b = a[2].split("</");
        return b[0];
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
        }
        if (arr2.size() > 0) {
            n1 = arr2.toArray(new Event[arr2.size()]);
        }
        for (int i = 0; i < n.length; i++) {
            values[i] = n[i];
            k++;
        }
        for (int i = 0; i < n1.length; i++) {
            values[k] = n1[i];
            k++;
        }
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

    protected class LaunchingEvent extends AsyncTask<Void, Void, Integer> {
        com.ids.ict.Error error;
        Event[] nn, nn1, nn2;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Log.d("launching", "passed here");
            TCTDbAdapter source = new TCTDbAdapter(SendReportActivity.this);
            source.open();
            AtomicReference<Event[]> ref = new AtomicReference<Event[]>(null);
            String lan;
            String eventsSource = "" + app.link
                    + "GeneralServices.asmx/GetIssueTypes?";
            eventsSource = eventsSource
                    + "language=en&password=&mainIssueTypeId=1";
            Log.d("eventsource", eventsSource);
            AtomicReference<Event[]> ref1 = new AtomicReference<Event[]>(null);
            eventsSource = ""
                    + app.link
                    + "GeneralServices.asmx/GetIssueTypes?language=en&password=&mainIssueTypeId=2";
            Log.d("eventsource", eventsSource);
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

//	public void sendRequest() {
//		Connection conn = null;
//		conn = new Connection("" + app.link + "PostData.asmx/SendReport");
//		try {
//			s = conn.executeMultipartPost_event("" + app.link
//					+ "PostData.asmx/SendReport3", gettok, affmobile,
//					mobilenumber, qatarid, email, issueid, spid, "1",
//					createdate, sendingdate, comments, locx, locy, countryid,
//					affqatarid, spname, IssueType, spComplaint, longWait,
//					cmplNum, cmplDate, callNum, calldate, isIndividual,
//					isCorporate, cRNumber, "6", channelusedid, isspecialneed,
//					specialneednumber);
//			System.out.println("String  " + s);
//			String m = splitres(s);
//			try {
//				k = Integer.parseInt(m);
//			} catch (Exception e) {
//				k = 0;
//			}
//			TCTDbAdapter datasource = new TCTDbAdapter(SendReportActivity.this);
//			datasource.open();
//			if (k > 0) {
//				try {
//					long ln = datasource.createReports_off(pass, mobilenumber,
//							email, qatarid, "English", comments, location,
//							createdate, date_aff, issue, "sended", issueid,
//							spid, "" + locx, "" + locy, location, affqatarid,
//							spname);
//					if (!(ln > -1)) {
//						throw new Exception();
//					}
//					Intent intent = new Intent(SendReportActivity.this,
//							SuccessReportConfirmActivity.class);
//					startActivity(intent);
//
//				} catch (Exception e) {
//					String url1 = "" + app.link
//							+ "PostData.asmx/VerifyRegistration";
//					Connection conn1 = new com.ids.ict.classes.Connection(url1);
//					try {
//						String error_return = conn1
//								.executeMultipartPost_Send_Error(this
//										.getClass().getSimpleName(), Actions
//										.getDeviceName(), "1", e.getMessage());
//					} catch (Exception e1) {
//						e1.printStackTrace();
//					}
//				}
//			} else {
//				try {
//
//					long ln = datasource.createReports_off(pass, mobilenumber,
//							email, qatarid, "English", comments, location,
//							createdate, date_aff, issue, "Failed", issueid,
//							spid, "" + locx, "" + locy, location, affqatarid,
//							spname);
//					if (!(ln > -1)) {
//						throw new Exception();
//					}
//					ArrayList<Mail_OFF> arr = datasource.getLastReports_off();
//					Intent intent = new Intent(SendReportActivity.this,
//							FailedReportActivity.class);
//					startActivity(intent);
//					datasource.close();
//				} catch (Exception e) {
//					datasource.close();
//				}
//			}
//
//		} catch (Exception e) {
//			try {
//				TCTDbAdapter datasource = new TCTDbAdapter(
//						SendReportActivity.this);
//				datasource.open();
//				long ln = datasource.createReports_off(pass, mobilenumber,
//						email, qatarid, "English", comments, location,
//						createdate, date_aff, issue, "Failed", issueid, spid,
//						"" + locx, "" + locy, location, affqatarid, spname);
//				if (!(ln > -1)) {// case row not inserted, may be duplicated
//					throw new Exception();
//				}
//				ArrayList<Mail_OFF> arr = datasource.getLastReports_off();
//				Intent intent = new Intent(SendReportActivity.this,
//						FailedReportActivity.class);
//				startActivity(intent);
//				datasource.close();
//			} catch (Exception e1) {
//			}
//		}
//	}

    public void topBarBack(View v) {

        if (gettok.equalsIgnoreCase("-1")) {
            if (IssueType.equalsIgnoreCase("2")) {
                MyApplication.lunched = -2;
            } else {
                MyApplication.lunched = -1;
            }
            Intent intent = new Intent();
            intent.setClass(SendReportActivity.this, ComplaintActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            MyApplication.lunched = -1;
            SendReportActivity.this.startActivity(intent);
            SendReportActivity.this.finish();
        } else {
            SendReportActivity.this.finish();
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

    private void setLookups() {

        try {

            final TextView textView1 = (TextView) findViewById(R.id.textView14);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                textView1.setText(getLookup("17").namear);
            } else {
                textView1.setText(getLookup("17").nameen);
            }
            textView1.setTypeface(tf);

            final TextView textView2 = (TextView) findViewById(R.id.textView7);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                textView2.setText(getLookup("18").namear);
            } else {
                textView2.setText(getLookup("18").nameen);
            }
            textView2.setTypeface(tf);

            TextView waittimetxt = (TextView) findViewById(R.id.waittimetxt);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                waittimetxt.setText(getLookup("11").namear);
            } else {
                waittimetxt.setText(getLookup("11").nameen);
            }
            //
            // final RadioButton radioButton1 = (RadioButton)
            // findViewById(R.id.radioButton1);
            // if(MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)){
            // radioButton1.setText(getLookup("3").namear);
            // }else{
            // radioButton1.setText(getLookup("3").nameen);
            // }
            // radioButton1.setTypeface(tf);
            //
            // final RadioButton radioButton2 = (RadioButton)
            // findViewById(R.id.radioButton2);
            // if(MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)){
            // radioButton2.setText(getLookup("4").namear);
            // }else{
            // radioButton2.setText(getLookup("4").nameen);
            // }
            // radioButton2.setTypeface(tf);
            //
            // final RadioButton radioButton3 = (RadioButton)
            // findViewById(R.id.radioButton3);
            // if(MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)){
            // radioButton3.setText(getLookup("5").namear);
            // }else{
            // radioButton3.setText(getLookup("5").nameen);
            // }
            // radioButton3.setTypeface(tf);

            final TextView textView3 = (TextView) findViewById(R.id.textView8);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                textView3.setText(getLookup("21").namear);
            } else {
                textView3.setText(getLookup("21").nameen);
            }
            textView3.setTypeface(tf);

            final TextView textView4 = (TextView) findViewById(R.id.textView10);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                textView4.setText(getLookup("19").namear);
            } else {
                textView4.setText(getLookup("19").nameen);
            }
            textView4.setTypeface(tf);

            final TextView textView7 = (TextView) findViewById(R.id.textView15);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                textView7.setText(getLookup("20").namear);
            } else {
                textView7.setText(getLookup("20").nameen);
            }
            textView7.setTypeface(tf);
        } catch (Exception e) {

        }
    }

}
