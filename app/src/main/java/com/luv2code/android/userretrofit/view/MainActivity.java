package com.luv2code.android.userretrofit.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.luv2code.android.userretrofit.R;
import com.luv2code.android.userretrofit.adapter.UserAdapter;
import com.luv2code.android.userretrofit.connection.RetrofitClient;
import com.luv2code.android.userretrofit.dialog.InfoDialog;
import com.luv2code.android.userretrofit.listener.UserActionListener;
import com.luv2code.android.userretrofit.model.User;
import com.luv2code.android.userretrofit.service.UserService;
import com.luv2code.android.userretrofit.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.luv2code.android.userretrofit.utils.AppConstants.INFO_DIALOG;

public class MainActivity extends AppCompatActivity implements UserActionListener {

    @BindView(R.id.etId)
    EditText etId;

    @BindView(R.id.etFirstName)
    EditText etFirstName;

    @BindView(R.id.etLastName)
    EditText etLastName;

    @BindView(R.id.btnInfo)
    Button btnInfo;

    @BindView(R.id.btnCreateUpdate)
    Button btnCreateUpdate;

    @BindView(R.id.btnDelete)
    Button btnDelete;

    @BindView(R.id.rvUsers)
    RecyclerView rvUsers;

    private UserService userService;

    private List<User> users;

    private UserAdapter userAdapter;

    private User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setUpUsers();
    }

    private void init() {
        ButterKnife.bind(this);
        userService = RetrofitClient.getRetrofit().create(UserService.class);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        users = new ArrayList<>();
        sortData();
        userAdapter = new UserAdapter(this, users, this);
        rvUsers.setAdapter(userAdapter);
    }

    private void sortData() {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                if (user1.getLastName().equals(user2.getLastName())) {
                    return user1.getFirstName().compareTo(user2.getFirstName());
                } else {
                    return user1.getLastName().compareTo(user2.getLastName());
                }
            }
        });
    }

    private void setUpUsers() {
        userService.findAll().enqueue(new Callback<Map<String, User>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, User>> call, @NonNull Response<Map<String, User>> response) {
                if (response.body() == null) {
                    return;
                }

                users.clear();
                users.addAll(response.body().values());
                userAdapter.notifyDataSetChanged();
                sortData();
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, User>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btnInfo)
    public void btnInfo() {
        InfoDialog infoDialog = new InfoDialog();
        infoDialog.setCancelable(false);
        infoDialog.show(getSupportFragmentManager(), INFO_DIALOG);
    }

    @OnTextChanged({ R.id.etFirstName, R.id.etLastName })
    public void onTextChanged() {
        btnCreateUpdate.setEnabled(!TextUtils.isEmpty(etFirstName.getText().toString().trim()) &&
                !TextUtils.isEmpty(etLastName.getText().toString().trim()));
        btnDelete.setEnabled(!TextUtils.isEmpty(etId.getText().toString()));
    }

    @OnClick(R.id.btnCreateUpdate)
    public void btnCreateUpdate() {
        User user = selectedUser;
        if (user == null) {
            user = new User(
                    etFirstName.getText().toString().trim(),
                    etLastName.getText().toString().trim()
            );
        } else {
            user.setFirstName(etFirstName.getText().toString().trim());
            user.setLastName(etLastName.getText().toString().trim());
        }

        userService.createOrUpdate(user.getId(), user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                User newUser = response.body();
                if (!users.contains(newUser)) {
                    users.add(newUser);
                }

                userAdapter.notifyDataSetChanged();
                sortData();
                clearForm();
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btnDelete)
    public void btnDelete() {
        userService.delete(selectedUser.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                users.remove(selectedUser);
                userAdapter.notifyDataSetChanged();
                sortData();
                clearForm();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm() {
        etId.getText().clear();
        etFirstName.getText().clear();
        etLastName.getText().clear();
        selectedUser = null;
        Utils.hideKeyboard(this);
    }

    @Override
    public void selectUser(User user) {
        selectedUser = user;
        etId.setText(selectedUser.getId());
        etFirstName.setText(selectedUser.getFirstName());
        etLastName.setText(selectedUser.getLastName());
    }
}
