package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EmailVerificationActivity extends MainActivity {
    private EditText verificationCodeEt1, verificationCodeEt2, verificationCodeEt3, verificationCodeEt4, verificationCodeEt5, verificationCodeEt6;
    private Button verifyBtn;

    ImageView backBtn;
    String email;
    String firstName;
    String middleName;
    String lastName;
    String genderValue;
    String password;

    ProgressDialog progressDialog;


    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        backBtn = findViewById(R.id.backBtn);
        email = getIntent().getStringExtra("email");
        firstName = getIntent().getStringExtra("firstName");
        middleName = getIntent().getStringExtra("middleName");
        lastName = getIntent().getStringExtra("lastName");
        genderValue = getIntent().getStringExtra("genderValue");
        password = getIntent().getStringExtra("password");

        String recipientName = firstName + " " + middleName + " " + lastName;
        sendVerificationCode(email, recipientName);

        verificationCodeEt1 = findViewById(R.id.verificationCodeEt1);
        verificationCodeEt2 = findViewById(R.id.verificationCodeEt2);
        verificationCodeEt3 = findViewById(R.id.verificationCodeEt3);
        verificationCodeEt4 = findViewById(R.id.verificationCodeEt4);
        verificationCodeEt5 = findViewById(R.id.verificationCodeEt5);
        verificationCodeEt6 = findViewById(R.id.verificationCodeEt6);

        verificationCodeEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed, but required to implement the TextWatcher interface
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed, but required to implement the TextWatcher interface
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    // Move the focus to the next EditText if a digit is entered
                    verificationCodeEt2.requestFocus();
                }
            }
        });

        verificationCodeEt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed, but required to implement the TextWatcher interface
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed, but required to implement the TextWatcher interface
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    // Move the focus to the next EditText if a digit is entered
                    verificationCodeEt3.requestFocus();
                }
            }
        });

        verificationCodeEt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed, but required to implement the TextWatcher interface
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed, but required to implement the TextWatcher interface
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    // Move the focus to the next EditText if a digit is entered
                    verificationCodeEt4.requestFocus();
                }
            }
        });

        verificationCodeEt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed, but required to implement the TextWatcher interface
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed, but required to implement the TextWatcher interface
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    // Move the focus to the next EditText if a digit is entered
                    verificationCodeEt5.requestFocus();
                }
            }
        });

        verificationCodeEt5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed, but required to implement the TextWatcher interface
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed, but required to implement the TextWatcher interface
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    // Move the focus to the next EditText if a digit is entered
                    verificationCodeEt6.requestFocus();
                }
            }
        });



        verifyBtn = findViewById(R.id.verifyBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the verification code entered by the user
                String enteredVerificationCode = verificationCodeEt1.getText().toString().trim() + verificationCodeEt2.getText().toString().trim() + verificationCodeEt3.getText().toString().trim() + verificationCodeEt4.getText().toString().trim() + verificationCodeEt5.getText().toString().trim() + verificationCodeEt6.getText().toString().trim();

                sendVerificationCodeForValidation(enteredVerificationCode);
            }
        });
    }

    private void registerUser(String firstName, String middleName, String lastName, String gender, String email, String password) {
        OkHttpClient client = new OkHttpClient();

        // Define the URL of your PHP script
        String url = "https://pawsomematch.online/android/signup_user.php"; // Replace with your actual server and script URL

        // Create the request body with user data
        RequestBody requestBody = new FormBody.Builder()
                .add("first_name", firstName)
                .add("middle_name", middleName)
                .add("last_name", lastName)
                .add("gender", gender)
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the failure, e.g., network issues
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    // Log the response
                    Log.d("RegistrationResponse", responseData);

                    // Handle the response here, e.g., show a toast or update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Example: display a toast with the response
                            Toast.makeText(getApplicationContext(), responseData, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EmailVerificationActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }

    private void sendVerificationCode(String email, String recipientName) {
        // Create an HTTP request to send the verification code to the PHP script
        // You can use a library like Retrofit or OkHttp to make the HTTP request

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Verification Code...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);

        // Create a request body with the recipient's email and name
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("recipientName", recipientName)
                .build();

        // Create an HTTP request
        Request request = new Request.Builder()
                .url("https://pawsomematch.online/android/email_verification.php") // Replace with your PHP script URL
                .post(requestBody)
                .build();

        // Create an OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Adjust the timeout value as needed
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        progressDialog.show();
        // Asynchronously execute the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the failure, e.g., show an error message and log the error
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(EmailVerificationActivity.this, "Failed to send verification code", Toast.LENGTH_SHORT).show();
                        // Log the error
                        Log.e("VerificationCode", "Failed to send verification code: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle the response from the server (PHP script)
                if (response.isSuccessful()) {
                    // The verification code has been sent successfully
                    // You can display a success message or perform any other actions

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(EmailVerificationActivity.this, "Verification code sent successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Handle the case where the request was not successful
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(EmailVerificationActivity.this, "Failed to send verification code", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void sendVerificationCodeForValidation(String enteredVerificationCode) {
        // Create an HTTP request to send the entered code to the PHP script for validation

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Validating Verification Code...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        // Create a request body with the entered verification code
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("enteredCode", enteredVerificationCode)
                .build();

        // Create an HTTP request
        Request request = new Request.Builder()
                .url("https://pawsomematch.online/android/validate_verification_code.php") // Replace with your PHP script URL
                .post(requestBody)
                .build();

        progressDialog.show();

        // Create an OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Asynchronously execute the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the failure, e.g., show an error message
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(EmailVerificationActivity.this, "Failed to verify the code", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String responseText = response.body().string();
                    if ("Verification successful".equals(responseText)) {
                        // Verification code is correct, proceed to registration
                        registerUser(firstName, middleName, lastName, genderValue, email, password);
                    } else if ("Invalid verification code".equals(responseText)) {
                        // Verification code is incorrect
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EmailVerificationActivity.this, "Invalid verification code", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if ("User not found".equals(responseText)) {
                        // Handle the case where the user's email is not found
                        // You might want to show a message or take appropriate action here
                    }
                } else {
                    // Handle the case where the HTTP request itself was not successful
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EmailVerificationActivity.this, "Failed to verify the code", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
