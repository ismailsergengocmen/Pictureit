package com.example.pictureit.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pictureit.R;
import com.example.pictureit.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    //Widgets
    private ImageView profilePhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        profilePhoto = view.findViewById(R.id.profilePhoto);

        setProfileImage();

        //setup the backarrow for navigating back to profile activity
        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to profile activity");
                getActivity().finish();
            }
        });
        return view;
    }

    //This method will be changed later since firebase is needed
    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting profile image");
        String imgURL = "cdn.webrazzi.com/uploads/2013/06/android-malware.jpg";
        UniversalImageLoader.setImage(imgURL, profilePhoto, null, "https://");
    }
}
