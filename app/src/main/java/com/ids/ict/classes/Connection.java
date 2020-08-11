package com.ids.ict.classes;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.ids.ict.Actions;
import com.ids.ict.Error;
import com.ids.ict.MyApplication;
import com.ids.ict.MyParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.ParserConfigurationException;

public class Connection {
    private URL url;
    private com.ids.ict.Error error;
    Context context;
    MyApplication app;

    public Connection(Context context, String url) {
        this.context = context;
        app = (MyApplication) context.getApplicationContext();
        error = new com.ids.ict.Error();
        try {
            this.url = new URL(url);

        } catch (MalformedURLException e) {
            error.setState(true);
            // error.setMessage("Error in Url: "+e.toString());
            error.setMessage(e.getMessage());
        }

    }


    public static String  POST(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {

            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //  conn.setReadTimeout(15000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
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

                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
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


    public static String  POST2(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //  conn.setReadTimeout(15000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
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


    public Connection(String url) {
        error = new com.ids.ict.Error();
        try {
            this.url = new URL(url);
            Log.d("url", url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            error.setState(true);
            //error.setMessage("Error in Url: "+e.toString());
            error.setMessage("Ø®Ø·Ø£ Ù�ÙŠ ØªØ¹Ø±ÙŠÙ� Ø¹Ù†ÙˆØ§Ù† Ø§Ù„Ù…ÙˆÙ‚Ø¹, Ø§Ù„Ø±Ø¬Ø§Ø¡ Ù…Ø±Ø§Ø¬Ø¹Ø© Ø§Ù„Ù…Ø³Ø¤Ù„ Ø§Ù„ØªÙ‚Ù†ÙŠ.");
        }

    }


    public static String encodeParameters(String[][] parameters) {
        String charset = "UTF-8";
        String paramString = "?";
        String queryToFormat = "";
        // loop on parameters to get the names and put in the correct form
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0)
                queryToFormat = queryToFormat + parameters[i][0] + "=%s";// using
                // %s
                // to
                // use
                // later
                // to
                // subistitute
                // with
                // the
                // encoded
                // value
            else
                queryToFormat = queryToFormat + "&" + parameters[i][0] + "=%s";
        }
        // ...
        try {// encoding the values of parameters to be ready to send to web
            paramString = paramString
                    + String.format(queryToFormat,
                    URLEncoder.encode(parameters[0][1], charset),
                    URLEncoder.encode(parameters[1][1], charset),
                    URLEncoder.encode(parameters[2][1], charset),
                    URLEncoder.encode(parameters[3][1], charset),
                    URLEncoder.encode(parameters[4][1], charset),
                    URLEncoder.encode(parameters[5][1], charset),
                    URLEncoder.encode(parameters[6][1], charset));
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return paramString;
    }


    public static String encodeParameters1(String[][] parameters) {
        String charset = "UTF-8";
        String paramString = "?";
        String queryToFormat = "";
        // loop on parameters to get the names and put in the correct form
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0)
                queryToFormat = queryToFormat + parameters[i][0] + "=%s";// using
                // %s
                // to
                // use
                // later
                // to
                // subistitute
                // with
                // the
                // encoded
                // value
            else
                queryToFormat = queryToFormat + "&" + parameters[i][0] + "=%s";
        }
        // ...
        try {// encoding the values of parameters to be ready to send to web
            paramString = paramString
                    + String.format(queryToFormat,
                    URLEncoder.encode(parameters[0][1], charset),
                    URLEncoder.encode(parameters[1][1], charset),
                    URLEncoder.encode(parameters[2][1], charset),
                    URLEncoder.encode(parameters[3][1], charset),
                    URLEncoder.encode(parameters[4][1], charset));
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return paramString;
    }


    public String executeMultipartPost_event(String url1, String reg_id,
                                             String mob_number, String aff_num, String qatarID, String email,
                                             String issueDetailId, String serviceProviderID, String roamingId,
                                             String creationDate, String sendingDate, String comments,
                                             String locationX, String locationY, String countryId,
                                             String affectedQatarId, String serviceProviderCode,
                                             String IssueType, String complaintospId, String log_wait,
                                             String compl_ref_number, String complaintdate, String call_num,
                                             String call_date, String isIndividual, String isCorporate,
                                             String cRNumber, String statusId, String channelusedid,
                                             String isspecialneed, String specialneednumber, String dateOfRequest,
                                             String areaId, String subAreaId, String street, String building, String zone, String contactNum) throws Exception {

        // app =(MyApplication) context.getApplicationContext();
        String url = MyApplication.link + "PostData.asmx/SendReport3";

        String all_param1 = mob_number + " " + aff_num + " " + "11111111111"
                + " " + email + " " + issueDetailId + " " + serviceProviderID
                + " " + getcurrentDate() + " " + getcurrentDate() + " "
                + Actions.getDeviceName() + " ";

        String all_param2 = comments + " ";

        String all_param3 = locationY + " " + locationX + " " + countryId + " "
                + affectedQatarId + " " + serviceProviderCode + " " + "0";
        // String all_param4 = "6" + " " + "1" + " " + "1" + " " + "true" + " "
        // + "1234";
        String all_param = all_param1 + all_param2 + all_param3;

        String getoken = Actions.create_token_new();
        String checksum = Actions.ecryptPassword(all_param, "MD5");
        System.out.println("checksum " + checksum);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("token", reg_id));
        nameValuePairs.add(new BasicNameValuePair("mobileNumber", mob_number));
        nameValuePairs.add(new BasicNameValuePair("affectedNumber", aff_num));
        nameValuePairs.add(new BasicNameValuePair("qatarId", "11111111111"));
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("issueDetailId",
                issueDetailId));
        nameValuePairs.add(new BasicNameValuePair("DateOfRequest",
                dateOfRequest));
        nameValuePairs.add(new BasicNameValuePair("areaId",
                areaId));
        nameValuePairs.add(new BasicNameValuePair("subAreaId",
                subAreaId));
        nameValuePairs.add(new BasicNameValuePair("serviceProviderID",
                serviceProviderID));
        nameValuePairs.add(new BasicNameValuePair("creationDate",
                getcurrentDate()));
        nameValuePairs.add(new BasicNameValuePair("buildingNo",
                building));
        nameValuePairs.add(new BasicNameValuePair("zone",
                zone));
        nameValuePairs.add(new BasicNameValuePair("streetName",
                street));
        nameValuePairs.add(new BasicNameValuePair("sendingDate",
                getcurrentDate()));
        nameValuePairs.add(new BasicNameValuePair("mobileDevice", Actions
                .getDeviceName()));
        nameValuePairs.add(new BasicNameValuePair("comments", comments));
        nameValuePairs.add(new BasicNameValuePair("locationX", locationY));
        nameValuePairs.add(new BasicNameValuePair("locationY", locationX));
        nameValuePairs.add(new BasicNameValuePair("countryId", countryId));
        nameValuePairs.add(new BasicNameValuePair("affectedQatarId",
                affectedQatarId));
        nameValuePairs.add(new BasicNameValuePair("serviceProviderCode",
                serviceProviderCode));
        nameValuePairs.add(new BasicNameValuePair("advertisingID", "0"));
        nameValuePairs.add(new BasicNameValuePair("tokenNumber", getoken));
        nameValuePairs.add(new BasicNameValuePair("checksum", checksum));
        nameValuePairs.add(new BasicNameValuePair("typeId", IssueType));
        nameValuePairs.add(new BasicNameValuePair("didyoucomplaintospId",
                complaintospId));
        nameValuePairs.add(new BasicNameValuePair("logwaittime", log_wait));
        nameValuePairs.add(new BasicNameValuePair("spcomplaintrefnumber",
                compl_ref_number));
        nameValuePairs.add(new BasicNameValuePair("spcomplaintdate",
                complaintdate));
        nameValuePairs
                .add(new BasicNameValuePair("calledfromnumber", call_num));
        nameValuePairs.add(new BasicNameValuePair("calldate", call_date));
        nameValuePairs
                .add(new BasicNameValuePair("IsIndividual", isIndividual));
        nameValuePairs.add(new BasicNameValuePair("IsCorporate", isCorporate));
        nameValuePairs.add(new BasicNameValuePair("CRNumber", cRNumber));
        nameValuePairs.add(new BasicNameValuePair("statusId", "6"));
        nameValuePairs.add(new BasicNameValuePair("channelUsedId",
                channelusedid));
        nameValuePairs.add(new BasicNameValuePair("complaintStatusId", "1"));
        nameValuePairs.add(new BasicNameValuePair("isSpecialNeed",
                isspecialneed));
        nameValuePairs.add(new BasicNameValuePair("specialNeedRegistrationNumber", specialneednumber));

        //added recently
        nameValuePairs.add(new BasicNameValuePair("connectionType", MyApplication.connectionType));
        nameValuePairs.add(new BasicNameValuePair("contactNumber", contactNum));

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

        for (int i=0; i< nameValuePairs.size(); i++) {
            Log.wtf(": " + nameValuePairs.get(i).getName() , ": " + nameValuePairs.get(i).getValue());
        }

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        String res = convertStreamToString(is);

        return res;
    }

