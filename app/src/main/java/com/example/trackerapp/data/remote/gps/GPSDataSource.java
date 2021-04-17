package com.example.trackerapp.data.remote.gps;

import com.example.trackerapp.data.entities.GPSEntity;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Callback;

public class GPSDataSource {

    private final GPSApiService apiService;

    @Inject
    public GPSDataSource(GPSApiService apiService) {
        this.apiService = apiService;
    }

    public void postGPSEntry(String token, GPSEntity data, Callback<Void> callback) {
        apiService.postGPSEntry(token, data).enqueue(callback);
    }

    public void postGPSEntries(String token, List<GPSEntity> data, Callback<Void> callback) {
        apiService.postGPSEntries(token, data).enqueue(callback);
    }
}
