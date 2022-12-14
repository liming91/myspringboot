package com.ming.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.entities.SysMenu;
import com.ming.service.SysMenuService;
import com.ming.mapper.SysMenuMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@AllArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {


    @Override
    public List<SysMenu> getMenuTree() {
        List<SysMenu> menuList = this.baseMapper.selectList(new LambdaQueryWrapper<SysMenu>());
        List<SysMenu> resultList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(menuList)) {
            Map<Long, SysMenu> map = menuList.stream().distinct().collect(Collectors.toMap(SysMenu::getMenuId, m -> m, (m1, m2) -> m1));
            menuList.forEach(m -> {
                if (m.getParentId() == 0) {
                    resultList.add(m);
                } else {
                    SysMenu sysMenu = map.get(m.getParentId());
                    if (ObjectUtil.isNotEmpty(sysMenu)) {
                        sysMenu.getChildren().add(m);
                    }
                }
            });
        }
        return resultList;
    }
}




