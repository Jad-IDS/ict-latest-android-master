package com.ids.ict.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.MobileConfiguration;
import com.ids.ict.parser.GetMobileConfiguration;

import org.shipp.util.DatabaseHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static com.ids.ict.R.id.textview_ar;

public class LanguageActivity extends Activity {
    Connection conn;
    MyApplication myApp;
    // String eventsSource = ""+app.link+"GeneralServices.asmx/GetIssueTypes?";
    SharedPreferences mSharedPreferences, sharedPrefs;
    ProgressDialog progress;
    TextView txtEn, txtAr;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;
    Typeface tf;
    public ArrayList<MobileConfiguration> mobile;
    DatabaseHandler db = new DatabaseHandler(LanguageActivity.this);
    public static Activity mActivity;

    Button butEn , butAr ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Actions.setLocal(this);
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.activity_lang);
        myApp = (MyApplication) getApplicationContext();
        ViewResizing.setPageTextResizing(this);

        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;
        } else {
            tf = MyApplication.faceDinar;
        }
        mActivity = this;

        if (getIntent().hasExtra("reLogin")) {
            Boolean reLogin = getIntent().getExtras().getBoolean("reLogin");
            if(reLogin){
                Toast.makeText(this,this.getResources().getString(R.string.reLogin),Toast.LENGTH_LONG).show();
            }
        }


        final RelativeLayout main = (RelativeLayout) findViewById(R.id.langMainRL);
        butEn = (Button) findViewById(R.id.en_button);
        butAr = (Button) findViewById(R.id.ar_button);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        /*GetLookUpAsynck get = new GetLookUpAsynck();
        get.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/

        TextView tex_ar = (TextView) findViewById(textview_ar);
        TextView tex_en = (TextView) findViewById(R.id.textview_en);
        txtEn = (TextView) findViewById(R.id.txtEn);
        txtAr = (TextView) findViewById(R.id.txtAr);
        if (MyApplication.nightMod) {
            main.setBackgroundResource(R.drawable.splash_bg_nt);

            butEn.setBackground(ContextCompat.getDrawable(LanguageActivity.this, R.drawable.button_gray));
            butAr.setBackground(ContextCompat.getDrawable(LanguageActivity.this, R.drawable.button_gray));


            tex_ar.setTextColor(getResources().getColor(R.color.white));
            tex_ar.setTextColor(getResources().getColor(R.color.white));
            tex_ar.setTextColor(getResources().getColor(R.color.white));
            tex_en.setTextColor(getResources().getColor(R.color.white));
        }
//		tex_ar.setTypeface(MyApplication.faceDinarLight);
//		tex_en.setTypeface(MyApplication.facePolarisMedium);
//		tex_ar.setTypeface(MyApplication.faceDinar);
//		tex_ar.setTypeface(MyApplication.facePolarisMedium);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = mSharedPreferences.edit();
        final SharedPreferences.Editor editor = sharedPrefs.edit();

        butEn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyApplication.Lang = MyApplication.ENGLISH;
                edit.putString(getString(R.string.language_key), MyApplication.ENGLISH).apply();
                editor.putString(getString(R.string.language_key), MyApplication.ENGLISH).apply();

                if (Actions.isNetworkAvailable(LanguageActivity.this)) {

                    GetLookUpAsynck get = new GetLookUpAsynck();
                    get.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    // LanguageActivity.this.finish();
                } else {
                    Actions.onCreateBlockedDialog4(LanguageActivity.this, getString(R.string.nonetwork2));
                }

            }
        });

        butAr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                MyApplication.Lang = MyApplication.ARABIC;
                edit.putString(getString(R.string.language_key), MyApplication.ARABIC).apply();
                editor.putString(getString(R.string.language_key), MyApplication.ARABIC).apply();
                Log.wtf("languageact", MyApplication.Lang);


                if (Actions.isNetworkAvailable(LanguageActivity.this)) {


                    GetLookUpAsynck get = new GetLookUpAsynck();
                    get.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    /*Intent intent = new Intent(LanguageActivity.this,
                            AboutUsPagination.class);
                    intent.putExtra("from", "lang");
                    startActivity(intent);*/
                    //  LanguageActivity.this.finish();
                } else {
                    Actions.onCreateBlockedDialog2(LanguageActivity.this, getString(R.string.nonetwork));
                }


            }
        });


        tex_ar.setTypeface(MyApplication.faceDinar);
        txtAr.setTypeface(MyApplication.faceDinar);

    }


    public void backTo(View v) {
        Actions.backTo(this);
    }


    public class GetLookUpAsynck extends AsyncTask<Boolean, Void, Void> {
        SharedPreferences prefsEn;
        SharedPreferences.Editor editEn;
        Boolean haveError = false;


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            prefsEn = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
            editEn = prefsEn.edit();

            /*progress = ProgressDialog.show(LanguageActivity.this, "", "", true);
            progress.setCanceledOnTouchOutside(false);*/

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            butEn.setEnabled(false);
            butAr.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Boolean... params) {
            // TODO Auto-generated method stub
            URL url;
            //Connection.disableSSLCertificateChecking();

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
                haveError = true;
                e.printStackTrace();
                Log.wtf("GetLookUpAsynck error","MalformedURLException : " + e.getMessage());
            } catch (ParserConfigurationException e) {
                haveError = true;
                e.printStackTrace();
                Log.wtf("GetLookUpAsynck error","ParserConfigurationException : " + e.getMessage());
            } catch (SAXException e) {
                haveError = true;
                e.printStackTrace();
                Log.wtf("GetLookUpAsynck error","SAXException : " + e.getMessage());
            } catch (IOException e) {
                haveError = true;
                e.printStackTrace();
                Log.wtf("GetLookUpAsynck error","IOException : " + e.getMessage());
            } catch (Exception e) {
                haveError = true;
                e.printStackTrace();
                Log.wtf("GetLookUpAsynck error","Exception : " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //if(!start) {
            //progress.dismiss();
            //}
            //else
            //	startMyActivity();

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            if(haveError) {
                Actions.onCreateDialog1(LanguageActivity.this, getResources().getString(R.string.nonetwork2));

                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                butEn.setEnabled(true);
                butAr.setEnabled(true);
            }
            else{
                new GetMobileConfigurationTask().execute();
            }

        }
    }


    private class GetMobileConfigurationTask extends AsyncTask<Void, Void, ArrayList<MobileConfiguration>> {

        ProgressDialog pd;
        int version = -1, force = -1, needsUpdate = -1;
        boolean proceed;


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected ArrayList<MobileConfiguration> doInBackground(Void... params) {
            if (Actions.isNetworkAvailable(LanguageActivity.this)) {
                //Connection.disableSSLCertificateChecking();

                String url = MyApplication.link + MyApplication.general + "GetMobileConfiguration?key=";
                Log.wtf("GetMobileConfigurationTask","url : " + url);

                Connection conn = new Connection(url);

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


            for (int i = 0; i < v.size(); i++) {
                if (v.get(i).getKey().equalsIgnoreCase("androidVersion")) {
                    version = Integer.parseInt(v.get(i).getValue());
                }
                if (v.get(i).getKey().equalsIgnoreCase("AndroidForceUpdate")) {
                    force = Integer.parseInt(v.get(i).getValue());
                }
            }

            needsUpdate = Actions.CheckVersion(LanguageActivity.this, version, force);

            if (needsUpdate != 0) {

                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                if (needsUpdate == 1) {
                    if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                        onCreateDialogForPlayStoreNoForce(LanguageActivity.this, getLookup("93").namear);
                    } else {
                        onCreateDialogForPlayStoreNoForce(LanguageActivity.this, getLookup("93").nameen);
                    }
                } else {
                    //force update
                    if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                        onCreateDialogForPlayStore(LanguageActivity.this, getLookup("93").namear);
                    } else {
                        onCreateDialogForPlayStore(LanguageActivity.this, getLookup("93").nameen);
                    }
                }
            } else if(v.size() > 0){

                Intent intent = new Intent(LanguageActivity.this, AboutUsPagination.class);
                intent.putExtra("from", "lang");
                startActivity(intent);
            }

            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            butEn.setEnabled(true);
            butAr.setEnabled(true);

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

    public void onCreateDialogForPlayStore(final Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));
        TextView textView;
        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_layout,
                null);
        textView = (TextView) textEntryView.findViewById(R.id.dialogMsg);
        textView.setGravity(Gravity.CENTER);
        textView.setText(msg);
        builder.setTitle(activity.getResources().getString(R.string.update_title));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(textEntryView)

                // Add action buttons
                .setNegativeButton(activity.getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
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
        Dialog d = builder.create();
        d.setCancelable(false);
        d.getWindow().setLayout(
                (int) (activity.getWindow().peekDecorView().getWidth() * 0.9),
                (int) (activity.getWindow().peekDecorView().getHeight() * 0.9));
        // WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        // lp.copyFrom(d.getWindow().getAttributes());
        // lp.width = WindowManager.LayoutParams.FILL_PARENT;
        // lp.height = WindowManager.LayoutParams.FILL_PARENT;
        d.show();
        // d.getWindow().setAttributes(lp);
    }


    public void onCreateDialogForPlayStoreNoForce(final Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));
        TextView textView;
        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_layout,
                null);
        textView = (TextView) textEntryView.findViewById(R.id.dialogMsg);
        textView.setGravity(Gravity.CENTER);
        textView.setText(msg);
        builder.setTitle(activity.getResources().getString(R.string.update_title));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(textEntryView)

                // Add action buttons
                .setPositiveButton(activity.getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
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
            }
        });
        Dialog d = builder.create();
        d.setCancelable(false);
        d.getWindow().setLayout(
                (int) (activity.getWindow().peekDecorView().getWidth() * 0.9),
                (int) (activity.getWindow().peekDecorView().getHeight() * 0.9));

        d.show();

    }

}