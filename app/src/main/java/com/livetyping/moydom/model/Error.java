package com.livetyping.moydom.model;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Map;

/**
 * Created by Ivan on 29.11.2017.
 */
@Root(name = "record")
public class Error {
    public static final String CODE = "code";
    public static final String CODE_OK = "0";
    public static final String ERROR_CODE = "error_code";
    public static final String ERROR_USER_MESSAGE = "error_user_message";
    public static final String ERROR_INTERNAL_MESSAGE = "error_internal_message";

    @ElementMap(entry="value", key="name", attribute=true, inline=true)
    private Map<String, String> errors;

    public Map<String, String> getErrors() {
        return errors;
    }
}
