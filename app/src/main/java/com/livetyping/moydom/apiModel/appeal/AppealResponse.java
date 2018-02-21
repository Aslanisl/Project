package com.livetyping.moydom.apiModel.appeal;

import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.apiModel.Record;

import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ivan on 13.12.2017.
 */
@Root(name = "table", strict = false)
public class AppealResponse extends BaseModel {
    // ID адресата
    public static final String ADDRESSES_ID = "addressee_id";
    // категория обращения
    public static final String ADDRESSES_TYPE_NAME = "addressee_type_name";
    // адресат
    public static final String ADDRESSES_NAME = "addressee_name";
    // e-mail адресата
    public static final String ADDRESSES_EMAIL = "addressee_email";

    public List<AppealModel> getAppealModels(){
        List<AppealModel> models = new ArrayList<>();
        if (records != null){
            for (Record record : records){
                AppealModel model = new AppealModel();
                Map<String, String> values = record.getRecords();
                if (values != null){
                    model.setId(getIntegerFromString(values.get(ADDRESSES_ID)));
                    model.setTypeName(values.get(ADDRESSES_TYPE_NAME));
                    model.setName(values.get(ADDRESSES_NAME));
                    model.setEmail(values.get(ADDRESSES_EMAIL));
                }
                models.add(model);
            }
        }
        return models;
    }
}
