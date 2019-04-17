package com.pzdf.essayjoke.util;

import com.bumptech.glide.load.engine.Resource;

/**
 * author  jihl on 2019/4/10.
 * version 1.0
 * Description:
 */
public class PatchUtils {
  /*
    1、写java的native方法
    2、生成一个头文件
    2.1、rebuild一次确保生产class文件
    2.2、cd进入项目E:\Resource\Android_Practice\essayJoke\app\build\intermediates\classes\debug
    2.3、生成头文件命令：javah com.pzdf.essayjoke.util.PatchUtils
    2.4、copy头文件到jni目录下面
    3.写jni的实现 下载第三方c库
    3.1、在build.gradle中添加
     sourceSets {
        main {
            jni.srcDirs = []//设置禁止gradle生成Android.mk
            jniLibs.srcDirs = ['libs']
        }
    }
    task ndkBuild(type: Exec) {//设置新的so的生成目录
        commandLine "D:\\AndroidStudio_SDK\\sdk\\sdk\\ndk-bundle\\ndk-build.cmd",
                'NDK_PROJECT_PATH=build/intermediates/ndk',
                'NDK_LIBS_OUT=libs',
                'APP_BUILD_SCRIPT=jni/Android.mk',
                'NDK_APPLICATION_MK=jni/Application.mk'
    }
    tasks.withType(JavaCompile) {
        compileTask -> compileTask.dependsOn ndkBuild
    }
    3.3、在总gradle.properties中新添
    android.useDeprecatedNdk=true
    */
    /**
     *
     * @param oldApkPath  原来的apk    1.0本地
     * @param newApkPath  合并后新的apk路径   需要生成
     * @param patch        查分包路径    从服务器上下载
     */
    public static native void combine(String oldApkPath,String newApkPath,String patch);
}
