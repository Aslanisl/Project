package com.livetyping.moydom.api;

import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.apiModel.Record;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by Ivan on 05.12.2017.
 */

public abstract class CallbackWrapper <T extends BaseModel> extends DisposableObserver<T> {

    private WeakReference<ServerCallback> weakReference;

    public CallbackWrapper(ServerCallback view) {
        this.weakReference = new WeakReference<>(view);
    }

    protected abstract void onSuccess(T t);

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        ServerCallback callback = weakReference.get();
        if (callback != null) {
            if (e instanceof HttpException) {
                ResponseBody responseBody = ((HttpException) e).response().errorBody();
                callback.onUnknownError(getErrorMessage(responseBody));
            } else if (e instanceof SocketTimeoutException) {
                callback.onTimeout();
            } else if (e instanceof IOException) {
                callback.onNetworkError();
            } else {
                callback.onUnknownError(e.getMessage());
            }
        }
    }

    @Override
    public void onComplete() {

    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}