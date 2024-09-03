package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CancelActivity extends AppCompatActivity {

    String pet_ID;
    String user_ID;
    RadioGroup radioGroup;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);

        SharedPreferences sharedPreferences = getSharedPreferences("Cancel", Context.MODE_PRIVATE);
        pet_ID = sharedPreferences.getString("pet_ID", "");
        user_ID = sharedPreferences.getString("user_ID", "");

        radioGroup = findViewById(R.id.radioGroup);
        btnConfirm = findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                String reason = radioButton.getText().toString();

                // Call the method to upload the data
                uploadCancelData(pet_ID, user_ID, reason);
            }
        });
    }

    private void uploadCancelData(String petID, String userID, String reason) {
        // The URL to your PHP script on your server
        String url = "https://pawsomematch.online/android/cancel.php";

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("pet_ID", petID)
                .add("user_ID", userID)
                .add("reason", reason)
                .add("status", "cancelled")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("ErrorTag", "Error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // The request was successful
                    // Handle the response if needed
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CancelActivity.this, "Successfully Cancelled", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CancelActivity.this, OrderActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    // The request failed
                    // Handle the error if needed
                    Log.e("ErrorTag", "HTTP request failed with response code: " + response.code());
                }
                response.close();
            }

        });
    }
}
