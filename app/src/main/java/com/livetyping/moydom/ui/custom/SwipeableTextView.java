package com.livetyping.moydom.ui.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by XlebNick for MoyDom.
 */


public class SwipeableTextView extends AppCompatTextView {


    private static final float SWIPE_MIN_DISTANCE = 50;
    private static final float SWIPE_MIN_SPEED = 7;
    private long mSwipeGesturePreviousTime;
    private float mSwipeGestureStartX;
    private float mSwipeGesturePreviousX;
    private SwipeListener swipeListener;
    private boolean isRightSwipeable = true;

    public SwipeableTextView(Context context) {
        super(context);
    }

    public SwipeableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSwipeListener(SwipeListener listener) {
        this.swipeListener = listener;
    }

    public void setRightSwipeable(boolean swipeable) {
        this.isRightSwipeable = swipeable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {

        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSwipeGesturePreviousTime = System.currentTimeMillis();
                mSwipeGestureStartX = - me.getRawX() + getX();

                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("***", me.getRawX() + mSwipeGestureStartX  + " " + mSwipeGesturePreviousX);
                if (!(!isRightSwipeable && me.getRawX() + mSwipeGestureStartX < 0)) {
                    mSwipeGesturePreviousTime = System.currentTimeMillis();
                    mSwipeGesturePreviousX = me.getRawX() + mSwipeGestureStartX;
                    setTranslationX(me.getRawX() + mSwipeGestureStartX);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (! (!isRightSwipeable && getTranslationX() < 0)) {
                    float totalDistance = getTranslationX();
                    if (totalDistance > SWIPE_MIN_DISTANCE) {
                        animate()
                                .translationX(500)
                                .alpha(0)
                                .setDuration(300)
                                .withEndAction(() -> {
                                    if (swipeListener != null) {
                                        swipeListener.onSwipe(true);
                                    }
                                })
                                .start();
                        Log.d("***", "left");
                    }
                    if (totalDistance < -SWIPE_MIN_DISTANCE) {
                        animate()
                                .translationX(-500)
                                .alpha(0)
                                .setDuration(300)
                                .withEndAction(() -> {
                                    if (swipeListener != null) {
                                        swipeListener.onSwipe(false);
                                    }
                                })
                                .start();
                        Log.d("***", "right");
                    }
                }
                return false;
        }
        return true;
    }

    public interface SwipeListener {
        public void onSwipe(boolean isSwipeOnLeft);
    }
}
