package com.example.pictureit.Login;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pictureit.R;
import com.example.pictureit.Utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private String email,username,password;
    private EditText mEmail,mPassword,mUsername;
    private TextView loadingPleaseWait;
    private Button btnRegister;
    private ProgressBar mProgressBar;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseMethods = new FirebaseMethods(mContext);
        mContext=RegisterActivity.this;
        Log.d(TAG, "onCreate: started.");

        initWidgets();
        setupFirebaseAuth();
        init();
    }

    private  void init(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();

                if (checkInputs(email, username, password)) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    loadingPleaseWait.setVisibility(View.VISIBLE);

                    firebaseMethods.registerNewEmail(email,password,username);
                }
            }
        });
    }

    private boolean checkInputs(String email, String username, String password){
            Log.d(TAG,"checkInputs: checking inputs for null values.");
            if(email.equals("") || username.equals("") || password.equals("")){
                Toast.makeText(mContext,"All fields must be filled out",Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }

    /**
     *  Initialize the activity widgets
     */
    private void initWidgets(){
        Log.d(TAG, "initWidgets: Initializing Widgets.");
        mEmail = (EditText) findViewById(R.id.input_email);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mUsername = (EditText) findViewById(R.id.input_username);
        btnRegister=(Button) findViewById(R.id.btn_register);
        loadingPleaseWait = (TextView) findViewById(R.id.loadingPleaseWait);
        mPassword= (EditText) findViewById(R.id.input_password);
        mContext= RegisterActivity.this;
        mProgressBar.setVisibility(View.GONE);
        loadingPleaseWait.setVisibility(View.GONE);
    }

    private boolean isStringNull(String string){
        Log.d(TAG,"isStringNull: checking string if null.");

        if (string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

    //-----------------------------------------Firebase-------------------------------------------------

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
