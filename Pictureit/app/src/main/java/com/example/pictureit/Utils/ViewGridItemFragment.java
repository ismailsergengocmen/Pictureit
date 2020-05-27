package com.example.pictureit.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.pictureit.R;
import com.example.pictureit.models.Photo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewGridItemFragment extends Fragment {

    //Constants
    private static final String TAG = "ViewGridItemFragment";
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    //Firebase
    private FirebaseMethods firebaseMethods;
    private StorageReference mStorageReference;
    private DatabaseReference mRef;

    //Variables
    private String currentPhotoPath;
    private String userID;
    private String currentTag1;
    private String currentTag2;
    private int imageCount;
    private Context mContext;

    //Widgets
    private ImageView camera;
    private SquareImageView image;
    private TextView tag1;
    private TextView tag2;
    private ImageView backArrow;
    private Photo mPhoto;
    private int position;

    public ViewGridItemFragment() {
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_griditem, container, false);
        Log.d(TAG, "onCreateView: starting.");

        mContext = getContext();
        firebaseMethods = new FirebaseMethods(mContext);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        position = this.getArguments().getInt("position");

        image = view.findViewById(R.id.task_photo);
        tag1 = view.findViewById(R.id.task_tag1);
        tag2 = view.findViewById(R.id.task_tag2);
        camera = view.findViewById(R.id.capture);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });
        backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to home activity");
                backToActivity();
            }
        });
        return view;
    }

    /**
     * Method for asking camera permission
     */
    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getActivity(), "Camera Permission is Required to Use Camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Method that starts camera intent and saves the captured photo in a file
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d(TAG, "dispatchTakePictureIntent: IOException: " + ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    /**
     * Method for creating a empty file with a unique name
     *
     * @return The unique created file
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Method which is called when photo capture is successful.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                Log.d("tag", "Absolute Url of Image: " + Uri.fromFile(f));

                setCurrentPhoto();
                uploadImageToStorage("photo_" + position, Uri.fromFile(f));
            }
        }
    }

    /**
     * A method to upload images to the firebase storage
     *
     * @param name name of the photo
     * @param uri  uri of the taken photo
     */
    private void uploadImageToStorage(final String name, Uri uri) {

        String newPhotoKey = mRef.child(mContext.getString(R.string.dbname_user_photos)).push().getKey();
        final StorageReference image = mStorageReference.child("images/" + userID + "/" + name);
        image.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                try {
                    Toast.makeText(getActivity(), "Upload succeeded", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    Log.e(TAG, "onCreateView: NullPointerException: " + e.getMessage());
                }
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Url " + uri.toString());
                        firebaseMethods.addPhotoToDatabase(name, uri.toString(), currentTag1, currentTag2);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show();
            }
        });

        final StorageReference all = mStorageReference.child("all_images/" + userID + "/" + newPhotoKey + "/" + name);
        all.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                try {
                    Toast.makeText(getActivity(), "Upload succeeded", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    Log.e(TAG, "onCreateView: NullPointerException: " + e.getMessage());
                }
                all.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            firebaseMethods.addPhotoToDatabase(name, uri.toString(), currentTag1, currentTag2, getActivity().getString(R.string.dbname_all_photos));
                            firebaseMethods.addPhotoToDatabase(name, uri.toString(), currentTag1, currentTag2, getActivity().getString(R.string.dbname_all_photos_and_tags));
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onCreateView: NullPointerException: " + e.getMessage());
                        }
                    }
                });
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageCount = firebaseMethods.getImageCount(dataSnapshot);
                Log.d(TAG, "uploadImageToStorage:image count " + imageCount);
                try {
                    DatabaseReference newRef = mRef.child(getContext().getString(R.string.dbname_user_account_settings))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(getString(R.string.field_posts));
                    newRef.setValue(imageCount);
                } catch (NullPointerException e) {
                    Log.e(TAG, "onCreateView: NullPointerException: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: cancelled.");
            }
        });
    }

    /**
     * This method sets the clicked photo's tags into related variables (currentTag1, currentTag2).
     */
    private void setCurrentPhoto() {
        DatabaseReference smallRef = mRef.child(getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("photo_" + position);
        Log.d(TAG, "setCurrentPhotoPosition: " + position);
        smallRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + dataSnapshot.getValue(Photo.class).getTag1());
                currentTag1 = dataSnapshot.getValue(Photo.class).getTag1();
                currentTag2 = dataSnapshot.getValue(Photo.class).getTag2();
                Log.d("lol", "onDataChange: " + currentTag2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: cancelled.");
            }
        });
    }

    private void init() {
        try {
            mPhoto = getPhotoFromBundle();
            UniversalImageLoader.setImage(getPhotoFromBundle().getImage_path(), image, null, "");
            tag1.setText(mPhoto.getTag1());
            tag2.setText(mPhoto.getTag2());
            String photo_id = mPhoto.getImage_id();

        } catch (NullPointerException e) {
            Log.e(TAG, "onCreateView: NullPointerException: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded()) {
            init();
        }
    }

    /**
     * Retrieve the photo from the incoming bundle from profileActivity interface
     *
     * @return The photo which comes with the Bundle
     */
    private Photo getPhotoFromBundle() {
        Log.d(TAG, "getPhotoFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable("PHOTO");
        } else {
            return null;
        }
    }

    public void backToActivity() {
        getFragmentManager().popBackStack();
    }
}
