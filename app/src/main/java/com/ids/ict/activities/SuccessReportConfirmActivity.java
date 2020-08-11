package com.ids.ict.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.classes.LookUp;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class SuccessReportConfirmActivity extends Activity {

    Connection conn;
    int footerButton;
    String IssueType = "";
    String lang = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = Actions.setLocal(this);
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.reportsuccess);
        Actions.loadMainBar(this);
        ViewResizing.setPageTextResizing(this);
        Log.d("add favorite", "passed here");

        Typeface tf;
        final ImageView bacbtn = (ImageView) findViewById(R.id.backbtn);
        bacbtn.setVisibility(View.GONE);
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            tf = MyApplication.facePolarisMedium;
        } else {
            tf = MyApplication.faceDinar;
        }
        final Bundle mbundle = this.getIntent().getExtras();
        try {
            IssueType = mbundle.getString("IssueType", "1");
        } catch (Exception e) {
        }

        Log.wtf("Issue type",""+IssueType);

        TextView txt = (TextView) findViewById(R.id.textView2);
        txt.setTypeface(tf);
        Button send = (Button) findViewById(R.id.sharefb);
        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String urlToShare = get_consumerlink();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

                boolean facebookAppFound = false;
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook")) {
                        intent.setPackage(info.activityInfo.packageName);
                        facebookAppFound = true;
                        break;
                    }
                }

                if (!facebookAppFound) {
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }

                startActivity(intent);
            }
        });

        Button can = (Button) findViewById(R.id.sharetw);
        can.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessReportConfirmActivity.this, TwitterActivity.class);
                startActivity(intent);
            }
        });

        Button con = (Button) findViewById(R.id.cont_button_rep);
        //con.setTypeface(tf);
        con.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessReportConfirmActivity.this, ComplaintActivity.class);
                if (IssueType.equals("1")) {
                    MyApplication.lunched = -1;
                } else if (IssueType.equals("-1")) {
                    MyApplication.lunched = -1;
                } else {
                    MyApplication.lunched = -2;
                }
                startActivity(intent);
                SuccessReportConfirmActivity.this.finish();

            }
        });

        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            con.setBackground(ContextCompat.getDrawable(SuccessReportConfirmActivity.this, R.drawable.button_night));
        }

        if (IssueType.equals("-1")) {
            if (MyApplication.Lang.equals(MyApplication.ENGLISH))
                txt.setText(getLookup("37").nameen);
            else
                txt.setText(getLookup("37").namear);
            send.setVisibility(View.GONE);
            can.setVisibility(View.GONE);
        } else {

            if(IssueType.equals("2")){

                if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    txt.setText((getLookup("83").nameen) + "\n\n" + getLookup("75").nameen);

                } else {
                    txt.setText((getLookup("83").namear) + "\n\n" + getLookup("75").namear);
                }

            }else{
                if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    txt.setText(getLookup("73").nameen + "\n\n" + getLookup("74").nameen + "\n\n" + getLookup("75").nameen);

                } else {
                    txt.setText(getLookup("73").namear + "\n\n" + getLookup("74").namear + "\n\n" + getLookup("75").namear);
                }
            }

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

    private String get_consumerlink() {
        String name = null;
        try {

            URL url = new URL("" + MyApplication.link + MyApplication.general + "GetSocailMediaLinks?password=" + MyApplication.pass + "&language=en");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("SocialMedia");
            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                Element fstElmnt = (Element) node;
                NodeList nameList = fstElmnt.getElementsByTagName("Name");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                String id = ((Node) nameList.item(0)).getNodeValue();

                NodeList websiteList = fstElmnt.getElementsByTagName("Link");
                Element websiteElement = (Element) websiteList.item(0);
                websiteList = websiteElement.getChildNodes();
                String title = ((Node) websiteList.item(0)).getNodeValue();

                if (id.equals("Facebook")) {
                    name = title;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("gg " + e);
            String url1 = "" + MyApplication.link + "PostData.asmx/VerifyRegistration";
            com.ids.ict.classes.Connection conn = new com.ids.ict.classes.Connection(url1);

            try {
                String error_return = conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return name;
    }

    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SuccessReportConfirmActivity.this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        SuccessReportConfirmActivity.this.finish();
    }

    public void backTo(View v) {
        Intent intent = new Intent(SuccessReportConfirmActivity.this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        SuccessReportConfirmActivity.this.finish();
    }

    public String[] get_values() {

        int k = 1;
        TCTDbAdapter source = new TCTDbAdapter(this);
        source.open();
        //source.de
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

}



