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
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;
import com.livetyping.moydom.presentation.features.base.fragment.BaseFragment;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.livetyping.moydom.api.Api.API_RETRY_CALL_COUNT;
import static com.livetyping.moydom.api.Api.API_RETRY_CALL_TIME;

/**
 * Created by Ivan on 06.12.2017.
 */

public class EnergyRepository implements ServerCallback {

    private static final int REFRESH_TIME = 30;
    private volatile static EnergyRepository sInstance;
    private WeakReference<EnergyCallback> mCallbackWeakReference;
    private WeakReference<BaseFragment> mBaseFragmentWeakReference;
    private WeakReference<BaseActivity> mBaseActivityWeakReference;

    private WeakReference<EnergyGraphCallback> mDayEnergyGraphCallbackWeakReference;
    private WeakReference<EnergyGraphCallback> mWeekEnergyGraphCallbackWeakReference;
    private WeakReference<EnergyGraphCallback> mMonthEnergyGraphCallbackWeakReference;
    private WeakReference<EnergyGraphCallback> mYearEnergyGraphCallbackWeakReference;

    private CurrentEnergyResponse mCurrentEnergy;

    private CompositeDisposable mCompositeDisposable;

    //to avoid creating not with getInstance();
    private EnergyRepository() {
    }

    public static synchronized EnergyRepository getInstance() {
        if (sInstance == null) {
            sInstance = new EnergyRepository();
        }
        return sInstance;
    }

    public void setEnergyCallback(BaseFragment fragment) {
        mBaseFragmentWeakReference = new WeakReference<>(fragment);
        if (fragment instanceof EnergyCallback) {
            mCallbackWeakReference = new WeakReference<>((EnergyCallback) fragment);
        }
    }

    public void setEnergyCallback(BaseActivity activity) {
        mBaseActivityWeakReference = new WeakReference<>(activity);
        if (activity instanceof EnergyCallback) {
            mCallbackWeakReference = new WeakReference<>((EnergyCallback) activity);
        }
    }

    public void removeEnergyCallback() {
        if (mBaseActivityWeakReference != null) mBaseActivityWeakReference = null;
        if (mBaseFragmentWeakReference != null) mBaseFragmentWeakReference = null;
        if (mCallbackWeakReference != null) mCallbackWeakReference = null;
        if (mCompositeDisposable != null) mCompositeDisposable.dispose();
    }

