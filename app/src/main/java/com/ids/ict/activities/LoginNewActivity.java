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
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.LoginActivity;
import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.ViewResizing;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class LoginNewActivity extends Activity {

    Connection conn;
    String otp_ref = "";
    String error_msg = "";
    NumberFormat nf;
    String lang = "";
    Typeface tf;

    Button btLogin;
    EditText etUsername,etPassword;
    //TextInputEditText etPassword;
    TextView tvUserName, tvPassword, tvLogin, tvTitle, tv_unsuspend_password,tv_reset_password, tv_getAccount, tv_Account_other;

    RelativeLayout progressBarLayout;
    ProgressBar progressBar;

    SharedPreferences mshared;
    SharedPreferences.Editor edit;
    private ImageView ivShow;
    int start,end;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = Actions.setLocal(this);
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        //Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_register_new);
        Actions.loadMainBar(this);
        ViewResizing.setPageTextResizing(this);

        mshared = PreferenceManager.getDefaultSharedPreferences(LoginNewActivity.this);
        edit = mshared.edit();
        lang = MyApplication.Lang;

        findViews();

        setListeners();

        Log.wtf("MyApplication.enableQOS",": " + MyApplication.enableQOS);

        if (mshared.getBoolean(getString(R.string.enable_qos), false)) {
            Log.wtf("mshared.getBoolean(getString(R.string.enable_qos)","true");
        }
        else{
            Log.wtf("mshared.getBoolean(getString(R.string.enable_qos)","false");
        }
    }


    private void findViews(){

        LinearLayout llUsername = (LinearLayout)findViewById(R.id.llUsername);
        LinearLayout llPassword = (LinearLayout)findViewById(R.id.llPassword);
        LinearLayout mainbar = (LinearLayout)findViewById(R.id.mainbar);
        LinearLayout main = (LinearLayout) findViewById(R.id.registerMainSC);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);

        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tv_unsuspend_password = (TextView) findViewById(R.id.tv_unsuspend_password);
        tv_reset_password = (TextView) findViewById(R.id.tv_reset_password);
        tv_getAccount = (TextView) findViewById(R.id.tv_getAccount);
        tv_Account_other = (TextView) findViewById(R.id.tv_Account_other);

        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ivShow=(ImageView)findViewById(R.id.ivShow);

        if (lang.equals(MyApplication.ENGLISH)) {
            tf = MyApplication.facePolarisMedium;

            tvUserName.setGravity(Gravity.LEFT);
            tvPassword.setGravity(Gravity.LEFT);
            etUsername.setGravity(Gravity.LEFT);
            etPassword.setGravity(Gravity.LEFT);

        } else {
            tf = MyApplication.faceDinar;

            tvUserName.setGravity(Gravity.RIGHT);
            tvPassword.setGravity(Gravity.RIGHT);
            etUsername.setGravity(Gravity.RIGHT);
            etPassword.setGravity(Gravity.RIGHT);
        }

        tvUserName.setTypeface(tf);
        tvPassword.setTypeface(tf);
        tvLogin.setTypeface(tf);
        tvTitle.setTypeface(tf);
        tv_unsuspend_password.setTypeface(tf);
       try{ tv_reset_password.setTypeface(tf);}catch (Exception e){}
        tv_getAccount.setTypeface(tf);
        tv_Account_other.setTypeface(tf);
        etPassword.setTypeface(tf);
        etUsername.setTypeface(MyApplication.facePolarisMedium);
        etPassword.setTypeface(Typeface.DEFAULT);
        btLogin.setTypeface(tf);



        if (MyApplication.nightMod) {

            llPassword.setBackgroundColor(getResources().getColor(R.color.night_gray));
            llUsername.setBackgroundColor(getResources().getColor(R.color.night_gray));

            mainbar.setBackgroundResource(R.drawable.footer_nt);
            main.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            tvTitle.setTextColor(getResources().getColor(R.color.white));
            tvUserName.setTextColor(getResources().getColor(R.color.white));
            tvPassword.setTextColor(getResources().getColor(R.color.white));
            tvLogin.setTextColor(getResources().getColor(R.color.white));
            tv_unsuspend_password.setTextColor(getResources().getColor(R.color.white));
            tv_reset_password.setTextColor(getResources().getColor(R.color.white));
            tv_getAccount.setTextColor(getResources().getColor(R.color.white));
            tv_Account_other.setTextColor(getResources().getColor(R.color.white));
            btLogin.setTextColor(getResources().getColor(R.color.white));

            etUsername.setTextColor(getResources().getColor(R.color.nightBlue));
            etPassword.setTextColor(getResources().getColor(R.color.nightBlue));

            btLogin.setBackground(ContextCompat.getDrawable( this, R.drawable.button_gray));

        }




        ivShow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
     /*                   etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        etPassword.setSelection(etPassword.length());*/
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        InputMethodSubtype ims = imm.getCurrentInputMethodSubtype();
                        String locale = ims.getLocale();
                        start=etPassword.getSelectionStart();
                        end=etPassword.getSelectionEnd();
                        etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        etPassword.setSelection(start,end);
                        // PRESSED
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
 /*                       etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etPassword.setSelection(etPassword.length());*/
                        start=etPassword.getSelectionStart();
                        end=etPassword.getSelectionEnd();
                        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());;
                        etPassword.setSelection(start,end);
                        // RELEASED
                        break;
                }
                return true;
            }
        });


    }


    private void setListeners(){

        btLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    String userName = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    String message, language;

                    if (Actions.isNetworkAvailable(LoginNewActivity.this)) {


                        if (userName.length() != 0 && password.length() != 0){

                            if (lang.equals(MyApplication.ENGLISH)) {
                                language = "English";
                                message = "Is this your current username? " + userName
                                        + ". You will receive verification code via SMS on its corresponding number.";
                            } else {

                                language = "Arabic";
                                message = "سوف يصلك رمز التفعيل عبر رسالة نصية على الرقم المرتبط باسم المستخدم "+userName ;
                            }

                            loginDialog(LoginNewActivity.this, message, userName, password);

                        }else{

                            Actions.onCreateDialog1(LoginNewActivity.this, getResources().getString(R.string.fill_all));
                        }


                    } else {
                        Actions.onCreateBlockedDialog5(LoginNewActivity.this, getString(R.string.nonetwork2));
                    }
                } catch (Exception e) {
                    Actions.onCreateDialog1(LoginNewActivity.this, getResources().getString(R.string.error_all));
                }
            }
        });

        tv_unsuspend_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String url = "https://www.stgnas.gov.qa/self-service/reset/personal";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/

                String url = "https://www.nas.gov.qa/self-service/unsuspend?";
                Intent i = new Intent(LoginNewActivity.this, webViewActivity.class);
                i.putExtra("url", url);
                startActivity(i);
            }
        });


        tv_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.nas.gov.qa/self-service/reset/personal?";
                Intent i = new Intent(LoginNewActivity.this, webViewActivity.class);
                i.putExtra("url", url);
                startActivity(i);
            }
        });

        tv_getAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String url = "https://www.stgnas.gov.qa/self-service/register/select-user-type";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/

