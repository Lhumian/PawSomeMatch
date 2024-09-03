package com.petopia.loginsignup;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<Notification> notifications;

    public NotificationAdapter() {
        this.notifications = new ArrayList<>();
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;
        private TextView dateTextView;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        public void bind(final Notification notification) {
            messageTextView.setText(notification.getMessage());
            dateTextView.setText(notification.getDate());

            if (notification.getMessage().toLowerCase().contains("liked")) {
                // If it does, add an Intent to navigate to the AcceptanceActivity
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Assuming you have an Intent for AcceptanceActivity
                        Intent intent = new Intent(itemView.getContext(), AcceptanceActivity.class);
                        // Pass any necessary data to AcceptanceActivity using intent.putExtra()
                        itemView.getContext().startActivity(intent);
                    }
                });
            } else if (notification.getMessage().toLowerCase().contains("bought")) {
                // If it contains "bought," add an Intent to navigate to the SaleActivity
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Assuming you have an Intent for SaleActivity
                        Intent intent = new Intent(itemView.getContext(), SaleActivity.class);
                        // Pass any necessary data to SaleActivity using intent.putExtra()
                        itemView.getContext().startActivity(intent);
                    }
                });
            } else if (notification.getMessage().toLowerCase().contains("purchased")) {
                // If it contains "bought," add an Intent to navigate to the SaleActivity
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Assuming you have an Intent for OrderActivity
                        Intent intent = new Intent(itemView.getContext(), OrderActivity.class);
                        // Pass any necessary data to SaleActivity using intent.putExtra()
                        itemView.getContext().startActivity(intent);

                    }
                });
            } else if (notification.getMessage().toLowerCase().contains("accepted")) {
                // If it contains "bought," add an Intent to navigate to the SaleActivity
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Assuming you have an Intent for OrderActivity
                        Intent intent = new Intent(itemView.getContext(), HomeActivity.class);
                        intent.putExtra("redirect_to_messaging", true);
                        itemView.getContext().startActivity(intent);
                    }
                });
            } else if (notification.getMessage().toLowerCase().contains("updated")) {
                // If it contains "bought," add an Intent to navigate to the SaleActivity
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Assuming you have an Intent for OrderActivity
                        Intent intent = new Intent(itemView.getContext(), HomeActivity.class);
                        intent.putExtra("redirect_to_messaging", true);
                        itemView.getContext().startActivity(intent);
                    }
                });
            }

            else if (notification.getMessage().toLowerCase().contains("check")) {
                // If it contains "bought," add an Intent to navigate to the SaleActivity
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Assuming you have an Intent for OrderActivity
                        Intent intent = new Intent(itemView.getContext(), HomeActivity.class);
                        // Pass any necessary data to SaleActivity using intent.putExtra()
                        itemView.getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}
