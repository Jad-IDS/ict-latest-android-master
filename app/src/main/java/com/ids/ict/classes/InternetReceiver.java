package com.ids.ict.classes;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.activities.SplashActivity;
import com.ids.ict.parser.QosParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.content.Context.JOB_SCHEDULER_SERVICE;


public class InternetReceiver extends BroadcastReceiver {


    ArrayList<QosTest> array;
    SharedPreference settings;

    public InternetReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Actions.isWifiAvailable(context)) {

            Log.wtf("App gained", "Wifi connection");
            try {
                SharedPreference settings = null;

     /*           try {
                    //<editor-fold desc="tickets section">
                    settings = new SharedPreference();
                    ArrayList<Mail_OFF> ticketsArray = settings.getFavorites(context);
                    Log.wtf("ticketsArray size", "" + ticketsArray.size());

                    int i = 0;
                    while (i < ticketsArray.size()) {
                        SendReport send = new SendReport();
                        send.execute(ticketsArray.get(i));
                        //   settings.removeFavorite( context, array.get(i));
                        ticketsArray.remove(i);
                        i = 0;
                    }
                    ticketsArray.clear();
                    settings.saveFavorites(context, ticketsArray);
                    //</editor-fold>
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                //<editor-fold desc="mobile qos tests section">
                /*settings = new SharedPreference();
                array = settings.getMobileDataQosTests(context);
                Log.wtf("mobile qos array size", "" + array.size());
                Log.d("mobile qos array size", "" + array.size());

                Log.wtf("Receiver isWifiAvailable", "yes");

                if (array.size() > 0) {

                    PostQosTest postQosTest = new PostQosTest(context, array, array.get(0));
                    postQosTest.executeOnExecutor(MyApplication.threadPoolExecutor);
                }*/

                //<editor-fold desc="wifi check and send">
                /*if (Actions.isWifiAvailable(context)) { //send posts not offline

                    Log.wtf("Receiver isWifiAvailable", "yes");

                    if (array.size() > 0) {

                        PostQosTest postQosTest = new PostQosTest(context, array, array.get(0));
                        postQosTest.executeOnExecutor(MyApplication.threadPoolExecutor);
                    }
                }*/
                //</editor-fold>

                TCTDbAdapter database = new TCTDbAdapter(context);
                database.open();
                array = database.getAllMobileQosTests();
                try{Log.wtf("array_db_size",array.size()+"");}catch (Exception e){Log.wtf("array_db_size_exception",e.toString());}
                try{Log.wtf("array_db_size_from_count",database.getQosTestsCount()+"");}catch (Exception e){Log.wtf("array_db_size_count_exception",e.toString());}
                int size = 0;
                try {

                    size = array.size();
                }catch (Exception e){
                    Log.wtf("database_exception",e.toString());
                    e.printStackTrace();
                    size = 0;
                }

                Log.wtf("size of queue", "is "+ size);
                if (size > 0) {

                    QosTest test = array.get(0);
                    database.deleteQosTest(array.get(0).getId());
                    PostQosTest postQosTest = new PostQosTest(context,test);
                    postQosTest.executeOnExecutor(MyApplication.threadPoolExecutor);
                }

                //</editor-fold>
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        /*TCTDbAdapter dtBase = new TCTDbAdapter(context);
        dtBase.open();
        ArrayList<Profile> profileList = dtBase.getAllProfiles();
        if (profileList.size() > 0) {

            //<editor-fold desc="Online Check Job Service">
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
                ComponentName componentName = new ComponentName(context, OnlineCheckJobService.class);
                JobInfo jobInfo = null;


                if (Build.VERSION.SDK_INT >= 24) {

                    jobInfo = new JobInfo.Builder(1, componentName)
                            .setMinimumLatency(10 * 1000)
                            .build();

                } else {

                    jobInfo = new JobInfo.Builder(1, componentName)
                            .setPeriodic(10 * 1000)
                            .build();
                }

                //<editor-fold desc="job scheduler">
                if (!isScheduledJobServiceOn(jobScheduler))
                    jobScheduler.schedule(jobInfo);
                //</editor-fold>
            }
            //</editor-fold>
        }*/


    }

    private boolean isScheduledJobServiceOn(JobScheduler jobScheduler) {

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

    private void setGeoLocation(Context context, QosTest qosTest) {

        if (qosTest.getLocality().length() == 0 && qosTest.getLocationX().length() > 0) {

            String countryName, locality, subLocality, province, route;

            try {
                Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(qosTest.getLocationX()), Double.parseDouble(qosTest.getLocationY()), 1);
                Address obj = addresses.get(0);

                try {
                    countryName = obj.getCountryName();
                } catch (Exception e) {
                    e.printStackTrace();
                    countryName = "";
                }

                try {
                    locality = obj.getLocality() == null ? obj.getAdminArea() : obj.getLocality();
                } catch (Exception e) {
                    e.printStackTrace();
                    locality = "";
                }

                try {
                    subLocality = obj.getSubLocality() == null ? obj.getSubAdminArea() : obj.getSubLocality();
                } catch (Exception e) {
                    e.printStackTrace();
                    subLocality = "";
                }

                try {
                    province = countryName;
                } catch (Exception e) {
                    e.printStackTrace();
                    province = "";
                }

                try {
                    route = obj.getThoroughfare() == null ? "" : obj.getThoroughfare();
                } catch (Exception e) {
                    e.printStackTrace();
                    route = "";
                }

                Log.wtf("countryName", "countryName is: " + countryName);
                Log.d("countryName", "countryName is: " + countryName);

                Log.wtf("locality", "locality is: " + locality);
                Log.d("locality", "locality is: " + locality);

                Log.wtf("subLocality", "subLocality is: " + subLocality);
                Log.d("subLocality", "subLocality is: " + subLocality);

                Log.wtf("province", "province is: " + province);
                Log.d("province", "province is: " + province);

                Log.wtf("route", "route is: " + route);
                Log.d("route", "route is: " + route);


            } catch (Exception e) {
                e.printStackTrace();
                countryName = "";
                locality = "";
                subLocality = "";
                province = "";
                route = "";

                Log.wtf("EX countryName", "countryName is: " + countryName);
                Log.d("EX countryName", "countryName is: " + countryName);
            }

            try {
                qosTest.setLocationIPAddress(countryName);
            } catch (Exception e) {
                e.printStackTrace();
                qosTest.setLocationIPAddress("");
            }

            try {
                qosTest.setLocality(locality);
            } catch (Exception e) {
                e.printStackTrace();
                qosTest.setLocality("");
            }

            try {
                qosTest.setSubLocality(subLocality);
            } catch (Exception e) {
                e.printStackTrace();
                qosTest.setSubLocality("");
            }

            try {
                qosTest.setProvince(province);
            } catch (Exception e) {
                e.printStackTrace();
                qosTest.setProvince("");
            }

            try {
                qosTest.setRoute(route);
            } catch (Exception e) {
                e.printStackTrace();
                qosTest.setRoute("");
            }
        }
    }

    private class PostQosTest extends AsyncTask<Void, Void, String> {

        String res;
        Profile profile;
        Context context;
        QosTest qosTest;
        SharedPreference settings;

        public PostQosTest(Context context, QosTest qosTest) {

            this.context = context;
            this.qosTest = qosTest;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            TCTDbAdapter sour = new TCTDbAdapter(context);
            sour.open();
            ArrayList<Profile> arr = sour.getAllProfiles();
            profile = arr.get(0);
            sour.close();

            //settings = new SharedPreference();

            setGeoLocation(context, qosTest);

            //array.remove(0);
            //settings.saveMobileDataQosTests(context, array);
        }

        @Override
        protected String doInBackground(Void... a) {
            // URLEncoder.encode(searchText, "utf-8");
            String url = MyApplication.link + MyApplication.post
                    + "PostQoSTest?";

            if (Actions.isWifiAvailable(context)) {

                //<editor-fold desc="old way">
                try {
                    url = url
                            + "MobileNumber="
                            + URLEncoder.encode(qosTest.getMobileNumber(), "utf-8")
                            + "&"
                            + "ServiceProvider="
                            + URLEncoder.encode(qosTest.getServiceProvider(), "utf-8")
                            + "&"
                            + "IpAddress="
                            + URLEncoder.encode(qosTest.getIp(), "utf-8")
                            + "&"
                            + "LocationIPAddress="
                            + URLEncoder.encode(qosTest.getLocationIPAddress(), "utf-8")
                            + "&"
                            + "DeviceId="
                            + URLEncoder.encode(String.valueOf(qosTest.getDeviceId()), "utf-8")
                            + "&"
                            + "LocationX="
                            + URLEncoder.encode(qosTest.getLocationX(), "utf-8")
                            + "&"
                            + "LocationY="
                            + URLEncoder.encode(qosTest.getLocationY(), "utf-8")
                            + "&"
                            + "TestTypeId="
                            + URLEncoder.encode(qosTest.getTestType(), "utf-8")
                            + "&"
                            + "CallDisconnectionReason="
                            + URLEncoder.encode(qosTest.getCallDisconnectionReason(), "utf-8")
                            + "&"
                            + "TestDate="
                            + URLEncoder.encode(qosTest.getTestDateTime(), "utf-8")
                            + "&"
                            + "SignalStrength="
                            + URLEncoder.encode(qosTest.getSignalStrength(), "utf-8")
                            + "&"
                            + "ConnectionType="
                            + URLEncoder.encode(qosTest.getConnectionType(), "utf-8")
                            + "&"
                            + "CallDuration="
                            + URLEncoder.encode(qosTest.getCallDuration(), "utf-8")
                            + "&"
                            + "TestTriggerTypeId="
                            + URLEncoder.encode(qosTest.getTestTriggerType(), "utf-8")
                            + "&"
                            + "TestTriggerId="
                            + URLEncoder.encode(qosTest.getAdHocRequestId(), "utf-8")
                            + "&"
                            + "locality="
                            + URLEncoder.encode(qosTest.getLocality(), "utf-8")
                            + "&"
                            + "sublocality="
                            + URLEncoder.encode(qosTest.getSubLocality(), "utf-8")
                            + "&"
                            + "route="
                            + URLEncoder.encode(qosTest.getRoute(), "utf-8")
                            + "&"
                            + "country="
                            + URLEncoder.encode(qosTest.getProvince(), "utf-8")
                            + "&"
                            + "SpectrumSignalStrength="
                            + URLEncoder.encode(qosTest.getSpectrumSignalStrength(), "utf-8")
                            + "&"
                            + "SignalQuality="
                            + URLEncoder.encode(qosTest.getSignalQuality(), "utf-8")
                            + "&"
                            + "notificationId="
                            + URLEncoder.encode(String.valueOf(qosTest.getNotificationId()), "utf-8");


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                Connection conn = new Connection(url);

                try {

                    QosParser parser = new QosParser();

                    res = parser.parse(conn.getInputStream2());

                    return res;
                } catch (Exception e) {

                    e.printStackTrace();
                }
                //</editor-fold>

                return res;

            } else { // save it in shared prefs

                /*SharedPreference settings = new SharedPreference();
                ArrayList<QosTest> array = settings.getMobileDataQosTests(context);
                if (array == null) {
                    Log.wtf("null", "array");
                    array = new ArrayList<QosTest>();
                    array.add(qosTest);
                    settings.saveMobileDataQosTests(context, array);
                } else
                    settings.addMobileDataQosTest(context, qosTest);*/

                return "";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            TCTDbAdapter database = new TCTDbAdapter(context);

            if (result.equalsIgnoreCase("Success: true")) {
                Log.wtf("*=*= Success", " sent");

                /*database.open();
                database.deleteQosTest(qosTest.getId());
                array.remove(0);
                settings.saveMobileDataQosTests(context, array);*/

            } else {

                Log.wtf("*=*= Failure", " not sent");
                Actions.UpdateNotificationStatus(qosTest.getNotificationId());
            }

            try {

                database.open();
               // int count = database.getMobileQosTestsCount();
                int count = database.getQosTestsCount();

                Log.wtf("getMobileQosTestsCount", "is "+count);
                database.close();
                if (count > 0) {

                    Intent intent = new Intent("com.ids.ict.classes.InternetReceiver");
                    context.sendBroadcast(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getcurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a",
                new Locale("en"));
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public class SendReport extends AsyncTask<Mail_OFF, Void, Void> {

        /*Context context;

        SendReport(Context context){

            this.context = context;
        }*/

        @Override
        protected Void doInBackground(Mail_OFF... params) {
            Mail_OFF tick = params[0];

            Connection conn = null;
            conn = new Connection("" + MyApplication.link
                    + "PostData.asmx/SendReport");
            try {
                if (tick.getloc().equals("")) tick.setloc("165");
                String s = conn.executeMultipartPost_event(
                        "PostData.asmx/SendReport3", gettoken(tick.getPassword()), tick.getnum(), tick.getAffectedNumber(),
                        tick.getqatarID(), tick.getemail(),
                        tick.getissueid(), tick.getspid(), "1", getcurrentDate(),
                        getcurrentDate(), tick.getcomm(), ""
                                + tick.getlocy(), "" + tick.getlocx(), tick.getloc(), tick.getaffecqatarid(),
                        "6", tick.getissuename(), "0", "false", "0", tick.getdate(), "", tick.getdate(), //issue type w bl rayeh
                        "false", "false", "0", "1", tick.getChannelUsedId(), String
                                .valueOf(tick.getIsSpecialNeed()), tick.getSpecialneednumber(),
                        tick.getDateOfRequest(), tick.getAreaId(),
                        tick.getSubAreaId(), tick.getStreet(), tick.getBuilding(), tick.getZone(), tick.getContactNumber());

                System.out.println("String  " + s);

                // String m = splitres(s);


            } catch (Exception e) {

            }
            return null;
        }

    }

    public String gettoken(String pass) {
        String name = "";
        try {
            URL url = new URL("" + MyApplication.link + "PostData.asmx/StartSendingReport?password=" + pass);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("string");
            Element nameElement = (Element) nodeList.item(0);
            nodeList = nameElement.getChildNodes();
            name = ((Node) nodeList.item(0)).getNodeValue();
        } catch (Exception e) {
            System.out.println("gg " + e);
            String url1 = "" + MyApplication.link + "PostData.asmx/VerifyRegistration";
            Connection conn = new Connection(url1);
            try {
//                String error_return = conn.executeMultipartPost_Send_Error(this
//                                .getClass().getSimpleName(), Actions.getDeviceName(),
//                        "1", e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return name;
    }
}