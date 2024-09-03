package com.petopia.loginsignup;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends MainActivity {
    TextView nameTv;
    static TextView emailTv, userIdTv;
    Button logoutbtn, settingsBtn;

    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        init();
        setContentView(R.layout.activity_home);

        //link views
        getViews();

        final LinearLayout homeLayout = findViewById(R.id.homeLayout);
        final LinearLayout matchingLayout = findViewById(R.id.matchingLayout);
        final LinearLayout sellLayout = findViewById(R.id.sellLayout);
        final LinearLayout profileLayout = findViewById(R.id.profileLayout);
        final LinearLayout messageLayout = findViewById(R.id.messageLayout);

        final ImageView homeImage = findViewById(R.id.homeImage);
        final ImageView matchingImage = findViewById(R.id.matchingImage);
        final ImageView sellImage = findViewById(R.id.sellImage);
        final ImageView profileImage = findViewById(R.id.profileImage);
        final ImageView messageImage = findViewById(R.id.messageImage);

        final TextView homeTxt = findViewById(R.id.homeTxt);
        final TextView matchingTxt = findViewById(R.id.matchingTxt);
        final TextView sellTxt = findViewById(R.id.sellTxt);
        final TextView profileTxt = findViewById(R.id.profileTxt);
        final TextView messageTxt = findViewById(R.id.messageTxt);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, HomeActivityF.class, null)
                .commit();

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab !=1){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, HomeActivityF.class, null)
                            .commit();

                    matchingTxt.setVisibility(View.GONE);
                    sellTxt.setVisibility(View.GONE);
                    profileTxt.setVisibility(View.GONE);
                    messageTxt.setVisibility(View.GONE);

                    matchingImage.setImageResource(R.drawable.pets);
                    sellImage.setImageResource(R.drawable.baseline_sell_24);
                    profileImage.setImageResource(R.drawable.person);
                    messageImage.setImageResource(R.drawable.message);

                    matchingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    sellLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    messageLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    homeTxt.setVisibility(View.VISIBLE);
                    homeImage.setImageResource(R.drawable.home_selected);
                    homeLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f , 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    homeLayout.startAnimation(scaleAnimation);

                    selectedTab = 1;
                }
            }
        });

        matchingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab !=2){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, MatchActivityF.class, null)
                            .commit();

                    homeTxt.setVisibility(View.GONE);
                    sellTxt.setVisibility(View.GONE);
                    profileTxt.setVisibility(View.GONE);
                    messageTxt.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.home);
                    sellImage.setImageResource(R.drawable.baseline_sell_24);
                    profileImage.setImageResource(R.drawable.person);
                    messageImage.setImageResource(R.drawable.message);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    sellLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    messageLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    matchingTxt.setVisibility(View.VISIBLE);
                    matchingImage.setImageResource(R.drawable.pets_selected);
                    matchingLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    selectedTab = 2;

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f , 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    matchingLayout.startAnimation(scaleAnimation);

                }
            }
        });

        sellLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab !=3){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, PetSellingF.class, null)
                            .commit();

                    homeTxt.setVisibility(View.GONE);
                    matchingTxt.setVisibility(View.GONE);
                    profileTxt.setVisibility(View.GONE);
                    messageTxt.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.home);
                    matchingImage.setImageResource(R.drawable.pets);
                    profileImage.setImageResource(R.drawable.person);
                    messageImage.setImageResource(R.drawable.message);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    matchingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    messageLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    sellTxt.setVisibility(View.VISIBLE);
                    sellImage.setImageResource(R.drawable.baseline_sell_24_selected);
                    sellLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f , 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    sellLayout.startAnimation(scaleAnimation);

                    selectedTab = 3;
                }
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab !=4){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ProfileF.class, null)
                            .commit();

                    homeTxt.setVisibility(View.GONE);
                    matchingTxt.setVisibility(View.GONE);
                    sellTxt.setVisibility(View.GONE);
                    messageTxt.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.home);
                    matchingImage.setImageResource(R.drawable.pets);
                    sellImage.setImageResource(R.drawable.baseline_sell_24);
                    messageImage.setImageResource(R.drawable.message);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    matchingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    sellLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    messageLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    profileTxt.setVisibility(View.VISIBLE);
                    profileImage.setImageResource(R.drawable.person_selected);
                    profileLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f , 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    profileLayout.startAnimation(scaleAnimation);

                    selectedTab = 4;
                }
            }
        });

        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab !=5){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, MessagingF.class, null)
                            .commit();

                    homeTxt.setVisibility(View.GONE);
                    matchingTxt.setVisibility(View.GONE);
                    sellTxt.setVisibility(View.GONE);
                    profileTxt.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.home);
                    matchingImage.setImageResource(R.drawable.pets);
                    sellImage.setImageResource(R.drawable.baseline_sell_24);
                    profileImage.setImageResource(R.drawable.person);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    matchingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    sellLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    messageTxt.setVisibility(View.VISIBLE);
                    messageImage.setImageResource(R.drawable.message_selected);
                    messageLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f , 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    messageLayout.startAnimation(scaleAnimation);

                    selectedTab = 5;
                }
            }
        });

        if (getIntent().getBooleanExtra("redirect_to_messaging", false)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new MessagingF())
                    .addToBackStack(null)
                    .commit();

            homeTxt.setVisibility(View.GONE);
            matchingTxt.setVisibility(View.GONE);
            sellTxt.setVisibility(View.GONE);
            profileTxt.setVisibility(View.GONE);

            homeImage.setImageResource(R.drawable.home);
            matchingImage.setImageResource(R.drawable.pets);
            sellImage.setImageResource(R.drawable.baseline_sell_24);
            profileImage.setImageResource(R.drawable.person);

            homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            matchingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            sellLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            messageTxt.setVisibility(View.VISIBLE);
            messageImage.setImageResource(R.drawable.message_selected);
            messageLayout.setBackgroundResource(R.drawable.round_back_home_100);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f , 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            messageLayout.startAnimation(scaleAnimation);

            selectedTab = 5;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.loginBtn:
                Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void getViews() {
        nameTv = findViewById(R.id.nameTv);
        nameTv.setText(sharedPreferences.getString("first_name",""));
        emailTv = findViewById(R.id.emailTv);
        emailTv.setText(sharedPreferences.getString("email",""));
        userIdTv = findViewById(R.id.userIdTv);
        userIdTv.setText(sharedPreferences.getString("user_id",""));
        logoutbtn = findViewById(R.id.logoutBtn);

    }
}