package edu.csumb.flightapp;
import edu.csumb.flightapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import edu.csumb.flightapp.model.FlightDao;
import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.LogRecord;
import edu.csumb.flightapp.model.User;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_ACTIVITY = "LoginActivity";
    private static final String ADMIN = "!admiM2";

    E_Dialog invalid = new E_Dialog("Message Not Set", 2);
    String username;
    String password;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOGIN_ACTIVITY, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FlightDao dao = FlightRoom.getFlightRoom(this).dao();

        Button login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(LOGIN_ACTIVITY, "onClick return called");
                EditText usern = findViewById(R.id.username);
                username = usern.getText().toString().trim();

                EditText pass = findViewById(R.id.password);
                password = pass.getText().toString().trim();
                Log.d(LOGIN_ACTIVITY, "onCreate called " + username + " " + password);

                String checkFlag = getIntent().getStringExtra("flag");

                if (checkFlag.equals("manage")) {//check if we're coming from main menu/manage button
                    if (username.equals(ADMIN) && password.equals(ADMIN)) {
                        Intent intent = new Intent(LoginActivity.this, ManageSystemActivity.class);
                        startActivity(intent);
                    } else {
                        //username and password are both valid, and not duplicates
                        openDialog("Username or Password were inputted incorrectly.\nPlease try again.");
                    }
                } else { //check if we're coming from anywhere else
                    List<User> userList = dao.getUserByUsername(username);
                    Log.d(LOGIN_ACTIVITY, "users count "+userList.size());
                    if(userList.size() > 0){
                        User user = userList.get(0);
                        if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                            Intent intent2 = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt("code", 1);
                            bundle.putString("Username", username);
                            intent2.putExtras(bundle);
                            setResult(RESULT_OK, intent2);
                            finish();
                        }
                        else{
                            openDialog("Username or Password were inputted incorrectly.\nPlease try again.");
                        }
                    } else {
                        //username and password are both valid, and not duplicates
                        openDialog("Username or Password were inputted incorrectly.\nPlease try again.");
                    }
                }
            }
        });
        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(LOGIN_ACTIVITY, "onClick return called");
                finish();
            }
        });
    }
    public void openDialog(String message){
        invalid.setMessage(message);
        invalid.show(getSupportFragmentManager(),"ok");
    }
}