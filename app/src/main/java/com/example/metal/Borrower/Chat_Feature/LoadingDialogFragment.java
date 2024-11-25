package com.example.metal.Borrower.Chat_Feature;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class LoadingDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Sending image...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);  // Prevent dialog from being dismissed
        return progressDialog;
    }
}
