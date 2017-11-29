package com.livetyping.moydom.api;

import com.livetyping.moydom.model.BaseModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryName;
import retrofit2.http.Url;

/**
 * Created by Ivan on 25.11.2017.
 */

public interface Endpoint {

    @GET
    Call<BaseModel> authorizationUser(@Url String url);

    @GET
    Call<BaseModel> sendPhone(@Url String url);
}
