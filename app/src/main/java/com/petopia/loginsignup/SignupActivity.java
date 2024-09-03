package com.petopia.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class SignupActivity extends MainActivity {
    private EditText firstnameEt, middlenameEt, lastnameEt, emailEt, passwordEt;
    private Button signupBtn;
    private TextView loginNowTv;
    private Spinner gender;
    private SharedPreferences sharedPref;

    String firstName;
    String middleName;
    String lastName;
    String email;
    String genderValue;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        init();
        setContentView(R.layout.activity_signup);
        getViews();
        sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
    }

    private void getViews() {
        firstnameEt = findViewById(R.id.firstnameEt);
        middlenameEt = findViewById(R.id.middlenameEt);
        lastnameEt = findViewById(R.id.lastnameEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        signupBtn = findViewById(R.id.SignupBtn);
        loginNowTv = findViewById(R.id.LoginNowTv);
        gender = findViewById(R.id.gender);

        firstName = firstnameEt.getText().toString().trim();
        middleName = middlenameEt.getText().toString().trim();
        lastName = lastnameEt.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        genderValue = gender.getSelectedItem().toString().trim();
        password = passwordEt.getText().toString().trim();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupValidation();
            }
        });

        loginNowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean isStrongPassword(String password) {
        // Check if the password contains at least one uppercase letter, one lowercase letter, one digit, and one special character
        return Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$").matcher(password).matches();
    }
    private void signupValidation() {
        firstName = firstnameEt.getText().toString().trim();
        middleName = middlenameEt.getText().toString().trim();
        lastName = lastnameEt.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        genderValue = gender.getSelectedItem().toString().trim();
        password = passwordEt.getText().toString().trim();

        if (firstName.length() < 1 || lastName.length() < 1) {
            Toast.makeText(context, "Please Enter Your First and Last Name.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isEmailValid(email)) {
            Toast.makeText(context, "Invalid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8 || !isStrongPassword(password)) {
            Toast.makeText(context, getString(R.string.short_password_error), Toast.LENGTH_LONG).show();
            return;
        }

        // Validate all fields before checking email existence
        checkEmailExistence();
    }

    private void checkEmailExistence() {
        class CheckEmailTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Create a URL connection and send a POST request to your PHP file
                    URL url = new URL("https://pawsomematch.online/android/email_checker.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    // Send the email as POST data
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write("email=" + email);
                    writer.flush();
                    writer.close();
                    os.close();

                    // Get the response from the server
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                        }
                        br.close();

                        return response.toString();
                    } else {
                        return "Error";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error";
                }
            }

            @Override
            protected void onPostExecute(String result) {
                // Log the full response
                Log.d("EmailExistenceResponse", result);

                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    String responseMessage = jsonResponse.getString("result");

                    if (responseMessage.equals("Email already exists")) {
                        Toast.makeText(context, "This email is already registered.", Toast.LENGTH_SHORT).show();
                    } else if (responseMessage.equals("Email does not exist")) {
                        Intent intent = new Intent(SignupActivity.this, EmailVerificationActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("middleName", middleName);
                        intent.putExtra("lastName", lastName);
                        intent.putExtra("genderValue", genderValue);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(context, "Error checking email availability.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error checking email availability.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        CheckEmailTask task = new CheckEmailTask();
        task.execute();
    }
}
