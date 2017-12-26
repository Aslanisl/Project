package com.livetyping.moydom.apiModel.myTarget;

/**
 * Created by Ivan on 21.12.2017.
 */

public class MyTargetModel {

    public static final float PERCENT_LOW = 0.05f;
    public static final float PERCENT_NORMAL = 0.1f;
    public static final float PERCENT_HIGH = 0.2f;

    private float percent;

    public MyTargetModel() {
    }

    public MyTargetModel(String packed){
        try {
            this.percent = Float.valueOf(packed);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public String pack(){
        return String.valueOf(percent);
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
