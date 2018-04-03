package com.livetyping.moydom.presentation.features.base.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.livetyping.moydom.R;
import com.livetyping.moydom.api.ServerCallback;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;

/**
 * Created by Ivan on 02.12.2017.
 */

public class BaseFragment extends Fragment implements ServerCallback{

    protected ProgressDialog mProgressDialog;

    protected void showToast(String message){
        if (getActivity() instanceof BaseActivity){
            ((BaseActivity) getActivity()).showToast(message);
        }
    }

    protected void showToast(@StringRes int resId){
        if (getActivity() instanceof BaseActivity){
            ((BaseActivity) getActivity()).showToast(resId);
        }
    }

    protected void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
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

    public void problemWithInternet(){
        Activity activity = getActivity();
        if (activity instanceof BaseActivity){
            ((BaseActivity)activity).problemWithInternet();
        }
    }

    @Override
    public void onUnknownError(String error) {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity){
            ((BaseActivity)activity).onUnknownError(error);
        }
    }

    @Override
    public void onTimeout() {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity){
            ((BaseActivity)activity).onTimeout();
        }
    }

    @Override
    public void onNetworkError() {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity){
            ((BaseActivity)activity).onNetworkError();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeProgress();
    }
}
