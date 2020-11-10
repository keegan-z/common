package com.keegan.commondemo.net;


import com.keegan.common.network.common.ApiProducer;
import com.keegan.commondemo.BuildConfig;
import com.keegan.commondemo.api.ApiService;

public class RetrofitHelper {

    private static ApiService mApiService;

    /**
     * 针对规定api风格
     *
     * @return
     */
    public static ApiService getApiService() {
        if (mApiService == null)
            mApiService = ApiProducer.getApiService(ApiService.class, BuildConfig.SERVER_HOST);
        return mApiService;
    }

}
