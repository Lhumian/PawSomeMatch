package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SaleActivity extends MainActivity {

    String user_id;
    ListView listView;
    ImageView backBtn;
    TextView noPetSaleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        backBtn = findViewById(R.id.backBtn);

        user_id = sharedPreferences.getString("user_id", "");

        listView = findViewById(R.id.saleListView);
        noPetSaleTextView = findViewById(R.id.noPetSaleTextView);

        // Fetch delivery orders based on user_id using AsyncTask
        new FetchDeliveryOrdersTask(this).execute(user_id);

        Button btnSellerTransactions = findViewById(R.id.btnSellerTransactions);
        btnSellerTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaleActivity.this, SellerPaymentTransactions.class);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

    }

    public void updateUI(List<DeliveryOrder> deliveryOrders) {
        // Update the UI with the fetched data
        SaleAdapter adapter = new SaleAdapter(this, deliveryOrders);
        listView.setAdapter(adapter);
    }


    // Method to fetch delivery orders based on user_id
    public class FetchDeliveryOrdersTask extends AsyncTask<String, Void, List<DeliveryOrder>> {

        private WeakReference<SaleActivity> activityReference;

        public FetchDeliveryOrdersTask(SaleActivity activity) {
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected List<DeliveryOrder> doInBackground(String... userId) {
            List<DeliveryOrder> orders = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();
            String url = "https://pawsomematch.online/android/retrieve_sale.php?user_id=" + userId[0];

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    // Parse JSON response
                    JSONArray jsonOrders = new JSONArray(responseBody);
                    for (int i = 0; i < jsonOrders.length(); i++) {
                        JSONObject jsonOrder = jsonOrders.getJSONObject(i);
                        DeliveryOrder order = new DeliveryOrder();

                        // Populate order object with data from JSON
                        order.setPetID(jsonOrder.getString("pet_ID"));
                        order.setBuyerId(jsonOrder.getInt("buyer_ID"));
                        order.setBuyerPhone(jsonOrder.getString("buyer_phone"));
                        order.setPetPrice(jsonOrder.getDouble("pet_price"));
                        order.setTotalPrice(jsonOrder.getDouble("total_price"));
                        order.setPaymentMode(jsonOrder.getString("payment_mode"));
                        order.setCity(jsonOrder.getString("city"));
                        order.setBarangay(jsonOrder.getString("barangay"));
                        order.setStreet(jsonOrder.getString("street"));
                        order.setOrderDate(jsonOrder.getString("order_date"));
                        order.setDeliveryProgress(jsonOrder.getString("delivery_progress"));

                        // Add the order to the list
                        orders.add(order);
                    }
                } else {
                    // Handle failed response
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return orders;
        }

        @Override
        protected void onPostExecute(List<DeliveryOrder> result) {
            SaleActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            if (!result.isEmpty()) {
                // Data retrieval was successful, update the UI
                activity.updateUI(result);
            } else {
                // Handle the case when data retrieval fails
                activity.noPetSaleTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
