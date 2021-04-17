package com.example.trackerapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.trackerapp.concurency.TaskExecutor;
import com.example.trackerapp.data.GenericCallback;
import com.example.trackerapp.data.GenericResponse;
import com.example.trackerapp.data.entities.GPSEntity;
import com.example.trackerapp.data.local.GPSDao;
import com.example.trackerapp.data.remote.gps.GPSDataSource;

import java.util.List;

import javax.inject.Inject;

public class MainRepository {

    private final GPSDataSource dataSource;
    private final GPSDao dao;
    private final AuthRepository authRepository;

    @Inject
    public MainRepository(GPSDataSource dataSource, GPSDao dao, AuthRepository authRepository) {
        this.dataSource = dataSource;
        this.dao = dao;
        this.authRepository = authRepository;
    }

    public LiveData<GenericResponse<Void>> postGPSEntry(GPSEntity data) {
        storeGPSData(data);

        MutableLiveData<GenericResponse<Void>> responseData = new MutableLiveData<>();

        dataSource.postGPSEntry(authRepository.getBearerToken(), data, new GenericCallback<>(responseData, response -> {
            if (response.isSuccessful()) {
                deleteGPSData(data);
            }
        }));

        return responseData;
    }

    public LiveData<GenericResponse<Void>> postGPSEntries(List<GPSEntity> data) {
        storeGPSData(data);

        MutableLiveData<GenericResponse<Void>> responseData = new MutableLiveData<>();

        dataSource.postGPSEntries(authRepository.getBearerToken(), data, new GenericCallback<>(responseData, response -> {
            if (response.isSuccessful()) {
                deleteGPSData(data);
            }
        }));

        return responseData;
    }

    public LiveData<List<GPSEntity>> getAllGPSEntities() {
        return dao.getAllGPSEntitiesLiveData();
    }

    public void storeGPSData(GPSEntity data) {
        TaskExecutor.BOUNDED.execute(() -> dao.insertGPSEntity(data));
    }

    public void storeGPSData(List<GPSEntity> data) {
        TaskExecutor.BOUNDED.execute(() -> dao.insertGPSEntities(data));
    }

    public void deleteGPSData(GPSEntity data) {
        TaskExecutor.BOUNDED.execute(() -> dao.delete(data));
    }

    public void deleteGPSData(List<GPSEntity> data) {
        TaskExecutor.BOUNDED.execute(() -> dao.delete(data));
    }
}
