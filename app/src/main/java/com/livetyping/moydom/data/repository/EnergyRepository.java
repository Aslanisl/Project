package com.livetyping.moydom.data.repository;

import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.api.RetryApiCallWithDelay;
import com.livetyping.moydom.api.ServerCallback;
import com.livetyping.moydom.apiModel.energy.model.CurrentEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.GraphEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.MonthEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.TodayEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.WeekEnergyModel;
import com.livetyping.moydom.apiModel.energy.response.CurrentEnergyResponse;
import com.livetyping.moydom.apiModel.energy.response.GraphEnergyResponse;
import com.livetyping.moydom.apiModel.energy.response.MonthEnergyResponse;
import com.livetyping.moydom.apiModel.energy.response.WeekEnergyResponse;
import com.livetyping.moydom.ui.activity.BaseActivity;
import com.livetyping.moydom.ui.fragment.BaseFragment;

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
    private WeakReference<EnergyGraphCallback> mGraphCallbackWeakReference;
    private WeakReference<BaseFragment> mBaseFragmentWeakReference;
    private WeakReference<BaseActivity> mBaseActivityWeakReference;

    private CurrentEnergyResponse mCurrentEnergy;

    private CompositeDisposable mCompositeDisposable;

    //to avoid creating not with getInstance();
    private EnergyRepository(){}

    public static synchronized EnergyRepository getInstance() {
        if (sInstance == null) {
            sInstance = new EnergyRepository();
        }
        return sInstance;
    }

    public void setEnergyCallback(BaseFragment fragment){
        mBaseFragmentWeakReference = new WeakReference<BaseFragment>(fragment);
        if (fragment instanceof EnergyCallback){
            mCallbackWeakReference = new WeakReference<EnergyCallback>((EnergyCallback) fragment);
        }
    }

    public void setEnergyCallback(BaseActivity activity){
        mBaseActivityWeakReference = new WeakReference<BaseActivity>(activity);
        if (activity instanceof EnergyCallback){
            mCallbackWeakReference = new WeakReference<EnergyCallback>((EnergyCallback) activity);
        }
    }

    public void setGraphCallback(EnergyGraphCallback callback){
        mGraphCallbackWeakReference = new WeakReference<EnergyGraphCallback>(callback);
    }

    public void removeEnergyCallback(){
        if (mBaseActivityWeakReference != null) mBaseActivityWeakReference = null;
        if (mBaseFragmentWeakReference != null) mBaseFragmentWeakReference = null;
        if (mCallbackWeakReference != null) mCallbackWeakReference = null;
        if (mCompositeDisposable != null) mCompositeDisposable.dispose();
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

    public void getDayGraphEnergy(DayEnergyGraphCallback callback) {
//        Disposable getDayGraphEnergy = Api.getApiService().getDayGraphEnergy(ApiUrlService.getDayGraphEnergyUrl())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT, API_RETRY_CALL_TIME))
//                .subscribeWith(new CallbackWrapper<GraphEnergyResponse>(this){
//                    @Override
//                    protected void onSuccess(GraphEnergyResponse monthEnergyResponse) {
//                        if (monthEnergyResponse.containsErrors()) {
//                            callback.onError(monthEnergyResponse.getErrorMessage());
//                        } else {
//                            callback.onDayGraphResponse(monthEnergyResponse.);
//                        }
//                    }
//                });
//        mCompositeDisposable.add(getDayGraphEnergy);
    }


    public void getWeekGraphEnergy(WeekEnergyGraphCallback callback) {
//        Disposable getWeekGraphEnergy = Api.getApiService().getWeekGraphEnergy(ApiUrlService.getWeekGraphEnergyUrl())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT, API_RETRY_CALL_TIME))
//                .subscribeWith(new CallbackWrapper<GraphEnergyResponse>(this){
//                    @Override
//                    protected void onSuccess(GraphEnergyResponse monthEnergyResponse) {
//                        if (monthEnergyResponse.containsErrors()) {
//                            callback.onError(monthEnergyResponse.getErrorMessage());
//                        } else {
//                            callback.onWeekGraphResponse(monthEnergyResponse.fillGraphEnergyModel());
//                        }
//                    }
//                });
//        mCompositeDisposable.add(getWeekGraphEnergy);
    }


    public void getMonthGraphEnergy(MonthEnergyGraphCallback callback) {
        Disposable getMonthGraphEnergy = Api.getApiService().getMonthGraphEnergy(ApiUrlService.getMonthGraphEnergyUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT, API_RETRY_CALL_TIME))
                .subscribeWith(new CallbackWrapper<GraphEnergyResponse>(this){
                    @Override
                    protected void onSuccess(GraphEnergyResponse monthEnergyResponse) {
                        if (monthEnergyResponse.containsErrors()) {
                            callback.onError(monthEnergyResponse.getErrorMessage());
                        } else {
                            callback.onMonthGraphResponse(monthEnergyResponse.fillGraphEnergyModel());
                        }
                    }
                });
        mCompositeDisposable.add(getMonthGraphEnergy);
    }


    public void getYearGraphEnergy(YearEnergyGraphCallback callback) {
//        Disposable getYearGraphEnergy = Api.getApiService().getYearGraphEnergy(ApiUrlService.getYearGraphEnergyUrl())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT, API_RETRY_CALL_TIME))
//                .subscribeWith(new CallbackWrapper<GraphEnergyResponse>(this){
//                    @Override
//                    protected void onSuccess(GraphEnergyResponse monthEnergyResponse) {
//                        if (monthEnergyResponse.containsErrors()) {
//                            callback.onError(monthEnergyResponse.getErrorMessage());
//                        } else {
//                            callback.onYearGraphResponse(monthEnergyResponse.fillGraphEnergyModel());
//                        }
//                    }
//                });
//        mCompositeDisposable.add(getYearGraphEnergy);
    }

    public interface EnergyCallback {
        void onCurrentEnergyResponse(CurrentEnergyModel currentEnergy);
        void onTodayEnergyResponse(TodayEnergyModel todayEnergy);
        void onWeekEnergyResponse(WeekEnergyModel weekEnergy);
        void onMonthEnergyResponse(MonthEnergyModel monthEnergy);

        void onError(String message);
    }

    public interface EnergyGraphCallback {

        void onDayGraphResponse(WeekEnergyModel energy);
        void onWeekGraphResponse(WeekEnergyModel energy);
        void onMonthGraphResponse(WeekEnergyModel energy);
        void onYearGraphResponse(WeekEnergyModel energy);

        void onError(String message);
    }


    public interface DayEnergyGraphCallback {
        void onDayGraphResponse(WeekEnergyModel energy);

        void onError(String message);
    }
    public interface WeekEnergyGraphCallback {
        void onWeekGraphResponse(WeekEnergyModel energy);

        void onError(String message);
    }
    public interface MonthEnergyGraphCallback {
        void onMonthGraphResponse(GraphEnergyModel energy);

        void onError(String message);
    }
    public interface YearEnergyGraphCallback {
        void onYearGraphResponse(WeekEnergyModel energy);

        void onError(String message);
    }
}
