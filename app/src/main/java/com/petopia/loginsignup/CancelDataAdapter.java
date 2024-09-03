package com.petopia.loginsignup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CancelDataAdapter extends RecyclerView.Adapter<CancelDataAdapter.ViewHolder> {
    private Context context;
    private List<CancelData> cancelDataList;
    private LayoutInflater inflater;

    public CancelDataAdapter(Context context, List<CancelData> cancelDataList) {
        this.context = context;
        this.cancelDataList = cancelDataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_cancel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CancelData cancelData = cancelDataList.get(position);
        holder.petNameTextView.setText(cancelData.getPet_name());
        holder.buyerPhoneTextView.setText(cancelData.getBuyer_phone());
        holder.petPriceTextView.setText("" + cancelData.getPet_price());
        holder.totalPriceTextView.setText("" + cancelData.getTotal_price());
        holder.paymentModeTextView.setText(cancelData.getPayment_mode());
        holder.cityTextView.setText(cancelData.getCity());
        holder.barangayTextView.setText(cancelData.getBarangay());
        holder.streetTextView.setText(cancelData.getStreet());
        holder.orderDateTextView.setText(cancelData.getOrder_date());
        holder.reasonTextView.setText(cancelData.getReason());
        holder.statusTextView.setText(cancelData.getStatus());
        holder.cancelDateTextView.setText(cancelData.getCancel_date());
    }

    @Override
    public int getItemCount() {
        return cancelDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView petNameTextView;
        TextView buyerPhoneTextView;
        TextView petPriceTextView;
        TextView totalPriceTextView;
        TextView paymentModeTextView;
        TextView cityTextView;
        TextView barangayTextView;
        TextView streetTextView;
        TextView orderDateTextView;
        TextView reasonTextView;
        TextView statusTextView;
        TextView cancelDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            petNameTextView = itemView.findViewById(R.id.petNameTextView);
            buyerPhoneTextView = itemView.findViewById(R.id.buyerPhoneTextView);
            petPriceTextView = itemView.findViewById(R.id.petPriceTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            paymentModeTextView = itemView.findViewById(R.id.paymentModeTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            barangayTextView = itemView.findViewById(R.id.barangayTextView);
            streetTextView = itemView.findViewById(R.id.streetTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            cancelDateTextView = itemView.findViewById(R.id.cancelDateTextView);
        }
    }
}
