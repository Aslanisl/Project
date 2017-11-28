package com.livetyping.moydom.ui.activity.authorization;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.activity.BaseActivity;
import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.redmadrobot.inputmask.PolyMaskTextChangedListener;
import com.redmadrobot.inputmask.helper.Mask;
import com.redmadrobot.inputmask.model.CaretString;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class EnterPhoneActivity extends BaseActivity implements MaskedTextChangedListener.ValueListener{

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_enter_phone_edit) EditText mPhoneEdit;

    private boolean mEnableDoneButton = false;
    private String mPhone;
    private Call<ResponseBody> mSendPhoneCall;

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
                sendPhone();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
        if (maskFilled){
            mEnableDoneButton = true;
            mPhone = extractedValue;
            invalidateOptionsMenu();
        }
    }

    private void sendPhone(){
        showToast(mPhone);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSendPhoneCall != null) mSendPhoneCall.cancel();
    }
}
