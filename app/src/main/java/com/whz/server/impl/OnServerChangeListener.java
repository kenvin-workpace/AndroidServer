package com.whz.server.impl;

/**
 * Created by kevin on 2018/6/24
 */
public interface OnServerChangeListener {

    void onServerStarted(String ipAddress);

    void onServerStopped();

    void onServerError(String errorMessage);

}
