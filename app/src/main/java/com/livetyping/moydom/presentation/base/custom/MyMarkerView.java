package com.livetyping.moydom.presentation.base.custom;


import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.utils.MPPointF;
import com.livetyping.moydom.R;

/**
 * Created by XlebNick for MoyDom.
 */

public class MyMarkerView extends com.github.mikephil.charting.components.MarkerView {

    private MPPointF mOffset;
    private View view;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        view = findViewById(R.id.line);
    }

    public void setHeight(int height){
        RelativeLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
        lp.height = height;
        view.setLayoutParams(lp);
        view.invalidate();
    }
    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            mOffset = new MPPointF(-(getWidth() / 2) , -getHeight()- 800);
        }

        return mOffset;
    }
}