package com.livetyping.moydom.apiModel.energy.model;

import android.graphics.Color;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.livetyping.moydom.ui.activity.settings.EnergySwitchModel;
import com.livetyping.moydom.utils.CalendarUtils;
import com.livetyping.moydom.utils.HelpUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by XlebNick for MoyDom.
 */

public class GraphEnergyModel {
    public static final int ZONE_PEAK = 6;
    public static final int ZONE_NIGHT = 7;
    public static final int ZONE_SEMIPEAK = 8;
    private List<GraphItemEnergyModel> childModels = new ArrayList<>();

    public void addDayModel(GraphItemEnergyModel model){
        childModels.add(model);
    }

    public List<GraphItemEnergyModel> getGraphItems(){
        return childModels;
    }

    public List<ZoneSummary> getZones(){

        List<ZoneSummary> zoneSummaries = new ArrayList<>();
        boolean doesZoneExist;
        for (GraphItemEnergyModel model : childModels){

            doesZoneExist = false;
            for (ZoneSummary summary : zoneSummaries){
                if (summary.id == model.getTariff().getTariffId()){
                    doesZoneExist = true;
                    summary.entriesCount++;
                    summary.totalEnergy += model.getPower();
                    summary.totalEnergyCost += model.getPowerCost();
                    break;
                }
            }

            if (!doesZoneExist){
                ZoneSummary zoneSummary = new ZoneSummary();
                zoneSummary.name = model.getTariff().getTariffName();
                zoneSummary.time = model.getTariff().getTariffTime().replace(';', '\n');
                zoneSummary.id = model.getTariff().getTariffId();
                zoneSummary.entriesCount = 1;
                zoneSummary.totalEnergyCost = model.getPowerCost();
                zoneSummary.totalEnergy = model.getPower();
                zoneSummaries.add(zoneSummary);
            }
        }

        Collections.sort(zoneSummaries, (z1, z2) -> z1.id - z2.id);
        return zoneSummaries;

    }

    public BarData getGraphData(int type){

        Collections.sort(childModels, (graphItemEnergyModel, t1) -> {
            if (graphItemEnergyModel.getStringDate().equals(t1.getStringDate())){
                return graphItemEnergyModel.getTariff().getTariffId() - t1.getTariff().getTariffId();
            } else {
                try {
                    return graphItemEnergyModel.getDate().compareTo(t1.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        HashMap<String, ArrayList<Float>> values = new HashMap<>();
        ArrayList<Integer> colors = new ArrayList<>();

        for (GraphItemEnergyModel model : childModels){
            if (!values.containsKey(model.getStringDate())){
                values.put(model.getStringDate(), new ArrayList<>());
            }
            values.get(model.getStringDate()).add(model.getPowerCost());
            if (model.getTariff().getTariffId() == ZONE_PEAK) {
                colors.add(Color.parseColor("#343d94"));
            } else if (model.getTariff().getTariffId() == ZONE_NIGHT) {
                colors.add(Color.parseColor("#ff5b91"));

            } else if (model.getTariff().getTariffId() == ZONE_SEMIPEAK) {
                colors.add(Color.parseColor("#ffc13c"));

            }
        }
//
//        if (type == EnergySwitchModel.ENERGY_TYPE_TODAY){
//            for (int i = 0; i < 7; i++) {
//
//                colors.add(Color.parseColor("#ff5b91"));
//            }
//            for (int i = 0; i < 3; i++) {
//
//                colors.add(Color.parseColor("#343d94"));
//            }
//            for (int i = 0; i < 7; i++) {
//
//                colors.add(Color.parseColor("#ffc13c"));
//            }
//            for (int i = 0; i < 4; i++) {
//
//                colors.add(Color.parseColor("#343d94"));
//            }
//            for (int i = 0; i < 2; i++) {
//
//                colors.add(Color.parseColor("#ffc13c"));
//            }
//            colors.add(Color.parseColor("#ff5b91"));
//        } else {
//
//        }


        List<BarEntry> entries = new ArrayList<>();
        BarEntry entry;
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        int field;
        if (type == EnergySwitchModel.ENERGY_TYPE_TODAY){
            field = Calendar.HOUR_OF_DAY;
        } else if (type == EnergySwitchModel.ENERGY_TYPE_WEEK || type == EnergySwitchModel.ENERGY_TYPE_THIS_MONTH){
            field = Calendar.DAY_OF_MONTH;
        } else {
            field = Calendar.MONTH;

        }
        Calendar calendar = Calendar.getInstance();
        for (Map.Entry<String, ArrayList<Float>> mapEntry : values.entrySet()){
            try {
                calendar.setTime(sdfIn.parse(mapEntry.getKey()));
                if (type == EnergySwitchModel.ENERGY_TYPE_TODAY)
                    entry = new BarEntry(Integer.parseInt(mapEntry.getKey().substring(11, 13)), HelpUtils.listToFloatArray(mapEntry.getValue()));
                else
                    entry = new BarEntry(calendar.get(field), HelpUtils.listToFloatArray(mapEntry.getValue()));
                entries.add(entry);
             } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(entries, (barEntry, t1) -> (int)(barEntry.getX() - t1.getX()));

        BarDataSet barDataSet = new BarDataSet(entries, "rub");
        barDataSet.setColors(colors);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
        return new BarData(barDataSet);
    }

    public float getTotalPowerCost(){
        float cost = 0;
        if (childModels != null && ! childModels.isEmpty()){
            for (GraphItemEnergyModel model: childModels) {
                cost = cost + model.getPowerCost();
            }
        }
        return cost;
    }

    public float getTotalPower(){
        float power = 0;
        if (childModels != null && ! childModels.isEmpty()){
            for (GraphItemEnergyModel model : childModels){
                power = power + model.getPower();
            }
        }
        return power;
    }


    public String getDataText(){
        StringBuilder result = new StringBuilder();
        for (GraphItemEnergyModel model : childModels){
            result.append(String.format(Locale.getDefault(),
                    "%s: %.1f кВт•ч на сумму %.1f по тарифу %s\n",
                    model.getStringDate(),
                    model.getPower(),
                    model.getPowerCost(),
                    model.getTariff().getTariffName()));
        }
        return result.toString();
    }
    public float getAveragePowerCost(){
        float cost = 0;
        if (childModels != null && ! childModels.isEmpty()){
            for (GraphItemEnergyModel model: childModels) {
                cost = cost + model.getPowerCost();
            }
            cost /= childModels.size();
        }
        return cost;
    }

    public float getAveragePower(){
        float power = 0;
        if (childModels != null && ! childModels.isEmpty()){
            for (GraphItemEnergyModel model : childModels){
                power = power + model.getPower();
            }
            power /= childModels.size();
        }
        return power;
    }

    public String getWeekDate(){
        long startWeekTime = 0;
        long finishWeekTime = 0;
        if (childModels != null && ! childModels.isEmpty()){
            for (GraphItemEnergyModel model : childModels){
                long currentTime = CalendarUtils.getTimeMillisFromServerDate(model.getStringDate());
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

    public class ZoneSummary{
        public String name;
        public String time;
        public int id;
        public int entriesCount;
        public float totalEnergy;
        public float totalEnergyCost;

    }

}