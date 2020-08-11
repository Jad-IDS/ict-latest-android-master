package com.ids.ict.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ids.ict.classes.Country;
import com.ids.ict.classes.IssuesDetails;
import com.ids.ict.classes.Mail_OFF;
import com.ids.ict.classes.Profile;
import com.ids.ict.classes.ServicePro;
import com.ids.ict.activities.Event;
import com.ids.ict.activities.Notifications;
import com.ids.ict.classes.Area;
import com.ids.ict.classes.SubArea;

import java.util.ArrayList;

import static com.ids.ict.TCTDbAdapter.Reports_Off_LOCATION;
import static com.ids.ict.TCTDbAdapter.Reports_Off_Lanq;

/**
 * Created by user on 12/23/2016.
 */

public class SecondaryDb {
    private static final String DATABASE_NAME = "ICTDB";// database name

    private static final int DATABASE_VERSION = 1;// database version
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
    public static final String Reports_Off_AFFMOBILE = "affectedNumber";
    public static final String Reports_Off_QATARID = "qatar_id";
    public static final String Reports_Off_EMAIL = "email";
    public static final String Reports_Off_ISSUEDETAIlID = "issueDetailId";
    public static final String Reports_Off_SERVICEPROVIDERID = "issueDetailId";
    public static final String Reports_Off_CREAIONDATE = "issueDetailId";
    public static final String Reports_Off_SENDINGDATE= "issueDetailId";
    public static final String Reports_Off_MOBILEDEVICE= "issueDetailId";
    public static final String Reports_Off_COMMENTS = "comments";
    public static final String Reports_Off_LOCATIONX = "locationx";
    public static final String Reports_Off_LOCATIONY = "locationy";
    public static final String Reports_Off_COUNTRYID = "countryId";
    public static final String Reports_Off_AFFECTEDQATARID = "affectedQatarId";
    public static final String Reports_Off_SERVICEPROVIDERCODE = "serviceProviderCode";
    public static final String Reports_Off_ADVERTISINGID = "advertisingID";

    public static final String Reports_Off_TOKENNUMBER = "tokenNumber";
    public static final String Reports_Off_CHECKSUM = "checksum";
    public static final String Reports_Off_TYPEID = "typeId";
    public static final String Reports_Off_DIDCOMPLAINTOSPID = "didyoucomplaintospId";
    public static final String Reports_Off_LONGWAITTIME = "logwaittime";
    public static final String Reports_Off_SPECIALCOMPLAINTREFNUMBER = "spcomplaintrefnumber";
    public static final String Reports_Off_SPECIALCOMPLAINTDATE = "spcomplaintdate";
    public static final String Reports_Off_CALLEDFROM = "calledfromnumber";
    public static final String Reports_Off_CALLDATE= "calledfromnumber";
    public static final String Reports_Off_STATUSID= "calledfromnumber";
    public static final String Reports_Off_ISINDIVIDUAL= "calledfromnumber";

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
    public SecondaryDb(Context ctx) {
        this.mCtx = ctx;
    }

    // when called the db is opened/created
    public SecondaryDb open() throws android.database.SQLException {
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
                + ISSUE_DETAIL_SPECIAL_NEEDS_UNIT_AR + ", " + ISSUE_DETAIL_CHECKAPPDATE + " from "
                + ISSUE_DETAILS_TABLE + " WHERE " + ISSUE_DETAIL_ISSUE_ID + "="
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

        db.update(reports_off_TABLE, args, Report_Off_ID_Report + "=" + value1,
                null);
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

    // to read each returned row and store data in events object

    // get the name of the category that the news belongs to

    public boolean deleteProfile(String newsKey) {
        return mDb.delete(PROFILE_TABLE, PROFILE_REG_ID + "=" + newsKey, null) > 0;
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
            db.execSQL("PRAGMA foreign_keys = ON;");

        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            // Enable foreign key constraints
            // must be called every time the database is opened
            // constraint enforcing is set to off automatically after
            // database is closed
            db.execSQL("PRAGMA foreign_keys = ON;");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Not used, but you could upgrade the database with ALTER
            // Scripts
            newVersion = DATABASE_VERSION;

            if (newVersion > oldVersion) {


            }
        }

    }
}
