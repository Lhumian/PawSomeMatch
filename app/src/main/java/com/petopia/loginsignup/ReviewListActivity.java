package com.petopia.loginsignup;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReviewListActivity extends MainActivity {

    String user_id;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        user_id = sharedPreferences.getString("user_id", "");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch reviews in the background
        new FetchReviewsTask().execute(user_id);
    }

    private class FetchReviewsTask extends AsyncTask<String, Void, List<ReviewList>> {

        @Override
        protected List<ReviewList> doInBackground(String... params) {
            // Call the PHP method to get reviews
            String sellerId = params[0];
            return fetchReviewsFromServer(sellerId);
        }

        @Override
        protected void onPostExecute(List<ReviewList> reviews) {
            // Update UI with the fetched reviews
            if (reviews != null) {
                ReviewListAdapter adapter = new ReviewListAdapter(ReviewListActivity.this, reviews);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private List<ReviewList> fetchReviewsFromServer(String sellerId) {
        List<ReviewList> reviews = new ArrayList<>();

        // Use your actual API endpoint
        String urlStr = "https://pawsomematch.online/android/retrieve_review_list.php?sellerId=" + sellerId;

        try {
            // Create URL object
            URL url = new URL(urlStr);

            // Create HttpURLConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Set request method to GET
            urlConnection.setRequestMethod("GET");

            // Get the response code
            int responseCode = urlConnection.getResponseCode();

            // If the response code is 200 (HTTP_OK), read the input stream
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                // Parse the JSON response
                JSONArray jsonArray = new JSONArray(response.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    // Extract data from JSON and create ReviewList objects
                    String petID = jsonObject.getString("pet_ID");
                    String petName = jsonObject.getString("pet_name");
                    String reference = jsonObject.getString("reference");
                    String buyerName = jsonObject.getString("buyer_name");
                    int rating = jsonObject.getInt("rating");
                    String description = jsonObject.getString("description");

                    ReviewList review = new ReviewList(petID, petName, reference, buyerName, rating, description);
                    reviews.add(review);
                }
            } else {
                // Handle the error case (e.g., log an error message)
                Log.e("HTTP Request", "Failed with response code: " + responseCode);
            }

            // Disconnect the HttpURLConnection
            urlConnection.disconnect();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }
}
