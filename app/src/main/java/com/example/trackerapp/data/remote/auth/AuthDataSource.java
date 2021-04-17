package com.example.trackerapp.data.remote.auth;

import com.example.trackerapp.BuildConfig;
import com.example.trackerapp.model.auth.AuthResponse;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

public class AuthDataSource {
    private final AuthApiService apiService;

    @Inject
    public AuthDataSource(AuthApiService apiService) {
        this.apiService = apiService;
    }

    public AuthResponse authenticate() {
        Call<AuthResponse> call = apiService.authorize(BuildConfig.clientId, BuildConfig.clientSecret, BuildConfig.grantType);
        try {
            Response<AuthResponse> response = call.execute();
            if (response.code() != 200)
                return null;
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
