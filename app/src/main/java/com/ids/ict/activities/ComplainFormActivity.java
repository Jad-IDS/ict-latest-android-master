package com.ids.ict.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.IssuesDetails;
import com.ids.ict.classes.Mail_OFF;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.Profile;
import com.ids.ict.R;
import com.ids.ict.classes.ServicePro;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.classes.Area;
import com.ids.ict.classes.InitialMapSettings;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.SharedPreference;
import com.ids.ict.classes.SubArea;
import com.ids.ict.parser.InitialMapSettingsXMLPullParserHandler;
import com.ids.ict.parser.SpeedTestResultParser;

import org.shipp.util.DatabaseHandler;
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

import static com.ids.ict.Actions.onCreateDialogNew;

public class ComplainFormActivity extends FragmentActivity implements TextWatcher {

    Spinner SpSpin, IssueSpin;
    ScrollView mScrollView;
    boolean isSpecialNeed = false, append = true, append2 = true;
    private KeyListener listener;
    ProgressDialog dialog;
    public boolean closed = false;
    String specialneednumber = "", compaintstatusid = "",
            specialneedduration = "";
    int IssuetypeId = 0;
    int footerButton, issue_id = 0, k = 0;
    String lang, cmpTime, s = "", gettok, provider, reg_id,
            qatarID = "11111111111", num, emailtxt, spComplaint,
            longWait = "false";
    String IssueType, issueName, issueMobModify, defMobile, issueID, spName,
            spId, address, waitDuration, waitUnit, specialneedwaitunit;
    MyApplication app;
    Bundle bundle;
    ArrayList<Event> listCateg;
    String checkapponDate = "false";
    ArrayAdapter<Event> spinnerCateg;
    ArrayList<IssuesDetails> IssuesDetailsList;
    ArrayList<ServicePro> ServiceProList;
    EditText number, qidEdt, referenceNumbertext, cmpdate, callNumEdt, dateEdt, complaint, calldate;
    LinearLayout qidlayout, ComplaintReferenceLayout, locationlayout, LocationSameLayout, dateOfRequestLayout, numbercalledlayout, CommentLayout;
    RadioButton radioMainIndv, radioMainCorp, didyoucomplain1, didyoucomplain2,
            channelanswer1, channelanswer2, locationsame1, locationsame2,
            channelanswer3, channelanswer4, status1, status2, RadioButtonWhenWasLodged1, RadioButtonWhenWasLodged2;

    RadioGroup radioGroupMainIndvCooporate, radioGroupDidYouComplain, statusradiogroup,
            channelradio, radioGroupWhenWasLodged, radiogrouplocationsame;
    TextView headerTxt, issue_label, phone_label, radioGroupMainTxt, cmpdateTxt, callNumTxt, commentLabel,
            qidTxt, Didyoucompalinquestion, channelquestion, statusquestion, mapLayoutTxt, newlocation,
            WhenWasLodgedText, referncenumberTitle, locCurrentTxt, loclabeldet, locationsametext, ivDatetxt;
    private DatePicker datePicker;
    TextView ivDate;
    private Calendar calendar;
    private int year, month, day, year2, month2, day2;
    String date = "1/1/1900", showOnMap, spTransfer;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;
    // Map Values
    Marker marker;
    GoogleMap googleMap;
    static LatLng location;
    Typeface tf;
    private float zoom, initialzoom;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    boolean waitingForLocationUpdate = true, ctgSelected = false,
            lineSelected = false;
    Double longitude = 0.0, latitude = 0.0, newlongitude = 0.0,
            newlatitude = 0.0;
    final String loc = "165";
    String isIndividual;
    String isCorporate;

    String cRNumber;
    private TextView nloca;
    AlertDialog d;
    LinearLayout linear;
    private boolean allow = false;
    TextView specialneednumbertxt;
    EditText etspecialneednb;
    private int mYear, mMonth, mDay;
    private DatePickerDialog datePickerDialog;
    TextView txtRadio;

    ArrayList<Area> areas = new ArrayList<Area>();
    String areaId = "0", subareaId = "0", dateiv = "";
    private Calendar todate;
    TextView buildingtxt, zonetxt, streettxt;
    EditText etbuilding, etzone, etstreet;
    //   private  todate;


    int normalDays, specialDays;
    SharedPreferences mshSharedPreferences;
    SharedPreferences.Editor edit;

    boolean sendwaittime = true;

    private EditText comp_phone;
    private LinearLayout contact_comp, comp_mobNumLayout;
    String contactNum;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplicationContext();
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        lang = Actions.setLocalComplaint(this);

        String spLang;
        TCTDbAdapter source = new TCTDbAdapter(ComplainFormActivity.this);
        source.open();

       if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            setContentView(R.layout.activity_form_en);
            tf = MyApplication.facePolarisMedium;
            spLang = "English";
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);
        } else {
            setContentView(R.layout.activity_form_ar);
            tf = MyApplication.faceDinar;
            spLang = "Arabic";
      }
        mshSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        edit = mshSharedPreferences.edit();

        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        ViewResizing.setPageTextResizing(ComplainFormActivity.this);
        areas = source.GetAreas();
        for (int i = 0; i < areas.size(); i++) {
            ArrayList<SubArea> subs = new ArrayList<SubArea>();
            subs = source.GetSubAreas(String.valueOf(areas.get(i).getId()));
            areas.get(i).setSubAreas(subs);
        }

//
//        ActivityCompat.requestPermissions(ComplainFormActivity.this,
//                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                1);
        final Button submit = (Button) findViewById(R.id.submit);
        txtRadio = (TextView) findViewById(R.id.Didyoucompalinquestion);
        txtRadio.setVisibility(View.GONE);
        //  getWindow().setSoftInputMode(
        //        WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
            footer.setBackgroundResource(R.drawable.footer_nt);
            final RelativeLayout ll = (RelativeLayout) findViewById(R.id.mainlayout);
            ll.setBackgroundColor(getResources().getColor(R.color.nightBlue));


            //submit.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            submit.setBackground(ContextCompat.getDrawable(ComplainFormActivity.this, R.drawable.button_night));


            final TextView headerTxt = (TextView) findViewById(R.id.complaint_header);
            headerTxt.setBackgroundColor(getResources().getColor(
                    R.color.nightBlue));
            headerTxt.setTextColor(getResources().getColor(R.color.white));

            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
                setRbDrawable();
            } else {
                setRbDrawableAR();
            }
        }
        dateOfRequestLayout = (LinearLayout) findViewById(R.id.dateOfRequestLayout);//check if delay in installation to show

        specialneednumbertxt = (TextView) findViewById(R.id.SpecialNeedTxt);
        etspecialneednb = (EditText) findViewById(R.id.specialneednumber);
        etspecialneednb.setTypeface(MyApplication.facePolarisMedium);
        channelquestion = (TextView) findViewById(R.id.channelquestion);
        channelquestion.setVisibility(View.GONE);
        statusquestion = (TextView) findViewById(R.id.statusquestion);
        linear = (LinearLayout) findViewById(R.id.qidlayout);

        comp_mobNumLayout = (LinearLayout) findViewById(R.id.comp_mobNumLayout);
        contact_comp = (LinearLayout) findViewById(R.id.contact_comp);

        streettxt = (TextView) findViewById(R.id.streettxt);
        buildingtxt = (TextView) findViewById(R.id.buildingtxt);
        zonetxt = (TextView) findViewById(R.id.zonetxt);
        etzone = (EditText) findViewById(R.id.etzone);
        etstreet = (EditText) findViewById(R.id.etstreet);
        etbuilding = (EditText) findViewById(R.id.etbuilding);

        etzone.setTypeface(MyApplication.facePolarisMedium);
        etstreet.setTypeface(MyApplication.facePolarisMedium);
        etbuilding.setTypeface(MyApplication.facePolarisMedium);

        comp_phone = (EditText) findViewById(R.id.comp_phone);
        comp_phone.setTypeface(MyApplication.facePolarisMedium);
        //InitializeAreaSpinner();
        statusquestion.setVisibility(View.GONE);
        statusradiogroup = (RadioGroup) findViewById(R.id.statusradiogroup);
        statusradiogroup.setVisibility(View.GONE);
        cmpdateTxt = (TextView) findViewById(R.id.cmpdateTxt);
        cmpdateTxt.setTypeface(tf);
        status1 = (RadioButton) findViewById(R.id.status1);
        status2 = (RadioButton) findViewById(R.id.status2);
        status1.setTypeface(tf);
        status2.setTypeface(tf);
        channelradio = (RadioGroup) findViewById(R.id.channelradio);
        channelradio.setVisibility(View.GONE);
        channelanswer1 = (RadioButton) findViewById(R.id.channelanswer1);
        channelanswer2 = (RadioButton) findViewById(R.id.channelanswer2);
        channelanswer3 = (RadioButton) findViewById(R.id.channelanswer3);
        channelanswer4 = (RadioButton) findViewById(R.id.channelanswer4);
        radioGroupMainIndvCooporate = (RadioGroup) findViewById(R.id.radioGroupMainIndvCooporate);
        radioGroupDidYouComplain = (RadioGroup) findViewById(R.id.radioGroupDidYouComplain);
        radioGroupDidYouComplain.setVisibility(View.GONE);
        radioGroupWhenWasLodged = (RadioGroup) findViewById(R.id.radioGroupWhenWasLodged);
        radiogrouplocationsame = (RadioGroup) findViewById(R.id.radiogrouplocationsame);
        RadioButtonWhenWasLodged1 = (RadioButton) findViewById(R.id.RadioButtonWhenWasLodged1);
        RadioButtonWhenWasLodged1.setTypeface(tf);
        RadioButtonWhenWasLodged2 = (RadioButton) findViewById(R.id.RadioButtonWhenWasLodged2);
        RadioButtonWhenWasLodged2.setTypeface(tf);
        ivDate = (TextView) findViewById(R.id.ivDate);
        ivDatetxt = (TextView) findViewById(R.id.ivDatetxt);
        ComplaintReferenceLayout = (LinearLayout) findViewById(R.id.ComplaintReferenceLayout);
        numbercalledlayout = (LinearLayout) findViewById(R.id.numbercalledlayout);
        locationlayout = (LinearLayout) findViewById(R.id.locationlayout);
        //   MapLayout = (LinearLayout) findViewById(R.id.AreaLayout);
        LocationSameLayout = (LinearLayout) findViewById(R.id.LocationSameLayout);
        CommentLayout = (LinearLayout) findViewById(R.id.CommentLayout);
        final TextView cmpText = (TextView) findViewById(R.id.commentLabel);

        cmpText.setTypeface(tf);
        WhenWasLodgedText = (TextView) findViewById(R.id.WhenWasLodgedText);
        qidTxt = (TextView) findViewById(R.id.qidTxt);
        qidTxt.setTypeface(tf);
        number = (EditText) findViewById(R.id.phone);
        number.setTypeface(MyApplication.facePolarisMedium);
        ivDate.setTypeface(MyApplication.facePolarisMedium);
        //number.setTypeface(tf);
        listener = number.getKeyListener();
        referenceNumbertext = (EditText) findViewById(R.id.referenceNumbertext);
        referenceNumbertext.setTypeface(MyApplication.facePolarisMedium);
        cmpdate = (EditText) findViewById(R.id.date);
        //cmpdate.setTypeface(tf);
        callNumEdt = (EditText) findViewById(R.id.callNumEdt);
        callNumEdt.setTypeface(MyApplication.facePolarisMedium);
        //  callNumEdt.setTypeface(tf);
        qidEdt = (EditText) findViewById(R.id.qidEdt);
        // qidEdt.setTypeface(tf);
        qidEdt.setTypeface(MyApplication.facePolarisMedium);
        calldate = (EditText) findViewById(R.id.dateEdt);


        //calldate.setTypeface(tf);
        calldate.setTypeface(MyApplication.facePolarisMedium);
        complaint = (EditText) findViewById(R.id.complaint);
        complaint.setTypeface(tf);
        locationsame1 = (RadioButton) findViewById(R.id.locationsame2);
        locationsame1.setTypeface(tf);
        locationsame2 = (RadioButton) findViewById(R.id.locationsame2);
        locationsame2.setTypeface(tf);
        radioGroupMainTxt = (TextView) findViewById(R.id.IndividualCooporateTxt);
        radioGroupMainTxt.setTypeface(tf);

        try {
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                radioGroupMainTxt.setText(getLookup("35").namear);
            } else {
                radioGroupMainTxt.setText(getLookup("35").nameen);
            }
        } catch (Exception e) {

        }
        mScrollView = (ScrollView) findViewById(R.id.scroll);
        bundle = this.getIntent().getExtras();
        issue_id = bundle.getInt("MainIssueId");

       try{ IssueType = bundle.getString("IssueType");}catch (Exception e){
           IssueType="";
       }
       if(IssueType==null)
           IssueType="";



        showOnMap = bundle.getString("showOnMap");
        spTransfer = bundle.getString("ServiceProviderTransfer");
        IssuetypeId = bundle.getInt("eventId");

        if (IssuetypeId == 1) {
            int off = 0;

            try {
                off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Exception e) {
                off = 0;
            }

            if (off == 0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ComplainFormActivity.this, R.style.AlertDialogCustom));
                builder
                        .setMessage(ComplainFormActivity.this.getResources().getString(R.string.enablegps2))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.ok2), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(onGPS, 0);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel2), new DialogInterface.OnClickListener() {
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

        String id = Integer.toString(IssuetypeId);
        IssuesDetailsList = source.getissue_detail(id, spLang);

       // IssuesDetailsList = source.getissue_detail();
        ServiceProList = source.getsp();

        if (spTransfer.equalsIgnoreCase("true")) {

            ServiceProList = source.getsp_lang(spLang);
        } else {

            ServiceProList = new ArrayList<ServicePro>();
            ArrayList<ServicePro> temp = source.getsp_lang(spLang);
            for (int k = 0; k < temp.size(); k++) {
                if (temp.get(k).getistrans().equalsIgnoreCase("false"))
                    ServiceProList.add(temp.get(k));
            }
        }
        Log.wtf("ServiceProList","size : " + ServiceProList.size());
        source.close();


        SpSpin = (Spinner) findViewById(R.id.spSpinner);
        IssueSpin = (Spinner) findViewById(R.id.issueSpinner);

        final ArrayList<IssuesDetails> overAllList = new ArrayList<>();
        IssuesDetails firstElement = new IssuesDetails();
        firstElement.setId(-1);
        //firstElement.setName(getResources().getString(R.string.choose_issue_type));
        firstElement.setName("");
        firstElement.setCheckappondate("false");
        /*if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            firstElement.setName(getLookup("98").nameen);
        } else {

            firstElement.setName(getLookup("98").namear);
        }*/

        //hsen spin
        overAllList.add(firstElement);
        overAllList.addAll(IssuesDetailsList);
        initIssueDetailSp(IssueSpin, overAllList);


        final ArrayList<ServicePro> overAllSPList = new ArrayList<>();
        ServicePro firstSpElement = new ServicePro();
        firstSpElement.setId(-1);
        /*if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            firstSpElement.setName(getLookup("101").nameen);
        } else {

            firstSpElement.setName(getLookup("101").namear);
        }*/

        firstSpElement.setName("");
        overAllSPList.add(firstSpElement);
        overAllSPList.addAll(ServiceProList);

        //hsen spin
        initializeSpSpin(SpSpin, overAllSPList);
        SpSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                try {
                    spName = overAllSPList.get(position).getName();
                    spId = Integer.toString(overAllSPList.get(position)
                            .getId());
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });


        //hsen test null
        //<editor-fold desc="setting initial value for cmpTime
       try {
           waitDuration = IssuesDetailsList.get(0)
                   .getWaitDuration();
       }catch (Exception e){}

      try {
          if (MyApplication.Lang
                  .equalsIgnoreCase(MyApplication.ARABIC)) {
              waitUnit = IssuesDetailsList.get(0)
                      .getWaitUnitAr();
              specialneedwaitunit = IssuesDetailsList.get(0)
                      .getSpecialneedUnitAr();
          } else {
              waitUnit = IssuesDetailsList.get(0)
                      .getWaitUnit();
              specialneedwaitunit = IssuesDetailsList.get(0)
                      .getSpecialneedUnit();
          }
      }catch (Exception e){}
      try {
          issueID = Integer.toString(IssuesDetailsList.get(0)
                  .getId());
      }catch (Exception e){}
          cmpTime = waitDuration + " " + waitUnit;
        try{  specialneedduration = IssuesDetailsList.get(0)
                  .getSpecialNeedDuration() + " " + specialneedwaitunit;}catch (Exception e){}

        //</editor-fold>


        IssueSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                try {
                    checkapponDate = overAllList.get(position).getCheckappondate();

                    issueName = overAllList.get(position).getName();

                    issueMobModify = overAllList.get(position)
                            .getmodifyNum();
                    if (issueMobModify.equalsIgnoreCase("false")) {
                        number.setKeyListener(null);
                        number.setText(defMobile);
                        Log.wtf("534","defMobile = " + defMobile);
                        spId = "5";
                    } else {
                        number.setKeyListener(listener);
                    }
                    waitDuration = overAllList.get(position)
                            .getWaitDuration();

                    if (MyApplication.Lang
                            .equalsIgnoreCase(MyApplication.ARABIC)) {
                        waitUnit = overAllList.get(position)
                                .getWaitUnitAr();
                        specialneedwaitunit = overAllList.get(position)
                                .getSpecialneedUnitAr();
                    } else {
                        waitUnit = overAllList.get(position)
                                .getWaitUnit();
                        specialneedwaitunit = overAllList.get(position)
                                .getSpecialneedUnit();
                    }
                    issueID = Integer.toString(overAllList.get(position)
                            .getId());
                    cmpTime = waitDuration + " " + waitUnit;
                    specialneedduration = overAllList.get(position)
                            .getSpecialNeedDuration() + " " + specialneedwaitunit;
                    if (!IssueType.equals("2")) {
                        if (checkapponDate.equals("true"))
                            resetForm();
                        else {
                            calldate.setClickable(true);
                            calldate.setEnabled(true);
                            dateOfRequestLayout.setVisibility(View.GONE);
                            ivDate.setText("");
                            cmpdate.setEnabled(true);
                            cmpdate.setClickable(true);
                            cmpdate.setText("");
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });


        RadioButtonWhenWasLodged1.setText(getResources().getString(R.string.more_than) + " " + cmpTime);
        RadioButtonWhenWasLodged2.setText(getResources().getString(R.string.less_than) + " " + cmpTime);
        try {
            waitDuration = IssuesDetailsList.get(0).getWaitDuration();
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                waitUnit = IssuesDetailsList.get(0).getWaitUnitAr();
            } else {
                waitUnit = IssuesDetailsList.get(0).getWaitUnit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

try{        issueMobModify = IssuesDetailsList.get(0).getmodifyNum();}catch (Exception e){}
       try{ if (issueMobModify.equalsIgnoreCase("false")) {
            number.setKeyListener(null);
            spId = "5";
        }}catch (Exception e){}

        if (MyApplication.Lang.equalsIgnoreCase("en")) {
            radioGroupDidYouComplain.setGravity(Gravity.RIGHT);
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        year2 = year;
        month = calendar.get(Calendar.MONTH);

        day = calendar.get(Calendar.DAY_OF_MONTH);
        day2 = day;

        month = month + 1;
        month2 = month;
        date = year + "-" + month + "-" + day;
        // showDate(year, month + 1, day);
        setLookups();

        new Thread(new Task()).start();

        if (IssueType.equalsIgnoreCase("2")) { //inquiry

            //for affected number
            contact_comp.setVisibility(View.GONE);
            comp_mobNumLayout.setVisibility(View.GONE);

            isIndividual = "false";
            dateiv = date;
            phone_label = (TextView) findViewById(R.id.phone_label);
            phone_label.setText(getString(R.string.contactnumber));

            txtRadio.setVisibility(View.GONE);
            radioGroupDidYouComplain.setVisibility(View.GONE);
            CommentLayout.setVisibility(View.VISIBLE);
            //   CommentLayout.requestFocus();
            radioGroupMainIndvCooporate.setVisibility(View.VISIBLE);
            radioGroupMainTxt.setVisibility(View.VISIBLE);
            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {


                    if (((IssuesDetails) IssueSpin.getSelectedItem()).getId() == -1) {

                        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                            onCreateDialogNew(ComplainFormActivity.this, getLookup("98").nameen);
                        } else {

                            onCreateDialogNew(ComplainFormActivity.this, getLookup("98").namear);
                        }


                    } else {

                        //Toast.makeText(ComplainFormActivity.this, "1", Toast.LENGTH_SHORT).show();


                        if (qidEdt.getText().toString().equals("")) {
                            if (isIndividual.equalsIgnoreCase("true")) {
                                onCreateDialogNew(ComplainFormActivity.this,
                                        getResources().getString(R.string.error_mob_qid3));
                            } else {
                                onCreateDialogNew(ComplainFormActivity.this,
                                        getResources().getString(R.string.error_mob_crnum2));
                            }
                        } else {

                            if (isIndividual.equalsIgnoreCase("true") && qidEdt.getText().toString().length() < 8) {
                                onCreateDialogNew(ComplainFormActivity.this, getResources().getString(R.string.error_mob_qid3));
                            } else {


                                if (Actions.isNetworkAvailable(ComplainFormActivity.this)) {

                                    new IsBlocked().execute(defMobile);
                                } else {
                                    if (mshSharedPreferences.getString(getResources().getString(R.string.is_blocked), "").equals("")) {

                                        /*edit.putString(getResources().getString(R.string.is_blocked), "true").apply();
                                //alert blocked
                                if (MyApplication.Lang.equals(MyApplication.ARABIC))
                                    Actions.onCreateBlockedDialog2(ComplainFormActivity.this, getLookup("94").namear);
                                else
                                    Actions.onCreateBlockedDialog2(ComplainFormActivity.this,  getLookup("94").nameen);*/

                                        sendreq eventLaunching = new sendreq();
                                        eventLaunching.execute();
                                    } else {
                                        if (mshSharedPreferences.getString(getResources().getString(R.string.is_blocked), "").equals("true")) {
                                            //alert blocked;
                                            if (MyApplication.Lang.equals(MyApplication.ARABIC))
                                                Actions.onCreateBlockedDialog2(ComplainFormActivity.this, getLookup("94").namear);
                                            else
                                                Actions.onCreateBlockedDialog2(ComplainFormActivity.this, getLookup("94").nameen);
                                        } else {
                                            //send offline;
                                            sendreq eventLaunching = new sendreq();
                                            eventLaunching.execute();
                                        }
                                    }

                                }
                            }



                        /*sendreq eventLaunching = new sendreq();
                        eventLaunching.execute();*/


                            //<editor-fold>
                        /*if (IssuetypeId == 1) {
                            if (etzone.getText().toString().equals("") || etstreet.getText().toString().equals("")
                                    || etbuilding.getText().toString().equals("")) {
                                if (MyApplication.Lang.equals(MyApplication.ENGLISH))
                                    Actions.onCreateDialogNew(ComplainFormActivity.this,
                                            getLookup("72").nameen);
                                else
                                    Actions.onCreateDialogNew(ComplainFormActivity.this,
                                            getLookup("72").namear);
                            } else {
                                sendreq eventLaunching = new sendreq();
                                eventLaunching.execute();
                            }
                        } else {
                            sendreq eventLaunching = new sendreq();
                            eventLaunching.execute();
                        }*/
                            //</editor-fold>
                        }
                    }


                }
            });
        }

