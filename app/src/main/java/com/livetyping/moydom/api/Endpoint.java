package com.livetyping.moydom.api;

import com.livetyping.moydom.apiModel.BaseModel;

import retrofit2.Call;
import retrofit2.http.GET;
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
