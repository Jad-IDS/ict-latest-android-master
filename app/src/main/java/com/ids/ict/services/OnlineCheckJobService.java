package com.ids.ict.services;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;


//@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
@TargetApi(Build.VERSION_CODES.M)
public class OnlineCheckJobService extends JobService {

    Intent intent = new Intent();
    JobParameters params;
    Boolean isInForeground = false;
    Boolean intentExist = true;

    @Override
    public void onCreate() {

        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "checking service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Checking services")
                    .setContentText("").build();


            startForeground(1, notification);
        }
    }


    @Override
    public boolean onStartJob(JobParameters params) {

        this.params = params;

        boolean connected = Actions.isOnline();

         Log.wtf("is connected", "? " + connected);
        Log.d("is connected", "? " + connected);

        if (Build.VERSION.SDK_INT >= 24) { //Nougat and oreo
            scheduleRefresh();
        }

        ComponentName comp = new ComponentName(getApplicationContext().getPackageName(), NetworkService.class.getName());
        intent.putExtra("isNetworkConnected", connected);
        intent.setComponent(comp);

//        if (intent != null)
//        {
//            isInForeground = intent.getBooleanExtra("pause", true);
//            ud = intent.getStringExtra("ud");
//        }
//        else
//        {
//            intentExist = false;
//            isInForeground = false;
//        }

        try{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                this.startForegroundService(intent);
//                getApplicationContext().startForegroundService(intent);
                getApplicationContext().startService(intent);
            }
            else{
                getApplicationContext().startService(intent);
            }

            Log.wtf("getApplicationContext().startService(intent)","Run well");
        }
        catch(Exception e){
            try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getApplicationContext().startForegroundService(intent);
                e.printStackTrace();
            }
        }catch (Exception e1){
                Log.wtf("Exception",e1.toString());
            }



        }

        jobFinished(params, false);

        return false;
    }


    private void scheduleRefresh() {

        Log.d("Online Check ", "scheduleRefresh");
        Log.wtf("Online Check ", "scheduleRefresh");
        JobScheduler mJobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo.Builder mJobBuilder = new JobInfo.Builder(1, new ComponentName(getPackageName(), OnlineCheckJobService.class.getName()));

        JobInfo jobInfo = mJobBuilder
                .setMinimumLatency(MyApplication.SCHEDULER_SECONDS * 1000)
                .build();

        mJobScheduler.schedule(jobInfo);
    }


    private boolean isReachable() {

        return Actions.isOnline();
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
