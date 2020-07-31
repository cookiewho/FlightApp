package edu.csumb.flightapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.List;

import edu.csumb.flightapp.model.Flight;
import edu.csumb.flightapp.model.LogRecord;
import edu.csumb.flightapp.model.FlightDao;
import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.User;

public class CreateAccountActivity  extends AppCompatActivity {

    private static final String CREATE_ACCOUNT_ACTIVITY = "CreateAccountActivity";

    E_Dialog invalid = new E_Dialog("Message Not Set",2);
    String username;
    String password;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(CREATE_ACCOUNT_ACTIVITY, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FlightDao dao = FlightRoom.getFlightRoom(this).dao();

        FlightRoom room = new FlightRoom() {
            @Override
            public FlightDao dao() {
                return null;
            }

            @NonNull
            @Override
            protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
                return null;
            }

            @NonNull
            @Override
            protected InvalidationTracker createInvalidationTracker() {
                return null;
            }

            @Override
            public void clearAllTables() {

            }
        };

        Button create_button = findViewById(R.id.create_account_button);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(CREATE_ACCOUNT_ACTIVITY, "onClick return called");
                    EditText usern = findViewById(R.id.username);
                    username = usern.getText().toString().trim();

                    EditText pass = findViewById(R.id.password);
                    password = pass.getText().toString().trim();

                if ((!hasChar(username) || !hasChar(password))) {
                        //check if username and pasword are valid
                        openDialog("Username or Password are Invalid.\nPlease try again.");
                    } else { //checks if user is a duplicate
                        List temp = dao.getUserByUsername(username);
                        if (temp.size() >= 1) {
                            openDialog("Username is already taken.\nPlease try again.");
                        } else {
                            //username and password are both valid, and not duplicates
                            User user = new User(username, password);
                            dao.addUser(user);
                            LogRecord newUsr_log = new LogRecord(1, "New account created", username);
                            dao.addLogRecord(newUsr_log);
                            AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
                            builder.setTitle("Account Created");
                            builder.setMessage("Welcome "+ user.getUsername()+"\nYour account has been created successfully!");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { finish();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                //make it so that it repeats.
            }
        });
        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(CREATE_ACCOUNT_ACTIVITY, "onClick return called");
                finish();
            }
        });

    }
    public void openDialog(String message){
        invalid.setMessage(message);
        invalid.show(getSupportFragmentManager(),"ok");
    }

    private boolean hasChar(String name) {
            String special_chars = "!@#$s";
            boolean num_p = false;
            boolean upCase_p = false;
            boolean lowCase_p = false;
            boolean specChar_p = false;
            char curr;

            for(int x = 0; x<name.length();x++){
                curr = name.charAt(x);
                if(Character.isDigit(curr)){num_p = true;}
                else if(Character.isUpperCase(curr)){upCase_p = true;}
                else if(Character.isUpperCase(curr)){upCase_p = true;}
                else if(Character.isLowerCase(curr)){lowCase_p = true;}
                else if(special_chars.contains(String.valueOf(curr))){specChar_p = true;}
            }

            return num_p && upCase_p && lowCase_p && specChar_p;

    }
}