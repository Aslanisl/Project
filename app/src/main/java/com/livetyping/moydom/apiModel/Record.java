package com.livetyping.moydom.apiModel;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.Map;

/**
 * Created by Ivan on 29.11.2017.
 */
@Root(name = "record")
public class Record {
    public static final String CODE = "code";
    public static final String CODE_OK = "0";
    public static final String ERROR_CODE = "error_code";
    public static final String ERROR_USER_MESSAGE = "error_user_message";
    public static final String ERROR_INTERNAL_MESSAGE = "error_internal_message";

    @ElementMap(entry="value", key="name", attribute=true, inline=true)
    protected Map<String, String> records;

    public Map<String, String> getRecords() {
        return records;
    }
}
