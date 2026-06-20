package com.example.a1222275_1220495_courseproject.models;

// Favorite model class
public class Favorite {

    private long favoriteId;
    private long userId;
    private long tripId;

    public Favorite() {
    }

    public Favorite(long favoriteId,
                    long userId,
                    long tripId) {

        this.favoriteId = favoriteId;
        this.userId = userId;
        this.tripId = tripId;
    }

    public long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }
}
