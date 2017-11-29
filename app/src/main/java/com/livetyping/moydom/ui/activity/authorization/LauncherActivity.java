package com.livetyping.moydom.ui.activity.authorization;

import android.content.Intent;
import android.os.Bundle;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.activity.BaseActivity;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //Check for some options
        //Just go to qr activity now
        Intent intent = new Intent(this, QrScannerActivity.class);
        startActivity(intent);
        finish();
    }
}
