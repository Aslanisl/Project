package com.livetyping.moydom.apiModel.energy.model;

import com.livetyping.moydom.apiModel.energy.model.TodayEnergyModel;
import com.livetyping.moydom.utils.CalendarUtils;

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

    public float getWeekPower(){
        float power = 0;
        if (dayModels != null && !dayModels.isEmpty()){
            for (TodayEnergyModel model : dayModels){
                power = power + model.getPower();
            }
        }
        return power;
    }

    public String getWeekDate(){
        long startWeekTime = 0;
        long finishWeekTime = 0;
        if (dayModels != null && !dayModels.isEmpty()){
            for (TodayEnergyModel model : dayModels){
                long currentTime = CalendarUtils.getTimeMillisFromServerDate(model.getDate());
                if (startWeekTime == 0 || finishWeekTime == 0){
                    startWeekTime = currentTime;
                    finishWeekTime = currentTime;
                }
                if (currentTime > finishWeekTime){
                    finishWeekTime = currentTime;
                }
                if (currentTime < startWeekTime){
                    startWeekTime = currentTime;
                }
            }
        }
        return CalendarUtils.getBetweenDateFromTimeMillis(startWeekTime, finishWeekTime);
    }
}
