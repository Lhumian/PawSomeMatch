package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    ImageView btnAddress, backBtn;
    Button btnCheckout;
    TextView selectedAddressTextView;
    TextView txtPrice, txtShipping, txtTotalPrice, txtPetName, txtSeller, txtPetCategory, txtPetColor, txtPetBreed;

    float selectedPrice;
    String selectedPhone;
    String selectedCity;
    String selectedBarangay;
    String selectedStreet;
    String selectedPetName;
    String selectedPetCategory;
    String selectedPetBreed;
    String selectedPetColor;

    SimpleDateFormat dateFormat;

    Button btnPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btnAddress = findViewById(R.id.btnAddress);
        selectedAddressTextView = findViewById(R.id.selectedAddressTextView);

        // Initialize TextViews for displaying pet details
        txtPrice = findViewById(R.id.txtPrice);
        /*txtShipping = findViewById(R.id.txtShipping);*/
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtPetName = findViewById(R.id.txtPetName);
        txtSeller = findViewById(R.id.txtSeller);
        txtPetCategory = findViewById(R.id.txtPetCategory);
        txtPetColor = findViewById(R.id.txtPetColor);
        txtPetBreed = findViewById(R.id.txtPetBreed);

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, AddressCheckoutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // Retrieve the selected address from the SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Address", MODE_PRIVATE);

        String selectedName = sharedPreferences.getString("selected_address_name", "");
        selectedPhone = sharedPreferences.getString("selected_address_phone", "");
        selectedCity = sharedPreferences.getString("selected_address_city", "");
        selectedBarangay = sharedPreferences.getString("selected_address_barangay", "");
        selectedStreet = sharedPreferences.getString("selected_address_street", "");

        if (!selectedName.isEmpty()) {
            // Create a formatted string to display the saved address
            String formattedAddress = "Selected Address:\n" +
                    "Name: " + selectedName + "\n" +
                    "Phone: " + selectedPhone + "\n" +
                    "City: " + selectedCity + "\n" +
                    "Barangay: " + selectedBarangay + "\n" +
                    "Street: " + selectedStreet;

            // Set the formatted address text to the TextView
            selectedAddressTextView.setText(formattedAddress);
        } else {
            // Handle the case where no address is saved
            selectedAddressTextView.setText("No address saved");
        }



        // Retrieve pet details from the intent
        SharedPreferences sharedPreferences1 = getSharedPreferences("SelectedPetData", Context.MODE_PRIVATE);
        String selectedPetID = sharedPreferences1.getString("selectedPetID", "");
        selectedPetName = sharedPreferences1.getString("selectedPetName", "");
        selectedPetCategory = sharedPreferences1.getString("selectedPetCategory", "");
        selectedPetBreed = sharedPreferences1.getString("selectedPetBreed", "");
        selectedPetColor = sharedPreferences1.getString("selectedPetColor", "");
        selectedPrice = sharedPreferences1.getFloat("selectedPrice", 0.0f); // Convert to float

        // Display pet details in TextViews
        txtPetName.setText(selectedPetName);
        txtPetCategory.setText(selectedPetCategory);
        txtPetColor.setText(selectedPetColor);
        txtPetBreed.setText(selectedPetBreed);
        txtPrice.setText("₱" + selectedPrice);

        // Calculate and display total price (price + shipping)
        /*double shippingPrice = 200.0; // Change this to the actual shipping price
        double totalPrice = selectedPrice + shippingPrice;
        txtShipping.setText("₱" + shippingPrice);*/
        txtTotalPrice.setText("₱" + selectedPrice);

        retrieveUserName(selectedPetID);

        btnPaymentMethod = findViewById(R.id.btnPaymentMethod);
        btnPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, PayConfirmation.class);

                startActivity(intent);
            }
        });

        btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckoutActivity.this, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void retrieveUserName(String selectedPetID) {
        // Replace "your_php_script_url" with the actual URL of your PHP script
        String phpScriptUrl = "https://pawsomematch.online/android/retrieve_seller.php?selectedPetID=" + selectedPetID;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(phpScriptUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        // Parse the JSON response
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String firstName = jsonObject.optString("first_name");
                        String middleName = jsonObject.optString("middle_name");
                        String lastName = jsonObject.optString("last_name");

                        // Combine the first name, middle name, and last name to get the full name
                        final String fullName = firstName + " " + middleName + " " + lastName;

                        // Update the TextView with the retrieved name on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtSeller.setText(fullName);
                                // Log the fullName
                                Log.d("FullName", fullName);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}

