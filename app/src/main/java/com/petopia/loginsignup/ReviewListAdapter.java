package com.petopia.loginsignup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private final List<ReviewList> reviewList;
    private final Context context;

    public ReviewListAdapter(Context context, List<ReviewList> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewList review = reviewList.get(position);

        // Populate the data into the template view using the data object
        holder.petIdTextView.setText("Pet ID: " + review.getPet_ID());
        holder.petNameTextView.setText("Pet Name: " + review.getPet_name());
        holder.referenceTextView.setText("Reference: " + review.getReference());
        holder.buyerNameTextView.setText("Buyer Name: " + review.getBuyer_name());
        holder.ratingTextView.setText("Rating: " + String.valueOf(review.getRating()));
        holder.descriptionTextView.setText("Description: " + review.getDescription());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView petIdTextView;
        public TextView petNameTextView;
        public TextView referenceTextView;
        public TextView buyerNameTextView;
        public TextView ratingTextView;
        public TextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            petIdTextView = itemView.findViewById(R.id.petIdTextView);
            petNameTextView = itemView.findViewById(R.id.petNameTextView);
            referenceTextView = itemView.findViewById(R.id.referenceTextView);
            buyerNameTextView = itemView.findViewById(R.id.buyerNameTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
