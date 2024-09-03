package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BreedActivity extends MainActivity {

    String pet_ID;
    String liked_pet_ID;

    PetBreed petBreed;

    Button updateStatusButton;
    Button btnViewDetails;
    ImageView backBtn;

    String pregnancyProgress;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breed);

        SharedPreferences sharedPreferences1 = getSharedPreferences("Chat", Context.MODE_PRIVATE);

        pet_ID = sharedPreferences1.getString("acceptingPetID", "");
        liked_pet_ID = sharedPreferences1.getString("acceptedPetID", "");

        btnViewDetails = findViewById(R.id.btnViewDetails);
        updateStatusButton = findViewById(R.id.updateStatusButton);
        backBtn = findViewById(R.id.backBtn);

        retrievePetStatus(pet_ID, liked_pet_ID);
        retrievePetStatus(liked_pet_ID, pet_ID);
        retrievePetBreedListWithProgressDialog();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BreedActivity.this, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BreedActivity.this, BirthResultActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        updateStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to launch StatusActivity
                Intent intent = new Intent(BreedActivity.this, StatusActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    private void retrievePetBreedListWithProgressDialog() {
        // Initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Gender");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Call the method to retrieve pet breed list
        retrievePetBreedList();
    }

    private List<PetBreed> retrievePetBreedList() {
        String url = "https://pawsomematch.online/android/retrieve_pet_breed.php?acceptingPetID=" + pet_ID + "&acceptedPetID=" + liked_pet_ID;

        final List<PetBreed> petDataList = new ArrayList<>();

        // Create a JsonArrayRequest to send a GET request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Process each pet breed object in the response
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                // Extract pet breed data from the JSON object
                                String user_ID = sharedPreferences.getString("user_id", "");
                                String pet_ID = jsonObject.getString("pet_ID"); // Use the alias "pet_ID"
                                String liked_pet_ID = jsonObject.getString("liked_pet_ID"); // Use the alias "liked_pet_ID"
                                String pet_Name = jsonObject.getString("pet_name");
                                String pet_liked_Name = jsonObject.getString("liked_pet_name");
                                String pet_Gender = jsonObject.getString("pet_gender");
                                String pet_liked_Gender = jsonObject.getString("pet_liked_gender");
                                String pet_Category = jsonObject.getString("pet_category");
                                String pet_liked_Category = jsonObject.getString("pet_liked_category");
                                String pet_Breed = jsonObject.getString("pet_breed");
                                String pet_liked_Breed = jsonObject.getString("pet_liked_breed");
                                String pet_Age = jsonObject.getString("pet_age");
                                String pet_liked_Age = jsonObject.getString("pet_liked_age");
                                String pet_Weight = jsonObject.getString("pet_weight");
                                String pet_liked_Weight = jsonObject.getString("pet_liked_weight");
                                String pet_Color = jsonObject.getString("pet_color");
                                String pet_liked_Color = jsonObject.getString("pet_liked_color");
                                String pet_Breedtype = jsonObject.getString("pet_breedtype");
                                String pet_liked_Breedtype = jsonObject.getString("pet_liked_breedtype");

                                // Create a PetBreed object and set its values

                                petBreed = new PetBreed(user_ID, pet_ID, liked_pet_ID, pet_Name, pet_liked_Name,
                                        pet_Gender, pet_liked_Gender, pet_Category, pet_liked_Category, pet_Breed, pet_liked_Breed,
                                        pet_Age, pet_liked_Age, pet_Weight, pet_liked_Weight, pet_Color, pet_liked_Color,
                                        pet_Breedtype, pet_liked_Breedtype);

                                // Add the PetBreed object to the list
                                petDataList.add(petBreed);

                                TextView petIdValueTextView = findViewById(R.id.petIdValueTextView);
                                TextView likedPetIdValueTextView = findViewById(R.id.likedPetIdValueTextView);
                                TextView petNameValueTextView = findViewById(R.id.petNameValueTextView);
                                TextView likedPetNameValueTextView = findViewById(R.id.likedPetNameValueTextView);
                                TextView petGenderValueTextView = findViewById(R.id.petGenderValueTextView);
                                TextView likedPetGenderValueTextView = findViewById(R.id.likedPetGenderValueTextView);
                                TextView petCategoryValueTextView = findViewById(R.id.petCategoryValueTextView);
                                TextView likedPetCategoryValueTextView = findViewById(R.id.likedPetCategoryValueTextView);
                                TextView petBreedValueTextView = findViewById(R.id.petBreedValueTextView);
                                TextView likedPetBreedValueTextView = findViewById(R.id.likedPetBreedValueTextView);
                                TextView petAgeValueTextView = findViewById(R.id.petAgeValueTextView);
                                TextView likedPetAgeValueTextView = findViewById(R.id.likedPetAgeValueTextView);
                                TextView petWeightValueTextView = findViewById(R.id.petWeightValueTextView);
                                TextView likedPetWeightValueTextView = findViewById(R.id.likedPetWeightValueTextView);
                                TextView petColorValueTextView = findViewById(R.id.petColorValueTextView);
                                TextView likedPetColorValueTextView = findViewById(R.id.likedPetColorValueTextView);
                                TextView petBreedtypeValueTextView = findViewById(R.id.petBreedtypeValueTextView);
                                TextView likedPetBreedtypeValueTextView = findViewById(R.id.likedPetBreedtypeValueTextView);

                                // Set the values of pet_ID and liked_pet_ID
                                petIdValueTextView.setText(petBreed.getPet_ID());
                                likedPetIdValueTextView.setText(petBreed.getLiked_pet_ID());
                                petNameValueTextView.setText(petBreed.getPet_Name());
                                likedPetNameValueTextView.setText(petBreed.getPet_liked_Name());
                                petGenderValueTextView.setText(petBreed.getPet_Gender());
                                likedPetGenderValueTextView.setText(petBreed.getPet_liked_Gender());
                                petCategoryValueTextView.setText(petBreed.getPet_Category());
                                likedPetCategoryValueTextView.setText(petBreed.getPet_liked_Category());
                                petBreedValueTextView.setText(petBreed.getPet_Breed());
                                likedPetBreedValueTextView.setText(petBreed.getPet_liked_Breed());
                                petAgeValueTextView.setText(petBreed.getPet_Age());
                                likedPetAgeValueTextView.setText(petBreed.getPet_liked_Age());
                                petWeightValueTextView.setText(petBreed.getPet_Weight());
                                likedPetWeightValueTextView.setText(petBreed.getPet_liked_Weight());
                                petColorValueTextView.setText(petBreed.getPet_Color());
                                likedPetColorValueTextView.setText(petBreed.getPet_liked_Color());
                                petBreedtypeValueTextView.setText(petBreed.getPet_Breedtype());
                                likedPetBreedtypeValueTextView.setText(petBreed.getPet_liked_Breedtype());


                                updateUIBasedOnGender(pet_Gender);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            // Dismiss the progress dialog once the data is processed
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
                    }
                });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(jsonArrayRequest);

        return petDataList;
    }

    private void updateUIBasedOnGender(String petGender) {
        if ("Done Giving Birth".equals(pregnancyProgress)) {
            // If it's done giving birth, hide the updateStatusButton and show btnViewDetails
            updateStatusButton.setVisibility(View.GONE);
            btnViewDetails.setVisibility(View.VISIBLE);
        } else {
            // If it's not done giving birth, handle the gender condition
            if ("Male".equals(petGender)) {
                updateStatusButton.setVisibility(View.GONE);
            } else if ("Female".equals(petGender)) {
                updateStatusButton.setVisibility(View.VISIBLE);
            }
        }
    }
    public void retrievePetStatus(String petID, String likedPetID) {

        String phpUrl = "https://pawsomematch.online/android/retrieve_status.php?pet_ID=" + petID + "&liked_pet_ID=" + likedPetID;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                phpUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Check if the response is "No data found"
                            if (response.has("error") && response.getString("error").equals("No data found.")) {
                                // Handle the case where no data is found
                                Log.e("Data Error", "No data found.");
                            } else {
                                // Retrieve the values
                                String pregnancyStatus = response.getString("pregnancy_status");
                                String healthStatus = response.getString("health_status");
                                String healthNote = response.getString("health_note");
                                pregnancyProgress = response.getString("pregnancy_process");

                                // Update TextViews with the retrieved data
                                TextView pregnancyStatusTextView = findViewById(R.id.statusDescription);
                                TextView healthStatusTextView = findViewById(R.id.healthDescription);
                                TextView healthNoteTextView = findViewById(R.id.noteDescription);
                                TextView pregnancyProgressTextView = findViewById(R.id.pregnancyProcessDescription);


                                pregnancyStatusTextView.setText(pregnancyStatus);
                                healthStatusTextView.setText(healthStatus);
                                healthNoteTextView.setText(healthNote);
                                pregnancyProgressTextView.setText(pregnancyProgress);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                            Log.e("JSON Parsing Error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}