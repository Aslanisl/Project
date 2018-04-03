package com.livetyping.moydom.api;

import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.presentation.utils.CalendarUtils;

import java.util.Date;

/**
 * Created by Ivan on 29.11.2017.
 */

public class ApiUrlService {


    public static final String API_CONTEXT = "users.admin.models.mobiles";
    public static final String FUNCTION_SET_PASSWORD = "set_pass";
    public static final String FUNCTION_SEND_PHONE = "ins_uuid_phones";
    public static final String FUNCTION_CURRENT_ENERGY = "get_electric_now";
    public static final String FUNCTION_WEEK_ON_DAY_ENERGY = "get_electric_grafs_week";
    public static final String FUNCTION_MONTH_ENERGY = "get_electric_month";
    public static final String FUNCTION_CAMERAS = "get_cameras";
    public static final String FUNCTION_ADDRESSES = "get_addressee";
    public static final String FUNCTION_AVERAGE_COST = "get_avg_cost";
    public static final String FUNCTION_ADVICE = "get_advices";
    public static final String FUNCTION_CHANGE_ADVICE_STATUS = "set_advice_status";
    private static final String OPERATION_CALL = "call";
    private static final String USERNAME = "mobile";
    private static final String PASSWORD = "MoBiLe2017";
    private static final String MAIN_METHOD = "rest/";
    private static final String FUNCTION_DAY_GRAPH_ENERGY = "get_electric_grafs_day";
    private static final String FUNCTION_WEEK_GRAPH_ENERGY = "get_electric_grafs_week";
    private static final String FUNCTION_MONTH_GRAPH_ENERGY = "get_electric_grafs_month";
    private static final String FUNCTION_YEAR_GRAPH_ENERGY = "get_electric_grafs_year";

    public static String getAuthorizationUrl(String uuid, String password){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
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
        url.append("p_context=").append(API_CONTEXT).append("&");
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

    private static String getUuidPassword(boolean withQuery){
        StringBuilder url = new StringBuilder();
        Prefs prefs = Prefs.getInstance();
        url.append(prefs.getUUID()).append("&");
        url.append(prefs.getPassword());
        if (withQuery) url.append("&");
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

    public static String getAverageEnergyCostUrl(){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_AVERAGE_COST).append("&");
        url.append(getUuidPassword(false));
        return url.toString();
    }

    public static String getAdviceUrl(){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_ADVICE).append("&");
        url.append(getUuidPassword(false));
        return url.toString();
    }

    public static String getChangeAdviceUrl(int adviceId, int status){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_CHANGE_ADVICE_STATUS).append("&");
        url.append(getUuidPassword(true));
        url.append(String.valueOf(adviceId)).append("&");
        url.append(String.valueOf(status));
        return url.toString();
    }

    public static String getDayGraphEnergyUrl(Date date){

        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_DAY_GRAPH_ENERGY).append("&");
        url.append(getUuidPassword(true));
        url.append(CalendarUtils.getStringFromDate(date));
        return url.toString();
    }

    public static String getWeekGraphEnergyUrl(Date date){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_WEEK_GRAPH_ENERGY).append("&");
        url.append(getUuidPassword(true));
        url.append(CalendarUtils.getStringFromDate(date));
        return url.toString();
    }

    public static String getMonthGraphEnergyUrl(Date date){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_MONTH_GRAPH_ENERGY).append("&");
        url.append(getUuidPassword(false)).append("&");
        url.append(CalendarUtils.getMonthFromDate(date)).append("&");
        url.append(CalendarUtils.getYearFromDate(date));
        return url.toString();
    }

    public static String getYearGraphEnergyUrl(Date date){
        StringBuilder url = new StringBuilder();
        url.append(getBaseOptions());
        url.append("p_function=").append(FUNCTION_YEAR_GRAPH_ENERGY).append("&");
        url.append(getUuidPassword(true));
        url.append(CalendarUtils.getYearFromDate(date));
        return url.toString();
    }

}
