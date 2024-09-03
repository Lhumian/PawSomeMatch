package com.petopia.loginsignup;

import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatMessage> messageList;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        holder.tvMessage.setText(message.getMessage());

        // Set message alignment based on sender or receiver
        LinearLayout.LayoutParams messageLayoutParams = (LinearLayout.LayoutParams) holder.tvMessage.getLayoutParams();
        LinearLayout.LayoutParams timestampLayoutParams = (LinearLayout.LayoutParams) holder.tvTimestamp.getLayoutParams();

        if (message.isSentByCurrentUser()) {
            messageLayoutParams.gravity = Gravity.END;
            timestampLayoutParams.gravity = Gravity.END;
            holder.tvMessage.setBackgroundResource(R.drawable.sender_message_bg);
            holder.tvTimestamp.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
        } else {
            messageLayoutParams.gravity = Gravity.START;
            timestampLayoutParams.gravity = Gravity.START;
            holder.tvMessage.setBackgroundResource(R.drawable.receiver_message_bg);
            holder.tvTimestamp.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
        }

        holder.tvMessage.setLayoutParams(messageLayoutParams);
        holder.tvTimestamp.setLayoutParams(timestampLayoutParams);

        // Set message timestamp
        holder.tvTimestamp.setText(message.getTimestamp());
    }








    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMessage;
        public TextView tvTimestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}
