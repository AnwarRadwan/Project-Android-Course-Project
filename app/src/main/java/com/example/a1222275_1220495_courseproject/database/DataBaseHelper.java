package com.example.a1222275_1220495_courseproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a1222275_1220495_courseproject.models.User;
import com.example.a1222275_1220495_courseproject.models.Trip;
import com.example.a1222275_1220495_courseproject.models.Reservation;
import com.example.a1222275_1220495_courseproject.models.AdminReservation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

// Database helper class
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TravelGo.db";
    private static final int DATABASE_VERSION = 3; // Incremented to v3 for unique passwords

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL("CREATE TABLE USERS(ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT UNIQUE, FIRSTNAME TEXT, LASTNAME TEXT, PASSWORD TEXT, GENDER TEXT, CATEGORY TEXT, PHONE TEXT, IMAGE TEXT)");
        db.execSQL("CREATE TABLE TRIPS(TRIP_ID INTEGER PRIMARY KEY AUTOINCREMENT, DESTINATION TEXT, COUNTRY TEXT, DURATION INTEGER, PRICE REAL, RATING REAL, DESCRIPTION TEXT, IMAGE TEXT)");
        db.execSQL("CREATE TABLE RESERVATIONS(RESERVATION_ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_ID INTEGER, TRIP_ID INTEGER, QUANTITY INTEGER, RESERVATION_TYPE TEXT, RESERVATION_DATE TEXT, STATUS TEXT)");
        db.execSQL("CREATE TABLE FAVORITES(FAVORITE_ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_ID INTEGER, TRIP_ID INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop and recreate tables to ensure fresh test data with new passwords
        db.execSQL("DROP TABLE IF EXISTS USERS");
        db.execSQL("DROP TABLE IF EXISTS TRIPS");
        db.execSQL("DROP TABLE IF EXISTS RESERVATIONS");
        db.execSQL("DROP TABLE IF EXISTS FAVORITES");
        onCreate(db);
    }

    // Insert user
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

    // Insert test data with Yousef, Anwar, and Ahmad using unique passwords
    public void insertTestUsers() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM USERS", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        // Check if we need to insert the initial users
        if (count <= 2) {
            // Add Admin
            if (getUserByEmail("admin@travelgo.com") == null) {
                insertUser(new User(-1, "admin@travelgo.com", "Admin", "User", hashPassword("admin123"), "Male", "Admin", "123456789", null));
            }

            // Yousef -> y12345
            if (getUserByEmail("yousef@travelgo.com") == null) {
                insertUser(new User(-1, "yousef@travelgo.com", "Yousef", "Hilal", hashPassword("y12345"), "Male", "User", "0599111222", null));
            }
            // Anwar -> a12345
            if (getUserByEmail("anwar@travelgo.com") == null) {
                insertUser(new User(-1, "anwar@travelgo.com", "Anwar", "Ali", hashPassword("a12345"), "Male", "User", "0599333444", null));
            }
            // Ahmad -> ah12345
            if (getUserByEmail("ahmad@travelgo.com") == null) {
                insertUser(new User(-1, "ahmad@travelgo.com", "Ahmad", "Saleh", hashPassword("ah12345"), "Male", "User", "0599555666", null));
            }
        }
    }

    // Helper to hash password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }

    // Get user by email
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

    // Get user by id
    public User getUserById(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("USERS", null, "ID = ?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = extractUser(cursor);
            cursor.close();
            return user;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("USERS", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                userList.add(extractUser(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return userList;
    }

    // Update user
    public int updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FIRSTNAME", user.getFirstName());
        values.put("LASTNAME", user.getLastName());
        values.put("GENDER", user.getGender());
        values.put("PHONE", user.getPhone());
        values.put("IMAGE", user.getImage());
        return db.update("USERS", values, "ID = ?", new String[]{String.valueOf(user.getId())});
    }

    // Update password
    public void updateUserPassword(long userId, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PASSWORD", newPassword);
        db.update("USERS", values, "ID = ?", new String[]{String.valueOf(userId)});
    }

    // Delete user
    public void deleteUser(long userId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("USERS", "ID = ?", new String[]{String.valueOf(userId)});
    }

    // Map cursor to user
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

    // Insert trip
    public long insertTrip(Trip trip) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DESTINATION", trip.getDestination());
        values.put("COUNTRY", trip.getCountry());
        values.put("DURATION", trip.getDuration());
        values.put("PRICE", trip.getPrice());
        values.put("RATING", trip.getRating());
        values.put("DESCRIPTION", trip.getDescription());
        values.put("IMAGE", trip.getImage());
        return db.insert("TRIPS", null, values);
    }

    // Update trip
    public int updateTrip(Trip trip) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DESTINATION", trip.getDestination());
        values.put("COUNTRY", trip.getCountry());
        values.put("DURATION", trip.getDuration());
        values.put("PRICE", trip.getPrice());
        values.put("RATING", trip.getRating());
        values.put("DESCRIPTION", trip.getDescription());
        values.put("IMAGE", trip.getImage());
        return db.update("TRIPS", values, "TRIP_ID = ?", new String[]{String.valueOf(trip.getTripId())});
    }

    // Delete trip
    public void deleteTrip(long tripId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("TRIPS", "TRIP_ID = ?", new String[]{String.valueOf(tripId)});
    }

    // Clear trips
    public void deleteAllTrips() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("TRIPS", null, null);
    }

    // Get trip by id
    public Trip getTripById(long tripId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("TRIPS", null, "TRIP_ID = ?", new String[]{String.valueOf(tripId)}, null, null, null);
        Trip trip = null;
        if (cursor != null && cursor.moveToFirst()) {
            trip = extractTrip(cursor);
            cursor.close();
        } else if (cursor != null) cursor.close();
        return trip;
    }

    // Get all trips
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

    // Get popular trips
    public List<Trip> getPopularDestinations() {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("TRIPS", null, null, null, null, null, "RATING DESC LIMIT 5");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                trips.add(extractTrip(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return trips;
    }

    // Get best offers
    public List<Trip> getBestTravelOffers() {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("TRIPS", null, null, null, null, null, "PRICE ASC LIMIT 5");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                trips.add(extractTrip(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return trips;
    }

    // Map cursor to trip
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

    // Insert reservation
    public boolean insertReservation(long userId, long tripId, int quantity, String type, String date, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USER_ID", userId);
        values.put("TRIP_ID", tripId);
        values.put("QUANTITY", quantity);
        values.put("RESERVATION_TYPE", type);
        values.put("RESERVATION_DATE", date);
        values.put("STATUS", status);
        long result = db.insert("RESERVATIONS", null, values);
        return result != -1;
    }

    // Get user reservations
    public List<Reservation> getUserReservations(long userId) {
        List<Reservation> reservationList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT R.*, T.DESTINATION FROM RESERVATIONS R " +
                       "LEFT JOIN TRIPS T ON R.TRIP_ID = T.TRIP_ID " +
                       "WHERE R.USER_ID = ? " +
                       "ORDER BY R.RESERVATION_ID DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Reservation reservation = extractReservation(cursor);
                String dest = cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION"));
                reservation.setTripDestination(dest != null ? dest : "Unknown Trip");
                reservationList.add(reservation);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return reservationList;
    }

    // Get all admin reservations
    public List<AdminReservation> getAllAdminReservations() {
        List<AdminReservation> adminReservations = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT R.RESERVATION_ID, U.FIRSTNAME || ' ' || U.LASTNAME AS USER_NAME, " +
                       "T.DESTINATION AS TRIP_NAME, R.QUANTITY, R.RESERVATION_TYPE, " +
                       "R.RESERVATION_DATE, R.STATUS " +
                       "FROM RESERVATIONS R " +
                       "LEFT JOIN USERS U ON R.USER_ID = U.ID " +
                       "LEFT JOIN TRIPS T ON R.TRIP_ID = T.TRIP_ID " +
                       "ORDER BY R.RESERVATION_ID DESC";
        
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                AdminReservation ar = new AdminReservation();
                ar.setReservationId(cursor.getLong(cursor.getColumnIndexOrThrow("RESERVATION_ID")));
                String uName = cursor.getString(cursor.getColumnIndexOrThrow("USER_NAME"));
                ar.setUserName(uName != null ? uName : "Unknown User");
                String tName = cursor.getString(cursor.getColumnIndexOrThrow("TRIP_NAME"));
                ar.setTripName(tName != null ? tName : "Unknown Trip");
                ar.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY")));
                ar.setReservationType(cursor.getString(cursor.getColumnIndexOrThrow("RESERVATION_TYPE")));
                ar.setReservationDate(cursor.getString(cursor.getColumnIndexOrThrow("RESERVATION_DATE")));
                ar.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("STATUS")));
                adminReservations.add(ar);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return adminReservations;
    }

    // Update reservation status
    public void updateReservationStatus(long resId, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("STATUS", status);
        db.update("RESERVATIONS", values, "RESERVATION_ID = ?", new String[]{String.valueOf(resId)});
    }

    // Delete reservation
    public void deleteReservation(long resId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("RESERVATIONS", "RESERVATION_ID = ?", new String[]{String.valueOf(resId)});
    }

    // Map cursor to reservation
    private Reservation extractReservation(Cursor cursor) {
        return new Reservation(
                cursor.getLong(cursor.getColumnIndexOrThrow("RESERVATION_ID")),
                cursor.getLong(cursor.getColumnIndexOrThrow("USER_ID")),
                cursor.getLong(cursor.getColumnIndexOrThrow("TRIP_ID")),
                cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY")),
                cursor.getString(cursor.getColumnIndexOrThrow("RESERVATION_TYPE")),
                cursor.getString(cursor.getColumnIndexOrThrow("RESERVATION_DATE")),
                cursor.getString(cursor.getColumnIndexOrThrow("STATUS"))
        );
    }

    // Add favorite
    public boolean addFavorite(long userId, long tripId) {
        if (isFavorite(userId, tripId)) return false;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USER_ID", userId);
        values.put("TRIP_ID", tripId);
        long result = db.insert("FAVORITES", null, values);
        return result != -1;
    }

    // Check if favorite
    public boolean isFavorite(long userId, long tripId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("FAVORITES", null, "USER_ID = ? AND TRIP_ID = ?",
                new String[]{String.valueOf(userId), String.valueOf(tripId)}, null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    // Remove favorite
    public void removeFavorite(long userId, long tripId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("FAVORITES", "USER_ID = ? AND TRIP_ID = ?",
                new String[]{String.valueOf(userId), String.valueOf(tripId)});
    }

    // Get user favorites
    public List<Trip> getFavoritesByUser(long userId) {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT T.* FROM TRIPS T " +
                       "INNER JOIN FAVORITES F ON T.TRIP_ID = F.TRIP_ID " +
                       "WHERE F.USER_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                trips.add(extractTrip(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return trips;
    }
}
