package com.ids.ict.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.FlipAnimation;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.OnSwipeTouchListener;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.ids.ict.activities.AboutUsActivity.detailstext;
import static com.ids.ict.activities.ConsumerActivity.textdetails;

/**
 * Created by Bushra on 11/24/2016.
 */
public class AboutUsPagination extends Activity {

    ArrayList<String> arrayOfTerms = new ArrayList<String>();
    LinearLayout agreeLL;
    ViewPager viewpager;
    MyPagerAdapter adapter;
    private String textColor;
    private String lang;
    int positionOfPager = 0;
    String from = "";
    LinearLayout footer;
    RelativeLayout rel, rlMap;
    WebView terms1, terms2;
    int page=1;
    private String fontName;
    TextView title;
    WebView abouttextview;
    SharedPreferences mSharedPreferences, mshSharedPreferences;
    SharedPreferences.Editor edit;
    MyApplication app;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        lang = Actions.setLocal(this);

        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        app = (MyApplication) getApplicationContext();
        setContentView(R.layout.aboutus_pagination);
        ViewResizing.setPageTextResizing(this);
        from = getIntent().getExtras().getString("from");
        footer = (LinearLayout) findViewById(R.id.footer);
        mSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        edit = mSharedPreferences.edit();

        mshSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        MyApplication.Lang = mSharedPreferences.getString(getResources().getString(R.string.language_key),"");
        lang = Actions.setLocal(this);

        Log.wtf("Aboutus Pagination", MyApplication.Lang);

        final TextView next = (TextView) findViewById(R.id.next);
        title=(TextView)findViewById(R.id.titletxt);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rel = (RelativeLayout) findViewById(R.id.rel);
        rlMap = (RelativeLayout) findViewById(R.id.rlMap);

