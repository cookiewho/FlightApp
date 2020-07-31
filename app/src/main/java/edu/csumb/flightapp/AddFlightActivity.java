package edu.csumb.flightapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import edu.csumb.flightapp.model.Flight;
import edu.csumb.flightapp.model.FlightDao;
import edu.csumb.flightapp.model.FlightRoom;

public class AddFlightActivity extends AppCompatActivity {

    private static final String ADD_FLIGHT_ACTIVITY = "AddFlightActivity";

    E_Dialog invalid = new E_Dialog("Message Not Set", 5);
    String flightNo;
    String departure;
    String arrival;
    String departureTime;
    int capacity;
    double price;
    int availableSeats;
    AlertDialog dialog;
    final FlightDao dao = FlightRoom.getFlightRoom(this).dao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(ADD_FLIGHT_ACTIVITY, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flight);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Button create_button = findViewById(R.id.addFlight_button);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(ADD_FLIGHT_ACTIVITY, "onClick add flight called");
                try {
                    EditText flNo = findViewById(R.id.flightNo);
                    flightNo = flNo.getText().toString().trim();

                    EditText dep = findViewById(R.id.departure);
                    departure = dep.getText().toString().trim();

                    EditText arr = findViewById(R.id.arrival);
                    arrival = arr.getText().toString().trim();

                    EditText dTime = findViewById(R.id.DTime);
                    departureTime = dTime.getText().toString().trim();

                    EditText seats = findViewById(R.id.seats);
                    capacity = Integer.parseInt(seats.getText().toString().trim());

                    EditText pr = findViewById(R.id.price);
                    price = Double.parseDouble(pr.getText().toString().trim());

                    List temp = dao.searchDuplicateFlight(flightNo);
                    if((hasAllInfo(flightNo,departure,arrival,departureTime)) && (temp.size() <1))
                    {
                        Flight flight = new Flight(flightNo,departure,arrival,departureTime,capacity,price);
                        confirm(flight).show();
                    }
                    else{
                        openDialog("Information entered is invalid. Try again.");
                    }
                }
                catch (NumberFormatException nfe){
                    openDialog("Information entered is invalid. Try again.");
                }

            }
        });
        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(ADD_FLIGHT_ACTIVITY, "onClick return called");
                finish();
            }
        });

    }
    public Dialog confirm(final Flight flight){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Confirm Information").setMessage(flight.toString2()).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dao.addFlight(flight);
                Intent intent = new Intent(AddFlightActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }
    public void openDialog(String message){
        invalid.setMessage(message);
        invalid.show(getSupportFragmentManager(),"ok");
    }


    private boolean hasAllInfo(String flightNo, String departure, String arrival, String departureTime) {
        boolean flNo = false;
        boolean dep = false;
        boolean arr = false;
        boolean dTime = false;

        if(flightNo.length() > 0) {flNo = true;}
        if(departure.length() > 0) {dep = true;}
        if(arrival.length() > 0) {arr = true;}
        if(departureTime.length() > 0) {dTime = true;}

        return flNo && dep && arr && dTime;

    }
}