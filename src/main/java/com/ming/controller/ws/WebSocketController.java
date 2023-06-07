package com.ming.controller.ws;

import com.alibaba.fastjson.JSON;
import com.ming.entities.YbWardCallRecordVo;
import com.ming.util.http.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@Slf4j
public class WebSocketController {

    @Autowired
    private MyWebSocket myWebSocket;

    @GetMapping("index")
    public ResponseEntity<String> index(){
        return ResponseEntity.ok("请求成功");
    }

    @GetMapping("page")
    public ModelAndView page(){
        return new ModelAndView("websocket");
    }

    @RequestMapping("/push/{toUserId}")
    public ResponseEntity<String> pushToWeb(String message, @PathVariable String toUserId) throws IOException {
        //MyWebSocket.sendInfo(message,toUserId);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }

    @ApiOperation(value = "空调消息推送")
    @PostMapping  (value = "/push")
    public Result<?> push(@RequestBody YbWardCallRecordVo wardCallRecordVo) throws IOException {
        wardCallRecordVo.setCode(wardCallRecordVo.getCode());
        wardCallRecordVo.setAirPuaVo(wardCallRecordVo.getAirPuaVo());
        System.out.println("信息推送："+JSON.toJSONString(wardCallRecordVo));
        log.info("信息推送："+JSON.toJSONString(wardCallRecordVo));
        myWebSocket.onMessage(JSON.toJSONString(wardCallRecordVo), null);
        System.out.println("信息推送====================================="+ JSON.toJSONString(wardCallRecordVo));
        return Result.success();
    }



}
