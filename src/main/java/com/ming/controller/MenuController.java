package com.ming.controller;


import com.ming.annotation.RateLimiter;
import com.ming.annotation.TestAnnotation;
import com.ming.entities.SysMenu;
import com.ming.entities.SysUser;
import com.ming.enums.LimitType;
import com.ming.service.SysMenuService;
import com.ming.util.MinIoUtil;
import com.ming.util.QRCodeUtil;
import com.ming.util.http.Result;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final SysMenuService sysMenuService;

    //@TestAnnotation(module = "菜单管理", desc = "测试菜单")
    @RateLimiter(key = "menu", limitType = LimitType.DEFAULT)
    @RequestMapping("/getMenuTree")
    public Result<?> getMenuTree(@RequestBody SysUser user) {
        List<SysMenu> list = sysMenuService.getMenuTree();
        return Result.success(list);
    }

    @ApiOperation("二维码生成")
    @GetMapping("/logoQRCode")
    public Result<?> logoQRCode(String content, String menuId) throws IOException {
        String name = sysMenuService.findMenuName(menuId);
        String logoPath = QRCodeUtil.getLogoQRCode(content, name);
        String staticPath = this.getClass().getClassLoader().getResource("images").getFile();
        log.info("images下的staticPath:{}", staticPath);
        log.info("二维码路径=========:" + logoPath);
        assert logoPath != null;
        String qrcodePath = MinIoUtil.upload("qrcode", new File(logoPath));
        return Result.success(qrcodePath);
    }

}
