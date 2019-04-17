package com.pzdf.essayjokebaselibrary.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author  jihl on 2019/2/15.
 */
public class ViewUtils {
    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    public static void inject(View view) {
        inject(new ViewFinder(view), view);
    }

    public static void inject(View view, Object object) {
        inject(new ViewFinder(view), object);
    }

    //兼容    object-->反射属性所需要执行的类
    private static void inject(ViewFinder viewFinder, Object object) {
        injectFiled(viewFinder, object);
        injectEvent(viewFinder, object);
    }

    /**
     * 事件注入
     *
     * @param viewFinder
     * @param object
     */
    private static void injectEvent(ViewFinder viewFinder, Object object) {
        //1、获取类里面所有的方法
        Class<?> clazz=object.getClass();
        Method[] methods=clazz.getMethods();
        //2、获取OnClick里面的value值
        for(Method method:methods){
            OnClick onClick=method.getAnnotation(OnClick.class);
            if(onClick!=null){
                int[] viewIds=onClick.value();
                for (int viewId:viewIds){
                    //3、findViewById找到View
                     View view=viewFinder.findViewById(viewId);


                     //扩展功能 检测网络
                    boolean isCheckNet=method.getAnnotation(CheckNet.class)!=null;

                     if(view!=null){
                         //4、view.SetOnClickListener
                         view.setOnClickListener(new DeclaredOnClickListener(method,object,isCheckNet));
                     }
                }
            }
        }

    }
    private static class DeclaredOnClickListener implements View.OnClickListener{
        private  Object mObject;
        private Method mMethod;
        private boolean mIsCheckNet;
        DeclaredOnClickListener(Method method,Object object,boolean isCheckNet){
            this.mObject=object;
            this.mMethod=method;
            this.mIsCheckNet=isCheckNet;
        }

        @Override
        public void onClick(View v) {
            //需不需要网络
            if(mIsCheckNet){

                if(!networkAvailable(v.getContext())){
                    Toast.makeText(v.getContext(),"网络不给力！",Toast.LENGTH_SHORT).show();
                }
            }

            try {
                //所有方法都可以，包括私有共有
                mMethod.setAccessible(true);
                //5、反射执行方法
                mMethod.invoke(mObject,v);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject,null);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    /**
     * 事件属性
     * @param viewFinder
     * @param object
     */
    private static void injectFiled(ViewFinder viewFinder, Object object) {

        //1、获取类里面所有的属性
        Class<?> clazz = object.getClass();
        //获取所有属性的包括共有和私有
        Field[] fields = clazz.getDeclaredFields();

        //2、获取ViewById里面的value值
        for (Field field : fields) {
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
                //获取注解里面的Id值-->R.id.**
                int viewId = viewById.value();
                //3、findViewById找到View
                View view = viewFinder.findViewById(viewId);
                if (view != null) {
                    //能够注入所有修饰符
                    field.setAccessible(true);
                    //4、动态注入找到的View
                    try {
                        field.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        System.out.println("IllegalAccessException===" + e.getMessage());
                    }
                }
            }
        }
    }
    /**
     * 判断网络是否可用
     */
    private static boolean networkAvailable(Context context){
        //得到连接管理对象
        try {
            ConnectivityManager connectivityManager=(ConnectivityManager)context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activityNetworkInfo=connectivityManager.getActiveNetworkInfo();
            if(activityNetworkInfo!=null&&activityNetworkInfo.isConnected()){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
