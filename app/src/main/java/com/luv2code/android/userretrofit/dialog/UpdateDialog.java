package com.luv2code.android.userretrofit.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.luv2code.android.userretrofit.R;
import com.luv2code.android.userretrofit.connection.RetrofitClient;
import com.luv2code.android.userretrofit.listener.UserActionListener;
import com.luv2code.android.userretrofit.model.User;
import com.luv2code.android.userretrofit.service.UserService;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lzugaj on 6/9/2019
 */

@SuppressLint("ValidFragment")
public class UpdateDialog extends DialogFragment {

    @BindView(R.id.etFirstNameUD)
    EditText etFirstNameUD;

    @BindView(R.id.etLastNameUD)
    EditText etLastNameUD;

    private User user;

    private UserActionListener listener;

    private UserService userService;

    public UpdateDialog(User user, UserActionListener actionListener) {
        this.user = user;
        this.listener = actionListener;
        this.userService = RetrofitClient.getRetrofit().create(UserService.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.update_dialog_title));
        builder.setMessage(getString(R.string.update_dialog_message) + user.toString());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_update, null);
        builder.setView(view);

        ButterKnife.bind(this, view);
        setUpFields();

        builder.setPositiveButton(getString(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUserFirstName = etFirstNameUD.getText().toString();
                String newUserLastName = etLastNameUD.getText().toString();
                user.setFirstName(newUserFirstName);
                user.setLastName(newUserLastName);
                updateUser(user);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

    private void setUpFields() {
        etFirstNameUD.setText(this.user.getFirstName());
        etLastNameUD.setText(this.user.getLastName());
    }

    private void updateUser(final User user) {
        if (areEditTextValuesEmpty()) {
            userService.createOrUpdate(user.getId(), user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    listener.updateUser(user);
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please fill all fields for successfully updating user. Try again...", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean areEditTextValuesEmpty() {
        return !TextUtils.isEmpty(etFirstNameUD.getText().toString().trim()) && !TextUtils.isEmpty(etLastNameUD.getText().toString().trim());
    }
}
