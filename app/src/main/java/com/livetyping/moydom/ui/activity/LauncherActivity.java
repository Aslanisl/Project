package com.livetyping.moydom.ui.activity;

import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.livetyping.moydom.R;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //Check for some options
        //Just go to qr activity now
        new IntentIntegrator(this).setOrientationLocked(false).setCaptureActivity(QrScannerActivity.class).initiateScan();
        finish();
    }
}
