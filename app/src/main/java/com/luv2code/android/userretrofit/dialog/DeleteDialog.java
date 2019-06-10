package com.luv2code.android.userretrofit.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.luv2code.android.userretrofit.R;
import com.luv2code.android.userretrofit.connection.RetrofitClient;
import com.luv2code.android.userretrofit.listener.UserActionListener;
import com.luv2code.android.userretrofit.model.User;
import com.luv2code.android.userretrofit.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lzugaj on 6/9/2019
 */

@SuppressLint("ValidFragment")
public class DeleteDialog extends DialogFragment {

    private User user;

    private int userPosition;

    private UserActionListener listener;

    private UserService userService;

    public DeleteDialog(User user, int position, UserActionListener actionListener) {
        this.user = user;
        this.userPosition = position;
        this.listener = actionListener;
        this.userService = RetrofitClient.getRetrofit().create(UserService.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.delete_dialog_title));
        builder.setMessage(getString(R.string.delete_dialog_message) + user.toString());
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser();
            }
        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

    private void deleteUser() {
        userService.delete(user.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                listener.deleteUser(userPosition);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
