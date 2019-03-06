package com.whz.server.handler;

import com.whz.server.ui.base.BaseApplication;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.FileUtils;

import org.apache.httpcore.HttpEntity;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.ContentType;
import org.apache.httpcore.entity.FileEntity;
import org.apache.httpcore.protocol.HttpContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by kevin on 2018/6/24
 */
public class DownloadFileHandler implements RequestHandler {

    @RequestMapping(method = RequestMethod.GET)
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        File file = createFile();
        HttpEntity httpEntity = new FileEntity(file, ContentType.create(FileUtils.getMimeType(file.getAbsolutePath()), Charset.defaultCharset()));
        response.setHeader("Content-Disposition", "attachment;filename=File.txt");
        response.setStatusCode(200);
        response.setEntity(httpEntity);
    }

    /**
     * 创建测试文本文件
     */
    private File createFile() {
        FileOutputStream fos = null;
        try {
            File file = File.createTempFile("test", ".txt", BaseApplication.getContext().getCacheDir());
            fos = new FileOutputStream(file);
            fos.write("This is a test data!".getBytes());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
