package com.livetyping.moydom.ui.custom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.livetyping.moydom.R;
import com.livetyping.moydom.utils.NetworkUtil;
import com.livetyping.moydom.utils.ViewUtils;

/**
 * Created by Ivan on 27.12.2017.
 */

public class InternetView extends LinearLayout {

    private static final String TAG = InternetView.class.getSimpleName();
    private static final int CONNECTED_ANIMATION_DELAYED = 1000;

    public enum State{
        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }

    private BroadcastReceiver mConnectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = NetworkUtil.isConnected(getContext());
            mState = isConnected ? State.CONNECTED : State.DISCONNECTED;
            changeState();
        }
    };

    private State mState;
    private Handler mAnimationHandler;
    private Runnable mInternetDisconnected;
    private Runnable mInternetConnected;

    private RelativeLayout mContainer;
    private TextView mTitle;

    public InternetView(Context context) {
        super(context);
        init(context);
    }

    public InternetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InternetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        inflate(context, R.layout.custom_internet_view, this);
        mContainer = findViewById(R.id.custom_internet_view_container);
        mTitle = findViewById(R.id.custom_internet_view_text);

        mAnimationHandler = new Handler();
        mInternetConnected = () -> ViewUtils.collapse(this);
        mInternetDisconnected = () -> ViewUtils.expand(this);
    }

    private void changeState(){
        switch (mState){
            case CONNECTED:
                mTitle.setText(R.string.connected_with_internet);
                mContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.internet_connected_color));
                mAnimationHandler.postDelayed(mInternetConnected, CONNECTED_ANIMATION_DELAYED);
                break;
            case DISCONNECTED:
                mTitle.setText(R.string.no_connection_with_internet);
                mContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.internet_not_connected_color));
                mAnimationHandler.postDelayed(mInternetDisconnected, 0);
                break;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Context context = getContext();
        if (context != null) {
            context.registerReceiver(mConnectedReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        Log.d(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Context context = getContext();
        if (context != null) {
            context.unregisterReceiver(mConnectedReceiver);
        }
        if (mAnimationHandler != null) {
            mAnimationHandler.removeCallbacks(mInternetDisconnected);
            mAnimationHandler.removeCallbacks(mInternetConnected);
        }
        Log.d(TAG, "onDetachedFromWindow");
    }
}
