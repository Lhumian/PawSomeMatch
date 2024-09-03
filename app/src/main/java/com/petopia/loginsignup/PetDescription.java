package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PetDescription extends AppCompatActivity {

    Button btnSaveDescription;
    EditText etDescription;
    ImageView backBtn;
    private int position;
    private String petId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_description);

        Intent intent = getIntent();
        petId = intent.getStringExtra("petId");

        btnSaveDescription = findViewById(R.id.btnSaveDescription);
        etDescription = findViewById(R.id.etDescription);

        etDescription.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        fetchDescriptionFromDatabase();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetDescription.this, PetDetailsActivity.class);
                intent.putExtra("petId", petId);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnSaveDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDescriptionToDatabase();
            }
        });
    }

    private void fetchDescriptionFromDatabase() {
        // Show progress dialog while fetching data
        progressDialog = ProgressDialog.show(this, "Fetching Description", "Please wait...", true);

        OkHttpClient client = new OkHttpClient();
        String phpUrl = "https://pawsomematch.online/android/retrieve_description.php";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pet_id", petId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Request request = new Request.Builder()
                .url(phpUrl)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Dismiss the progress dialog on failure
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
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
                            etDescription.setText(responseBody);
                            // Dismiss the progress dialog after successfully fetching data
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void saveDescriptionToDatabase() {
        progressDialog = ProgressDialog.show(this, "Saving Description", "Please wait...", true);

        String description = etDescription.getText().toString();

        OkHttpClient client = new OkHttpClient();
        JSONObject params = new JSONObject();
        try {
            params.put("pet_id", petId);
            params.put("pet_description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), params.toString());

        String phpUrl = "https://pawsomematch.online/android/add_description.php";

        Request request = new Request.Builder()
                .url(phpUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(PetDescription.this, "Failed to save description", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(PetDescription.this, "Successfully added description!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }
}