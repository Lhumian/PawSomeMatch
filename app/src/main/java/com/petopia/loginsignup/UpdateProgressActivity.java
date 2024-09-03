package com.petopia.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class UpdateProgressActivity extends AppCompatActivity {

    Spinner spinnerDeliveryProgress;
    String pet_ID;

    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_progress);

        backBtn = findViewById(R.id.backBtn);

        // Retrieve the values passed from the Intent
        Intent intent = getIntent();
        pet_ID = intent.getStringExtra("petId");
        String buyerPhone = intent.getStringExtra("buyerPhone");
        String petPrice = intent.getStringExtra("petPrice");
        String totalPrice = intent.getStringExtra("totalPrice");
        String paymentMode = intent.getStringExtra("paymentMode");
        String city = intent.getStringExtra("city");
        String barangay = intent.getStringExtra("barangay");
        String street = intent.getStringExtra("street");
        String orderDate = intent.getStringExtra("orderDate");
        String deliveryProgress = intent.getStringExtra("deliveryProgress");

        // Find the TextView elements in your layout
        TextView txtPetId = findViewById(R.id.txtPetId);
        TextView txtBuyerPhone = findViewById(R.id.txtBuyerPhone);
        TextView txtPetPrice = findViewById(R.id.txtPetPrice);
        TextView txtTotalPrice = findViewById(R.id.txtTotalPrice);
        TextView txtPaymentMode = findViewById(R.id.txtPaymentMode);
        TextView txtCity = findViewById(R.id.txtCity);
        TextView txtBarangay = findViewById(R.id.txtBarangay);
        TextView txtStreet = findViewById(R.id.txtStreet);
        TextView txtOrderDate = findViewById(R.id.txtOrderDate);
        TextView txtDeliveryProgress = findViewById(R.id.txtDeliveryProgress);
        spinnerDeliveryProgress = findViewById(R.id.spinnerDeliveryProgress);
        Button btnUpdateProgress = findViewById(R.id.btnUpdateProgress);

        // Set the text for each TextView with the retrieved values
        txtPetId.setText("Pet ID: " + pet_ID);
        txtBuyerPhone.setText(buyerPhone);
        txtPetPrice.setText("Pet Price: " + petPrice);
        txtTotalPrice.setText("Total Price: " + totalPrice);
        txtPaymentMode.setText("Payment Mode: " + paymentMode);
        txtCity.setText(city);
        txtBarangay.setText(barangay);
        txtStreet.setText(street);
        txtOrderDate.setText(orderDate);
        txtDeliveryProgress.setText(deliveryProgress);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        spinnerDeliveryProgress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item from the spinner
                String selectedDeliveryProgress = spinnerDeliveryProgress.getSelectedItem().toString().trim();
                // Update the TextView to match the selected item
                txtDeliveryProgress.setText(selectedDeliveryProgress);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle nothing selected if needed
            }
        });


        // Set a listener for the button click
        btnUpdateProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected item from the spinner
                String selectedDeliveryProgress = spinnerDeliveryProgress.getSelectedItem().toString().trim();
                txtDeliveryProgress.setText(selectedDeliveryProgress);
                updateDeliveryProgress(selectedDeliveryProgress);
                Intent intent = new Intent(UpdateProgressActivity.this, SaleActivity.class);
                startActivity(intent);
                finish();
                onBackPressed();
            }
        });
    }

    private void updateDeliveryProgress(String selectedDeliveryProgress) {

        // Define the URL for updating delivery progress
        String updateUrl = "https://pawsomematch.online/android/delivery_update.php";

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a StringRequest to make a POST request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Handle the response here
                if (response.equals("success")) {
                    // Update the UI or show a success message
                    Toast.makeText(UpdateProgressActivity.this, "Delivery Progress updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the case where the update was not successful
                    Toast.makeText(UpdateProgressActivity.this, "Error updating Delivery Progress", Toast.LENGTH_SHORT).show();
                    Log.e("UpdateProgressActivity", "Error updating Delivery Progress. Response: " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle network errors here
                error.printStackTrace();
                // Show a toast message for the network error
                Toast.makeText(UpdateProgressActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                Log.e("UpdateProgressActivity", "Network Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pet_ID", pet_ID);
                params.put("deliveryProgress", selectedDeliveryProgress);
                return params;
            }
        };

        // Add the request to the queue
        requestQueue.add(stringRequest);
    }
}
