package com.example.a1222275_1220495_courseproject.models;

// Admin reservation model
public class AdminReservation {
    private long reservationId;
    private String userName;
    private String tripName;
    private int quantity;
    private String reservationType;
    private String reservationDate;
    private String status;

    public AdminReservation() {}

    public AdminReservation(long reservationId, String userName, String tripName, int quantity, 
                            String reservationType, String reservationDate, String status) {
        this.reservationId = reservationId;
        this.userName = userName;
        this.tripName = tripName;
        this.quantity = quantity;
        this.reservationType = reservationType;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    public long getReservationId() { return reservationId; }
    public void setReservationId(long reservationId) { this.reservationId = reservationId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getTripName() { return tripName; }
    public void setTripName(String tripName) { this.tripName = tripName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getReservationType() { return reservationType; }
    public void setReservationType(String reservationType) { this.reservationType = reservationType; }

    public String getReservationDate() { return reservationDate; }
    public void setReservationDate(String reservationDate) { this.reservationDate = reservationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
