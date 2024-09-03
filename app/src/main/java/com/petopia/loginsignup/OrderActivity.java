package com.petopia.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    String user_id;
    ArrayList<Order> orderList = new ArrayList();
    OrderAdapter orderAdapter;
    RecyclerView recyclerView;
    ImageView backBtn;
    ImageView btnCancelHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        backBtn = findViewById(R.id.backBtn);

        // Retrieve user_id from HomeActivity
        user_id = HomeActivity.userIdTv.getText().toString();
        TextView noPetRegisteredTextView = findViewById(R.id.noPetRegisteredTextView);

        // Initialize the RecyclerView and adapter
        recyclerView = findViewById(R.id.orderRecyclerView);
        orderAdapter = new OrderAdapter(this, orderList);
        recyclerView.setAdapter(orderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Call the method to fetch orders
        fetchUserOrders(user_id);

        if (orderList.isEmpty()) {
            noPetRegisteredTextView.setVisibility(View.VISIBLE);
        } else {
            noPetRegisteredTextView.setVisibility(View.GONE);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, Settings.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }

        });

        btnCancelHistory = findViewById(R.id.btnCancelHistory);
        btnCancelHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, CancelHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchUserOrders(String userId) {
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("user_id", userId)
                .build();

        Request request = new Request.Builder()
                .url("https://pawsomematch.online/android/retrieve_order.php") // Replace with your server URL
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
                            JSONObject jsonOrder = jsonArray.getJSONObject(i);
                            int petId = jsonOrder.getInt("pet_ID");
                            String petName = jsonOrder.getString("pet_name");
                            String buyerPhone = jsonOrder.getString("buyer_phone");
                            double petPrice = jsonOrder.getDouble("pet_price");;
                            double totalPrice = jsonOrder.getDouble("total_price");
                            String paymentMode = jsonOrder.getString("payment_mode");
                            String city = jsonOrder.getString("city");
                            String barangay = jsonOrder.getString("barangay");
                            String street = jsonOrder.getString("street");
                            String orderDate = jsonOrder.getString("order_date");
                            String deliveryProgress = jsonOrder.getString("delivery_progress");

                            // Create an Order object with the retrieved details
                            Order order = new Order(petName, buyerPhone, petPrice, totalPrice, paymentMode, city, barangay, street, orderDate, deliveryProgress, petId);

                            // Add the order to the list
                            orderList.add(order);
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
