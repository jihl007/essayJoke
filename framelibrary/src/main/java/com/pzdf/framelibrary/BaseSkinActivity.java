package com.pzdf.framelibrary;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import com.pzdf.essayjokebaselibrary.base.BaseActivity;
import com.pzdf.framelibrary.skin.SkinManager;
import com.pzdf.framelibrary.skin.SkinResource;
import com.pzdf.framelibrary.skin.attr.SkinAttr;
import com.pzdf.framelibrary.skin.attr.SkinView;
import com.pzdf.framelibrary.skin.callback.ISkinChangeListener;
import com.pzdf.framelibrary.skin.support.SkinAppCompatViewInflater;
import com.pzdf.framelibrary.skin.support.SkinAttrSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * author  jihl on 2019/2/18.
 * version 1.0
 * Description:
 */
public abstract class BaseSkinActivity extends BaseActivity implements ISkinChangeListener{
    //插件换肤
    private SkinAppCompatViewInflater mSkinAppCompatViewInflater;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater=LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory2(layoutInflater,this);


        /*LayoutInflater layoutInflater=LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory(layoutInflater, new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View view, String name, Context context, AttributeSet attributeSet) {

                if(name.equals("Button")){
                    TextView tv=new TextView(BaseSkinActivity.this);
                    tv.setText("拦截");
                    return  tv;
                }
                return null;
            }
        });*/


        super.onCreate(savedInstanceState);
    }
    /**
     * 统一管理SkinView
     */
    private void managerSkinView(SkinView skinView){
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if(skinViews == null){
            skinViews = new ArrayList<>();
            SkinManager.getInstance().register(this,skinViews);
        }
        skinViews.add(skinView);
    }
    @Override
    public View setContentView(View parent, String name,Context context,AttributeSet attrs) {

        // 拦截到View的创建  获取View之后要去解析
        // 1. 创建View
        // If the Factory didn't handle it, let our createView() method try
        View view = createView(parent, name, context, attrs);

        // 2. 解析属性  src  textColor  background  自定义属性
        // Log.e(TAG, view + "");

        // 2.1 一个activity的布局肯定对应多个这样的 SkinView
        if(view != null) {
            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);
            SkinView skinView = new SkinView(view,skinAttrs);
            // 3.统一交给SkinManager管理
            managerSkinView(skinView);

            //4、判断一下要不要换肤
            SkinManager.getInstance().checkChangeSkin(skinView);

        }
        return view;
    }

    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs){
        final boolean isPre21= Build.VERSION.SDK_INT<21;
        if(mSkinAppCompatViewInflater==null){
            mSkinAppCompatViewInflater=new SkinAppCompatViewInflater();
        }
        final  boolean inheritContext=isPre21&&true
                &&shouldInheritContext((ViewParent) parent);
        return mSkinAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true /* Read read app:theme as a fallback at all times for legacy reasons */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == getWindow().getDecorView() || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    @Override
    public void changeSkin(SkinResource skinResource) {

    }

    @Override
    protected void onDestroy() {
        SkinManager.getInstance().unregister(this);
        super.onDestroy();
    }
}
