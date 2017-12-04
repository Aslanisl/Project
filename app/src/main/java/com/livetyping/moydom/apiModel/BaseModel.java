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
    @ElementList(name = "records")
    private List<Error> records;

    public List<Error> getErrorRecords() {
        return records;
    }

    public boolean containsErrors() {
        boolean containErrors = false;
        if (records != null && !records.isEmpty()) {
            Map<String, String> errors = records.get(0).getErrors();
            if (errors != null) {
                containErrors = errors.containsKey(Error.ERROR_CODE);
            }
        }
        return containErrors;
    }

    public String getErrorMessage(){
        String message = null;
        if (records != null && !records.isEmpty()) {
            Map<String, String> errors = records.get(0).getErrors();
            if (errors != null) {
                message = errors.get(Error.ERROR_USER_MESSAGE);
            }
        }
        return message;
    }
}
