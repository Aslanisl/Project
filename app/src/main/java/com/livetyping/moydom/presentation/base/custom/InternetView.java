package com.livetyping.moydom.presentation.base.custom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.livetyping.moydom.R;
import com.livetyping.moydom.presentation.utils.NetworkUtil;
import com.livetyping.moydom.presentation.utils.ViewUtils;

/**
 * Created by Ivan on 27.12.2017.
 */

public class InternetView extends LinearLayout {

    public static final String TAG = InternetView.class.getSimpleName();
    private static final int CONNECTED_ANIMATION_DELAYED = 1000;
    private static final int CONNECTING_DELAYED = 3000;

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
    private Handler mHandler;
    private Runnable mInternetDisconnected;
    private Runnable mInternetConnected;
    private Runnable mInternetConnecting;

    private RelativeLayout mContainer;
    private TextView mTitle;
    private ImageView mReload;
    private ProgressBar mProgressBar;
    private ImageView mDone;

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
        mReload = findViewById(R.id.custom_internet_view_reload);
        mProgressBar = findViewById(R.id.custom_internet_view_progress);
        mProgressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(getContext(), R.color.gray), PorterDuff.Mode.SRC_IN );
        mReload.setOnClickListener(view -> {
            mState = State.CONNECTING;
            changeState();
        });
        mDone = findViewById(R.id.custom_internet_view_done);
        setVisibility(NetworkUtil.isConnected(getContext()) ? GONE : VISIBLE);

        mHandler = new Handler();
        mInternetConnected = () -> {
            if (getVisibility() == VISIBLE) ViewUtils.collapse(this);
        };
        mInternetDisconnected = () -> {
            if (getVisibility() == GONE) ViewUtils.expand(this);
        };
        mInternetConnecting = () -> {
            mState = State.DISCONNECTED;
            changeState();
        };
    }

    private void changeState(){
        switch (mState){
            case CONNECTED:
                mTitle.setText(R.string.connected_with_internet);
                mProgressBar.setVisibility(GONE);
                mReload.setVisibility(GONE);
                mDone.setVisibility(VISIBLE);
                mContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.internet_connected_color));
                mHandler.removeCallbacks(mInternetConnecting);
                mHandler.postDelayed(mInternetConnected, CONNECTED_ANIMATION_DELAYED);
                break;
            case CONNECTING:
                mTitle.setText(R.string.connecting_with_internet);
                mProgressBar.setVisibility(VISIBLE);
                mReload.setVisibility(GONE);
                mDone.setVisibility(GONE);
                mContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.internet_connecting_color));
                mHandler.postDelayed(mInternetConnecting, CONNECTING_DELAYED);
                break;
            case DISCONNECTED:
                mTitle.setText(R.string.no_connection_with_internet);
                mReload.setVisibility(VISIBLE);
                mProgressBar.setVisibility(GONE);
                mDone.setVisibility(GONE);
                mContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.internet_not_connected_color));
                mHandler.postDelayed(mInternetDisconnected, 0);
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
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Context context = getContext();
        if (context != null) {
            context.unregisterReceiver(mConnectedReceiver);
        }
        if (mHandler != null) {
            mHandler.removeCallbacks(mInternetDisconnected);
            mHandler.removeCallbacks(mInternetConnected);
        }
    }
}
