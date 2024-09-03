package com.petopia.loginsignup;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BrowseAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<SellPets> petsSellArrayList;
    ImageView imgPhoto;
    ImageView backBtn;

    public BrowseAdapter(Context context, ArrayList<SellPets> petsSellArrayList) {
        this.context = context;
        this.petsSellArrayList = petsSellArrayList;
    }
    // Constructor and methods...

    @Override
    public int getCount() {
        return petsSellArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.browse_list_pet, container, false);

        TextView petNameTextView = view.findViewById(R.id.pet_name);
        TextView breedTextView = view.findViewById(R.id.breed);
        TextView breedTypeTextView = view.findViewById(R.id.breed_type);
        TextView genderTextView = view.findViewById(R.id.gender);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView descriptionTextView = view.findViewById(R.id.description);

        imgPhoto = view.findViewById(R.id.imgPhoto);
        backBtn = view.findViewById(R.id.backBtn);

        SellPets pet = petsSellArrayList.get(position);
        petNameTextView.setText(pet.getPet_Name());
        breedTextView.setText(pet.getBreed());
        breedTypeTextView.setText(pet.getBreedtype());
        genderTextView.setText(pet.getGender());
        priceTextView.setText(String.format("%.2f", pet.getPrice()) );
        descriptionTextView.setText(pet.getDescription());

        ImageView payButton = view.findViewById(R.id.payButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) context).finish();
                // Apply transition animations
                ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellPets selectedPet = petsSellArrayList.get(position);

                // Create an Intent to pass the selected pet information to the PayConfirmation activity

                float price = petsSellArrayList.get(position).getPrice();

                SharedPreferences sharedPreferences = context.getSharedPreferences("SelectedPetData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Store the selected pet's information
                editor.putString("selectedPetID", selectedPet.getPet_ID());
                editor.putString("selectedPetName", selectedPet.getPet_Name());
                editor.putString("selectedPetCategory", selectedPet.getCategory());
                editor.putString("selectedPetBreed", selectedPet.getBreed());
                editor.putString("selectedPetColor", selectedPet.getColor());
                editor.putFloat("selectedPrice", price);

                // Apply the changes
                editor.apply();

                Intent intent = new Intent(context, CheckoutActivity.class);
                context.startActivity(intent);
            }
        });



        new FetchImageUrlTask().execute(pet.getPet_ID());

        container.addView(view);
        return view;
    }



    private class FetchImageUrlTask extends AsyncTask<String, Void, String> {
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
                String imageUrl1 = "https://pawsomematch.online/android/sell_pet_photos/" + imageUrl;
                Picasso.get().load(imageUrl1).into(imgPhoto);
                Log.d(TAG, "Image URL: " + imageUrl1);
            } else {
                Log.d(TAG, "Error Retrieving Photo");
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}