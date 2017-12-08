package com.livetyping.moydom.ui.activity.settings;

import android.text.TextUtils;

/**
 * Created by Ivan on 04.12.2017.
 */

public class SettingsSwitchModel {

    public static final int ENERGY_TYPE_CURRENT = 0;
    public static final int ENERGY_TYPE_TODAY = 1;
    public static final int ENERGY_TYPE_WEEK = 2;
    public static final int ENERGY_TYPE_THIS_MONTH = 3;

    protected String title;
    protected boolean checked;
    protected int position;
    private int type;

    public SettingsSwitchModel() {
    }

    public SettingsSwitchModel(String packed){
        String[] unpack = packed.split("_");
        this.title = unpack[0];
        try {
            this.checked = Boolean.valueOf(unpack[1]);
            this.position = Integer.valueOf(unpack[2]);
            this.type = Integer.valueOf(unpack[3]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public String pack(){
        return TextUtils.join("_", new String[] {title, String.valueOf(checked), String.valueOf(position), String.valueOf(type)});
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public boolean isChecked() {
        return checked;
    }

    public int getPosition() {
        return position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
