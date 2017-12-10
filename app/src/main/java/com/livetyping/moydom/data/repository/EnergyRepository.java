package com.livetyping.moydom.data.repository;

import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.api.RetryApiCallWithDelay;
import com.livetyping.moydom.api.ServerCallback;
import com.livetyping.moydom.apiModel.energy.model.CurrentEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.MonthEnergyModel;
import com.livetyping.moydom.apiModel.energy.response.CurrentEnergyResponse;
import com.livetyping.moydom.apiModel.energy.model.TodayEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.WeekEnergyModel;
import com.livetyping.moydom.apiModel.energy.response.MonthEnergyResponse;
import com.livetyping.moydom.apiModel.energy.response.WeekEnergyResponse;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ivan on 06.12.2017.
 */

public class EnergyRepository implements ServerCallback{

    private static final int API_RETRY_CALL_COUNT = 10;
    private static final int API_RETRY_CALL_TIME = 5000;

    private volatile static EnergyRepository sInstance;

    private WeakReference<EnergyCallback> mCallbackWeakReference;

    private CurrentEnergyResponse mCurrentEnergy;

    private CompositeDisposable mCompositeDisposable;

    public void setEnergyCallback(EnergyCallback callback){
        mCallbackWeakReference = new WeakReference<EnergyCallback>(callback);
    }

    public void removeEnergyCallback(){
        if (mCallbackWeakReference != null) mCallbackWeakReference = null;
        if (mCompositeDisposable != null) mCompositeDisposable.dispose();
    }

    public static synchronized EnergyRepository getInstance() {
        if (sInstance == null) {
            sInstance = new EnergyRepository();
        }
        return sInstance;
    }

    public void getEnergy(){
        mCompositeDisposable = new CompositeDisposable();
        Disposable getCurrentEnergy = Api.getApiService().getCurrentEnergy(ApiUrlService.getCurrentEnergyUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT, API_RETRY_CALL_TIME))
                .subscribeWith(new CallbackWrapper<CurrentEnergyResponse>(this) {
                    @Override
                    protected void onSuccess(CurrentEnergyResponse energy) {
                        if (mCallbackWeakReference != null && mCallbackWeakReference.get() != null) {
                            if (energy.containsErrors()) {
                                mCallbackWeakReference.get().onError(energy.getErrorMessage());
                            } else {
                                mCallbackWeakReference.get().onCurrentEnergyResponse(energy.getCurrentEnergyModel());
                            }
                        }
                    }
                });
        mCompositeDisposable.add(getCurrentEnergy);
        Disposable getTodayWeekEnergy = Api.getApiService().getTodayWeekEnergy(ApiUrlService.getTodayWeekEnergyUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT, API_RETRY_CALL_TIME))
                .subscribeWith(new CallbackWrapper<WeekEnergyResponse>(this){
                    @Override
                    protected void onSuccess(WeekEnergyResponse weekEnergyResponse) {
                        if (mCallbackWeakReference != null && mCallbackWeakReference.get() != null) {
                            if (weekEnergyResponse.containsErrors()) {
                                mCallbackWeakReference.get().onError(weekEnergyResponse.getErrorMessage());
                            } else {
                                mCallbackWeakReference.get().onTodayEnergyResponse(weekEnergyResponse.getTodayEnergyModel());
                                mCallbackWeakReference.get().onWeekEnergyResponse(weekEnergyResponse.getWeekEnergyModel());
                            }
                        }
                    }
                });
        mCompositeDisposable.add(getTodayWeekEnergy);
        Disposable getMonthEnergy = Api.getApiService().getMonthEnergy(ApiUrlService.getMonthEnergyUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT, API_RETRY_CALL_TIME))
                .subscribeWith(new CallbackWrapper<MonthEnergyResponse>(this){
                    @Override
                    protected void onSuccess(MonthEnergyResponse monthEnergyResponse) {
                        if (mCallbackWeakReference != null && mCallbackWeakReference.get() != null) {
                            if (monthEnergyResponse.containsErrors()) {
                                mCallbackWeakReference.get().onError(monthEnergyResponse.getErrorMessage());
                            } else {
                                mCallbackWeakReference.get().onMonthEnergyResponse(monthEnergyResponse.getMonthEnergyModel());
                            }
                        }
                    }
                });
        mCompositeDisposable.add(getMonthEnergy);
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
        void onCurrentEnergyResponse(CurrentEnergyModel currentEnergy);
        void onTodayEnergyResponse(TodayEnergyModel todayEnergy);
        void onWeekEnergyResponse(WeekEnergyModel weekEnergy);
        void onMonthEnergyResponse(MonthEnergyModel monthEnergy);
        void onError(String message);
    }
}
