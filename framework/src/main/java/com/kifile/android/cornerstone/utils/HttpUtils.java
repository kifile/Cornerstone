package com.kifile.android.cornerstone.utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 处理网络交互工具类.
 * <p/>
 *
 * @author kifile
 */
public class HttpUtils {

    private static final OkHttpClient sClient = new OkHttpClient();

    static {
        // 设置连接 5 秒超时.
        sClient.setWriteTimeout(5, TimeUnit.SECONDS);
        sClient.setConnectTimeout(5, TimeUnit.SECONDS);
        sClient.setReadTimeout(5, TimeUnit.SECONDS);
    }

    /**
     * 执行Request命令.
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response getResponse(Request request) throws IOException {
        return sClient.newCall(request).execute();
    }
}
