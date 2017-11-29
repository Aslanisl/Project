package com.livetyping.moydom.ui.activity.authorization;

import android.content.Intent;

import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.model.BaseModel;
import com.livetyping.moydom.model.Error;
import com.livetyping.moydom.ui.activity.BaseActivity;
import com.livetyping.moydom.ui.activity.MainActivity;
import com.livetyping.moydom.ui.fragment.NoInternetDialogFragment;
import com.livetyping.moydom.utils.HelpUtils;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class AuthorizationActivity extends BaseActivity {
    private Call<BaseModel> mAuthorizationCall;

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
            mAuthorizationCall = Api.getApiService().authorizationUser(ApiUrlService.getAuthorizationUrl(uuid, password));
            mAuthorizationCall.enqueue(this);
        }
    }

    @Override
    protected void onServerResponse(Call call, Response response) {
        if (response.body() instanceof BaseModel && response.body() != null){
            BaseModel model = (BaseModel) response.body();
            List<Error> errorRecords = model.getErrorRecords();
            if (errorRecords != null && !errorRecords.isEmpty()){
                Map<String, String> errors = errorRecords.get(0).getErrors();
                if (errors != null){
                    if (errors.containsKey(Error.CODE) && errors.get(Error.CODE).equals(Error.CODE_OK)){
                        successAuthorization();
                    } else {
                        unsuccessAuthorization();
                    }
                }
            }
        }
    }

    private void successAuthorization(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void unsuccessAuthorization(){
        Intent intent = new Intent(AuthorizationActivity.this, CodeNotFoundActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthorizationCall != null) mAuthorizationCall.cancel();
    }
}
