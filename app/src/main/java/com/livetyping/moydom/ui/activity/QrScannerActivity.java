package com.livetyping.moydom.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.livetyping.moydom.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QrScannerActivity extends BaseActivity{

    private static final int REQUEST_CAMERA_CODE = 1;
    private static final int REQUEST_CAMERA_CODE_FROM_ACTIVITY = 2;

    @BindView(R.id.activity_qr_scanner_view) DecoratedBarcodeView mScannerView;
    @BindView(R.id.activity_qr_scanner_flash) ImageView mFlashView;

    private boolean mTorchSwitched = false;
    private boolean mAskedPermission = false;
    private BeepManager mBeepManager;
    private String lastText;

    private BarcodeCallback mCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            mScannerView.setStatusText(result.getText());
            mBeepManager.playBeepSoundAndVibrate();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        ButterKnife.bind(this);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            mFlashView.setVisibility(View.GONE);
        }

        mScannerView.decodeContinuous(mCallback);
        mBeepManager = new BeepManager(this);
    }

    @TargetApi(23)
    private void openCameraWithPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mScannerView.resume();
        } else if (!mAskedPermission){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_CODE);
            mAskedPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                mScannerView.resume();
            } else {
                Intent intent = new Intent(this, CameraPermissionActivity.class);
                startActivityForResult(intent, REQUEST_CAMERA_CODE_FROM_ACTIVITY);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_CODE_FROM_ACTIVITY){
            if (resultCode == RESULT_OK) {
                mScannerView.resume();
            } else {
                mAskedPermission = false;
            }
        }
    }

    @OnClick(R.id.activity_qr_scanner_enter_code)
    void enterCode(){
        Intent intent = new Intent(this, ManuallyEnterCodeActivity.class);
        startActivity(intent);
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @OnClick(R.id.activity_qr_scanner_flash)
    void switchFlashlight(){
        if (mTorchSwitched){
            mScannerView.setTorchOff();
            mTorchSwitched = false;
        } else {
            mScannerView.setTorchOn();
            mTorchSwitched = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= 23) {
            openCameraWithPermission();
        } else {
            mScannerView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.pause();
    }
}
