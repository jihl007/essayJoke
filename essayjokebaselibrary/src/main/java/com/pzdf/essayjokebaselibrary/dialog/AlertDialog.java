package com.pzdf.essayjokebaselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.pzdf.essayjokebaselibrary.R;

/**
 * author  jihl on 2019/2/27.
 * version 1.0
 * Description: 自定义Dialog
 */
public class AlertDialog extends Dialog {
    private AlertController mAlert;
    public AlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        mAlert=new AlertController(this,getWindow());
    }
    public void setText(int viewId, CharSequence charSequence) {
        mAlert.setText(viewId,charSequence);
    }
    public  <T extends View > T getView(int viewId){

        return mAlert.getView(viewId);
    }
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mAlert.setOnClickListener(viewId,listener);
    }

    public static class Builder{
        private final AlertController.AlertParams P;

        public Builder(Context context){
            P=new AlertController.AlertParams(context, R.style.dialog);
        }
        public Builder(Context context,int themeResId){
            P=new AlertController.AlertParams(context,themeResId);
        }

        public Builder setContentView(View view){
            P.mView=view;
            P.mViewlayoutResId=0;
            return this;
        }
        public Builder setContentView(int layoutId){
            P.mView=null;
            P.mViewlayoutResId=layoutId;
            return this;
        }
        //设置文本
        public Builder setText(int viewId,CharSequence text){
            P.mTextArray.put(viewId,text);
            return this;

        }
        //设置点击事件
        public Builder setOnClickListener(int view,View.OnClickListener listener){
            P.mClickArray.put(view,listener);
            return this;
        }
        public Builder setCancelable(boolean cancelable){
            P.mCancelable=cancelable;
            return this;
        }
        public Builder setOnCancelListener(OnCancelListener onCancelListener){
            P.mOnCancelListener=onCancelListener;
            return this;
        }
        public Builder setOnDismissListener(OnDismissListener onDismissListener){
            P.mOnDismissListener=onDismissListener;
            return this;
        }
        public Builder setOnKeyListener(View.OnKeyListener onKeyListener){
            P.mOnKeyListener= (OnKeyListener) onKeyListener;
            return this;
        }

        /**
         * 配置万能参数
         * @return
         */
        public Builder fullWidth(){
            P.mWidth= ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 从底部弹出
         * @param isAnimation
         * @return
         */
        public Builder fromBottom(boolean isAnimation){
            if(isAnimation){
                P.mAnimations=R.style.dialog_from_bottom_anim;
            }
            P.mGravity= Gravity.BOTTOM;
            return this;
        }

        /**
         * 设置Dialog的宽高
         * @param width
         * @param height
         * @return
         */
        public Builder setWidthAndHeight(int width,int height){
            P.mWidth=width;
            P.mHeight=height;
            return this;
        }
        public Builder addDefaultAnimation(){
            P.mAnimations=R.style.dialog_scale_anim;
            return this;
        }
        /**
         * 设置动画
         * @param styleAnimations
         * @return
         */
        public Builder setAnimations(int styleAnimations){
            P.mAnimations=styleAnimations;
            return this;
        }

        public AlertDialog create(){
            final AlertDialog dialog=new AlertDialog(P.mContext,P.mThemeResId);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if(P.mCancelable){
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if(P.mOnKeyListener!=null){
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return  dialog;
        }

        public AlertDialog show(){
            final AlertDialog dialog=create();
            dialog.show();
            return dialog;
        }
    }
}
