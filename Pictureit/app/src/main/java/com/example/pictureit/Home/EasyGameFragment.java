package com.example.pictureit.Home;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pictureit.R;
import com.example.pictureit.Utils.ImageAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.pictureit.R;
import com.example.pictureit.Utils.BottomNavigationViewHelper;
import com.example.pictureit.Utils.SectionsPagerAdapter;
import com.example.pictureit.Utils.UniversalImageLoader;
import com.google.android.material.tabs.TabLayout;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EasyGameFragment extends Fragment {

    private static final String TAG = "EasyGameFragment";
    GridView gridView;
    public int[] images = {R.drawable.ic_empty_grid,R.drawable.ic_empty_grid,R.drawable.ic_empty_grid,R.drawable.ic_empty_grid,R.drawable.ic_empty_grid};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_easy_game, container, false);
        gridView = (GridView) view.findViewById(R.id.grid_view);
        ImageAdapter adapter = new ImageAdapter(getActivity(), images);
        gridView.setAdapter(adapter);

        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to profile activity");
                backToActivity();
            }
        });
        return view;
    }

    public void backToActivity() {
        getFragmentManager().popBackStack();
    }
}
