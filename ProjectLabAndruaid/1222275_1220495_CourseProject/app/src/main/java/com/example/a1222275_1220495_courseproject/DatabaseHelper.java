package com.example.a1222275_1220495_courseproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "travel_planner.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_TRIPS = "trips";

    // Column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESTINATION = "destination";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_DURATION_DAYS = "duration_days";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image";

    // CREATE TABLE statement
    private static final String CREATE_TABLE_TRIPS =
            "CREATE TABLE " + TABLE_TRIPS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_DESTINATION + " TEXT NOT NULL, " +
            COLUMN_COUNTRY + " TEXT NOT NULL, " +
            COLUMN_DURATION_DAYS + " INTEGER, " +
            COLUMN_PRICE + " REAL, " +
            COLUMN_RATING + " REAL, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_IMAGE + " TEXT" +
            ");";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRIPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        onCreate(db);
    }

    /**
     * Inserts or replaces a single trip in the database.
     *
     * @param trip the Trip object to save
     * @return the row ID of the inserted/replaced row, or -1 on error
     */
    public long insertOrReplaceTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, trip.getId());
        values.put(COLUMN_DESTINATION, trip.getDestination());
        values.put(COLUMN_COUNTRY, trip.getCountry());
        values.put(COLUMN_DURATION_DAYS, trip.getDurationDays());
        values.put(COLUMN_PRICE, trip.getPrice());
        values.put(COLUMN_RATING, trip.getRating());
        values.put(COLUMN_DESCRIPTION, trip.getDescription());
        values.put(COLUMN_IMAGE, trip.getImage());
        return db.insertWithOnConflict(TABLE_TRIPS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * Inserts or replaces a list of trips in the database inside a transaction
     * for performance.
     *
     * @param trips the list of Trip objects to save
     */
    public void insertOrReplaceTrips(List<Trip> trips) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Trip trip : trips) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_ID, trip.getId());
                values.put(COLUMN_DESTINATION, trip.getDestination());
                values.put(COLUMN_COUNTRY, trip.getCountry());
                values.put(COLUMN_DURATION_DAYS, trip.getDurationDays());
                values.put(COLUMN_PRICE, trip.getPrice());
                values.put(COLUMN_RATING, trip.getRating());
                values.put(COLUMN_DESCRIPTION, trip.getDescription());
                values.put(COLUMN_IMAGE, trip.getImage());
                db.insertWithOnConflict(TABLE_TRIPS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Retrieves all trips stored in the database.
     *
     * @return a list of Trip objects
     */
    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRIPS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Trip trip = new Trip();
                trip.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                trip.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESTINATION)));
                trip.setCountry(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)));
                trip.setDurationDays(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION_DAYS)));
                trip.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                trip.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)));
                trip.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                trip.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
                trips.add(trip);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return trips;
    }

    /**
     * Deletes all trips from the database.
     */
    public void clearTrips() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIPS, null, null);
    }

    /**
     * Returns the total number of trips stored in the database.
     */
    public int getTripCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TRIPS, null);
        int count = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }
}
