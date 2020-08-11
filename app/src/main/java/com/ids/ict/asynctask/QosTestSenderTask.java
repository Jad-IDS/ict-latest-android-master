package com.ids.ict.asynctask;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.NetworkUtil;
import com.ids.ict.classes.Profile;
import com.ids.ict.classes.QosNotification;
import com.ids.ict.classes.QosTest;
import com.ids.ict.classes.SharedPreference;
import com.ids.ict.services.QosFcmServiceTest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.POWER_SERVICE;

public class QosTestSenderTask extends AsyncTask<Void, Void, Integer> {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    SharedPreferences mshared;
    SharedPreferences.Editor edit;

    private Context context;
    private double longitude = 0.0, latitude = 0.0;
    String provider;
    private boolean waitingForLocationUpdate = true, sendTest = false;
    private int signalSupport = 0;
    private String countryName = "", locality = "", subLocality = "", route = "", province = "", testDate = "", spectrumSignalStrength = "", signalQuality = "";
    private int freq, scheduleId;
    private String testType, testTriggerType, testTriggerId;
    private TelephonyManager telephonyManager;
    public MyPhoneStateListener psListener = null;
    PowerManager pm;
    private PowerManager.WakeLock wl;
    private WifiManager.WifiLock mWifiLock = null;
    private QosNotification qosNotification;
    private CountDownTimer locationTimer;
    private boolean isWaitingLocation = true;
    private boolean isWaitingPhoneState = true;
    private boolean qosSent = false;
    private Location oldLocation;

