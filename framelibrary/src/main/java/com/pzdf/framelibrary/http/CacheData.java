package com.pzdf.framelibrary.http;

/**
 * author  jihl on 2019/3/16.
 * version 1.0
 * Description:缓存的实体类
 */
public class CacheData {
    //请求链接
    private String mUrlKey;
    //后台返回的Json
    private String mResultJosn;


    public CacheData() {
    }

    public CacheData(String urlKey, String resultJosn) {
        mUrlKey = urlKey;
        mResultJosn = resultJosn;
    }

    public String getUrlKey() {
        return mUrlKey;
    }

    public void setUrlKey(String urlKey) {
        mUrlKey = urlKey;
    }

    public String getResultJosn() {
        return mResultJosn;
    }

    public void setResultJosn(String resultJosn) {
        mResultJosn = resultJosn;
    }
}
