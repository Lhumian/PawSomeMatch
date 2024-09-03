package com.petopia.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.os.Bundle;
import android.util.Log;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PusherOptions options = new PusherOptions().setCluster("ap1");
        Pusher pusher = new Pusher("893ee8a8d6c71e89dec7", options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe("my-channel");

        channel.bind("my-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                Log.i("Pusher", "Received event with data: " + event.toString());
            }
        });

        // Initialize SharedPreferences
        init();
    }

    Context context;
    Intent intent;
    SharedPreferences sharedPreferences;
    String SHARED_PREF_NAME ="user_pref";
    SharedPreferences.Editor sharedPrefEditor;

    protected String name,email,password, first_name, middle_name, last_name, genderValue;
    protected String user_id, pet_name, pet, vaccine, category, breed,  age, weight, vaccine_status, gender, color, description, breedtype, studfee, shares;
    protected float price;

    protected boolean isLoggedIn(){
        return sharedPreferences.getBoolean("login",false);
    }



    protected void logout(){
        sharedPrefEditor.putBoolean("login",false);
        sharedPrefEditor.apply();
        sharedPrefEditor.commit();
    }


    public boolean isEmailValid(String email) {
        // Define a regular expression for email formats from Gmail, Yahoo, and Outlook
        String emailRegex = "^[A-Za-z0-9+_.-]+@(gmail\\.com|yahoo\\.com|outlook\\.com)$";

        // Create a pattern object to compile the regular expression
        Pattern pattern = Pattern.compile(emailRegex);

        // Create a matcher object to match the input email against the pattern
        Matcher matcher = pattern.matcher(email);

        // Check if the email matches the pattern
        return matcher.matches();
    }

    public void init() {
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        sharedPrefEditor = sharedPreferences.edit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
