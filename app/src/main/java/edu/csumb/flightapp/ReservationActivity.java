package edu.csumb.flightapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.net.URL;
import java.util.List;
import java.util.Scanner;

import edu.csumb.flightapp.domain.Weather;
import edu.csumb.flightapp.model.Flight;
import edu.csumb.flightapp.model.FlightDao;
import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.LogRecord;
import edu.csumb.flightapp.model.Reservation;

public class ReservationActivity extends AppCompatActivity {

    private static final String RESERVATION_ACTIVITY = "ReservationActivity";

    E_Dialog invalid = new E_Dialog("Message Not Set", 3);
    final FlightDao dao = FlightRoom.getFlightRoom(this).dao();
    private ReservationAdapter adapter;
    List<Flight> flights;
    String departure;
    String arrival;
    String departureTime;
    int numTickets;
    String username;
    Flight fin_fli;

    AlertDialog dialog;

    static final String OPENWEATHER = "https://api.openweathermap.org/data/2.5/weather";
    static final String APIKEY = "4cdc90111e0528db2d929c9090ee6e3c";
    String cityName;
    String temperature;
    String condition;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(RESERVATION_ACTIVITY, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();

        Button create_button = findViewById(R.id.show_flights_button);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(RESERVATION_ACTIVITY, "onClick add reservation called");
                try {
                    EditText dep = findViewById(R.id.depart);
                    departure = dep.getText().toString().trim();

                    EditText arr = findViewById(R.id.arrive);
                    arrival = arr.getText().toString().trim();

                    EditText num = findViewById(R.id.amnt);
                    numTickets = Integer.parseInt(num.getText().toString().trim());

                    if (hasAllInfo(departure, arrival) && (7 > numTickets && numTickets > 0)) {
                        flights = dao.searchFlight(departure, arrival);
                        Log.d(RESERVATION_ACTIVITY, "reservations count " + flights.size());
                        if (flights.size() < 1) {
                            openDialog("No available flights with inputted information");
                        } else {
                            ListView rv = findViewById(R.id.fly_list);
                            adapter = new ReservationAdapter(ReservationActivity.this, numTickets);
                            adapter.notifyDataSetChanged();
                            rv.setAdapter(adapter);
                        }
                    } else {
                        openDialog("Information entered is invalid. Try again.");
                    }
                } catch (NumberFormatException nfe) {
                    openDialog("Information entered is invalid. Try again.");
                }
            }
        });
        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(RESERVATION_ACTIVITY, "onClick return called");
                finish();
            }
        });

    }

    public void openDialog(String message) {
        invalid.setMessage(message);
        invalid.show(getSupportFragmentManager(), "ok");
    }

    private boolean hasAllInfo(String departure, String arrival) {
        boolean dep = false;
        boolean arr = false;
        if (departure.length() > 0) {
            dep = true;
        }
        if (arrival.length() > 0) {
            arr = true;
        }

        return dep && arr;
    }

    private class ReservationAdapter extends ArrayAdapter<Flight> {
        private int ammount;

        private ReservationAdapter(Activity context, int ammount) {
            super(context, R.layout.reservation_item, flights);
            this.ammount = ammount;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = ReservationActivity.this.getLayoutInflater();
            View itemView = inflater.inflate(R.layout.flight_item, null, true);
            TextView flightNoview = itemView.findViewById(R.id.flNo);
            TextView departureview = itemView.findViewById(R.id.dep);
            TextView arrivalview = itemView.findViewById(R.id.arr);
            TextView departureTimeview = itemView.findViewById(R.id.deptime);
            TextView capacityview = itemView.findViewById(R.id.cap);
            TextView priceview = itemView.findViewById(R.id.pri);
            final Flight flt = flights.get(position);
            flightNoview.setText(flt.getFlightNo());
            departureview.setText(flt.getDeparture());
            arrivalview.setText(flt.getArrival());
            departureTimeview.setText(flt.getDepartureTime());
            capacityview.setText(String.format("%d seats", flt.getCapacity()));
            priceview.setText(String.format("$%.2f", flt.getPrice()));
            //set the value of a row in the ListView to the flight info using toString()

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cityName = flt.getArrival();
                    Thread t = new Thread(new WorkerTask());
                    t.start();
                    fin_fli = flt;
                    Intent intent = new Intent(ReservationActivity.this, LoginActivity.class);
                    intent.putExtra("flag", "createRes");
                    startActivityForResult(intent, 0);
                    return;
                }
            });
            return itemView;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            int rc = bundle.getInt("code");
            if (rc == 1) {
                username = data.getStringExtra("Username");
                confirm();
                return;
            }
        }
        // login was unsuccessful or user is not admin
        openDialog("Login Unsuccessful.\nPlease try again.");
    }

    public void confirm() {
        final Reservation r = new Reservation(numTickets, fin_fli, username);
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
        builder.setTitle("Confim Reservation");
        builder.setMessage("Are you sure you want to reserve the flight, with the following information?\n" +
                r.toString2() + "\nCurrent Conditions: \nTemp: " + temperature + "\nConditions: " + condition);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user selected yes
                dao.addReservation(r);
                LogRecord newUsr_log = new LogRecord(2, r.toString2(), username);
                dao.addLogRecord(newUsr_log);
                // refresh UI
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user select no, do nothing
                openDialog(("The reservation has was canceled"));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }
    public class WorkerTask implements Runnable {
        String message_text;
        String condition_text;

        @Override
        public void run() {
            Log.d(RESERVATION_ACTIVITY, "WorkerTask started.");
            try {
                // get weather data for city.  reply is JSON string.
                String weather_json = getWeather(cityName);
                // extract necessary data from reply string and convert to double

                Gson gson = new Gson();
                Weather weather = gson.fromJson(weather_json, Weather.class);

                double deg_F = (weather.getMain().getTemp() - 273.15)*9.0/5.0+32.0;
                message_text = String.format("%.0f \u00B0F", deg_F);
                condition_text = String.valueOf(weather.getWeather()[0].getDescription());

            } catch (Exception e) {
                Log.d(RESERVATION_ACTIVITY, "exception WorkerTask.run " + e.getMessage());
                message_text = "Error. Enter a valid city name.";
            }
            //  we are done. so dismiss the progress dialog
            //  and update layout textview with current temp
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(RESERVATION_ACTIVITY, "handler called.");
                    temperature = message_text;
                    condition = condition_text;
                    // dismiss the dialog and let user enter another request
                }
            });
            Log.d(RESERVATION_ACTIVITY, "WorkerTask ending.");
        }
        private String getWeather(String city) {
            Scanner reader = null;
            String urlstring = OPENWEATHER+"?q=" + city + "&appid=" + APIKEY;
            Log.d(RESERVATION_ACTIVITY, "getWeather " + urlstring);

            try {
                URL url = new URL(urlstring);
                reader = new Scanner(url.openConnection().getInputStream());
                StringBuffer sb = new StringBuffer();
                while (reader.hasNext())
                    sb.append(reader.nextLine());
                Log.d(RESERVATION_ACTIVITY, "getWeather end");
                String result = sb.toString();
                Log.d(RESERVATION_ACTIVITY, result);
                return result;
            } catch (Exception e) {
                Log.d(RESERVATION_ACTIVITY, "getWeather exception " + e.getMessage());
                return null;
            } finally {
                if (reader != null) reader.close();
            }
        }
    }
}
