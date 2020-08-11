/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ids.ict.classes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.ids.ict.R;
import com.ids.ict.activities.SplashActivity;

import static com.ids.ict.classes.CommonUtilities.SENDER_ID;
import static com.ids.ict.classes.CommonUtilities.displayMessage;



/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
      //  displayMessage(context, getString(R.string.gcm_registered));
      boolean registered= ServerUtilities.registerPushSharp(context, registrationId);
      SharedPreferences	 mSharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
   	final SharedPreferences.Editor editor = mSharedPreferences.edit();
      if (!registered) {
          GCMRegistrar.unregister(context);
          editor.putBoolean(context.getResources().getString(R.string.notification_key), false);
      }else{
    	  editor.putBoolean(context.getResources().getString(R.string.notification_key), true);
      }
      editor.commit();
       // ServerUtilities.register(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
       // displayMessage(context, getString(R.string.gcm_unregistered));
        if (GCMRegistrar.isRegisteredOnServer(context)) {
        	 boolean unregistered= ServerUtilities.unregisterPushSharp(context, registrationId);
        	  SharedPreferences	 mSharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        	   	final SharedPreferences.Editor editor = mSharedPreferences.edit();
        	      if (!unregistered) {
        	          GCMRegistrar.unregister(context);
        	          editor.putBoolean(context.getResources().getString(R.string.notification_key), true);
        	      }else{
        	    	  editor.putBoolean(context.getResources().getString(R.string.notification_key), false);
        	      }
        	      editor.commit();
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            Log.i(TAG, "Ignoring unregister callback");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message =intent.getStringExtra("alert");
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
      //  String message = getString(R.string.gcm_deleted, total);
        String message = "From GCM: server deleted %1$d pending messages!";
        
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
      //  displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
       // displayMessage(context, getString(R.string.gcm_recoverable_error,
       //         errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, SplashActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        notification  = builder.setContentIntent(intent)
                .setSmallIcon(icon)
                .setTicker(message).setWhen(when)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setVibrate(new long[] { 500, 500, 500, 500, 500 })
                .setContentText(message).build();

        //notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
 
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
    }

	
}