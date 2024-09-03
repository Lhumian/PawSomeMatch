package com.petopia.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.widget.Toast;
import androidx.viewpager.widget.ViewPager;

public class BrowsePet extends MainActivity {
    ListView listView;
    ViewPager viewPager;
    BrowseAdapter adapter;
    public static ArrayList<SellPets> petsSellArrayList = new ArrayList<>();
    String url = "https://pawsomematch.online/android/retrievesellingpets.php";
    SellPets pets;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        context = this;
        setContentView(R.layout.activity_browse_pet);

        viewPager = findViewById(R.id.viewPager);
        adapter = new BrowseAdapter(this, petsSellArrayList);
        viewPager.setAdapter(adapter);

        retrieveSellPets();

        ImageView btnCategory = findViewById(R.id.btnCategory);
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrowsePet.this, CategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void retrieveSellPets() {
        // Retrieve the email from shared preferences
        String email = sharedPreferences.getString("email","");

        // Create a new RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        // Define the URL for the PHP file that will retrieve the pet data from the database
        String url = "https://pawsomematch.online/android/retrievebrowsepets.php";
        // Create a StringRequest object to send a POST request to the server
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        processResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Display an error message
                Toast.makeText(BrowsePet.this, "Error retrieving pet", Toast.LENGTH_SHORT).show();
                Log.e("Volley", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add the email to the POST request parameters
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        queue.add(request);
    }


    private void processResponse(String response) {
        petsSellArrayList.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String success = jsonObject.getString("success");
            JSONArray jsonArray = jsonObject.getJSONArray("selling");

            if (success.equals("1")) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    String pet_ID = object.getString("pet_ID");
                    String pet_name = object.getString("pet_name");
                    String category = object.getString("category");
                    String breed = object.getString("breed");
                    String age = object.getString("age");
                    String weight = object.getString("weight");
                    String vaccine_status = object.getString("vaccine_status");
                    String gender = object.getString("gender");
                    String color = object.getString("color");
                    String breedtype = object.getString("breedtype");
                    float price = Float.parseFloat(object.getString("price"));
                    String description = object.getString("description");

                    pets = new SellPets(pet_ID, pet_name, category, breed, age, weight, vaccine_status, gender, color, breedtype, description, price);
                    petsSellArrayList.add(pets);
                    adapter.notifyDataSetChanged();

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
