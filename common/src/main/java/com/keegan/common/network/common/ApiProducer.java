package com.keegan.common.network.common;

import retrofit2.Retrofit;

public class ApiProducer {
    public static <T> T getApiService(Class<T> cls, String baseUrl) {
        Retrofit retrofit = RetrofitUtils.getRetrofitBuilder(baseUrl).build();
        return retrofit.create(cls);
    }

}
