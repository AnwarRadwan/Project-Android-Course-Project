    package com.example.a1222275_1220495_courseproject.database;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    import com.example.a1222275_1220495_courseproject.models.User;
    import com.example.a1222275_1220495_courseproject.models.Trip;

    /**
     * DataBaseHelper class to manage SQLite database operations for the application.
     * It handles the creation, version management, and data access for users, trips, and reservations.
     */
    public class DataBaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "TravelGo.db";
        private static final int DATABASE_VERSION = 1;

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Called when the database is created for the first time.
         * This method defines the schema for all tables and inserts initial mock data.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create table for storing user profile and account details
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

            // Create table for storing trip packages information
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

            // Create table for storing user trip bookings and reservations
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

            // Create table for storing user's favorite trips
            db.execSQL(
                    "CREATE TABLE FAVORITES(" +
                            "FAVORITE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "USER_ID INTEGER," +
                            "TRIP_ID INTEGER)"
            );

        }

        /**
         * Called when the database needs to be upgraded.
         * This version drops all existing tables and recreates them.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS USERS");
            db.execSQL("DROP TABLE IF EXISTS TRIPS");
            db.execSQL("DROP TABLE IF EXISTS RESERVATIONS");
            db.execSQL("DROP TABLE IF EXISTS FAVORITES");
            onCreate(db);
        }

        /**
         * Inserts a new user record into the database.
         * @param user The user object containing all registration info.
         */
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

        /**
         * Retrieves a single user from the database by their email address.
         * @param email The user email to look for.
         * @return User object if found, otherwise null.
         */
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

        /**
         * Inserts or updates a trip package in the TRIPS table.
         * @param trip The trip data to be inserted or replaced.
         */
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
            // Use replace to ensure existing trips are updated if trip_id already exists
            db.replace("TRIPS", null, values);
        }

        /**
         * Deletes all records from the TRIPS table.
         */
        public void deleteAllTrips() {
            SQLiteDatabase db = getWritableDatabase();
            db.delete("TRIPS", null, null);
        }

        /**
         * Returns a cursor containing all user records from the database.
         * @return Cursor object for UI display or processing.
         */
        public Cursor getAllUsers() {
            SQLiteDatabase db = getReadableDatabase();
            return db.rawQuery("SELECT * FROM USERS", null);
        }
        public void insertTestUsers() {
            String[][] testUsers = {
                    {"ahmad@example.com", "Ahmad", "Khalil", "Ahmad123", "Male", "Regular", "0599123456"},
                    {"sara@example.com", "Sara", "Yousef", "Sara654", "Female", "Premium", "0598123456"},
                    {"omar@example.com", "Omar", "Hasan", "Omar123", "Male", "Regular", "0597123456"}
            };
            for (String[] u : testUsers) {
                if (getUserByEmail(u[0]) == null) {
                    String hashedPassword = hashPassword(u[3]);

                    ContentValues values = new ContentValues();
                    values.put("EMAIL", u[0]);
                    values.put("FIRSTNAME", u[1]);
                    values.put("LASTNAME", u[2]);
                    values.put("PASSWORD", hashedPassword);
                    values.put("GENDER", u[4]);
                    values.put("CATEGORY", u[5]);
                    values.put("PHONE", u[6]);
                    values.put("IMAGE", "");

                    getWritableDatabase().insert("USERS", null, values);
                }
            }
        }
        /**
         * Hashes a plain text password using SHA-256.
         * Used here to keep test data consistent with the app's real password storage.
         */
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
            } catch (java.security.NoSuchAlgorithmException e) {
                return password;
            }
        }
    }
