package com.livetyping.moydom.apiModel.myTarget;

import com.livetyping.moydom.apiModel.BaseModel;

import org.simpleframework.xml.Root;

/**
 * Created by Ivan on 21.12.2017.
 */
@Root(name = "table", strict = false)
public class AverageEnergyCostResponse extends BaseModel {

    // дата/время по часам
    public static final String AVERAGE_COST = "get_avg_cost";

    public float getAverageCost(){
        return getFloatFromString(getValue(AVERAGE_COST));
    }
}
