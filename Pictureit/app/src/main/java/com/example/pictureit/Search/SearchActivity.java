package com.example.pictureit.Search;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.pictureit.Utils.ViewGridItemFragment;
import com.example.pictureit.R;
import com.example.pictureit.Utils.BottomNavigationViewHelper;
import com.example.pictureit.Utils.ImageAdapter;
import com.example.pictureit.Utils.StringManipulation;
import com.example.pictureit.models.Photo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity  {

    private static final String TAG = "SearchActivity";

    //Constant
    private static final int ACTIVITY_NUMBER = 1;
    private static final int NUM_GRID_COLUMNS = 3;
    private final Context mContext = SearchActivity.this;

    //Widgets
    private EditText mSearchBar;
    private GridView mGridView;

    //Variables
    private List<Photo> photoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "onCreate: started.");

        mSearchBar = findViewById(R.id.searchbar);
        mGridView = findViewById(R.id.search_gridview);

        initTextListener();
        setupBottomNavigationView();
    }

    private void initTextListener() {
        Log.d(TAG, "initTextListener: initializing.");
        photoList = new ArrayList<Photo>();

        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = mSearchBar.getText().toString().toLowerCase();
                searchForMatch(text);
            }
        });
    }

    private void searchForMatch(final String keyword) {
        Log.d(TAG, "searchForMatch: searching for string: " + keyword);
        photoList.clear();
        if (keyword.length() == 0) {

        } else {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            reference.child(getString(R.string.dbname_tags_and_photos))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(keyword).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    photoList.clear();
                    final StringManipulation stringManipulation;
                    stringManipulation = new StringManipulation();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Photo photo = new Photo();
                        photo.setUser_id(ds.child(getString(R.string.field_user_id)).getValue().toString());
                        photo.setDate_created(stringManipulation.timeStampConverter(ds.child(getString(R.string.field_date_created)).getValue().toString()));
                        photo.setImage_path(ds.child(getString(R.string.field_image_path)).getValue().toString());
                        photo.setImage_id(ds.child(getString(R.string.field_user_id)).getValue().toString());
                        photo.setTag1(ds.child(getString(R.string.field_tag1)).getValue().toString());
                        photo.setTag2(ds.child(getString(R.string.field_tag2)).getValue().toString());

                        photoList.add(photo);
                        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                gridFragmentInit(photoList.get(position));
                            }
                        });
                    }

                    //setup our image grid
                    int gridWidth = getResources().getDisplayMetrics().widthPixels;
                    int imageWidth = gridWidth / NUM_GRID_COLUMNS;
                    mGridView.setColumnWidth(imageWidth);

                    ArrayList<String> imgUrls = new ArrayList<String>();
                    for (int i = 0; i < photoList.size(); i++) {
                        imgUrls.add(photoList.get(i).getImage_path());
                    }
                    ImageAdapter adapter = new ImageAdapter(mContext, R.layout.layout_grid_imageview,
                            "", imgUrls);
                    mGridView.setAdapter(adapter);

                    reference.child(getString(R.string.dbname_tags_and_photos))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(keyword).removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: cancelled.");
                }
            });

        }

    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }

    private void gridFragmentInit(Photo photo) {
        ViewGridItemFragment fragment = new ViewGridItemFragment();
        Bundle args = new Bundle();
        args.putParcelable("PHOTO", photo);
        args.putString("tag1", photo.getTag1());
        args.putString("tag2", photo.getTag2());
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
