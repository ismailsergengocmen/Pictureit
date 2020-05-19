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
import com.example.pictureit.Utils.BadgeCardRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BadgeFragment extends Fragment {

    private static final String TAG = "BadgeFragment";
    private Context mContext;

    private List<String> mImageUrls;
    private List<String> mTask;
    private List<Integer> mProgress;
    private List<Integer> mIcons;

    private DatabaseReference mRef;
    private int count;

    //Widgets
    private RecyclerView recyclerView;
    private BadgeCardRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_badges, container, false);

        mContext = getContext();
        mImageUrls = new ArrayList<>();
        mTask = new ArrayList<>();
        mProgress = new ArrayList<>();
        mIcons = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference();
        mProgress.add(0,0);
        mProgress.add(1,0);
        mImageUrls.add(0,"");
        mImageUrls.add(1,"");
        mTask.add(0,"");
        mTask.add(1,"");
        mIcons.add(0,0);
        mIcons.add(1,0);

        initImageBitmaps();
        recyclerView = view.findViewById(R.id.badgeRecyclerView);
        adapter = new BadgeCardRecyclerViewAdapter(mContext, mTask, mProgress, mImageUrls, mIcons);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.set(0,"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSNpECKU91DuMBzwzY9hWrILIQ0bbdNNqsHGPweliLYkXZ2VcDD&usqp=CAU");
        mTask.set(0,"Capture 2 unique black item");
        mIcons.set(0, R.drawable.ic_not_ok);

        mImageUrls.set(1,"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTlKnCxpZrKYUIghYEmLbKB-ZaNKQa3zmu4VXiKQkDoiJlFsYw_&usqp=CAU");
        mTask.set(1,"Capture 2 cat");
        mIcons.set(1, R.drawable.ic_not_ok);

        setProgress();
    }

    private void setProgress() {
        DatabaseReference redRef = mRef
                .child(getString(R.string.dbname_tags_and_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("black");
        redRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = 0;
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mProgress.set(0, count);

        DatabaseReference catRef = mRef
                .child(getString(R.string.dbname_tags_and_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("cat");
        catRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = 0;
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mProgress.set(1, count);
    }

}
