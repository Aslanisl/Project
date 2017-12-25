package com.livetyping.moydom.ui.fragment.mainScreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.livetyping.moydom.R;
import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.api.RetryApiCallWithDelay;
import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.apiModel.advice.AdviceModel;
import com.livetyping.moydom.apiModel.advice.AdviceResponse;
import com.livetyping.moydom.apiModel.energy.model.CurrentEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.MonthEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.TodayEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.WeekEnergyModel;
import com.livetyping.moydom.data.repository.EnergyRepository;
import com.livetyping.moydom.ui.adapter.EnergyMyHomeAdapter;
import com.livetyping.moydom.ui.fragment.BaseFragment;
import com.livetyping.moydom.utils.HelpUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.livetyping.moydom.apiModel.advice.AdviceModel.STATUS_READED;

public class ResourcesFragment extends BaseFragment implements EnergyRepository.EnergyCallback{
    public static final String TAG = ResourcesFragment.class.getSimpleName();

    private static final int API_RETRY_CALL_COUNT = 10;
    private static final int API_RETRY_CALL_TIME = 5000;

    @BindView(R.id.fragment_resources_recycler) RecyclerView mResourcesRecycler;
    private EnergyMyHomeAdapter mAdapter;
    private CompositeDisposable mCompositeDisposable;

    private EnergyRepository mEnergyRepository;
    private Unbinder mUnbinder;

    public static ResourcesFragment newInstance() {
        return new ResourcesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEnergyRepository = EnergyRepository.getInstance();
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resourses, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mAdapter = new EnergyMyHomeAdapter(getContext(), true);
        mResourcesRecycler.setAdapter(mAdapter);
        mResourcesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mResourcesRecycler.setNestedScrollingEnabled(false);
        mAdapter.setAdviceListener(this::closeAdvice);

        mEnergyRepository.setEnergyCallback(this);
        mEnergyRepository.getEnergy();

        initAdvice();
        return rootView;
    }

    private void initAdvice(){
        mCompositeDisposable.add(Api.getApiService().getAdvice(ApiUrlService.getAdviceUrl())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT, API_RETRY_CALL_TIME))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<AdviceResponse>(this){
                    @Override
                    protected void onSuccess(AdviceResponse adviceResponse) {
                        if (adviceResponse.containsErrors()){
                            showToast(adviceResponse.getErrorMessage());
                        } else {
                            initAdvices(adviceResponse.getAdviceModels());
                        }
                    }
                }));
    }

    @Override
    public void onCurrentEnergyResponse(CurrentEnergyModel currentEnergy) {
        mAdapter.addCurrentEnergy(currentEnergy);
    }

    @Override
    public void onTodayEnergyResponse(TodayEnergyModel todayEnergy) {
        mAdapter.addTodayEnergy(todayEnergy);
    }

    @Override
    public void onWeekEnergyResponse(WeekEnergyModel weekEnergy) {
        mAdapter.addWeekEnergy(weekEnergy);
    }

    @Override
    public void onMonthEnergyResponse(MonthEnergyModel monthEnergy) {
        mAdapter.addMonthEnergy(monthEnergy);
    }

    private void initAdvices(List<AdviceModel> models){
        if (!models.isEmpty()){
            // Get first advice
            mAdapter.setAdviceModel(models.get(0));
        }
    }

    private void closeAdvice(int adviceId){
        mCompositeDisposable.add(
                Api.getApiService().changeAdviceStatus(ApiUrlService.getChangeAdviceUrl(adviceId, STATUS_READED))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<BaseModel>(this){
                    @Override
                    protected void onSuccess(BaseModel baseModel) {
                        if (baseModel.containsErrors()){
                            showToast(baseModel.getErrorMessage());
                        }
                    }
                }));
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if (mCompositeDisposable != null) mCompositeDisposable.dispose();
    }
}
