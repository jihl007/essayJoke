package com.pzdf.essayjokebaselibrary.ioc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * author  jihl on 2019/2/18.
 * version 1.0
 * Description: 单列的设计模式异常捕捉
 *
 * @author jihl
 */
public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static ExceptionCrashHandler mInstance;

    //获取系统默认的
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;
    private Field fi;

    public static ExceptionCrashHandler getInstance() {
        if (mInstance == null) {
            //解决多并发发问题
            synchronized (ExceptionCrashHandler.class) {
                mInstance = new ExceptionCrashHandler();
            }
        }
        return mInstance;
    }

    private Context mContext;
    public void init(Context context){
        this.mContext=context;
        //设置全局的异常类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);

        mDefaultExceptionHandler= Thread.getDefaultUncaughtExceptionHandler();
    }
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //全局异常
        System.out.println("报异常了！！");

        //写入到本地文件或是发送到指定的服务器（崩溃信息、版本、包名、手机信息）
        //上传，不在该处
        //保存当前文件，等应用再次启动以后再上传
        String crashFilename=saveInfoToSD(e);
        //缓存奔溃日志文件
        cacheCrasFile(crashFilename);
        

        //让系统默认处理
        mDefaultExceptionHandler.uncaughtException(t,e);
   
    }

    /**
     * 缓存奔溃日志文件
     * @param crashFile
     */
    private void cacheCrasFile(String crashFile) {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences("crash",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("CRASH_FILE_NAME",crashFile).commit();
    }

    /**
     * 获取奔溃文件名称
     * @return
     */
    public File getCrashFile(){
        String crashFileName=mContext.getSharedPreferences("crash",Context.MODE_PRIVATE)
                .getString("CRASH_FILE_NAME","");
        return new File(crashFileName);
    }
    /**
     * 保存获取的 软件信息，设备信息和出错信息保存到SDCard中
     * @param e
     * @return
     */
    private String saveInfoToSD(Throwable e) {
        String filename=null;
        StringBuffer stringBuffer=new StringBuffer();
         //手机信息+应用信息--->obtainSimpleInfo
        for(Map.Entry<String,String> entry:obtainSimpleInfo(mContext)
                .entrySet()){
            String key=entry.getKey();
            String value=entry.getValue();
            stringBuffer.append(key).append("=").append(value).append("\n");
        }
        //奔溃详细信息
        stringBuffer.append(obtainExceptionInfo(e));

        //保存文件  手机应用的目录 并没有拿手机sdcard目录。6.0上需要动态申请权限
        if(Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)){
            File dir=new File(mContext.getFilesDir()+File.separator+"crash"
            +File.separator);

            //先删除之前的异常信息
            if(dir.exists()){
                //删除子文件
                deleteDir(dir);
            }
            if(!dir.exists()){
                dir.mkdir();
            }
            try {
                filename=dir.toString()
                        +File.separator
                        +getAssignTime("yyyy_MM_dd_HH_mm")
                        +".txt";
                System.out.println("filename=="+filename);
                FileOutputStream fileOutputStream=new FileOutputStream(filename);
                fileOutputStream.write(stringBuffer.toString().getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }
        return filename;
    }

    /**
     * 获取系统为捕捉的错误信息
     * @param throwable
     * @return
     */
    private String obtainExceptionInfo(Throwable throwable) {
        //Java基础 异常
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }
    private String getAssignTime(String dateFormatStr){
        DateFormat dateFormat=new SimpleDateFormat(dateFormatStr);
        long currentTime=System.currentTimeMillis();
        return dateFormat.format(currentTime);
    }
    /**
     * 获取简单信息，软件版本，手机版本、型号信息存放在HashMap中
     * @param context
     * @return
     */
    private HashMap<String,String> obtainSimpleInfo(Context context){
        HashMap<String,String> map=new HashMap<>();
        PackageManager mPackaeManager=context.getPackageManager();
        PackageInfo mPackageInfo=null;
        try {
            mPackageInfo=mPackaeManager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        map.put("versionName",mPackageInfo.versionName);
        map.put("versionCode",""+mPackageInfo.versionCode);
        map.put("MODEL",""+ Build.MODEL);
        map.put("SDK_INT",""+Build.VERSION.SDK_INT);
        map.put("PRODUCT",Build.PRODUCT);
        map.put("MOBLE_INFO",getMobileInfo());
        return map;

    }

    /**
     * Cell phone infomation
     * @return
     */
    private String getMobileInfo() {
        StringBuffer stringBuffer=new StringBuffer();
        try {
            //反射获取Build的所有属性
            Field[] files=Build.class.getDeclaredFields();
            for(Field field:files){
                field.setAccessible(true);
                String name= field.getName();
                String value= field.get(null).toString();
                stringBuffer.append(name+"="+value);
                stringBuffer.append("\n");

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir
     * @return
     */
    private boolean deleteDir(File dir){
        if(dir.isDirectory()){
            File[] children=dir.listFiles();
            //递归删除目录中子目录下
            for(File child:children){
                child.delete();
            }
        }
        //目录此时为空，可以删除
        return true;
    }
}
