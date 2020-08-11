package com.ids.ict;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.ids.ict.activities.Event;
import com.ids.ict.activities.Notifications;
import com.ids.ict.activities.test;
import com.ids.ict.classes.Area;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.Country;
import com.ids.ict.classes.IssuesDetails;
import com.ids.ict.classes.Mail_OFF;
import com.ids.ict.classes.Profile;
import com.ids.ict.classes.QosTest;
import com.ids.ict.classes.ServicePro;
import com.ids.ict.classes.SharedPreference;
import com.ids.ict.classes.SubArea;
import com.ids.ict.parser.QosParser;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TCTDbAdapter {
    private static final String DATABASE_NAME = "ICTDB";// database name

    private static final int DATABASE_VERSION = 5;// database version
    // tables
    private static final String PROFILE_TABLE = "profile";// categories table
    private static final String reports_off_TABLE = "reports_off";// news table
    private static final String reports_TABLE = "reports";// news table
    private static final String ISSUE_TYPE_TABLE = "issue_type";
    private static final String ISSUE_DETAILS_TABLE = "issue_details";
    private static final String NOTIFICATION_TABLE = "noti";
    private static final String SP_TABLE = "sp";
    private static final String COUNTRY_TABLE = "country";
    private static final String DateStamp_TABLE = "datestamp";
    private static final String AREA_TABLE = "area";
    private static final String SUB_AREA_TABLE = "sub_area";

    private static final String TABLE_QOS_TESTS = "qostests";

    //area table values
    public static final String AREA_NAME_AR = "name_ar";
    public static final String AREA_NAME_EN = "name_en";
    public static final String AREA_ID = "id";

    //sub area table values
    public static final String SUB_AREA_NAME_AR = "name_ar";
    public static final String SUB_AREA_NAME_EN = "name_en";
    public static final String SUB_AREA_ID = "id";
    public static final String SUB_PARENT_AREA_ID = "parent_id";
    // categories table attributes/columns
    public static final String PROFILE_REG_ID = "reg_id";
    public static final String PROFILE_MOBILE = "mobilenumber";
    public static final String PROFILE_QATARID = "qatar_id";
    public static final String PROFILE_EMAIL = "Email";
    public static final String PROFILE_Lanq = "Lang";

    // categories table attributes/columns id INTEGER PRIMARY KEY AUTOINCREMENT
    public static final String Report_Off_ID_Report = "id_report";
    public static final String Report_Off_REG_ID = "reg_id";
    public static final String Reports_Off_MOBILE = "mobilenumber";
    public static final String Reports_Off_QATARID = "qatar_id";
    public static final String Reports_Off_EMAIL = "email";
    public static final String Reports_Off_Lanq = "lang";
    public static final String Reports_Off_COMMENTS = "comments";
    public static final String Reports_Off_LOCATION = "location";
    public static final String Reports_Off_Date = "date";
    public static final String Reports_Off_DATE_AFF = "date_aff";
    public static final String Reports_Off_ISSUENAME = "issuename";
    public static final String Reports_Off_Status = "status";
    public static final String Reports_Off_issueid = "issue_id";
    public static final String Reports_Off_spid = "sp_id";
    public static final String Reports_Off_locx = "locx";
    public static final String Reports_Off_locy = "locy";
    public static final String Reports_Off_countryid = "countryid";
    public static final String Reports_Off_affecqatariid = "affecqatariid";
    public static final String Reports_Off_spname = "spname";

    // categories table attributes/columns
    public static final String ISSUE_TYPE_ID = "id_type";
    public static final String ISSUE_TYPE_NAME = "name_type";
    public static final String ISSUE_TYPE_SHOWMAP = "showmap_type";
    public static final String ISSUE_TYPE_LOCMAN = "locman_type";
    public static final String ISSUE_TYPE_SPTRANS = "sptrans_type";
    public static final String ISSUE_TYPE_ROAMING = "roaming_type";
    public static final String ISSUE_TYPE_MAIN_ISSUE = "main_issue_type";
    public static final String ISSUE_TYPE_IMAGE_DAY = "main_issue_image_day";
    public static final String ISSUE_TYPE_IMAGE_NIGHT = "main_issue_image_night";
    public static final String ISSUE_TYPE_IMAGE = "main_issue_image";
    // categories table attributes/columns
    public static final String ISSUE_DETAIL_ID = "id_detail";
    public static final String ISSUE_DETAIL_NAME = "name_detail";
    public static final String ISSUE_DETAIL_Modify = "mobile_modify";
    public static final String ISSUE_DETAIL_ISSUE_ID = "issue_id_detail";
    public static final String ISSUE_DETAIL_WAIT_DURATION = "wait_duration";
    public static final String ISSUE_DETAIL_WAIT_UNIT = "wait_unit";
    public static final String ISSUE_DETAIL_WAIT_UNIT_AR = "wait_unit_ar";
    public static final String ISSUE_DETAIL_LANGUAGE = "lang_detail";
    public static final String ISSUE_DETAIL_SPECIAL_NEEDS_DURATION = "special_needs_duration";
    public static final String ISSUE_DETAIL_SPECIAL_NEEDS_UNIT = "special_needs_unit";
    public static final String ISSUE_DETAIL_SPECIAL_NEEDS_UNIT_AR = "special_needs_unit_ar";
    public static final String ISSUE_DETAIL_CHECKAPPDATE = "issue_details_appdate";
    // categories table attributes/columns
    public static final String NOTIFICATION_ID = "id_not";
    public static final String NOTIFICATION_NAME = "name_not";
    public static final String NOTIFICATION_DETAILS = "details_not";
    public static final String NOTIFICATION_date = "date_not";
    public static final String NOTIFICATION_lang = "lang_not";

    // categories table attributes/columns
    public static final String SP_ID = "id_sp";
    public static final String SP_Code = "code_sp";
    public static final String SP_NAME = "name_sp";
    public static final String SP_ISFORTRANSFER = "istrans_sp";
    public static final String SP_LANQ = "lang_sp";

    // categories table attributes/columns
    public static final String COUNTRY_ID = "id_country";
    public static final String COUNTRY_NAME = "name_country";
    public static final String COUNTRY_Lang = "lang_country";

    // categories table attributes/columns
    public static final String DATE_VALUE = "date_value";

    // helps in working with db
    private DatabaseHelper mDbHelper;
    // sqlite database object
    private SQLiteDatabase mDb;

    //create table area
    private static final String AREA_CREATE_TABLE = "create table "
            + AREA_TABLE + " (" + AREA_ID + " integer, "
            + AREA_NAME_AR + " text  , " + AREA_NAME_EN
            + " text);";
    // create news table sub area
    private static final String SUB_AREA_CREATE_TABLE = "create table "
            + SUB_AREA_TABLE + " (" + SUB_AREA_ID + " integer, "
            + SUB_AREA_NAME_AR + " text , " + SUB_AREA_NAME_EN
            + " text, "
            + SUB_PARENT_AREA_ID + " integer);";

    // create news table query
    private static final String PROFILE_TABLE_CREATE = "create table "
            + PROFILE_TABLE + " (" + PROFILE_REG_ID + " text not null, "
            + PROFILE_MOBILE + " text not null, " + PROFILE_QATARID
            + " text not null, " + PROFILE_EMAIL + " text not null, "
            + PROFILE_Lanq + " text);";

    private static final String Reports_OFF_TABLE_CREATE = "create table "
            + reports_off_TABLE + " (" + Report_Off_ID_Report
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Report_Off_REG_ID
            + " text not null, " + Reports_Off_MOBILE + " text not null, "
            + Reports_Off_QATARID + " text not null, " + Reports_Off_EMAIL
            + " text not null, " + Reports_Off_Lanq + " text not null, "
            + Reports_Off_COMMENTS + " text not null, " + Reports_Off_LOCATION
            + " text not null, " + Reports_Off_DATE_AFF + " text not null, "
            + Reports_Off_ISSUENAME + " text not null, " + Reports_Off_Status
            + " text not null, " + Reports_Off_issueid + " text not null, "
            + Reports_Off_spid + " text not null, " + Reports_Off_locx
            + " text not null, " + Reports_Off_locy + " text not null, "
            + Reports_Off_countryid + " text not null, "
            + Reports_Off_affecqatariid + " text not null, "
            + Reports_Off_spname + " text not null, " + Reports_Off_Date
            + " text);";

    private static final String Reports_TABLE_CREATE = "create table "
            + reports_TABLE + " (" + Report_Off_REG_ID + " text not null, "
            + Reports_Off_MOBILE + " text not null, " + Reports_Off_QATARID
            + " text not null, " + Reports_Off_EMAIL + " text not null, "
            + Reports_Off_Lanq + " text not null, " + Reports_Off_COMMENTS
            + " text not null, " + Reports_Off_LOCATION + " text not null, "
            + Reports_Off_DATE_AFF + " text not null, " + Reports_Off_ISSUENAME
            + " text not null, " + Reports_Off_Date + " text);";

    private static final String ISSUE_DETAIL_TABLE_CREATE = "create table "
            + ISSUE_DETAILS_TABLE + " (" + ISSUE_DETAIL_ID
            + " integer not null, " + ISSUE_DETAIL_NAME + " text not null, "
            + ISSUE_DETAIL_Modify + " text not null, " + ISSUE_DETAIL_LANGUAGE
            + " text not null, " + ISSUE_DETAIL_WAIT_DURATION
            + " text not null, " + ISSUE_DETAIL_WAIT_UNIT + " text not null, "
            + ISSUE_DETAIL_SPECIAL_NEEDS_DURATION + " text null, "
            + ISSUE_DETAIL_WAIT_UNIT_AR + " text null, "
            + ISSUE_DETAIL_SPECIAL_NEEDS_UNIT + " text null, "
            + ISSUE_DETAIL_SPECIAL_NEEDS_UNIT_AR + " text null, "
            + ISSUE_DETAIL_ISSUE_ID + " text, " + ISSUE_DETAIL_CHECKAPPDATE + " text null);";

    private static final String NOTIFICATION_TABLE_CREATE = "create table "
            + NOTIFICATION_TABLE + " (" + NOTIFICATION_ID
            + " integer not null, " + NOTIFICATION_NAME + " text not null, "
            + NOTIFICATION_DETAILS + " text not null, " + NOTIFICATION_lang
            + " text not null, " + NOTIFICATION_date + " text);";

    private static final String SP_TABLE_CREATE = "create table " + SP_TABLE
            + " (" + SP_ID + " integer not null, " + SP_NAME
            + " text not null, " + SP_LANQ + " text not null, " + SP_Code
            + " text not null, " + SP_ISFORTRANSFER + " text);";

    private static final String COUNTRY_TABLE_CREATE = "create table "
            + COUNTRY_TABLE + " (" + COUNTRY_ID + " text not null, "
            + COUNTRY_Lang + " text not null, " + COUNTRY_NAME + " text);";

    private static final String DateStamp_TABLE_CREATE = "create table "
            + DateStamp_TABLE + " ("

            + DATE_VALUE + " text);";

    private static final String ISSUE_TYPE_TABLE_CREATE = "create table "
            + ISSUE_TYPE_TABLE + " (" + ISSUE_TYPE_ID + " integer not null, "
            + ISSUE_TYPE_NAME + " text not null, " + ISSUE_TYPE_SHOWMAP
            + " text not null, " + ISSUE_TYPE_LOCMAN + " text not null, "
            + ISSUE_TYPE_SPTRANS + " text not null, " + ISSUE_TYPE_MAIN_ISSUE
            + " text not null, " + ISSUE_TYPE_IMAGE_DAY + " blob, "
            + ISSUE_TYPE_IMAGE_NIGHT + " blob, " + ISSUE_TYPE_ROAMING
            + " text);";
    // create events table query

    // context object associated with the sqlite object
    private final Context mCtx;

    // constructor in it wec set the context
    public TCTDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    // when called the db is opened/created
    public TCTDbAdapter open() throws android.database.SQLException {
        // instantiation of the inner class object to help in working with the
        // db
        mDbHelper = new DatabaseHelper(mCtx);
        // with the helper we open the database in an editable mode
        // here the database is actually created and opened
        // mDb = mDbHelper.getReadableDatabase();

        mDb = mDbHelper.getWritableDatabase();
        // return itself to the caller which is the activity
        // the activity needs this to access the database through it

        return this;
    }

    // to close the database after finishing
    // may be called from the activity back button to be closed when activity is
    // killed
    // or from the asynctask after loading the needed data
    public void close() {
        mDbHelper.close();
    }

    // insert a new record in the create table
    public long createProfile(String key, String title, String detail,
                              String date, String imageUrl) {
        // to declare values to be inserted in the table
        ContentValues initialValues = new ContentValues();
        initialValues.put(PROFILE_REG_ID, key);
        initialValues.put(PROFILE_MOBILE, title);
        initialValues.put(PROFILE_QATARID, detail);
        initialValues.put(PROFILE_EMAIL, date);
        initialValues.put(PROFILE_Lanq, imageUrl);
        // actual insert query execution done here and return a value -1 if
        // failure otherwise the record id
        return mDb.insert(PROFILE_TABLE, null, initialValues);
    }

    public long insertArea(Area a) {
        // to declare values to be inserted in the table
        ContentValues initialValues = new ContentValues();
        initialValues.put(AREA_ID, a.getId());
        initialValues.put(AREA_NAME_AR, a.getNameAr());
        initialValues.put(AREA_NAME_EN, a.getNameEn());


        // actual insert query execution done here and return a value -1 if
        // failure otherwise the record id
        return mDb.insert(AREA_TABLE, null, initialValues);
    }

    public boolean deleteArea() {

        return mDb.delete(AREA_TABLE, null, null) > 0;
    }

    public boolean deleteSubArea() {

        return mDb.delete(SUB_AREA_TABLE, null, null) > 0;
    }

    public ArrayList<Area> GetAreas() {
        String query = "select * from " + AREA_TABLE + ";";

        ArrayList<Area> areas = new ArrayList<Area>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            Area area = cursorToArea(cursor);
            areas.add(area);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return areas;
    }

    public ArrayList<SubArea> GetSubAreas(String areaId) {
        String query = "select * from " + SUB_AREA_TABLE + " WHERE "
                + SUB_PARENT_AREA_ID + "=" + areaId + ";";

        ArrayList<SubArea> subareas = new ArrayList<SubArea>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            SubArea subarea = cursorToSubArea(cursor);
            subareas.add(subarea);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return subareas;
    }

    public long insertSubArea(SubArea a) {
        // to declare values to be inserted in the table
        ContentValues initialValues = new ContentValues();
        initialValues.put(SUB_AREA_ID, a.getId());
        initialValues.put(SUB_AREA_NAME_AR, a.getNameAr());
        initialValues.put(SUB_AREA_NAME_EN, a.getNameEn());
        initialValues.put(SUB_PARENT_AREA_ID, a.getParentId());

        // actual insert query execution done here and return a value -1 if
        // failure otherwise the record id
        return mDb.insert(SUB_AREA_TABLE, null, initialValues);
    }

    public long createissue_type(int key, String title, String showmap,
                                 String locman, String sptrans, String roaming, String main_issue,
                                 String imgday, String imgnight, String spTransfer) {
        // to declare values to be inserted in the table
        ContentValues initialValues = new ContentValues();
        initialValues.put(ISSUE_TYPE_ID, key);
        initialValues.put(ISSUE_TYPE_NAME, title);
        initialValues.put(ISSUE_TYPE_SHOWMAP, showmap);
        initialValues.put(ISSUE_TYPE_LOCMAN, locman);
        initialValues.put(ISSUE_TYPE_SPTRANS, sptrans);
        initialValues.put(ISSUE_TYPE_ROAMING, roaming);
        initialValues.put(ISSUE_TYPE_MAIN_ISSUE, main_issue);
        initialValues.put(ISSUE_TYPE_IMAGE_DAY, imgday);
        initialValues.put(ISSUE_TYPE_IMAGE_NIGHT, imgnight);
        initialValues.put(ISSUE_TYPE_SPTRANS, spTransfer);

        // actual insert query execution done here and return a value -1 if
        // failure otherwise the record id
        return mDb.insert(ISSUE_TYPE_TABLE, null, initialValues);
    }

    public long createissue_detail(int key, String title, String issue_type_id,
                                   String modify, String wait_duration, String wait_unit,
                                   String wait_unit_ar, String lang, String specialneeddurationvalue, String specialneedunit, String specialneedunitar, String CheckOnApplicationDate) {
        // to declare values to be inserted in the table
        ContentValues initialValues = new ContentValues();
        initialValues.put(ISSUE_DETAIL_ID, key);
        initialValues.put(ISSUE_DETAIL_NAME, title);
        initialValues.put(ISSUE_DETAIL_Modify, modify);
        initialValues.put(ISSUE_DETAIL_WAIT_DURATION, wait_duration);
        initialValues.put(ISSUE_DETAIL_WAIT_UNIT, wait_unit);
        initialValues.put(ISSUE_DETAIL_WAIT_UNIT_AR, wait_unit_ar);
        initialValues.put(ISSUE_DETAIL_ISSUE_ID, issue_type_id);
        initialValues.put(ISSUE_DETAIL_LANGUAGE, lang);
        initialValues.put(ISSUE_DETAIL_SPECIAL_NEEDS_DURATION,
                specialneeddurationvalue);
        initialValues.put(ISSUE_DETAIL_SPECIAL_NEEDS_DURATION,
                specialneeddurationvalue);
        initialValues.put(ISSUE_DETAIL_SPECIAL_NEEDS_UNIT,
                specialneedunit);
        initialValues.put(ISSUE_DETAIL_CHECKAPPDATE,
                CheckOnApplicationDate);
        initialValues.put(ISSUE_DETAIL_SPECIAL_NEEDS_UNIT_AR,
                specialneedunitar);
        // actual insert query execution done here and return a value -1 if
        // failure otherwise the record id
        return mDb.insert(ISSUE_DETAILS_TABLE, null, initialValues);
    }

    public long createissue_notification(int key, String title, String desc,
                                         String date, String lang) {
        // to declare values to be inserted in the table
        ContentValues initialValues = new ContentValues();
        initialValues.put(NOTIFICATION_ID, key);
        initialValues.put(NOTIFICATION_NAME, title);
        initialValues.put(NOTIFICATION_DETAILS, desc);
        initialValues.put(NOTIFICATION_date, date);
        initialValues.put(NOTIFICATION_lang, lang);
        // actual insert query execution done here and return a value -1 if
        // failure otherwise the record id
        return mDb.insert(NOTIFICATION_TABLE, null, initialValues);
    }

    public long create_sp(int key, String title, String istrans, String lang,
                          String code) {
        // to declare values to be inserted in the table
        ContentValues initialValues = new ContentValues();
        initialValues.put(SP_ID, key);
        initialValues.put(SP_NAME, title);
        initialValues.put(SP_ISFORTRANSFER, istrans);
        initialValues.put(SP_LANQ, lang);
        initialValues.put(SP_Code, code);
        // actual insert query execution done here and return a value -1 if
        // failure otherwise the record id
        return mDb.insert(SP_TABLE, null, initialValues);
    }

    public long create_country(String key, String title, String lang) {
        // to declare values to be inserted in the table
        ContentValues initialValues = new ContentValues();
        initialValues.put(COUNTRY_ID, key);
        initialValues.put(COUNTRY_NAME, title);
        initialValues.put(COUNTRY_Lang, lang);
        // actual insert query execution done here and return a value -1 if
        // failure otherwise the record id
        return mDb.insert(COUNTRY_TABLE, null, initialValues);
    }

    public long create_datestamp(String key) {
        // to declare values to be inserted in the table
        ContentValues initialValues = new ContentValues();
        initialValues.put(DATE_VALUE, key);

        // actual insert query execution done here and return a value -1 if
        // failure otherwise the record id
        return mDb.insert(DateStamp_TABLE, null, initialValues);
    }


    public long getOfflineReportsCount() {
        long cnt = DatabaseUtils.queryNumEntries(mDb, reports_off_TABLE);
        mDb.close();
        return cnt;
    }

    public void deleteAllOfflineReports() {

        mDb.execSQL("delete from " + reports_off_TABLE);

    }

    public long createReports_off(String key, String mobile, String email,
                                  String qatarid, String lang, String comm, String loc, String date,
                                  String date_aff, String issuename, String status, String issueid,
                                  String spid, String locx, String locy, String countryid,
                                  String affecid, String spaname) {
        // to declare values to be inserted in the table
        ContentValues initialValues = new ContentValues();
        initialValues.put(Report_Off_REG_ID, key);
        initialValues.put(Reports_Off_MOBILE, mobile);
        initialValues.put(Reports_Off_EMAIL, email);
        initialValues.put(Reports_Off_QATARID, qatarid);
        initialValues.put(Reports_Off_Lanq, lang);
        initialValues.put(Reports_Off_COMMENTS, comm);
        initialValues.put(Reports_Off_LOCATION, loc);
        initialValues.put(Reports_Off_Date, date);
        initialValues.put(Reports_Off_DATE_AFF, date_aff);
        initialValues.put(Reports_Off_ISSUENAME, issuename);
        initialValues.put(Reports_Off_Status, status);
        initialValues.put(Reports_Off_issueid, issueid);
        initialValues.put(Reports_Off_spid, spid);
        initialValues.put(Reports_Off_locx, locx);
        initialValues.put(Reports_Off_locy, locy);
        initialValues.put(Reports_Off_countryid, countryid);
        initialValues.put(Reports_Off_affecqatariid, affecid);
        initialValues.put(Reports_Off_spname, spaname);
        // actual insert query execution done here and return a value -1 if
        // failure otherwise the record id
        return mDb.insert(reports_off_TABLE, null, initialValues);
    }


    // insert a new record in the create table
    public long createReports(String key, String mobile, String email,
                              String qatarid, String lang, String comm, String loc, String date,
                              String date_aff, String issuename) {
        // to declare values to be inserted in the table
        ContentValues initialValues = new ContentValues();
        initialValues.put(Report_Off_REG_ID, key);
        initialValues.put(Reports_Off_MOBILE, mobile);
        initialValues.put(Reports_Off_EMAIL, email);
        initialValues.put(Reports_Off_QATARID, qatarid);
        initialValues.put(Reports_Off_Lanq, lang);
        initialValues.put(Reports_Off_COMMENTS, comm);
        initialValues.put(Reports_Off_LOCATION, loc);
        initialValues.put(Reports_Off_Date, date);
        initialValues.put(Reports_Off_DATE_AFF, date_aff);
        initialValues.put(Reports_Off_ISSUENAME, issuename);
        // actual insert query execution done here and return a value -1 if
        // failure otherwise the record id
        return mDb.insert(reports_TABLE, null, initialValues);
    }

    public ArrayList<Profile> getAllProfiles() {
        String query = "select " + PROFILE_REG_ID + ", " + PROFILE_MOBILE
                + ", " + PROFILE_QATARID + ", " + PROFILE_EMAIL + ", "
                + PROFILE_Lanq + " from " + PROFILE_TABLE + ";";

        ArrayList<Profile> newsList = new ArrayList<Profile>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            Profile news = cursorToNews(cursor);

            newsList.add(news);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public ArrayList<Mail_OFF> getAllReports_off() {
        String query = "select " + Report_Off_ID_Report + ", "
                + Report_Off_REG_ID + ", " + Reports_Off_MOBILE + ", "
                + Reports_Off_QATARID + ", " + Reports_Off_EMAIL + ", "
                + Reports_Off_Lanq + ", " + Reports_Off_COMMENTS + ", "
                + Reports_Off_LOCATION + ", " + Reports_Off_Date + ", "
                + Reports_Off_DATE_AFF + ", " + Reports_Off_ISSUENAME + ", "
                + Reports_Off_Status + ", " + Reports_Off_issueid + ", "
                + Reports_Off_spid + ", " + Reports_Off_locx + ", "
                + Reports_Off_locy + ", " + Reports_Off_countryid + ", "
                + Reports_Off_affecqatariid + ", " + Reports_Off_spname
                + " from " + reports_off_TABLE + " ORDER BY "
                + Report_Off_ID_Report + " DESC;";

        ArrayList<Mail_OFF> newsList = new ArrayList<Mail_OFF>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            Mail_OFF news = cursorToMail_OFF(cursor);

            newsList.add(news);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public ArrayList<Mail_OFF> getLastReports_off() {
        String query = "select " + Report_Off_ID_Report + ", "
                + Report_Off_REG_ID + ", " + Reports_Off_MOBILE + ", "
                + Reports_Off_QATARID + ", " + Reports_Off_EMAIL + ", "
                + Reports_Off_Lanq + ", " + Reports_Off_COMMENTS + ", "
                + Reports_Off_LOCATION + ", " + Reports_Off_Date + ", "
                + Reports_Off_DATE_AFF + ", " + Reports_Off_ISSUENAME + ", "
                + Reports_Off_Status + ", " + Reports_Off_issueid + ", "
                + Reports_Off_spid + ", " + Reports_Off_locx + ", "
                + Reports_Off_locy + ", " + Reports_Off_countryid + ", "
                + Reports_Off_affecqatariid + ", " + Reports_Off_spname
                + " from " + reports_off_TABLE + " WHERE "
                + Report_Off_ID_Report + " = (SELECT MAX("
                + Report_Off_ID_Report + ")  FROM " + reports_off_TABLE + ");";

        ArrayList<Mail_OFF> newsList = new ArrayList<Mail_OFF>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            Mail_OFF news = cursorToMail_OFF(cursor);

            newsList.add(news);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public ArrayList<Mail_OFF> getAllReports() {
        String query = "select " + Report_Off_REG_ID + ", "
                + Reports_Off_MOBILE + ", " + Reports_Off_QATARID + ", "
                + Reports_Off_EMAIL + ", " + Reports_Off_Lanq + ", "
                + Reports_Off_COMMENTS + ", " + Reports_Off_LOCATION + ", "
                + Reports_Off_Date + ", " + Reports_Off_DATE_AFF + ", "
                + Reports_Off_ISSUENAME + " from " + reports_TABLE + ";";

        ArrayList<Mail_OFF> newsList = new ArrayList<Mail_OFF>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            Mail_OFF news = cursorToMail_OFF(cursor);

            newsList.add(news);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public ArrayList<Event> getAllissue_Type() {
        String query = "select " + ISSUE_TYPE_ID + ", " + ISSUE_TYPE_NAME
                + ", " + ISSUE_TYPE_SHOWMAP + ", " + ISSUE_TYPE_LOCMAN + ", "
                + ISSUE_TYPE_SPTRANS + ", " + ISSUE_TYPE_ROAMING + ", "
                + ISSUE_TYPE_IMAGE_DAY + ", " + ISSUE_TYPE_IMAGE_NIGHT
                + " from " + ISSUE_TYPE_TABLE + ";";

        ArrayList<Event> newsList = new ArrayList<Event>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            Event news = cursorToIssue_type(cursor);
            newsList.add(news);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public ArrayList<Event> getissue_Type(String main_issue) {
        String query = "select " + ISSUE_TYPE_ID + ", " + ISSUE_TYPE_NAME
                + ", " + ISSUE_TYPE_SHOWMAP + ", " + ISSUE_TYPE_LOCMAN + ", "
                + ISSUE_TYPE_SPTRANS + ", " + ISSUE_TYPE_ROAMING + ", " + ISSUE_TYPE_SPTRANS + ", "
                + ISSUE_TYPE_IMAGE_DAY + ", " + ISSUE_TYPE_IMAGE_NIGHT
                + " from " + ISSUE_TYPE_TABLE + " WHERE "
                + ISSUE_TYPE_MAIN_ISSUE + "=" + main_issue + ";";

        ArrayList<Event> newsList = new ArrayList<Event>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            Event news = cursorToIssue_type(cursor);
            newsList.add(news);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public String getissue_det_name_en(String main_issue) {
        String query = "select " + ISSUE_DETAIL_NAME + " from "
                + ISSUE_DETAILS_TABLE + " WHERE " + ISSUE_DETAIL_LANGUAGE
                + " = 'English'AND " + ISSUE_DETAIL_ID + " = " + main_issue
                + ";";

        String newsList = "";
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            // Event news = cursorToIssue_type(cursor);

            newsList = cursor.getString(0);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public String getissue_det_name_ar(String main_issue) {
        String query = "select " + ISSUE_DETAIL_NAME + " from "
                + ISSUE_DETAILS_TABLE + " WHERE " + ISSUE_DETAIL_LANGUAGE
                + " = 'Arabic' AND " + ISSUE_DETAIL_ID + " = " + main_issue
                + ";";

        String newsList = "";
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            // Event news = cursorToIssue_type(cursor);

            newsList = cursor.getString(0);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public ArrayList<IssuesDetails> getissue_detail(String type_issue,
                                                    String lang) {
        String query = "select " + ISSUE_DETAIL_ID + ", " + ISSUE_DETAIL_NAME
                + ", " + ISSUE_DETAIL_ISSUE_ID + ", " + ISSUE_DETAIL_Modify
                + ", " + ISSUE_DETAIL_WAIT_DURATION + ", "
                + ISSUE_DETAIL_WAIT_UNIT + ", " + ISSUE_DETAIL_WAIT_UNIT_AR
                + ", " + ISSUE_DETAIL_LANGUAGE + ", "
                + ISSUE_DETAIL_SPECIAL_NEEDS_DURATION + ", "
                + ISSUE_DETAIL_SPECIAL_NEEDS_UNIT + " , "
                + ISSUE_DETAIL_SPECIAL_NEEDS_UNIT_AR + ", " + ISSUE_DETAIL_CHECKAPPDATE
                + " from " + ISSUE_DETAILS_TABLE
                + " WHERE " + ISSUE_DETAIL_ISSUE_ID + "="
                + type_issue + " AND " + ISSUE_DETAIL_LANGUAGE + "='" + lang
                + "';";

        ArrayList<IssuesDetails> newsList = new ArrayList<IssuesDetails>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            IssuesDetails news = cursorToissuedet(cursor);
            newsList.add(news);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public ArrayList<Notifications> getnotification(String lang) {
        String query = "select " + NOTIFICATION_ID + ", " + NOTIFICATION_NAME
                + ", " + NOTIFICATION_DETAILS + ", " + NOTIFICATION_date + ", "
                + NOTIFICATION_lang + " from " + NOTIFICATION_TABLE + " where "
                + NOTIFICATION_lang + "='" + lang + "';";

        ArrayList<Notifications> newsList = new ArrayList<Notifications>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            Notifications news = cursorToNotification(cursor);
            newsList.add(news);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public String[] getissue_detail_name(String type_issue, String lang) {
        String query = "select " + ISSUE_DETAIL_ID + ", " + ISSUE_DETAIL_NAME
                + ", " + ISSUE_DETAIL_ISSUE_ID + ", " + ISSUE_DETAIL_Modify
                + ", " + ISSUE_DETAIL_WAIT_DURATION + ", "
                + ISSUE_DETAIL_WAIT_UNIT + ", " + ISSUE_DETAIL_WAIT_UNIT_AR
                + ", " + ISSUE_DETAIL_LANGUAGE + ", "
                + ISSUE_DETAIL_SPECIAL_NEEDS_DURATION + ", "
                + ISSUE_DETAIL_SPECIAL_NEEDS_UNIT + ", "
                + ISSUE_DETAIL_SPECIAL_NEEDS_UNIT_AR + " from "
                + ISSUE_DETAILS_TABLE + " WHERE " + ISSUE_DETAIL_ISSUE_ID + "="
                + type_issue + " AND " + ISSUE_DETAIL_LANGUAGE + "='" + lang
                + "';";

        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        String[] newsList = new String[cursor.getCount()];
        int i = 0;
        while (!cursor.isAfterLast()) {
            // fill news info
            IssuesDetails news = cursorToissuedet(cursor);

            newsList[i] = news.getName();

            i++;
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public void updateProfileRow(String name, String value1, String value2) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put(PROFILE_Lanq, name);
        args.put(PROFILE_EMAIL, value2);

        db.update(PROFILE_TABLE, args, "1=1", null);
    }

    public void updateDate(String name) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(DATE_VALUE, name);
        db.update(DateStamp_TABLE, args, "1=1", null);
    }

    public void updateReportt(String name, int value1) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put(Reports_Off_Status, name);

        db.update(reports_off_TABLE, args, Report_Off_ID_Report + "=" + value1, null);
    }

    public String[] getissue_detail_id(String type_issue, String lang) {
        String query = "select " + ISSUE_DETAIL_ID + ", " + ISSUE_DETAIL_NAME
                + ", " + ISSUE_DETAIL_ISSUE_ID + ", " + ISSUE_DETAIL_Modify
                + ", " + ISSUE_DETAIL_LANGUAGE + " from " + ISSUE_DETAILS_TABLE
                + " WHERE " + ISSUE_DETAIL_ISSUE_ID + "=" + type_issue
                + " AND " + ISSUE_DETAIL_LANGUAGE + "='" + lang + "';";

        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        String[] newsList = new String[cursor.getCount()];
        int i = 0;
        while (!cursor.isAfterLast()) {
            // fill news info
            IssuesDetails news = cursorToissuedet(cursor);

            newsList[i] = Integer.toString(news.getId());

            i++;
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public String[] getissue_detail() {
        String query = "select " + ISSUE_DETAIL_ID + ", " + ISSUE_DETAIL_NAME
                + ", " + ISSUE_DETAIL_ISSUE_ID + ", " + ISSUE_DETAIL_Modify
                + ", " + ISSUE_DETAIL_LANGUAGE + " from " + ISSUE_DETAILS_TABLE;

        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        String[] newsList = new String[cursor.getCount()];
        int i = 0;
        while (!cursor.isAfterLast()) {
            // fill news info
            IssuesDetails news = cursorToissuedet(cursor);

            newsList[i] = Integer.toString(news.getId());

            i++;
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public ArrayList<ServicePro> getsp() {
        String query = "select " + SP_ID + ", " + SP_NAME + ", "
                + SP_ISFORTRANSFER + ", " + SP_LANQ + ", " + SP_Code + " from "
                + SP_TABLE + ";";

        ArrayList<ServicePro> newsList = new ArrayList<ServicePro>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            ServicePro news = cursorTosp(cursor);

            newsList.add(news);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public ArrayList<ServicePro> getsp_lang(String lang) {
        String query = "select " + SP_ID + ", " + SP_NAME + ", "
                + SP_ISFORTRANSFER + ", " + SP_LANQ + ", " + SP_Code
                + "  from " + SP_TABLE + " WHERE " + SP_LANQ + "='" + lang
                + "';";

        ArrayList<ServicePro> newsList = new ArrayList<ServicePro>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            ServicePro news = cursorTosp(cursor);

            newsList.add(news);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public ArrayList<Country> getcountry(String lang) {
        String query = "select " + COUNTRY_ID + ", " + COUNTRY_NAME + ", "
                + COUNTRY_Lang + " from " + COUNTRY_TABLE + " WHERE "
                + COUNTRY_Lang + "='" + lang + "';";

        ArrayList<Country> newsList = new ArrayList<Country>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            Country news = cursorToCountry(cursor);

            newsList.add(news);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public String getdatestamp() {
        String query = "select " + DATE_VALUE + " from " + DateStamp_TABLE
                + ";";

        String newsList = "";
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            String news = cursorToDate(cursor);

            newsList = news;

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public String[] getsp_name() {
        String query = "select " + SP_ID + ", " + SP_NAME + ", "
                + SP_ISFORTRANSFER + ", " + SP_LANQ + " from " + SP_TABLE + ";";

        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        String[] newsList = new String[cursor.getCount()];
        int i = 0;
        while (!cursor.isAfterLast()) {
            // fill news info
            ServicePro news = cursorTosp(cursor);

            newsList[i] = news.getName();
            i++;

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public String[] getsp_id() {
        String query = "select " + SP_ID + ", " + SP_NAME + ", "
                + SP_ISFORTRANSFER + ", " + SP_LANQ + " from " + SP_TABLE + ";";

        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        String[] newsList = new String[cursor.getCount()];
        int i = 0;
        while (!cursor.isAfterLast()) {
            // fill news info
            ServicePro news = cursorTosp(cursor);

            newsList[i] = Integer.toString(news.getId());
            i++;

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    // to read each returned row and store data in news object
    private Profile cursorToNews(Cursor cursor) {
        Profile news = new Profile();
        news.setId(cursor.getString(0));
        news.setnum(cursor.getString(1));
        news.setqatarID(cursor.getString(2));
        news.setemail(cursor.getString(3));
        news.setlang(cursor.getString(4));
        return news;
    }

    private IssuesDetails cursorToissuedet(Cursor cursor) {
        IssuesDetails news = new IssuesDetails();
        news.setId(cursor.getInt(0));
        news.setName(cursor.getString(1));
        news.setShowOnMap(cursor.getString(2));
        news.setmodifyNum(cursor.getString(3));
        news.setWaitDuration(cursor.getString(4));
        news.setWaitUnit(cursor.getString(5));
        news.setWaitUnitAr(cursor.getString(6));
        news.setlang(cursor.getString(7));
        news.setSpecialNeedDuration(cursor.getString(8));
        news.setSpecialneedUnit(cursor.getString(9));
        news.setSpecialneedUnitAr(cursor.getString(10));
        news.setCheckappondate(cursor.getString(11));
        return news;
    }

    private Notifications cursorToNotification(Cursor cursor) {
        Notifications news = new Notifications();
        news.setId(cursor.getInt(0));
        news.setName(cursor.getString(1));
        news.setdescription(cursor.getString(2));
        news.setDate(cursor.getString(3));
        news.setlang(cursor.getString(4));
        return news;
    }

    private ServicePro cursorTosp(Cursor cursor) {
        ServicePro news = new ServicePro();
        news.setId(cursor.getInt(0));
        news.setName(cursor.getString(1));
        news.setShowOnMap(cursor.getString(2));
        news.setLang(cursor.getString(3));
        news.setcode(cursor.getString(4));
        return news;
    }

    private Country cursorToCountry(Cursor cursor) {
        Country news = new Country();
        news.setId(cursor.getString(0));
        news.setName(cursor.getString(1));
        news.setlang(cursor.getString(2));
        return news;
    }

    private String cursorToDate(Cursor cursor) {
        String news = new String();
        news = cursor.getString(0);

        return news;
    }

    private Event cursorToIssue_type(Cursor cursor) {
        Event news = new Event();
        news.setId(cursor.getInt(0));
        news.setName(cursor.getString(1));
        news.setShowMap(cursor.getString(2));
        news.setdescription(cursor.getString(3));
        news.setServiceProviderTransfer(cursor.getString(6));
        news.setThumbnailUrlDay(cursor.getString(7));
        news.setThumbnailUrlNight(cursor.getString(8));
        //news.setimgDay(cursor.getBlob(7));
        //news.setimgNight(cursor.getBlob(8));
        return news;
    }

    private Area cursorToArea(Cursor cursor) {
        Area area = new Area();
        area.setId(cursor.getInt(0));
        area.setNameAr(cursor.getString(1));
        area.setNameEn(cursor.getString(2));

        return area;
    }

    private SubArea cursorToSubArea(Cursor cursor) {
        SubArea subarea = new SubArea();
        subarea.setId(cursor.getInt(0));
        subarea.setNameAr(cursor.getString(1));
        subarea.setNameEn(cursor.getString(2));
        subarea.setParentId(cursor.getInt(0));
        return subarea;
    }

    private Mail_OFF cursorToMail_OFF(Cursor cursor) {
        Mail_OFF news = new Mail_OFF();
        news.setId_rep(cursor.getInt(0));
        news.setId(cursor.getString(1));
        news.setnum(cursor.getString(2));
        news.setqatarID(cursor.getString(3));
        news.setemail(cursor.getString(4));
        news.setlang(cursor.getString(5));
        news.setloc(cursor.getString(7));
        news.setcomm(cursor.getString(6));
        news.setdate(cursor.getString(8));
        news.setdate_Aff(cursor.getString(9));
        news.setissuename(cursor.getString(10));
        news.setstatus(cursor.getString(11));
        news.setissueid(cursor.getString(12));
        news.setspid(cursor.getString(13));
        news.setlocx(cursor.getString(14));
        news.setlocy(cursor.getString(15));
        news.setcountid(cursor.getString(16));
        news.setaffcqatar(cursor.getString(17));
        news.setspname(cursor.getString(18));
        return news;
    }

    public boolean deleteProfile(String newsKey) {
        return mDb.delete(PROFILE_TABLE, PROFILE_REG_ID + "=" + newsKey, null) > 0;
    }

    public boolean deleteAllProfile() {
        return mDb.delete(PROFILE_TABLE, null, null) > 0;
    }

    public boolean deleteCountry(String newsKey) {
        return mDb.delete(COUNTRY_TABLE, COUNTRY_ID + "='" + newsKey + "'",
                null) > 0;
    }

    public boolean deleteAllCountry(String newsKey) {
        return mDb.delete(COUNTRY_TABLE, null, null) > 0;
    }

    public boolean deleteAllNotifications(String newsKey) {
        return mDb.delete(NOTIFICATION_TABLE, null, null) > 0;
    }

    public boolean deleteDate(String newsKey) {
        return mDb.delete(DateStamp_TABLE, DATE_VALUE + "='" + newsKey + "'",
                null) > 0;
    }

    public boolean deleteMail_offf(int newsKey) {
        return mDb.delete(reports_off_TABLE, Report_Off_ID_Report + "="
                + newsKey + " AND " + Reports_Off_Status + "='Failed'", null) > 0;
    }

    public boolean deleteMail(int newsKey) {
        return mDb.delete(reports_TABLE, Report_Off_REG_ID + "='" + newsKey
                + "'", null) > 0;
    }

    public boolean deleteIssue_type(int newsKey) {
        return mDb
                .delete(ISSUE_TYPE_TABLE, ISSUE_TYPE_ID + "=" + newsKey, null) > 0;
    }

    public boolean deleteAllIssue_type(int newsKey) {
        return mDb.delete(ISSUE_TYPE_TABLE, null, null) > 0;
    }

    public boolean deleteIssue_detail(int newsKey) {
        return mDb.delete(ISSUE_DETAILS_TABLE, ISSUE_DETAIL_ID + "=" + newsKey,
                null) > 0;
    }

    public boolean deleteAllIssue_detail(int newsKey) {
        return mDb.delete(ISSUE_DETAILS_TABLE, null, null) > 0;
    }

    public boolean deleteNotification(int newsKey) {
        return mDb.delete(NOTIFICATION_TABLE, NOTIFICATION_ID + "=" + newsKey,
                null) > 0;
    }

    public boolean deletesp(int newsKey) {
        return mDb.delete(SP_TABLE, SP_ID + "=" + newsKey, null) > 0;
    }

    public boolean deleteAllsp(int newsKey) {
        return mDb.delete(SP_TABLE, null, null) > 0;
    }






    //hsen 31-5-2018
    public QosTest getTopQosTest(boolean min) {

        String selectQuery = "";
        if (min)
            selectQuery = "SELECT * FROM " + TABLE_QOS_TESTS + " where testSource=" + MyApplication.TYPE_NOT_CONNECTED+" ORDER BY id DESC LIMIT 1";
        else
            selectQuery = "SELECT * FROM " + TABLE_QOS_TESTS + " where testSource=" + MyApplication.TYPE_NOT_CONNECTED+" ORDER BY id ASC LIMIT 1";


        Cursor cursor = mDb.rawQuery(selectQuery, null);
        QosTest p = new QosTest();

        if (cursor.moveToFirst()) {

            try {
                p.setId(cursor.getInt(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setTestSource(cursor.getInt(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setNotificationId(cursor.getInt(2));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setUserId(cursor.getString(3));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setDeviceId(cursor.getString(4));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setServiceProvider(cursor.getString(5));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setLocationX(cursor.getString(6));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setLocationY(cursor.getString(7));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setIp(cursor.getString(8));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setLocationIPAddress(cursor.getString(9));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setTimeStamp(cursor.getString(10));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setTestType(cursor.getString(11));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setCallDisconnectionReason(cursor.getString(12));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setTestDateTime(cursor.getString(13));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setIsIncident(cursor.getString(14));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setSignalStrength(cursor.getString(15));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setConnectionType(cursor.getString(16));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setCallDuration(cursor.getString(17));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setTestTriggerType(cursor.getString(18));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setTriggerStartDate(cursor.getString(19));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setValidationEndDate(cursor.getString(20));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setResultId(cursor.getString(21));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setStatusId(cursor.getString(22));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setDeviceModel(cursor.getString(23));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setDeviceType(cursor.getString(24));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setMobileNumber(cursor.getString(25));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setAdHocRequestId(cursor.getString(26));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setLocality(cursor.getString(27));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setSubLocality(cursor.getString(28));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setRoute(cursor.getString(29));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setProvince(cursor.getString(30));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setSignalStrengthFlag(Boolean.parseBoolean(cursor.getString(31)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setSpectrumSignalStrength(cursor.getString(32));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setSignalQuality(cursor.getString(33));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        cursor.close();
        mDb.close();

        return p;
    }

    public ArrayList<QosTest> getAllOfflineQosTests() {

        String query = "select * from " + TABLE_QOS_TESTS + " where testSource=" + MyApplication.TYPE_NOT_CONNECTED;

        ArrayList<QosTest> newsList = new ArrayList<>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            QosTest news = cursorToQos(cursor);
            newsList.add(news);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public int getMobileQosTestsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_QOS_TESTS+ " where testSource=" + MyApplication.TYPE_MOBILE;
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public ArrayList<QosTest> getAlPendingQosTests() {

        String query = "select * from " + TABLE_QOS_TESTS + " where testSource!=" + MyApplication.TYPE_NOT_CONNECTED;

        ArrayList<QosTest> newsList = new ArrayList<>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            QosTest news = cursorToQos(cursor);
            newsList.add(news);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    public ArrayList<QosTest> getAllMobileQosTests() {

       // String query = "select * from " + TABLE_QOS_TESTS + " where testSource=" + MyApplication.TYPE_MOBILE;
          String query = "SELECT  * FROM " + TABLE_QOS_TESTS;
        ArrayList<QosTest> newsList = new ArrayList<>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            QosTest news = cursorToQos(cursor);
            newsList.add(news);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }

    private String getDateFormatted(Date date){

        String formattedDate = "";
        //SimpleDateFormat output = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat output = null;
        try {
            output = new SimpleDateFormat("HH:mm:ss MM-dd-YYYY", Locale.ENGLISH);
        } catch (Exception e) {
            e.printStackTrace();
            output = new SimpleDateFormat("HH:mm:ss MM-dd-yyyy", Locale.ENGLISH);
        }
        formattedDate = output.format(date);
        return  formattedDate;
    }

    public ArrayList<QosTest> getAllInRangeQosTests(Date date) {

        Date before = new Date(date.getTime() - 59000L);
        Date after =  new Date(date.getTime() + 59000L);

        String query = "select * from " + TABLE_QOS_TESTS + " where testSource=" + MyApplication.TYPE_NOT_CONNECTED+ " and testDateTime BETWEEN '"+ getDateFormatted(before) + "' AND '"+ getDateFormatted(after)+"'";

        ArrayList<QosTest> newsList = new ArrayList<>();
        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill news info
            QosTest news = cursorToQos(cursor);
            newsList.add(news);

            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return newsList;
    }


    //hsen 31-5-2018
    public int deleteOfflineQosTests() {

        return mDb.delete(TABLE_QOS_TESTS, "testSource="+MyApplication.TYPE_NOT_CONNECTED, null);
    }

    //hsen 31-5-2018
    public int deleteQosTest(long id) {
        return mDb.delete(TABLE_QOS_TESTS, "id="+id, null);
    }

    private QosTest cursorToQos(Cursor cursor) {
        QosTest p = new QosTest();
        try {
            p.setId(cursor.getInt(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setTestSource(cursor.getInt(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setNotificationId(cursor.getInt(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setUserId(cursor.getString(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setDeviceId(cursor.getString(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setServiceProvider(cursor.getString(5));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setLocationX(cursor.getString(6));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setLocationY(cursor.getString(7));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setIp(cursor.getString(8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setLocationIPAddress(cursor.getString(9));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setTimeStamp(cursor.getString(10));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setTestType(cursor.getString(11));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setCallDisconnectionReason(cursor.getString(12));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setTestDateTime(cursor.getString(13));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setIsIncident(cursor.getString(14));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setSignalStrength(cursor.getString(15));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setConnectionType(cursor.getString(16));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setCallDuration(cursor.getString(17));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setTestTriggerType(cursor.getString(18));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setTriggerStartDate(cursor.getString(19));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setValidationEndDate(cursor.getString(20));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setResultId(cursor.getString(21));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setStatusId(cursor.getString(22));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setDeviceModel(cursor.getString(23));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setDeviceType(cursor.getString(24));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setMobileNumber(cursor.getString(25));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setAdHocRequestId(cursor.getString(26));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setLocality(cursor.getString(27));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setSubLocality(cursor.getString(28));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setRoute(cursor.getString(29));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setProvince(cursor.getString(30));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setSignalStrengthFlag(Boolean.parseBoolean(cursor.getString(31)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setSpectrumSignalStrength(cursor.getString(32));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.setSignalQuality(cursor.getString(33));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    //hsen 31-5-2018
    public QosTest getQosTest(int id) {

        String selectQuery = "SELECT * FROM " + TABLE_QOS_TESTS + " where id=" + id;

        Cursor cursor = mDb.rawQuery(selectQuery, null);
        QosTest p = new QosTest();

        if (cursor.moveToFirst()) {

            try {
                p.setId(cursor.getInt(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setTestSource(cursor.getInt(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setNotificationId(cursor.getInt(2));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setUserId(cursor.getString(3));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setDeviceId(cursor.getString(4));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setServiceProvider(cursor.getString(5));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setLocationX(cursor.getString(6));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setLocationY(cursor.getString(7));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setIp(cursor.getString(8));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setLocationIPAddress(cursor.getString(9));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setTimeStamp(cursor.getString(10));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setTestType(cursor.getString(11));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setCallDisconnectionReason(cursor.getString(12));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setTestDateTime(cursor.getString(13));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setIsIncident(cursor.getString(14));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setSignalStrength(cursor.getString(15));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setConnectionType(cursor.getString(16));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setCallDuration(cursor.getString(17));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setTestTriggerType(cursor.getString(18));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setTriggerStartDate(cursor.getString(19));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setValidationEndDate(cursor.getString(20));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setResultId(cursor.getString(21));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setStatusId(cursor.getString(22));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setDeviceModel(cursor.getString(23));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setDeviceType(cursor.getString(24));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setMobileNumber(cursor.getString(25));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setAdHocRequestId(cursor.getString(26));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setLocality(cursor.getString(27));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setSubLocality(cursor.getString(28));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setRoute(cursor.getString(29));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setProvince(cursor.getString(30));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setSignalStrengthFlag(Boolean.parseBoolean(cursor.getString(31)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setSpectrumSignalStrength(cursor.getString(32));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.setSignalQuality(cursor.getString(33));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        cursor.close();
        mDb.close();

        return p;
    }

    //hsen 31-5-2018
    public long insertQosTest(QosTest qosTest, boolean offline) {

        long id;

        ContentValues values = new ContentValues();
        values.put("userId", qosTest.getUserId());
        values.put("testSource", qosTest.getTestSource());
        values.put("notificationId", qosTest.getNotificationId());
        values.put("deviceId", qosTest.getDeviceId());
        values.put("serviceProvider", qosTest.getServiceProvider());
        values.put("locationX", qosTest.getLocationX());
        values.put("locationY", qosTest.getLocationY());
        values.put("ip", qosTest.getIp());
        values.put("locationIPAddress", qosTest.getLocationIPAddress());
        values.put("timeStamp", qosTest.getTimeStamp());
        values.put("testType", qosTest.getTestType());
        values.put("callDisconnectionReason", qosTest.getCallDisconnectionReason());
        values.put("testDateTime", qosTest.getTestDateTime());
        values.put("isIncident", qosTest.getIsIncident());
        values.put("signalStrength", qosTest.getSignalStrength());
        values.put("connectionType", qosTest.getConnectionType());
        values.put("callDuration", qosTest.getCallDuration());
        values.put("testTriggerType", qosTest.getTestTriggerType());
        values.put("triggerStartDate", qosTest.getTriggerStartDate());
        values.put("ValidationEndDate", qosTest.getValidationEndDate());
        values.put("resultId", qosTest.getResultId());
        values.put("statusId", qosTest.getStatusId());
        values.put("deviceModel", qosTest.getDeviceModel());
        values.put("deviceType", qosTest.getDeviceType());
        values.put("mobileNumber", qosTest.getMobileNumber());
        values.put("adHocRequestId", qosTest.getAdHocRequestId());
        values.put("locality", qosTest.getLocality());
        values.put("subLocality", qosTest.getSubLocality());
        values.put("route", qosTest.getRoute());
        values.put("province", qosTest.getProvince());
        values.put("isSignalStrength", String.valueOf(qosTest.isSignalStrength()));
        values.put("SpectrumSignalStrength", qosTest.getSpectrumSignalStrength());
        values.put("SignalQuality", qosTest.getSignalQuality());

        id = mDb.insert(TABLE_QOS_TESTS, null, values);
        if (offline)
            Log.wtf("Offline entry", "inserted");

        return id;
    }

    public void insertAndPost(Context context, QosTest qosTest){

        long id = insertQosTest(qosTest, false);

        Log.wtf("!!!! pre counttt", "is "+this.getQosTestsCount());
        Log.d("!!!! pre counttt", "is "+this.getQosTestsCount());

        QosTest temp = getQosTest((int)id);

        //<editor-fold desc="to be restored">
        if (Actions.isWifiAvailable(context)) {

            //setGeoLocation(context, temp);
            Log.wtf("insertAndPost", "fe wifi");
            Log.d("insertAndPost", "fe wifi");

            PostQosTest postQosTest = new PostQosTest(context, temp, id);
            postQosTest.executeOnExecutor(MyApplication.threadPoolExecutor);
        } else {

            Log.wtf("insertAndPost", "no wifi");
            Log.d("insertAndPost", "no wifi");
            /*SharedPreference settings = new SharedPreference();
            ArrayList<QosTest> array = settings.getMobileDataQosTests(context);
            if (array == null) {
                Log.wtf("null", "array");
                Log.d("null", "array");
                array = new ArrayList<QosTest>();
                array.add(qosTest);
                settings.saveMobileDataQosTests(context, array);
            } else
                settings.addMobileDataQosTest(context, qosTest);*/

            TCTDbAdapter database = new TCTDbAdapter(context);
            database.open();
            temp.setTestSource(MyApplication.TYPE_MOBILE);
            database.insertQosTest(temp, true);


        }
        //</editor-fold>

    }


    private void setGeoLocation(Context context, QosTest qosTest){

        if (Looper.myLooper() == null)  {
            Looper.prepare();
        }
        if (qosTest.getLocality().length() == 0 && qosTest.getLocationX().length() > 0){

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

                Log.wtf("countryName", "countryName1 is: " + countryName);
                Log.d("countryName", "countryName1 is: " + countryName);

                Log.wtf("locality", "locality1 is: " + locality);
                Log.d("locality", "locality1 is: " + locality);

                Log.wtf("subLocality", "subLocality1 is: " + subLocality);
                Log.d("subLocality", "subLocality1 is: " + subLocality);

                Log.wtf("province", "province1 is: " + province);
                Log.d("province", "province1 is: " + province);

                Log.wtf("route", "route1 is: " + route);
                Log.d("route", "route1 is: " + route);


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
        //Looper.myLooper().quit();
    }

    //hsen 31-5-2018
    public int getQosTestsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_QOS_TESTS;
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    private class PostQosTest extends AsyncTask<Void, Void, String> {

        String params;
        String res;
        Profile profile;
        Context context;
        QosTest qosTest;
        long id;
        TCTDbAdapter database;

        public PostQosTest(Context context, QosTest qosTest, long id) {

            this.context = context;
            this.qosTest = qosTest;
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            database = new TCTDbAdapter(context);
            database.open();
            ArrayList<Profile> arr = database.getAllProfiles();

            if(arr.size() > 0){
                profile = arr.get(0);
            }
            database.close();

            setGeoLocation(context, qosTest);
        }

        @Override
        protected String doInBackground(Void... a) {
            // URLEncoder.encode(searchText, "utf-8");
            //Connection.disableSSLCertificateChecking();
            Log.wtf("Do", "IN BACKGROUND adapter");
            Log.d("Do", "IN BACKGROUND adapter");

            String url = MyApplication.link + MyApplication.post + "PostQoSTest?";

            if (Actions.isWifiAvailable(context)) {
                Log.wtf("BACKGROUND adapter", "fe wifi");

                //<editor-fold desc="old way">
                try {

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd", Locale.ENGLISH);
                    String formattedDate = df.format(c.getTime());
                    //+ formattedDate

                    params = "MobileNumber="
                            + URLEncoder.encode(qosTest.getMobileNumber(), "utf-8")
                            + "&"
                            + "ServiceProvider="
                            + URLEncoder.encode(qosTest.getServiceProvider(), "utf-8")
                            + "&"
                            + "IPAddress="
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
                            //testing + URLEncoder.encode(qosTest.getTestDateTime(), "utf-8")
                            //+ qosTest.getTestDateTime()
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

                    Log.wtf("PostQoSTest","url : " + url);
                    Log.wtf("PostQoSTest","params : " + params);

                    url = url + params;

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    res = "Error";

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

                Log.wtf("doInBackground", "no wifi");
                Log.d("doInBackground", "no wifi");
                /*SharedPreference settings = new SharedPreference();
                ArrayList<QosTest> array = settings.getMobileDataQosTests(context);
                if (array == null) {
                    Log.wtf("null", "array");
                    array = new ArrayList<QosTest>();
                    array.add(qosTest);
                    settings.saveMobileDataQosTests(context, array);
                } else
                    settings.addMobileDataQosTest(context, qosTest);*/

                TCTDbAdapter database = new TCTDbAdapter(context);
                database.open();
                qosTest.setTestSource(MyApplication.TYPE_MOBILE);
                database.insertQosTest(qosTest, true);

                return "";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            //Log.wtf("Parameters", params);

            Actions.PostLog(context,MyApplication.TAG, "Parameters "+params);
            Actions.PostLog(context,MyApplication.TAG, "Post Execute Task "+result);
            Actions.PostLog(context,"======================================", "======================================");

            if (result.equalsIgnoreCase("Success: true")) {
                Log.wtf("*=*= Success", " sent");
                Log.d("*=*= Success", " sent");
                Actions.PostLog(context,MyApplication.TAG, "TCTAdapter post success");
                database.open();
                database.deleteQosTest(id);
                database.close();

                database.open();
                int count = database.getQosTestsCount();

                //database.deleteOfflineQosTests();
                database.close();
                Log.wtf("!!!!post count", "is "+count);
                Log.d("!!!!post count", "is "+count);
            } else {
                Actions.PostLog(context,MyApplication.TAG, "TCTAdapter post failed"+id);
                Log.wtf("*=*= Failure", " not sent");
                Log.d("*=*= Failure", " not sent");
                Actions.UpdateNotificationStatus(qosTest.getNotificationId());
            }

            Intent bc = new Intent("com.ids.ict.classes.InternetReceiver");
            context.sendBroadcast(bc);

        }
    }




    // inner class declaration to help in working with the db
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            // call to parent constructor, here we set the database name and
            // version
            // but not actually created it yet
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // called when db is created for the first time
        @Override
        public void onCreate(SQLiteDatabase db) {

            StringBuilder qostests = new StringBuilder();
            qostests.append("CREATE TABLE " + TABLE_QOS_TESTS);
            qostests.append("(id INTEGER PRIMARY KEY AUTOINCREMENT, ");
            qostests.append("testSource INTEGER, ");
            qostests.append("notificationId INTEGER, ");
            qostests.append("userId TEXT, ");
            qostests.append("deviceId TEXT, ");
            qostests.append("serviceProvider TEXT, ");
            qostests.append("locationX TEXT, ");
            qostests.append("locationY TEXT, ");
            qostests.append("ip TEXT, ");
            qostests.append("locationIPAddress TEXT, ");
            qostests.append("timeStamp TEXT, ");
            qostests.append("testType TEXT, ");
            qostests.append("callDisconnectionReason TEXT, ");
            qostests.append("testDateTime TEXT, ");
            qostests.append("isIncident TEXT, ");
            qostests.append("signalStrength TEXT, ");
            qostests.append("connectionType TEXT, ");
            qostests.append("callDuration TEXT, ");
            qostests.append("testTriggerType TEXT, ");
            qostests.append("triggerStartDate TEXT, ");
            qostests.append("ValidationEndDate TEXT, ");
            qostests.append("resultId TEXT, ");
            qostests.append("statusId TEXT, ");
            qostests.append("deviceModel TEXT, ");
            qostests.append("deviceType TEXT, ");
            qostests.append("mobileNumber TEXT, ");
            qostests.append("adHocRequestId TEXT, ");
            qostests.append("locality TEXT, ");
            qostests.append("subLocality TEXT, ");
            qostests.append("route TEXT, ");
            qostests.append("province TEXT, ");
            qostests.append("isSignalStrength TEXT, ");
            qostests.append("SpectrumSignalStrength TEXT, ");
            qostests.append("SignalQuality TEXT)");
            db.execSQL(qostests.toString());


            db.execSQL(PROFILE_TABLE_CREATE);
            db.execSQL(Reports_OFF_TABLE_CREATE);
            db.execSQL(Reports_TABLE_CREATE);
            db.execSQL(ISSUE_TYPE_TABLE_CREATE);
            db.execSQL(ISSUE_DETAIL_TABLE_CREATE);
            db.execSQL(SP_TABLE_CREATE);
            db.execSQL(COUNTRY_TABLE_CREATE);
            db.execSQL(NOTIFICATION_TABLE_CREATE);
            db.execSQL(DateStamp_TABLE_CREATE);
            db.execSQL(AREA_CREATE_TABLE);
            db.execSQL(SUB_AREA_CREATE_TABLE);
            //db.execSQL("PRAGMA foreign_keys = ON;");

        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            // Enable foreign key constraints
            // must be called every time the database is opened
            // constraint enforcing is set to off automatically after
            // database is closed
            //db.execSQL("PRAGMA foreign_keys = ON;");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Not used, but you could upgrade the database with ALTER
            // Scripts
            newVersion = DATABASE_VERSION;

            if (newVersion > oldVersion) {

                Log.wtf("UPDATING", "DATABASE");

                db.execSQL("DROP TABLE IF EXISTS "+TABLE_QOS_TESTS);


                /*db.execSQL("DROP TABLE IF EXISTS "+PROFILE_TABLE);
                db.execSQL("DROP TABLE IF EXISTS "+reports_off_TABLE);
                db.execSQL("DROP TABLE IF EXISTS "+reports_TABLE);
                db.execSQL("DROP TABLE IF EXISTS "+ISSUE_TYPE_TABLE);
                db.execSQL("DROP TABLE IF EXISTS "+ISSUE_DETAILS_TABLE);
                db.execSQL("DROP TABLE IF EXISTS "+NOTIFICATION_TABLE);
                db.execSQL("DROP TABLE IF EXISTS "+SP_TABLE);
                db.execSQL("DROP TABLE IF EXISTS "+COUNTRY_TABLE);
                db.execSQL("DROP TABLE IF EXISTS "+DateStamp_TABLE);
                db.execSQL("DROP TABLE IF EXISTS "+AREA_TABLE);
                db.execSQL("DROP TABLE IF EXISTS "+SUB_AREA_TABLE);*/


                StringBuilder qostests = new StringBuilder();
                qostests.append("CREATE TABLE " + TABLE_QOS_TESTS);
                qostests.append("(id INTEGER PRIMARY KEY AUTOINCREMENT, ");
                qostests.append("testSource INTEGER, ");
                qostests.append("notificationId INTEGER, ");
                qostests.append("userId TEXT, ");
                qostests.append("deviceId TEXT, ");
                qostests.append("serviceProvider TEXT, ");
                qostests.append("locationX TEXT, ");
                qostests.append("locationY TEXT, ");
                qostests.append("ip TEXT, ");
                qostests.append("locationIPAddress TEXT, ");
                qostests.append("timeStamp TEXT, ");
                qostests.append("testType TEXT, ");
                qostests.append("callDisconnectionReason TEXT, ");
                qostests.append("testDateTime TEXT, ");
                qostests.append("isIncident TEXT, ");
                qostests.append("signalStrength TEXT, ");
                qostests.append("connectionType TEXT, ");
                qostests.append("callDuration TEXT, ");
                qostests.append("testTriggerType TEXT, ");
                qostests.append("triggerStartDate TEXT, ");
                qostests.append("ValidationEndDate TEXT, ");
                qostests.append("resultId TEXT, ");
                qostests.append("statusId TEXT, ");
                qostests.append("deviceModel TEXT, ");
                qostests.append("deviceType TEXT, ");
                qostests.append("mobileNumber TEXT, ");
                qostests.append("adHocRequestId TEXT, ");
                qostests.append("locality TEXT, ");
                qostests.append("subLocality TEXT, ");
                qostests.append("route TEXT, ");
                qostests.append("province TEXT, ");
                qostests.append("isSignalStrength TEXT, ");
                qostests.append("SpectrumSignalStrength TEXT, ");
                qostests.append("SignalQuality TEXT)");
                db.execSQL(qostests.toString());


                /*db.execSQL(PROFILE_TABLE_CREATE);
                db.execSQL(Reports_OFF_TABLE_CREATE);
                db.execSQL(Reports_TABLE_CREATE);
                db.execSQL(ISSUE_TYPE_TABLE_CREATE);
                db.execSQL(ISSUE_DETAIL_TABLE_CREATE);
                db.execSQL(SP_TABLE_CREATE);
                db.execSQL(COUNTRY_TABLE_CREATE);
                db.execSQL(NOTIFICATION_TABLE_CREATE);
                db.execSQL(DateStamp_TABLE_CREATE);
                db.execSQL(AREA_CREATE_TABLE);
                db.execSQL(SUB_AREA_CREATE_TABLE);
                db.execSQL("PRAGMA foreign_keys = ON;");*/
            }
        }

    }
}
