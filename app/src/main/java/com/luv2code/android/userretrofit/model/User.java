package com.luv2code.android.userretrofit.model;

import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by lzugaj on 6/9/2019
 */

public class User {

    private String id;

    private String firstName;

    private String lastName;

    public User(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String firstName, String lastName) {
        this(UUID.randomUUID().toString(), firstName, lastName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s %s", firstName, lastName);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && id.equals(((User) obj).getId());
    }
}
