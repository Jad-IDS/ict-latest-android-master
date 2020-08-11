package com.ids.ict.classes;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ids.ict.Actions;
import com.ids.ict.R;
import com.ids.ict.activities.SplashActivity;

import static com.ids.ict.classes.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.ids.ict.classes.CommonUtilities.EXTRA_MESSAGE;
import static com.ids.ict.classes.CommonUtilities.SENDER_ID;

public class PushNotificationActions {

    //check if device is registered on server
    public static boolean isRegistered(Activity activity) {
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(activity);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        //  GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(activity);
        if (regId.equals("")) {
            //device not registerd
            return false;
        } else {
            // Device is already registered on GCM, check server.
            if (GCMRegistrar.isRegisteredOnServer(activity)) {
                //registered on server
                return true;
            } else {//not registered on server
                return false;
            }
        }


    }


    public static void showRegistrationDialog(final SplashActivity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(Actions.errorMessage(msg, Color.BLACK));

        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        final SharedPreferences.Editor editor = mSharedPreferences.edit();

        builder.setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Log.wtf("firebaseToken", FirebaseInstanceId.getInstance().getToken());
                        editor.putBoolean(activity.getResources().getString(R.string.notification_key), true).apply();
                        editor.putBoolean(activity.getResources().getString(R.string.enable_notification), true).apply();
                        editor.putBoolean("show", false).apply();
                        //register in push notification
                        //register(activity, editor);
                        dialog.dismiss();
                        activity.loadPage();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editor.putBoolean(activity.getResources().getString(R.string.notification_key), false).apply();
                        editor.putBoolean(activity.getResources().getString(R.string.enable_notification), false).apply();
                        editor.putBoolean("show", false).apply();
                        //do nothing
                        dialog.dismiss();
                        activity.loadPage();
                    }
                })
        ;


        AlertDialog alert = builder.create();
        alert.getContext().setTheme(R.style.CustomDialogTheme);
        // alert.setContentView(R.layout.custom_dialog);
        alert.setCancelable(false);
        alert.show();

    }

    static AsyncTask<Void, Void, Void> mRegisterTask;

    public static void register(final Activity activity, final SharedPreferences.Editor edit) {


        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(activity.getResources().getString(R.string.enable_notification), true).apply();
    }

    public static void unRegister(final Activity activity) {

        //GCMRegistrar.unregister(activity);

        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(activity.getResources().getString(R.string.enable_notification), false).apply();
    }

    private final static BroadcastReceiver mHandleMessageReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);

                }
            };
}
