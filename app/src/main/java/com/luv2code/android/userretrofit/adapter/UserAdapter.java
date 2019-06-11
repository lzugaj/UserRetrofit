package com.luv2code.android.userretrofit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luv2code.android.userretrofit.R;
import com.luv2code.android.userretrofit.connection.RetrofitClient;
import com.luv2code.android.userretrofit.dialog.DeleteDialog;
import com.luv2code.android.userretrofit.dialog.UpdateDialog;
import com.luv2code.android.userretrofit.listener.UserActionListener;
import com.luv2code.android.userretrofit.model.User;
import com.luv2code.android.userretrofit.service.UserService;
import com.luv2code.android.userretrofit.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.luv2code.android.userretrofit.utils.AppConstants.DELETE_DIALOG;
import static com.luv2code.android.userretrofit.utils.AppConstants.UPDATE_DIALOG;

/**
 * Created by lzugaj on 6/9/2019
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;

    private List<User> users;

    private UserActionListener listener;

    private UserService userService;

    public UserAdapter(Context context, List<User> users, UserActionListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
        userService = RetrofitClient.getRetrofit().create(UserService.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int item) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        final User user = users.get(position);
        viewHolder.tvUser.setText(user.toString());
        viewHolder.tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectUser(user);
            }
        });

        viewHolder.ivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                UpdateDialog updateDialog = new UpdateDialog(user, listener);
                updateDialog.show(fragmentManager, UPDATE_DIALOG);
                updateDialog.setCancelable(false);
            }
        });

        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                DeleteDialog deleteDialog = new DeleteDialog(user, position, listener);
                deleteDialog.show(fragmentManager, DELETE_DIALOG);
                deleteDialog.setCancelable(false);
            }
        });

        viewHolder.tvUser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                userService.delete(user.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        users.remove(user);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void searchUserList(List<User> searchedUsers) {
        users = new ArrayList<>();
        users.addAll(searchedUsers);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvUser)
        TextView tvUser;

        @BindView(R.id.ivDelete)
        ImageView ivDelete;

        @BindView(R.id.ivUpdate)
        ImageView ivUpdate;

        private ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
