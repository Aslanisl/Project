package com.livetyping.moydom.ui.activity.settings;

/**
 * Created by Ivan on 04.12.2017.
 */

public class SettingsSwitchModel {

    private String title;
    private boolean checked;
    private int position;

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
