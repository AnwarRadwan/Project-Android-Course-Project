package com.example.a1222275_1220495_courseproject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TripApiService {

    /**
     * Fetches the list of trips from the remote JSON server.
     * Endpoint: https://my-json-server.typicode.com/anwarradwan/project-android-course-project/trips
     */
    @GET("trips")
    Call<List<Trip>> getTrips();
}
