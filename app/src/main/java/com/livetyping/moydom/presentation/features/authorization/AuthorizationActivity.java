package com.livetyping.moydom.presentation.features.authorization;

import android.content.Intent;
import android.view.ViewGroup;

import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;
import com.livetyping.moydom.presentation.features.target.activity.NewTargetActivity;
import com.livetyping.moydom.presentation.features.base.fragment.NoInternetDialogFragment;
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.presentation.utils.HelpUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AuthorizationActivity extends BaseActivity implements NoInternetDialogFragment.OnInternetDialogListener {
    private Disposable mAuthorizationDisposable;
    private String mUUID;
    private String mPassword;

    private ViewGroup mProgressContainer;

    protected void callAuthorization(String uuid, ViewGroup progressContainer){
        mProgressContainer = progressContainer;
        showProgress(mProgressContainer);
        Long timeStampLong = System.currentTimeMillis()/1000;
        String timeStamp = timeStampLong.toString();
        String passwordInput = uuid + timeStamp;
        String md5 = HelpUtils.md5(passwordInput);
        String password = null;
        if (md5 != null) {
            if (md5.length() > 16) {
                password = md5.substring(0, 16);
                password = password.toLowerCase();
            }
            if (password != null) {
                mUUID = uuid;
                mPassword = password;
                authorizationUser();
            }
        }
    }

    private void authorizationUser(){
        mAuthorizationDisposable = Api.getApiService().authorizationUser(ApiUrlService.getAuthorizationUrl(mUUID, mPassword))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<BaseModel>(this){
                    @Override
                    protected void onSuccess(BaseModel baseModel) {
                        handlingResult(baseModel);
                    }
                });
    }

    private void handlingResult(BaseModel baseModel){
        if (baseModel != null){
            if (baseModel.containsErrors()){
                unsuccessAuthorization();
            } else {
                Prefs prefs = Prefs.getInstance();
                prefs.savePassword(mPassword);
                prefs.saveUUID(mUUID);
                successAuthorization();
            }
        }
    }

    @Override
    public void onUnknownError(String error) {
        removeProgress(mProgressContainer);
        showToast(error);
    }

    @Override
    public void onTimeout() {
        handlingInternetError();
    }

    @Override
    public void onNetworkError() {
        handlingInternetError();
    }

    protected void handlingInternetError(){
        removeProgress(mProgressContainer);
        NoInternetDialogFragment fragment = NoInternetDialogFragment.newInstance();
        fragment.show(getSupportFragmentManager(), NoInternetDialogFragment.TAG);
    }

    @Override
    public void tryInternetCallAgain() {
        authorizationUser();
    }

    private void successAuthorization(){
        Intent intent = new Intent(this, NewTargetActivity.class);
        intent.putExtra(NewTargetActivity.EDIT, false);
        startActivity(intent);
        finish();
        removeProgress(mProgressContainer);
    }

    private void unsuccessAuthorization(){
        Intent intent = new Intent(AuthorizationActivity.this, CodeNotFoundActivity.class);
        startActivity(intent);
        removeProgress(mProgressContainer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthorizationDisposable != null && !mAuthorizationDisposable.isDisposed())
            mAuthorizationDisposable.dispose();
    }
}
