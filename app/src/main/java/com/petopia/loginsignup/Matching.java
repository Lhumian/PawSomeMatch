package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;


public class Matching extends MainActivity implements AdapterView.OnItemSelectedListener {
    private Spinner speciesSpinner;
    private Spinner breedSpinner;
    private Button matchButton;

    private List<Category> speciesList;
    private List<Breed> breedList;
    private int selectedSpeciesId;
    private int selectedBreedId;

    private Spinner colorSpinner;

    int position1;


    private Spinner genderSpinner;
    private Spinner breedtypeSpinner;

    private Spinner vaccineStatus;

    String preSelectedBreed;
    String preSelectedCategory;

    String preSelectedGender;
    String preSelectedPetId;

    ImageView backBtn;

    String petname;

    TextView txtpetname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_matching);

        speciesSpinner = findViewById(R.id.species_spinner);
        breedSpinner = findViewById(R.id.breed_spinner);
        matchButton = findViewById(R.id.match_button);
        backBtn = findViewById(R.id.backBtn);

        speciesList = new ArrayList<>();
        breedList = new ArrayList<>();

        // Set up the spinner listeners
        speciesSpinner.setOnItemSelectedListener(this);
        breedSpinner.setOnItemSelectedListener(this);

        genderSpinner = findViewById(R.id.gender_spinner);
        genderSpinner.setOnItemSelectedListener(this);

        breedtypeSpinner = findViewById(R.id.breedtype_spinner);
        breedtypeSpinner.setOnItemSelectedListener(this);

        colorSpinner = findViewById(R.id.color_spinner);
        colorSpinner.setOnItemSelectedListener(this);

        ImageView imgPhoto = findViewById(R.id.imgPhoto);


        // Load the species data from the server
        loadSpeciesData();
        loadColorData();

        Intent intent = getIntent();
        int category_ID = intent.getIntExtra("category_ID", 0);
        preSelectedCategory = intent.getStringExtra("category");
        preSelectedBreed = intent.getStringExtra("breed");
        preSelectedGender = intent.getStringExtra("gender");
        preSelectedPetId = intent.getStringExtra("petId");
        petname = intent.getStringExtra("petname");

        String imageUrlFetchUrl = "https://pawsomematch.online/android/get_pet_image.php?petId=" + preSelectedPetId;

        // Load the image using Picasso
        new Matching.FetchImageUrlTask(imgPhoto).execute(imageUrlFetchUrl);
        txtpetname = findViewById(R.id.petname);
        txtpetname.setText(petname);

        speciesSpinner.setEnabled(false);
        speciesSpinner.setClickable(false);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        // Set up the match button listener
        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedGender = genderSpinner.getSelectedItem().toString();
                Category selectedSpecies = (Category) speciesSpinner.getSelectedItem();
                Breed selectedBreed = (Breed) breedSpinner.getSelectedItem();
                String selectedColor = colorSpinner.getSelectedItem().toString();
                String selectedBreedtype = breedtypeSpinner.getSelectedItem().toString();

                Intent intent = new Intent(Matching.this, MatchingResult.class);
                intent.putExtra("position1", position1);
                intent.putExtra("category", selectedSpecies.getName());
                intent.putExtra("breed", selectedBreed.getName());
                intent.putExtra("gender", selectedGender);
                intent.putExtra("color", selectedColor);
                intent.putExtra("breedtype", selectedBreedtype);
                intent.putExtra("petId", preSelectedPetId);
                startActivity(intent);

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
                            ArrayAdapter<Category> speciesAdapter = new ArrayAdapter<>(Matching.this,
                                    android.R.layout.simple_spinner_item, speciesList);
                            speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            speciesSpinner.setAdapter(speciesAdapter);

                            // Load the breed data for the initially selected species
                            int selectedSpeciesId = ((Category) speciesSpinner.getSelectedItem()).getId();
                            loadBreedData(selectedSpeciesId, preSelectedCategory);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Matching.this, "Error loading species data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Matching.this, "Error loading species data", Toast.LENGTH_SHORT).show();
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
                            ArrayAdapter<Breed> breedAdapter = new ArrayAdapter<>(Matching.this,
                                    android.R.layout.simple_spinner_item, breedList);
                            breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            breedSpinner.setAdapter(breedAdapter);

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
                                speciesSpinner.setSelection(categoryPosition);
                            }
                            if (breedPosition != -1) {
                                breedSpinner.setSelection(breedPosition);
                            }

                            if (preSelectedGender != null) {
                                if (preSelectedGender.equals("Male")) {
                                    genderSpinner.setSelection(1);
                                } else if (preSelectedGender.equals("Female")) {
                                    genderSpinner.setSelection(0);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Matching.this, "Error loading breed data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Matching.this, "Error loading breed data", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(stringRequest);
    }



    private void loadColorData() {
        // Make a GET request to the PHP file that retrieves the color data from the server
        String colorUrl = "https://pawsomematch.online/android/get_colors.php";

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a string request to load the color data
        StringRequest colorRequest = new StringRequest(Request.Method.GET, colorUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response to an array of colors
                            JSONArray jsonArray = new JSONArray(response);
                            List<String> colorList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String color = jsonObject.getString("colors");
                                colorList.add(color);
                            }

                            // Set up the color spinner with the loaded data
                            ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(Matching.this,
                                    android.R.layout.simple_spinner_item, colorList);
                            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            colorSpinner.setAdapter(colorAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Matching.this, "Error loading color data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Matching.this, "Error loading color data", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the request queue
        requestQueue.add(colorRequest);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        PetMatchData petMatchData = new PetMatchData();

        if (parent == speciesSpinner) {
            // When a species is selected, load the corresponding breed data
            Category selectedSpecies = (Category) parent.getItemAtPosition(position);
            selectedSpeciesId = selectedSpecies.getId();
            loadBreedData(selectedSpeciesId, preSelectedCategory);
        } else if (parent == breedSpinner) {
            // When a breed is selected, record the selected breed id
            Breed selectedBreed = (Breed) parent.getItemAtPosition(position);
            selectedBreedId = selectedBreed.getBreed_ID();
        } else if (parent == genderSpinner) {
            // When a gender is selected, record the selected gender
            String selectedGender = parent.getItemAtPosition(position).toString();
            // Store the selected gender in your PetMatchData object or variable
            // For example, assuming you have a petMatchData object:
            petMatchData.setGender(selectedGender);
        } else if (parent == colorSpinner) {
            // When a color is selected, record the selected color
            String selectedColor = parent.getItemAtPosition(position).toString();
            // Store the selected color in your PetMatchData object or variable
            // For example, assuming you have a petMatchData object:
            petMatchData.setColor(selectedColor);
        } else if (parent == breedtypeSpinner) {
            // When a color is selected, record the selected color
            String selectedBreedtype = parent.getItemAtPosition(position).toString();
            // Store the selected color in your PetMatchData object or variable
            // For example, assuming you have a petMatchData object:
            petMatchData.setColor(selectedBreedtype);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    public class FetchImageUrlTask extends AsyncTask<String, Void, String> {
        private ImageView imageView;

        public FetchImageUrlTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected String doInBackground(String... params) {
            String imageUrl = "";

            try {
                String imageUrlFetchUrl = params[0];
                URL url = new URL(imageUrlFetchUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                    inputStream.close();

                    imageUrl = response.toString();
                } else {
                    // Handle error here, e.g., log an error message
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error here, e.g., log an error message
            }

            return imageUrl;
        }

        @Override
        protected void onPostExecute(String imageUrl) {
            // Log the image URL
            Log.d("Matching", "Image URL: " + imageUrl);

            String imageUrll = "https://pawsomematch.online/android/pictures/" + imageUrl;

            // Load the image into the ImageView using Picasso
            if (!imageUrl.isEmpty()) {
                Picasso.get().load(imageUrll).into(imageView);
            } else {
                // Handle the case where the image URL is empty or invalid
                Log.e("Matching", "Empty or invalid image URL");
            }
        }

    }
}
