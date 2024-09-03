package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BirthResultActivity extends AppCompatActivity {
    String pet_ID;

    TextView totalMalePetsTextView, totalFemalePetsTextView, totalPetsTextView;

    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth_result);

        SharedPreferences sharedPreferences = getSharedPreferences("Chat", Context.MODE_PRIVATE);
        pet_ID = sharedPreferences.getString("acceptingPetID", "");

        backBtn = findViewById(R.id.backBtn);
        totalMalePetsTextView = findViewById(R.id.totalMalePetsTextView);
        totalFemalePetsTextView = findViewById(R.id.totalFemalePetsTextView);
        totalPetsTextView = findViewById(R.id.totalPetsTextView);

        retrieveDataBasedOnPetID(pet_ID);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BirthResultActivity.this, BreedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void retrieveDataBasedOnPetID(String petID) {
        // Create a RequestQueue for the HTTP request
        RequestQueue queue = Volley.newRequestQueue(this);

        // Define the URL for your PHP script that will retrieve data based on pet_ID
        String url = "https://pawsomematch.online/android/retrieve_birth_result.php?pet_ID=" + petID;

        // Create a StringRequest to send a GET request
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String totalMalePets = jsonObject.getString("total_male");
                            String totalFemalePets = jsonObject.getString("total_female");
                            String totalPets = jsonObject.getString("total_offspring");

                            totalMalePetsTextView.setText("Total Male Pets: " + totalMalePets);
                            totalFemalePetsTextView.setText("Total Female Pets: " + totalFemalePets);
                            totalPetsTextView.setText("Total Pets: " + totalPets);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(BirthResultActivity.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        queue.add(request);
    }
}