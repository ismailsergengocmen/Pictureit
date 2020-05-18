package com.example.pictureit.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.pictureit.R;
import com.example.pictureit.models.Photo;
import com.example.pictureit.models.User;
import com.example.pictureit.models.UserAccountSettings;
import com.example.pictureit.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FirebaseMethods {

    //Constants
    private static final String TAG = "FirebaseMethods";

    //Variables
    private Context mContext;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private StorageReference mStorageReference;
    private String userID;


    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public void updateDisplayName(String displayName) {
        if (displayName != null) {
            Log.d(TAG, "updating display name to:" + displayName);
            myRef.child(mContext.getString(R.string.dbname_user_account_settings)).child(userID).child(mContext.getString(R.string.field_display_name)).setValue(displayName);
        }
    }

    /**
     * update username in the 'users' node and 'user_account_settings' node
     *
     * @param username
     */
    public void updateUsername(String username) {
        if (username != null) {
            Log.d(TAG, "updateUsername: updating username to: " + username);
            myRef.child(mContext.getString(R.string.dbname_users)).child(userID).child(mContext.getString(R.string.field_username)).setValue(username);
            myRef.child(mContext.getString(R.string.dbname_user_account_settings)).child(userID).child(mContext.getString(R.string.field_username)).setValue(username);
        }
        if (username == null) {
            Log.d(TAG, "please enter username");
        }
    }

    /**
     * update the email in the 'users' node
     *
     * @param email
     */
    public void updateEmail(String email) {
        Log.d(TAG, "updateUsername: updating email to: " + email);
        myRef.child(mContext.getString(R.string.dbname_users)).child(userID).child(mContext.getString(R.string.field_email)).setValue(email);
    }

    /**
     * Register a new email and password to Firebase Authentication
     *
     * @param email    user email
     * @param password user password
     * @param username user username
     */
    public void registerNewEmail(final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "CreateUserWithEmail:onComplete:" + task.isSuccessful());

                        //If sign in fails, display a massage to the user.If sign in succeeds the auth
                        //state listener will be notified and logic to handle the signed in user can
                        //be handled in the listener
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else if (task.isSuccessful()) {
                            //send verification mail
                            sendVerificationEmail();
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed:" + userID);
                        }
                    }
                });
    }

    public void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                    } else {
                        Toast.makeText(mContext, "couldn't send verification email.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * Add information to the users nodes
     * Add information to the user_account_setting node
     *
     * @param email         user email
     * @param username      user username
     * @param profile_photo user profile photo
     */
    public void addNewUser(String email, String username, String profile_photo) {

        User user = new User(userID, email, StringManipulation.condenseUsername(username));

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);

        UserAccountSettings settings = new UserAccountSettings(
                username,
                0,
                profile_photo,
                StringManipulation.condenseUsername(username)

        );
        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);
        addPhotoToDatabase("photo_0", "https://firebasestorage.googleapis.com/v0/b/pictureit-7d068.appspot.com/o/EasyGamePhotos%2Fphoto0.png?alt=media&token=7bad77d7-7ca4-4aa4-bd53-0a03feca2a13", "Black", "Cat");
        addPhotoToDatabase("photo_1", "https://firebasestorage.googleapis.com/v0/b/pictureit-7d068.appspot.com/o/EasyGamePhotos%2Fphoto1.png?alt=media&token=e4e9d72c-798a-481b-93aa-ffd29dd84861", "Yellow", "Dog");
        addPhotoToDatabase("photo_2", "https://firebasestorage.googleapis.com/v0/b/pictureit-7d068.appspot.com/o/EasyGamePhotos%2Fphoto2.png?alt=media&token=b45637ac-7a43-40a8-b38f-10fd9a562f3f", "Orange", "Fish");
    }


    /**
     * Retrieves the account settings for tech user currently logged in
     * Database: user_account_settings node
     *
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase");

        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            //user_account_settings node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserAccountSettings: datasnapshot:" + ds);

                try {
                    settings.setDisplay_name(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );
                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );

                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information:" + settings.toString());
                } catch (NullPointerException e) {
                    Log.e(TAG, " getUserAccountSettings : NullPointerException: " + e.getMessage());
                }

            }
            //users node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountSettings: datasnapshot:" + ds);

                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()
                );
                Log.d(TAG, "getUserAccountSettings : retrieved users information : " + user.toString());
            }
        }
        return new UserSettings(user, settings);
    }

    /**
     * A method for adding photo to "user photos" node of the firebase database
     *
     * @param url this is the download url of the photo. It shows the location of the photo in the firebase storage
     */
    public void addPhotoToDatabase(String url) {
        Log.d(TAG, "addPhotoToDatabase: adding photo to database.");

        String newPhotoKey = myRef.child(mContext.getString(R.string.dbname_user_photos)).push().getKey();
        Photo photo = new Photo();
        photo.setImage_path(url);
        photo.setDate_created(getTimestamp());
        photo.setTag1("");
        photo.setTag2("");
        photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        photo.setImage_id(newPhotoKey);

        //insert into database
        myRef.child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(newPhotoKey).setValue(photo);
    }

    /**
     * A method for adding photo to "user photos" , "tags and photos" nodes of the firebase database
     *
     * @param url  this is the download url of the photo. It shows the location of the photo in the firebase storage
     * @param tag1 tag1 of the photo
     * @param tag2 tag2 of the photo
     */
    public void addPhotoToDatabase(String image_id, String url, String tag1, String tag2) {
        Log.d(TAG, "addPhotoToDatabase: adding photo to database.");

        String newPhotoKey = myRef.child(mContext.getString(R.string.dbname_user_photos)).push().getKey();
        Photo photo = new Photo();
        photo.setImage_path(url);
        photo.setDate_created(getTimestamp());
        photo.setTag1(tag1);
        photo.setTag2(tag2);
        photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        photo.setImage_id(image_id);

        //insert into database's user_photos node
        myRef.child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(image_id).setValue(photo);

//        //insert into database's all_photos node
//        myRef.child(mContext.getString(R.string.dbname_all_photos))
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child(newPhotoKey).setValue(photo);

        //adds the photo to the tags_and_name node (under tag1 branch)
        myRef.child(mContext.getString(R.string.dbname_tags_and_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(tag1.toLowerCase())
                .child(image_id).setValue(photo);

        //adds the photo to the tags_and_name node (under tag2 branch)
        myRef.child(mContext.getString(R.string.dbname_tags_and_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(tag2.toLowerCase())
                .child(image_id).setValue(photo);
    }

    //Please do not use this method for uploading image!! This is only for all_photos node
    public void addPhotoToDatabase(String image_id, String url, String tag1, String tag2, String node) {
        Log.d(TAG, "addPhotoToDatabase: adding photo to database.");

        String newPhotoKey = myRef.child(mContext.getString(R.string.dbname_all_photos)).push().getKey();
        Photo photo = new Photo();
        photo.setImage_path(url);
        photo.setDate_created(getTimestamp());
        photo.setTag1(tag1);
        photo.setTag2(tag2);
        photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        photo.setImage_id(image_id);

        //insert into database's all_photos node
        myRef.child(node)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(newPhotoKey).setValue(photo);
    }

    /**
     * A method for producing String which is the representation of current date and time
     *
     * @return the current time and date
     */
    public String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
        return sdf.format(new Date());
    }
}
