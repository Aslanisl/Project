package com.livetyping.moydom.ui.activity.settings;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.livetyping.moydom.R;
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.ui.activity.BaseActivity;
import com.livetyping.moydom.ui.activity.MainActivity;
import com.livetyping.moydom.utils.ItemTouchMoveHelperCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_settings_cameras_recycler) RecyclerView mCamerasRecycler;
    private SettingsRecyclerAdapter mCamerasAdapter;
    @BindView(R.id.activity_settings_energy_recycler) RecyclerView mEnergyRecycler;
    private SettingsRecyclerAdapter mEnergyAdapter;
    private Prefs mPrefs;

    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mPrefs = Prefs.getInstance();
        mCompositeDisposable = new CompositeDisposable();

        mToolbar.setNavigationIcon(R.drawable.close);
        mToolbar.setTitle(R.string.settings);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        initCameras();
        initDataForCameras();
        initEnergy();
        initDataForEnergy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                saveFilters();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initCameras(){
        mCamerasAdapter = new SettingsRecyclerAdapter();
        ItemTouchMoveHelperCallback camerasCallback = new ItemTouchMoveHelperCallback(mCamerasAdapter);
        final ItemTouchHelper camerasItemTouchHelper = new ItemTouchHelper(camerasCallback);
        camerasItemTouchHelper.attachToRecyclerView(mCamerasRecycler);
        mCamerasRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCamerasRecycler.setAdapter(mCamerasAdapter);
        mCamerasAdapter.setOnDragListener(camerasItemTouchHelper::startDrag);
    }

    private void initEnergy(){
        mEnergyAdapter = new SettingsRecyclerAdapter();
        ItemTouchMoveHelperCallback energyCallback = new ItemTouchMoveHelperCallback(mEnergyAdapter);
        final ItemTouchHelper energyItemTouchHelper = new ItemTouchHelper(energyCallback);
        energyItemTouchHelper.attachToRecyclerView(mEnergyRecycler);
        mEnergyRecycler.setLayoutManager(new LinearLayoutManager(this));
        mEnergyRecycler.setAdapter(mEnergyAdapter);
        mEnergyAdapter.setOnDragListener(energyItemTouchHelper::startDrag);
    }

    private void initDataForCameras(){
        mCompositeDisposable.add(Observable.create((ObservableOnSubscribe<List<SettingsSwitchModel>>) e -> {
            e.onNext(mPrefs.getFilters(Prefs.KEY_CAMERAS_FILTER));
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mCamerasAdapter::addSettings, throwable -> showToast(throwable.getMessage())));
    }

    private void initDataForEnergy(){
        mCompositeDisposable.add(Observable.create((ObservableOnSubscribe<List<SettingsSwitchModel>>) e -> {
            e.onNext(mPrefs.getFilters(Prefs.KEY_ENERGY_FILTER));
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mEnergyAdapter::addSettings, throwable -> showToast(throwable.getMessage())));
    }

    private void saveFilters(){
        showProgress();
        mCompositeDisposable.add(Completable.create(e -> {
            mPrefs.saveFilters(mCamerasAdapter.getSettingsList(), Prefs.KEY_CAMERAS_FILTER);
            mPrefs.saveFilters(mEnergyAdapter.getSettingsList(), Prefs.KEY_ENERGY_FILTER);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    removeProgress();
                    setResult(RESULT_OK);
                    finish();
                }, throwable -> {
                    removeProgress();
                    setResult(RESULT_CANCELED);
                    finish();
                })
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()){
            mCompositeDisposable.dispose();
        }
    }
}
