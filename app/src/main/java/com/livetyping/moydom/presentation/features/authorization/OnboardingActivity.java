package com.livetyping.moydom.presentation.features.authorization;

import android.content.Intent;
import android.os.Bundle;

import com.livetyping.moydom.R;
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnboardingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_onboarding_done)
    void onDoneClicked(){
        Prefs prefs = Prefs.getInstance();
        prefs.setFirstLaunch(false);
        Intent intent = new Intent(this, QrScannerActivity.class);
        startActivity(intent);
        finish();
    }
}
