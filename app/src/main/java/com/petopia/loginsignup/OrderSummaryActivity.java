package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class OrderSummaryActivity extends AppCompatActivity {

    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        btnDone = findViewById(R.id.btnDone);

        // Retrieve data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("OrderData", MODE_PRIVATE);
        String seller = sharedPreferences.getString("seller", "");
        String petName = sharedPreferences.getString("petName", "");
        String petCategory = sharedPreferences.getString("petCategory", "");
        String petBreed = sharedPreferences.getString("petBreed", "");
        String petColor = sharedPreferences.getString("petColor", "");

        String reference = sharedPreferences.getString("reference", "");
        String petID = sharedPreferences.getString("pet_ID", "");
        String buyerID = sharedPreferences.getString("buyerID", "");
        String buyerPhone = sharedPreferences.getString("buyerPhone", "");
        float petPrice = sharedPreferences.getFloat("petPrice", 0.0f);
        /*String shippingPrice = sharedPreferences.getString("shippingPrice", "");*/
        float totalPrice = sharedPreferences.getFloat("totalPrice", 0.0f);
        String paymentMode = sharedPreferences.getString("paymentMode", "");
        String city = sharedPreferences.getString("city", "");
        String barangay = sharedPreferences.getString("barangay", "");
        String street = sharedPreferences.getString("street", "");
        String orderDate = sharedPreferences.getString("orderDate", "");
        String deliveryProgress = sharedPreferences.getString("deliveryProgress", "");

        TextView txtSeller1 = findViewById(R.id.txtSeller1);
        TextView txtPetName1 = findViewById(R.id.txtPetName1);
        TextView txtPetCategory1 = findViewById(R.id.txtPetCategory1);
        TextView txtPetBreed1 = findViewById(R.id.txtPetBreed1);
        TextView txtColor1 = findViewById(R.id.txtPetColor1);


        TextView txtPetPrice1 = findViewById(R.id.txtPetPrice1);
        /*TextView txtShippingFee1 = findViewById(R.id.txtShippingFee1);)*/
        TextView txtTotalPayment1 = findViewById(R.id.txtTotalPayment1);
        TextView txtPaymentMode1 = findViewById(R.id.txtPaymentModeDisplay);
        TextView txtOrderDate1 = findViewById(R.id.txtOrderDateDisplay);
        TextView txtDeliveryProcess1 = findViewById(R.id.txtDeliveryProcess1);

        TextView txtReference = findViewById(R.id.txtReference);

        txtSeller1.setText(seller);
        txtPetName1.setText(petName);
        txtPetCategory1.setText(petCategory);
        txtPetBreed1.setText(petBreed);
        txtColor1.setText(petColor);

        txtReference.setText(reference);
        txtPetPrice1.setText(String.valueOf(petPrice));
        /*txtShippingFee1.setText(shippingPrice);*/
        txtTotalPayment1.setText(String.valueOf(totalPrice));
        txtPaymentMode1.setText(paymentMode);
        txtOrderDate1.setText(orderDate);
        txtDeliveryProcess1.setText(deliveryProgress);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("OrderData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(OrderSummaryActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}