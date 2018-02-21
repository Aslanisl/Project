package com.livetyping.moydom.apiModel.energy.response;

import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.apiModel.Record;
import com.livetyping.moydom.apiModel.energy.model.TodayEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.WeekEnergyModel;
import com.livetyping.moydom.utils.CalendarUtils;

import org.simpleframework.xml.Root;

import java.util.Map;

/**
 * Created by Ivan on 06.12.2017.
 */

@Root(name = "table", strict = false)
public class MonthGraphEnergyResponse extends BaseModel{

    // дата/время по часам
    public static final String DT = "dt";
    // потребленная электрическая энергия за этот час в кВт.ч
    public static final String POWER = "power";
    // стоимость потребленной электрической энергии за этот час в рублях
    public static final String POWER_COST = "power_cost";
    public static final String TARIFF_ID = "tariff_id";
    public static final String TARIFF_TYPE_ID = "tariff_type_id";
    // значение тарифа за этот час в рублях за кВт
    public static final String TARIFF_VALUE = "tariff_value";
    // значение тарифа за этот час в рублях за кВт
    public static final String TARIFF_NAME = "tariff_name";
    public static final String TARIFF_TIME = "tariff_time";

    public String getDt(){
        return getValue(DT);
    }

    public String getPower(){
        return getValue(POWER);
    }

    public String getPowerCost(){
        return getValue(POWER_COST);
    }

    public String getTariffValue(){
        return getValue(TARIFF_VALUE);
    }

    public String getTariffName(){
        return getValue(TARIFF_NAME);
    }

    public TodayEnergyModel getTodayEnergyModel(){
        if (records != null && !records.isEmpty()) {
            for (int i = 0; i < records.size(); i++) {
                Record record = records.get(i);
                Map<String, String> values = record.getRecords();
                if (values != null) {
                    String recordDate = values.get(DT);
                    if (recordDate != null && CalendarUtils.isTodayDate(recordDate)) {
                        return getDayEnergyModel(values);
                    }
                }
            }
        }
        return new TodayEnergyModel();
    }

    private TodayEnergyModel getDayEnergyModel(Map<String, String> values){
        TodayEnergyModel model = new TodayEnergyModel();
        if (model.getDate() == null) model.setDate(values.get(DT));
        model.addPower(getFloatFromString(values.get(POWER)));
        model.addPowerCost(getFloatFromString(values.get(POWER_COST)));
        if (model.getTariffId() == 0)
            model.setTariffId(getIntegerFromString(values.get(TARIFF_ID)));
        if (model.getTariffTypeId() == 0)
            model.setTariffTypeId(getIntegerFromString(values.get(TARIFF_TYPE_ID)));
        model.addTariffValue(getFloatFromString(values.get(TARIFF_VALUE)));
        model.addTariffName(values.get(TARIFF_NAME));
        model.addTariffTime(values.get(TARIFF_TIME));
        return model;
    }

    public WeekEnergyModel getWeekEnergyModel() {
        WeekEnergyModel model = new WeekEnergyModel();
        if (records != null && !records.isEmpty()) {
            for (int i = 0; i < records.size(); i++) {
                Record record = records.get(i);
                Map<String, String> values = record.getRecords();
                if (values != null) {
                    model.addDayModel(getDayEnergyModel(values));
                }
            }
        }
        return model;
    }
}