//        int status = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(getBaseContext());
//        if (status != ConnectionResult.SUCCESS) {
//            int requestCode = 10;
//            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
//                    requestCode);
//            dialog.show();
//
//        } else {
//            if (googleMap == null) {
//                googleMap = ((WorkaroundMapFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.map)).getMap();
//                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                googleMap.getUiSettings().setZoomControlsEnabled(true);
//            }
//            ((WorkaroundMapFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.map))
//                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
//                        @Override
//                        public void onTouch() {
//                            mScrollView
//                                    .requestDisallowInterceptTouchEvent(true);
//                        }
//                    });
        // }

        //   registerLocationUpdates();

//        googleMap.setOnMarkerDragListener(new OnMarkerDragListener() {
//
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//
//            }
//
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//                LatLng newPos = marker.getPosition();
//                newlatitude = newPos.latitude;
//                newlongitude = newPos.longitude;
//                nloca = (TextView) findViewById(R.id.locnew);
//                nloca.setTypeface(tf);
//                nloca.setText(getcountrycode1(newlatitude, newlongitude));
//                address = nloca.getText().toString();
//                CommentLayout.requestFocus();
//
//            }
//
//            @Override
//            public void onMarkerDrag(Marker marker) {
//            }
//        });


        radioGroupMainIndvCooporate
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.radioMainIndv:
                                RadioButton bb = (RadioButton) findViewById(checkedId);
                                if (bb.isChecked()) {
                                    qidTxt.setText(getResources().getString(
                                            R.string.indv2));
                                    linear.setVisibility(View.VISIBLE);

                                    isIndividual = "true";
                                    isCorporate = "false";
                                    qidEdt.setText("");

                                    RadioButtonWhenWasLodged1.setText(getResources()
                                            .getString(
                                                    R.string.more_than)
                                            + " " + cmpTime);
                                    RadioButtonWhenWasLodged2.setText(getResources()
                                            .getString(
                                                    R.string.less_than)
                                            + " " + cmpTime);

                                    ShowSpecialNeedDialog();

                                }
                                // else
                                //   bb.setChecked(true);
                                break;
                            case R.id.radioMainCorp:
                                qidTxt.setText(getResources().getString(R.string.corp2));
                                specialneednumbertxt.setVisibility(View.GONE);
                                etspecialneednb.setVisibility(View.GONE);
                                linear.setVisibility(View.VISIBLE);
                                // dateOfRequestLayout.setVisibility(View.GONE);
                                isIndividual = "false";
                                isCorporate = "true";
                                isSpecialNeed = false;
                                qidEdt.setText("");
                                if (!IssueType.equals("2")) {
                                    if (checkapponDate.equals("true")) {

                                        //hsen b added 4:38pm 26-4-2017
                                        RadioButtonWhenWasLodged1.setText(getResources().getString(
                                                R.string.more_than)
                                                + " " + cmpTime);
                                        RadioButtonWhenWasLodged2.setText(getResources().getString(
                                                R.string.less_than)
                                                + " " + cmpTime);


                                        if (ivDate.getText().toString().length() != 0) {
                                            final Calendar cl = Calendar.getInstance();
                                            long diff = cl.getTimeInMillis() - todate.getTimeInMillis();
                                            long days = diff / (24 * 60 * 60 * 1000);
                                            // Toast.makeText(ComplainFormActivity.this,days+"",Toast.LENGTH_LONG).show();


                                            if (days < 14) {
                                                createDialogForWorkingDays();
                                            } else {
                                                // radioGroupDidYouComplain.setVisibility(View.VISIBLE);ivD
                                                // radioGroup.requestFocus();
                                                /*RadioButtonWhenWasLodged1.setText(getResources().getString(
                                                        R.string.more_than)
                                                        + " " + cmpTime);
                                                RadioButtonWhenWasLodged2.setText(getResources().getString(
                                                        R.string.less_than)
                                                        + " " + cmpTime);*/
                                                mScrollView.post(new Runnable() {
                                                    public void run() {
                                                        mScrollView.fullScroll(View.FOCUS_DOWN);
                                                    }
                                                });
                                                txtRadio.setVisibility(View.VISIBLE);
                                                qidTxt.setText(getResources().getString(
                                                        R.string.corp2));

                                                linear.setVisibility(View.VISIBLE);
                                                dateOfRequestLayout.setVisibility(View.GONE);
                                                isIndividual = "false";
                                                isCorporate = "true";
                                                qidEdt.setText("");
                                            }
                                        } else {
                                            dateOfRequestLayout.setVisibility(View.VISIBLE);
                                            radioGroupDidYouComplain.setVisibility(View.GONE);
                                            txtRadio.setVisibility(View.GONE);

                                        }
                                        //by hsen
                                        cmpdate.setEnabled(true);
                                        cmpdate.setClickable(true);
                                        ivDate.setEnabled(true);
                                        ivDate.setClickable(true);
                                    } else {

                                        radioGroupDidYouComplain.setVisibility(View.VISIBLE);
                                        // radioGroup.requestFocus();
                                        RadioButtonWhenWasLodged1.setText(getResources().getString(
                                                R.string.more_than)
                                                + " " + cmpTime);
                                        RadioButtonWhenWasLodged2.setText(getResources().getString(
                                                R.string.less_than)
                                                + " " + cmpTime);
                                        mScrollView.post(new Runnable() {
                                            public void run() {
                                                mScrollView.fullScroll(View.FOCUS_DOWN);
                                            }
                                        });
                                        txtRadio.setVisibility(View.VISIBLE);
                                        qidTxt.setText(getResources().getString(
                                                R.string.corp2));

                                        linear.setVisibility(View.VISIBLE);
                                        dateOfRequestLayout.setVisibility(View.GONE);
                                        isIndividual = "false";
                                        isCorporate = "true";
                                        qidEdt.setText("");
                                    }
                                }
                                break;
                        }

                    }
                });

        statusradiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.status1:
                        RadioButton bb = (RadioButton) findViewById(checkedId);
                        if (bb.isChecked()) {
                            radioGroupWhenWasLodged.setVisibility(View.VISIBLE);
                            radioGroupWhenWasLodged.clearCheck();
                            if (isSpecialNeed || isIndividual.equals("true")) {

                                //by hsen
                                if (isSpecialNeed) {
                                    RadioButtonWhenWasLodged1.setText(getResources().getString(R.string.more_than) + " " + specialneedduration);

                                    RadioButtonWhenWasLodged2.setText(getResources().getString(R.string.less_than) + " " + specialneedduration);
                                } else {
                                    RadioButtonWhenWasLodged1.setText(getResources().getString(R.string.more_than) + " " + cmpTime);

                                    RadioButtonWhenWasLodged2.setText(getResources().getString(R.string.less_than) + " " + cmpTime);
                                }

                                /*RadioButtonWhenWasLodged1.setText(getResources().getString(
                                        R.string.more_than) + " " + specialneedduration);

                                RadioButtonWhenWasLodged2.setText(getResources().getString(
                                        R.string.less_than) + " " + specialneedduration);*/
                            }
                            WhenWasLodgedText.setVisibility(View.VISIBLE);


                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    mScrollView.smoothScrollTo(0, WhenWasLodgedText.getTop());
                                }
                            });
                            CommentLayout.setVisibility(View.GONE);

                            ComplaintReferenceLayout.setVisibility(View.GONE);
                            /*if (checkapponDate.equals("true")) {
                                calldate.setText(ivDate.getText().toString());
                                cmpdate.setText(ivDate.getText().toString());
                                cmpdate.setEnabled(false);
                                cmpdate.setClickable(false);
                            }*/


                        }
                        sendwaittime = true;
                        closed = false;
                        break;

                    case R.id.status2:
                        //datePicker.setMaxDate(System.currentTimeMillis());
                        RadioButton bb2 = (RadioButton) findViewById(checkedId);
                        if (bb2.isChecked()) {
                            radioGroupWhenWasLodged.setVisibility(View.GONE);
                            WhenWasLodgedText.setVisibility(View.GONE);
                            ComplaintReferenceLayout.setVisibility(View.VISIBLE);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    mScrollView.smoothScrollTo(0, ComplaintReferenceLayout.getTop());
                                }
                            });
                            CommentLayout.setVisibility(View.VISIBLE);
                            complaint.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);


                            //hsen gps
                            if (IssuetypeId == 1 && issue_id == 1)
                                showDialogForAffectedLocation();
                        }
                        closed = true;
                        sendwaittime = false;
                        break;
                }

            }
        });


        channelradio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                TextView callNumTxt = (TextView) findViewById(R.id.callNumTxt);
                TextView callNumStar = (TextView) findViewById(R.id.callNumStar);
                EditText CallNumberEdittext = (EditText) findViewById(R.id.callNumEdt);
