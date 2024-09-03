package com.petopia.loginsignup;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MyAdapter extends ArrayAdapter<Pets> {
    Context context;
    List<Pets> arrayListPets;

    public MyAdapter(@NonNull Context context, List<Pets> arrayListPets) {
        super(context, R.layout.custom_list_item, arrayListPets);
        this.context = context;
        this.arrayListPets = arrayListPets;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.custom_list_item, parent, false);
        }

        TextView tvPetName = view.findViewById(R.id.txt_petname);
        ImageView imgPhoto = view.findViewById(R.id.imgPhoto);
        TextView tvPetBreed = view.findViewById(R.id.txt_breed);

        Pets currentPet = arrayListPets.get(position);

        tvPetName.setText(currentPet.getPet_Name());
        tvPetBreed.setText(currentPet.getBreed());

        String imageUrlFetchUrl = "https://pawsomematch.online/android/get_pet_image.php?petId=" + currentPet.getPet_ID();

        // Check if the image URL is empty or null, and load the default image in that case
        if (imageUrlFetchUrl != null && !imageUrlFetchUrl.isEmpty()) {
            // Load the image using Picasso
            new FetchImageUrlTask(imgPhoto).execute(imageUrlFetchUrl);
        } else {
            // Load the default image
            imgPhoto.setImageResource(R.drawable.learn1); // You can set the default image
        }

        return view;
    }

    private class FetchImageUrlTask extends AsyncTask<String, Void, String> {
        private ImageView imageView;

        public FetchImageUrlTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected String doInBackground(String... params) {
            String imageUrl = "";

            try {
                String imageUrlFetchUrl = params[0];
                URL url = new URL(imageUrlFetchUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
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
                    // Handle error here, e.g., log an error message
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error here, e.g., log an error message
            }

            return imageUrl;
        }

        @Override
        protected void onPostExecute(String imageUrl) {
            // Log the image URL
            Log.d("MyAdapter", "Image URL: " + imageUrl);

            if (!imageUrl.isEmpty()) {
                String imageUrll = "https://pawsomematch.online/android/pictures/" + imageUrl;
                // Load the image into the ImageView using Picasso
                Picasso.get().load(imageUrll)
                        .placeholder(R.drawable.pawsomelogo) // You can set a placeholder image
                        .error(R.drawable.pawsomelogo) // You can set an error image if loading fails
                        .into(imageView);
            } else {
                // Handle the case where the image URL is empty or invalid
                Log.e("MyAdapter", "Empty or invalid image URL");
                // Load the default image
                imageView.setImageResource(R.drawable.pawsomelogo); // You can set the default image
            }
        }
    }
}