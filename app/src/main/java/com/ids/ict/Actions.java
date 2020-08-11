package com.ids.ict;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.ids.ict.activities.Event;
import com.ids.ict.activities.MoreActivity;
import com.ids.ict.activities.NotificationListActivity;
import com.ids.ict.activities.Notifications;
import com.ids.ict.activities.SettingsNewActivity;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.MyParser;
import com.ids.ict.classes.Profile;
import com.ids.ict.classes.ReportsListActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static android.content.Context.MODE_PRIVATE;
import static com.ids.ict.classes.ViewResizing.setViewGroupContentTextSize;

public class Actions {
     public static String lastLatitude;
    public static String lastLongitude;
    public static String signalSupport,signalQuality,spectrumSignalStrength;
    public static String getReleaseVersion(Context context) {
        String version = "";
        int verCode = 0;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
            verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //return "Arsel_"+version+"(" + verCode + ")";
        return "Arsel "+version;
    }


    public static ArrayList<Notifications> ReadNews(String url2) throws ParserConfigurationException, IOException {

        ArrayList<Notifications> notifications = new ArrayList<>();


        try {

            URL url = new URL(url2);
            DocumentBuilderFactory dbf = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("News");


            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;

                NodeList nameList = fstElmnt.getElementsByTagName("Id");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                String id = ((Node) nameList.item(0)).getNodeValue();

                NodeList websiteList = fstElmnt
                        .getElementsByTagName("Title");
                Element websiteElement = (Element) websiteList.item(0);
                websiteList = websiteElement.getChildNodes();
                String title = ((Node) websiteList.item(0)).getNodeValue();
                String details = "";
                try {
                    NodeList codeList = fstElmnt.getElementsByTagName("Details");
                    Element codeElement = (Element) codeList.item(0);
                    codeList = codeElement.getChildNodes();
                    details = ((Node) codeList.item(0)).getNodeValue();
                } catch (Exception e) {
                    details = "";
                }


                NodeList datenode = fstElmnt.getElementsByTagName("Date");
                Element detElement = (Element) datenode.item(0);
                datenode = detElement.getChildNodes();
                String date = ((Node) datenode.item(0)).getNodeValue();


                Notifications pr = new Notifications();
                pr.setName(title);

                pr.setId(Integer.parseInt(id));
                pr.setdescription(details);
                pr.setDate(date);
                notifications.add(pr);
            }

        } catch (Exception e) {

        }


