package com.livetyping.moydom.ui.activity.settings;

import android.text.TextUtils;

/**
 * Created by Ivan on 04.12.2017.
 */

public class EnergySwitchModel {

    public static final int ENERGY_TYPE_CURRENT = 0;
    public static final int ENERGY_TYPE_TODAY = 1;
    public static final int ENERGY_TYPE_WEEK = 2;
    public static final int ENERGY_TYPE_THIS_MONTH = 3;
    public static final int ENERGY_TYPE_YEAR = 4;

    protected String title;
    protected boolean checked;
    private int type;

    public EnergySwitchModel() {
    }

    public EnergySwitchModel(String packed){
        String[] unpack = packed.split("_");
        this.title = unpack[0];
        try {
            this.checked = Boolean.valueOf(unpack[1]);
            this.type = Integer.valueOf(unpack[2]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public String pack(){
        return TextUtils.join("_", new String[] {title, String.valueOf(checked), String.valueOf(type)});
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
