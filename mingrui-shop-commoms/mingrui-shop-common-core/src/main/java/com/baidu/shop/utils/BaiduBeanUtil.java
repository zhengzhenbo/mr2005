package com.baidu.shop.utils;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.BeanUtils;

/**
 * @ClassName BaiduBeanUtil
 * @Description: TODO
 * @Author shenyaqi
 * @Date 2020/12/25
 * @Version V1.0
 **/
public class BaiduBeanUtil<T> {

    public static <T> T copyProperties(Object source,Class<T> clazz){

        try {
            T t = clazz.newInstance();//创建当前类型的实例
            BeanUtils.copyProperties(source,t);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}