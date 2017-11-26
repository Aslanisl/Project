package com.livetyping.moydom.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.livetyping.moydom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManuallyEnterCodeActivity extends BaseActivity {

    @BindView(R.id.activity_manually_code_toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_enter_code);
        ButterKnife.bind(this);
        mToolbar.setNavigationIcon(R.drawable.close);
        mToolbar.setNavigationOnClickListener((v) -> onBackPressed());
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.enter_manually);
    }
}
