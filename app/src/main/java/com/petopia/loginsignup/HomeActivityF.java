package com.petopia.loginsignup;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeActivityF extends Fragment {

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    public ImageView acceptance;
    public ImageView notification;
    private Handler handler = new Handler();
    String userId;
    ImageView dogBtn, catBtn, hamsterBtn, rabbitBtn;
    private static final int POLL_INTERVAL = 6000;
    private List<UserNotification> userList = new ArrayList<>();

    private List<Notification> notificationList = new ArrayList<>();
    private ImageView noNotificationTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_activity, container, false);
        acceptance = view.findViewById(R.id.acceptance);
        notification = view.findViewById(R.id.notification);

        userId = HomeActivity.userIdTv.getText().toString().trim();


        viewPager2 = view.findViewById(R.id.learn);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.learn1));
        sliderItems.add(new SliderItem(R.drawable.learn2));
        sliderItems.add(new SliderItem(R.drawable.learn3));
        sliderItems.add(new SliderItem(R.drawable.learn4));

        SliderAdapter sliderAdapter = new SliderAdapter(sliderItems, viewPager2);
        sliderAdapter.setOnSliderItemClickListener(new SliderAdapter.OnSliderItemClickListener() {
            @Override
            public void onSliderItemClick(int position) {
                switch (position) {
                    case 0:
                        Intent intent1 = new Intent(requireContext(), dog.class);
                        startActivity(intent1);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case 1:
                        Intent intent2 = new Intent(requireContext(), cat.class);
                        startActivity(intent2);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case 2:
                        Intent intent3 = new Intent(requireContext(), rabbit.class);
                        startActivity(intent3);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case 3:
                        Intent intent4 = new Intent(requireContext(), hamster.class);
                        startActivity(intent4);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                }
            }
        });

        viewPager2.setAdapter(sliderAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        noNotificationTextView = view.findViewById(R.id.noNotificationTextView);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        ImageView notif1 = view.findViewById(R.id.notification);
        notif1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NotificationActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageView dogBtn = view.findViewById(R.id.dogBtn);

        dogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), qDog.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageView catBtn = view.findViewById(R.id.catBtn);

        catBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), qCat.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageView hamsterBtn = view.findViewById(R.id.hamsterBtn);

        hamsterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), qHamster.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        ImageView rabbitBtn = view.findViewById(R.id.rabbitBtn);

        rabbitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), qRabbit.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        ImageView btnAddPet = view.findViewById(R.id.btnAddPet);

        btnAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), PetRegister.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageView btnBrowsePet = view.findViewById(R.id.btnBrowsePet);

        btnBrowsePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), BrowsePet.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageView btnPurchases = view.findViewById(R.id.btnPurchases);

        btnPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), OrderActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageView btnSales = view.findViewById(R.id.btnSales);

        btnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), SaleActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageView btnSellPet = view.findViewById(R.id.btnSellPet);

        btnSellPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), AddPost.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageView set = view.findViewById(R.id.settings);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Settings.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        acceptance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), AcceptanceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);

                int currentPosition = position % sliderItems.size();

            }
        });

        SharedPreferences preferences = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean noNotifications = preferences.getBoolean("noNotifications", false);

        if (noNotifications) {
            noNotificationTextView.setVisibility(View.VISIBLE);
        } else {
            noNotificationTextView.setVisibility(View.GONE);
        }

        retrieveUserList();
        fetchNotifications(userId);
        handler.post(pollNotifications);
        handler.post(pollServer);

        return view;
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
        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest);
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

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            // Show the indicator if there are new like
                            if (!newLikedPets.isEmpty()) {
                                // Debugging output
                                for (UserNotification likedPet : newLikedPets) {
                                    acceptance.setImageResource(R.drawable.pawprint);
                                    Log.d("NewLikedPet", "New Liked Pet: " + likedPet.getPetName());
                                }
                            } else {
                                acceptance.setImageResource(R.drawable.heart);
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

            Volley.newRequestQueue(getContext()).add(likedPetsRequest);

            // Schedule the next poll
            handler.postDelayed(this, POLL_INTERVAL);
        }
    };


    private void fetchNotifications(String userId) {
        String url = "https://pawsomematch.online/android/get_notification.php?user_id=" + userId;

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int notif_id = jsonObject.getInt("notif_id");
                            String message = jsonObject.getString("message");
                            String date = jsonObject.getString("date");
                            // Add the new notification to the list
                            notificationList.add(new Notification(notif_id, message, date));
                        }
                        // Notify any observers of the change in data
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(url);
    }

    private Runnable pollNotifications = new Runnable() {
        @Override
        public void run() {
            // Make a request to the PHP script to fetch notifications
            String notificationsUrl = "https://pawsomematch.online/android/get_notification.php?user_id=" + userId;

            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    try {
                        URL url = new URL(params[0]);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        return result.toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    List<Notification> newNotificationList = new ArrayList<>();
                    if (s != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            // Process the response to check for new notifications
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int notif_id = jsonObject.getInt("notif_id");
                                // Check if this notification is new, e.g., not in the previously fetched data
                                if (!isNotificationInPreviouslyFetchedData(notif_id)) {
                                    String message = jsonObject.getString("message");
                                    String date = jsonObject.getString("date");
                                    // Add the new notification to the list
                                    Notification newNotification = new Notification(notif_id, message, date);

                                    newNotificationList.add(newNotification);
                                }
                            }

                            if (!newNotificationList.isEmpty()) {
                                // Debugging output
                                for (Notification newNotification : newNotificationList) {
                                    notification.setImageResource(R.drawable.notification);
                                    Log.d("NewNotif", "New Notif: " + newNotification.getNotif_id());
                                }
                            } else {
                                notification.setImageResource(R.drawable.hnotif);
                                // Debugging output
                                Log.d("NewNotif", "No new Notif");
                            }
                            // Notify any observers of the change in data
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(notificationsUrl);

            // Schedule the next poll
            handler.postDelayed(this, POLL_INTERVAL);
        }
    };

    // Your method to check if the notification is in previously fetched data
    private boolean isNotificationInPreviouslyFetchedData(int newNotifId) {
        for (Notification existingNotification : notificationList) {
            // Compare the new notification with existing notifications based on some unique identifier.
            // For example, you might compare them based on notification ID or some other unique property.
            if (newNotifId == existingNotification.getNotif_id()) {
                // If there is a match, it's not a new notification
                return true;
            }
        }
        // If no match is found, it's a new notification
        return false;
    }

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

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // Set this fragment as the listener in HomeActivity
        handler.post(pollServer);
        handler.post(pollNotifications);
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }


    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(pollServer);
        handler.removeCallbacks(pollNotifications);
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}
