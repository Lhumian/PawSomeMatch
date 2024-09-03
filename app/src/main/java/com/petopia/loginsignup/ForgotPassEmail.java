package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class ForgotPassEmail extends AppCompatActivity {

    EditText emailEt;
    Button verifyBtn;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_email);

        emailEt = findViewById(R.id.emailEt);

        verifyBtn = findViewById(R.id.verifyBtn);

        backBtn = findViewById(R.id.backBtn);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailExistence();
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassEmail.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
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
                    writer.write("email=" + emailEt.getText().toString());
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
                        Intent intent = new Intent(ForgotPassEmail.this, ForgotPassActivity.class);
                        intent.putExtra("email", emailEt.getText().toString());
                        startActivity(intent);
                        finish();
                    } else if (responseMessage.equals("Email does not exist")) {
                        Toast.makeText(ForgotPassEmail.this, "This email does not exist!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgotPassEmail.this, "Error checking email availability.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ForgotPassEmail.this, "Error checking email availability.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        CheckEmailTask task = new CheckEmailTask();
        task.execute();
    }
}