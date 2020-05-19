package com.example.pictureit.Profile;

import android.media.Image;
import android.os.Bundle;
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

    private static final String TAG = "HelpFragment";

    //Variables
    private TextView FirstP,SecondP,ThirdP,FourthP,FifthP;
    private ImageView Questionmark;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        FirstP = view.findViewById(R.id.FirstP);
        SecondP = view.findViewById(R.id.SecondP);
        ThirdP = view.findViewById(R.id.ThirdP);
        FourthP = view.findViewById(R.id.FourthP);
        FifthP = view.findViewById(R.id.FifthP);
        Questionmark = view.findViewById(R.id.QuestionEmoji);

        return view;
    }
}
