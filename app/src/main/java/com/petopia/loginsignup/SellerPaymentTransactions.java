package com.petopia.loginsignup;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SellerPaymentTransactions extends MainActivity {

    private String user_id;
    private RecyclerView recyclerView;
    private SellerPaymentAdapter sellerPaymentAdapter;
    private List<SellerPayment> sellerPaymentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_payment_transactions);

        user_id = sharedPreferences.getString("user_id", "");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sellerPaymentList = new ArrayList<>();
        sellerPaymentAdapter = new SellerPaymentAdapter(this, sellerPaymentList);
        recyclerView.setAdapter(sellerPaymentAdapter);

        fetchPetDetails();
    }

    // Inside the SellerPaymentTransactions class
    private void fetchPetDetails() {
        // Replace with your server URL
        String url = "https://pawsomematch.online/android/get_seller_transactions.php?user_id=" + user_id;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Iterate through the JSON array and get pet details
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                // Get values from the JSON object
                                int petID = jsonObject.getInt("pet_ID");
                                int imageID = jsonObject.getInt("image_id");
                                String referenceNumber = jsonObject.getString("reference_number");
                                String imageName = jsonObject.getString("image_name");

                                // Create a SellerPayment object and add it to the list
                                SellerPayment sellerPayment = new SellerPayment(imageID, petID, referenceNumber, imageName);
                                sellerPaymentList.add(sellerPayment);
                            }

                            // Notify the adapter that the data has changed
                            sellerPaymentAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SellerPaymentTransactions.this, "Error fetching pet details", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
