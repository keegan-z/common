package com.keegan.common.network.interceptor;



import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class HttpHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        //  配置请求头
        Request request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Connection", "keep-alive")
                //.addHeader("Accept-Encoding", "identity")
                .build();

        return chain.proceed(request);
    }
}
