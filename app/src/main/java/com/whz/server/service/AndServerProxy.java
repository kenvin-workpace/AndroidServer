package com.whz.server.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.whz.server.impl.OnServerChangeListener;

/**
 * Created by kevin on 2018/6/24
 */
public class AndServerProxy {

    private static final int VALUE_START = 100;
    private static final int VALUE_STOPP = 200;
    private static final int VALUE_ERROR = 300;
    private static final String KEY_SERVER_STATE = "serverState";
    private static final String KEY_SERVER_MESSAGE = "serverMessage";
    private static final String ACTION_SERVER_CHANGE = "com.whz.action_server_change";

    private OnServerChangeListener mServerChangeListener;


    public AndServerProxy(Context context, OnServerChangeListener listener) {
        this.mServerChangeListener = listener;
        context.registerReceiver(mBroadcastReceiver, new IntentFilter(ACTION_SERVER_CHANGE));
    }

    /**
     * 开启服务
     */
    public void startService(Context context) {
        context.startService(new Intent(context, AndServerService.class));
    }

    /**
     * 停止服务
     */
    public void stopService(Context context) {
        context.stopService(new Intent(context, AndServerService.class));
    }

    /**
     * 取消广播注册
     */
    public void unregister(Context context) {
        context.unregisterReceiver(mBroadcastReceiver);
        mBroadcastReceiver = null;
    }

    /**
     * 发送服务启动广播
     */
    public static void startServer(Context context, String ipAddress) {
        Intent intent = new Intent(ACTION_SERVER_CHANGE);
        intent.putExtra(KEY_SERVER_STATE, VALUE_START);
        intent.putExtra(KEY_SERVER_MESSAGE, ipAddress);
        context.sendBroadcast(intent);
    }

    /**
     * 发送服务停止广播
     */
    public static void stopServer(Context context) {
        Intent intent = new Intent(ACTION_SERVER_CHANGE);
        intent.putExtra(KEY_SERVER_STATE, VALUE_STOPP);
        context.sendBroadcast(intent);
    }

    /**
     * 发送服务错误广播
     */
    public static void errorServer(Context context, String message) {
        Intent intent = new Intent(ACTION_SERVER_CHANGE);
        intent.putExtra(KEY_SERVER_STATE, VALUE_ERROR);
        intent.putExtra(KEY_SERVER_MESSAGE, message);
        context.sendBroadcast(intent);
    }

    /**
     * 广播服务
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(KEY_SERVER_STATE, 0);
            switch (state) {
                case VALUE_START:
                    if (mServerChangeListener != null) {
                        mServerChangeListener.onServerStarted(intent.getStringExtra(KEY_SERVER_MESSAGE));
                    }
                    break;
                case VALUE_STOPP:
                    if (mServerChangeListener != null) {
                        mServerChangeListener.onServerStopped();
                    }
                    break;
                case VALUE_ERROR:
                    if (mServerChangeListener != null) {
                        mServerChangeListener.onServerError(intent.getStringExtra(KEY_SERVER_MESSAGE));
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
