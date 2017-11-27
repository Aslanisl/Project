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
import com.livetyping.moydom.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

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
                if (mEnableDoneButton && s.length() < CODE_LENGTH){
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
        //TODO call to server
        //if error start intent below
        Intent intent = new Intent(this, CodeNotFoundActivity.class);
        startActivity(intent);
    }
}
