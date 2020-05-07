package com.example.pictureit.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.pictureit.R;
import com.example.pictureit.Utils.BottomNavigationViewHelper;
import com.example.pictureit.Utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUMBER = 2;

    private Context mContext = ProfileActivity.this;
    private ProgressBar mProgressBar;
    private ImageView profileImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");

        init();

//        setupBottomNavigationView();
//        setupToolBar();
//        setupActivityWidgets();
//        setProfileImage();
    }

    /**
     * This method manages frames inside of the Profile Activity
     */
    private void init() {
        Log.d(TAG, "init: inflating " + getString(R.string.profile_fragment));

        ProfileFragment fragment = new ProfileFragment();
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();
    }
//
//    //This method will be changed later since firebase is needed
//    private void setProfileImage() {
//        Log.d(TAG, "setProfileImage: setting profile image.");
//        String imgURL = "cdn.webrazzi.com/uploads/2013/06/android-malware.jpg";
//        UniversalImageLoader.setImage(imgURL, profileImage, mProgressBar, "https://");
//    }
//
//    private void setupActivityWidgets() {
//        mProgressBar = findViewById(R.id.profileProgressBar);
//        mProgressBar.setVisibility(View.GONE);
//        profileImage = findViewById(R.id.profileImage);
//    }
//
//    private void setupToolBar() {
//        Toolbar profileToolBar = (Toolbar) findViewById(R.id.profileToolBar);
//        setSupportActionBar(profileToolBar);
//
//        ImageView profileMenu = findViewById(R.id.profileMenu);
//        profileMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: navigating to settings");
//                Intent intent = new Intent(mContext,AccountSettingsActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    /**
//     * BottomNavigationView setup
//     */
//    private void setupBottomNavigationView() {
//        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
//        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
//
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
//        menuItem.setChecked(true);
//    }

}
