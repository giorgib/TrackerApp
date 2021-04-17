package com.example.trackerapp.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.trackerapp.data.GenericResponse;
import com.example.trackerapp.data.entities.GPSEntity;
import com.example.trackerapp.repository.MainRepository;
import com.example.trackerapp.worker.BulkSaveWorker;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends AndroidViewModel {
    private final WorkManager workManager;
    private final MainRepository repository;
    private final MutableLiveData<GPSEntity> saveGPSRequest;
    private final LiveData<GenericResponse<Void>> saveGPSResponse;

    @Inject
    public MainViewModel(Application application, MainRepository repository) {
        super(application);

        workManager = WorkManager.getInstance(application);
        this.repository = repository;
        saveGPSRequest = new MutableLiveData<>();
        saveGPSResponse = Transformations.switchMap(saveGPSRequest, repository::postGPSEntry);
    }

    public void saveGPS(GPSEntity data) {
        saveGPSRequest.setValue(data);
    }

    public void startWorker() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest request =
                new OneTimeWorkRequest.Builder(BulkSaveWorker.class)
                        .setConstraints(constraints)
                        .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
                        .build();

        workManager.enqueueUniqueWork("BulkSaveWorker", ExistingWorkPolicy.REPLACE, request);
    }

    public LiveData<GenericResponse<Void>> getSaveGPSResponse() {
        return saveGPSResponse;
    }

    public LiveData<List<GPSEntity>> getAllGPSEntities() {
        return repository.getAllGPSEntities();
    }
}
