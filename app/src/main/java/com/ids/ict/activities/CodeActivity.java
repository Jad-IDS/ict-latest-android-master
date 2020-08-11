package com.ids.ict.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.Country;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.MyExceptionHandler;
import com.ids.ict.classes.Profile;
import com.ids.ict.R;
import com.ids.ict.classes.ServicePro;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.classes.LookUp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.fabric.sdk.android.Fabric;


public class CodeActivity extends Activity {

    Bundle mbundle;

    ProgressDialog dialog;
    ProgressBar prog;
    String lang = "", eventsSource = "", lan = "", error_msg = "", otp_ref;
    Connection conn;
    EditText code_text;
    MyApplication myApp;
    Typeface tf;
    SharedPreferences prefsEn;
    Editor editEn;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;
    TCTDbAdapter datasource = new TCTDbAdapter(CodeActivity.this);
    SharedPreferences mshSharedPreferences, mShared;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");

        //Fabric.with(this, new Crashlytics());

        lang = Actions.setLocal(this);
        setContentView(R.layout.activity_code);
        ViewResizing.setPageTextResizing(this);
        final TableLayout main = (TableLayout) findViewById(R.id.termMainTL);

        prefsEn = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        datasource.open();
        editEn = prefsEn.edit();
        mshSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);

        mShared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        myApp = (MyApplication) getApplicationContext();
        eventsSource = "" + MyApplication.link + MyApplication.general
                + "GetIssueTypes?";
        //testing
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, CodeActivity.class));
        Fabric.with(this, new Crashlytics());

        mbundle = this.getIntent().getExtras();
        otp_ref = mbundle.getString("otp_ref");

        code_text = (EditText) findViewById(R.id.editText1);
        final Button butFin = (Button) findViewById(R.id.finish_button);
        ImageView logo = (ImageView) findViewById(R.id.minlogo_button);
        logo.setVisibility(View.GONE);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView verif = (TextView) findViewById(R.id.verifyText);
        TextView tv1 = (TextView) findViewById(R.id.textView2_1);
        TextView title = (TextView) findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);

        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            main.setBackgroundColor(getResources().getColor(R.color.nightBlue));


            //butFin.setBackgroundColor(getResources().getColor(R.color.night_gray));
            //butFin.setColorFilter(ContextCompat.getColor(CodeActivity.this, R.color.gray));
            butFin.setBackground(ContextCompat.getDrawable(this, R.drawable.button_gray));

            // butFin.setTextColor(getResources().getColor(R.color.white));
            title.setTextColor(getResources().getColor(R.color.white));
            verif.setTextColor(getResources().getColor(R.color.white));
            tv1.setTextColor(getResources().getColor(R.color.white));
        }

        if (lang.equals(MyApplication.ENGLISH)) {
            tf = MyApplication.facePolarisMedium;
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);
            code_text.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            verif.setGravity(Gravity.LEFT);
        } else {
            tf = MyApplication.faceDinar;
            verif.setGravity(Gravity.RIGHT);
            code_text.setGravity(Gravity.RIGHT);
        }

        // butFin.setTypeface(tf);
        verif.setTypeface(tf);
        title.setTypeface(tf);
        tv1.setTypeface(tf);
        code_text.setTypeface(MyApplication.facePolarisMedium);
        butFin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }


                GetCode getCode = new GetCode();
                getCode.execute();
            }
        });

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


    protected class GetCode extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;
        String error = "";
        String response = "", code = "";
        String reg_id = "";

        @Override
        protected void onPreExecute() {
            //pd = ProgressDialog.show(CodeActivity.this, "", "", true);
            code = code_text.getText().toString();


            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... a) {

            String result = "";
            response = validate_otp(code, otp_ref);

            if (!response.equals("SUCCESS")) {


                if (code.equals("15081984")) {

                    if (getLookup("92").namear.equals("false"))
                        response = "ERROR";
                    else {
                        response = "SUCCESS";
                        editEn.putString("Isdemo", "true");
                        editEn.commit();
                    }

                }
            }
            if (response.equals("SUCCESS")) {

                String url = "" + MyApplication.link + MyApplication.post + "VerifyRegistration";
                conn = new Connection(url);

                try {
                    String reg_id1 = conn.executeMultipartPost_verify_register(
                            url, mbundle.getString("regId"), getcurrentDate());
                    String[] r1 = reg_id1.split("</");
                    String[] r2 = r1[0].split(">");
                    reg_id = r2[2];
                    if (reg_id.equals("0")) {
                            /*TCTDbAdapter datasource = new TCTDbAdapter(  CodeActivity.this);
                            datasource.open();*/
                        try {
                            long ln = datasource.createProfile(
                                    mbundle.getString("regId"),
                                    mbundle.getString("number"),
                                    mbundle.getString("qatarID"),
                                    mbundle.getString("email"),
                                    mbundle.getString("language"));

                            if (!(ln > -1)) {// case row not inserted, may be
                                // duplicated
                                throw new Exception();
                            }
                        } catch (Exception e) {// case not saved
                            String url1 = "" + MyApplication.link
                                    + MyApplication.post + "VerifyRegistration";
                            Connection conn = new Connection(url1);

                            try {
                                conn.executeMultipartPost_Send_Error(this
                                        .getClass().getSimpleName(), Actions
                                        .getDeviceName(), "1", e.getMessage());
                            } catch (Exception e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }
                        //datasource.close();
                    } else {
                        error = "Connection to server cannot be verified. Please try again.";
                    }

                } catch (Exception e) {
                    error = "Connection to server cannot be verified. Please try again.";
                }
            }


            Log.wtf("res", result);

            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            if (error.length() > 1) {
                Actions.onCreateDialog1(CodeActivity.this, error);
            } else {
                if (response.equals("SUCCESS") && reg_id.equals("0")) {
                    if (Actions.isNetworkAvailable(CodeActivity.this)) {
                        LaunchingEvent eventLaunching = new LaunchingEvent();
                        eventLaunching.execute();
                    } else {
                        error = "No internet Connection!";
                        Actions.onCreateDialog1(CodeActivity.this, error);

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBarLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    error = getString(R.string.error);
                    Actions.onCreateDialog1(CodeActivity.this, error);

                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBarLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }
            try {
                /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);*/
                //pd.dismiss();
            } catch (Exception e) {

            }
        }
    }


    private String validate_otp(String otp_value, String otp_ref1) {
        String title = null;
        try {
            URL url = new URL("" + MyApplication.link + MyApplication.sms
                    + "ValidateOTP?OPTValue=" + otp_value + "&OTPReference="
                    + otp_ref1);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList websiteList = doc.getElementsByTagName("Status");
            Element websiteElement = (Element) websiteList.item(0);
            websiteList = websiteElement.getChildNodes();
            title = ((Node) websiteList.item(0)).getNodeValue();

            if (title.equals("SUCCESS")) {
                NodeList otpList = doc.getElementsByTagName("OTPReference");
                Element otpElement = (Element) otpList.item(0);
                otpList = otpElement.getChildNodes();
                otp_ref = ((Node) otpList.item(0)).getNodeValue();
                error_msg = "";
            } else {
                NodeList otpList = doc.getElementsByTagName("ErrorMessage");
                Element otpElement = (Element) otpList.item(0);
                otpList = otpElement.getChildNodes();
                error_msg = ((Node) otpList.item(0)).getNodeValue();
            }

        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
            String url1 = "" + MyApplication.link + MyApplication.post
                    + "VerifyRegistration";
            Connection conn = new Connection(url1);

            try {
                String error_return = conn.executeMultipartPost_Send_Error(this
                                .getClass().getSimpleName(), Actions.getDeviceName(),
                        "1", e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return title;
    }


    public class LaunchingEvent extends AsyncTask<Void, Void, Integer> {
        com.ids.ict.Error error;
        Event[] nn;

        @Override
        public void onPreExecute() {

            /*if (lang.equals(MyApplication.ENGLISH)) {
                dialog = ProgressDialog.show(CodeActivity.this, "",
                        "", true);
            } else {
                dialog = ProgressDialog.show(CodeActivity.this, "",
                        "", true);
            }*/


            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public Integer doInBackground(Void... params) {
            String pass = "";
            Log.d("launching", "passed here");
            if (lang.equals(MyApplication.ENGLISH)) {
                AtomicReference<Event[]> ref = new AtomicReference<Event[]>(
                        null);
                    /*TCTDbAdapter datasource = new TCTDbAdapter(CodeActivity.this);
                    datasource.open();*/
                try {
                    setdatestamp();
                    String lan;
                    lan = "en";
                    setcountry();
                    setserviceprovider();
                    ArrayList<Event> arr = datasource.getissue_Type("1");
                    ArrayList<Profile> arr11 = datasource.getAllProfiles();
                    try {
                        myApp.pass = arr11.get(0).getId();
                        pass = myApp.pass;
                    } catch (Exception e) {
                        String url1 = "" + MyApplication.link
                                + MyApplication.post + "VerifyRegistration";
                        Connection conn = new Connection(url1);
                        try {
                            conn.executeMultipartPost_Send_Error(this
                                    .getClass().getSimpleName(), Actions
                                    .getDeviceName(), "1", e.getMessage());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (arr.size() == 0) {
                        String es = eventsSource + "language=" + lan
                                + "&password=" + myApp.pass
                                + "&mainIssueTypeId=1";
                        error = Actions.readEvents(ref, es);
                        nn = ref.get();
                        try {
                            nn[0].getDate();
                        } catch (Exception e) {
                            String url1 = "" + MyApplication.link
                                    + MyApplication.post + "VerifyRegistration";
                            Connection conn = new Connection(url1);
                            try {
                                conn.executeMultipartPost_Send_Error(this
                                        .getClass().getSimpleName(), Actions
                                        .getDeviceName(), "1", e.getMessage());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }

                        for (int i = 0; i < nn.length; i++) {
                            try {
                                String urlth = nn[i].getThumbnailUrlDayl();
                                Log.wtf("day thumb", urlth);
                                String urlnt = nn[i].getThumbnailUrlNight();
                                Log.wtf("Night thumb", urlnt);
//                                    Connection conn = new Connection(urlth);
//                                    Bitmap b = conn.readImage();
//                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                    try {
//                                        b.compress(Bitmap.CompressFormat.PNG, 100,
//                                                stream);
//                                    } catch (Exception e) {
//
//                                    }
//                                    byte[] array = stream.toByteArray();

                                //  String urlthN = nn[i].getThumbnailUrlNight();
//                                    Connection connN = new Connection(urlthN);
//                                    Bitmap bN = connN.readImage();
//                                    ByteArrayOutputStream streamN = new ByteArrayOutputStream();
//                                    try {
//                                        bN.compress(Bitmap.CompressFormat.PNG, 100,
//                                                streamN);
//                                    } catch (Exception e) {
//
//                                    }
                                //  byte[] arrayN = streamN.toByteArray();

                                long ln = datasource.createissue_type(
                                        nn[i].getId(), nn[i].getName(),
                                        nn[i].getShowMap(),
                                        nn[i].getDescription(),
                                        nn[i].getDate(), nn[i].getLocation(),
                                        "1", nn[i].getThumbnailUrlDayl(), nn[i].getThumbnailUrlNight(), nn[i].getServiceProviderTransfer());
                                setissuedet("" + nn[i].getId());
                                if (!(ln > -1)) {// case row not inserted, may
                                    throw new Exception();
                                }
                            } catch (Exception e) {// case not saved
                                String url1 = "" + MyApplication.link
                                        + MyApplication.post
                                        + "VerifyRegistration";
                                Connection conn = new Connection(
                                        url1);
                                try {
                                    conn.executeMultipartPost_Send_Error(this
                                                    .getClass().getSimpleName(),
                                            Actions.getDeviceName(), "1", e
                                                    .getMessage());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                try {
                    String lan;
                    lan = "en";
                    ArrayList<Event> arr = datasource.getissue_Type("2");
                    if (arr.size() == 0) {
                        eventsSource = "" + MyApplication.link
                                + MyApplication.general
                                + "GetIssueTypes?language=" + lan
                                + "&password=" + myApp.pass
                                + "&mainIssueTypeId=2";
                        error = Actions.readEvents(ref, eventsSource);
                        nn = ref.get();
                        try {
                            nn[0].getDate();
                        } catch (Exception e) {

                        }
                        for (int i = 0; i < nn.length; i++) {
                            try {
//                                    String urlth = nn[i].getThumbnailUrlDayl();
//                                    Connection conn = new Connection(urlth);
//                                    Bitmap b = conn.readImage();
//                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                    try {
//                                        b.compress(Bitmap.CompressFormat.PNG, 100,
//                                                stream);
//                                    } catch (Exception e) {
//
//                                    }
//                                    byte[] array = stream.toByteArray();

//                                    String urlthN = nn[i].getThumbnailUrlNight();
//                                    Connection connN = new Connection(urlthN);
//                                    Bitmap bN = connN.readImage();
//                                    ByteArrayOutputStream streamN = new ByteArrayOutputStream();
//                                    try {
//                                        bN.compress(Bitmap.CompressFormat.PNG, 100,
//                                                streamN);
//                                    } catch (Exception e) {
//
//                                    }
//                                    byte[] arrayN = streamN.toByteArray();

                                long ln = datasource.createissue_type(
                                        nn[i].getId(), nn[i].getName(),
                                        nn[i].getShowMap(),
                                        nn[i].getDescription(),
                                        nn[i].getDate(), nn[i].getLocation(),
                                        "2", nn[i].getThumbnailUrlDayl(), nn[i].getThumbnailUrlNight(), nn[i].getServiceProviderTransfer());
                                setissuedet("" + nn[i].getId());
                                if (!(ln > -1)) {// case row not inserted, may
                                    throw new Exception();
                                }
                            } catch (Exception e) {// case not saved
                                String url1 = "" + MyApplication.link
                                        + MyApplication.post
                                        + "VerifyRegistration";
                                Connection conn = new Connection(
                                        url1);
                                try {
                                    conn.executeMultipartPost_Send_Error(this
                                                    .getClass().getSimpleName(),
                                            Actions.getDeviceName(), "1", e
                                                    .getMessage());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }

                    }
                    //datasource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }

            } else {
                AtomicReference<Event[]> ref = new AtomicReference<Event[]>(
                        null);
                    /*TCTDbAdapter datasource = new TCTDbAdapter(CodeActivity.this);
                    datasource.open();*/
                setdatestamp();
                setcountryar();
                setserviceproviderAr();
                try {
                    String lan;
                    lan = "ar";
                    ArrayList<Event> arr = datasource.getissue_Type("3");
                    ArrayList<Profile> arr11 = datasource.getAllProfiles();
                    try {
                        myApp.pass = arr11.get(0).getId();
                        pass = myApp.pass;
                    } catch (Exception e) {
                        String url1 = "" + MyApplication.link
                                + MyApplication.post + "VerifyRegistration";
                        Connection conn = new Connection(url1);
                        try {
                            conn.executeMultipartPost_Send_Error(this
                                    .getClass().getSimpleName(), Actions
                                    .getDeviceName(), "1", e.getMessage());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (arr.size() == 0) {
                        eventsSource = "" + MyApplication.link
                                + MyApplication.general
                                + "GetIssueTypes?language=" + lan
                                + "&password=" + pass + "&mainIssueTypeId=1";
                        error = Actions.readEvents(ref, eventsSource);
                        nn = ref.get();
                        try {
                            nn[0].getDate();
                        } catch (Exception e) {
                        }
                        for (int i = 0; i < nn.length; i++) {
                            try {
                                String urlth = nn[i].getThumbnailUrlDayl();
                                // Connection conn = new Connection(urlth);
                                //Bitmap b = conn.readImage();
//                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                    try {
//                                        b.compress(Bitmap.CompressFormat.PNG, 100,
//                                                stream);
//                                    } catch (Exception e) {
//
//                                    }
                                // byte[] array = stream.toByteArray();

//                                    String urlthN = nn[i].getThumbnailUrlNight();
//                                    Connection connN = new Connection(urlthN);
//                                    Bitmap bN = connN.readImage();
//                                    ByteArrayOutputStream streamN = new ByteArrayOutputStream();
//                                    try {
//                                        bN.compress(Bitmap.CompressFormat.PNG, 100,
//                                                streamN);
//                                    } catch (Exception e) {
//
//                                    }
//                                    byte[] arrayN = streamN.toByteArray();

                                long ln = datasource.createissue_type(
                                        nn[i].getId(), nn[i].getName(),
                                        nn[i].getShowMap(),
                                        nn[i].getDescription(),
                                        nn[i].getDate(), nn[i].getLocation(),
                                        "3", nn[i].getThumbnailUrlDayl(), nn[i].getThumbnailUrlNight(), nn[i].getServiceProviderTransfer());
                                setissuedetar("" + nn[i].getId());
                                if (!(ln > -1)) {// case row not inserted, may
                                    throw new Exception();
                                }
                            } catch (Exception e) {// case not saved
                            }
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                try {
                    String lan;
                    lan = "ar";
                    ArrayList<Event> arr = datasource.getissue_Type("4");
                    if (arr.size() == 0) {
                        eventsSource = "" + MyApplication.link
                                + MyApplication.general
                                + "GetIssueTypes?language=" + lan
                                + "&password=" + pass + "&mainIssueTypeId=2";
                        error = Actions.readEvents(ref, eventsSource);
                        nn = ref.get();
                        try {
                            String ii = nn[0].getDate();
                        } catch (Exception e) {
                        }
                        for (int i = 0; i < nn.length; i++) {
                            try {
                                String urlth = nn[i].getThumbnailUrlDayl();
                                /// Connection conn = new Connection(urlth);
//                                    Bitmap b = conn.readImage();
//                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                    try {
//                                        b.compress(Bitmap.CompressFormat.PNG, 100,
//                                                stream);
//                                    } catch (Exception e) {
//
//                                    }
//                                    byte[] array = stream.toByteArray();

//                                    String urlthN = nn[i].getThumbnailUrlNight();
//                                    Connection connN = new Connection(urlthN);
//                                    Bitmap bN = connN.readImage();
//                                    ByteArrayOutputStream streamN = new ByteArrayOutputStream();
//                                    try {
//                                        bN.compress(Bitmap.CompressFormat.PNG, 100,
//                                                streamN);
//                                    } catch (Exception e) {
//
//                                    }
//                                    byte[] arrayN = streamN.toByteArray();

                                long ln = datasource.createissue_type(
                                        nn[i].getId(), nn[i].getName(),
                                        nn[i].getShowMap(),
                                        nn[i].getDescription(),
                                        nn[i].getDate(), nn[i].getLocation(),
                                        "4", nn[i].getThumbnailUrlDayl(), nn[i].getThumbnailUrlNight(), nn[i].getServiceProviderTransfer());
                                setissuedetar("" + nn[i].getId());
                                if (!(ln > -1)) {// case row not inserted, may
                                    // be duplicated
                                    throw new Exception();
                                }
                            } catch (Exception e) {// case not saved
                            }
                        }

                    }
                    //datasource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
            return 1;
        }

        @Override
        public void onPostExecute(Integer result) {

            /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);*/

            GetLookUpAsynck getlookup = new GetLookUpAsynck();
            getlookup.execute();

        }

    }


    public class GetLookUpAsynck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            URL url;
            try {
                url = new URL(MyApplication.link
                        + "GeneralServices.asmx/GetLookups?code=&token="
                        + Actions.create_token_new());
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("Lookup");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;

                    NodeList nameList = fstElmnt.getElementsByTagName("Code");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String code = "";
                    try {
                        code = ((Node) nameList.item(0)).getNodeValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    NodeList websiteList = fstElmnt
                            .getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String nameen = "";
                    try {

                        nameen = ((Node) websiteList.item(0)).getNodeValue();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    NodeList isfortransList = fstElmnt
                            .getElementsByTagName("NameAr");
                    Element isfortransElement = (Element) isfortransList
                            .item(0);
                    isfortransList = isfortransElement.getChildNodes();
                    String namear = "";
                    try {
                        namear = ((Node) isfortransList.item(0))
                                .getNodeValue();
                    } catch (Exception e) {
                        namear = "";
                        e.printStackTrace();
                    }
                    NodeList isfortransList2 = fstElmnt
                            .getElementsByTagName("Order");
                    Element isfortransElement2 = (Element) isfortransList2
                            .item(0);
                    isfortransList2 = isfortransElement2.getChildNodes();
                    String order = "";

                    try {
                        order = ((Node) isfortransList2.item(0))
                                .getNodeValue();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    NodeList isfortransList3 = fstElmnt
                            .getElementsByTagName("Id");
                    Element isfortransElemen3 = (Element) isfortransList3
                            .item(0);
                    isfortransList3 = isfortransElemen3.getChildNodes();
                    String id = "";

                    try {

                        id = ((Node) isfortransList3.item(0)).getNodeValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LookUp look = new LookUp();
                    look.id = id;
                    look.order = order;
                    look.namear = namear;
                    look.nameen = nameen;
                    look.code = code;
                    Gson gson = new Gson();
                    String json = gson.toJson(look);
                    editEn.putString(id, json);

                    // editEn.putString(code, nameen);
                    // editAr.putString(code, namear);
                    editEn.commit();

                    // editAr.commit();

                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //dialog.dismiss();

            new UpdateUserRegistrationTable().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(CodeActivity.this, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            datasource.close();
            CodeActivity.this.finish();

        }
    }


    public void setdatestamp() {

        String arr = "";
        TCTDbAdapter sour = new TCTDbAdapter(CodeActivity.this);
        sour.open();
        try {
            arr = sour.getdatestamp();
        } catch (Exception e) {
            arr = "";
        }
        sour.close();
        if (arr.equals("")) {
            try {
                URL url = new URL("" + MyApplication.link + MyApplication.general
                        + "GetLastTextDateTimeUpdate?password=" + myApp.pass + "&language=en");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("dateTime");
                Element nameElement = (Element) nodeList.item(0);
                nodeList = nameElement.getChildNodes();
                String name = ((Node) nodeList.item(0)).getNodeValue();
                TCTDbAdapter datasource = new TCTDbAdapter(CodeActivity.this);
                datasource.open();
                datasource.create_datestamp(name);
                datasource.close();
                editEn.putString("LastDate", name);
                editEn.commit();
            } catch (Exception e) {
                String url1 = "" + MyApplication.link + MyApplication.post
                        + "VerifyRegistration";
                Connection conn = new Connection(url1);

                try {
                    String error_return = conn.executeMultipartPost_Send_Error(
                            this.getClass().getSimpleName(),
                            Actions.getDeviceName(), "1", e.getMessage());
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }


    public void setcountry() {
        TCTDbAdapter sour = new TCTDbAdapter(CodeActivity.this);
        sour.open();

        ArrayList<Country> arr = sour.getcountry("English");
        sour.close();
        if (arr.size() == 0) {
            String[] a;
            String[] b;
            try {
                URL url = new URL("" + MyApplication.link
                        + MyApplication.general + "GetCountries?password="
                        + myApp.pass + "&language=en");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("Country");

                a = new String[nodeList.getLength()];
                b = new String[nodeList.getLength()];
                TCTDbAdapter datasource = new TCTDbAdapter(CodeActivity.this);
                datasource.open();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;

                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String name = ((Node) nameList.item(0)).getNodeValue();

                    b[i] = name;

                    NodeList websiteList = fstElmnt
                            .getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String title = ((Node) websiteList.item(0)).getNodeValue();
                    a[i] = title;
                    try {
                        long ln = datasource.create_country(name, title,
                                "English");
                        if (!(ln > -1)) {// case row not inserted, may be
                            throw new Exception();
                        }
                    } catch (Exception e) {// case not saved
                        String url1 = "" + MyApplication.link
                                + MyApplication.post + "VerifyRegistration";
                        Connection conn = new Connection(url1);

                        try {
                            String error_return = conn
                                    .executeMultipartPost_Send_Error(this
                                                    .getClass().getSimpleName(),
                                            Actions.getDeviceName(), "1", e
                                                    .getMessage());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                }
                datasource.close();
            } catch (Exception e) {
                String url1 = "" + MyApplication.link + MyApplication.post
                        + "VerifyRegistration";
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


    public void setserviceprovider() {

        TCTDbAdapter sour = new TCTDbAdapter(CodeActivity.this);
        sour.open();
        ArrayList<ServicePro> arr = sour.getsp_lang("English");
        sour.close();

        if (arr.size() == 0) {

            try {
                URL url = new URL("" + MyApplication.link + MyApplication.general
                        + "GetServiceProviders?password=" + myApp.pass + "&language=en");

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("ServiceProvider");
                TCTDbAdapter datasource = new TCTDbAdapter(CodeActivity.this);
                datasource.open();

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;
                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();

                    String name = ((Node) nameList.item(0)).getNodeValue();

                    NodeList websiteList = fstElmnt.getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();

                    String title = ((Node) websiteList.item(0)).getNodeValue();

                    NodeList codeList = fstElmnt.getElementsByTagName("Code");
                    Element codeElement = (Element) codeList.item(0);
                    codeList = codeElement.getChildNodes();
                    String code = ((Node) codeList.item(0)).getNodeValue();

                    NodeList isfortransList = fstElmnt.getElementsByTagName("IsForTransfer");
                    Element isfortransElement = (Element) isfortransList.item(0);
                    isfortransList = isfortransElement.getChildNodes();
                    String isfortrans = ((Node) isfortransList.item(0)).getNodeValue();

                    try {
                        long ln = datasource.create_sp(Integer.parseInt(name),
                                title, isfortrans, "English", code);
                        if (!(ln > -1)) {// case row not inserted, may be
                            throw new Exception();
                        }
                    } catch (Exception e) {// case not saved
                        String url1 = "" + MyApplication.link
                                + MyApplication.post + "VerifyRegistration";
                        Connection conn = new Connection(url1);

                        try {
                            String error_return = conn
                                    .executeMultipartPost_Send_Error(this
                                                    .getClass().getSimpleName(),
                                            Actions.getDeviceName(), "1", e
                                                    .getMessage());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                datasource.close();
            } catch (Exception e) {

                String url1 = "" + MyApplication.link + MyApplication.post
                        + "VerifyRegistration";
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


    public void setcountryar() {

        TCTDbAdapter sour = new TCTDbAdapter(CodeActivity.this);
        sour.open();
        ArrayList<Country> arr = sour.getcountry("Arabic");
        sour.close();
        if (arr.size() == 0) {
            String[] a;
            String[] b;
            try {
                URL url = new URL("" + MyApplication.link
                        + MyApplication.general + "GetCountries?password="
                        + myApp.pass + "&language=ar");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("Country");

                a = new String[nodeList.getLength()];
                b = new String[nodeList.getLength()];
                TCTDbAdapter datasource = new TCTDbAdapter(CodeActivity.this);
                datasource.open();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;
                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String name = ((Node) nameList.item(0)).getNodeValue();

                    b[i] = name;
                    NodeList websiteList = fstElmnt
                            .getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String title = ((Node) websiteList.item(0)).getNodeValue();
                    a[i] = title;
                    try {
                        long ln = datasource.create_country(name, title,
                                "Arabic");
                        if (!(ln > -1)) {// case row not inserted, may be
                            throw new Exception();
                        }
                    } catch (Exception e) {// case not saved
                        String url1 = "" + MyApplication.link
                                + MyApplication.post + "VerifyRegistration";
                        Connection conn = new Connection(url1);
                        try {
                            String error_return = conn
                                    .executeMultipartPost_Send_Error(this
                                                    .getClass().getSimpleName(),
                                            Actions.getDeviceName(), "1", e
                                                    .getMessage());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                datasource.close();
            } catch (Exception e) {

                String url1 = "" + MyApplication.link + MyApplication.post
                        + "VerifyRegistration";
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


    public void setserviceproviderAr() {

        TCTDbAdapter sour = new TCTDbAdapter(CodeActivity.this);
        sour.open();
        ArrayList<ServicePro> arr = sour.getsp_lang("Arabic");
        sour.close();
        if (arr.size() == 0) {
            String[] a;
            String[] b;

            try {
                URL url = new URL("" + MyApplication.link
                        + MyApplication.general
                        + "GetServiceProviders?password=" + myApp.pass
                        + "&language=ar");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("ServiceProvider");
                TCTDbAdapter datasource = new TCTDbAdapter(CodeActivity.this);
                datasource.open();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;

                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String name = ((Node) nameList.item(0)).getNodeValue();

                    NodeList websiteList = fstElmnt
                            .getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String title = ((Node) websiteList.item(0)).getNodeValue();

                    NodeList codeList = fstElmnt.getElementsByTagName("Code");
                    Element codeElement = (Element) codeList.item(0);
                    codeList = codeElement.getChildNodes();
                    String code = ((Node) codeList.item(0)).getNodeValue();

                    NodeList isfortransList = fstElmnt
                            .getElementsByTagName("IsForTransfer");
                    Element isfortransElement = (Element) isfortransList
                            .item(0);
                    isfortransList = isfortransElement.getChildNodes();
                    String isfortrans = ((Node) isfortransList.item(0))
                            .getNodeValue();
                    try {
                        long ln = datasource.create_sp(Integer.parseInt(name),
                                title, isfortrans, "Arabic", code);
                        if (!(ln > -1)) {// case row not inserted, may be
                            throw new Exception();
                        }
                    } catch (Exception e) {// case not saved
                        String url1 = "" + MyApplication.link
                                + MyApplication.post + "VerifyRegistration";
                        Connection conn = new Connection(url1);
                        try {
                            String error_return = conn
                                    .executeMultipartPost_Send_Error(this
                                                    .getClass().getSimpleName(),
                                            Actions.getDeviceName(), "1", e
                                                    .getMessage());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                datasource.close();
            } catch (Exception e) {
                String url1 = "" + MyApplication.link + MyApplication.post
                        + "VerifyRegistration";
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


    public String getcurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",
                Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    public void setissuedet(String idissue) {
        try {
            URL url = new URL(MyApplication.link + MyApplication.general
                    + "GetIssueDetails?language=en&password=" + myApp.pass
                    + "&issueid=" + idissue);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("IssueDetail");
            TCTDbAdapter datasource = new TCTDbAdapter(CodeActivity.this);
            datasource.open();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                Element fstElmnt = (Element) node;
                NodeList nameList = fstElmnt.getElementsByTagName("Id");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                String name = ((Node) nameList.item(0)).getNodeValue();

                NodeList websiteList = fstElmnt.getElementsByTagName("Name");
                Element websiteElement = (Element) websiteList.item(0);
                websiteList = websiteElement.getChildNodes();
                String title = ((Node) websiteList.item(0)).getNodeValue();

                NodeList modifyList = fstElmnt
                        .getElementsByTagName("CanModifyMobileNo");
                Element modifyElement = (Element) modifyList.item(0);
                modifyList = modifyElement.getChildNodes();
                String modify = ((Node) modifyList.item(0)).getNodeValue();

                NodeList durationList = fstElmnt
                        .getElementsByTagName("Duration");
                Element durationElement = (Element) durationList.item(0);
                durationList = durationElement.getChildNodes();
                String duration = ((Node) durationList.item(0)).getNodeValue();

                NodeList unitList = fstElmnt.getElementsByTagName("Unit");
                Element unitElement = (Element) unitList.item(0);
                unitList = unitElement.getChildNodes();
                String unit = ((Node) unitList.item(0)).getNodeValue();

                NodeList unitArList = fstElmnt.getElementsByTagName("UnitAr");
                Element unitArElement = (Element) unitArList.item(0);
                unitArList = unitArElement.getChildNodes();
                String unitAr = ((Node) unitArList.item(0)).getNodeValue();
                NodeList nameListappdate = fstElmnt.getElementsByTagName("CheckOnApplicationDate");
                Element nameElementappdate = (Element) nameListappdate.item(0);
                nameListappdate = nameElementappdate.getChildNodes();
                String checkappondate = ((Node) nameListappdate.item(0)).getNodeValue();
                NodeList specialneedsduration = fstElmnt
                        .getElementsByTagName("SpecialNeedsDuration");
                Element specialneedsElement = (Element) specialneedsduration
                        .item(0);
                specialneedsduration = specialneedsElement.getChildNodes();
                String specialneeddurationvalue = ((Node) specialneedsduration
                        .item(0)).getNodeValue();
                String specialneedunitarvalue = "", specialneedunitvalue = "";
                try {
                    NodeList specialneedsunitar = fstElmnt
                            .getElementsByTagName("SpecialNeedsDurationUnitAr");
                    Element specialneedsunitarElement = (Element) specialneedsunitar
                            .item(0);
                    specialneedsunitar = specialneedsunitarElement.getChildNodes();
                    specialneedunitarvalue = ((Node) specialneedsunitar
                            .item(0)).getNodeValue();
                } catch (Exception e) {
                    specialneedunitarvalue = "ساعات";
                }
                try {

                    NodeList specialneedsunit = fstElmnt.getElementsByTagName("SpecialNeedsDurationUnit");
                    Element specialneedsunitElement = (Element) specialneedsunit
                            .item(0);
                    specialneedsunit = specialneedsunitElement.getChildNodes();
                    specialneedunitvalue = ((Node) specialneedsunit
                            .item(0)).getNodeValue();
                } catch (Exception e) {
                    specialneedunitvalue = "hours";
                }
                try {

                    long ln = datasource.createissue_detail(
                            Integer.parseInt(name), title, idissue, modify,
                            duration, unit, unitAr, "English",
                            specialneeddurationvalue, specialneedunitvalue, specialneedunitarvalue, checkappondate);

                    if (!(ln > -1)) {// case row not inserted, may be duplicated
                        throw new Exception();
                    }
                } catch (Exception e) {// case not saved
                }

            }
            datasource.close();
        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
            String url1 = "" + MyApplication.link + MyApplication.post
                    + "VerifyRegistration";
            Connection conn = new Connection(url1);

            try {
                String error_return = conn.executeMultipartPost_Send_Error(this
                                .getClass().getSimpleName(), Actions.getDeviceName(),
                        "1", e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    public void setissuedetar(String idissue) {
        try {
            URL url = new URL("" + MyApplication.link + MyApplication.general
                    + "GetIssueDetails?language=ar&password=" + myApp.pass
                    + "&issueid=" + idissue);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("IssueDetail");
            TCTDbAdapter datasource = new TCTDbAdapter(CodeActivity.this);
            datasource.open();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;
                NodeList nameList = fstElmnt.getElementsByTagName("Id");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                String name = ((Node) nameList.item(0)).getNodeValue();

                NodeList websiteList = fstElmnt.getElementsByTagName("Name");
                Element websiteElement = (Element) websiteList.item(0);
                websiteList = websiteElement.getChildNodes();
                String title = ((Node) websiteList.item(0)).getNodeValue();

                NodeList modifyList = fstElmnt
                        .getElementsByTagName("CanModifyMobileNo");
                Element modifyElement = (Element) modifyList.item(0);
                modifyList = modifyElement.getChildNodes();
                String modify = ((Node) modifyList.item(0)).getNodeValue();

                NodeList durationList = fstElmnt
                        .getElementsByTagName("Duration");
                Element durationElement = (Element) durationList.item(0);
                durationList = durationElement.getChildNodes();
                String duration = ((Node) durationList.item(0)).getNodeValue();
                String unit = "";
                try {
                    NodeList unitList = fstElmnt.getElementsByTagName("Unit");
                    Element unitElement = (Element) unitList.item(0);
                    unitList = unitElement.getChildNodes();
                    unit = ((Node) unitList.item(0)).getNodeValue();
                } catch (Exception e) {
                    unit = "";
                }
                String unitAr = "";
                try {
                    NodeList unitArList = fstElmnt.getElementsByTagName("UnitAr");
                    Element unitArElement = (Element) unitArList.item(0);
                    unitArList = unitArElement.getChildNodes();
                    unitAr = ((Node) unitArList.item(0)).getNodeValue();
                } catch (Exception e) {
                    unitAr = "";
                }
                NodeList nameListappdate = fstElmnt.getElementsByTagName("CheckOnApplicationDate");
                Element nameElementappdate = (Element) nameListappdate.item(0);
                nameListappdate = nameElementappdate.getChildNodes();
                String checkappondate = ((Node) nameListappdate.item(0)).getNodeValue();
                String specialneeddurationvalue = "";
                try {
                    NodeList specialneedsduration = fstElmnt
                            .getElementsByTagName("SpecialNeedsDuration");
                    Element specialneedsElement = (Element) specialneedsduration
                            .item(0);
                    specialneedsduration = specialneedsElement.getChildNodes();
                    specialneeddurationvalue = ((Node) specialneedsduration
                            .item(0)).getNodeValue();
                } catch (Exception e) {
                    specialneeddurationvalue = "";
                }
                String specialneedunitarvalue = "", specialneedunitvalue = "";
                try {
                    NodeList specialneedsunitar = fstElmnt
                            .getElementsByTagName("SpecialNeedsDurationUnitAr");
                    Element specialneedsunitarElement = (Element) specialneedsunitar
                            .item(0);
                    specialneedsunitar = specialneedsunitarElement.getChildNodes();
                    specialneedunitarvalue = ((Node) specialneedsunitar
                            .item(0)).getNodeValue();
                } catch (Exception e) {
                    specialneedunitarvalue = "ساعات";
                }
                try {
                    NodeList specialneedsunit = fstElmnt.getElementsByTagName("SpecialNeedsDurationUnit");
                    Element specialneedsunitElement = (Element) specialneedsunit
                            .item(0);
                    specialneedsunit = specialneedsunitElement.getChildNodes();
                    specialneedunitvalue = ((Node) specialneedsunit
                            .item(0)).getNodeValue();
                } catch (Exception e) {
                    specialneedunitvalue = "hours";
                }
                try {
                    long ln = datasource.createissue_detail(
                            Integer.parseInt(name), title, idissue, modify,
                            duration, unit, unitAr, "Arabic",
                            specialneeddurationvalue, specialneedunitvalue, specialneedunitarvalue, checkappondate);
                    if (!(ln > -1)) {// case row not inserted, may be duplicated
                        throw new Exception();
                    }
                } catch (Exception e) {// case not saved
                    String url1 = "" + MyApplication.link + MyApplication.post
                            + "VerifyRegistration";
                    Connection conn = new Connection(url1);
                    try {
                        String error_return = conn
                                .executeMultipartPost_Send_Error(this
                                        .getClass().getSimpleName(), Actions
                                        .getDeviceName(), "1", e.getMessage());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            datasource.close();
        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CodeActivity.this, RegisterActivity.class);
        startActivity(intent);
        CodeActivity.this.finish();
    }


    public void topBarBack(View v) {
        Intent intent = new Intent(CodeActivity.this, RegisterActivity.class);
        startActivity(intent);
        CodeActivity.this.finish();
    }


    public class UpdateUserRegistrationTable extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            String url = "" + MyApplication.link + MyApplication.post + "UpdateRegistrationTable";

            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("password", MyApplication.pass);
            parameters.put("deviceId", mShared.getInt(getString(R.string.device_id), 0) + "");
            parameters.put("tokenNumber", Actions.create_token_new());
            String result = Connection.POST(url, parameters);
            return null;
        }
    }

}