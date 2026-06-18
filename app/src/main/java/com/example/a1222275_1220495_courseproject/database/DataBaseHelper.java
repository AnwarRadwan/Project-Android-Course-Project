package com.example.a1222275_1220495_courseproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a1222275_1220495_courseproject.models.User;
import com.example.a1222275_1220495_courseproject.models.Trip;
import com.example.a1222275_1220495_courseproject.models.Reservation;

import java.util.ArrayList;
import java.util.List;

/**
 * DataBaseHelper class to manage SQLite database operations for the application.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TravelGo.db";
    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USERS(ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT UNIQUE, FIRSTNAME TEXT, LASTNAME TEXT, PASSWORD TEXT, GENDER TEXT, CATEGORY TEXT, PHONE TEXT, IMAGE TEXT)");
        db.execSQL("CREATE TABLE TRIPS(TRIP_ID INTEGER PRIMARY KEY, DESTINATION TEXT, COUNTRY TEXT, DURATION INTEGER, PRICE REAL, RATING REAL, DESCRIPTION TEXT, IMAGE TEXT)");
        db.execSQL("CREATE TABLE RESERVATIONS(RESERVATION_ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_ID INTEGER, TRIP_ID INTEGER, QUANTITY INTEGER, RESERVATION_TYPE TEXT, RESERVATION_DATE TEXT, STATUS TEXT)");
        db.execSQL("CREATE TABLE FAVORITES(FAVORITE_ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_ID INTEGER, TRIP_ID INTEGER)");
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
        if (cursor != null) cursor.close();
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
        db.replace("TRIPS", null, values);
    }

    public void deleteAllTrips() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("TRIPS", null, null);
    }

    public Trip getTripById(int tripId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("TRIPS", null, "TRIP_ID = ?", new String[]{String.valueOf(tripId)}, null, null, null);
        Trip trip = null;
        if (cursor != null && cursor.moveToFirst()) {
            trip = new Trip(
                    cursor.getInt(cursor.getColumnIndexOrThrow("TRIP_ID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION")),
                    cursor.getString(cursor.getColumnIndexOrThrow("COUNTRY")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("DURATION")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("RATING")),
                    cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")),
                    cursor.getString(cursor.getColumnIndexOrThrow("IMAGE"))
            );
            cursor.close();
        } else if (cursor != null) cursor.close();
        return trip;
    }

    public List<Trip> getAllTrips() {
        List<Trip> tripList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("TRIPS", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                tripList.add(new Trip(
                        cursor.getLong(cursor.getColumnIndexOrThrow("TRIP_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION")),
                        cursor.getString(cursor.getColumnIndexOrThrow("COUNTRY")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("DURATION")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("RATING")),
                        cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")),
                        cursor.getString(cursor.getColumnIndexOrThrow("IMAGE"))
                ));
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        return tripList;
    }

    public void insertReservation(long userId, long tripId, int quantity, String reservationType, String reservationDate, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USER_ID", userId);
        values.put("TRIP_ID", tripId);
        values.put("QUANTITY", quantity);
        values.put("RESERVATION_TYPE", reservationType);
        values.put("RESERVATION_DATE", reservationDate);
        values.put("STATUS", status);
        db.insert("RESERVATIONS", null, values);
    }

    public List<Reservation> getReservationsByUserId(long userId) {
        List<Reservation> reservationList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // استخدام LEFT JOIN لضمان جلب الحجز حتى لو كانت الرحلة غير مخزنة في جدول TRIPS حالياً
        String query = "SELECT R.*, T.DESTINATION FROM RESERVATIONS R " +
                       "LEFT JOIN TRIPS T ON R.TRIP_ID = T.TRIP_ID " +
                       "WHERE R.USER_ID = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation(
                        cursor.getLong(cursor.getColumnIndexOrThrow("RESERVATION_ID")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("USER_ID")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("TRIP_ID")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY")),
                        cursor.getString(cursor.getColumnIndexOrThrow("RESERVATION_TYPE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("RESERVATION_DATE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("STATUS"))
                );
                String dest = cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION"));
                reservation.setTripDestination(dest != null ? dest : "Unknown Trip #" + reservation.getTripId());
                reservationList.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reservationList;
    }

    public void addFavorite(int userId, int tripId) {
        if (!isFavorite(userId, tripId)) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("USER_ID", userId);
            values.put("TRIP_ID", tripId);
            db.insert("FAVORITES", null, values);
        }
    }

    public void removeFavorite(long userId, long tripId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("FAVORITES", "USER_ID = ? AND TRIP_ID = ?", new String[]{String.valueOf(userId), String.valueOf(tripId)});
    }

    public boolean isFavorite(int userId, int tripId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("FAVORITES", null, "USER_ID = ? AND TRIP_ID = ?", new String[]{String.valueOf(userId), String.valueOf(tripId)}, null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    public List<Trip> getFavoritesByUser(long userId) {
        List<Trip> favoriteTrips = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT T.* FROM TRIPS T JOIN FAVORITES F ON T.TRIP_ID = F.TRIP_ID WHERE F.USER_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                favoriteTrips.add(new Trip(
                        cursor.getInt(cursor.getColumnIndexOrThrow("TRIP_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION")),
                        cursor.getString(cursor.getColumnIndexOrThrow("COUNTRY")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("DURATION")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("RATING")),
                        cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")),
                        cursor.getString(cursor.getColumnIndexOrThrow("IMAGE"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favoriteTrips;
    }

    public void insertTestUsers() {
        String[][] testUsers = {
                {"ahmad@example.com", "Ahmad", "Khalil", "Ahmad123", "Male", "Regular", "0599123456"},
                {"sara@example.com", "Sara", "Yousef", "Sara654", "Female", "Premium", "0598123456"}
        };
        for (String[] u : testUsers) {
            if (getUserByEmail(u[0]) == null) {
                ContentValues values = new ContentValues();
                values.put("EMAIL", u[0]);
                values.put("FIRSTNAME", u[1]);
                values.put("LASTNAME", u[2]);
                values.put("PASSWORD", hashPassword(u[3]));
                values.put("GENDER", u[4]);
                values.put("CATEGORY", u[5]);
                values.put("PHONE", u[6]);
                values.put("IMAGE", "");
                getWritableDatabase().insert("USERS", null, values);
            }
        }
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) { return password; }
    }
}