//                String url = "https://www.nas.gov.qa/self-service/register/select-user-type?";
//                Intent i = new Intent(LoginNewActivity.this, webViewActivity.class);
//                i.putExtra("url", url);
//                startActivity(i);

                //testing
                if(MyApplication.isTesting){
                    Intent i = new Intent(LoginNewActivity.this, HomePageActivity.class);
                    startActivity(i);
                }
                else{

                    String url = "https://www.nas.gov.qa/self-service/register/select-user-type?";
                    Intent i = new Intent(LoginNewActivity.this, webViewActivity.class);
                    i.putExtra("url", url);
                    startActivity(i);
                }


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


    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(RegisterActivity.this, TermsActivity.class);
        //startActivity(intent);
        LoginNewActivity.this.finish();
    }


    public boolean checkIfHaveSimCard() {
        boolean have = false;
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            have = false;
        } else {
            if (isSimSupport(LoginNewActivity.this))
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


    public void loginDialog(final Activity activity, String msg, final String username, final String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        TextView textView;
        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_layout, null);
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

                        try{
                            InputMethodManager inputManager = (InputMethodManager) LoginNewActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(LoginNewActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }catch (Exception e){
                            e.getMessage();
                            e.printStackTrace();
                        }

                     //  startActivity(new Intent(LoginNewActivity.this, HomePageActivity.class));

                        LoginTask loginTask = new LoginTask(username, password);
                        loginTask.execute();

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


    protected class LoginTask extends AsyncTask<Void, Void, String> {

        ProgressDialog pd;
        String reg_id = null;
        String username, password;

        String transactionId = "", transportKey = "", authenticationState = "", errorMessage = "";

        SharedPreferences mShared;
        private SharedPreferences.Editor edit;

        public LoginTask(String username, String password){
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            //pd = ProgressDialog.show(RegisterActivity.this, "", "", true);

            mShared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            edit = mShared.edit();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            //username = "22222222224";
            //password = "Qatar2060";

        }

        @Override
        protected String doInBackground(Void... a) {

            try {

                URL url = new URL("" + MyApplication.link + MyApplication.sms + "AuthenticateUsernameAndPassword_OTP?username=" + username + "&password=" + password);
                Log.wtf("LoginTask","url : " + url.toString());

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList websiteList0 = doc.getElementsByTagName("AuthenticationState");
                Element websiteElement0 = (Element) websiteList0.item(0);
                websiteList0 = websiteElement0.getChildNodes();


                  // /*
                authenticationState = ((Node) websiteList0.item(0)).getNodeValue();

                try {
                    NodeList websiteList = doc.getElementsByTagName("TransactionId");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    transactionId = ((Node) websiteList.item(0)).getNodeValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    transactionId = "";
                }
                Log.wtf("Login","transactionId : " + transactionId);

                try {
                    NodeList websiteList2 = doc.getElementsByTagName("TransportKey");
                    Element websiteElement2 = (Element) websiteList2.item(0);
                    websiteList2 = websiteElement2.getChildNodes();
                    transportKey = ((Node) websiteList2.item(0)).getNodeValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    transportKey = "";
                }
                Log.wtf("Login","transportKey : " + transportKey);

                try {
                    NodeList websiteList3 = doc.getElementsByTagName("Error");
                    Element websiteElement3 = (Element) websiteList3.item(0);
                    websiteList3 = websiteElement3.getChildNodes();
                    errorMessage = ((Node) websiteList3.item(0)).getNodeValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMessage = "";
                }
                Log.wtf("Login","errorMessage : " + errorMessage);
                //*/


                //testing Login
                if(MyApplication.isTesting){
                    authenticationState = "2";
                    transactionId = MyApplication.testingTransactionId;
                    transportKey = MyApplication.testingTransportKey ;
                    errorMessage = "";
                }

                /*NodeList websiteList3 = doc.getElementsByTagName("PhoneNumber");
                Element websiteElement3 = (Element) websiteList3.item(0);
                websiteList3 = websiteElement3.getChildNodes();
                phoneNumber = ((Node) websiteList3.item(0)).getNodeValue();*/

            }   catch (Exception e) {
                e.printStackTrace();
                transactionId = "";
                transportKey = "";
                errorMessage = "";


                if(MyApplication.isTesting){
                    authenticationState = "2";
                    transactionId = MyApplication.testingTransactionId;
                    transportKey = MyApplication.testingTransportKey ;
                    errorMessage = "";
                }
                //phoneNumber = "";
            }

            return authenticationState;
        }

        protected void onPostExecute(String result) {

            if (authenticationState.equals("2")) {

                edit.putBoolean("toRegister", false).apply();

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);


                Intent intent = new Intent(LoginNewActivity.this, CodeNewActivity.class);
                Bundle bundle = new Bundle();
                MyApplication myApp;

                myApp = (MyApplication) getApplicationContext();
                myApp.pass = transactionId;

                //testing Login
                if(MyApplication.isTesting){
                    username = "22222222224";
                    password = "Qatar2022";
                }


                bundle.putString("transactionId", transactionId);
                bundle.putString("transportKey", transportKey);
                bundle.putString("username", username);
                bundle.putString("password", password);
                bundle.putString("language", lang);
                bundle.putString("qatarID", "11111111111");
                // adding the bundle to the intent
                intent.putExtras(bundle);
                startActivity(intent);
                LoginNewActivity.this.finish();

            } else {

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                String[] array = errorMessage.split(Pattern.quote("||"));
                if (lang.equals("ar")){

                    try {
                        error_msg = array[0];
                    } catch (Exception e) {
                        e.printStackTrace();
                        error_msg = "";
                    }
                }else {

                    try {
                        error_msg = array[1];
                    } catch (Exception e) {
                        e.printStackTrace();
                        error_msg = "";
                    }
                }

                Log.wtf("error", "is "+ error_msg);
                Actions.onCreateDialog1(LoginNewActivity.this, error_msg);
            }

            try {
                pd.dismiss();
            } catch (Exception e) {
                //e.printStackTrace();
            }

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

    }

}
