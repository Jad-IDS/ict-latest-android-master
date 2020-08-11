package com.ids.ict.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.classes.CommonUtilities;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.Country;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.Profile;
import com.ids.ict.classes.PushNotificationActions;
import com.ids.ict.R;
import com.ids.ict.classes.QosTest;
import com.ids.ict.classes.ServicePro;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.MobileConfiguration;
import com.ids.ict.classes.NetworkUtil;
import com.ids.ict.classes.RoutineSchedule;
import com.ids.ict.parser.GetMobileConfiguration;
import com.ids.ict.parser.RoutineScheduleParser;

import org.json.JSONObject;
import org.shipp.util.DatabaseHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SplashActivity extends Activity {

    MyApplication myApp;
    ProgressBar prog;
    RelativeLayout main;
    boolean acceptNotification, notificationsDialog;
    String eventsSource = "https://arsel.qa/ArselServices/GeneralServices.asmx/GetIssueTypes?";
    String dateStamp, lan = "";
    Connection conn;
    SharedPreferences mshSharedPreferences;
    SharedPreferences mshared;
    Editor edit, editor;
    private RetrieveOnlineStatusTask retrieveOnlineStatusTask;
    public ArrayList<MobileConfiguration> mobile;
    DatabaseHandler db = new DatabaseHandler(SplashActivity.this);
    private GetMobileConfigurationTask getMobileConfigurationTask;
    Dialog d;
    boolean toRegister = true , isDeleteDbAndReload = false;
    JobScheduler jobScheduler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        mshSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        edit = mshSharedPreferences.edit();
        //edit.putString("LastDate", "d2614744058741a5b7ac3211e4c7ed2e");







        mshared = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        editor = mshared.edit();

        FirebaseApp.initializeApp(SplashActivity.this);
        setContentView(R.layout.activity_splash);
        Actions.setLocal(this);

        MyApplication.open = true;
        Actions.setScreenWidthHeight(SplashActivity.this);

        MyApplication.enableQOS = mshared.getBoolean(getResources().getString(R.string.enable_qos), false);
        MyApplication.goToQosActivity = mshared.getBoolean(getResources().getString(R.string.go_qos), true);
        MyApplication.Lang = mshared.getString(getResources().getString(R.string.language_key), MyApplication.ENGLISH);
        toRegister = mshared.getBoolean("toRegister", true);

        Log.wtf("enableQOS", "is " + MyApplication.enableQOS);
        Log.wtf("goToQosActivity", "is " + MyApplication.goToQosActivity);

        MyApplication.fromSettings = false;
        myApp = (MyApplication) getApplicationContext();
        prog = (ProgressBar) findViewById(R.id.progressBar1);
        main = (RelativeLayout) findViewById(R.id.relativeMAin);

        Log.wtf("firebaseToken", FirebaseInstanceId.getInstance().getToken());
        TCTDbAdapter dtBase = new TCTDbAdapter(SplashActivity.this);
        dtBase.open();
        ArrayList<Profile> profileList = dtBase.getAllProfiles();
        dtBase.close();


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd", Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        //+ formattedDate
        Log.wtf("formattedDate",": " + formattedDate);

        Log.wtf("## ## ## profileList","count : " + profileList.size());

        if (profileList.size() > 0) {

            if (false /*MyApplication.isTesting*/) {
                //<editor-fold desc = "MyApplication.isTesting">
                if (mshared.getBoolean("deleteAllData", true)) {

                    Log.wtf("delete", "all data");

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);

                        if (isScheduledJobServiceOn(1)) {

                            jobScheduler.cancel(1);
                            Log.wtf("job", "cancelled");
                        } else {

                            Log.wtf("job", "not scheduled");
                        }
                    }

                    deleteDatabase("ICTDB");
                    editor.clear().apply();
                    edit.clear().apply();

                    editor.putBoolean("deleteAllData", false).apply();
                } else {

                    myApp.pass = profileList.get(0).getId();
                }
                //</editor-fold>
            } else {

                myApp.pass = profileList.get(0).getId();
            }
        }
        Log.wtf("passs", myApp.pass);

        themeSetup();

        MyApplication.connectionType = NetworkUtil.getConnectivityStatusString(this);

        if (MyApplication.nightMod) {
            main.setBackgroundResource(R.drawable.splash_bg);
        }




        if (mshSharedPreferences.getBoolean(getResources().getString(R.string.first_launch), true)) {

            Log.wtf("first", "launch");

            edit.putBoolean(getResources().getString(R.string.first_launch), false).apply();

            retrieveOnlineStatusTask = new RetrieveOnlineStatusTask();
            retrieveOnlineStatusTask.execute();

            Launching eventLaunching = new Launching();
            eventLaunching.execute();
        }
        else {

            MyApplication.test = false;

            Log.wtf("once?", "is: " + mshSharedPreferences.getBoolean("once", true));
            Log.wtf("dev id?", "is: " + mshSharedPreferences.getInt(getString(R.string.device_id), 0));

            if (mshSharedPreferences.getBoolean("once", true) && mshared.getInt(getString(R.string.device_id), 0) != 0) {

                Log.wtf("UpdateDeviceOnce", "UpdateDeviceOnce");
             /*   try {

                    if (Actions.isNetworkAvailable(this))
                        new UpdateDeviceOnce().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.wtf("exc", e.getMessage());
                    edit.putInt(getString(R.string.device_id), 0).apply();
                }*/
            } else {

                Log.wtf("in?", "else ");
            }


            getMobileConfigurationTask = new GetMobileConfigurationTask();
            getMobileConfigurationTask.execute();

        }

        try {

            if (Actions.isNetworkAvailable(this))
                new UpdateDeviceOnce().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
            Log.wtf("exc", e.getMessage());
            edit.putInt(getString(R.string.device_id), 0).apply();
        }


        Log.wtf("MyApplication.pass", "is : " + MyApplication.pass);

    //   if (!MyApplication.pass.equals("")) {
            Log.wtf("update reg", "table");
            UpdateUserRegistrationTable update = new UpdateUserRegistrationTable();
            update.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
     //   }

        /*retrieveOnlineStatusTask = new RetrieveOnlineStatusTask();
        retrieveOnlineStatusTask.execute();*/

    }


    private boolean isScheduledJobServiceOn(int jobID) {

        boolean hasBeenScheduled = false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
                if (jobInfo.getId() == jobID) {
                    hasBeenScheduled = true;
                    break;
                }
            }
        }

        return hasBeenScheduled;
    }


    private void themeSetup() {

        //<editor-fold desc = "Theme Setup">
        if (mshSharedPreferences.getString("theme", "auto").equals("auto")) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (hour < 5 || hour > 16) {
                MyApplication.nightMod = true;
                //MyApplication.nightMod2 = true;
                //	edit.putString("theme", "navy");
                Log.wtf("FROM nightMod", "" + MyApplication.nightMod);
            } else {
                //	edit.putString("theme", "maroon");
                MyApplication.nightMod = false;

                Log.wtf("FROM nightMod", "" + MyApplication.nightMod);
            }
            //	edit.commit();
        } else if (mshSharedPreferences.getString("theme", "auto").equals("maroon")) {

            MyApplication.nightMod = false;
            edit.putString("theme", "maroon").apply();


            Log.wtf("FROM nightMod", "" + MyApplication.nightMod);


        } else if (mshSharedPreferences.getString("theme", "auto").equals("navy")) {
            MyApplication.nightMod = true;
            //MyApplication.nightMod2 = true;
            edit.putString("theme", "navy").apply();

            Log.wtf("FROM nightMod", "" + MyApplication.nightMod);
        }
        //</editor-fold>
    }


    @Override
    public void onBackPressed() {

        try {
            d.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected class Launching extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {

            Log.wtf("Launching", "start");
            prog.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... a) {

            dateStamp = setdatestamp(); //myApp.pass.equals("") ? "" :
            String dbDate = mshSharedPreferences.getString("LastDate", "");

            Log.wtf("dateStamp", ": " + dateStamp);
            Log.wtf("dbDate", ": " + dbDate);

            /*if ( (isNetworkAvailable() && !dateStamp.equals(dbDate) && !dbDate.equals("") ) || (dateStamp.equals("0001-01-01T00:00:00")) ) {

                if(!dateStamp.equals("0001-01-01T00:00:00")){
                    edit.putString("LastDate", dateStamp).apply();
                }else{
                    isDeleteDbAndReload = !myApp.pass.equals("") ? true : false;
                }
                return 1;
            } else {
                return 0;
            }*/

            if(MyApplication.isTesting){
                return 0;
            }

            if(dateStamp.equals("0001-01-01T00:00:00")){
                isDeleteDbAndReload = !myApp.pass.equals("") ? true : false;
            }

            if ( (isNetworkAvailable() && !dateStamp.equals("0001-01-01T00:00:00") ) ) {
                edit.putString("LastDate", dateStamp).apply();
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {

            Log.wtf("Launching", "end");
            Log.wtf("result", "" + result);
            //prog.setVisibility(View.GONE);

            if (result == 0) {
                //	GetLookUpAsynck get = new GetLookUpAsynck();
                //	get.execute(true);

                startMyActivity();
            } else {
                GetLookUpAsynck getlookup = new GetLookUpAsynck();
                getlookup.execute(false);
            }
        }
    }


    public class UpdateUserRegistrationTable extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //Connection.disableSSLCertificateChecking();

            String url = "" + MyApplication.link + MyApplication.post + "UpdateRegistrationTable";
            String token = Actions.create_token_new();

            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("password", MyApplication.pass);
            parameters.put("deviceId", mshared.getInt(getString(R.string.device_id), 0) + "");
            parameters.put("tokenNumber", token);
            String result = Connection.POST(url, parameters);

            Log.wtf("password reg table", "is " + MyApplication.pass);
            Log.wtf("deviceId reg table", "is " + mshared.getInt(getString(R.string.device_id), 0) + "");
            Log.wtf("tokenNumber reg table", "is " + token);
            Log.wtf("result reg table", "is " + result);
            return null;
        }
    }


    public class GetLookUpAsynck extends AsyncTask<Boolean, Void, Void> {
        SharedPreferences prefsEn;
        Editor editEn;
        boolean start = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            Log.wtf("GetLookUpAsynck", "start");

            super.onPreExecute();
            prefsEn = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);

            editEn = prefsEn.edit();
        }

        @Override
        protected Void doInBackground(Boolean... params) {
            // TODO Auto-generated method stub
            URL url;
            start = params[0];
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
                    String code = "";

                    try {
                        NodeList nameList = fstElmnt.getElementsByTagName("Code");
                        Element nameElement = (Element) nameList.item(0);
                        nameList = nameElement.getChildNodes();

                        code = ((Node) nameList.item(0)).getNodeValue();
                    } catch (Exception e) {
                        Log.wtf("Index ", "" + i);
                        code = "";
                        e.printStackTrace();
                    }

                    NodeList websiteList = fstElmnt.getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String nameen = ((Node) websiteList.item(0)).getNodeValue();

                    NodeList isfortransList = fstElmnt.getElementsByTagName("NameAr");
                    Element isfortransElement = (Element) isfortransList.item(0);
                    isfortransList = isfortransElement.getChildNodes();
                    String namear = "";
                    try {
                        namear = ((Node) isfortransList.item(0))
                                .getNodeValue();
                    } catch (Exception e) {

                    }
                    NodeList isfortransList2 = fstElmnt.getElementsByTagName("Order");
                    Element isfortransElement2 = (Element) isfortransList2.item(0);
                    isfortransList2 = isfortransElement2.getChildNodes();
                    String order = ((Node) isfortransList2.item(0)).getNodeValue();

                    NodeList isfortransList3 = fstElmnt.getElementsByTagName("Id");
                    Element isfortransElemen3 = (Element) isfortransList3.item(0);
                    isfortransList3 = isfortransElemen3.getChildNodes();
                    String id = ((Node) isfortransList3.item(0)).getNodeValue();

                    LookUp look = new LookUp();
                    look.id = id;
                    look.order = order;
                    look.namear = namear;
                    look.nameen = nameen;
                    look.code = code;
                    Gson gson = new Gson();
                    String json = gson.toJson(look);
                    editEn.putString(id, json).apply();
                    editEn.putString(code, json).apply();

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
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Log.wtf("GetLookUpAsynck", "end");

            if (mshSharedPreferences.getString("Isdemo", "false").equals("true")) {

                //<editor-fold desc = "Isdemo">
                if (getLookup("92").namear.equals("false")) {
                    Dialog d = null;
                    Typeface tf;

                    if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
                        tf = MyApplication.facePolarisMedium;
                    } else {
                        tf = MyApplication.faceDinar;
                    }

                    //final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(SplashActivity.this, R.style.AlertDialogCustom));

                    LayoutInflater inflater = getLayoutInflater();
                    final View textEntryView = inflater.inflate(R.layout.dialog_warning,
                            null);
                    TextView textView = (TextView) textEntryView
                            .findViewById(R.id.title);
                    textView.setGravity(Gravity.CENTER);
                    textView.setText(getString(R.string.warning2));
                    final TextView msgg = (TextView) textEntryView
                            .findViewById(R.id.msg);
                    textView.setGravity(Gravity.CENTER);
                    if (MyApplication.Lang.equals(MyApplication.ENGLISH))
                        msgg.setText(getLookup("95").nameen);
                    else
                        msgg.setText(getLookup("95").namear);
                    textView.setTypeface(tf);
                    msgg.setTypeface(tf);
                    alertDialog.setView(textEntryView);

                    alertDialog.setPositiveButton(getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                                    //    edit.putString("LastDate","Repeat");
                                    //   edit.commit();
                                    //  deleteDatabase("ICTDB");
                                    TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
                                    datasource.open();
                                    ArrayList<Profile> arr11 = datasource.getAllProfiles();


                                    if (arr11.size() > 0) {
                                        lan = arr11.get(0).getlang();
                                        datasource.updateProfileRow("", "", "");

                                    }
                                    datasource.close();
                                    startActivity(intent);
                                    finish();
                                }
                            });

                    d = alertDialog.create();
                    d.setCanceledOnTouchOutside(false);
                    d.show();
                } else {
//                    TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
//                    datasource.open();
//                    ArrayList<Profile> arr11 = datasource.getAllProfiles();
//
//
//                    if (arr11.size() > 0) {
//                        lan = arr11.get(0).getlang();
//                        datasource.updateProfileRow(mshared.getString(getString(R.string.language_key),"en"),"","");
//
//                    }
//                    datasource.close();
                    LaunchingEvent eventLaunching = new LaunchingEvent();
                    eventLaunching.execute();
                }
                //</editor-fold>
            } else {
                //if(!start) {
                LaunchingEvent eventLaunching = new LaunchingEvent();
                eventLaunching.execute();
            }
            //}
            //else
            //	startMyActivity();
        }
    }


    public String setdatestamp() {

        String date = "";
        try {

            //Connection.disableSSLCertificateChecking();
            URL url = new URL(MyApplication.link + MyApplication.general + "GetLastTextDateTimeUpdate?password=" + myApp.pass);
            Log.wtf("Splach","setdatestamp url " + url);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("dateTime");
            Element nameElement = (Element) nodeList.item(0);
            nodeList = nameElement.getChildNodes();
            String name = ((Node) nodeList.item(0)).getNodeValue();
            date = name;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }


    public void startMyActivity() {

        CommonUtilities.setSenderId(SplashActivity.this);
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        acceptNotification = mPrefs.getBoolean(getString(R.string.notification_key), true);

        notificationsDialog = mPrefs.getBoolean("show", true);
        Editor editor = mPrefs.edit();
        if (notificationsDialog) {
            editor.putBoolean("show", false).apply();
            /*boolean registered = PushNotificationActions
                    .isRegistered(SplashActivity.this);
            if (!registered) {

                //if (mshSharedPreferences.getBoolean(getResources().getString(R.string.first_launch), true)) {
                    Log.wtf("im ","here");
                    PushNotificationActions.showRegistrationDialog(
                            SplashActivity.this,
                            "Do you want to recieve notifications?");
                //}



            } else {
                loadPage();
            }*/

            Log.wtf("im ", "here");
            PushNotificationActions.showRegistrationDialog(SplashActivity.this, "Do you want to recieve notifications?");
        } else {
            loadPage();
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


    private class GetMobileConfigurationTask extends AsyncTask<Void, Void, ArrayList<MobileConfiguration>> {

        ProgressDialog pd;
        int version = -1, force = -1, needsUpdate = -1;
        boolean proceed;

        @Override
        protected void onPreExecute() {

            prog.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MobileConfiguration> doInBackground(Void... params) {
            if (Actions.isNetworkAvailable(SplashActivity.this)) {

                String url = MyApplication.link + MyApplication.general + "GetMobileConfiguration?key=";

                Connection conn = new Connection(url);
                //conn.disableSSLCertificateChecking();

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

            //prog.setVisibility(View.GONE);

            for (int i = 0; i < v.size(); i++) {
                if (v.get(i).getKey().equalsIgnoreCase("androidVersion")) {
                    version = Integer.parseInt(v.get(i).getValue());

                }
                if (v.get(i).getKey().equalsIgnoreCase("AndroidForceUpdate")) {
                    force = Integer.parseInt(v.get(i).getValue());

                }
                if (v.get(i).getKey().equalsIgnoreCase("Qos_Log")) {
                    MyApplication.qosLog = Integer.parseInt(v.get(i).getValue());
                }
                if (v.get(i).getKey().equalsIgnoreCase("Qos_Log_Devices")) {
                    MyApplication.qosLogDevices = v.get(i).getValue();
                }
            }
            needsUpdate = Actions.CheckVersion(SplashActivity.this, version, force);

            Log.wtf("version", "" + version);
            Log.wtf("force", "" + force);
            Log.wtf("needsUpdate", "" + needsUpdate);


            if (needsUpdate == 0) {

                Launching eventLaunching = new Launching();
                eventLaunching.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                retrieveOnlineStatusTask = new RetrieveOnlineStatusTask();
                retrieveOnlineStatusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } else {

                if (needsUpdate == 1) {
                    if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                        onCreateDialogForPlayStoreNoForce(SplashActivity.this, getLookup("93").namear);
                    } else {
                        onCreateDialogForPlayStoreNoForce(SplashActivity.this, getLookup("93").nameen);
                    }
                } else {
                    //force update
                    if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                        onCreateDialogForPlayStore(SplashActivity.this, getLookup("93").namear);
                    } else {
                        onCreateDialogForPlayStore(SplashActivity.this, getLookup("93").nameen);
                    }
                }
            }

        }

    }


    public void onCreateDialogForPlayStore(final Activity activity, String msg) {

        d = null;
        Typeface tf;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;


        } else {

            tf = MyApplication.faceDinar;
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));
        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_warning,
                null);
        TextView textView = (TextView) textEntryView
                .findViewById(R.id.title);
        textView.setGravity(Gravity.CENTER);
        textView.setText(activity.getString(R.string.warning2));
        final TextView msgg = (TextView) textEntryView
                .findViewById(R.id.msg);
        textView.setGravity(Gravity.CENTER);
        msgg.setText(msg);
        textView.setTypeface(tf);
        msgg.setTypeface(tf);
        alertDialog.setView(textEntryView);

        alertDialog.setNegativeButton(activity.getResources().getString(R.string.update2), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //dialog.dismiss();
                final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getLookup("96").namear)));

                    activity.finish();
                    ;
                } catch (android.content.ActivityNotFoundException anfe) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getLookup("96").namear)));
                    activity.finish();
                    ;
                }
            }
        });
        d = alertDialog.create();
        d.setCanceledOnTouchOutside(false);
        d.show();

        d.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SplashActivity.this.finish();
            }
        });
        // d.getWindow().setAttributes(lp);
    }


    public void onCreateDialogForPlayStoreNoForce(final Activity activity, String msg) {

        d = null;
        Typeface tf;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;


        } else {

            tf = MyApplication.faceDinar;

        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));
        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_warning,
                null);
        TextView textView = (TextView) textEntryView
                .findViewById(R.id.title);
        textView.setGravity(Gravity.CENTER);
        textView.setText(activity.getString(R.string.warning2));
        final TextView msgg = (TextView) textEntryView
                .findViewById(R.id.msg);
        textView.setGravity(Gravity.CENTER);
        msgg.setText(msg);
        textView.setTypeface(tf);
        msgg.setTypeface(tf);
        alertDialog.setView(textEntryView);


        // Add action buttons
        alertDialog.setPositiveButton(activity.getResources().getString(R.string.update2), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getLookup("96").namear)));

                    activity.finish();
                } catch (android.content.ActivityNotFoundException anfe) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getLookup("96").namear)));
                    activity.finish();
                }
            }
        }).setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                retrieveOnlineStatusTask = new RetrieveOnlineStatusTask();
                retrieveOnlineStatusTask.execute();

                Launching l = new Launching();
                l.execute();
            }
        });
        d = alertDialog.create();
        d.setCanceledOnTouchOutside(false);
        d.show();

        d.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SplashActivity.this.finish();
            }
        });

    }


    public void loadPage() {


        //lan = mshared.getString(getString(R.string.language_key),"");
        lan = "";

        TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
        datasource.open();
        ArrayList<Profile> profilesList = datasource.getAllProfiles();
        datasource.close();

        if (profilesList.size() > 0) lan = profilesList.get(0).getlang();
        if (lan.equals("")) {
            if (profilesList.size() > 1)
                lan = profilesList.get(1).getlang();
        }

        Log.wtf("SPLASH", "LAN is : " + lan);

        if (lan.equalsIgnoreCase("English") || lan.equalsIgnoreCase("En")) {
            MyApplication.Lang = MyApplication.ENGLISH;

            if (Actions.isNetworkAvailable(SplashActivity.this)) {

                new GetRoutineSchedule().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {

                Gson gson = new Gson();
                String json = mshared.getString("RoutineSchedule", "");
                MyApplication.routineSchedule = gson.fromJson(json, RoutineSchedule.class);
                Log.wtf("read", "from gson");
            }

            Intent intent;
            if (MyApplication.isNotification) {
                MyApplication.isNotification=false;
                intent = new Intent(SplashActivity.this, NotificationListActivity.class);
            }

            else  if (MyApplication.goToQosActivity) {

                intent = new Intent(SplashActivity.this, QosTermsActivity.class);
            }


            else {

                intent = new Intent(SplashActivity.this, HomePageActivity.class);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            MyApplication.Lang = lan;
            startActivity(intent);
            SplashActivity.this.finish();

        } else if (lan.equalsIgnoreCase("Arabic") || lan.equalsIgnoreCase("Ar")) {

            MyApplication.Lang = MyApplication.ARABIC;

            if (Actions.isNetworkAvailable(SplashActivity.this)) {

                new GetRoutineSchedule().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Gson gson = new Gson();
                String json = mshared.getString("RoutineSchedule", "");
                MyApplication.routineSchedule = gson.fromJson(json, RoutineSchedule.class);

                Log.wtf("read", "from gson");
            }

            Intent intent;
            if (MyApplication.isNotification) {
                MyApplication.isNotification=false;
                intent = new Intent(SplashActivity.this, NotificationListActivity.class);
            }
            else if (MyApplication.goToQosActivity) {
                Log.wtf("go_to","terms");
                intent = new Intent(SplashActivity.this, QosTermsActivity.class);
            }



            else {
                Log.wtf("go_to","home");
                intent = new Intent(SplashActivity.this, HomePageActivity.class);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            MyApplication.Lang = lan;
            startActivity(intent);
            SplashActivity.this.finish();

            //if (!checkIfHaveSimCard()) {
            //    intent = new Intent(SplashActivity.this, RegisterActivity.class);
            //}

        } else {
            new GetRoutineSchedule().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            Intent intent = new Intent(SplashActivity.this, LanguageActivity.class);
            intent.putExtra("reLogin",isDeleteDbAndReload);
            startActivity(intent);
            SplashActivity.this.finish();
        }
    }


    public class LaunchingEvent extends AsyncTask<Void, Void, Integer> {
        com.ids.ict.Error error;
        Event[] event;

        @Override
        public void onPreExecute() {
            prog.setVisibility(View.VISIBLE);

        }

        @Override
        public Integer doInBackground(Void... params) {
            Log.d("LaunchingEvent", "passed here");
            AtomicReference<Event[]> ref = new AtomicReference<Event[]>(null);

            try {
                TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
                datasource.open();
                datasource.deleteAllCountry("1");
                datasource.deleteAllIssue_detail(1);
                datasource.deleteAllIssue_type(1);
                datasource.deleteAllsp(1);
                datasource.deleteAllProfile();

                Log.wtf("LaunchingEvent","Delete tables ( Country , Issue_detail , Issue_type , sp)");
                setcountry();
                setcountryar();
                setserviceprovider();
                setserviceproviderAr();
                datasource.updateDate(dateStamp);
                ArrayList<Event> issueList = datasource.getissue_Type("1");
                ArrayList<Profile> profilesList = datasource.getAllProfiles();

                if(profilesList.size() > 0){
                    try {
                        myApp.pass = profilesList.get(0).getId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (issueList.size() == 0) {
                    eventsSource = MyApplication.link + MyApplication.general + "GetIssueTypes?language=en&password=" + myApp.pass + "&mainIssueTypeId=1";
                    Log.wtf("eventsSource",": " + eventsSource);

                    error = Actions.readEvents(ref, eventsSource);
                    Log.wtf("eventsSource","error msg : " + error.getMessage());

                    event = new Event[]{};
                    event = ref.get();
                    Log.wtf("event","en-1 size = " + event.length);

                    /*try {
                        String ii = event[0].getDate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                    for (int i = 0; i < event.length; i++) {
                        try {
                            String urlth = event[i].getThumbnailUrlDayl();
                            Connection conn = new Connection(urlth);
                            /*Bitmap b = conn.readImage();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            try {
                                b.compress(Bitmap.CompressFormat.PNG, 100,
                                        stream);
                            } catch (Exception e) {

                            }
                            byte[] array = stream.toByteArray();

                            String urlthN = event[i].getThumbnailUrlNight();
                            Connection connN = new Connection(urlthN);
                            Bitmap bN = connN.readImage();
                            ByteArrayOutputStream streamN = new ByteArrayOutputStream();
                            try {
                                bN.compress(Bitmap.CompressFormat.PNG, 100,
                                        streamN);
                            } catch (Exception e) {

                            }
                            byte[] arrayN = streamN.toByteArray();*/

                            long ln = datasource.createissue_type(
                                    event[i].getId(), event[i].getName(),
                                    event[i].getShowMap(),
                                    event[i].getDescription(),
                                    event[i].getDate(), event[i].getLocation(),
                                    "1", event[i].getThumbnailUrlDayl(), event[i].getThumbnailUrlNight(), event[i].getServiceProviderTransfer());
                            setissuedet("" + event[i].getId());

                            if (!(ln > -1)) {// case row not inserted, may be
                                // duplicated
                                throw new Exception();
                            }
                        } catch (Exception e) {// case not saved
                            e.printStackTrace();
                        }
                    }
                }
                datasource.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
                datasource.open();
                ArrayList<Event> arr = datasource.getissue_Type("2");
                if (arr.size() == 0) {
                    eventsSource = MyApplication.link + MyApplication.general + "GetIssueTypes?language=en&password=" + myApp.pass + "&mainIssueTypeId=2";
                    // eventsSource =
                    // "https://arsel.qa/ArselServices/GeneralServices.asmx/GetIssueTypes?language="
                    // + lan + "&password="+myApp.pass+"&mainIssueTypeId=2" ;
                    Log.d("eventsource", eventsSource);

                    event = new Event[]{};
                    error = Actions.readEvents(ref, eventsSource);
                    event = ref.get();
                    Log.wtf("event","en-2 size = " + event.length);
                    /*try {
                        String ii = event[0].getDate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                    for (int i = 0; i < event.length; i++) {
                        try {
                            String urlth = event[i].getThumbnailUrlDayl();
                            Connection conn = new Connection(urlth);
                            /*Bitmap b = conn.readImage();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            try {
                                b.compress(Bitmap.CompressFormat.PNG, 100,
                                        stream);
                            } catch (Exception e) {

                            }
                            byte[] array = stream.toByteArray();

                            String urlthN = event[i].getThumbnailUrlNight();
                            Connection connN = new Connection(urlthN);
                            Bitmap bN = connN.readImage();
                            ByteArrayOutputStream streamN = new ByteArrayOutputStream();
                            try {
                                bN.compress(Bitmap.CompressFormat.PNG, 100,
                                        streamN);
                            } catch (Exception e) {

                            }
                            byte[] arrayN = streamN.toByteArray();*/


                            long ln = datasource.createissue_type(
                                    event[i].getId(), event[i].getName(),
                                    event[i].getShowMap(),
                                    event[i].getDescription(),
                                    event[i].getDate(), event[i].getLocation(),
                                    "2", event[i].getThumbnailUrlDayl(), event[i].getThumbnailUrlNight(), event[i].getServiceProviderTransfer());
                            setissuedet("" + event[i].getId());

                            if (!(ln > -1)) {// case row not inserted, may be
                                // duplicated
                                throw new Exception();
                            }
                        } catch (Exception e) {// case not saved
                            e.printStackTrace();
                        }
                    }

                }
                datasource.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
                datasource.open();

                ArrayList<Event> arr = datasource.getissue_Type("3");
                if (arr.size() == 0) {
                    eventsSource = MyApplication.link + MyApplication.general + "GetIssueTypes?language=ar&password=" + myApp.pass + "&mainIssueTypeId=1";
                    // eventsSource =
                    // "https://arsel.qa/ArselServices/GeneralServices.asmx/GetIssueTypes?language=ar&password="+myApp.pass+"&mainIssueTypeId=1"
                    // ;
                    Log.d("eventsource", eventsSource);

                    event = new Event[]{};
                    error = Actions.readEvents(ref, eventsSource);
                    event = ref.get();
                    Log.wtf("event","ar-1 size = " + event.length);

                    /*try {
                        String ii = event[0].getDate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                    for (int i = 0; i < event.length; i++) {
                        try {
                            String urlth = event[i].getThumbnailUrlDayl();
                            Connection conn = new Connection(urlth);
                            /*Bitmap b = conn.readImage();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            try {
                                b.compress(Bitmap.CompressFormat.PNG, 100,
                                        stream);
                            } catch (Exception e) {

                            }
                            byte[] array = stream.toByteArray();

                            String urlthN = event[i].getThumbnailUrlNight();
                            Connection connN = new Connection(urlthN);
                            Bitmap bN = connN.readImage();
                            ByteArrayOutputStream streamN = new ByteArrayOutputStream();
                            try {
                                bN.compress(Bitmap.CompressFormat.PNG, 100,
                                        streamN);
                            } catch (Exception e) {

                            }
                            byte[] arrayN = streamN.toByteArray();*/


                            long ln = datasource.createissue_type(
                                    event[i].getId(), event[i].getName(),
                                    event[i].getShowMap(),
                                    event[i].getDescription(),
                                    event[i].getDate(), event[i].getLocation(),
                                    "3", event[i].getThumbnailUrlDayl(), event[i].getThumbnailUrlNight(), event[i].getServiceProviderTransfer());
                            setissuedetar("" + event[i].getId());

                            if (!(ln > -1)) {// case row not inserted, may be
                                // duplicated
                                throw new Exception();
                            }
                        } catch (Exception e) {// case not saved
                            e.printStackTrace();
                        }
                    }

                }
                datasource.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
                datasource.open();
                ArrayList<Event> arr = datasource.getissue_Type("4");
                Log.wtf("arr","getissue_Type(4) Count = " + arr.size());

                if (arr.size() == 0) {
                    eventsSource = MyApplication.link + MyApplication.general + "GetIssueTypes?language=ar&password=" + myApp.pass + "&mainIssueTypeId=2";
                    // eventsSource =
                    // "https://arsel.qa/ArselServices/GeneralServices.asmx/GetIssueTypes?language=ar&password="+myApp.pass+"&mainIssueTypeId=2"
                    // ;
                    Log.d("eventsource", ": " + eventsSource);

                    event = new Event[]{};
                    error = Actions.readEvents(ref, eventsSource);
                    event = ref.get();
                    Log.wtf("event","ar-2 size = " + event.length);

                    /*try {
                        String ii = event[0].getDate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                    for (int i = 0; i < event.length; i++) {
                        try {
                            String urlth = event[i].getThumbnailUrlDayl();
                            Connection conn = new Connection(urlth);

                           /* Bitmap b = conn.readImage();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            try {
                                b.compress(Bitmap.CompressFormat.PNG, 100,
                                        stream);
                            } catch (Exception e) {

                            }
                            byte[] array = stream.toByteArray();

                            String urlthN = event[i].getThumbnailUrlNight();
                            Connection connN = new Connection(urlthN);
                            Bitmap bN = connN.readImage();
                            ByteArrayOutputStream streamN = new ByteArrayOutputStream();
                            try {
                                bN.compress(Bitmap.CompressFormat.PNG, 100,
                                        streamN);
                            } catch (Exception e) {

                            }
                            byte[] arrayN = streamN.toByteArray();*/

                            long ln = datasource.createissue_type(
                                    event[i].getId(), event[i].getName(),
                                    event[i].getShowMap(),
                                    event[i].getDescription(),
                                    event[i].getDate(), event[i].getLocation(),
                                    "4", event[i].getThumbnailUrlDayl(), event[i].getThumbnailUrlNight(), event[i].getServiceProviderTransfer());
                            setissuedetar("" + event[i].getId());

                            if (!(ln > -1)) {// case row not inserted, may be
                                // duplicated
                                throw new Exception();
                            }
                        } catch (Exception e) {// case not saved
                            e.printStackTrace();
                        }
                    }

                }
                datasource.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
            datasource.open();
            ArrayList<Profile> arr11 = datasource.getAllProfiles();
            datasource.close();

            if (arr11.size() > 0) {
                lan = arr11.get(0).getlang();
            }
            return 1;
        }

        @Override
        public void onPostExecute(Integer result) {

            CommonUtilities.setSenderId(SplashActivity.this);
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
            acceptNotification = mPrefs.getBoolean(getString(R.string.notification_key), true);
            notificationsDialog = mPrefs.getBoolean("show", true);

            /*if(isDeleteDbAndReload){
                loadPage();
            }
            else*/
            if (notificationsDialog) {
                boolean registered = PushNotificationActions.isRegistered(SplashActivity.this);
                if (!registered) {
                    PushNotificationActions.showRegistrationDialog( SplashActivity.this,"Do you want to recieve notifications?");
                } else {
                    loadPage();
                }
            }
            else{
                loadPage();
            }

        }
    }


    public void setcountry() {

        TCTDbAdapter dbSQL = new TCTDbAdapter(SplashActivity.this);
        dbSQL.open();
        ArrayList<Country> countryList = dbSQL.getcountry("English");
        dbSQL.close();

        if (countryList.size() == 0) {

            try {
                URL url = new URL(MyApplication.link + MyApplication.general
                        + "GetCountries?password=" + myApp.pass
                        + "&language=en");
                // URL url = new
                // URL("https://arsel.qa/ArselServices/GeneralServices.asmx/GetCountries?password="+myApp.pass+"&language=en");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("Country");
                TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
                datasource.open();

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;

                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String id = ((Node) nameList.item(0)).getNodeValue();

                    NodeList websiteList = fstElmnt
                            .getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String name = ((Node) websiteList.item(0)).getNodeValue();

                    try {
                        long ln = datasource
                                .create_country(id, name, "English");
                        if (!(ln > -1)) {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                    }
                }
                datasource.close();
            } catch (Exception e) {

            }
        }
    }


    public void setcountryar() {
        TCTDbAdapter sour = new TCTDbAdapter(SplashActivity.this);
        sour.open();

        ArrayList<Country> arr = sour.getcountry("Arabic");
        sour.close();
        if (arr.size() == 0) {

            try {
                URL url = new URL(MyApplication.link + MyApplication.general
                        + "GetCountries?password=" + myApp.pass
                        + "&language=ar");
                // URL url = new
                // URL("https://arsel.qa/ArselServices/GeneralServices.asmx/GetCountries?password="+myApp.pass+"&language=ar");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("Country");

                TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
                datasource.open();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;

                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String id = ((Node) nameList.item(0)).getNodeValue();

                    NodeList websiteList = fstElmnt
                            .getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String name = ((Node) websiteList.item(0)).getNodeValue();

                    try {
                        long ln = datasource.create_country(id, name, "Arabic");
                        if (!(ln > -1)) {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                    }
                }
                datasource.close();
            } catch (Exception e) {

            }
        }
    }


    public void setserviceproviderAr() {
        TCTDbAdapter sour = new TCTDbAdapter(SplashActivity.this);
        sour.open();
        ArrayList<ServicePro> arr = sour.getsp_lang("Arabic");
        sour.close();

        if (arr.size() == 0) {

            try {
                URL url = new URL(MyApplication.link + MyApplication.general
                        + "GetServiceProviders?password=" + myApp.pass
                        + "&language=ar");
                // URL url = new
                // URL("https://arsel.qa/ArselServices/GeneralServices.asmx/GetServiceProviders?password="+myApp.pass+"&language=ar");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("ServiceProvider");
                TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
                datasource.open();

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;

                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String id = ((Node) nameList.item(0)).getNodeValue();

                    NodeList websiteList = fstElmnt
                            .getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String name = ((Node) websiteList.item(0)).getNodeValue();

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
                        long ln = datasource.create_sp(Integer.parseInt(id),
                                name, isfortrans, "Arabic", code);
                        if (!(ln > -1)) {// case row not inserted, may be
                            // duplicated
                            throw new Exception();
                        }
                    } catch (Exception e) {
                    }
                }
                datasource.close();
            } catch (Exception e) {
                Log.wtf("setserviceproviderAr","crash : " + e.getMessage());
            }
        }
    }


    public void setserviceprovider() {
        TCTDbAdapter sour = new TCTDbAdapter(SplashActivity.this);
        sour.open();
        ArrayList<ServicePro> arr = sour.getsp_lang("English");
        sour.close();

        if (arr.size() == 0) {
            try {
                URL url = new URL(MyApplication.link + MyApplication.general
                        + "GetServiceProviders?password=" + myApp.pass
                        + "&language=en");
                // URL url = new
                // URL("https://arsel.qa/ArselServices/GeneralServices.asmx/GetServiceProviders?password="+myApp.pass+"&language=en");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("ServiceProvider");
                TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
                datasource.open();

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;

                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String id = ((Node) nameList.item(0)).getNodeValue();

                    NodeList websiteList = fstElmnt
                            .getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String name = ((Node) websiteList.item(0)).getNodeValue();

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
                        long ln = datasource.create_sp(Integer.parseInt(id),
                                name, isfortrans, "English", code);
                        if (!(ln > -1)) {// case row not inserted, may be
                            // duplicated
                            throw new Exception();
                        }
                    } catch (Exception e) {// case not saved
                    }

                }
                datasource.close();
            } catch (Exception e) {
                Log.wtf("setserviceproviderAr","crash : " + e.getMessage());
            }
        }
    }


    public boolean checkIfHaveSimCard() {
        boolean have = false;
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            have = false;
        } else {
            if (isSimSupport(SplashActivity.this))
                have = true;
            else
                have = false;
        }
        return have;
    }


    public boolean isSimSupport(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE); // gets the
        // current
        // TelephonyManager
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

    }


    public void setissuedet(String idissue) {
        try {
            URL url = new URL(MyApplication.link + MyApplication.general
                    + "GetIssueDetails?language=en&password=" + myApp.pass
                    + "&issueid=" + idissue);
            // URL url = new
            // URL("https://arsel.qa/ArselServices/GeneralServices.asmx/GetIssueDetails?language=en&password="+myApp.pass+"&issueid="
            // + idissue);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("IssueDetail");

            TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
            datasource.open();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;

                NodeList nameList = fstElmnt.getElementsByTagName("Id");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                String id = ((Node) nameList.item(0)).getNodeValue();


                NodeList nameListappdate = fstElmnt.getElementsByTagName("CheckOnApplicationDate");
                Element nameElementappdate = (Element) nameListappdate.item(0);
                nameListappdate = nameElementappdate.getChildNodes();
                String checkappondate = ((Node) nameListappdate.item(0)).getNodeValue();


                NodeList websiteList = fstElmnt.getElementsByTagName("Name");
                Element websiteElement = (Element) websiteList.item(0);
                websiteList = websiteElement.getChildNodes();
                String name = ((Node) websiteList.item(0)).getNodeValue();

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
                String unit = "", unitAr = "";
                try {
                    NodeList unitList = fstElmnt.getElementsByTagName("Unit");
                    Element unitElement = (Element) unitList.item(0);
                    unitList = unitElement.getChildNodes();
                    unit = ((Node) unitList.item(0)).getNodeValue();
                } catch (Exception e) {

                }
                try {
                    NodeList unitArList = fstElmnt.getElementsByTagName("UnitAr");
                    Element unitArElement = (Element) unitArList.item(0);
                    unitArList = unitArElement.getChildNodes();
                    unitAr = ((Node) unitArList.item(0)).getNodeValue();
                } catch (Exception e) {

                }
                NodeList specialneedsduration = fstElmnt
                        .getElementsByTagName("SpecialNeedsDuration");
                Element specialneedsElement = (Element) specialneedsduration
                        .item(0);
                specialneedsduration = specialneedsElement.getChildNodes();
                String specialneeddurationvalue = ((Node) specialneedsduration
                        .item(0)).getNodeValue();
                String specialneedunitarvalue, specialneedunitvalue;
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
                            Integer.parseInt(id), name, idissue, modify,
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
        }
    }


    public void setissuedetar(String idissue) {
        try {
            URL url = new URL(MyApplication.link + MyApplication.general
                    + "GetIssueDetails?language=ar&password=" + myApp.pass
                    + "&issueid=" + idissue);
            // URL url = new
            // URL("https://arsel.qa/ArselServices/GeneralServices.asmx/GetIssueDetails?language=ar&password="+myApp.pass+"&issueid="
            // + idissue);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("IssueDetail");

            TCTDbAdapter datasource = new TCTDbAdapter(SplashActivity.this);
            datasource.open();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;

                NodeList nameList = fstElmnt.getElementsByTagName("Id");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                String id = ((Node) nameList.item(0)).getNodeValue();

                NodeList websiteList = fstElmnt.getElementsByTagName("Name");
                Element websiteElement = (Element) websiteList.item(0);
                websiteList = websiteElement.getChildNodes();
                String name = ((Node) websiteList.item(0)).getNodeValue();

                NodeList modifyList = fstElmnt
                        .getElementsByTagName("CanModifyMobileNo");
                Element modifyElement = (Element) modifyList.item(0);
                modifyList = modifyElement.getChildNodes();
                String modify = ((Node) modifyList.item(0)).getNodeValue();


                NodeList nameListappdate = fstElmnt.getElementsByTagName("CheckOnApplicationDate");
                Element nameElementappdate = (Element) nameListappdate.item(0);
                nameListappdate = nameElementappdate.getChildNodes();
                String checkappondate = ((Node) nameListappdate.item(0)).getNodeValue();


                NodeList durationList = fstElmnt
                        .getElementsByTagName("Duration");
                Element durationElement = (Element) durationList.item(0);
                durationList = durationElement.getChildNodes();
                String duration = ((Node) durationList.item(0)).getNodeValue();
                String unit = "", unitAr = "";
                try {
                    NodeList unitList = fstElmnt.getElementsByTagName("Unit");
                    Element unitElement = (Element) unitList.item(0);
                    unitList = unitElement.getChildNodes();
                    unit = ((Node) unitList.item(0)).getNodeValue();
                } catch (Exception e) {

                }
                try {
                    NodeList unitArList = fstElmnt.getElementsByTagName("UnitAr");
                    Element unitArElement = (Element) unitArList.item(0);
                    unitArList = unitArElement.getChildNodes();
                    unitAr = ((Node) unitArList.item(0)).getNodeValue();
                } catch (Exception e) {

                }
                NodeList specialneedsduration = fstElmnt
                        .getElementsByTagName("SpecialNeedsDuration");
                Element specialneedsElement = (Element) specialneedsduration
                        .item(0);
                specialneedsduration = specialneedsElement.getChildNodes();
                String specialneeddurationvalue = ((Node) specialneedsduration
                        .item(0)).getNodeValue();
                String specialneedunitarvalue, specialneedunitvalue;
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
                            Integer.parseInt(id), name, idissue, modify,
                            duration, unit, unitAr, "Arabic",
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
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    class RetrieveOnlineStatusTask extends AsyncTask<Void, Void, BufferedReader> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.wtf("RetrieveOnlineStatusTask", "start");
            prog.setVisibility(View.VISIBLE);
        }

        @Override
        protected BufferedReader doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            URL url;
            BufferedReader bufferedReader = null;
            InputStreamReader in = null;
            HttpURLConnection urlConnection = null;
            try {
                //url = new URL("https://ifcfg.me/ip");
                url = new URL("http://ip-api.com/json");
                urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                in = new InputStreamReader(urlConnection.getInputStream());
                bufferedReader = new BufferedReader(in);
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                    buffer.append('\r');
                }
                bufferedReader.close();
                in.close();
                JSONObject json_data = new JSONObject(buffer.toString());
                MyApplication.ip = json_data.getString("query");
                MyApplication.isp = json_data.getString("isp");

                //ip = buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(BufferedReader bufferedReader) {
            super.onPostExecute(bufferedReader);

          //  prog.setVisibility(View.GONE);
        }
    }


    private class UpdateDeviceOnce extends AsyncTask<String, Void, Integer> {

        private String android_id;
        String currentDateandTime;
        String code = "1", notificationFlag = "", carrierName, mobileNumber = "";
        Profile profile = new Profile();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            currentDateandTime = sdf.format(new Date());
            PackageInfo pInfo;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                code = pInfo.versionCode + "";
            } catch (Exception e) {

                e.printStackTrace();
            }

            if (mshared.getBoolean(getString(R.string.notification_enabled), true)) {

                notificationFlag = "1";
            } else {

                notificationFlag = "0";
            }

            try{
            TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            carrierName = manager.getSimOperatorName();
            TCTDbAdapter sour = new TCTDbAdapter(SplashActivity.this);
            sour.open();
            ArrayList<Profile> arr = sour.getAllProfiles();
            profile = arr.get(0);
            sour.close();

            try {
                mobileNumber = profile.getnum();
            } catch (Exception e) {
                e.printStackTrace();
                mobileNumber = "";
            }}catch (Exception e){
                mobileNumber="";
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            String result = "";
            String url = "" + MyApplication.link + MyApplication.post + "RegisterDevice2";

            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("deviceType", "1");
            parameters.put("deviceToken", FirebaseInstanceId.getInstance().getToken());
            parameters.put("deviceModel", Actions.getDeviceName());
            parameters.put("modelName", Actions.getDeviceName());
            parameters.put("osVersion", Actions.getAndroidVersion());
            parameters.put("uniqueDeviceId", android_id);
            parameters.put("registrationDate", currentDateandTime);
            parameters.put("appVersion", code);
            parameters.put("notificationFlag", notificationFlag);
            parameters.put("mobileNumver", mobileNumber);
            parameters.put("timestamp", currentDateandTime);
            parameters.put("QoSEnabled", mshared.getBoolean(getString(R.string.enable_qos), false) ? "1" : "0");
            parameters.put("ServiceProvider", carrierName);

            Log.wtf("deviceToken", "is " + FirebaseInstanceId.getInstance().getToken());
            Log.wtf("deviceModel", "is " + Actions.getDeviceName());
            Log.wtf("osVersion", "is " + Actions.getAndroidVersion());
            Log.wtf("uniqueDeviceId", "is " + android_id);
            Log.wtf("registrationDate", "is " + currentDateandTime);
            Log.wtf("appVersion", "is " + code);
            Log.wtf("notificationFlag", "is " + notificationFlag);
            Log.wtf("mobileNumver", "is " + mobileNumber);
            Log.wtf("timestamptimestamp", "is " + currentDateandTime);
            Log.wtf("QoSEnabled", "is " + mshared.getBoolean(getString(R.string.enable_qos), false));
            Log.wtf("ServiceProvider", "is " + carrierName);

            try {
                result = Connection.POST(url, parameters);
                Log.wtf("result", "is " + result);
                String[] r1 = result.split("</");
                String[] r2 = r1[0].split(">");
                result = r2[2];
            } catch (Exception e) {
                e.printStackTrace();
                Log.wtf("result", "is " + e.getMessage());
                result = String.valueOf(mshSharedPreferences.getInt(getResources().getString(R.string.device_id), 0));
            }
            return Integer.parseInt(result);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.wtf("res", result + "");
            Log.wtf("UPDATEEEE", result + "");

            try {

                edit.putBoolean("once", false).apply();
                edit.putInt(getResources().getString(R.string.device_id), result).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class GetRoutineSchedule extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             prog.setVisibility(View.VISIBLE);
            edit.putString("link", MyApplication.link).apply();
            edit.putString("generalURL", MyApplication.general).apply();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                RoutineScheduleParser parser = new RoutineScheduleParser();
                Connection conn = new Connection(MyApplication.link + MyApplication.general + "GetRoutineSchedule?");
                MyApplication.routineSchedule = parser.parse(conn.getInputStream2());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prog.setVisibility(View.GONE);
            Log.wtf("splash", "routine");
            Log.wtf("Id", MyApplication.routineSchedule.getId() + "");
            Log.wtf("Frequency", MyApplication.routineSchedule.getFrequency() + "");
            Log.wtf("Enabled", MyApplication.routineSchedule.getEnabled() + "");
            Log.wtf("getStartingTime", MyApplication.routineSchedule.getStartingTime() + "");

            edit.putInt(getResources().getString(R.string.schedule_id), MyApplication.routineSchedule.getId()).apply();
            edit.putInt(getResources().getString(R.string.schedule_freq), MyApplication.routineSchedule.getFrequency()).apply();
            edit.putString(getResources().getString(R.string.schedule_enabled), MyApplication.routineSchedule.getEnabled()).apply();
            edit.putString(getResources().getString(R.string.schedule_start), MyApplication.routineSchedule.getStartingTime()).apply();

            Gson gson = new Gson();
            String json = gson.toJson(MyApplication.routineSchedule);
            edit.putString("RoutineSchedule", json).apply();
            //Signal Strength test
        }
    }

}