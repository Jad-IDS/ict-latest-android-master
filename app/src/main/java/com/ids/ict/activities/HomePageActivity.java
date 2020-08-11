package com.ids.ict.activities;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ClipDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.MyExceptionHandler;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.animation.HorizantalAnimatedImageView;
import com.ids.ict.classes.Area;
import com.ids.ict.classes.PeekThroughImageView;
import com.ids.ict.classes.QosTest;
import com.ids.ict.classes.SharedPreference;
import com.ids.ict.classes.SubArea;
import com.ids.ict.services.OnlineCheckJobService;
import com.ids.ict.services.QosFcmJobService;

import org.shipp.util.MenuEventController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import io.fabric.sdk.android.Fabric;

import static com.ids.ict.R.id.imgback;


public class HomePageActivity extends Activity {

    Connection conn;
    ;
    private PeekThroughImageView peak2, peak1;
    ImageView logo;
    ImageView imgbg;
    ImageView mainbar;
    SharedPreferences.Editor edit;
    SharedPreferences mshSharedPreferences;
    SharedPreferences mshared;
    private ImageView compImg, guidImg, networkImg, aboutImg, ntfImg, speedImg;
    LinearLayout footer;

    private final Context context = this;
    MyApplication app;
    String eventsSource = "", lang;
    RelativeLayout relativeLayout, linear;
    float newvalues[], values[] = {45, 45, 45, 90, 45, 45, 45};
    TCTDbAdapter datasource;
    ImageView logotxt;
    int footerButton;
    private boolean open = false, called = false, granted = false;
    String version;
    private ClipDrawable mImageDrawable;
    private ImageView bg;
    JobScheduler jobScheduler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fabric.with(this, new Crashlytics());

        lang = Actions.setLocal(this);
        setContentView(R.layout.activity_homepage);

        mshared = PreferenceManager.getDefaultSharedPreferences(this);
        mshSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        edit = mshSharedPreferences.edit();
        footer = (LinearLayout) findViewById(R.id.footer);
        mainbar = (ImageView) findViewById(R.id.mainbar);
        /*if (MyApplication.nightMod) {

            footer.setBackgroundResource(R.drawable.footer_nt);
        }*/

        /*TCTDbAdapter database = new TCTDbAdapter(getApplicationContext());
        database.open();
        database.deleteOfflineQosTests();*/

        ntfImg = (ImageView) findViewById(R.id.ntfImg);
        aboutImg = (ImageView) findViewById(R.id.aboutImg);

        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {

            setEnIcons();
        }else{

        }
        //Log.d("Refreshed token: ",    FirebaseInstanceId.getInstance().getToken());


        //<editor-fold desc="checking internet QOS">
        /*
        try{
            if (mshared.getBoolean(getString(R.string.enable_qos), false)) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    JobInfo jobInfo;

                    if (Build.VERSION.SDK_INT >= 24) { //Nougat, Oreo

                        //<editor-fold desc="job scheduler">
                        jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
                        ComponentName componentName = new ComponentName(getApplicationContext(), OnlineCheckJobService.class);
//                        ComponentName componentName = new ComponentName(getApplicationContext(), QosFcmJobService.class);
                        jobInfo = new JobInfo.Builder(1, componentName)
                                .setRequiresCharging(false)
                                .setRequiresDeviceIdle(false)
                                //.setPeriodic(10 * 1000)
                                .setMinimumLatency(MyApplication.SCHEDULER_SECONDS * 1000)
                                .build();
                        //</editor-fold>
                    }else{

                        //<editor-fold desc="job scheduler">
                        jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
                        ComponentName componentName = new ComponentName(getApplicationContext(), OnlineCheckJobService.class);
//                        ComponentName componentName = new ComponentName(getApplicationContext(), QosFcmJobService.class);
                        jobInfo = new JobInfo.Builder(1, componentName)
                                .setRequiresCharging(false)
                                .setRequiresDeviceIdle(false)
                                .setPeriodic(MyApplication.SCHEDULER_SECONDS * 1000)
                                .build();
                        //</editor-fold>
                    }

                    if (!isScheduledJobServiceOn()) {

                        Log.wtf("Qos got", "scheduled");
                        jobScheduler.schedule(jobInfo);
                    }else{

                        Log.wtf("Qos already", "scheduled");
                        jobScheduler.cancel(1);
                        Log.wtf("Qos re", "scheduled");
                        jobScheduler.schedule(jobInfo);
                    }
                }
            } else {

                Log.wtf("***** Qos not  ", "enabled");
                Log.d("***** Qos not  ", "enabled");
            }

        }catch (Exception e){
            Log.wtf("QOS Crash","e : " + e.getMessage());
        }
        */
        //</editor-fold>

