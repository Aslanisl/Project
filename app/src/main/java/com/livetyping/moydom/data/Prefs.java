package com.livetyping.moydom.data;

import android.content.Context;
import android.text.TextUtils;

import com.ironz.binaryprefs.BinaryPreferencesBuilder;
import com.ironz.binaryprefs.Preferences;
import com.livetyping.moydom.App;
import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.activity.settings.CamerasSwitchModel;
import com.livetyping.moydom.ui.activity.settings.EnergySwitchModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 02.12.2017.
 */

public class Prefs {
    private volatile static Prefs sInstance;

    private static final String PREFERENCES_NAME = "user_preferences";
    private static final String KEY_UUID = "k1";
    private static final String KEY_PASSWORD = "k2";
    private static final String KEY_CAMERAS_FILTER = "k5";
    private static final String KEY_ENERGY_FILTER = "k6";
    private static final String KEY_TARGET_PERCENT = "k7";
    private static final String KEY_FIRST_LAUNCH = "k8";

    private Preferences mPreferences;

    private Prefs() {
        mPreferences = new BinaryPreferencesBuilder(App.getAppContext())
                .name(PREFERENCES_NAME)
                .build();
    }

    public static synchronized Prefs getInstance() {
        if (sInstance == null) {
            sInstance = new Prefs();
        }
        return sInstance;
    }

    public boolean isFirstLaunch(){
        return mPreferences.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public void setFirstLaunch(boolean showed){
        persistBoolean(KEY_FIRST_LAUNCH, showed);
    }

    public void saveUUID(String uuid){
        persistString(KEY_UUID, uuid);
    }

    public String getUUID(){
        return mPreferences.getString(KEY_UUID, null);
    }

    public void savePassword(String password){
        persistString(KEY_PASSWORD, password);
    }

    public String getPassword(){
        return mPreferences.getString(KEY_PASSWORD, null);
    }

    public void saveEnergyFilters(List<EnergySwitchModel> filter){
        StringBuilder filterText = new StringBuilder();
        for (int i = 0; i < filter.size(); i++){
            EnergySwitchModel model = filter.get(i);
            filterText.append(model.pack());
            //Don't add to last item
            if (i < filter.size() - 1){
                filterText.append(";");
            }
        }
        persistString(KEY_ENERGY_FILTER, filterText.toString());
    }

    public List<EnergySwitchModel> getEnergyFilters(){
        List<EnergySwitchModel> models = new ArrayList<>();
        String listPacked = mPreferences.getString(KEY_ENERGY_FILTER, "");
        if (TextUtils.isEmpty(listPacked)) {
            createEnergyFilters();
            listPacked = mPreferences.getString(KEY_ENERGY_FILTER, "");
        }
        for (String s : listPacked.split(";")){
            EnergySwitchModel model = new EnergySwitchModel(s);
            models.add(model);
        }
        return models;
    }

    private void createEnergyFilters(){
        Context appContext = App.getAppContext();
        List<EnergySwitchModel> energyModel = new ArrayList<>();
        String[] energyTitle = appContext.getResources().getStringArray(R.array.electric_energy_names);
        for (String title : energyTitle) {
            EnergySwitchModel model = new EnergySwitchModel();
            model.setTitle(title);
            model.setChecked(true);
            if (title.contains(appContext.getString(R.string.electric_energy_current))) {
                model.setType(EnergySwitchModel.ENERGY_TYPE_CURRENT);
            } else if (title.contains(appContext.getString(R.string.electric_energy_this_day))) {
                model.setType(EnergySwitchModel.ENERGY_TYPE_TODAY);
            } else if (title.contains(appContext.getString(R.string.electric_energy_this_week))) {
                model.setType(EnergySwitchModel.ENERGY_TYPE_WEEK);
            } else if (title.contains(appContext.getString(R.string.electric_energy_this_month))) {
                model.setType(EnergySwitchModel.ENERGY_TYPE_THIS_MONTH);
            }
            energyModel.add(model);
        }
        saveEnergyFilters(energyModel);
    }

    public void saveCamerasFilters(List<CamerasSwitchModel> filter){
        StringBuilder filterText = new StringBuilder();
        for (int i = 0; i < filter.size(); i++){
            CamerasSwitchModel model = filter.get(i);
            filterText.append(model.pack());
            //Don't add to last item
            if (i < filter.size() - 1){
                filterText.append(";");
            }
        }
        persistString(KEY_CAMERAS_FILTER, filterText.toString());
    }

    public List<CamerasSwitchModel> getCamerasFilters(){
        List<CamerasSwitchModel> models = new ArrayList<>();
        String listPacked = mPreferences.getString(KEY_CAMERAS_FILTER, "");
        if (TextUtils.isEmpty(listPacked)) {
            return models;
        }
        for (String s : listPacked.split(";")){
            CamerasSwitchModel model = new CamerasSwitchModel(s);
            models.add(model);
        }
        return models;
    }

    public float getTargetPercent(){
        return mPreferences.getFloat(KEY_TARGET_PERCENT, 0);
    }

    public void saveTargetPercent(float percent){
        persistFloat(KEY_TARGET_PERCENT, percent);
    }

    private void persistBoolean(final String key, final boolean value) {
        mPreferences.edit().putBoolean(key, value).commit();
    }

    private void persistFloat(final String key, final float value) {
        mPreferences.edit().putFloat(key, value).commit();
    }

    private void persistString(final String key, final String value) {
        mPreferences.edit().putString(key, value).commit();
    }

    private void persistInt(final String key, final int value) {
        mPreferences.edit().putInt(key, value).commit();
    }
}
