package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class StatusActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String pet_ID;
    String liked_pet_ID;

    Spinner statusSpinner;
    Spinner healthConditionSpinner;

    Spinner PregnancyProgressEdit;
    EditText stateConditionEditText;
    Button updateStatusButton;

    ImageView backBtn;

    Button birthStatusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        backBtn = findViewById(R.id.backBtn);
        statusSpinner = findViewById(R.id.statusSpinner);
        healthConditionSpinner = findViewById(R.id.healthConditionSpinner);
        stateConditionEditText = findViewById(R.id.stateConditionEditText);
        updateStatusButton = findViewById(R.id.updateStatusButton);
        birthStatusButton = findViewById(R.id.birthStatusButton);
        PregnancyProgressEdit = findViewById(R.id.PregnancyProgressEdit);

        birthStatusButton.setVisibility(View.INVISIBLE);

        // Set the OnItemSelectedListener on statusSpinner
        statusSpinner.setOnItemSelectedListener(this);
        healthConditionSpinner.setOnItemSelectedListener(this);
        PregnancyProgressEdit.setOnItemSelectedListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("Chat", Context.MODE_PRIVATE);
        pet_ID = sharedPreferences.getString("acceptingPetID", "");
        liked_pet_ID = sharedPreferences.getString("acceptedPetID", "");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatusActivity.this, BreedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        PregnancyProgressEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProgress = parent.getItemAtPosition(position).toString();

                // Check if "Still in Progress" is selected
                if (selectedProgress.equals("Still In Progress")) {
                    // Disable the birthStatusButton
                    birthStatusButton.setEnabled(false);
                } else {
                    // Enable the birthStatusButton
                    birthStatusButton.setEnabled(true);
                }
                if (selectedProgress.equals("Done Giving Birth")) {
                    // Disable the birthStatusButton
                    statusSpinner.setSelection(1);
                } else {
                    statusSpinner.setSelection(0);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle nothing selected if needed
            }
        });

        updateStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String selectedStatus = statusSpinner.getSelectedItem().toString();
                String selectedHealthCondition = healthConditionSpinner.getSelectedItem().toString();
                String stateCondition = stateConditionEditText.getText().toString();
                String pregnancyProgress = PregnancyProgressEdit.getSelectedItem().toString();

                String url = "https://pawsomematch.online/android/update_pet_status.php";
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle the response from your PHP script here if needed.
                                // For example, you can display a message based on the response.
                                Toast.makeText(StatusActivity.this, response, Toast.LENGTH_SHORT).show();

                                String selectedProgress = PregnancyProgressEdit.getSelectedItem().toString();

                                // Check if "Done Giving Birth" is selected
                                if (selectedProgress.equals("Done Giving Birth")) {
                                    // Enable the birthStatusButton
                                    birthStatusButton.setVisibility(View.VISIBLE);
                                } else {
                                    // Disable the birthStatusButton
                                    birthStatusButton.setVisibility(View.INVISIBLE);
                                }

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
                        params.put("pet_ID", pet_ID);
                        params.put("liked_pet_ID", liked_pet_ID);
                        params.put("status", selectedStatus);
                        params.put("health_condition", selectedHealthCondition);
                        params.put("state_condition", stateCondition);
                        params.put("pregnancy_process", pregnancyProgress);
                        return params;
                    }
                };

                Volley.newRequestQueue(StatusActivity.this).add(request);
            }
        });

        birthStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatusActivity.this, BirthActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // This method will be called when an item is selected in the statusSpinner
        // You can add your logic here to handle the selected item.
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // This method will be called when nothing is selected in the statusSpinner
        // You can add your logic here if needed.
    }
}
