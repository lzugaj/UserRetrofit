package com.luv2code.android.userretrofit.service;

import com.luv2code.android.userretrofit.model.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by lzugaj on 6/9/2019
 */

public interface UserService {

    @PUT("/user/{id}.json")
    Call<User> createOrUpdate(@Path("id") String id, @Body User user);

    @GET("/user/{id}.json")
    Call<User> findOne(@Path("id") String id);

    @GET("/user/.json")
    Call<Map<String, User>> findAll();

    @DELETE("/user/{id}.json")
    Call<Void> delete(@Path("id") String id);

}
