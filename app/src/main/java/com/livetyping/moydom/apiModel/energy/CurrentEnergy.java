package com.livetyping.moydom.apiModel.energy;

import com.livetyping.moydom.apiModel.BaseModel;

import org.simpleframework.xml.Root;

/**
 * Created by Ivan on 06.12.2017.
 */
@Root(name = "table", strict = false)
public class CurrentEnergy extends BaseModel {
    // текущее значение потребляемой электрической мощности в кВт
    public static final String POWER_NOW = "power_now";
    // статус текущего значения потребляемой электрической мощности в кВт (0 - не проверяется, 1 - мало, 2 - как обычно, 3 - много)
    public static final String POWER_NOW_STATUS = "power_now_status";
    // текущее потребление электричества в рублях/час
    public static final String COST_NOW = "cost_now";
    // статус текущего потребления электричества в рублях/час (0 - не проверяется, 1 - мало, 2 - как обычно, 3 - много)
    public static final String COST_NOW_STATUS = "cost_now_status";
    public static final String TARIFF_ID = "tariff_id";
    public static final String TARIFF_TYPE_ID = "tariff_type_id";
    // название действующего тарифа
    public static final String TARIFF_NAME = "tariff_name";
    // значение действующего тарифа в рублях за кВт
    public static final String TARIFF_VALUE = "tariff_value";

    public String getPowerNow(){
        return getValue(POWER_NOW);
    }

    public String getPowerNowStatus(){
        return getValue(POWER_NOW_STATUS);
    }

    public String getCostNow(){
        return getValue(COST_NOW);
    }

    public String getCostNowStatus(){
        return getValue(COST_NOW_STATUS);
    }

    public String getTariffId(){
        return getValue(TARIFF_ID);
    }

    public String getTariffTypeId(){
        return getValue(TARIFF_TYPE_ID);
    }

    public String getTariffName(){
        return getValue(TARIFF_NAME);
    }

    public String getTariffValue(){
        return getValue(TARIFF_VALUE);
    }
}
