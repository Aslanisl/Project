package com.livetyping.moydom.ui.activity.settings;

import android.text.TextUtils;

/**
 * Created by Ivan on 10.12.2017.
 */

public class CamerasSwitchModel {

    private int cameraId;
    private String cameraTitle;
    private boolean cameraChecked;

    public CamerasSwitchModel() {
    }

    public CamerasSwitchModel(String packed){
        String[] unpack = packed.split("_");
        try {
            this.cameraId =  Integer.valueOf(unpack[0]);
            this.cameraTitle = unpack[1];
            this.cameraChecked = Boolean.valueOf(unpack[2]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public String pack(){
        return TextUtils.join("_", new String[] {String.valueOf(cameraId), cameraTitle, String.valueOf(cameraChecked)});
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraTitle() {
        return cameraTitle;
    }

    public void setCameraTitle(String cameraTitle) {
        this.cameraTitle = cameraTitle;
    }

    public boolean isCameraChecked() {
        return cameraChecked;
    }

    public void setCameraChecked(boolean cameraChecked) {
        this.cameraChecked = cameraChecked;
    }
}
