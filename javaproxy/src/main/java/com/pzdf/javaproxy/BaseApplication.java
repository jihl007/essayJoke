package com.pzdf.javaproxy;

import com.morgoo.droidplugin.PluginApplication;

/**
 * author  jihl on 2019/4/10.
 * version 1.0
 * Description:
 */
public class BaseApplication extends PluginApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        /*
        //extends Application
        //自定义实现 插件化架构
        try {
            HookStartActivityUtil hookStartActivityUtil =
                    new HookStartActivityUtil(this,ProxyActivity.class);
            hookStartActivityUtil.hookStartActivityUtil();
            hookStartActivityUtil.hookLaunchActiviyt();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        //extends PluginApplication   360插件
        //备注：如果安装不成功，修改DroidPlugin-->AndroidMainfest.xml中的android:authorities该成自己的包名即可。

    }
}
