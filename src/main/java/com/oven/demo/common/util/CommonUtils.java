package com.oven.demo.common.util;

import com.oven.basic.common.util.IPUtils;
import com.oven.basic.common.util.SpringContextUtil;
import com.oven.demo.common.constant.AppConst;
import com.oven.demo.core.user.entity.User;
import com.oven.demo.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 常用工具类
 *
 * @author Oven
 */
@Slf4j
public class CommonUtils {

    /**
     * 获取当前登录人信息
     */
    public static User getCurrentUser() {
        Object user = SecurityUtils.getSubject().getSession().getAttribute(AppConst.CURRENT_USER);
        return (User) user;
    }

    /**
     * 获取当前登录人IP地址
     */
    public static String getCurrentUserIp() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        return IPUtils.getClientIPAddr(servletRequestAttributes.getRequest());
    }

    public static <T> void setCreateName(List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        List<Integer> ids = list.stream().map(item -> {
                    try {
                        Field createIdField = item.getClass().getDeclaredField("createId");
                        createIdField.setAccessible(true);
                        Object value = createIdField.get(item);
                        if (value == null) {
                            return null;
                        }
                        return Integer.parseInt(String.valueOf(value));
                    } catch (Exception e) {
                        log.error("反射获取createId属性异常：", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return;
        }
        UserService userService = SpringContextUtil.getBean(UserService.class);
        List<User> userList = userService.getByIds(ids);
        if (userList == null || userList.isEmpty()) {
            return;
        }
        Map<Integer, String> map = userList.stream().collect(Collectors.toMap(User::getId, User::getNickName));
        for (T item : list) {
            try {
                Field createIdField = item.getClass().getDeclaredField("createId");
                createIdField.setAccessible(true);
                Object value = createIdField.get(item);
                if (value == null) {
                    continue;
                }
                Integer createId = Integer.parseInt(String.valueOf(value));
                String createName = map.get(createId);
                if (StringUtils.isEmpty(createName)) {
                    continue;
                }
                Field createNameField = item.getClass().getDeclaredField("createName");
                createNameField.setAccessible(true);
                createNameField.set(item, createName);
            } catch (Exception e) {
                log.error("反射异常：", e);
            }
        }
    }

    public static <T> void setLastModifyName(List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        List<Integer> ids = list.stream().map(item -> {
                    try {
                        Field lastModyfiIdField = item.getClass().getDeclaredField("lastModifyId");
                        lastModyfiIdField.setAccessible(true);
                        Object value = lastModyfiIdField.get(item);
                        if (value == null) {
                            return null;
                        }
                        return Integer.parseInt(String.valueOf(value));
                    } catch (Exception e) {
                        log.error("反射获取lastModifyId属性异常：", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return;
        }
        UserService userService = SpringContextUtil.getBean(UserService.class);
        List<User> userList = userService.getByIds(ids);
        if (userList == null || userList.isEmpty()) {
            return;
        }
        Map<Integer, String> map = userList.stream().collect(Collectors.toMap(User::getId, User::getNickName));
        for (T item : list) {
            try {
                Field lastModifyIdField = item.getClass().getDeclaredField("lastModifyId");
                lastModifyIdField.setAccessible(true);
                Object value = lastModifyIdField.get(item);
                if (value == null) {
                    continue;
                }
                Integer lastModifyId = Integer.parseInt(String.valueOf(value));
                String lastModifyName = map.get(lastModifyId);
                if (StringUtils.isEmpty(lastModifyName)) {
                    continue;
                }
                Field lastModifyNameField = item.getClass().getDeclaredField("lastModifyName");
                lastModifyNameField.setAccessible(true);
                lastModifyNameField.set(item, lastModifyName);
            } catch (Exception e) {
                log.error("反射异常：", e);
            }
        }
    }

}
