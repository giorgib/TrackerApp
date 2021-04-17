package com.example.trackerapp.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.trackerapp.data.entities.GPSEntity;

@Database(entities = {GPSEntity.class}, version = 1, exportSchema = false)
public abstract class GPSDatabase extends RoomDatabase {
    public abstract GPSDao gpsDao();

    public static volatile GPSDatabase instance;

    public static GPSDatabase getGPSDatabase(final Context context) {
        if (instance == null) {
            synchronized (GPSDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            GPSDatabase.class, "gps_database").
                            build();
                }
            }
        }

        return instance;
    }

}
