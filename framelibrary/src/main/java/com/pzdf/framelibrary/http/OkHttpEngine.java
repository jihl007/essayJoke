package com.pzdf.framelibrary.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pzdf.essayjokebaselibrary.http.EngineCallBack;
import com.pzdf.essayjokebaselibrary.http.HttpUtils;
import com.pzdf.essayjokebaselibrary.http.IHttpEngine;
import com.pzdf.essayjokebaselibrary.md5.MD5Util;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jihl
 * Version 1.0
 * Description: OkHttp默认的引擎
 *
 * @author jihl
 */
public class OkHttpEngine implements IHttpEngine {
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void post(Context context, boolean mCache, String url, Map<String, Object> params,
                     final EngineCallBack callBack) {

        final String jointUrl = HttpUtils.jointParams(url, params);  //打印
        Log.e("Post请求路径：", jointUrl);

        // 了解 Okhhtp
        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // 这个 两个回掉方法都不是在主线程中
                        String result = response.body().string();
                        Log.e("Post返回结果：", jointUrl);
                        callBack.onSuccess(result);
                    }
                }
        );
    }

    /**
     * 组装post请求参数body
     */
    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    // 添加参数
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    // 处理文件 --> Object File
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody
                            .create(MediaType.parse(guessMimeType(file
                                    .getAbsolutePath())), file));
                } else if (value instanceof List) {
                    // 代表提交的是 List集合
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            // 获取文件
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key + i, file.getName(), RequestBody
                                    .create(MediaType.parse(guessMimeType(file
                                            .getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    /**
     * 猜测文件类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    @Override
    public void get(Context context, final boolean mCache, String url,
                    Map<String, Object> params, final EngineCallBack callBack) {

        final String mFinalUrl = HttpUtils.jointParams(url, params);
        Log.e("Get请求路径：", url);


        //判断是否有缓存
        if (mCache) {
            String resultJson = CacheDataUtil.getCacheResultJson(mFinalUrl);

            if (!TextUtils.isEmpty(resultJson)) {
                //需要缓存，而且数据库有缓存,直接去执行onSccess
                callBack.onSuccess(resultJson);
            }
        }

        Request.Builder requestBuilder = new Request.Builder().url(MD5Util.string2MD5(mFinalUrl)).tag(context);
        //可以省略，默认是GET请求
        Request request = requestBuilder.build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultJson = response.body().string();

                //获取数据以后执行成功方法
                if (mCache) {
                    String cacheResultJosn = CacheDataUtil.getCacheResultJson(mFinalUrl);
                    if (!TextUtils.isEmpty(cacheResultJosn)) {
                        if (resultJson.equals(cacheResultJosn)) {
                            //内容一致，不需要执行成功方法刷新页面
                            return;
                        }
                    }
                }
                //执行成功
                callBack.onSuccess(resultJson);
                Log.e("Get返回结果：", resultJson);

                if (mCache) {
                    //缓存数据
                    CacheDataUtil.cacheData(mFinalUrl,resultJson);
                }
            }
        });
    }
}
