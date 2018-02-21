package com.livetyping.moydom.apiModel.energy.model;

/**
 * Created by Ivan on 07.12.2017.
 */

public class MonthEnergyModel {

    private float powerMonth;
    private float costMonth;
    private float powerTotal;
    private float costTotal;
    private int powerTotalStatus;
    private int costTotalStatus;

    public float getPowerMonth() {
        return powerMonth;
    }

    public void setPowerMonth(float powerMonth) {
        this.powerMonth = powerMonth;
    }

    public float getCostMonth() {
        return costMonth;
    }

    public void setCostMonth(float costMonth) {
        this.costMonth = costMonth;
    }

    public float getPowerTotal() {
        return powerTotal;
    }

    public void setPowerTotal(float powerTotal) {
        this.powerTotal = powerTotal;
    }

    public float getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(float costTotal) {
        this.costTotal = costTotal;
    }

    public int getPowerTotalStatus() {
        return powerTotalStatus;
    }

    public void setPowerTotalStatus(int powerTotalStatus) {
        this.powerTotalStatus = powerTotalStatus;
    }

    public int getCostTotalStatus() {
        return costTotalStatus;
    }

    public void setCostTotalStatus(int costTotalStatus) {
        this.costTotalStatus = costTotalStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  MonthEnergyModel){
            MonthEnergyModel anotherModel = (MonthEnergyModel) obj;
            return
                   powerMonth == anotherModel.powerMonth &&
                   costMonth == anotherModel.costMonth &&
                   powerTotal == anotherModel.powerTotal &&
                   costTotal == anotherModel.costTotal &&
                   powerTotalStatus == anotherModel.powerTotalStatus &&
                   costTotalStatus == anotherModel.costTotalStatus;
        }
        return super.equals(obj);
    }
}
