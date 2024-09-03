package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GcashPaymentActivity extends AppCompatActivity {

    private String uniqueID;
    private String selectedPetID;
    private String buyerID;
    private String buyerPhone;
    private float petPrice;
    private float totalPrice;
    private String paymentMode;
    private String city;
    private String barangay;
    private String street;
    private String orderDate;
    private String deliveryProgress;

    private EditText referenceET;
    private Button btnUpload;
    private ImageView gcashImage;
    private String status;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private Uri imageUri;

    String txtSeller;
    String selectedPetName;
    String selectedPetCategory;
    String selectedPetBreed;
    String selectedPetColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcash_payment);

        SharedPreferences sharedPreferences = getSharedPreferences("GcashManual", Context.MODE_PRIVATE);

        uniqueID = sharedPreferences.getString("uniqueID", "");
        selectedPetID = sharedPreferences.getString("selectedPetID", "");
        buyerID = sharedPreferences.getString("buyerID", "");
        buyerPhone = sharedPreferences.getString("buyerPhone", "");
        petPrice = sharedPreferences.getFloat("petPrice", 0.0f);
        totalPrice = sharedPreferences.getFloat("totalPrice", 0.0f);
        paymentMode = sharedPreferences.getString("paymentMode", "");
        city = sharedPreferences.getString("city", "");
        barangay = sharedPreferences.getString("barangay", "");
        street = sharedPreferences.getString("street", "");
        orderDate = sharedPreferences.getString("orderDate", "");
        deliveryProgress = sharedPreferences.getString("deliveryProgress", "");
        txtSeller = sharedPreferences.getString("seller", "");
        selectedPetName = sharedPreferences.getString("petName", "");
        selectedPetCategory = sharedPreferences.getString("petCategory", "");
        selectedPetBreed = sharedPreferences.getString("petBreed", "");
        selectedPetColor = sharedPreferences.getString("petColor", "");

        referenceET = findViewById(R.id.referenceET);
        btnUpload = findViewById(R.id.btnUpload);
        gcashImage = findViewById(R.id.gcashImage);
        status = "paid";

        gcashImage.setOnClickListener(view -> showImageOptions());

        btnUpload.setOnClickListener(view -> new UploadTask().execute());
    }

    private void showImageOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Gallery", "Camera"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Choose from gallery
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
                    break;
                case 1:
                    // Take a picture
                    openCamera();
                    break;
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Set the imageUri directly without saving to a file
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    private class UploadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                uploadGcashPaymentData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Handle UI updates or feedback after the upload is complete
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                // Get the image data from the camera intent
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                // Convert Bitmap to Uri
                imageUri = getImageUri(this, imageBitmap);

                // Set the image in the ImageView
                gcashImage.setImageBitmap(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null && data.getData() != null) {
                // Handle image picked from gallery
                imageUri = data.getData();
                gcashImage.setImageURI(imageUri);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Image selection/capture canceled", Toast.LENGTH_SHORT).show();
        }
    }

    // Utility method to convert Bitmap to Uri
    private Uri getImageUri(Context context, Bitmap imageBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), imageBitmap, "Title", null);
        return Uri.parse(path);
    }


    private void uploadGcashPaymentData() throws IOException {
        String lineEnd = "\r\n";
        String uploadUrl = "https://pawsomematch.online/android/upload_gcash_payment.php";

        URL url = new URL(uploadUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);

        // Creating a boundary to specify the form data
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        // Open a new DataOutputStream
        DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());

        // Writing the form data
        // Writing the form data
        outputStream.writeBytes("--" + boundary + lineEnd);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"reference\"" + lineEnd);
        outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(URLEncoder.encode(uniqueID, "UTF-8") + lineEnd);

        outputStream.writeBytes("--" + boundary + lineEnd);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"pet_ID\"" + lineEnd);  // Update this line
        outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(URLEncoder.encode(selectedPetID, "UTF-8") + lineEnd);

        outputStream.writeBytes("--" + boundary + lineEnd);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"buyer_ID\"" + lineEnd);  // Update this line
        outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(URLEncoder.encode(buyerID, "UTF-8") + lineEnd);

        outputStream.writeBytes("--" + boundary + lineEnd);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"status\"" + lineEnd);
        outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(URLEncoder.encode(status, "UTF-8") + lineEnd);

        outputStream.writeBytes("--" + boundary + lineEnd);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"gcashReference\"" + lineEnd);
        outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(URLEncoder.encode(referenceET.getText().toString(), "UTF-8") + lineEnd);


        // If imageUri is not null, upload the image
        if (imageUri != null) {
            // Writing the file data
            outputStream.writeBytes("--" + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"gcashImage\";filename=\"" + "gcash_image.jpg" + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
            outputStream.writeBytes(lineEnd);

            // Open an InputStream from the image URI
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            // Read the image data and write it to the output stream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the input stream
            inputStream.close();

            // Write the line end after the image data
            outputStream.writeBytes(lineEnd);
        }

        // Writing the end boundary
        outputStream.writeBytes("--" + boundary + "--" + lineEnd);

        // Flush and close the output stream
        outputStream.flush();
        outputStream.close();

        // Get the server response code
        int responseCode = httpURLConnection.getResponseCode();

        // Read the server response
        InputStream responseStream = httpURLConnection.getInputStream();
        java.util.Scanner s = new java.util.Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";

        // Log the server response using Android Log
        Log.d("GcashPaymentActivity", "Server Response Code: " + responseCode);
        Log.d("GcashPaymentActivity", "Server Response: " + response);

        // Handle the response on the UI thread
        runOnUiThread(() -> {
            if (responseCode == HttpURLConnection.HTTP_OK && response.equals("Data uploaded successfully")) {
                Toast.makeText(GcashPaymentActivity.this, response, Toast.LENGTH_SHORT).show();
                RequestBody requestBody = new FormBody.Builder()
                        .add("reference", uniqueID)
                        .add("pet_ID", selectedPetID)
                        .add("buyer_ID", buyerID)
                        .add("buyer_phone", buyerPhone)
                        .add("pet_price", String.valueOf(petPrice))
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
                        // Handle failure (e.g., show an error message)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GcashPaymentActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
                            }
                        });
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
                                        Toast.makeText(GcashPaymentActivity.this, "Order created successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(GcashPaymentActivity.this, OrderSummaryActivity.class);

                                        // After creating the request body
                                        SharedPreferences sharedPreferences = getSharedPreferences("OrderData", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("reference", uniqueID);
                                        editor.putString("pet_ID", selectedPetID);

                                        editor.putString("seller", txtSeller);
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

                                        startActivity(intent);
                                    } else {
                                        // Insertion failed
                                        Toast.makeText(GcashPaymentActivity.this, "Failed to create the order: " + responseBody, Toast.LENGTH_SHORT).show();
                                        Log.e("OrderCreationError", responseBody); // Log the error
                                    }
                                }
                            });
                        } else {
                            // Handle non-successful response (e.g., show an error message)
                            final String errorBody = response.body().string(); // Log the error body
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("ServerError", "HTTP Code: " + response.code() + ", Response: " + errorBody);
                                    Toast.makeText(GcashPaymentActivity.this, "Server returned an error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            } else {
                Toast.makeText(GcashPaymentActivity.this, "Error uploading data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
