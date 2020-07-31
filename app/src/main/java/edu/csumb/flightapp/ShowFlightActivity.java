package edu.csumb.flightapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import edu.csumb.flightapp.model.Flight;
import edu.csumb.flightapp.model.FlightRoom;

/*
Example of an Activity that uses a ListView to display a list of Flights
An inner class FlightListAdapter, adapts the Flights array
to the rows in the ListView.
 */
public class ShowFlightActivity extends AppCompatActivity {

    private static final String SHOWFLIGHT_ACTIVITY = "ShowFlightActivity";

    List<Flight> flights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(SHOWFLIGHT_ACTIVITY, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_flight);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(SHOWFLIGHT_ACTIVITY, "onClick return called");
                finish();
            }
        });

        // get all flights from database and assign to flights array
        flights = FlightRoom.getFlightRoom(this).dao().getAllFlights();
        Log.d(SHOWFLIGHT_ACTIVITY, "flights count "+flights.size());

        ListView flights_view = findViewById(R.id.flight_list);
        flights_view.setAdapter( new FlightListAdapter( this, flights) );
    }

    public class FlightListAdapter extends ArrayAdapter<Flight> {

        public FlightListAdapter (Activity context, List<Flight> flights){
            super(context, R.layout.row_layout , flights);
        }

        /*
           given an index into the flights array - position
           create a rowView and set TextView contained in the row
           to the flight data.
         */
        @Override
        public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater=ShowFlightActivity.this.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.row_layout, null,true);
            TextView rowField = rowView.findViewById(R.id.row_id);

            //set the value of a row in the ListView to the flight info using toString()
            rowField.setText(flights.get(position).toString());
            return rowView;
        }

    }
}
