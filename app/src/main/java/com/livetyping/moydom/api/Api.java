package com.livetyping.moydom.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.livetyping.moydom.BuildConfig;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Ivan on 25.11.2017.
 */

public class Api {
    public static final String BASE_URL = "https://agtest.opk-bulat.ru";
    private static final String OPERATION_CALL = "call";
    private static final String USERNAME = "mobile";
    private static final String PASSWORD = "MoBiLe2017";

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
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //Add hardcore constance to query call
        builder.addInterceptor((chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("p_operation", OPERATION_CALL)
                    .addQueryParameter("p_username", USERNAME)
                    .addQueryParameter("p_password", PASSWORD)
                    .build();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        }));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (BuildConfig.DEBUG) builder.addInterceptor(interceptor);

        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();
    }
}
