package com.ids.ict.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
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
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.ids.ict.Actions;
import com.ids.ict.asynctask.QosTestSenderTask;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.parser.QosParser;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.NetworkUtil;
import com.ids.ict.classes.Profile;
import com.ids.ict.classes.QosNotification;
import com.ids.ict.classes.QosTest;
import com.ids.ict.classes.SharedPreference;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class QosFcmServiceTest extends Service {


    protected LocationManager locationManager;
    protected LocationListener locationListener;
    SharedPreferences mshared;
    SharedPreferences.Editor edit;

    double longitude = 0.0, latitude = 0.0;
    String provider;
    boolean waitingForLocationUpdate = true, sendTest = false;
    private int signalSupport = 0;
    String countryName = "", locality = "", subLocality = "", route = "", province = "", testDate = "", spectrumSignalStrength = "", signalQuality = "";
    int freq, scheduleId;
    String testType, testTriggerType, testTriggerId;
    TelephonyManager telephonyManager;
    MyPhoneStateListener psListener = null;
    QosNotification qosNotification;
    PowerManager pm;
    PowerManager.WakeLock wl;
    WifiManager.WifiLock mWifiLock = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {

            Actions.PostLog(getApplicationContext(), MyApplication.TAG, "Job Destroyed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();

        try {
            psListener = new MyPhoneStateListener();
            telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        } catch (Exception e) {
            Log.wtf("pslistener exception1",e.toString());
            e.printStackTrace();
        }
    }


    @SuppressLint("InvalidWakeLockTag")
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        //<editor-fold desc="code">

        //<editor-fold desc="wake lock service">
        try {
            pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "QosData");

            if ((wl != null) && (!wl.isHeld())) {
                wl.acquire();//60 * 60 * 1000L /*60 minutes*/);
            }


            if (!pm.isScreenOn()) {
                wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "QosData");
                if ((wl != null) && (!wl.isHeld())) {
                    wl.acquire();//60 * 60 * 1000L /*60 minutes*/);
                }
            }
            holdWifiLock();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //</editor-fold>

        mshared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = mshared.edit();

        try {

            qosNotification = intent.getExtras().getParcelable("qosNotification");

        } catch (Exception e) {
            e.printStackTrace();
            qosNotification = new QosNotification();
        }

        freq = qosNotification.getFreq();

        scheduleId = qosNotification.getScheduleId();

        testTriggerType = qosNotification.getTestTriggerType();

        testType = qosNotification.getTestType();

        testTriggerId = qosNotification.getTestTriggerId();

        MyApplication.TAG = String.valueOf(mshared.getInt(getApplicationContext().getString(R.string.device_id), 0)) + "_" + qosNotification.getId() + " Arsel Local Log";


        Actions.PostLog(getApplicationContext(), MyApplication.TAG, "Job Started");
        Actions.PostLog(getApplicationContext(), MyApplication.TAG, "Notification Received " + Actions.convertMilliSecondsToFormattedDate(qosNotification.getDate()));
        Actions.PostLog(getApplicationContext(), MyApplication.TAG, "Notification Parameters " + qosNotification.toString());

      try {
          Log.wtf("- 59", "is: " + Actions.convertMilliSecondsToFormattedDate(new Date(System.currentTimeMillis() - 59000L)));
          Log.wtf("time sent", "is: " + Actions.convertMilliSecondsToFormattedDate(qosNotification.getDate()));
          Log.wtf("+ 59", "is: " + Actions.convertMilliSecondsToFormattedDate(new Date(System.currentTimeMillis() + 59000L)));


          Log.d("- 59", "is: " + Actions.convertMilliSecondsToFormattedDate(new Date(System.currentTimeMillis() - 59000L)));
          Log.d("time sent", "is: " + Actions.convertMilliSecondsToFormattedDate(qosNotification.getDate()));
          Log.d("+ 59", "is: " + Actions.convertMilliSecondsToFormattedDate(new Date(System.currentTimeMillis() + 59000L)));
      }catch (Exception e){}


        try {
            new QosTestSenderTask(getApplicationContext(), qosNotification).executeOnExecutor(MyApplication.threadPoolExecutor);
        } catch (Exception e) {
            Actions.PostLog(getApplicationContext(), MyApplication.TAG, "QOS test sender exception" +e.toString());

            e.printStackTrace();
        }

        //<editor-fold desc="all cases">
        /*if (qosNotification.getDate().after(new Date(System.currentTimeMillis() - 59000L)) &&
                qosNotification.getDate().before(new Date(System.currentTimeMillis() + 59000L))) {  //Send Immediately

            Log.wtf("Send", "Immediately");
            Log.d("Send", "Immediately");
            Actions.PostLog(getApplicationContext(), MyApplication.TAG, "Post Qos Process Initiation");

            try {
                new QosTestSenderTask(getApplicationContext(), qosNotification).executeOnExecutor(MyApplication.threadPoolExecutor);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (qosNotification.getDate().before(new Date(System.currentTimeMillis() + 59000L))) { //filteration queue2

            Log.wtf("Not Sent", "Immediately");

            //<editor-fold desc="group of notifications">
            TCTDbAdapter database = new TCTDbAdapter(getApplicationContext());
            database.open();

            ArrayList<QosTest> inRange = database.getAllInRangeQosTests(qosNotification.getDate());
            Log.wtf("inRange", "size " + inRange.size());
            if (inRange.size() > 0) {

                QosTest near = new QosTest();
                try {

                    near = getNearestTest(inRange, qosNotification.getDate());

                    near.setNotificationId(qosNotification.getId());
                    near.setSignalStrengthFlag(testType.equals("104"));

                    try {
                        near.setTestType(testType);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        near.setAdHocRequestId(testTriggerId);//testTriggerType.equals("104") ? String.valueOf(scheduleId) : testTriggerId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        near.setAdHocRequestId("-1");
                    }

                    try {
                        near.setTestTriggerType(testTriggerType);
                    } catch (Exception e) {
                        e.printStackTrace();
                        near.setTestTriggerType("");
                    }

                    postTest(near);
                    database.deleteQosTest(near.getId());
                } catch (Exception e) {
                    e.printStackTrace();

                    inRange.get(0).setNotificationId(qosNotification.getId());
                    inRange.get(0).setSignalStrengthFlag(testType.equals("104"));
                    inRange.get(0).setTestType(testType);
                    inRange.get(0).setAdHocRequestId(testTriggerId);
                    inRange.get(0).setTestTriggerType(testTriggerType);
                    postTest(inRange.get(0));
                    database.deleteQosTest(inRange.get(0).getId());
                }
            }
            //</editor-fold>
        }*/
        //</editor-fold>

        return START_STICKY;
    }


    private QosTest getNearestTest(ArrayList<QosTest> inRange, Date currentDate) {
        long minDiff = -1, currentTime = currentDate.getTime();
        QosTest nearestTest = new QosTest();
        for (QosTest test : inRange) {
            long diff = Math.abs(currentTime - test.getDate().getTime());
            if ((minDiff == -1) || (diff < minDiff)) {
                minDiff = diff;
                nearestTest = test;
            }
        }
        return nearestTest;
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

              // try{ psListener.getSignalLevel()}catch (Exception e)


                Actions.PostLog(getApplicationContext(), MyApplication.TAG, "phone state 1 listener start");

                try {
                    telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                   try {
                        if(psListener==null) {
                            Log.wtf("psListener1","null1");
                            telephonyManager.listen(new PhoneStateListener(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                        }
                        else {
                            Log.wtf("psListener1","OK");
                            telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                        }
                    }catch (Exception e){

                    }

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
            Actions.PostLog(getApplicationContext(), MyApplication.TAG, "phone state 1 listener strength change");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                signalSupport = signalStrength.getLevel();
            } else {

                signalSupport = getSignalLevel(signalStrength);
            }
            Log.wtf("SIGNAL SUPPORT1 ", "IS " + signalSupport);

            try {
                //<editor-fold desc="signal spectrum">
                if (NetworkUtil.getNetworkClass(getApplicationContext()).equals("4G")) {

                    int signalStrengthrsrp = getSignalStrengthByName(signalStrength, "getLteRsrp");
                    int signalStrengthrsrq = getSignalStrengthByName(signalStrength, "getLteRsrq");
                    Log.wtf("signalStrengthrsrp ", "sss " + signalStrengthrsrp);
                    Log.wtf("signalStrengthrsrq ", "sss " + signalStrengthrsrq);

                    signalQuality = String.valueOf(signalStrengthrsrq);
                    int signalStrengthDbm = getSignalStrengthByName(signalStrength, "getDbm");
                    spectrumSignalStrength = signalStrengthDbm == -1 ? "" : String.valueOf(signalStrengthDbm);

                } else if (NetworkUtil.getNetworkClass(getApplicationContext()).equals("3G")) {

                    int getWcdmaRscp = getSignalStrengthByName(signalStrength, "getWcdmaRscp");
                    int getWCdmaEcio = getSignalStrengthByName(signalStrength, "getWcdmaEcio");

                    Log.wtf("getWcdmaRscp ", "sss " + getWcdmaRscp);
                    Log.wtf("getWCdmaEcio ", "sss " + getWCdmaEcio);

                    signalQuality = String.valueOf(getWCdmaEcio);
                    //<editor-fold desc="signal quality">
                    int signalStrengthDbm = getSignalStrengthByName(signalStrength, "getDbm");
                    Log.wtf(getClass().getCanonicalName(), "signalStrengthDbm : " + signalStrengthDbm);
                    spectrumSignalStrength = signalStrengthDbm == -1 ? "" : String.valueOf(signalStrengthDbm);
                    //</editor-fold>

                } else if (NetworkUtil.getNetworkClass(getApplicationContext()).equals("2G")) {

                    int asu = getSignalStrengthByName(signalStrength, "getAsuLevel");

                    spectrumSignalStrength = String.valueOf(asu);
                    //<editor-fold desc="signal quality">
                    signalQuality = "";
                    //</editor-fold>
                }
                //</editor-fold>
            }catch (Exception e){
                e.printStackTrace();
            }

            //<editor-fold desc="Signal Strength in different scales">

            //signalSupport = signalStrength.getLevel();// bars
           /* if (signalSupport >= 30) {
                Log.wtf(getClass().getCanonicalName(), "Signal GSM : Good");


            } else if (signalSupport >= 20 && signalSupport < 30) {
                Log.wtf(getClass().getCanonicalName(), "Signal GSM : Avarage");


            } else if (signalSupport < 20 && signalSupport > 3) {
                Log.wtf(getClass().getCanonicalName(), "Signal GSM : Weak");


            } else if (signalSupport <= 3) {
                Log.wtf(getClass().getCanonicalName(), "Signal GSM : Very weak");
            }

            int signalStrengthAsuLevel = getSignalStrengthByName(signalStrength, "getAsuLevel");
            Log.wtf(getClass().getCanonicalName(), "signalStrengthAsuLevel : " +signalStrengthAsuLevel);*/
            //</editor-fold>

            //<editor-fold desc="signal quality">
            /*int signalStrengthDbm = getSignalStrengthByName(signalStrength, "getDbm");
            Log.wtf(getClass().getCanonicalName(), "signalStrengthDbm : " +signalStrengthDbm);
            signalQuality = signalStrengthDbm == -1 ? "" : String.valueOf(signalStrengthDbm);

            if (signalStrengthDbm  == -1){

                signalQuality = "";
            } else if (signalStrengthDbm > - 85){ //Good

                signalQuality = "Good";
            }else if (signalStrengthDbm > -95 && signalStrengthDbm < -85){ //Fair

                signalQuality = "Fair";
            }else{ //Poor

                signalQuality = "Poor";
            }*/
            //</editor-fold>

            Actions.PostLog(getApplicationContext(), MyApplication.TAG, "Signal Section");

            if (sendTest) {
                sendTest = false;
                sendQosTest();
            }
        }

        private int getSignalStrengthByName(SignalStrength signalStrength, String methodName) {
            try {
                Class classFromName = Class.forName(SignalStrength.class.getName());
                java.lang.reflect.Method method = classFromName.getDeclaredMethod(methodName);
                Object object = method.invoke(signalStrength);
                return (int) object;
            } catch (Exception ex) {
                return -1;
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

        locationManager = (LocationManager) this .getSystemService(LOCATION_SERVICE);

        provider = locationManager.getBestProvider(criteria, true);

        // Cant get a hold of provider
        if (provider == null) {
            return;
        }

        locationListener = new MyLocationListener(signalSupport);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        // connect to the GPS location service

        Location oldLocation = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);

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

            Actions.PostLog(getApplicationContext(), MyApplication.TAG, "Location Section qosfcmtest");

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            /*try {
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

                *//*Log.wtf("countryName", "countryName is: " + countryName);
                Log.d("countryName", "countryName is: " + countryName);

                Log.wtf("locality", "locality is: " + locality);
                Log.d("locality", "locality is: " + locality);

                Log.wtf("subLocality", "subLocality is: " + subLocality);
                Log.d("subLocality", "subLocality is: " + subLocality);

                Log.wtf("province", "province is: " + province);
                Log.d("province", "province is: " + province);

                Log.wtf("route", "route is: " + route);
                Log.d("route", "route is: " + route);*//*


            } catch (IOException e) {
                e.printStackTrace();
                countryName = "";
                locality = "";
                subLocality = "";
                province = "";
                route = "";

                Log.wtf("EX countryName", "countryName is: " + countryName);
                Log.d("EX countryName", "countryName is: " + countryName);
            }*/


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

                Actions.PostLog(getApplicationContext(), MyApplication.TAG, "Sim State1: Absent");
                sendQosTest();

            } else { //listen to signal strength
                Actions.PostLog(getApplicationContext(), MyApplication.TAG, "Sim State1: Present");
                sendTest = true;
                telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            }
        }

        public void onStatusChanged(String s, int i, Bundle bundle) {
            // Log.v(MyApplication.TAG, "Status changed: " + s);
        }

        public void onProviderEnabled(String s) {
            // Log.e(MyApplication.TAG, "PROVIDER DISABLED: " + s);
        }

        public void onProviderDisabled(String s) {
            // Log.e(MyApplication.TAG, "PROVIDER DISABLED: " + s);
        }
    }


    //QosTest qosTest;
    private void sendQosTest() {

        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        TCTDbAdapter sour = new TCTDbAdapter(getApplicationContext());
        sour.open();
        ArrayList<Profile> arr = sour.getAllProfiles();
        Profile profile = arr.get(0);
        sour.close();

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getSimOperatorName();
        Log.wtf("@@@@@@ carrierName1 ", "is " + carrierName);
        Log.d("@@@@@@ carrierName1 ", "is " + carrierName);

        QosTest qosTest = new QosTest();

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

        //<editor-fold desc="geocoder">
        /*try {
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
        }*/
        //</editor-fold>

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

        try {
            qosTest.setSignalQuality(signalQuality);
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setSignalQuality("");
        }

        try {

            qosTest.setSpectrumSignalStrength(spectrumSignalStrength);
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setSpectrumSignalStrength("");
        }

        try {
            qosTest.setTestSource(MyApplication.TYPE_WIFI);
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setSignalQuality("");
        }
        //</editor-fold>


        Log.wtf("Ended!!!! ", "Execution");
        Log.d("Ended!!!! ", "Execution");
        Actions.PostLog(getApplicationContext(), MyApplication.TAG, qosTest.toString());
        Actions.PostLog(getApplicationContext(), MyApplication.TAG, "Execution End Insert in Sqlite and Post");

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


    }


    private void setGeoLocation(Context context, QosTest qosTest) {

        /*if (Looper.myLooper() == null)  {
            Looper.prepare();
        }*/
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


    private void postTest(QosTest qosTest) {

        PostQosTest postQosTest = new PostQosTest(getApplicationContext(), qosTest);
        postQosTest.executeOnExecutor(MyApplication.threadPoolExecutor);
    }


    private class PostQosTest extends AsyncTask<Void, Void, String> {

        String res;
        Profile profile;
        Context context;
        QosTest qosTest;
        String params;

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

            setGeoLocation(context, qosTest);
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

                    params = "MobileNumber="
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

                    url = url + params;

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

            Log.wtf("Parameters", params);

            Actions.PostLog(context,MyApplication.TAG, "Parameters "+params);
            Actions.PostLog(context,MyApplication.TAG, "Post Execute Task "+result);
            Actions.PostLog(context,"======================================", "======================================");


            if (result.equalsIgnoreCase("Success: true")) {
                Log.wtf("*=*= Success", " sent");
                Actions.PostLog(context,MyApplication.TAG, "QOSFCM post success");
                Log.d("*=*= Success", " sent");
            } else {
                Actions.PostLog(context,MyApplication.TAG, "QOSFCM post failed");
                Log.wtf("*=*= Failure", " not sent");
                Log.d("*=*= Failure", " not sent");
                Actions.UpdateNotificationStatus(qosTest.getNotificationId());
            }

            Intent bc = new Intent("com.ids.ict.classes.InternetReceiver");
            sendBroadcast(bc);

        }
    }


    private static class SingleShotLocationProvider {

        private static interface LocationCallback {
            public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location);
        }

        // calls back to calling thread, note this is for low grain: if you want higher precision, swap the
        // contents of the else and if. Also be sure to check gps permission/settings are allowed.
        // call usually takes <10ms

        private static void requestSingleUpdate(final Context context, final SingleShotLocationProvider.LocationCallback callback) {

            final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isNetworkEnabled) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        callback.onNewLocationAvailable(new SingleShotLocationProvider.GPSCoordinates(location.getLatitude(), location.getLongitude()));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                }, null);

            } else {
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (isGPSEnabled) {
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    locationManager.requestSingleUpdate(criteria, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            callback.onNewLocationAvailable(new SingleShotLocationProvider.GPSCoordinates(location.getLatitude(), location.getLongitude()));
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                        }
                    }, null);
                }
            }
        }


        // consider returning Location instead of this dummy wrapper class
        public static class GPSCoordinates {
            public float longitude = -1;
            public float latitude = -1;

            public GPSCoordinates(float theLatitude, float theLongitude) {
                longitude = theLongitude;
                latitude = theLatitude;
            }

            public GPSCoordinates(double theLatitude, double theLongitude) {
                longitude = (float) theLongitude;
                latitude = (float) theLatitude;
            }
        }
    }
}
