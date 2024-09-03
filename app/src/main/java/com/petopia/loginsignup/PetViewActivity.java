package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PetViewActivity extends AppCompatActivity {

    ImageView backBtn;

    String petId;
    ImageView imgCertification;
    ImageView imgVaccine;
    ImageView imgPhoto;

    TextView species_spinnerMother, breed_spinnerMother, breedtype_spinnerMother;
    TextView species_spinnerFather, breed_spinnerFather, breedtype_spinnerFather;

    TextView txtgender;
    TextView txtweight;
    TextView txtbreedtype;
    TextView txtcolor;
    TextView txtpetname;
    TextView txtpetbreed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_view);

        backBtn = findViewById(R.id.backBtn);

        txtgender = findViewById(R.id.txtgender);
        txtweight = findViewById(R.id.txtweight);
        txtbreedtype = findViewById(R.id.txtspecies);
        txtcolor = findViewById(R.id.txtcolor);
        txtpetbreed = findViewById(R.id.txtbreed);
        txtpetname = findViewById(R.id.txtpetname);

        petId = getIntent().getStringExtra("petId");
        imgCertification = findViewById(R.id.imgCertification);
        imgVaccine = findViewById(R.id.imgVaccine);
        imgPhoto = findViewById(R.id.imgPhoto);

        species_spinnerMother = findViewById(R.id.species_spinnerMother);
        breed_spinnerMother = findViewById(R.id.breed_spinnerMother);
        breedtype_spinnerMother = findViewById(R.id.breedtype_spinnerMother);

        species_spinnerFather = findViewById(R.id.species_spinnerFather);
        breed_spinnerFather = findViewById(R.id.breed_spinnerFather);
        breedtype_spinnerFather = findViewById(R.id.breedtype_spinnerFather);

        loadCertificationImage();
        loadVaccineImage();
        loadProfilePhotoImage();
        retrievePetDataFromServer();
        retrievePetFromServer();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    private void loadCertificationImage() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return getImageUrlFromDatabase("certification");
            }

            @Override
            protected void onPostExecute(String imageUrl) {
                if (!imageUrl.isEmpty()) {
                    Picasso.get().load(imageUrl).into(imgCertification);
                }
            }
        }.execute();
    }

    private void loadVaccineImage() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return getImageUrlFromDatabase("vaccine");
            }

            @Override
            protected void onPostExecute(String imageUrl) {
                if (!imageUrl.isEmpty()) {
                    Picasso.get().load(imageUrl).into(imgVaccine);
                }
            }
        }.execute();
    }

    private void loadProfilePhotoImage() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return getImageUrlFromDatabase("photo");
            }

            @Override
            protected void onPostExecute(String imageUrl) {
                if (!imageUrl.isEmpty()) {
                    Picasso.get().load(imageUrl).into(imgPhoto);
                }
            }
        }.execute();
    }

    private String getImageUrlFromDatabase(String imageType) {
        String imageUrl = "https://pawsomematch.online/android/pictures/";

        // Make an HTTP request to your PHP script passing petId and imageType
        // Handle the response and extract the image URL

        String phpUrl = "https://pawsomematch.online/android/retrieveuploadedphoto.php";
        String postData = "pet_id=" + petId + "&image_type=" + imageType;

        try {
            URL url = new URL(phpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String responseLine;
            StringBuilder response = new StringBuilder();

            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            br.close();

            // Extract the image URL from the response
            String imageUrlFromResponse = response.toString();

            if (!imageUrlFromResponse.isEmpty()) {
                return imageUrl + imageUrlFromResponse;
            } else {
                return ""; // Return an empty string if no image URL found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ""; // Return an empty string on error
        }
    }
    private void retrievePetDataFromServer() {
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                // Define the URL of your PHP script
                String phpUrl = "https://pawsomematch.online/android/retrieve_pet_parent.php";

                try {
                    URL url = new URL(phpUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    // Send the 'petId' parameter to the PHP script
                    String postData = "petId=" + petId;
                    OutputStream os = conn.getOutputStream();
                    os.write(postData.getBytes());
                    os.flush();
                    os.close();

                    // Read the response from the PHP script
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String responseLine;
                    StringBuilder response = new StringBuilder();

                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    br.close();

                    // Parse the JSON response
                    return new JSONObject(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(JSONObject data) {
                if (data != null) {
                    try {
                        // Populate the TextViews with the retrieved data
                        species_spinnerMother.setText(data.getString("mother_category"));
                        breed_spinnerMother.setText(data.getString("mother_breed"));
                        breedtype_spinnerMother.setText(data.getString("mother_breedtype"));
                        species_spinnerFather.setText(data.getString("father_category"));
                        breed_spinnerFather.setText(data.getString("father_breed"));
                        breedtype_spinnerFather.setText(data.getString("father_breedtype"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    private void retrievePetFromServer() {
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                // Define the URL of your PHP script
                String phpUrl = "https://pawsomematch.online/android/view_pet_data.php";

                try {
                    URL url = new URL(phpUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    // Send the 'petId' parameter to the PHP script
                    String postData = "petId=" + petId;
                    OutputStream os = conn.getOutputStream();
                    os.write(postData.getBytes());
                    os.flush();
                    os.close();

                    // Read the response from the PHP script
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String responseLine;
                    StringBuilder response = new StringBuilder();

                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    br.close();

                    // Parse the JSON response
                    return new JSONObject(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(JSONObject data) {
                if (data != null) {
                    try {
                        // Populate the TextViews with the retrieved data
                        txtgender.setText(data.getString("gender"));
                        txtweight.setText(data.getString("weight") + " kg");
                        txtbreedtype.setText(data.getString("breedtype"));
                        txtcolor.setText(data.getString("color"));
                        txtpetbreed.setText(data.getString("breed"));
                        txtpetname.setText(data.getString("pet_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

}