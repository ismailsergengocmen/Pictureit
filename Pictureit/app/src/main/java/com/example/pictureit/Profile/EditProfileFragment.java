package com.example.pictureit.Profile;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pictureit.R;
import com.example.pictureit.Utils.FirebaseMethods;
import com.example.pictureit.Utils.UniversalImageLoader;
import com.example.pictureit.dialogs.ConfirmPasswordDialog;
import com.example.pictureit.models.User;
import com.example.pictureit.models.UserAccountSettings;
import com.example.pictureit.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    //Widgets
    private EditText mDisplayName, mEmail, mUserName;
    private ImageView mProfilePhoto;
    private TextView mChangeProfilePhoto;

    //Variables
    private UserSettings mUserSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mProfilePhoto = view.findViewById(R.id.profilePhoto);
        mDisplayName = (EditText) view.findViewById(R.id.editTextProfileName);
        mUserName = (EditText) view.findViewById(R.id.editTextUserName);
        mEmail = (EditText) view.findViewById(R.id.editTextEmailAddress);
        mChangeProfilePhoto = (TextView) view.findViewById(R.id.changeProfilePhoto);
        mFirebaseMethods = new FirebaseMethods(getActivity());

        // setProfileImage();
        setupFirebaseAuth();

        //setup the back arrow for navigating back to profile activity
        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to profile activity");
                backToActivity();
            }
        });

        TextView saveChanges = (TextView) view.findViewById(R.id.saveChanges);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attempting to save chenges");
                saveProfileSettings();
            }
        });

        return view;
    }

    // String imgURL = "cdn.webrazzi.com/uploads/2013/06/android-malware.jpg";

    /**
     * Retrieves the data contined in the widgets and submits it to the database
     * Before doing so it checks to make sure the username chosen is unique
     */
    private void saveProfileSettings() {
        final String displayName = mDisplayName.getText().toString();
        final String username = mUserName.getText().toString();
        final String email = mEmail.getText().toString();


        //case1: If the user made a change to their username
        if (!mUserSettings.getUser().getUsername().equals(username)) {

            checkIfUsernameExists(username);
        }
        //case2: If the user made a change to their mail
        if (!mUserSettings.getUser().getEmail().equals(email)) {

            //step 1)Reauthenticate
            //       - Confirm the password and email
            ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
            dialog.show(getFragmentManager(), getString(R.string.confirm_password_dialog));

            //step 2)Check if the email is already registered
            //       - 'fetchProvidersForEmail(String email)'
            //step 3)Change the email
            //       - submit the new email to the database and authentication

        }
    }

    /**
     * Check if @param username already exists in teh database
     *
     * @param username
     */
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.dbname_users)).orderByChild(getString(R.string.field_username)).equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    //add the username
                    mFirebaseMethods.updateUsername(username);
                    Toast.makeText(getActivity(), "saved username.", Toast.LENGTH_SHORT).show();
                }
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.exists()) {
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "That username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setProfileWidgets(UserSettings userSettings) {
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getUser().getEmail());

        mUserSettings = userSettings;
        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();

        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");

        mDisplayName.setText(settings.getDisplay_name());
        mUserName.setText(settings.getUsername());
        mEmail.setText(userSettings.getUser().getEmail());

    }

    //-----------------------------------------Firebase-------------------------------------------------

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    //User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
                } else {
                    //User is signed out
                    Log.d(TAG, "on AuthStateChanged:signed_out");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

                //retrieve images for the user in question
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void backToActivity() {
        getFragmentManager().popBackStack();
    }
}
