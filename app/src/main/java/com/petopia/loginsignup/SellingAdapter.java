package com.petopia.loginsignup;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SellingAdapter extends ArrayAdapter<SellPets> {

    Context context;
    List<SellPets> petsSellArrayList;
    ImageView sellPetImage;
    public SellingAdapter(@NonNull Context context, List<SellPets> petsSellArrayList) {
        super(context, R.layout.selling_list,petsSellArrayList);

        this.context=context;
        this.petsSellArrayList=petsSellArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selling_list, null, true);

        // Initialize your TextViews
        TextView tvID = view.findViewById(R.id.txt_id);
        TextView tvPetName = view.findViewById(R.id.txt_petname);
        TextView tvSpecies = view.findViewById(R.id.txt_species);
        TextView breed = view.findViewById(R.id.breed);
        TextView price = view.findViewById(R.id.price);
        TextView description = view.findViewById(R.id.txt_desc);

        // Initialize your ImageView within the scope of this method
        sellPetImage = view.findViewById(R.id.browsePetImage);

        // Populate your TextViews with data
        float price1 = petsSellArrayList.get(position).getPrice();
        tvID.setText(petsSellArrayList.get(position).getPet_ID());
        tvPetName.setText(petsSellArrayList.get(position).getPet_Name());
        tvSpecies.setText(petsSellArrayList.get(position).getCategory());
        breed.setText(petsSellArrayList.get(position).getBreed());
        price.setText(String.valueOf(price1));
        description.setText(petsSellArrayList.get(position).getDescription());

        // Load the image using Picasso
        new FetchImageUrlTask(sellPetImage).execute(petsSellArrayList.get(position).getPet_ID());
        return view;
    }


    private class FetchImageUrlTask extends AsyncTask<String, Void, String> {
        private ImageView imageView;

        public FetchImageUrlTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected String doInBackground(String... params) {
            String pet_ID = params[0];
            String imageUrl = "";

            try {
                String imageUrlFetchUrl = "https://pawsomematch.online/android/get_pet_sell_image.php?pet_ID=" + pet_ID;

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
                imageView.setImageDrawable(null);
                String imageUrl1 = "https://pawsomematch.online/android/sell_pet_photos/" + imageUrl;
                Picasso.get().load(imageUrl1).into(imageView);
                Log.d(TAG, "Image URL: " + imageUrl1);
            } else {
                Log.d(TAG, "Error Retrieving Photo");
            }
        }
    }


}