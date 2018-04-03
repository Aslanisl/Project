package com.livetyping.moydom.presentation.features.otherSettings.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.livetyping.moydom.R;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;
import com.livetyping.moydom.presentation.features.otherSettings.activity.MyTargetActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherSettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_settings);
        ButterKnife.bind(this);
        mToolbar.setTitle(R.string.settings);
        mToolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @OnClick(R.id.activity_other_settings_intercom)
    void intercomClicked(){
        //TODO go to intercom settings
    }

    @OnClick(R.id.activity_other_settings_notifications)
    void notificationsClicked(){
        //TODO go to notifications settings
    }

    @OnClick(R.id.activity_other_settings_my_target)
    void myTargetClicked(){
        Intent intent = new Intent(this, MyTargetActivity.class);
        startActivity(intent);
    }
}
