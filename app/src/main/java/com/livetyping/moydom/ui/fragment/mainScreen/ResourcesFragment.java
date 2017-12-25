package com.livetyping.moydom.ui.fragment.mainScreen;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.energy.model.CurrentEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.MonthEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.TodayEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.WeekEnergyModel;
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.data.repository.EnergyRepository;
import com.livetyping.moydom.ui.activity.settings.EnergySwitchModel;
import com.livetyping.moydom.ui.adapter.EnergyMyHomeAdapter;
import com.livetyping.moydom.ui.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ResourcesFragment extends BaseFragment implements EnergyRepository.EnergyCallback{
    public static final String TAG = ResourcesFragment.class.getSimpleName();
    @BindView(R.id.fragment_resources_energy_recycler) RecyclerView mRecycler;
    private Unbinder mUnbinder;
    private EnergyMyHomeAdapter mAdapter;

    private EnergyRepository mEnergyRepository;

    private CompositeDisposable mCompositeDisposable;
    private Prefs mPrefs;

    public static ResourcesFragment newInstance() {
        return new ResourcesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEnergyRepository = EnergyRepository.getInstance();

        mPrefs = Prefs.getInstance();
        mCompositeDisposable = new CompositeDisposable();

        if (getActivity() != null
                && getActivity() instanceof AppCompatActivity
                && ((AppCompatActivity) getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.resources);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resourses, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mEnergyRepository.setEnergyCallback(this);
        mEnergyRepository.getEnergy();
        initEnergyView();
        return rootView;
    }

    private void initEnergyView() {
        mAdapter = new EnergyMyHomeAdapter(getContext());
        mAdapter.setIsItemClickable(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter);
        mRecycler.setNestedScrollingEnabled(false);
        getEnergyFilters();
    }

    private void getEnergyFilters(){
        mCompositeDisposable.add(Observable.create((ObservableOnSubscribe<List<EnergySwitchModel>>) e -> {
                    e.onNext(mPrefs.getEnergyFilters());
                    e.onComplete();
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(settingsSwitchModels -> {
                                    mAdapter.addEnergyModels(settingsSwitchModels);
                                    mEnergyRepository.getEnergy();
                                },
                                throwable -> showToast(throwable.getMessage()))
        );
    }

    @Override
    public void onCurrentEnergyResponse(CurrentEnergyModel energy) {
        mAdapter.addCurrentEnergy(energy);
    }

    @Override
    public void onTodayEnergyResponse(TodayEnergyModel energy) {
        mAdapter.addTodayEnergy(energy);

    }

    @Override
    public void onWeekEnergyResponse(WeekEnergyModel weekEnergy) {
        mAdapter.addWeekEnergy(weekEnergy);
    }

    @Override
    public void onMonthEnergyResponse(MonthEnergyModel monthEnergy) {
        mAdapter.addMonthEnergy(monthEnergy);
    }

    @Override
    public void onError(String message) {
        showToast(message);
    }


    @Override
    public void onDestroyView() {
        mEnergyRepository.removeEnergyCallback();
        mUnbinder.unbind();
        super.onDestroyView();
    }

}
