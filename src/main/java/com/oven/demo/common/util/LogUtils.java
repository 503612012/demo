package com.oven.demo.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * 获取对象属性更改日志
 *
 * @author Oven
 */
@Slf4j
public class LogUtils {

    /**
     * 获取对象属性更改日志
     *
     * @param oldObj 修改前对象
     * @param newObj 修改后对象
     */
    private static String getChangeInfo(Object oldObj, Object newObj) {
        try {
            StringBuilder result = new StringBuilder();
            Field[] fields = oldObj.getClass().getDeclaredFields();
            boolean isChange = false;
            for (Field item : fields) {
                item.setAccessible(true);
                String oldValue = item.get(oldObj).toString();
                String newValue = item.get(newObj).toString();
                if (oldValue != null && !oldValue.equals(newValue)) {
                    result.append(item.getName()).append("由[").append(oldValue).append("]改为[").append(newValue).append("]").append(", ");
                    isChange = true;
                }
            }
            if (isChange) {
                String str = result.toString();
                return str.substring(0, str.length() - 2);
            }
            return result.toString();
        } catch (Exception e) {
            log.error("获取对象属性更改日志异常：", e);
            return "";
        }
    }

}