        try {

            try {
                LanguageActivity.mActivity.finish();
            }
            catch (Exception e) { }

            edit.putBoolean("first", false).apply();

            String getoken = Actions.create_token_new();
            app = (MyApplication) getApplicationContext();
            eventsSource = "" + MyApplication.link + MyApplication.general + "GetIssueTypes?";
            //testing
            //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, SplashActivity.class));
            Fabric.with(this, new Crashlytics());

            this.linear = (RelativeLayout) findViewById(R.id.linear);
            newvalues = calculateData(values);

            speedImg = (ImageView) findViewById(R.id.speedImg);
            ntfImg = (ImageView) findViewById(R.id.ntfImg);
            aboutImg = (ImageView) findViewById(R.id.aboutImg);
            networkImg = (ImageView) findViewById(R.id.networkImg);
            guidImg = (ImageView) findViewById(R.id.guidImg);
            compImg = (ImageView) findViewById(R.id.compImg);


            speedImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, FaqActivity.class); //SpeedTestNewActivity
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);

                }
            });

            networkImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, MyLocationActivity.class);
                    context.startActivity(intent);
                }
            });

            aboutImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, AboutUsPagination.class);
                    intent.putExtra("from", "Main");
                    context.startActivity(intent);
                }
            });

            ntfImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, NotificationListActivity.class);
                    startActivity(intent);
                }
            });

            guidImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, ConsumerActivity.class);
                    context.startActivity(intent);
                }
            });

            compImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("idb", 1);
                    intent.putExtras(bundle);
                    MyApplication.lunched = 0;
                    intent.setClass(context, ComplaintActivity.class);
                    context.startActivity(intent);
                }
            });

            mainbar = (ImageView) findViewById(R.id.mainbar);
            //linear.addView(new MyGraphview(MainActivity.this, newvalues));
            footerButton = this.getIntent().getIntExtra("footerButton", R.id.home);
            relativeLayout = (RelativeLayout) findViewById(R.id.relative);
            imgbg = (ImageView) findViewById(R.id.imgBg);
            ImageView imgback = (ImageView) findViewById(R.id.imgback);

            if (MyApplication.nightMod) {
                imgback.setImageResource(R.drawable.bg_night);
                imgbg.setImageResource(R.drawable.bg_colors_night);
                footer.setBackgroundResource(R.drawable.footer_nt);
            } else {
                imgback.setImageResource(R.drawable.bg_day);
                imgbg.setImageResource(R.drawable.bg_color);
            }


            if (Actions.isNetworkAvailable(HomePageActivity.this)) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);


                String dateStamp = setdatestamp();
                TCTDbAdapter dtBase = new TCTDbAdapter(HomePageActivity.this);
                dtBase.open();
                String dbDate = dtBase.getdatestamp();
                dtBase.close();
                if (Actions.isNetworkAvailable(HomePageActivity.this) && !dateStamp.equals(dbDate)
                        && !dbDate.equals("")) {
                    datasource = new TCTDbAdapter(HomePageActivity.this);
                    datasource.open();
                    datasource.deleteArea();
                    datasource.deleteSubArea();
                    GetAreasAndSubAreas get = new GetAreasAndSubAreas();
                    get.execute();

                }

            }

            logotxt = (ImageView) findViewById(R.id.logotxt);


            logotxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            logotxt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            Log.wtf("IN", "TRY");

        } catch (Exception e) {
            String getoken = Actions.create_token_new();
            app = (MyApplication) getApplicationContext();
            eventsSource = "" + MyApplication.link + MyApplication.general + "GetIssueTypes?";
            //testing
            //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, SplashActivity.class));
            Fabric.with(this, new Crashlytics());

            this.linear = (RelativeLayout) findViewById(R.id.linear);
            newvalues = calculateData(values);

            speedImg = (ImageView) findViewById(R.id.speedImg);
            ntfImg = (ImageView) findViewById(R.id.ntfImg);
            aboutImg = (ImageView) findViewById(R.id.aboutImg);
            networkImg = (ImageView) findViewById(R.id.networkImg);
            guidImg = (ImageView) findViewById(R.id.guidImg);
            compImg = (ImageView) findViewById(R.id.compImg);


            speedImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, FaqActivity.class); //SpeedTestNewActivity
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);

                }
            });

            networkImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, MyLocationActivity.class);
                    context.startActivity(intent);
                }
            });

            aboutImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, AboutUsPagination.class);
                    intent.putExtra("from", "Main");
                    context.startActivity(intent);
                }
            });

            ntfImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, NotificationListActivity.class);
                    startActivity(intent);
                }
            });

            guidImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, ConsumerActivity.class);
                    context.startActivity(intent);
                }
            });

            compImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("idb", 1);
                    intent.putExtras(bundle);
                    MyApplication.lunched = 0;
                    intent.setClass(context, ComplaintActivity.class);
                    context.startActivity(intent);
                }
            });


            mainbar = (ImageView) findViewById(R.id.mainbar);
            //linear.addView(new MyGraphview(MainActivity.this, newvalues));
            footerButton = this.getIntent().getIntExtra("footerButton", R.id.home);
            relativeLayout = (RelativeLayout) findViewById(R.id.relative);
            imgbg = (ImageView) findViewById(R.id.imgBg);
            ImageView imgback = (ImageView) findViewById(R.id.imgback);

            if (MyApplication.nightMod) {
                imgback.setImageResource(R.drawable.bg_night);
                imgbg.setImageResource(R.drawable.bg_colors_night);
                footer.setBackgroundResource(R.drawable.footer_nt);
            } else {
                imgback.setImageResource(R.drawable.bg_day);
                imgbg.setImageResource(R.drawable.bg_color);
            }

            if (Actions.isNetworkAvailable(HomePageActivity.this)) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);

                String dateStamp = setdatestamp();
                TCTDbAdapter dtBase = new TCTDbAdapter(HomePageActivity.this);
                dtBase.open();
                String dbDate = dtBase.getdatestamp();
                dtBase.close();
                if (Actions.isNetworkAvailable(HomePageActivity.this) && !dateStamp.equals(dbDate)
                        && !dbDate.equals("")) {
                    datasource = new TCTDbAdapter(HomePageActivity.this);
                    datasource.open();
                    datasource.deleteArea();
                    datasource.deleteSubArea();
                    GetAreasAndSubAreas get = new GetAreasAndSubAreas();
                    get.execute();

                }

            }

            logotxt = (ImageView) findViewById(R.id.logotxt);
            logotxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            logotxt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });


            logo = (ImageView) findViewById(R.id.logo);
            version = Build.VERSION.RELEASE;

            if (!version.equals("6.0.1") || android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                // Call some material design APIs here
                logo.setPadding(0, getStatusBarHeight(), 0, 0);
                logotxt.setPadding(0, getStatusBarHeight(), 0, 0);
            }


            Log.wtf("IN", "CATCH");
        }


        //setLight();
    }


    private void checkPermissions() {

        /*if (mshared.getBoolean(getString(R.string.enable_qos), false)) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                Log.wtf("***** Qos    ", "enabled");
                granted = true;

                //<editor-fold desc="job scheduler">
                jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
                ComponentName componentName = new ComponentName(getApplicationContext(), OnlineCheckJobService.class);
                JobInfo jobInfo = null;
                jobInfo = new JobInfo.Builder(1, componentName)
                        .setRequiresCharging(false)
                        .setRequiresDeviceIdle(false)
                        .setPeriodic(10 * 1000)
                        .build();

                if (!isScheduledJobServiceOn())
                    jobScheduler.schedule(jobInfo);
                //</editor-fold>
            }

        } else {

            Log.wtf("***** Qos not  ", "enabled");
        }*/

    }


    private boolean isScheduledJobServiceOn() {

        boolean hasBeenScheduled = false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
                if (jobInfo.getId() == 1) {
                    hasBeenScheduled = true;
                    break;
                }
            }
        }

        return hasBeenScheduled;
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public void openCloseMenu(View view) {
        if (!this.open) {
            this.open = true;
            MenuEventController.open(this.context, this.linear);
            MenuEventController.closeKeyboard(this.context, view);
        } else {
            this.open = false;
            MenuEventController.close(this.context, this.linear);
            MenuEventController.closeKeyboard(this.context, view);
        }
    }


    public String setdatestamp() {

        String date = "";
        try {
            URL url = new URL(MyApplication.link + MyApplication.general
                    + "GetLastTextDateTimeUpdate?password=" + MyApplication.pass);
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


    public class GetAreasAndSubAreas extends AsyncTask<Void, Void, Void> {
        SharedPreferences prefsEn;
        SharedPreferences.Editor editEn;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            prefsEn = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);

            editEn = prefsEn.edit();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            URL url;
            try {

                url = new URL(MyApplication.link
                        + "GeneralServices.asmx/GetAreas?password="
                        + MyApplication.pass);
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("Area");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    ArrayList<SubArea> subs = new ArrayList<SubArea>();
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
                    String nameen = ((Node) websiteList.item(0)).getNodeValue();

                    NodeList isfortransList = fstElmnt
                            .getElementsByTagName("NameAr");
                    Element isfortransElement = (Element) isfortransList
                            .item(0);
                    isfortransList = isfortransElement.getChildNodes();
                    String namear = ((Node) isfortransList.item(0))
                            .getNodeValue();

                    NodeList subareas = fstElmnt
                            .getElementsByTagName("SubArea");
                    for (int j = 0; j < subareas.getLength(); j++) {
                        Node sub_node = subareas.item(j);
                        Element sub_fstElmnt = (Element) sub_node;

                        NodeList sub_nameList = sub_fstElmnt.getElementsByTagName("Id");
                        Element sub_nameElement = (Element) sub_nameList.item(0);
                        sub_nameList = sub_nameElement.getChildNodes();
                        String sub_id = ((Node) sub_nameList.item(0)).getNodeValue();


                        NodeList sub_nameList2 = sub_fstElmnt.getElementsByTagName("Name");
                        Element sub_nameElement2 = (Element) sub_nameList2.item(0);
                        sub_nameList2 = sub_nameElement2.getChildNodes();
                        String sub_name = ((Node) sub_nameList2.item(0)).getNodeValue();

                        NodeList sub_nameList3 = sub_fstElmnt.getElementsByTagName("NameAr");
                        Element sub_nameElement3 = (Element) sub_nameList3.item(0);
                        sub_nameList3 = sub_nameElement3.getChildNodes();
                        String sub_name_ar = ((Node) sub_nameList3.item(0)).getNodeValue();
                        SubArea sub = new SubArea();
                        sub.setNameEn(sub_name);
                        sub.setNameAr(sub_name_ar);
                        sub.setId(Integer.parseInt(sub_id));
                        sub.setParentId(Integer.parseInt(id));
                        subs.add(sub);
                        long a = datasource.insertSubArea(sub);

                    }

                    Area area = new Area();
                    area.setId(Integer.parseInt(id));
                    area.setNameEn(nameen);
                    area.setNameAr(namear);
                    area.setSubAreas(subs);
                    long b = datasource.insertArea(area);


                }
                datasource.close();
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
            TCTDbAdapter datasource = new TCTDbAdapter(HomePageActivity.this);
            datasource.open();
            ArrayList<Area> ares = datasource.GetAreas();
            ArrayList<SubArea> aaa = datasource.GetSubAreas("1");
        }
    }


    private float[] calculateData(float[] data) {
        float total = 0;
        for (int i = 0; i < data.length; i++) {
            total += data[i];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = 360 * (data[i] / total);
        }
        return data;

    }


    private void fadeOutAndHideImage(final ImageView img) {
        //if(!MyApplication.animated) {
        Animation fadeOut = new AlphaAnimation(0, 1);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(300);
        MyApplication.animated = true;
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.VISIBLE);


                peak2 = (PeekThroughImageView) findViewById(R.id.circle);
                peak2.start();
                peak1 = (PeekThroughImageView) findViewById(R.id.linesnew);
                peak1.start();
                //	raduis = peak.getRaduis();

                bg = (ImageView) findViewById(R.id.imgBg);
                ImageView imgback = (ImageView) findViewById(R.id.imgback);
                if (MyApplication.nightMod) {
                    imgback.setImageResource(R.drawable.bg_night);
                    bg.setImageResource(R.drawable.clip_source_night);
                } else {
                    imgback.setImageResource(R.drawable.bg_day);
                    bg.setImageResource(R.drawable.clip_source);
                }
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
//                lp.addRule(RelativeLayout.BELOW, R.id.mainbar);
//                lp.addRule(RelativeLayout.ABOVE, R.id.footer);
//              //  lp.setMargins(0, 0, 0, 0);
//                bg.setLayoutParams(lp);
                mImageDrawable = (ClipDrawable) bg.getDrawable();
                mImageDrawable.setLevel(0);
                mHandler.post(animateImage);

                // bg.start();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(fadeOut);
    }


    private int mLevel = 0;
    private Handler mHandler = new Handler();
    private Runnable animateImage = new Runnable() {

        @Override
        public void run() {
            bg.setVisibility(View.VISIBLE);
            mLevel += 600;
            mImageDrawable.setLevel(mLevel);
            if (mLevel <= 10000) {

                mHandler.postDelayed(animateImage, 60);
            } else {
                mHandler.removeCallbacks(animateImage);
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.lunched = 0;
        called = false;

        if (MyApplication.fromSettings) {
            logo = (ImageView) findViewById(R.id.logo);

            //logo.setPadding(0, getStatusBarHeight(), 0, 0);
            //logotxt.setPadding(0, getStatusBarHeight(), 0, 0);

            Log.wtf("FROM SETTINGS", "TRUE");
            //<editor-fold desc="fix layout when from settings">
            if (MyApplication.nightMod) {
                Log.wtf("FROM nightMod", "TRUE");

                footer = (LinearLayout) findViewById(R.id.footer);
                relativeLayout.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this, R.color.nightBlue));
                footer.setBackgroundResource(R.drawable.footer_nt);
                mainbar.setImageDrawable(ContextCompat.getDrawable(HomePageActivity.this, R.drawable.top_nt));
                ImageView networkImg = (ImageView) findViewById(R.id.networkImg);
                ImageView speedImg = (ImageView) findViewById(R.id.speedImg);
                ImageView ntfImg = (ImageView) findViewById(R.id.ntfImg);
                ImageView aboutImg = (ImageView) findViewById(R.id.aboutImg);
                ImageView compImg = (ImageView) findViewById(R.id.compImg);
                ImageView guidImg = (ImageView) findViewById(R.id.guidImg);
                ImageView logotxt = (ImageView) findViewById(R.id.logotxt);

                ImageView ImageViews[] = {networkImg, speedImg, ntfImg, aboutImg, compImg, guidImg};//, logotxt};
                int iconsNt[] = {R.drawable.network_nt_en, R.drawable.speed_test_nt_en,
                        R.drawable.notification_nt_en, R.drawable.about_us_nt_en, R.drawable.complaint_nt_en, R.drawable.guide_nt_en};//, R.drawable.logo_text_nt};


                for (int i = 0; i < ImageViews.length; i++) {
                    //ImageViews[i].setImageResource(iconsNt[i]);
                    ImageViews[i].setColorFilter(ContextCompat.getColor(HomePageActivity.this, R.color.gray_light));
                }


                Log.wtf("LaLa", "Land");
                ImageView imageback = (ImageView) findViewById(R.id.imgback);
                imgbg.setVisibility(View.GONE);
                imageback.setImageResource(R.drawable.bg_colors_night);
                imageback.setVisibility(View.VISIBLE);


                logo.setImageResource(R.drawable.logo_yellow);
                logotxt.setImageResource(R.drawable.logo_text_nt);



                if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    setEnIcons();
                } else {
                    setArIcons();
                }
            } else {

                footer = (LinearLayout) findViewById(R.id.footer);
                relativeLayout.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this, R.color.relative_color));
                footer.setBackgroundResource(R.drawable.footer);
                mainbar.setImageDrawable(ContextCompat.getDrawable(HomePageActivity.this, R.drawable.top_day));
                ImageView networkImg = (ImageView) findViewById(R.id.networkImg);
                ImageView speedImg = (ImageView) findViewById(R.id.speedImg);
                ImageView ntfImg = (ImageView) findViewById(R.id.ntfImg);
                ImageView aboutImg = (ImageView) findViewById(R.id.aboutImg);
                ImageView compImg = (ImageView) findViewById(R.id.compImg);
                ImageView guidImg = (ImageView) findViewById(R.id.guidImg);
                ImageView logotxt = (ImageView) findViewById(R.id.logotxt);


                ImageView ImageViews[] = {networkImg, speedImg, ntfImg, aboutImg, compImg, guidImg};//, logotxt};
                int iconsNt[] = {R.drawable.network_en, R.drawable.speed_test_en,
                        R.drawable.notification_en, R.drawable.about_us_en, R.drawable.complaint_en, R.drawable.guide_en};//, R.drawable.logo_text};


                for (int i = 0; i < ImageViews.length; i++) {
                    //ImageViews[i].setImageResource(iconsNt[i]);

                    ImageViews[i].setColorFilter(ContextCompat.getColor(HomePageActivity.this, R.color.bordo));
                }

                ImageView imageback2 = (ImageView) findViewById(R.id.imgback);
                ImageView bg3 = (ImageView) findViewById(R.id.imgBg);
                imgbg = (ImageView) findViewById(R.id.imgBg);
                imageback2.setImageResource(R.drawable.bg_color);
                bg3.setImageResource(R.drawable.bg_color);


                Log.wtf("LaLa", "Land");
                ImageView imageback = (ImageView) findViewById(imgback);
                imgbg.setVisibility(View.GONE);
                imageback.setImageResource(R.drawable.bg_color);
                imageback.setVisibility(View.VISIBLE);

                logo.setImageResource(R.drawable.logo_prpl);

                logotxt.setImageResource(R.drawable.logo_text);

                if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    setEnIcons();
                    Log.wtf("Language", "on resume en");
                } else {
                    Log.wtf("Language", "on resume ar");
                    setArIcons();
                }
            }
            //</editor-fold>
            MyApplication.fromSettings = false;

        } else {


            MyApplication.Lang = mshSharedPreferences.getString(getResources().getString(R.string.language_key), "");
            Log.wtf("ONRESUME", MyApplication.Lang);

            version = Build.VERSION.RELEASE;
            if (!version.equals("6.0.1") || android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                logo = (ImageView) findViewById(R.id.logo);
                if (mshSharedPreferences.getBoolean("first", true)) {

                    Log.wtf("ON RESUME", "FIRST");
                    logo.setPadding(0, getStatusBarHeight(), 0, 0);
                    logotxt.setPadding(0, getStatusBarHeight(), 0, 0);
                } else {
                    Log.wtf("ON RESUME", "SECOND");
                    if (mshSharedPreferences.getInt("val", 0) == 0) {
                        Log.wtf("val ", "msafer");
                        edit.putInt("val", 1).apply();

//                        int marginBottom = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MyApplication.screenHeight/200, getResources().getDisplayMetrics());
//                        Log.wtf("marginBottom ",""+marginBottom);

//                        HorizantalAnimatedImageView bg = (HorizantalAnimatedImageView) findViewById(R.id.imgBg);
//                        //ImageView imgback = (ImageView) findViewById(R.id.imgback);
//                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//                        lp.addRule(RelativeLayout.BELOW, R.id.mainbar);
//                        lp.setMargins(0, 0, 0, marginBottom);
//                        bg.setLayoutParams(lp);

                    } else {
                        if (mshSharedPreferences.getInt("val", 0) == 1) {
                            if (!MyApplication.test) {

                                logo.setPadding(0, getStatusBarHeight(), 0, 0);
                                logotxt.setPadding(0, getStatusBarHeight(), 0, 0);
                                edit.putInt("val", 1).apply();
                                Log.wtf("val", "1");
                            }
                        }
                    }
                }
            }


            logo = (ImageView) findViewById(R.id.logo);
            fadeOutAndHideImage(logo);


            if (MyApplication.nightMod) {
                footer = (LinearLayout) findViewById(R.id.footer);
                relativeLayout.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this, R.color.nightBlue));
                footer.setBackgroundResource(R.drawable.footer_nt);
                mainbar.setImageDrawable(ContextCompat.getDrawable(HomePageActivity.this, R.drawable.top_nt));
                logo.setImageResource(R.drawable.logo_yellow);

                logotxt.setImageResource(R.drawable.logo_text_nt);


            } else {
                footer = (LinearLayout) findViewById(R.id.footer);
                relativeLayout.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this, R.color.relative_color));
                footer.setBackgroundResource(R.drawable.footer);
                mainbar.setImageDrawable(ContextCompat.getDrawable(HomePageActivity.this, R.drawable.top_day));

                logo.setImageResource(R.drawable.logo_prpl);

                logotxt.setImageResource(R.drawable.logo_text);
            }


            if (MyApplication.Lang.length() == 0 ){
                MyApplication.Lang = lang;
            }
            if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                setEnIcons();
            } else {
                setArIcons();
            }

        }

    }


    private void addMarginsInDp(View view, int leftInDp, int topInDp, int rightInDp, int bottomInDp) {
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(convertDpToPx(leftInDp, dm), convertDpToPx(topInDp, dm), convertDpToPx(rightInDp, dm), convertDpToPx(bottomInDp, dm));
        view.setLayoutParams(lp);
    }


    private int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(pixels);
    }

