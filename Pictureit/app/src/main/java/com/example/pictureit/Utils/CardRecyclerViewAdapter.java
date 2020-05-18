package com.example.pictureit.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pictureit.R;
import com.example.pictureit.models.Photo;

import java.util.ArrayList;
import java.util.List;

public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "CardRecyclerViewAdapter";

    private List<Photo> mPhotos;
    private Context mContext;
    private UniversalImageLoader universalImageLoader;

    public CardRecyclerViewAdapter(Context context, List<Photo> photo) {
        mContext = context;
        mPhotos = photo;
        universalImageLoader= new UniversalImageLoader(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_cardview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        universalImageLoader.setImage(mPhotos.get(position).getImage_path(), holder.image, null, "" );
        holder.text.setText(mPhotos.get(position).getDate_created());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: you clicked.");
                Toast.makeText(mContext, mPhotos.get(position).getDate_created(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textView);
            image = itemView.findViewById(R.id.imageView);
        }

    }

}
