package com.livetyping.moydom.api;

import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.utils.CalendarUtils;

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
    public static final String FUNCTION_WEEK_ON_DAY_ENERGY = "get_electric_grafs_week";
    public static final String FUNCTION_MONTH_ENERGY = "get_electric_month";
    public static final String FUNCTION_CAMERAS = "get_cameras";
    public static final String FUNCTION_ADDRESSES = "get_addressee";

    public static String getAuthorizationUrl(String uuid, String password){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_SET_PASSWORD).append("&");
        url.append(uuid).append("&");
        url.append(password);
        return url.toString();
    }

    public static String getCallbackPhoneUrl(String phone){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_SEND_PHONE).append("&&&");
        url.append(phone);
        return url.toString();
    }

    public static String getCurrentEnergyUrl(){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_CURRENT_ENERGY).append("&");
        url.append(getUuidPassword(false));
        return url.toString();
    }

    public static String getCamerasUrl(){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_CAMERAS).append("&");
        url.append(getUuidPassword(false));
        return url.toString();
    }

    public static String getTodayWeekEnergyUrl(){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_WEEK_ON_DAY_ENERGY).append("&");
        url.append(getUuidPassword(true));
        url.append(CalendarUtils.getCurrentDate());
        return url.toString();
    }

    public static String getMonthEnergyUrl(){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_MONTH_ENERGY).append("&");
        url.append(getUuidPassword(true));
        url.append(CalendarUtils.getCurrentMonth()).append("&");
        url.append(CalendarUtils.getCurrentYear());
        return url.toString();
    }

    public static String getAddressesUrl(){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_ADDRESSES).append("&");
        url.append(getUuidPassword(false));
        return url.toString();
    }

    private static String getUuidPassword(boolean withQuery){
        StringBuilder url = new StringBuilder();
        Prefs prefs = Prefs.getInstance();
        url.append(prefs.getUUID()).append("&");
        url.append(prefs.getPassword());
        if (withQuery) url.append("&");
        return url.toString();
    }

    private static String getBaseOptions(){
        StringBuilder url = new StringBuilder();
        url.append(MAIN_METHOD);
        url.append("p_operation=").append(OPERATION_CALL).append("&");
        url.append("p_username=").append(USERNAME).append("&");
        url.append("p_password=").append(PASSWORD).append("&");
        url.append("p_context=").append(API_CONTEXT).append("&");
        return url.toString();
    }
}
