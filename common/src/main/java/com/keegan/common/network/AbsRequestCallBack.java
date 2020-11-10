package com.keegan.common.network;

public abstract class AbsRequestCallBack<T, H> {

    public abstract void onSuccess(T t);

    public abstract void onFail(H h);


}
