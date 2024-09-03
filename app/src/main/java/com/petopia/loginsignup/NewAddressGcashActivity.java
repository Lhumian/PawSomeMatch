package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NewAddressGcashActivity extends AppCompatActivity {


    Button btnSubmit;
    EditText etName;
    EditText etPhone;
    Spinner spinnerCity;
    Spinner spinnerBarangay;
    EditText etStreet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etStreet = findViewById(R.id.etStreet);
        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerBarangay = findViewById(R.id.spinnerBarangay);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = HomeActivity.userIdTv.getText().toString().trim();
                String etName1 = etName.getText().toString();
                String etPhone1 = etPhone.getText().toString();
                String etStreet1 = etStreet.getText().toString();
                String spinnerCity1 = spinnerCity.getSelectedItem().toString();
                String spinnerBarangay1 = spinnerBarangay.getSelectedItem().toString();

                String url = "https://pawsomematch.online/android/add_address.php";
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(NewAddressGcashActivity.this, response, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(NewAddressGcashActivity.this, AddressGcashActivity.class);
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
                        params.put("user_ID", userId);
                        params.put("name", etName1);
                        params.put("phone", etPhone1);
                        params.put("street", etStreet1);
                        params.put("city", spinnerCity1);
                        params.put("barangay", spinnerBarangay1);
                        return params;
                    }
                };

                Volley.newRequestQueue(NewAddressGcashActivity.this).add(request);


            }
        });
    }
}