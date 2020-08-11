package com.ids.ict.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.Connection;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.MyExceptionHandler;
import com.ids.ict.R;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.Profile;
import com.ids.ict.parser.RoutineScheduleParser;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.fabric.sdk.android.Fabric;

public class QosTermsActivity extends Activity {

    LinearLayout footer;
    RelativeLayout rel, mainbar, progressBarLayout;
    ProgressBar progressBar;
    ImageView backbtn;
    TextView title, tvTitle;
    WebView wvTerms;
    Button btDisagree, btAgree;
    SharedPreferences mshard;
    SharedPreferences.Editor edit;
    Intent intent;
    boolean fromAboutUs = false;
    String txt = "", lang = "";
    ArrayList<String> arrayOfTerms = new ArrayList<String>();
    ViewPager viewpager;
    MyPagerAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lang = Actions.setLocal(QosTermsActivity.this);
        setContentView(R.layout.activity_qos_terms);

        Actions.overrideFonts(QosTermsActivity.this);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Actions.setStatusBarColor(QosTermsActivity.this);
        }*/

        //testing
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, SplashActivity.class));
        Fabric.with(this, new Crashlytics());

        mshard = PreferenceManager.getDefaultSharedPreferences(QosTermsActivity.this);
        edit = mshard.edit();

        if (getIntent().hasExtra("fromAboutUs")) {
            fromAboutUs = getIntent().getExtras().getBoolean("fromAboutUs");
        }


        Log.wtf("QosTermsActivity", MyApplication.Lang);
        findViews();

        String lan = "";
        TCTDbAdapter datasource = new TCTDbAdapter(QosTermsActivity.this);
        datasource.open();
        ArrayList<Profile> profilesList = datasource.getAllProfiles();
        datasource.close();

        if (profilesList.size() > 0)
            lan = profilesList.get(0).getlang();
        if (lan.equals("")) {
            if (profilesList.size() > 1)
                lan = profilesList.get(1).getlang();
        }

        //checkPermissions();

        if (lan.length() > 0){
            MyApplication.Lang = lan;
            lang =  MyApplication.Lang;
        }

        try {
            tvTitle.setText(MyApplication.Lang.equals(MyApplication.ARABIC) ? getLookup("119").namear : getLookup("119").nameen);
        } catch (Exception e) {
            e.printStackTrace();
            Log.wtf("Exc", e.getMessage());
        }

        Log.wtf("MyApplication.Lang", "is "+MyApplication.Lang);
        Log.wtf("lang", "is "+lang);
        Log.wtf("lan", "is "+lan);

        try {
            if (MyApplication.Lang.equals(MyApplication.ARABIC)) { //Ar

                if (getLookup("115").namear.length() == 0){

                    GetLookUpAsynck get = new GetLookUpAsynck();
                    get.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{

                    arrayOfTerms.add(getLookup("115").namear + "<br/><br/>" + getLookup("116").namear + "<br/><br/>" + getLookup("117").namear);
                }

                btDisagree.setText("غير موافق");
                btAgree.setText("موافق");

            } else if (MyApplication.Lang.equals(MyApplication.ENGLISH)) { //En

                if (getLookup("115").nameen.length() == 0){

                    GetLookUpAsynck get = new GetLookUpAsynck();
                    get.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{

                    arrayOfTerms.add(getLookup("115").nameen + "<br/><br/>" + getLookup("116").nameen + "<br/><br/>" + getLookup("117").nameen);
                }

                btDisagree.setText("I Disagree");
                btAgree.setText("I Agree");

            }
            adapter.notifyDataSetChanged();
            Actions.LoadWebViewWithCustomFont2(txt, wvTerms);
        } catch (Exception e) {
            e.printStackTrace();
            Log.wtf("Exc", e.getMessage());
        }


        Log.wtf("dev", mshard.getInt(getResources().getString(R.string.device_id), 0) + " ss");
        btAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApplication.goToQosActivity = false;
                edit.putBoolean(getResources().getString(R.string.go_qos), MyApplication.goToQosActivity).apply();
                checkPermissions();


            }
        });

        btDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApplication.enableQOS = false;
                edit.putBoolean(getResources().getString(R.string.enable_qos), MyApplication.enableQOS).apply();

                MyApplication.goToQosActivity = false;
                edit.putBoolean(getResources().getString(R.string.go_qos), MyApplication.goToQosActivity).apply();

                if (!fromAboutUs)
                    intent = new Intent(QosTermsActivity.this, HomePageActivity.class);
                else {
                    //intent = new Intent(QosTermsActivity.this, RegisterActivity.class);
                    intent = new Intent(QosTermsActivity.this, LoginNewActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                QosTermsActivity.this.finish();

                new AddDevice().execute("");
            }
        });

        /*btDisagree.setText(getResources().getString(R.string.disagree_term));
        btAgree.setText(getResources().getString(R.string.agree_term));*/
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


    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(QosTermsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(QosTermsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                //ContextCompat.checkSelfPermission(QosTermsActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                //||
                ContextCompat.checkSelfPermission(QosTermsActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
            //||
            //ContextCompat.checkSelfPermission(QosTermsActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(QosTermsActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 1);

        } else { //permissions granted


            MyApplication.enableQOS = true;
            edit.putBoolean(getResources().getString(R.string.enable_qos), MyApplication.enableQOS).apply();
            new GetRoutineSchedule().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            if (!fromAboutUs)
                intent = new Intent(QosTermsActivity.this, HomePageActivity.class);
            else {
                //intent = new Intent(QosTermsActivity.this, RegisterActivity.class);
                intent = new Intent(QosTermsActivity.this, LoginNewActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            QosTermsActivity.this.finish();

            new AddDevice().execute("");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            Log.wtf("permissions", "length " + permissions.length);
            Log.wtf("grantResults", "length " + grantResults.length);

            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                //&& grantResults[3] == PackageManager.PERMISSION_GRANTED
                //&& grantResults[4] == PackageManager.PERMISSION_GRANTED
                    ) { //GRANTED
                MyApplication.enableQOS = true;
                edit.putBoolean(getResources().getString(R.string.enable_qos), MyApplication.enableQOS).apply();

                new GetRoutineSchedule().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                if (!fromAboutUs)
                    intent = new Intent(QosTermsActivity.this, HomePageActivity.class);
                else {
                 //   intent = new Intent(QosTermsActivity.this, RegisterActivity.class);
                    intent = new Intent(QosTermsActivity.this, LoginNewActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                QosTermsActivity.this.finish();

                new AddDevice().execute("");
            } else {

                MyApplication.enableQOS = false;
                edit.putBoolean(getResources().getString(R.string.enable_qos), MyApplication.enableQOS).apply();

                if (!fromAboutUs)
                    intent = new Intent(QosTermsActivity.this, HomePageActivity.class);
                else {
                    //   intent = new Intent(QosTermsActivity.this, RegisterActivity.class);
                    intent = new Intent(QosTermsActivity.this, LoginNewActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                QosTermsActivity.this.finish();

                new AddDevice().execute("");
            }
        }
    }


    private void findViews() {

        footer = (LinearLayout) findViewById(R.id.footer);
        rel = (RelativeLayout) findViewById(R.id.rel);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mainbar = (RelativeLayout) findViewById(R.id.mainbar);
        backbtn = (ImageView) mainbar.findViewById(R.id.backbtn);
        title = (TextView) mainbar.findViewById(R.id.title);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        wvTerms = (WebView) findViewById(R.id.wvTerms);
        btDisagree = (Button) findViewById(R.id.btDisagree);
        btAgree = (Button) findViewById(R.id.btAgree);
        viewpager = (ViewPager) findViewById(R.id.pager);

        adapter = new MyPagerAdapter(QosTermsActivity.this, arrayOfTerms);
        viewpager.setAdapter(adapter);

        backbtn.setVisibility(View.GONE);
        title.setVisibility(View.GONE);

        wvTerms.setVisibility(View.GONE);
        wvTerms.setBackgroundColor(0x00000000);
        wvTerms.getSettings().setJavaScriptEnabled(true);
        wvTerms.setScrollbarFadingEnabled(true);
        wvTerms.setVerticalScrollBarEnabled(true);
        wvTerms.setBackgroundColor(0x00000000);
        wvTerms.setHorizontalScrollBarEnabled(true);

        if(MyApplication.nightMod){
            mainbar.setBackgroundResource(R.drawable.footer_nt);

            btAgree.setBackground(ContextCompat.getDrawable(QosTermsActivity.this, R.drawable.button_gray));
            btDisagree.setBackground(ContextCompat.getDrawable(QosTermsActivity.this, R.drawable.button_gray));

            //footer.setBackgroundResource(R.drawable.footer_nt);
            rel.setBackgroundColor(getResources().getColor(R.color.nightBlue));

            title.setBackgroundColor(getResources().getColor(R.color.white));

            LinearLayout agreeLL = (LinearLayout) findViewById(R.id.agreeLL);
            agreeLL.setBackgroundColor(getResources().getColor(R.color.nightBlue));
        }

    }


    public class GetRoutineSchedule extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Connection conn = new Connection(MyApplication.link + MyApplication.general + "/GetRoutineSchedule?");


                RoutineScheduleParser parser = new RoutineScheduleParser();

                MyApplication.routineSchedule = parser.parse(conn.getInputStream2());


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.wtf("Id", MyApplication.routineSchedule.getId() + "");
            Log.wtf("Frequency", MyApplication.routineSchedule.getFrequency() + "");
            Log.wtf("Enabled", MyApplication.routineSchedule.getEnabled() + "");
            Log.wtf("getStartingTime", MyApplication.routineSchedule.getStartingTime() + "");

            //Signal Strength test
        }
    }


    private class MyPagerAdapter extends PagerAdapter {

        private Context mContext;
        ArrayList<String> array;

        public MyPagerAdapter(Context context, ArrayList<String> arraystring) {
            mContext = context;
            array = arraystring;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, final int position) {
            String modelObject = array.get(position);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.aboutus_item, collection, false);
            collection.addView(layout);
            WebView txt = (WebView) layout.findViewById(R.id.terms);
            String fontName = "GE_Dinar_One_Medium.otf";
            TextView see = (TextView) layout.findViewById(R.id.see);

            see.setVisibility(View.GONE);

            if (MyApplication.nightMod) {
                see.setTextColor(ContextCompat.getColor(QosTermsActivity.this, R.color.darkBlue));
            }

            if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                fontName = "file:///android_asset/fonts/Galaxie_Polaris_Medium.otf";
                // see.setGravity(Gravity.LEFT);
                see.setTypeface(MyApplication.facePolarisMedium);
            } else {
                fontName = "file:///android_asset/fonts/GE_Dinar_One_Medium.otf";
                // see.setGravity(Gravity.RIGHT);
                see.setTypeface(MyApplication.faceDinar);
            }

            if (MyApplication.Lang.equals(MyApplication.ARABIC)) {
                txt.getSettings().setJavaScriptEnabled(true);

                txt.loadDataWithBaseURL("file:///android_asset/", "<head>" +
                        "<style type=\"text/css\">@font-face {font-family: MyFont;\n" +
                        "    src: url(\"" + fontName + "\")\n" +
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
                        "</style><body dir=\"rtl\">" + modelObject + "</body></html>", "text/html", "UTF-8", "");
            } else {
                txt.getSettings().setJavaScriptEnabled(true);

                txt.loadDataWithBaseURL("file:///android_asset/", "<head><style type=\"text/css\">@font-face {font-family: MyFont;\n" +
                        "    src: url(\"" + fontName + "\")\n" +
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
                        "</style><body dir=\"ltr\">" + modelObject + "</body></html>", "text/html", "UTF-8", "");
            }
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }


    private class AddDevice extends AsyncTask<String, Void, Integer> {

        private String android_id;
        String currentDateandTime;
        String code = "1", notificationFlag = "", carrierName;

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

            if (mshard.getBoolean(getString(R.string.notification_enabled), true)) {

                notificationFlag = "1";
            } else {

                notificationFlag = "0";
            }

            TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            carrierName = manager.getSimOperatorName();
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
            parameters.put("mobileNumver", "");
            parameters.put("timestamp", currentDateandTime);
            parameters.put("QoSEnabled", mshard.getBoolean(getString(R.string.enable_qos), false) ? "1" : "0");
            parameters.put("ServiceProvider", carrierName);

            Log.wtf("deviceToken", "is " + FirebaseInstanceId.getInstance().getToken());
            Log.wtf("deviceModel", "is " + Actions.getDeviceName());
            Log.wtf("osVersion", "is " + Actions.getAndroidVersion());
            Log.wtf("uniqueDeviceId", "is " + android_id);
            Log.wtf("registrationDate", "is " + currentDateandTime);
            Log.wtf("appVersion", "is " + code);
            Log.wtf("notificationFlag", "is " + notificationFlag);
            Log.wtf("mobileNumver", "is " + "");
            Log.wtf("timestamptimestamp", "is " + currentDateandTime);
            Log.wtf("QoSEnabled",   mshard.getBoolean(getString(R.string.enable_qos), false) ? "1" : "0");
            Log.wtf("ServiceProvider", "is " + carrierName);

            result = Connection.POST(url, parameters);
            Log.wtf("AddDevice","result : " + result);

            String[] r1 = result.split("</");
            String[] r2 = r1[0].split(">");
            try {
                result = r2[2];
            } catch (Exception e) {
                e.printStackTrace();
                result = "0";
            }
            return Integer.parseInt(result);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.wtf("RegisterDevice2 res", result + "");

            try {

                edit.putInt(getResources().getString(R.string.device_id), result).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class GetLookUpAsynck extends AsyncTask<Boolean, Void, Void> {
        SharedPreferences prefsEn;
        SharedPreferences.Editor editEn;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            prefsEn = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);

            editEn = prefsEn.edit();

            /*progress = ProgressDialog.show(LanguageActivity.this, "", "", true);
            progress.setCanceledOnTouchOutside(false);*/

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Boolean... params) {
            // TODO Auto-generated method stub
            URL url;

            try {
                url = new URL(MyApplication.link + "GeneralServices.asmx/GetLookups?code=&token=" + Actions.create_token_new());

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

                    String namear = "";
                    NodeList isfortransList = fstElmnt
                            .getElementsByTagName("NameAr");
                    Element isfortransElement = (Element) isfortransList
                            .item(0);
                    isfortransList = isfortransElement.getChildNodes();

                    try {
                        namear = ((Node) isfortransList.item(0))
                                .getNodeValue();
                    } catch (Exception e) {
                        namear = "";
                    }
                    NodeList isfortransList2 = fstElmnt
                            .getElementsByTagName("Order");
                    Element isfortransElement2 = (Element) isfortransList2
                            .item(0);
                    isfortransList2 = isfortransElement2.getChildNodes();
                    String order = ((Node) isfortransList2.item(0))
                            .getNodeValue();

                    NodeList isfortransList3 = fstElmnt
                            .getElementsByTagName("Id");
                    Element isfortransElemen3 = (Element) isfortransList3
                            .item(0);
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
                    editEn.putString(id, json);
                    editEn.putString(code, json);
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

            try {
                if (MyApplication.Lang.equals(MyApplication.ARABIC)) { //Ar

                   arrayOfTerms.add(getLookup("115").namear + "<br/><br/>" + getLookup("116").namear + "<br/><br/>" + getLookup("117").namear);



                } else if (MyApplication.Lang.equals(MyApplication.ENGLISH)) { //En

                    arrayOfTerms.add(getLookup("115").nameen + "<br/><br/>" + getLookup("116").nameen + "<br/><br/>" + getLookup("117").nameen);

                }
                adapter.notifyDataSetChanged();
                Actions.LoadWebViewWithCustomFont2(txt, wvTerms);
            } catch (Exception e) {
                e.printStackTrace();
                Log.wtf("Exc", e.getMessage());
            }

        }
    }

}
