package com.example.pictureit.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pictureit.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmPasswordDialog extends DialogFragment {

    private static final String TAG = "ConfirmPasswordDialog";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_password,container,false);
        Log.d(TAG,"onCreateView: started.");
        return view;
    }
}
