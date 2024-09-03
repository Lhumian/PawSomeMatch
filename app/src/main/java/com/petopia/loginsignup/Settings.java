package com.petopia.loginsignup;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import com.petopia.loginsignup.settings.About;
import com.petopia.loginsignup.settings.ChPassword;
import com.petopia.loginsignup.settings.FAQ;
import com.petopia.loginsignup.settings.PersonalInfo;
import com.petopia.loginsignup.settings.PlatformRules;
import com.petopia.loginsignup.settings.TAC;
import com.squareup.picasso.Picasso;

import androidx.appcompat.widget.AppCompatButton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Settings extends MainActivity {

    TextView name, number;
    RelativeLayout logoutBtn, pafBtn, changeBtn, faqBtn, aboutBtn, tacBtn, pfBtn,btnOrder, btnSale, btnListMatches, btnCompletedTransactions, btnReviewList;
    AppCompatButton editBtn;
    ImageView backBtn;

    ImageView imageProfile;

    String UserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        init();

        setContentView(R.layout.activity_settings);

        logoutBtn = findViewById(R.id.logoutBtn);
        editBtn = findViewById(R.id.editProf);
        backBtn = findViewById(R.id.backBtn);
        pafBtn = findViewById(R.id.PAF);
        faqBtn = findViewById(R.id.FAQ);
        aboutBtn = findViewById(R.id.aboutUs);
        tacBtn = findViewById(R.id.TAC);
        pfBtn = findViewById(R.id.PF);

        btnListMatches = findViewById(R.id.btnListMatches);
        btnCompletedTransactions = findViewById(R.id.btnCompletedTransactions);
        btnReviewList = findViewById(R.id.btnReviewList);

        getViews();

        btnListMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MatchesList.class);
                startActivity(intent);
            }
        });

        btnCompletedTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, CompletedTransactionActivity.class);
                startActivity(intent);
            }
        });

        btnReviewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, ReviewListActivity.class);
                startActivity(intent);
            }
        });

        btnOrder = findViewById(R.id.btnOrder);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, OrderActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnSale = findViewById(R.id.btnSale);
        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, SaleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        pfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, PlatformRules.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, About.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        tacBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, TAC.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        faqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, FAQ.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


        pafBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, PersonalInfo.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, EditUserProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_logout, null);
                builder.setView(dialogView);

                Button logoutBtn = dialogView.findViewById(R.id.logoutBtn);
                Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);

                final AlertDialog alertDialog = builder.create();

                logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sharedPreferences1 = getSharedPreferences("Address", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                        editor.clear(); // Clear all preferences
                        editor.apply();

                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.putBoolean("login", false);
                        editor1.apply();
                        Intent intent = new Intent(Settings.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });


                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        imageProfile = findViewById(R.id.imageProfile);
        UserID = sharedPreferences.getString("user_id", "");
        new FetchImageUrlTask().execute(UserID);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void getViews() {
        name = findViewById(R.id.nameText);
        String firstName = sharedPreferences.getString("first_name", "");
        name.setText(firstName);
    }

    private class FetchImageUrlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String userId = params[0];
            String imageUrl = "";

            try {
                String imageUrlFetchUrl = "https://pawsomematch.online/android/get_profile_image.php?UserID=" + userId;
                URL url = new URL(imageUrlFetchUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                Log.d(TAG, "Response code: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                    inputStream.close();

                    imageUrl = response.toString();
                } else {
                    Log.e(TAG, "Error fetching image URL. Response code: " + responseCode);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching image URL: " + e.getMessage());
            }

            return imageUrl;
        }

        @Override
        protected void onPostExecute(String imageUrl) {
            if (!imageUrl.isEmpty()) {
                String imageUrl1 = "https://pawsomematch.online/android/" + imageUrl;
                Picasso.get().load(imageUrl1).into(imageProfile);
                Log.d(TAG, "Image URL: " + imageUrl1);
            } else {
                Log.d(TAG, "Error Retrieving Photo");
            }
        }
    }
}
