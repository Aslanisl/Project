package com.livetyping.moydom.ui.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by XlebNick for MoyDom.
 */

public class MyViewPager extends ViewPager {
    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(
            @NonNull Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        return v.getClass() == SwipeableTextView.class ||
                v.getClass().getName().startsWith("com.github.mikephil") ||
                super.canScroll(v, checkV, dx, x, y);
    }
}
