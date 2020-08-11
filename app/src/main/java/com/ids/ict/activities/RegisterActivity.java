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
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.LookUp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class RegisterActivity extends Activity {

    Connection conn;
    String otp_ref = "";
    String error_msg = "";
    NumberFormat nf;
    String lang = "";
    Typeface tf;
    EditText numEdt, emailEdt;
    TextView nosimcard;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;

    SharedPreferences mshared;
    SharedPreferences.Editor edit;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = Actions.setLocal(this);
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_register);
        RelativeLayout main = (RelativeLayout) findViewById(R.id.registerMainSC);
        Actions.loadMainBar(this);
        ViewResizing.setPageTextResizing(this);

        mshared = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
        edit = mshared.edit();
        lang = MyApplication.Lang;

        numEdt = (EditText) findViewById(R.id.number_edit);
        emailEdt = (EditText) findViewById(R.id.email_edit);
        nosimcard = (TextView) findViewById(R.id.nosimcard);
        Typeface tf = null;
        nosimcard.setTypeface(MyApplication.faceDinarLight);
        EditText codeText = (EditText) findViewById(R.id.editText1);

        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView regText = (TextView) findViewById(R.id.registerText);
        TextView mobText = (TextView) findViewById(R.id.mobileText);
        TextView emailText = (TextView) findViewById(R.id.emailText);



        if (lang.equals(MyApplication.ENGLISH)) {
            tf = MyApplication.facePolarisMedium;
            codeText.setTypeface(tf);

            regText.setGravity(Gravity.LEFT);
            mobText.setGravity(Gravity.LEFT);
            emailText.setGravity(Gravity.LEFT);

        } else {
            tf = MyApplication.faceDinar;

            regText.setGravity(Gravity.RIGHT);
            mobText.setGravity(Gravity.RIGHT);
            emailText.setGravity(Gravity.RIGHT);
            //codeText.setTypeface(tf);
            //codeText.setText("+٩٧٤");
        }
        codeText.setText("+974");
        regText.setTypeface(tf);
        mobText.setTypeface(tf);
        TextView title = (TextView) findViewById(R.id.title);
        emailText.setTypeface(tf);
        title.setTypeface(tf);
        final Button butCont = (Button) findViewById(R.id.cont_button);
        //	butCont.setTypeface(tf);
        numEdt.setTypeface(MyApplication.facePolarisMedium);
        codeText.setTypeface(MyApplication.facePolarisMedium);

        if (MyApplication.nightMod) {
            main.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            title.setBackgroundColor(getResources()
                    .getColor(R.color.night_gray));
            title.setTextColor(getResources().getColor(R.color.white));
            regText.setTextColor(getResources().getColor(R.color.white));

            mobText.setTextColor(getResources().getColor(R.color.white));
            emailText.setTextColor(getResources().getColor(R.color.white));

            butCont.setBackground(ContextCompat.getDrawable( this, R.drawable.button_gray));
            //butCont.setTextColor(getResources().getColor(R.color.white));
            //butCont.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.buttonnight));
        }

        butCont.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    String mobile = numEdt.getText().toString();
                    String email = emailEdt.getText().toString();
                    String message, language;
                    if (Actions.isNetworkAvailable(RegisterActivity.this)) {
                        if (lang.equals(MyApplication.ENGLISH)) {
                            language = "English";
                            message = "Is this your current mobile number? +974 "
                                    + mobile
                                    + ". You will receive verification code via SMS on this number.";
                        } else {
                            nf = NumberFormat.getInstance(new Locale("ar_EG"));
                            NumberFormat formatter = NumberFormat
                                    .getInstance(new Locale("ar"));

                            language = "Arabic";
                            message = "سوف يصلك رمز التفعيل عبر رسالة نصية على الرقم "
                                    + "974" + mobile + "+";
                        }

                        if (!isEmailValid(email)) {
                            Actions.onCreateDialog1(RegisterActivity.this,
                                    getResources().getString(R.string.error_email));
                        } else if (!(mobile.length() == 8)) {
                            Actions.onCreateDialog1(RegisterActivity.this,
                                    getResources().getString(R.string.error_num));
                        } else {

                            onCreateDialog2(RegisterActivity.this, message, "",
                                    mobile, language, email);
                        }
                    } else {
                        Actions.onCreateBlockedDialog5(RegisterActivity.this, getString(R.string.nonetwork2));
                    }
                } catch (Exception e) {
                    Actions.onCreateDialog1(RegisterActivity.this,
                            getResources().getString(R.string.error_all));
                }
            }
        });

        /*if (!checkIfHaveSimCard()) {
			nosimcard.setVisibility(View.VISIBLE);
			if (MyApplication.Lang.equals("ar"))
				nosimcard.setText(getLookup("52").nameen);
			else
				nosimcard.setText(getLookup("52").namear);
			butCont.setVisibility(View.GONE);
		}*/
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

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(RegisterActivity.this, TermsActivity.class);
        //startActivity(intent);
        RegisterActivity.this.finish();
    }

    public boolean checkIfHaveSimCard() {
        boolean have = false;
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            have = false;
        } else {
            if (isSimSupport(RegisterActivity.this))
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

    public void onCreateDialog2(final Activity activity, String msg,
                                final String reg_id1, final String num, final String lan,
                                final String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        TextView textView;
        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_layout,
                null);
        textView = (TextView) textEntryView.findViewById(R.id.dialogMsg);
        textView.setText(msg);
        //	textView.setGravity(Gravity.CENTER);
        String conf = "", can = "";
        if (lang.equals(MyApplication.ENGLISH)) {
            conf = "Confirm";
            can = "Edit";
        } else {
            conf = "تأكيد";
            can = "تعديل";
        }
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(textEntryView)
                // Add action buttons
                .setPositiveButton(conf, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Register register = new Register();
                        register.execute();
                    }
                });

        builder.setView(textEntryView).setNegativeButton(can,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        Dialog d = builder.create();
        d.getWindow().setLayout(
                (int) (activity.getWindow().peekDecorView().getWidth() * 0.9),
                (int) (activity.getWindow().peekDecorView().getHeight() * 0.9));
        d.show();
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        if (email.equals("")) {
            isValid = true;
        } else {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            CharSequence inputStr = email;

            Pattern pattern = Pattern.compile(expression,
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches()) {
                isValid = true;
            }
        }
        return isValid;
    }

    public String getcurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private String generate_otp(String num) {
        String title = null, link="";
        try {

            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH))
                link = "" + MyApplication.link + MyApplication.sms
                        + "GenerateOTP3?mobileNumber=" + num + "&pass="
                        + Actions.create_token_new() + "&language=en";
            else
                link = "" + MyApplication.link + MyApplication.sms
                        + "GenerateOTP3?mobileNumber=" + num + "&pass="
                        + Actions.create_token_new() + "&language=ar";

           /* link = "" + MyApplication.link + MyApplication.sms
                    + "GenerateOTP3?mobileNumber=" + num + "&pass="
                    + Actions.create_token_new() + "&language="+MyApplication.Lang;*/



            URL url = new URL(link);


            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList websiteList = doc.getElementsByTagName("Status");
            Element websiteElement = (Element) websiteList.item(0);
            websiteList = websiteElement.getChildNodes();
            title = ((Node) websiteList.item(0)).getNodeValue();

            if (title.equals("SUCCESS")) {
                NodeList otpList = doc.getElementsByTagName("OTPReference");
                Element otpElement = (Element) otpList.item(0);
                otpList = otpElement.getChildNodes();
                otp_ref = ((Node) otpList.item(0)).getNodeValue();
                error_msg = "";
            } else {
                NodeList otpList = doc.getElementsByTagName("ErrorMessage");
                Element otpElement = (Element) otpList.item(0);
                otpList = otpElement.getChildNodes();
                error_msg = ((Node) otpList.item(0)).getNodeValue();
            }
        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);

        }
        return title;

    }

    protected class Register extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;
        String reg_id = null;
        String numbertxt, emailtxt;

        SharedPreferences mShared;
        private SharedPreferences.Editor edit;

        @Override
        protected void onPreExecute() {
            //pd = ProgressDialog.show(RegisterActivity.this, "", "", true);

            mShared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            edit = mShared.edit();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);


            numbertxt = numEdt.getText().toString();
            emailtxt = emailEdt.getText().toString();
        }

        @Override
        protected String doInBackground(Void... a) {
            String url = "" + MyApplication.link + MyApplication.post
                    + "SaveRegistration3";
            // String url = "" + MyApplication.link + MyApplication.link +
            // "SaveRegistration";
            conn = new Connection(url);
            String response = "";
            String reg_id1 = "";
            try {
                reg_id1 = conn.executeMultipartPost_register(url, numbertxt, mShared.getInt(getString(R.string.device_id), 0) + "" ,"", emailtxt, getcurrentDate());
                String[] r1 = reg_id1.split("</");
                String[] r2 = r1[0].split(">");
                reg_id = r2[2];

       /*         if (!reg_id.equals("0")) {
                    response = generate_otp(numbertxt);

                }*/
            } catch (Exception e) {
                // TODO Auto-generated catch block
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

            return response;

        }

        protected void onPostExecute(String result) {

            if (result.equals("SUCCESS")) {

                edit.putBoolean("toRegister", false).apply();

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);


                Intent intent = new Intent(RegisterActivity.this, CodeActivity.class);
                Bundle bundle = new Bundle();
                MyApplication myApp;
                myApp = (MyApplication) getApplicationContext();
                myApp.pass = reg_id;
                bundle.putString("regId", reg_id);
                bundle.putString("number", numEdt.getText().toString());
                bundle.putString("language", lang);
                bundle.putString("qatarID", "11111111111");
                bundle.putString("email", emailEdt.getText().toString());
                bundle.putString("otp_ref", otp_ref);
                // adding the bundle to the intent
                intent.putExtras(bundle);
                startActivity(intent);
                RegisterActivity.this.finish();


            } else {

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);


                Actions.onCreateDialog1(RegisterActivity.this, error_msg);
            }

            try {
                pd.dismiss();
            } catch (Exception e) {

            }

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

    }

}
