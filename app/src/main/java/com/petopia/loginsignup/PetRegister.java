    package com.petopia.loginsignup;

    import androidx.appcompat.app.AppCompatActivity;

    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;
    import android.app.DatePickerDialog;
    import android.widget.DatePicker;

    import java.text.SimpleDateFormat;
    import java.util.Calendar;
    import java.util.Locale;


    import com.android.volley.AuthFailureError;
    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.StringRequest;
    import com.android.volley.toolbox.Volley;
    import com.loopj.android.http.JsonHttpResponseHandler;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;
    import org.w3c.dom.Text;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class PetRegister extends MainActivity implements AdapterView.OnItemSelectedListener {
        EditText pet_Name, Breed, Species, Weight, vax, age, Shares;
        ImageView backBtn;

        TextView sharestv;
        TextView studtv;
        Spinner vaccine;
        private Spinner Gender;
        private Spinner BreedType;
        private Spinner StudFee;
        private Spinner speciesSpinner;
        private Spinner breedSpinner;

        private List<Category> speciesList;
        private List<Breed> breedList;
        private int selectedSpeciesId;
        private int selectedBreedId;

        private Spinner Color;

        Button add_pet_Btn;
        Button petBtn;
        Button ageBtn;

        private Calendar calendar;
        private DatePickerDialog.OnDateSetListener dateSetListener;

        private ProgressDialog progressDialog;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            super.onCreate(savedInstanceState);
            context = this;
            init();
            setContentView(R.layout.activity_pet_register);
            getViews();

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Registering Pet...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);

            backBtn = findViewById(R.id.backBtn);
            speciesSpinner = findViewById(R.id.species_spinner);
            breedSpinner = findViewById(R.id.breed_spinner);
            sharestv = findViewById(R.id.sharestv);
            studtv = findViewById(R.id.studtv);
            Shares = findViewById(R.id.shares);
            StudFee = findViewById(R.id.StudFee);

            speciesList = new ArrayList<>();
            breedList = new ArrayList<>();

            // Set up the spinner listeners
            speciesSpinner.setOnItemSelectedListener(this);
            breedSpinner.setOnItemSelectedListener(this);
            Gender.setOnItemSelectedListener(this);


            Color = findViewById(R.id.Color);
            Color.setOnItemSelectedListener(this);

            // Load the species data from the server
            loadSpeciesData();
            loadColorData();

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                                ArrayAdapter<Category> speciesAdapter = new ArrayAdapter<>(PetRegister.this,
                                        android.R.layout.simple_spinner_item, speciesList);
                                speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                speciesSpinner.setAdapter(speciesAdapter);

                                // Load the breed data for the initially selected species
                                int selectedSpeciesId = ((Category) speciesSpinner.getSelectedItem()).getId();
                                loadBreedData(selectedSpeciesId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(PetRegister.this, "Error loading species data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse( VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(PetRegister.this, "Error loading species data", Toast.LENGTH_SHORT).show();
                }
            });

            // Add the request to the request queue
            Volley.newRequestQueue(this).add(stringRequest);
        }

        private void loadBreedData(int category_ID) {
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
                                ArrayAdapter<Breed> breedAdapter = new ArrayAdapter<>(PetRegister.this,
                                        android.R.layout.simple_spinner_item, breedList);
                                breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                breedSpinner.setAdapter(breedAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(PetRegister.this, "Error loading breed data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(PetRegister.this, "Error loading breed data", Toast.LENGTH_SHORT).show();
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
                                ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(PetRegister.this,
                                        android.R.layout.simple_spinner_item, colorList);
                                colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                Color.setAdapter(colorAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(PetRegister.this, "Error loading color data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(PetRegister.this, "Error loading color data", Toast.LENGTH_SHORT).show();
                }
            });

            // Add the request to the request queue
            requestQueue.add(colorRequest);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent == speciesSpinner) {
                // When a species is selected, load the corresponding breed data
                Category selectedSpecies = (Category) parent.getItemAtPosition(position);
                selectedSpeciesId = selectedSpecies.getId();
                breedList.clear();
                loadBreedData(selectedSpeciesId);
            } else if (parent == breedSpinner) {
                // When a breed is selected, record the selected breed id
                Breed selectedBreed = (Breed) parent.getItemAtPosition(position);
                selectedBreedId = selectedBreed.getBreed_ID();
            }

            if (parent == Gender) {
                String selectedGender = Gender.getSelectedItem().toString();

                if (selectedGender.equals("Male")) {
                    StudFee.setVisibility(View.VISIBLE); // Set visibility to GONE
                    Shares.setVisibility(View.VISIBLE);
                    studtv.setVisibility(View.VISIBLE);
                    sharestv.setVisibility(View.VISIBLE);
                    StudFee.setEnabled(true);
                    Shares.setEnabled(false);

                    ArrayAdapter<String> studFeeAdapter = new ArrayAdapter<>(PetRegister.this,
                            android.R.layout.simple_spinner_item, new String[]{"None", "Stud Fee", "Share Offspring"});
                    studFeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    StudFee.setAdapter(studFeeAdapter);
                    sharestv.setText("No Fee Selected");

                    StudFee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            if (position == 1) {
                                Shares.setEnabled(true);
                                sharestv.setText("Select Fee");
                            } else if (position == 2) {
                                Shares.setEnabled(true);
                                sharestv.setText("Select Portion");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            // Handle case when nothing is selected
                        }
                    });
                } else if (selectedGender.equals("Female")) {
                    StudFee.setVisibility(View.GONE); // Set visibility to GONE
                    Shares.setVisibility(View.GONE);
                    studtv.setVisibility(View.GONE);
                    sharestv.setVisibility(View.GONE);

                    ArrayAdapter<String> studFeeAdapter = new ArrayAdapter<>(PetRegister.this,
                            android.R.layout.simple_spinner_item, new String[]{"None"});
                    studFeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    StudFee.setAdapter(studFeeAdapter);

                    sharestv.setText("No Fee Selected");
                }

            }

        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }

        public void getViews() {
            pet_Name = findViewById(R.id.pet_name);
            Weight = findViewById(R.id.Weight);
            vaccine = findViewById(R.id.vaccine);
            Color = findViewById(R.id.Color);
            Gender = findViewById(R.id.gender);
            BreedType = findViewById(R.id.purity_spinner);
            StudFee = findViewById(R.id.StudFee);
            Shares = findViewById(R.id.shares);
            add_pet_Btn = findViewById(R.id.add_pet_Btn);
            age = findViewById(R.id.Age);
            ageBtn = findViewById(R.id.AgeBtn);

            add_pet_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (pet_Name.getText().toString().isEmpty() || Weight.getText().toString().isEmpty() ||
                            age.getText().toString().isEmpty() || vaccine.getSelectedItem().toString().isEmpty() ||
                            Color.getSelectedItem().toString().isEmpty()) {
                        Toast.makeText(context, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    signupValidation();
                    progressDialog.show();
                    addPetToDatabase();

                }
            });

            // Set up the DatePickerDialog for selecting the date
            calendar = Calendar.getInstance();
            dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Update the EditText field with the selected date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    age.setText(dateFormat.format(calendar.getTime()));
                }
            };

            ageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Create a new DatePickerDialog with the custom theme
                    DatePickerDialog datePickerDialog = new DatePickerDialog(PetRegister.this, R.style.GreenDatePickerDialogTheme, dateSetListener,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));

                    // Show the DatePickerDialog when the EditText is clicked
                    datePickerDialog.show();
                }
            });
        }

        private void signupValidation() {
            pet_name = pet_Name.getText().toString();
            category = speciesSpinner.getSelectedItem().toString();
            breed =  breedSpinner.getSelectedItem().toString();
            weight = Weight.getText().toString();
            vaccine_status = vaccine.getSelectedItem().toString();
            gender = Gender.getSelectedItem().toString();
            color = Color.getSelectedItem().toString();
            breedtype = BreedType.getSelectedItem().toString();
            studfee = StudFee.getSelectedItem().toString();
            shares = Shares.getText().toString();


            if (pet_name.length() < 0) {
                Toast.makeText(context, "Input Name.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (category.length() == 0) {
                Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                return;
            }

            if (breed.length() == 0) {
                Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                return;
            }

            if (age.length() == 0) {
                Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                return;
            }
            if (weight.length() == 0) {
                Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                return;
            }
            if (vaccine_status.length() == 0) {
                Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                return;
            }
            if (color.length() == 0) {
                Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        private class ResponseHandler extends JsonHttpResponseHandler {
            @Override
            public void onStart() {
                super.onStart();
            }
        }
        private void addPetToDatabase() {
            // Get the values from the form
            String name = pet_Name.getText().toString().trim();
            String category = ((Category) speciesSpinner.getSelectedItem()).getName();
            String breed = ((Breed) breedSpinner.getSelectedItem()).getName();
            String ageValue = age.getText().toString().trim();
            String weight = Weight.getText().toString().trim();
            String vaccineStatus = vaccine.getSelectedItem().toString();
            String gender = Gender.getSelectedItem().toString();
            String color = Color.getSelectedItem().toString().trim();
            String email =  sharedPreferences.getString("email","");
            String breedtype = BreedType.getSelectedItem().toString();
            String studfee = StudFee.getSelectedItem().toString();
            String shares = Shares.getText().toString();

            // Create a new RequestQueue
            RequestQueue queue = Volley.newRequestQueue(this);

            // Define the URL for the PHP file that will insert the pet data into the database
            String url = "https://pawsomematch.online/android/petreg2.php";

            // Create a StringRequest object to send a POST request to the server
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display a success message
                            Toast.makeText(PetRegister.this, "Pet registered successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            // Clear the form
                            pet_Name.setText("");
                            Weight.setText("");
                            finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Display an error message
                    Toast.makeText(PetRegister.this, "Error registering pet", Toast.LENGTH_SHORT).show();
                    Log.e("Volley", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    // Add the pet data to the POST request parameters
                    Map<String, String> params = new HashMap<>();
                    params.put("pet_name", name);
                    params.put("category", category);
                    params.put("breed", breed);
                    params.put("age", ageValue);
                    params.put("weight", weight);
                    params.put("vaccine_status", vaccineStatus);
                    params.put("gender", gender);
                    params.put("color", color);
                    params.put("breedtype", breedtype);
                    params.put("studfee", studfee);
                    params.put("shares", shares);
                    params.put("email", email);
                    return params;
                }
            };

            // Add the request to the RequestQueue
            queue.add(request);
        }

    }

