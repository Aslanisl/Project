package com.livetyping.moydom.presentation.features.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.livetyping.moydom.R;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;
import com.livetyping.moydom.presentation.features.main.activity.MainActivity;
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.presentation.features.target.activity.NewTargetActivity;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //Check for some options
        Prefs prefs = Prefs.getInstance();
        String uuid = prefs.getUUID();
        String password = prefs.getPassword();
        float targetCost = prefs.getTargetCost();
        float targetPercent = prefs.getTargetPercent();
        boolean firstLaunch = prefs.isFirstLaunch();
        if (firstLaunch){
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            finish();
        } else if (TextUtils.isEmpty(uuid) || TextUtils.isEmpty(password)) {
            Intent intent = new Intent(this, QrScannerActivity.class);
            startActivity(intent);
            finish();
        } else if (targetCost == 0 || targetPercent == 0){
            Intent intent = new Intent(this, NewTargetActivity.class);
            intent.putExtra(NewTargetActivity.EDIT, false);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
