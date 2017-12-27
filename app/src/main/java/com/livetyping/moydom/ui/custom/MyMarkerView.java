package com.livetyping.moydom.ui.custom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

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
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = height;
        view.setLayoutParams(lp);
        view.invalidate();
        lp = getLayoutParams();
        lp.height = height;
        setLayoutParams(lp);
        invalidate();
    }
    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            mOffset = new MPPointF(-(getWidth() / 2) , -getHeight()- 800);
        }

        return mOffset;
    }
}