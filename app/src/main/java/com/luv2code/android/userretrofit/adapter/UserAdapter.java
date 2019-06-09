package com.luv2code.android.userretrofit.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luv2code.android.userretrofit.listener.UserActionListener;
import com.luv2code.android.userretrofit.R;
import com.luv2code.android.userretrofit.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzugaj on 6/9/2019
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> users;

    private UserActionListener listener;

    public UserAdapter(List<User> users, UserActionListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int item) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final User user = users.get(position);
        viewHolder.tvUser.setText(user.toString());
        viewHolder.tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectUser(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvUser)
        TextView tvUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
