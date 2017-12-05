package com.livetyping.moydom.api;

import com.livetyping.moydom.apiModel.BaseModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Ivan on 25.11.2017.
 */

public interface Endpoint {

    @GET
    Observable<BaseModel> authorizationUser(@Url String url);

    @GET
    Observable<BaseModel> sendPhone(@Url String url);
}
