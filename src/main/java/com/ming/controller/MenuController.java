package com.ming.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.ming.annotation.Log;
import com.ming.annotation.TestAnnotation;
import com.ming.entities.SysMenu;
import com.ming.enums.BusinessType;
import com.ming.service.SysMenuService;
import com.ming.util.http.ResponseResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final SysMenuService sysMenuService;

    @TestAnnotation(module = "菜单管理", desc = "测试菜单")
    @GetMapping("/getMenuTree")
    public ResponseResult<?> getMenuTree(String name) {
        List<SysMenu> list = sysMenuService.getMenuTree();
        return ResponseResult.success(list);
    }
}
