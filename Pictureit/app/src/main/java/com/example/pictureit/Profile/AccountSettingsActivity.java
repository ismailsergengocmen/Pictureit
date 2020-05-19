package com.example.pictureit.Profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.pictureit.R;
import com.example.pictureit.Utils.BottomNavigationViewHelper;
import com.example.pictureit.Utils.SectionsStatePagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class AccountSettingsActivity extends AppCompatActivity {

    //Constants
    private static final String TAG = "AccountSettingsActivity";
    private Context mContext ;

    //Widgets
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;

    //Adapter
    private SectionsStatePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        Log.d(TAG, "onCreate: started.");
        mContext =  AccountSettingsActivity.this;

        mViewPager = findViewById(R.id.container);
        mRelativeLayout = findViewById(R.id.relLayout1);

        setupSettingsList();
        setupBottomNavigationView();
        setupFragments();

        //setup the back arrow for navigating back to profile activity
        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to profile activity");
                finish();
                AccountSettingsActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
    }

    private void setupFragments() {
        pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile)); //fragment 0
        pagerAdapter.addFragment(new HelpFragment(), getString(R.string.help)); //fragment 1
        pagerAdapter.addFragment(new SignOutFragment(), getString(R.string.sign_out)); //fragment 2
    }

    private void setViewPager(int fragmentNumber) {
        Log.d(TAG, "setViewPager: navigating to fragment #: " + fragmentNumber);
        if (fragmentNumber == 0) {
            replaceFragment(new EditProfileFragment());
        } else if (fragmentNumber == 1) {
            replaceFragment(new HelpFragment());
        } else if (fragmentNumber == 2) {
            replaceFragment(new SignOutFragment());
        }

    }

    private void setupSettingsList() {
        Log.d(TAG, "setupSettingsList: initializing 'account settings' list");
        ListView listView = findViewById(R.id.lVAccountSettings);

        ArrayList<String> options = new ArrayList<String>();
        options.add(getString(R.string.edit_profile)); //fragment 0
        options.add(getString(R.string.help)); //fragment 1
        options.add(getString(R.string.sign_out)); //fragment 2

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: navigating to fragment #: " + position);
                setViewPager(position);
            }
        });
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.relLayout1, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
