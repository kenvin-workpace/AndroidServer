package com.whz.server.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.whz.server.R;
import com.whz.server.impl.OnServerChangeListener;
import com.whz.server.service.AndServerProxy;
import com.whz.server.utils.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnServerChangeListener {

    private AndServerProxy mAndServerProxy;
    private TextView mAddress;
    private CardView mCvStart, mCvStop;
    private List<String> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
        intiListener();
    }

    @Override
    public void onServerStarted(String ipAddress) {
        showOnServerStarted();
        if (ipAddress.length() == 2) {
            lists.add("请检查网络连接");
        } else {
            lists.add("http://" + ipAddress + ":" + ConfigUtil.PORT_SERVER + ConfigUtil.GET_FILE);
            lists.add("http://" + ipAddress + ":" + ConfigUtil.PORT_SERVER + ConfigUtil.GET_IMAGE);
            lists.add("http://" + ipAddress + ":" + ConfigUtil.PORT_SERVER + ConfigUtil.POST_JSON);
        }
        mAddress.setText(TextUtils.join("\n", lists));
    }

    @Override
    public void onServerStopped() {
        showOnServerStopped();
        lists.clear();
        mAddress.setText("服务停止了");
    }

    @Override
    public void onServerError(String errorMessage) {
        showOnServerStopped();
        lists.clear();
        mAddress.setText("服务发生了错误，错误信息：" + errorMessage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAndServerProxy.unregister(this);
        mAndServerProxy = null;
    }

    /**
     * 显示关闭服务按钮
     */
    private void showOnServerStarted() {
        mCvStart.setVisibility(View.GONE);
        mCvStop.setVisibility(View.VISIBLE);
    }

    /**
     * 显示开启服务按钮
     */
    private void showOnServerStopped() {
        mCvStop.setVisibility(View.GONE);
        mCvStart.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化View监听
     */
    private void intiListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.cv_stop:
                        mAndServerProxy.stopService(MainActivity.this);
                        break;
                    case R.id.cv_start:
                        mAndServerProxy.startService(MainActivity.this);
                        break;
                    default:
                        break;
                }
            }
        };

        mCvStop.setOnClickListener(listener);
        mCvStart.setOnClickListener(listener);
    }

    /**
     * 初始化View事件
     */
    private void initEvent() {
        mAndServerProxy = new AndServerProxy(this, this);
    }

    /**
     * 初始化View
     */
    private void initView() {
        mCvStop = findViewById(R.id.cv_stop);
        mCvStart = findViewById(R.id.cv_start);
        mAddress = findViewById(R.id.tv_address);
    }
}