    public String UpdateLocationFunction(String token, String locationx, String locationy, String reportId) throws Exception {

        // app =(MyApplication) context.getApplicationContext();
        String url = MyApplication.link + "PostData.asmx/UpdateReportLocation";

        // DecimalFormat df = new DecimalFormat("#.##");
        // String dx = df.format(locationx);
        // double x = Double.valueOf(dx);
        // String dy = df.format(locationy);
        // double y = Double.valueOf(dy);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("token", token));
        nameValuePairs.add(new BasicNameValuePair("locationX", String
                .valueOf(locationx)));
        nameValuePairs.add(new BasicNameValuePair("locationY", String
                .valueOf(locationy)));
        nameValuePairs.add(new BasicNameValuePair("reportId", reportId));
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        String res = convertStreamToString(is);

        return res;
    }


    public String getcurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    public String executeMultipartPost_register(String url1, String mob_number,String deviceId,
                                                String qatarID, String email, String regisDate) throws Exception {

        String all_param = mob_number + " " + qatarID + " " + email + " "
                + regisDate + " " + "0" + " " + Actions.getDeviceName();

        System.out.println("all param " + all_param);
        String checksum = Actions.ecryptPassword(all_param, "MD5");
        System.out.println("checksum " + checksum);
        String tokennum = Actions.create_token_new();
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("deviceId", deviceId));
        nameValuePairs.add(new BasicNameValuePair("qatarId", qatarID));
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("mobileNumber", mob_number));
        nameValuePairs.add(new BasicNameValuePair("mobileDevice", Actions.getDeviceName()));
        nameValuePairs.add(new BasicNameValuePair("registrationDate", regisDate));
        nameValuePairs.add(new BasicNameValuePair("advertisingID", "0"));
        nameValuePairs.add(new BasicNameValuePair("tokenNumber", tokennum));
        nameValuePairs.add(new BasicNameValuePair("checksum", checksum));
        // ...ect
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url1);
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        String res = convertStreamToString(is);
        Log.wtf("ress", res);

        return res;
    }


    public String executeMultipartPost_verify_register(String url1, String password, String regisDate) throws Exception {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("password", password));
        nameValuePairs.add(new BasicNameValuePair("verificationDate", regisDate));
        // ...ect
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url1);
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        String res = convertStreamToString(is);
        Log.d("ress", res);

        return res;
    }


    public String executeMultipartPost_verify(String url, String otp, String transactionId, String transportKey) throws Exception {


        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("OTP", otp));
        nameValuePairs.add(new BasicNameValuePair("Transaction_Id", transactionId));
        nameValuePairs.add(new BasicNameValuePair("Transport_Key", transportKey));
        nameValuePairs.add(new BasicNameValuePair("mobileDevice", Actions.getDeviceName()));

        Log.wtf("executeMultipartPost_verify","url :" + url);
        Log.wtf("executeMultipartPost_verify","otp :" + otp);
        Log.wtf("executeMultipartPost_verify","transactionId :" + transactionId);
        Log.wtf("executeMultipartPost_verify","transportKey :" + transportKey);
        Log.wtf("executeMultipartPost_verify","mobileDevice :" + Actions.getDeviceName());
        // ...ect
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        String res = convertStreamToString(is);
        Log.wtf("ress", res);

        return res;
    }


    public String executeMultipartPost_login(String url1, String username, String password) throws Exception {


        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("username", username));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        // ...ect
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url1);
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        String res = convertStreamToString(is);
        Log.wtf("ress", res);

        return res;
    }


    public String executeMultipartPost_Send_Error(String activity_name, String devicemodel, String devicetypeid, String errormessage)
            throws Exception {

        String url1 = "https://arsel.qa/arselservicesStg/PostData.asmx/SaveServiceErrorLog";

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("deviceModel", devicemodel));
        nameValuePairs.add(new BasicNameValuePair("deviceTypeId", devicetypeid));
        nameValuePairs.add(new BasicNameValuePair("activityName", activity_name));
        nameValuePairs.add(new BasicNameValuePair("errorMessage", errormessage));

        // ...ect
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url1);
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        String res = convertStreamToString(is);
        Log.wtf("executeMultipartPost_Send_Error","result : " + res);

        return res;
    }


    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public String send() {
        HttpURLConnection urlConnection;
        String s = "";
        // OutputStream out;_ev
        try {
            // open connection
            urlConnection = (HttpURLConnection) url.openConnection();
            // set the encoding of connection same as parameters
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setDoOutput(true);
            // here the parameters send and response to it is received
            InputStream response = urlConnection.getInputStream();
            InputSource input = new InputSource(response);
            try {
                s = MyParser.ParseReply(input);
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // response.close();
            // urlConnection.disconnect();

        } catch (IOException e) {
            error.setState(true);
            s = setErrorMessage(e);
        }

        return s;
    }


    public boolean hasError() {
        return error.getState();
    }


    public Error getError() {
        return this.error;
    }


    public InputSource readWebPage() {
        Log.d("reading", "passing here");
        error.reset();
        InputSource page = null;
        try {
            URLConnection xmlConn = url.openConnection();
            InputStreamReader xmlStream = new InputStreamReader(
                    xmlConn.getInputStream());
            // build a buffered reader
            BufferedReader xmlBuff = new BufferedReader(xmlStream);
            page = new InputSource(xmlBuff);
            // xmlBuff.close();
        } catch (IOException e) {
            Log.d("no connection", "passed here");
            setErrorMessage(e);
        }
        return page;
    }


    private String setErrorMessage(IOException e) {

        String type = e.getClass().getName();
        error.setState(true);
        if (type.contains("FileNotFoundException"))
            error.setMessage("File not found exception!");
        else if (type.contains("UnknownHostException")
                || type.contains("ConnectException"))
            error.setMessage("No internet connection!");
        else if (type.contains("TimeOutException"))
            error.setMessage("Not able to load data. Weak connection!");
        else
            error.setMessage("Error in reading data!");
        return error.getMessage();
    }


    // to read image from url
    public Bitmap readImage() {
        error.reset();

        Bitmap bm = null;
        try {
            URLConnection conn = (URLConnection) this.url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            /* Buffered is always good for a performance plus. */
            BufferedInputStream bis = new BufferedInputStream(is);
			/* Decode url-data to a bitmap. */
            bm = BitmapFactory.decodeStream(is);
            bis.close();
            is.close();
        } catch (Exception e) {
            error.setState(true);
            error.setMessage("Error in reading loading image feed: "
                    + e.toString());
        }
		/*
		 * catch(IOException e) { error.setState(true);
		 * error.setMessage("Error in reading loading image feed: "
		 * +e.toString()); }
		 */
        return bm;
    }


    public Bitmap decodeFile() {
        Bitmap bm = null;
        try {
            URLConnection conn = (URLConnection) this.url.openConnection();
            conn.connect();
            InputStream in2 = conn.getInputStream();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            // BitmapFactory.decodeStream(in1,null,o);

            // The new size we want to scale to
            final int IMAGE_MAX_SIZE = 90;

            // Find the correct scale value. It should be the power of 2.
            int scale = 2;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(
                        2,
                        (int) Math.round(Math.log(IMAGE_MAX_SIZE
                                / (double) Math.max(o.outHeight, o.outWidth))
                                / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inJustDecodeBounds = false;
            o2.inSampleSize = 8;
            bm = BitmapFactory.decodeStream(in2, null, o2);
            return bm;
        } catch (Exception e) {
            return null;
        }
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static Drawable grabImageFromUrl(String url) throws Exception {
        return Drawable.createFromStream(
                (InputStream) new URL(url).getContent(), "src");
    }


    public String getFromService(String remove) {

        // build the string to store the response text from the server
        error.reset();
        String response = "";
        HttpURLConnection conn;

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // to read the response
            BufferedReader bufferReader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String StringBuffer;

            while ((StringBuffer = bufferReader.readLine()) != null) {
                response += StringBuffer;
            }
            // to remove the leading"0*" character in the retuned value
            // using // before * to escape the special character
            response = response.replaceAll(remove, "");

            bufferReader.close();

        } catch (IOException e) {

            error.setState(true);
            String s = setErrorMessage(e);
        }
        return response;

    }


    public String getXmlFromUrl() {
        String xml = null;

        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(url.toString());

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            error.setState(true);
            error.setMessage(this.setErrorMessage(e));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            error.setState(true);
            error.setMessage(this.setErrorMessage(e));

        } catch (IOException e) {
            e.printStackTrace();
            error.setState(true);
            error.setMessage(this.setErrorMessage(e));

        }
        // return XML
        return xml;
    }


    public String registerUnregisterPushNotification(String regId) {

        // Create a new HttpClient and Post Header

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(this.url.toString());

        String result = "";// contain returned values from url
        try {
            // Add user name and password
            // httppost.setHeader("deviceType", "1");//encode to use arabic
            // characters
            // httppost.setHeader("deviceToken", regId);
            // httppost.setHeader("deviceModel", Actions.getDeviceName());
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("deviceType", "1"));
            nameValuePairs.add(new BasicNameValuePair("deviceToken", regId));
            nameValuePairs.add(new BasicNameValuePair("deviceModel", Actions
                    .getDeviceName()));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            error.setState(true);
            error.setMessage(this.setErrorMessage(e));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            error.setState(true);
            error.setMessage(this.setErrorMessage(e));

        } catch (IOException e) {
            e.printStackTrace();
            error.setState(true);
            error.setMessage(this.setErrorMessage(e));

        }

        return result;

    }


    public InputStream getInputStream() {
        InputStream stream = null;

        try {
            HttpURLConnection conn;

            conn = (HttpURLConnection) this.url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(30000);// 30 seconds
            stream = conn.getInputStream();

        } catch (SocketTimeoutException e) {
            error.setState(true);

            setErrorMessage(e);

        } catch (IOException e) {

            error.setState(true);

            setErrorMessage(e);
        }

        return stream;
    }


    public InputStream getInputStream2() {
        InputStream stream = null;

        try {
            HttpURLConnection conn;
            conn = (HttpURLConnection) this.url.openConnection();

            //conn.setDoOutput(true);
            //conn.setDoInput(true);
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            //conn.setRequestProperty("Accept","*/*");
            boolean isError = conn.getResponseCode() >= 400;
            Log.wtf("Error?",""+isError);
            conn.setReadTimeout(30000);// 30 seconds
            conn.connect();
            stream = conn.getInputStream();

        } catch (SocketTimeoutException e) {
            error.setState(true);

            setErrorMessage(e);

        } catch (IOException e) {

            error.setState(true);

            setErrorMessage(e);
        }

        return stream;
    }



    public String readWebPageString() {
        error.reset();
        String stringText = "";

        try {

            BufferedReader bufferReader = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            String StringBuffer;

            while ((StringBuffer = bufferReader.readLine()) != null) {
                stringText += StringBuffer;
            }
            bufferReader.close();

        } catch (IOException e) {

            error.setState(true);
            // error.setMessage("Error in reading news feed: "+e.toString());
            setErrorMessage(e);
            return stringText;

        }
        return stringText;
    }
}
