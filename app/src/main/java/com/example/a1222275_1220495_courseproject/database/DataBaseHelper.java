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

    // --- User Methods ---
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

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("USERS", null, null, null, null, null, "ID DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                users.add(extractUser(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return users;
    }

    public void deleteUser(long userId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("USERS", "ID = ?", new String[]{String.valueOf(userId)});
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("USERS", null, "EMAIL = ?", new String[]{email}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = extractUser(cursor);
            cursor.close();
            return user;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public User getUserById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("USERS", null, "ID = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = extractUser(cursor);
            cursor.close();
            return user;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    private User extractUser(Cursor cursor) {
        return new User(
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
    }

    public void updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FIRSTNAME", user.getFirstName());
        values.put("LASTNAME", user.getLastName());
        values.put("PHONE", user.getPhone());
        values.put("IMAGE", user.getImage());
        db.update("USERS", values, "ID = ?", new String[]{String.valueOf(user.getId())});
    }

    public void updateUserPassword(long userId, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PASSWORD", newPassword);
        db.update("USERS", values, "ID = ?", new String[]{String.valueOf(userId)});
    }

    // --- Trip Methods ---
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

    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("TRIPS", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                trips.add(extractTrip(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return trips;
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
            trip = extractTrip(cursor);
            cursor.close();
        } else if (cursor != null) cursor.close();
        return trip;
    }

    public List<Trip> getPopularDestinations() {
        List<Trip> popularTrips = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("TRIPS", null, "RATING >= ?", new String[]{"4.5"}, null, null, "RATING DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                popularTrips.add(extractTrip(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return popularTrips;
    }

    public List<Trip> getBestTravelOffers() {
        List<Trip> cheapTrips = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("TRIPS", null, null, null, null, null, "PRICE ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                cheapTrips.add(extractTrip(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return cheapTrips;
    }

    private Trip extractTrip(Cursor cursor) {
        return new Trip(
                cursor.getLong(cursor.getColumnIndexOrThrow("TRIP_ID")),
                cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION")),
                cursor.getString(cursor.getColumnIndexOrThrow("COUNTRY")),
                cursor.getInt(cursor.getColumnIndexOrThrow("DURATION")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("RATING")),
                cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")),
                cursor.getString(cursor.getColumnIndexOrThrow("IMAGE"))
        );
    }

    // --- Favorites Logic ---
    public boolean addFavorite(long userId, long tripId) {
        if (!isFavorite((int) userId, (int) tripId)) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("USER_ID", userId);
            values.put("TRIP_ID", tripId);
            long result = db.insert("FAVORITES", null, values);
            return result != -1;
        }
        return false;
    }

    public boolean isFavorite(int userId, int tripId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("FAVORITES", null, "USER_ID = ? AND TRIP_ID = ?", 
                new String[]{String.valueOf(userId), String.valueOf(tripId)}, null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    public void removeFavorite(long userId, long tripId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("FAVORITES", "USER_ID = ? AND TRIP_ID = ?", new String[]{String.valueOf(userId), String.valueOf(tripId)});
    }

    public List<Trip> getFavoritesByUser(long userId) {
        List<Trip> favoriteTrips = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT T.* FROM TRIPS T JOIN FAVORITES F ON T.TRIP_ID = F.TRIP_ID WHERE F.USER_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                favoriteTrips.add(extractTrip(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return favoriteTrips;
    }

    // --- Reservation Logic ---
    public void insertReservation(long userId, long tripId, int quantity, String type, String date, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USER_ID", userId);
        values.put("TRIP_ID", tripId);
        values.put("QUANTITY", quantity);
        values.put("RESERVATION_TYPE", type);
        values.put("RESERVATION_DATE", date);
        values.put("STATUS", status);
        db.insert("RESERVATIONS", null, values);
    }

    public List<Reservation> getReservationsByUserId(long userId) {
        List<Reservation> reservationList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT R.*, T.DESTINATION FROM RESERVATIONS R " +
                       "LEFT JOIN TRIPS T ON R.TRIP_ID = T.TRIP_ID " +
                       "WHERE R.USER_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
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
                reservation.setTripDestination(dest != null ? dest : "Unknown Trip");
                reservationList.add(reservation);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return reservationList;
    }

    public void insertTestUsers() {
        // (Existing implementation for testing)
    }
}
