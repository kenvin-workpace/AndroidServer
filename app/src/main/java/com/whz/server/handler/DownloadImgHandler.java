package com.whz.server.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.whz.server.R;
import com.whz.server.ui.base.BaseApplication;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.SimpleRequestHandler;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.FileUtils;
import com.yanzhenjie.andserver.view.View;

import org.apache.httpcore.HttpEntity;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.entity.ContentType;
import org.apache.httpcore.entity.FileEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by kevin on 2018/6/24
 */
public class DownloadImgHandler extends SimpleRequestHandler {

    private File mFile = new File(Environment.getExternalStorageDirectory(), "test.png");

    @RequestMapping(method = RequestMethod.GET)
    @Override
    protected View handle(HttpRequest request) throws HttpException, IOException {
        createFile();
        HttpEntity httpEntity = new FileEntity(mFile, ContentType.create(FileUtils.getMimeType(mFile.getAbsolutePath()), Charset.defaultCharset()));
        return new View(200, httpEntity);
    }

    /**
     * 创建测试图片文件
     */
    private void createFile() {
        FileOutputStream fos = null;
        try {
            if (!mFile.exists()) {
                synchronized (DownloadImgHandler.class) {
                    if (!mFile.exists()) {
                        boolean newFile = mFile.createNewFile();
                        if (!newFile) {
                            throw new IOException("create file exception");
                        }
                        fos = new FileOutputStream(mFile);
                        Bitmap bitmap = BitmapFactory.decodeResource(BaseApplication.getContext().getResources(), R.mipmap.ic_launcher);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    }
                }
            }
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
    }
}
