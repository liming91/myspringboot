package com.ming.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.entities.SysUser;
import com.ming.mapper.SysUserMapper;
import com.ming.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author liming
 * @Date 2023/3/31 11:22
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;

    @Override
    public IPage<SysUser> userPage(Integer pageNo, Integer pageSize) {
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        return sysUserMapper.userPage(page);
    }

    @Override
    public IPage<SysUser> userPage2(Integer pageNo, Integer pageSize) {
        List<SysUser> list = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>().isNotNull(SysUser::getUserName));
        List<SysUser> collect = list.stream().skip((pageNo - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        Page<SysUser> page = new Page(pageNo, pageSize);
        page.setRecords(collect);
        page.setSize(pageSize);
        page.setTotal(list.size());
        return page;
    }
}
