package com.ids.ict.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.Profile;
import com.ids.ict.R;
import com.ids.ict.classes.ShareHelper;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.adapters.SatisfactionListArrayAdapter;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.Provider;
import com.ids.ict.classes.Satisfaction;
import com.ids.ict.parser.ShareSpeedTestTextXMLPullParserHandler;
import com.ids.ict.parser.SpeedTestResultParser;
import com.ids.ict.parser.SpeedTestSPParser;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SpeedTestFormActivity extends Activity implements TextWatcher {
    // asynctask to read market state and time from net

    int footerButton;
    String lang = "";

    MyApplication app;
    EditText email, comment;
    EditText phone;
    Bundle bundle;
    TextView download, upload, uploadtxt, downloadtxt, testednumber, tvSpeedNote, resultsHeader, satisfiedHeader;
    ArrayAdapter<Satisfaction> providerSpinnerArrayAdapter;
    Spinner providerSpinner;
    ListView satisfactionList;
    SatisfactionListArrayAdapter satisfactionAdapter;
    View form, summary, shareTestRow, submitCancelRow;
    // summary values
    TextView downloadSummary, uploadSummary, satisfactionSummary, commentxt,
            commentSummary, providertxt, emailtxt, commenttxt, sumdownloadtxt, sumuploadtxt, experiencetxt, ispSummary, connectionSummary, ispSummaryForm, connectionSummaryForm;
    TextView ispText, connectionText, ispTextForm, connectionTextForm;
    String shareText;
    Profile profile;
    String longitude, latitude;
    Typeface tf;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplicationContext();
        lang = Actions.setLocal(this);

       // TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.speedtest_form_activity);
        Actions.loadMainBar(this);
        ViewResizing.setPageTextResizing(this);
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;


        } else {

            tf = MyApplication.faceDinar;

        }

        resultsHeader = (TextView) findViewById(R.id.resultsHeader);
        satisfiedHeader = (TextView) findViewById(R.id.satisfiedHeader);
        uploadtxt = (TextView) findViewById(R.id.uploadtxt);
        tvSpeedNote = (TextView) findViewById(R.id.tvSpeedNote);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        downloadtxt = (TextView) findViewById(R.id.downloadtxt);
        testednumber = (TextView) findViewById(R.id.testnumber);
        providertxt = (TextView) findViewById(R.id.providertxt);
        emailtxt = (TextView) findViewById(R.id.emailtxt);
        commenttxt = (TextView) findViewById(R.id.commenttxt2);
        sumdownloadtxt = (TextView) findViewById(R.id.sumdownloadtxt);
        sumuploadtxt = (TextView) findViewById(R.id.sumuploadtxt);
        experiencetxt = (TextView) findViewById(R.id.experiencetxt);
        commentxt = (TextView) findViewById(R.id.commenttxt);
        upload = (TextView) findViewById(R.id.upload_speed);
        phone = (EditText) findViewById(R.id.phone);
        download = (TextView) findViewById(R.id.dowload_speed);

        ispText = (TextView) findViewById(R.id.ispText);
        ispTextForm = (TextView) findViewById(R.id.ispTextForm);
        connectionText = (TextView) findViewById(R.id.connectiontxt);
        connectionTextForm = (TextView) findViewById(R.id.connectiontxt_form);

        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
            footer.setBackgroundResource(R.drawable.footer_nt);

            Button b = (Button) findViewById(R.id.go_to_summary);
            //b.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            b.setBackground(ContextCompat.getDrawable(SpeedTestFormActivity.this, R.drawable.button_night));


            Button b1 = (Button) findViewById(R.id.submit);
            //b1.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            b1.setBackground(ContextCompat.getDrawable(SpeedTestFormActivity.this, R.drawable.button_night));


            Button b2 = (Button) findViewById(R.id.back_to_form);
            //b2.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            b2.setBackground(ContextCompat.getDrawable(SpeedTestFormActivity.this, R.drawable.button_night));

            Button b3 = (Button) findViewById(R.id.repeat_test);
            //b3.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            b3.setBackground(ContextCompat.getDrawable(SpeedTestFormActivity.this, R.drawable.button_night));

            Button b4 = (Button) findViewById(R.id.share);
            //b3.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            b4.setBackground(ContextCompat.getDrawable(SpeedTestFormActivity.this, R.drawable.button_night));

            /*RelativeLayout b4 = (RelativeLayout) findViewById(R.id.share);
            //b4.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            b4.setBackground(ContextCompat.getDrawable(SpeedTestFormActivity.this, R.drawable.buttonnight));*/

            satisfiedHeader.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));
            resultsHeader.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));
            commentxt.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));

            sumdownloadtxt.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));
            sumuploadtxt.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));
            experiencetxt.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));
            commenttxt.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));

            ispText.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));
            connectionText.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));

            ispTextForm.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));
            connectionTextForm.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));


            /*commentxt.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));
            upload.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));
            download.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));
            phone.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));
            commenttxt.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));
            tvSpeedNote.setTextColor(ContextCompat.getColor(SpeedTestFormActivity.this, R.color.nightBlue));*/
        }

        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);
        } else {
        }

        email = (EditText) findViewById(R.id.email);

        comment = (EditText) findViewById(R.id.comment);

        //	email.addTextChangedListener(this);

        comment.addTextChangedListener(this);
        // set phone

        TCTDbAdapter sour = new TCTDbAdapter(this);
        sour.open();
        ArrayList<Profile> arr = sour.getAllProfiles();
        try {
            profile = arr.get(0);
            phone.setText(arr.get(0).getnum());
        } catch (Exception e) {
            e.printStackTrace();
            profile = new Profile();
            phone.setText("");
        }
        sour.close();
        // set speed up/down
        bundle = getIntent().getExtras();
        String up = bundle.getString("upload");
        String down = bundle.getString("download");

        upload.setText(up);
        download.setText(down);
        upload.setTypeface(MyApplication.facePolarisMedium);
        download.setTypeface(MyApplication.facePolarisMedium);

        // get longitude, latitude
        longitude = Double.toString(bundle.getDouble("longitude"));
        latitude = Double.toString(bundle.getDouble("latitude"));
        // set provider spinner values
        FillProvider fillProvider = new FillProvider();
        fillProvider.execute();
        FillSatisfaction fillSatisfaction = new FillSatisfaction();
        fillSatisfaction.execute();

        // control views display and hide, on start default from xml, changed in
        // submit function
        form = findViewById(R.id.form);
        summary = findViewById(R.id.summary);
        shareTestRow = findViewById(R.id.share_test_row);
        submitCancelRow = findViewById(R.id.submit_cancel_row);

        // summary feilds
        downloadSummary = (TextView) findViewById(R.id.dowload_speed_summary);
        uploadSummary = (TextView) findViewById(R.id.upload_speed_summary);
        satisfactionSummary = (TextView) findViewById(R.id.experience_summary);
        commentSummary = (TextView) findViewById(R.id.comment_summary);

        ispSummary = (TextView) findViewById(R.id.isp_summary);
        connectionSummary = (TextView) findViewById(R.id.connection_summary);

        ispSummaryForm = (TextView) findViewById(R.id.isp_summary_form);
        connectionSummaryForm = (TextView) findViewById(R.id.connection_summary_form);

        /*setNightColor(uploadtxt);
        setNightColor(downloadtxt);
        setNightColor(testednumber);
        setNightColor(providertxt);
        setNightColor(emailtxt);
        setNightColor(commenttxt);
        setNightColor(sumdownloadtxt);
        setNightColor(sumuploadtxt);
        setNightColor(experiencetxt);
        setNightColor(commentxt);
        setNightColor(tvSpeedNote);*/
        setNightColor(resultsHeader);
        setNightColor(satisfiedHeader);
        setNightColor(commentxt);
        setNightColor(sumdownloadtxt);
        setNightColor(sumuploadtxt);
        setNightColor(experiencetxt);
        setNightColor(commenttxt);
        setNightColor(ispText);
        setNightColor(connectionText);
        setNightColor(ispTextForm);
        setNightColor(connectionTextForm);

        if (MyApplication.Lang.equals(MyApplication.ARABIC))
            tvSpeedNote.setText(getLookup("84").namear);
        else
            tvSpeedNote.setText(getLookup("84").nameen);


        downloadSummary.setTypeface(MyApplication.facePolarisMedium);
        uploadSummary.setTypeface(MyApplication.facePolarisMedium);
        ispSummary.setTypeface(MyApplication.facePolarisMedium);
        connectionSummary.setTypeface(MyApplication.facePolarisMedium);

        ispSummary.setText(MyApplication.isp);
        connectionSummary.setText(MyApplication.connectionType);

        ispSummaryForm.setTypeface(MyApplication.facePolarisMedium);
        connectionSummaryForm.setTypeface(MyApplication.facePolarisMedium);

        ispSummaryForm.setText(MyApplication.isp);
        connectionSummaryForm.setText(MyApplication.connectionType);
    }

    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }

    public void backTo(View v) {
        if(form.getVisibility()==View.VISIBLE)
            finish();
        else {
            form.setVisibility(View.VISIBLE);
            tvSpeedNote.setVisibility(View.VISIBLE);
            summary.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Actions.setLocal(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
        checkError();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    public void checkError() {
        Actions.setLocal(this);// }
        String emailText = email.getText().toString().trim();

        if (!Actions.isValidEmailAddress(emailText))
            email.setError(Actions.errorMessage(getResources().getString(
                    R.string.email_error_message)));
        else
            email.setError(null);

    }

    public void submit(View v) {
        int id = v.getId();
        String providerID;
        switch (id) {
            case R.id.go_to_summary: {


                try {

                    providerID = ((Provider) providerSpinner.getSelectedItem()).getId();
                } catch (Exception e) {
                    providerID = "0";
                }

                // checkError();
                if (providerSpinner.getSelectedItemPosition() == 0) {
                    Actions.showDialog(this, getResources().getString(R.string.selectprovidererror), false);
                    Log.wtf("providerID", "id: " + providerID);
                } else {
                    // hide form show summary and display entered data
                    downloadSummary.setText(download.getText());
                    uploadSummary.setText(upload.getText());
                    satisfactionSummary.setText(satisfactionAdapter
                            .getSelectedSatisfaction().getValue());
                    commentSummary.setText(comment.getText());
                    form.setVisibility(View.GONE);
                    tvSpeedNote.setVisibility(View.GONE);
                    summary.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(comment.getWindowToken(), 0);
                    Log.wtf("providerID", "id: " + providerID);
                }
                break;
            }

            case R.id.back_to_form: {
                // hide summary and show form
                form.setVisibility(View.VISIBLE);
                summary.setVisibility(View.GONE);
                break;
            }
            case R.id.submit: {
                // post form and show share pannel+get share text
                ShareSpeedTestTextTask getText = new ShareSpeedTestTextTask();
                //getText.execute();

                PostSpeedTestFeedback post = new PostSpeedTestFeedback();
                post.execute();
                break;
            }
            case R.id.share: {
                // share function
                String oldUrl = "http://arsel.qa/";
                String newUrl = "https://play.google.com/store/apps/details?id=com.ids.ict&hl=en";


                if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ARABIC)) {
                    shareText = getLookup("91").namear ;
                } else {
                    shareText = getLookup("91").nameen ;
                }



                String facebookBody = newUrl;//"http://arsel.qa/";
                String twitterBody = shareText + "\n"+newUrl;//\nhttp://arsel.qa/";
                String htmlText = shareText + "\n"+newUrl;//\nhttp://arsel.qa/";
                String textBody = shareText + "\n"+newUrl;//\nhttp://arsel.qa/";
                ShareHelper mShareHelper = new ShareHelper(this, "", textBody,
                        htmlText, twitterBody, facebookBody);
                mShareHelper.share();
                break;
            }
            case R.id.repeat_test: {
                // finish activity to show speed test again
                this.finish();
                break;
            }
        }

    }

    protected class FillSatisfaction extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;
        ArrayList<Satisfaction> result = new ArrayList<Satisfaction>();

        @Override
        protected void onPreExecute() {
           /* pd = ProgressDialog.show(
                    SpeedTestFormActivity.this,
                    "",
                    SpeedTestFormActivity.this.getResources().getString(
                            R.string.loading), false);*/

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);


            providerSpinner = (Spinner) findViewById(R.id.provider);
        }

        @Override
        protected String doInBackground(Void... a) {
//            //Connection.disableSSLCertificateChecking();
////			String url = getResources().getString(R.string.serviceLink)
////					+ getResources().getString(R.string.satisfactionLink);
//            String url = MyApplication.link + MyApplication.general
//                    + "GetSpeedTestSatisfactions?Language=" + lang;
//
//            Connection conn = new Connection(url);
//
//            if (!conn.hasError()) {
//                SatisfactionXMLPullParserHandler parser = new SatisfactionXMLPullParserHandler();
//
//                result = parser.parse(conn.getInputStream());
//            }
            return "";

        }

        @Override
        protected void onPostExecute(String r) {
            // fill satisfaction spinner
            Satisfaction sat1 = new Satisfaction();
            sat1.setId(getLookup("59").id);
            if (MyApplication.Lang.equals(MyApplication.ENGLISH))
                sat1.setValue(getLookup("59").nameen);
            else
                sat1.setValue(getLookup("59").namear);
            result.add(sat1);
            Satisfaction sat2 = new Satisfaction();
            sat2.setId(getLookup("60").id);
            if (MyApplication.Lang.equals(MyApplication.ENGLISH))
                sat2.setValue(getLookup("60").nameen);
            else
                sat2.setValue(getLookup("60").namear);
            result.add(sat2);
            Satisfaction sat3 = new Satisfaction();
            sat3.setId(getLookup("62").id);
            if (MyApplication.Lang.equals(MyApplication.ENGLISH))
                sat3.setValue(getLookup("62").nameen);
            else
                sat3.setValue(getLookup("62").namear);
            result.add(sat3);
            satisfactionList = (ListView) findViewById(R.id.experience_list);

            satisfactionAdapter = new SatisfactionListArrayAdapter(
                    SpeedTestFormActivity.this, result);

            satisfactionList.setAdapter(satisfactionAdapter);
            setListViewHeightBasedOnChildren(satisfactionList);

            try {

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);


                //pd.dismiss();
            } catch (Exception e) {

            }

        }
    }

    protected class FillProvider extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;
        ArrayList<Provider> result;
        String lang = "en";

        @Override
        protected void onPreExecute() {


           /* pd = ProgressDialog.show(
                    SpeedTestFormActivity.this,
                    "",
                    SpeedTestFormActivity.this.getResources().getString(
                            R.string.loading), false);*/


            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);


            providerSpinner = (Spinner) findViewById(R.id.provider);
            if (MyApplication.Lang.equals(MyApplication.ENGLISH))
                lang = "en";
            else
                lang = "ar";

        }

        @Override
        protected String doInBackground(Void... a) {
            //Connection.disableSSLCertificateChecking();
//			String url = getResources().getString(R.string.mapLink)
//					+ getResources().getString(R.string.providerLink);
            String url = MyApplication.link + MyApplication.map
                    + "GetServiceProviders?Language=" + lang;

            Connection conn = new Connection(url);
            conn.readWebPage();
            if (!conn.hasError()) {
                SpeedTestSPParser parser = new SpeedTestSPParser(
                        getString(R.string.ServiceProviderChoose));

                result = parser.parse(conn.getInputStream());
            }
            return "";

        }

        @Override
        protected void onPostExecute(String r) {
            // fill provider spinner

            ArrayAdapter<Provider> adapter = new ArrayAdapter<Provider>(
                    SpeedTestFormActivity.this, R.layout.spinner_text) {

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View v = convertView;
                    if (v == null) {
                        Context mContext = this.getContext();
                        LayoutInflater vi = (LayoutInflater) mContext
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        v = vi.inflate(R.layout.spinner_list_text, null);
                        v.setTag(this.getItem(position).getId());
                    }
                    TextView tv = (TextView) v.findViewById(R.id.TextView01);
                    tv.setText(this.getItem(position).getName());
                    tv.setTypeface(tf);
                    // ViewResizing.setListRowTextResizing(v,ComplainFormActivity.this);
                    return v;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        Context mContext = this.getContext();
                        LayoutInflater vi = (LayoutInflater) mContext
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = vi
                                .inflate(R.layout.spinner_item_day_en, null);
                    }
                    TextView tv = (TextView) convertView
                            .findViewById(R.id.TextView01);
                    tv.setTypeface(tf);
                    tv.setText(this.getItem(position).getName());
                    if (MyApplication.nightMod) {
                        ImageView Iv = (ImageView) convertView
                                .findViewById(R.id.spinnerArrow);
                        Iv.setImageResource(R.drawable.arrow_spinner_nt);
                    }
                    ViewResizing.setListRowTextResizing(convertView, SpeedTestFormActivity.this);
                    return convertView;
                }
            };
            adapter.setDropDownViewResource(R.layout.spinner_list_text);
            providerSpinner.setAdapter(adapter);
            for (int i = 0; i < result.size(); i++) {

                adapter.add(result.get(i));
            }

            adapter.notifyDataSetChanged();
            // return adapter;


            try {
                //pd.dismiss();


                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

            } catch (Exception e) {

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

    public void setNightColor(TextView v) {
        if (MyApplication.nightMod)
            v.setTextColor(getResources().getColor(R.color.nightBlue));
        v.setTypeface(tf);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop()
                + listView.getPaddingBottom();
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
                MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you
                // won't get measured height at all. The listitem needs to be
                // drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void topBarBack(View v) {

        if(form.getVisibility()==View.VISIBLE)
            finish();
        else {
            form.setVisibility(View.VISIBLE);
            tvSpeedNote.setVisibility(View.VISIBLE);
            summary.setVisibility(View.GONE);
        }
    }

    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(SpeedTestFormActivity.this, MoreActivity.class);
                SpeedTestFormActivity.this.startActivity(intent);
                break;
            }
            case R.id.home: {
                intent.setClass(SpeedTestFormActivity.this, HomePageActivity.class);
                SpeedTestFormActivity.this.startActivity(intent);
                break;
            }

        }
    }

    protected class ShareSpeedTestTextTask extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... a) {
            //Connection.disableSSLCertificateChecking();
//			String url = getResources().getString(R.string.serviceLink)
//					+ getResources().getString(R.string.shareSpeedTestLink);

            String url;
            if (MyApplication.Lang.equals(MyApplication.ARABIC)){
                url = MyApplication.link + MyApplication.general  + "GetSocialMediaSharingTextForSpeedtest?Language=ar";
            }else{
                url = MyApplication.link + MyApplication.general  + "GetSocialMediaSharingTextForSpeedtest?Language=en";
            }


            Connection conn = new Connection(url);

            if (!conn.hasError()) {
                ShareSpeedTestTextXMLPullParserHandler parser = new ShareSpeedTestTextXMLPullParserHandler();

                shareText = parser.parse(conn.getInputStream());
            }
            return "";

        }

        @Override
        protected void onPostExecute(String r) {

        }
    }

    protected class PostSpeedTestFeedback extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;
        String deviceName, ipAddress, emailText, downloadText, uploadText, commentText, providerId, res = "";

        @Override
        protected void onPreExecute() {
            /*pd = ProgressDialog.show(
                    SpeedTestFormActivity.this,
                    "",
                    SpeedTestFormActivity.this.getResources().getString(
                            R.string.loading), false);*/

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            providerSpinner = (Spinner) findViewById(R.id.provider);
            emailText = email.getText().toString();
            downloadText = download.getText().toString();
            uploadText = upload.getText().toString();
            commentText = comment.getText().toString();

            try {

                providerId = ((Provider) providerSpinner.getSelectedItem()).getId();
            } catch (Exception e) {
                providerId = "0";
            }
        }

        @Override
        protected String doInBackground(Void... a) {
            //Connection.disableSSLCertificateChecking();
            // get model and ipaddress
            deviceName = android.os.Build.MODEL;
            try {
                InetAddress ip = Actions.ip();
                ipAddress = ip.toString();

            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // URLEncoder.encode(searchText, "utf-8");
            String url = MyApplication.link + MyApplication.post
                    + "SendSpeedTestReport?";
            try {
                url = url
                        + "mobileNumber="
                        + URLEncoder.encode(profile.getnum(), "utf-8")
                        + "&"
                        + "qatarId="
                        + URLEncoder.encode(profile.getqatarID(), "utf-8")
                        + "&"
                        + "email="
                        + URLEncoder
                        .encode(emailText, "utf-8")
                        + "&"
                        + "creationDate="
                        + URLEncoder.encode(new SimpleDateFormat("MM-dd-yyyy",
                        Locale.ENGLISH).format(Calendar.getInstance()
                        .getTime()), "utf-8")
                        + "&"
                        + "ipAddress="
                        + URLEncoder.encode(ipAddress.substring(1), "utf-8")
                        + "&"
                        + "mobileDevice="
                        + URLEncoder.encode(deviceName, "utf-8")
                        + "&"
                        + "comments="
                        + URLEncoder.encode(commentText,
                        "utf-8")
                        + "&"
                        + "locationX="
                        + URLEncoder.encode(longitude, "utf-8")
                        + "&"
                        + "locationY="
                        + URLEncoder.encode(latitude, "utf-8")
                        + "&"
                        + "speedTestSatisfaction="
                        + satisfactionAdapter.getSelectedSatisfaction().getId()
                        + "&"
                        + "downloadResult="
                        + downloadText.replaceAll("Mbps", "")
                        .trim()
                        + "&"
                        + "uploadResult="
                        + uploadText.replaceAll("Mbps", "")
                        .trim()
                        + "&"
                        + "advertisingID=0"
                        + "&"
                        /*+ "serviceProviderId="
                        + providerId*/
                        + "ISP="
                        + URLEncoder.encode(MyApplication.isp,
                            "utf-8")

                        + "&"
                        +"connectionType="
                        +URLEncoder.encode(MyApplication.connectionType, "utf-8")
                        + "&"
                        +"contactNumber="
                        +"";

                //+ ((Provider) providerSpinner.getSelectedItem()).getId();

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            /*try{
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(new URL(url).openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("int");
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);

                    Element fstElmnt = (Element) node;
                    NodeList nameList = fstElmnt.getElementsByTagName("Name");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String id = ((Node) nameList.item(0)).getNodeValue();

                }
            }catch (Exception e){
                e.printStackTrace();
            }*/








            Connection conn = new Connection(url);

            if (!conn.hasError()) {

                SpeedTestResultParser parser = new SpeedTestResultParser();

                res = parser.parse(conn.getInputStream2());

                return res + "";
            }
            return "";

        }

        @Override
        protected void onPostExecute(String r) {
            // show share pannel
            shareTestRow.setVisibility(View.VISIBLE);
            submitCancelRow.setVisibility(View.GONE);
            try {
                //pd.dismiss();

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

            } catch (Exception e) {

            }

        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        if (summary.getVisibility() == View.VISIBLE) {
            if (submitCancelRow.getVisibility() == View.VISIBLE) {
                summary.setVisibility(View.GONE);
                form.setVisibility(View.VISIBLE);
            } else {
                this.finish();
            }
        } else {
            this.finish();
        }
    }
}