    public QosTestSenderTask(Context context, QosNotification qosNotification) {
        qosSent = false;
        this.qosNotification = qosNotification;

        try {
            Log.wtf("qosNotification", "getDate = " + qosNotification.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Log.wtf("qosNotification", "getDateCreated = " + qosNotification.getDateCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Log.wtf("qosNotification", "getDateFormatted = " + qosNotification.getDateFormatted());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.context = context;

        try {
            Actions.PostLog(context, MyApplication.TAG, "qos task start" + qosNotification.getDate());
        } catch (Exception e) {
        }


        try{
            telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        }catch (Exception e){}


/*        try {
            telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            psListener = new MyPhoneStateListener();

        } catch (Exception e) {
            Log.wtf("pslistener exception2",e.toString());
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        qosSent=false;
        freq = qosNotification.getFreq();

        scheduleId = qosNotification.getScheduleId();

        testTriggerType = qosNotification.getTestTriggerType();

        testType = qosNotification.getTestType();

        testTriggerId = qosNotification.getTestTriggerId();

        mshared = PreferenceManager.getDefaultSharedPreferences(context);
        edit = mshared.edit();

    }

    @Override
    protected Integer doInBackground(Void... voids) {

        //<editor-fold desc = "Fcm way">
        try {

            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                Log.wtf("GPS2 ", "OFF");
                Log.d("GPS2 ", "OFF");
                Actions.PostLog(context, MyApplication.TAG, "GPS OFF");
                latitude = 0;
                longitude = 0;

                if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {

                    Actions.PostLog(context, MyApplication.TAG, "Sim State2: Absent");
                    sendQosTest();

                } else { //listen to signal strength

                    Log.wtf("there's", "sim");
                    Log.d("there's", "sim");

                    Actions.PostLog(context, MyApplication.TAG, "Sim State2: Present");

                    sendTest = true;
                    telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    psListener = new MyPhoneStateListener();
                    telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                    waitForPhoneState(4);
                }

            } else {

                Log.wtf("GPS2 ", "ON");
                Log.d("GPS2 ", "ON");

                Actions.PostLog(context, MyApplication.TAG, "GPS ON");


                try {
                    registerLocationUpdates(signalSupport);
                } catch (Exception e) {
                    e.printStackTrace();
                    Actions.PostLog(context, MyApplication.TAG, "GPS ON Exception");
                    Log.wtf("Exception registerLocationUpdates", e.getMessage());
                }
            }


            Log.wtf("after  ", "listen");


        } catch (Exception e) {
            e.printStackTrace();
            Log.wtf("Exception e", e.getMessage());
            Actions.PostLog(context, MyApplication.TAG, "Job background exception" + e.toString());
        }
        //</editor-fold>


        SharedPreference settings = new SharedPreference();
        int size = 0;
        try {
            size = settings.getNoInternetQosTests(context).size();
        } catch (Exception e) {
            e.printStackTrace();
            size = 0;
        }

        if (size > 0) {
            settings.removeAllNoInternetQosTests(context);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }


    public class MyPhoneStateListener extends PhoneStateListener {

        @SuppressLint("InvalidWakeLockTag")
        public MyPhoneStateListener() {

            sendTest = false;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                // only for nougat and newer versions
                telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    if (psListener == null) {
                        Log.wtf("psListener", "null2");

                        try {
                            int PROXIMITY_WAKE_LOCK = 32;
                            pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                            wl = pm.newWakeLock(PROXIMITY_WAKE_LOCK, "mywakelocktag");
                            // telephonyManager.listen(new PhoneStateListener(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                        } catch (Exception e) {
                        }
                    } else {
                        Log.wtf("psListener", "OK");
                        //   telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                    }


                   /* if(psListener==null)
                    telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);*/
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

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            Log.wtf("signalStrengthChanged", "true");
            Actions.PostLog(context, MyApplication.TAG, "signalStrengthChanged : true");
            isWaitingPhoneState = false;
            getSignalStrengthsValues(signalStrength);


        }


        private void getSignalStrengthsValues(SignalStrength signalStrength) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    signalSupport = signalStrength.getLevel();
                } else {

                    signalSupport = getSignalLevel(signalStrength);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Actions.setSignalSupport(context, signalSupport + "");
            Log.wtf("SIGNAL SUPPORT ", "IS " + signalSupport);


            try {

                switch (NetworkUtil.getNetworkClass(context.getApplicationContext())) {
                    case "4G": {

                        int signalStrengthrsrp = getSignalStrengthByName(signalStrength, "getLteRsrp");
                        int signalStrengthrsrq = getSignalStrengthByName(signalStrength, "getLteRsrq");

                        signalQuality = String.valueOf(signalStrengthrsrq);
                        int signalStrengthDbm = getSignalStrengthByName(signalStrength, "getDbm");
                        spectrumSignalStrength = signalStrengthDbm == -1 ? "" : String.valueOf(signalStrengthDbm);

                        break;
                    }
                    case "3G": {

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

                        break;
                    }
                    case "2G":

                        int asu = getSignalStrengthByName(signalStrength, "getAsuLevel");

                        spectrumSignalStrength = String.valueOf(asu);
                        //<editor-fold desc="signal quality">
                        signalQuality = "";
                        //</editor-fold>
                        break;
                }

                Actions.setSignalQuality(context, signalQuality);
                Actions.setSpectrumSignalStrength(context, spectrumSignalStrength);
                //</editor-fold>
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                Actions.PostLog(context, MyApplication.TAG, "Signal Section");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                // if (sendTest) {
                //  sendTest = false;
                sendQosTest();
                //  }
            } catch (Exception e) {
                e.printStackTrace();
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


    private void setGeoLocation(double latitude, double longitude) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
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
    }


    private class MyLocationListener implements LocationListener {

        private int signalSupport;

        public MyLocationListener(int signalSupport) {
            Log.wtf("location_listener", "start");
            sendTest = false;
            isWaitingLocation = true;
            this.signalSupport = signalSupport;
        }

        public void onLocationChanged(Location location) {
            Log.wtf("location_listener", "changed");
            Actions.PostLog(context, MyApplication.TAG, "Location Section Task");
            isWaitingLocation = false;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Actions.setLastLatitude(context, latitude + "");
            Actions.setLastLongitude(context, longitude + "");

            setGeoLocation(latitude, longitude);

            if (waitingForLocationUpdate) {
                // getNearbyStores();
                waitingForLocationUpdate = false;
            }

            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d("no permission", "no permission");
                return;
            }
            locationManager.removeUpdates(this);

            sendTest = false;

            if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {

                Log.wtf("location_listener", "Sim State: Absent");
                Actions.PostLog(context, MyApplication.TAG, "Sim State: Absent");
                sendQosTest();

            } else { //listen to signal strength
                Log.wtf("location_listener", "Sim State: Present");
                Actions.PostLog(context, MyApplication.TAG, "Sim State: Present");
                sendTest = true;
                telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                psListener = new MyPhoneStateListener();
                telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                waitForPhoneState(4);
            }
        }

        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.v(MyApplication.TAG, "Status changed: " + s);
        }

        public void onProviderEnabled(String s) {
            Log.e(MyApplication.TAG, "PROVIDER DISABLED: " + s);
        }

        public void onProviderDisabled(String s) {
            Log.e(MyApplication.TAG, "PROVIDER DISABLED: " + s);
        }
    }


    private void registerLocationUpdates(int signalSupport) {
        Actions.PostLog(context, MyApplication.TAG, "register location1");
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        provider = locationManager.getBestProvider(criteria, true);

        // Cant get a hold of provider
        if (provider == null) {
            return;
        }
        isWaitingLocation = true;
        locationListener = new MyLocationListener(signalSupport);
        Actions.PostLog(context, MyApplication.TAG, "register location2");

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.wtf("checking permission", "checked");
            Actions.PostLog(context, MyApplication.TAG, "checking permission");
            return;
        }
        // connect to the GPS location service


   /*     if (Looper.myLooper() == null)  {
            Looper.prepare();
        }

        Log.wtf("old location","checking old location");

        Location oldLocation = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Looper.loop();

        if (oldLocation != null) {
            Log.wtf("old location","old location waiting false");
            waitingForLocationUpdate = false;
        }*/


        try {
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            oldLocation = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
           // Looper.loop();
        } catch (Exception e) {
            Actions.PostLog(context, MyApplication.TAG, "register location1 exception"+e.toString());
            Log.wtf("old location exception", e.toString());
        }


        waitForLocation(4);


    }