        final Button agree = (Button) findViewById(R.id.agree_button);
        final Button disagree = (Button) findViewById(R.id.disagree_button);
        agreeLL = (LinearLayout) findViewById(R.id.agreeLL);
        terms1 = (WebView) findViewById(R.id.terms1);
        abouttextview=(WebView)findViewById(R.id.abouttextview);
        terms2 = (WebView) findViewById(R.id.terms2);
        ImageView backbtn = (ImageView)findViewById(R.id.backbtn);
        backbtn.setVisibility(View.GONE);
        viewpager = (ViewPager) findViewById(R.id.pager);
        final PageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicatorDetailsNews);

        if (from.equals("lang")) {

            title.setText(getResources().getString(R.string.terms));
            //    agree.setAlpha(.5f);
            //   disagree.setAlpha(.5f);
            try {
                if (MyApplication.Lang.equals(MyApplication.ARABIC)) {

                    arrayOfTerms.add(getLookup("80").namear + "<br/><br/>" + getLookup("81").namear + "<br/><br/>" + getLookup("82").namear);
                    arrayOfTerms.add(getLookup("77").namear + "<br/><br/>" + getLookup("78").namear + "<br/><br/>" + getLookup("79").namear);

                } else if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    arrayOfTerms.add(getLookup("77").nameen + "<br/><br/>" + getLookup("78").nameen + "<br/><br/>" + getLookup("79").nameen);
                    arrayOfTerms.add(getLookup("80").nameen + "<br/><br/>" + getLookup("81").nameen + "<br/><br/>" + getLookup("82").nameen);
                }
            }catch (Exception e)  {
                e.printStackTrace();
            }
            // LoadWebViews();

            //  agreeLL.setVisibility(View.VISIBLE);
            //agree.setClickable(false);
            // disagree.setClickable(false);
            next.setClickable(true);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipCard();
                    if (positionOfPager == arrayOfTerms.size() - 1)
                        positionOfPager = positionOfPager - 1;
                    else
                        positionOfPager = positionOfPager + 1;
                    viewpager.setCurrentItem(positionOfPager);
                    if (positionOfPager == arrayOfTerms.size() - 1) {
                        //   next.setVisibility(View.VISIBLE);
                        next.setText(getResources().getString(R.string.previous));

                        //     agree.setClickable(true);
                        //   disagree.setClickable(true);


                    } else {
                        //  next.setVisibility(View.VISIBLE);
                        next.setText(getResources().getString(R.string.next));

                        //  agree.setClickable(false);
                        //  disagree.setClickable(false);
                    }
                }
            });

        } else {
            backbtn.setVisibility(View.VISIBLE);

            if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                backbtn.setImageResource(R.drawable.back_btn_en);
            }
            title.setVisibility(View.GONE);
            View v=(View)mIndicator;
            v.setVisibility(View.GONE);
            title.setText(getResources().getString(R.string.about_us));
            //  rel.setBackground(ContextCompat.getDrawable(AboutUsPagination.this,R.drawable.map));
            ImageView map =(ImageView)findViewById(R.id.map);
            map.setVisibility(View.VISIBLE);
            rlMap.setVisibility(View.VISIBLE);

            // map.setAlpha(.5f);
            String txt ="";
            try {
                if (MyApplication.Lang.equals(MyApplication.ARABIC)) {
                    //  arrayOfTerms.add(getLookup("65").namear);
                    // arrayOfTerms.add(getLookup("66").namear);
                    // arrayOfTerms.add(getLookup("67").namear);
                    //   arrayOfTerms.add(getLookup("68").namear);
                    txt = "\"" + getLookup("65").namear + "\"" + "\n\n" + "\"" + getLookup("66").namear + "\"" + "\n\n" + "\"" + getLookup("67").namear + "\""
                            + "\n\n" + "\"" + getLookup("68").namear + "\"" + "\n\n" + "\"" + getLookup("85").namear + "\"" + "\n\n" + "\"" + getLookup("86").namear + "\"";
                    arrayOfTerms.add(txt);
                } else if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    //   arrayOfTerms.add(getLookup("65").nameen);
                    // arrayOfTerms.add(getLookup("66").nameen);
                    // arrayOfTerms.add(getLookup("67").nameen);
                    // arrayOfTerms.add(getLookup("68").nameen);
                    txt = "\"" + getLookup("65").nameen + "\"" + "\n\n" + "\"" + getLookup("66").nameen + "\"" + "\n\n" + "\"" + getLookup("67").nameen + "\""
                            + "\n\n" + "\"" + getLookup("68").nameen + "\"" + "\n\n" + "\"" + getLookup("85").nameen + "\"" + "\n\n" + "\"" + getLookup("86").nameen + "\"";
                    arrayOfTerms.add(txt);
                }
            }
            catch (Exception e)
            {

            }
            footer.setVisibility(View.VISIBLE);
            //      LoadWebViewsAbout();
            next.setClickable(true);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipCard();

                    if (positionOfPager == arrayOfTerms.size() - 1)
                        positionOfPager = positionOfPager - 1;
                    else
                        positionOfPager = positionOfPager + 1;

                    viewpager.setCurrentItem(positionOfPager);
                    if (positionOfPager == arrayOfTerms.size() - 1) {
                        // next.setVisibility(View.VISIBLE);
                        next.setText(getString(R.string.previous));
                        //agreeLL.setVisibility(View.VISIBLE);
                        //  agree.setAlpha(1f);
                        //  disagree.setAlpha(1f);
                    } else {
                        //  next.setVisibility(View.VISIBLE);
                        next.setText(getString(R.string.next));
                        // agreeLL.setVisibility(View.GONE);
                        //   agree.setAlpha(.5f);
                        //  disagree.setAlpha(.5f);
                    }
                }
            });
            viewpager.setVisibility(View.GONE);


            title.setVisibility(View.GONE);

            /*abouttextview.setVisibility(View.VISIBLE);
            abouttextview.setText(txt);*/

            if (Actions.isNetworkAvailable(AboutUsPagination.this)) {
                ReadAboutUs read = new ReadAboutUs();
                read.execute();
            } else {
                if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {

                    textdetails = mSharedPreferences.getString("aboutusEn", "");
                }else{

                    textdetails = mSharedPreferences.getString("aboutusAr", "");
                }
                title.setVisibility(View.GONE);
                abouttextview.setVisibility(View.VISIBLE);
                abouttextview.setVerticalScrollBarEnabled(true);
                abouttextview.setScrollbarFadingEnabled(true);
                abouttextview.setBackgroundColor(0x00000000);
                abouttextview.getSettings().setJavaScriptEnabled(true);

                abouttextview.getSettings().setUseWideViewPort(true);
                Actions.LoadWebViewWithCustomFont2(textdetails, abouttextview);
            }
        }


        ViewResizing.setPageTextResizing(this);

        adapter = new MyPagerAdapter(AboutUsPagination.this, arrayOfTerms);
        viewpager.setAdapter(adapter);
        if(MyApplication.Lang.equals(MyApplication.ARABIC))
            viewpager.setCurrentItem(arrayOfTerms.size()-1);
        mIndicator.setViewPager(viewpager);
        LinearLayout nextll = (LinearLayout) findViewById(R.id.nextll);
        if (MyApplication.Lang.equals(MyApplication.ARABIC)) {
            // nextll.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            next.setGravity(Gravity.RIGHT);
        }
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                positionOfPager = position;
                mIndicator.setCurrentItem(positionOfPager);
                viewpager.setScrollBarFadeDuration(R.styleable.CirclePageIndicator_fillColor);
                if(MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    if (position == arrayOfTerms.size() - 1) {
                        // if (from.equals("lang"))
                        agreeLL.setVisibility(View.VISIBLE);
                        //  next.setVisibility(View.VISIBLE);
                        next.setText(getString(R.string.previous));
                        //  agree.setClickable(true);
                        //  disagree.setClickable(true);
                        // agree.setAlpha(1f);
                        // disagree.setAlpha(1f);


                    } else {
                        // if (from.equals("lang"))
                        agreeLL.setVisibility(View.INVISIBLE);
                        // next.setVisibility(View.VISIBLE);
                        next.setText(getString(R.string.next));
                        // agree.setClickable(false);
                        // disagree.setClickable(false);
                        // agree.setAlpha(.5f);
                        // disagree.setAlpha(.5f);
                    }
                }
                else
                {
                    if (position == arrayOfTerms.size() - 1) {
                        // if (from.equals("lang"))
                        agreeLL.setVisibility(View.VISIBLE);
                        //  next.setVisibility(View.VISIBLE);
                        next.setText(getString(R.string.previous));
                        //  agree.setClickable(true);
                        //  disagree.setClickable(true);
                        // agree.setAlpha(1f);
                        // disagree.setAlpha(1f);
                        agreeLL.setVisibility(View.INVISIBLE);
                        // next.setVisibility(View.VISIBLE);
                        next.setText(getString(R.string.next));

                    } else {
                        // if (from.equals("lang"))
                        agreeLL.setVisibility(View.VISIBLE);
                        //  next.setVisibility(View.VISIBLE);
                        next.setText(getString(R.string.previous));
                        // agree.setClickable(false);
                        // disagree.setClickable(false);
                        // agree.setAlpha(.5f);
                        // disagree.setAlpha(.5f);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //     final RelativeLayout main = (RelativeLayout) findViewById(R.id.termMainRL);
        Typeface tf = null;
        if (lang.equals(MyApplication.ENGLISH)) {
            tf = MyApplication.facePolarisMedium;
        } else {
            tf = MyApplication.faceDinar;
        }

        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);

            agree.setBackground(ContextCompat.getDrawable(AboutUsPagination.this, R.drawable.button_gray));
            disagree.setBackground(ContextCompat.getDrawable(AboutUsPagination.this, R.drawable.button_gray));

            footer.setBackgroundResource(R.drawable.footer_nt);
            rel.setBackgroundColor(getResources().getColor(R.color.nightBlue));

            agreeLL.setBackgroundColor(getResources().getColor(R.color.nightBlue));

            title.setBackgroundColor(getResources().getColor(R.color.white));
            textColor = "ffffff";
        } else
            textColor = "77777B";


        agree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Intent intent = new Intent(AboutUsPagination.this, RegisterActivity.class);

                Intent intent = new Intent(AboutUsPagination.this, QosTermsActivity.class);
                intent.putExtra("fromAboutUs",true);

                startActivity(intent);
                AboutUsPagination.this.finish();
                AboutUsPagination.this.finish();
            }
        });

        disagree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AboutUsPagination.this, LanguageActivity.class);
