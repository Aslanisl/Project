package com.livetyping.moydom.data.repository;

import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.api.RetryApiCallWithDelay;
import com.livetyping.moydom.api.ServerCallback;
import com.livetyping.moydom.apiModel.myTarget.AverageEnergyCostResponse;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;
import com.livetyping.moydom.presentation.features.base.fragment.BaseFragment;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.livetyping.moydom.api.Api.API_RETRY_CALL_COUNT;
import static com.livetyping.moydom.api.Api.API_RETRY_CALL_TIME;

/**
 * Created by Ivan on 22.12.2017.
 */

public class AverageEnergyCostRepository implements ServerCallback {

    private volatile static AverageEnergyCostRepository sInstance;

    public static synchronized AverageEnergyCostRepository getInstance() {
        if (sInstance == null) {
            sInstance = new AverageEnergyCostRepository();
        }
        return sInstance;
    }

    private CompositeDisposable mCompositeDisposable;

    private WeakReference<AverageCostCallback> mCallbackWeakReference;
    private WeakReference<BaseFragment> mBaseFragmentWeakReference;
    private WeakReference<BaseActivity> mBaseActivityWeakReference;

    private float mAverageCost;

    public void setAverageCostCallback(BaseFragment fragment){
        mBaseFragmentWeakReference = new WeakReference<BaseFragment>(fragment);
        if (fragment instanceof AverageCostCallback) {
            mCallbackWeakReference = new WeakReference<AverageCostCallback>((AverageCostCallback) fragment);
        }
    }

    public void setAverageCostCallback(BaseActivity activity){
        mBaseActivityWeakReference = new WeakReference<BaseActivity>(activity);
        if (activity instanceof AverageCostCallback){
            mCallbackWeakReference = new WeakReference<AverageCostCallback>((AverageCostCallback) activity);
        }
    }

    public void removeAverageCostCallback(){
        if (mCallbackWeakReference != null) mCallbackWeakReference = null;
        if (mCompositeDisposable != null) mCompositeDisposable.dispose();
    }

    public void getAverageCost(){
        if (mAverageCost == 0) {
            mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(Api.getApiService()
                    .getAverageEnergyCost(ApiUrlService.getAverageEnergyCostUrl())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT, API_RETRY_CALL_TIME))
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new CallbackWrapper<AverageEnergyCostResponse>(this) {
                        @Override
                        protected void onSuccess(AverageEnergyCostResponse averageEnergyCostResponse) {
                            if (mCallbackWeakReference != null && mCallbackWeakReference.get() != null) {
                                if (averageEnergyCostResponse.containsErrors()) {
                                    mCallbackWeakReference.get().onErrorResponse(averageEnergyCostResponse.getErrorMessage());
                                } else {
                                    mAverageCost = averageEnergyCostResponse.getAverageCost();
                                    mCallbackWeakReference.get().onAverageCostResponse(mAverageCost);
                                }
                            }
                        }
                    }));
        } else {
            if (mCallbackWeakReference != null && mCallbackWeakReference.get() != null) {
                mCallbackWeakReference.get().onAverageCostResponse(mAverageCost);
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

    public interface AverageCostCallback{
        void onAverageCostResponse(float averageCost);
        void onErrorResponse(String error);
    }
}
