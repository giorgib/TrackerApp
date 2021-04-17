package com.example.trackerapp.di;

import android.content.Context;

import com.example.trackerapp.BuildConfig;
import com.example.trackerapp.data.Authenticator;
import com.example.trackerapp.data.local.GPSDao;
import com.example.trackerapp.data.local.GPSDatabase;
import com.example.trackerapp.data.remote.gps.GPSApiService;
import com.example.trackerapp.data.remote.gps.GPSDataSource;
import com.example.trackerapp.repository.AuthRepository;
import com.example.trackerapp.repository.MainRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
public abstract class AppModule {

    @Singleton
    @Provides
    public static Retrofit provideRetrofit(Gson gson, AuthRepository repository) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        builder.authenticator(new Authenticator(repository));

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
    }

    @Provides
    public static Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();
    }

    @Singleton
    @Provides
    public static GPSApiService provideGPSApiService(Retrofit retrofit) {
        return retrofit.create(GPSApiService.class);
    }

    @Singleton
    @Provides
    public static GPSDataSource provideGPSDataSource(GPSApiService apiService) {
        return new GPSDataSource(apiService);
    }

    @Singleton
    @Provides
    public static MainRepository provideMainRepository(AuthRepository authRepository, GPSDataSource dataSource, GPSDao dao) {
        return new MainRepository(dataSource, dao, authRepository);
    }

    @Singleton
    @Provides
    public static GPSDao provideFilesDao(GPSDatabase database) {
        return database.gpsDao();
    }

    @Singleton
    @Provides
    public static GPSDatabase provideFilesDatabase(@ApplicationContext Context context) {
        return GPSDatabase.getGPSDatabase(context);
    }
}