//                TextView dateofrequesttitle = (TextView) findViewById(R.id.dateofrequesttitle);
//                EditText dateofrequesttxt = (EditText) findViewById(R.id.dateEdt);
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.channelanswer1:
                        numbercalledlayout.setVisibility(View.VISIBLE);
                        complaint.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);

                        callNumTxt.setVisibility(View.VISIBLE);
                        callNumStar.setVisibility(View.GONE);
                        CallNumberEdittext.setVisibility(View.VISIBLE);
                        CommentLayout.setVisibility(View.VISIBLE);
                        mScrollView.post(new Runnable() {
                            public void run() {
                                mScrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                        break;
                    case R.id.channelanswer2:
                        numbercalledlayout.setVisibility(View.VISIBLE);
                        complaint.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        callNumTxt.setVisibility(View.GONE);
                        try {

                            callNumStar.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        CallNumberEdittext.setVisibility(View.GONE);
//                        CallNumberEdittext.setT
                        CallNumberEdittext.setText("");

                        CommentLayout.setVisibility(View.VISIBLE);
                        mScrollView.post(new Runnable() {
                            public void run() {
                                mScrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                        break;
                    case R.id.channelanswer3:
                        numbercalledlayout.setVisibility(View.VISIBLE);
                        complaint.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        callNumTxt.setVisibility(View.GONE);
                        try {

                            callNumStar.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        CallNumberEdittext.setVisibility(View.GONE);
                        CallNumberEdittext.setText("");

                        CommentLayout.setVisibility(View.VISIBLE);
                        mScrollView.post(new Runnable() {
                            public void run() {
                                mScrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                        break;

                    case R.id.channelanswer4:
                        numbercalledlayout.setVisibility(View.VISIBLE);
                        complaint.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        callNumTxt.setVisibility(View.GONE);
                        try {

                            callNumStar.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        CallNumberEdittext.setVisibility(View.GONE);
                        CallNumberEdittext.setText("");

                        CommentLayout.setVisibility(View.VISIBLE);
                        mScrollView.post(new Runnable() {
                            public void run() {
                                mScrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                        break;


                }

            }
        });
        radioGroupDidYouComplain.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.didyoucomplain1:
                        RadioButton bb = (RadioButton) findViewById(checkedId);
                        if (bb.isChecked()) {
                            spComplaint = "3";
                            // radioGroupWhenWasLodged.setVisibility(View.VISIBLE);
                            if (isSpecialNeed) {
                                RadioButtonWhenWasLodged1.setText(getResources().getString(
                                        R.string.more_than) + " " + specialneedduration);

                                RadioButtonWhenWasLodged2.setText(getResources().getString(
                                        R.string.less_than)
                                        + " " + specialneedduration);
                            }
                            // radioGroupWhenWasLodged.clearCheck();
                            WhenWasLodgedText.setVisibility(View.GONE);
                            radioGroupWhenWasLodged.setVisibility(View.GONE);
                            // rg2lbl.requestFocus();
                            mScrollView.post(new Runnable() {
                                public void run() {
                                    mScrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });

                            // radioGroup3.setVisibility(View.GONE);
                            locationlayout.setVisibility(View.GONE);
                            numbercalledlayout.setVisibility(View.GONE);
                            //MapLayout.setVisibility(View.GONE);
                            etbuilding.setVisibility(View.GONE);
                            etstreet.setVisibility(View.GONE);
                            etzone.setVisibility(View.GONE);
                            buildingtxt.setVisibility(View.GONE);
                            streettxt.setVisibility(View.GONE);
                            zonetxt.setVisibility(View.GONE);

                            CommentLayout.setVisibility(View.GONE);
                            //callNumEdt.setText("");
                            referenceNumbertext.setText("");
                            if (showOnMap.equalsIgnoreCase("true")) {
                                if (IssuetypeId == 1)

                                    locationlayout.setVisibility(View.VISIBLE);
                                //  numbercalledlayout.setVisibility(View.VISIBLE);
                                // layout2.requestFocus();
                                mScrollView.post(new Runnable() {
                                    public void run() {
                                        mScrollView.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                                //  radiogrouplocationsame.setVisibility(View.VISIBLE);
                                radiogrouplocationsame.check(-1);
                                CommentLayout.setVisibility(View.GONE);
//                            if ((latitude == 0.0 || longitude == 0.0)) {
//                                boolean gps_enabled = locationManager
//                                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
//                                if (gps_enabled) {
//                                    dialog = ProgressDialog.show(
//                                            ComplainFormActivity.this,
//                                            "",
//                                            getResources().getString(
//                                                    R.string.wait_gps), true);
//                                }
//                            }
                            } else {
                                cmpText.setText("");
                                radiogrouplocationsame.setVisibility(View.GONE);
                                //    numbercalledlayout.setVisibility(View.VISIBLE);
                                // CommentLayout.setVisibility(View.VISIBLE);
                                // layout3.requestFocus();
                                mScrollView.post(new Runnable() {
                                    public void run() {
                                        mScrollView.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                            }
                            channelquestion.setVisibility(View.GONE);
                            channelradio.setVisibility(View.GONE);
                            statusradiogroup.setVisibility(View.VISIBLE);
                            statusradiogroup.clearCheck();
                            statusquestion.setVisibility(View.VISIBLE);
//                        if(checkapponDate.equals("true"))
//                        {
//                            calldate.setText(ivDate.getText().toString());
//                          //  calldate.setClickable(false);
//                            //calldate.setEnabled(false);
//                        }
//                        else
//                        {
//                            calldate.setClickable(true);
//                            calldate.setEnabled(true);
//                        }
                           /* if (checkapponDate.equals("true")) {
                                calldate.setText(ivDate.getText().toString());
                                cmpdate.setText(ivDate.getText().toString());
                                cmpdate.setClickable(false);
                                cmpdate.setEnabled(false);
                            }*/

                            //by hsen
                            if (checkapponDate.equals("true")) {
                                calldate.setClickable(true);
                                calldate.setEnabled(true);
                            }
                        }
                        break;

                    case R.id.didyoucomplain2:
                        RadioButton bb2 = (RadioButton) findViewById(checkedId);
                        if (bb2.isChecked()) {
                            // here to show the new row
                            statusradiogroup.setVisibility(View.GONE);
                            channelquestion.setVisibility(View.VISIBLE);
                            channelradio.setVisibility(View.VISIBLE);
                            etstreet.setText("");
                            etzone.setText("");
                            etbuilding.setText("");
                            calldate.setText("");
                            cmpdate.setText("");
                            CommentLayout.setVisibility(View.GONE);

                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    mScrollView.smoothScrollTo(0, channelquestion.getTop());
                                }
                            });
                            statusquestion.setVisibility(View.GONE);
                            spComplaint = "4";
                            WhenWasLodgedText.setVisibility(View.GONE);
                            radioGroupWhenWasLodged.setVisibility(View.GONE);
                            // callNumEdt.setText("");
                            referenceNumbertext.setText("");
                            if (showOnMap.equalsIgnoreCase("true")) {
                                if (IssuetypeId == 1) {
                                    locationlayout.setVisibility(View.VISIBLE);
                                    //  numbercalledlayout.setVisibility(View.VISIBLE);
                                    showDialogForAffectedLocation();
                                }
                                /*if (checkapponDate.equals("true")) {
                                    calldate.setText(ivDate.getText().toString());
                                    cmpdate.setEnabled(false);
                                    cmpdate.setClickable(false);

                                }*/
                                // layout2.requestFocus();


                                //  radiogrouplocationsame.setVisibility(View.VISIBLE);
                                radiogrouplocationsame.check(-1);
                                CommentLayout.setVisibility(View.GONE);
//                                if ((latitude == 0.0 || longitude == 0.0)) {
//                                    boolean gps_enabled = locationManager
//                                            .isProviderEnabled(LocationManager.GPS_PROVIDER);
//                                    if (gps_enabled) {
//                                        dialog = ProgressDialog.show(
//                                                ComplainFormActivity.this,
//                                                "",
//                                                getResources().getString(
//                                                        R.string.wait_gps), true);
//                                    }
//                                }
                            } else {
                                cmpText.setText("");
                                radiogrouplocationsame.setVisibility(View.GONE);
                                //  numbercalledlayout.setVisibility(View.VISIBLE);
                                //CommentLayout.setVisibility(View.VISIBLE);
                                // layout3.requestFocus();

                            }
                            // CommentLayout.setVisibility(View.VISIBLE);
                            //  MapLayout.setVisibility(View.GONE);
                            ShowHideAres(View.GONE);
                            etbuilding.setVisibility(View.GONE);
                            etstreet.setVisibility(View.GONE);
                            etzone.setVisibility(View.GONE);
                            buildingtxt.setVisibility(View.GONE);
                            streettxt.setVisibility(View.GONE);
                            zonetxt.setVisibility(View.GONE);
                            mScrollView.fullScroll(View.FOCUS_DOWN);
                            ComplaintReferenceLayout.setVisibility(View.GONE);
                        }
                        //CommentLayout.setVisibility(View.VISIBLE);
                        break;

                    case R.id.didyoucomplain3:
                        RadioButton bb3 = (RadioButton) findViewById(checkedId);
                        if (bb3.isChecked()) {

                            etstreet.setText("");
                            etzone.setText("");
                            etbuilding.setText("");
                            calldate.setText("");
                            cmpdate.setText("");

                            statusradiogroup.setVisibility(View.GONE);
                            channelquestion.setVisibility(View.GONE);
                            statusquestion.setVisibility(View.GONE);

                            CommentLayout.setVisibility(View.GONE);
                            channelradio.clearCheck();
                            channelradio.setVisibility(View.GONE);
                            spComplaint = "5";
                            radioGroupWhenWasLodged.setVisibility(View.GONE);
                            //  callNumEdt.setText("");
                            referenceNumbertext.setText("");
                            // radioGroupWhenWasLodged.clearCheck();
                            locationlayout.setVisibility(View.GONE);
                            numbercalledlayout.setVisibility(View.GONE);
                            // radioGroup3.setVisibility(View.GONE);
                            //MapLayout.setVisibility(View.GONE);
                            ShowHideAres(View.GONE);
                            //areaTxt.setVisibility(View.GONE);
                            WhenWasLodgedText.setVisibility(View.GONE);
                            ComplaintReferenceLayout.setVisibility(View.GONE);
                            googleMap = null;
                            complaint.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            // layout3.requestFocus();
                            if (showOnMap.equalsIgnoreCase("true")) {
                                if (IssuetypeId == 1)
                                    locationlayout.setVisibility(View.VISIBLE);
                                // numbercalledlayout.setVisibility(View.VISIBLE);
                                // layout2.requestFocus();
                                mScrollView.post(new Runnable() {
                                    public void run() {
                                        mScrollView.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                                // radiogrouplocationsame.setVisibility(View.VISIBLE);
                                radiogrouplocationsame.check(-1);
                                CommentLayout.setVisibility(View.GONE);
//                                if ((latitude == 0.0 || longitude == 0.0)) {
//                                    boolean gps_enabled = locationManager
//                                            .isProviderEnabled(LocationManager.GPS_PROVIDER);
//                                    if (gps_enabled) {
//                                        dialog = ProgressDialog.show(
//                                                ComplainFormActivity.this,
//                                                "",
//                                                getResources().getString(
//                                                        R.string.wait_gps), true);
//                                    }
//                                }
                            } else {
                                cmpText.setText("");
                                radiogrouplocationsame.setVisibility(View.GONE);
                                //    numbercalledlayout.setVisibility(View.VISIBLE);
                                CommentLayout.setVisibility(View.VISIBLE);
                                complaint.setVisibility(View.VISIBLE);
                                submit.setVisibility(View.VISIBLE);
                                // layout3.requestFocus();
                                mScrollView.post(new Runnable() {
                                    public void run() {
                                        mScrollView.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                            }
                            try {
                                if (IssueType.equalsIgnoreCase("2")) {
                                    if (MyApplication.Lang
                                            .equalsIgnoreCase(MyApplication.ARABIC)) {
                                        // cmpText.setText(getLookup("45").namear);
                                        onCreateDialog2(ComplainFormActivity.this, getLookup("45").namear);
                                    } else {
                                        //  cmpText.setText(getLookup("45").nameen);
                                        onCreateDialog2(ComplainFormActivity.this, getLookup("45").nameen);
                                    }
                                } else {
                                    if (MyApplication.Lang
                                            .equalsIgnoreCase(MyApplication.ARABIC)) {
                                        //  cmpText.setText(getLookup("16").namear);
                                        onCreateDialog2(ComplainFormActivity.this, getLookup("16").namear);
                                    } else {
                                        // cmpText.setText(getLookup("16").nameen);
                                        onCreateDialog2(ComplainFormActivity.this, getLookup("16").nameen);
                                    }
                                }
                                CommentLayout.setVisibility(View.GONE);
                            } catch (Exception e) {

                            }
                        }
                        break;
                }
            }
        });

        radioGroupWhenWasLodged.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.RadioButtonWhenWasLodged1:
                        RadioButton bb = (RadioButton) findViewById(checkedId);
                        if (bb.isChecked()) {


                            //by hsen
                            //<editor-fold desc="by hsen">
                            if (status1.isChecked()) {
                                if (waitUnit.equals("Hours") || waitUnit.equals("ساعات") || waitUnit.equals("ساعة")) {
                                    normalDays = Integer.parseInt(waitDuration) / 24;
                                    specialDays = Integer.parseInt(specialneedduration.substring(0, specialneedduration.indexOf(" "))) / 24;

                                    Log.wtf("NORMAL waitDuration", "" + waitDuration);
                                    Log.wtf("NORMAL DAYS", "" + normalDays);

                                    Log.wtf("SPECIAL DAYS", "" + specialneedduration);
                                    Log.wtf("SPECIAL DAYS", "" + specialDays);
                                } else {

                                    normalDays = Integer.parseInt(waitDuration);
                                    specialDays = Integer.parseInt(specialneedduration.substring(0, specialneedduration.indexOf(" "))) / 24;

                                    Log.wtf("NORMAL waitDuration", "" + normalDays);

                                    Log.wtf("SPECIAL DAYS", "" + specialDays);
                                }
                            }
                            //</editor-fold>


                            longWait = "true";
                            ComplaintReferenceLayout.setVisibility(View.VISIBLE);
                            CommentLayout.setVisibility(View.VISIBLE);
                            complaint.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            // layout3.requestFocus();
                            mScrollView.post(new Runnable() {
                                public void run() {
                                    mScrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                            cmpText.setText("");
                            complaint.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);

                            longWait = "true";
                            ComplaintReferenceLayout.setVisibility(View.VISIBLE);
                            CommentLayout.setVisibility(View.VISIBLE);
                            complaint.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            // layout3.requestFocus();
                            mScrollView.post(new Runnable() {
                                public void run() {
                                    mScrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                            cmpText.setText("");
                            complaint.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            if (IssuetypeId == 1)
                                // MapLayout.setVisibility(View.VISIBLE);
                                //   ShowHideAres(View.VISIBLE);
                                showDialogForAffectedLocation();
                           /* if (checkapponDate.equals("true")) {
                                calldate.setText(ivDate.getText().toString());
                                cmpdate.setText(ivDate.getText().toString());
                                cmpdate.setEnabled(false);
                                cmpdate.setClickable(false);
                            }*/

                            //by hsen
                            if (checkapponDate.equals("true")) {
                                calldate.setClickable(true);
                                calldate.setEnabled(true);
                            }

                        }
                        break;
                    case R.id.RadioButtonWhenWasLodged2:
                        RadioButton bb2 = (RadioButton) findViewById(checkedId);
                        if (bb2.isChecked()) {
                            longWait = "false";
                            // layout1.setVisibility(View.GONE);
                            // layout3.setVisibility(View.VISIBLE);
                            // // layout3.requestFocus();
                            // mScrollView.post(new Runnable() { public void run() {
                            // mScrollView.fullScroll(View.FOCUS_DOWN); } });

                            complaint.setVisibility(View.GONE);
                            submit.setVisibility(View.GONE);
                            ComplaintReferenceLayout.setVisibility(View.GONE);

                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    mScrollView.smoothScrollTo(0, CommentLayout.getTop());
                                }
                            });
                            try {
                                if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {

                                    String newString;

                                    if (!isSpecialNeed)
                                        newString = getLookup("22").namear.replace("NumberOfDays", waitDuration + " " + waitUnit);
                                    else
                                        newString = getLookup("22").namear.replace("NumberOfDays", specialneedduration);//.replace(" " + waitUnit, ""));// + " " + waitUnit);
                                    //cmpText.setText(newString);
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ComplainFormActivity.this);
// ...Irrelevant code for customizing the buttons and title
                                    LayoutInflater inflater = ComplainFormActivity.this.getLayoutInflater();
                                    View dialogView = inflater.inflate(R.layout.about_us_popup, null);
                                    dialogBuilder.setView(dialogView);
                                    final AlertDialog alertDialog = dialogBuilder.create();

                                    TextView abouttitle = (TextView) dialogView.findViewById(R.id.abouttitle);
                                    abouttitle.setVisibility(View.GONE);
                                    WebView wv = (WebView) dialogView.findViewById(R.id.wv);
                                    TextView x = (TextView) dialogView.findViewById(R.id.x);
                                    TextView b = (TextView) dialogView.findViewById(R.id.ok);

                                    x.setVisibility(View.GONE);
                                    b.setVisibility(View.VISIBLE);
                                    b.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();
                                        }
                                    });

                                    //<editor-fold desc="hsen dialog">
                                    wv.getSettings().setJavaScriptEnabled(true);
                                    String fontName = "";
                                    if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                                        fontName = "file:///android_asset/fonts/Galaxie_Polaris_Medium.otf";
                                        abouttitle.setTypeface(MyApplication.facePolarisMedium);
                                    } else {
                                        fontName = "file:///android_asset/fonts/GE_Dinar_One_Medium.otf";
                                        abouttitle.setTypeface(MyApplication.faceDinar);

                                    }
                                    wv.loadDataWithBaseURL("", "<html><head>" +
                                            "<style type=\"text/css\">@font-face {font-family: MyFont;" +
                                            "src: url(\"" + fontName + "\")\n" +
                                            "}\n" +
                                            "body {\n" +
                                            "    font-family: MyFont;\n" +
                                            "    font-size: medium;\n" +
                                            "    color: #000000;\n" +
                                            "    text-align: center;\n" +
                                            "}\n" +

                                            "p {\n" +
                                            "    font-family: MyFont;\n" +
                                            "    font-size: medium;\n" +
                                            "    color: #000000;\n" +
                                            "    text-align: center;\n" +
                                            "}\n" +
                                            "</style>" +
                                            "</head><body style=\"text-align:center;\">" + newString + "</body></html>", "text/html", "UTF-8", "");
                                    wv.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            view.loadUrl(url);

                                            return true;
                                        }
                                    });
                                    //</editor-fold>


                                    //<editor-fold desc="amal dialog">
                                    /*wv.getSettings().setJavaScriptEnabled(true);
                                    wv.loadDataWithBaseURL("", "<html><body style=\'text-align:justify;\'><p style=\'text-align:justify;\'>" + newString + "</p></body></html>", "text/html", "UTF-8", "");
                                    wv.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            view.loadUrl(url);

                                            return true;
                                        }
                                    });*/
                                    //</editor-fold>


                                    alertDialog.setView(dialogView, 0, 0, 0, 0);
                                    alertDialog.show();
                                } else {

                                    String newString2;

                                    if (!isSpecialNeed)
                                        newString2 = getLookup("22").nameen.replace("NumberOfDays", waitDuration + " " + waitUnit);
                                    else
                                        newString2 = getLookup("22").nameen.replace("NumberOfDays", specialneedduration.replace(" " + waitUnit, ""));// + " " + waitUnit);

                                    //cmpText.setText(newString2);
                                    //  Actions.onCreateDialog1(ComplainFormActivity.this,newString2);
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ComplainFormActivity.this);

                                    LayoutInflater inflater = ComplainFormActivity.this.getLayoutInflater();
                                    View dialogView = inflater.inflate(R.layout.about_us_popup, null);
                                    dialogBuilder.setView(dialogView);
                                    final AlertDialog alertDialog = dialogBuilder.create();
                                    TextView abouttitle = (TextView) dialogView.findViewById(R.id.abouttitle);
                                    abouttitle.setVisibility(View.GONE);
                                    WebView wv = (WebView) dialogView.findViewById(R.id.wv);
                                    TextView x = (TextView) dialogView.findViewById(R.id.x);
                                    TextView b = (TextView) dialogView.findViewById(R.id.ok);
                                    x.setVisibility(View.GONE);
                                    b.setVisibility(View.VISIBLE);
                                    b.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();
                                        }
                                    });


                                    //<editor-fold desc="hsen dialog">
                                    wv.getSettings().setJavaScriptEnabled(true);
                                    String fontName = "";
                                    if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                                        fontName = "file:///android_asset/fonts/Galaxie_Polaris_Medium.otf";
                                        abouttitle.setTypeface(MyApplication.facePolarisMedium);
                                    } else {
                                        fontName = "file:///android_asset/fonts/GE_Dinar_One_Medium.otf";
                                        abouttitle.setTypeface(MyApplication.faceDinar);

                                    }
                                    wv.loadDataWithBaseURL("", "<html><head>" +
                                            "<style type=\"text/css\">@font-face {font-family: MyFont;" +
                                            "src: url(\"" + fontName + "\")\n" +
                                            "}\n" +
                                            "body {\n" +
                                            "    font-family: MyFont;\n" +
                                            "    font-size: medium;\n" +
                                            "    color: #000000;\n" +
                                            "    text-align: center;\n" +
                                            "}\n" +

                                            "p {\n" +
                                            "    font-family: MyFont;\n" +
                                            "    font-size: medium;\n" +
                                            "    color: #000000;\n" +
                                            "    text-align: center;\n" +
                                            "}\n" +
                                            "</style>" +
                                            "</head><body style=\"text-align:center;\">" + newString2 + "</body></html>", "text/html", "UTF-8", "");
                                    wv.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            view.loadUrl(url);

                                            return true;
                                        }
                                    });
                                    //</editor-fold>


                                    //<editor-fold desc="amal dialog">
                                    /*wv.getSettings().setJavaScriptEnabled(true);
                                    wv.loadDataWithBaseURL("", "<html><body style=\"text-align:justify;\"><p style=\"text-align:justify;\">" + newString2 + "</p></body></html>", "text/html", "UTF-8", "");
                                    wv.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            view.loadUrl(url);

                                            return true;
                                        }
                                    });*/
                                    //</editor-fold>


                                    alertDialog.setView(dialogView, 0, 0, 0, 0);
                                    alertDialog.show();
                                }
                            } catch (Exception e) {

                            }
                            if (IssuetypeId == 1)
                                // MapLayout.setVisibility(View.VISIBLE);
                                ShowHideAres(View.GONE);
                            //   areaTxt.setVisibility(View.VISIBLE);

                            //complaint.setVisibility(View.VISIBLE);
                            // submit.setVisibility(View.VISIBLE);
//                        if (googleMap != null) {
//                            FillInitialMapSettings fillinitialmaps = new FillInitialMapSettings();
//                            fillinitialmaps.execute();
//                        }
                            cmpText.setText("");


//                        new Handler().post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mScrollView.smoothScrollTo(0, MapLayout.getTop());
//                            }
//                        });
                            mScrollView.post(new Runnable() {
                                public void run() {
                                    mScrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        }
                        break;
                }

            }
        });

        radiogrouplocationsame.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case (-1):
                        break;
                    case R.id.locationsame1:
                        //MapLayout.setVisibility(View.GONE);
                        ShowHideAres(View.GONE);
                        //areaTxt.setVisibility(View.GONE);
                        CommentLayout.setVisibility(View.VISIBLE);
                        complaint.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        // layout3.requestFocus();
                        mScrollView.post(new Runnable() {
                            public void run() {
                                mScrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                        cmpText.setText("");

                        break;
                    case R.id.locationsame2:
                        //MapLayout.setVisibility(View.VISIBLE);
                        ShowHideAres(View.VISIBLE);
                        // areaTxt.setVisibility(View.VISIBLE);
                        CommentLayout.setVisibility(View.VISIBLE);
                        complaint.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        if (googleMap != null) {
                            FillInitialMapSettings fillinitialmaps = new FillInitialMapSettings();
                            fillinitialmaps.execute();
                        }
                        cmpText.setText("");
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                mScrollView.smoothScrollTo(0, buildingtxt.getTop());
                            }
                        });
                        break;
                }

            }
        });


        try {
            final Calendar cl = Calendar.getInstance();
            mYear = cl.get(Calendar.YEAR);
            mMonth = cl.get(Calendar.MONTH);
            mDay = cl.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            int m = monthOfYear;
                            monthOfYear = monthOfYear + 1;
                            String mm = String.valueOf(monthOfYear);
                            if (mm.length() == 1)
                                mm = "0" + mm;
                            String dd = String.valueOf(dayOfMonth);
                            if (dd.length() == 1)
                                dd = "0" + dd;


                            //returnedDate = year + "-" + mm + "-" + dd;

                            if (mm.equals("00")) {
                                mm = "12";
                                year -= 1;
                            }

                            ivDate.setText(year + "-" + mm + "-" + dd);
                            todate = Calendar.getInstance();
                            todate.set(year, m, dayOfMonth);
                            //returnedDate=returnedDatefom;
                            long diff = cl.getTimeInMillis() - todate.getTimeInMillis();
                            long days = diff / (24 * 60 * 60 * 1000);
                            // Toast.makeText(ComplainFormActivity.this,days+"",Toast.LENGTH_LONG).show();
                            if (days < 14) {
                                createDialogForWorkingDays();
                            } else
                            // continue normally
                            {


                                calldate.setClickable(false);
                                calldate.setEnabled(false);
                                calldate.setText(year + "-" + mm + "-" + dd);


                                if (!IssueType.equals("2")) {
                                    radioGroupDidYouComplain.setVisibility(View.VISIBLE);
                                    // radioGroup.requestFocus();
                                    mScrollView.post(new Runnable() {
                                        public void run() {
                                            mScrollView.fullScroll(View.FOCUS_DOWN);
                                        }
                                    });
                                    //hsen b
                                    //qidTxt.setText(getResources().getString(R.string.indv2));
                                    linear.setVisibility(View.VISIBLE);
                                    dateOfRequestLayout.setVisibility(View.VISIBLE);

                                    // qidEdt.setText("");
                                    txtRadio.setVisibility(View.VISIBLE);


                                }
                            }
                        }
                    }, mYear, mMonth, mDay);

            DatePicker dp = datePickerDialog.getDatePicker();
            dp.setMaxDate(System.currentTimeMillis());
            //Set the DatePicker minimum date selection to current date
            //   dp.setMinDate(cl.getTimeInMillis());//get the current day
            String d = "";
            if (dp.getMonth() + 2 <= 10) {
                d = "0" + (dp.getMonth() + 2);
                if (d.equals("010"))
                    d = "10";
            } else
                d = String.valueOf(dp.getMonth() + 1);
            if (d.length() == 1)
                d = "0" + d;
            // etDate.setText(dp.getYear() + "-" + (d) + "-" + (dp.getDayOfMonth()));
            //  monthNext = dp.getYear() + "-" + (d) + "-" + (dp.getDayOfMonth());
            ivDate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    showDateTimePickerFrom(ivDate);
                    // Actions.setLocal(ComplainFormActivity.this);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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


            if (value.contains("0")) { //not blocked


                sendreq eventLaunching = new sendreq();
                eventLaunching.execute();
            } else { //blocked

                if (MyApplication.Lang.equals(MyApplication.ARABIC))
                    Actions.onCreateBlockedDialog2(ComplainFormActivity.this, getLookup("94").namear);
                else
                    Actions.onCreateBlockedDialog2(ComplainFormActivity.this, getLookup("94").nameen);

            }

        }
    }


    public void onCreateDialog2(final Activity activity, String msg) {


        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        //   .setTitle(ComplainFormActivity.this.getString(R.string.warning2))


        TextView myMsg = new TextView(this);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        //ViewResizing.textResize(ComplainFormActivity.this,myMsg,13);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(myMsg);
        myMsg.setText(msg);

        builder.setPositiveButton(R.string.ok2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
            }
        })
                //   .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ComplainFormActivity.this);
