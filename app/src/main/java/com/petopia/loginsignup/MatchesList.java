package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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

public class MatchesList extends MainActivity {

    String user_id;
    private ListView userListView;

    private MatchesAdapter userListAdapter;
    private List<Matches> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_list);

        user_id = sharedPreferences.getString("user_id", "");

        userList = new ArrayList<>();

        userListAdapter = new MatchesAdapter(MatchesList.this, userList);
        userListView = findViewById(R.id.userListView);
        userListView.setAdapter(userListAdapter);
        retrieveUserList();

    }

    private void retrieveUserList() {
        String url = "https://pawsomematch.online/android/retrieve_pet_list.php?user_id=" + user_id;

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
                                userList.add(new Matches(acceptedUserId, acceptedFirstName, acceptedLastName, acceptingUserId, acceptingFirstName, acceptingLastName, acceptingPetName, acceptingPetID, acceptedPetName, acceptedPetID, isAccepted));
                            }

                            // Notify the adapter that the data has changed
                            userListAdapter.notifyDataSetChanged();

                            TextView noMessageTextView = findViewById(R.id.noMessageTextView);
                            noMessageTextView.setVisibility(userList.isEmpty() ? View.VISIBLE : View.GONE);


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
        Volley.newRequestQueue(MatchesList.this).add(jsonArrayRequest);
    }
}