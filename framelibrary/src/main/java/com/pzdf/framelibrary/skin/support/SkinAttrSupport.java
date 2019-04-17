package com.pzdf.framelibrary.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.pzdf.framelibrary.skin.attr.SkinAttr;
import com.pzdf.framelibrary.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

/**
 * author  jihl on 2019/3/19.
 * version 1.0
 * Description:皮肤属性解析的支持类
 */
public class SkinAttrSupport {
    /**
     * 获取SkinAttrs的属性
     *
     * @param context
     * @param attrs
     * @return
     */
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        //background src  textColor
        List<SkinAttr> skinAttrs = new ArrayList<>();
        int attrLength = attrs.getAttributeCount();
        for (int index = 0; index < attrLength; index++) {
            //获取属性名称
            String attrName = attrs.getAttributeName(index);
            String attrValue = attrs.getAttributeValue(index);
            System.out.println("attrName===" + attrName);
            System.out.println("attrValue===" + attrValue);

            SkinType skinType=getSkinType(attrName);
            if(skinType!=null){
                //参数1：资源名称  参数二：类型
                //目前只要attrValue是一个@int 类型
                String resName=getResName(context,attrValue);

                if (TextUtils.isEmpty(resName)) {
                      continue;
                }
                SkinAttr skinAttr=new SkinAttr(resName,skinType);
                skinAttrs.add(skinAttr);
            }
        }

        return skinAttrs;
    }

    /**
     * 获取资源的名字
     * @param context
     * @param attrValue
     * @return
     */
    private static String getResName(Context context, String attrValue) {
        if (attrValue.startsWith("@")) {
            attrValue=attrValue.substring(1);

            int resId=Integer.parseInt(attrValue);

            return  context.getResources().getResourceEntryName(resId);
        }
        return null;
    }

    /**
     * 通过名称获取SkinType
     * @param attrName
     * @return
     */
    private static SkinType getSkinType(String attrName) {
        SkinType[] skinTypes=SkinType.values();
        for (SkinType skinType : skinTypes) {
            if(skinType.getResName().equals(attrName)){
                return skinType;
            }
        }
        return null;
    }
}
