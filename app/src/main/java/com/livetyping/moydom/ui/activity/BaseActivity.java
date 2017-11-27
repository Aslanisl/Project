package com.livetyping.moydom.ui.activity;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.livetyping.moydom.R;

/**
 * Created by Ivan on 25.11.2017.
 */

public class BaseActivity extends AppCompatActivity {

    protected ProgressDialog mProgressDialog;

    protected void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(@StringRes int resId){
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    protected void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.please_wait));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    protected void showProgress(boolean indeterminate) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.please_wait));
            mProgressDialog.setIndeterminate(indeterminate);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.setIndeterminate(indeterminate);
        mProgressDialog.show();
    }
}
