package com.example.a1222275_1220495_courseproject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL =
            "https://my-json-server.typicode.com/anwarradwan/project-android-course-project/";

    private static RetrofitClient instance;
    private final Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Returns the singleton instance of RetrofitClient.
     */
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    /**
     * Creates and returns the TripApiService interface implementation.
     */
    public TripApiService getTripApiService() {
        return retrofit.create(TripApiService.class);
    }
}
