package com.petopia.loginsignup;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AcceptanceActivity extends MainActivity {
    private ListView userListView;
    private UserListAdapter userListAdapter;
    private List<UserNotification> userList;
    private List<RetrievePet> petList;

    private String userId;
    private final Handler handler = new Handler();
    private final int POLL_INTERVAL = 60000;

    private TextView noNotificationTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptance);

        userId = sharedPreferences.getString("user_id", "");
        int currentUserId = Integer.parseInt(userId);

        userList = new ArrayList<>();
        userListAdapter = new UserListAdapter(this, userList, currentUserId);
        userListView = findViewById(R.id.userListView);
        userListView.setAdapter(userListAdapter);
        petList = new ArrayList<>();

        noNotificationTextView = findViewById(R.id.noNotificationTextView);
        ImageView backBtn = findViewById(R.id.backBtn);
        retrieveUserList();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected user
                UserNotification selectedUser = userList.get(position);

                // Check if the user is accepted
                if (selectedUser != null && selectedUser.isAccepted()) {
                    // Pass the user_id as an extra to the Chat activity
                    Intent intent = new Intent(getApplicationContext(), Messaging.class);
                    intent.putExtra("user_id", selectedUser.getUserId()); // Use "user_id" as the key for the user ID
                    intent.putExtra("receiver_name", selectedUser.getFirstName() + " " + selectedUser.getLastName()); // Use "receiver_name" as the key for the receiver's name
                    startActivity(intent);
                }
            }
        });

        // Check if the user list is empty and show/hide the "NO NOTIFICATION FOUND" message accordingly
    }

    private void retrieveUserList() {
        String url = "https://pawsomematch.online/android/retrieve_user_notification.php?user_id=" + userId;

        // Create a JsonArrayRequest to send a GET request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            userList.clear();

                            // Process each user object in the response
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                // Extract user data from the JSON object
                                int like_ID = jsonObject.getInt("like_ID");
                                int userId = jsonObject.getInt("user_id");
                                String firstName = jsonObject.getString("first_name");
                                String lastName = jsonObject.getString("last_name");
                                String likedPetId = jsonObject.getString("liked_pet_ID");
                                String petId = jsonObject.getString("pet_ID"); // Include pet_ID field
                                String petName = jsonObject.getString("pet_name");

                                // Add the user to the list
                                userList.add(new UserNotification(like_ID,userId, firstName, lastName, petId, petName, likedPetId, false));
                            }

                            // Notify the adapter that the data has changed
                            userListAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    private Runnable pollServer = new Runnable() {
        @Override
        public void run() {
            // Make a request to the PHP script to fetch liked pets
            String likedPetsUrl = "https://pawsomematch.online/android/retrieve_user_notification.php?user_id=" + userId;

            JsonArrayRequest likedPetsRequest = new JsonArrayRequest(Request.Method.GET, likedPetsUrl, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // Process the response to check for new likes
                            List<UserNotification> newLikedPets = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    // Extract liked pet data

                                    // Check if this liked pet is new, e.g., not in the previously fetched data
                                    if (!isInPreviouslyFetchedData(jsonObject)) {
                                        // Extract liked pet data
                                        int like_ID = jsonObject.getInt("like_ID");
                                        int userId = jsonObject.getInt("user_id");
                                        String firstName = jsonObject.getString("first_name");
                                        String lastName = jsonObject.getString("last_name");
                                        String likedPetId = jsonObject.getString("liked_pet_ID");
                                        String petId = jsonObject.getString("pet_ID");
                                        String petName = jsonObject.getString("pet_name");

                                        // Create a UserNotification object with the extracted data
                                        UserNotification newLikedPet = new UserNotification(like_ID, userId, firstName, lastName, petId, petName, likedPetId, false);

                                        // Add the newLikedPet to the list
                                        newLikedPets.add(newLikedPet);
                                    }

                                    if (newLikedPets.isEmpty()) {
                                        userListView.setVisibility(View.VISIBLE);
                                        noNotificationTextView.setVisibility(View.GONE);
                                    } else {
                                        userListView.setVisibility(View.GONE);
                                        noNotificationTextView.setVisibility(View.VISIBLE);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            // Show the indicator if there are new like
                            if (!newLikedPets.isEmpty()) {
                                // Debugging output
                                for (UserNotification likedPet : newLikedPets) {
                                    Log.d("NewLikedPet", "New Liked Pet: " + likedPet.getPetName());
                                }
                            } else {
                                // Debugging output
                                Log.d("NewLikedPet", "No new liked pets");
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                        }
                    });

            Volley.newRequestQueue(getApplicationContext()).add(likedPetsRequest);

            // Schedule the next poll
            handler.postDelayed(this, POLL_INTERVAL);
        }
    };

    private boolean isInPreviouslyFetchedData(JSONObject newLikedPet) {
        for (UserNotification existingLikedPet : userList) {
            // Compare the newLikedPet with existingLikedPet based on some unique identifier.
            // For example, you might compare them based on pet ID or some other unique property.

            try {
                // Extract the unique identifier from the JSON objects
                int newPetId = newLikedPet.getInt("like_ID");
                int existingPetId = existingLikedPet.getLike_ID();

                // Compare the unique identifier
                if (newPetId == existingPetId) {
                    // If there is a match, it's not a new liked pet
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // If no match is found, it's a new liked pet
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        handler.post(pollServer); // Start polling when the activity is resumed
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(pollServer); // Stop polling when the activity is paused
    }

    private void showNewLikeIndicator(boolean show) {
        if (show) {
            Toast.makeText(AcceptanceActivity.this, "New Liked", Toast.LENGTH_SHORT).show();
        }
    }
}
