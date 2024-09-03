package com.petopia.loginsignup;

import android.app.DatePickerDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.app.ProgressDialog;

public class AddPost extends MainActivity implements AdapterView.OnItemSelectedListener {
    EditText pet_Name, Breed, Species, Weight, vax, Price, Description, age;
    Spinner vaccine;
    private Spinner Gender;
    private Spinner breedtypeSpinner;
    private Spinner speciesSpinner;
    private Spinner breedSpinner;

    private List<Category> speciesList;
    private List<Breed> breedList;
    private int selectedSpeciesId;
    private int selectedBreedId;

    private Spinner Color;

    Button add_pet_Btn;
    Button ageBtn;

    ImageView backBtn;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private ImageView petImageView;
    private RelativeLayout btnUploadImage;
    private boolean isButtonClickable = true;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        init();
        setContentView(R.layout.activity_add_post);
        backBtn = findViewById(R.id.backBtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Pet...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);


        speciesSpinner = findViewById(R.id.species_spinner);
        breedSpinner = findViewById(R.id.breed_spinner);

        speciesList = new ArrayList<>();
        breedList = new ArrayList<>();

        // Set up the spinner listeners
        speciesSpinner.setOnItemSelectedListener(this);
        breedSpinner.setOnItemSelectedListener(this);

        breedtypeSpinner = findViewById(R.id.purity_spinner); // Add this line
        breedtypeSpinner.setOnItemSelectedListener(this); // Add this line


        Color = findViewById(R.id.Color);
        Color.setOnItemSelectedListener(this);

        Color = findViewById(R.id.Color);
        Color.setOnItemSelectedListener(this);

        // Load the species data from the server
        loadSpeciesData();
        loadColorData();

        petImageView = findViewById(R.id.browsePetImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        // Set an onClickListener for the "Upload Image" button
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        getViews();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            Uri uri = data.getData();

            try {
                // Load the selected image into the ImageView
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                petImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImageToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
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
                            ArrayAdapter<Category> speciesAdapter = new ArrayAdapter<>(AddPost.this,
                                    android.R.layout.simple_spinner_item, speciesList);
                            speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            speciesSpinner.setAdapter(speciesAdapter);

                            // Load the breed data for the initially selected species
                            int selectedSpeciesId = ((Category) speciesSpinner.getSelectedItem()).getId();
                            loadBreedData(selectedSpeciesId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddPost.this, "Error loading species data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse( VolleyError error) {
                error.printStackTrace();
                Toast.makeText(AddPost.this, "Error loading species data", Toast.LENGTH_SHORT).show();
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
                            ArrayAdapter<Breed> breedAdapter = new ArrayAdapter<>(AddPost.this,
                                    android.R.layout.simple_spinner_item, breedList);
                            breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            breedSpinner.setAdapter(breedAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddPost.this, "Error loading breed data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(AddPost.this, "Error loading breed data", Toast.LENGTH_SHORT).show();
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
                            ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(AddPost.this,
                                    android.R.layout.simple_spinner_item, colorList);
                            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Color.setAdapter(colorAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddPost.this, "Error loading color data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(AddPost.this, "Error loading color data", Toast.LENGTH_SHORT).show();
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
        add_pet_Btn = findViewById(R.id.add_pet_Btn);
        Price = findViewById(R.id.Price);
        Description = findViewById(R.id.Description);
        age = findViewById(R.id.Age);
        ageBtn = findViewById(R.id.AgeBtn);

        add_pet_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isButtonClickable) {
                    // Disable the button
                    isButtonClickable = false;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isButtonClickable = true;
                        }
                    }, 2000); // 2000 milliseconds (2 seconds)
                }

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPost.this, R.style.GreenDatePickerDialogTheme, dateSetListener,
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
        breedtype = breedtypeSpinner.getSelectedItem().toString();
        price = Float.parseFloat(Price.getText().toString().trim());
        description = Description.getText().toString();

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
        String breedtype = breedtypeSpinner.getSelectedItem().toString();
        String email = sharedPreferences.getString("email", "");
        float price = Float.parseFloat(Price.getText().toString().trim());
        String description = Description.getText().toString().trim();

        // Encode the image to base64
        String imageBase64 = encodeImageToBase64(bitmap);

        // Create a new RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Define the URL for the PHP file that will insert the pet data into the database
        String url = "https://pawsomematch.online/android/add_pet_selling.php";

        // Create a StringRequest object to send a POST request to the server
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display a success message
                        progressDialog.dismiss();
                        Toast.makeText(AddPost.this, "Pet added successfully", Toast.LENGTH_SHORT).show();

                        // Clear the form
                        pet_Name.setText("");
                        Weight.setText("");
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Display an error message
                progressDialog.dismiss();
                Toast.makeText(AddPost.this, "Error registering pet", Toast.LENGTH_SHORT).show();
                Log.e("Volley", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add the pet data, including the image, to the POST request parameters
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
                params.put("email", email);
                params.put("price", String.valueOf(price));
                params.put("description", description);
                params.put("image_base64", imageBase64); // Add the image data

                return params;
            }
        };

        // Add the request to the RequestQueue
        queue.add(request);
    }


}

