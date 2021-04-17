package com.example.trackerapp.data;

import androidx.lifecycle.MutableLiveData;

import com.example.trackerapp.result.ResultCode;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenericCallback<T> implements Callback<T> {
    private static final int UNAUTHORIZED = 401;
    private final MutableLiveData<GenericResponse<T>> liveData;

    private Interceptor<T> interceptor;

    public interface Interceptor<T> {
        void intercept(Response<T> response);
    }

    public GenericCallback(MutableLiveData<GenericResponse<T>> liveData) {
        this.liveData = liveData;
        liveData.setValue(GenericResponse.loading());
    }

    public GenericCallback(MutableLiveData<GenericResponse<T>> liveData, Interceptor<T> interceptor) {
        this.liveData = liveData;
        this.interceptor = interceptor;

        liveData.setValue(GenericResponse.loading());
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (interceptor != null)
            interceptor.intercept(response);

        if (response.isSuccessful()) {
            liveData.setValue(GenericResponse.success(response.body(), response.message()));
        } else {
            ErrorResponse errorResponse = errorBodyParse(response.errorBody());
            if (errorResponse == null)
                errorResponse = new ErrorResponse(ResultCode.Unknown, "");
            liveData.setValue(GenericResponse.error(errorResponse));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof UnknownHostException || t instanceof SocketTimeoutException) {
            liveData.setValue(GenericResponse.error(new ErrorResponse(ResultCode.NetworkError, t.getMessage())));
        } else
            liveData.setValue(GenericResponse.error(new ErrorResponse(ResultCode.Unknown, t.getMessage())));
    }

    private ErrorResponse errorBodyParse(ResponseBody errorBody) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(errorBody.charStream(), ErrorResponse.class);
        } catch (Exception e) {
            return new ErrorResponse(ResultCode.Unknown.getCode(), "");
        }
    }
}
