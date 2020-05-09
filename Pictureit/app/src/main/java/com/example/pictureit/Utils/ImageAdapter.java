package com.example.pictureit.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.pictureit.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    public int[] imageArray;

    public ImageAdapter( Context context, int[] images){
        this.imageArray = images;
        this.mContext = context;
    }

    @Override
    public int getCount(){
        return imageArray.length;
    }

    @Override
    public Object getItem(int position) {
        return imageArray[position];
    }

    @Override
    public long getItemId (int position){
        return 0;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(imageArray[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(240, 245));
        return imageView;
    }
}
