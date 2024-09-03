package com.petopia.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends MainActivity {

    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        init();
        setContentView(R.layout.activity_splash_screen);

        lottie = findViewById(R.id.lottie);

        // Set up a fade-in animation
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(3000);
        lottie.startAnimation(fadeIn);

        // Set the Lottie animation to loop
        lottie.setRepeatCount(Animation.INFINITE);

        // Check if the user is logged in after a delay (replace this with your own login check logic)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoggedIn()) {
                    //Redirect to home page
                    intent = new Intent(context, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //Redirect to Login Page
                    intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 5000);
    }

}
