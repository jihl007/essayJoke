package com.pzdf.framelibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * author  jihl on 2019/3/19.
 * version 1.0
 * Description:
 * @author jihl
 */
public class SkinView {
    private View mView;

    private List<SkinAttr> mAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView=view;
        this.mAttrs=skinAttrs;
    }

    public void skin(){
        for (SkinAttr attr : mAttrs) {
            attr.skin(mView);
        }
    }

}
