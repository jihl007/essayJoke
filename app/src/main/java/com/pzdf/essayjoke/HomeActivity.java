package com.pzdf.essayjoke;

import com.pzdf.framelibrary.BaseSkinActivity;
import com.pzdf.framelibrary.DefaultNavigationBar;

/**
 * author  jihl on 2019/3/23.
 * version 1.0
 * Description:
 */
public class HomeActivity extends BaseSkinActivity {
    @Override
    protected void initDate() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar=new DefaultNavigationBar.Builder(this)
                .setTitle("首页")
                .hideLeftIcon()
                .builder();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_home);
    }
}
