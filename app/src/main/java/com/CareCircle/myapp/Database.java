package com.CareCircle.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Database {

    private static final String DATABASE_NAME = "CareCircle.db";
    private static final int DATABASE_VERSION = 2;

    // Users table and columns
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    // Contacts table and columns
    private static final String TABLE_CONTACTS = "contacts";
    private static final String COL_CONTACT_ID = "id";
    private static final String COL_CONTACT_NAME = "name";
    private static final String COL_CONTACT_PHONE = "phone";

    // Check-in table and columns
    private static final String TABLE_CHECKIN = "checkin_log";
    private static final String COL_CHECKIN_ID = "id";
    private static final String COL_CHECKIN_USERNAME = "username";
    private static final String COL_CHECKIN_TIMESTAMP = "timestamp";

    private final SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    public Database(Context context) {
        openHelper = new SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                // create users table
                db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                        COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COL_USERNAME + " TEXT UNIQUE," +
                        COL_EMAIL + " TEXT," +
                        COL_PASSWORD + " TEXT)");

                // create contacts table
                db.execSQL("CREATE TABLE " + TABLE_CONTACTS + " (" +
                        COL_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COL_CONTACT_NAME + " TEXT," +
                        COL_CONTACT_PHONE + " TEXT)");

                // create check-in table
                db.execSQL("CREATE TABLE " + TABLE_CHECKIN + " (" +
                        COL_CHECKIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COL_CHECKIN_USERNAME + " TEXT," +
                        COL_CHECKIN_TIMESTAMP + " TEXT)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // drop existing tables if database version changes
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKIN);
                onCreate(db);
            }
        };
        db = openHelper.getWritableDatabase(); // open database for read/write
    }

    // Insert new user into users table
    public boolean insertUser(String username, String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username);
        cv.put(COL_EMAIL, email);
        cv.put(COL_PASSWORD, password);
        return db.insert(TABLE_USERS, null, cv) != -1;
    }

    // Check if a user exists with given username & password
    public boolean checkUser(String username, String password) {
        String[] columns = {COL_USER_ID};
        String selection = COL_USERNAME + "=? AND " + COL_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Insert new contact into contacts table
    public boolean insertContact(String name, String phone) {
        ContentValues cv = new ContentValues();
        cv.put(COL_CONTACT_NAME, name);
        cv.put(COL_CONTACT_PHONE, phone.replaceAll("\\s+","")); // remove spaces from phone
        return db.insert(TABLE_CONTACTS, null, cv) != -1;
    }

    // Return cursor of all contacts (ID + name + phone)
    public Cursor getAllContactsCursor() {
        return db.query(TABLE_CONTACTS,
                new String[]{COL_CONTACT_ID, COL_CONTACT_NAME, COL_CONTACT_PHONE},
                null, null, null, null,
                COL_CONTACT_NAME + " ASC"); // sort by name
    }

    // Delete contact by ID
    public boolean deleteContactById(int id) {
        int result = db.delete(TABLE_CONTACTS, COL_CONTACT_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Insert a check-in record for a user with current timestamp
    public void insertCheckIn(String username) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues cv = new ContentValues();
        cv.put(COL_CHECKIN_USERNAME, username);
        cv.put(COL_CHECKIN_TIMESTAMP, timeStamp);
        db.insert(TABLE_CHECKIN, null, cv);
    }

    // Get all check-in records as formatted strings
    public ArrayList<String> getAllCheckIns() {
        ArrayList<String> checkins = new ArrayList<>();
        Cursor cursor = db.query(TABLE_CHECKIN,
                new String[]{COL_CHECKIN_USERNAME, COL_CHECKIN_TIMESTAMP},
                null, null, null, null,
                COL_CHECKIN_TIMESTAMP + " DESC"); // latest first

        if (cursor.moveToFirst()) {
            do {
                String user = cursor.getString(cursor.getColumnIndexOrThrow(COL_CHECKIN_USERNAME));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COL_CHECKIN_TIMESTAMP));
                checkins.add(user + " logged in at " + time);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return checkins;
    }
}
