package com.example.a1222275_1220495_courseproject;

import com.google.gson.annotations.SerializedName;

public class Trip {

    @SerializedName("id")
    private int id;

    @SerializedName("destination")
    private String destination;

    @SerializedName("country")
    private String country;

    @SerializedName("duration_days")
    private int durationDays;

    @SerializedName("price")
    private double price;

    @SerializedName("rating")
    private double rating;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String image;

    public Trip() {}

    public Trip(int id, String destination, String country, int durationDays,
                double price, double rating, String description, String image) {
        this.id = id;
        this.destination = destination;
        this.country = country;
        this.durationDays = durationDays;
        this.price = price;
        this.rating = rating;
        this.description = description;
        this.image = image;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    @Override
    public String toString() {
        return "Trip{id=" + id + ", destination='" + destination + "', country='" + country + "'}";
    }
}
