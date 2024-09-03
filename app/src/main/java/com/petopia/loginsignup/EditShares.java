package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class EditShares extends AppCompatActivity {

    String pet_ID;
    String shares;

    EditText etAmount;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shares);

        // Retrieve values from the intent
        Intent intent = getIntent();
        pet_ID = intent.getStringExtra("pet_ID");
        shares = intent.getStringExtra("shares");

        etAmount = findViewById(R.id.etAmount);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newShares = etAmount.getText().toString();
                performUpdateSharesRequest(pet_ID, newShares);
            }
        });

    }

    private void performUpdateSharesRequest(String pet_ID, String newShares) {
        // Create a new thread to perform the network request (Note: In a production environment, consider using AsyncTask or a background thread)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Define the PHP script URL
                    URL url = new URL("https://pawsomematch.online/android/update_shares.php"); // Replace with your actual PHP script URL

                    // Open a connection to the URL
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set the request method to POST
                    connection.setRequestMethod("POST");

                    // Enable input and output streams
                    connection.setDoOutput(true);

                    // Create the POST data
                    String postData = "pet_ID=" + URLEncoder.encode(pet_ID, "UTF-8") + "&newShares=" + URLEncoder.encode(newShares, "UTF-8");

                    // Get the output stream and write the POST data to it
                    try (OutputStream outputStream = connection.getOutputStream()) {
                        byte[] input = postData.getBytes(StandardCharsets.UTF_8);
                        outputStream.write(input, 0, input.length);
                    }

                    // Get the response code
                    int responseCode = connection.getResponseCode();

                    // Read the response
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Successfully updated shares
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI or show a message indicating successful update
                                // You might want to navigate back to the previous activity or update the UI as needed
                                Toast.makeText(EditShares.this, "Shares updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } else {
                        // Handle unsuccessful response (e.g., show an error message)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EditShares.this, "Error updating shares", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    // Disconnect the connection
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle exception (e.g., show an error message)
                }
            }
        }).start();
    }

}