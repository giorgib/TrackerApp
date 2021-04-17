package com.example.trackerapp.data.remote.auth;

import com.example.trackerapp.model.auth.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthApiService {

    @FormUrlEncoded
    @POST("connect/token")
    Call<AuthResponse> authorize(@Field("client_id") String clientId,
                                 @Field("client_secret") String clientSecret,
                                 @Field("grant_type") String grantType);
}
