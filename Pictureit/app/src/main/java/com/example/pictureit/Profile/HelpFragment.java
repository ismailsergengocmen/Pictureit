package com.example.pictureit.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pictureit.R;


public class HelpFragment extends Fragment {

    //Constants
    private static final String TAG = "HelpFragment";

    //Variables
    private TextView firstP, secondP, thirdP, fourthP, fifthP;
    private ImageView questionmark, backArrow;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        firstP = view.findViewById(R.id.FirstP);
        secondP = view.findViewById(R.id.SecondP);
        thirdP = view.findViewById(R.id.ThirdP);
        fourthP = view.findViewById(R.id.FourthP);
        fifthP = view.findViewById(R.id.FifthP);
        backArrow = view.findViewById(R.id.HelpbackArrow);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to profile activity");
                backToActivity();
            }
        });
        return view;
    }

    private void backToActivity() {
        getFragmentManager().popBackStack();
    }

}


