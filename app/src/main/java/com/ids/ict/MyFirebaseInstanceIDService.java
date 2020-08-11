package com.ids.ict;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ids.ict.classes.Connection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by user on 11/1/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        new AddDevice().execute(token);
    }
    public class UpdateUserRegistrationTable extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            String url = "" + MyApplication.link + MyApplication.post + "UpdateRegistrationTable";

            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("password", MyApplication.pass);
            parameters.put("deviceId", params[0]);
            parameters.put("tokenNumber", Actions.create_token_new());
            String result = Connection.POST(url, parameters);
            return null;
        }
    }
    private class AddDevice extends AsyncTask<String, Void, Integer> {

        SharedPreferences mShared;
        private SharedPreferences.Editor edit;
        private String android_id;
        String currentDateandTime;
        String code="1";
        String carrierName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mShared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            edit = mShared.edit();
            android_id  = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            currentDateandTime  = sdf.format(new Date());
            PackageInfo pInfo;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                code = pInfo.versionCode + "";
            } catch (Exception e) {
                e.printStackTrace();
            }


            TelephonyManager manager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            carrierName = manager.getSimOperatorName();
        }

        @Override
        protected Integer doInBackground(String... params) {
            String result = "";
            String url = "" + MyApplication.link +  MyApplication.post + "RegisterDevice2";

            HashMap<String,String> parameters = new HashMap<>();
            parameters.put("deviceType","1");
            parameters.put("deviceToken",params[0]);
            parameters.put("deviceModel",Actions.getDeviceName());
            parameters.put("modelName",Actions.getDeviceName());
            parameters.put("osVersion",Actions.getAndroidVersion());
            parameters.put("uniqueDeviceId",android_id);
            parameters.put("registrationDate",currentDateandTime);
            parameters.put("appVersion",code);
            parameters.put("notificationFlag","1");
            parameters.put("mobileNumver","");
            parameters.put("timestamp",currentDateandTime);
            parameters.put("QoSEnabled", mShared.getBoolean(getString(R.string.enable_qos), false) ? "1" : "0");
            parameters.put("ServiceProvider",carrierName);

            Log.wtf("deviceToken", "is " + FirebaseInstanceId.getInstance().getToken());
            Log.wtf("deviceModel", "is " + Actions.getDeviceName());
            Log.wtf("osVersion", "is " + Actions.getAndroidVersion());
            Log.wtf("uniqueDeviceId", "is " + android_id);
            Log.wtf("registrationDate", "is " + currentDateandTime);
            Log.wtf("appVersion", "is " + code);
            Log.wtf("notificationFlag", "is " + "1");
            Log.wtf("mobileNumver", "is " + "");
            Log.wtf("timestamptimestamp", "is " + currentDateandTime);
            Log.wtf("QoSEnabled",   mShared.getBoolean(getString(R.string.enable_qos), false) ? "1" : "0");
            Log.wtf("ServiceProvider", "is " + carrierName);

            try {
                result = Connection.POST(url,parameters);
                String[] r1 = result.split("</");
                String[] r2 = r1[0].split(">");
                result = r2[2];
            } catch (Exception e) {
                e.printStackTrace();
                result = String.valueOf(mShared.getInt(getResources().getString(R.string.device_id), 0));
            }
            return Integer.parseInt(result);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.wtf("res", result+"");

            try {

                edit.putInt(getResources().getString(R.string.device_id), result).apply();
                // UpdateUserRegistrationTable update = new UpdateUserRegistrationTable();
                // update.execute(""+result);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
