package com.livetyping.moydom.ui;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by XlebNick for MoyDom.
 */

public class MyMarkerView extends com.github.mikephil.charting.components.MarkerView {

    private TextView tvContent;
    private MPPointF mOffset;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
    }

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            mOffset = new MPPointF(-(getWidth() / 2) , -getHeight()- 240);
        }

        return mOffset;
    }
}