package com.livetyping.moydom.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.widget.TextView;

import com.livetyping.moydom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraPermissionActivity extends BaseActivity {
    private static final int REQUEST_CAMERA_CODE = 2;
    private static final int REQUEST_CAMERA_CODE_SETTINGS = 3;

    private static final int STATE_REQUEST_PERMISSION_DIALOG = 0;
    private static final int STATE_REQUEST_PERMISSION_SETTINGS = 1;

    @BindView(R.id.activity_camera_permission_enable_access) TextView mEnableAccessText;

    private int mPermissionState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_permission);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_camera_permission_enter_manually)
    void enterManually(){
        Intent intent = new Intent(this, ManuallyEnterCodeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.activity_camera_permission_enable_access)
    void enableAccess(){
        if (mPermissionState == STATE_REQUEST_PERMISSION_DIALOG) {
            checkCameraPermission();
        } else if (mPermissionState == STATE_REQUEST_PERMISSION_SETTINGS){
            Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(appSettingsIntent, REQUEST_CAMERA_CODE_SETTINGS);
        }
    }

    private void checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // permission was granted
            setResult(RESULT_OK);
            finish();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_CODE_SETTINGS){
            checkCameraPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CAMERA_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                setResult(RESULT_OK);
                finish();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                //TODO enable rationale
            } else {
                showToast(R.string.have_to_go_setting_to_enable_access_to_camera);
                mPermissionState = STATE_REQUEST_PERMISSION_SETTINGS;
                mEnableAccessText.setText(R.string.enable_access_settings);
            }
        }
    }
}
