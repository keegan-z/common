package com.keegan.common.rxjava;

public interface IRequestCallBack {


    <T> void onSuccess(T t);

    <H> void onFail(H t);


}