//
//        LayoutInflater inflater = ComplainFormActivity.this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.about_us_popup, null);
//        dialogBuilder.setView(dialogView);
//        final AlertDialog alertDialog = dialogBuilder.create();
//
//        WebView wv = (WebView) dialogView.findViewById(R.id.wv);
//        TextView x = (TextView) dialogView.findViewById(R.id.x);
//        TextView b = (TextView) dialogView.findViewById(R.id.ok);
//        b.setVisibility(View.VISIBLE);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//        x.setVisibility(View.GONE);
//        wv.getSettings().setJavaScriptEnabled(true);
//        wv.loadDataWithBaseURL("", "<html><body style=\"text-align:justify;\"><p style=\"text-align:justify;\">" + msg + "</p></body></html>", "text/html", "UTF-8", "");
//        wv.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//
//                return true;
//            }
//        });
//        alertDialog.setView(dialogView, 0, 0, 0, 0);
//        alertDialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            registerLocationUpdates();
        }
    }


    public void showDialogForAffectedLocation() {

        //by hseny
        String msg = "";
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH))
            msg = getLookup("12").nameen;
        else
            msg = getLookup("12").namear;
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        //builder.setTitle(ComplainFormActivity.this.getString(R.string.warning2))
        TextView myMsg = new TextView(this);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        //ViewResizing.textResize(ComplainFormActivity.this,myMsg,13);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(myMsg);
        myMsg.setText(msg);
        builder.setPositiveButton(getString(R.string.yes2), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
                ShowHideAres(View.GONE);


                int off = 0;
                try {
                    off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
                } catch (Exception e) {
                    off = 0;
                }
                if (off == 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ComplainFormActivity.this, R.style.AlertDialogCustom));
                    builder
                            .setMessage(ComplainFormActivity.this.getResources().getString(R.string.enablegps2))
                            .setCancelable(true)
                            .setPositiveButton(getString(R.string.ok2), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(onGPS, 0);
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel2), new DialogInterface.OnClickListener() {
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
        });
        builder.setNegativeButton(ComplainFormActivity.this.getString(R.string.no2),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ShowHideAres(View.VISIBLE);
                    }
                }).setCancelable(false);
        final AlertDialog dg = builder.create();
        dg.show();

    }


    public void resetForm() {

        dateOfRequestLayout.setVisibility(View.GONE);
        radioGroupMainIndvCooporate.clearCheck();
        radioMainCorp = (RadioButton) findViewById(R.id.radioMainCorp);
        radioMainIndv = (RadioButton) findViewById(R.id.radioMainIndv);
        // radioMainCorp.setChecked(false);
        // radioMainIndv.setChecked(false);
        TextView SpecialNeedTxt = (TextView) findViewById(R.id.SpecialNeedTxt);
        SpecialNeedTxt.setVisibility(View.GONE);
        EditText specialneednumber = (EditText) findViewById(R.id.specialneednumber);
        specialneednumber.setVisibility(View.GONE);
        specialneednumber.setText("");
        LinearLayout qidlayout = (LinearLayout) findViewById(R.id.qidlayout);
        qidlayout.setVisibility(View.GONE);
        EditText qidEdt = (EditText) findViewById(R.id.qidEdt);
        qidEdt.setText("");
        LinearLayout dateOfRequestLayout = (LinearLayout) findViewById(R.id.dateOfRequestLayout);
        dateOfRequestLayout.setVisibility(View.GONE);
        ivDate.setText("");
        TextView Didyoucompalinquestion = (TextView) findViewById(R.id.Didyoucompalinquestion);

        Didyoucompalinquestion.setVisibility(View.GONE);
        radioGroupDidYouComplain.setVisibility(View.GONE);
        radioGroupDidYouComplain.clearCheck();
        RadioButton didyoucomplain3;
        didyoucomplain1 = (RadioButton) findViewById(R.id.didyoucomplain1);
        didyoucomplain2 = (RadioButton) findViewById(R.id.didyoucomplain2);
        didyoucomplain3 = (RadioButton) findViewById(R.id.didyoucomplain3);

        //didyoucomplain1.setChecked(false);
        // didyoucomplain2.setChecked(false);
        // didyoucomplain3.setChecked(false);
        TextView channelquestion = (TextView) findViewById(R.id.channelquestion);
        channelquestion.setVisibility(View.GONE);
        channelradio.clearCheck();
        //  channelanswer1.setChecked(false);
        //channelanswer2.setChecked(false);
        //channelanswer3.setChecked(false); channelanswer4.setChecked(false);

        channelradio.setVisibility(View.GONE);
        statusquestion.setVisibility(View.GONE);
        statusradiogroup.clearCheck();
        ;
        statusradiogroup.setVisibility(View.GONE);
        WhenWasLodgedText.setVisibility(View.GONE);
        radioGroupWhenWasLodged.setVisibility(View.GONE);
        radioGroupWhenWasLodged.clearCheck();
        //  RadioButtonWhenWasLodged1.setChecked(false);
        // RadioButtonWhenWasLodged2.setChecked(false);
        ComplaintReferenceLayout.setVisibility(View.GONE);
        referenceNumbertext.setText("");
        EditText date = (EditText) findViewById(R.id.date);
        date.setText("");
        date.setTypeface(MyApplication.facePolarisMedium);
        locationlayout.setVisibility(View.GONE);
        LinearLayout MapLayout = (LinearLayout) findViewById(R.id.MapLayout);
        MapLayout.setVisibility(View.GONE
        );
        LinearLayout numbercalledlayout = (LinearLayout) findViewById(R.id.numbercalledlayout);
        numbercalledlayout.setVisibility(View.GONE);
        LinearLayout CommentLayoutLinearLayout = (LinearLayout) findViewById(R.id.CommentLayout);
        CommentLayout.setVisibility(View.GONE);
        if (checkapponDate.equals("true")) {
            ivDate.setVisibility(View.VISIBLE);
            ivDatetxt.setVisibility(View.VISIBLE);
            //by hsen
            cmpdate.setEnabled(true);
            cmpdate.setClickable(true);
            ivDate.setEnabled(true);
            ivDate.setClickable(true);

            //dateOfRequestLayout.setVisibility(View.VISIBLE);
        } else {
            ivDate.setVisibility(View.GONE);
            ivDatetxt.setVisibility(View.GONE);
            dateOfRequestLayout.setVisibility(View.GONE);

        }
    }


    public void ShowHideAres(int v) {


        etbuilding.setVisibility(v);
        etstreet.setVisibility(v);
        etzone.setVisibility(v);
        buildingtxt.setVisibility(v);
        streettxt.setVisibility(v);
        zonetxt.setVisibility(v);

        if (append) {

            buildingtxt.append(" *");
            String text1 = buildingtxt.getText().toString();
            String htmlText1 = text1.replace("*", "<font color='#ff0000'>*</font>");
            buildingtxt.setText(Html.fromHtml(htmlText1));

            streettxt.append(" *");
            String text2 = streettxt.getText().toString();
            String htmlText2 = text2.replace("*", "<font color='#ff0000'>*</font>");
            streettxt.setText(Html.fromHtml(htmlText2));

            zonetxt.append(" *");
            String text3 = zonetxt.getText().toString();
            String htmlText3 = text3.replace("*", "<font color='#ff0000'>*</font>");
            zonetxt.setText(Html.fromHtml(htmlText3));

            append = false;
        }

    }


    public void createDialogForWorkingDays() {

        String msg = "";
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH))
            msg = getLookup("55").nameen;
        else
            msg = getLookup("55").namear;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ComplainFormActivity.this);

        LayoutInflater inflater = ComplainFormActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.about_us_popup, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();

        WebView wv = (WebView) dialogView.findViewById(R.id.wv);
        TextView x = (TextView) dialogView.findViewById(R.id.x);
        TextView b = (TextView) dialogView.findViewById(R.id.ok);
        TextView abouttitle = (TextView) dialogView.findViewById(R.id.abouttitle);
        abouttitle.setVisibility(View.GONE);
        x.setVisibility(View.GONE);
        b.setVisibility(View.VISIBLE);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        //<editor-fold desc="hsen dialog">
        wv.getSettings().setJavaScriptEnabled(true);
        String fontName = "";
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            fontName = "file:///android_asset/fonts/Galaxie_Polaris_Medium.otf";
            abouttitle.setTypeface(MyApplication.facePolarisMedium);
        } else {
            fontName = "file:///android_asset/fonts/GE_Dinar_One_Medium.otf";
            abouttitle.setTypeface(MyApplication.faceDinar);

        }
        wv.loadDataWithBaseURL("", "<html><head>" +
                "<style type=\"text/css\">@font-face {font-family: MyFont;" +
                "src: url(\"" + fontName + "\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: MyFont;\n" +
                "    font-size: medium;\n" +
                "    color: #000000;\n" +
                "    text-align: center;\n" +
                "}\n" +

                "p {\n" +
                "    font-family: MyFont;\n" +
                "    font-size: medium;\n" +
                "    color: #000000;\n" +
                "    text-align: center;\n" +
                "}\n" +
                "</style>" +
                "</head><body style=\"text-align:center;\">" + msg + "</body></html>", "text/html", "UTF-8", "");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        //</editor-fold>


        //<editor-fold desc="amal dialog">
        /*wv.getSettings().setJavaScriptEnabled(true);
        wv.loadDataWithBaseURL("", "<html><body style=\"text-align:justify;\"><p style=\"text-align:justify;\">" + msg + "</p></body></html>", "text/html", "UTF-8", "");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });*/
        //</editor-fold>


        alertDialog.setView(dialogView, 0, 0, 0, 0);
        alertDialog.show();

    }


    public void createDialog2ForWorkingDays() {

        String msg = "";
        if (isSpecialNeed) {
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH))
                msg = getLookup("88").nameen + " " + specialneedduration.replace(" " + specialneedwaitunit, "") + " " + specialneedwaitunit;
            else
                msg = getLookup("88").namear + " " + specialneedduration.replace(" " + specialneedwaitunit, "") + " " + specialneedwaitunit;
        } else {
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH))
                msg = getLookup("88").nameen + " " + waitDuration + " " + waitUnit;
            else
                msg = getLookup("88").namear + " " + waitDuration + " " + waitUnit;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ComplainFormActivity.this);

        LayoutInflater inflater = ComplainFormActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.about_us_popup, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();

        WebView wv = (WebView) dialogView.findViewById(R.id.wv);
        TextView x = (TextView) dialogView.findViewById(R.id.x);
        TextView b = (TextView) dialogView.findViewById(R.id.ok);
        TextView abouttitle = (TextView) dialogView.findViewById(R.id.abouttitle);
        abouttitle.setVisibility(View.GONE);
        x.setVisibility(View.GONE);
        b.setVisibility(View.VISIBLE);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        //<editor-fold desc="hsen dialog">
        wv.getSettings().setJavaScriptEnabled(true);
        String fontName = "";
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            fontName = "file:///android_asset/fonts/Galaxie_Polaris_Medium.otf";
            abouttitle.setTypeface(MyApplication.facePolarisMedium);
        } else {
            fontName = "file:///android_asset/fonts/GE_Dinar_One_Medium.otf";
            abouttitle.setTypeface(MyApplication.faceDinar);

        }
        wv.loadDataWithBaseURL("", "<html><head>" +
                "<style type=\"text/css\">@font-face {font-family: MyFont;" +
                "src: url(\"" + fontName + "\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: MyFont;\n" +
                "    font-size: medium;\n" +
                "    color: #000000;\n" +
                "    text-align: center;\n" +
                "}\n" +

                "p {\n" +
                "    font-family: MyFont;\n" +
                "    font-size: medium;\n" +
                "    color: #000000;\n" +
                "    text-align: center;\n" +
                "}\n" +
                "</style>" +
                "</head><body style=\"text-align:center;\">" + msg + "</body></html>", "text/html", "UTF-8", "");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        //</editor-fold>


        //<editor-fold desc="amal dialog">
        /*wv.getSettings().setJavaScriptEnabled(true);
        wv.loadDataWithBaseURL("", "<html><body style=\"text-align:justify;\"><p style=\"text-align:justify;\">" + msg + "</p></body></html>", "text/html", "UTF-8", "");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });*/
        //</editor-fold>


        alertDialog.setView(dialogView, 0, 0, 0, 0);
        alertDialog.show();

    }


    public String showDateTimePickerFrom(final View v) {
        // Actions.setLocalComplaint(ComplainFormActivity.this);

        try {
            //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }


    public void showSpecialNumberRegistrationDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(ComplainFormActivity.this, R.style.AlertDialogCustom));

        LayoutInflater inflater = ComplainFormActivity.this.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_edittext,
                null);
        TextView textView = (TextView) textEntryView
                .findViewById(R.id.dialogMsg);
        textView.setGravity(Gravity.CENTER);

        final EditText inputnumber = (EditText) textEntryView
                .findViewById(R.id.registrationnumber);
        textView.setGravity(Gravity.CENTER);
        if (MyApplication.Lang.equals(MyApplication.ARABIC))
            textView.setText(getLookup("50").namear);
        else
            textView.setText(getLookup("50").nameen);
        // Inflate and set the layout for the dialog
        textView.setTypeface(tf);
        //   inputnumber.setTypeface(tf);
        // Pass null as the parent view because its going in
        // the dialog layout
        alertDialog.setView(textEntryView);
        inputnumber.setPadding(5, 5, 5, 5);
        inputnumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                else
                    d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                if (s.length() > 0)
                    d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                else
                    d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() > 0)
                    d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                else
                    d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });
        alertDialog.setPositiveButton(getString(R.string.ok2),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        specialneednumber = inputnumber.getText().toString();
                        specialneednumbertxt.setVisibility(View.VISIBLE);
                        etspecialneednb.setVisibility(View.VISIBLE);
                        etspecialneednb.setText(specialneednumber);

                        inputnumber.clearFocus();
                        if (inputnumber != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(inputnumber.getWindowToken(), 0);
                        }
                        if (checkapponDate.equals("true")) {

                            dateOfRequestLayout.setVisibility(View.VISIBLE);
                            radioGroupDidYouComplain.setVisibility(View.GONE);
                            txtRadio.setVisibility(View.GONE);
                            if (ivDate.getText().toString().length() != 0) {
                                final Calendar cl = Calendar.getInstance();
                                long diff = cl.getTimeInMillis() - todate.getTimeInMillis();
                                long days = diff / (24 * 60 * 60 * 1000);
                                //  Toast.makeText(ComplainFormActivity.this, days + "", Toast.LENGTH_LONG).show();
                                if (days < 14) {
                                    createDialogForWorkingDays();
                                }
                            } else {
                                dateOfRequestLayout.setVisibility(View.VISIBLE);
                                radioGroupDidYouComplain.setVisibility(View.GONE);
                                txtRadio.setVisibility(View.GONE);
                            }

                            //by hsen
                            cmpdate.setEnabled(true);
                            cmpdate.setClickable(true);
                            ivDate.setEnabled(true);
                            ivDate.setClickable(true);
                        }

                    }
                });

        alertDialog.setNegativeButton(getString(R.string.cancel2),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        specialneednumbertxt.setVisibility(View.GONE);
                        etspecialneednb.setVisibility(View.GONE);
                        inputnumber.clearFocus();
                        if (inputnumber != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(inputnumber.getWindowToken(), 0);
                        }
                        if (checkapponDate.equals("true")) {

                            dateOfRequestLayout.setVisibility(View.VISIBLE);
                            radioGroupDidYouComplain.setVisibility(View.GONE);
                            txtRadio.setVisibility(View.GONE);
                            if (ivDate.getText().toString().length() != 0) {
                                final Calendar cl = Calendar.getInstance();
                                long diff = cl.getTimeInMillis() - todate.getTimeInMillis();
                                long days = diff / (24 * 60 * 60 * 1000);
                                //  Toast.makeText(ComplainFormActivity.this, days + "", Toast.LENGTH_LONG).show();
                                if (days < 14) {
                                    createDialogForWorkingDays();
                                }
                            } else {
                                dateOfRequestLayout.setVisibility(View.VISIBLE);
                                radioGroupDidYouComplain.setVisibility(View.GONE);
                                txtRadio.setVisibility(View.GONE);
                            }

                            //by hsen
                            cmpdate.setEnabled(true);
                            cmpdate.setClickable(true);
                            ivDate.setEnabled(true);
                            ivDate.setClickable(true);
                        }
                    }
                });


        d = alertDialog.create();
        d.setCanceledOnTouchOutside(false);
        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        d.show();
        d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

    }


    protected void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    //<editor-fold desc="Area Spinner">
    //    public void InitializeAreaSpinner()
