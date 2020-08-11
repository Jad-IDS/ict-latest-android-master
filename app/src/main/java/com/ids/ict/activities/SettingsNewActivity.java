package com.ids.ict.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.CommonUtilities;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.Country;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.Profile;
import com.ids.ict.classes.PushNotificationActions;
import com.ids.ict.classes.ServicePro;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.services.OnlineCheckJobService;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import info.hoang8f.android.segmented.SegmentedGroup;

public class SettingsNewActivity extends Activity {


    String selectedLang = "";
    private boolean themeChanged = false;
    RelativeLayout back;
    LinearLayout footer;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;
    EditText email;
    String eventsSource = "";

    Typeface tf;
    LinearLayout lllayout;
    ToggleButton tgnotification, tgQos;
    int firstPosition;
    RelativeLayout mainbar;
    SegmentedGroup rdgtheme;
    RadioButton rbmaroon, rbnavy, rbauto;
    int languageselected = 1;
    Button btnsubmit;
    LinearLayout rllanguagespinner;
    TextView tvlanguage, tvnotification, tvemail, tvtheme, tvQos, tvVersionNumber, langTxt;
    final ArrayList<String> languages = new ArrayList<String>();
    TCTDbAdapter database = new TCTDbAdapter(SettingsNewActivity.this);
    JobScheduler jobScheduler;
    SharedPreferences.Editor edit, editor;
    SharedPreferences mSharedPreferences, mshared;

