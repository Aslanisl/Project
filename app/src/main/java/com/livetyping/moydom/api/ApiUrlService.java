package com.livetyping.moydom.api;

/**
 * Created by Ivan on 29.11.2017.
 */

public class ApiUrlService {

    private static final String OPERATION_CALL = "call";
    private static final String USERNAME = "mobile";
    private static final String PASSWORD = "MoBiLe2017";

    public static final String MAIN_METHOD = "rest/";

    public static final String API_CONTEXT = "users.admin.models.mobiles";
    public static final String FUNCTION_SET_PASSWORD = "set_pass";
    public static final String FUNCTION_SEND_PHONE = "ins_uuid_phones";

    public static String getAuthorizationUrl(String uuid, String password){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_context=").append(API_CONTEXT).append("&");
        url.append("p_function=").append(FUNCTION_SET_PASSWORD).append("&");
        url.append(uuid).append("&");
        url.append(password);
        return url.toString();
    }

    private static String getBaseOptions(){
        StringBuilder url = new StringBuilder();
        url.append(MAIN_METHOD);
        url.append("p_operation=").append(OPERATION_CALL).append("&");
        url.append("p_username=").append(USERNAME).append("&");
        url.append("p_password=").append(PASSWORD).append("&");
        return url.toString();
    }
}
