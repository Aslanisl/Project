package com.livetyping.moydom.ui.activity;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.livetyping.moydom.R;
import com.livetyping.moydom.api.ServerCallback;
import com.livetyping.moydom.ui.custom.InternetView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ivan on 25.11.2017.
 */

public class BaseActivity extends AppCompatActivity implements ServerCallback{

    protected ProgressDialog mProgressDialog;
    private Toast mToast;

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
