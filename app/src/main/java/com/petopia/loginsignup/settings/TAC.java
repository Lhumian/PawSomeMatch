package com.petopia.loginsignup.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.petopia.loginsignup.R;
import com.petopia.loginsignup.Settings;

public class TAC extends AppCompatActivity {

    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tac);

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TAC.this, Settings.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });


        final TextView txt1 = findViewById(R.id.txt1);
        final TextView txt11 = findViewById(R.id.txt11);
        final ImageView plus = findViewById(R.id.plus);
        final ImageView minus = findViewById(R.id.minus);

        // Initially set the additionalInfo TextView as GONE
        txt11.setVisibility(View.GONE);

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When txt1 is clicked, toggle the visibility of additionalInfo
                if (txt11.getVisibility() == View.VISIBLE) {
                    txt11.setVisibility(View.GONE);
                    minus.setVisibility(View.GONE);
                    plus.setVisibility(View.VISIBLE);
                } else {
                    txt11.setVisibility(View.VISIBLE);
                    txt11.setText("By using PawsomeMatch, you agree to abide by these terms and conditions. If you do not agree with any part of these terms, please refrain from using the app.");
                    minus.setVisibility(View.VISIBLE);
                    plus.setVisibility(View.GONE);
                }
            }
        });

        final TextView txt2 = findViewById(R.id.txt2);
        final TextView txt22 = findViewById(R.id.txt22);
        final ImageView plus1 = findViewById(R.id.plus1);
        final ImageView minus1 = findViewById(R.id.minus1);

        txt22.setVisibility(View.GONE);

        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When txt1 is clicked, toggle the visibility of additionalInfo
                if (txt22.getVisibility() == View.VISIBLE) {
                    txt22.setVisibility(View.GONE);
                    minus1.setVisibility(View.GONE);
                    plus1.setVisibility(View.VISIBLE);
                } else {
                    txt22.setVisibility(View.VISIBLE);
                    txt22.setText("You must be at least 18 years of age to use PawsomeMatch. By using the app, you confirm that you meet this age requirement.");
                    minus1.setVisibility(View.VISIBLE);
                    plus1.setVisibility(View.GONE);
                }
            }
        });

        final TextView txt3 = findViewById(R.id.txt3);
        final TextView txt33 = findViewById(R.id.txt33);
        final ImageView plus2 = findViewById(R.id.plus2);
        final ImageView minus2 = findViewById(R.id.minus2);

        txt33.setVisibility(View.GONE);

        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When txt1 is clicked, toggle the visibility of additionalInfo
                if (txt33.getVisibility() == View.VISIBLE) {
                    txt33.setVisibility(View.GONE);
                    minus2.setVisibility(View.GONE);
                    plus2.setVisibility(View.VISIBLE);
                } else {
                    txt33.setVisibility(View.VISIBLE);
                    txt33.setText("Users are responsible for their actions and content shared on the app.\n \n" +
                                  "Inappropriate, offensive, or harmful behavior will not be tolerated.");
                    minus2.setVisibility(View.VISIBLE);
                    plus2.setVisibility(View.GONE);
                }
            }
        });

        final TextView txt4 = findViewById(R.id.txt4);
        final TextView txt44 = findViewById(R.id.txt44);
        final ImageView plus3 = findViewById(R.id.plus3);
        final ImageView minus3 = findViewById(R.id.minus3);

        txt44.setVisibility(View.GONE);

        txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When txt1 is clicked, toggle the visibility of additionalInfo
                if (txt44.getVisibility() == View.VISIBLE) {
                    txt44.setVisibility(View.GONE);
                    minus3.setVisibility(View.GONE);
                    plus3.setVisibility(View.VISIBLE);
                } else {
                    txt44.setVisibility(View.VISIBLE);
                    txt44.setText("PawsomeMatch respects your privacy and handles data as outlined in our Privacy Policy.");
                    minus3.setVisibility(View.VISIBLE);
                    plus3.setVisibility(View.GONE);
                }
            }
        });

        final TextView txt5 = findViewById(R.id.txt5);
        final TextView txt55 = findViewById(R.id.txt55);
        final ImageView plus4 = findViewById(R.id.plus4);
        final ImageView minus4 = findViewById(R.id.minus4);

        txt55.setVisibility(View.GONE);

        txt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When txt1 is clicked, toggle the visibility of additionalInfo
                if (txt55.getVisibility() == View.VISIBLE) {
                    txt55.setVisibility(View.GONE);
                    minus4.setVisibility(View.GONE);
                    plus4.setVisibility(View.VISIBLE);
                } else {
                    txt55.setVisibility(View.VISIBLE);
                    txt55.setText("You are responsible for maintaining the confidentiality of your account information.\n \n" +
                                  "Notify us immediately if you suspect unauthorized access to your account.");
                    minus4.setVisibility(View.VISIBLE);
                    plus4.setVisibility(View.GONE);
                }
            }
        });

        final TextView txt6 = findViewById(R.id.txt6);
        final TextView txt66 = findViewById(R.id.txt66);
        final ImageView plus5 = findViewById(R.id.plus5);
        final ImageView minus5 = findViewById(R.id.minus5);

        txt66.setVisibility(View.GONE);

        txt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When txt1 is clicked, toggle the visibility of additionalInfo
                if (txt66.getVisibility() == View.VISIBLE) {
                    txt66.setVisibility(View.GONE);
                    minus5.setVisibility(View.GONE);
                    plus5.setVisibility(View.VISIBLE);
                } else {
                    txt66.setVisibility(View.VISIBLE);
                    txt66.setText("All pet listings, including those for sale, adoption, or breeding, must comply with local laws and ethical standards.\n \n" +
                                  "Misrepresentation or false information in listings is prohibited.");
                    minus5.setVisibility(View.VISIBLE);
                    plus5.setVisibility(View.GONE);
                }
            }
        });

        final TextView txt7 = findViewById(R.id.txt7);
        final TextView txt77 = findViewById(R.id.txt77);
        final ImageView plus6 = findViewById(R.id.plus6);
        final ImageView minus6 = findViewById(R.id.minus6);

        txt77.setVisibility(View.GONE);

        txt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When txt1 is clicked, toggle the visibility of additionalInfo
                if (txt77.getVisibility() == View.VISIBLE) {
                    txt77.setVisibility(View.GONE);
                    minus6.setVisibility(View.GONE);
                    plus6.setVisibility(View.VISIBLE);
                } else {
                    txt77.setVisibility(View.VISIBLE);
                    txt77.setText("Content on PawsomeMatch, including text, images, and trademarks, is protected by intellectual property laws.\n \n" +
                                  "Users may not use PawsomeMatch's content without permission.");
                    minus6.setVisibility(View.VISIBLE);
                    plus6.setVisibility(View.GONE);
                }
            }
        });

        final TextView txt8 = findViewById(R.id.txt8);
        final TextView txt88 = findViewById(R.id.txt88);
        final ImageView plus7 = findViewById(R.id.plus7);
        final ImageView minus7 = findViewById(R.id.minus7);

        txt88.setVisibility(View.GONE);

        txt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When txt1 is clicked, toggle the visibility of additionalInfo
                if (txt88.getVisibility() == View.VISIBLE) {
                    txt88.setVisibility(View.GONE);
                    minus7.setVisibility(View.GONE);
                    plus7.setVisibility(View.VISIBLE);
                } else {
                    txt88.setVisibility(View.VISIBLE);
                    txt88.setText("PawsomeMatch is a platform for connecting pet enthusiasts and is not responsible for transactions, breeding outcomes, or the conduct of its users.");
                    minus7.setVisibility(View.VISIBLE);
                    plus7.setVisibility(View.GONE);
                }
            }
        });
    }
}
