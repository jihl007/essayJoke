package com.pzdf.framelibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.pzdf.framelibrary.config.SkinConfig;
import com.pzdf.framelibrary.config.SkinPreUtils;
import com.pzdf.framelibrary.skin.attr.SkinView;
import com.pzdf.framelibrary.skin.callback.ISkinChangeListener;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author  jihl on 2019/3/19.
 * version 1.0
 * Description:
 */
public class SkinManager {
    private static SkinManager mInstance;
    private Context mContext;
    private SkinResource mSkinResource;
    private Map<ISkinChangeListener,List<SkinView>> mSkinViews=new HashMap<>();


    static {
        mInstance = new SkinManager();
    }

    public static SkinManager getInstance() {
        return mInstance;
    }
    public void init(Context context){
        this.mContext=context.getApplicationContext();


        //每次打开apk都会进入该方法，防止皮肤被任意删除，做一些措施
        String currentSkinPath=SkinPreUtils.getInstance(mContext).getSkinPath();
        File file=new File(currentSkinPath);
        if(!file.exists()){
            //皮肤存在，清空皮肤
            SkinPreUtils.getInstance(mContext).clearSkinInfo();
            return;
        }
        //能不能获取到包名,获取skinPath包名
        String mPackageName=context.getPackageManager()
                .getPackageArchiveInfo(currentSkinPath, PackageManager.GET_ACTIVITIES)
                .packageName;
        if(TextUtils.isEmpty(mPackageName)){
            SkinPreUtils.getInstance(context).clearSkinInfo();
            return;
        }
        //校验签名  增量跟新

        //初始化
        mSkinResource=new SkinResource(mContext,currentSkinPath);
    }

    /**
     * 获取SkinView通过Activity
     * @param activity
     * @return
     */
    public List<SkinView> getSkinViews(Activity activity) {

        return mSkinViews.get(activity);
    }

    /**
     * 注册
     * @param skinChangeListener
     * @param skinViews
     */
    public void register(ISkinChangeListener skinChangeListener, List<SkinView> skinViews) {
        mSkinViews.put(skinChangeListener,skinViews);
    }

    /**
     * 加载皮肤
     * @return
     */
    public int loadSkin(String skinPath){
        File file=new File(skinPath);
        if(!file.exists()){
            return SkinConfig.SKIN_FILE_NOEXSIST;
        }

        //能不能获取到包名,获取skinPath包名
        String mPackageName=mContext.getPackageManager()
                .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                .packageName;
        if(TextUtils.isEmpty(mPackageName)){
            return SkinConfig.SKIN_FILE_ERROR;
        }

        //当前皮肤如果一样
        String currentSkinPath=SkinPreUtils.getInstance(mContext).getSkinPath();
        if (skinPath.equals(currentSkinPath)) {
            return SkinConfig.SKIN_CHANGE_SUCCESS;
        }


        //校验签名   增量更新再说

        //初始化资源
        mSkinResource=new SkinResource(mContext,skinPath);

        changeSkin();

        saveSkinStatus(skinPath);

        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    /**
     * 改变皮肤
     */
    private void changeSkin() {
        Set<ISkinChangeListener> keys=mSkinViews.keySet();
        for (ISkinChangeListener key : keys) {
            List<SkinView> skinViews=mSkinViews.get(key);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }

            //通知Activity
            key.changeSkin(mSkinResource);
        }
    }

    /**
     * 保存皮肤的状态
     * @param skinPath
     */
    private void saveSkinStatus(String skinPath) {
        SkinPreUtils.getInstance(mContext).saveSkinPath(skinPath);
    }

    /**
     * 恢复默认
     * @return
     */
    public int restoreDefault(){
        //判断当前有没有皮肤，没有就直接退出
        String currentSkinPath=SkinPreUtils.getInstance(mContext).getSkinPath();
        if (TextUtils.isEmpty(currentSkinPath)) {
            return SkinConfig.SKIN_CHANGE_NOTHING;
        }

        //当前运行app的路径
        String skinPath=mContext.getPackageResourcePath();
        //初始化资源管理
        mSkinResource=new SkinResource(mContext,skinPath);

       changeSkin();

       //把皮肤信息清空
        SkinPreUtils.getInstance(mContext).clearSkinInfo();

        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    /**
     * 获取当前资源管理
     * @return
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }

    /**
     * 检测要不要换肤
     * @param skinView
     */
    public void checkChangeSkin(SkinView skinView) {
        //如果当前有皮肤，保存了皮肤路径，就换肤
        String currentSkinPath=SkinPreUtils.getInstance(mContext).getSkinPath();
        if(!TextUtils.isEmpty(currentSkinPath)){
            skinView.skin();
        }
    }

    /**
     * 防止内存泄漏
     * @param skinChangeListener
     */
    public void unregister(ISkinChangeListener skinChangeListener) {
        mSkinViews.remove(skinChangeListener);
    }
}
