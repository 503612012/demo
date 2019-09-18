package com.oven.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 获取对象更改的字段
 *
 * @author Oven
 */
public class BeanChangeUtils {

    public static String contrastObj(Object oldObj, Object newObj) {
        StringBuilder str = new StringBuilder();
        try {
            // 通过反射获取类的类类型及字段属性
            Class clazz = oldObj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // 排除序列化属性
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                // 获取对应属性值
                Method getMethod = pd.getReadMethod();
                Object o1 = getMethod.invoke(oldObj);
                Object o2 = getMethod.invoke(newObj);
                if (o1 == null || o2 == null) {
                    continue;
                }
                if (!o1.toString().equals(o2.toString())) {
                    str.append("字段名称:").append(field.getName()).append(",旧值:").append(o1).append(",新值:").append(o2).append(";");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

}
