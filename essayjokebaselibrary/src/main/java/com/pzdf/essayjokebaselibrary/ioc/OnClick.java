package com.pzdf.essayjokebaselibrary.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author  jihl on 2019/2/15.
 * version 1.0
 *  Description:View事件注入的Annotation
 */
@Target(ElementType.METHOD)
//@Target(ElementType.FIELD) 代表Annotation位置    FIELD属性  TYPE属性  CONSTRUCTOR 构造函数上

@Retention(RetentionPolicy.RUNTIME)
//@Retention(RetentionPolicy.SOURCE)  代表什么时候生效  CLASS 编译时  RUNTIME 运行时  SOURCE源码资源
public @interface OnClick {
    int[] value();
}
