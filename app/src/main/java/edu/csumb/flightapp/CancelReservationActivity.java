package edu.csumb.flightapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

import edu.csumb.flightapp.model.Flight;
import edu.csumb.flightapp.model.FlightDao;
import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.LogRecord;
import edu.csumb.flightapp.model.Reservation;

public class CancelReservationActivity extends AppCompatActivity {
    private static final String CANCEL_RES_ACTIVITY = "CancelReservActivity";
    final FlightDao dao = FlightRoom.getFlightRoom(this).dao();
    E_Dialog invalid = new E_Dialog("Message Not Set", 1);
    private CancelReservationAdapter adapter;
    List<Reservation> reservations;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reservation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // login the user before continuing.
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("flag", "cancelRes");
        this.startActivityForResult(intent, 0);
        return;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            int rc = bundle.getInt("code");
            if (rc == 1) {
                username = data.getStringExtra("Username");
                    continueCreate();
                    return;
            }
        }
        // login was unsuccessful or user is not admin
        finish();
    }

    private void continueCreate() {

        reservations = dao.getReservationsByUsername(username);
        Log.d(CANCEL_RES_ACTIVITY, "reservations count " + reservations.size());
        if (reservations.size() < 1){
            openDialog("There are no reservations under this account at the moment.");
        }
        else {
            ListView reservation_view = findViewById(R.id.reserv_list);
            adapter = new CancelReservationAdapter(this, reservations);
            reservation_view.setAdapter(adapter);
        }

        Button return_main_button = findViewById(R.id.return_button2);
        return_main_button.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(CANCEL_RES_ACTIVITY, "onClick return called");
                finish();
            }
        });
    }
    public class CancelReservationAdapter extends ArrayAdapter<Reservation> {

        public CancelReservationAdapter(Activity context, List<Reservation> reservations){
            super(context,R.layout.row_layout, reservations);
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            LayoutInflater inflater= CancelReservationActivity.this.getLayoutInflater();
            View itemView=inflater.inflate(R.layout.reservation_item, null,true);
            TextView resNoview = itemView.findViewById(R.id.reservNo);
            TextView flightNoview = itemView.findViewById(R.id.flNo);
            TextView departureview = itemView.findViewById(R.id.depa);
            TextView arrivalview = itemView.findViewById(R.id.arr);
            TextView departureTimeview = itemView.findViewById(R.id.deptime);
            TextView ammountview = itemView.findViewById(R.id.ammount);
            final Reservation resv = reservations.get(position);
            resNoview.setText(Long.toString(resv.getReservation_id()));
            flightNoview.setText(resv.getFlightNo());
            departureview.setText(resv.getDeparture());
            arrivalview.setText(resv.getArrival());
            departureTimeview.setText(resv.getDepartureTime());
            ammountview.setText(Integer.toString(resv.getAmmount()));

            //set the value of a row in the ListView to the flight info using toString()
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // confirm dialog to delete this history item
                    AlertDialog.Builder builder = new AlertDialog.Builder(CancelReservationActivity.this);
                    builder.setTitle("Confim Cancelation");
                    builder.setMessage("Are you sure you want to cancel reservation No: "+ reservations.get(position).getReservation_id() +"?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // user selected yes
                            dao.deleteReservation(resv);
                            LogRecord newUsr_log = new LogRecord(3, resv.toString(), username);
                            dao.addLogRecord(newUsr_log);
                            reservations.remove(resv);
                            // refresh UI
                            adapter.notifyDataSetChanged();
                            finish();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // user select no, do nothing
                                openDialog(("The cancellation has failed"));
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            return itemView;
        }
    }
    public void openDialog(String message){
        invalid.setMessage(message);
        invalid.show(getSupportFragmentManager(),"ok");
    }
}

