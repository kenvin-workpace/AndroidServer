package com.whz.server.handler;

import android.util.Log;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.protocol.HttpContext;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by kevin on 2018/6/24
 */
public class DownloadJsonHandler implements RequestHandler {

    private final String aTag = DownloadJsonHandler.class.getSimpleName();

    @RequestMapping(method = RequestMethod.POST)
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        try {
            String body = HttpRequestParser.getContentFromBody(request);
            Log.e(aTag, body);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "hongzhenw");
            jsonObject.put("age", 18);
            Log.e(aTag, jsonObject.toString());


            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            response.setStatusCode(200);
            response.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
