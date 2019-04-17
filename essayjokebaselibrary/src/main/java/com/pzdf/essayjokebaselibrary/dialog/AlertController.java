package com.pzdf.essayjokebaselibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * author  jihl on 2019/2/27.
 * version 1.0
 * Description:
 */
class AlertController {
    private AlertDialog mDialog;
    private Window mWindow;

    private DialogViewHelper mViewHelper;

    public AlertController(AlertDialog alertDialog, Window window) {
        this.mDialog = alertDialog;
        this.mWindow = window;
    }

    public void setViewHelper(DialogViewHelper viewHelper) {
        mViewHelper = viewHelper;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
    }

    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListener(viewId, listener);
    }

    public AlertDialog getDialog() {
        return mDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public static class AlertParams {
        public Context mContext;
        public int mThemeResId;
        //点击空白是否能够取消
        public boolean mCancelable = true;

        //取消监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //Dismiss监听
        public DialogInterface.OnDismissListener mOnDismissListener;
        //Key 监听
        public DialogInterface.OnKeyListener mOnKeyListener;
        //布局
        public View mView;
        //布局Id
        public int mViewlayoutResId;

        //存放字体的修改
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();

        //存放点击事件
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();

        // 宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 动画
        public int mAnimations = 0;
        // 位置
        public int mGravity = Gravity.CENTER;
        // 高度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;


        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 绑定射设置参数
         *
         * @param mAlert
         */
        public void apply(AlertController mAlert) {
            //1设置Dialog的布局
            DialogViewHelper viewHelper = null;
            if (mViewlayoutResId != 0) {
                viewHelper = new DialogViewHelper(mContext, mViewlayoutResId);
            }
            if (mView != null) {
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }
            if (viewHelper == null) {
                throw new IllegalArgumentException("pleass new setContentView");
            }

            //设置Controller的辅助类
            mAlert.setViewHelper(viewHelper);

            //设置文本
            int textArrarySize = mTextArray.size();
            for (int i = 0; i < textArrarySize; i++) {
                mAlert.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }
            //设置点击
            int clickArrarySize = mClickArray.size();
            for (int i = 0; i < clickArrarySize; i++) {
                mAlert.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }


            //给Dialog设置布局
            mAlert.getDialog().setContentView(viewHelper.getContentView());


            //设置自定义效果   全屏  从底部弹出   默认动画
            Window window = mAlert.getWindow();
            window.setGravity(mGravity);

            //设置动画
            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }
            //设置宽高
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }
}
