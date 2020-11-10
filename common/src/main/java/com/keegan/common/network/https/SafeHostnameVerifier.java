package com.keegan.common.network.https;







import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;


public class SafeHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
       /* if (BuildConfig.SERVER_NAME.equals(hostname)) {    //校验hostname是否正确，如果正确则建立连接
            return true;
        }*/
        return false;
    }
}
