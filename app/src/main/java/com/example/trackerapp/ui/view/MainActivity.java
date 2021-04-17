package com.example.trackerapp.ui.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.trackerapp.data.GenericResponse;
import com.example.trackerapp.data.entities.GPSEntity;
import com.example.trackerapp.databinding.ActivityMainBinding;
import com.example.trackerapp.helper.Utils;
import com.example.trackerapp.ui.viewmodel.MainViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        createLocationRequest();
        setupLocationCallback();
        setupSwitch();
        setupObservers();
    }

    private void setupSwitch() {
        binding.switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startLocationUpdates();
                binding.textView.setText("Location updates enabled");
            } else {
                stopLocationUpdates();
                viewModel.startWorker();

                binding.textView.setText("Location updates disabled");
            }
        });
    }

    private void setupObservers() {
        viewModel.getSaveGPSResponse().observe(this, response -> {
            if (response.getStatus() == GenericResponse.Status.SUCCESS) {
                Toast.makeText(MainActivity.this, "Data successfully uploaded", Toast.LENGTH_LONG).show();
            } else if (response.getStatus() == GenericResponse.Status.ERROR) {
                Toast.makeText(MainActivity.this, "Error while uploading data", Toast.LENGTH_LONG).show();
                viewModel.startWorker();
            }
        });
        viewModel.getAllGPSEntities().observe(this, new Observer<List<GPSEntity>>() {
            @Override
            public void onChanged(List<GPSEntity> gpsEntities) {
                if (!gpsEntities.isEmpty())
                    viewModel.startWorker();

                viewModel.getAllGPSEntities().removeObserver(this);
            }
        });
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(10000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for (Location location : locationResult.getLocations()) {
                    viewModel.saveGPS(new GPSEntity(Utils.formatDate(new Date()), location.getLatitude(), location.getLongitude()));
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        startLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (hasPermissions()) {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }
}