package com.livetyping.moydom.presentation.features.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.livetyping.moydom.R;
import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;
import com.livetyping.moydom.presentation.features.base.fragment.NoInternetDialogFragment;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.presentation.utils.HelpUtils;
import com.livetyping.moydom.presentation.utils.NetworkUtil;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EnterPhoneActivity extends BaseActivity implements MaskedTextChangedListener.ValueListener,
        NoInternetDialogFragment.OnInternetDialogListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_enter_phone_container) LinearLayout mContainer;
    @BindView(R.id.activity_enter_phone_edit) EditText mPhoneEdit;

    private boolean mEnableDoneButton = false;
    private String mPhone;

    private Disposable mSendPhoneDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone);
        ButterKnife.bind(this);
        mToolbar.setNavigationIcon(R.drawable.close);
        mToolbar.setTitle(R.string.phone);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener((v) -> onBackPressed());

        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "+7 ([000]) [000] [00] [00]",
                true,
                mPhoneEdit,
                null,
                this
        );

        mPhoneEdit.addTextChangedListener(listener);
        mPhoneEdit.setOnFocusChangeListener(listener);
        mPhoneEdit.setHint(listener.placeholder());
        HelpUtils.focusEditSoft(mPhoneEdit, this);
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
                HelpUtils.hideSoftKeyboard(this);
                enableOptionMenu(false);
                sendPhone();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
        if (maskFilled){
            enableOptionMenu(true);
            mPhone = extractedValue;
        } else if (mEnableDoneButton){
            enableOptionMenu(false);
        }
    }

    private void enableOptionMenu(boolean enable){
        mEnableDoneButton = enable;
        invalidateOptionsMenu();
    }

    private void sendPhone(){
        showProgress(mContainer);
        mSendPhoneDisposable = Api.getApiService().sendPhone(ApiUrlService.getCallbackPhoneUrl(mPhone))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<BaseModel>(this) {
                    @Override
                    protected void onSuccess(BaseModel baseModel) {
                        handlingResult(baseModel);
                    }
                });
    }

    private void handlingResult(BaseModel model){
        if (model != null) {
            if (model.containsErrors()) {
                showToast(model.getErrorMessage());
                enableOptionMenu(true);
                removeProgress(mContainer);
            } else {
                Intent intent = new Intent(this, QrScannerActivity.class);
                intent.putExtra(QrScannerActivity.KEY_ALERT_DIALOG_FROM_PHONE, true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                removeProgress(mContainer);
            }
        }
    }

    @Override
    public void onUnknownError(String error) {
        removeProgress(mContainer);
        enableOptionMenu(true);
        showToast(error);
    }

    @Override
    public void onTimeout() {
        handlingError();
    }

    @Override
    public void onNetworkError() {
        handlingError();
    }

    private void handlingError(){
        removeProgress(mContainer);
        enableOptionMenu(true);
        if (!NetworkUtil.isConnected(this)) {
            NoInternetDialogFragment fragment = NoInternetDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(), NoInternetDialogFragment.TAG);
        }
    }

    @Override
    public void tryInternetCallAgain() {
        sendPhone();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSendPhoneDisposable != null && !mSendPhoneDisposable.isDisposed()) mSendPhoneDisposable.dispose();
    }
}