    private void waitForLocation(int TimeInnSecond) {
        Log.wtf("location waiting", "start");
        Actions.PostLog(context, MyApplication.TAG, "location waiting start");
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Run your task here
                if (isWaitingLocation) {
                    try {
                        Actions.PostLog(context, MyApplication.TAG, "location waiting stopped");
                        Log.wtf("stop listening", "stopped send old data");
                        locationManager.removeUpdates(locationListener);
                    } catch (Exception e) {
                    }
                    setLastLocationValues();

                }
            }
        }, 6000);


    }


    private void waitForPhoneState(int TimeInnSecond) {
        Log.wtf("phone state waiting", "start");
        Actions.PostLog(context, MyApplication.TAG, "phone state waiting start");
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Run your task here
                if (isWaitingPhoneState) {
                    try {
                        Actions.PostLog(context, MyApplication.TAG, "phone state waiting stopped");
                        Log.wtf("stop listening", "stopped phone state waiting");
                        telephonyManager.listen(psListener, PhoneStateListener.LISTEN_NONE);

                    } catch (Exception e) {
                    }
                    setLastPhoneStateValues();

                }
            }
        }, 4000);


    }


    private void setLastPhoneStateValues() {
        signalSupport = Integer.parseInt(Actions.getSignalSupport(context));
        signalQuality = Actions.getSignalQuality(context);
        spectrumSignalStrength = Actions.getSpectrumSignalStrength(context);


        try {
            Log.wtf("last_phone_state", "send last");
            Actions.PostLog(context, MyApplication.TAG, "Signal Section from last phone state");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //if (sendTest) {
              //  sendTest = false;
                sendQosTest();
           // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLastLocationValues() {

      try{
          latitude = oldLocation.getLatitude();
          longitude = oldLocation.getLongitude();
      }catch (Exception e){



        try {
            latitude = Double.parseDouble(Actions.getLastLatitude(context));
            longitude = Double.parseDouble(Actions.getLastLongitude(context));
        } catch (Exception e1) {
            latitude = 1;
            longitude = 1;
        }
      }
        sendTest = false;

        Log.wtf("last location values", "send");
        Actions.PostLog(context, MyApplication.TAG, "last location values send");


        if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {


            Actions.PostLog(context, MyApplication.TAG, "Sim State: Absent");
            sendQosTest();

        } else { //listen to signal strength

            Actions.PostLog(context, MyApplication.TAG, "Sim State: Present");
            sendTest = true;
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            psListener = new MyPhoneStateListener();
            telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            waitForPhoneState(4);
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

            final LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
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


    @SuppressLint("InvalidWakeLockTag")
    private void sendQosTest() {

if(!qosSent) {
    qosSent = true;
    TCTDbAdapter sour = new TCTDbAdapter(context);
    sour.open();
    ArrayList<Profile> arr = sour.getAllProfiles();
    Profile profile = new Profile();

    if (arr.size() > 0) {
        profile = arr.get(0);
    }
    sour.close();

    String carrierName = "";
    try {
        carrierName = telephonyManager.getSimOperatorName();
    } catch (Exception e) {
        e.printStackTrace();
        carrierName = "";
    }

    Log.wtf("@@@@@@ carrierName ", "is " + carrierName);
    Log.d("@@@@@@ carrierName ", "is " + carrierName);

    QosTest qosTest = new QosTest();

    //<editor-fold desc = "Setting data">
    qosTest.setId(0);

    try {
        qosTest.setSignalStrengthFlag(testType.equals("104"));
    } catch (Exception e) {
        e.printStackTrace();
        qosTest.setSignalStrengthFlag(true);
    }

    try {
        qosTest.setNotificationId(qosNotification.getId());
    } catch (Exception e) {
        e.printStackTrace();
        qosTest.setMobileNumber("");
    }

    try {
        qosTest.setMobileNumber(profile.getnum());
    } catch (Exception e) {
        e.printStackTrace();
        qosTest.setMobileNumber("");
    }

    try {
        qosTest.setDeviceId(String.valueOf(mshared.getInt(context.getString(R.string.device_id), 0)));
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

    try {
        qosTest.setTestType(testType);
    } catch (Exception e) {
        e.printStackTrace();
        qosTest.setTestType("");
    }

    qosTest.setCallDisconnectionReason("");

    try {
        testDate = new SimpleDateFormat("HH:mm:ss M-dd-yyyy", Locale.ENGLISH).format(qosNotification.getDate());
    } catch (Exception e) {
        e.printStackTrace();
        testDate = new SimpleDateFormat("HH:mm:ss M-dd-yyyy", Locale.ENGLISH).format(new Date());
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
        qosTest.setConnectionType(NetworkUtil.getNetworkClass(context));
    } catch (Exception e) {
        e.printStackTrace();
        qosTest.setConnectionType(NetworkUtil.getNetworkClass(context));
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
    //</editor-fold>


    Log.wtf("Ended!!!! ", "Execution");
    Log.d("Ended!!!! ", "Execution");

    Actions.PostLog(context, MyApplication.TAG, qosTest.toString());

    Actions.PostLog(context, MyApplication.TAG, "Execution End Insert in Sqlite and Post task");


    Log.wtf("INSERT", "AND POST");
    Log.d("INSERT", "AND POST");

    //Looper.myLooper().quit();
    TCTDbAdapter database = new TCTDbAdapter(context);
    database.open();
    database.insertAndPost(context, qosTest);


    int PROXIMITY_WAKE_LOCK = 32;
    pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    wl = pm.newWakeLock(PROXIMITY_WAKE_LOCK, "mywakelocktag");


    try {
        if (wl.isHeld())
            wl.release();
    } catch (Exception e) {
        e.printStackTrace();
    }
    try {
        releaseWifiLock();
    } catch (Exception e) {
        e.printStackTrace();
    }
}else {
    Log.wtf("isSentBefore","true");
    Actions.PostLog(context, MyApplication.TAG, "is sent before true");
    try {
        Actions.PostLog(context, MyApplication.TAG, "location waiting stopped");
        Log.wtf("stop listening", "stopped send old data");
        locationManager.removeUpdates(locationListener);
    } catch (Exception e) {
    }

    try {
        Actions.PostLog(context, MyApplication.TAG, "phone state waiting stopped");
        Log.wtf("stop listening", "stopped phone state waiting");
        telephonyManager.listen(psListener, PhoneStateListener.LISTEN_NONE);

    } catch (Exception e) {
    }

}
    }

    private void releaseWifiLock() {

        if (mWifiLock == null)
            Log.w("WifiLock", "#releaseWifiLock mWifiLock was not created previously");

        if (mWifiLock != null && mWifiLock.isHeld()) {
            mWifiLock.release();
            //mWifiLock = null;
        }
    }
}
