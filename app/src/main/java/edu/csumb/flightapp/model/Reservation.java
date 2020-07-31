package edu.csumb.flightapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Reservation {
    @PrimaryKey(autoGenerate = true)
    private long reservation_id;

    private int ammount;
    private String username;
    private String flightNo;
    private String departure;
    private String arrival;
    private String departureTime;
    private double total_price;

    public  Reservation(){}

    @Ignore
    public Reservation(int amnt, Flight flt, String usr) {
        this.ammount = amnt;
        this.flightNo = flt.getFlightNo();
        this.departure = flt.getDeparture();
        this.arrival = flt.getArrival();
        this.departureTime = flt.getDepartureTime();
        this.total_price = ammount*flt.getPrice();
        this.username = usr;
    }

    public long getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(long reservation_id) {
        this.reservation_id = reservation_id;
    }

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    @Override
    public String toString() {
        return
                "\nUsername= " + username +"/id= " + reservation_id +
                "\n FlightNo= " +flightNo+
                "\nDeparture= " + departure + "/ Arrival= " + arrival+
                "\nDeparture Time= " + departureTime+
                "\nNumber  of Tickets= "+ammount;
    }
    public String toString2() {
        return "Username: " + username +"\nFlightNo: " +flightNo+
                "\nFrom: " + departure + " to: " + arrival+
                "\nNumm of Tickets: "+ammount+ "  /  Reserv Num: " + reservation_id +
                String.format("\nTotal Price: $%.2f",total_price);
    }
}
