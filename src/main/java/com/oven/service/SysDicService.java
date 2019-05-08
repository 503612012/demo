package com.oven.service;

import com.oven.dao.SysDicDao;
import com.oven.vo.SysDicVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统级字典Service层
 *
 * @author Oven
 */
@Service
public class SysDicService {

    @Resource
    private SysDicDao sysDicDao;


    /**
     * 查询所有
     */
    public List<SysDicVo> findAll() {
        return sysDicDao.findAll();
    }

}
