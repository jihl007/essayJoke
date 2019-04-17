package com.pzdf.essayjokebaselibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * author  jihl on 2019/2/27.
 * version 1.0
 * Description:处理页面
 */
public class DialogViewHelper {
    private View mContentView=null;

    //软引用   防止内存泄露
    private SparseArray<WeakReference<View>> mView;

    public DialogViewHelper(Context context,int layoutId){
        this();
        mContentView= LayoutInflater.from(context).inflate(layoutId,null);
    }

    public DialogViewHelper() {
        mView=new SparseArray<>();
    }

    /**
     * 设置布局View
     */
    public void setContentView(View view) {
        this.mContentView=view;
    }

    public void setText(int viewId, CharSequence charSequence) {
        //设置软引用 减少findViewById次数
        TextView tv= getView(viewId);
        if(tv!=null){
            tv.setText(charSequence);
        }
    }
    public  <T extends View > T getView(int viewId){
        //软引用解决内存泄露问题
        WeakReference<View> viewReference=mView.get(viewId);
        View view=null;
        if(viewReference!=null){
            view=viewReference.get();
        }
        if(view==null){
            view=mContentView.findViewById(viewId);
            if(view!=null){
                mView.put(viewId,new WeakReference<>(view));
            }
        }
        return (T) view;
    }
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view=getView(viewId);
        if(view!=null){
            view.setOnClickListener(listener);
        }
    }
    /**
     * 获取ContentView
     * @return
     */
    public View getContentView() {
        return mContentView;
    }
}
