package com.ids.ict.services;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.Profile;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.NetworkUtil;
import com.ids.ict.classes.QosTest;
import com.ids.ict.classes.SharedPreference;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by DEV on 3/2/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class OfflineJobService extends JobService {

    final int JOB_ID = 2;
    JobParameters params;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    double longitude = 0.0, latitude = 0.0;
    String provider;
    boolean waitingForLocationUpdate = true, sendTest = false;
    TelephonyManager telephonyManager;
     myPhoneStateListener psListener = null;
    private int signalSupport = 0;
    String enabled = "true", countryName = "", locality = "", subLocality = "", route = "", province = "",  testDate = "", signalQuality = "", spectrumSignalStrength = "";
    int freq, scheduleId;
    String startingTime, endingTime;
    String testType, testTriggerType;
    private static final int INVALID = Integer.MAX_VALUE;
    SharedPreferences mShared;
    SharedPreferences.Editor edit;

    @Override
    public void onCreate() {
        super.onCreate();

        mShared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = mShared.edit();
        psListener = new myPhoneStateListener();
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public boolean onStartJob(JobParameters params) {

        this.params = params;

        if (Build.VERSION.SDK_INT >= 24) { //Nougat and oreo
            scheduleRefresh();
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Log.wtf("GPS ", "OFF");
            Log.d("GPS ", "OFF");

            latitude = 0;
            longitude = 0;

            if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {


                queueQosTest();

            } else { //listen to signal strength



                sendTest = true;
                telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            }

        } else {

            Log.wtf("GPS ", "ON");
            Log.d("GPS ", "ON");

            registerLocationUpdates(signalSupport);
        }


        return false;
    }

    private void scheduleRefresh() {

        Log.d("offline job service", "scheduleRefresh");
        Log.wtf("offline job service", "scheduleRefresh");
        JobScheduler mJobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo.Builder mJobBuilder = new JobInfo.Builder(JOB_ID, new ComponentName(getPackageName(), OfflineJobService.class.getName()));

        JobInfo jobInfo = mJobBuilder
                .setRequiresDeviceIdle(false)
                .setMinimumLatency(60 * 1000)
                .build();

        mJobScheduler.schedule(jobInfo);
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }

    public class myPhoneStateListener extends PhoneStateListener {

        public myPhoneStateListener() {

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
            Log.d("in signal", "level");
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
            }else{

                signalSupport = getSignalLevel(signalStrength);
            }

            Log.wtf("SIGNAL SUPPPORT IS ", "sss " + signalSupport);
            Log.d("SIGNAL SUPPPORT IS ", "sss " + signalSupport);

            //<editor-fold desc="signal spectrum">
            if (NetworkUtil.getNetworkClass(getApplicationContext()).equals("4G")){

                int signalStrengthrsrp = getSignalStrengthByName(signalStrength, "getLteRsrp");
                int signalStrengthrsrq = getSignalStrengthByName(signalStrength, "getLteRsrq");
                Log.wtf("signalStrengthrsrp ", "sss " + signalStrengthrsrp);
                Log.wtf("signalStrengthrsrq ", "sss " + signalStrengthrsrq);

                signalQuality = String.valueOf(signalStrengthrsrq);
                int signalStrengthDbm = getSignalStrengthByName(signalStrength, "getDbm");
                spectrumSignalStrength = signalStrengthDbm == -1 ? "" : String.valueOf(signalStrengthDbm);

            } else if ( NetworkUtil.getNetworkClass(getApplicationContext()).equals("3G")){

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


            }else if ( NetworkUtil.getNetworkClass(getApplicationContext()).equals("2G")){

                int asu = getSignalStrengthByName(signalStrength, "getAsuLevel");

                spectrumSignalStrength = String.valueOf(asu);
                //<editor-fold desc="signal quality">
                int signalStrengthDbm = getSignalStrengthByName(signalStrength, "getDbm");
                Log.wtf(getClass().getCanonicalName(), "signalStrengthDbm : " +signalStrengthDbm);
                signalQuality =  "";
                //</editor-fold>
            }
            //</editor-fold>

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

            if (sendTest) {
                sendTest = false;
                queueQosTest();
            }
        }

        private int getSignalStrengthByName(SignalStrength signalStrength, String methodName)  {
            try {
                Class classFromName = Class.forName(SignalStrength.class.getName());
                java.lang.reflect.Method method = classFromName.getDeclaredMethod(methodName);
                Object object = method.invoke(signalStrength);
                return (int)object;
            }  catch (Exception ex) {
                return -1;
            }
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void registerLocationUpdates(int signalSupport) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);

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
                Log.d("route", "route is: " + route);

                Log.wtf("countryName", "countryName is: " + countryName);*//*

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
*/
            countryName = "";
            locality = "";
            subLocality = "";
            province = "";
            route = "";
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

                queueQosTest();

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

    private void queueQosTest(){

        TCTDbAdapter sour = new TCTDbAdapter(getApplicationContext());
        sour.open();
        ArrayList<Profile> arr = sour.getAllProfiles();
        Profile profile = arr.get(0);
        sour.close();

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getSimOperatorName();

        QosTest qosTest = new QosTest();

        qosTest.setId(0);

        qosTest.setSignalStrengthFlag(true);

        try {
            qosTest.setMobileNumber(profile.getnum());
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setMobileNumber("");
        }

        try {
            qosTest.setDeviceId(String.valueOf(mShared.getInt(getString(R.string.device_id), 0)));
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
        qosTest.setLocationIPAddress("");
        qosTest.setLocality("");
        qosTest.setSubLocality("");
        qosTest.setProvince("");
        qosTest.setRoute("");

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

        qosTest.setTestType("104");

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
            qosTest.setTestTriggerType("108");
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
            qosTest.setAdHocRequestId("85");
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setAdHocRequestId("-1");
        }

        try {
            qosTest.setSpectrumSignalStrength(spectrumSignalStrength);
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setSignalQuality("");
        }

        try {
            qosTest.setSignalQuality(signalQuality);
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setSignalQuality("");
        }

        try {
            qosTest.setTestSource(MyApplication.TYPE_NOT_CONNECTED);
        } catch (Exception e) {
            e.printStackTrace();
            qosTest.setSignalQuality("");
        }


        Log.wtf("END QOS ", "QUEUED OBJECT");
        Log.d("END QOS ", "QUEUED OBJECT");

        TCTDbAdapter database = new TCTDbAdapter(getApplicationContext());
        database.open();
        database.insertQosTest(qosTest, true);

       /* SharedPreference settings = new SharedPreference();
        ArrayList<QosTest> array = settings.getNoInternetQosTests(getApplicationContext());
        if (array == null) {
            Log.wtf("null", "array");
            Log.d("null", "array");
            array = new ArrayList<QosTest>();
            array.add(qosTest);
            settings.saveNoInternetQosTests(getApplicationContext(), array);
        } else
            settings.addNoInternetQosTest(getApplicationContext(), qosTest);*/

        jobFinished(params, false);
    }
}
