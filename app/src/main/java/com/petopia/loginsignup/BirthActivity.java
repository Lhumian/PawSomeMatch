package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class BirthActivity extends AppCompatActivity {

    String pet_ID;
    String liked_pet_ID;
    EditText totalBirth;
    EditText totalFemale;
    EditText totalMale;
    Button btnAdd;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth);

        backBtn = findViewById(R.id.backBtn);

        SharedPreferences sharedPreferences = getSharedPreferences("Chat", Context.MODE_PRIVATE);
        pet_ID = sharedPreferences.getString("acceptingPetID", "");
        liked_pet_ID = sharedPreferences.getString("acceptedPetID", "");

        totalBirth = findViewById(R.id.totalBirth);
        totalFemale = findViewById(R.id.totalFemale);
        totalMale = findViewById(R.id.totalMale);
        btnAdd = findViewById(R.id.btnAdd);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BirthActivity.this, BreedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalBirth1 = totalBirth.getText().toString();
                String totalFemale1 = totalFemale.getText().toString();
                String totalMale1 = totalMale.getText().toString();

                String url = "https://pawsomematch.online/android/add_birth.php";
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle the response from your PHP script here if needed.
                                // For example, you can display a message based on the response.
                                Toast.makeText(BirthActivity.this, response, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BirthActivity.this, BreedActivity.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("totalBirth", totalBirth1);
                        params.put("totalFemale", totalFemale1);
                        params.put("totalMale", totalMale1);
                        params.put("mother", pet_ID);
                        params.put("father", liked_pet_ID);
                        return params;
                    }
                };

                Volley.newRequestQueue(BirthActivity.this).add(request);
            }
        });
    }
}