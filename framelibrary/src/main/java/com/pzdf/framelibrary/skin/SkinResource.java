package com.pzdf.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Method;

/**
 * author  jihl on 2019/3/19.
 * version 1.0
 * Description:皮肤的资源管理
 */
public class SkinResource {
    //通过资源这个对象获取
    private Resources mSkinResource;
    private String mPackageName="";
    public SkinResource(Context context, String skinPath) {
        try {
            //读取本地一个*.skin里面的资源
            Resources superRes=context.getResources();
            //创建AssetManager
            AssetManager asset=null;
            asset=AssetManager.class.newInstance();
            //添加本地下载好点的资源皮肤   Native层C和C++
            Method method=AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
            method.setAccessible(true);
            //反射执行方法
            method.invoke(asset, skinPath);
            mSkinResource=new Resources(asset,superRes.getDisplayMetrics(),
                    superRes.getConfiguration());

            //获取skinPath包名
            mPackageName=context.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 通过名字获取Drawable
     * @param resName
     * @return
     */
    public Drawable getDrawableByName(String resName) {
        try {
            int resId=mSkinResource.getIdentifier(resName,"drawable",mPackageName);
            System.out.println("resId----->"+resId);
            System.out.println("resName--->"+resName);
            System.out.println("mPackageName--->"+mPackageName);
            Drawable drawable = mSkinResource.getDrawable(resId);
            return drawable;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过名字获取颜色
     * @param resName
     * @return
     */
    public ColorStateList getColorByName(String resName) {
        try {
            int resId=mSkinResource.getIdentifier(resName,"color",mPackageName);
            ColorStateList color = mSkinResource.getColorStateList(resId);
            return color;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
