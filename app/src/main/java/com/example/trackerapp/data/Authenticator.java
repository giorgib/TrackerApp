package com.example.trackerapp.data;

import com.example.trackerapp.model.auth.AuthResponse;
import com.example.trackerapp.repository.AuthRepository;

import javax.annotation.Nullable;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class Authenticator implements okhttp3.Authenticator {

    private final AuthRepository repository;

    public Authenticator(AuthRepository repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) {
        try {
            AuthResponse authResponse = repository.authenticate();
            repository.setToken(authResponse.getAccessToken());

            return response.request().newBuilder()
                    .header("Authorization", repository.getBearerToken())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}
