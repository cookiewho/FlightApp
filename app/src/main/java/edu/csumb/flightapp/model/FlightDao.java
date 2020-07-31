package edu.csumb.flightapp.model;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FlightDao {

    @Query("select * from Flight")
    List<Flight> getAllFlights();

    @Query("select * from Flight where departure=:departure and arrival=:arrival")
    List<Flight> searchFlight(String departure, String arrival);

    @Query("select * from Flight where flightNo=:flightNo")
    List<Flight> searchDuplicateFlight(String flightNo);
    // the generated id value is returned
    @Insert
    long addFlight(Flight flight);

    // return the number of rows actually updated.  Should be 1
    @Update
    int updateFlight(Flight flight);

    @Query("select * from User")
    List<User> getAllUsers();

    @Query("select * from User where username=:username")
    List<User> getUserByUsername(String username);

    @Insert
    void addUser(User user);

    @Query("Select * from Reservation order by username desc")
    List<Reservation> getAllReservations();

    @Query("Select * from Reservation where username=:username")
    List<Reservation> getReservationsByUsername(String username);

    @Insert
    long addReservation(Reservation reservation);

    @Delete
    int deleteReservation(Reservation reservation);

    @Query("select * from LogRecord order by datetime desc")
    List<LogRecord> getAllLogRecords();

    @Insert
    long addLogRecord(LogRecord log);




}