//    private void setLight() {
//
//        if (MyApplication.nightMod2 == 1) {
//
//            ImageView imgback = (ImageView) findViewById(R.id.imgback);
//            HorizantalAnimatedImageView imgBg = (HorizantalAnimatedImageView) findViewById(R.id.imgBg);
//            imgBg.setVisibility(View.GONE);
//            imgback.setImageResource(R.drawable.bg_bg);
//            imgBg.setImageResource(R.drawable.bg_bg);
//
//
//            // MyApplication.nightMod2 = -1;
//        } else {
//
//            if (MyApplication.nightMod2 == 2) {
//
//                ImageView imgback = (ImageView) findViewById(R.id.imgback);
//                HorizantalAnimatedImageView imgBg = (HorizantalAnimatedImageView) findViewById(R.id.imgBg);
//                imgBg.setVisibility(View.GONE);
//                imgback.setVisibility(View.GONE);
//                imgback.setImageResource(R.drawable.bg_color2);
//                imgBg.setImageResource(R.drawable.bg_color2);
//
//                imgback.setVisibility(View.VISIBLE);
//
//            }
//        }
//
//
//        MyApplication.nightMod2 = -1;
//
//    }

    private void setEnIcons() {
        ImageView networkImg = (ImageView) findViewById(R.id.networkImg);
        ImageView speedImg = (ImageView) findViewById(R.id.speedImg);
        ImageView ntfImg = (ImageView) findViewById(R.id.ntfImg);
        ImageView aboutImg = (ImageView) findViewById(R.id.aboutImg);
        ImageView compImg = (ImageView) findViewById(R.id.compImg);
        ImageView guidImg = (ImageView) findViewById(R.id.guidImg);

        ImageView ImageViews[] = {networkImg, speedImg, ntfImg, aboutImg, compImg, guidImg};
        int iconsNt[] = {R.drawable.network_nt_en, R.drawable.n_faq_home_en, R.drawable.notification_nt_en, R.drawable.about_us_nt_en, R.drawable.complaint_nt_en, R.drawable.guide_nt_en,};
        int iconsDay[] = {R.drawable.network_en, R.drawable.faq_home_en, R.drawable.notification_en, R.drawable.about_us_en, R.drawable.complaint_en, R.drawable.guide_en,};
        if (MyApplication.nightMod) {
            for (int i = 0; i < ImageViews.length; i++) {
                ImageViews[i].setImageResource(iconsDay[i]);
                ImageViews[i].setColorFilter(ContextCompat.getColor(HomePageActivity.this, R.color.gray_light));
            }

        } else {
            for (int i = 0; i < ImageViews.length; i++) {
                ImageViews[i].setImageResource(iconsDay[i]);

                ImageViews[i].setColorFilter(ContextCompat.getColor(HomePageActivity.this, R.color.bordo));
            }
        }
    }


    private void setArIcons() {
        ImageView networkImg = (ImageView) findViewById(R.id.networkImg);
        ImageView speedImg = (ImageView) findViewById(R.id.speedImg);
        ImageView ntfImg = (ImageView) findViewById(R.id.ntfImg);
        ImageView aboutImg = (ImageView) findViewById(R.id.aboutImg);
        ImageView compImg = (ImageView) findViewById(R.id.compImg);
        ImageView guidImg = (ImageView) findViewById(R.id.guidImg);

        ImageView ImageViews[] = {networkImg, speedImg, ntfImg, aboutImg, compImg, guidImg};
        int iconsNt[] = {R.drawable.network_nt, R.drawable.n_faq_home, R.drawable.notification_nt, R.drawable.about_us_nt, R.drawable.complaint_nt, R.drawable.guide_nt,};
        int iconsDay[] = {R.drawable.network, R.drawable.faq_home, R.drawable.notification, R.drawable.about_us, R.drawable.complaint, R.drawable.guide,};
        if (MyApplication.nightMod) {
            for (int i = 0; i < ImageViews.length; i++) {

                ImageViews[i].setImageResource(iconsDay[i]);
                ImageViews[i].setColorFilter(ContextCompat.getColor(HomePageActivity.this, R.color.gray_light));
            }


        } else {
            for (int i = 0; i < ImageViews.length; i++) {
                ImageViews[i].setImageResource(iconsDay[i]);

                ImageViews[i].setColorFilter(ContextCompat.getColor(HomePageActivity.this, R.color.bordo));
            }
        }

    }


    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }


    public void backTo(View v) {
        Actions.backTo(this);
    }


    public void loadPage(int i) {

        Intent intent = new Intent();

        switch (i) {
            case 0: {
                intent.setClass(context, MyLocationActivity.class);
                context.startActivity(intent);
                break;
            }
            case 1: {
                intent.setClass(context, ConsumerActivity.class);
                context.startActivity(intent);
                break;
            }
            case 2: {
                Bundle bundle = new Bundle();
                bundle.putInt("idb", 1);
                intent.putExtras(bundle);
                MyApplication.lunched = 0;
                intent.setClass(context, ComplaintActivity.class);
                context.startActivity(intent);
                break;
            }

            case 3: {
                intent.setClass(context, FaqActivity.class); //SpeedTestNewActivity
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);

                break;
            }
            case 4: {

                intent.setClass(context, NotificationListActivity.class);
                startActivity(intent);
                break;

            }
            case 5: {
                intent.setClass(context, AboutUsPagination.class);
                intent.putExtra("from", "Main");
                //intent.setClass(context, AboutUsActivity.class);
                context.startActivity(intent);
                break;
            }

        }
    }


    public void footer(View v) {

        Intent intent = new Intent();
        ImageButton mButton = (ImageButton) v;
        lang = MyApplication.Lang;
        switch (v.getId()) {
            case R.id.morebtn: {
                if (lang.equals(MyApplication.ENGLISH)) {
                    intent.setClass(context, MoreActivity.class);
                } else {
                    intent.setClass(context, MoreActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("footerButton", R.id.morebtn);
                context.startActivity(intent);
                break;
            }
            case R.id.home: {

            }

        }
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
}
