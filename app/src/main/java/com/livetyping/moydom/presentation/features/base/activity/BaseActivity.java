package com.livetyping.moydom.presentation.features.base.activity;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.livetyping.moydom.R;
import com.livetyping.moydom.api.ServerCallback;
import com.livetyping.moydom.presentation.features.base.custom.CustomButtonView;
import com.livetyping.moydom.presentation.features.base.custom.InternetView;
import com.livetyping.moydom.presentation.utils.GlideApp;
import com.livetyping.moydom.presentation.utils.HelpUtils;

/**
 * Created by Ivan on 25.11.2017.
 */

public class BaseActivity extends AppCompatActivity implements ServerCallback{

    private static final int LOADING_CONTAINER_WIDTH_HEIGHT_NORMAL = 80;
    private static final int LOADING_CONTAINER_WIDTH_HEIGHT_SMALL = 64;

    protected ProgressDialog mProgressDialog;
    private Toast mToast;

    private FrameLayout mLoadingContainer;
    private ImageView mLoadingView;

    public void showToast(String message){
        if (mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void showToast(@StringRes int resId){
        if (mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        mToast.show();
    }

    //Anchor to view bottom
    protected void setUpInternetView(View container, View anchorView) {
        if (container != null && anchorView != null) {
            if (container instanceof RelativeLayout) {
                InternetView internetView = new InternetView(this);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.addRule(RelativeLayout.BELOW, anchorView.getId());
                internetView.setLayoutParams(layoutParams);
                ((RelativeLayout) container).addView(internetView);
            } else {
                Log.d(InternetView.TAG, "Container to internet view must be relative layout");
            }
        } else {
            throw new NullPointerException("Container or anchorView must not be null");
        }
    }

    protected void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.please_wait));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    protected void removeProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void showProgress(ViewGroup view){
        if (mLoadingContainer == null){
            mLoadingContainer = new FrameLayout(this);
            mLoadingContainer.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            mLoadingView = new ImageView(this);
            setLayoutParams(view);
            mLoadingContainer.addView(mLoadingView);
            GlideApp.with(this).load(R.drawable.loader).into(mLoadingView);
        } else if (mLoadingView != null){
            setLayoutParams(view);
        }
        mLoadingContainer.setClickable(false);
        for (int i = 0; i < view.getChildCount(); i++) {
            View childView = view.getChildAt(i);
            childView.setVisibility(View.GONE);
        }
        view.removeView(mLoadingContainer);
        view.addView(mLoadingContainer);
    }

    private void setLayoutParams(ViewGroup view){
        FrameLayout.LayoutParams imageLayoutParams = new FrameLayout.LayoutParams(
                HelpUtils.dpToPx(
                        view instanceof CustomButtonView
                                ? LOADING_CONTAINER_WIDTH_HEIGHT_SMALL
                                : LOADING_CONTAINER_WIDTH_HEIGHT_NORMAL,
                        this
                ),
                HelpUtils.dpToPx(
                        view instanceof CustomButtonView
                                ? LOADING_CONTAINER_WIDTH_HEIGHT_SMALL
                                : LOADING_CONTAINER_WIDTH_HEIGHT_NORMAL,
                        this
                )
        );
        imageLayoutParams.gravity = Gravity.CENTER;
        mLoadingView.setLayoutParams(imageLayoutParams);
    }

    protected void removeProgress(ViewGroup view){
        if (mLoadingContainer != null && view != null) {
            mLoadingContainer.setClickable(true);
            view.removeView(mLoadingContainer);
            for (int i = 0; i < view.getChildCount(); i++) {
                View childView = view.getChildAt(i);
                childView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onUnknownError(String error) {
        //TODO implementation
        showToast(error);
    }

    @Override
    public void onTimeout() {
        //TODO implementation
        removeProgress();
    }

    @Override
    public void onNetworkError() {
        //TODO implementation
        removeProgress();
    }
    //Show dialog or something with not internet interface
    public void problemWithInternet(){
        //TODO implementation
        removeProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeProgress();
    }
}
