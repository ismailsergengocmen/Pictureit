package com.example.pictureit.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.pictureit.R;
import com.example.pictureit.models.Photo;
import com.example.pictureit.models.User;
import com.example.pictureit.models.UserAccountSettings;
import com.example.pictureit.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

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
        addPhotoToDatabase("photo_3", "https://firebasestorage.googleapis.com/v0/b/pictureit-7d068.appspot.com/o/EasyGamePhotos%2F02903af6a22cd87679bb384e806dc2cb.png?alt=media&token=76061755-dee1-4c71-99c8-0b6878bee97f", "Yellow", "Flower");
        addPhotoToDatabase("photo_4", "https://firebasestorage.googleapis.com/v0/b/pictureit-7d068.appspot.com/o/EasyGamePhotos%2F1f1458057fdf5f190f71f0e7667fa393.jpg?alt=media&token=7c66c17f-b106-4f2c-a4a5-a61b5a72182a", "Red", "Flower");
        addPhotoToDatabase("photo_5", "https://firebasestorage.googleapis.com/v0/b/pictureit-7d068.appspot.com/o/EasyGamePhotos%2F78b49e23522b6ad462f1ae94eaba1adb.png?alt=media&token=17ed21a6-f42e-450c-b744-8e230f0bf0e5", "White", "Dog");
        addPhotoToDatabase("photo_6", "https://firebasestorage.googleapis.com/v0/b/pictureit-7d068.appspot.com/o/EasyGamePhotos%2F91002391-car-icon-vector-flat-simple-cartoon-transportation-symbol-isolated-on-white-side-view-.jpg?alt=media&token=ae401717-4c18-4b1e-92c1-c25a6e0a082c", "Black", "Car");
        addPhotoToDatabase("photo_7", "https://firebasestorage.googleapis.com/v0/b/pictureit-7d068.appspot.com/o/EasyGamePhotos%2Fd00428efa0bf27b9edd37eac32dfd2c1.png?alt=media&token=79329c25-8ca1-47c2-a1e5-44653cf89a9e", "Green", "Tree");
        addPhotoToDatabase("photo_8", "https://firebasestorage.googleapis.com/v0/b/pictureit-7d068.appspot.com/o/EasyGamePhotos%2Fdownload%20(1).png?alt=media&token=7d338f79-f95f-4888-85ec-68525e2ef42d", "Yellow", "Cat");
        addPhotoToDatabase("photo_9", "https://firebasestorage.googleapis.com/v0/b/pictureit-7d068.appspot.com/o/EasyGamePhotos%2Fdownload.png?alt=media&token=5ae934de-a9c6-41de-94cb-a6bd778919b1", "Brown", "Bird");
        addPhotoToDatabase("photo_10", "https://firebasestorage.googleapis.com/v0/b/pictureit-7d068.appspot.com/o/EasyGamePhotos%2Fimages.png?alt=media&token=047b9d9f-9501-44fc-96e2-0506fe0328c7", "Orange", "Cone");
        addPhotoToDatabase("photo_11", "https://firebasestorage.googleapis.com/v0/b/pictureit-7d068.appspot.com/o/EasyGamePhotos%2Funnamed.jpg?alt=media&token=a94a7302-6720-48d4-a37c-c913a70e9227", "White", "Cloud");
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
     *  Uploads images to firebase database
     * @param url url of the image
     * @param node node of the database that you want to upload the photo
     */

    public void addPhotoToDatabase(String url, String node) {
        Log.d(TAG, "addPhotoToDatabase: adding photo to database.");

        //insert into database
        myRef.child(node)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("profile_photo").setValue(url);
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

        if (node == mContext.getString(R.string.dbname_all_photos)) {
            //insert into database's all_photos node
            myRef.child(node)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(newPhotoKey).setValue(photo);

        } else if (node == mContext.getString(R.string.dbname_all_photos_and_tags)) {
            //insert into database's all_photos_and_tags node(for tag1)
            myRef.child(node)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(tag1.toLowerCase())
                    .child(newPhotoKey)
                    .setValue(photo);

            //insert into database's all_photos_and_tags node(for tag2)
            myRef.child(node)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(tag2.toLowerCase())
                    .child(newPhotoKey)
                    .setValue(photo);
        }
    }

    public int getImageCount(DataSnapshot dataSnapshot) {
        int count = 0;
        for (DataSnapshot ds : dataSnapshot
                .child(mContext.getString(R.string.dbname_all_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .getChildren()) {
            count++;
        }
        return count;
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
