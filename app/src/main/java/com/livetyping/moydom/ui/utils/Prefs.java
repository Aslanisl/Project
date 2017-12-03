package com.livetyping.moydom.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.livetyping.moydom.App;

/**
 * Created by Ivan on 02.12.2017.
 */

public class Prefs {
    private volatile static Prefs sInstance;

    private static final String PREFERENCES_NAME = "user_preferences";
    private static final String KEY_UUID = "k1";
    private static final String KEY_PASSWORD = "k2";

    private SharedPreferences mSharedPreferences;

    public Prefs() {
        mSharedPreferences = App.getAppContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
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
        return mSharedPreferences.getString(KEY_UUID, null);
    }

    public void savePassword(String password){
        persistString(KEY_PASSWORD, password);
    }

    public String getPassword(){
        return mSharedPreferences.getString(KEY_PASSWORD, null);
    }

    private void persistBoolean(final String key, final boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    private void persistFloat(final String key, final float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    private void persistString(final String key, final String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    private void persistInt(final String key, final int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }
}
