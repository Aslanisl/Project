package com.livetyping.moydom.ui.activity;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.livetyping.moydom.R;
import com.livetyping.moydom.api.ServerCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ivan on 25.11.2017.
 */

public class BaseActivity extends AppCompatActivity implements ServerCallback{

    protected ProgressDialog mProgressDialog;

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(@StringRes int resId){
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onNetworkError() {
        //TODO implementation
    }
    //Show dialog or something with not internet interface
    public void problemWithInternet(){
        //TODO implementation
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeProgress();
    }
}
