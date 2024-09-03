package com.petopia.loginsignup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleAdapter extends BaseAdapter {
    private Context context;
    private List<DeliveryOrder> deliveryOrders;

    String petID;

    public SaleAdapter(Context context, List<DeliveryOrder> deliveryOrders) {
        this.context = context;
        this.deliveryOrders = deliveryOrders;
    }

    @Override
    public int getCount() {
        return deliveryOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return deliveryOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void getPetName(String petId, TextView petIdTextView, View convertView) {
        String url = "https://pawsomematch.online/android/get_selling_petname.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Check if the tag matches the current pet ID
                        if (convertView.getTag() != null && convertView.getTag().toString().equals(petId)) {
                            // Set the pet name in the TextView
                            petIdTextView.setText("Pet Name: " + response.trim());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error if needed
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pet_ID", petId);
                return params;
            }
        };

        // Tag the request with the pet ID
        request.setTag(petId);

        // Add the request to the request queue
        Volley.newRequestQueue(context).add(request);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sale_item_layout, null);
        }

        TextView petIdTextView = convertView.findViewById(R.id.petIdTextView);
        TextView buyerPhoneTextView = convertView.findViewById(R.id.buyerPhoneTextView);
        TextView petPriceTextView = convertView.findViewById(R.id.petPriceTextView);
        TextView totalPriceTextView = convertView.findViewById(R.id.totalPriceTextView);
        TextView paymentModeTextView = convertView.findViewById(R.id.paymentModeTextView);
        TextView cityTextView = convertView.findViewById(R.id.cityTextView);
        TextView barangayTextView = convertView.findViewById(R.id.barangayTextView);
        TextView streetTextView = convertView.findViewById(R.id.streetTextView);
        TextView orderDateTextView = convertView.findViewById(R.id.orderDateTextView);
        TextView deliveryProgressTextView = convertView.findViewById(R.id.deliveryProgressTextView);

        // Get the current delivery order
        DeliveryOrder order = deliveryOrders.get(position);

        petID = order.getPetID();
        petIdTextView.setText("Pet Name: Loading...");
        convertView.setTag(petID);

        if (convertView.getTag() != null) {
            Volley.newRequestQueue(context).cancelAll(convertView.getTag().toString());
        }

        // Make a new request
        getPetName(order.getPetID(), petIdTextView, convertView);

        buyerPhoneTextView.setText(order.getBuyerPhone());
        petPriceTextView.setText("₱" + order.getPetPrice());
        totalPriceTextView.setText("₱" + order.getTotalPrice());
        paymentModeTextView.setText(order.getPaymentMode());
        cityTextView.setText("City: " + order.getCity());
        barangayTextView.setText("Barangay: " + order.getBarangay());
        streetTextView.setText("Street: " + order.getStreet());
        orderDateTextView.setText("Order Date: " + order.getOrderDate());
        deliveryProgressTextView.setText("Delivery Progress: " + order.getDeliveryProgress());

        // Inside your getView() method in SaleAdapter
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_update, null);
                builder.setView(dialogView);

                Button updateButton = dialogView.findViewById(R.id.updateButton);
                Button cancelButton = dialogView.findViewById(R.id.cancelButton);

                final AlertDialog alertDialog = builder.create();

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Extract the values from TextViews
                        String buyerPhone = buyerPhoneTextView.getText().toString();
                        String petPrice = petPriceTextView.getText().toString();
                        String totalPrice = totalPriceTextView.getText().toString();
                        String paymentMode = paymentModeTextView.getText().toString();
                        String city = cityTextView.getText().toString();
                        String barangay = barangayTextView.getText().toString();
                        String street = streetTextView.getText().toString();
                        String orderDate = orderDateTextView.getText().toString();
                        String deliveryProgress = deliveryProgressTextView.getText().toString();

                        // Create an Intent to start the UpdateProgressActivity
                        Intent intent = new Intent(context, UpdateProgressActivity.class);

                        // Pass the values as extras in the Intent
                        intent.putExtra("petId", order.getPetID());
                        intent.putExtra("buyerPhone", buyerPhone);
                        intent.putExtra("petPrice", petPrice);
                        intent.putExtra("totalPrice", totalPrice);
                        intent.putExtra("paymentMode", paymentMode);
                        intent.putExtra("city", city);
                        intent.putExtra("barangay", barangay);
                        intent.putExtra("street", street);
                        intent.putExtra("orderDate", orderDate);
                        intent.putExtra("deliveryProgress", deliveryProgress);

                        context.startActivity(intent);

                        alertDialog.dismiss();
                    }
                });


                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });


        return convertView;
    }
}
