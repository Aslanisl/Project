package com.livetyping.moydom.apiModel.energy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 07.12.2017.
 */

public class TodayEnergyModel {
    private String date;
    private float power;
    private float powerCost;
    private int tariffId;
    private int tariffTypeId;
    private float tariffValue;
    private List<String> tariffNames = new ArrayList<>();
    private List<String> tariffTimes = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public void addPower(float power) {
        this.power = this.power + power;
    }

    public float getPowerCost() {
        return powerCost;
    }

    public void setPowerCost(float powerCost) {
        this.powerCost = powerCost;
    }

    public void addPowerCost(float powerCost) {
        this.powerCost = this.powerCost + powerCost;
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

    public float getTariffValue() {
        return tariffValue;
    }

    public void setTariffValue(float tariffValue) {
        this.tariffValue = tariffValue;
    }

    public void addTariffValue(float tariffValue) {
        this.tariffValue = this.tariffValue + tariffValue;
    }

    public void addTariffName(String tariffName) {
        this.tariffNames.add(tariffName);
    }

    public void addTariffTime(String tariffTime) {
        this.tariffTimes.add(tariffTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  TodayEnergyModel){
            TodayEnergyModel anotherModel = (TodayEnergyModel) obj;
            if (anotherModel.getTariffTimes() != null && tariffTimes != null ){

                if (anotherModel.getTariffTimes().size() != tariffTimes.size())
                    return false;

                for (int i = 0; i < tariffTimes.size(); i++){
                    if (!anotherModel.getTariffTimes().get(i).equals(tariffTimes.get(i)))
                        return false;
                }
            } else if (anotherModel.getTariffTimes() != tariffTimes)
                return false;

            if (anotherModel.getTariffNames() != null && tariffNames != null ){

                if (anotherModel.getTariffNames().size() != tariffNames.size())
                    return false;

                for (int i = 0; i < tariffNames.size(); i++){
                    if (!anotherModel.getTariffNames().get(i).equals(tariffNames.get(i)))
                        return false;
                }
            } else if (anotherModel.getTariffNames() != tariffNames)
                return false;

            return power == anotherModel.power &&
                    powerCost == anotherModel.powerCost &&
                    tariffId == anotherModel.tariffId &&
                    tariffTypeId == anotherModel.tariffTypeId &&
                    tariffValue == anotherModel.tariffValue &&
                    (date == null || date.equals(anotherModel.date));
        }
        return super.equals(obj);
    }

    public List<String> getTariffTimes() {
        return tariffTimes;
    }

    public List<String> getTariffNames() {
        return tariffNames;
    }
}
