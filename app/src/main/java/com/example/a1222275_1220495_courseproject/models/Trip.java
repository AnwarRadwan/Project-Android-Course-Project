package com.example.a1222275_1220495_courseproject.models;

// Trip model class
public class Trip {

    private long tripId;
    private String destination;
    private String country;
    private int duration;
    private double price;
    private double rating;
    private String description;
    private String image;

    public Trip() {
    }

    public Trip(long tripId,
                String destination,
                String country,
                int duration,
                double price,
                double rating,
                String description,
                String image) {

        this.tripId = tripId;
        this.destination = destination;
        this.country = country;
        this.duration = duration;
        this.price = price;
        this.rating = rating;
        this.description = description;
        this.image = image;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
  }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
