package com.whz.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.whz.server.handler.DownloadFileHandler;
import com.whz.server.handler.DownloadImgHandler;
import com.whz.server.handler.DownloadJsonHandler;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.filter.HttpCacheFilter;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import static com.whz.server.utils.ConfigUtil.GET_FILE;
import static com.whz.server.utils.ConfigUtil.GET_IMAGE;
import static com.whz.server.utils.ConfigUtil.PORT_SERVER;
import static com.whz.server.utils.ConfigUtil.POST_JSON;
import static com.whz.server.utils.ConfigUtil.getLocalIPAddress;

/**
 * Created by kevin on 2018/6/22
 */
public class AndServerService extends Service {

    private Server mServer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mServer = AndServer.serverBuilder()
                .inetAddress(getLocalIPAddress())
                .port(PORT_SERVER)
                .timeout(10, TimeUnit.SECONDS)
                .registerHandler(GET_FILE, new DownloadFileHandler())
                .registerHandler(GET_IMAGE, new DownloadImgHandler())
                .registerHandler(POST_JSON, new DownloadJsonHandler())
                .filter(new HttpCacheFilter())
                .listener(new Server.ServerListener() {
                    @Override
                    public void onStarted() {
                        AndServerProxy.startServer(AndServerService.this, mServer.getInetAddress().getHostAddress());
                    }

                    @Override
                    public void onStopped() {
                        AndServerProxy.stopServer(AndServerService.this);
                    }

                    @Override
                    public void onError(Exception e) {
                        AndServerProxy.errorServer(AndServerService.this, e.toString());
                    }
                })
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startService();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService();
    }

    /**
     * 启动服务
     */
    private void startService() {
        if (mServer.isRunning()) {
            InetAddress inetAddress = mServer.getInetAddress();
            if (inetAddress != null) {
                String hostAddress = inetAddress.getHostAddress();
                if (!TextUtils.isEmpty(hostAddress)) {
                    AndServerProxy.startServer(AndServerService.this, hostAddress);
                }
            }
        } else {
            mServer.startup();
        }
    }

    /**
     * 停止服务
     */
    private void stopService() {
        if (mServer != null && mServer.isRunning()) {
            mServer.shutdown();
        }
    }
}
