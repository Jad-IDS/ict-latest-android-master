package org.shipp.util;

import java.util.ArrayList;
import java.util.List;

import org.shipp.model.Menu;

import com.ids.ict.classes.InitialMapSettings;
import com.ids.ict.classes.IssueNumber;
import com.ids.ict.classes.IssueTypes;
import com.ids.ict.classes.MapLabel;
import com.ids.ict.classes.MapPinLocation;
import com.ids.ict.classes.MobileConfiguration;
import com.ids.ict.classes.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class to access menu database
 *
 * @author Leonardo Salles
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Shipp";
    private static final String TABLE_MENU = "menu";
    private static final String TABLE_SERVICE_PROVIDER = "serviceprovider";
    private static final String TABLE_ISSUE_TYPES = "issuetypes";
    private static final String TABLE_PINS = "pins";
    private static final String TABLE_MAP_SETTINGS = "mapsettings";
    private static final String TABLE_MAP_LABEL = "maplabel";
    private static final String TABLE_MOBILE_CONFIGURATION = "mobileconfig";
    private static final String TABLE_ISSUE_NUMBER = "issuenumber";

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String ICON = "icon";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder tableMenu = new StringBuilder();
        tableMenu.append("CREATE TABLE menu ");
        tableMenu.append("(id INTEGER PRIMARY KEY, ");
        tableMenu.append("icon NUMBER, ");
        tableMenu.append("title TEXT, ");
        tableMenu.append("description TEXT)");

        db.execSQL(tableMenu.toString());

        this.addDefaultMenu(MenuConfigAdapter.getMenuDefault(), db);

        //create service provider table
        StringBuilder providerTable = new StringBuilder();
        providerTable.append("CREATE TABLE " + TABLE_SERVICE_PROVIDER);
        providerTable.append("(id INTEGER PRIMARY KEY, ");
        providerTable.append("name TEXT)");


        //create issue types table
        StringBuilder issutable = new StringBuilder();
        issutable.append("CREATE TABLE " + TABLE_ISSUE_TYPES);
        issutable.append("(id INTEGER PRIMARY KEY, ");
        issutable.append("name TEXT, ");
        issutable.append("logo TEXT)");

        //create maplabel table


        StringBuilder maplabel = new StringBuilder();
        maplabel.append("CREATE TABLE " + TABLE_MAP_LABEL);
        maplabel.append("(imagename TEXT, ");
        maplabel.append("label TEXT, ");
        maplabel.append("Number TEXT)");


        //create pins table
        StringBuilder pinsTable = new StringBuilder();
        pinsTable.append("CREATE TABLE " + TABLE_PINS);
        pinsTable.append("(X TEXT, ");
        pinsTable.append("Y TEXT, ");
        pinsTable.append("Locality TEXT, ");
        pinsTable.append("IssueType TEXT, ");
        pinsTable.append("Number TEXT, ");
        pinsTable.append("provider TEXT, ");
        pinsTable.append("view TEXT, ");
        pinsTable.append("PinIcon TEXT, ");
        pinsTable.append("issueId TEXT, ");
        pinsTable.append("subLocality TEXT, ");
        pinsTable.append("subLocalityAr TEXT, ");
        pinsTable.append("IssueTypeAr TEXT)");

        //create table map settings

        StringBuilder mapsettings = new StringBuilder();
        mapsettings.append("CREATE TABLE " + TABLE_MAP_SETTINGS);
        mapsettings.append("(X TEXT, ");
        mapsettings.append("Y TEXT, ");
        mapsettings.append("zoom TEXT, ");
        mapsettings.append("showservice TEXT)");

        //create table mobile config

        StringBuilder config = new StringBuilder();
        config.append("CREATE TABLE " + TABLE_MOBILE_CONFIGURATION);
        config.append("(id INTEGER PRIMARY KEY, ");
        config.append("key TEXT, ");
        config.append("value TEXT)");

        //create issue number table
        StringBuilder isseunumber = new StringBuilder();
        isseunumber.append("CREATE TABLE " + TABLE_ISSUE_NUMBER);
        isseunumber.append("(imagename TEXT, ");
        isseunumber.append("label TEXT, ");
        isseunumber.append("view TEXT, ");
        isseunumber.append("number TEXT)");


        db.execSQL(providerTable.toString());
        db.execSQL(issutable.toString());
        db.execSQL(pinsTable.toString());
        db.execSQL(mapsettings.toString());
        db.execSQL(maplabel.toString());
        db.execSQL(config.toString());
        db.execSQL(isseunumber.toString());
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 2:
                    db.execSQL("DROP TABLE IF EXISTS menu");

                    //create service provider table
                    StringBuilder providerTable = new StringBuilder();
                    providerTable.append("CREATE TABLE " + TABLE_SERVICE_PROVIDER);
                    providerTable.append("(id INTEGER PRIMARY KEY, ");
                    providerTable.append("name TEXT)");

                    StringBuilder maplabel = new StringBuilder();
                    maplabel.append("CREATE TABLE " + TABLE_MAP_LABEL);
                    maplabel.append("(imagename TEXT, ");
                    maplabel.append("label TEXT, ");
                    maplabel.append("Number TEXT)");

                    //create issue types table
                    StringBuilder issutable = new StringBuilder();
                    issutable.append("CREATE TABLE " + TABLE_ISSUE_TYPES);
                    issutable.append("(id INTEGER PRIMARY KEY, ");
                    issutable.append("name TEXT, ");
                    issutable.append("logo TEXT)");


                    //create pins table
                    StringBuilder pinsTable = new StringBuilder();
                    pinsTable.append("CREATE TABLE " + TABLE_PINS);
                    pinsTable.append("(X TEXT, ");
                    pinsTable.append("Y TEXT, ");
                    pinsTable.append("Locality TEXT, ");
                    pinsTable.append("IssueType TEXT, ");
                    pinsTable.append("Number TEXT, ");
                    pinsTable.append("provider TEXT, ");
                    pinsTable.append("view TEXT, ");
                    pinsTable.append("PinIcon TEXT, ");
                    pinsTable.append("issueId TEXT, ");
                    pinsTable.append("subLocality TEXT, ");
                    pinsTable.append("subLocalityAr TEXT, ");
                    pinsTable.append("IssueTypeAr TEXT)");
                    //create table map settings

                    StringBuilder mapsettings = new StringBuilder();
                    mapsettings.append("CREATE TABLE " + TABLE_MAP_SETTINGS);
                    mapsettings.append("(X TEXT, ");
                    mapsettings.append("Y TEXT, ");
                    mapsettings.append("zoom TEXT, ");
                    mapsettings.append("showservice TEXT)");


                    //create table mobile config

                    StringBuilder config = new StringBuilder();
                    config.append("CREATE TABLE " + TABLE_MOBILE_CONFIGURATION);
                    config.append("(id INTEGER PRIMARY KEY, ");
                    config.append("key TEXT, ");
                    config.append("value TEXT)");


                    //create issue number table
                    StringBuilder isseunumber = new StringBuilder();
                    isseunumber.append("CREATE TABLE " + TABLE_ISSUE_NUMBER);
                    isseunumber.append("(imagename TEXT, ");
                    isseunumber.append("label TEXT, ");
                    isseunumber.append("view TEXT, ");
                    isseunumber.append("number TEXT)");


                    db.execSQL(providerTable.toString());
                    db.execSQL(issutable.toString());
                    db.execSQL(pinsTable.toString());
                    db.execSQL(mapsettings.toString());
                    db.execSQL(maplabel.toString());
                    db.execSQL(config.toString());
                    db.execSQL(isseunumber.toString());
            }
            upgradeTo++;
        }
    }

    public List<Menu> getMenuConfig() {
        List<Menu> listMenu = new ArrayList<Menu>();

        String selectQuery = "SELECT * FROM menu";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();
                menu.setId(cursor.getInt(0));
                menu.setIconReference(cursor.getInt(1));
                menu.setTitle(cursor.getString(2));
                menu.setDescription(cursor.getString(3));

                listMenu.add(menu);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listMenu;
    }

    public int deletePins(String view, String provider, String issue) {

        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_PINS, " view='" + view + "' and provider='" + provider + "' and issueId='" + issue + "'", null);
    }

    public int deleteIssueNumber(String view) {

        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_ISSUE_NUMBER, " view='" + view + "'", null);
    }

    public int deleteProviders() {

        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_SERVICE_PROVIDER, "", null);
    }

    public int deleteIssueTypes() {

        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_ISSUE_TYPES, "", null);
    }

    public int deleteMapLabels() {

        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_MAP_LABEL, "", null);
    }

    public int deleteMobileConfig() {

        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_MOBILE_CONFIGURATION, "", null);
    }

    public int deleteMapSettings() {

        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_MAP_SETTINGS, "", null);
    }

    public ArrayList<Provider> getProviders() {
        ArrayList<Provider> providerList = new ArrayList<Provider>();

        String selectQuery = "SELECT * FROM " + TABLE_SERVICE_PROVIDER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Provider p = new Provider();
                p.setId(cursor.getString(0));
                p.setName(cursor.getString(1));

                providerList.add(p);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return providerList;
    }

    public ArrayList<IssueNumber> getIssueNumber(String view) {
        ArrayList<IssueNumber> IssueNumberList = new ArrayList<IssueNumber>();

        String selectQuery = "SELECT * FROM " + TABLE_ISSUE_NUMBER + " where view='" + view + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                IssueNumber p = new IssueNumber();
                p.setImageName(cursor.getString(0));
                p.setLabel(cursor.getString(1));
                p.setNumber(cursor.getString(3));
                IssueNumberList.add(p);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return IssueNumberList;
    }

    public ArrayList<IssueTypes> getIssueTypes() {
        ArrayList<IssueTypes> issuesList = new ArrayList<IssueTypes>();

        String selectQuery = "SELECT * FROM " + TABLE_ISSUE_TYPES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                IssueTypes p = new IssueTypes();
                p.setId(cursor.getString(0));
                p.setName(cursor.getString(1));
                p.setLogo(cursor.getString(1));
                issuesList.add(p);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return issuesList;
    }

    public InitialMapSettings getMapSettings() {
        InitialMapSettings map = new InitialMapSettings();

        String selectQuery = "SELECT * FROM " + TABLE_MAP_SETTINGS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {


            map.setLocationX(cursor.getString(0));
            map.setLocationY(cursor.getString(1));
            map.setZoomSettings(cursor.getString(2));
            map.setShowServiceProvider(Boolean.parseBoolean(cursor.getString(3)));


        }

        cursor.close();
        db.close();

        return map;
    }

    public ArrayList<MapPinLocation> getPins(String serviceProvider, String issueType, String view) {
        ArrayList<MapPinLocation> pins = new ArrayList<MapPinLocation>();

        String selectQuery = "SELECT * FROM " + TABLE_PINS + " where view='" + view + "' and issueId='" + issueType + "' and provider='" + serviceProvider + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MapPinLocation p = new MapPinLocation();
                p.setLocationX(cursor.getString(0));
                p.setLocationY(cursor.getString(1));
                p.setLocality(cursor.getString(2));
                p.setIssueType(cursor.getString(3));
                p.setNumber(cursor.getString(4));
                p.setPinIcon(cursor.getString(7));
                p.setSubLocality(cursor.getString(8));
                p.setSubLocalityAr(cursor.getString(9));
                p.setIssueTypeAr(cursor.getString(10));
                pins.add(p);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pins;
    }

    public ArrayList<MapLabel> getMapLabels() {
        ArrayList<MapLabel> labels = new ArrayList<MapLabel>();

        String selectQuery = "SELECT * FROM " + TABLE_MAP_LABEL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MapLabel p = new MapLabel();
                p.setImageName(cursor.getString(0));
                p.setLabel(cursor.getString(1));
                p.setNumber(cursor.getString(2));

                labels.add(p);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return labels;
    }

    public ArrayList<MobileConfiguration> getMobileconfigs() {
        ArrayList<MobileConfiguration> configs = new ArrayList<MobileConfiguration>();

        String selectQuery = "SELECT * FROM " + TABLE_MOBILE_CONFIGURATION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MobileConfiguration p = new MobileConfiguration();
                p.setId(cursor.getString(0));
                p.setkey(cursor.getString(1));
                p.setValue(cursor.getString(2));

                configs.add(p);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return configs;
    }

    public Menu searchMenuById(int menuId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MENU, new String[]{ID,
                        ICON,
                        TITLE,
                        DESCRIPTION},
                ID + " = ?",
                new String[]{String.valueOf(menuId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Menu menu = new Menu(cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3));

        cursor.close();
        db.close();
        close();

        return menu;
    }

    public void addDefaultMenu(List<Menu> listaMenu, SQLiteDatabase db) {

        for (int i = 0; i < listaMenu.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(ID, listaMenu.get(i).getId());
            values.put(ICON, listaMenu.get(i).getIconReference());
            values.put(TITLE, listaMenu.get(i).getTitle());
            values.put(DESCRIPTION, listaMenu.get(i).getDescription());

            db.insert(TABLE_MENU, null, values);
        }
    }


    public void InsertServiceProviders(List<Provider> providers) {
        deleteProviders();
        SQLiteDatabase db = this.getReadableDatabase();

        for (int i = 0; i < providers.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("id", providers.get(i).getId());
            values.put("name", providers.get(i).getName());


            db.insert(TABLE_SERVICE_PROVIDER, null, values);
        }
    }

    public void InsertIssueTypes(List<IssueTypes> issueTypes) {
        deleteIssueTypes();
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < issueTypes.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("id", issueTypes.get(i).getId());
            values.put("name", issueTypes.get(i).getName());

            values.put("logo", issueTypes.get(i).getLogo());
            db.insert(TABLE_ISSUE_TYPES, null, values);
        }
    }


    public void InsertPins(List<MapPinLocation> pins, String view, String providerid, String issueId) {
        deletePins(view, providerid, issueId);
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < pins.size(); i++) {

            ContentValues values = new ContentValues();
            values.put("X", pins.get(i).getLocationX());
            values.put("Y", pins.get(i).getLocationY());

            values.put("Locality", pins.get(i).getLocality());
            values.put("IssueType", pins.get(i).getIssueType());
            values.put("issueId", issueId);
            values.put("Number", pins.get(i).getNumber());
            values.put("provider", providerid);
            values.put("view", view);
            values.put("PinIcon", pins.get(i).getPinIcon());
            values.put("subLocality", pins.get(i).getSubLocality());
            values.put("subLocalityAr", pins.get(i).getSubLocalityAr());
            values.put("IssueTypeAr", pins.get(i).getIssueTypeAr());
            db.insert(TABLE_PINS, null, values);
        }

    }

    public void InsertMapSettings(InitialMapSettings settings) {
        deleteMapSettings();
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("X", settings.getLocationX());
        values.put("Y", settings.getLocationY());

        values.put("zoom", settings.getZoomSettings());
        values.put("showservice", settings.isShowServiceProvider());

        db.insert(TABLE_MAP_SETTINGS, null, values);

    }

    public void InsertMapLabels(List<MapLabel> labels) {
        deleteMapLabels();
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < labels.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("imagename", labels.get(i).getImageName());
            values.put("label", labels.get(i).getLabel());

            values.put("Number", labels.get(i).getNumber());

            db.insert(TABLE_MAP_LABEL, null, values);
        }
    }

    public void InsertMobileconfig(List<MobileConfiguration> config) {
        deleteMobileConfig();
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < config.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("id", config.get(i).getId());
            values.put("key", config.get(i).getKey());

            values.put("value", config.get(i).getValue());

            db.insert(TABLE_MOBILE_CONFIGURATION, null, values);
        }
    }

    public void InsertIssueNumber(List<IssueNumber> issuenumber, String view) {
        deleteIssueNumber(view);
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < issuenumber.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("imagename", issuenumber.get(i).getImageName());
            values.put("view", view);

            values.put("label", issuenumber.get(i).getLabel());
            values.put("number", issuenumber.get(i).getNumber());
            db.insert(TABLE_ISSUE_NUMBER, null, values);
        }
    }
}