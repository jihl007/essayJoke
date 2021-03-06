package com.pzdf.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * Description:
 */
public class DaoSupportFactory {

    private static DaoSupportFactory mFactory;


    // 持有外部数据库的引用
    private SQLiteDatabase mSqLiteDatabase;

    private DaoSupportFactory() {

        // 把数据库放到内存卡里面  判断是否有存储卡 6.0要动态申请权限
        File dbRoot = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "nhdz" + File.separator + "database");
        if (!dbRoot.exists()) {
            dbRoot.mkdirs();
        }
        System.out.println("dbRoot---->"+dbRoot);
        File dbFile = new File(dbRoot, "nhdz.db");

        // 打开或者创建一个数据库
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public static DaoSupportFactory getFactory() {
        if (mFactory == null) {
            synchronized (DaoSupportFactory.class) {
                if (mFactory == null) {
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> IDaoSupport<T> getDao(Class<T> clazz) {
        IDaoSupport<T> daoSoupport = new DaoSupport();
        daoSoupport.init(mSqLiteDatabase,clazz);
        return daoSoupport;
    }
}
