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
import android.widget.TextView;
import android.widget.Toast;

import com.adyen.checkout.components.model.PaymentMethodsApiResponse;
import com.adyen.checkout.components.model.payments.Amount;
import com.adyen.checkout.core.api.Environment;
import com.adyen.checkout.dropin.DropIn;
import com.adyen.checkout.dropin.DropInConfiguration;

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
import java.util.Random;
import java.util.UUID;

public class DeliveryGcashActivity extends AppCompatActivity {

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

    String selectedName1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_gcash);

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
                Intent intent = new Intent(DeliveryGcashActivity.this, AddressGcashActivity.class);
                startActivity(intent);
            }
        });

        // Retrieve the selected address from the SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Address", MODE_PRIVATE);

        selectedName1 = sharedPreferences.getString("selected_address_name", "");
        selectedPhone = sharedPreferences.getString("selected_address_phone", "");
        selectedCity = sharedPreferences.getString("selected_address_city", "");
        selectedBarangay = sharedPreferences.getString("selected_address_barangay", "");
        selectedStreet = sharedPreferences.getString("selected_address_street", "");

        if (!selectedName1.isEmpty()) {
            // Create a formatted string to display the saved address
            String formattedAddress = "Selected Address:\n" +
                    "Name: " + selectedName1 + "\n" +
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

        btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedName1.isEmpty()) {
                    // Show a toast or alert indicating that the address is required
                    Toast.makeText(DeliveryGcashActivity.this, "Please select an address before checkout", Toast.LENGTH_SHORT).show();
                    return; // Stop the checkout process if the address is not selected
                }

                Amount amount = new Amount();
                amount.setCurrency("PHP");
                amount.setValue((int) (selectedPrice * 100));
                String paymentMethodData = "gcash";
                PaymentMethodsApiResponse paymentMethodsApiResponse = getIntent().getParcelableExtra("paymentMethodsApiResponse");

                DropInConfiguration dropInConfiguration = new DropInConfiguration.Builder(
                        DeliveryGcashActivity.this,
                        PaymentService.class,
                        "test_NSVLLNOFWBBBLLH77YL3RR5QIANXYR6W"
                )
                        .setAmount(amount)
                        .setEnvironment(Environment.TEST)
                        .build();

                Intent resultIntent = new Intent(DeliveryGcashActivity.this, PaymentService.class);
                resultIntent.putExtra("paymentMethodData", paymentMethodData);

                // Start the payment process
                DropIn.startPayment(DeliveryGcashActivity.this, paymentMethodsApiResponse, dropInConfiguration, resultIntent);

                String uniqueID = generateNumericUUID();
                String selectedPetID = sharedPreferences1.getString("selectedPetID", "");
                String buyerID =  HomeActivity.userIdTv.getText().toString().trim();
                String buyerPhone = selectedPhone;
                float petPrice = selectedPrice;
                /*double shippingPrice = 200.0;*/
                float totalPrice = petPrice;
                String paymentMode = "GCash";
                String city = selectedCity;
                String barangay = selectedBarangay;
                String street = selectedStreet;
                String orderDate = dateFormat.format(new Date());;
                String deliveryProgress = "On Progress";

                // Create a request body with the data
                RequestBody requestBody = new FormBody.Builder()
                        .add("reference", uniqueID)
                        .add("pet_ID", selectedPetID)
                        .add("buyer_ID", buyerID)
                        .add("buyer_phone", buyerPhone)
                        .add("pet_price", String.valueOf(petPrice))
                        /*.add("shipping", String.valueOf(shippingPrice))*/
                        .add("total_price", String.valueOf(totalPrice))
                        .add("payment_mode", paymentMode)
                        .add("city", city)
                        .add("barangay", barangay)
                        .add("street", street)
                        .add("order_date", orderDate)
                        .add("delivery_progress", deliveryProgress)
                        .build();

                // Replace "your_php_script_url" with the actual URL of your PHP script
                String phpScriptUrl = "https://pawsomematch.online/android/add_delivery.php";

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(phpScriptUrl)
                        .post(requestBody) // Use POST method to send data
                        .build();

                // Inside your Android code where you handle the response from the PHP script
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String responseBody = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (responseBody.contains("successfully")) {
                                        // Order created successfully
                                        Log.d("DeliveryGcashActivity", "Order created successfully");
                                    } else {
                                        // Insertion failed
                                        Toast.makeText(DeliveryGcashActivity.this, "Failed to create the order: " + responseBody, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });


                // After creating the request body
                SharedPreferences sharedPreferences = getSharedPreferences("OrderData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("reference", uniqueID);
                editor.putString("pet_ID", selectedPetID);

                editor.putString("seller", txtSeller.getText().toString());
                editor.putString("petName", selectedPetName);
                editor.putString("petCategory", selectedPetCategory);
                editor.putString("petBreed", selectedPetBreed);
                editor.putString("petColor", selectedPetColor);


                editor.putFloat("petPrice", petPrice);
                /*editor.putString("shippingPrice", String.valueOf(shippingPrice));*/
                editor.putFloat("totalPrice", totalPrice);
                editor.putString("paymentMode", paymentMode);
                editor.putString("orderDate", orderDate);
                editor.putString("deliveryProgress", deliveryProgress);


                editor.putString("buyerID", buyerID);
                editor.putString("buyerPhone", buyerPhone);
                editor.putString("city", city);
                editor.putString("barangay", barangay);
                editor.putString("street", street);


                editor.apply();

                startService(resultIntent);
            }
        });

    }
    private String generateNumericUUID() {
        Random random = new Random();
        StringBuilder numericUUID = new StringBuilder();
        for (int i = 0; i < 15; i++) { // You can change 9 to the desired length
            numericUUID.append(random.nextInt(10)); // Append a random digit (0-9)
        }
        return numericUUID.toString();
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

