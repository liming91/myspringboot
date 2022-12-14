package com.ming.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.ming.entities.SysMenu;
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

    @GetMapping("/getMenuTree")
    public ResponseResult<?> getMenuTree() {
        List<SysMenu> list = sysMenuService.getMenuTree();

        return ResponseResult.success(list);
    }
}
