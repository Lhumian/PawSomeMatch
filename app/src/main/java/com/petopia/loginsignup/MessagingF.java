package com.petopia.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

public class MessagingF extends Fragment {
    private ListView userListView;

    private MessageUserAdapter userListAdapter;
    private List<UserMessage> userList;

    private String userId;

    private View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messaging_r, container, false);

        userId = HomeActivity.userIdTv.getText().toString().trim();

        userList = new ArrayList<>();

        userListAdapter = new MessageUserAdapter(getContext(), userList);
        userListView = view.findViewById(R.id.userListView);
        userListView.setAdapter(userListAdapter);
        retrieveUserList();

        return view;
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

                            TextView noMessageTextView = view.findViewById(R.id.noMessageTextView);
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
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }

    private void openChat(UserMessage user) {
        int userId = user.getAcceptedUserId();
        String userName = user.getAcceptedFirstName() + " " + user.getAcceptedLastName();
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("receiver_name", userName);

        intent.putExtra("acceptingPetID", user.getAcceptingPetID());
        intent.putExtra("acceptedPetID", user.getAcceptedPetID());
        startActivity(intent);
    }

}