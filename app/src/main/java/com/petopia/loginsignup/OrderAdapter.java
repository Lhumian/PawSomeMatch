package com.petopia.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private Context context;

    Order order;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        order = orderList.get(position);
        holder.petNameTextView.setText(order.getPetName());
        holder.buyerPhoneTextView.setText(order.getBuyerPhone());
        holder.petPriceTextView.setText("Pet Price: " + order.getPetPrice());
        holder.totalPriceTextView.setText("Total Payment: ₱" + order.getTotalPrice());
        holder.paymentModeTextView.setText(order.getPaymentMode());
        holder.cityTextView.setText("City: " + order.getCity());
        holder.barangayTextView.setText("Barangay: " + order.getBarangay());
        holder.streetTextView.setText("Street: " + order.getStreet());
        holder.orderDateTextView.setText("Date Ordered: " + order.getOrderDate());
        holder.deliveryProgressTextView.setText("Delivery Status: " + order.getDeliveryProgress());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView petNameTextView;
        TextView buyerPhoneTextView;
        TextView petPriceTextView;
        TextView shippingTextView;
        TextView totalPriceTextView;
        TextView paymentModeTextView;
        TextView cityTextView;
        TextView barangayTextView;
        TextView streetTextView;
        TextView orderDateTextView;
        TextView deliveryProgressTextView;
        Button btnOrderReceived;
        TextView btnCancel;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            petNameTextView = itemView.findViewById(R.id.textPetName);
            buyerPhoneTextView = itemView.findViewById(R.id.textBuyerPhone);
            petPriceTextView = itemView.findViewById(R.id.textPetPrice);
            shippingTextView = itemView.findViewById(R.id.textShipping);
            totalPriceTextView = itemView.findViewById(R.id.textTotalPrice);
            paymentModeTextView = itemView.findViewById(R.id.textPaymentMode);
            cityTextView = itemView.findViewById(R.id.textCity);
            barangayTextView = itemView.findViewById(R.id.textBarangay);
            streetTextView = itemView.findViewById(R.id.textStreet);
            orderDateTextView = itemView.findViewById(R.id.textOrderDate);
            deliveryProgressTextView = itemView.findViewById(R.id.textDeliveryProgress);
            btnOrderReceived = itemView.findViewById(R.id.btnOrderReceived);
            btnCancel = itemView.findViewById(R.id.btnCancel);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Assuming you have a valid context, you can use the context to get SharedPreferences
                    Intent intent = new Intent(context, CancelActivity.class);
                    SharedPreferences sharedPreferences = context.getSharedPreferences("Cancel", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Store the data in SharedPreferences
                    String user_ID = HomeActivity.userIdTv.getText().toString().trim();
                    editor.putString("user_ID", user_ID);
                    editor.putString("pet_ID", String.valueOf(order.getPetId()));

                    editor.apply(); // Commit the changes
                    context.startActivity(intent);
                }
            });

            btnOrderReceived.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the button click event, e.g., mark the order as received
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Order clickedOrder = orderList.get(position);

                        // Implement the logic to mark the order as received and update the UI
                        markOrderAsReceived(clickedOrder.getPetId());
                    }
                }
            });

            checkOrderAsReceived();

        }

        private void markOrderAsReceived(int petId) {
            // Define the URL for updating order status
            String updateUrl = "https://pawsomematch.online/android/mark_order_received.php";

            // Create a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            // Create a StringRequest to make a POST request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Handle the response here
                    if (response.equals("success")) {
                        // Update the UI or show a success message
                        // For example, update the text on the deliveryProgressTextView
                        deliveryProgressTextView.setText("");
                        // Disable the button
                        btnOrderReceived.setEnabled(false);
                        btnOrderReceived.setText("Received"); // Optionally, change the button text
                        // Inform the user
                        Toast.makeText(context, "Order marked as received", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("already_received")) {
                        // Handle the case where the order is already marked as received
                        btnOrderReceived.setEnabled(false);
                        btnOrderReceived.setText("Received");
                        Toast.makeText(context, "Order is already marked as received", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle other response cases (e.g., errors)
                        Toast.makeText(context, "Error marking order as received", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle network errors here
                    error.printStackTrace();
                    // Show a toast message for the network error
                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("pet_ID", String.valueOf(petId)); // Convert the petId to String
                    return params;
                }
            };

            // Add the request to the queue
            requestQueue.add(stringRequest);
        }

        // Inside the OrderViewHolder class
        private void checkOrderAsReceived() {
            // Define the URL for checking order status
            String checkUrl = "https://pawsomematch.online/android/check_order_received.php";

            // Create a request queue for checking
            RequestQueue checkRequestQueue = Volley.newRequestQueue(context);

            // Create a StringRequest to make a POST request to check order_received status
            StringRequest checkStringRequest = new StringRequest(Request.Method.POST, checkUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("1")) {
                        // Order is already marked as received
                        deliveryProgressTextView.setText("");
                        btnOrderReceived.setEnabled(false);
                        btnOrderReceived.setText("Received");
                    } else if (response.equals("0")) {
                        // Order is not marked as received, proceed to mark it as received
                        // No need to do anything here since the button is already enabled
                    } else {
                        // Handle other response cases (e.g., errors)
                        Toast.makeText(context, "Error checking order status", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("pet_ID", String.valueOf(orderList.get(getAdapterPosition()).getPetId())); // Get petId from the orderList
                    return params;
                }
            };

            // Add the check request to the queue
            checkRequestQueue.add(checkStringRequest);
        }


    }
}
