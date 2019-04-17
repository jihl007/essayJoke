package com.pzdf.essayjokebaselibrary.fixBug;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * author  jihl on 2019/2/22.
 * version 1.0
 * Description:
 */
public class FixDexManager {
    private Context mContext;
    private File mDexDir;

    public FixDexManager(Context context) {
        this.mContext = context;
        //获取应用可以访问的目录
        this.mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
    }

    /**
     * 修复包
     *
     * @param fixDexPath
     */
    public void fixDex(String fixDexPath) throws Exception {

        //2、获取下载好的补丁dexElement
        //2.1移动到系统能够访问到的目录下  dex目录下    ClassLoader
        File srcFile = new File(fixDexPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(fixDexPath);
        }
        File destFile = new File(mDexDir, srcFile.getName());
        if (destFile.exists()) {
            System.out.println("patch [ " + fixDexPath + "] has be loaded.");
            return;
        }

        copyFile(srcFile, destFile);

        //2.2ClassLoader读取fixDex路径    为什么要加入集合 ：已启动就有可能要修复BaseApplication
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);

        fixDexFiles(fixDexFiles);
    }

    /**
     * 把dexElement注入到classLoader中
     *
     * @param applicationClassLoader
     * @param applicationDexElements
     */
    private void injectDexElements(ClassLoader applicationClassLoader, Object applicationDexElements)
            throws Exception {
        //先获取PathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        //
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(applicationClassLoader);

        //2、pathLlist里面的dexElements
        Field dexElementField = pathList.getClass().getDeclaredField("dexElements");
        dexElementField.setAccessible(true);

        dexElementField.set(pathList, applicationDexElements);
    }

    /**
     * 合并两个数组
     *
     * @param arrayLhs 前边数组
     * @param arrayRhs 后边数组
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> loaderClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(loaderClass, j);
        for (int k = 0; k < j; k++) {
            if (k < j) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    /**
     * copy file
     *
     * @param src  source file
     * @param dest target file
     * @throws IOException
     */
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * 从classLoader中获取dexElement
     *
     * @param applicationClassLoader
     * @return
     */
    private Object getDexElementsByClassLoader(ClassLoader applicationClassLoader) throws Exception {
        //先获取PathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        //
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(applicationClassLoader);

        //2、pathLlist里面的dexElements
        Field dexElementField = pathList.getClass().getDeclaredField("dexElements");
        dexElementField.setAccessible(true);
        return dexElementField.get(pathList);
    }

    /**
     * 加载全部修复包
     */
    public void loadFixDex() throws Exception{
        File[] dexFiles = mDexDir.listFiles();
        List<File> fixDexFiles = new ArrayList<>();

        for (File dexFile : dexFiles) {
            if (dexFile.getName().endsWith(".dex")) {
                fixDexFiles.add(dexFile);
            }
        }
        fixDexFiles(fixDexFiles);
    }

    /**
     * 修复dex
     */
    private void fixDexFiles(List<File> fixDexFiles) throws Exception{

        //1、先获取已经运行的dexElement
        ClassLoader applicationClassLoader = mContext.getClassLoader();

        Object applicationDexElements = getDexElementsByClassLoader(applicationClassLoader);


        File optimizedDirectory = new File(mDexDir, "odex");
        if (!optimizedDirectory.exists()) {
            optimizedDirectory.mkdirs();
        }

        //修复
        for (File fixDexFile : fixDexFiles) {
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(),//dex路径  必须要在应用目录下的odex文件中
                    optimizedDirectory,//解压路径
                    null,//.so位置
                    applicationClassLoader//父ClassLoader
            );
            Object fixDexElements = getDexElementsByClassLoader(fixDexClassLoader);

            //3、把补丁的dexElement插到已经运行的dexElement的最前面
            // applicationClassLoader数组合并  fixDexElements数组


            //3.1合并完成
            combineArray(fixDexElements, applicationDexElements);
        }
        //3.2把合并的数组注入到原来的类中 applicationClassLoader
        injectDexElements(applicationClassLoader, applicationDexElements);

    }
}
