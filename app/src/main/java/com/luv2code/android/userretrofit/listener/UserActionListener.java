package com.luv2code.android.userretrofit.listener;

import com.luv2code.android.userretrofit.model.User;

/**
 * Created by lzugaj on 6/9/2019
 */

public interface UserActionListener {

    void selectUser(User user);

    void deleteUser(int userPosition);

    void updateUser(User user);

}
