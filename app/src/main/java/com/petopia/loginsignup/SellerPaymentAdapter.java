package com.petopia.loginsignup;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class SellerPaymentAdapter extends RecyclerView.Adapter<SellerPaymentAdapter.SellerPaymentViewHolder> {

    private Context context;
    private List<SellerPayment> sellerPaymentList;

    Button btnSellerPayment;

    public SellerPaymentAdapter(Context context, List<SellerPayment> sellerPaymentList) {
        this.context = context;
        this.sellerPaymentList = sellerPaymentList;
    }

    @NonNull
    @Override
    public SellerPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seller_payment, parent, false);
        return new SellerPaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerPaymentViewHolder holder, int position) {
        SellerPayment sellerPayment = sellerPaymentList.get(position);

        holder.petIdTextView.setText("Pet ID: " + sellerPayment.getPetID());
        holder.referenceNumberTextView.setText("Reference Number: " + sellerPayment.getReferenceNumber());
        holder.imageNameTextView.setText("Image Name: " + sellerPayment.getImageName());
        String link = "https://pawsomematch.online/pictures/";

        Picasso.get().load(link + sellerPayment.getImageName()).into(holder.imageView);

        // Reset button state
        holder.btnSellerPayment.setEnabled(true);
        holder.btnSellerPayment.setText("Confirm Payment");

        // Call the checkPaymentStatus method to update the button based on payment status
        checkPaymentStatus(sellerPayment.getPetID(), holder.btnSellerPayment);

        holder.btnSellerPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the PHP function here to update the database
                updatePaymentConfirmation(sellerPayment.getPetID(), holder.btnSellerPayment);
            }
        });
    }


    @Override
    public int getItemCount() {
        return sellerPaymentList.size();
    }

    public static class SellerPaymentViewHolder extends RecyclerView.ViewHolder {
        TextView petIdTextView;
        TextView referenceNumberTextView;
        TextView imageNameTextView;

        Button btnSellerPayment;

        ImageView imageView;


        public SellerPaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            btnSellerPayment = itemView.findViewById(R.id.btnSellerConfirm);
            petIdTextView = itemView.findViewById(R.id.petIdTextView);
            referenceNumberTextView = itemView.findViewById(R.id.referenceNumberTextView);
            imageNameTextView = itemView.findViewById(R.id.imageNameTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
    private void updatePaymentConfirmation(int petId, Button btnSellerPayment) {
        // Display progress dialog here
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Updating payment confirmation...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Make a network call to your PHP script
        // You need to replace the URL with the actual URL of your PHP script
        String phpScriptUrl = "https://pawsomematch.online/android/seller_confirmation.php?pet_id=" + petId;

        // Use Volley for the network request (you can also use Retrofit)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, phpScriptUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // After a successful request, update the button text and disable it
                        btnSellerPayment.setText("Payment Confirmed");
                        btnSellerPayment.setEnabled(false);

                        // Dismiss the progress dialog
                        progressDialog.dismiss();
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        progressDialog.dismiss();
                        Toast.makeText(context, "Error updating payment confirmation", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void checkPaymentStatus(int petId, Button btnSellerPayment) {
        // Display progress dialog here

        // Make a network call to your PHP script to check payment status
        String phpScriptUrl = "https://pawsomematch.online/android/seller_check_confirmation.php?pet_id=" + petId;

        // Use Volley for the network request (you can also use Retrofit)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, phpScriptUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // After a successful request, update the button text and disable it if payment is confirmed
                        if (response.trim().equals("1")) {
                            btnSellerPayment.setText("Payment Confirmed");
                            btnSellerPayment.setEnabled(false);
                        }

                        // Dismiss the progress dialog
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Toast.makeText(context, "Error checking payment status", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
