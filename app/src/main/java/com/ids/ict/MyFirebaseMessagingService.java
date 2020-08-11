package com.ids.ict;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.ids.ict.activities.SplashActivity;
import com.ids.ict.classes.MobileConfiguration;
import com.ids.ict.classes.Profile;
import com.ids.ict.classes.QosNotification;
import com.ids.ict.services.OnlineCheckJobService;
import com.ids.ict.services.QosFcmServiceTest;

import org.json.JSONObject;
import org.shipp.util.DatabaseHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 11/1/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    SharedPreferences mshared;
    SharedPreferences.Editor edit;
    Date date;
    QosNotification qosNotification;
    String provider;
    int freq, scheduleId;
    String testType, testTriggerType;
    String carrierNames = "";
    String dateCreated = "";
    int byPass = -1;
    ArrayList<MobileConfiguration> mobileConfigList;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        mshared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = mshared.edit();

        String recordId = remoteMessage.getData().get("Id");

        Log.wtf("received", "notif");
        Log.d("received", "notification");

        Actions.PostLog(getApplicationContext(),MyApplication.TAG, "FCM data recieved"+remoteMessage.getData().toString());

        try{ RetrieveOnlineStatusTask retrieveOnlineStatusTask = new RetrieveOnlineStatusTask();
            retrieveOnlineStatusTask.execute();}catch (Exception e){}



        int id = Integer.parseInt(recordId);

        String message = remoteMessage.getData().get("message");

       // if (remoteMessage.getData().get("frequency").equals("-1")) {
       // Log.wtf("frequency",remoteMessage.getData().get("frequency"));
        if (remoteMessage.getData().get("frequency").equals("null") || remoteMessage.getData().get("frequency")==null || remoteMessage.getData().get("frequency").equals("-1")) {

            Log.wtf("in send    ", "notification");

            sendNotification(id, message);
        } else {

            //<editor-fold desc="Qos">
            if (mshared.getBoolean(getString(R.string.enable_qos), false)) {

                String testTriggerId = remoteMessage.getData().get("testTriggeid");
                String frequency = remoteMessage.getData().get("frequency");
                testType = remoteMessage.getData().get("testtype");
                testTriggerType = remoteMessage.getData().get("testtriggerType");
                dateCreated = remoteMessage.getData().get("dateCreated");
                Log.wtf("qosNotification dateCreated    ", "is " + dateCreated);
                Log.d("qos dateCreated", "is " + dateCreated);

                try {
                    freq = Integer.parseInt(frequency);
                } catch (Exception e) {
                    e.printStackTrace();
                    freq = 500;
                }
                try {
                    scheduleId = testType.equals("104") ? 1 : Integer.parseInt(testTriggerId);
                } catch (Exception e) {
                    e.printStackTrace();
                    scheduleId = -1;
                }
                qosNotification = new QosNotification(id, scheduleId, freq, testTriggerType, testTriggerId, testType, dateCreated);

                mobileConfigList = db.getMobileconfigs();

                //<editor-fold desc = "precaution for oreo network change">
                /*TCTDbAdapter database = new TCTDbAdapter(getApplicationContext());
                database.open();
                int size = 0;
                try {
                    size = database.getMobileQosTestsCount();
                } catch (Exception e) {
                    e.printStackTrace();
                    size = 0;
                }
                Log.wtf("size of queue", "is " + size);
                if (size > 0) {

                    Log.wtf("will fire", "sendBroadcast");
                    Intent bc = new Intent("com.ids.ict.classes.InternetReceiver");
                    getApplicationContext().sendBroadcast(bc);
                }*/
                //</editor-fold>


             /*

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    //<editor-fold desc="online check job scheduler">
                    JobScheduler onlineCheckJobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
                    ComponentName onlineCheckComponentName = new ComponentName(getApplicationContext(), OnlineCheckJobService.class);
                    JobInfo onlineCheckJobInfo = null;
                    onlineCheckJobInfo = new JobInfo.Builder(1, onlineCheckComponentName)
                            .setRequiresCharging(false)
                            .setRequiresDeviceIdle(false)
                            .setPeriodic(MyApplication.SCHEDULER_SECONDS * 1000)
                            .build();

                    if (!isScheduledJobServiceOn(onlineCheckJobScheduler))
                        onlineCheckJobScheduler.schedule(onlineCheckJobInfo);
                    //</editor-fold>
                }


               */


                Date sent = new Date(remoteMessage.getSentTime());
                Date beforeMinutes = new Date(System.currentTimeMillis() - 900000L);
                Log.wtf("- 15", "is: " + Actions.convertMilliSecondsToFormattedDate(beforeMinutes));

                if (sent.before(beforeMinutes)) {
                    Actions.PostLog(getApplicationContext(),MyApplication.TAG, "FCM data discarded"+remoteMessage.getSentTime());

                    Log.wtf("to be", "discarded");
                    Log.d("to be", "discarded");
                } else {
                    Actions.PostLog(getApplicationContext(),MyApplication.TAG, "FCM data allowed"+remoteMessage.getSentTime());

                    Log.wtf("initiate", "qos");
                    Log.d("initiate", "qos");

                    //<editor-fold desc="old exec">
                    final Intent mIntent = new Intent(getApplicationContext(), QosFcmServiceTest.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("qosNotification", qosNotification);
                    mIntent.putExtras(mBundle);


                    TCTDbAdapter sour = new TCTDbAdapter(getApplicationContext());
                    sour.open();
                    ArrayList<Profile> arr = sour.getAllProfiles();
                    //testing login
                    if (arr.size() > 0){
                        checkParametersAndStartService(mIntent, mobileConfigList);
                    }
                    //</editor-fold>

                    //<editor-fold desc="FDJ exec">
                    /*Gson g = new Gson();
                    String json = g.toJson(qosNotification);

                    Bundle bundle = new Bundle();
                    bundle.putString("QosNotification", json);

                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
                    Job fcmJob = dispatcher.newJobBuilder()
                            .setService(FcmJobService.class)
                            .setRecurring(false)
                            .setLifetime(Lifetime.FOREVER)
                            //.setTrigger(Trigger.NOW)
                            .setTrigger(Trigger.executionWindow(0, 1))
                            .setConstraints(Constraint.ON_ANY_NETWORK)
                            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                            .setExtras(bundle)
                            .setTag("FcmJobService")
                            .build();
                    dispatcher.mustSchedule(fcmJob);*/
                    //</editor-fold>
                }

            }
            //</editor-fold>
        }

    }

    private void checkParametersAndStartJob(JobScheduler fcmJob, JobInfo jobInfo, ArrayList<MobileConfiguration> mobileConfigList) {

        for (int i = 0; i < mobileConfigList.size(); i++) {
            if (mobileConfigList.get(i).getKey().equalsIgnoreCase("Qos_BypassSimValidation")) {
                byPass = Integer.parseInt(mobileConfigList.get(i).getValue());
            }

            if (mobileConfigList.get(i).getKey().equalsIgnoreCase("Qos_CarrierNames")) {
                carrierNames = mobileConfigList.get(i).getValue();
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            if (byPass == 1) {

                Log.wtf("@@@@@@ BYPASS ", "SEND");
                Log.d("@@@@@@ BYPASS ", "SEND");

                fcmJob.schedule(jobInfo);

            } else {

                boolean acceptedCarrier = false;
                TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String carrierName = manager.getSimOperatorName();
                Log.wtf("@@@@@@ carrierName ", "is " + carrierName);

                for (String carrier : carrierNames.split(",")) {
                    Log.wtf("@@@@@@ carrier ", "is " + carrier);
                    if (carrierName.equalsIgnoreCase(carrier)) {
                        acceptedCarrier = true;
                        break;
                    }
                }

                if (acceptedCarrier) {

                    Log.wtf("@@@@@@ Proceed ", "SEND");
                    fcmJob.schedule(jobInfo);
                } else {

                    Log.wtf("@@@@@@ Won't ", "SEND");
                }
            }
        }
    }

    private void checkParametersAndStartService(Intent mIntent, ArrayList<MobileConfiguration> mobileConfigList) {

        for (int i = 0; i < mobileConfigList.size(); i++) {
            if (mobileConfigList.get(i).getKey().equalsIgnoreCase("Qos_BypassSimValidation")) {
                byPass = Integer.parseInt(mobileConfigList.get(i).getValue());
            }

            if (mobileConfigList.get(i).getKey().equalsIgnoreCase("Qos_CarrierNames")) {
                carrierNames = mobileConfigList.get(i).getValue();
            }
        }

        if (byPass == 1) {

            Log.wtf("@@@@@@ BYPASS ", "SEND");
            startService(mIntent);

        } else {

            boolean acceptedCarrier = false;
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String carrierName = manager.getSimOperatorName();
            Log.wtf("@@@@@@ carrierName ", "is " + carrierName);

            for (String carrier : carrierNames.split(",")) {
                Log.wtf("@@@@@@ carrier ", "is " + carrier);
                if (carrierName.equalsIgnoreCase(carrier)) {
                    acceptedCarrier = true;
                    break;
                }
            }

            if (acceptedCarrier) {

                Log.wtf("@@@@@@ Proceed ", "SEND");
                startService(mIntent);
            } else {

                Log.wtf("@@@@@@ Won't ", "SEND");
            }
        }
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

  /*  private void sendNotification(int id, String messageBody) {

        Log.wtf("received", "notification");

        SharedPreferences mshared = PreferenceManager.getDefaultSharedPreferences(this);
        if (mshared.getBoolean(getString(R.string.enable_notification), true)) {

            //<editor-fold desc="amal's way">
            Intent intent = null;
            Log.wtf("notificationid", id + "");

            intent = new Intent(this, SplashActivity.class);
            intent.putExtra("notificationId", id);

            *//*Intent home = new Intent(getApplicationContext(), HomePageActivity.class);
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*//*

            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setSmallIcon(R.mipmap.notification);

            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
            mBuilder.setContentTitle(getString(R.string.app_name));
            mBuilder.setContentText(messageBody);
            mBuilder.setAutoCancel(true);
            mBuilder.setColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

            mBuilder.setContentIntent(pendingIntent);

            notificationManager.notify(id, mBuilder.build());
        }
    }*/






    private void sendNotification(int id,  String messageBody) {

        Intent intent = null;

        Log.wtf("received", "notification_new");


        SharedPreferences mshared = PreferenceManager.getDefaultSharedPreferences(this);
        if (mshared.getBoolean(getString(R.string.enable_notification), true)) {
            MyApplication.isNotification=true;


            intent = new Intent(this, SplashActivity.class);
            intent.putExtra("notificationId", id);


            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, MyApplication.UNIQUE_REQUEST_CODE++, intent, PendingIntent.FLAG_ONE_SHOT);

        /*Intent backIntent = new Intent(this, SplashActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, UNIQUE_REQUEST_CODE++, new Intent[] {backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);*/

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Arsel_001")
                    .setSmallIcon(R.mipmap.notification)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(messageBody)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(messageBody))
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("Arsel_001", "Arsel", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
        }
    }






    public class RetrieveOnlineStatusTask extends AsyncTask<Void, Void, BufferedReader> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.wtf("RetrieveOnlineStatusTask", "start");
        }

        @Override
        protected BufferedReader doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            URL url;
            BufferedReader bufferedReader = null;
            InputStreamReader in = null;
            HttpURLConnection urlConnection = null;
            try {
                //url = new URL("https://ifcfg.me/ip");
                url = new URL("http://ip-api.com/json");
                urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                in = new InputStreamReader(urlConnection.getInputStream());
                bufferedReader = new BufferedReader(in);
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                    buffer.append('\r');
                }
                bufferedReader.close();
                in.close();
                JSONObject json_data = new JSONObject(buffer.toString());
                MyApplication.ip = json_data.getString("query");
                MyApplication.isp = json_data.getString("isp");

                //ip = buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(BufferedReader bufferedReader) {
            super.onPostExecute(bufferedReader);

        }
    }


}