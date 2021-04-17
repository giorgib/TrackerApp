package com.example.trackerapp.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.trackerapp.data.entities.GPSEntity;

import java.util.List;

@Dao
public interface GPSDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGPSEntities(List<GPSEntity> data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGPSEntity(GPSEntity data);

    @Query("DELETE FROM GPSEntity")
    int clearAll();

    @Delete
    void delete(List<GPSEntity> data);

    @Delete
    void delete(GPSEntity data);

    @Query("SELECT * FROM GPSEntity")
    LiveData<List<GPSEntity>> getAllGPSEntitiesLiveData();

    @Query("SELECT * FROM GPSEntity")
    List<GPSEntity> getAllGPSEntities();
}