//    {
//        ArrayAdapter<Area> adapter = new ArrayAdapter<Area>(
//                ComplainFormActivity.this, R.layout.spinner_text) {
//
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View v = convertView;
//                if (v == null) {
//                    Context mContext = this.getContext();
//                    LayoutInflater vi = (LayoutInflater) mContext
//                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    v = vi.inflate(R.layout.spinner_list_text, null);
//                    v.setTag(this.getItem(position).getId());
//                }
//                TextView tv = (TextView) v.findViewById(R.id.TextView01);
//                if(MyApplication.Lang.equals(MyApplication.ENGLISH)) {
//                    tv.setText(this.getItem(position).getNameEn());
//                    tv.setGravity(Gravity.LEFT);
//                }
//                else {
//                    tv.setText(this.getItem(position).getNameAr());
//                    tv.setGravity(Gravity.RIGHT);
//                }
//                tv.setTypeface(tf);
//
//              //  ViewResizing.setListRowTextResizing(v,ComplainFormActivity.this);
//                return v;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                if (convertView == null) {
//                    Context mContext = this.getContext();
//                    LayoutInflater vi = (LayoutInflater) mContext
//                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    convertView = vi
//                            .inflate(R.layout.spinner_item_day_en, null);
//                }
//                TextView tv = (TextView) convertView
//                        .findViewById(R.id.TextView01);
//                if(MyApplication.Lang.equals(MyApplication.ENGLISH)) {
//                    tv.setText(this.getItem(position).getNameEn());
//                    tv.setGravity(Gravity.LEFT);
//                }
//                else {
//                    tv.setText(this.getItem(position).getNameAr());
//                    tv.setGravity(Gravity.RIGHT);
//                }
//                tv.setTypeface(tf);
//                if (MyApplication.nightMod) {
//                    ImageView Iv = (ImageView) convertView
//                            .findViewById(R.id.spinnerArrow);
//                    Iv.setImageResource(R.drawable.arrow_spinner_nt);
//                }
//                ViewResizing.setListRowTextResizing(convertView,ComplainFormActivity.this);
//                return convertView;
//
//            }
//        };
//        adapter.setDropDownViewResource(R.layout.spinner_list_text);
//        areaSpinner.setAdapter(adapter);
//        for (int i = 0; i < areas.size(); i++) {
//
//            adapter.add(areas.get(i));
//        }
//
//        adapter.notifyDataSetChanged();
//        areaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView,
//                                       View selectedItemView, int position, long id) {
//                subAreaSpinner.setClickable(true);
//                InitializesubAreaSpinner(areas.get(position).getSubAreas());
//                areaId=String.valueOf(areas.get(position).getId());
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//            }
//
//        });
//    }
//    public void InitializesubAreaSpinner(ArrayList<SubArea> subs)
//    {
//        ArrayAdapter<SubArea> adapter = new ArrayAdapter<SubArea>(
//                ComplainFormActivity.this, R.layout.spinner_text) {
//
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View v = convertView;
//                if (v == null) {
//                    Context mContext = this.getContext();
//                    LayoutInflater vi = (LayoutInflater) mContext
//                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    v = vi.inflate(R.layout.spinner_list_text, null);
//                    v.setTag(this.getItem(position).getId());
//                }
//                TextView tv = (TextView) v.findViewById(R.id.TextView01);
//                if(MyApplication.Lang.equals(MyApplication.ENGLISH)) {
//                    tv.setText(this.getItem(position).getNameEn());
//                    tv.setGravity(Gravity.LEFT);
//                }
//                else {
//                    tv.setText(this.getItem(position).getNameAr());
//                    tv.setGravity(Gravity.RIGHT);
//                }
//                tv.setTypeface(tf);
//              //  ViewResizing.setListRowTextResizing(v,ComplainFormActivity.this);
//                return v;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                if (convertView == null) {
//                    Context mContext = this.getContext();
//                    LayoutInflater vi = (LayoutInflater) mContext
//                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    convertView = vi
//                            .inflate(R.layout.spinner_item_day_en, null);
//                }
//                TextView tv = (TextView) convertView
//                        .findViewById(R.id.TextView01);
//                if(MyApplication.Lang.equals(MyApplication.ENGLISH)) {
//                    tv.setText(this.getItem(position).getNameEn());
//                    tv.setGravity(Gravity.LEFT);
//                }
//                else {
//                    tv.setText(this.getItem(position).getNameAr());
//                    tv.setGravity(Gravity.RIGHT);
//                }
//                tv.setTypeface(tf);
//                if (MyApplication.nightMod) {
//                    ImageView Iv = (ImageView) convertView
//                            .findViewById(R.id.spinnerArrow);
//                    Iv.setImageResource(R.drawable.arrow_spinner_nt);
//                }
//                ViewResizing.setListRowTextResizing(convertView,ComplainFormActivity.this);
//                return convertView;
//
//            }
//        };
//        adapter.setDropDownViewResource(R.layout.spinner_list_text);
//        subAreaSpinner.setAdapter(adapter);
//        for (int i = 0; i < subs.size(); i++) {
//
//            adapter.add(subs.get(i));
//        }
//
//        adapter.notifyDataSetChanged();
//        subAreaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView,
//                                       View selectedItemView, int position, long id) {
//             //   subAreaSpinner.setClickable(true);
//                subareaId=String.valueOf(areas.get(position).getId());
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//            }
//
//        });
//    }
    //</editor-fold>


    private void setRbDrawable() {

        final RadioButton radioButton1 = (RadioButton) findViewById(R.id.didyoucomplain1);
        radioButton1.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.custom_btn_radio_nt, 0, 0, 0);
        radioButton1.setTypeface(tf);
        final RadioButton status1 = (RadioButton) findViewById(R.id.status1);
        status1.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.custom_btn_radio_nt, 0, 0, 0);
        final RadioButton radioButton2 = (RadioButton) findViewById(R.id.didyoucomplain2);
        radioButton2.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.custom_btn_radio_nt, 0, 0, 0);
        radioButton2.setTypeface(tf);

        final RadioButton radioButton3 = (RadioButton) findViewById(R.id.didyoucomplain3);
        radioButton3.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.custom_btn_radio_nt, 0, 0, 0);
        radioButton3.setTypeface(tf);

        final RadioButton radioButton4 = (RadioButton) findViewById(R.id.radioMainIndv);
        radioButton4.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.custom_btn_radio_nt, 0, 0, 0);
        radioButton4.setTypeface(tf);

        final RadioButton radioButton5 = (RadioButton) findViewById(R.id.radioMainCorp);
        radioButton5.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.custom_btn_radio_nt, 0, 0, 0);
        radioButton5.setTypeface(tf);

        final RadioButton radioButton6 = (RadioButton) findViewById(R.id.locationsame1);
        radioButton6.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.custom_btn_radio_nt, 0, 0, 0);
        radioButton6.setTypeface(tf);

        final RadioButton radioButton7 = (RadioButton) findViewById(R.id.locationsame2);
        radioButton7.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.custom_btn_radio_nt, 0, 0, 0);
        radioButton7.setTypeface(tf);

        final RadioButton radioButton8 = (RadioButton) findViewById(R.id.RadioButtonWhenWasLodged1);
        radioButton8.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.custom_btn_radio_nt, 0, 0, 0);
        radioButton8.setTypeface(tf);

        final RadioButton radioButton9 = (RadioButton) findViewById(R.id.RadioButtonWhenWasLodged2);
        radioButton9.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.custom_btn_radio_nt, 0, 0, 0);
        radioButton9.setTypeface(tf);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    allow = true;
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void setRbDrawableAR() {

        final RadioButton radioButton1 = (RadioButton) findViewById(R.id.didyoucomplain1);
        radioButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.custom_btn_radio_nt, 0);
        radioButton1.setTypeface(tf);
        final RadioButton status1 = (RadioButton) findViewById(R.id.status1);
        status1.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.custom_btn_radio_nt, 0);
        radioButton1.setTypeface(tf);
        final RadioButton radioButton2 = (RadioButton) findViewById(R.id.didyoucomplain2);
        radioButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.custom_btn_radio_nt, 0);
        radioButton2.setTypeface(tf);

        final RadioButton radioButton3 = (RadioButton) findViewById(R.id.didyoucomplain3);
        radioButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.custom_btn_radio_nt, 0);
        radioButton3.setTypeface(tf);

        final RadioButton radioButton4 = (RadioButton) findViewById(R.id.radioMainIndv);
        radioButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.custom_btn_radio_nt, 0);
        radioButton4.setTypeface(tf);

        final RadioButton radioButton5 = (RadioButton) findViewById(R.id.radioMainCorp);
        radioButton5.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.custom_btn_radio_nt, 0);
        radioButton5.setTypeface(tf);

        final RadioButton radioButton6 = (RadioButton) findViewById(R.id.RadioButtonWhenWasLodged1);
        radioButton6.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.custom_btn_radio_nt, 0);
        radioButton6.setTypeface(tf);

        final RadioButton radioButton7 = (RadioButton) findViewById(R.id.RadioButtonWhenWasLodged2);
        radioButton7.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.custom_btn_radio_nt, 0);
        radioButton7.setTypeface(tf);

        final RadioButton radioButton8 = (RadioButton) findViewById(R.id.locationsame1);
        radioButton8.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.custom_btn_radio_nt, 0);
        radioButton8.setTypeface(tf);

        final RadioButton radioButton9 = (RadioButton) findViewById(R.id.locationsame2);
        radioButton9.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.custom_btn_radio_nt, 0);
        radioButton9.setTypeface(tf);
    }


    private void setLookups() {
        final TextView titlebar = (TextView) findViewById(R.id.title);
        titlebar.setVisibility(View.GONE);
        titlebar.setTypeface(tf);
        final ImageView logoBAr = (ImageView) findViewById(R.id.minlogo_button);
        logoBAr.setVisibility(View.VISIBLE);

        final TextView headerTxt = (TextView) findViewById(R.id.complaint_header);
        String eventName = bundle.getString("eventName");
        String enentCateg = bundle.getString("eventCateg");
        String event;
        if (IssueType.equalsIgnoreCase("2")) {
            event = getResources().getString(R.string.inquiry);
        } else {
            event = getResources().getString(R.string.complaint);
        }
        if (issue_id == 1)
            enentCateg = getResources().getString(R.string.mobile2);
        else
            enentCateg = getResources().getString(R.string.phone2);
        String header = event + " : " + enentCateg + " : " + eventName;
        headerTxt.setText(header);
        headerTxt.setTypeface(tf);
        if (MyApplication.nightMod) {
            headerTxt.setTextColor(getResources().getColor(R.color.white));
            headerTxt.setBackgroundColor(getResources().getColor(
                    R.color.nightBlue));
        }
        try {
            final TextView textView15 = (TextView) findViewById(R.id.issue_label);
            textView15.setTypeface(tf);

            phone_label = (TextView) findViewById(R.id.phone_label);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                phone_label.setText(getLookup("17").namear);
            } else {
                phone_label.setText(getLookup("17").nameen);
            }
            phone_label.setTypeface(tf);
            final RadioButton radioButton4 = (RadioButton) findViewById(R.id.radioMainIndv);

            radioButton4.setTypeface(tf);

            final RadioButton radioButton5 = (RadioButton) findViewById(R.id.radioMainCorp);

            radioButton5.setTypeface(tf);
            final TextView textView2 = (TextView) findViewById(R.id.Didyoucompalinquestion);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                textView2.setText(getLookup("18").namear);
            } else {
                textView2.setText(getLookup("18").nameen);
            }
            textView2.setTypeface(tf);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                specialneednumbertxt.setText(getLookup("50").namear);
            } else {
                specialneednumbertxt.setText(getLookup("50").nameen);
            }
            specialneednumbertxt.setTypeface(tf);

            final RadioButton radioButton1 = (RadioButton) findViewById(R.id.didyoucomplain1);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                radioButton1.setText(getLookup("3").namear);
            } else {
                radioButton1.setText(getLookup("3").nameen);
            }
            radioButton1.setTypeface(tf);

            final RadioButton radioButton2 = (RadioButton) findViewById(R.id.didyoucomplain2);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                radioButton2.setText(getLookup("4").namear);
            } else {
                radioButton2.setText(getLookup("4").nameen);
            }
            radioButton2.setTypeface(tf);

            final RadioButton radioButton3 = (RadioButton) findViewById(R.id.didyoucomplain3);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                radioButton3.setText(getLookup("5").namear);
            } else {
                radioButton3.setText(getLookup("5").nameen);
            }
            radioButton3.setTypeface(tf);

            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                WhenWasLodgedText.setText(getLookup("11").namear);
            } else {
                WhenWasLodgedText.setText(getLookup("11").nameen);
            }
            WhenWasLodgedText.setTypeface(tf);

            final TextView textView3 = (TextView) findViewById(R.id.referncenumberTitle);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                textView3.setText(getLookup("21").namear);

            } else {
                textView3.setText(getLookup("21").nameen);
            }

            textView3.append(" *");
            String text = textView3.getText().toString();
            String htmlText = text.replace("*", "<font color='#ff0000'>*</font>");
            textView3.setText(Html.fromHtml(htmlText));
            textView3.setTypeface(tf);

            final TextView textView4 = (TextView) findViewById(R.id.callNumTxt);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                textView4.setText(getLookup("19").namear);
            } else {
                textView4.setText(getLookup("19").nameen);
            }
            if (append2) {

                textView4.append(" *");
                String text3 = textView4.getText().toString();
                String htmlText3 = text3.replace("*", "<font color='#ff0000'>*</font>");
                textView4.setText(Html.fromHtml(htmlText3));

                append2 = false;
            }
            textView4.setTypeface(tf);

            final TextView textView5 = (TextView) findViewById(R.id.locationsametext);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                textView5.setText(getLookup("12").namear);
            } else {
                textView5.setText(getLookup("12").nameen);
            }
            textView5.setTypeface(tf);

            final TextView textView6 = (TextView) findViewById(R.id.mapLayoutTxt);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                textView6.setText(getLookup("13").namear);
            } else {
                textView6.setText(getLookup("13").nameen);
            }
            textView6.setTypeface(tf);

            final TextView textView7 = (TextView) findViewById(R.id.locCurrentTxt);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                textView7.setText(getLookup("20").namear);
            } else {
                textView7.setText(getLookup("20").nameen);
            }
            textView7.setTypeface(tf);
