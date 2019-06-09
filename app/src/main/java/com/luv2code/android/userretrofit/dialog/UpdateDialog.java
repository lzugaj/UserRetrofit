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
import com.luv2code.android.userretrofit.adapter.UserAdapter;
import com.luv2code.android.userretrofit.connection.RetrofitClient;
import com.luv2code.android.userretrofit.listener.UserActionListener;
import com.luv2code.android.userretrofit.model.User;
import com.luv2code.android.userretrofit.service.UserService;
import com.luv2code.android.userretrofit.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

    private int userPosition;

    private UserService userService;

    private UserAdapter userAdapter;

    private List<User> users;

    public UpdateDialog(User user, int position) {
        this.user = user;
        this.userPosition = position;
        userService = RetrofitClient.getRetrofit().create(UserService.class);
        users = new ArrayList<>();
        userAdapter = new UserAdapter(getActivity(), users, (UserActionListener) this.getActivity());
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

    private void updateUser(User user) {
        if (checkEditTextValue()) {
            userService.createOrUpdate(user.getId(), user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    User newUser = response.body();
                    if (!users.contains(newUser)) {
                        users.add(newUser);
                        userAdapter.notifyItemChanged(userPosition);
                        userAdapter.notifyDataSetChanged();
                    }

                    dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean checkEditTextValue() {
        if (!TextUtils.isEmpty(etFirstNameUD.getText().toString().trim()) && !TextUtils.isEmpty(etLastNameUD.getText().toString().trim())) {
            return true;
        } else {
            Toast.makeText(getContext(), "Please fill every edit field.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
