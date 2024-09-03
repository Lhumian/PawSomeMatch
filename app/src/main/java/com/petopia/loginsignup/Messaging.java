package com.petopia.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Messaging extends MainActivity {
    private ListView userListView;
    private MessageUserAdapter userListAdapter;
    private List<UserMessage> userList;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        userId = sharedPreferences.getString("user_id","");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_message);

        userList = new ArrayList<>();

        /*userMessageAdapter = new UserMessageAdapter(this, userMessages);*/
        userListAdapter = new MessageUserAdapter(this, userList);
        userListView = findViewById(R.id.userListView);
        userListView.setAdapter(userListAdapter);

        retrieveUserList();


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_match:
                    startActivity(new Intent(getApplicationContext(), Match.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottom_paw_pound:
                    startActivity(new Intent(getApplicationContext(), PetSelling.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottom_message:
                    return true;
            }
            return false;
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected user
                UserMessage selectedUser = userList.get(position);

                openChat(selectedUser);
            }
        });

    }

    private void retrieveUserList() {
        String url = "https://pawsomematch.online/android/retrieve_pet_list.php?user_id=" + userId;

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
                                int acceptedUserId = jsonObject.getInt("accepted_user_id");
                                String acceptedFirstName = jsonObject.getString("accepted_first_name");
                                String acceptedLastName = jsonObject.getString("accepted_last_name");
                                int acceptingUserId = jsonObject.getInt("accepting_user_id");
                                String acceptingFirstName = jsonObject.getString("accepting_first_name");
                                String acceptingLastName = jsonObject.getString("accepting_last_name");
                                String acceptingPetName = jsonObject.getString("accepting_pet_name");
                                String acceptingPetID = jsonObject.getString("accepting_pet_id");
                                String acceptedPetName = jsonObject.getString("accepted_pet_name");
                                String acceptedPetID = jsonObject.getString("accepted_pet_id");

                                boolean isAccepted = jsonObject.has("accepted") && jsonObject.getInt("accepted") == 1;

                                // Add the user to the list
                                userList.add(new UserMessage(acceptedUserId, acceptedFirstName, acceptedLastName, acceptingUserId, acceptingFirstName, acceptingLastName, acceptingPetName, acceptingPetID, acceptedPetName, acceptedPetID, isAccepted));
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
    private void openChat(UserMessage user) {
        int userId = user.getAcceptedUserId();
        String userName = user.getAcceptedFirstName() + " " + user.getAcceptedLastName();
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("receiver_name", userName);

        intent.putExtra("acceptingPetID", user.getAcceptingPetID());
        intent.putExtra("acceptedPetID", user.getAcceptedPetID());
        startActivity(intent);
    }

    private void retrievePetBreed(){

    }
}
