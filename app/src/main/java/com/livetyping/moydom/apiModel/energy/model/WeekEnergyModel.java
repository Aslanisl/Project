package com.livetyping.moydom.apiModel.energy.model;

import com.livetyping.moydom.apiModel.energy.model.TodayEnergyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 07.12.2017.
 */

public class WeekEnergyModel {
    private List<TodayEnergyModel> dayModels = new ArrayList<>();

    public void addDayModel(TodayEnergyModel model){
        dayModels.add(model);
    }

    public List<TodayEnergyModel> getWeekDays(){
        return dayModels;
    }

    public float getWeekPowerCost(){
        float cost = 0;
        if (dayModels != null && !dayModels.isEmpty()){
            for (TodayEnergyModel model: dayModels) {
                cost = cost + model.getPowerCost();
            }
        }
        return cost;
    }
}
