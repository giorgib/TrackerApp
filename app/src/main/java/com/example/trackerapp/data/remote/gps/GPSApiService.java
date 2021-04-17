package com.example.trackerapp.data.remote.gps;

import com.example.trackerapp.data.entities.GPSEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GPSApiService {
    @POST("api/GPSEntries")
    Call<Void> postGPSEntry(@Header("Authorization") String authorization, @Body GPSEntity gpsEntity);

    @POST("api/GPSEntries/bulk")
    Call<Void> postGPSEntries(@Header("Authorization") String authorization, @Body List<GPSEntity> gpsEntities);


}
