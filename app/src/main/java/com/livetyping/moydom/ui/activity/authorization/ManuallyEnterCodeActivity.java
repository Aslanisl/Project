package com.livetyping.moydom.ui.activity.authorization;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.livetyping.moydom.R;
import com.livetyping.moydom.utils.HelpUtils;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManuallyEnterCodeActivity extends AuthorizationActivity {

    private static final int CODE_LENGTH = 16;

    private boolean mEnableDoneButton;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_manually_container) LinearLayout mContainer;
    @BindView(R.id.activity_manually_code_edit) EditText mCodeEdit;

    private String mCode;

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

    @Override
    protected void onStart() {
        super.onStart();
        checkCode();
    }

    @Override
    protected void handlingInternetError() {
        super.handlingInternetError();
        checkCode();
    }

    private void initEditCode(){
        mCodeEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                callCode();
                return true;
            }
            return false;
        });

        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "[____] [____] [____] [____]",
                false,
                mCodeEdit,
                null,
                (maskFilled, extractedValue) ->{
                    mCode = extractedValue;
                    checkCode();
                }
        );

        mCodeEdit.addTextChangedListener(listener);
        mCodeEdit.setOnFocusChangeListener(listener);

        HelpUtils.focusEditSoft(mCodeEdit, this);
    }

    private void checkCode(){
        if (mCode != null) {
            if (mCode.length() == CODE_LENGTH) {
                enableOptionMenu(true);
            } else if (mEnableDoneButton) {
                enableOptionMenu(false);
            }
        }
    }

    private void enableOptionMenu(boolean enable){
        mEnableDoneButton = enable;
        invalidateOptionsMenu();
    }

    private void callCode(){
        enableOptionMenu(false);
        HelpUtils.hideSoftKeyborad(this);
        callAuthorization(mCode, mContainer);
    }
}
