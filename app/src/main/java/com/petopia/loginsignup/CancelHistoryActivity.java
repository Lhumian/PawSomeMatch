package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.util.Log; // Added for logging
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CancelHistoryActivity extends AppCompatActivity {

    ArrayList<CancelData> orderList = new ArrayList();

    CancelDataAdapter orderAdapter;

    private RecyclerView cancelRecyclerView;
    String user_id;

    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_history);

        user_id = HomeActivity.userIdTv.getText().toString().trim();

        // Initialize the RecyclerView
        cancelRecyclerView = findViewById(R.id.cancelRecyclerView);
        orderAdapter = new CancelDataAdapter(this, orderList);
        cancelRecyclerView.setAdapter(orderAdapter);
        cancelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CancelHistoryActivity.this, OrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        fetchUserOrders(user_id);
    }

    private void fetchUserOrders(String userId) {
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("user_id", userId)
                .build();

        Request request = new Request.Builder()
                .url("https://pawsomematch.online/android/retrieve_cancel_order.php") // Replace with your server URL
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int petID = jsonObject.getInt("pet_ID");
                            String petName = jsonObject.getString("pet_name");
                            String buyerPhone = jsonObject.getString("buyer_phone");
                            double petPrice = jsonObject.getDouble("pet_price");
                            double totalPrice = jsonObject.getDouble("total_price");
                            String paymentMode = jsonObject.getString("payment_mode");
                            String city = jsonObject.getString("city");
                            String barangay = jsonObject.getString("barangay");
                            String street = jsonObject.getString("street");
                            String orderDate = jsonObject.getString("order_date");
                            String reason = jsonObject.getString("reason");
                            String status = jsonObject.getString("status");
                            String cancelDate = jsonObject.getString("cancel_date");

                            // Create an Order object with the retrieved details
                            CancelData cancelData = new CancelData(
                                    petID, petName, buyerPhone, petPrice, totalPrice,
                                    paymentMode, city, barangay, street, orderDate,
                                    reason, status, cancelDate);

                            // Add the order to the list
                            orderList.add(cancelData);
                        }

                        // Update the UI on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                orderAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
