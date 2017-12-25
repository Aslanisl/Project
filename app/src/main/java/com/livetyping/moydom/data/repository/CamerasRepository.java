package com.livetyping.moydom.data.repository;

import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.api.RetryApiCallWithDelay;
import com.livetyping.moydom.api.ServerCallback;
import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.apiModel.cameras.CameraModel;
import com.livetyping.moydom.apiModel.cameras.CamerasResponse;
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.ui.activity.BaseActivity;
import com.livetyping.moydom.ui.activity.settings.CamerasSwitchModel;
import com.livetyping.moydom.ui.fragment.BaseFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.livetyping.moydom.api.Api.API_RETRY_CALL_COUNT;
import static com.livetyping.moydom.api.Api.API_RETRY_CALL_TIME;

/**
 * Created by Ivan on 08.12.2017.
 */

public class CamerasRepository implements ServerCallback{

    private volatile static CamerasRepository sInstance;

    private CompositeDisposable mCompositeDisposable;

    private WeakReference<CamerasCallback> mCallbackWeakReference;
    private WeakReference<BaseFragment> mBaseFragmentWeakReference;
    private WeakReference<BaseActivity> mBaseActivityWeakReference;

    private List<CameraModel> mCameraModels = new ArrayList<>();

    public void setCamerasCallback(BaseFragment fragment){
        mBaseFragmentWeakReference = new WeakReference<BaseFragment>(fragment);
        if (fragment instanceof CamerasCallback) {
            mCallbackWeakReference = new WeakReference<CamerasCallback>((CamerasCallback) fragment);
        }
    }

    public void setCamerasCallback(BaseActivity activity){
        mBaseActivityWeakReference = new WeakReference<BaseActivity>(activity);
        if (activity instanceof CamerasCallback){
            mCallbackWeakReference = new WeakReference<CamerasCallback>((CamerasCallback) activity);
        }
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

    //With settings enable filters from settings activity
    public void getCameras(boolean withSettings){
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
                                if (!withSettings) {
                                    mCallbackWeakReference.get().onCamerasResponse(mCameraModels);
                                } else {
                                    applySettings(mCameraModels);
                                }
                            }
                        }
                    }
                });
        mCompositeDisposable.add(getCameras);
    }

    private void applySettings(List<CameraModel> cameras){
        List<CameraModel> filterCameras = new ArrayList<>();
        Prefs prefs = Prefs.getInstance();
        List<CamerasSwitchModel> camerasSwitchModels = prefs.getCamerasFilters();
        List<Integer> camerasProcessed = new ArrayList<>();
        for (CamerasSwitchModel model: camerasSwitchModels){
            for (int i = 0; i < cameras.size(); i++){
                CameraModel cameraModel = cameras.get(i);
                if (model.getCameraId() == cameraModel.getCameraId()){
                    camerasProcessed.add(i);
                    if (model.isCameraChecked()) filterCameras.add(cameraModel);
                }
            }
        }
        //Add new cameras if they not included in filters before
        if (camerasProcessed.size() < cameras.size()){
            for (int i = 0; i < cameras.size(); i++){
                CameraModel cameraModel = cameras.get(i);
                if (!camerasProcessed.contains((Integer) i)){
                    filterCameras.add(cameraModel);
                }
            }
        }
        if (mCallbackWeakReference != null && mCallbackWeakReference.get() != null){
            mCallbackWeakReference.get().onCamerasResponse(filterCameras);
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
        handlingInternetProblem();
    }

    @Override
    public void onNetworkError() {
        handlingInternetProblem();
    }

    private void handlingInternetProblem(){
        if (mBaseActivityWeakReference != null && mBaseActivityWeakReference.get() != null){
            mBaseActivityWeakReference.get().problemWithInternet();
        }
        if (mBaseFragmentWeakReference != null && mBaseFragmentWeakReference.get() != null){
            mBaseFragmentWeakReference.get().problemWithInternet();
        }
    }

    public interface CamerasCallback{
        void onCamerasResponse(List<CameraModel> cameras);
        void onErrorResponse(String error);
    }
}
