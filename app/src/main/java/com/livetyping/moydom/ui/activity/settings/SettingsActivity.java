package com.livetyping.moydom.ui.activity.settings;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.activity.BaseActivity;
import com.livetyping.moydom.utils.ItemTouchMoveHelper;
import com.livetyping.moydom.utils.ItemTouchMoveHelperCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.activity_settings_cameras_recycler) RecyclerView mCamerasRecycler;
    @BindView(R.id.activity_settings_energy_recycler) RecyclerView mEnergyRecycler;

    private ItemTouchMoveHelper mCamerasTouchMoveHelper = ((fromHolder, toHolder) -> {
        showToast("from_holder" + fromHolder.getAdapterPosition() + "to_holder" + toHolder.getAdapterPosition());
    });

    private ItemTouchMoveHelper mEnergyTouchMoveHelper = ((fromHolder, toHolder) -> {
        showToast("from_holder" + fromHolder.getAdapterPosition() + "to_holder" + toHolder.getAdapterPosition());
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        List<SettingsSwitchModel> camerasModel = new ArrayList<>();
        String[] camerasTitle = getResources().getStringArray(R.array.cameras_names);
        for (String title : camerasTitle){
            SettingsSwitchModel model = new SettingsSwitchModel();
            model.setTitle(title);
            model.setChecked(true);
            camerasModel.add(model);
        }
        SettingsRecyclerAdapter camerasAdapter = new SettingsRecyclerAdapter(camerasModel);
        ItemTouchMoveHelperCallback camerasCallback = new ItemTouchMoveHelperCallback(mCamerasTouchMoveHelper);
        final ItemTouchHelper camerasItemTouchHelper = new ItemTouchHelper(camerasCallback);
        camerasItemTouchHelper.attachToRecyclerView(mCamerasRecycler);
        mCamerasRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCamerasRecycler.setAdapter(camerasAdapter);

        List<SettingsSwitchModel> energyModel = new ArrayList<>();
        String[] energyTitle = getResources().getStringArray(R.array.electric_energy_names);
        for (String title : energyTitle){
            SettingsSwitchModel model = new SettingsSwitchModel();
            model.setTitle(title);
            model.setChecked(true);
            energyModel.add(model);
        }
        SettingsRecyclerAdapter energyAdapter = new SettingsRecyclerAdapter(energyModel);
        ItemTouchMoveHelperCallback energyCallback = new ItemTouchMoveHelperCallback(mEnergyTouchMoveHelper);
        final ItemTouchHelper energyItemTouchHelper = new ItemTouchHelper(energyCallback);
        energyItemTouchHelper.attachToRecyclerView(mEnergyRecycler);
        mEnergyRecycler.setLayoutManager(new LinearLayoutManager(this));
        mEnergyRecycler.setAdapter(energyAdapter);
    }
}
