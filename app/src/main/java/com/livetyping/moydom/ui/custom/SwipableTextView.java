package com.livetyping.moydom.ui.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by XlebNick for MoyDom.
 */


public class SwipableTextView extends AppCompatTextView {


    private static final float SWIPE_MIN_DISTANCE = 50;
    private static final float SWIPE_MIN_SPEED = 7;
    private long mSwipeGesturePreviousTime;
    private float mSwipeGestureStartX;
    private float mSwipeGesturePreviousX;
    private SwipeListener swipeListener;

    public SwipableTextView(Context context) {
        super(context);
    }

    public SwipableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSwipeListener(SwipeListener listener){
        this.swipeListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {

        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSwipeGesturePreviousTime = System.currentTimeMillis();
                mSwipeGestureStartX = - me.getRawX() + getX();

                break;
            case MotionEvent.ACTION_MOVE:
                mSwipeGesturePreviousTime = System.currentTimeMillis();
                mSwipeGesturePreviousX = me.getX();
                setTranslationX(me.getRawX() + mSwipeGestureStartX);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float totalDistance = Math.abs(me.getX() - mSwipeGestureStartX);
                float lastSpeed = Math.abs(me.getX() - mSwipeGesturePreviousX) /
                        (System.currentTimeMillis() - mSwipeGesturePreviousTime);
                Log.d("***",
                        totalDistance + " " + Math.abs(me.getX() - mSwipeGesturePreviousX) + " " +
                                lastSpeed);
                if (totalDistance > SWIPE_MIN_DISTANCE || -totalDistance < -SWIPE_MIN_DISTANCE) {
                    animate()
                            .translationX(500)
                            .alpha(0)
                            .setDuration(300)
                            .withEndAction(() -> {
                                if (swipeListener != null){
                                    swipeListener.onSwipe(totalDistance > 0);
                                }
                            })
                            .start();
                }
                return false;
        }
        return true;
    }

    public interface SwipeListener{
        public void onSwipe(boolean isSwipeOnLeft);
    }
}
