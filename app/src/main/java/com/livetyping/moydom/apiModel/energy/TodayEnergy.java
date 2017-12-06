package com.livetyping.moydom.apiModel.energy;

import com.livetyping.moydom.apiModel.BaseModel;

import org.simpleframework.xml.Root;

/**
 * Created by Ivan on 06.12.2017.
 */

@Root(name = "table", strict = false)
public class TodayEnergy extends BaseModel{

    // дата/время по часам
    public static final String DT = "dt";
    // потребленная электрическая энергия за этот час в кВт.ч
    public static final String POWER = "power";
    // стоимость потребленной электрической энергии за этот час в рублях
    public static final String POWER_COST = "power_cost";
    // значение тарифа за этот час в рублях за кВт
    public static final String TARIFF_VALUE = "tariff_value";
    // значение тарифа за этот час в рублях за кВт
    public static final String TARIFF_NAME = "tariff_name";
}
