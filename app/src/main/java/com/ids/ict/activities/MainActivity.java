package com.ids.ict.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.MyExceptionHandler;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.animation.HorizantalAnimatedImageView;
import com.ids.ict.animation.SimpleDrawingView;
import com.ids.ict.classes.Area;
import com.ids.ict.classes.PeekThroughImageView;
import com.ids.ict.classes.SubArea;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity {

    Connection conn;

    private final Context context = this;
    MyApplication app;
    String eventsSource = "", lang;
    RelativeLayout relativeLayout, linear;
    LinearLayout footer;
    ImageView imgbg;
    ImageView mainbar;
    private boolean open = false;
    int footerButton, hour;
    float newvalues[], values[] = {45, 45, 45, 90, 45, 45, 45};
    TCTDbAdapter datasource;
    ImageView logotxt;
    float raduis;
    boolean called = false, themeChanged = false;
    ImageView logo;
    static SimpleDrawingView draw;
    private float centerX, centerY;

    SharedPreferences.Editor edit;
    SharedPreferences mshSharedPreferences;
    SharedPreferences mshared;
    private ImageView compImg, guidImg, networkImg, aboutImg, ntfImg, speedImg;
    private LinearLayout llSpeed, llNetwork;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = Actions.setLocal(this);

        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        mshared = PreferenceManager.getDefaultSharedPreferences(this);
        mshSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        edit = mshSharedPreferences.edit();

        if (MyApplication.nightMod) {
            setContentView(R.layout.servicephone_night);
            footer = (LinearLayout) findViewById(R.id.footer);
            footer.setBackgroundResource(R.drawable.footer_nt);
        } else {
            setContentView(R.layout.servicephone_day);
        }

        ntfImg = (ImageView) findViewById(R.id.ntfImg);
        aboutImg = (ImageView) findViewById(R.id.aboutImg);

        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            setEnIcons();
        }

        try {
            LanguageActivity.mActivity.finish();



            edit.putBoolean("first", false).apply();
            String getoken = Actions.create_token_new();
            app = (MyApplication) getApplicationContext();
            eventsSource = "" + MyApplication.link + MyApplication.general + "GetIssueTypes?";
            //testing
            //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, SplashActivity.class));
            Fabric.with(this, new Crashlytics());

            this.linear = (RelativeLayout) findViewById(R.id.linear);
            newvalues = calculateData(values);

            llSpeed = (LinearLayout) findViewById(R.id.llSpeed);
            llNetwork = (LinearLayout) findViewById(R.id.llNetwork);
            speedImg = (ImageView) findViewById(R.id.speedImg);
            ntfImg = (ImageView) findViewById(R.id.ntfImg);
            aboutImg = (ImageView) findViewById(R.id.aboutImg);
            networkImg = (ImageView) findViewById(R.id.networkImg);
            guidImg = (ImageView) findViewById(R.id.guidImg);
            compImg = (ImageView) findViewById(R.id.compImg);


            llSpeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, SpeedTestNewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);

                }
            });

            llNetwork.setOnClickListener(new View.OnClickListener() {
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
            logo = (ImageView) findViewById(R.id.logo);

            fadeOutAndHideImage(logo);
            if (Actions.isNetworkAvailable(MainActivity.this)) {
                String dateStamp = setdatestamp();
                TCTDbAdapter dtBase = new TCTDbAdapter(MainActivity.this);
                dtBase.open();
                String dbDate = dtBase.getdatestamp();
                dtBase.close();
                if (Actions.isNetworkAvailable(MainActivity.this) && !dateStamp.equals(dbDate)
                        && !dbDate.equals("")) {
                    datasource = new TCTDbAdapter(MainActivity.this);
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
        }

        catch (Exception e) {
            String getoken = Actions.create_token_new();
            app = (MyApplication) getApplicationContext();
            eventsSource = "" + MyApplication.link + MyApplication.general + "GetIssueTypes?";
            //testing
            //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, SplashActivity.class));
            Fabric.with(this, new Crashlytics());

            this.linear = (RelativeLayout) findViewById(R.id.linear);
            newvalues = calculateData(values);

            llSpeed = (LinearLayout) findViewById(R.id.llSpeed);
            llNetwork = (LinearLayout) findViewById(R.id.llNetwork);
            speedImg = (ImageView) findViewById(R.id.speedImg);
            ntfImg = (ImageView) findViewById(R.id.ntfImg);
            aboutImg = (ImageView) findViewById(R.id.aboutImg);
            networkImg = (ImageView) findViewById(R.id.networkImg);
            guidImg = (ImageView) findViewById(R.id.guidImg);
            compImg = (ImageView) findViewById(R.id.compImg);


            llSpeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, SpeedTestNewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);

                }
            });

            llNetwork.setOnClickListener(new View.OnClickListener() {
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
            logo = (ImageView) findViewById(R.id.logo);

            fadeOutAndHideImage(logo);
            if (Actions.isNetworkAvailable(MainActivity.this)) {
                String dateStamp = setdatestamp();
                TCTDbAdapter dtBase = new TCTDbAdapter(MainActivity.this);
                dtBase.open();
                String dbDate = dtBase.getdatestamp();
                dtBase.close();
                if (Actions.isNetworkAvailable(MainActivity.this) && !dateStamp.equals(dbDate)
                        && !dbDate.equals("")) {
                    datasource = new TCTDbAdapter(MainActivity.this);
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


        }

        /*if (mshSharedPreferences.getBoolean("first", true)) {

            edit.putBoolean("first", false).apply();
            String getoken = Actions.create_token_new();
            app = (MyApplication) getApplicationContext();
            eventsSource = "" + MyApplication.link + MyApplication.general + "GetIssueTypes?";
            Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this,
                    SplashActivity.class));

            this.linear = (RelativeLayout) findViewById(R.id.linear);
            newvalues = calculateData(values);

            llSpeed = (LinearLayout) findViewById(R.id.llSpeed);
            llNetwork = (LinearLayout) findViewById(R.id.llNetwork);
            speedImg = (ImageView) findViewById(R.id.speedImg);
            ntfImg = (ImageView) findViewById(R.id.ntfImg);
            aboutImg = (ImageView) findViewById(R.id.aboutImg);
            networkImg = (ImageView) findViewById(R.id.networkImg);
            guidImg = (ImageView) findViewById(R.id.guidImg);
            compImg = (ImageView) findViewById(R.id.compImg);


            llSpeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, SpeedTestNewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);

                }
            });

            llNetwork.setOnClickListener(new View.OnClickListener() {
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
            logo = (ImageView) findViewById(R.id.logo);

            fadeOutAndHideImage(logo);
            if (Actions.isNetworkAvailable(MainActivity.this)) {
                String dateStamp = setdatestamp();
                TCTDbAdapter dtBase = new TCTDbAdapter(MainActivity.this);
                dtBase.open();
                String dbDate = dtBase.getdatestamp();
                dtBase.close();
                if (Actions.isNetworkAvailable(MainActivity.this) && !dateStamp.equals(dbDate)
                        && !dbDate.equals("")) {
                    datasource = new TCTDbAdapter(MainActivity.this);
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
        }
        /**/


    }

    public void aaa(View v) {

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
            TCTDbAdapter datasource = new TCTDbAdapter(MainActivity.this);
            datasource.open();
            ArrayList<Area> ares = datasource.GetAreas();
            ArrayList<SubArea> aaa = datasource.GetSubAreas("1");
        }
    }

    private void setEnIcons() {
        ImageView networkImg = (ImageView) findViewById(R.id.networkImg);
        ImageView speedImg = (ImageView) findViewById(R.id.speedImg);
        ImageView ntfImg = (ImageView) findViewById(R.id.ntfImg);
        ImageView aboutImg = (ImageView) findViewById(R.id.aboutImg);
        ImageView compImg = (ImageView) findViewById(R.id.compImg);
        ImageView guidImg = (ImageView) findViewById(R.id.guidImg);

        ImageView ImageViews[] = {networkImg, speedImg, ntfImg, aboutImg, compImg, guidImg};
        int iconsNt[] = {R.drawable.network_nt_en, R.drawable.speed_test_nt_en, R.drawable.notification_nt_en, R.drawable.about_us_nt_en, R.drawable.complaint_nt_en, R.drawable.guide_nt_en,};
        int iconsDay[] = {R.drawable.network_en, R.drawable.speed_test_en, R.drawable.notification_en, R.drawable.about_us_en, R.drawable.complaint_en, R.drawable.guide_en,};
        if (MyApplication.nightMod) {
            for (int i = 0; i < ImageViews.length; i++) {
                ImageViews[i].setImageResource(iconsNt[i]);
            }

        } else {
            for (int i = 0; i < ImageViews.length; i++) {
                ImageViews[i].setImageResource(iconsDay[i]);
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
        int iconsNt[] = {R.drawable.network_nt, R.drawable.speed_test_nt, R.drawable.notification_nt, R.drawable.about_us_nt, R.drawable.complaint_nt, R.drawable.guide_nt,};
        int iconsDay[] = {R.drawable.network, R.drawable.speed_test, R.drawable.notification, R.drawable.about_us, R.drawable.complaint, R.drawable.guide,};
        if (MyApplication.nightMod) {
            for (int i = 0; i < ImageViews.length; i++) {
                ImageViews[i].setImageResource(iconsNt[i]);
            }

        } else {
            for (int i = 0; i < ImageViews.length; i++) {
                ImageViews[i].setImageResource(iconsDay[i]);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.lunched = 0;
        called = false;

        /*if(getIntent().hasExtra("fromSettings")){

            Log.wtf("FROM SETTINGS","TRUE");

            //<editor-fold desc="fix layout when from settings">
            if (MyApplication.nightMod) {
                ///   setContentView(R.layout.servicephone_night);
                footer = (LinearLayout) findViewById(R.id.footer);
                relativeLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.nightBlue));
                footer.setBackgroundResource(R.drawable.footer_nt);
                mainbar.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.top_nt));
                ImageView networkImg = (ImageView) findViewById(R.id.networkImg);
                ImageView speedImg = (ImageView) findViewById(R.id.speedImg);
                ImageView ntfImg = (ImageView) findViewById(R.id.ntfImg);
                ImageView aboutImg = (ImageView) findViewById(R.id.aboutImg);
                ImageView compImg = (ImageView) findViewById(R.id.compImg);
                ImageView guidImg = (ImageView) findViewById(R.id.guidImg);

                ImageView ImageViews[] = {networkImg, speedImg, ntfImg, aboutImg, compImg, guidImg};
                int iconsNt[] = {R.drawable.network_nt_en, R.drawable.speed_test_nt_en, R.drawable.notification_nt_en, R.drawable.about_us_nt_en, R.drawable.complaint_nt_en, R.drawable.guide_nt_en,};


                for (int i = 0; i < ImageViews.length; i++) {
                    ImageViews[i].setImageResource(iconsNt[i]);
                }
                ImageView imageback = (ImageView)findViewById(R.id.imgback);

                // imgbg.setImageResource(R.drawable.bg_colors_night);
                // imageback.setImageResource(R.drawable.bg_night);
                imgbg.setVisibility(View.GONE);
                imageback.setVisibility(View.GONE);
                HorizantalAnimatedImageView  imgBgcolor =(HorizantalAnimatedImageView)findViewById(R.id.imgBgcolor);
                imgBgcolor.setVisibility(View.VISIBLE);
                imgBgcolor.start();
                logo.setImageResource(R.drawable.logo_yellow);

            } else {
                // setContentView(R.layout.servicephone_day);

            }
            //</editor-fold>

        }*/

        if(MyApplication.fromSettings){
            Log.wtf("FROM SETTINGS","TRUE");
            //<editor-fold desc="fix layout when from settings">
            if (MyApplication.nightMod) {

                footer = (LinearLayout) findViewById(R.id.footer);
                relativeLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.nightBlue));
                footer.setBackgroundResource(R.drawable.footer_nt);
                mainbar.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.top_nt));
                ImageView networkImg = (ImageView) findViewById(R.id.networkImg);
                ImageView speedImg = (ImageView) findViewById(R.id.speedImg);
                ImageView ntfImg = (ImageView) findViewById(R.id.ntfImg);
                ImageView aboutImg = (ImageView) findViewById(R.id.aboutImg);
                ImageView compImg = (ImageView) findViewById(R.id.compImg);
                ImageView guidImg = (ImageView) findViewById(R.id.guidImg);
                ImageView logotxt = (ImageView) findViewById(R.id.logotxt);

                ImageView ImageViews[] = {networkImg, speedImg, ntfImg, aboutImg, compImg, guidImg, logotxt};
                int iconsNt[] = {R.drawable.network_nt_en, R.drawable.speed_test_nt_en,
                        R.drawable.notification_nt_en, R.drawable.about_us_nt_en, R.drawable.complaint_nt_en, R.drawable.guide_nt_en, R.drawable.logo_text_nt};


                for (int i = 0; i < ImageViews.length; i++) {
                    ImageViews[i].setImageResource(iconsNt[i]);
                }
                ImageView imageback = (ImageView)findViewById(R.id.imgback);

                // imgbg.setImageResource(R.drawable.bg_colors_night);

                imgbg.setVisibility(View.GONE);
                imageback.setImageResource(R.drawable.bg_colors_night);
                imageback.setVisibility(View.VISIBLE);

                logo.setImageResource(R.drawable.logo_yellow);


                if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    setEnIcons();
                }else{
                    setArIcons();
                }
            } else {
                // setContentView(R.layout.servicephone_day);



                footer = (LinearLayout) findViewById(R.id.footer);
                relativeLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.relative_color));
                footer.setBackgroundResource(R.drawable.footer);
                mainbar.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.top_day));
                ImageView networkImg = (ImageView) findViewById(R.id.networkImg);
                ImageView speedImg = (ImageView) findViewById(R.id.speedImg);
                ImageView ntfImg = (ImageView) findViewById(R.id.ntfImg);
                ImageView aboutImg = (ImageView) findViewById(R.id.aboutImg);
                ImageView compImg = (ImageView) findViewById(R.id.compImg);
                ImageView guidImg = (ImageView) findViewById(R.id.guidImg);
                ImageView logotxt = (ImageView) findViewById(R.id.logotxt);



                ImageView ImageViews[] = {networkImg, speedImg, ntfImg, aboutImg, compImg, guidImg, logotxt};
                int iconsNt[] = { R.drawable.network_en, R.drawable.speed_test_en,
                        R.drawable.notification_en, R.drawable.about_us_en, R.drawable.complaint_en, R.drawable.guide_en, R.drawable.logo_text};


                for (int i = 0; i < ImageViews.length; i++) {
                    ImageViews[i].setImageResource(iconsNt[i]);
                }


                ImageView imageback = (ImageView)findViewById(R.id.imgback);
                /*ImageView imgBg = (ImageView) findViewById(R.id.imgBg);

                imgbg.setImageResource(R.drawable.bg_colors_night);*/
                imgbg.setVisibility(View.GONE);
                imageback.setImageResource(R.drawable.bg_color);
                imageback.setVisibility(View.VISIBLE);



                logo.setImageResource(R.drawable.logo_prpl);

                if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    setEnIcons();
                }else{
                    setArIcons();
                }
            }
            //</editor-fold>
            MyApplication.fromSettings=false;






            if (MyApplication.Lang.equals(MyApplication.ARABIC)) {

                try {
                    ntfImg = (ImageView) findViewById(R.id.ntfImg);
                    aboutImg = (ImageView) findViewById(R.id.aboutImg);

                    ntfImg.getLayoutParams().height = (int) getResources().getDimension(R.dimen.img_height);
                    ntfImg.getLayoutParams().width = (int) getResources().getDimension(R.dimen.img_width);

                    aboutImg.getLayoutParams().height = (int) getResources().getDimension(R.dimen.img_height);
                    aboutImg.getLayoutParams().width = (int) getResources().getDimension(R.dimen.img_width);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else{

                try {
                    ntfImg = (ImageView) findViewById(R.id.ntfImg);
                    aboutImg = (ImageView) findViewById(R.id.aboutImg);

                    ntfImg.getLayoutParams().height = (int) getResources().getDimension(R.dimen.img_height2);
                    ntfImg.getLayoutParams().width = (int) getResources().getDimension(R.dimen.img_width2);

                    aboutImg.getLayoutParams().height = (int) getResources().getDimension(R.dimen.img_height2);
                    aboutImg.getLayoutParams().width = (int) getResources().getDimension(R.dimen.img_width2);
                }catch (Exception e){
                    e.printStackTrace();
                }

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
                intent.setClass(context, SpeedTestNewActivity.class);
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

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
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

    public class MyGraphview extends View {
        //	boolean called = false;
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float[] value_degree;
        //		 private int[] COLORS = { Color.BLUE, Color.TRANSPARENT, Color.YELLOW,
//		 Color.TRANSPARENT, Color.BLACK, Color.TRANSPARENT, Color.BLUE };
        RectF rectf = new RectF(-480, -250, 1920, 2150);
        //		RectF rectf = new RectF(0, 0, MyApplication.screenWidth, (float) (MyApplication.screenHeight*.8));
        int temp = 0;

        public MyGraphview(Context context, float[] values) {

            super(context);
            value_degree = new float[values.length];
            // System.out.println("values"+value_degree);
            for (int i = 0; i < values.length; i++) {
                value_degree[i] = values[i];
                System.out.println("degree" + value_degree[i]);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

//			for (int i = 0; i < value_degree.length; i++) {// values2.length;
//				// i++) {
//				if (i == 0) {
//					paint.setColor(Color.TRANSPARENT);
//					canvas.drawArc(rectf, 0, value_degree[i], true, paint);
//				} else {
//					temp += (int) value_degree[i - 1];
//					paint.setColor(Color.TRANSPARENT);
//					canvas.drawArc(rectf, temp, value_degree[i], true, paint);
//				}
//			}


            /*Log.wtf("canvas ClipBounds", "" + canvas.getClipBounds().toString());
            Log.wtf("canvas ClipBounds", "" + canvas.getClipBounds().flattenToString());
            Log.wtf("canvas ClipBounds", "" + canvas.getClipBounds().toShortString());
            Log.wtf("canvas CenterX", "" + canvas.getClipBounds().exactCenterX());
            Log.wtf("canvas CenterY", "" + canvas.getClipBounds().exactCenterY());*/

            centerX = canvas.getClipBounds().exactCenterX();
            centerY = canvas.getClipBounds().exactCenterY();
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {

            if (!called) {
                int action = event.getAction();
//			float relX = event.getX() - 720;
//			float relY = event.getY() - 945;
                float x = event.getX();
                float y = event.getY();

                Log.wtf("Abscissa X", "" + x);
                Log.wtf("Ordinate Y", "" + y);


                if (x < 200 || x > 350 || y > 460 || y < 330) {
                    float relX = event.getX() - MyApplication.screenWidth / 2;
                    float relY = event.getY() - MyApplication.screenHeight / 2;

                    switch (action) {

                        case MotionEvent.ACTION_DOWN:
//						if    (( x < MyApplication.screenWidth / 2 && x > MyApplication.screenWidth / 2 - raduis && y < MyApplication.screenHeight / 2 && y > MyApplication.screenHeight / 2 - raduis)
//								||
//								(x > MyApplication.screenWidth / 2 && x < MyApplication.screenWidth / 2 + raduis && y < MyApplication.screenHeight / 2 && y > MyApplication.screenHeight / 2 - raduis)
//								||
//								(x > MyApplication.screenWidth / 2 && x < MyApplication.screenWidth / 2 + raduis && y > MyApplication.screenHeight / 2 && y < MyApplication.screenHeight / 2 + raduis)
//								||
//								(x < MyApplication.screenWidth / 2 && x > MyApplication.screenWidth / 2 - raduis && y > MyApplication.screenHeight / 2 && y < MyApplication.screenHeight / 2 + raduis)
//
//								) {
//
//						} else {
                            float angleInRad = (float) Math.atan2(relY, -relX);

                            int degree = (int) ((angleInRad + Math.PI) * 180 / Math.PI);

                            float temp = 0;
                            for (int j = 0; j < values.length; j++) {
                                float temp1 = temp + values[j];
                                if (degree > temp && degree < temp1) {
                                    if (j == 6)
                                        j = 0;
                                    //loadPage(j);
                                    break;
                                } else {
                                    temp += values[j];
                                }
                            }
                            //}
                            break;

                    }
                    called = true;
                }
            }
            return true;
        }

    }

    private void fadeOutAndHideImage(final ImageView img) {
        //if(!MyApplication.animated) {
        Animation fadeOut = new AlphaAnimation(0, 1);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);
        MyApplication.animated = true;
        ;
        fadeOut.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.VISIBLE);


                if (MyApplication.nightMod) {
                    draw = new SimpleDrawingView(MainActivity.this);
                } else {
                    int color = Color.parseColor("#B2B2B3");
                    draw = new SimpleDrawingView(MainActivity.this,
                            color);
                }
                relativeLayout.addView(draw);
                relativeLayout.invalidate();
                PeekThroughImageView peak = new PeekThroughImageView(
                        MainActivity.this);
                peak = (PeekThroughImageView) findViewById(R.id.lines);
                peak.start();
                //	raduis = peak.getRaduis();
                HorizantalAnimatedImageView bg = new HorizantalAnimatedImageView(
                        MainActivity.this);
                bg = (HorizantalAnimatedImageView) findViewById(R.id.imgBg);
                bg.start();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(fadeOut);
//		}
//		else {
//			img.setVisibility(View.VISIBLE);
//			PeekThroughImageView	peak = (PeekThroughImageView) findViewById(R.id.lines);
//			peak.start();
//			SimpleDrawingView draw;
//			if (MyApplication.nightMod) {
//				draw = new SimpleDrawingView(MainActivity.this);
//			} else {
//				int color = Color.parseColor("#B2B2B3");
//				draw = new SimpleDrawingView(MainActivity.this,
//						color);
//			}
//			relativeLayout.addView(draw);
//		}
    }


}