//            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
//                areaTxt.setText(getLookup("56").namear);
//            } else {
//                areaTxt.setText(getLookup("56").nameen);
//            }
//            areaTxt.setTypeface(tf);
//            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
//                subareaTxt.setText(getLookup("57").namear);
//            } else {
//                subareaTxt.setText(getLookup("57").nameen);
//            }
//            subareaTxt.setTypeface(tf);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                status1.setText(getLookup("6").namear);
            } else {
                status1.setText(getLookup("6").nameen);
            }
            status1.setTag("6");
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                status2.setText(getLookup("8").namear);
            } else {
                status2.setText(getLookup("8").nameen);
            }
            status2.setTag("8");
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {//for date of request !!
                ivDatetxt.setText(getLookup("53").namear);
            } else {
                ivDatetxt.setText(getLookup("53").nameen);
            }
            ivDatetxt.setTag("53");
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                channelquestion.setText(getLookup("40").namear);
            } else {
                channelquestion.setText(getLookup("40").nameen);
            }
            channelquestion.setTypeface(tf);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                statusquestion.setText(getLookup("51").namear);
            } else {
                statusquestion.setText(getLookup("51").nameen);
            }
            statusquestion.setTypeface(tf);
            channelanswer1 = (RadioButton) findViewById(R.id.channelanswer1);
            channelanswer2 = (RadioButton) findViewById(R.id.channelanswer2);
            channelanswer3 = (RadioButton) findViewById(R.id.channelanswer3);
            channelanswer4 = (RadioButton) findViewById(R.id.channelanswer4);
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                channelanswer1.setText(getLookup("41").namear);

            } else {
                channelanswer1.setText(getLookup("41").nameen);
            }
            channelanswer1.setTypeface(tf);
            channelanswer1.setTag("41");

            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                channelanswer2.setText(getLookup("42").namear);
            } else {
                channelanswer2.setText(getLookup("42").nameen);
            }
            channelanswer2.setTypeface(tf);
            channelanswer2.setTag("42");

            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                channelanswer3.setText(getLookup("43").namear);
            } else {
                channelanswer3.setText(getLookup("43").nameen);
            }
            channelanswer3.setTypeface(tf);
            channelanswer3.setTag("43");

            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                channelanswer4.setText(getLookup("44").namear);
            } else {
                channelanswer4.setText(getLookup("44").nameen);
            }
            channelanswer4.setTypeface(tf);
            channelanswer4.setTag("44");
        } catch (Exception e) {
            Log.d("ee", e + "");
        }
    }


    public String isBlocked(String num) {
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


    class Task implements Runnable {

        @Override
        public void run() {
            TCTDbAdapter source = new TCTDbAdapter(ComplainFormActivity.this);
            source.open();
            ArrayList<Profile> arr = new ArrayList<>();

            arr = source.getAllProfiles();

            try {
                defMobile = arr.get(0).getnum();
            }catch (Exception e){
                e.printStackTrace();
            }

            if(arr.size() > 0){
                number.setText("" + defMobile);
                Log.wtf("3093","defMobile = " + defMobile);
                reg_id = arr.get(0).getId();
                gettok = gettoken();
                qatarID = arr.get(0).getqatarID();
                num = arr.get(0).getnum();
                emailtxt = arr.get(0).getemail();
            }
            source.close();
        }
    }


    public void ShowSpecialNeedDialog() {
        if (!IssueType.equals("2")) {

            if (!checkapponDate.equals("true")) {
                radioGroupDidYouComplain.setVisibility(View.VISIBLE);
                txtRadio.setVisibility(View.VISIBLE);
            }
            // radioGroup.requestFocus();
            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
            qidTxt.setText(getResources().getString(
                    R.string.indv2));
            linear.setVisibility(View.VISIBLE);


            qidEdt.setText("");
            String msg = "";
            //by hseny
            if (MyApplication.Lang.equals(MyApplication.ARABIC))
                msg = getLookup("47").namear;
            else
                msg = getLookup("47").nameen;
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
            //builder.setTitle(ComplainFormActivity.this.getString(R.string.warning2));
            TextView myMsg = new TextView(this);
            myMsg.setTextColor(Color.BLACK);
            myMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
            myMsg.setPadding(20, 20, 20, 20);
            builder.setView(myMsg);

            String text = "\n" + msg + "\n";

            myMsg.setText(msg);
            builder.setPositiveButton(R.string.yes2, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                    dialog.dismiss();
                    isSpecialNeed = true;

                    // change the duration

                    RadioButtonWhenWasLodged1.setText(getResources()
                            .getString(
                                    R.string.more_than)
                            + " "
                            + specialneedduration);

                    RadioButtonWhenWasLodged2.setText(getResources()
                            .getString(
                                    R.string.less_than)
                            + " "
                            + specialneedduration);
                    showSpecialNumberRegistrationDialog();
                }
            });
            builder.setNegativeButton(R.string.no2, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();


                    isSpecialNeed = false;
                    if (checkapponDate.equals("true")) {

                        dateOfRequestLayout.setVisibility(View.VISIBLE);
                        radioGroupDidYouComplain.setVisibility(View.GONE);
                        txtRadio.setVisibility(View.GONE);
                        if (ivDate.getText().toString().length() != 0) {
                            final Calendar cl = Calendar.getInstance();
                            long diff = cl.getTimeInMillis() - todate.getTimeInMillis();
                            long days = diff / (24 * 60 * 60 * 1000);
                            //   Toast.makeText(ComplainFormActivity.this, days + "", Toast.LENGTH_LONG).show();
                            if (days < 14) {
                                createDialogForWorkingDays();
                            }
                        } else {
                            dateOfRequestLayout.setVisibility(View.VISIBLE);
                            radioGroupDidYouComplain.setVisibility(View.GONE);
                            txtRadio.setVisibility(View.GONE);
                        }

                        //by hsen
                        cmpdate.setEnabled(true);
                        cmpdate.setClickable(true);
                        ivDate.setEnabled(true);
                        ivDate.setClickable(true);
                    }
                    RadioButtonWhenWasLodged1.setText(getResources()
                            .getString(
                                    R.string.more_than)
                            + " " + cmpTime);
                    RadioButtonWhenWasLodged2.setText(getResources()
                            .getString(
                                    R.string.less_than)
                            + " " + cmpTime);
                    specialneednumbertxt.setVisibility(View.GONE);
                    etspecialneednb.setVisibility(View.GONE);
                    // continue with delete
                }
            });
            //   .setIcon(android.R.drawable.ic_dialog_alert)
            final AlertDialog dg = builder.create();

            /*dg.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    ((Button)dg.getButton(Dialog.BUTTON_POSITIVE)).setTypeface(tf);
                    ((Button)dg.getButton(Dialog.BUTTON_NEGATIVE)).setTypeface(tf);

                }
            });*/

            dg.show();
            /*TextView textView = (TextView) dg.findViewById(android.R.id.message);
            textView.setTypeface(tf);*/
