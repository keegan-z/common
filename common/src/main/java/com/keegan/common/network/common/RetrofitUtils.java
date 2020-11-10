package com.keegan.common.network.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.keegan.common.network.interceptor.HttpCacheInterceptor;
import com.keegan.common.network.interceptor.HttpHeaderInterceptor;
import com.keegan.common.network.interceptor.LoggingInterceptor;
import com.keegan.common.util.ContextUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitUtils {

    /**
     * 网络请求超时时间毫秒
     */
    public static final int DEFAULT_TIMEOUT = 20000;

    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        File cacheFile = new File(ContextUtil.getContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        return new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpHeaderInterceptor())
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .addInterceptor(new LoggingInterceptor())
                //.sslSocketFactory(SslContextFactory.getSSLSocketFactoryForTwoWay())  // https认证 如果要使用https且为自定义证书 可以去掉这两行注释，并自行配制证书。
                //.hostnameVerifier(new SafeHostnameVerifier())
                .cache(cache);
    }

    /**
     * 自定义gson解析器
     * @param baseUrl
     * @return
     */
    public static Retrofit.Builder getRetrofitBuilder(String baseUrl) {
        //这一行代码的作用是为了防止gson解析字段为null的情况；把null替换成空字符串
        Gson gson = new GsonBuilder().setLenient().registerTypeAdapter(String.class, STRING).create();

        OkHttpClient okHttpClient = getOkHttpClientBuilder().build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl);
    }

    /**
     * 自定义TypeAdapter ,null对象将被解析成空字符串
     */
    public static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
        public String read(JsonReader reader) {
            try {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return ""; // 原先是返回null，这里改为返回空字符串
                }
                return reader.nextString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public void write(JsonWriter writer, String value) {
            try {
                if (value == null) {
                    writer.nullValue();
                    return;
                }
                writer.value(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}
