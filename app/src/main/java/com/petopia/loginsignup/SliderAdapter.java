package com.petopia.loginsignup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;
    private OnSliderItemClickListener sliderItemClickListener;

    public SliderAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    public void setOnSliderItemClickListener(OnSliderItemClickListener listener) {
        this.sliderItemClickListener = listener;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.learn_slide_container, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final SliderViewHolder holder, int position) {
        final int realPosition = position % sliderItems.size();
        holder.setImage(sliderItems.get(realPosition)); // Use modulo to handle looping

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sliderItemClickListener != null) {
                    int clickedPosition = holder.getAdapterPosition() % sliderItems.size(); // Use modulo
                    sliderItemClickListener.onSliderItemClick(clickedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.learnSlide);
        }

        void setImage(SliderItem sliderItem) {
            imageView.setImageResource(sliderItem.getImage());
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

    public interface OnSliderItemClickListener {
        void onSliderItemClick(int position);
    }
}