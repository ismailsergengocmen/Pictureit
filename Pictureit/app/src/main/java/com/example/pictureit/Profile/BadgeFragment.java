package com.example.pictureit.Profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.List;

public class BadgeFragment extends Fragment {

    private static final String TAG = "BadgeFragment";
    private Context mContext;
    private int count1;
    private int count2;

    private List<String> mImageUrls;
    private List<String> mTask;
    private ArrayList<Integer> mProgress;
    private List<Integer> mStatus;

    private DatabaseReference mRef;
    private int count;

    //Widgets
    private RecyclerView recyclerView;
    private BadgeCardRecyclerViewAdapter adapter;
    private ImageView backArrow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_badges, container, false);

        count1 = 0;
        count2 = 0;
        mContext = getContext();
        mImageUrls =  new ArrayList<String>(Arrays.asList("", "", "", "", ""));
        mTask =  new ArrayList<String>(Arrays.asList("", "", "", "", ""));
        mProgress =  new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0));
        mProgress =  new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0));
        mStatus =   new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0));
        mRef = FirebaseDatabase.getInstance().getReference();
        backArrow = view.findViewById(R.id.backArrow);

        initImageBitmaps();
        recyclerView = view.findViewById(R.id.badgeRecyclerView);
        adapter = new BadgeCardRecyclerViewAdapter(mContext, mTask, mProgress, mImageUrls, mStatus);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to profile activity");
                backToActivity();
            }
        });

        return view;
    }

    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.set(0, "https://cdn3.iconfinder.com/data/icons/survey-feedback-caramel-vol-2/512/TOP_RATED-512.png");
        mTask.set(0, "Capture 2 black item");

        mImageUrls.set(1, "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcR6TVU4Ygfpl8mWsPTdJIzaYydTh0jvsHrro2dAk2fMk4Ffgtk-&usqp=CAU");
        mTask.set(1, "Capture 2 cats");

        mImageUrls.set(2, "https://www.iconpacks.net/icons/1/free-badge-icon-1361-thumb.png");
        mTask.set(2, "Capture 3 yellow items");

        mImageUrls.set(3, "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcS9Re5qg3RRzNBbbo7xFRTFpeeaQgEQOVip780WCmZ6JjDEa1gA&usqp=CAU");
        mTask.set(3, "Capture 10 photos");

        mImageUrls.set(4, "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQ0lY4t0fbV2e4IVskvYjBUjnU3bVzXiOThRA11L_bhJdVrAtz4&usqp=CAU");
        mTask.set(4, "Capture 20 photos");

        setProgress();
    }

    private void setProgress() {
        DatabaseReference newRef = mRef
                .child(getString(R.string.dbname_all_photos_and_tags))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = 0;
                for (DataSnapshot ds : dataSnapshot.child("black").getChildren()) {
                    count++;
                }
                mProgress.set(0, count);
                if (count >= 2) {
                    mStatus.set(0, 1);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = 0;
                for (DataSnapshot ds : dataSnapshot.child("cat").getChildren()) {
                    count++;
                }
                mProgress.set(1, count);
                if (count >= 2) {
                    mStatus.set(1, 1);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = 0;
                for (DataSnapshot ds : dataSnapshot.child("yellow").getChildren()) {
                    count++;
                }
                mProgress.set(2, count);
                if (count >= 3) {
                    mStatus.set(2, 1);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = 0;
                for (DataSnapshot ds : dataSnapshot
                        .child(getContext().getString(R.string.dbname_all_photos))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .getChildren()) {
                    count++;
                }
                mProgress.set(3, count);
                if (count >= 10) {
                    mStatus.set(3, 1);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = 0;
                for (DataSnapshot ds : dataSnapshot
                        .child(getString(R.string.dbname_all_photos))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .getChildren()) {
                    count++;
                }
                mProgress.set(4, count);
                if (count >= 20) {
                    mStatus.set(4, 1);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void backToActivity() {
        getFragmentManager().popBackStack();
    }
}
