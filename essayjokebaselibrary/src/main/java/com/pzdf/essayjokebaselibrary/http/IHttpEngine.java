package com.pzdf.essayjokebaselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by jihl
 * Version 1.0
 * Description:  引擎的规范
 */
public interface IHttpEngine {

    // get请求
    void get(Context context,boolean mCache, String url, Map<String, Object> params, EngineCallBack callBack);

    // post请求
    void post(Context context,boolean mCache, String url, Map<String, Object> params, EngineCallBack callBack);

    // 下载文件


    // 上传文件


    // https 添加证书
}
