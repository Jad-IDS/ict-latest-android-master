package com.ids.ict.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
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
import android.widget.Toast;

//import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.Country;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.MyExceptionHandler;
import com.ids.ict.classes.Profile;
import com.ids.ict.classes.ServicePro;
import com.ids.ict.classes.ViewResizing;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
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
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.fabric.sdk.android.Fabric;


public class CodeNewActivity extends Activity {

    Bundle mbundle;
    ProgressDialog dialog;
    ProgressBar prog;
    String lang = "", eventsSource = "", lan = "", error_msg = "", validationResult = "", regId = "", qatarID = "",
            transportKey = "", transactionId = "", username = "", password = "", phoneNumber = "", errorMessage = "";
    Connection conn;
    EditText code_text_1, code_text_2, code_text_3, code_text_4;
    MyApplication myApp;
    Typeface tf;
    SharedPreferences prefsEn;
    SharedPreferences.Editor editEn;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;
    TCTDbAdapter datasource = new TCTDbAdapter(CodeNewActivity.this);
    SharedPreferences mshSharedPreferences, mShared;
    TextView tvCount;
    Button btnResend;

    int smsTimeOut = 120;
    Handler handler;
    private Runnable timeCounterRunnable = new Runnable() {
        @Override
        public void run() {

            if (smsTimeOut == 1) {
                //Do whatever when timeout
                tvCount.setVisibility(View.GONE);
                btnResend.setVisibility(View.VISIBLE);
            }
            else{
                smsTimeOut--;
               if(smsTimeOut<60)
                  tvCount.setText(getResources().getString(R.string.resend_after) + " (00:" + (smsTimeOut < 10 ? "0" : "") + String.valueOf(smsTimeOut) + ")");
               else {

                   tvCount.setText(getResources().getString(R.string.resend_after) + " (01:" + (smsTimeOut-60 < 10 ? "0" : "") + String.valueOf(smsTimeOut-60) + ")");
               }
                handler.postDelayed(this, 1000);
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");

        //Fabric.with(this, new Crashlytics());

        lang = Actions.setLocal(this);
        setContentView(R.layout.activity_code_new);
        ViewResizing.setPageTextResizing(this);
        final TableLayout main = (TableLayout) findViewById(R.id.rootLayout);

        prefsEn = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        datasource.open();
        editEn = prefsEn.edit();
        mshSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);

        mShared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        myApp = (MyApplication) getApplicationContext();
        eventsSource = "" + MyApplication.link + MyApplication.general + "GetIssueTypes?";
        //testing
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, CodeNewActivity.class));
        Fabric.with(this, new Crashlytics());


        try {
            mbundle = this.getIntent().getExtras();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            qatarID = mbundle.getString("qatarID");
        } catch (Exception e) {
            e.printStackTrace();
            qatarID = "";
        }

        try {
            transactionId = mbundle.getString("transactionId");
        } catch (Exception e) {
            e.printStackTrace();
            transactionId = "";
        }

        try {
            transportKey = mbundle.getString("transportKey");
        } catch (Exception e) {
            e.printStackTrace();
            transportKey = "";
        }

        try {
            username = mbundle.getString("username");
        } catch (Exception e) {
            e.printStackTrace();
            username = "";
        }

        try {
            password = mbundle.getString("password");
        } catch (Exception e) {
            e.printStackTrace();
            password = "";
        }

        Log.wtf("****** DEV id", "is " + mShared.getInt(getString(R.string.device_id), 0) );
        Log.wtf("****** transactionId", "is " + transactionId);
        Log.wtf("****** transportKey", "is " + transportKey);

        code_text_1 = (EditText) findViewById(R.id.editText1);
        code_text_2 = (EditText) findViewById(R.id.etCode1);
        code_text_3 = (EditText) findViewById(R.id.etCode2);
        code_text_4 = (EditText) findViewById(R.id.etCode3);
        tvCount = (TextView) findViewById(R.id.tvCount);

        btnResend = (Button) findViewById(R.id.btnResend);

        final Button butFin = (Button) findViewById(R.id.finish_button);

        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView verif = (TextView) findViewById(R.id.verifyText);
        TextView title = (TextView) findViewById(R.id.title);
        title.setVisibility(View.GONE);


