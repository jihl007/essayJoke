package com.pzdf.essayjoke.db;

import org.litepal.crud.DataSupport;

/**
 * author  jihl on 2019/3/15.
 * version 1.0
 * Description:
 */
public class Person  extends DataSupport{
    private String mName;
    private int mAge;

    // 默认的构造方法
    public Person() {

    }

    public Person(String name, int age) {
        this.mName = name;
        this.mAge = age;
    }
}
