package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ReviewActivity extends MainActivity {

    String petId;
    String user_id;
    Button btnSaveRate;
    EditText etRating;
    EditText etRateDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        user_id = sharedPreferences.getString("user_id", "");

        petId = getIntent().getStringExtra("pet_ID");

        btnSaveRate = findViewById(R.id.btnSaveRate);
        etRating = findViewById(R.id.etRating);
        etRateDescription = findViewById(R.id.etRateDescription);

        btnSaveRate.setOnClickListener(v -> {
            // Get the review data
            String rating = etRating.getText().toString();
            String description = etRateDescription.getText().toString();

            // Call the AsyncTask to upload the review
            new UploadReviewTask().execute(petId, user_id, rating, description);
        });
    }

    private class UploadReviewTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String petId = params[0];
            String buyerId = params[1];
            String rating = params[2];
            String description = params[3];

            try {
                // Set up the connection
                URL url = new URL("https://pawsomematch.online/android/save_review.php"); // Replace with your PHP script URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // Create the data to send
                Map<String, String> postData = new HashMap<>();
                postData.put("pet_ID", petId);
                postData.put("buyer_ID", buyerId);
                postData.put("rating", rating);
                postData.put("description", description);

                // Write the data to the connection
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(getPostDataString(postData));
                writer.flush();
                writer.close();
                outputStream.close();

                // Get the response from the server
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return "Review uploaded successfully";
                } else {
                    return "Error: " + responseCode;
                }
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(ReviewActivity.this, result, Toast.LENGTH_SHORT).show();
            if (result.equals("Review uploaded successfully")) {
                finish(); // Close the ReviewActivity
            }

        }

        private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (postData.length() != 0) {
                    postData.append('&');
                }
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(param.getValue(), "UTF-8"));
            }
            return postData.toString();
        }
    }
}