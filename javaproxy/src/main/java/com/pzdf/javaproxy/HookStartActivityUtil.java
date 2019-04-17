package com.pzdf.javaproxy;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * author  jihl on 2019/4/10.
 * version 1.0
 * Description:
 *
 * @author jihl
 */
public class HookStartActivityUtil {
    private Context mContext;
    private Class<?> mProxyClass;
    private final String EXTRA_ORIGIN_INTENT = "EXTRA_ORIGIN_INTENT";

    public HookStartActivityUtil(Context context, Class<?> proxyClass) {
        this.mContext = context.getApplicationContext();
        this.mProxyClass = proxyClass;
    }

    public void hookLaunchActiviyt() throws Exception {
        //获取ActivityThread的实例
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field scatField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
        scatField.setAccessible(true);
        Object sCurrentActivityThread = scatField.get(null);
        //获取ActivityThread中的mH
        Field mhField = activityThreadClass.getDeclaredField("mH");
        mhField.setAccessible(true);
        Object mHandler = mhField.get(sCurrentActivityThread);
        //hook handlerLaunchActivity
        //给Handler设置CallBack回调，通过反射
        Class<?> handlerClass = Class.forName("android.os.Handler");
        Field mCallBackField = handlerClass.getDeclaredField("mCallback");
        mCallBackField.setAccessible(true);
        mCallBackField.set(mHandler, new HandlerCallBack());

    }

    private class HandlerCallBack implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            System.out.println("handleMessage------------>");
            //没发一个消息都会走一次这个callBack发放
            if (msg.what == 100) {
                handlerLauncherActivity(msg);
            }
            return false;
        }
    }

    /**
     * 开始启动创建Activity拦截
     *
     * @param msg
     */
    private void handlerLauncherActivity(Message msg) {

        try {
            Object record = msg.obj;
            //1、从record中获取过安检的Intent
            Field intentField = record.getClass().getDeclaredField("intent");
            intentField.setAccessible(true);
            Intent safeIntent = (Intent) intentField.get(record);
            //2、从safeIntent从获取原来的originIntent
            Intent originIntent = safeIntent.getParcelableExtra(EXTRA_ORIGIN_INTENT);
            //3、重新设置回去
            if (originIntent != null) {
                intentField.set(record, originIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("e.getMessage()---->" + e.getMessage());
        }

    }

    public void hookStartActivityUtil() throws Exception {
        //获取ActivityManagerNative里面的gDefault
        Class<?> amnClass = Class.forName("android.app.ActivityManagerNative");
        Field gDefaultField = amnClass.getDeclaredField("gDefault");
        gDefaultField.setAccessible(true);
        Object gDefault = gDefaultField.get(null);

        //获取gDefault中的mInstance属性
        Class<?> singletonClass = Class.forName("android.util.Singleton");
        Field mInstanceField = singletonClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        Object iamInstance = mInstanceField.get(gDefault);

        Class<?> iamClass = Class.forName("android.app.IActivityManager");

        iamInstance = Proxy.newProxyInstance(HookStartActivityUtil.class.getClassLoader(),
                new Class[]{iamClass}
                //InvocationHandler 必须执行者，谁去执行
                , new StartActivityInvocationHandler(iamInstance));

        //重新指定
        mInstanceField.set(gDefault, iamInstance);
    }

    private class StartActivityInvocationHandler implements InvocationHandler {
        //方法的执行者
        private Object mObject;

        public StartActivityInvocationHandler(Object object) {
            this.mObject = object;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            System.out.println("method.getName---->" + method.getName());

            //替换Intent，通话AndroidMainFest.xml检测
            if ("startActivity".equals(method.getName())) {
                //1、首先获取原来的Intent
                Intent originIntent = (Intent) args[2];

                //创建一个安全Intent
                Intent safeIntent = new Intent(mContext, mProxyClass);
                args[2] = safeIntent;
                //绑定原来的Intent
                safeIntent.putExtra(EXTRA_ORIGIN_INTENT, originIntent);


            }


            return method.invoke(mObject, args);
        }
    }
}
