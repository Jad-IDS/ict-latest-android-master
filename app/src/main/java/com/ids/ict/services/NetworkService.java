package com.ids.ict.services;

import android.app.IntentService;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.ids.ict.Actions;
import com.ids.ict.TCTDbAdapter;

public class NetworkService extends IntentService {

    JobScheduler jobScheduler;
    ComponentName componentName;
    JobInfo jobInfo;
    final int JOB_ID = 2;

    public NetworkService() {
        super("NetworkService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();


        boolean isNetworkConnected = extras.getBoolean("isNetworkConnected");
        Log.d("isNetworkConnected", "? "+ isNetworkConnected);

        //<editor-fold desc="all offline code">
        /*//<editor-fold desc="job scheduler">
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {


            if (Build.VERSION.SDK_INT >= 24) {

                jobScheduler = (JobScheduler)  getSystemService(JOB_SCHEDULER_SERVICE);
                componentName = new ComponentName(this, OfflineJobService.class);
                jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                        .setMinimumLatency(60 * 1000)
                        .build();

            }else{

                jobScheduler = (JobScheduler)  getSystemService(JOB_SCHEDULER_SERVICE);
                componentName = new ComponentName(this, OfflineJobService.class);
                jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                        .setPeriodic(60 * 1000)
                        .build();
            }

        }
        //</editor-fold>


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            isNetworkConnected = extras.getBoolean("isNetworkConnected");

            if (!isNetworkConnected) { // initiate scheduler


                if (!isScheduledJobServiceOn()) {
                    //jobScheduler.schedule(jobInfo);
                    Log.wtf("offline job must be", "initialized");
                }else{

                    Log.wtf("job ", "already running");
                }
                Log.d("NO", "network");
                Log.wtf("NO", "network");

            } else {

                Log.d("YES", "network");
                Log.wtf("YES", "network");
                if (isScheduledJobServiceOn()) { //stop the scheduler, then filter

                    jobScheduler.cancelAll();
                    Log.wtf("jobScheduler", "canceled");
                }

                //<editor-fold desc = "precaution for oreo network change">
                *//*TCTDbAdapter database = new TCTDbAdapter(getApplicationContext());
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
                }*//*
                //</editor-fold>
            }
        }*/
        //</editor-fold>
    }

    private boolean isScheduledJobServiceOn() {

        boolean hasBeenScheduled = false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
                if (jobInfo.getId() == JOB_ID) {
                    hasBeenScheduled = true;
                    break;
                }
            }
        }

        return hasBeenScheduled;
    }

}