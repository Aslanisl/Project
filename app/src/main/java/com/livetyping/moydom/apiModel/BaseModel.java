package com.livetyping.moydom.apiModel;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.Map;

/**
 * Created by Ivan on 29.11.2017.
 */

@Root(name = "table", strict = false)
public class BaseModel {
    @ElementList(name = "records", required=false)
    protected List<Record> records;

    public List<Record> getRecords() {
        return records;
    }

    public boolean containsErrors() {
        boolean containErrors = false;
        if (records != null && !records.isEmpty()) {
            Map<String, String> errors = records.get(0).getRecords();
            if (errors != null) {
                containErrors = errors.containsKey(Record.ERROR_CODE);
            }
        }
        return containErrors;
    }

    public int getErrorCode() {
        int code = -1;
        if (records != null && !records.isEmpty()) {
            Map<String, String> errors = records.get(0).getRecords();
            if (errors != null) {
                code = Integer.parseInt(errors.get(Record.ERROR_CODE));
            }
        }
        return code;
    }

    public String getErrorMessage(){
        String message = "";
        if (records != null && !records.isEmpty()) {
            Map<String, String> errors = records.get(0).getRecords();
            if (errors != null) {
                message = errors.get(Record.ERROR_USER_MESSAGE);
            }
        }
        return message;
    }
    //Get value from first record
    protected String getValue(String key){
        if (records == null || records.isEmpty()) return "";
        Map<String, String> recordsValue = records.get(0).getRecords();
        String value = null;
        if (recordsValue != null) value = recordsValue.get(key);
        if (value != null) return value;
        return "";
    }

    protected int getIntegerFromString(String text){
        int number = 0;
        try {
            number = Integer.valueOf(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return number;
    }

    protected float getFloatFromString(String text){
        float number = 0;
        try {
            number = Float.valueOf(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return number;
    }
}
