package com.petopia.loginsignup;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pusher.client.user.User;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MessageUserAdapter extends ArrayAdapter<UserMessage> {
    private Context context;
    private List<UserMessage> userList;


    public MessageUserAdapter(Context context, List<UserMessage> userList) {
        super(context, 0, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_user_list_item, parent, false);
        }

        final UserMessage user = getItem(position); // Get the user at the current position

        if (user != null) {

            TextView userNameTextView = convertView.findViewById(R.id.userNameTextView);
            TextView likedPetTextView = convertView.findViewById(R.id.likedPetTextView);
            TextView petNameTextView = convertView.findViewById(R.id.petNameTextView);

            String ownerName = user.getAcceptedFirstName() + " " + user.getAcceptedLastName();
            String petName = user.getAcceptingPetName();

            userNameTextView.setText(ownerName);
            likedPetTextView.setText("Liked your Pet:");
            petNameTextView.setText(petName);

            ImageView userPhotoImageView = convertView.findViewById(R.id.userPhotoImageView);

            String UserID = String.valueOf(user.getAcceptedUserId());
            new FetchImageUrlTask(userPhotoImageView).execute(UserID);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the SharedPreferences instance
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("Chat", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    int userId = user.getAcceptedUserId();
                    String userName = user.getAcceptedFirstName() + " " + user.getAcceptedLastName();

                    editor.putInt("user_id", userId);
                    editor.putString("receiver_name", userName);
                    editor.putString("acceptingPetID", user.getAcceptingPetID());
                    editor.putString("acceptedPetID", user.getAcceptedPetID());
                    editor.apply();

                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    getContext().startActivity(intent);
                }
            });
        }

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
}
