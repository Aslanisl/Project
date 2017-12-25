package com.livetyping.moydom.api;

import com.livetyping.moydom.apiModel.BaseModel;
import com.livetyping.moydom.apiModel.advice.AdviceResponse;
import com.livetyping.moydom.apiModel.appeal.AppealResponse;
import com.livetyping.moydom.apiModel.cameras.CamerasResponse;
import com.livetyping.moydom.apiModel.energy.response.CurrentEnergyResponse;
import com.livetyping.moydom.apiModel.energy.response.MonthEnergyResponse;
import com.livetyping.moydom.apiModel.energy.response.WeekEnergyResponse;
import com.livetyping.moydom.apiModel.myTarget.AverageEnergyCostResponse;

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

    @GET
    Observable<CurrentEnergyResponse> getCurrentEnergy(@Url String url);

    @GET
    Observable<WeekEnergyResponse> getTodayWeekEnergy(@Url String url);

    @GET
    Observable<MonthEnergyResponse> getMonthEnergy(@Url String url);

    @GET
    Observable<CamerasResponse> getCameras(@Url String url);

    @GET
    Observable<AppealResponse> getAddresses(@Url String url);

    @GET
    Observable<AverageEnergyCostResponse> getAverageEnergyCost(@Url String url);

    @GET
    Observable<AdviceResponse> getAdvice(@Url String url);

    @GET
    Observable<BaseModel> changeAdviceStatus(@Url String url);
}
