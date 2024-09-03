package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentBreedActivity extends MainActivity implements AdapterView.OnItemSelectedListener {

    String petId;
    String category;

    Spinner speciesSpinnerMother;
    Spinner speciesSpinnerFather;

    Spinner breedSpinnerMother;
    Spinner breedSpinnerFather;

    Spinner breedtypeSpinnerMother;
    Spinner breedtypeSpinnerFather;

    private List<Category> speciesList;
    private List<Breed> breedList;
    private int selectedSpeciesId;
    private int selectedBreedId;
    private String preSelectedBreed;
    String selectedMotherCategory;
    String selectedMotherBreed;
    String selectedMotherBreedtype;
    String selectedFatherCategory;
    String selectedFatherBreed;
    String selectedFatherBreedtype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_breed);

        Intent intent = getIntent();
        petId = intent.getStringExtra("petId");
        category = intent.getStringExtra("category");
        preSelectedBreed = intent.getStringExtra("breed");

        speciesList = new ArrayList<>();
        breedList = new ArrayList<>();

        speciesSpinnerMother = findViewById(R.id.species_spinnerMother);
        speciesSpinnerFather = findViewById(R.id.species_spinnerFather);
        speciesSpinnerMother.setOnItemSelectedListener(this);
        speciesSpinnerFather.setOnItemSelectedListener(this);

        breedSpinnerMother = findViewById(R.id.breed_spinnerMother);
        breedSpinnerFather = findViewById(R.id.breed_spinnerFather);
        breedSpinnerFather.setOnItemSelectedListener(this);
        breedSpinnerMother.setOnItemSelectedListener(this);

        breedtypeSpinnerMother = findViewById(R.id.breedtype_spinnerMother);
        breedtypeSpinnerFather = findViewById(R.id.breedtype_spinnerFather);
        breedtypeSpinnerMother.setOnItemSelectedListener(this);
        breedtypeSpinnerFather.setOnItemSelectedListener(this);

        loadSpeciesData();
        loadSpeciesData1();

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMotherCategory = speciesSpinnerMother.getSelectedItem().toString().trim();
                selectedMotherBreed = breedSpinnerMother.getSelectedItem().toString().trim();
                selectedMotherBreedtype = breedtypeSpinnerMother.getSelectedItem().toString().trim();

                selectedFatherCategory = speciesSpinnerFather.getSelectedItem().toString().trim();
                selectedFatherBreed = breedSpinnerFather.getSelectedItem().toString().trim();
                selectedFatherBreedtype = breedtypeSpinnerFather.getSelectedItem().toString().trim();
                updateParentData();
            }
        });
    }

    private void loadSpeciesData() {
        // Make a GET request to the PHP file that retrieves the species data from the server
        String url = "https://pawsomematch.online/android/get_species.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response to an array of Species objects
                            JSONArray jsonArray = new JSONArray(response);
                            speciesList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Category species = new Category(jsonObject.getInt("cat_ID"), jsonObject.getString("category"));
                                speciesList.add(species);
                            }

                            // Set up the species spinner with the loaded data
                            ArrayAdapter<Category> speciesAdapter = new ArrayAdapter<>(ParentBreedActivity.this,
                                    android.R.layout.simple_spinner_item, speciesList);
                            speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            speciesSpinnerMother.setAdapter(speciesAdapter);

                            // Load the breed data for the initially selected species
                            int selectedSpeciesId = ((Category) speciesSpinnerMother.getSelectedItem()).getId();
                            loadBreedData(selectedSpeciesId, category);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ParentBreedActivity.this, "Error loading species data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ParentBreedActivity.this, "Error loading species data", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadBreedData(int category_ID, String preSelectedCategory) {
        // Make a GET request to the PHP file that retrieves the breed data for the selected species from the server
        String url = "https://pawsomematch.online/android/get_breeds.php?category_ID=" + category_ID;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response to an array of Breed objects
                            JSONArray jsonArray = new JSONArray(response);
                            breedList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Breed breed = new Breed(jsonObject.getInt("breed_ID"), jsonObject.getString("breed"),
                                        jsonObject.getInt("category_ID"));
                                breedList.add(breed);
                            }

                            // Set up the breed spinner with the loaded data
                            ArrayAdapter<Breed> breedAdapter = new ArrayAdapter<>(ParentBreedActivity.this,
                                    android.R.layout.simple_spinner_item, breedList);
                            breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            breedSpinnerMother.setAdapter(breedAdapter);

                            // Set the pre-selected category and breed in the spinners
                            int categoryPosition = -1;
                            int breedPosition = -1;
                            for (int i = 0; i < speciesList.size(); i++) {
                                if (speciesList.get(i).getName().equals(preSelectedCategory)) {
                                    categoryPosition = i;
                                    break;
                                }
                            }

                            for (int i = 0; i < breedList.size(); i++) {
                                if (breedList.get(i).getName().equals(preSelectedBreed)) {
                                    breedPosition = i;
                                    break;
                                }
                            }

                            if (categoryPosition != -1) {
                                speciesSpinnerMother.setSelection(categoryPosition);
                            }
                            if (breedPosition != -1) {
                                breedSpinnerMother.setSelection(breedPosition);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ParentBreedActivity.this, "Error loading breed data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ParentBreedActivity.this, "Error loading breed data", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadSpeciesData1() {
        // Make a GET request to the PHP file that retrieves the species data from the server
        String url = "https://pawsomematch.online/android/get_species.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response to an array of Species objects
                            JSONArray jsonArray = new JSONArray(response);
                            speciesList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Category species = new Category(jsonObject.getInt("cat_ID"), jsonObject.getString("category"));
                                speciesList.add(species);
                            }

                            // Set up the species spinner with the loaded data
                            ArrayAdapter<Category> speciesAdapter = new ArrayAdapter<>(ParentBreedActivity.this,
                                    android.R.layout.simple_spinner_item, speciesList);
                            speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            speciesSpinnerFather.setAdapter(speciesAdapter);

                            // Load the breed data for the initially selected species
                            int selectedSpeciesId = ((Category) speciesSpinnerFather.getSelectedItem()).getId();
                            loadBreedData1(selectedSpeciesId, category);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ParentBreedActivity.this, "Error loading species data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ParentBreedActivity.this, "Error loading species data", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadBreedData1(int category_ID, String preSelectedCategory) {
        // Make a GET request to the PHP file that retrieves the breed data for the selected species from the server
        String url = "https://pawsomematch.online/android/get_breeds.php?category_ID=" + category_ID;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response to an array of Breed objects
                            JSONArray jsonArray = new JSONArray(response);
                            breedList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Breed breed = new Breed(jsonObject.getInt("breed_ID"), jsonObject.getString("breed"),
                                        jsonObject.getInt("category_ID"));
                                breedList.add(breed);
                            }

                            // Set up the breed spinner with the loaded data
                            ArrayAdapter<Breed> breedAdapter = new ArrayAdapter<>(ParentBreedActivity.this,
                                    android.R.layout.simple_spinner_item, breedList);
                            breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            breedSpinnerFather.setAdapter(breedAdapter);

                            // Set the pre-selected category and breed in the spinners
                            int categoryPosition = -1;
                            int breedPosition = -1;
                            for (int i = 0; i < speciesList.size(); i++) {
                                if (speciesList.get(i).getName().equals(preSelectedCategory)) {
                                    categoryPosition = i;
                                    break;
                                }
                            }

                            for (int i = 0; i < breedList.size(); i++) {
                                if (breedList.get(i).getName().equals(preSelectedBreed)) {
                                    breedPosition = i;
                                    break;
                                }
                            }

                            if (categoryPosition != -1) {
                                speciesSpinnerFather.setSelection(categoryPosition);
                            }
                            if (breedPosition != -1) {
                                breedSpinnerFather.setSelection(breedPosition);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ParentBreedActivity.this, "Error loading breed data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ParentBreedActivity.this, "Error loading breed data", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        PetMatchData petMatchData = new PetMatchData();

        if (parent == speciesSpinnerMother) {
            // When a species is selected, load the corresponding breed data
            Category selectedSpecies = (Category) parent.getItemAtPosition(position);
            selectedSpeciesId = selectedSpecies.getId();
            loadBreedData(selectedSpeciesId, category);

        } else if (parent == breedSpinnerMother) {
            // When a breed is selected, record the selected breed id
            Breed selectedBreed = (Breed) parent.getItemAtPosition(position);
            selectedBreedId = selectedBreed.getBreed_ID();
        } else if (parent == speciesSpinnerFather) {
            // When a species is selected, load the corresponding breed data
            Category selectedSpecies = (Category) parent.getItemAtPosition(position);
            selectedSpeciesId = selectedSpecies.getId();
            loadBreedData1(selectedSpeciesId, category);

        } else if (parent == breedSpinnerFather) {
            // When a breed is selected, record the selected breed id
            Breed selectedBreed = (Breed) parent.getItemAtPosition(position);
            selectedBreedId = selectedBreed.getBreed_ID();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    private void updateParentData() {

        String url = "https://pawsomematch.online/android/add_parent.php"; // Replace with the URL of your PHP script
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Handle the response from the server (e.g., show a success message)
                Toast.makeText(ParentBreedActivity.this, response, Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle any errors that occur during the request
                error.printStackTrace();
                Toast.makeText(ParentBreedActivity.this, "Error updating data", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("petId", petId);
                params.put("motherCategory", selectedMotherCategory);
                params.put("motherBreed", selectedMotherBreed);
                params.put("motherBreedtype", selectedMotherBreedtype);
                params.put("fatherCategory", selectedFatherCategory);
                params.put("fatherBreed", selectedFatherBreed);
                params.put("fatherBreedtype", selectedFatherBreedtype);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

}