//                startActivity(intent);

                edit.putInt(getString(R.string.device_id), 0).apply();
                AboutUsPagination.this.finish();
            }
        });

        agree.setText(getResources().getString(R.string.agree_term));
        disagree.setText(getResources().getString(R.string.disagree_term));

    }


    public void topBarBack(View v) {
        finish();
    }


    public class ReadAboutUs extends AsyncTask<Void, Void, String> {
        //ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            //pd=ProgressDialog.show(AboutUsActivity.this, "", AboutUsActivity.this.getResources().getString(R.string.loading), false);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            final String a = get_terms();
            return a;
        }

        @Override
        protected void onPostExecute(String v) {
            detailstext = v;
            title.setVisibility(View.GONE);
            abouttextview.setVisibility(View.VISIBLE);
            abouttextview.setBackgroundColor(0x00000000);
            abouttextview.getSettings().setJavaScriptEnabled(true);
            abouttextview.setScrollbarFadingEnabled(true);
            abouttextview.getSettings().setUseWideViewPort(true);
            if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {

                edit.putString("aboutusEn", detailstext).apply();
            }else{

                edit.putString("aboutusAr", detailstext).apply();
            }
            Actions.LoadWebViewWithCustomFont2(detailstext, abouttextview);

            try {
                //pd.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {

            }
        }

    }


    private String get_terms() {
        String title = null;
        try {
            URL url;
            // com.ids.indyact.ViewResizing.setPageTextResizing(this);
            /*if (mSharedPreferences.getString(getString(R.string.language_key), "en").equals(MyApplication.ENGLISH)) {
                url = new URL("" + app.link + "GeneralServices.asmx/GetAboutUs?language=en&password=" + app.pass + "");
            } else {
                url = new URL("" + app.link + "GeneralServices.asmx/GetAboutUs?language=ar&password=" + app.pass + "");
            }*/
            if (MyApplication.Lang.equals(MyApplication.ARABIC)){
                url = new URL("" + app.link + "GeneralServices.asmx/GetAboutUs?language=ar&password=" + app.pass + "");
            }else{
                url = new URL("" + app.link + "GeneralServices.asmx/GetAboutUs?language=en&password=" + app.pass + "");
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList websiteList = doc.getElementsByTagName("string");
            Element websiteElement = (Element) websiteList.item(0);
            websiteList = websiteElement.getChildNodes();
            title = ((Node) websiteList.item(0)).getNodeValue();
            /** Assign textview array lenght by arraylist size */
            //	name = new TextView[nodeList.getLength()];
            //	website = new TextView[nodeList.getLength()];
            //category = new TextView[nodeList.getLength()];
        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);

        }
        return title;
    }


    private void LoadWebViews() {
        TextView see1, see2;


        see1 = (TextView) findViewById(R.id.see);
        see2 = (TextView) findViewById(R.id.see2);
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            fontName = "file:///android_asset/fonts/Galaxie_Polaris_Medium.otf";
            see1.setGravity(Gravity.LEFT);
            see2.setGravity(Gravity.LEFT);
        } else {
            fontName = "file:///android_asset/fonts/GE_Dinar_One_Medium.otf";
            see2.setGravity(Gravity.RIGHT);
            see1.setGravity(Gravity.RIGHT);
        }
        String txt1 = "",txt2="";
        if (MyApplication.Lang.equals(MyApplication.ARABIC)) {
            txt1 = getLookup("63").namear;
            txt2 = getLookup("64").namear;
            terms1.getSettings().setJavaScriptEnabled(true);

            terms1.loadDataWithBaseURL("file:///android_asset/", "<head><style type=\"text/css\">@font-face {font-family: MyFont;\n" +
                    "    src: url(\"" + fontName + "\")\n" +
                    "}\n" +
                    "body {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +

                    "p {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +
                    "</style><body dir=\"rtl\">" + txt1 + "</body></html>", "text/html", "UTF-8", "");
            terms2.getSettings().setJavaScriptEnabled(true);

            terms2.loadDataWithBaseURL("file:///android_asset/", "<head><style type=\"text/css\">@font-face {font-family: MyFont;\n" +
                    "    src: url(\"" + fontName + "\")\n" +
                    "}\n" +
                    "body {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +

                    "p {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +
                    "</style><body dir=\"rtl\">" + txt2 + "</body></html>", "text/html", "UTF-8", "");
        } else {
            txt1 = getLookup("63").nameen;
            txt2 = getLookup("64").nameen;
            terms2.getSettings().setJavaScriptEnabled(true);

            terms2.loadDataWithBaseURL("file:///android_asset/", "<head><style type=\"text/css\">@font-face {font-family: MyFont;\n" +
                    "    src: url(\"" + fontName + "\")\n" +
                    "}\n" +
                    "body {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +

                    "p {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +
                    "</style><body>" + txt2 + "</body></html>", "text/html", "UTF-8", "");
            terms1.loadDataWithBaseURL("file:///android_asset/", "<head><style type=\"text/css\">@font-face {font-family: MyFont;\n" +
                    "    src: url(\"" + fontName + "\")\n" +
                    "}\n" +
                    "body {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +

                    "p {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +
                    "</style><body>" + txt1 + "</body></html>", "text/html", "UTF-8", "");
            terms1.getSettings().setJavaScriptEnabled(true);
        }
        final TextView next = (TextView) findViewById(R.id.next);
        rel = (RelativeLayout) findViewById(R.id.rel);
        final Button agree = (Button) findViewById(R.id.agree_button);
        final Button disagree = (Button) findViewById(R.id.disagree_button);
        terms2.setOnTouchListener(new OnSwipeTouchListener(AboutUsPagination.this) {

            public void onSwipeRight() {
                flipCard();
                page=1;
                //  next.setVisibility(View.VISIBLE);
                next.setText(getString(R.string.next));
                //  agree.setClickable(false);
                //  disagree.setClickable(false);
                //  agree.setAlpha(.5f);
                // disagree.setAlpha(.5f);

            }
            public void onSwipeLeft() {
                flipCard();
                page=1;
                //    next.setVisibility(View.VISIBLE);
                next.setText(getString(R.string.next));
                //  agree.setClickable(false);
                // disagree.setClickable(false);
                // agree.setAlpha(.5f);
                //  disagree.setAlpha(.5f);
            }


        });
        terms1.setOnTouchListener(new OnSwipeTouchListener(AboutUsPagination.this) {

            public void onSwipeRight() {
                flipCard();
                //   next.setVisibility(View.VISIBLE);
                next.setText(getString(R.string.previous));
                // agree.setClickable(true);
                // disagree.setClickable(true);
                //  agree.setAlpha(1f);
                //  disagree.setAlpha(1f);
                page=2;

            }
            public void onSwipeLeft() {
                flipCard();
                //  next.setVisibility(View.VISIBLE);
                next.setText(getString(R.string.previous));
                // agree.setClickable(true);
                // disagree.setClickable(true);
                //   agree.setAlpha(1f);
                //  disagree.setAlpha(1f);
                page=2;

            }


        });


    }


    private void LoadWebViewsAbout() {
        TextView see1, see2;
        see1 = (TextView) findViewById(R.id.see);
        see2 = (TextView) findViewById(R.id.see2);
        see1.setVisibility(View.GONE);
        see2.setVisibility(View.GONE);
        String txt1 = "",txt2="",txt3="",txt4="";
        if (MyApplication.Lang.equals(MyApplication.ARABIC)) {
            txt1 = getLookup("65").namear;
            txt2 = getLookup("66").namear;
            txt3=getLookup("67").namear;
            txt4= getLookup("68").namear;
            terms1.getSettings().setJavaScriptEnabled(true);

            terms1.loadDataWithBaseURL("file:///android_asset/", "<head><style type=\"text/css\">@font-face {font-family: MyFont;\n" +
                    "    src: url(\"" + fontName + "\")\n" +
                    "}\n" +
                    "body {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +

                    "p {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +
                    "</style><body dir=\"rtl\">" + txt1 + "<br/><br/>" + txt2+ "</body></html>", "text/html", "UTF-8", "");
            terms1.getSettings().setJavaScriptEnabled(true);

            terms2.loadDataWithBaseURL("file:///android_asset/", "<head><style type=\"text/css\">@font-face {font-family: MyFont;\n" +
                    "    src: url(\"" + fontName + "\")\n" +
                    "}\n" +
                    "body {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +

                    "p {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +
                    "</style><body dir=\"rtl\">" + txt3+ "<br/><br/>" + txt4+  "</body></html>", "text/html", "UTF-8", "");
        } else {
            txt1 = getLookup("65").nameen;
            txt2 = getLookup("66").nameen;
            txt3 = getLookup("67").nameen;
            txt4 = getLookup("68").nameen;
            terms1.getSettings().setJavaScriptEnabled(true);

            terms1.loadDataWithBaseURL("file:///android_asset/", "<head><style type=\"text/css\">@font-face {font-family: MyFont;\n" +
                    "    src: url(\"" + fontName + "\")\n" +
                    "}\n" +
                    "body {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +

                    "p {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +
                    "</style><body dir=\"ltr\">"+ txt1 + "<br/><br/>" + txt2+ "</body></html>", "text/html", "UTF-8", "");
            terms2.loadDataWithBaseURL("file:///android_asset/", "<head><style type=\"text/css\">@font-face {font-family: MyFont;\n" +
                    "    src: url(\"" + fontName + "\")\n" +
                    "}\n" +
                    "body {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +

                    "p {\n" +
                    "    font-family: MyFont;\n" +
                    "    font-size: medium;\n" +
                    "    color: #77777B;\n" +
                    "    text-align: center;\n" +
                    "}\n" +
                    "</style><body dir=\"rtl\">" + txt3 + "<br/><br/>" + txt4+  "</body></html>", "text/html", "UTF-8", "");
            terms2.getSettings().setJavaScriptEnabled(true);
        }
        terms2.setOnTouchListener(new OnSwipeTouchListener(AboutUsPagination.this) {

            public void onSwipeRight() {
                flipCard();
            }
            public void onSwipeLeft() {
                flipCard();
            }


        });
        terms1.setOnTouchListener(new OnSwipeTouchListener(AboutUsPagination.this) {

            public void onSwipeRight() {
                flipCard();
            }
            public void onSwipeLeft() {
                flipCard();
            }


        });
    }


    private void flipCard() {
        View rootLayout = (View) findViewById(R.id.main_activity_root);
        View cardFace = (View) findViewById(R.id.main_activity_card_face);
        View cardBack = (View) findViewById(R.id.main_activity_card_back);

        FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);

        if (cardFace.getVisibility() == View.GONE) {
            flipAnimation.reverse();
        }
        rootLayout.startAnimation(flipAnimation);
    }


    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(AboutUsPagination.this, MoreActivity.class);
                AboutUsPagination.this.startActivity(intent);
                break;
            }
            case R.id.home: {
                intent.setClass(AboutUsPagination.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                AboutUsPagination.this.startActivity(intent);
                AboutUsPagination.this.finish();
                break;
            }

        }
    }


    public void backTo(View v) {
        Actions.backTo(this);
    }


    public LookUp getLookup(String id) {
        SharedPreferences mshaPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        LookUp look = new LookUp();
        Gson gson = new Gson();
        String json = mshaPreferences.getString(id, "");
        look = gson.fromJson(json, LookUp.class);

        return look;
    }


    public void showpopupaboutus(View v) {
        Launching lan = new Launching();
        lan.execute();

    }


    protected class Launching extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            //  pd=ProgressDialog.show(AboutUsPagination.this, "","",true );
        }

        @Override
        protected String doInBackground(Void... a) {
            String result = null;
            try {
                URL url;
                if (lang.equals(MyApplication.ENGLISH)) {
                    url = new URL("" + MyApplication.link + MyApplication.general + "/GetTerms?language=en&password=");
                } else {
                    url = new URL("" + MyApplication.link + MyApplication.general + "/GetTerms?language=ar&password=");
                }
                Log.wtf("Launching","url : " + url.toString());

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList websiteList = doc.getElementsByTagName("string");
                Element websiteElement = (Element) websiteList.item(0);
                websiteList = websiteElement.getChildNodes();
                result = ((Node) websiteList.item(0)).getNodeValue();

            } catch (Exception e) {
                System.out.println("XML Pasing Excpetion = " + e);
                String url1 = "" + MyApplication.link + MyApplication.post + "VerifyRegistration";
                Connection conn = new Connection(url1);

                try {
                    String error_return = conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            String text = "";

            //    ViewResizing.textResize(AboutUsPagination.this,details,(int)details.getTextSize());
            //showPopup(result);


            startActivity(new Intent(AboutUsPagination.this,TermsConditionsPopup.class).putExtra("result",result));
        }
    }


    private void showPopup(String result) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        //AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo));

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup, null);
        dialogView.setBackgroundColor(Color.WHITE);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();

        WebView wv = (WebView) dialogView.findViewById(R.id.wv);
        TextView x = (TextView) dialogView.findViewById(R.id.x);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        wv.getSettings().setJavaScriptEnabled(true);

        //   wv.loadDataWithBaseURL("", "<html><body style=\"text-align:center;\">" + result + "</body></html>", "text/html", "UTF-8", "");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        Actions.LoadWebViewwithCustomFont(result, wv);
//        alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        //      alertDialog.getWindow().setDimAmount(0.0f);
        alertDialog.setView(dialogView, 0, 0, 0, 0);
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alertDialog.getWindow().setLayout(MyApplication.screenWidth - 3, ViewPager.LayoutParams.WRAP_CONTENT);
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

            if(MyApplication.nightMod){
                see.setTextColor(ContextCompat.getColor(AboutUsPagination.this,R.color.darkBlue));
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

}
