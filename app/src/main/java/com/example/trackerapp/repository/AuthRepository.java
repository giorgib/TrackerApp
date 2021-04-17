package com.example.trackerapp.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.trackerapp.data.remote.auth.AuthDataSource;
import com.example.trackerapp.helper.PrefKeys;
import com.example.trackerapp.model.auth.AuthResponse;

import javax.inject.Inject;

public class AuthRepository {
    private final AuthDataSource dataSource;
    private final SharedPreferences sharedPreferences;

    @Inject
    public AuthRepository(Context context, AuthDataSource dataSource) {
        this.dataSource = dataSource;
        sharedPreferences = context.getSharedPreferences(PrefKeys.AUTH_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public String getToken() {
        return sharedPreferences.getString(PrefKeys.TOKEN, "");
    }

    public String getBearerToken() {
        return "Bearer " + getToken();
    }

    public void setToken(String token) {
        sharedPreferences.edit().putString(PrefKeys.TOKEN, token).apply();
    }

    public AuthResponse authenticate() {
        return dataSource.authenticate();
    }
}
