package com.example.pictureit.Utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pictureit.R;

import java.util.List;

public class BadgeCardRecyclerViewAdapter extends RecyclerView.Adapter<BadgeCardRecyclerViewAdapter.BadgeViewHolder> {

    //Constants
    private static final String TAG = "BadgeCardRecyclerViewAd";

    //Variables
    private List<String> mTask;
    private List<Integer> mProgress;
    private List<String> mImageUrls;
    private List<Integer> mStatus;
    private Context mContext;
    private UniversalImageLoader universalImageLoader;

    public BadgeCardRecyclerViewAdapter(Context mContext, List<String> mTask, List<Integer> mProgress, List<String> mImageUrls, List<Integer> mStatus) {
        this.mTask = mTask;
        this.mProgress = mProgress;
        this.mImageUrls = mImageUrls;
        this.mContext = mContext;
        this.mStatus = mStatus;
        universalImageLoader = new UniversalImageLoader(mContext);
    }

    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_badge_cardview, parent, false);
        return new BadgeCardRecyclerViewAdapter.BadgeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        universalImageLoader.setImage(mImageUrls.get(position), holder.image, null, "");
        holder.task.setText(mTask.get(position));
        holder.progress.setText(mProgress.get(position) + " photos are taken");

        if (mStatus.get(position) == 0) {
            holder.status.setImageResource(R.drawable.ic_not_ok);
        } else {
            holder.status.setImageResource(R.drawable.ic_ok);
            holder.progress.setTextColor(Color.parseColor("#36BA0B"));
        }
    }

    @Override
    public int getItemCount() {
        return mTask.size();
    }

    public class BadgeViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView status;
        TextView task;
        TextView progress;

        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.badgeImageView);
            task = itemView.findViewById(R.id.task);
            progress = itemView.findViewById(R.id.progress);
            status = itemView.findViewById(R.id.status);
        }
    }
}
