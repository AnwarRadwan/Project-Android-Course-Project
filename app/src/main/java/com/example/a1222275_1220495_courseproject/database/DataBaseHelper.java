package com.example.a1222275_1220495_courseproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a1222275_1220495_courseproject.models.User;
import com.example.a1222275_1220495_courseproject.models.Trip;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TravelGo.db";
    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE USERS(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "EMAIL TEXT UNIQUE," +
                        "FIRSTNAME TEXT," +
                        "LASTNAME TEXT," +
                        "PASSWORD TEXT," +
                        "GENDER TEXT," +
                        "CATEGORY TEXT," +
                        "PHONE TEXT," +
                        "IMAGE TEXT)"
        );

        db.execSQL(
                "CREATE TABLE TRIPS(" +
                        "TRIP_ID INTEGER PRIMARY KEY," +
                        "DESTINATION TEXT," +
                        "COUNTRY TEXT," +
                        "DURATION INTEGER," +
                        "PRICE REAL," +
                        "RATING REAL," +
                        "DESCRIPTION TEXT," +
                        "IMAGE TEXT)"
        );

        db.execSQL(
                "CREATE TABLE RESERVATIONS(" +
                        "RESERVATION_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "USER_ID INTEGER," +
                        "TRIP_ID INTEGER," +
                        "QUANTITY INTEGER," +
                        "RESERVATION_TYPE TEXT," +
                        "RESERVATION_DATE TEXT," +
                        "STATUS TEXT)"
        );

        db.execSQL(
                "CREATE TABLE FAVORITES(" +
                        "FAVORITE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "USER_ID INTEGER," +
                        "TRIP_ID INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USERS");
        db.execSQL("DROP TABLE IF EXISTS TRIPS");
        db.execSQL("DROP TABLE IF EXISTS RESERVATIONS");
        db.execSQL("DROP TABLE IF EXISTS FAVORITES");
        onCreate(db);
    }

    public void insertUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("EMAIL", user.getEmail());
        values.put("FIRSTNAME", user.getFirstName());
        values.put("LASTNAME", user.getLastName());
        values.put("PASSWORD", user.getPassword());
        values.put("GENDER", user.getGender());
        values.put("CATEGORY", user.getCategory());
        values.put("PHONE", user.getPhone());
        values.put("IMAGE", user.getImage());
        db.insert("USERS", null, values);
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("USERS", null, "EMAIL = ?", new String[]{email}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getLong(cursor.getColumnIndexOrThrow("ID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("EMAIL")),
                    cursor.getString(cursor.getColumnIndexOrThrow("FIRSTNAME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("LASTNAME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("PASSWORD")),
                    cursor.getString(cursor.getColumnIndexOrThrow("GENDER")),
                    cursor.getString(cursor.getColumnIndexOrThrow("CATEGORY")),
                    cursor.getString(cursor.getColumnIndexOrThrow("PHONE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("IMAGE"))
            );
            cursor.close();
            return user;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public void insertTrip(Trip trip) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TRIP_ID", trip.getTripId());
        values.put("DESTINATION", trip.getDestination());
        values.put("COUNTRY", trip.getCountry());
        values.put("DURATION", trip.getDuration());
        values.put("PRICE", trip.getPrice());
        values.put("RATING", trip.getRating());
        values.put("DESCRIPTION", trip.getDescription());
        values.put("IMAGE", trip.getImage());
        // Use replace to avoid duplicates if trip_id exists
        db.replace("TRIPS", null, values);
    }

    public void deleteAllTrips() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("TRIPS", null, null);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM USERS", null);
    }
}
