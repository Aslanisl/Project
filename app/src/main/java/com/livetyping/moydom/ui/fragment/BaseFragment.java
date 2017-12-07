package com.livetyping.moydom.ui.fragment;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.activity.BaseActivity;

/**
 * Created by Ivan on 02.12.2017.
 */

public class BaseFragment extends Fragment {

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeProgress();
    }
}
