package com.livetyping.moydom.ui.activity.settings;

import android.text.TextUtils;

/**
 * Created by Ivan on 04.12.2017.
 */

public class SettingsSwitchModel {

    private String title;
    private boolean checked;
    private int position;

    public SettingsSwitchModel() {
    }

    public SettingsSwitchModel(String packed){
        String[] unpack = packed.split("_");
        this.title = unpack[0];
        this.checked = Boolean.valueOf(unpack[1]);
        this.position = Integer.valueOf(unpack[2]);
    }

    public String pack(){
        return TextUtils.join("_", new String[] {title, String.valueOf(checked), String.valueOf(position)});
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
}
