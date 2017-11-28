package com.livetyping.moydom.ui.activity.authorization;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.livetyping.moydom.R;
import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.Endpoint;
import com.livetyping.moydom.ui.activity.BaseActivity;
import com.livetyping.moydom.utils.HelpUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorizationActivity extends BaseActivity {
    private Call<ResponseBody> mAuthorizationCall;

    protected void callAuthorization(String uuid){
        showProgress();
        Long timeStampLong = System.currentTimeMillis()/1000;
        String timeStamp = timeStampLong.toString();
        String passwordInput = uuid + timeStamp;
        String md5 = HelpUtils.md5(passwordInput);
        String password = null;
        if (md5.length() > 16){
            password = md5.substring(0, 16);
        }
        if (password != null) {
            mAuthorizationCall = Api.getApiService().authorizationUser(Endpoint.API_CONTEXT, Endpoint.FUNCTION_SET_PASSWORD, uuid, password);
            mAuthorizationCall.enqueue(this);
        }
    }

    @Override
    protected void onServerResponse(Call call, Response response) {
        showToast(response.body().toString());
        removeProgress();
    }

    @Override
    protected void onServerFailure(Call call, Throwable t) {
        showToast(t.getMessage());
        removeProgress();
        Intent intent = new Intent(AuthorizationActivity.this, CodeNotFoundActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthorizationCall != null) mAuthorizationCall.cancel();
    }
}
