package com.example.trackerapp.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.trackerapp.data.entities.GPSEntity;
import com.example.trackerapp.data.local.GPSDao;
import com.example.trackerapp.data.remote.gps.GPSApiService;
import com.example.trackerapp.repository.AuthRepository;

import java.io.IOException;
import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import retrofit2.Call;
import retrofit2.Response;

@HiltWorker
public class BulkSaveWorker extends Worker {
    private final GPSApiService apiService;
    private final GPSDao dao;
    private final AuthRepository authRepository;

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    @AssistedInject
    public BulkSaveWorker(
            @Assisted @NonNull Context appContext,
            @Assisted @NonNull WorkerParameters workerParams,
            GPSApiService apiService,
            GPSDao dao,
            AuthRepository authRepository) {
        super(appContext, workerParams);

        this.apiService = apiService;
        this.dao = dao;
        this.authRepository = authRepository;
    }

    @NonNull
    @Override
    public Result doWork() {
        List<GPSEntity> data = dao.getAllGPSEntities();

        if (data.isEmpty())
            return Result.success();

        Call<Void> call = apiService.postGPSEntries(authRepository.getBearerToken(), data);

        try {
            Response<Void> response = call.execute();
            if (response.code() != 200)
                return Result.retry();

            dao.delete(data);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.retry();
        }

        return Result.success();
    }
}
