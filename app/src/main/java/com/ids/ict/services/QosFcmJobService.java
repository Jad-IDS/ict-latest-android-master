package com.ids.ict.services;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.NetworkUtil;
import com.ids.ict.classes.Profile;
import com.ids.ict.classes.QosNotification;
import com.ids.ict.classes.QosTest;
import com.ids.ict.classes.SharedPreference;
import com.ids.ict.parser.QosParser;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class QosFcmJobService extends JobService {

    JobParameters params;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    SharedPreferences mshared;
    SharedPreferences.Editor edit;

    double longitude = 0.0, latitude = 0.0;
    String provider;
    boolean waitingForLocationUpdate = true, sendTest = false;
    private int signalSupport = 0;
    String countryName = "", locality = "", subLocality = "", route = "", province = "", testDate = "";
    int freq, scheduleId;
    String testType, testTriggerType, testTriggerId;
    TelephonyManager telephonyManager;
    MyPhoneStateListener psListener = null;
    QosNotification qosNotification;
    PowerManager pm;
    PowerManager.WakeLock wl;
    WifiManager.WifiLock mWifiLock = null;

    int i = 0;


    @Override
    public void onCreate() {
        super.onCreate();

        try {
            psListener = new MyPhoneStateListener();
            telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //<editor-fold desc="wake lock service">
        try {
            pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "QosData");

            if ((wl != null) && (!wl.isHeld())) {
                wl.acquire(60 * 60 * 1000L /*60 minutes*/);
            }

            if (!pm.isScreenOn()) {
                wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "QosData");
                if ((wl != null) && (!wl.isHeld())) {
                    wl.acquire(60 * 60 * 1000L /*60 minutes*/);
                }
            }
            holdWifiLock();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //</editor-fold>

        mshared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = mshared.edit();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }


    @Override
    public boolean onStartJob(final JobParameters params) {

        this.params = params;

        new Thread(new Runnable() {
            @Override
            public void run() {

                //<editor-fold desc="job">

                String json = params.getExtras().getString("qosNotification");
                Gson g = new Gson();
                qosNotification = g.fromJson(json, QosNotification.class);

                freq = qosNotification.getFreq();

                scheduleId = qosNotification.getScheduleId();

                testTriggerType = qosNotification.getTestTriggerType();

                testType = qosNotification.getTestType();

                testTriggerId = qosNotification.getTestTriggerId();

                Log.wtf("- 59", "is: " + Actions.convertMilliSecondsToFormattedDate(new Date(System.currentTimeMillis() - 59000L)));
                Log.wtf("time sent", "is: " + Actions.convertMilliSecondsToFormattedDate(qosNotification.getDate()));
                Log.wtf("+ 59", "is: " + Actions.convertMilliSecondsToFormattedDate(new Date(System.currentTimeMillis() + 59000L)));

                Log.d("- 59", "is: " + Actions.convertMilliSecondsToFormattedDate(new Date(System.currentTimeMillis() - 59000L)));
                Log.d("time sent", "is: " + Actions.convertMilliSecondsToFormattedDate(qosNotification.getDate()));
                Log.d("+ 59", "is: " + Actions.convertMilliSecondsToFormattedDate(new Date(System.currentTimeMillis() + 59000L)));

                if (qosNotification.getDate().after(new Date(System.currentTimeMillis() - 59000L)) &&
                        qosNotification.getDate().before(new Date(System.currentTimeMillis() + 59000L))) {  //Send Immediately

                    Log.wtf("Send", "Immediately");
                    Log.d("Send", "Immediately");

                    //<editor-fold desc = "Fcm way">
                    try {

                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            Log.wtf("GPS ", "OFF");
                            Log.d("GPS ", "OFF");
                            latitude = 0;
                            longitude = 0;

                            if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {

                                sendQosTest();

                            } else { //listen to signal strength

                                Log.wtf("there's", "sim");

                                sendTest = true;
                                telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                                telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                            }

                        } else {

                            Log.wtf("GPS ", "ON");
                            Log.d("GPS ", "ON");


                            try {
                                registerLocationUpdates(signalSupport);
                            } catch (Exception e) {
                                e.printStackTrace();


                                Log.wtf("Exception registerLocationUpdates", e.getMessage());
                            }
                        }

                        Log.wtf("after  ", "listen");

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.wtf("Exception e", e.getMessage());
                    }
                    //</editor-fold>


                    SharedPreference settings = new SharedPreference();
                    int size = 0;
                    try {
                        size = settings.getNoInternetQosTests(getApplicationContext()).size();
                    } catch (Exception e) {
                        e.printStackTrace();
                        size = 0;
                    }

                    if (size > 0) {

                        settings.removeAllNoInternetQosTests(getApplicationContext());
                    }

                } else if (qosNotification.getDate().before(new Date(System.currentTimeMillis() + 59000L))) { //filteration queue2

                    //<editor-fold desc="group of notifications">

                    Log.wtf("Not Sent", "Immediately");

                    SharedPreference settings = new SharedPreference();
                    ArrayList<QosTest> array = settings.getNoInternetQosTests(getApplicationContext());
                    int size = 0;

                    try {
                        size = array.size();
                    } catch (Exception e) {
                        e.printStackTrace();
                        size = 0;
                    }
                    Log.wtf("array", "size " + size);

                    int j = 0;
                    while (j < size) {

                        QosTest test = array.get(j);

                        if (qosNotification.getDate().after(new Date(Long.parseLong(test.getTimeStamp()) - 59000L)) &&
                                qosNotification.getDate().before(new Date(Long.parseLong(test.getTimeStamp()) + 59000L))) {

                            Log.wtf(" between", "range");


                            test.setSignalStrengthFlag(testType.equals("104"));

                            try {
                                test.setTestType(testType);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                test.setAdHocRequestId(testTriggerId);//testTriggerType.equals("104") ? String.valueOf(scheduleId) : testTriggerId);
                            } catch (Exception e) {
                                e.printStackTrace();
                                test.setAdHocRequestId("-1");
                            }

                            try {
                                test.setTestTriggerType(testTriggerType);
                            } catch (Exception e) {
                                e.printStackTrace();
                                test.setTestTriggerType("");
                            }

                            postTest(test);
                            array.remove(j);
                            settings.saveNoInternetQosTests(getApplicationContext(), array);
                            break;
                        } else {

                            Log.wtf(" not in", "range");
                        }
                        j++;
                    }

                    //</editor-fold>
                }
                //</editor-fold>
            }
        }).start();

        return false;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


    private void holdWifiLock() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (mWifiLock == null)
            mWifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "WifiLock");

        mWifiLock.setReferenceCounted(false);

        if (!mWifiLock.isHeld())
            mWifiLock.acquire();
    }


    private void releaseWifiLock() {

        if (mWifiLock == null)
            Log.w("WifiLock", "#releaseWifiLock mWifiLock was not created previously");

        if (mWifiLock != null && mWifiLock.isHeld()) {
            mWifiLock.release();
            //mWifiLock = null;
        }

    }


    public class MyPhoneStateListener extends PhoneStateListener {

        public MyPhoneStateListener() {

            sendTest = false;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                // only for nougat and newer versions
                telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private int getSignalLevel(final SignalStrength signal) {
            Log.wtf("in signal", "level");
            try {
                final Method m = SignalStrength.class.getDeclaredMethod("getLevel", (Class[]) null);
                m.setAccessible(true);
                return (Integer) m.invoke(signal, (Object[]) null);
            } catch (Exception e) {
                return 0;
            }
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                signalSupport = signalStrength.getLevel();
            } else {

                signalSupport = getSignalLevel(signalStrength);
            }
            Log.wtf("SIGNAL SUPPORT ", "IS " + signalSupport);

            if (sendTest) {
                sendTest = false;
                sendQosTest();
            }
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            Log.wtf("onCallStateChanged", "------ gsm signal --> " + signalSupport);
        }
    }


    private void registerLocationUpdates(int signalSupport) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        provider = locationManager.getBestProvider(criteria, true);

        // Cant get a hold of provider
        if (provider == null) {
            return;
        }

        locationListener = new MyLocationListener(signalSupport);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        // connect to the GPS location service
        Location oldLocation = locationManager.getLastKnownLocation(provider);

        if (oldLocation != null) {
            waitingForLocationUpdate = false;
        }
    }


    private class MyLocationListener implements LocationListener {

        private int signalSupport;

        public MyLocationListener(int signalSupport) {

            sendTest = false;
            this.signalSupport = signalSupport;
        }

        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
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

                /*Log.wtf("countryName", "countryName is: " + countryName);
                Log.d("countryName", "countryName is: " + countryName);

                Log.wtf("locality", "locality is: " + locality);
                Log.d("locality", "locality is: " + locality);

                Log.wtf("subLocality", "subLocality is: " + subLocality);
                Log.d("subLocality", "subLocality is: " + subLocality);

                Log.wtf("province", "province is: " + province);
                Log.d("province", "province is: " + province);

                Log.wtf("route", "route is: " + route);
                Log.d("route", "route is: " + route);*/

            } catch (IOException e) {
                e.printStackTrace();
                countryName = "";
                locality = "";
                subLocality = "";
                province = "";
                route = "";

                Log.wtf("EX countryName", "countryName is: " + countryName);
                Log.d("EX countryName", "countryName is: " + countryName);
            }

            if (waitingForLocationUpdate) {
                // getNearbyStores();
                waitingForLocationUpdate = false;
            }

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d("no permission", "no permission");
                return;
            }
            locationManager.removeUpdates(this);

            sendTest = false;

            if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {

                sendQosTest();

            } else { //listen to signal strength

                sendTest = true;
                telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            }
        }

        public void onStatusChanged(String s, int i, Bundle bundle) {
            // Log.v(TAG, "Status changed: " + s);
        }

        public void onProviderEnabled(String s) {
            // Log.e(TAG, "PROVIDER DISABLED: " + s);
        }

        public void onProviderDisabled(String s) {
            // Log.e(TAG, "PROVIDER DISABLED: " + s);
        }
    }


    //QosTest qosTest;
    private void sendQosTest() {

        Log.wtf("@@@@@@ sendQosTest ", "is sendQosTest");
        Log.d("@@@@@@ sendQosTest ", "is sendQosTest");

        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        TCTDbAdapter sour = new TCTDbAdapter(getApplicationContext());
        sour.open();
        ArrayList<Profile> arr = sour.getAllProfiles();
        Profile profile = arr.get(0);
        sour.close();

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getSimOperatorName();
        Log.wtf("@@@@@@ carrierName ", "is " + carrierName);

        QosTest qosTest = new QosTest();

        Log.d("----- sending   ", " data --------");

        //<editor-fold desc = "Setting data">
        qosTest.setId(0);

        qosTest.setSignalStrengthFlag(testType.equals("104"));

        try {
            qosTest.setMobileNumber(profile.getnum());
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setMobileNumber("");
        }

        try {
            qosTest.setDeviceId(String.valueOf(mshared.getInt(getString(R.string.device_id), 0)));
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setDeviceId("");
        }

        try {
            qosTest.setDeviceModel(Actions.getDeviceName());
        } catch (Exception e) {

            qosTest.setDeviceModel("");
        }

        qosTest.setDeviceType("1");

        try {
            qosTest.setServiceProvider(carrierName);
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setServiceProvider("");
        }

        try {
            qosTest.setIp(MyApplication.ip);
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setIp("");
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

        try {
            qosTest.setLocationX(Double.toString(latitude));
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setLocationX("");
        }

        try {
            qosTest.setLocationY(Double.toString(longitude));
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setLocationY("");
        }

        try {
            qosTest.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setTimeStamp("");
        }

        try {
            qosTest.setTestType(testType);
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setTestType("");
        }

        qosTest.setCallDisconnectionReason("");

        try {
            testDate = new SimpleDateFormat("HH:mm:ss M-dd-yyyy", Locale.ENGLISH).format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            qosTest.setTestDateTime(testDate);
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setTestDateTime(testDate);
        }

        try {
            qosTest.setIsIncident("");
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setIsIncident("");
        }

        try {
            qosTest.setConnectionType(NetworkUtil.getNetworkClass(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setConnectionType(NetworkUtil.getNetworkClass(getApplicationContext()));
        }

        try {
            if (qosTest.getConnectionType().equals("No Sim")) {

                qosTest.setSignalStrength("0");
            } else {

                qosTest.setSignalStrength(String.valueOf(signalSupport));
            }
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setSignalStrength("0");
        }

        try {
            qosTest.setCallDuration("");
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setCallDuration("");
        }

        try {
            qosTest.setTestTriggerType(testTriggerType);
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setTestTriggerType("");
        }

        try {
            qosTest.setTriggerStartDate("");
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setTriggerStartDate("");
        }

        try {
            qosTest.setValidationEndDate("");
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setValidationEndDate("");
        }

        try {
            qosTest.setResultId("");
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setResultId("");
        }

        try {
            qosTest.setStatusId("");
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setStatusId("-1");
        }

        try {
            qosTest.setAdHocRequestId(testTriggerId);//testTriggerType.equals(MyApplication.ROUTINE_TEST_TRIGGER) ? MyApplication.ROUTINE_TEST_TRIGGER : testTriggerId);
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setAdHocRequestId("-1");
        }
        //</editor-fold>

        Log.d("----- End sending   ", " data --------");

        Log.wtf("Ended!!!! ", "Execution");
        Log.d("Ended!!!! ", "Execution");

        Log.wtf("INSERT", "AND POST");
        Log.d("INSERT", "AND POST");

        TCTDbAdapter database = new TCTDbAdapter(getApplicationContext());
        database.open();
        database.insertAndPost(getApplicationContext(), qosTest);

        try {
            wl.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            releaseWifiLock();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try {
            final JobScheduler fcmJob = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
            if (fcmJob.getAllPendingJobs().size() > 0) {

                Log.d("PENDING", "JOBS");
                Log.wtf("PENDING", "JOBS");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("POST", "HANDLER");
                        Log.wtf("POST", "HANDLER");
                        for (i = 0; i < fcmJob.getAllPendingJobs().size(); i++) {
                            fcmJob.schedule(fcmJob.getAllPendingJobs().get(i));
                        }
                    }
                }, 3000);

            } else {

                Log.d("NO", "PENDIND");
                Log.wtf("NO", "PENDIND");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        jobFinished(params, false);
    }


    private void postTest(QosTest qosTest) {

        PostQosTest postQosTest = new PostQosTest(getApplicationContext(), qosTest);
        postQosTest.executeOnExecutor(MyApplication.threadPoolExecutor);
    }


    private class PostQosTest extends AsyncTask<Void, Void, String> {

        String res;
        Profile profile;
        Context context;
        QosTest qosTest;

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
        }

        @Override
        protected String doInBackground(Void... a) {
            // URLEncoder.encode(searchText, "utf-8");
            //Connection.disableSSLCertificateChecking();
            Log.wtf("Do", "IN BACKGROUND");
            Log.d("Do", "IN BACKGROUND");

            String url = MyApplication.link + MyApplication.post + "PostQoSTest?";

            if (Actions.isWifiAvailable(context)) {

                //<editor-fold desc="old way">
                try {

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd", Locale.ENGLISH);
                    String formattedDate = df.format(c.getTime());

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
                            //+ URLEncoder.encode(qosTest.getTestDateTime(), "utf-8")
                            + formattedDate
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
                            + URLEncoder.encode(qosTest.getProvince(), "utf-8");

                } catch (Exception e) {
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

                SharedPreference settings = new SharedPreference();
                ArrayList<QosTest> array = settings.getMobileDataQosTests(context);
                if (array == null) {
                    Log.wtf("null", "array");
                    array = new ArrayList<QosTest>();
                    array.add(qosTest);
                    settings.saveMobileDataQosTests(context, array);
                } else
                    settings.addMobileDataQosTest(context, qosTest);

                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                wl.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                releaseWifiLock();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (result.equalsIgnoreCase("Success: true")) {
                Log.wtf("*=*= Success", " sent");
                Log.d("*=*= Success", " sent");
            } else {

                Log.wtf("*=*= Failure", " not sent");
                Log.d("*=*= Failure", " not sent");
            }

            jobFinished(params, false);
            Intent bc = new Intent("com.ids.ict.classes.InternetReceiver");
            sendBroadcast(bc);

        }
    }
}
