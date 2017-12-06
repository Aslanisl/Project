package com.livetyping.moydom.api;

import com.livetyping.moydom.data.Prefs;

/**
 * Created by Ivan on 29.11.2017.
 */

public class ApiUrlService {

    private static final String OPERATION_CALL = "call";
    private static final String USERNAME = "mobile";
    private static final String PASSWORD = "MoBiLe2017";

    private static final String MAIN_METHOD = "rest/";

    public static final String API_CONTEXT = "users.admin.models.mobiles";
    public static final String FUNCTION_SET_PASSWORD = "set_pass";
    public static final String FUNCTION_SEND_PHONE = "ins_uuid_phones";
    public static final String FUNCTION_CURRENT_ENERGY = "get_electric_now";

    public static String getAuthorizationUrl(String uuid, String password){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_context=").append(API_CONTEXT).append("&");
        url.append("p_function=").append(FUNCTION_SET_PASSWORD).append("&");
        url.append(uuid).append("&");
        url.append(password);
        return url.toString();
    }

    public static String getCallbackPhoneUrl(String phone){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_context=").append(API_CONTEXT).append("&");
        url.append("p_function=").append(FUNCTION_SEND_PHONE).append("&&&");
        url.append(phone);
        return url.toString();
    }

    public static String getCurrentEnergyUrl(){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_context=").append(API_CONTEXT).append("&");
        url.append("p_function=").append(FUNCTION_CURRENT_ENERGY).append("&");
        Prefs prefs = Prefs.getInstance();
        url.append(prefs.getUUID()).append("&");
        url.append(prefs.getPassword());
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
