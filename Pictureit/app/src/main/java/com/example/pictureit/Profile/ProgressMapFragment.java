package com.example.pictureit.Profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pictureit.R;
import com.example.pictureit.Utils.CardRecyclerViewAdapter;
import com.example.pictureit.Utils.StringManipulation;
import com.example.pictureit.models.Photo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProgressMapFragment extends Fragment {

    private static final String TAG = "ProgressMapFragment";

    //Firebase
    private DatabaseReference reference;

    //Variables
    private List<Photo> photoList;
    private Context mContext;

    //Widgets
    private RecyclerView recyclerView;
    private CardRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_map, container, false);

        photoList = new ArrayList<Photo>();
        mContext = getContext();
        reference = FirebaseDatabase.getInstance().getReference();
        setPhotos();
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new CardRecyclerViewAdapter(mContext, photoList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private void setPhotos() {
        //change the node!!
        final StringManipulation stringManipulation = new StringManipulation();

        reference.child(getString(R.string.dbname_all_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        photoList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Photo photo = new Photo();
                            try {
                                photo.setUser_id(ds.child(getActivity().getString(R.string.field_user_id)).getValue().toString());
                                photo.setDate_created(StringManipulation.timeStampConverter(ds.child(getActivity().getString(R.string.field_date_created)).getValue().toString()));
                                photo.setImage_path(ds.child(getActivity().getString(R.string.field_image_path)).getValue().toString());
                                photo.setImage_id(ds.child(getActivity().getString(R.string.field_user_id)).getValue().toString());
                                photo.setTag1(ds.child(getActivity().getString(R.string.field_tag1)).getValue().toString());
                                photo.setTag2(ds.child(getActivity().getString(R.string.field_tag2)).getValue().toString());

                                photoList.add(photo);
                            } catch (NullPointerException e) {
                                Log.d(TAG, "onDataChange: NullPointerException: " + e.getMessage());
                            }
                        }
                        recyclerView.setAdapter(adapter);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        reference.child(Objects.requireNonNull(getActivity()).getString(R.string.dbname_user_photos))
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

}
