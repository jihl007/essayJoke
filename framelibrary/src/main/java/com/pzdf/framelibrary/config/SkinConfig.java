package com.pzdf.framelibrary.config;

/**
 * author  jihl on 2019/3/21.
 * version 1.0
 * Description:
 */
public class SkinConfig {
    //SP 的文件名称
    public static final String SKIN_INFO_NAME="skinInfo";

    //保存皮肤文件的路径的名称
    public static final String SKIN_PATH_NAME="skinPath";

    //不需要改变任何东西
    public static final int SKIN_CHANGE_NOTHING = -1;

    //换肤成功
    public static final int SKIN_CHANGE_SUCCESS = 1;

    //换肤失败
    public static final int SKIN_CHANGE_FAILED= 1;

    //文件不存
    public static final int SKIN_FILE_NOEXSIST =-2 ;

    //皮肤文件有错误可能不是一个apk
    public static final int SKIN_FILE_ERROR = -3;
}
