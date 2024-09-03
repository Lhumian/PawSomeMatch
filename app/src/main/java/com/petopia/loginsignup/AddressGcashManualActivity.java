package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressGcashManualActivity extends AppCompatActivity {

    ImageView addAddress;
    ListView listView;
    AddressAdapter adapter;
    ArrayList<AddressModel> addressList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        addAddress = findViewById(R.id.addAddress);
        listView = findViewById(R.id.listView);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressGcashManualActivity.this, NewAddressGcashManualActivity.class);
                startActivity(intent);
            }
        });

        // Initialize the adapter and set it to the ListView
        adapter = new AddressAdapter(this, addressList);
        listView.setAdapter(adapter);

        retrieveAddresses();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected address
                AddressModel selectedAddress = addressList.get(position);

                // Create an Intent to pass the selected address to the DeliveryActivity
                Intent intent = new Intent(AddressGcashManualActivity.this, GCashManualActivity.class);
                intent.putExtra("selected_address", selectedAddress);

                // Save the selected address in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("Address", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_address_name", selectedAddress.getName());
                editor.putString("selected_address_phone", selectedAddress.getPhone());
                editor.putString("selected_address_city", selectedAddress.getCity());
                editor.putString("selected_address_barangay", selectedAddress.getBarangay());
                editor.putString("selected_address_street", selectedAddress.getStreet());
                editor.apply();

                startActivity(intent);
            }
        });
    }

    private void retrieveAddresses() {
        // Define the URL for your PHP script to retrieve address data
        String userId = HomeActivity.userIdTv.getText().toString().trim();
        String url = "https://pawsomematch.online/android/retrieve_address.php";

// Create a StringRequest to send a POST request to the server with user_ID
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here (e.g., network issues)
                        Toast.makeText(AddressGcashManualActivity.this, "Error retrieving addresses", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add the user_ID to the POST request parameters
                Map<String, String> params = new HashMap<>();
                params.put("user_ID", userId);
                return params;
            }
        };

// Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);
    }

    private void processResponse(String response) {
        try {
            // Parse the JSON response
            JSONArray jsonArray = new JSONArray(response);

            // Clear the existing list of addresses
            addressList.clear();

            // Iterate through the JSON array and populate the addressList
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String phone = jsonObject.getString("phone");
                String city = jsonObject.getString("city");
                String barangay = jsonObject.getString("barangay");
                String street = jsonObject.getString("street");

                // Create an AddressModel and add it to the list
                AddressModel address = new AddressModel(name, phone, city, barangay, street);
                addressList.add(address);
            }

            // Notify the adapter that the data has changed
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
