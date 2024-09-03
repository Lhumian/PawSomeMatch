package com.petopia.loginsignup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPassActivity extends AppCompatActivity {
    private String email;

    private Button verifyBtn;
    private ImageView backBtn;
    private EditText verificationCodeEt1, verificationCodeEt2, verificationCodeEt3, verificationCodeEt4, verificationCodeEt5, verificationCodeEt6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        email = getIntent().getStringExtra("email");

        sendPasswordResetCode(email, this);


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

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassActivity.this, ForgotPassEmail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        verifyBtn = findViewById(R.id.verifyBtn);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the verification code entered by the user
                String enteredVerificationCode = verificationCodeEt1.getText().toString().trim() + verificationCodeEt2.getText().toString().trim() + verificationCodeEt3.getText().toString().trim() + verificationCodeEt4.getText().toString().trim() + verificationCodeEt5.getText().toString().trim() + verificationCodeEt6.getText().toString().trim();

                verifyResetPasswordCode(email, enteredVerificationCode);
            }
        });
    }

    private void sendPasswordResetCode(String email, Context context) {
        // Create an HTTP request to send the reset code to the PHP script
        // You can use AsyncTask or other methods to perform the network operation

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Sending Verification Code...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);

        // Create a request body with the recipient's email and name
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .build();

        // Create an HTTP request
        Request request = new Request.Builder()
                .url("https://pawsomematch.online/android/password_verification.php") // Replace with your PHP script URL
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
                        Toast.makeText(ForgotPassActivity.this, "Failed to send verification code", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ForgotPassActivity.this, "Verification code sent successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Handle the case where the request was not successful
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(ForgotPassActivity.this, "Failed to send verification code", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void verifyResetPasswordCode(String email, String enteredVerificationCode) {

        ProgressDialog progressDialog = new ProgressDialog(this);
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
                .url("https://pawsomematch.online/android/validate_resetpass_verification_code.php") // Replace with your PHP script URL
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
                        Toast.makeText(ForgotPassActivity.this, "Failed to verify the code", Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(ForgotPassActivity.this, ChangePasswordActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else if ("Invalid verification code".equals(responseText)) {
                        // Verification code is incorrect
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ForgotPassActivity.this, "Invalid verification code", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ForgotPassActivity.this, "Failed to verify the code", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
