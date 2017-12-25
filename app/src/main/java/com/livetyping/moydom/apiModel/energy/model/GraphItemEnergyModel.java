package com.livetyping.moydom.apiModel.energy.model;

/**
 * Created by XlebNick for MoyDom.
 */

public class GraphItemEnergyModel {
    private String date;
    private float power;
    private float powerCost;
    private Tariff tariff;

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

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }


    public static class Tariff{
        private float tariffValue;
        private int tariffTypeId;
        private String tariffTime;
        private String tariffName;
        private int tariffId;

        public Tariff(int tariffId, int tariffTypeId, float tariffValue, String tariffName, String tariffTime) {

            this.tariffValue = tariffValue;
            this.tariffTypeId = tariffTypeId;
            this.tariffTime = tariffTime;
            this.tariffName = tariffName;
            this.tariffId = tariffId;
        }

        public float getTariffValue() {
            return tariffValue;
        }

        public int getTariffTypeId() {
            return tariffTypeId;
        }

        public String getTariffTime() {
            return tariffTime;
        }

        public String getTariffName() {
            return tariffName;
        }

        public int getTariffId() {
            return tariffId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Tariff){
                return ((Tariff) obj).tariffId == tariffId;
            }
            return super.equals(obj);
        }
    }
}
