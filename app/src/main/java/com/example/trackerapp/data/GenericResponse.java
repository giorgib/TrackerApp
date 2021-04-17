package com.example.trackerapp.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class GenericResponse<T> {
    @NonNull
    private final Status status;
    @Nullable
    private final T data;
    @Nullable
    private final ErrorResponse error;


    private GenericResponse(@NonNull Status status, @Nullable T data, @Nullable ErrorResponse error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> GenericResponse<T> success(@Nullable T data, @NonNull String successMessage) {
        return new GenericResponse<>(GenericResponse.Status.SUCCESS, data, null);
    }


    public static <T> GenericResponse<T> error(@NonNull ErrorResponse serverError) {
        return new GenericResponse<>(Status.ERROR, null, serverError);
    }

    public static <T> GenericResponse<T> loading() {
        return new GenericResponse<>(Status.LOADING, null, null);
    }


    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }


    @Nullable
    public ErrorResponse getError() {
        return error;
    }

    public enum Status {SUCCESS, ERROR, LOADING}

}
