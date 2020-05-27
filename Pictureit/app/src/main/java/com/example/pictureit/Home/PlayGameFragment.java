package com.example.pictureit.Home;

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

import com.example.pictureit.R;
import com.example.pictureit.Utils.ImageAdapter;

import android.content.Context;

import com.example.pictureit.models.Photo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;

public class PlayGameFragment extends Fragment {

    private static final String TAG = "PlayGameFragment";

    /**
     * This interface will be used for selecting photos from the grid.
     * We need an interface since we want to call it from the Activity.
     */
    public interface OnGridImageSelectedListener {
        void onGridImageSelected(Photo photo, int position, Context context);
    }

    OnGridImageSelectedListener mOnGridImageSelectedListener;

    GridView gridView;
    private static final int NUM_GRID_COLUMNS = 3;


    /**
     * When the user calls the PlayGameFragment by pressing the play button it will be initialized with
     * a gridView and a backArrow
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_game, container, false);
        gridView = (GridView) view.findViewById(R.id.grid_view);
        setupGridView();


        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to home activity");
                backToActivity();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        try {
            mOnGridImageSelectedListener = (OnGridImageSelectedListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException:  " + e.getMessage());
        }
        super.onAttach(context);
    }

    /**
     * Creating the gridView with the items on database.
     */
    private void setupGridView() {
        Log.d(TAG, "setupGridView: Setting up image grid.");

        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * This method observes the changed data and controls the grid accordingly.
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    Photo photo = new Photo();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    try {
                        photo.setTag1(objectMap.get(getString(R.string.field_tag1)).toString());
                        photo.setTag2(objectMap.get(getString(R.string.field_tag2)).toString());
                        photo.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                        photo.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
                        photo.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());
                        photo.setPosition(Integer.parseInt(objectMap.get(getString(R.string.field_position)).toString()));

                        photos.add(photo);
                    } catch (NullPointerException e) {
                        Log.e(TAG, "onDataChange: NullPointerException: " + e.getMessage());
                    }
                }

                //setup our image grid
                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridWidth / NUM_GRID_COLUMNS;
                gridView.setColumnWidth(imageWidth);

                ArrayList<String> imgUrls = new ArrayList<String>();
                for (int i = 0; i < photos.size(); i++) {
                    imgUrls.add(photos.get(i).getImage_path());
                }
                ImageAdapter adapter = new ImageAdapter(getActivity(), R.layout.layout_grid_imageview,
                        "", imgUrls);
                gridView.setAdapter(adapter);

                //Using our interface for OnItemClickListener.
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mOnGridImageSelectedListener.onGridImageSelected(photos.get(position), position, getActivity());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }


    /**
     * Will be used when the back arrow is pressed.
     */
    public void backToActivity() {
        getFragmentManager().popBackStack();
    }
}
