package com.livetyping.moydom.apiModel.energy.response;

import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.apiModel.Record;
import com.livetyping.moydom.apiModel.energy.model.GraphEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.GraphItemEnergyModel;

import org.simpleframework.xml.Root;

import java.util.Map;

/**
 * Created by Nikita for MoyDom.
 */

@Root(name = "table", strict = false)
public class GraphEnergyResponse extends BaseModel{

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

    public GraphEnergyModel fillGraphEnergyModel() {
        GraphEnergyModel model = new GraphEnergyModel();
        if (records != null && !records.isEmpty()) {
            for (int i = 0; i < records.size(); i++) {
                Record record = records.get(i);
                Map<String, String> values = record.getRecords();
                if (values != null) {
                    model.addDayModel(getItemEnergyModel(values));
                }
            }
        }
        return model;
    }

    private GraphItemEnergyModel getItemEnergyModel(Map<String, String> values){
        GraphItemEnergyModel model = new GraphItemEnergyModel();
        if (model.getDate() == null) model.setDate(values.get(DT));
        model.addPower(getFloatFromString(values.get(POWER)));
        model.addPowerCost(getFloatFromString(values.get(POWER_COST)));
        model.setTariff(new GraphItemEnergyModel.Tariff(getIntegerFromString(values.get(TARIFF_ID)),
                getIntegerFromString(values.get(TARIFF_TYPE_ID)),
                getFloatFromString(values.get(TARIFF_VALUE)),
                values.get(TARIFF_NAME),
                values.get(TARIFF_TIME)
                ));
//        if (model.getTariffId() == 0)
//            model.setTariffId(getIntegerFromString(values.get(TARIFF_ID)));
//        if (model.getTariffTypeId() == 0)
//            model.setTariffTypeId(getIntegerFromString(values.get(TARIFF_TYPE_ID)));
//        model.setTariffValue(getFloatFromString(values.get(TARIFF_VALUE)));
//        model.setTariffName(values.get(TARIFF_NAME));
//        model.setTariffTime(values.get(TARIFF_TIME));
        return model;
    }
}