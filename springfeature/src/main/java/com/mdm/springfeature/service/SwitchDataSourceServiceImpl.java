package com.mdm.springfeature.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdm.springfeature.annotation.SwitchSource;
import com.mdm.springfeature.entity.Department;
import com.mdm.springfeature.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: madongming
 * @DATE: 2022/6/7 18:54
 */
@Service
public class SwitchDataSourceServiceImpl implements SwitchDataSourceService {

    @Resource
    DepartmentMapper departmentMapper;

    @Override
    @SwitchSource
    public Object showAfterSwitchData() {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        Department department = departmentMapper.selectOne(queryWrapper.eq("id", 1));
        return department;
    }
}
