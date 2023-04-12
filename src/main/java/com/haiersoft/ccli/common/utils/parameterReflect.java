package com.haiersoft.ccli.common.utils;

import java.lang.reflect.Modifier;
import java.util.Date;
import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

public class parameterReflect {

    public static <T> T reflectParameter(HttpServletRequest request, Class<T> target) {

        try {

            T obj = target.newInstance();

            reflectParameter(obj, request);

            return obj;

        } catch (InstantiationException | IllegalAccessException e) {

            e.printStackTrace();

            return null;
        }

    }


    /**
     * 将HttpServletRequest中的参数反射至实体类
     * <p>
     * * @param <T>
     * * @param clazz
     * * @param request
     * * @return
     */

    public static <T> void reflectParameter(T obj, HttpServletRequest request) {
        try {
            //获取对象的Class
            Class<?> clazz = obj.getClass();
            //获取Class中所有已声明的属性集合
            Field[] fileds = clazz.getDeclaredFields();
            //遍历属性结合
            for (Field f : fileds) {
                //过滤被final、static修饰的成员变量
                if ((f.getModifiers() & Modifier.FINAL) > 0 || (f.getModifiers() & Modifier.STATIC) > 0) {
                    continue;
                }
                //取消所有私有变量的限制
                f.setAccessible(true);
                //取消Field的访问检查
                //获取属性的类型,long/int/string....
                Class<?> fieldType = f.getType();
                //获取属性的名字
                String fieldName = f.getName();
                //根据属性的名字从request中获取value
                String paramVal = request.getParameter(fieldName);
                //判断类型,如果是String
                if (String.class == fieldType) {
                    f.set(obj, paramVal);
                } else if (long.class == fieldType || Long.class == fieldType) {//判断类型,如果是long,则使用NumberUtils.toLong,即使为空,也赋默认值0L
                    f.set(obj, NumberUtils.toLong(paramVal));
                } else if (int.class == fieldType || Integer.class == fieldType) {//判断类型,如果是int,则使用NumberUtils.toInt,即使为空,也赋默认值0
                    f.set(obj, NumberUtils.toInt(paramVal));
                } else if (double.class == fieldType || Double.class == fieldType) {//判断类型,如果是double,则使用NumberUtils.toDouble,即使为空,也赋默认值0
                    f.set(obj, NumberUtils.toDouble(paramVal));
                } else if (Date.class == fieldType) {//判断类型,如果是date,则使用DateUtil.parseDateNewFormat格式化日期格式
                    f.set(obj, DateUtils.parseDate(paramVal));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
