package com.livetyping.moydom.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

/**
 * Created by Ivan on 25.11.2017.
 */

public interface Endpoint {

    String API_CONTEXT = "users.admin.models.mobiles";
    String FUNCTION_SET_PASSWORD = "set_pass";

    @GET("/rest/")
    Call<ResponseBody> authorizationUser(@Query("p_context") String apiContext,
                                         @Query("p_function") String function,
                                         @QueryName String uuid,
                                         @QueryName String password);
}