    public void getEnergy() {
        mCompositeDisposable = new CompositeDisposable();
        Disposable getCurrentEnergy =
                Api.getApiService().getCurrentEnergy(ApiUrlService.getCurrentEnergyUrl())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT,
                                API_RETRY_CALL_TIME))
                        .repeatWhen(completed -> completed.delay(REFRESH_TIME, TimeUnit.SECONDS))
                        .subscribeWith(new CallbackWrapper<CurrentEnergyResponse>(this) {
                            @Override
                            protected void onSuccess(CurrentEnergyResponse energy) {
                                if (mCallbackWeakReference != null &&
                                        mCallbackWeakReference.get() != null) {
                                    if (energy.containsErrors()) {
                                        mCallbackWeakReference.get()
                                                .onError(energy.getErrorMessage());
                                    } else {
                                        mCallbackWeakReference.get()
                                                .onCurrentEnergyResponse(energy.getCurrentEnergyModel());
                                    }
                                }
                            }
                        });
        mCompositeDisposable.add(getCurrentEnergy);
        Disposable getTodayWeekEnergy =
                Api.getApiService().getTodayWeekEnergy(ApiUrlService.getTodayWeekEnergyUrl())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT,
                                API_RETRY_CALL_TIME))
                        .repeatWhen(completed -> completed.delay(REFRESH_TIME, TimeUnit.SECONDS))
                        .subscribeWith(new CallbackWrapper<WeekEnergyResponse>(this) {
                            @Override
                            protected void onSuccess(WeekEnergyResponse weekEnergyResponse) {
                                if (mCallbackWeakReference != null &&
                                        mCallbackWeakReference.get() != null) {
                                    if (weekEnergyResponse.containsErrors()) {
                                        mCallbackWeakReference.get()
                                                .onError(weekEnergyResponse.getErrorMessage());
                                    } else {
                                        mCallbackWeakReference.get()
                                                .onTodayEnergyResponse(weekEnergyResponse.getTodayEnergyModel());
                                        mCallbackWeakReference.get()
                                                .onWeekEnergyResponse(weekEnergyResponse.getWeekEnergyModel());
                                    }
                                }
                            }
                        });
        mCompositeDisposable.add(getTodayWeekEnergy);
        Disposable getMonthEnergy =
                Api.getApiService().getMonthEnergy(ApiUrlService.getMonthEnergyUrl())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT,
                                API_RETRY_CALL_TIME))
                        .repeatWhen(completed -> completed.delay(REFRESH_TIME, TimeUnit.SECONDS))
                        .subscribeWith(new CallbackWrapper<MonthEnergyResponse>(this) {
                            @Override
                            protected void onSuccess(MonthEnergyResponse monthEnergyResponse) {
                                if (mCallbackWeakReference != null &&
                                        mCallbackWeakReference.get() != null) {
                                    if (monthEnergyResponse.containsErrors()) {
                                        mCallbackWeakReference.get()
                                                .onError(monthEnergyResponse.getErrorMessage());
                                    } else {
                                        mCallbackWeakReference.get()
                                                .onMonthEnergyResponse(monthEnergyResponse.getMonthEnergyModel());
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

    private void handlingInternetProblem() {
        if (mBaseActivityWeakReference != null && mBaseActivityWeakReference.get() != null) {
            mBaseActivityWeakReference.get().problemWithInternet();
        }
        if (mBaseFragmentWeakReference != null && mBaseFragmentWeakReference.get() != null) {
            mBaseFragmentWeakReference.get().problemWithInternet();
        }
    }

    public void getDayGraphEnergy(EnergyGraphCallback callback, Date offset) {
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mDayEnergyGraphCallbackWeakReference = new WeakReference<>(callback);
        Disposable getDayGraphEnergy =
                Api.getApiService().getDayGraphEnergy(ApiUrlService.getDayGraphEnergyUrl(offset))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT,
                                API_RETRY_CALL_TIME))
                        .subscribeWith(new CallbackWrapper<GraphEnergyResponse>(this) {
                            @Override
                            protected void onSuccess(GraphEnergyResponse monthEnergyResponse) {
                                if (mDayEnergyGraphCallbackWeakReference != null &&
                                        mDayEnergyGraphCallbackWeakReference.get() != null) {
                                    if (monthEnergyResponse.containsErrors()) {
                                        mDayEnergyGraphCallbackWeakReference.get()
                                                .onError(monthEnergyResponse.getErrorCode(),
                                                        monthEnergyResponse.getErrorMessage());
                                    } else {
                                        mDayEnergyGraphCallbackWeakReference.get()
                                                .onGraphResponse(monthEnergyResponse.fillGraphEnergyModel());
                                    }
                                }
                            }
                        });
        mCompositeDisposable.add(getDayGraphEnergy);
    }

    public void removeDayGraphEnergy() {
        if (mDayEnergyGraphCallbackWeakReference != null) {
            mDayEnergyGraphCallbackWeakReference = null;
        }
    }


    public void getWeekGraphEnergy(EnergyGraphCallback callback, Date offset) {
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mWeekEnergyGraphCallbackWeakReference = new WeakReference<>(callback);
        Disposable getWeekGraphEnergy =
                Api.getApiService().getWeekGraphEnergy(ApiUrlService.getWeekGraphEnergyUrl(offset))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT,
                                API_RETRY_CALL_TIME))
                        .subscribeWith(new CallbackWrapper<GraphEnergyResponse>(this) {
                            @Override
                            protected void onSuccess(GraphEnergyResponse monthEnergyResponse) {
                                if (mWeekEnergyGraphCallbackWeakReference != null &&
                                        mWeekEnergyGraphCallbackWeakReference.get() != null) {
                                    if (monthEnergyResponse.containsErrors()) {
                                        mWeekEnergyGraphCallbackWeakReference.get()
                                                .onError(monthEnergyResponse.getErrorCode(),
                                                        monthEnergyResponse.getErrorMessage());
                                    } else {
                                        mWeekEnergyGraphCallbackWeakReference.get()
                                                .onGraphResponse(monthEnergyResponse.fillGraphEnergyModel());
                                    }
                                }
                            }
                        });
        mCompositeDisposable.add(getWeekGraphEnergy);
    }

    public void removeWeekGraphEnergy() {
        if (mWeekEnergyGraphCallbackWeakReference != null) {
            mWeekEnergyGraphCallbackWeakReference = null;
        }
    }


    public void getMonthGraphEnergy(EnergyGraphCallback callback, Date offset) {
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mMonthEnergyGraphCallbackWeakReference = new WeakReference<>(callback);
        Disposable getMonthGraphEnergy =
                Api.getApiService().getMonthGraphEnergy(ApiUrlService.getMonthGraphEnergyUrl(offset))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT,
                                API_RETRY_CALL_TIME))
                        .subscribeWith(new CallbackWrapper<GraphEnergyResponse>(this) {
                            @Override
                            protected void onSuccess(GraphEnergyResponse monthEnergyResponse) {
                                if (mMonthEnergyGraphCallbackWeakReference != null &&
                                        mMonthEnergyGraphCallbackWeakReference.get() != null) {
                                    if (monthEnergyResponse.containsErrors()) {
                                        mMonthEnergyGraphCallbackWeakReference.get()
                                                .onError(monthEnergyResponse.getErrorCode(),
                                                        monthEnergyResponse.getErrorMessage());
                                    } else {
                                        mMonthEnergyGraphCallbackWeakReference.get()
                                                .onGraphResponse(monthEnergyResponse.fillGraphEnergyModel());
                                    }
                                }
                            }
                        });
        mCompositeDisposable.add(getMonthGraphEnergy);
    }

    public void removeMonthGraphEnergy() {
        if (mMonthEnergyGraphCallbackWeakReference != null) {
            mMonthEnergyGraphCallbackWeakReference = null;
        }
    }


    public void getYearGraphEnergy(EnergyGraphCallback callback, Date offset) {
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mYearEnergyGraphCallbackWeakReference = new WeakReference<>(callback);
        Disposable getYearGraphEnergy =
                Api.getApiService().getYearGraphEnergy(ApiUrlService.getYearGraphEnergyUrl(offset))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT,
                                API_RETRY_CALL_TIME))
                        .subscribeWith(new CallbackWrapper<GraphEnergyResponse>(this) {
                            @Override
                            protected void onSuccess(GraphEnergyResponse monthEnergyResponse) {
                                if (mYearEnergyGraphCallbackWeakReference != null &&
                                        mYearEnergyGraphCallbackWeakReference.get() != null) {
                                    if (monthEnergyResponse.containsErrors()) {
                                        mYearEnergyGraphCallbackWeakReference.get()
                                                .onError(monthEnergyResponse.getErrorCode(),
                                                        monthEnergyResponse.getErrorMessage());
                                    } else {
                                        mYearEnergyGraphCallbackWeakReference.get()
                                                .onGraphResponse(monthEnergyResponse.fillGraphEnergyModel());
                                    }
                                }
                            }
                        });
        mCompositeDisposable.add(getYearGraphEnergy);
    }

    public void removeYearGraphEnergy() {
        if (mYearEnergyGraphCallbackWeakReference != null) {
            mYearEnergyGraphCallbackWeakReference = null;
        }
    }

    public interface EnergyCallback {
        void onCurrentEnergyResponse(CurrentEnergyModel currentEnergy);

        void onTodayEnergyResponse(TodayEnergyModel todayEnergy);

        void onWeekEnergyResponse(WeekEnergyModel weekEnergy);

        void onMonthEnergyResponse(MonthEnergyModel monthEnergy);

        void onError(String message);
    }


    public interface EnergyGraphCallback {
        void onGraphResponse(GraphEnergyModel energy);

        void onError(int code, String message);
    }
}