        return notifications;
    }


    public static String dateFormatter = "yyyy-MM-dd HH:mm:ss aa";


    public static void writeToLogFile(String sBody) {

        /*File root = new File(Environment.getExternalStorageDirectory(), "ArselApp");
        if (!root.exists())
            root.mkdir();

        File gpxfile = new File(root, "QosTest.txt");
        try {
            FileWriter writer = new FileWriter(gpxfile,true);
            DateFormat df = new SimpleDateFormat(dateFormatter, Locale.ENGLISH);
            String date = df.format(Calendar.getInstance().getTime());


            writer.append("[" + date + "] " + sBody + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }


    public static  String convertMilliSecondsToFormattedDate(Date date){
        return new SimpleDateFormat(dateFormatter, Locale.ENGLISH).format(date);
    }


    public static void writeToFileAdHoc(String sBody) {
        /*File root = new File(Environment.getExternalStorageDirectory(), "ArselApp");
        File gpxfile = new File(root, "Adhoc.txt");
        try {
            FileWriter writer = new FileWriter(gpxfile,true);
            DateFormat df = new SimpleDateFormat(dateFormatter, Locale.ENGLISH);
            String date = df.format(Calendar.getInstance().getTime());


            writer.append("[" + date + "] " + sBody + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }


    public static void writeToFileQueued(String sBody) {
        /*File root = new File(Environment.getExternalStorageDirectory(), "ArselApp");
        File gpxfile = new File(root, "queued.txt");
        try {
            FileWriter writer = new FileWriter(gpxfile,true);
            DateFormat df = new SimpleDateFormat(dateFormatter, Locale.ENGLISH);
            String date = df.format(Calendar.getInstance().getTime());


            writer.append("[" + date + "] " + sBody + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }


    public static void writeToFileNotifications(String sBody) {

        /*if (MyApplication.writeLog){

            File root = new File(Environment.getExternalStorageDirectory(), "ArselApp");
            File gpxfile = new File(root, "Notifications.txt");
            try {
                FileWriter writer = new FileWriter(gpxfile,true);
                DateFormat df = new SimpleDateFormat(dateFormatter, Locale.ENGLISH);
                String date = df.format(Calendar.getInstance().getTime());


                writer.append("[" + date + "] " + sBody + "\n");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/
    }


    public static boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 4 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }


    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static boolean isWifiAvailable(Context context) {

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected() && isOnline();
    }


    public static int CheckVersion(Activity c, int newVersion, int force) {

        int needUrgentUpdate = -1;

        try {
            PackageInfo pInfo;
            try {
                pInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
                int version = pInfo.versionCode;

                try {

                    if (newVersion > version) {
                        if (force == 1) {

                            needUrgentUpdate = 2;
                        } else {
                            needUrgentUpdate = 1;
                        }
                    } else {
                        //continue to app
                        needUrgentUpdate = 0;

                    }
                } catch (Exception e) {
                    Log.d("eee", "" + e);

                }
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {

        }
        return needUrgentUpdate;
    }


    public static  String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android:" + sdkVersion + " (" + release +")";
    }


    public static void onCreateBlockedDialog2(final Activity activity, String msg) {
        Dialog d = null;
        Typeface tf;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;


        } else {

            tf = MyApplication.faceDinar;

        }

        //final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));

        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_warning,
                null);
        TextView textView = (TextView) textEntryView
                .findViewById(R.id.title);
        textView.setGravity(Gravity.CENTER);
        textView.setText(activity.getString(R.string.warning2));
        final TextView msgg = (TextView) textEntryView
                .findViewById(R.id.msg);
        textView.setGravity(Gravity.CENTER);
        msgg.setText(msg);
        textView.setTypeface(tf);
        msgg.setTypeface(tf);
        alertDialog.setView(textEntryView);

        alertDialog.setPositiveButton(activity.getString(R.string.ok2),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        d = alertDialog.create();
        d.setCanceledOnTouchOutside(false);
        d.show();

    }


    public static void onCreateBlockedDialog5(final Activity activity, String msg) {
        Dialog d = null;
        Typeface tf;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;


        } else {

            tf = MyApplication.faceDinar;

        }

        //final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));

        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_warning,
                null);
        TextView textView = (TextView) textEntryView
                .findViewById(R.id.title);
        textView.setGravity(Gravity.CENTER);
        textView.setText(activity.getString(R.string.warning));
        final TextView msgg = (TextView) textEntryView
                .findViewById(R.id.msg);
        textView.setGravity(Gravity.CENTER);
        msgg.setText(msg);
        textView.setTypeface(tf);
        msgg.setTypeface(tf);
        alertDialog.setView(textEntryView);

        alertDialog.setPositiveButton(activity.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        d = alertDialog.create();
        d.setCanceledOnTouchOutside(false);
        d.show();

    }


    public static void onCreateBlockedDialog4(final Activity activity, String msg) {
        Dialog d = null;
        Typeface tf;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;


        } else {

            tf = MyApplication.faceDinar;

        }

        //final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));

        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_warning,
                null);
        TextView textView = (TextView) textEntryView
                .findViewById(R.id.title);
        textView.setGravity(Gravity.CENTER);
        textView.setText(activity.getString(R.string.warning));
        final TextView msgg = (TextView) textEntryView
                .findViewById(R.id.msg);
        textView.setGravity(Gravity.CENTER);
        msgg.setText(msg);
        textView.setTypeface(tf);
        msgg.setTypeface(tf);
        alertDialog.setView(textEntryView);

        alertDialog.setPositiveButton(activity.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        d = alertDialog.create();
        d.setCanceledOnTouchOutside(false);
        d.show();

    }


    public static Drawable getImageFromResources(Activity context, String imageName) {
        Drawable image = context.getResources().getDrawable(
                context.getResources().getIdentifier(imageName, "drawable",
                        context.getPackageName()));
        return image;
    }


    public static String imageNameClean(String name) {
        String cleanedName = "";
        cleanedName = name;
        cleanedName = cleanedName.replaceAll("\\-", "_");
        cleanedName = cleanedName.replaceAll("\\(", "");
        cleanedName = cleanedName.replaceAll("\\)", "");
        cleanedName = cleanedName.replaceAll(" ", "");
        cleanedName = cleanedName.replaceAll(".png", "");
        cleanedName = cleanedName.toLowerCase();
        if (name.equals("06.png") || name.equals("11.png")) {
            cleanedName = "m" + cleanedName;
        }
        return cleanedName;
    }


    public static InetAddress ip() throws SocketException {
        Enumeration<NetworkInterface> nis = NetworkInterface
                .getNetworkInterfaces();
        NetworkInterface ni;
        while (nis.hasMoreElements()) {
            ni = nis.nextElement();
            if (!ni.isLoopback()/* not loopback */ && ni.isUp()/* it works now */) {
                for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                    // filter for ipv4/ipv6
                    if (ia.getAddress().getAddress().length == 4) {
                        // 4 for ipv4, 16 for ipv6
                        return ia.getAddress();
                    }
                }
            }
        }
        return null;
    }


    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }


    public static void goToSettings(Activity activity) {
        Intent intent;
        String lang = PreferenceManager.getDefaultSharedPreferences(activity)
                .getString(activity.getString(R.string.language_key),
                        MyApplication.ENGLISH);

        if (lang.equals(MyApplication.ENGLISH)) {
            intent = new Intent(activity, SettingsNewActivity.class);
        } else {
            intent = new Intent(activity, SettingsNewActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);

    }


    public static void setWebViewTextSize(Activity activity, WebView webView) {
        WebSettings webSettings = webView.getSettings();
        int fontSize = webSettings.getDefaultFontSize();
        MyApplication app;
        app = (MyApplication) activity.getApplicationContext();
        // calculate perscentage according 480 px width screen
        double px = convertPixelsToDp(fontSize, activity) * 1.5;
        double percentage = (double) ((double) px / 480);
        // float x=textView.getTextSize();
        webSettings
                .setDefaultFontSize((int) (app.screenWidth * percentage * 3));

    }


    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }


    public static String create_token() {
        String tok = "";
        Random rnd = new Random();
        int n1 = 100000 + rnd.nextInt(900000);

        Random rnd2 = new Random();
        int n2 = 10000 + rnd2.nextInt(90000);

        int sum = n1 + n2;

        tok = Integer.toString(n1) + Integer.toString(n2)
                + Integer.toString(sum);

        return tok;

    }


    public static String create_token_new() {
        String tok = "";
        Random rnd = new Random();
        int n1 = 1000 + rnd.nextInt(9000);

        Random rnd2 = new Random();
        int n2 = 1000 + rnd2.nextInt(9000);

        Random rnd3 = new Random();
        int n3 = 1000 + rnd3.nextInt(9000);

        int sum = n1 + 2 * n2 + n3;

        tok = Integer.toString(n1) + Integer.toString(n2) + Integer.toString(n3) + Integer.toString(sum);

        return tok;
    }


    public static String ecryptPassword(String pass, String hashingMethod) {// hashing/*/
        // method
        // is
        // a
        // string
        // ..for
        // md5
        // use
        // MD5
        // ,
        // other
        // example
        // :
        // u
        // may
        // use
        // SHA-512,
        // etc..
        MessageDigest md;
        String result = "";
        try {

            md = MessageDigest.getInstance(hashingMethod);
            md.update(pass.getBytes());
            byte byteData[] = md.digest();
            md.reset();

            // convert the byte to hex format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
                result = hexString.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    public static void loadMainBar(Activity activity) {
        try {
            View backButton, backText;
            backButton = activity.findViewById(R.id.back);

            if (activity instanceof MoreActivity
                    || activity instanceof NotificationListActivity
                    || activity instanceof ReportsListActivity) {
                backButton.setVisibility(View.GONE);

            }

        } catch (Exception e) {

        }
    }


    public static void backTo(Activity activity) {
        activity.finish();
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            // return capitalize(manufacturer) + " " + model;
            return capitalize(manufacturer) + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    public static SpannableStringBuilder errorMessage(String errorMsg,
                                                      int textColor) {

        ForegroundColorSpan fgcspan = new ForegroundColorSpan(textColor);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(errorMsg);
        ssbuilder.setSpan(fgcspan, 0, errorMsg.length(), 0);
        return ssbuilder;

    }


    private static AtomicReference<com.ids.ict.classes.IssuesType[]> IssuesType;


    public static void setScreenWidthHeight(Activity activity) {
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        MyApplication app;
        app = (MyApplication) activity.getApplicationContext();
        app.screenWidth = width;
        app.screenHeight = height;

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("widthscreen", MyApplication.screenWidth);
        editor.putInt("heightscreen", MyApplication.screenHeight);

        editor.commit();
    }


    public static Error readEvents(AtomicReference<Event[]> events, String connString) throws ParserConfigurationException, IOException {
        Connection conn = new Connection(connString);
        if (!conn.hasError()) {
            InputSource page = conn.readWebPage();
            if (!conn.hasError()) {
                MyParser.ParseEvent(events, page);
            }
        }
        return conn.getError();
    }


    public static void LoadWebViewwithCustomFont(String text, WebView web)//like file:///android_asset/OpenSans-Regular.ttf
    {
        String fontName = "";
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            fontName = "file:///android_asset/css/fonts/Galaxie_Polaris_Medium.otf";

        } else {
            fontName = "file:///android_asset/css/fonts/GE_Dinar_One_Medium.otf";

        }
        String color = "#000000";
        if (MyApplication.nightMod)
            color = "#1A2345";
        else
            color = "#A11C37";

        web.getSettings().setJavaScriptEnabled(true);

        web.loadDataWithBaseURL("file:///android_asset/css",
                "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"" + fontName + "\")\n" +
                        "}\n" +
                        "body {\n" +
                        "    font-family: MyFont !important;\n" +
                        "    font-size: medium;\n" +
                        "    color: #000000;\n" +
                        "    text-align: center;\n" +
                        "}\n" +

                        "p {\n" +
                        "    font-family: MyFont;\n" +
                        "    font-size: medium;\n" +
                        "    color: #000000;\n" +
                        "    text-align: center;\n" +
                        "}\n"

                        + "</style><link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/bootstrap.min.css\" />" +
                        "<script src=\"file:///android_asset/css/jquery.min.js\" type=\"text/javascript\"></script>" +
                        "<script src=\"file:///android_asset/css/bootstrap.min.js\" type=\"text/javascript\"></script>" +
                        "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/font-awesome.min.css\" />" +
                        "<script type=\"text/javascript\">\n" +
                        "var color = '" + color + "';\n" +
                        "</script>" +
                        "</head>" +
                        "<body>" + text + "</body></html>", "text/html", "UTF-8", "");

    }


    public static void LoadWebViewWithCustomFontNotifications(String text, WebView web)//like file:///android_asset/OpenSans-Regular.ttf
    {
        String fontName = "";
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            fontName = "file:///android_asset/css/fonts/Galaxie_Polaris_Medium.otf";

        } else {
            fontName = "file:///android_asset/css/fonts/GE_Dinar_One_Medium.otf";

        }
        String color = "#000000";
        if (MyApplication.nightMod)
            color = "#1A2345";
        else
            color = "#A11C37";

        web.getSettings().setJavaScriptEnabled(true);
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            web.loadDataWithBaseURL("file:///android_asset/css",
                    "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"" + fontName + "\")\n" +
                            "}\n"
                            + "</style><link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/bootstrap.min.css\" />" +
                            "<script src=\"file:///android_asset/css/jquery.min.js\" type=\"text/javascript\"></script>" +
                            "<script src=\"file:///android_asset/css/bootstrap.min.js\" type=\"text/javascript\"></script>" +
                            "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/font-awesome.min.css\" />" +
                            "<script type=\"text/javascript\">\n" +
                            "var color = '" + color + "';\n" +
                            "</script>" +
                            "</head>" +
                            "<body>" + text + "</body></html>", "text/html", "UTF-8", "");
        } else {
            web.loadDataWithBaseURL("file:///android_asset/css",
                    "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"" + fontName + "\")\n" +
                            "}\n"+
                            "body {\n" +
                            "    font-family: MyFont !important;\n" +
                            "}\n" +

                            "p {\n" +
                            "    font-family: MyFont !important;\n" +
                            "}\n"

                            + "</style><link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/bootstrap.min.css\" />" +
                            "<script src=\"file:///android_asset/css/jquery.min.js\" type=\"text/javascript\"></script>" +
                            "<script src=\"file:///android_asset/css/bootstrap.min.js\" type=\"text/javascript\"></script>" +
                            "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/font-awesome.min.css\" />" +
                            "<script type=\"text/javascript\">\n" +
                            "var color = '" + color + "';\n" +
                            "</script>" +
                            "</head>" +
                            "<body dir=\"rtl\">" + text + "</body></html>", "text/html", "UTF-8", "");
        }

    }


    public static void LoadWebViewwithCustomFont3(String text, WebView web)//like file:///android_asset/OpenSans-Regular.ttf
    {
        String fontName = "";
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            fontName = "file:///android_asset/css/fonts/Galaxie_Polaris_Medium.otf";

        } else {
            fontName = "file:///android_asset/css/fonts/GE_Dinar_One_Medium.otf";

        }
        String color = "#000000";
        if (MyApplication.nightMod)
            color = "#1A2345";
        else
            color = "#A11C37";

        web.getSettings().setJavaScriptEnabled(true);

        web.loadDataWithBaseURL("file:///android_asset/css",
                "<html lang=\"en\" class=\"no-js\"><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"" + fontName + "\")\n" +
                        "}\n" +
                        "body {\n" +
                        "    font-family: MyFont !important;\n" +
                        "    font-size: medium;\n" +
                        "    color: #000000;\n" +
                        "    text-align: center;\n" +
                        "}\n" +

                        "p {\n" +
                        "    font-family: MyFont;\n" +
                        "    font-size: medium;\n" +
                        "    color: #000000;\n" +
                        "    text-align: center;\n" +
                        "}\n"

                        + "</style>" +
                        "</head>" +
                        "<body>" + text + "</body></html>", "text/html", "UTF-8", "");

    }


    public static void LoadWebViewWithCustomFont2(String text, WebView web)//like file:///android_asset/OpenSans-Regular.ttf
    {
        String fontName = "";
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            fontName = "file:///android_asset/css/fonts/Galaxie_Polaris_Medium.otf";

        } else {
            fontName = "file:///android_asset/css/fonts/GE_Dinar_One_Medium.otf";

        }
        String color = "#000000";
        if (MyApplication.nightMod)
            color = "#1A2345";
        else
            color = "#A11C37";

        web.getSettings().setJavaScriptEnabled(true);

        web.getSettings().setUseWideViewPort(true);
        web.loadDataWithBaseURL("file:///android_asset/css",
                "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"" + fontName + "\")\n" +
                        "}\n" +
                        "body {\n" +
                        "    font-family: MyFont !important;\n" +
                        "    font-size: medium;\n" +
                        "    color: #000000;\n" +
                        "    text-align: center;\n" +
                        "}\n" +

                        "p {\n" +
                        "    font-family: MyFont;\n" +
                        "    font-size: medium;\n" +
                        "    color: #000000;\n" +
                        "    text-align: center;\n" +
                        "}\n"

                        + "</style><link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/bootstrap.min.css\" />" +
                        "<script src=\"file:///android_asset/css/jquery.min.js\" type=\"text/javascript\"></script>" +
                        "<script src=\"file:///android_asset/css/bootstrap.min.js\" type=\"text/javascript\"></script>" +
                        "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/font-awesome.min.css\" />" +
                        "<script type=\"text/javascript\">\n" +
                        "var color = '" + color + "';\n" +
                        "</script>" +
                        "</head>" +
                        "<body>" + text + "</body></html>", "text/html", "UTF-8", "");

    }


    public static Error readNotification(
            AtomicReference<Notifications[]> events, String connString)
            throws ParserConfigurationException, IOException {
        Connection conn = new Connection(connString);
        if (!conn.hasError()) {
            InputSource page = conn.readWebPage();
            if (!conn.hasError()) {
                MyParser.ParseNotification(events, page);

            }
        }
        return conn.getError();
    }


    public static void detailsFooter(View v, Activity activity,
                                     AtomicReference<Integer> counterRef) {

        switch (v.getId()) {

            case R.id.decrease_text_size: {
                TextView details = (TextView) activity
                        .findViewById(R.id.news_details);
                float fontSize = details.getTextSize();

                int counter = counterRef.get();
                if (counter > -4) {
                    fontSize -= 4;
                    details.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
                    counter -= 1;
                }
                counterRef.set(counter);

                break;
            }
            case R.id.increase_text_size: {
                TextView details = (TextView) activity
                        .findViewById(R.id.news_details);
                float fontSize = details.getTextSize();

                int counter = counterRef.get();
                if (counter < 4) {
                    fontSize += 4;
                    details.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
                    counter += 1;
                }
                counterRef.set(counter);
                break;
            }
        }
    }


    public static void onCreateDialogNew(final Activity activity, String msg) {
        Dialog d = null;
        Typeface tf;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;


        } else {

            tf = MyApplication.faceDinar;

        }

        //final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));
        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_warning,
                null);
        TextView textView = (TextView) textEntryView
                .findViewById(R.id.title);
        textView.setGravity(Gravity.CENTER);
        textView.setText(activity.getString(R.string.warning2));


        final TextView msgg = (TextView) textEntryView
                .findViewById(R.id.msg);
        textView.setGravity(Gravity.CENTER);
        msgg.setText(msg);
        textView.setTypeface(tf);
        msgg.setTypeface(tf);
        alertDialog.setView(textEntryView);

        alertDialog.setPositiveButton(activity.getString(R.string.ok2),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        d = alertDialog.create();
        d.setCanceledOnTouchOutside(false);
        d.show();

    }


    public static void onCreateBlockedDialog(final Activity activity, String msg) {
        Dialog d = null;
        Typeface tf;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;


        } else {

            tf = MyApplication.faceDinar;

        }

        //final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));

        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_warning,
                null);
        TextView textView = (TextView) textEntryView
                .findViewById(R.id.title);
        textView.setGravity(Gravity.CENTER);
        textView.setText(activity.getString(R.string.warning));
        final TextView msgg = (TextView) textEntryView
                .findViewById(R.id.msg);
        textView.setGravity(Gravity.CENTER);
        msgg.setText(msg);
        textView.setTypeface(tf);
        msgg.setTypeface(tf);
        alertDialog.setView(textEntryView);

        alertDialog.setPositiveButton(activity.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        d = alertDialog.create();
        d.setCanceledOnTouchOutside(false);
        d.show();

    }


    public static void onCreateDialog1(final Activity activity, String msg) {
        Dialog d = null;
        Typeface tf;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;


        } else {

            tf = MyApplication.faceDinar;

        }

        //final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));

        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_warning,
                null);
        TextView textView = (TextView) textEntryView
                .findViewById(R.id.title);
        textView.setGravity(Gravity.CENTER);
        textView.setText(activity.getString(R.string.warning));
        final TextView msgg = (TextView) textEntryView
                .findViewById(R.id.msg);
        textView.setGravity(Gravity.CENTER);
        msgg.setText(msg);
        textView.setTypeface(tf);
        msgg.setTypeface(tf);
        alertDialog.setView(textEntryView);

        alertDialog.setPositiveButton(activity.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        d = alertDialog.create();
        d.setCanceledOnTouchOutside(false);
        d.show();

    }


    public static void onCreateDialog3(final Activity activity, String msg) {
        try{

            Dialog d = null;
            Typeface tf;
            if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

                tf = MyApplication.facePolarisMedium;
            } else {

                tf = MyApplication.faceDinar;
            }

            //final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));

            LayoutInflater inflater = activity.getLayoutInflater();
            final View textEntryView = inflater.inflate(R.layout.dialog_warning,
                    null);
            TextView textView = (TextView) textEntryView
                    .findViewById(R.id.title);
            textView.setVisibility(View.GONE);
        /*textView.setGravity(Gravity.CENTER);
        textView.setText(activity.getString(R.string.warning));
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(tf);*/

            final TextView msgg = (TextView) textEntryView
                    .findViewById(R.id.msg);
            msgg.setText(msg);
            msgg.setTypeface(tf);
            alertDialog.setView(textEntryView);

            alertDialog.setPositiveButton(activity.getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });


            d = alertDialog.create();
            d.setCanceledOnTouchOutside(false);
            d.show();
        }catch (Exception e){
            Log.wtf("onCreateDialog3","crach : " + e.getMessage());
        }
    }


    public static void onCreateDialog4(final Activity activity, String msg) {
        Dialog d = null;
        Typeface tf;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;


        } else {

            tf = MyApplication.faceDinar;

        }
        //final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));

        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_warning,
                null);
        TextView textView = (TextView) textEntryView
                .findViewById(R.id.title);
        textView.setVisibility(View.GONE);
        /*textView.setGravity(Gravity.CENTER);
        textView.setText(activity.getString(R.string.warning));
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(tf);*/
        final TextView msgg = (TextView) textEntryView
                .findViewById(R.id.msg);
        msgg.setText(msg);
        msgg.setTypeface(tf);
        alertDialog.setView(textEntryView);

        alertDialog.setPositiveButton(activity.getString(R.string.ok2),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        d = alertDialog.create();
        d.setCanceledOnTouchOutside(false);
        d.show();
    }


    public static void onCreateDialog2(final Activity activity, String msg) {
        Dialog d = null;
        Typeface tf;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {

            tf = MyApplication.facePolarisMedium;


        } else {

            tf = MyApplication.faceDinar;

        }

        //final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));

        LayoutInflater inflater = activity.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_warning,
                null);
        TextView textView = (TextView) textEntryView
                .findViewById(R.id.title);
        textView.setGravity(Gravity.CENTER);
        textView.setText(activity.getString(R.string.warning2));
        final TextView msgg = (TextView) textEntryView
                .findViewById(R.id.msg);
        textView.setGravity(Gravity.CENTER);
        msgg.setText(msg);
        textView.setTypeface(tf);
        msgg.setTypeface(tf);
        alertDialog.setView(textEntryView);

        alertDialog.setPositiveButton(activity.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//
//        lp.copyFrom(d.getWindow().getAttributes());
//      //  lp.width = 150;
//        lp.height = 500;

        d = alertDialog.create();
        d.setCanceledOnTouchOutside(false);
        d.show();
        //  d.getWindow().getAttributes().height=600;
        //  lp.x=-170;
        // lp.y=100;
    }


    public static Typeface getFont() {
        Typeface tf = null;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            tf = MyApplication.facePolarisMedium;

        } else {
            tf = MyApplication.faceDinar;
        }
        return tf;
    }


    public static String readAboutUs(String connString)
            throws ParserConfigurationException, IOException {
        Connection conn = new Connection(connString);
        if (!conn.hasError()) {
            InputSource page = conn.readWebPage();
            if (!conn.hasError()) {
                return "" + MyParser.ParseAboutUs(page);
            }
        }
        return conn.getError().getMessage();
    }


    public static void overrideFonts(Activity activity) {

        final ViewGroup mContainer = (ViewGroup) activity.findViewById(
                android.R.id.content).getRootView();
        try {
            setViewGroupContentTextSize(activity, mContainer);

        } catch (NoSuchFieldError e) {

        }
    }


    public static LookUp getLookup(String id, Context c) {
        SharedPreferences mshaPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        LookUp look = new LookUp();
        Gson gson = new Gson();
        String json = mshaPreferences.getString(id, "");
        look = gson.fromJson(json, LookUp.class);

        return look;
    }


    public static String setLocal(Activity activity) {

        SharedPreferences mshSharedPreferences = activity.getSharedPreferences("PrefEng", MODE_PRIVATE);

        //MyApplication.Lang = mshSharedPreferences.getString(activity.getResources().getString(R.string.language_key),"");

        Log.wtf("LANGUAGE IS","is "+ MyApplication.Lang);

        if (mshSharedPreferences.getString(activity.getResources().getString(R.string.language_key),"").length() == 0){
            String lan = "";
            TCTDbAdapter datasource = new TCTDbAdapter(activity);
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
            }



        }else{

            MyApplication.Lang = mshSharedPreferences.getString(activity.getResources().getString(R.string.language_key),"");
        }


        String language = MyApplication.Lang;
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext()
                .getResources()
                .updateConfiguration(
                        config,
                        activity.getBaseContext().getResources()
                                .getDisplayMetrics());
        return language;
    }


    public static String setLocalComplaint(Activity activity) {
        String language = "en";
        if (MyApplication.Lang.equals(MyApplication.ARABIC))
            language = "ar";
        else
            language = "en";

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext()
                .getResources()
                .updateConfiguration(
                        config,
                        activity.getBaseContext().getResources()
                                .getDisplayMetrics());
        return language;
    }


    public static SpannableStringBuilder errorMessage(String errorMsg) {

        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(errorMsg);
        ssbuilder.setSpan(fgcspan, 0, errorMsg.length(), 0);
        return ssbuilder;

    }


    public static void showDialog(final Activity activity, String msg,
                                  final boolean finishActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(errorMessage(msg));

        builder.setCancelable(false).setPositiveButton(
                activity.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        if (finishActivity) {

                            activity.finish();

                        }

                    }
                });
        AlertDialog alert = builder.create();

        alert.getContext().setTheme(R.style.CustomDialogTheme);

        try {
            alert.show();
        } catch (Exception e) {

        }

    }


    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    private static String POST(String requestURL,
                               HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //Connection.disableSSLCertificateChecking();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //Connection.disableSSLCertificateChecking();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    private static boolean checkIfDeviceIncluded(Context context, String qosLogDevices){

        boolean found = false;
        SharedPreferences mshSharedPreferences;
        mshSharedPreferences = context.getSharedPreferences("PrefEng", MODE_PRIVATE);

        if (qosLogDevices.length() == 0){
            return false;
        }else{

            try {

                String[] devices = qosLogDevices.split(",");
                for (String device : devices) {
                    if (mshSharedPreferences.getInt(context.getResources().getString(R.string.device_id), 0) == Integer.parseInt(device)) {
                        found = true;
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                found = false;
            }
        }
        return true;
    }


    public static void PostLog(Context context, String tag, String value){

        /*int allowLogging = 1;
        DatabaseHandler db = new DatabaseHandler(context);
        ArrayList<MobileConfiguration> mobileConfigList = db.getMobileconfigs();
        for (int i = 0; i < mobileConfigList.size(); i++) {
            if (mobileConfigList.get(i).getKey().equalsIgnoreCase("Allow_Log")) {
                allowLogging = Integer.parseInt(mobileConfigList.get(i).getValue());
            }
        }*/

    //  if (MyApplication.qosLog == 1 && checkIfDeviceIncluded(context, MyApplication.qosLogDevices)){
        if (MyApplication.sendLogs){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(tag);
            myRef.push().setValue(value);
            //myRef.child(tag).push().setValue(value);
       }
    }


    public static void UpdateNotificationStatus(int notificationId){

        UpdateNotification updateNotification = new UpdateNotification(notificationId);
        updateNotification.execute();
    }


    //<editor-fold desc="Update Notification Task">
    private static class UpdateNotification extends AsyncTask<Void, Void, String> {

        int notificationId;

        public UpdateNotification(int notificationId){

            this.notificationId = notificationId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.wtf("notificationId", "is " +notificationId);
        }

        @Override
        protected String doInBackground(Void... params) {

            String res = "";
            String url = MyApplication.link + MyApplication.post + "UpdateNotificationStatus?";

            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("notificationID", ""+notificationId);
            parameters.put("status", "-1");

            res = POST(url, parameters);
            Log.wtf("UpdateNotificationStatus returned result", "is: "+res);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }




    public static String getLastLatitude(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE);
        lastLatitude = sharedPref.getString(AppConstants.LAST_LATITUDE, "1");
        return lastLatitude;
    }

    public static void setLastLatitude(Context context,String lastLatitude) {

        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString(AppConstants.LAST_LATITUDE, lastLatitude);
        editor.apply();
        Actions.lastLatitude = lastLatitude;

    }




    public static String getLastLongitude(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE);
        lastLongitude = sharedPref.getString(AppConstants.LAST_LONGITUDE, "1");
        return lastLongitude;
    }

    public static void setLastLongitude(Context context,String lastLongitude) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString(AppConstants.LAST_LONGITUDE, lastLongitude);
        editor.apply();
        Actions.lastLongitude = lastLongitude;
    }





    public static String getSignalSupport(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE);
        signalSupport = sharedPref.getString(AppConstants.LAST_SIGNAL_SUPPORT, "0");
        return signalSupport;
    }

    public static void setSignalSupport(Context context,String signalSupport) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString(AppConstants.LAST_SIGNAL_SUPPORT, signalSupport);
        editor.apply();
        Actions.signalSupport = signalSupport;
    }

    public static String getSignalQuality(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE);
        signalQuality = sharedPref.getString(AppConstants.LAST_SIGNAL_QUALITY, "");
        return signalQuality;
    }

    public static void setSignalQuality(Context context,String signalQuality) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString(AppConstants.LAST_SIGNAL_QUALITY, signalQuality);
        editor.apply();
        Actions.signalQuality = signalQuality;
    }

    public static String getSpectrumSignalStrength(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE);
        spectrumSignalStrength = sharedPref.getString(AppConstants.LAST_SPECTRUM_SIGNAL_STRENGTH, "");
        return spectrumSignalStrength;
    }

    public static void setSpectrumSignalStrength(Context context,String spectrumSignalStrength) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString(AppConstants.LAST_SPECTRUM_SIGNAL_STRENGTH, spectrumSignalStrength);
        editor.apply();
        Actions.spectrumSignalStrength = spectrumSignalStrength;
    }


    //</editor-fold>
}
