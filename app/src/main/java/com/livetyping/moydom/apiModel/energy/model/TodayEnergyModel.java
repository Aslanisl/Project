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

    public List<String> getTariffNames() {
        return tariffNames;
    }

    public void addTariffName(String tariffName) {
        this.tariffNames.add(tariffName);
    }

    public List<String> getTariffTimes() {
        return tariffTimes;
    }

    public void addTariffTime(String tariffTime) {
        this.tariffTimes.add(tariffTime);
    }
}
