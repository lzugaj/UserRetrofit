package com.luv2code.android.userretrofit.connection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.luv2code.android.userretrofit.utils.AppConstants.BASE_URL;

/**
 * Created by lzugaj on 6/9/2019
 */

public final class RetrofitClient {

    private static Retrofit retrofit;

    private RetrofitClient() {
        // private default constructor
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            init();
        }

        return retrofit;
    }

    private static void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
