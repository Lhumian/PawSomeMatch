package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryBrowsePet extends MainActivity {
    String category;
    String breed;
    String gender;
    String color;
    String breedtype;

    ViewPager viewPager;
    BrowseAdapter adapter;
    public static ArrayList<SellPets> petsSellArrayList = new ArrayList<>();
    SellPets pets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        context = this;
        setContentView(R.layout.activity_category_browse_pet);

        petsSellArrayList.clear();

        category = getIntent().getStringExtra("category");
        breed = getIntent().getStringExtra("breed");
        gender = getIntent().getStringExtra("gender");
        color = getIntent().getStringExtra("color");
        breedtype = getIntent().getStringExtra("breedtype");

        viewPager = findViewById(R.id.viewPager);
        adapter = new BrowseAdapter(this, petsSellArrayList);
        viewPager.setAdapter(adapter);

        retrieveSellPets(category, breed, gender, color, breedtype);

        ImageView btnCategory = findViewById(R.id.btnCategory);

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryBrowsePet.this, CategoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

    }

    private void retrieveSellPets(String category, String breed, String gender, String color, String breedtype) {
        // Retrieve the email from shared preferences
        String email = sharedPreferences.getString("email", "");

        // Create a new RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Define the URL for the PHP file that will retrieve the pet data from the database
        String url = "https://pawsomematch.online/android/retrievecategorybrowsepets.php";

        // Create a StringRequest object to send a POST request to the server
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log the response for debugging
                        Log.d("Response", response);
                        // Handle the response from the server
                        processResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Display an error message
                Toast.makeText(CategoryBrowsePet.this, "Error retrieving pet", Toast.LENGTH_SHORT).show();
                Log.e("Volley", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add the email and filter values to the POST request parameters
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("category", category);
                params.put("breed", breed);
                params.put("gender", gender);
                params.put("color", color);
                params.put("breedtype", breedtype);
                return params;
            }
        };
        queue.add(request);
    }

    private void processResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String success = jsonObject.getString("success");

            if (success.equals("1")) {
                JSONArray jsonArray = jsonObject.getJSONArray("selling");
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
                }
            }

            adapter.notifyDataSetChanged(); // Notify the adapter to update the UI
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(CategoryBrowsePet.this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }
}

