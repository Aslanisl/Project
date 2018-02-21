package com.livetyping.moydom.ui.custom;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Build;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.HashMap;

/**
 * Created by XlebNick for MoyDom.
 */

public class ChartDataRenderer extends BarChartRenderer {
    public ChartDataRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mBarBorderPaint.setColor(dataSet.getBarBorderColor());
        mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getBarBorderWidth()));

        final boolean drawBorder = dataSet.getBarBorderWidth() > 0.f;

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        // initialize the buffer
        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));
        buffer.setBarWidth(mChart.getBarData().getBarWidth());

        buffer.feed(dataSet);

        trans.pointValuesToPixel(buffer.buffer);

        final boolean isSingleColor = dataSet.getColors().size() == 1;

        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }

        LinearGradient gradient;
        int color1 = 0, color2 = 0;
        HashMap<Float, Float> maxValues = new HashMap<>();
        for (int j = 0; j < buffer.size(); j += 4) {
            if (maxValues.containsKey(buffer.buffer[j])) {
                if (maxValues.get(buffer.buffer[j]) > buffer.buffer[j + 1]) {
                    maxValues.put(buffer.buffer[j], buffer.buffer[j + 1]);
                }
            } else {
                maxValues.put(buffer.buffer[j], buffer.buffer[j + 1]);
            }
        }
        for (int j = 0; j < buffer.size(); j += 4) {

            if (! mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) { continue; }

            if (! mViewPortHandler.isInBoundsRight(buffer.buffer[j])) { break; }

            try {
                if (! isSingleColor) {
                    // Set the color for the currently drawn value. If the index
                    // is out of bounds, reuse colors.
                    mRenderPaint.setColor(dataSet.getColor(j / 4));
                    if (dataSet.getColor(j / 4) == Color.parseColor("#ff5b91")) {
                        color2 = Color.parseColor("#d23285");
                        color1 = Color.parseColor("#ff5b91");
                    } else if (dataSet.getColor(j / 4) == Color.parseColor("#343d94")) {
                        color2 = Color.parseColor("#343d94");
                        color1 = Color.parseColor("#6d2dd3");
                    } else if (dataSet.getColor(j / 4) == Color.parseColor("#ffc13c")) {
                        color1 = Color.parseColor("#ffc13c");
                        color2 = Color.parseColor("#ff7e69");
                    }

                }
            } catch (ArithmeticException e) {
                e.printStackTrace();
            }

            gradient = new LinearGradient(0, buffer.buffer[j + 1], 0,
                    c.getHeight(), color1, color2, Shader.TileMode.MIRROR);
            mRenderPaint.setShader(gradient);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                    maxValues.get(buffer.buffer[j]) == buffer.buffer[j + 1]) {
                c.drawPath(drawRoundedRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3], 3, 3, true), mRenderPaint);

            } else {
                c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3], mRenderPaint);
            }

            if (drawBorder) {
                c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3], mBarBorderPaint);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Path drawRoundedRect(float left, float top, float right, float bottom, float rx, float ry, boolean conformToOriginalPost) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width/2) rx = width/2;
        if (ry > height/2) ry = height/2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.arcTo(right - 2*rx, top, right, top + 2*ry, 0, -90, false); //top-right-corner
        path.rLineTo(-widthMinusCorners, 0);
        path.arcTo(left, top, left + 2*rx, top + 2*ry, 270, -90, false);//top-left corner.
        path.rLineTo(0, heightMinusCorners);
        if (conformToOriginalPost) {
            path.rLineTo(0, ry);
            path.rLineTo(width, 0);
            path.rLineTo(0, -ry);
        }
        else {
            path.arcTo(left, bottom - 2 * ry, left + 2 * rx, bottom, 180, -90, false); //bottom-left corner
            path.rLineTo(widthMinusCorners, 0);
            path.arcTo(right - 2 * rx, bottom - 2 * ry, right, bottom, 90, -90, false); //bottom-right corner
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.
        return path;
    }
}
