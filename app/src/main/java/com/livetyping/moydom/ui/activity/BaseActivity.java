package com.livetyping.moydom.ui.activity;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.fragment.NoInternetDialogFragment;
import com.livetyping.moydom.utils.NetworkUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ivan on 25.11.2017.
 */

public class BaseActivity extends AppCompatActivity implements Callback{

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
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }
    protected void removeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onResponse(Call call, Response response) {
        removeProgress();
        onServerResponse(call, response);
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        removeProgress();
        onServerFailure(call, t);
    }

    protected void onServerResponse(Call call, Response response){}

    protected void onServerFailure(Call call, Throwable t){}
}
