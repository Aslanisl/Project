package com.livetyping.moydom.apiModel.cameras;

/**
 * Created by Ivan on 08.12.2017.
 */

public class CameraModel {
    public static final int CAMERA_ONLINE = 0;
    public static final int CAMERA_OFFLINE = 1;

    private int cameraId;
    private String cameraName;
    private String cameraUrl;
    private int cameraStatus;

    public int getCameraId() {
        return cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public String getCameraUrl() {
        return cameraUrl;
    }

    public int getCameraStatus() {
        return cameraStatus;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public void setCameraUrl(String cameraUrl) {
        this.cameraUrl = cameraUrl;
    }

    public void setCameraStatus(int cameraStatus) {
        this.cameraStatus = cameraStatus;
    }
}
