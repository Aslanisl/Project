package com.livetyping.moydom.ui.activity.settings;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.cameras.CameraModel;
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.data.repository.CamerasRepository;
import com.livetyping.moydom.ui.activity.BaseActivity;
import com.livetyping.moydom.utils.ItemTouchMoveHelperCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements CamerasRepository.CamerasCallback{

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_settings_cameras_recycler) RecyclerView mCamerasRecycler;
    private SettingsRecyclerAdapter mCamerasAdapter;
    @BindView(R.id.activity_settings_energy_recycler) RecyclerView mEnergyRecycler;
    private SettingsRecyclerAdapter mEnergyAdapter;
    private Prefs mPrefs;

    private CamerasRepository mCamerasRepository;

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
        ItemTouchMoveHelperCallback camerasCallback = new ItemTouchMoveHelperCallback(mCamerasAdapter);
        final ItemTouchHelper camerasItemTouchHelper = new ItemTouchHelper(camerasCallback);
        camerasItemTouchHelper.attachToRecyclerView(mCamerasRecycler);
        mCamerasRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCamerasRecycler.setAdapter(mCamerasAdapter);
        mCamerasRecycler.setNestedScrollingEnabled(false);
        mCamerasAdapter.setOnDragListener(camerasItemTouchHelper::startDrag);

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
        ItemTouchMoveHelperCallback energyCallback = new ItemTouchMoveHelperCallback(mEnergyAdapter);
        final ItemTouchHelper energyItemTouchHelper = new ItemTouchHelper(energyCallback);
        energyItemTouchHelper.attachToRecyclerView(mEnergyRecycler);
        mEnergyRecycler.setLayoutManager(new LinearLayoutManager(this));
        mEnergyRecycler.setAdapter(mEnergyAdapter);
        mEnergyRecycler.setNestedScrollingEnabled(false);
        mEnergyAdapter.setOnDragListener(energyItemTouchHelper::startDrag);
        mEnergyAdapter.addEnergySettings(mPrefs.getEnergyFilters());
    }

    private void saveFilters(){
        if (mCamerasAdapter != null && mEnergyAdapter != null) {
            mPrefs.saveCamerasFilters(mCamerasAdapter.getCamerasList());
            mPrefs.saveEnergyFilters(mEnergyAdapter.getEnergyList());
        } else {
            mCamerasRepository.removeCamerasCallback();
        }
        setResult(RESULT_OK);
        finish();
    }
}
