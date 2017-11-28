package com.livetyping.moydom.ui.activity.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.livetyping.moydom.R;
import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.Endpoint;
import com.livetyping.moydom.ui.activity.BaseActivity;
import com.livetyping.moydom.utils.HelpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManuallyEnterCodeActivity extends BaseActivity {

    private static final int CODE_LENGTH = 16;
    private boolean mEnableDoneButton;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_manually_code_edit) EditText mCodeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_enter_code);
        ButterKnife.bind(this);
        mToolbar.setNavigationIcon(R.drawable.close);
        mToolbar.setTitle(R.string.enter_manually);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener((v) -> onBackPressed());
        initEditCode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_done);
        if (mEnableDoneButton){
            item.setEnabled(true);
            item.setIcon(R.drawable.accept_active);
        } else {
            item.setEnabled(false);
            item.setIcon(R.drawable.accept_disable);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                callCode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initEditCode(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEnableDoneButton && s.length() != CODE_LENGTH){
                    mEnableDoneButton = false;
                    invalidateOptionsMenu();
                } else if (!mEnableDoneButton && s.length() >= CODE_LENGTH){
                    mEnableDoneButton = true;
                    invalidateOptionsMenu();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        mCodeEdit.addTextChangedListener(textWatcher);
        mCodeEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                callCode();
                return true;
            }
            return false;
        });
    }

    private void callCode(){
        String uuid = mCodeEdit.getText().toString();
        Long timeStampLong = System.currentTimeMillis()/1000;
        String timeStamp = timeStampLong.toString();
        String passwordInput = uuid + timeStamp;
        String md5 = HelpUtils.md5(passwordInput);
        String password = null;
        if (md5.length() > 16){
            password = md5.substring(0, 16);
        }
        if (password != null) {
            Call<ResponseBody> call = Api.getApiService().authorizationUser(Endpoint.API_CONTEXT, Endpoint.FUNCTION_SET_PASSWORD, uuid, password);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    showToast(response.body().toString());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showToast(t.getMessage());
                    Intent intent = new Intent(ManuallyEnterCodeActivity.this, CodeNotFoundActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