    int themetype = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Actions.setLocal(this);
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.activity_settings_layout);
        ViewResizing.setPageTextResizing(this);

        mSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        edit = mSharedPreferences.edit();
        mshared = PreferenceManager.getDefaultSharedPreferences(this);
        editor = mshared.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        }

        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            tf = MyApplication.facePolarisMedium;
        } else {
            tf = MyApplication.faceDinar;
        }

        findViews();

        Log.wtf("languae", "is "+MyApplication.Lang);

        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            lllayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            langTxt.setGravity(Gravity.RIGHT);
        }else{

            lllayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            langTxt.setGravity(Gravity.LEFT);

        }
    }


    private void findViews(){

        langTxt = (TextView) findViewById(R.id.langTxt);
        tvlanguage = (TextView) findViewById(R.id.tvlanguage);
        tvnotification = (TextView) findViewById(R.id.tvnotification);
        tvemail = (TextView) findViewById(R.id.tvemail);
        tvtheme = (TextView) findViewById(R.id.tvtheme);

        lllayout = (LinearLayout) findViewById(R.id.lllayout);
        rllanguagespinner = (LinearLayout) findViewById(R.id.rllanguagespinner);
        mainbar = (RelativeLayout) findViewById(R.id.main_bar);
        footer = (LinearLayout) findViewById(R.id.footer);
        TextView tvtheme = (TextView) findViewById(R.id.tvtheme);
        TextView tvemail = (TextView) findViewById(R.id.tvemail);

        rdgtheme = (SegmentedGroup) findViewById(R.id.rdgtheme);
        rbmaroon = (RadioButton) findViewById(R.id.rbmaroon);
        rbmaroon.setTypeface(tf);
        rbnavy = (RadioButton) findViewById(R.id.rbnavy);
        rbnavy.setTypeface(tf);
        rbauto = (RadioButton) findViewById(R.id.rbauto);
        rbauto.setTypeface(tf);

        back = (RelativeLayout) findViewById(R.id.back);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvQos = (TextView) findViewById(R.id.tvQos);
        tgQos = (ToggleButton) findViewById(R.id.tgQos);

        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        tgnotification = (ToggleButton) findViewById(R.id.tgnotification);
        email = (EditText) findViewById(R.id.email);

        tvQos = (TextView) findViewById(R.id.tvQos);

        if (MyApplication.enableQOS)
            tgQos.setChecked(true);
        else
            tgQos.setChecked(false);

        tvVersionNumber = (TextView) findViewById(R.id.tvVersionNumber);
        tvVersionNumber.setText(Actions.getReleaseVersion(this));
        tvVersionNumber.setTypeface(MyApplication.facePolarisMedium);

        switch (mSharedPreferences.getString("theme", "auto")) {
            case "auto":
                themetype = 0;
                break;
            case "maroon":
                themetype = 1;
                break;
            default:
                themetype = 2;
                break;
        }


        tvtheme.setText(getLookup("58").nameen);

        if (MyApplication.Lang.equals(MyApplication.ARABIC)) {
            tvtheme.setText(getLookup("58").namear);
            tvemail.setGravity(Gravity.RIGHT);
        }else{
            tvemail.setGravity(Gravity.LEFT);
        }

        setTheme();

        setLanguageSettings();

        rbnavy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTheme(rbnavy);
            }
        });

        rbauto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTheme(rbauto);
            }
        });

        rbmaroon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTheme(rbmaroon);
            }
        });

    }


    private void setTheme(){

        if (MyApplication.nightMod) {
            setNightMode();

            /*tgnotification.setButtonDrawable(R.drawable.notification_selector_night);
            tgQos.setButtonDrawable(R.drawable.notification_selector_night);*/

            tgnotification.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_night));
            tgQos.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_night));

            back.setBackgroundColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.nightBlue));
            mainbar.setBackgroundResource(R.drawable.footer_nt);

            footer.setBackgroundResource(R.drawable.footer_nt);
            btnsubmit.setBackground(ContextCompat.getDrawable(this, R.drawable.button_gray));
            if (themetype == 0) {
                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                if (hour < 5 || hour > 16) {

                    setNightChecked(rbauto);
                    setNotNightChecked(rbmaroon);
                    setNotNightChecked(rbnavy);
                }
            } else if (themetype == 1) {
                setNightChecked(rbmaroon);
                setNotNightChecked(rbauto);
                setNotNightChecked(rbnavy);
            } else {
                setNightChecked(rbnavy);
                setNotNightChecked(rbmaroon);
                setNotNightChecked(rbauto);
            }

        } else {
            setDayMode();
            back.setBackgroundColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.gray_light));
            btnsubmit.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.button_bordo));
            if (themetype == 0) {
                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                if (hour < 5 || hour > 16) {
                    setNightChecked(rbauto);
                    setNotNightChecked(rbmaroon);
                    setNotNightChecked(rbnavy);
                } else {
                    setDayChecked(rbauto);
                    setNotDayChecked(rbmaroon);
                    setNotDayChecked(rbnavy);
                }
            } else if (themetype == 1) {
                setDayChecked(rbmaroon);
                setNotDayChecked(rbauto);
                setNotDayChecked(rbnavy);
            } else {
                setNightChecked(rbnavy);
                setNotNightChecked(rbmaroon);
                setNotNightChecked(rbauto);
            }
        }
    }


    private void setLanguageSettings(){

        eventsSource = "" + MyApplication.link + "GeneralServices.asmx/GetIssueTypes?";
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] fill1 = {"English", "العربية"};
        if (MyApplication.nightMod)
            langTxt.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.white));
        else
            langTxt.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.black));
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);
            langTxt.setText("English");

            builder.setItems(fill1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        selectedLang = MyApplication.ENGLISH;
                        langTxt.setText("English");
                    } else {
                        selectedLang = MyApplication.ARABIC;
                        langTxt.setText("العربية");
                    }

                    submitClick();

                }
            });

        } else {
            final String[] fill1Ar = {"English", "العربية"};
            langTxt.setText("العربية");
            builder.setItems(fill1Ar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        selectedLang = MyApplication.ENGLISH;
                        langTxt.setText("English");
                    } else {
                        selectedLang = MyApplication.ARABIC;
                        langTxt.setText("العربية");
                    }


                    submitClick();


                }
            });
        }

        rllanguagespinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
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


    public void setNightChecked(TextView tv) {
        tv.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.bordernight));
        tv.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.nightBlue));
    }


    public void setDayChecked(TextView tv) {
        tv.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.border));
        tv.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.white));
    }


    public void setNotDayChecked(TextView tv) {
        tv.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.bordernotclicked));
        tv.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.bordo));
    }


    public void setNotNightChecked(TextView tv) {
        tv.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.bordernightnotclicked));
        tv.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.nightYellow));
    }


    public void setNightMode() {

        tvlanguage.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.white));
        tvnotification.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.white));
        tvemail.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.white));
        tvtheme.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.white));
        tvQos.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.white));
        langTxt.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.white));
        tvVersionNumber.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.white));
    }


    public void setDayMode() {

        tvlanguage.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.black));
        tvnotification.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.black));
        tvemail.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.black));
        tvtheme.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.black));
        tvQos.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.black));
        langTxt.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.black));
        tvVersionNumber.setTextColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.black));

    }


    public void clickTheme(View v) {


        int id = v.getId();
        if (MyApplication.nightMod) {
            switch (id) {
                case R.id.rbauto:

                    //<editor-fold desc="Auto">
                    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    if (hour < 5 || hour > 16) {
                        setNightMode();
                        setNightChecked(rbauto);
                        setNotNightChecked(rbmaroon);
                        setNotNightChecked(rbnavy);

                        /*tgnotification.setButtonDrawable(R.drawable.notification_selector_night);
                        tgQos.setButtonDrawable(R.drawable.notification_selector_night); */

                        tgnotification.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_night));
                        tgQos.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_night));

                        back.setBackgroundColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.nightBlue));
                        mainbar.setBackgroundResource(R.drawable.footer_nt);

                        final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
                        footer.setBackgroundResource(R.drawable.footer_nt);
                        btnsubmit.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.button_gray));

                        MyApplication.nightMod = true;
                        MyApplication.nightMod2 = 1;
                        edit.putString("theme", "auto").apply();

                    } else {
                        setDayMode();
                        setDayChecked(rbauto);
                        setNotDayChecked(rbmaroon);
                        setNotDayChecked(rbnavy);

                        /*tgnotification.setButtonDrawable(R.drawable.notification_selector_day);
                        tgQos.setButtonDrawable(R.drawable.notification_selector_day);*/

                        tgnotification.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_day));
                        tgQos.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_day));

                        back.setBackgroundColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.gray_light));
                        mainbar.setBackgroundResource(R.drawable.footer);

                        final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
                        footer.setBackgroundResource(R.drawable.footer);
                        btnsubmit.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.button_bordo));

                        edit.putString("theme", "auto").apply();
                        MyApplication.nightMod = false;
                        MyApplication.nightMod2 = 2;
                    }
                    themetype = 0;
                    //</editor-fold>

                    break;
                case R.id.rbmaroon:

                    //<editor-fold desc="Maroon">
                    MyApplication.nightMod = false;
                    MyApplication.nightMod2 = 2;
                    edit.putString("theme", "maroon").apply();


                    setDayChecked(rbmaroon);
                    setNotDayChecked(rbauto);
                    setNotDayChecked(rbnavy);

                    /*tgnotification.setButtonDrawable(R.drawable.notification_selector_day);
                    tgQos.setButtonDrawable(R.drawable.notification_selector_day);*/

                    tgnotification.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_day));
                    tgQos.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_day));

                    back.setBackgroundColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.gray_light));
                    mainbar.setBackgroundResource(R.drawable.footer);

                    final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
                    footer.setBackgroundResource(R.drawable.footer);
                    btnsubmit.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.button_bordo));
                    setDayMode();
                    themetype = 1;
                    //</editor-fold>

                    break;
                case R.id.rbnavy:

                    //<editor-fold desc="navy">
                    MyApplication.nightMod = true;
                    MyApplication.nightMod2 = 1;
                    edit.putString("theme", "navy").apply();

                    setNightChecked(rbnavy);
                    setNotNightChecked(rbmaroon);
                    setNotNightChecked(rbauto);

                    /*tgnotification.setButtonDrawable(R.drawable.notification_selector_night);
                    tgQos.setButtonDrawable(R.drawable.notification_selector_night);*/

                    tgnotification.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_night));
                    tgQos.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_night));

                    back.setBackgroundColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.nightBlue));
                    mainbar.setBackgroundResource(R.drawable.footer_nt);

                    final LinearLayout footer1 = (LinearLayout) findViewById(R.id.footer);
                    footer1.setBackgroundResource(R.drawable.footer_nt);
                    btnsubmit.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.button_gray));
                    setNightMode();
                    themetype = 2;
                    //</editor-fold>
                    break;
            }
        } else {
            switch (id) {
                case R.id.rbauto:

                    //<editor-fold desc=" Auto">
                    themetype = 0;
                    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    if (hour < 5 || hour > 16) {

                        setNightChecked(rbauto);
                        setNotNightChecked(rbmaroon);
                        setNotNightChecked(rbnavy);

                        /*tgnotification.setButtonDrawable(R.drawable.notification_selector_day);
                        tgQos.setButtonDrawable(R.drawable.notification_selector_day);*/

                        tgnotification.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_day));
                        tgQos.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_day));


                        back.setBackgroundColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.nightBlue));
                        mainbar.setBackgroundResource(R.drawable.footer_nt);

                        final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
                        footer.setBackgroundResource(R.drawable.footer_nt);
                        btnsubmit.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.button_gray));
                        setNightMode();

                        MyApplication.nightMod = true;
                        MyApplication.nightMod2 = 1;
                        edit.putString("theme", "auto").apply();


                    } else {
                        setDayChecked(rbauto);
                        setNotDayChecked(rbmaroon);
                        setNotDayChecked(rbnavy);

                        /*tgnotification.setButtonDrawable(R.drawable.notification_selector_day);
                        tgQos.setButtonDrawable(R.drawable.notification_selector_day);*/

                        tgnotification.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_day));
                        tgQos.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_day));

                        back.setBackgroundColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.gray_light));
                        mainbar.setBackgroundResource(R.drawable.footer);

                        final LinearLayout footer1 = (LinearLayout) findViewById(R.id.footer);
                        footer1.setBackgroundResource(R.drawable.footer);
                        btnsubmit.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.button_bordo));
                        setDayMode();

                        MyApplication.nightMod = false;
                        MyApplication.nightMod2 = 2;
                        edit.putString("theme", "auto").apply();
                    }
                    //</editor-fold>

                    break;
                case R.id.rbmaroon:

                    //<editor-fold desc="Maroon">

                    themetype = 1;
                    setDayChecked(rbmaroon);
                    setNotDayChecked(rbauto);
                    setNotDayChecked(rbnavy);

                    /*tgnotification.setButtonDrawable(R.drawable.notification_selector_day);
                    tgQos.setButtonDrawable(R.drawable.notification_selector_day);*/

                    tgnotification.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_day));
                    tgQos.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_day));



                    back.setBackgroundColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.gray_light));
                    mainbar.setBackgroundResource(R.drawable.footer);

                    final LinearLayout footer1 = (LinearLayout) findViewById(R.id.footer);
                    footer1.setBackgroundResource(R.drawable.footer);
                    btnsubmit.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.button_bordo));
                    setDayMode();

                    MyApplication.nightMod = false;
                    MyApplication.nightMod2 = 2;
                    edit.putString("theme", "maroon").apply();

                    //</editor-fold>

                    break;
                case R.id.rbnavy:

                    //<editor-fold desc="Navy">

                    themetype = 2;
                    setNightChecked(rbnavy);
                    setNotNightChecked(rbmaroon);
                    setNotNightChecked(rbauto);

                    /*tgnotification.setButtonDrawable(R.drawable.notification_selector_night);
                    tgQos.setButtonDrawable(R.drawable.notification_selector_night);*/

                    tgnotification.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_night));
                    tgQos.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_night));

                    back.setBackgroundColor(ContextCompat.getColor(SettingsNewActivity.this, R.color.nightBlue));
                    mainbar.setBackgroundResource(R.drawable.footer_nt);

                    final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
                    footer.setBackgroundResource(R.drawable.footer_nt);
                    btnsubmit.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.button_gray));
                    setNightMode();

                    MyApplication.nightMod = true;
                    MyApplication.nightMod2 = 1;
                    edit.putString("theme", "navy").apply();

                    //</editor-fold>

                    break;
            }
        }
        themeChanged = true;
    }


    public void backTo(View v) {
        Log.wtf("On back pressed", "");
        if (themeChanged) {
            Log.wtf("On back pressed", "changed");
            Intent intent = new Intent(SettingsNewActivity.this,
                    HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("fromSettings", true);
            MyApplication.fromSettings = true;

            startActivity(intent);
            finish();
        } else {
            Log.wtf("On back pressed", "not");
            Actions.backTo(this);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.wtf("On back pressed", "");
        if (themeChanged) {
            Log.wtf("On back pressed", "changed");
            Intent intent = new Intent(SettingsNewActivity.this,
                    HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("fromSettings", true);
            MyApplication.fromSettings = true;

            startActivity(intent);
            finish();
        } else {
            Log.wtf("On back pressed", "not");

            //MyApplication.themeChanged = false;

            Actions.backTo(this);
        }
    }


    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private void submitClick(){

        TCTDbAdapter source = new TCTDbAdapter(SettingsNewActivity.this);
        source.open();
        String x = selectedLang;

        Log.wtf("SELECTED LANGUAGE", selectedLang);

        if (!email.getText().toString().equals("")) {
            if (!isValidEmail(email.getText().toString())) {
                Actions.onCreateDialog1(SettingsNewActivity.this, getString(R.string.email_error_message));
            } else {
                if (selectedLang.equalsIgnoreCase(MyApplication.ENGLISH)) {
                    source.updateProfileRow(MyApplication.ENGLISH, "1", email
                            .getText().toString());
                    MyApplication.Lang = MyApplication.ENGLISH;
                    edit.putString(getString(R.string.language_key),
                            MyApplication.ENGLISH);
                    edit.commit();
                    TCTDbAdapter sour = new TCTDbAdapter(SettingsNewActivity.this);
                    sour.open();
                    int size = 0;
                    try {
                        ArrayList<Event> ar = sour.getissue_Type("1");
                        size = ar.size();
                    } catch (Exception e) {
                        size = 0;
                    }
                    sour.close();

                    if (size == 0) {
                        LaunchingEvent1 eventLaunching = new LaunchingEvent1();
                        eventLaunching.execute();
                    } else {
                        Intent intent = new Intent(SettingsNewActivity.this, HomePageActivity.class);
                        MyApplication.fromSettings = true;
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    source.updateProfileRow(MyApplication.ARABIC, "1", email
                            .getText().toString());
                    MyApplication.Lang = MyApplication.ARABIC;
                    edit.putString(getString(R.string.language_key),
                            MyApplication.ARABIC);
                    edit.commit();

                    TCTDbAdapter sour = new TCTDbAdapter(SettingsNewActivity.this);
                    sour.open();
                    int size = 0;
                    try {
                        ArrayList<Event> ar = sour.getissue_Type("3");
                        size = ar.size();
                    } catch (Exception e) {
                        size = 0;
                    }
                    sour.close();
                    if (size == 0) {
                        LaunchingEvent eventLaunching = new LaunchingEvent();
                        eventLaunching.execute();
                    } else {
                        Intent intent = new Intent(SettingsNewActivity.this, HomePageActivity.class);
                        MyApplication.fromSettings = true;
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);
                        finish();
                    }

                }

            }
        } else {
            if (selectedLang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                TCTDbAdapter sour = new TCTDbAdapter(SettingsNewActivity.this);
                sour.open();
                int size = 0;
                try {
                    ArrayList<Event> ar = sour.getissue_Type("1");
                    size = ar.size();
                } catch (Exception e) {
                    size = 0;
                }
                sour.close();

                if (size == 0) {

                    if (Actions.isNetworkAvailable(SettingsNewActivity.this)) {
                        source.updateProfileRow(MyApplication.ENGLISH, "1", email
                                .getText().toString());
                        MyApplication.Lang = MyApplication.ENGLISH;
                        edit.putString(getString(R.string.language_key), MyApplication.ENGLISH).apply();

                        LaunchingEvent1 eventLaunching = new LaunchingEvent1();
                        eventLaunching.execute();
                    } else {
                        String error;
                        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH))
                            error = getLookup("90").nameen;
                        else
                            error = getLookup("90").namear;
                        Toast.makeText(SettingsNewActivity.this, error, Toast.LENGTH_LONG).show();
                    }


                } else {

                    source.updateProfileRow(MyApplication.ENGLISH, "1", email
                            .getText().toString());
                    MyApplication.Lang = MyApplication.ENGLISH;
                    edit.putString(getString(R.string.language_key), MyApplication.ENGLISH).apply();

                    Intent intent = new Intent(SettingsNewActivity.this, HomePageActivity.class);
                    MyApplication.fromSettings = true;
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            } else {


                TCTDbAdapter sour = new TCTDbAdapter(SettingsNewActivity.this);
                sour.open();
                int size = 0;
                try {
                    ArrayList<Event> ar = sour.getissue_Type("3");
                    size = ar.size();
                } catch (Exception e) {
                    size = 0;
                }
                sour.close();
                if (size == 0) {

                    if (Actions.isNetworkAvailable(SettingsNewActivity.this)) {
                        source.updateProfileRow(MyApplication.ARABIC, "1", email.getText().toString());
                        MyApplication.Lang = MyApplication.ARABIC;

                        edit.putString(getString(R.string.language_key), MyApplication.ARABIC).apply();

                        LaunchingEvent eventLaunching = new LaunchingEvent();
                        eventLaunching.execute();
                    } else {
                        String error;
                        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH))
                            error = getLookup("90").nameen;
                        else
                            error = getLookup("90").namear;

                        Toast.makeText(SettingsNewActivity.this, error, Toast.LENGTH_LONG).show();
                    }

                } else {

                    source.updateProfileRow(MyApplication.ARABIC, "1", email.getText().toString());
                    MyApplication.Lang = MyApplication.ARABIC;
                    edit.putString(getString(R.string.language_key), MyApplication.ARABIC).apply();

                    Intent intent = new Intent(SettingsNewActivity.this, HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    MyApplication.fromSettings = true;
                    startActivity(intent);
                    finish();
                }

            }
        }
        source.close();
        if (themetype == 1) {
            MyApplication.nightMod = false;
            edit.putString("theme", "maroon").apply();
        } else if (themetype == 2) {
            MyApplication.nightMod = true;
            edit.putString("theme", "navy").apply();

        } else if (themetype == 0) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (hour < 5 || hour > 16) {

                mainbar.setBackgroundResource(R.drawable.footer_nt);
                btnsubmit.setBackground(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.button_gray));
                final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
                footer.setBackgroundResource(R.drawable.footer_nt);
                MyApplication.nightMod = true;

                //tgnotification.setButtonDrawable(R.drawable.notification_selector_night);
                //tgQos.setButtonDrawable(R.drawable.notification_selector_night);

                tgnotification.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_night));
                tgQos.setBackgroundDrawable(ContextCompat.getDrawable(SettingsNewActivity.this, R.drawable.notification_selector_night));

                edit.putString("theme", "auto");
            } else {
                edit.putString("theme", "auto");
                MyApplication.nightMod = false;
            }
            edit.commit();
        }
    }


    public void setdatestamp() {

        String arr = "";
        TCTDbAdapter sour = new TCTDbAdapter(SettingsNewActivity.this);
        sour.open();
        try {
            arr = sour.getdatestamp();
        } catch (Exception e) {
            arr = "";
        }
        sour.close();
        if (arr.equals("")) {
            try {
                URL url = new URL("" + MyApplication.link + "GeneralServices.asmx/GetLastTextDateTimeUpdate?password="
                        + MyApplication.pass + "&language=en");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("dateTime");
                Element nameElement = (Element) nodeList.item(0);
                nodeList = nameElement.getChildNodes();
                String name = ((Node) nodeList.item(0)).getNodeValue();
                TCTDbAdapter datasource = new TCTDbAdapter(SettingsNewActivity.this);
                datasource.open();
                datasource.create_datestamp(name);
                datasource.close();
            } catch (Exception e) {

                String url1 = "" + MyApplication.link
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


    public void setserviceprovider() {

        TCTDbAdapter sour = new TCTDbAdapter(SettingsNewActivity.this);
        sour.open();
        ArrayList<ServicePro> arr = sour.getsp_lang("English");
        sour.close();
        if (arr.size() == 0) {
            String[] a;
            String[] b;

            try {


                URL url = new URL("" + MyApplication.link
                        + "GeneralServices.asmx/GetServiceProviders?password="
                        + MyApplication.pass + "&language=en");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("ServiceProvider");

                /** Assign textview array lenght by arraylist size */
                // name = new TextView[nodeList.getLength()];
                // website = new TextView[nodeList.getLength()];
                // category = new TextView[nodeList.getLength()];
                // RelativeLayout relab = (RelativeLayout)
                // findViewById(R.id.relab);
                // a = new String[nodeList.getLength()];
                // b = new String[nodeList.getLength()];
                // bspin2 = new String[nodeList.getLength()];
                TCTDbAdapter datasource = new TCTDbAdapter(SettingsNewActivity.this);
                datasource.open();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);

                    Element fstElmnt = (Element) node;
                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String name = ((Node) nameList.item(0)).getNodeValue();

                    // b[i] = name;

                    NodeList websiteList = fstElmnt
                            .getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String title = ((Node) websiteList.item(0)).getNodeValue();

                    // a[i] = title;

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

                    // a[i] = title;

                    try {

                        long ln = datasource.create_sp(Integer.parseInt(name),
                                title, isfortrans, "English", code);

                        if (!(ln > -1)) {// case row not inserted, may be
                            // duplicated
                            throw new Exception();
                        }

                        // case saved
                        // Actions.onCreateDialog(this,
                        // "Event saved successfully.",false);

                    } catch (Exception e) {// case not saved
                        // Actions.onCreateDialog(this,
                        // "Event had been previously saved.",false)
                        // ;
                    }



                }
                datasource.close();
            } catch (Exception e) {
                String url1 = "" + MyApplication.link
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


    public void setcountry() {

        TCTDbAdapter sour = new TCTDbAdapter(SettingsNewActivity.this);
        sour.open();
        ArrayList<Country> arr = sour.getcountry("English");
        sour.close();
        if (arr.size() == 0) {
            String[] a;
            String[] b;

            try {

                // com.ids.indyact.ViewResizing.setPageTextResizing(this);

                URL url = new URL("" + MyApplication.link
                        + "GeneralServices.asmx/GetCountries?password="
                        + MyApplication.pass + "&language=en");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("Country");
                a = new String[nodeList.getLength()];
                b = new String[nodeList.getLength()];
                // bspin2 = new String[nodeList.getLength()];
                TCTDbAdapter datasource = new TCTDbAdapter(SettingsNewActivity.this);
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
                            // duplicated
                            throw new Exception();
                        }

                        // case saved
                        // Actions.onCreateDialog(this,
                        // "Event saved successfully.",false);

                    } catch (Exception e) {// case not saved
                        // Actions.onCreateDialog(this,
                        // "Event had been previously saved.",false)
                        // ;
                    }

                }
                datasource.close();
            } catch (Exception e) {

                String url1 = "" + MyApplication.link
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


    public void setissuedet(String idissue) {
        try {

            // com.ids.indyact.ViewResizing.setPageTextResizing(this);

            URL url = new URL(
                    ""
                            + MyApplication.link
                            + "GeneralServices.asmx/GetIssueDetails?language=en&password="
                            + MyApplication.pass + "&issueid=" + idissue);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("IssueDetail");

            /** Assign textview array lenght by arraylist size */
            // name = new TextView[nodeList.getLength()];
            // website = new TextView[nodeList.getLength()];
            // category = new TextView[nodeList.getLength()];
            // RelativeLayout relab = (RelativeLayout) findViewById(R.id.relab);

            TCTDbAdapter datasource = new TCTDbAdapter(SettingsNewActivity.this);
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

                }
                NodeList nameListappdate = fstElmnt.getElementsByTagName("CheckOnApplicationDate");
                Element nameElementappdate = (Element) nameListappdate.item(0);
                nameListappdate = nameElementappdate.getChildNodes();
                String checkappondate = ((Node) nameListappdate.item(0)).getNodeValue();
                try {

                    long ln = datasource.createissue_detail(
                            Integer.parseInt(name), title, idissue, modify,
                            duration, unit, unitAr, "English",
                            specialneeddurationvalue, specialneedunitvalue, specialneedunitarvalue, checkappondate);

                    if (!(ln > -1)) {// case row not inserted, may be duplicated
                        throw new Exception();
                    }

                    // case saved
                    // Actions.onCreateDialog(this,
                    // "Event saved successfully.",false);

                } catch (Exception e) {// case not saved
                    // Actions.onCreateDialog(this,
                    // "Event had been previously saved.",false)
                    // ;
                }


            }
            datasource.close();
        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
            String url1 = "" + MyApplication.link
                    + "PostData.asmx/VerifyRegistration";
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


    public class LaunchingEvent1 extends AsyncTask<Void, Void, Integer> {
        com.ids.ict.Error error;
        Event[] nn;

        @Override
        public void onPreExecute() {


            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public Integer doInBackground(Void... params) {
            Log.d("launching", "passed here");
            AtomicReference<Event[]> ref = new AtomicReference<Event[]>(null);
            TCTDbAdapter datasource = new TCTDbAdapter(SettingsNewActivity.this);
            datasource.open();
            try {

                setdatestamp();
                String lan;
                lan = "en";
                setcountry();
                setserviceprovider();

                ArrayList<Event> arr = datasource.getissue_Type("1");
                ArrayList<Profile> arr11 = datasource.getAllProfiles();
                try {
                    MyApplication.pass = arr11.get(0).getId();
                } catch (Exception e) {
                    String url1 = "" + MyApplication.link
                            + "PostData.asmx/VerifyRegistration";
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
                if (arr.size() == 0) {
                    eventsSource = eventsSource + "language=" + lan
                            + "&password=" + MyApplication.pass
                            + "&mainIssueTypeId=1";
                    Log.d("eventsource", eventsSource);
                    error = Actions.readEvents(ref, eventsSource);

                    nn = ref.get();
                    try {
                        String ii = nn[0].getDate();
                    } catch (Exception e) {

                        String url1 = "" + MyApplication.link
                                + "PostData.asmx/VerifyRegistration";
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

                    for (int i = 0; i < nn.length; i++) {
                        try {
//							String urlth = nn[i].getThumbnailUrlDayl();
//							Connection conn = new Connection(urlth);
//							Bitmap b = conn.readImage();
//							ByteArrayOutputStream stream = new ByteArrayOutputStream();
//							b.compress(Bitmap.CompressFormat.PNG, 100, stream);
//							byte[] array = stream.toByteArray();
//
//							String urlthN = nn[i].getThumbnailUrlNight();
//							Connection connN = new Connection(urlthN);
//							Bitmap bN = connN.readImage();
//							ByteArrayOutputStream streamN = new ByteArrayOutputStream();
//							bN.compress(Bitmap.CompressFormat.PNG, 100, streamN);
//							byte[] arrayN = streamN.toByteArray();

                            long ln = datasource.createissue_type(
                                    nn[i].getId(), nn[i].getName(),
                                    nn[i].getShowMap(), nn[i].getDescription(),
                                    nn[i].getDate(), nn[i].getLocation(), "1",
                                    nn[i].getThumbnailUrlDayl(), nn[i].getThumbnailUrlNight(), nn[i].getServiceProviderTransfer());

                            setissuedet("" + nn[i].getId());

                            if (!(ln > -1)) {// case row not inserted, may be
                                // duplicated
                                throw new Exception();
                            }

                        } catch (Exception e) {// case not saved
                            String url1 = "" + MyApplication.link
                                    + "PostData.asmx/VerifyRegistration";
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

                }

            } catch (IOException e) {
                e.printStackTrace();
                String url1 = "" + MyApplication.link
                        + "PostData.asmx/VerifyRegistration";
                Connection conn = new Connection(url1);

                try {
                    String error_return = conn.executeMultipartPost_Send_Error(
                            this.getClass().getSimpleName(),
                            Actions.getDeviceName(), "1", e.getMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                String url1 = "" + MyApplication.link
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
            try {
                String lan;
                lan = "en";
                ArrayList<Event> arr = datasource.getissue_Type("2");
                if (arr.size() == 0) {
                    eventsSource = "" + MyApplication.link
                            + "GeneralServices.asmx/GetIssueTypes?language="
                            + lan + "&password=" + MyApplication.pass
                            + "&mainIssueTypeId=2";
                    Log.d("eventsource", eventsSource);
                    error = Actions.readEvents(ref, eventsSource);

                    nn = ref.get();
                    try {
                        String ii = nn[0].getDate();
                    } catch (Exception e) {

                        String url1 = "" + MyApplication.link
                                + "PostData.asmx/VerifyRegistration";
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

                    for (int i = 0; i < nn.length; i++) {
                        try {
//							String urlth = nn[i].getThumbnailUrlDayl();
//							Connection conn = new Connection(urlth);
//							Bitmap b = conn.readImage();
//							ByteArrayOutputStream stream = new ByteArrayOutputStream();
//							b.compress(Bitmap.CompressFormat.PNG, 100, stream);
//							byte[] array = stream.toByteArray();
//
//							String urlthN = nn[i].getThumbnailUrlNight();
//							Connection connN = new Connection(urlthN);
//							Bitmap bN = connN.readImage();
//							ByteArrayOutputStream streamN = new ByteArrayOutputStream();
//							bN.compress(Bitmap.CompressFormat.PNG, 100, streamN);
//							byte[] arrayN = streamN.toByteArray();

                            long ln = datasource.createissue_type(
                                    nn[i].getId(), nn[i].getName(),
                                    nn[i].getShowMap(), nn[i].getDescription(),
                                    nn[i].getDate(), nn[i].getLocation(), "2",
                                    nn[i].getThumbnailUrlDayl(), nn[i].getThumbnailUrlNight(), nn[i].getServiceProviderTransfer());

                            setissuedet("" + nn[i].getId());

                            if (!(ln > -1)) {// case row not inserted, may be
                                // duplicated
                                throw new Exception();
                            }

                        } catch (Exception e) {// case not saved
                            String url1 = "" + MyApplication.link
                                    + "PostData.asmx/VerifyRegistration";
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

                }
                datasource.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            return 1;
        }

        @Override
        public void onPostExecute(Integer result) {

            //dialog.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

            Intent intent = new Intent(SettingsNewActivity.this, HomePageActivity.class);
            MyApplication.fromSettings = true;
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();

        }

    }


    public class LaunchingEvent extends AsyncTask<Void, Void, Integer> {
        com.ids.ict.Error error;
        Event[] nn;

        @Override
        public void onPreExecute() {
            // prog.setVisibility(View.VISIBLE);



            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public Integer doInBackground(Void... params) {
            Log.d("launching", "passed here");
            AtomicReference<Event[]> ref = new AtomicReference<Event[]>(null);
            TCTDbAdapter datasource = new TCTDbAdapter(SettingsNewActivity.this);
            datasource.open();
            // try {

            setdatestamp();

            setcountryar();

            setserviceproviderAr();
            try {
                ArrayList<Event> arr = datasource.getissue_Type("3");
                if (arr.size() == 0) {
                    eventsSource = ""
                            + MyApplication.link
                            + "GeneralServices.asmx/GetIssueTypes?language=ar&password="
                            + MyApplication.pass + "&mainIssueTypeId=1";
                    Log.d("eventsource", eventsSource);
                    error = Actions.readEvents(ref, eventsSource);

                    nn = ref.get();
                    try {
                        String ii = nn[0].getDate();
                    } catch (Exception e) {

                    }

                    for (int i = 0; i < nn.length; i++) {
                        try {

                            String urlth = nn[i].getThumbnailUrlDayl();
                            Connection conn = new Connection(urlth);
                            /*Bitmap b = conn.readImage();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            try {
                                b.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            } catch (Exception e) {

                            }
                            byte[] array = stream.toByteArray();

                            String urlthN = nn[i].getThumbnailUrlNight();
                            Connection connN = new Connection(urlthN);
                            Bitmap bN = connN.readImage();
                            ByteArrayOutputStream streamN = new ByteArrayOutputStream();
                            try {
                                bN.compress(Bitmap.CompressFormat.PNG, 100, streamN);
                            } catch (Exception e) {

                            }
                            byte[] arrayN = streamN.toByteArray();*/

                            long ln = datasource.createissue_type(
                                    nn[i].getId(), nn[i].getName(),
                                    nn[i].getShowMap(), nn[i].getDescription(),
                                    nn[i].getDate(), nn[i].getLocation(), "3",
                                    nn[i].getThumbnailUrlDayl(), nn[i].getThumbnailUrlNight(), nn[i].getServiceProviderTransfer());

                            setissuedetar("" + nn[i].getId());

                            if (!(ln > -1)) {// case row not inserted, may be
                                // duplicated
                                throw new Exception();
                            }
                        } catch (Exception e) {// case not saved
                            // Actions.onCreateDialog(this,
                            // "Event had been previously saved.",false)
                            // ;
                            String url1 = "" + MyApplication.link
                                    + "PostData.asmx/VerifyRegistration";
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

                }

            } catch (IOException e) {
                e.printStackTrace();
                String url1 = "" + MyApplication.link
                        + "PostData.asmx/VerifyRegistration";
                Connection conn = new Connection(url1);

                try {
                    String error_return = conn.executeMultipartPost_Send_Error(
                            this.getClass().getSimpleName(),
                            Actions.getDeviceName(), "1", e.getMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                String url1 = "" + MyApplication.link
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

            try {
                String lan;
                // if(lang.equals("English")){
                lan = "en";
                // }
                // else{
                // lan="ar";
                // }

                ArrayList<Event> arr = datasource.getissue_Type("4");
                if (arr.size() == 0) {
                    eventsSource = ""
                            + MyApplication.link
                            + "GeneralServices.asmx/GetIssueTypes?language=ar&password="
                            + MyApplication.pass + "&mainIssueTypeId=2";
                    Log.d("eventsource", eventsSource);
                    error = Actions.readEvents(ref, eventsSource);

                    nn = ref.get();
                    try {
                        String ii = nn[0].getDate();
                    } catch (Exception e) {

                    }

                    for (int i = 0; i < nn.length; i++) {
                        try {
                            String urlth = nn[i].getThumbnailUrlDayl();
                            Connection conn = new Connection(urlth);
                           /* Bitmap b = conn.readImage();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            try {
                                b.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            } catch (Exception e) {

                            }
                            byte[] array = stream.toByteArray();

                            String urlthN = nn[i].getThumbnailUrlNight();
                            Connection connN = new Connection(urlthN);
                            Bitmap bN = connN.readImage();
                            ByteArrayOutputStream streamN = new ByteArrayOutputStream();
                            try {
                                bN.compress(Bitmap.CompressFormat.PNG, 100, streamN);
                            } catch (Exception e) {

                            }
                            byte[] arrayN = streamN.toByteArray();*/

                            long ln = datasource.createissue_type(
                                    nn[i].getId(), nn[i].getName(),
                                    nn[i].getShowMap(), nn[i].getDescription(),
                                    nn[i].getDate(), nn[i].getLocation(), "4",
                                    nn[i].getThumbnailUrlDayl(), nn[i].getThumbnailUrlNight(), nn[i].getServiceProviderTransfer());

                            setissuedetar("" + nn[i].getId());

                            if (!(ln > -1)) {// case row not inserted, may be
                                // duplicated
                                throw new Exception();
                            }

                            // case saved
                            // Actions.onCreateDialog(this,
                            // "Event saved successfully.",false);

                        } catch (Exception e) {// case not saved
                            // Actions.onCreateDialog(this,
                            // "Event had been previously saved.",false)
                            // ;
                            String url1 = "" + MyApplication.link
                                    + "PostData.asmx/VerifyRegistration";
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

                }
                datasource.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            return 1;
        }

        @Override
        public void onPostExecute(Integer result) {

            //dialog.dismiss();

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

            // prog.setVisibility(View.GONE);
            Intent intent = new Intent(SettingsNewActivity.this,
                    HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            MyApplication.fromSettings = true;
            startActivity(intent);
            finish();

        }
    }


    public void setcountryar() {

        TCTDbAdapter sour = new TCTDbAdapter(SettingsNewActivity.this);
        sour.open();
        ArrayList<Country> arr = sour.getcountry("Arabic");
        sour.close();
        if (arr.size() == 0) {
            String[] a;
            String[] b;

            try {

                URL url = new URL("" + MyApplication.link
                        + "GeneralServices.asmx/GetCountries?password="
                        + MyApplication.pass + "&language=ar");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("Country");
                a = new String[nodeList.getLength()];
                b = new String[nodeList.getLength()];
                TCTDbAdapter datasource = new TCTDbAdapter(
                        SettingsNewActivity.this);
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
                            // duplicated
                            throw new Exception();
                        }

                        // case saved
                        // Actions.onCreateDialog(this,
                        // "Event saved successfully.",false);

                    } catch (Exception e) {// case not saved
                        // Actions.onCreateDialog(this,
                        // "Event had been previously saved.",false)
                        // ;
                        String url1 = "" + MyApplication.link
                                + "PostData.asmx/VerifyRegistration";
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

                    // relab.addView(myButton);

                    /*
                     * Element fstElmnt3 = (Element) node; NodeList nameList2 =
                     * fstElmnt3.getElementsByTagName("AuthorName"); Element
                     * nameElement2 = (Element) nameList2.item(0); nameList2 =
                     * nameElement2.getChildNodes(); String name2 = ((Node)
                     * nameList2.item(0)).getNodeValue();
                     */

                }
                datasource.close();
            } catch (Exception e) {

                String url1 = "" + MyApplication.link
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


    public void setissuedetar(String idissue) {
        try {

            URL url = new URL(
                    ""
                            + MyApplication.link
                            + "GeneralServices.asmx/GetIssueDetails?language=ar&password="
                            + MyApplication.pass + "&issueid=" + idissue);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("IssueDetail");

            TCTDbAdapter datasource = new TCTDbAdapter(SettingsNewActivity.this);
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

                }
                try {
                    NodeList specialneedsunit = fstElmnt.getElementsByTagName("SpecialNeedsDurationUnit");
                    Element specialneedsunitElement = (Element) specialneedsunit
                            .item(0);
                    specialneedsunit = specialneedsunitElement.getChildNodes();
                    specialneedunitvalue = ((Node) specialneedsunit
                            .item(0)).getNodeValue();
                } catch (Exception e) {

                }
                NodeList nameListappdate = fstElmnt.getElementsByTagName("CheckOnApplicationDate");
                Element nameElementappdate = (Element) nameListappdate.item(0);
                nameListappdate = nameElementappdate.getChildNodes();
                String checkappondate = ((Node) nameListappdate.item(0)).getNodeValue();
                try {

                    long ln = datasource.createissue_detail(
                            Integer.parseInt(name), title, idissue, modify,
                            duration, unit, unitAr, "Arabic",
                            specialneeddurationvalue, specialneedunitvalue, specialneedunitarvalue, checkappondate);

                    if (!(ln > -1)) {// case row not inserted, may be duplicated
                        throw new Exception();
                    }
                } catch (Exception e) {// case not saved
                    String url1 = "" + MyApplication.link
                            + "PostData.asmx/VerifyRegistration";
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
            String url1 = "" + MyApplication.link
                    + "PostData.asmx/VerifyRegistration";
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


    public void setserviceproviderAr() {

        TCTDbAdapter sour = new TCTDbAdapter(SettingsNewActivity.this);
        sour.open();
        ArrayList<ServicePro> arr = sour.getsp_lang("Arabic");
        sour.close();
        if (arr.size() == 0) {
            try {
                URL url = new URL("" + MyApplication.link
                        + "GeneralServices.asmx/GetServiceProviders?password="
                        + MyApplication.pass + "&language=ar");
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("ServiceProvider");
                TCTDbAdapter datasource = new TCTDbAdapter(
                        SettingsNewActivity.this);
                datasource.open();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);

                    Element fstElmnt = (Element) node;
                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String name = ((Node) nameList.item(0)).getNodeValue();

                    // b[i] = name;

                    NodeList websiteList = fstElmnt
                            .getElementsByTagName("Name");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String title = ((Node) websiteList.item(0)).getNodeValue();

                    // a[i] = title;

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

                    // a[i] = title;

                    try {

                        long ln = datasource.create_sp(Integer.parseInt(name), title, isfortrans, "Arabic", code);

                        if (!(ln > -1)) {// case row not inserted, may be
                            // duplicated
                            throw new Exception();
                        }

                    } catch (Exception e) {// case not saved
                        String url1 = "" + MyApplication.link + "PostData.asmx/VerifyRegistration";
                        Connection conn = new Connection(url1);

                        try {
                            String error_return = conn .executeMultipartPost_Send_Error(this .getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                }
                datasource.close();
            } catch (Exception e) {
                String url1 = "" + MyApplication.link
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


    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(SettingsNewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(SettingsNewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                //ContextCompat.checkSelfPermission(QosTermsActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                //||
                ContextCompat.checkSelfPermission(SettingsNewActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
            //||
            //ContextCompat.checkSelfPermission(QosTermsActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(SettingsNewActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 1);
            Log.wtf("qos", "req req");

        } else { //permissions granted

            MyApplication.enableQOS = true;

            Log.wtf("qos", "3 true k");
            editor.putBoolean(getString(R.string.enable_qos), MyApplication.enableQOS).apply();

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
                Log.wtf("granted", "permission");
                editor.putBoolean(getString(R.string.enable_qos), MyApplication.enableQOS).apply();


                new AddDevice().execute("");

            } else {


                MyApplication.enableQOS = false;

                editor.putBoolean(getString(R.string.enable_qos), MyApplication.enableQOS).apply();

                tgQos.setChecked(MyApplication.enableQOS);
                Log.wtf("not granted", "permission");

                try {

                    PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "QosData");
                    wakeLock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new AddDevice().execute("");
            }
        }
    }


    public void setUpQos(View v) {


        if (tgQos.isChecked()) {

            Log.wtf("qos", "3melneha check");

            checkPermissions();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                ComponentName componentName = new ComponentName(getApplicationContext(), OnlineCheckJobService.class);
                JobInfo jobInfo = null;
                jobInfo = new JobInfo.Builder(1, componentName)
                        .setRequiresCharging(false)
                        .setRequiresDeviceIdle(false)
                        .setPeriodic(MyApplication.SCHEDULER_SECONDS * 1000)
                        .build();

                if (!isScheduledJobServiceOn()) {
                    jobScheduler.schedule(jobInfo);
                    Log.wtf("job ", "initialized");
                }
            }

        } else {
            MyApplication.enableQOS = false;
            Log.wtf("qos", "3melneha uncheck");

            Log.wtf("nabled", "aa " + mshared.getBoolean(getString(R.string.enable_qos), false));

            editor.putBoolean(getString(R.string.enable_qos), MyApplication.enableQOS).apply();

            Log.wtf("nabled", "aa " + mshared.getBoolean(getString(R.string.enable_qos), false));

            try {

                PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "QosData");
                wakeLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                if (isScheduledJobServiceOn()) { //stop the scheduler, then filter

                    jobScheduler.cancel(1);
                    Log.wtf("jobScheduler", "canceled");
                }
            }

            new AddDevice().execute("");
        }
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


    private class AddDevice extends AsyncTask<String, Void, Integer> {

        private String android_id;
        String currentDateandTime;
        String code = "1", notificationFlag = "", carrierName, mobileNumber = "";
        Profile profile = new Profile();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            android_id = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            currentDateandTime = sdf.format(new Date());
            PackageInfo pInfo;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                code = pInfo.versionCode + "";
            } catch (Exception e) {

                e.printStackTrace();
            }

            if (mSharedPreferences.getBoolean(getString(R.string.notification_enabled), true)) {

                notificationFlag = "1";
            } else {

                notificationFlag = "0";
            }
            TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            carrierName = manager.getSimOperatorName();


            TCTDbAdapter sour = new TCTDbAdapter(SettingsNewActivity.this);
            sour.open();
            ArrayList<Profile> arr = sour.getAllProfiles();
            profile = arr.get(0);
            sour.close();

            try {
                mobileNumber = profile.getnum();
            } catch (Exception e) {
                e.printStackTrace();
                mobileNumber = "";
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

            try {
                result = Connection.POST(url, parameters);
                String[] r1 = result.split("</");
                String[] r2 = r1[0].split(">");

                Log.wtf("result is", result);

                result = r2[2];
            } catch (Exception e) {
                e.printStackTrace();
                result = String.valueOf(mshared.getInt(getResources().getString(R.string.device_id), 0));
            }
            return Integer.parseInt(result);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.wtf("res", result + "");
            Log.d("!!!res", result + "");

            try {

                edit.putInt(getResources().getString(R.string.device_id), result).apply();
                editor.putInt(getResources().getString(R.string.device_id), result).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setUpNotification(View view) {

        ToggleButton tb = (ToggleButton) view;
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        CommonUtilities.setSenderId(this);
        if (tb.isChecked()) {

            edit.putBoolean(getString(R.string.notification_enabled), true).apply(); //custom
            edit.putBoolean(getString(R.string.notification_key), true).apply(); //custom
            editor.putBoolean(getString(R.string.enable_notification), true).apply(); //tebe3 lal default


            boolean registered = PushNotificationActions.isRegistered(this);
            if (!registered) {
                // register
                try {
                    PushNotificationActions.register(this, edit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            edit.putBoolean(getString(R.string.notification_enabled), false).apply();
            edit.putBoolean(getString(R.string.notification_key), false).apply();
            editor.putBoolean(getString(R.string.enable_notification), false).apply();

            try {
                PushNotificationActions.unRegister(this);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        tgnotification.setChecked(PushNotificationActions.isRegistered(this));

        tgnotification.setChecked(mSharedPreferences.getBoolean(getString(R.string.notification_key), true));
    }

    public void topBarBack(View v) {

        Log.wtf("On back pressed", "");
        if (themeChanged) {
            Log.wtf("On back pressed", "changed");
            Intent intent = new Intent(SettingsNewActivity.this,
                    HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("fromSettings", true);
            MyApplication.fromSettings = true;

            startActivity(intent);
            finish();
        } else {
            SettingsNewActivity.this.finish();
        }

    }

    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(SettingsNewActivity.this, MoreActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SettingsNewActivity.this.startActivity(intent);
                SettingsNewActivity.this.finish();
                break;
            }
            case R.id.home: {

                if (themeChanged) {
                    Log.wtf("On back pressed", "changed");
                    intent = new Intent(SettingsNewActivity.this,
                            HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("fromSettings", true);
                    MyApplication.fromSettings = true;

                    startActivity(intent);
                    finish();
                } else {
                    intent.setClass(SettingsNewActivity.this, HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SettingsNewActivity.this.startActivity(intent);
                }


                break;
            }

        }
    }
}
