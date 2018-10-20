package com.skyer.service;

import com.skyer.cache.CacheService;
import com.skyer.contants.RedisCacheKey;
import com.skyer.mapper.MenuMapper;
import com.skyer.vo.Menu;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 菜单服务层
 *
 * @author SKYER
 */
@Service
public class MenuService {

    @Resource
    private MenuMapper menuMapper;
    @Resource
    private CacheService cacheService;

    /**
     * 通过id获取
     *
     * @param id 菜单ID
     */
    public Menu getById(Integer id) {
        Menu menu = cacheService.get(RedisCacheKey.GET_MENU_BY_ID + id); // 先读取缓存
        if (menu == null) { // double check
            synchronized (this) {
                menu = cacheService.get(RedisCacheKey.GET_MENU_BY_ID + id); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (menu == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    menu = menuMapper.getById(id);
                    cacheService.set(RedisCacheKey.GET_MENU_BY_ID + id, menu);
                }
            }
        }
        return menu;
    }

}
