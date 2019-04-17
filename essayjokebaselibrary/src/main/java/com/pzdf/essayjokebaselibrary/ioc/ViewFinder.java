package com.pzdf.essayjokebaselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * author  jihl on 2019/2/15.
 * version 1.0
 * Description:View的FindViewById的辅助类
 */
public class ViewFinder {
    private Activity mActivity;
    private View mView;
    public ViewFinder(Activity activity) {
        this.mActivity=activity;
    }

    public ViewFinder(View view) {
        this.mView=view;
    }

    public View findViewById(int viewId){
        return mActivity!=null?mActivity.findViewById(viewId):mView.findViewById(viewId);
    }
}
