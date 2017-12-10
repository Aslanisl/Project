package com.livetyping.moydom.apiModel.cameras;

import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.apiModel.Record;

import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ivan on 08.12.2017.
 */
@Root(name = "table", strict = false)
public class CamerasResponse extends BaseModel{
    // ID видеокамеры
    public static final String CAMERA_ID = "camera_id";
    // описание (название) видеокамеры
    public static final String CAMERA_NAME = "camera_name";
    // URL видеокамеры
    public static final String CAMERA_URL = "camera_url";
    // статус работоспособности видеокамеры (0 - В эфире, 1 - Нет сигнала с камеры)
    public static final String CAMERA_STATUS = "camera_status";

    public List<CameraModel> getCamerasModel(){
        List<CameraModel> cameras = new ArrayList<>();
        if (records != null){
            for (Record record : records){
                CameraModel model = new CameraModel();
                Map<String, String> values = record.getRecords();
                if (values != null){
                    model.setCameraId(getIntegerFromString(values.get(CAMERA_ID)));
                    model.setCameraName(values.get(CAMERA_NAME));
                    model.setCameraUrl(values.get(CAMERA_URL));
                    model.setCameraStatus(getIntegerFromString(values.get(CAMERA_STATUS)));
                }
                cameras.add(model);
            }
        }
        return cameras;
    }
}
