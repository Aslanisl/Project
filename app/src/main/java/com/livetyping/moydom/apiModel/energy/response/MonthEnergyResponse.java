package com.livetyping.moydom.apiModel.energy.response;

import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.apiModel.energy.model.MonthEnergyModel;

import org.simpleframework.xml.Root;

/**
 * Created by Ivan on 07.12.2017.
 */
@Root(name = "table", strict = false)
public class MonthEnergyResponse extends BaseModel{
  // потребленная электрическая энергия с первого числа месяца в кВт.ч
  public static final String POWER_MONTH = "power_month";
  // стоимость потребленной электрической энергии с первого числа по текущий момент в рублях
  public static final String COST_MONTH = "cost_month";
  // прогноз (аппроксимация) потребления электрической энергии на конец месяца в кВт.ч
  public static final String POWER_TOTAL = "power_total";
  // прогноз (аппроксимация) стоимости потребленной электрической энергии на конец месяца в рублях
  public static final String COST_TOTAL = "cost_total";
  //  статус значения потребленной электрической энергии на конец месяца в кВт.ч (0 - не проверяется, 1 - мало, 2 - как обычно, 3 - много)
  public static final String POWER_TOTAL_STATUS = "power_total_status";
  // статус стоимости потребленной электрической энергии на конец месяца в рублях (0 - не проверяется, 1 - мало, 2 - как обычно, 3 - много)
  public static final String COST_TOTAL_STATUS = "cost_total_status";

  public MonthEnergyModel getMonthEnergyModel(){
    MonthEnergyModel model = new MonthEnergyModel();
    model.setPowerMonth(getFloatFromString(getPowerMonth()));
    model.setCostMonth(getFloatFromString(getCostMonth()));
    model.setPowerTotal(getFloatFromString(getPowerTotal()));
    model.setCostTotal(getFloatFromString(getCostTotal()));
    model.setPowerTotalStatus(getIntegerFromString(getPowerTotalStatus()));
    model.setCostTotalStatus(getIntegerFromString(getCostTotalStatus()));
    return model;
  }

  public String getPowerMonth() {
    return getValue(POWER_MONTH);
  }

  public String getCostMonth() {
    return getValue(COST_MONTH);
  }

  public String getPowerTotal() {
    return getValue(POWER_TOTAL);
  }

  public String getCostTotal() {
    return getValue(COST_TOTAL);
  }

  public String getPowerTotalStatus() {
    return getValue(POWER_TOTAL_STATUS);
  }

  public String getCostTotalStatus() {
    return getValue(COST_TOTAL_STATUS);
  }
}
