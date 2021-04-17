package com.example.trackerapp.di;

import android.content.Context;

import com.example.trackerapp.BuildConfig;
import com.example.trackerapp.annotationclasses.AuthAnnotationClass;
import com.example.trackerapp.data.remote.auth.AuthApiService;
import com.example.trackerapp.data.remote.auth.AuthDataSource;
import com.example.trackerapp.repository.AuthRepository;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public abstract class AuthModule {
    @Singleton
    @Provides
    @AuthAnnotationClass
    public static Retrofit provideAuthRetrofit(Gson gson) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
    }

    @Singleton
    @Provides
    public static AuthApiService provideAuthApiService(@AuthAnnotationClass Retrofit retrofit) {
        return retrofit.create(AuthApiService.class);
    }

    @Singleton
    @Provides
    public static AuthDataSource provideAuthDataSource(AuthApiService apiService) {
        return new AuthDataSource(apiService);
    }

    @Singleton
    @Provides
    public static AuthRepository provideAuthRepository(@ApplicationContext Context context, AuthDataSource dataSource) {
        return new AuthRepository(context, dataSource);
    }
}
