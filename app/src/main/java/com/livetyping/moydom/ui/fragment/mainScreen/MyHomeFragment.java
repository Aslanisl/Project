package com.livetyping.moydom.ui.fragment.mainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.cameras.CameraModel;
import com.livetyping.moydom.apiModel.energy.model.CurrentEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.MonthEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.TodayEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.WeekEnergyModel;
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.data.repository.CamerasRepository;
import com.livetyping.moydom.data.repository.EnergyRepository;
import com.livetyping.moydom.ui.activity.settings.EnergySwitchModel;
import com.livetyping.moydom.ui.activity.settings.SettingsActivity;
import com.livetyping.moydom.ui.adapter.CameraMyHomeAdapter;
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

import static android.app.Activity.RESULT_OK;

public class MyHomeFragment extends BaseFragment implements EnergyRepository.EnergyCallback, CamerasRepository.CamerasCallback{
    public static final String TAG = MyHomeFragment.class.getSimpleName();

    private static final int SETTINGS_REQUEST_CODE = 2;

    @BindView(R.id.fragment_my_home_cameras_recycler) RecyclerView mCamerasRecycler;
    private CameraMyHomeAdapter mCamerasAdapter;
    @BindView(R.id.fragment_my_home_energy_recycler) RecyclerView mEnergyRecycler;
    private EnergyMyHomeAdapter mEnergyAdapter;

    private EnergyRepository mEnergyRepository;
    private CamerasRepository mCamerasRepository;

    private CompositeDisposable mCompositeDisposable;
    private Prefs mPrefs;

    private Unbinder mUnbinder;


    public static MyHomeFragment newInstance() {
        return new MyHomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPrefs = Prefs.getInstance();
        mCompositeDisposable = new CompositeDisposable();
        mEnergyRepository = EnergyRepository.getInstance();
        mCamerasRepository = CamerasRepository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_home, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mEnergyRepository.setEnergyCallback(this);
        initEnergyView();

        mCamerasRepository.setCamerasCallback(this);
        initCameras();
        mCamerasRepository.getCameras(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initEnergyView(){
        mEnergyAdapter = new EnergyMyHomeAdapter(getContext(), false);
        mEnergyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mEnergyRecycler.setAdapter(mEnergyAdapter);
        mEnergyRecycler.setNestedScrollingEnabled(false);
        getEnergyFilters();
    }

    private void getEnergyFilters(){
        mCompositeDisposable.add(Observable.create((ObservableOnSubscribe<List<EnergySwitchModel>>) e -> {
                    e.onNext(mPrefs.getEnergyFilters());
                    e.onComplete();
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(settingsSwitchModels -> {
                                    mEnergyAdapter.addEnergyModels(settingsSwitchModels);
                                    mEnergyRepository.getEnergy();
                                },
                                throwable -> showToast(throwable.getMessage()))
        );
    }

    private void initCameras(){
        mCamerasAdapter = new CameraMyHomeAdapter(getContext());
        mCamerasRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mCamerasRecycler.setAdapter(mCamerasAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mCamerasRecycler);
    }

    @Override
    public void onCurrentEnergyResponse(CurrentEnergyModel energy) {
        mEnergyAdapter.addCurrentEnergy(energy);
    }

    @Override
    public void onTodayEnergyResponse(TodayEnergyModel energy) {
        mEnergyAdapter.addTodayEnergy(energy);
    }

    @Override
    public void onWeekEnergyResponse(WeekEnergyModel weekEnergy) {
        mEnergyAdapter.addWeekEnergy(weekEnergy);
    }

    @Override
    public void onMonthEnergyResponse(MonthEnergyModel monthEnergy) {
        mEnergyAdapter.addMonthEnergy(monthEnergy);
    }

    @Override
    public void onError(String message) {
        showToast(message);
    }

    @Override
    public void onCamerasResponse(List<CameraModel> cameras) {
        mCamerasAdapter.addCameras(cameras);
    }

    @Override
    public void onErrorResponse(String error) {
        showToast(error);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK){
            getEnergyFilters();
            mCamerasRepository.setCamerasCallback(this);
            mCamerasRepository.getCameras(true);
        }
    }

    @Override
    public void onDestroyView() {
        mEnergyRepository.removeEnergyCallback();
        mCamerasRepository.removeCamerasCallback();
        mUnbinder.unbind();
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) mCompositeDisposable.dispose();
        super.onDestroyView();
    }
}
