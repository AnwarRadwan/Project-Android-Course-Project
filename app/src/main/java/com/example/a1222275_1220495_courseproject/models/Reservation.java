package com.example.a1222275_1220495_courseproject.models;

public class Reservation {

    private long reservationId;
    private long userId;
    private long tripId;
    private int quantity;
    private String reservationType;
    private String reservationDate;
    private String status;
    private String tripDestination;
    private String userEmail;

    public Reservation() {
    }

    public Reservation(long reservationId,
                       long userId,
                       long tripId,
                       int quantity,
                       String reservationType,
                       String reservationDate,
                       String status) {

        this.reservationId = reservationId;
        this.userId = userId;
        this.tripId = tripId;
        this.quantity = quantity;
        this.reservationType = reservationType;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    public long getReservationId() {
        return reservationId;
    }

    public void setReservationId(long reservationId) {
        this.reservationId = reservationId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReservationType() {
        return reservationType;
    }

    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTripDestination() {
        return tripDestination;
    }

    public void setTripDestination(String tripDestination) {
        this.tripDestination = tripDestination;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}