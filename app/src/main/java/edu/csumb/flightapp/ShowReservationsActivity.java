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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import edu.csumb.flightapp.R;
import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.Reservation;

public class ShowReservationsActivity extends AppCompatActivity {
    private static final String SHOWRESERVATIONS_ACTIVITY = "ShowUsersActivity";

    List<Reservation> reservations;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        Log.d(SHOWRESERVATIONS_ACTIVITY, "onCreate called");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_show_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(SHOWRESERVATIONS_ACTIVITY, "onClick return called");
                finish();
            }
        });

        reservations = FlightRoom.getFlightRoom(this).dao().getAllReservations();
        Log.d(SHOWRESERVATIONS_ACTIVITY, "users count" + reservations.size());

        ListView users_view = findViewById(R.id.user_list);
        users_view.setAdapter(new ReservationsListAdapter(this, reservations));
    }

    public class ReservationsListAdapter extends ArrayAdapter<Reservation> {

        public ReservationsListAdapter(Activity context, List<Reservation> reservations){
            super(context,R.layout.row_layout, reservations);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater= ShowReservationsActivity.this.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.row_layout, null,true);
            TextView rowField = rowView.findViewById(R.id.row_id);

            //set the value of a row in the ListView to the flight info using toString()
            rowField.setText(reservations.get(position).toString());
            return rowView;
        }

    }
}
