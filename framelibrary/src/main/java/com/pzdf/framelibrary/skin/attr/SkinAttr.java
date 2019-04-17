package com.pzdf.framelibrary.skin.attr;

import android.view.View;

/**
 * author  jihl on 2019/3/19.
 * version 1.0
 * Description:
 */
public class SkinAttr {
    private String mResourceName;
    private SkinType mSkinType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResourceName=resName;
        this.mSkinType=skinType;
    }

    public void skin(View mView){
        mSkinType.skin(mView,mResourceName);
    }
}