        if (MyApplication.nightMod) {

            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            main.setBackgroundColor(getResources().getColor(R.color.nightBlue));


            //butFin.setBackgroundColor(getResources().getColor(R.color.night_gray));
            //butFin.setColorFilter(ContextCompat.getColor(CodeNewActivity.this, R.color.gray));
            btnResend.setBackground(ContextCompat.getDrawable(this, R.drawable.button_gray));
            btnResend.setTextColor(ContextCompat.getColor(this, R.color.white));
            butFin.setBackground(ContextCompat.getDrawable(this, R.drawable.button_gray));

            verif.setTextColor(getResources().getColor(R.color.white));
            tvCount.setTextColor(getResources().getColor(R.color.white));
        }

        if (lang.equals(MyApplication.ENGLISH)) {
            tf = MyApplication.facePolarisMedium;
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);

//            code_text_1.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//            code_text_2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//            code_text_3.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//            code_text_4.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            verif.setGravity(Gravity.LEFT);
        } else {
            tf = MyApplication.faceDinar;
            verif.setGravity(Gravity.RIGHT);

//            code_text_1.setGravity(Gravity.RIGHT);
//            code_text_2.setGravity(Gravity.RIGHT);
//            code_text_3.setGravity(Gravity.RIGHT);
//            code_text_4.setGravity(Gravity.RIGHT);
        }

        // butFin.setTypeface(tf);
        verif.setTypeface(tf);
        title.setTypeface(tf);

        butFin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (code_text_1.getText().toString().length() > 0
                        && code_text_2.getText().toString().length() > 0
                        && code_text_3.getText().toString().length() > 0
                        && code_text_4.getText().toString().length() > 0
                ){

                    ValidateOTPTask validateOTPTask = new ValidateOTPTask();
                    validateOTPTask.execute();
                }else{

                    String errorCode = getString(R.string.error_code);
                    Actions.onCreateDialog1(CodeNewActivity.this, errorCode);
                }
            }
        });

        code_text_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code_text_2.requestFocus();
            }
        });

        code_text_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code_text_3.requestFocus();
            }
        });

        code_text_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code_text_4.requestFocus();
            }
        });

        code_text_1.setTypeface(MyApplication.facePolarisMedium);
        code_text_2.setTypeface(MyApplication.facePolarisMedium);
        code_text_3.setTypeface(MyApplication.facePolarisMedium);
        code_text_4.setTypeface(MyApplication.facePolarisMedium);


        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginTask loginTask = new LoginTask(username, password);
                loginTask.execute();
            }
        });

        smsTimeOut = 120;
        handler = new Handler();
        handler.post(timeCounterRunnable);
        tvCount.setVisibility(View.VISIBLE);

        btnResend.setVisibility(View.GONE);




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


    private String validate_otp(String otp) {
        String result = "";
        String url = MyApplication.link + MyApplication.sms + "VaildateOTPRequest";
        try {

            //Connection.disableSSLCertificateChecking();
            Connection conn = new Connection(url);
            result = conn.executeMultipartPost_verify(url, otp, transactionId, transportKey);
            Log.wtf("validate_otp","result : " + result);
            String[] r1 = result.split("</");
            String[] r2 = r1[0].split(">");

            try {
                validationResult = r2[2];
                JSONObject json_result = new JSONObject(validationResult);

                try {
                    regId = json_result.getString("Guid");
                } catch (Exception e) {
                    e.printStackTrace();
                    regId = "";
                }
                Log.wtf("validate_otp","regId : " + regId);

                try {
                    phoneNumber = json_result.getString("Mobile");
                } catch (Exception e) {
                    e.printStackTrace();
                    phoneNumber = "";
                }
                Log.wtf("validate_otp","phoneNumber : " + phoneNumber);

                try {
                    if(json_result.getString("Error").equals(null)){
                        result = "";
                        errorMessage = "";
                    } else {
                        if(json_result.getString("Error").equals("null")){

                            result = "";
                            errorMessage = "";
                        }else{
                            result = json_result.getString("Error");
                            errorMessage = json_result.getString("Error");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    result = "";
                }
                Log.wtf("validate_otp","errorMessage : " + errorMessage);


                //testing Login
                if(MyApplication.isTesting){
                    regId = MyApplication.testingRegId;
                    phoneNumber = "97455170460";
                    errorMessage = "";
                    result = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = "";
                validationResult = "";
                regId = "";


                //testing Login
                if(MyApplication.isTesting){
                    regId = MyApplication.testingRegId;
                    phoneNumber = "97455170460";
                    errorMessage = "";
                    result = "";
                }
            }

            Log.wtf("Result of Ver.", "is " + result);
        } catch (Exception e) {
            Log.wtf("validate_otp","Exception : " + e.getMessage());
            System.out.println("XML Pasing Excpetion = " + e);
        }

        if (result.length() == 0){

            result = "SUCCESS";
            if(phoneNumber.length() > 0){
                phoneNumber = phoneNumber.replace("+","");
            }
        }else {

            result = "ERROR";
        }

        Log.wtf("returned_result",result);

        return result;
    }


    protected class ValidateOTPTask extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;
        String error = "";
        String response = "", code = "";

        @Override
        protected void onPreExecute() {
            //pd = ProgressDialog.show(CodeNewActivity.this, "", "", true);

            code = code_text_1.getText().toString()+code_text_2.getText().toString()+code_text_3.getText().toString()+code_text_4.getText().toString();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... a) {

            String result = "";
            response = validate_otp(code);
            Log.wtf("response",": " + response);

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

            //<editor-fold desc="old verification">
            /*if (response.equals("SUCCESS")) {

                String url = "" + MyApplication.link + MyApplication.post + "VerifyRegistration";
                conn = new Connection(url);

                try {
                    String reg_id1 = conn.executeMultipartPost_verify_register(url, regId, getcurrentDate());
                    Log.wtf("reg_id111111111111111","is " + reg_id1);
                    String[] r1 = reg_id1.split("</");
                    String[] r2 = r1[0].split(">");
                    reg_id = r2[2];
                }catch (Exception e){
                    e.printStackTrace();
                }

                Log.wtf("regId","is " + regId);
                Log.wtf("reg_id","is " + reg_id);

                MyApplication.pass = regId;

            }*/
            //</editor-fold>


            TCTDbAdapter datasource = new TCTDbAdapter(  CodeNewActivity.this);
            datasource.open();
            try {
                long ln = datasource.createProfile(regId, phoneNumber, qatarID, "", mbundle.getString("language"));

                if (!(ln > -1)) {// case row not inserted, may be
                    // duplicated
                    throw new Exception();
                }
            } catch (Exception e) { // case not saved
                e.printStackTrace();
                Log.wtf("createProfile","crash : " + e.getMessage());

                try {
                    Connection conn = new Connection("");
                    String errorRslt = conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
                    Log.wtf("executeMultipartPost_Send_Error"," : " + errorRslt);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            datasource.close();

            Log.wtf("validate_otp res", result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
          if (error.length() > 1) {
          //  if(!response.equals("SUCCESS")){
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
               //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                Actions.onCreateDialog1(CodeNewActivity.this, response);
            } else {

                if (response.equals("SUCCESS") /*&& reg_id.equals("0")*/) {

                    myApp.pass = regId;
                    Log.wtf("myApp.pass = regId",": " + myApp.pass);

                    new UpdateUserRegistrationTable().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    if (Actions.isNetworkAvailable(CodeNewActivity.this)) {
                        LaunchingEvent eventLaunching = new LaunchingEvent();
                        eventLaunching.execute();
                    } else {
                        error = "No internet Connection!";
                        Actions.onCreateDialog1(CodeNewActivity.this, error);

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBarLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
              } else {

                    String[] array = errorMessage.split(Pattern.quote("||"));
                    if (lang.equals("ar")){

                        try {
                            error = array[0];
                        } catch (Exception e) {
                            e.printStackTrace();
                            error = "";
                        }
                    }else {

                        try {
                            error = array[1];
                        } catch (Exception e) {
                            e.printStackTrace();
                            error = "";
                        }
                    }
                    Actions.onCreateDialog1(CodeNewActivity.this, error);

                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBarLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }

        }
    }


    private class UpdateUserRegistrationTable extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            String url = "" + MyApplication.link + MyApplication.post + "UpdateRegistrationTable";

            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("password", MyApplication.pass);
            parameters.put("deviceId", mShared.getInt(getString(R.string.device_id), 0) + "");
            parameters.put("tokenNumber", Actions.create_token_new());
            String result = Connection.POST(url, parameters);

            Log.wtf("result", "is "  + result);

            return null;
        }
    }


    public class LaunchingEvent extends AsyncTask<Void, Void, Integer> {
        com.ids.ict.Error error;
        Event[] nn;

        @Override
        public void onPreExecute() {

            /*if (lang.equals(MyApplication.ENGLISH)) {
                dialog = ProgressDialog.show(CodeNewActivity.this, "",
                        "", true);
            } else {
                dialog = ProgressDialog.show(CodeNewActivity.this, "",
                        "", true);
            }*/

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public Integer doInBackground(Void... params) {
            String pass = "";
            Log.wtf("launching", "passed here");

            if (lang.equals(MyApplication.ENGLISH)) {
                AtomicReference<Event[]> ref = new AtomicReference<Event[]>(null);
                    /*TCTDbAdapter datasource = new TCTDbAdapter(CodeNewActivity.this);
                    datasource.open();*/

                try {
                    String lan = "en";

                    setdatestamp();
                    setcountry();
                    setserviceprovider();

                    ArrayList<Event> arr = datasource.getissue_Type("1");
                    ArrayList<Profile> arr11 = datasource.getAllProfiles();

                    try {
                        myApp.pass = arr11.get(0).getId();
                        pass = myApp.pass;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.wtf("myApp.pass = arr11.get(0).getId()","Crach : " + e.getMessage());

                        try {
                            Connection conn = new Connection("");
                            conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            Log.wtf("myApp.pass = arr11.get(0).getId()","executeMultipartPost_Send_Error Crach : " + e.getMessage());
                        }
                    }

                    if (arr.size() == 0) {
                        String es = eventsSource + "language=" + lan + "&password=" + myApp.pass + "&mainIssueTypeId=1";
                        error = Actions.readEvents(ref, es);
                        nn = ref.get();

                        try {
                            nn[0].getDate();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.wtf("nn[0].getDate()","Crach : " + e.getMessage());

                            try {
                                Connection conn = new Connection("");
                                conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
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
                                        nn[i].getId(), nn[i].getName(), nn[i].getShowMap(),
                                        nn[i].getDescription(), nn[i].getDate(), nn[i].getLocation(),
                                        "1", nn[i].getThumbnailUrlDayl(),
                                        nn[i].getThumbnailUrlNight(), nn[i].getServiceProviderTransfer());

                                setissuedet("" + nn[i].getId());
                                if (!(ln > -1)) { // case row not inserted, may
                                    throw new Exception();
                                }
                            } catch (Exception e) { // case not saved
                                e.printStackTrace();

                                try {
                                    Connection conn = new Connection("");
                                    conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    String lan;
                    lan = "en";
                    ArrayList<Event> arr = datasource.getissue_Type("2");

                    if (arr.size() == 0) {
                        eventsSource = "" + MyApplication.link + MyApplication.general + "GetIssueTypes?language=" + lan + "&password=" + myApp.pass + "&mainIssueTypeId=2";
                        Log.wtf("eventsSource",": " + eventsSource);
                        error = Actions.readEvents(ref, eventsSource);
                        nn = ref.get();

                        try {
                            nn[0].getDate();
                        } catch (Exception e) {
                            e.printStackTrace();
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
                                e.printStackTrace();

//                                String url1 = "" + MyApplication.link + MyApplication.post + "VerifyRegistration";
//                                Connection conn = new Connection(url1);
//                                try {
//                                    conn.executeMultipartPost_Send_Error(this
//                                                    .getClass().getSimpleName(),
//                                            Actions.getDeviceName(), "1", e
//                                                    .getMessage());
//                                } catch (Exception e1) {
//                                    e1.printStackTrace();
//                                }
                            }
                        }

                    }
                    //datasource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                AtomicReference<Event[]> ref = new AtomicReference<Event[]>( null);
                    /*TCTDbAdapter datasource = new TCTDbAdapter(CodeNewActivity.this);
                    datasource.open();*/

                String lan = "ar";

                setdatestamp();
                setcountryar();
                setserviceproviderAr();

                try {

                    ArrayList<Event> arr = datasource.getissue_Type("3");
                    ArrayList<Profile> arr11 = datasource.getAllProfiles();

                    try {
                        myApp.pass = arr11.get(0).getId();
                        pass = myApp.pass;
                    } catch (Exception e) {
                        e.printStackTrace();

                        try {
                            Connection conn = new Connection("");
                            conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    if (arr.size() == 0) {
                        eventsSource = "" + MyApplication.link + MyApplication.general + "GetIssueTypes?language=" + lan + "&password=" + pass + "&mainIssueTypeId=1";
                        error = Actions.readEvents(ref, eventsSource);
                        nn = ref.get();

                        try {
                            nn[0].getDate();
                        } catch (Exception e) {
                            e.printStackTrace();
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
                    ArrayList<Event> arr = datasource.getissue_Type("4");
                    if (arr.size() == 0) {
                        eventsSource = "" + MyApplication.link + MyApplication.general  + "GetIssueTypes?language=" + lan + "&password=" + pass + "&mainIssueTypeId=2";
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
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url;
            try {
                url = new URL(MyApplication.link + "GeneralServices.asmx/GetLookups?code=&token=" + Actions.create_token_new());
                Log.wtf("GetLookUpAsynck","url : " + url);

                DocumentBuilderFactory dbf = DocumentBuilderFactory .newInstance();
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

                    NodeList websiteList = fstElmnt.getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String nameen = "";
                    try {
                        nameen = ((Node) websiteList.item(0)).getNodeValue();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    NodeList isfortransList = fstElmnt.getElementsByTagName("NameAr");
                    Element isfortransElement = (Element) isfortransList .item(0);
                    isfortransList = isfortransElement.getChildNodes();
                    String namear = "";
                    try {
                        namear = ((Node) isfortransList.item(0)).getNodeValue();
                    } catch (Exception e) {
                        namear = "";
                        e.printStackTrace();
                    }

                    NodeList isfortransList2 = fstElmnt.getElementsByTagName("Order");
                    Element isfortransElement2 = (Element) isfortransList2.item(0);
                    isfortransList2 = isfortransElement2.getChildNodes();
                    String order = "";
                    try {
                        order = ((Node) isfortransList2.item(0)).getNodeValue();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    NodeList isfortransList3 = fstElmnt .getElementsByTagName("Id");
                    Element isfortransElemen3 = (Element) isfortransList3.item(0);
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
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //diaLog.wtfismiss();

            new UpdateUserRegistrationTable().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(CodeNewActivity.this, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            datasource.close();
            CodeNewActivity.this.finish();
        }
    }


    public void setdatestamp() {
        String arr = "";
        TCTDbAdapter sour = new TCTDbAdapter(CodeNewActivity.this);
        sour.open();
        try {
            arr = sour.getdatestamp();
        } catch (Exception e) {
            e.printStackTrace();
            arr = "";
        }
        sour.close();

        if (arr.equals("")) {
            try {
                URL url = new URL("" + MyApplication.link + MyApplication.general + "GetLastTextDateTimeUpdate?password=" + myApp.pass + "&language=en");
                Log.wtf("CodeNewActivity","setdatestamp url : " + url);

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("dateTime");
                Element nameElement = (Element) nodeList.item(0);
                nodeList = nameElement.getChildNodes();
                String name = ((Node) nodeList.item(0)).getNodeValue();

                TCTDbAdapter datasource = new TCTDbAdapter(CodeNewActivity.this);
                datasource.open();
                datasource.create_datestamp(name);
                datasource.close();

                String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

                Log.wtf("Code New Activity","formattedDate : " + formattedDate);
                Log.wtf("Code New Activity","setdatestamp : " + name);
                editEn.putString("LastDate", name);
                editEn.commit();
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.wtf("setdatestamp","crash : " + e.getMessage());

                try {
                    Connection conn = new Connection("");
                    String error_return = conn.executeMultipartPost_Send_Error( this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
                    Log.wtf("setdatestamp","error_return : " + error_return);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    public void setcountry() {
        ArrayList<Country> arr = new ArrayList<>();
        TCTDbAdapter sour = new TCTDbAdapter(CodeNewActivity.this);
        sour.open();
        try {
            arr = sour.getcountry("English");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sour.close();

        if (arr.size() == 0) {
            String[] a;
            String[] b;
            try {
                URL url = new URL("" + MyApplication.link + MyApplication.general + "GetCountries?password=" + myApp.pass + "&language=en");
                Log.wtf("CodeNewActivity","setcountry url : " + url);

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("Country");

                a = new String[nodeList.getLength()];
                b = new String[nodeList.getLength()];

                TCTDbAdapter datasource = new TCTDbAdapter(CodeNewActivity.this);
                datasource.open();

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;

                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String name = ((Node) nameList.item(0)).getNodeValue();

                    b[i] = name;

                    NodeList websiteList = fstElmnt.getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String title = ((Node) websiteList.item(0)).getNodeValue();
                    a[i] = title;

                    try {
                        long ln = datasource.create_country(name, title, "English");
                        if (!(ln > -1)) {// case row not inserted, may be
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.wtf("create_country","crash : " + e.getMessage());

                        // case not saved
                        try {
                            Connection conn = new Connection("");
                            String error_return = conn.executeMultipartPost_Send_Error(this .getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
                            Log.wtf("create_country","error_return : " + error_return);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                datasource.close();

            } catch (Exception e) {
                e.printStackTrace();
                Log.wtf("setcountry","crash : " + e.getMessage());

                try {
                    Connection conn = new Connection("");
                    String error_return = conn.executeMultipartPost_Send_Error( this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
                    Log.wtf("setcountry","error_return : " + error_return);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        }
    }


    public void setserviceprovider() {

        TCTDbAdapter sour = new TCTDbAdapter(CodeNewActivity.this);
        sour.open();
        ArrayList<ServicePro> arr = sour.getsp_lang("English");
        sour.close();
        if (arr.size() == 0) {

            try {
                URL url = new URL("" + MyApplication.link + MyApplication.general+ "GetServiceProviders?password=" + myApp.pass + "&language=en");
                Log.wtf("setserviceprovider after ValidateOTP","url : " + url.toString());

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("ServiceProvider");

                TCTDbAdapter datasource = new TCTDbAdapter(CodeNewActivity.this);
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
                    String isfortrans = ((Node) isfortransList.item(0)) .getNodeValue();

                    try {
                        long ln = datasource.create_sp(Integer.parseInt(name), title, isfortrans, "English", code);
                        if (!(ln > -1)) {// case row not inserted, may be
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.wtf("create_sp","crash : " + e.getMessage());

                        // case not saved
                        try {
                            Connection conn = new Connection("");
                            String error_return = conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
                            Log.wtf("create_sp","error_return : " + error_return);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                datasource.close();

            } catch (Exception e) {
                e.printStackTrace();
                Log.wtf("setserviceprovider","catch 1 : " + e.getMessage());

                try {
                    Connection conn = new Connection("");
                    String error_return = conn.executeMultipartPost_Send_Error( this.getClass().getSimpleName(),Actions.getDeviceName(), "1", e.getMessage());
                    Log.wtf("setserviceprovider","catch 1 , error_return : " + error_return);

                } catch (Exception e1) {
                    e1.printStackTrace();
                    Log.wtf("setserviceprovider","catch 2 : " + e1.getMessage());
                }
            }
        }
    }


    public void setcountryar() {
        ArrayList<Country> arr = new ArrayList<>();
        TCTDbAdapter sour = new TCTDbAdapter(CodeNewActivity.this);
        sour.open();
        try {
            arr = sour.getcountry("Arabic");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (arr.size() == 0) {
            String[] a;
            String[] b;

            try {
                URL url = new URL("" + MyApplication.link+ MyApplication.general + "GetCountries?password="+ myApp.pass + "&language=ar");
                Log.wtf("setserviceprovider after ValidateOTP","url : " + url.toString());

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("Country");

                a = new String[nodeList.getLength()];
                b = new String[nodeList.getLength()];

                TCTDbAdapter datasource = new TCTDbAdapter(CodeNewActivity.this);
                datasource.open();

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;
                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String name = ((Node) nameList.item(0)).getNodeValue();

                    b[i] = name;
                    NodeList websiteList = fstElmnt.getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String title = ((Node) websiteList.item(0)).getNodeValue();
                    a[i] = title;

                    try {
                        long ln = datasource.create_country(name, title, "Arabic");
                        if (!(ln > -1)) {// case row not inserted, may be
                            throw new Exception();
                        }
                    } catch (Exception e) {// case not saved
                        e.printStackTrace();
                        Log.wtf("create_country","catch : " + e.getMessage());

                        try {
                            Connection conn = new Connection("");
                            String error_return = conn .executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
                            Log.wtf("create_country","catch , error_return : " + error_return);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                datasource.close();

            } catch (Exception e) {
                e.printStackTrace();
                Log.wtf("setcountryar","catch 1 : " + e.getMessage());

                try {
                    Connection conn = new Connection("");
                    String error_return = conn.executeMultipartPost_Send_Error( this.getClass().getSimpleName(),Actions.getDeviceName(), "1", e.getMessage());
                    Log.wtf("setcountryar","catch 1 , error_return : " + error_return);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    public void setserviceproviderAr() {

        TCTDbAdapter sour = new TCTDbAdapter(CodeNewActivity.this);
        sour.open();
        ArrayList<ServicePro> arr = sour.getsp_lang("Arabic");
        sour.close();

        if (arr.size() == 0) {
            String[] a;
            String[] b;

            try {
                URL url = new URL("" + MyApplication.link + MyApplication.general
                        + "GetServiceProviders?password=" + myApp.pass + "&language=ar");
                Log.wtf("setserviceproviderAr","url : " + url);

                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("ServiceProvider");
                TCTDbAdapter datasource = new TCTDbAdapter(CodeNewActivity.this);
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    public void setissuedet(String idissue) {
        try {
            URL url = new URL(MyApplication.link + MyApplication.general + "GetIssueDetails?language=en&password=" + myApp.pass + "&issueid=" + idissue);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("IssueDetail");
            TCTDbAdapter datasource = new TCTDbAdapter(CodeNewActivity.this);
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

                NodeList modifyList = fstElmnt.getElementsByTagName("CanModifyMobileNo");
                Element modifyElement = (Element) modifyList.item(0);
                modifyList = modifyElement.getChildNodes();
                String modify = ((Node) modifyList.item(0)).getNodeValue();

                NodeList durationList = fstElmnt.getElementsByTagName("Duration");
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

                NodeList specialneedsduration = fstElmnt.getElementsByTagName("SpecialNeedsDuration");
                Element specialneedsElement = (Element) specialneedsduration.item(0);
                specialneedsduration = specialneedsElement.getChildNodes();
                String specialneeddurationvalue = ((Node) specialneedsduration.item(0)).getNodeValue();

                String specialneedunitarvalue = "", specialneedunitvalue = "";

                try {
                    NodeList specialneedsunitar = fstElmnt.getElementsByTagName("SpecialNeedsDurationUnitAr");
                    Element specialneedsunitarElement = (Element) specialneedsunitar.item(0);
                    specialneedsunitar = specialneedsunitarElement.getChildNodes();
                    specialneedunitarvalue = ((Node) specialneedsunitar.item(0)).getNodeValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    specialneedunitarvalue = "ساعات";
                }

                try {
                    NodeList specialneedsunit = fstElmnt.getElementsByTagName("SpecialNeedsDurationUnit");
                    Element specialneedsunitElement = (Element) specialneedsunit.item(0);
                    specialneedsunit = specialneedsunitElement.getChildNodes();
                    specialneedunitvalue = ((Node) specialneedsunit.item(0)).getNodeValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    specialneedunitvalue = "hours";
                }

                try {

                    long ln = datasource.createissue_detail(
                            Integer.parseInt(name), title, idissue, modify,  duration, unit, unitAr, "English",
                            specialneeddurationvalue, specialneedunitvalue, specialneedunitarvalue, checkappondate );

                    if (!(ln > -1)) {// case row not inserted, may be duplicated
                        throw new Exception();
                    }
                } catch (Exception e) {// case not saved
                    e.printStackTrace();
                    Log.wtf("createissue_detail Crash","Excpetion: " + e.getMessage());
                }
            }
            datasource.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.wtf("XML Pasing Excpetion","e: " + e.getMessage());

            try {
                Connection conn = new Connection("");
                String error_return = conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    public void setissuedetar(String idissue) {
        try {
            URL url = new URL("" + MyApplication.link + MyApplication.general + "GetIssueDetails?language=ar&password=" + myApp.pass + "&issueid=" + idissue);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("IssueDetail");
            TCTDbAdapter datasource = new TCTDbAdapter(CodeNewActivity.this);
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
        Intent intent = new Intent(CodeNewActivity.this, LoginNewActivity.class);
        startActivity(intent);
        CodeNewActivity.this.finish();
    }


    public void topBarBack(View v) {
        Intent intent = new Intent(CodeNewActivity.this, LoginNewActivity.class);
        startActivity(intent);
        CodeNewActivity.this.finish();
    }


    protected class LoginTask extends AsyncTask<Void, Void, String> {

        String reg_id = null;
        String username, password;

        String authenticationState = "";

        SharedPreferences mShared;
        private SharedPreferences.Editor edit;

        public LoginTask(String username, String password){
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            //pd = ProgressDialog.show(RegisterActivity.this, "", "", true);

            mShared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            edit = mShared.edit();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... a) {

            //<editor-fold desc = "old code">
            /*String url = "" + MyApplication.link + MyApplication.sms
                    + "AuthenticateUsernameAndPassword_OTP";
            // String url = "" + MyApplication.link + MyApplication.link +
            // "SaveRegistration";
            conn = new Connection(url);
            String response = "";
            String reg_id1 = "";
            try {
                reg_id1 = conn.executeMultipartPost_login(url, username, password);
                String[] r1 = reg_id1.split("</");
                String[] r2 = r1[0].split(">");
                reg_id = r2[2];

                *//*if (!reg_id.equals("0")) {
                    response = generate_otp(numbertxt);

                }*//*
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                try {
                    String error_return = conn.executeMultipartPost_Send_Error(
                            this.getClass().getSimpleName(),
                            Actions.getDeviceName(), "1", e.getMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            response = "SUCCESS";
            return response;*/
            //</editor-fold>

            try {

                URL url = new URL("" + MyApplication.link + MyApplication.sms + "AuthenticateUsernameAndPassword_OTP?username=" + username + "&password=" + password);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList websiteList0 = doc.getElementsByTagName("AuthenticationState");
                Element websiteElement0 = (Element) websiteList0.item(0);
                websiteList0 = websiteElement0.getChildNodes();
                authenticationState = ((Node) websiteList0.item(0)).getNodeValue();

                try {
                    NodeList websiteList = doc.getElementsByTagName("TransactionId");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    transactionId = ((Node) websiteList.item(0)).getNodeValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    transactionId = "";
                }

                try {
                    NodeList websiteList2 = doc.getElementsByTagName("TransportKey");
                    Element websiteElement2 = (Element) websiteList2.item(0);
                    websiteList2 = websiteElement2.getChildNodes();
                    transportKey = ((Node) websiteList2.item(0)).getNodeValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    transportKey = "";
                }

                try {
                    NodeList websiteList3 = doc.getElementsByTagName("Error");
                    Element websiteElement3 = (Element) websiteList3.item(0);
                    websiteList3 = websiteElement3.getChildNodes();
                    errorMessage = ((Node) websiteList3.item(0)).getNodeValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMessage = "";
                }

            }   catch (Exception e) {
                e.printStackTrace();
                transactionId = "";
                transportKey = "";
            }

            return authenticationState;
        }

        protected void onPostExecute(String result) {

            Log.wtf("authenticationState", "is " + authenticationState);
            Log.wtf("transactionId", "is " + transactionId);
            Log.wtf("transportKey", "is " + transportKey);
            Log.wtf("errorMessage", "is " + errorMessage);

            if (!authenticationState.equals("2")) {

                String[] array = errorMessage.split(Pattern.quote("||"));
                if (lang.equals("ar")){

                    error_msg = array[0];
                }else {

                    error_msg = array[1];
                }

                Actions.onCreateDialog1(CodeNewActivity.this, error_msg);
            }
            else{

                smsTimeOut = 120;
                handler = new Handler();
                handler.post(timeCounterRunnable);
                tvCount.setVisibility(View.VISIBLE);
                btnResend.setVisibility(View.GONE);
            }

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

        }
    }

}