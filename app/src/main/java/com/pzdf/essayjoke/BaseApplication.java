package com.pzdf.essayjoke;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alipay.euler.andfix.patch.PatchManager;
import com.pzdf.essayjokebaselibrary.fixBug.FixDexManager;
import com.pzdf.essayjokebaselibrary.http.HttpUtils;
import com.pzdf.essayjokebaselibrary.ioc.ExceptionCrashHandler;
import com.pzdf.framelibrary.http.OkHttpEngine;
import com.pzdf.framelibrary.skin.SkinManager;

import org.litepal.LitePalApplication;

/**
 * author  jihl on 2019/2/18.
 * version 1.0
 * Description:
 * @author jihl
 */
public class BaseApplication extends LitePalApplication {
    public static PatchManager mPatchManager;
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化okHttp引擎
        HttpUtils.init(new OkHttpEngine());


        SkinManager.getInstance().init(this);

        //设置全局异常捕捉类
        ExceptionCrashHandler.getInstance().init(this);

        //初始化阿里热修复
        mPatchManager=new PatchManager(this);
        try {
            PackageManager packageManager=this.getPackageManager();
            PackageInfo mInfo=packageManager.getPackageInfo(this.getPackageName(),0);
            //初始化版本，获取当前应用的版本
            mPatchManager.init(mInfo.versionName);
        }catch (Exception e){
            e.printStackTrace();
        }
        //加载之前的aptach包
        mPatchManager.loadPatch();


        //自定义修复初始化
        try {
           FixDexManager fixDexManager=new FixDexManager(this);
            //加载所有修复的dex包
            fixDexManager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //备注：aliHotFix和自定义修复二选一即可。
    }
}
