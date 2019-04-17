package com.pzdf.javaproxy;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.morgoo.droidplugin.pm.PluginManager;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "*.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onView(View view) {
        switch (view.getId()) {
            case R.id.btn_az:

                //自定义安装
                //PluginManager.install(this,apkPath);


                try {
                    //360安装
                    int result = PluginManager.getInstance().installPackage(apkPath, 0);
                    //360卸载
                    PluginManager.getInstance().deletePackage(apkPath,0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_hw:
                try {
                    //启动插件，插件下载好放在内存卡里面
                    Intent intent = new Intent();
                    //类找不，热修复解决
                    //dex  加载apk
                    intent.setClass(this, Class.forName("其他apk中的路径+类名"));
                    //参数

                    startActivity(intent);



                    //360启动
                    PackageManager pm=getPackageManager();
                    //通过apk的路径获取apk的包名
                    PackageInfo info=pm.getPackageArchiveInfo(apkPath,PackageManager.GET_ACTIVITIES);
                    String packageName=info.packageName;

                    Intent inte=pm.getLaunchIntentForPackage(packageName);
                    inte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(inte);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }
}
