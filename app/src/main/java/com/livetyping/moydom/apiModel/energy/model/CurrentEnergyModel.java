package com.livetyping.moydom.apiModel.energy.model;

import android.util.Log;

/**
 * Created by Ivan on 07.12.2017.
 */

public class CurrentEnergyModel {

    private float powerNow;
    private int powerNowStatus;
    private float costNow;
    private int costNowStatus;
    private int tariffId;
    private int tariffTypeId;
    private String tariffName;
    private float tariffValue;

    public float getPowerNow() {
        return powerNow;
    }

    public void setPowerNow(float powerNow) {
        this.powerNow = powerNow;
    }

    public int getPowerNowStatus() {
        return powerNowStatus;
    }

    public void setPowerNowStatus(int powerNowStatus) {
        this.powerNowStatus = powerNowStatus;
    }

    public float getCostNow() {
        return costNow;
    }

    public void setCostNow(float costNow) {
        this.costNow = costNow;
    }

    public int getCostNowStatus() {
        return costNowStatus;
    }

    public void setCostNowStatus(int costNowStatus) {
        this.costNowStatus = costNowStatus;
    }

    public int getTariffId() {
        return tariffId;
    }

    public void setTariffId(int tariffId) {
        this.tariffId = tariffId;
    }

    public int getTariffTypeId() {
        return tariffTypeId;
    }

    public void setTariffTypeId(int tariffTypeId) {
        this.tariffTypeId = tariffTypeId;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public float getTariffValue() {
        return tariffValue;
    }

    public void setTariffValue(float tariffValue) {
        this.tariffValue = tariffValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  CurrentEnergyModel){
            CurrentEnergyModel anotherModel = (CurrentEnergyModel) obj;
            Log.d("***", (powerNow == anotherModel.powerNow) + " " +
                    (powerNowStatus == anotherModel.powerNowStatus) + " " +
                    (costNow == anotherModel.costNow) + " " +
                    (costNowStatus == anotherModel.costNowStatus) + " " +
                    (tariffId == anotherModel.tariffId) + " " +
                    (tariffTypeId == anotherModel.tariffTypeId) + " " +
                    tariffName.equals(anotherModel.tariffName) + " " +
                    (tariffValue == anotherModel.tariffValue));
            return
                    powerNow == anotherModel.powerNow &&
                    powerNowStatus == anotherModel.powerNowStatus &&
                    costNow == anotherModel.costNow &&
                    costNowStatus == anotherModel.costNowStatus &&
                    tariffId == anotherModel.tariffId &&
                    tariffTypeId == anotherModel.tariffTypeId &&
                    tariffName.equals(anotherModel.tariffName) &&
                    tariffValue == anotherModel.tariffValue;
        }
        return super.equals(obj);
    }
}
