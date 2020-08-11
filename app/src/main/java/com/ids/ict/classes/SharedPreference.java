package com.ids.ict.classes;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dev on 7/29/2016.
 */

public class SharedPreference {

    public static final String PREFS_NAME = "ICT_APP";
    public static final String FAVORITES = "Company_Favorite";


    public static final String MOBILE_QOS_TEST = "Mobile_Qos_Test";
    public static final String NO_INTERNET_QOS_TEST = "No_Internet_Qos_Test";
    public static final String SCHEDULED_QOS_TEST = "Scheduled_Qos_Test";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Mail_OFF> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public boolean isFound(Context context, Mail_OFF company) {
        List<Mail_OFF> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Mail_OFF>();
        for (int i = 0; i < favorites.size(); i++) {

            if (favorites.get(i).equals(company)) {
                return true;
            }

        }
        return false;
    }

    public void addFavorite(Context context, Mail_OFF company) {
        List<Mail_OFF> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Mail_OFF>();

        // for (int i = 0; i < favorites.size(); i++) {
        //
        // if (favorites.get(i).equals(company)) {
        // Log.d("Already", "exists");
        // return;
        // }
        // /*
        // * if(favorites.get(i).getCompanyId() == company.getCompanyId()){
        // * Log.d("Already","exists"); return; }
        // */
        // }
        // Log.d("before", "add");
        favorites.add(company);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Mail_OFF company) {
        ArrayList<Mail_OFF> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(company);
            saveFavorites(context, favorites);
        }
    }

    public void removeAllFavorite(Context context) {
        ArrayList<Mail_OFF> favorites = getFavorites(context);
        if (favorites != null) {
            for (int i = 0; i < favorites.size(); i++)
                favorites.remove(favorites.get(i));

        }
    }

    public ArrayList<Mail_OFF> getFavorites(Context context) {
        SharedPreferences settings;
        List<Mail_OFF> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Mail_OFF[] favoriteItems = gson.fromJson(jsonFavorites,
                    Mail_OFF[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Mail_OFF>(favorites);

        } else
            return null;

        return (ArrayList<Mail_OFF>) favorites;
    }


    public ArrayList<QosTest> getScheduledQosTests(Context context) {

        SharedPreferences settings;
        List<QosTest> offlineTests;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(SCHEDULED_QOS_TEST)) {
            String jsonTests = settings.getString(SCHEDULED_QOS_TEST, null);
            Gson gson = new Gson();
            QosTest[] offlineTestsArray = gson.fromJson(jsonTests, QosTest[].class);

            offlineTests = Arrays.asList(offlineTestsArray);
            offlineTests = new ArrayList<QosTest>(offlineTests);

        } else
            return null;

        return (ArrayList<QosTest>) offlineTests;
    }

    public void saveMobileDataQosTests(Context context, List<QosTest> offlineTests) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonOfflineTests = gson.toJson(offlineTests);

        editor.putString(MOBILE_QOS_TEST, jsonOfflineTests).apply();

    }

    public void addMobileDataQosTest(Context context, QosTest qosTest) {

        List<QosTest> offlineQosTests = getMobileDataQosTests(context);
        if (offlineQosTests == null)
            offlineQosTests = new ArrayList<QosTest>();


        offlineQosTests.add(qosTest);
        saveMobileDataQosTests(context, offlineQosTests);
    }

    public void removeOfflineQosTest(Context context, QosTest qosTest) {
        ArrayList<QosTest> offlineQosTests = getMobileDataQosTests(context);
        if (offlineQosTests != null) {
            offlineQosTests.remove(qosTest);
            saveMobileDataQosTests(context, offlineQosTests);
        }
    }

    public void removeAllOfflineQosTests(Context context) {

        ArrayList<QosTest> offlineQosTests = getMobileDataQosTests(context);
        if (offlineQosTests != null) {
            // offlineQosTests.removeAll(offlineQosTests);
            offlineQosTests.clear();
        }
        saveMobileDataQosTests(context,offlineQosTests);
    }

    public ArrayList<QosTest> getMobileDataQosTests(Context context) {

        SharedPreferences settings;
        List<QosTest> offlineTests;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(MOBILE_QOS_TEST)) {
            String jsonTests = settings.getString(MOBILE_QOS_TEST, null);
            Gson gson = new Gson();
            QosTest[] offlineTestsArray = gson.fromJson(jsonTests, QosTest[].class);

            offlineTests = Arrays.asList(offlineTestsArray);
            offlineTests = new ArrayList<QosTest>(offlineTests);

        } else
            return null;

        return (ArrayList<QosTest>) offlineTests;
    }




    public void removeAllNoInternetQosTests(Context context) {

        ArrayList<QosTest> offlineQosTests = getNoInternetQosTests(context);
        if (offlineQosTests != null) {
            offlineQosTests.clear();
        }
        saveNoInternetQosTests(context,offlineQosTests);
    }
    public void addNoInternetQosTest(Context context, QosTest qosTest) {

        List<QosTest> noNetQosTests = getNoInternetQosTests(context);
        if (noNetQosTests == null)
            noNetQosTests = new ArrayList<QosTest>();

        noNetQosTests.add(qosTest);
        saveNoInternetQosTests(context, noNetQosTests);
    }
    public void saveNoInternetQosTests(Context context, List<QosTest> noNetQosTests) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonOfflineTests = gson.toJson(noNetQosTests);

        editor.putString(NO_INTERNET_QOS_TEST, jsonOfflineTests).apply();

    }
    public ArrayList<QosTest> getNoInternetQosTests(Context context) {

        SharedPreferences settings;
        List<QosTest> noNetTests;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(NO_INTERNET_QOS_TEST)) {
            String jsonTests = settings.getString(NO_INTERNET_QOS_TEST, null);
            Gson gson = new Gson();
            QosTest[] noNetTestsArray = gson.fromJson(jsonTests, QosTest[].class);

            noNetTests = Arrays.asList(noNetTestsArray);
            noNetTests = new ArrayList<QosTest>(noNetTests);

        } else
            return null;

        return (ArrayList<QosTest>) noNetTests;
    }
}
