package com.pzdf.framelibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pzdf.framelibrary.skin.SkinManager;
import com.pzdf.framelibrary.skin.SkinResource;

/**
 * author  jihl on 2019/3/19.
 * version 1.0
 * Description:
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum  SkinType {

    TEXT_COLOR("textColor") {
        @Override
        public void skin(View mView, String resourceName) {
            SkinResource skinResource=getSkinResource();
            ColorStateList color=skinResource.getColorByName(resourceName);
            if(color==null){
                return;
            }
            TextView textView= (TextView) mView;
            textView.setTextColor(color);
        }
    },BACKGROUND("background") {
        @Override
        public void skin(View mView, String resourceName) {
            SkinResource skinResource=getSkinResource();
            Drawable drawable=skinResource.getDrawableByName(resourceName);
            if(drawable!=null){
                ImageView imageView= (ImageView) mView;
                imageView.setBackgroundDrawable(drawable);
                return;
            }
            ColorStateList color=skinResource.getColorByName(resourceName);
            if(color!=null){
                mView.setBackgroundColor(color.getDefaultColor());
                return;
            }

        }
    },SRC("src") {
        @Override
        public void skin(View mView, String resourceName) {
            //获取资源
            SkinResource skinResource=getSkinResource();
            Drawable drawable=skinResource.getDrawableByName(resourceName);
            if(drawable!=null){
                ImageView imageView= (ImageView) mView;
                imageView.setImageDrawable(drawable);
                return;
            }
        }
    };
    //根据名字调用对应的方法


    private String mResourceName;
    SkinType(String resName) {
        this.mResourceName=resName;
    }

    public abstract void skin(View mView, String resourceName);

    public String getResName() {
        return mResourceName;
    }
    public SkinResource getSkinResource(){
        return SkinManager.getInstance().getSkinResource();
    }
}
