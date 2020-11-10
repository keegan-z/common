package com.keegan.common.websocket;

import android.util.Log;


import com.keegan.common.util.ContextUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okio.ByteString;

public class WSService {
    private static final String TAG = "WSService";

    private static  String DEF_RELEASE_URL = "wss://qt.fxtool365.com/k";     //连接地址


    private WsManager wsManager;



    public void connect() {
        if (wsManager != null) {
            wsManager.stopConnect();
            wsManager = null;
        }

        wsManager = new WsManager.Builder(ContextUtil.getContext()).client(
                new OkHttpClient().newBuilder()
                        .pingInterval(15, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build())
                .needReconnect(true)
                .wsUrl(DEF_RELEASE_URL)
                .build();

        wsManager.setWsStatusListener(wsStatusListener);
        wsManager.startConnect();
    }

    public void reconnect(){
        if (wsManager != null){
            wsManager.tryReconnect();
        }
    }

    public void sendMessage(String message){
        if (wsManager != null && wsManager.isWsConnected()) {
            boolean isSend = wsManager.sendMessage(message);
        }
    }


    private WsStatusListener wsStatusListener = new WsStatusListener() {
        @Override
        public void onOpen(Response response) {
            Log.d(TAG, "WsManager-----服务器连接成功");


        }

        @Override
        public void onMessage(String text) {

        }

        @Override
        public void onMessage(ByteString bytes) {
            Log.d(TAG, "WsManager-----onMessage");
        }

        @Override
        public void onReconnect() {
            Log.d(TAG, "WsManager-----服务器重连接中...");
        }

        @Override
        public void onClosing(int code, String reason) {
            Log.d(TAG, "WsManager-----服务器连接关闭中...");
        }

        @Override
        public void onClosed(int code, String reason) {
            Log.d(TAG, "WsManager-----服务器连接已关闭");
        }

        @Override
        public void onFailure(Throwable t, Response response) {
            Log.d(TAG, "WsManager-----服务器连接失败");
        }
    };

    public void disConnectWS() {
        if (wsManager != null) {
            wsManager.stopConnect();
            wsManager = null;
        }
    }

    private OnConnectStatusListeren onConnectStatusListeren;

    public interface OnConnectStatusListeren {
        void onOpen(Response response);
    }

    public void setOnConnectStatusListeren(OnConnectStatusListeren onConnectStatusListeren) {
        this.onConnectStatusListeren = onConnectStatusListeren;
    }


}
