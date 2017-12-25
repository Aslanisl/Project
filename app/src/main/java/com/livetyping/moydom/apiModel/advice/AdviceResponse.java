package com.livetyping.moydom.apiModel.advice;

import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.apiModel.Record;

import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ivan on 25.12.2017.
 */
@Root(name = "table", strict = false)
public class AdviceResponse extends BaseModel {
    // ID совета
    public static final String ADVICE_ID = "advice_id";
    // ID фона (тип совета)
    public static final String BACKGROUND_ID = "background_id";
    // URL иконки
    public static final String ICON_URL = "icon_url";
    // заголовок
    public static final String TITLE = "title";
    // текст
    public static final String DESCRIPTION = "description";
    //  текущий статус (0 - новый/непрочитанный)
    public static final String STATUS = "status";
    // дата/время формирования совета в БД
    public static final String DT = "dt";

    public List<AdviceModel> getAdviceModels(){
        List<AdviceModel> models = new ArrayList<>();
        if (records != null){
            for (Record record : records){
                AdviceModel model = new AdviceModel();
                Map<String, String> values = record.getRecords();
                if (values != null){
                    model.setAdviceId(getIntegerFromString(values.get(ADVICE_ID)))
                            .setBackgroundId(getIntegerFromString(values.get(BACKGROUND_ID)))
                            .setIconUrl(values.get(ICON_URL))
                            .setTitle(values.get(TITLE))
                            .setDescription(values.get(DESCRIPTION))
                            .setStatus(getIntegerFromString(values.get(STATUS)))
                            .setDate(values.get(DT));
                }
                if (model.getAdviceId() != 0){
                    models.add(model);
                }
            }
        }

        return models;
    }
}