//            AlertDialog.Builder builder = new AlertDialog.Builder(
//                    ComplainFormActivity.this);
//            TextView textView;
//            // Get the layout inflater
//            LayoutInflater inflater = ComplainFormActivity.this
//                    .getLayoutInflater();
//            final View textEntryView = inflater.inflate(
//                    R.layout.dialog_layout, null);
//            textView = (TextView) textEntryView
//                    .findViewById(R.id.dialogMsg);
//            textView.setGravity(Gravity.CENTER);
//            if (MyApplication.Lang
//                    .equals(MyApplication.ARABIC))
//                textView.setText(getLookup("47").namear);
//            else
//                textView.setText(getLookup("47").nameen);
//            // Inflate and set the layout for the dialog
//            textView.setTypeface(tf);
//            // Pass null as the parent view because its
//            // going in
//            // the dialog layout
//            builder.setView(textEntryView)
//                    // Add action buttons
//                    .setNegativeButton(
//                            getString(R.string.no2),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(
//                                        DialogInterface dialog,
//                                        int id) {
//                                    dialog.dismiss();
//                                    isSpecialNeed = false;
//                                    if (checkapponDate.equals("true")) {
//
//                                        dateOfRequestLayout.setVisibility(View.VISIBLE);
//                                        radioGroupDidYouComplain.setVisibility(View.GONE);
//                                        txtRadio.setVisibility(View.GONE);
//                                        if (ivDate.getText().toString().length() != 0) {
//                                            final Calendar cl = Calendar.getInstance();
//                                            long diff = cl.getTimeInMillis() - todate.getTimeInMillis();
//                                            long days = diff / (24 * 60 * 60 * 1000);
//                                            //   Toast.makeText(ComplainFormActivity.this, days + "", Toast.LENGTH_LONG).show();
//                                            if (days < 14) {
//                                                createDialogForWorkingDays();
//                                            }
//                                        } else {
//                                            dateOfRequestLayout.setVisibility(View.VISIBLE);
//                                            radioGroupDidYouComplain.setVisibility(View.GONE);
//                                            txtRadio.setVisibility(View.GONE);
//                                        }
//                                    }
//                                    RadioButtonWhenWasLodged1.setText(getResources()
//                                            .getString(
//                                                    R.string.more_than)
//                                            + " " + cmpTime);
//                                    RadioButtonWhenWasLodged2.setText(getResources()
//                                            .getString(
//                                                    R.string.less_than)
//                                            + " " + cmpTime);
//                                    specialneednumbertxt.setVisibility(View.GONE);
//                                    etspecialneednb.setVisibility(View.GONE);
//                                }
//                            })
//                    .setPositiveButton(
//                            getString(R.string.yes2),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(
//                                        DialogInterface dialog,
//                                        int id) {
//                                    dialog.dismiss();
//                                    isSpecialNeed = true;
//
//                                    // change the duration
//
//                                    RadioButtonWhenWasLodged1.setText(getResources()
//                                            .getString(
//                                                    R.string.more_than)
//                                            + " "
//                                            + specialneedduration);
//
//                                    RadioButtonWhenWasLodged2.setText(getResources()
//                                            .getString(
//                                                    R.string.less_than)
//                                            + " "
//                                            + specialneedduration);
//                                    showSpecialNumberRegistrationDialog();
//
//                                }
//                            });
//            Dialog d = builder.create();
//            d.getWindow().setLayout(
//                    (int) (ComplainFormActivity.this
//                            .getWindow().peekDecorView()
//                            .getWidth() * 0.9),
//                    (int) (ComplainFormActivity.this
//                            .getWindow().peekDecorView()
//                            .getHeight() * 0.9));
//            d.setCanceledOnTouchOutside(false);
//            d.show();
        }
    }


    public void submit(View v) {

        if (((IssuesDetails) IssueSpin.getSelectedItem()).getId() == -1) {

            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                onCreateDialogNew(ComplainFormActivity.this, getLookup("98").nameen);
            } else {

                onCreateDialogNew(ComplainFormActivity.this, getLookup("98").namear);
            }
        } else {

            if (((ServicePro) SpSpin.getSelectedItem()).getId() == -1) {

                if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                    onCreateDialogNew(ComplainFormActivity.this, getLookup("100").nameen);
                } else {

                    onCreateDialogNew(ComplainFormActivity.this, getLookup("100").namear);
                }
            } else {


                //<editor-fold desc="Issue Spin not empty">
                if (number.getText().toString().equals("")) {
                    if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                        onCreateDialogNew(ComplainFormActivity.this, getLookup("103").nameen);
                    } else {

                        onCreateDialogNew(ComplainFormActivity.this, getLookup("103").namear);
                    }
                } else {


                    if (comp_phone.getText().toString().equals("")) {

                        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                            onCreateDialogNew(ComplainFormActivity.this, getLookup("102").nameen);
                        } else {

                            onCreateDialogNew(ComplainFormActivity.this, getLookup("102").namear);
                        }

                        /*if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                        firstElement.setName(getLookup("98").nameen);
                    } else {

                        firstElement.setName(getLookup("98").namear);
                    }*/

                    } else {
                        contactNum = comp_phone.getText().toString();
                        if (contactNum.length() < 8) {

                            //Actions.onCreateDialogNew(ComplainFormActivity.this, getResources().getString(R.string.error_mob_qid4));
                            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                                onCreateDialogNew(ComplainFormActivity.this, getLookup("102").nameen);
                            } else {

                                onCreateDialogNew(ComplainFormActivity.this, getLookup("102").namear);
                            }


                        } else {

                            //<editor-fold desc="to be sent">

                            if (qidEdt.getText().toString().equals("")) {
                                if (isIndividual.equalsIgnoreCase("true")) {
                                    onCreateDialogNew(ComplainFormActivity.this,
                                            getResources().getString(R.string.error_mob_qid3));
                                } else {

                                    if (qidEdt.getText().toString().equals(""))
                                        onCreateDialogNew(ComplainFormActivity.this,
                                                getResources().getString(R.string.error_mob_crnum2));
                                }
                            } else {

                                if (isIndividual.equalsIgnoreCase("true") && qidEdt.getText().toString().length() < 8) {
                                    onCreateDialogNew(ComplainFormActivity.this,
                                            getResources().getString(R.string.error_mob_qid3));
                                } else {

                                    if (spComplaint.equals("3") && longWait.equals("true")) {
                                        if (referenceNumbertext.getText().toString().equals("")) {
                                            onCreateDialogNew(ComplainFormActivity.this,
                                                    getResources().getString(R.string.error_cmpl_num2));
                                        } else if (cmpdate.getText().toString().equals("")) {
                                            onCreateDialogNew(ComplainFormActivity.this,
                                                    getResources().getString(R.string.error_campl_date2));
                                        } else {

                                            //<editor-fold desc="mandatory location fields">
                                            if (IssuetypeId == 1 && issue_id == 1) {

                                                if (etbuilding.getVisibility() == View.VISIBLE){

                                                    if (etbuilding.getText().toString().length() > 0 && etzone.getText().toString().length() > 0
                                                            && etstreet.getText().toString().length() > 0) {

                                                        sendRequest();
                                                    } else {
                                                        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                                                            onCreateDialogNew(ComplainFormActivity.this, getLookup("72").nameen);
                                                        } else {

                                                            onCreateDialogNew(ComplainFormActivity.this, getLookup("72").namear);
                                                        }
                                                    }
                                                }else{

                                                    sendRequest();
                                                }

                                            } else {

                                                sendRequest();
                                            }
                                            //</editor-fold>
                                            //sendRequest();
                                        }
                                    } else if (spComplaint.equals("3") && closed) {
                                        if (referenceNumbertext.getText().toString().equals("")) {
                                            onCreateDialogNew(ComplainFormActivity.this,
                                                    getResources().getString(R.string.error_cmpl_num2));
                                        } else if (cmpdate.getText().toString().equals("")) {
                                            onCreateDialogNew(ComplainFormActivity.this,
                                                    getResources().getString(R.string.error_campl_date2));
                                        } else {

                                            //<editor-fold desc="mandatory location fields">
                                            if (IssuetypeId == 1 && issue_id == 1) {

                                                if (etbuilding.getText().toString().length() > 0 && etzone.getText().toString().length() > 0
                                                        && etstreet.getText().toString().length() > 0) {

                                                    sendRequest();
                                                } else {
                                                    if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                                                        onCreateDialogNew(ComplainFormActivity.this, getLookup("72").nameen);
                                                    } else {

                                                        onCreateDialogNew(ComplainFormActivity.this, getLookup("72").namear);
                                                    }
                                                }

                                            } else {

                                                sendRequest();
                                            }
                                            //</editor-fold>

                                            //sendRequest();
                                        }
                                    } else if (spComplaint.equals("4")) {
                                        if (callNumEdt.getVisibility() == View.VISIBLE) {
                                            if (callNumEdt.getText().toString().equals("")) {

                                                if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                                                    onCreateDialogNew(ComplainFormActivity.this, getLookup("103").nameen);
                                                } else {

                                                    onCreateDialogNew(ComplainFormActivity.this, getLookup("103").namear);
                                                }
                                                //Actions.onCreateDialogNew(ComplainFormActivity.this, getResources().getString(R.string.error_call_mob2));
                                            } else {

                                                //<editor-fold desc="mandatory location fields">
                                                if (IssuetypeId == 1 && issue_id == 1) {

                                                    if (calldate.getText().toString().equals("")) {

                                                        Actions.onCreateDialogNew(ComplainFormActivity.this,
                                                                getResources().getString(R.string.error_campl_date2));
                                                    } else {

                                                        if (etbuilding.getText().toString().length() > 0 && etzone.getText().toString().length() > 0
                                                                && etstreet.getText().toString().length() > 0) {

                                                            sendRequest();
                                                        } else {
                                                            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                                                                onCreateDialogNew(ComplainFormActivity.this, getLookup("72").nameen);
                                                            } else {

                                                                onCreateDialogNew(ComplainFormActivity.this, getLookup("72").namear);
                                                            }
                                                        }
                                                    }
                                                } else {


                                                    if (calldate.getText().toString().equals("")) {

                                                        Actions.onCreateDialogNew(ComplainFormActivity.this,
                                                                getResources().getString(R.string.error_campl_date2));
                                                    } else
                                                        sendRequest();
                                                }
                                                //</editor-fold>
                                                //sendRequest();
                                            }
                                        } else if (calldate.getText().toString().equals("")) {
                                            onCreateDialogNew(ComplainFormActivity.this,
                                                    getResources().getString(R.string.error_campl_date2));
                                        } else {

                                            //<editor-fold desc="mandatory location fields">
                                            if (IssuetypeId == 1 && issue_id == 1) {
                                                if(locationlayout.getVisibility()==View.VISIBLE) {
                                                    if (etbuilding.getText().toString().length() > 0 && etzone.getText().toString().length() > 0
                                                            && etstreet.getText().toString().length() > 0) {

                                                        sendRequest();
                                                    } else {
                                                        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                                                            onCreateDialogNew(ComplainFormActivity.this, getLookup("72").nameen);
                                                        } else {

                                                            onCreateDialogNew(ComplainFormActivity.this, getLookup("72").namear);
                                                        }
                                                    }

                                                }else
                                                    sendRequest();


                                            } else {

                                                sendRequest();
                                            }
                                            //</editor-fold>
                                            //sendRequest();
                                        }
                                    } else {

                                        //<editor-fold desc="mandatory location fields">
                                        if (IssuetypeId == 1 && issue_id == 1) {

                                            if(locationlayout.getVisibility()==View.VISIBLE){
                                            if (etbuilding.getText().toString().length() > 0 && etzone.getText().toString().length() > 0
                                                    && etstreet.getText().toString().length() > 0) {

                                                sendRequest();
                                            } else {
                                                if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                                                    onCreateDialogNew(ComplainFormActivity.this, getLookup("72").nameen);
                                                } else {

                                                    onCreateDialogNew(ComplainFormActivity.this, getLookup("72").namear);
                                                }
                                            }}else {
                                                sendRequest();
                                            }

                                        } else {

                                            sendRequest();
                                        }
                                        //</editor-fold>
                                        //sendRequest();
                                    }
                                }


                            }
                            //</editor-fold>
                        }
                    }

                }
                //</editor-fold>
            }
        }


    }


    private void sendRequest() {
        String channelusedid = "0";
        try {
            if (channelradio.getVisibility() == View.VISIBLE) {
                int radioButtonID = channelradio.getCheckedRadioButtonId();
                View radioButton = channelradio.findViewById(radioButtonID);
                channelusedid = (String) radioButton.getTag().toString();

            }
        } catch (Exception e)

        {

        }
        Intent intent = new Intent(ComplainFormActivity.this,
                SendReportActivity.class);
        Bundle bundle = new Bundle();
        // database variables
        bundle.putString("gettok", gettok);
        bundle.putString("from", "complaint");
        bundle.putString("mobilenum", num);
        bundle.putString("contactnum", contactNum);
        bundle.putString("email", emailtxt);
        bundle.putString("qatariID", qatarID);
        if (isIndividual.equalsIgnoreCase("true")) {
            bundle.putString("iniqatarid", qidEdt.getText().toString());
        } else {
            bundle.putString("iniqatarid", "" + qatarID);
        }
        bundle.putString("password", reg_id);
        // form variables
        bundle.putString("IssueType", IssueType);
        bundle.putString("issue", issueName);
        bundle.putString("issueid", issueID);
        bundle.putString("affmobilenum", number.getText().toString());
        bundle.putString("comments", complaint.getText().toString());
        bundle.putString("sp", spName);
        bundle.putString("spid", spId);
        if (!ivDate.getText().toString().equals(""))
            bundle.putString("dateOfRequest", ivDate.getText().toString());
        else
            bundle.putString("dateOfRequest", getRequestDate());
        bundle.putString("locx", "" + latitude);
        bundle.putString("locy", "" + longitude);
        bundle.putString("locat", address);
        bundle.putString("countryid", loc);
        bundle.putString("areaId", areaId);
        bundle.putString("subAreaId", subareaId);
        bundle.putString("street", etstreet.getText().toString());
        bundle.putString("building", etbuilding.getText().toString());
        bundle.putString("zone", etzone.getText().toString());
        bundle.putString("spComplaint", spComplaint);
        bundle.putString("longWait", longWait);

        if (!isSpecialNeed) {
            if (sendwaittime)
                bundle.putString("waitTime", cmpTime);
            else
                bundle.putString("waitTime", "");
        } else {
            if (sendwaittime)
                bundle.putString("waitTime", specialneedduration);
            else
                bundle.putString("waitTime", "");
        }


        /*RadioButtonWhenWasLodged1.setText(getResources().getString(
                R.string.more_than) + " " + specialneedduration);

        RadioButtonWhenWasLodged2.setText(getResources().getString(
                R.string.less_than) + " " + specialneedduration);*/

        bundle.putString("cmplNum", referenceNumbertext.getText().toString());
        bundle.putString("cmplDate", date);
        bundle.putString("callNum", callNumEdt.getText().toString());
        bundle.putString("calldate", date);
        bundle.putString("isIndividual", isIndividual);
        bundle.putString("isCorporate", isCorporate);
        bundle.putString("channelusedid", channelusedid);
        bundle.putString("isspecialneed", isSpecialNeed + "");

        Log.wtf("is special need", "" + isSpecialNeed);

        bundle.putString("specialneedregnumber", specialneednumber);
        if (isCorporate.equalsIgnoreCase("true")) {
            bundle.putString("CRNumber", qidEdt.getText().toString());
        } else {
            bundle.putString("CRNumber", "");
        }
        // bundle.putString("roam_country",roamsp.getText().toString());
        // other variables
        bundle.putString("date", getcurrentDate());
        bundle.putString("creationdate", getcurrentDate());
        bundle.putString("sendingdate", getcurrentDate());
        bundle.putString("date_aff", date);
        bundle.putString("status", "sended");
        intent.putExtras(bundle);
        startActivity(intent);

    }


    void registerLocationUpdates() {
        locationlayout.setVisibility(View.GONE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) this .getSystemService(LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        provider = locationManager.getBestProvider(criteria, true);
        if (provider == null) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ComplainFormActivity.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION } , 1);
            }

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ComplainFormActivity.this, new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION } , 2);
            }

            return;
        }
        else{

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Location oldLocation = locationManager.getLastKnownLocation(provider);

            if (oldLocation != null) {
                waitingForLocationUpdate = false;
            }
        }
    }


    private class MyLocationListener implements LocationListener {
        TextView loca;

        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("hello im here", "the value are " + latitude + " "
                    + longitude);
            loca = (TextView) findViewById(R.id.loclabeldet);
            loca.setText(getcountrycode1(latitude, longitude));
            try {
                dialog.dismiss();
            } catch (Exception e) {

            }
            address = loca.getText().toString();
            if (waitingForLocationUpdate) {
                waitingForLocationUpdate = false;
            }

            if (ActivityCompat.checkSelfPermission(ComplainFormActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ComplainFormActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            if (googleMap != null) {
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(location.getLatitude(), location
                                        .getLongitude())).title("My location")
                        .draggable(true));
            }
        }

        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        public void onProviderEnabled(String s) {
        }

        public void onProviderDisabled(String s) {
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


    protected class FillInitialMapSettings extends
            AsyncTask<Void, Void, InitialMapSettings> {

        ArrayList<InitialMapSettings> result;

        @Override
        protected void onPreExecute() {
            //pd = ProgressDialog.show( ComplainFormActivity.this, "", ComplainFormActivity.this.getResources().getString(R.string.loading), false);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected InitialMapSettings doInBackground(Void... a) {

            DatabaseHandler db = new DatabaseHandler(ComplainFormActivity.this);
            InitialMapSettings map = new InitialMapSettings();

            if (Actions.isNetworkAvailable(ComplainFormActivity.this)) {
                //Connection.disableSSLCertificateChecking();
                // String url = getResources().getString(R.string.mapLink) +
                // getResources().getString(R.string.initialMapSettings);
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
            zoom = Float.parseFloat(r.getZoomSettings());

            initialzoom = Float.parseFloat(r.getZoomSettings());
            location = new LatLng(latitude, longitude);
            try {

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        location, Float.parseFloat(r.getZoomSettings())));
                // Zoom in, animating the camera.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(Float
                        .parseFloat(r.getZoomSettings())), 2000, null);
                //pd.dismiss();

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {

            }

            mScrollView.fullScroll(View.FOCUS_DOWN);
        }
    }


    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }


    public void backTo(View v) {
        Actions.backTo(this);
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        lang = Actions.setLocalComplaint(this);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    @Override
    public void afterTextChanged(Editable s) {

    }


    public ArrayAdapter<IssuesDetails> initIssueDetailSp(Spinner spinner,
                                                         ArrayList<IssuesDetails> arrayList) {

        ArrayAdapter<IssuesDetails> adapter = new ArrayAdapter<IssuesDetails>( ComplainFormActivity.this, R.layout.spinner_text) {

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
               /* View v = convertView;
                if (v == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.spinner_list_text, null);
                    v.setTag(this.getItem(position).getId());
                }
                TextView tv = (TextView) v.findViewById(R.id.TextView01);
                tv.setText(this.getItem(position).getName());
                tv.setTypeface(tf);
                // ViewResizing.setListRowTextResizing(v,ComplainFormActivity.this);
                return v;*/

                View v = null;
                // If this is the initial dummy entry, make it hidden
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                } else {
                    // Pass convertView as null to prevent reuse of special case views

                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.spinner_list_text, null);
                    v.setTag(this.getItem(position).getId());

                    TextView tv = (TextView) v.findViewById(R.id.TextView01);

                    tv.setText(this.getItem(position).getName());
                    tv.setTypeface(tf);
                }

                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = vi
                            .inflate(R.layout.spinner_item_day_en, null);
                }
                TextView tv = (TextView) convertView
                        .findViewById(R.id.TextView01);
                tv.setText(this.getItem(position).getName());
                tv.setTypeface(tf);
                if (MyApplication.nightMod) {
                    ImageView Iv = (ImageView) convertView
                            .findViewById(R.id.spinnerArrow);
                    Iv.setImageResource(R.drawable.arrow_spinner_nt);
                }
                ViewResizing.setListRowTextResizing(convertView, ComplainFormActivity.this);
                return convertView;

            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_list_text);
        spinner.setAdapter(adapter);

        for (int i = 0; i < arrayList.size(); i++) {

            adapter.add(arrayList.get(i));
        }

        adapter.notifyDataSetChanged();
        return adapter;
    }


    public ArrayAdapter<ServicePro> initializeSpSpin(Spinner spinner, final ArrayList<ServicePro> arrayList) {

        ArrayAdapter<ServicePro> adapter = new ArrayAdapter<ServicePro>(ComplainFormActivity.this, R.layout.spinner_text) {

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = null;
                /*
                View v = convertView;
                if (v == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.spinner_list_text, null);
                    v.setTag(this.getItem(position).getId());
                }


                    TextView tv = (TextView) v.findViewById(R.id.TextView01);

                    tv.setText(this.getItem(position).getName());
                    tv.setTypeface(tf);

                return v;*/


                // If this is the initial dummy entry, make it hidden
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                } else {
                    // Pass convertView as null to prevent reuse of special case views

                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.spinner_list_text, null);
                    v.setTag(this.getItem(position).getId());

                    TextView tv = (TextView) v.findViewById(R.id.TextView01);

                    tv.setText(this.getItem(position).getName());
                    tv.setTypeface(tf);
                }

                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = vi
                            .inflate(R.layout.spinner_item_day_en, null);
                }
                TextView tv = (TextView) convertView
                        .findViewById(R.id.TextView01);
                tv.setTypeface(tf);
                tv.setText(this.getItem(position).getName());
                if (MyApplication.nightMod) {
                    ImageView Iv = (ImageView) convertView
                            .findViewById(R.id.spinnerArrow);
                    Iv.setImageResource(R.drawable.arrow_spinner_nt);
                }
                ViewResizing.setListRowTextResizing(convertView, ComplainFormActivity.this);
                return convertView;
            }
        };

        adapter.setDropDownViewResource(R.layout.spinner_list_text);
        //arrayList.remove(0);
        adapter.addAll(arrayList);
        spinner.setAdapter(adapter);
        /*for (int i = 0; i < arrayList.size(); i++) {

            adapter.add(arrayList.get(i));
        }

        adapter.notifyDataSetChanged();*/
        return adapter;
    }


    public String gettoken() {
        String name = "";
        try {
            URL url = new URL("" + MyApplication.link + "PostData.asmx/StartSendingReport?password=" + reg_id);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("string");
            Element nameElement = (Element) nodeList.item(0);
            nodeList = nameElement.getChildNodes();
            name = ((Node) nodeList.item(0)).getNodeValue();
        } catch (Exception e) {
            System.out.println("gg " + e);
            String url1 = "" + MyApplication.link
                    + "PostData.asmx/VerifyRegistration";
            Connection conn = new Connection(url1);
            try {
//                String error_return = conn.executeMultipartPost_Send_Error(this
//                                .getClass().getSimpleName(), Actions.getDeviceName(),
//                        "1", e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
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


    public String getRequestDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",
                new Locale("en"));
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {


        /*if (ivDate.getText().toString().equals(""))
        //    showDialog(999);
        showDateTimePicker();
        else {
            Log.wtf("DATE","LOCKED");
            calldate.setText(ivDate.getText().toString());
        }*/

        try {

            showDateTimePicker();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {

            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            if (arg2 == 0)
                arg2 = 12;

            showDate(arg1, arg2 + 1, arg3);
        }
    };


    public String showDateTimePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(ComplainFormActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        monthOfYear = monthOfYear + 1;
                        String mm = String.valueOf(monthOfYear);
                        if (mm.length() == 1)
                            mm = "0" + mm;
                        String dd = String.valueOf(dayOfMonth);
                        if (dd.length() == 1)
                            dd = "0" + dd;

                        showDate(year, Integer.parseInt(mm), Integer.parseInt(dd));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

        return "";
    }


    private void showDate(int year, int month, int day) {

        //by hsen
        //<editor-fold desc="by hsen">
        cmpdate.setTypeface(MyApplication.facePolarisMedium);
        calldate.setTypeface(MyApplication.facePolarisMedium);
        if (status1.isChecked() && RadioButtonWhenWasLodged1.isChecked()) {


            Calendar calendar2 = Calendar.getInstance();
            calendar2.add(Calendar.MONTH, 2);
            calendar2.set(Calendar.MILLISECOND, 0);
            calendar2.set(Calendar.SECOND, 0);
            calendar2.set(Calendar.MINUTE, 0);
            calendar2.set(Calendar.HOUR_OF_DAY, 0);
            mYear = calendar2.get(Calendar.YEAR);
            mMonth = calendar2.get(Calendar.MONTH);
            mDay = calendar2.get(Calendar.DAY_OF_MONTH);
            if (mMonth == 1) mMonth = 12;
            else mMonth = mMonth - 1;
            calendar2.set(mYear, mMonth, mDay);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.MILLISECOND, 0);
            calendar1.set(Calendar.SECOND, 0);
            calendar1.set(Calendar.MINUTE, 0);
            calendar1.set(Calendar.HOUR_OF_DAY, 0);

            calendar1.set(year, month, day);


            long diff = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
            long days = diff / (24 * 60 * 60 * 1000);
            //int days =  (int)dayss+1;

            if (isSpecialNeed) {
                if (days < specialDays) {
                    //dialog
                    createDialog2ForWorkingDays();
                    calldate.setText("");
                    cmpdate.setText("");
                } else {

                    calldate.setText(new StringBuilder().append(year).append("-")
                            .append(month).append("-").append(day));
                    cmpdate.setText(new StringBuilder().append(year).append("-")
                            .append(month).append("-").append(day));
                    date = year + "-" + month + "-" + day;
                }
            } else {


                if (days < normalDays) {
                    //dialog
                    createDialog2ForWorkingDays();
                    calldate.setText("");
                    cmpdate.setText("");
                } else {

                    calldate.setText(new StringBuilder().append(year).append("-")
                            .append(month).append("-").append(day));
                    cmpdate.setText(new StringBuilder().append(year).append("-")
                            .append(month).append("-").append(day));
                    date = year + "-" + month + "-" + day;
                }
            }

        } else {

            calldate.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
            cmpdate.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
            date = year + "-" + month + "-" + day;
        }
        //</editor-fold>

        /*calldate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        cmpdate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        date = year + "/" + month + "/" + day;*/
    }


    public class sendreq extends AsyncTask<Void, Void, Integer> {
        com.ids.ict.Error error;
        Event[] nn;
        int visibility;
        String nb = "", compainttxt;
        String channelusedid = "0";

        String streetName, buildingName, zoneName = "";

        @Override
        public void onPreExecute() {
            //dialog = ProgressDialog.show(ComplainFormActivity.this, "", "Loading..Wait..", true);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            visibility = channelradio.getVisibility();

            try {
                if (visibility == View.VISIBLE) {
                    int radioButtonID = channelradio
                            .getCheckedRadioButtonId();
                    View radioButton = channelradio
                            .findViewById(radioButtonID);
                    channelusedid = (String) radioButton.getTag()
                            .toString();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            nb = number.getText().toString();
            compainttxt = complaint.getText().toString();

            streetName = etstreet.getText().toString();
            buildingName = etbuilding.getText().toString();
            zoneName = etzone.getText().toString();

            dateiv = ivDate.getText().toString();
            if (dateiv.equals(""))
                dateiv = date;
        }

        @Override
        public Integer doInBackground(Void... params) {

            try {
                Connection conn = null;
                conn = new Connection("" + app.link
                        + "PostData.asmx/SendReport");
                try {

                    s = conn.executeMultipartPost_event(
                            "PostData.asmx/SendReport3", gettok, num, nb, qatarID, emailtxt,
                            issueID, spId, "1", getcurrentDate(),
                            getcurrentDate(), compainttxt, ""
                                    + latitude, "" + longitude, loc, qatarID,
                            "6", IssueType, "0", "false", "0", date, "", date, //issue type w bl rayeh
                            "false", "false", "0", "1", channelusedid, String
                                    .valueOf(isSpecialNeed), specialneednumber, dateiv, areaId, subareaId, streetName, buildingName, zoneName, "");

                    System.out.println("String  " + s);

                    String m = splitres(s);
                    try {
                        k = Integer.parseInt(m);
                    } catch (Exception e) {
                        k = 0;
                    }
                    TCTDbAdapter datasource = new TCTDbAdapter(
                            ComplainFormActivity.this);
                    datasource.open();
                    if (k > 0) {
                        try {
                            long ln = datasource.createReports_off(reg_id, num,
                                    emailtxt, qatarID, "English",
                                    "Test Test Test", loc, getcurrentDate(),
                                    date, "Poor Coverage Indoor", "sended",
                                    "73", "5", "" + latitude, "" + longitude,
                                    loc, qatarID, "فودافون قطر");

                            if (!(ln > -1)) {
                                throw new Exception();
                            }
                            return 1;
                        } catch (Exception e) {
                            return 0;
                        }
                    } else {
                        try {
                            long ln = datasource.createReports_off(reg_id, num,
                                    emailtxt, qatarID, "English",
                                    "Test Test Test", loc, getcurrentDate(),
                                    date, "Poor Coverage Indoor", "Failed",
                                    "73", "5", "" + latitude, "" + longitude,
                                    loc, qatarID, "فودافون قطر");
                            if (!(ln > -1)) {
                                throw new Exception();
                            }
                            onCreateDialogNew(ComplainFormActivity.this,
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
//                                ComplainFormActivity.this);
//                        datasource.open();
//                        long ln = datasource.createReports_off(reg_id, num,
//                                emailtxt, qatarID, "English", "Test Test Test",
//                                loc, getcurrentDate(), date,
//                                "Poor Coverage Indoor", "Failed", "73", "5", ""
//                                        + latitude, "" + longitude, loc,
//                                qatarID, "فودافون قطر");
//                        if (!(ln > -1)) {
//                            throw new Exception();
//                        }
//                        ArrayList<Mail_OFF> arr = datasource
//                                .getLastReports_off();
//                        datasource.close();
//

                        Mail_OFF tick = new Mail_OFF();
                        tick.setAreaId(areaId);
                        tick.setBuilding(buildingName);
                        tick.setStreet(streetName);
                        tick.setToken(gettok);
                        tick.setnum(num);
                        tick.setPassword(reg_id);
                        tick.setAffectedNumber(nb);
                        tick.setqatarID(qatarID);
                        tick.setemail(emailtxt);
                        tick.setissueid(issueID);
                        tick.setspid(spId);
                        tick.setcomm(compainttxt);
                        tick.setlocy(latitude + "");
                        tick.setlocx(longitude + "");
                        tick.setloc(loc);
                        tick.setaffcqatar(qatarID);
                        tick.setissuename(IssueType);
                        tick.setdate(date);
                        tick.setChannelUsedId(channelusedid);
                        tick.setIsSpecialNeed("" + isSpecialNeed);
                        tick.setSpecialneednumber(specialneednumber);
                        tick.setDateOfRequest(dateiv);
                        tick.setAreaId(areaId);
                        tick.setSubAreaId(subareaId);
                        tick.setStreet(streetName);
                        tick.setBuilding(buildingName);
                        tick.setZone(zoneName);
                        tick.setContactNumber("");


                        SharedPreference settings = new SharedPreference();
                        ArrayList<Mail_OFF> array = settings
                                .getFavorites(getApplicationContext());
                        if (array == null) {
                            array = new ArrayList<Mail_OFF>();
                            array.add(tick);
                            settings.saveFavorites(getApplicationContext(), array);
                        } else
                            settings.addFavorite(getApplicationContext(),
                                    tick);
                        // editEn.putString(code, nameen);
                        // editAr.putString(code, namear);

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


            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            try {
                if (result == 0) {
                    IssueType = "-1";
                }
                Intent intent = new Intent(ComplainFormActivity.this,
                        SuccessReportConfirmActivity.class);
                Bundle bundle = new Bundle();

                Log.wtf("ComplaintFormIssueType", "" + IssueType);

                bundle.putString("IssueType", IssueType);
                intent.putExtras(bundle);
                startActivity(intent);

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


    public String splitres(String res) {
        String[] a = res.split(">");
        String[] b = a[2].split("</");
        return b[0];
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


    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.setClass(ComplainFormActivity.this, ComplaintActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ComplainFormActivity.this.startActivity(intent);
        ComplainFormActivity.this.finish();

    }


    public void topBarBack(View v) {

        Intent intent = new Intent();
        intent.setClass(ComplainFormActivity.this, ComplaintActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ComplainFormActivity.this.startActivity(intent);
        ComplainFormActivity.this.finish();
    }


    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(ComplainFormActivity.this, MoreActivity.class);
                ComplainFormActivity.this.startActivity(intent);
                break;
            }
            case R.id.home: {

                intent.setClass(ComplainFormActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ComplainFormActivity.this.startActivity(intent);
                ComplainFormActivity.this.finish();
                break;
            }

        }
    }
}