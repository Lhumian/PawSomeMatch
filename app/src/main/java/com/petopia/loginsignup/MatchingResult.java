package com.petopia.loginsignup;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MatchingResult extends MainActivity {
    private ViewPager mViewPager;
    private PetPagerAdapter mPagerAdapter;
    private static final String TAG = MatchingResult.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private PetMatchAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListView mListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<String> likedUserList = new ArrayList<>();

    private String selectedColor;
    private String selectedBreedtype;

    String selectedPetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        context = this;
        setContentView(R.layout.activity_matching_result);

        mViewPager = findViewById(R.id.view_pager);
        mRecyclerView = findViewById(R.id.pet_list_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        String category = getIntent().getStringExtra("category");
        String breed = getIntent().getStringExtra("breed");
        String gender = getIntent().getStringExtra("gender");
        selectedColor = getIntent().getStringExtra("color");
        selectedBreedtype = getIntent().getStringExtra("breedtype");
        selectedPetID = getIntent().getStringExtra("petId");

        String email = sharedPreferences.getString("email","");

        mPagerAdapter = new PetPagerAdapter(new ArrayList<>(), email, selectedPetID); // Initialize the adapter
        mViewPager.setAdapter(mPagerAdapter); // Set the adapter to the ViewPager

        new GetPetTask(email).execute(category, breed, gender, selectedColor, selectedBreedtype); // Pass the gender to the AsyncTask

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform the refresh action
                refreshPets(email, category, breed, gender, selectedColor, selectedBreedtype);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
    }

    private void refreshPets(String email, String category, String breed, String gender, String color, String breedtype) {
        new GetPetTask(email).execute(category, breed, gender, color, breedtype); // Pass the gender to the AsyncTask
    }

    public class GetPetTask extends AsyncTask<String, Void, List<PetMatchData>> {
        private String email;

        public GetPetTask(String email) {
            this.email = email;
        }

        @Override
        protected List<PetMatchData> doInBackground(String... params) {

            String category = params[0];
            String breed = params[1];
            String gender = params[2];
            List<PetMatchData> petList = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                String encodedCategory = URLEncoder.encode(category, "UTF-8");
                String encodedBreed = URLEncoder.encode(breed, "UTF-8");
                String encodedEmail = URLEncoder.encode(email, "UTF-8");
                String encodedGender = URLEncoder.encode(gender, "UTF-8");
                String encodedColor = URLEncoder.encode(selectedColor, "UTF-8");
                String encodedBreedtype = URLEncoder.encode(selectedBreedtype, "UTF-8");
                String encodedPetId = URLEncoder.encode(selectedPetID, "UTF-8"); // Encode pet_ID

                URL url = new URL("https://pawsomematch.online/android/get_pet.php?category=" + encodedCategory + "&breed=" + encodedBreed + "&gender=" + encodedGender + "&color=" + encodedColor + "&breedtype=" + encodedBreedtype + "&email=" + encodedEmail + "&pet_ID=" + encodedPetId);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }

                if (builder.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }

                String jsonString = builder.toString();
                Log.d(TAG, "JSON response: " + jsonString); // Log the received JSON response

                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    PetMatchData pet = new PetMatchData();
                    pet.setPet_ID(jsonObject.getInt("pet_ID"));
                    pet.setPet_name(jsonObject.getString("pet_name"));
                    pet.setGender(jsonObject.getString("gender"));
                    pet.setCategory(jsonObject.getString("category"));
                    pet.setBreed(jsonObject.getString("breed"));
                    pet.setBreedtype(jsonObject.getString("breedtype"));
                    pet.setColor(jsonObject.getString("color"));
                    pet.setStudfee(jsonObject.getString("studfee"));
                    pet.setShares(jsonObject.getString("shares"));

                    int petId = jsonObject.getInt("pet_ID");
                    String imageUrl = getImageUrlForPet(petId);

                    pet.setImageUrl(imageUrl);
                    petList.add(pet);
                }

            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error ", e);
                // If the code didn't successfully get the pets data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            return petList;
        }

        private String getImageUrlForPet(int petId) {
            String imageUrl = ""; // Initialize an empty string to store the image URL

            try {
                String imageUrlFetchUrl = "https://pawsomematch.online/android/get_pet_image.php?petId=" + petId;

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

                    imageUrl = response.toString(); // Store the retrieved image URL
                } else {
                    Log.e(TAG, "Error fetching image URL. Response code: " + responseCode);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching image URL: " + e.getMessage());
            }

            return imageUrl;
        }


        @Override
        protected void onPostExecute(List<PetMatchData> petList) {
            if (petList != null && !petList.isEmpty()) {
                mAdapter = new PetMatchAdapter(MatchingResult.this, petList);
                mPagerAdapter.setPetList(petList); // Update the petList in the adapter
                mPagerAdapter.notifyDataSetChanged(); // Notify the adapter of the data change
            } else {
                TextView noPetsTextView = findViewById(R.id.no_pets_textview);
                noPetsTextView.setText("NO MATCH FOUND");
                noPetsTextView.setVisibility(View.VISIBLE);
            }

            swipeRefreshLayout.setRefreshing(false); // Hide the refresh indicator
        }

    }

    public class PetPagerAdapter extends PagerAdapter {
        private List<PetMatchData> petList;
        private String userEmail;
        private String selectedPetID;


        public PetPagerAdapter(ArrayList<PetMatchData> petList, String userEmail, String selectedPetID) {
            this.petList = petList;
            this.userEmail = userEmail;
            this.selectedPetID = selectedPetID;
        }

        public void setPetList(List<PetMatchData> petList) {
            this.petList = petList;
        }

        @Override
        public int getCount() {
            if (petList != null) {
                return petList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.pet_item, container, false);

            ImageView petImageView = view.findViewById(R.id.pet_image);
            TextView nameTextView = view.findViewById(R.id.pet_name);
            TextView genderTextView = view.findViewById(R.id.pet_gender);
            TextView categoryTextView = view.findViewById(R.id.pet_category);
            TextView breedTextView = view.findViewById(R.id.pet_breed);
            TextView breedtypeTextView = view.findViewById(R.id.pet_breedtype);
            TextView studfeeTextView = view.findViewById(R.id.pet_studfee);
            TextView sharesTextView = view.findViewById(R.id.pet_shares);

            ImageView backBtn = view.findViewById(R.id.backBtn);
            ImageView likeButton = view.findViewById(R.id.btn_like);

            // Set pet name and gender in the view
            PetMatchData pet = petList.get(position);
            nameTextView.setText(pet.getPet_name());
            genderTextView.setText(pet.getGender());
            categoryTextView.setText(pet.getCategory());
            breedTextView.setText(pet.getBreed());
            breedtypeTextView.setText(pet.getBreedtype());

            String gender = pet.getGender();

            if (gender.equals("Female")) {
                studfeeTextView.setVisibility(View.GONE); // Set studfeeTextView to GONE
                sharesTextView.setVisibility(View.GONE);   // Set sharesTextView to GONE
            } else {
                studfeeTextView.setVisibility(View.VISIBLE); // Set studfeeTextView to VISIBLE
                sharesTextView.setVisibility(View.VISIBLE);   // Set sharesTextView to VISIBLE
                studfeeTextView.setText(pet.getStudfee()); // Set the text when gender is not female
                sharesTextView.setText(pet.getShares());   // Set the text when gender is not female
            }

            String imageUrl = "https://pawsomematch.online/android/pictures/" + pet.getImageUrl(); // Replace with your actual image URL
            Log.d(TAG, "Image URL: " + imageUrl);
            Picasso.get().load(imageUrl).into(petImageView);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });


            // Handle the like button click event
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = sharedPreferences.getString("email","");
                    int likePetId = pet.getPet_ID();
                    String petName = pet.getPet_name();
                    String ownerName = pet.getOwner_name(); // Replace with the actual owner name field

                    // Send the pet ID to the PHP script
                    String likeUrl = "https://pawsomematch.online/android/like_pet.php?email=" + email + "&likePetId=" + likePetId + "&petId=" + selectedPetID;

                    // Perform the HTTP request to like the pet
                    StringRequest likeRequest = new StringRequest(Request.Method.GET, likeUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Handle the response from the PHP script
                            Toast.makeText(container.getContext(), response, Toast.LENGTH_SHORT).show();

                            // Parse the response and populate the likedUserList
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                likedUserList.clear(); // Clear the previous list

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String likedUser = jsonArray.getString(i);
                                    likedUserList.add(likedUser);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error
                            Toast.makeText(container.getContext(), "Error liking the pet: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Add the request to the request queue
                    RequestQueue queue = Volley.newRequestQueue(container.getContext());
                    queue.add(likeRequest);
                }
            });
            // Add the view to the container
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class LikePetTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String likeUrl = params[0];

            try {
                URL url = new URL(likeUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                int responseCode = urlConnection.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Pet liked successfully, update UI or perform any desired action
            } else {
                // Failed to like the pet, handle the error
            }
        }
    }
}

