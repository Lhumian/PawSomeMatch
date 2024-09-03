package com.petopia.loginsignup;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListAdapter extends ArrayAdapter<UserNotification> {
    private Context context;
    private List<UserNotification> userList;  // Change this to UserNotification
    private SharedPreferences sharedPreferences;
    private int loggedInUserId;
    ImageView userPhotoImageView;

    public UserListAdapter(Context context, List<UserNotification> userList, int loggedInUserId) {
        super(context, 0, userList);
        this.context = context;
        this.userList = userList;
        this.loggedInUserId = loggedInUserId;
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);
        }

        // Get the user notification at the current position
        UserNotification user = getItem(position);

        // Get the views from the inflated layout
        TextView userNameTextView = convertView.findViewById(R.id.userNameTextView);
        TextView petNameTextView = convertView.findViewById(R.id.petNameTextView);
        RelativeLayout acceptButton = convertView.findViewById(R.id.acceptButton);
        RelativeLayout declineButton = convertView.findViewById(R.id.declineButton);
        RelativeLayout btnView = convertView.findViewById(R.id.btnView);

        // Set the user's name
        if (user != null) {
            String ownerName = user.getFirstName() + " " + user.getLastName();
            String petName = user.getPetName();
            userNameTextView.setText(ownerName);
            petNameTextView.setText(petName);
        }

        // Check if the user has been accepted
        if (user != null && user.isAccepted()) {
            // User is accepted, hide accept and decline buttons
            acceptButton.setVisibility(View.GONE);
            declineButton.setVisibility(View.GONE);
        } else {
            // User is not accepted, show accept and decline buttons
            acceptButton.setVisibility(View.VISIBLE);
            declineButton.setVisibility(View.VISIBLE);
        }

        // Set OnClickListener for the accept button
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    acceptUser(user);
                }
            }
        });

        // Set OnClickListener for the decline button
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    declineUser(user);
                }
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PetViewActivity.class);
                String petId = user.getPetId();
                intent.putExtra("petId", petId);
                context.startActivity(intent);

            }
        });

        userPhotoImageView = convertView.findViewById(R.id.userPhotoImageView);
        new FetchImageUrlTask(userPhotoImageView).execute(String.valueOf(user.getUserId()));

        return convertView;
    }

    private class FetchImageUrlTask extends AsyncTask<String, Void, String> {

        private ImageView imageView;
        public FetchImageUrlTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected String doInBackground(String... params) {
            String userId = params[0];
            String imageUrl = "";

            try {
                String imageUrlFetchUrl = "https://pawsomematch.online/android/get_profile_image.php?UserID=" + userId;
                URL url = new URL(imageUrlFetchUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                Log.d(TAG, "Response code: " + responseCode);
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
                    Log.e(TAG, "Error fetching image URL. Response code: " + responseCode);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching image URL: " + e.getMessage());
            }

            return imageUrl;
        }

        @Override
        protected void onPostExecute(String imageUrl) {
            if (!imageUrl.isEmpty()) {
                String imageUrl1 = "https://pawsomematch.online/android/" + imageUrl;

                Picasso.get().load(imageUrl1).into(imageView);
                Log.d(TAG, "Image URL: " + imageUrl1);
            } else {
                Log.d(TAG, "Error Retrieving Photo");
            }
        }
    }


    private void openChat(int userId, String userName) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("user_id", userId); // Use "user_id" as the key for the user ID
        intent.putExtra("receiver_name", userName); // Use "receiver_name" as the key for the receiver's name
        getContext().startActivity(intent);
    }

    private void acceptUser(UserNotification user) {
        // Update the acceptance status in the local list
        user.setAccepted(true);
        notifyDataSetChanged();

        // Update the acceptance status in the server
        int acceptingUserId = loggedInUserId;
        int acceptedUserId = user.getUserId();
        String likedPetId = user.getLikedPetId(); // Updated to getLikedPetId()
        String petId = user.getPetId(); // Add the pet_ID getter method
        updateAcceptanceStatus(acceptingUserId, acceptedUserId, likedPetId, petId, true);
    }

    private void declineUser(UserNotification user) {
        // Remove the user from the local list
        userList.remove(user);
        notifyDataSetChanged();

        // Update the acceptance status in the server
        int acceptingUserId = loggedInUserId;
        int declinedUserId = user.getUserId();
        String likedPetId = user.getLikedPetId(); // Updated to getLikedPetId()
        String petId = user.getPetId(); // Add the pet_ID getter method
        updateAcceptanceStatus(acceptingUserId, declinedUserId, likedPetId, petId, false);
    }


    private void updateAcceptanceStatus(int acceptingUserId, int acceptedUserId, String likedPetId, String petId, boolean isAccepted) {
        String url = "https://pawsomematch.online/android/update_acceptance.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response
                        if (response.equals("Acceptance status updated successfully and message sent")) {
                            // Display a success message
                            Toast.makeText(context, "User accepted and message sent!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(context, "Error updating acceptance status: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("accepting_user_id", String.valueOf(acceptingUserId));
                params.put("accepted_user_id", String.valueOf(acceptedUserId));
                params.put("liked_pet_id", String.valueOf(likedPetId)); // Include liked_pet_id
                params.put("pet_ID", String.valueOf(petId)); // Include pet_ID
                params.put("is_accepted", isAccepted ? "1" : "0");
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(context).add(stringRequest);
    }
}
