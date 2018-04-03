package com.livetyping.moydom.presentation.features.myHomeSettings.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.cameras.CameraModel;
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.data.repository.CamerasRepository;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;
import com.livetyping.moydom.presentation.features.myHomeSettings.model.CamerasSwitchModel;
import com.livetyping.moydom.presentation.features.myHomeSettings.adapter.SettingsRecyclerAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements CamerasRepository.CamerasCallback, SettingsRecyclerAdapter.OnChangeListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_settings_cameras_recycler) RecyclerView mCamerasRecycler;
    private SettingsRecyclerAdapter mCamerasAdapter;
    private RecyclerViewDragDropManager mCamerasRecyclerViewDragDropManager;
    @BindView(R.id.activity_settings_energy_recycler) RecyclerView mEnergyRecycler;
    private SettingsRecyclerAdapter mEnergyAdapter;
    private RecyclerViewDragDropManager mEnergyRecyclerViewDragDropManager;
    private Prefs mPrefs;

    private CamerasRepository mCamerasRepository;
    private boolean mEnableDoneButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mPrefs = Prefs.getInstance();
        mCamerasRepository = CamerasRepository.getInstance();
        mCamerasRepository.setCamerasCallback(this);
        mCamerasRepository.getCameras(false);

        mToolbar.setNavigationIcon(R.drawable.close);
        mToolbar.setTitle(R.string.settings);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        initEnergy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_done);
        if (mEnableDoneButton){
            item.setEnabled(true);
            item.setIcon(R.drawable.accept_active);
        } else {
            item.setEnabled(false);
            item.setIcon(R.drawable.accept_disable);
        }
        return super.onPrepareOptionsMenu(menu);
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

    @Override
    public void onCamerasResponse(List<CameraModel> cameras) {
        mCamerasRepository.removeCamerasCallback();
        initCameras(cameras);
    }

    @Override
    public void onErrorResponse(String error) {
        mCamerasRepository.removeCamerasCallback();
        showToast(error);
    }

    private void initCameras(List<CameraModel> cameras){
        mCamerasAdapter = new SettingsRecyclerAdapter();
        mCamerasRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        RecyclerView.Adapter wrappedAdapter = mCamerasRecyclerViewDragDropManager.createWrappedAdapter(mCamerasAdapter);
        ((SimpleItemAnimator) mCamerasRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mCamerasRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCamerasRecycler.setAdapter(wrappedAdapter);
        final GeneralItemAnimator animator = new DraggableItemAnimator();
        mCamerasRecycler.setItemAnimator(animator);
        mCamerasAdapter.setOnChangeListener(this);
        mCamerasRecyclerViewDragDropManager.attachRecyclerView(mCamerasRecycler);

        //Remove cameras if they not in server response now
        List<CamerasSwitchModel> camerasSwitchModels = mPrefs.getCamerasFilters();
        Iterator<CamerasSwitchModel> models = camerasSwitchModels.iterator();
        List<Integer> includedItems = new ArrayList<>();
        while (models.hasNext()){
            CamerasSwitchModel model = models.next();
            boolean contains = false;
            //Hold if camera title is changed
            String newTitle = null;
            for (int i = 0; i < cameras.size(); i++){
                CameraModel cameraModel = cameras.get(i);
                if (model.getCameraId() == cameraModel.getCameraId()){
                    contains = true;
                    newTitle = cameraModel.getCameraName();
                    includedItems.add(i);
                }
            }
            if (newTitle != null){
                model.setCameraTitle(newTitle);
            }
            if (!contains){
                models.remove();
            }
        }
        //Add new cameras if they not included in filters before
        if (includedItems.size() < cameras.size()){
            for (int i = 0; i < cameras.size(); i++){
                CameraModel cameraModel = cameras.get(i);
                if (!includedItems.contains((Integer) i)){
                    CamerasSwitchModel model = new CamerasSwitchModel();
                    model.setCameraId(cameraModel.getCameraId());
                    model.setCameraTitle(cameraModel.getCameraName());
                    model.setCameraChecked(true);
                    camerasSwitchModels.add(model);
                }
            }
        }
        mCamerasAdapter.addCamerasSettings(camerasSwitchModels);
    }

    private void initEnergy(){
        mEnergyAdapter = new SettingsRecyclerAdapter();
        mEnergyRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        RecyclerView.Adapter wrappedAdapter = mEnergyRecyclerViewDragDropManager.createWrappedAdapter(mEnergyAdapter);
        mEnergyRecycler.setLayoutManager(new LinearLayoutManager(this));
        ((SimpleItemAnimator) mEnergyRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mEnergyRecycler.setAdapter(wrappedAdapter);
        final GeneralItemAnimator animator = new DraggableItemAnimator();
        mEnergyRecycler.setItemAnimator(animator);
        mEnergyAdapter.addEnergySettings(mPrefs.getEnergyFilters());
        mEnergyAdapter.setOnChangeListener(this);
        mEnergyRecyclerViewDragDropManager.attachRecyclerView(mEnergyRecycler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mEnergyRecyclerViewDragDropManager != null){
            mEnergyRecyclerViewDragDropManager.cancelDrag();
        }
        if (mCamerasRecyclerViewDragDropManager != null){
            mCamerasRecyclerViewDragDropManager.cancelDrag();
        }
    }

    @Override
    public void changedSettings() {
        if (!mEnableDoneButton) {
            mEnableDoneButton = true;
            invalidateOptionsMenu();
        }
    }

    private void saveFilters(){
        if (mCamerasAdapter != null) {
            mPrefs.saveCamerasFilters(mCamerasAdapter.getCamerasList());
        }
        if (mEnergyAdapter != null){
            mPrefs.saveEnergyFilters(mEnergyAdapter.getEnergyList());
        }
        mCamerasRepository.removeCamerasCallback();

        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEnergyRecyclerViewDragDropManager != null){
            mEnergyRecyclerViewDragDropManager.release();
            mEnergyRecyclerViewDragDropManager = null;
        }
        if (mCamerasRecyclerViewDragDropManager != null){
            mCamerasRecyclerViewDragDropManager.release();
            mCamerasRecyclerViewDragDropManager = null;
        }
    }
}
