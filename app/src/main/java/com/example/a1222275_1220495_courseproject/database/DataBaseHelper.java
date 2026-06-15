package com.example.a1222275_1220495_courseproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a1222275_1220495_courseproject.models.User;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context,
                          String name,
                          SQLiteDatabase.CursorFactory factory,
                          int version) {

        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE USERS(" +
                        "ID LONG PRIMARY KEY," +
                        "EMAIL TEXT," +
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
                        "TRIP_ID LONG PRIMARY KEY," +
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
                        "RESERVATION_ID LONG PRIMARY KEY," +
                        "USER_ID LONG," +
                        "TRIP_ID LONG," +
                        "QUANTITY INTEGER," +
                        "RESERVATION_TYPE TEXT," +
                        "RESERVATION_DATE TEXT," +
                        "STATUS TEXT)"
        );

        db.execSQL(
                "CREATE TABLE FAVORITES(" +
                        "FAVORITE_ID LONG PRIMARY KEY," +
                        "USER_ID LONG," +
                        "TRIP_ID LONG)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

    }

    public void insertUser(User user) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("ID", user.getId());
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

    public Cursor getAllUsers() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM USERS",
                null
        );
    }
}
