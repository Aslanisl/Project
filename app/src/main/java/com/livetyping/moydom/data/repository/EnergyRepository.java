package com.livetyping.moydom.data.repository;

import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.api.ServerCallback;
import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.apiModel.energy.CurrentEnergy;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ivan on 06.12.2017.
 */

public class EnergyRepository implements ServerCallback{

    private volatile static EnergyRepository sInstance;

    private WeakReference<EnergyCallback> mCallbackWeakReference;

    private CurrentEnergy mCurrentEnergy;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public void setEnergyCallback(EnergyCallback callback){
        mCallbackWeakReference = new WeakReference<EnergyCallback>(callback);
    }

    public void removeEnergyCallback(){
        if (mCallbackWeakReference != null) mCallbackWeakReference = null;
    }

    public static synchronized EnergyRepository getInstance() {
        if (sInstance == null) {
            sInstance = new EnergyRepository();
        }
        return sInstance;
    }

    public void getEnergy(){
        Disposable getCurrentEnergy = Api.getApiService().getCurrentEnergy(ApiUrlService.getCurrentEnergyUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<CurrentEnergy>(this) {
                    @Override
                    protected void onSuccess(CurrentEnergy energy) {
                        if (mCallbackWeakReference != null && mCallbackWeakReference.get() != null) {
                            mCallbackWeakReference.get().onCurrentEnergyResponse(energy);
                        }
                    }
                });
        mCompositeDisposable.add(getCurrentEnergy);
    }

    @Override
    public void onUnknownError(String error) {
        if (mCallbackWeakReference != null && mCallbackWeakReference.get() != null) {
            mCallbackWeakReference.get().onError(error);
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

    public interface EnergyCallback {
        void onCurrentEnergyResponse(CurrentEnergy energy);
        void onError(String message);
    }
}
