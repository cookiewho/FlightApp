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

import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.User;

public class ShowUsersActivity extends AppCompatActivity {
    private static final String SHOWUSERS_ACTIVITY = "ShowUsersActivity";

    List<User> users;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        Log.d(SHOWUSERS_ACTIVITY, "onCreate called");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_show_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(SHOWUSERS_ACTIVITY, "onClick return called");
                finish();
            }
        });

        users = FlightRoom.getFlightRoom(this).dao().getAllUsers();
        Log.d(SHOWUSERS_ACTIVITY, "users count" + users.size());

        ListView users_view = findViewById(R.id.user_list);
        users_view.setAdapter(new UserListAdapter(this, users));
    }

    public class UserListAdapter extends ArrayAdapter<User>{
        public  UserListAdapter(Activity context, List<User> users){
            super(context,R.layout.row_layout, users);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater=ShowUsersActivity.this.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.row_layout, null,true);
            TextView rowField = rowView.findViewById(R.id.row_id);

            //set the value of a row in the ListView to the flight info using toString()
            rowField.setText(users.get(position).toString());
            return rowView;
        }

    }
}
