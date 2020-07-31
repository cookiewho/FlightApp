package edu.csumb.flightapp.model;

import android.content.Context;
import android.util.Log;

import java.util.List;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


//TODO  update this class to include LogRecord as entity

@Database(entities={Flight.class, User.class, Reservation.class, LogRecord.class}, version=1)
public abstract class FlightRoom extends RoomDatabase {
    // singleton
    private static FlightRoom instance;

    public abstract FlightDao dao();

    public static FlightRoom getFlightRoom(final Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FlightRoom.class,
                    "FlightDB") // database name
                    .allowMainThreadQueries()  // temporary for now
                    .build();
        }
        return instance;
    }

    public void loadData(Context context){

        // if flight table is empty, then load data for  flights

        List<Flight> flight_list = FlightRoom.getFlightRoom(context).dao().getAllFlights();
        if (flight_list.size() == 0) {
            Log.d("FlightRoom", "loading data ");
            loadFlights(context);
        }
        List<User> user_list = FlightRoom.getFlightRoom(context).dao().getAllUsers();
        if (user_list.size() == 0) {
            Log.d("FlightRoom", "loading data ");
            loadUsers(context);
        }
        List<Reservation> reservation_list = FlightRoom.getFlightRoom(context).dao().getAllReservations();
        if (reservation_list.size() == 0) {
            Log.d("FlightRoom", "loading data ");
            loadReservations(context);
        }
        List<LogRecord> record_list = FlightRoom.getFlightRoom(context).dao().getAllLogRecords();
        if (reservation_list.size() == 0) {
            Log.d("FlightRoom", "loading data ");
            loadLogRecords(context);
        }
    }

    private void loadFlights(Context context){
        FlightDao dao = getFlightRoom(context).dao();

        Flight otter101 = new Flight("Otter101", "Monterey", "Los Angeles", "10:30(am)", 10, 150);
        dao.addFlight(otter101);
        Flight otter102 = new Flight("Otter102", "Los Angeles", "Monterey", "1:00(pm)", 10, 150);
        dao.addFlight(otter102);
        Flight otter201 = new Flight("Otter201", "Monterey", "Seattle", "11:00(am)", 5, 200.50);
        dao.addFlight(otter201);
        Flight otter205 = new Flight("Otter205", "Monterey", "Seattle", "3:45(pm)", 15, 150.00);
        dao.addFlight(otter205);
        Flight otter202 = new Flight("Otter202", "Seattle", "Monterey", "2:10(pm)", 5, 200.50);
        dao.addFlight(otter202);
        Log.d("FlightRoom", "5 flights added to database");
    }
    private void loadUsers(Context context) {
        FlightDao dao = getFlightRoom(context).dao();
        User admin = new User("!admiM2", "!admiM2");
        dao.addUser(admin);
        LogRecord log = new LogRecord(1, "New account created", "admiM2");
        dao.addLogRecord(log);
        User Alice = new User("A@lice5", "@cSit100");
        dao.addUser(Alice);
        LogRecord log1 = new LogRecord(1, "New account created", "A@lice5");
        dao.addLogRecord(log1);
        User Brian = new User("$BriAn7", "123aBc##");
        dao.addUser(Brian);
        LogRecord log2 = new LogRecord(1, "New account created", "$BriAn7");
        dao.addLogRecord(log2);
        User Chris = new User("!chriS12!", "CHrIS12!!");
        dao.addUser(Chris);
        LogRecord log3 = new LogRecord(1, "New account created", "!chriS12!");
        dao.addLogRecord(log3);
        Log.d("FlightRoom", "4 users added to database");
    }

    private void loadReservations(Context context) {
        FlightDao dao = getFlightRoom(context).dao();

        User Alice = new User("A@lice5", "@cSit100");
        Flight otter101 = new Flight("Otter101", "Monterey", "Los Angeles", "10:30(am)", 10, 150);
        Reservation reserv = new Reservation(3, otter101, Alice.getUsername());
        dao.addReservation(reserv);
        LogRecord log3 = new LogRecord(2, reserv.toString(), "A@lice5");
        dao.addLogRecord(log3);
        Log.d("FlightRoom", "1 reservation added to database");
    }

    private void loadLogRecords(Context context) {
        FlightDao dao = getFlightRoom(context).dao();
        Log.d("FlightRoom", "LogRecord added to database");
    }
}
