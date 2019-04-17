package com.pzdf.essayjokebaselibrary.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.pzdf.essayjokebaselibrary.ioc.ViewUtils;

/**
 * author  jihl on 2019/2/18.
 * version 1.0
 * Description:
 *
 * @author jihl
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //针对于mvc模式，mvp有区别

        //设置布局Layout
        setContentView();

        //一些特点的算法，子类都需要的d
        ViewUtils.inject(this);

        //初始化头部
        initTitle();
        //初始化页面
        initView();
        //初始化数据
        initDate();
    }

    protected abstract void initDate();

    protected abstract void initView();

    protected abstract void initTitle();

    protected abstract void setContentView();

    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected <T extends View> T viewById(int viewId) {
        return (T) findViewById(viewId);
    }

    public abstract View setContentView(View parent, String name, Context context, AttributeSet attrs);
}
