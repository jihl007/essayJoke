package com.pzdf.javaproxy.plugin;

import android.content.Context;

/**
 * author  jihl on 2019/4/10.
 * version 1.0
 * Description: 解决类加载的问题
 */
public class PluginManager {
    public static final void install(Context context,String apkPath){
        try {
            FixDexManager fixDexManager=new FixDexManager(context);
            //把apk的class加载到applicationClassLoader
            fixDexManager.fixDex(apkPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
