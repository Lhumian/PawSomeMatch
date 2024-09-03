package com.petopia.loginsignup;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class PetMatchAdapter extends RecyclerView.Adapter<PetMatchAdapter.PetViewHolder> {
    private List<PetMatchData> mPetList;
    private Context mContext;


    public PetMatchAdapter(Context context, List<PetMatchData> petList) {
        mContext = context;
        mPetList = petList;
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_match_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PetViewHolder holder, int position) {
        PetMatchData pet = mPetList.get(position);
        holder.mNameTextView.setText(pet.getPet_name());
        holder.mGenderTextView.setText(pet.getGender());

        // Handle the like button click event
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String petName = pet.getPet_name();
                String ownerName = pet.getOwner_name();
                showLikeNotification(petName, ownerName);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPetList.size();
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTextView;
        public TextView mGenderTextView;
        public Button likeButton;

        public PetViewHolder(View view) {
            super(view);
            mNameTextView = view.findViewById(R.id.pet_name);
            mGenderTextView = view.findViewById(R.id.pet_gender);
            likeButton = view.findViewById(R.id.btn_like);
        }
    }

    private void showLikeNotification(String petName, String ownerName) {
        Intent intent = new Intent(mContext, AcceptanceActivity.class);
        intent.putExtra("pet_name", petName);
        intent.putExtra("owner_name", ownerName);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.notification_icon);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "channel_id")
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(largeIcon)
                .setContentTitle("Pet Liked")
                .setContentText("You liked " + petName + "'s profile. Reach out to " + ownerName + " for more information.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(0, builder.build());
    }
}
