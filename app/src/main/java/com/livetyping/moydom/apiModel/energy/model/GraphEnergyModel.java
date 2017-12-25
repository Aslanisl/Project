package com.livetyping.moydom.apiModel.energy.model;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
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
    private List<GraphItemEnergyModel> childModels = new ArrayList<>();

    public void addDayModel(GraphItemEnergyModel model){
        childModels.add(model);
    }

    public List<GraphItemEnergyModel> getGraphItems(){
        return childModels;
    }

    public BarData getGraphData(int type){
        HashMap<String, ArrayList<Float>> values = new HashMap<>();
        ArrayList<Integer> colors = new ArrayList<>();

        for (GraphItemEnergyModel model : childModels){
            if (!values.containsKey(model.getDate())){
                values.put(model.getDate(), new ArrayList<>());
            }
            values.get(model.getDate()).add(model.getPowerCost());
        }


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
                colors.add(Color.YELLOW);
                colors.add(Color.BLUE);
                colors.add(Color.GREEN);

                entry = new BarEntry(calendar.get(field), HelpUtils.listToFloatArray(mapEntry.getValue()));
                entries.add(entry);
                Log.d("***", calendar.get(field) + " " +  new Gson().toJson(HelpUtils.listToFloatArray(mapEntry.getValue())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(entries, (barEntry, t1) -> (int)(barEntry.getX() - t1.getX()));
        BarDataSet barDataSet = new BarDataSet(entries, "rub");
        barDataSet.setColors(colors);
        barDataSet.setHighlightEnabled(true);
        return new BarData(barDataSet);
    }

    public float getWeekPowerCost(){
        float cost = 0;
        if (childModels != null && ! childModels.isEmpty()){
            for (GraphItemEnergyModel model: childModels) {
                cost = cost + model.getPowerCost();
            }
        }
        return cost;
    }

    public float getWeekPower(){
        float power = 0;
        if (childModels != null && ! childModels.isEmpty()){
            for (GraphItemEnergyModel model : childModels){
                power = power + model.getPower();
            }
        }
        return power;
    }

    public String getWeekDate(){
        long startWeekTime = 0;
        long finishWeekTime = 0;
        if (childModels != null && ! childModels.isEmpty()){
            for (GraphItemEnergyModel model : childModels){
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