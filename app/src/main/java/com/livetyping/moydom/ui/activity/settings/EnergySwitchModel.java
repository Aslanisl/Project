package com.livetyping.moydom.ui.activity.settings;

import android.text.TextUtils;

/**
 * Created by Ivan on 06.12.2017.
 */

public class EnergySwitchModel extends SettingsSwitchModel {
    public static final int TYPE_TODAY = 0;
    public static final int TYPE_THIS_WEEK = 1;
    public static final int TYPE_THIS_MONTH = 2;
    public static final int TYPE_THIS_YEAR = 3;

    private int type;

    private int price;
    private int energyWorth;

    public EnergySwitchModel(){
    }

    public EnergySwitchModel(String packed) {
        super(packed);
        String[] unpack = packed.split("_");
        if (unpack.length > 3) {
            this.type = Integer.valueOf(unpack[3]);
        }
    }

    @Override
    public String pack() {
        return TextUtils.join("_", new String[] {title, String.valueOf(checked), String.valueOf(position), String.valueOf(type)});
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getEnergyWorth() {
        return energyWorth;
    }

    public void setEnergyWorth(int energyWorth) {
        this.energyWorth = energyWorth;
    }
}
