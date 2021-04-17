package com.example.trackerapp.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class GPSEntity {
    @PrimaryKey
    @NonNull
    @SerializedName("createdDateTime")
    @Expose
    private String createdDateTime;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public GPSEntity(@NonNull String createdDateTime, Double latitude, Double longitude) {
        this.createdDateTime = createdDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(@NonNull String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
