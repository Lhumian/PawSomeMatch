package com.petopia.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompletedTransactionAdapter extends RecyclerView.Adapter<CompletedTransactionAdapter.TransactionViewHolder> {

    private List<CompletedTransaction> transactionList;
    private Context context;

    // Constructor
    public CompletedTransactionAdapter(List<CompletedTransaction> transactionList, Context context) {
        this.transactionList = transactionList;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_item_layout, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        CompletedTransaction transaction = transactionList.get(position);

        // Set the values to the views in your transaction_item_layout.xml
        holder.petIdTextView.setText(transaction.getPetId());
        holder.petNameTextView.setText(transaction.getPetName());
        holder.sellerTextView.setText(transaction.getSeller());
        holder.referenceTextView.setText(transaction.getReference());
        holder.priceTextView.setText(String.valueOf(transaction.getPrice()));
        holder.orderDateTextView.setText(transaction.getOrderDate());

        holder.reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start ReviewActivity with pet_ID
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.putExtra("pet_ID", transaction.getPetId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView petIdTextView, petNameTextView, sellerTextView, referenceTextView, priceTextView, orderDateTextView;
        Button reviewButton;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            petIdTextView = itemView.findViewById(R.id.petIdTextView);
            petNameTextView = itemView.findViewById(R.id.petNameTextView);
            sellerTextView = itemView.findViewById(R.id.sellerTextView);
            referenceTextView = itemView.findViewById(R.id.referenceTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            reviewButton = itemView.findViewById(R.id.reviewButton);
        }
    }
}
