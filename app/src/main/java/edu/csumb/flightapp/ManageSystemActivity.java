package edu.csumb.flightapp;

import android.app.Activity;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.LogRecord;

public class ManageSystemActivity extends AppCompatActivity {
    private static final String MANAGE_SYSTEM_ACTIVITY = "ManageSystemActivity";

    List<LogRecord> records;
    E_Dialog info = new E_Dialog("Message Not Set", 5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MANAGE_SYSTEM_ACTIVITY, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_system);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button addFlightButton = findViewById(R.id.addFlight_button);
        addFlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MANAGE_SYSTEM_ACTIVITY, "onClick add flight called");
                Intent intent = new Intent(ManageSystemActivity.this, AddFlightActivity.class);
                startActivity(intent);
            }
        });

        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MANAGE_SYSTEM_ACTIVITY, "onClick return called");
                Intent intent = new Intent(ManageSystemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        records = FlightRoom.getFlightRoom(this).dao().getAllLogRecords();
        Log.d(MANAGE_SYSTEM_ACTIVITY, "Log record count "+records.size());

        if(records.size() == 0)
        {
            openDialog("There is no log information at the moment.");
        }
        else {
            ListView records_view = findViewById(R.id.record_list);
            records_view.setAdapter(new LogRecordsAdapter(this, records));
        }
    }

    public void openDialog(String message){
        info.setMessage(message);
        info.show(getSupportFragmentManager(),"ok");
    }

    public class LogRecordsAdapter extends ArrayAdapter<LogRecord> {
        public LogRecordsAdapter (Activity context, List<LogRecord> records){
            super(context, R.layout.row_layout , records);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater=ManageSystemActivity.this.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.row_layout, null,true);
            TextView rowField = rowView.findViewById(R.id.row_id);

            //set the value of a row in the ListView to the flight info using toString()
            rowField.setText(records.get(position).toString());
            return rowView;
        }

    }
}
