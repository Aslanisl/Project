package com.livetyping.moydom.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.livetyping.moydom.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Ivan on 25.11.2017.
 */

public class Api {
    public static final String BASE_URL = "https://agtest.opk-bulat.ru";

    private static volatile Endpoint mAPIServiceInstance;

    public static Endpoint getApiService() {
        Endpoint localInstance = mAPIServiceInstance;
        if (localInstance == null) {
            synchronized (Endpoint.class) {
                localInstance = mAPIServiceInstance;
                if (localInstance == null) {
                    Retrofit retrofit = Api.getRetrofit();
                    mAPIServiceInstance = localInstance = retrofit.create(Endpoint.class);
                }
            }
        }
        return localInstance;
    }

    static Retrofit getRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) builder.addInterceptor(interceptor);

        OkHttpClient client = builder.build();

        Gson gson = new GsonBuilder().create();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();
    }
}
