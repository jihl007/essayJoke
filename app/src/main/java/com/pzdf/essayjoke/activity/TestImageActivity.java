package com.pzdf.essayjoke.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.pzdf.essayjoke.R;
import com.pzdf.essayjoke.selectimage.ImageSelector;
import com.pzdf.essayjoke.selectimage.SelectImageActivity;
import com.pzdf.framelibrary.BaseSkinActivity;

import java.util.ArrayList;


public class TestImageActivity extends BaseSkinActivity{
    private ArrayList<String> mImageList;
    private final int SELECT_IMAGE_REQUEST = 0x0011;

    @Override
    protected void initDate() {

    }

    @Override
    protected void initView() {

    }


    @Override
    protected void initTitle() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test_image);
    }

    // 选择图片
    public void selectImage(View view){
        // 6.0 请求权限，危险权限，读取内存卡，拍照

        // 这样传递参数有没有问题
        // 没问题，但是不符合框架的思想，
        // 用可能SelectImageActivity 别人是看不到的只能用，中间搞一层不要让开发者关注太多
        /*Intent intent = new Intent(this,SelectImageActivity.class);
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_COUNT,9);
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MODE,SelectImageActivity.MODE_MULTI);
        intent.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST, mImageList);
        intent.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA, true);
        startActivityForResult(intent, SELECT_IMAGE_REQUEST);*/

        // 第一个只关注想要什么，良好的封装性，不要暴露太多
        ImageSelector.create().count(9).multi().origin(mImageList)
                .showCamera(true).start(this,SELECT_IMAGE_REQUEST);


    }
    public void compressImg(View view) {
        //把选择好的图片做压缩
        for (String path:mImageList) {
            //做优化  第一个decodeFile有可能会内存移除
            // 一般后台会规定尺寸  800  小米 规定了宽度 720
            // 上传的时候可能会多张 for循环 最好用线程池 （2-3）
            // Bitmap要释放，Bitmap往往只有一个对象，九个最好每次都释放
            
//            Bitmap bitmap=
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_IMAGE_REQUEST && data != null){
                mImageList = data.getStringArrayListExtra(SelectImageActivity.EXTRA_RESULT);
                // 做一下显示
                Log.e("TAG",mImageList.toString());
            }
        }
    }
}
