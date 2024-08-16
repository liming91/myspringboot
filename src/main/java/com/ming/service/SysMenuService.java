package com.ming.service;

import com.ming.entities.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> getMenuTree();

    String findMenuName(String menuId);
}
