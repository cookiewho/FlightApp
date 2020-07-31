package edu.csumb.flightapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Flight {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String flightNo;
    private String departure;
    private String arrival;
    private String departureTime;
    private int capacity;
    private double price;
    private int availableSeats;

    public Flight() {}

    @Ignore
    public Flight(String flightNo, String departure, String arrival, String departureTime, int capacity, double price) {
        this.flightNo = flightNo;
        this.departure = departure;
        this.arrival = arrival;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.price = price;
        this.availableSeats = capacity;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo( String flightNo) {
        this.flightNo = flightNo;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture( String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival( String arrival) {
        this.arrival = arrival;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime( String departureTime) {
        this.departureTime = departureTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "id:  "+id+" flightNo:  "+flightNo+"\nfrom:  "+departure+" to:  "+arrival+"\ndeparture time:  "+departureTime;
    }
    public String toString2() {
        return "FlightNo:  " + flightNo + "\nfrom:  " + departure + " to:  " + arrival + "\nDeparture time:  " + departureTime
                + "\nCapacity:  " + capacity + "\nPrice:  $" + price;
    }

}
