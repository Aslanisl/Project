package com.livetyping.moydom.data;

import android.content.Context;
import android.text.TextUtils;

import com.ironz.binaryprefs.BinaryPreferencesBuilder;
import com.ironz.binaryprefs.Preferences;
import com.livetyping.moydom.App;
import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.activity.settings.SettingsSwitchModel;

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
    public static final String KEY_CAMERAS_FILTER = "k3";
    public static final String KEY_ENERGY_FILTER = "k4";

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

    public void saveFilters(List<SettingsSwitchModel> filter, String key){
        StringBuilder filterText = new StringBuilder();
        for (int i = 0; i < filter.size(); i++){
            SettingsSwitchModel model = filter.get(i);
            filterText.append(model.pack());
            //Don't add to last item
            if (i < filter.size() - 1){
                filterText.append(";");
            }
        }
        persistString(key, filterText.toString());
    }

    public List<SettingsSwitchModel> getFilters(String key){
        List<SettingsSwitchModel> models = new ArrayList<>();
        String listPacked = mPreferences.getString(key, "");
        if (TextUtils.isEmpty(listPacked)) {
            createFilters(key);
            listPacked = mPreferences.getString(key, "");
        }
        for (String s : listPacked.split(";")){
            SettingsSwitchModel model = new SettingsSwitchModel(s);
            models.add(model);
        }
        return models;
    }

    private void createFilters(String key){
        Context appContext = App.getAppContext();
        switch (key){
            case KEY_ENERGY_FILTER:
                List<SettingsSwitchModel> energyModel = new ArrayList<>();
                String[] energyTitle = appContext.getResources().getStringArray(R.array.electric_energy_names);
                for (String title : energyTitle){
                    SettingsSwitchModel model = new SettingsSwitchModel();
                    model.setTitle(title);
                    model.setChecked(true);
                    if (title.contains(appContext.getString(R.string.electric_energy_current))) {
                        model.setType(SettingsSwitchModel.ENERGY_TYPE_CURRENT);
                    } else if (title.contains(appContext.getString(R.string.electric_energy_this_day))) {
                        model.setType(SettingsSwitchModel.ENERGY_TYPE_TODAY);
                    } else if (title.contains(appContext.getString(R.string.electric_energy_this_week))) {
                        model.setType(SettingsSwitchModel.ENERGY_TYPE_WEEK);
                    } else if (title.contains(appContext.getString(R.string.electric_energy_this_month))){
                        model.setType(SettingsSwitchModel.ENERGY_TYPE_THIS_MONTH);
                    }
                    energyModel.add(model);
                }
                saveFilters(energyModel, KEY_ENERGY_FILTER);
                break;
            case KEY_CAMERAS_FILTER:
                List<SettingsSwitchModel> camerasModel = new ArrayList<>();
                String[] camerasTitle = appContext.getResources().getStringArray(R.array.cameras_names);
                for (String title : camerasTitle){
                    SettingsSwitchModel model = new SettingsSwitchModel();
                    model.setTitle(title);
                    model.setChecked(true);
                    camerasModel.add(model);
                }
                saveFilters(camerasModel, KEY_CAMERAS_FILTER);
                break;
        }
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
