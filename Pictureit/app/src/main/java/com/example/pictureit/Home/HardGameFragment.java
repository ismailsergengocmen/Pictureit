package com.example.pictureit.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pictureit.R;
import com.example.pictureit.Utils.ImageAdapter;

public class HardGameFragment extends Fragment {

    private static final String TAG = "HardGameFragment";
    GridView gridView;
    public int[] images = {R.drawable.ic_empty_grid,R.drawable.ic_empty_grid,R.drawable.ic_empty_grid,R.drawable.ic_empty_grid,R.drawable.ic_empty_grid};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hard_game, container, false);
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
