package com.livetyping.moydom.data.repository;

import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.api.RetryApiCallWithDelay;
import com.livetyping.moydom.api.ServerCallback;
import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.apiModel.cameras.CameraModel;
import com.livetyping.moydom.apiModel.cameras.CamerasResponse;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ivan on 08.12.2017.
 */

public class CamerasRepository implements ServerCallback{

    private static final int API_RETRY_CALL_COUNT = 10;
    private static final int API_RETRY_CALL_TIME = 5000;

    private volatile static CamerasRepository sInstance;

    private CompositeDisposable mCompositeDisposable;

    private WeakReference<CamerasCallback> mCallbackWeakReference;

    private List<CameraModel> mCameraModels = new ArrayList<>();

    public void setCamerasCallback(CamerasCallback camerasCallback){
        mCallbackWeakReference = new WeakReference<CamerasCallback>(camerasCallback);
    }

    public void removeCamerasCallback(){
        if (mCallbackWeakReference != null) mCallbackWeakReference = null;
        if (mCompositeDisposable != null) mCompositeDisposable.dispose();
    }

    public static synchronized CamerasRepository getInstance() {
        if (sInstance == null) {
            sInstance = new CamerasRepository();
        }
        return sInstance;
    }

    public void getCameras(){
        if (mCameraModels.isEmpty()) {
            mCompositeDisposable = new CompositeDisposable();
            Disposable getCameras = Api.getApiService().getCameras(ApiUrlService.getCamerasUrl())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT, API_RETRY_CALL_TIME))
                    .subscribeWith(new CallbackWrapper<CamerasResponse>(this) {
                        @Override
                        protected void onSuccess(CamerasResponse camerasResponse) {
                            if (mCallbackWeakReference != null && mCallbackWeakReference.get() != null) {
                                if (camerasResponse.containsErrors()) {
                                    mCallbackWeakReference.get().onErrorResponse(camerasResponse.getErrorMessage());
                                } else {
                                    mCameraModels = camerasResponse.getCamerasModel();
                                    mCallbackWeakReference.get().onCamerasResponse(mCameraModels);
                                }
                            }
                        }
                    });
            mCompositeDisposable.add(getCameras);
        } else {
            if (mCallbackWeakReference != null && mCallbackWeakReference.get() != null){
                mCallbackWeakReference.get().onCamerasResponse(mCameraModels);
            }
        }
    }

    @Override
    public void onUnknownError(String error) {
        if (mCallbackWeakReference != null && mCallbackWeakReference.get() != null) {
            mCallbackWeakReference.get().onErrorResponse(error);
        }
    }

    @Override
    public void onTimeout() {
        //TODO timeout
    }

    @Override
    public void onNetworkError() {
        //TODO network error
    }

    public interface CamerasCallback{
        void onCamerasResponse(List<CameraModel>cameras);
        void onErrorResponse(String error);
    }
}
