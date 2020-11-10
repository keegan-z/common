package com.keegan.commondemo.api;



import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by dell on 2020/6/2.
 */

public interface ApiService {

    /**
     * 会员中心
     */
    @GET(ApiConstants.URL_MEMBER)
    Observable<String> queryMember();


}
