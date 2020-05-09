package com.example.pictureit.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.pictureit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    private Context mContext;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;


    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * Register a new email and password to Firebase Authentication
     *
     * @param email
     * @param password
     * @param Username
     */
    public void registerNewEmail(final String email, String password, final String Username) {
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
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onConplete: Authstate changed:" + userID);
                        }
                    }
                });
    }
}